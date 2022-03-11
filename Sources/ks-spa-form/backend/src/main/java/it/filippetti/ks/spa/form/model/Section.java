/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.spa.form.model;

import com.fasterxml.jackson.databind.node.ArrayNode;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author marco.mazzocchetti
 */
@Entity
@Table(
    name = "KS_SECTION",
    indexes = {
        // we need to redefine pk index because of column order
        @Index(columnList = "tenant, organization, key_")
    }        
)
@Sortables(
    defaultSort = "key:ASC",
    value = {
        @Sortable(property = "key", pathExpression = "id.key")
    }
)
@Fetchables(
    value = {
        @Fetchable(property = "value")
    }
)
public class Section extends Identifiable {

    @EmbeddedId
    private Id id;
    
    @Basic
    @Column(nullable = false)    
    private String lastModifiedBy;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)    
    private Date lastModifiedTs;  
    
    @Embedded
    private JsonLob value;

    public Section() {
    }

    public Section(AuthenticationContext context, String key, ArrayNode value) {
        this.id = new Id(context.getTenant(), context.getOrganization(), key);
        setValue(context, value);
    }

    @Override
    public Id getId() {
        return id;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public Date getLastModifiedTs() {
        return lastModifiedTs;
    }

    public String getKey() {
        return id.getKey();
    }

    public ArrayNode getValue() {
        return (ArrayNode) value.asJsonNode();
    }
    
    public final void setValue(AuthenticationContext context, ArrayNode value) {
        this.lastModifiedBy = context.getUserId();
        this.lastModifiedTs = new Date();        
        this.value = new JsonLob(value);
    }  
    
    @Embeddable
    public static final class Id implements Serializable {
        
        private String tenant;
        private String organization;
        
        @Column(name = "key_")
        private String key;

        public Id() {
        }

        public Id(String tenant, String organization, String key) {
            this.tenant = tenant;
            this.organization = organization;
            this.key = key;
        }

        public String getTenant() {
            return tenant;
        }

        public String getOrganization() {
            return organization;
        }

        public String getKey() {
            return key;
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 47 * hash + Objects.hashCode(this.tenant);
            hash = 47 * hash + Objects.hashCode(this.organization);
            hash = 47 * hash + Objects.hashCode(this.key);
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
            final Id other = (Id) obj;
            if (!Objects.equals(this.tenant, other.tenant)) {
                return false;
            }
            if (!Objects.equals(this.organization, other.organization)) {
                return false;
            }
            return Objects.equals(this.key, other.key);
        }
    }        
}
