/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.spa.form.model;

import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author marco.mazzocchetti
 */
@Entity
@Table(
    name = "KS_BACKUP",
    indexes = {
        @Index(columnList = "form_id, lastModifiedTs")
    }
)
@Fetchables(
    value = {
        @Fetchable(property = "definition", type = BackupDefinition.class)
    }
)
public class Backup extends Identifiable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)    
    private Long id;   

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false)    
    private Form form;

    @Basic
    @Column(nullable = false)    
    private String lastModifiedBy;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)    
    private Date lastModifiedTs;    

    @OneToOne(mappedBy = "backup", fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
    private BackupDefinition definition;

    public Backup() {
    }
    
    protected Backup(Form form) {
        this.form = form;
        this.lastModifiedBy = form.getLastModifiedBy();
        this.lastModifiedTs = form.getLastModifiedTs();
        this.definition = new BackupDefinition(this, form.getDefinition());
    }

    @Override
    public Long getId() {
        return id;
    } 

    public Form getForm() {
        return form;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public Date getLastModifiedTs() {
        return lastModifiedTs;
    }

    public ObjectNode getDefinition() {
        return definition.getValue();
    }
}
