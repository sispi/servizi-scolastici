/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author marco.mazzocchetti
 */
@Entity
@Table(
    name = "KS_EVENT",    
    indexes = {
        @Index(columnList="eventId")}
)
@Sortables(
    defaultSort = "id:ASC",
    value = {
        @Sortable(property = "id", pathExpression = "eventId")
    }
)
@Fetchables({
    @Fetchable(property = "properties")    
})
public class Event extends Identifiable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Basic
    @Column(nullable = false)    
    private String eventId;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_", nullable = false)    
    private EventType type;

    @Basic
    @Column(name = "start_")    
    private Boolean start;
    
    @Basic    
    @Column(length = 1024)
    private String correlationSubscription;

    @Basic        
    @Column(length = 1024)
    private String retrievalExpression;
    
    @Basic        
    private String queue;    
    
    @Basic
    private String summary;
    
    @Basic
    @Column(name = "schema_")
    private String schema;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(
            name = "value", 
            column = @Column(name = "properties", length = 4000))
    })        
    private JsonString properties;

    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY)
    private Set<DeploymentEvent> deployments;
            
    public Event() {
    }

    public Event(
        String eventId,
        EventType type,
        Boolean start, 
        String correlationSubscription,
        String retrievalExpression, 
        String queue,
        String summary,
        String schema,
        Map<String, Object> properties) {
        this.eventId = eventId;
        this.type = type;
        this.start = start;
        this.correlationSubscription = correlationSubscription;
        this.retrievalExpression = retrievalExpression;
        this.queue = queue;
        this.summary = summary;
        this.schema = schema;
        this.properties = new JsonString(properties);
        this.deployments = new HashSet<>();
    }

    @Override
    public Long getId() {
        return id;
    }

    public String getEventId() {
        return eventId;
    }

    public EventType getType() {
        return type;
    }

    public boolean isStart() {
        return start;
    }

    public String getCorrelationSubscription() {
        return correlationSubscription;
    }

    public String getRetrievalExpression() {
        return retrievalExpression;
    }

    public String getQueue() {
        return queue;
    }

    public String getSummary() {
        return summary;
    }

    public String getSchema() {
        return schema;
    }

    public JsonValue getProperties() {
        return properties;
    }

    public Set<Deployment> getDeployments() {
        return Collections.unmodifiableSet(deployments
            .stream()
            .map(e -> e.getDeployment())
            .collect(Collectors.toSet()));
    }

    public boolean isDeclaredOnlyBy(Deployment deployment) {
        return !deployments.stream().anyMatch(e -> !e.getDeployment().equals(deployment));
    }
    
    public boolean semanticEquals(Event event) {
        if (this == event) {
            return true;
        }
        if (event == null) {
            return false;
        }
        if (this.type != event.type) {
            return false;
        } 
        if (!Objects.equals(this.eventId, event.eventId)) {
            return false;
        }
        if (!Objects.equals(this.correlationSubscription, event.correlationSubscription)) {
            return false;
        }
        if (!Objects.equals(this.retrievalExpression, event.retrievalExpression)) {
            return false;
        }
        return Objects.equals(this.queue, event.queue);
    }

    public Event update(Event event) throws IllegalArgumentException {
        
        if (!this.eventId.equals(event.getEventId())) {
            throw new IllegalArgumentException();
        }
        
        this.type = event.type;
        this.start = event.start;
        this.correlationSubscription = event.correlationSubscription;
        this.retrievalExpression = event.retrievalExpression;
        this.queue = event.queue;
        this.summary = event.summary;
        this.schema = event.schema;
        this.properties = event.properties;
        return this;
    }
    
    /**
     * 
     * @param event
     * @return
     * @throws IllegalArgumentException 
     */
    public Event mergeStart(Event event) throws IllegalArgumentException {
        
        if (!this.eventId.equals(event.getEventId())) {
            throw new IllegalArgumentException();
        }
        
        if (!this.start) {
            this.start = event.start;
        
        }
        return this;
    }    
}
