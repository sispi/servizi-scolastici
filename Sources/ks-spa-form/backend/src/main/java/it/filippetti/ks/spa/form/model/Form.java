/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.spa.form.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import it.filippetti.ks.spa.form.configuration.ApplicationProperties;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author marco.mazzocchetti
 */
@Entity
@Table(
    name = "KS_FORM",
    indexes = {
        @Index(columnList = "tenant, organization, name", unique = true)
    }
)
@Sortables(
    defaultSort = "name:ASC",
    value = {
        @Sortable(property = "name"),
        @Sortable(property = "lastModifiedTs")
    }
)
@Fetchables(
    value = {
        @Fetchable(property = "definition"),
        @Fetchable(property = "schema"),
        @Fetchable(property = "backups", type = Backup.class)
    }
)
public class Form extends Identifiable {
    
    @EmbeddedId
    private Id id;

    @Basic
    @Column(nullable = false)
    private String tenant;

    @Basic
    @Column(nullable = false)
    private String organization;

    @Basic
    @Column(nullable = false)    
    private String name;

    @Basic
    @Column(nullable = false)    
    private String lastModifiedBy;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)    
    private Date lastModifiedTs;    

    @OneToOne(mappedBy = "form", fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
    private FormSchema schema;
    
    @OneToOne(mappedBy = "form", fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
    private FormDefinition definition;

    @OrderBy("lastModifiedTs DESC")
    @OneToMany(mappedBy = "form", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Backup> backups;
    
    @MapKey(name = "id.sectionId.key")
    @OneToMany(mappedBy = "form", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Map<String, FormSection> sections;
    
    public Form() {
    }

    public Form(AuthenticationContext context, String id, String name, ObjectNode schema, ObjectNode definition) {
        this.id = new Id(context.getTenant(), context.getOrganization(), id);
        this.tenant = context.getTenant();
        this.organization = context.getOrganization();
        this.name = name;
        this.lastModifiedBy = context.getUserId();
        this.lastModifiedTs = new Date();
        this.schema = new FormSchema(this, schema);
        this.definition = new FormDefinition(this, definition);
        this.backups = new ArrayList<>();
        this.sections = new HashMap<>();
        updateDefinition(context, definition);
    }

    @Override
    public Id getId() {
        return id;
    }

    public String getTenant() {
        return tenant;
    }

    public String getOrganization() {
        return organization;
    }

    public String getName() {
        return name;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public Date getLastModifiedTs() {
        return lastModifiedTs;
    }

    public ObjectNode getSchema() {
        return schema.getValue();
    }

    public ObjectNode getDefinition() {
        
        ObjectNode definition;
        
        // get definition
        definition = this.definition.getValue();
        
        // compute sections
        definition
            .with("sections")
            .setAll(sections
                .values()
                .stream()
                .map(s -> s.getSection())
                .collect(Collectors
                    .toMap(
                        s -> s.getKey(), 
                        s -> s.getValue())));
        
        // return definition
        return definition;
    }

    /**
     * 
     * @return 
     */
    public String getScript() {
        
        JsonNode script;
        
        // get script
        script = definition.getValue().get("script");

        // return script text
        return script != null ? script.asText() : "";
    }
    
    public List<Backup> getBackups() {
        return backups;
    }

    /**
     * 
     * @param context
     * @param name 
     */
    public void updateName(AuthenticationContext context, String name) {

        if (this.name.equals(name)) {
            return;
        }
        
        this.lastModifiedBy = context.getUserId();
        this.lastModifiedTs = new Date();
        this.name = name;
    }

    /**
     * 
     * @param context
     * @param schema 
     */
    public void updateSchema(AuthenticationContext context, ObjectNode schema) {
        
        if (this.schema.getValue().equals(schema)) {
            return;
        }
        
        this.lastModifiedBy = context.getUserId();
        this.lastModifiedTs = new Date();
        this.schema.update(schema);
    }
    
    /**
     * 
     * @param context
     * @param definition 
     */
    public void updateDefinition(AuthenticationContext context, ObjectNode definition) {
        
        if (!isNew() && this.definition.getValue().equals(definition)) {
            return;
        }
        
        // add backup
        if (!isNew()) {
            backups.add(0, new Backup(this));
            if (backups.size() > ApplicationProperties.get().maxBackups()) {
                backups
                    .subList(ApplicationProperties.get().maxBackups(), backups.size())
                    .clear();
            }
        }
        
        // set modified
        lastModifiedBy = context.getUserId();
        lastModifiedTs = new Date();
        
        // update definition
        this.definition.update(definition);
        
        // update sections
        sections
            .keySet()
            .removeIf(k -> !definition.get("sections").has(k));
        definition
            .get("sections")
            .fields()
            .forEachRemaining(e -> {
                String k = e.getKey();
                ArrayNode v = (ArrayNode) e.getValue();
                sections.put(k, new FormSection(this, new Section(context, k, v)));
        });
    }

    @Embeddable
    public static final class Id implements Serializable {
        
        private String id;

        public Id() {
        }

        public Id(String tenant, String organization, String id) {
            this.id = String.format("%s/%s/%s", tenant, organization, id);
        }

        public String getTenant() {
            return id.split("/")[0];
        }

        public String getOrganization() {
            return id.split("/")[1];
        }

        public String getId() {
            return id.split("/")[2];
        }

        @Override
        public String toString() {
            return id;
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 97 * hash + Objects.hashCode(this.id);
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
            if (!Objects.equals(this.id, other.id)) {
                return false;
            }
            return true;
        }
    }            
}
