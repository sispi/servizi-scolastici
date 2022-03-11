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
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author dino
 */
@Entity
@Table(name = "SERVICE")
public class Service extends Auditable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)    
    private Long id;
    
    @Basic
    @ManyToOne(fetch = FetchType.LAZY, optional = true, targetEntity = Service.class)
    private Service parent;

    @Basic
    @Column(nullable = false)
    private String name;
    
    @Basic
    @Column(nullable = true)
    private Integer position;
    
    @Lob
    @Column(nullable = true)
    private String logo;

    @Basic
    @Column(nullable = false)
    private String code;
    
    @Basic
    @Column(nullable = true)
    private Boolean valid;
    
    @Basic
    @Column(nullable = true)
    private Boolean externalService;
    
    @Basic
    @Column(nullable = true)
    private String link;
    
    @Basic
    @Column(nullable = false)    
    private String tenant;
    
    @Basic
    @Column(nullable = false)
    private String organization;

    public Service() {
    }

    public Service(Service parent, String name, Integer position, String logo, String code, Boolean valid, Boolean externalService, String link, String tenant, String organization) {
        this.parent = parent;
        this.name = name;
        this.position = position;
        this.logo = logo;
        this.code = code;
        this.valid = valid;
        this.externalService = externalService;
        this.link = link;
        this.tenant = tenant;
        this.organization = organization;
    }

    public Service(Long id, Service parent, String name, Integer position, String logo, String code, Boolean valid, Boolean externalService, String link, String tenant, String organization) {
        this.id = id;
        this.parent = parent;
        this.name = name;
        this.position = position;
        this.logo = logo;
        this.code = code;
        this.valid = valid;
        this.externalService = externalService;
        this.link = link;
        this.tenant = tenant;
        this.organization = organization;
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

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Boolean isValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    public Boolean isExternalService() {
        return externalService;
    }

    public void setExternalService(Boolean externalService) {
        this.externalService = externalService;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public Long getId() {
        return id;
    }

    public Service getParent() {
        return parent;
    }

    public void setParent(Service parent) {
        this.parent = parent;
    }
}
