/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.service;

import it.filippetti.ks.api.bpm.configuration.RoutingConfiguration;
import it.filippetti.ks.api.bpm.dto.EventDTO;
import it.filippetti.ks.api.bpm.dto.PageDTO;
import it.filippetti.ks.api.bpm.dto.PublishEventDTO;
import it.filippetti.ks.api.bpm.dto.PublishOutcomeDTO;
import it.filippetti.ks.api.bpm.exception.ApplicationException;
import it.filippetti.ks.api.bpm.exception.AuthorizationException;
import it.filippetti.ks.api.bpm.exception.BusinessError;
import it.filippetti.ks.api.bpm.exception.BusinessException;
import it.filippetti.ks.api.bpm.exception.NotFoundException;
import it.filippetti.ks.api.bpm.exception.ValidationException;
import it.filippetti.ks.api.bpm.mapper.MappingContext;
import it.filippetti.ks.api.bpm.mapper.dto.EventMapper;
import it.filippetti.ks.api.bpm.model.AssetType;
import it.filippetti.ks.api.bpm.model.AuthenticationContext;
import it.filippetti.ks.api.bpm.model.Deployment;
import it.filippetti.ks.api.bpm.model.Event;
import it.filippetti.ks.api.bpm.model.EventScope;
import it.filippetti.ks.api.bpm.model.EventScopeType;
import it.filippetti.ks.api.bpm.model.EventType;
import it.filippetti.ks.api.bpm.model.Fetcher;
import it.filippetti.ks.api.bpm.model.Pager;
import static it.filippetti.ks.api.bpm.model.Settings.KEY_EVENTS;
import it.filippetti.ks.api.bpm.repository.EventRepository;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 *
 * @author marco.mazzocchetti
 */
@Service
public class EventService {

    @Autowired
    private RoutingConfiguration routingConfiguration;
            
    @Autowired
    private ValidationService validationService;

    @Autowired
    private RoutingService routingService;
    
    @Autowired
    private EventRepository eventRepository;
    
    @Autowired
    private EventMapper eventMapper;

    public EventService() {
    }
    
    /**
     * 
     * @param context
     * @param pageNumber
     * @param pageSize
     * @param orderBy
     * @param fetch
     * @return
     * @throws ApplicationException 
     */
    public PageDTO<EventDTO> getEvents(
        AuthenticationContext context, Integer pageNumber, Integer pageSize, String orderBy, String fetch) 
        throws ApplicationException {
        
        return eventMapper.map(
            eventRepository.findAll(
                context,
                Pager.of(Event.class, pageNumber, pageSize, orderBy)),
            MappingContext.of(
                context, 
                Fetcher.of(Event.class, fetch)));
    }
    
    /**
     * 
     * @param context
     * @param eventId
     * @param dto
     * @return
     * @throws ApplicationException 
     */
    @Transactional(rollbackFor = Exception.class)
    public PublishOutcomeDTO publishEvent(
        AuthenticationContext context, String eventId, 
        PublishEventDTO dto) 
        throws ApplicationException {
        
        Event event;
        EventScopeType scopeType;
        EventScope scope;
        Object outcome;
        
        validationService.validate(dto);
        
        // get event
        event = eventRepository.findByEventId(context, eventId);
        if (event == null || !event.getType().isPublishable()) {
            throw new NotFoundException();
        }
        // check authorization
        if (!context.isAdmin()) {
            throw new AuthorizationException();
        }
        // check signal supported
        if (!event.getType().isPublishable()) {
            throw new BusinessException(
                BusinessError.EVENT_PUBLISHING_NOT_SUPPORTED, 
                "Publishing not supported for this type of event");
        }        
        // get scope
        scopeType = EventScopeType.valueOf(dto.getScope().getType());
        switch (scopeType) {
            case organization : 
                scope = EventScope.organization();
                break;
            case instance:
                scope = EventScope.instance(dto.getScope().getId());
            default:
                throw new IncompatibleClassChangeError();
        }
        // publish event
        switch (event.getType()) {
            case message : 
                outcome = routingService.routeMessage(
                    context.getTenant(), 
                    context.getOrganization(), 
                    eventId, 
                    dto.getPayload(), 
                    scope, 
                    dto.getAsync(), 
                    null, 
                    dto.getOutcomeVariable());
                break;
            case signal:
                outcome = null;
                routingService.routeSignal(
                    context.getTenant(), 
                    context.getOrganization(), 
                    eventId, 
                    dto.getPayload(), 
                    scope, dto.getAsync());
                break;
            default:
                throw new IncompatibleClassChangeError();
        }
        
        // return outcome
        return new PublishOutcomeDTO(outcome);
    }
    
