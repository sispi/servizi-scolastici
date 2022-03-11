/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.portal.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author dino
 */
@Entity
@Table(name = "FAVORITE_PROCEEDING")
public class FavoriteProceeding extends Auditable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)    
    private Long id;
    
    @Basic
    @ManyToOne(fetch = FetchType.LAZY, optional = false, targetEntity = Proceeding.class)
    private Proceeding proceeding;
    
    @Basic
    @Column(nullable = false)    
    private String tenant;
    
    @Basic
    @Column(nullable = false)
    private String organization;
    
    @Basic
    @Column(nullable = false)
    private String userId;

    public FavoriteProceeding() {
    }

    public FavoriteProceeding(Proceeding proceeding, String tenant, String organization, String userId) {
        this.proceeding = proceeding;
        this.tenant = tenant;
        this.organization = organization;
        this.userId = userId;
    }

    public FavoriteProceeding(Long id, Proceeding proceeding, String tenant, String organization, String userId) {
        this.id = id;
        this.proceeding = proceeding;
        this.tenant = tenant;
        this.organization = organization;
        this.userId = userId;
    }

    @Override
    public Long getId() {
        return id;
    }

    public Proceeding getProceeding() {
        return proceeding;
    }

    public void setProceeding(Proceeding proceeding) {
        this.proceeding = proceeding;
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
