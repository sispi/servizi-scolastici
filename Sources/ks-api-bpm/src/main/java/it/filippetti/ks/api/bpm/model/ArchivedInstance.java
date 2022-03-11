/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.model;

import it.filippetti.ks.api.bpm.exception.ValidationException;
import it.filippetti.ks.api.bpm.mapper.ContextMapper;
import it.filippetti.ks.api.bpm.mapper.MappingContext;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

/**
 *
 * @author marco.mazzocchetti
 */
@Entity
@Table(
    name = "KS_ARCHIVED_INSTANCE",
    indexes = {
        @Index(columnList = "lastModifiedTs"),
        @Index(columnList = "tenant, organization, name, versionNumber")
    }
)
public class ArchivedInstance extends Auditable {
    
    @Id
    private Long id;
    
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
    private String version;

    @Basic
    @Column(nullable = false)    
    private Integer versionNumber;

    @Basic
    @Column(nullable = false)    
    private String processId;

    @Basic
    @Column(nullable = false)    
    private String processUnitId;

    @Basic
    @Column(nullable = false)    
    private String unitId;

    @Basic
    @Column(nullable = false)    
    private Long rootId;
    
    @Basic
    private Long parentId;
    
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(
            name = "value", 
            column = @Column(name = "data"))
    })     
    private JsonLob data;
    
    public ArchivedInstance() {
    }

    public ArchivedInstance(Instance instance, ContextMapper<Instance, ? extends Object> mapper) {
        id = instance.getId();
        tenant = instance.getTenant();
        organization = instance.getOrganization();
        name = instance.getName();
        version = instance.getVersion();
        versionNumber = instance.getVersionNumber();
        processId = instance.getProcessId();
        processUnitId = instance.getProcessUnitId();
        unitId = instance.getUnitId();
        rootId = instance.getRootId();
        parentId = instance.getParentId();
        try {
            data = new JsonLob(mapper.map(
                instance, 
                MappingContext.of(
                    AuthenticationContext.system(tenant, organization),
                    Fetcher.of(Instance.class, ""
                    + "input,"
                    + "output,"
                    + "variables?publicOnly("
                        + "value),"
                    + "tasks("
                        + "input,"
                        + "output,"
                        + "attachments,"    
                        + "assignments,"
                        + "comments),"
                    + "notifications("
                        + "body,"
                        + "tags,"
                        + "recipients,"
                        + "attachments),"
                    + "history("
                        + "task)"
                    + (instance.isRoot() ? "," 
                    + "chat("
                        + "members,"
                        + "messages("
                            + "text))" : "")))));
        } catch (ValidationException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public Long getId() {
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

    public String getVersion() {
        return version;
    }

    public Integer getVersionNumber() {
        return versionNumber;
    }

    public String getProcessId() {
        return processId;
    }

    public String getProcessUnitId() {
        return processUnitId;
    }

    public String getUnitId() {
        return unitId;
    }

    public Long getRootId() {
        return rootId;
    }

    public Long getParentId() {
        return parentId;
    }

    public JsonLob getData() {
        return data;
    }
}