    /**
     * 
     * @param deployment
     * @throws IOException
     * @throws ApplicationException 
     */
    protected void updateEvents(Deployment deployment) 
        throws IOException, ApplicationException {
        
        Set<Event> events;
        Map<String, Object> settingsEvents;
        List<Event> declaredEvents;
        Map<String, Event> deployedEvents;
        Event deployedEvent;
        
        if (!TransactionSynchronizationManager.isActualTransactionActive()) {
            throw new IllegalStateException();
        }
        
        // get settings events
        settingsEvents = deployment.getSettings().getMap(KEY_EVENTS);
        if (settingsEvents == null) {
            settingsEvents = Collections.EMPTY_MAP;
        }
        // build and validate declared events
        declaredEvents = new ArrayList<>();
        for (Map.Entry<String, Object> settingsEvent : settingsEvents.entrySet()) {
            declaredEvents.add(createEvent(settingsEvent.getKey(), (Map) settingsEvent.getValue()));
        }
        // get already deployed events
        deployedEvents = eventRepository
            .findByEventId(
                deployment.getTenant(),
                deployment.getOrganization(),
                declaredEvents
                    .stream()
                    .map(e -> e.getEventId())
                    .collect(Collectors.toSet()))
            .stream()
            .collect(Collectors.toMap(
                e -> e.getEventId(), 
                e -> e));
        // merge events
        events = new HashSet<>();        
        for (Event declaredEvent : declaredEvents) {
            deployedEvent = deployedEvents.get(declaredEvent.getEventId());
            // event not deployed: add new
            if (deployedEvent == null) {
                events.add(declaredEvent);
            }
            // event deployed but declared only by this deployment: update and add old
            else if (deployedEvent.isDeclaredOnlyBy(deployment)) {
                events.add(deployedEvent.update(declaredEvent));
            }
            // event deployed with same attributes: merge start and add old
            else if (deployedEvent.semanticEquals(declaredEvent)) {
                events.add(deployedEvent.mergeStart(declaredEvent));
            // event deployed with different attributes: error    
            } else {
                throw new BusinessException(
                    BusinessError.ASSET_NOT_VALID, 
                    String.format(
                        "%s: event '%s' already defined by other deployments with incompatible attributes", 
                        AssetType.settings.entryName(),
                        declaredEvent.getEventId()));
            }
        }
        deployment.setEvents(events);
    } 
    
    /**
     * 
     * @param eventId
     * @param attributes
     * @return
     * @throws ValidationException 
     */
    private Event createEvent(String eventId, Map<String, Object> attributes) throws ApplicationException {

        EventType type;
        Boolean start;
        String correlationSubscription, retrievalExpression, queue, summary, schema;
        Map<String, Object> properties;
        
        // event id
        if (StringUtils.isBlank(eventId)) {
            throw new IllegalArgumentException();
        }
        // event type
        type = EventType.valueOf((String) attributes.get("type"));        
        // attributes
        try {
            // message correlation attributes
            start = false;
            correlationSubscription = null;
            retrievalExpression = null;
            queue = null;
            if (type.equals(EventType.message)) {
                // start
                start = (Boolean) attributes.get("start");
                if (start == null) {
                    start = false;
                }
                // correlationSubscription
                correlationSubscription = (String) attributes.get("correlationSubscription");
                if (StringUtils.isBlank(correlationSubscription)) {
                    correlationSubscription = null;
                }
                // retrievalExpression
                retrievalExpression = (String) attributes.get("retrievalExpression");
                if (StringUtils.isBlank(retrievalExpression)) {
                    retrievalExpression = null;
                }
                if (!start && correlationSubscription == null && retrievalExpression == null) {
                   throw new IllegalArgumentException("missing correlationSubscription/retrievalExpression");
                }
                // queue
                queue = (String) attributes.get("queue");
                if (StringUtils.isBlank(queue)) {
                    queue = null;
                }
                if (queue != null && routingConfiguration.queue(queue) == null) {
                   throw new IllegalArgumentException("queue not defined");
                }
            }
            // summary        
            summary = (String) attributes.get("summary");
            if (StringUtils.isBlank(summary)) {
                summary = null;
            }
            // schema
            schema = (String) attributes.get("schema");
            if (StringUtils.isBlank(schema)) {
                schema = null;
            }
            // properties
            properties = (Map) attributes.get("properties");
        } catch (IllegalArgumentException | ClassCastException e) {
            throw new BusinessException(
                BusinessError.ASSET_NOT_VALID,
                String.format(
                    "%s: invalid event '%s' definition, %s", 
                    AssetType.settings.entryName(),
                    eventId,
                    e.getMessage() != null ? 
                        e.getMessage() : 
                        "syntax error"));
        }
        
        return new Event(
            eventId,
            type,
            start,
            correlationSubscription,
            retrievalExpression,
            queue,
            summary,
            schema,
            properties);
    }    
}
