/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.model;

import java.util.Objects;
import java.util.Optional;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 *
 * @author marco.mazzocchetti
 */
@Entity
@Table(
    name = "KS_INSTANCE_ACCESS",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"instance_id", "identity_", "type", "task_id"})
    },
    indexes = {
        @Index(columnList = "rootInstance_id, identity_")
    }
)
public class InstanceAccess extends Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false)    
    private Instance instance;

    // we need to set optional true for root instances because in some scenario 
    // (independent call activity) a child instance can survive to root    
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))    
    private Instance rootInstance;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InstanceAccessType type;

    @Basic
    @Column(name = "identity_", nullable = false)
    private String identity;
    
    @Basic
    @Column(name = "task_id")
    private Long taskId;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(
        name = "task_id", 
        insertable = false,
        updatable = false,
        referencedColumnName = "id")
    private Task task;
    
    public InstanceAccess() {
    }

    protected InstanceAccess(
        Instance instance,
        String identity,
        InstanceAccessType type,
        Task task) {
        this.instance = instance;
        this.rootInstance = instance.getRootInstance().orElse(null);
        this.identity = identity;
        this.type = type;
        if (task != null) {
            this.taskId = task.getId();
            this.task = task;
        }
        
        if (type == InstanceAccessType.task && task == null || 
            type != InstanceAccessType.task && task != null) {
            throw new IllegalArgumentException();
        }
    }
    
    @Override
    public Long getId() {
        return id;
    }

    public Instance getInstance() {
        return instance;
    }

    public Optional<Instance> getRootInstance() {
        return optionalProxy(rootInstance);
    }

    public String getIdentity() {
        return identity;
    }
    
    public InstanceAccessType getType() {
        return type;
    }

    public Long getTaskId() {
        return taskId;
    }

    public Task getTask() {
        return task;
    }

    @Override
    public int hashCode() {
        int hash = 7;
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
        final InstanceAccess other = (InstanceAccess) obj;
        if (!Objects.equals(this.identity, other.identity)) {
            return false;
        }
        if (this.type != other.type) {
            return false;
        }
        if (!Objects.equals(this.taskId, other.taskId)) {
            return false;
        }
        return true;
    }
}
