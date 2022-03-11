/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.model;

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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author marco.mazzocchetti
 */
@Entity
@Table(
    name = "KS_INSTANCE_CORRELATION",
    indexes = {
        @Index(columnList = "instance_id, correlationType, correlationKeyHash, messageId")
    }
)
public class InstanceCorrelation extends Auditable {
    
    public static final String ANY_MESSAGE_ID = "*";
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false)
    private Instance instance;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CorrelationType correlationType;

    @Embedded
    private CorrelationKey correlationKey;

    @Basic
    private String messageId;

    public InstanceCorrelation() {
    }

    private InstanceCorrelation(
        Instance instance, 
        CorrelationType correlationType, 
        CorrelationKey correlationKey, 
        String messageId) {
        this.instance = instance;
        this.correlationType = correlationType;
        this.correlationKey = correlationKey;
        this.messageId = messageId;
    }

    @Override
    public Long getId() {
        return id;
    }

    public Instance getInstance() {
        return instance;
    }

    public CorrelationKey getCorrelationKey() {
        return correlationKey;
    }

    public void setCorrelationKey(CorrelationKey correlationKey) {
        if (correlationType == CorrelationType.conversation) {
            throw new UnsupportedOperationException();
        }
        this.correlationKey = correlationKey;
    }

    public CorrelationType getCorrelationType() {
        return correlationType;
    }

    public String getMessageId() {
        return messageId;
    }
    
    public static InstanceCorrelation newContextCorrelation(
        Instance instance, CorrelationKey correlationKey) {
        return new InstanceCorrelation(
            instance, CorrelationType.context, correlationKey, ANY_MESSAGE_ID);
    } 
    
    public static InstanceCorrelation newConversationCorrelation(
        Instance instance, CorrelationKey correlationKey, String messageId) {
        return new InstanceCorrelation(
            instance, CorrelationType.conversation, correlationKey, messageId);
    }    
}
