/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author marco.mazzocchetti
 */
@Entity
@Table(
    name = "KS_BOOKMARK",
    indexes = {
        @Index(columnList =  "userId")}        
)
@Sortables(
    defaultSort = "id:ASC",
    value = {
        @Sortable(property = "id"),
        @Sortable(property = "description")
    }
)
public class Bookmark extends Auditable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)    
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false)    
    private Instance instance;   

    @Basic
    @Column(nullable = false)    
    private String userId;
    
    @Basic    
    private String description;

    public Bookmark() {
    }

    public Bookmark(Instance instance, String userId, String description) {
        this.instance = instance;
        this.userId = userId;
        this.description = StringUtils.abbreviate(description, 255);
    }

    @Override
    public Long getId() {
        return id;
    }

    public Instance getInstance() {
        return instance;
    }

    public String getUserId() {
        return userId;
    }

    public String getDescription() {
        return description != null ? 
            description : 
            instance.getBusinessName();
    }
}
