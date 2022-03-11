/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.model;

import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
    name = "KS_PERMISSION",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"configuration_id", "type", "identity_"})}
)
public class Permission extends Auditable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)    
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false)
    private Configuration configuration;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)        
    private PermissionType type;
    
    @Basic
    @Column(name = "identity_", nullable = false)        
    private String identity;
    
    public Permission() {
    }

    public Permission(Configuration configuration, PermissionType type, String identity) {
        this.configuration = configuration;
        this.type = type;
        this.identity = identity;
    }

    @Override
    public Long getId() {
        return id;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public PermissionType getType() {
        return type;
    }

    public String getIdentity() {
        return identity;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + Objects.hashCode(this.type);
        hash = 23 * hash + Objects.hashCode(this.identity);
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
        final Permission other = (Permission) obj;
        if (!Objects.equals(this.identity, other.identity)) {
            return false;
        }
        if (this.type != other.type) {
            return false;
        }
        return true;
    }
}
