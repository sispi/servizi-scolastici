/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.command;

import it.filippetti.ks.api.bpm.exception.CorrelationException;
import it.filippetti.ks.api.bpm.model.Correlation;
import it.filippetti.ks.api.bpm.model.CorrelationError;
import it.filippetti.ks.api.bpm.model.CorrelationKey;
import it.filippetti.ks.api.bpm.model.CorrelationType;
import it.filippetti.ks.api.bpm.model.EventScope;
import it.filippetti.ks.api.bpm.model.InstanceReference;
import it.filippetti.ks.api.bpm.repository.InstanceCorrelationRepository;
import it.filippetti.ks.api.bpm.service.InstanceService;
import it.filippetti.ks.api.bpm.service.RoutingService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.kie.api.executor.ExecutionResults;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author marco.mazzocchetti
 */
public class RoutingCommand extends Command {

    @Autowired
    private InstanceService instanceService;
    
    @Autowired
    private RoutingService routingService;
    
    @Autowired
    private InstanceCorrelationRepository instanceCorrelationRepository;
    
    /**
     *
     * @param context
     * @return
     */
    @Override
    public ExecutionResults execute(CommandContext context) {
        
        String tenant, organization, messageId, uuid;
        Long senderInstanceId;
        Map<String, Object> messagePayload;
        EventScope scope;
        Correlation correlation;
        Map<String, Object> workItemResults;
        ExecutionResults executionResults;
        List<InstanceReference> correlatedInstances;
        InstanceReference correlatedInstance;
        CorrelationKey correlationKey;
        
        // get command parameters
        tenant = (String) context.getData("tenant");
        organization = (String) context.getData("organization");
        messageId = (String) context.getData("messageId");
        messagePayload = (Map) context.getData("messagePayload");
        scope = (EventScope) context.getData("scope");
        senderInstanceId = (Long) context.getData("processInstanceId");
        correlation = (Correlation) context.getData("correlation");
        
        // init results
        executionResults = new ExecutionResults();
        
        // message is correlated
        if (correlation != null) {
            // correlation has workitem (receive task)
            if (correlation.getWorkItemId() != null) {

                // set workitem results
                workItemResults = new HashMap<>();
                workItemResults.put("messageId", messageId);
                workItemResults.put("messagePayload", messagePayload);
                workItemResults.put("scope", scope);

                // complete correlated workitem
                instanceService.completeWorkItem(
                    correlation.getUnitId(),
                    correlation.getInstanceId(),
                    correlation.getWorkItemId(), 
                    workItemResults);
            }
            // correlation doesn't have workitem (catch message)
            else {
                // signal correlated instance
                routingService.routeSignal(tenant,
                    organization,
                    String.format("Message-%s", messageId), 
                    messagePayload, 
                    EventScope.instance(correlation.getInstanceId()), 
                    false);
            }
        }
        // message is not correlated, try to start instance and check that 
        // exactly one instance was started, i.e. has registered the 
        // correlation carried through the message
        else {
            // enrich payload with a random generated start uuid
            messagePayload.put("$start", UUID.randomUUID().toString());

            // signal scope
            routingService.routeSignal(
                tenant,
                organization,
                String.format("Message-%s", messageId), 
                messagePayload, 
                scope, 
                false);

            // get correlated instances
            correlatedInstances = instanceCorrelationRepository.findCorrelatedInstances(
                tenant, 
                organization, 
                messageId, 
                CorrelationType.conversation, 
                new CorrelationKey(messagePayload.get("$start")), 
                scope);

            // exclude sender if specified, we doesn't 
            // have this info if called through rest api
            if (senderInstanceId != null) {
                correlatedInstances.removeIf(i -> 
                    i.getId().equals(senderInstanceId)
                );
            }

            // check number of instances found 
            // and set exception in case of error
            if (correlatedInstances.isEmpty()) {
                executionResults.setData("correlationException", new CorrelationException(
                    CorrelationError.correlation_not_found, 
                    String.format(
                        "No correlation " + 
                        "for messageId '%s', scope %s",
                        messageId, scope)));

            } 
            else if (correlatedInstances.size() > 1) {
                executionResults.setData("correlationException", new CorrelationException(
                    CorrelationError.correlation_non_unique, 
                    String.format(
                        "Non unique correlation " + 
                        "for messageId '%s', scope %s",
                        messageId, scope)));
            }
            else {
                // instance found
                correlatedInstance = correlatedInstances.get(0);

                // set correlation
                executionResults.setData("correlation", new Correlation(
                    correlatedInstance.getId(), 
                    correlatedInstance.getUnitId()));
            }
        }
        return executionResults;
    }
}
