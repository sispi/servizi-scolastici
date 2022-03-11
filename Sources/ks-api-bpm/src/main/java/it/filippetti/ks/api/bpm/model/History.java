/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.model;

import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author marco.mazzocchetti
 */
@Entity
@Table(
    name = "KS_HISTORY"
)
@Fetchables({
    @Fetchable(property = "task")
})
@Sortables(
    defaultSort = "timestamp:ASC",
    value = {
        @Sortable(property = "timestamp", pathExpression = "id")  
    }
)
public class History extends Identifiable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false)
    private Instance instance;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    private Task task;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date timestamp;
    
    @Basic
    @Column(nullable = false)
    private String action;

    @Basic
    @Column(nullable = false)
    private String userId;

    @Basic
    private String targetIdentity;
    
    @Basic
    private String message;

    public History() {
    }

    public History(
        Instance instance, 
        String action, 
        AuthenticationContext context, 
        String message) {
        this.instance = instance;
        this.action = action;
        this.userId = context.getUserId();
        this.message = StringUtils.abbreviate(message, 255);
        this.timestamp = new Date();
    }
    
    public History(
        Task task, 
        String action, 
        AuthenticationContext context, 
        String targetIdentity, 
        String message) {
        this.instance = task.getInstance();
        this.task = task;
        this.action = action;
        this.userId = context.getUserId();
        this.targetIdentity = targetIdentity;
        this.message = StringUtils.abbreviate(message, 255);
        this.timestamp = new Date();        
    }

    @Override
    public Long getId() {
        return id;
    }

    public Instance getInstance() {
        return instance;
    }

    public Task getTask() {
        return task;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getAction() {
        return action;
    }

    public String getUserId() {
        return userId;
    }

    public String getTargetIdentity() {
        return targetIdentity;
    }

    public String getMessage() {
        return message;
    }
}
