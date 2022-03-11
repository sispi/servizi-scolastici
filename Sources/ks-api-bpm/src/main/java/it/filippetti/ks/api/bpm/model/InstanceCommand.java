/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.model;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.Immutable;
import org.kie.api.executor.STATUS;

/**
 *
 * @author marco.mazzocchetti
 */

@Immutable 
@Entity
@Table(
    name = "RequestInfo",
    indexes = {
        @Index(columnList = "processInstanceId"),
        @Index(columnList = "businessKey")}
)
@Fetchables({
    @Fetchable(property = "node"),
    @Fetchable(property = "context"),
    @Fetchable(property = "results"),
    @Fetchable(property = "errors", type = InstanceCommandError.class)
})
public class InstanceCommand extends Identifiable {

    @Id
    private Long id;

    @Basic
    @Column(name = "commandName")
    private String name;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "timestamp")
    private Date executionTs;

    @Basic
    private Integer executions;
    
    @Enumerated(EnumType.STRING)
    private STATUS status;

    @Basic
    private Integer retries;
    
    @Basic
    private Integer priority;
    
    @Basic
    private String message;

    @Basic
    private String businessKey;

    @Basic
    @Column(name = "processInstanceId")
    private Long instanceId;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(
        name = "processInstanceId", 
        insertable = false,
        updatable = false,
        referencedColumnName = "id",
        foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Instance instance; 

    @OneToOne(mappedBy="command", fetch = FetchType.LAZY, optional = false)
    private InstanceCommandIO io ;
    
    @OrderBy("id ASC")
    @OneToMany(mappedBy="command", fetch = FetchType.LAZY)
    private List<InstanceCommandError> errors ;

    public InstanceCommand() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Date getExecutionTs() {
        return executionTs;
    }

    public Integer getExecutions() {
        return executions;
    }

    public STATUS getStatus() {
        return status;
    }
    
    public boolean isExecuting() {
        return 
            status == STATUS.QUEUED || 
            status == STATUS.RUNNING || 
            status == STATUS.RETRYING;    
    }

    public Integer getRetries() {
        return retries;
    }

    public Integer getPriority() {
        return priority;
    }

    public String getMessage() {
        return message;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public InstanceNode getNode() {
        if (businessKey != null) {
            return instance
                .getNodes(false)
                .stream()
                .filter(n -> Objects.equals(n.getBusinessKey(), businessKey))
                .findFirst()
                .orElse(null);
        } else {
            return null;
        }
    }

    public Long getInstanceId() {
        return instanceId;
    }

    public Instance getInstance() {
        return instance;
    }

    public InstanceCommandIO getIo() {
        return io;
    }

    public List<InstanceCommandError> getErrors() {
        return Collections.unmodifiableList(errors);
    }
}
