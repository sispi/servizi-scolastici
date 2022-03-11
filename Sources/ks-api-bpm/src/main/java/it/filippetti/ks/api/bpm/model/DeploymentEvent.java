/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.model;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 *
 * @author marco.mazzocchetti
 */
@Entity
@Table(
    name = "KS_DEPLOYMENT_EVENT",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"deployment_id", "event_id"})
    }
)
public class DeploymentEvent extends Identifiable {    
    
    @EmbeddedId
    private Id id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("deploymentId")
    private Deployment deployment;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST}, optional = false)
    @MapsId("eventId")
    private Event event;

    public DeploymentEvent() {
    }
    
    public DeploymentEvent(Deployment deployment, Event event) {
        this.id = new Id(deployment.getId(), event.getId());
        this.deployment = deployment;
        this.event = event;
    }

    @Override
    public Id getId() {
        return id;
    }

    public Deployment getDeployment() {
        return deployment;
    }

    public Event getEvent() {
        return event;
    }
    
    @Embeddable
    public static final class Id implements Serializable {
        
        private Long deploymentId;
        private Long eventId;

        public Id() {
        }

        public Id(Long deploymentId, Long eventId) {
            this.deploymentId = deploymentId;
            this.eventId = eventId;
        }

        public Long getDeploymentId() {
            return deploymentId;
        }

        public Long getEventId() {
            return eventId;
        }

        @Override
        public int hashCode() {
            int hash = 3;
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Id other = (Id) obj;
            if (!Objects.equals(this.deploymentId, other.deploymentId)) {
                return false;
            }
            return Objects.equals(this.eventId, other.eventId);
        }
    }
}

