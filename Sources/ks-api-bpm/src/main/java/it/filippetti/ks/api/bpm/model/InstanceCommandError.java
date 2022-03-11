/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.model;

import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.Immutable;

/**
 *
 * @author marco.mazzocchetti
 */
@Immutable
@Entity
@Table(
    name = "ErrorInfo"      
)
public class InstanceCommandError extends Identifiable {
    
    @Id
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

    @Basic
    private String message;

    @Basic
    @Column(length = 5000)
    private String stacktrace;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "REQUEST_ID", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private InstanceCommand command;    

    public InstanceCommandError() {
    }

    @Override
    public Long getId() {
        return id;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getStacktrace() {
        return stacktrace;
    }

    public InstanceCommand getCommand() {
        return command;
    }
}
