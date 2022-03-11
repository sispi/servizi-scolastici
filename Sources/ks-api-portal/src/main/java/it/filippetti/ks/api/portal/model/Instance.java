/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.portal.model;

import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author dino
 */
@Entity
@Table(name = "INSTANCE")
public class Instance extends Auditable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)    
    private Long id;
    
    @Basic
    @ManyToOne(fetch = FetchType.LAZY, optional = false, targetEntity = Proceeding.class)
    private Proceeding proceeding;
    
    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date creationDate;
    
    @Lob
    @Column(nullable = false)
    private String model;
    
    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = true)
    private Date dispatchDate;
    
    @Basic
    @Column(nullable = false)
    private Boolean sent;
    
    @Basic
    @Column(nullable = true, unique = true)
    private Long bpmInstanceId;
    
    @Basic
    @Column(nullable = false)
    private String tenant;
    
    @Basic
    @Column(nullable = false)
    private String organization;
    
    @Basic
    @Column(nullable = false)
    private String userId;

    public Instance() {
    }

    public Instance(Long id, Proceeding proceeding, Date creationDate, String model, Date dispatchDate, Boolean sent, Long bpmInstanceId, String tenant, String organization, String userId) {
        this.id = id;
        this.proceeding = proceeding;
        this.creationDate = creationDate;
        this.model = model;
        this.dispatchDate = dispatchDate;
        this.sent = sent;
        this.bpmInstanceId = bpmInstanceId;
        this.tenant = tenant;
        this.organization = organization;
        this.userId = userId;
    }

    public Instance(Proceeding proceeding, Date creationDate, String model, Date dispatchDate, Boolean sent, Long bpmInstanceId, String tenant, String organization, String userId) {
        this.proceeding = proceeding;
        this.creationDate = creationDate;
        this.model = model;
        this.dispatchDate = dispatchDate;
        this.sent = sent;
        this.bpmInstanceId = bpmInstanceId;
        this.tenant = tenant;
        this.organization = organization;
        this.userId = userId;
    }

    @Override
    public Long getId() {
        return id;
    }

    public Long getBpmInstanceId() {
        return bpmInstanceId;
    }

    public void setBpmInstanceId(Long bpmInstanceId) {
        this.bpmInstanceId = bpmInstanceId;
    }

    public Proceeding getProceeding() {
        return proceeding;
    }

    public void setProceeding(Proceeding proceeding) {
        this.proceeding = proceeding;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Date getDispatchDate() {
        return dispatchDate;
    }

    public void setDispatchDate(Date dispatchDate) {
        this.dispatchDate = dispatchDate;
    }

    public Boolean getSent() {
        return sent;
    }

    public void setSent(Boolean sent) {
        this.sent = sent;
    }

    public String getTenant() {
        return tenant;
    }

    public void setTenant(String tenant) {
        this.tenant = tenant;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    
}
