/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.portal.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 *
 * @author dino
 */
@Entity
@Table(name = "MEDIA")
public class Media  extends Auditable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Basic
    @Column(nullable = false)
    private String name;
    
    @Basic
    @Column(nullable = true)
    private String description;
    
    @Lob
    @Column(name = "file_", nullable = false)
    private String file;
    
    @Basic
    @Column(nullable = false)
    private String fileType;
    
    @Basic
    @Column(nullable = false)
    private String mimeType;
    
    @Basic
    @Column(name = "size_", nullable = true)
    private Long size;
    
    @Basic
    @Column(nullable = true, unique = true)
    private String myKey;
    
    @Basic
    @Column(nullable = false)
    private String tenant;
    
    @Basic
    @Column(nullable = false)
    private String organization;

    @Override
    public Long getId() {
        return id;
    }

    public Media() {
    }

    public Media(String name, String description, String file, String fileType, String mimeType, Long size, String myKey, String tenant, String organization) {
        this.name = name;
        this.description = description;
        this.file = file;
        this.fileType = fileType;
        this.mimeType = mimeType;
        this.size = size;
        this.myKey = myKey;
        this.tenant = tenant;
        this.organization = organization;
    }

    public Media(Long id, String name, String description, String file, String fileType, String mimeType, Long size, String myKey, String tenant, String organization) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.file = file;
        this.fileType = fileType;
        this.mimeType = mimeType;
        this.size = size;
        this.myKey = myKey;
        this.tenant = tenant;
        this.organization = organization;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getMyKey() {
        return myKey;
    }

    public void setMyKey(String myKey) {
        this.myKey = myKey;
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
    
}
