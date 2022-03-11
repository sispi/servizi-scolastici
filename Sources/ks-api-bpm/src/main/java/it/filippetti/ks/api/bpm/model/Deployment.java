/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.model;

import com.google.common.collect.Sets;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;


/**
 *
 * @author marco.mazzocchetti
 */
@Entity
@Table(
    name = "KS_DEPLOYMENT",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"tenant", "organization", "name", "versionNumber"}),
    },
    indexes = {
        @Index(columnList = "tenant, shared, organization"),
        @Index(columnList = "tenant, processId, shared, organization")
    }
)
@Sortables(
    defaultSort = "name:ASC,version:ASC",
    value = {
        @Sortable(property = "name"),
        @Sortable(property = "version", pathExpression = "versionNumber"),
        @Sortable(property = "deployTs", pathExpression = "status.deployTs")    
    }
)
@Fetchables(
    defaultFetch = "configurations", 
    value = {
        @Fetchable(property = "configurations", type = Configuration.class),
        @Fetchable(property = "assets", type = Asset.class),
        @Fetchable(property = "dependencies", type = Deployment.class),
        @Fetchable(property = "dependants", type = Deployment.class),
        @Fetchable(property = "events", type = Event.class)    
    }
)
public class Deployment extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)    
    private Long id;

    @Basic
    @Column(nullable = false, unique = true)    
    private String unitId;

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
    private Boolean shared;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "unitId", 
        insertable = false,
        updatable = false,
        referencedColumnName = "unitId", 
        foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))        
    private DeploymentStatus status;

    @MapKey(name = "name")
    @OneToMany(mappedBy = "deployment", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Map<String, DeploymentAsset> assets;    

    @OneToMany(mappedBy = "deployment", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Configuration> configurations;
    
    @OneToMany(mappedBy = "dependant", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<DeploymentDependency> dependencies;

    @OneToMany(mappedBy = "dependency", fetch = FetchType.LAZY, 
        cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    private Set<DeploymentDependency> dependants;

    @OneToMany(mappedBy = "deployment", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<DeploymentEvent> events;
    
    public Deployment() {
    }

    public Deployment(
        String unitId, 
        String tenant, 
        String organization,
        String name, 
        String version, 
        Integer versionNumber, 
        String processId, 
        DeploymentStatus status, 
        Map<String, byte[]> contents) {
        this.unitId =  unitId;
        this.tenant = tenant;
        this.organization = organization;
        this.name = name;
        this.version = version;
        this.versionNumber =  versionNumber;
        this.processId = processId;
        this.shared = false;
        this.status = status;
        this.assets = new HashMap<>();
        this.dependencies = new HashSet<>();
        this.dependants = new HashSet<>();
        this.events = new HashSet<>();
        this.configurations = new HashSet<>();
        setAssets(contents);
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

    public String getGroupId() {
        return unitId.split(":")[0];
    }

    public String getArtifactId() {
        return unitId.split(":")[1];
    }

    public String getUnitId() {
        return unitId;
    }
    
    public String getProcessId() {
        return processId;
    }

    public boolean isShared() {
        return shared;
    }

    public void setShared(boolean shared) {
        this.shared = shared;
    }

    public DeploymentStatus getStatus() {
        return status;
    }

    public boolean hasAsset(String name) {
        return assets.containsKey(name);
    }

    public DeploymentAsset getAsset(String name) {
        return assets.get(name);
    }

    public DeploymentAsset getAsset(AssetType type) {
        
        if (type.isMultiple()) {
            throw new IllegalArgumentException();
        }
        for (DeploymentAsset asset: assets.values()) {
            if (asset.getType() == type) {
                return asset;
            }
        }
        return null;
    }

    public Collection<DeploymentAsset> getAssets(AssetType type) {
        
        List<DeploymentAsset> assets = new ArrayList<>();
        
        for (DeploymentAsset asset: this.assets.values()) {
            if (asset.getType() == type) {
                assets.add(asset);
            }
        }
        return Collections.unmodifiableCollection(assets);
    }

    public Set<AssetType> getAssetTypes() {
        return assets
            .values()
            .stream()
            .map(a -> a.getType())
            .collect(Collectors.toSet());
    }
    
    public Map<String, DeploymentAsset> getAssets() {
        return Collections.unmodifiableMap(assets);
    }
    
    public void setAssets(Map<String, byte[]> contents) {
        
        // remove obsolete
        assets
            .keySet()
            .removeIf(key -> !contents.containsKey(key));
        // add/replace current
        for (Map.Entry<String, byte[]> entry: contents.entrySet()) {
            putAsset(entry.getKey(), entry.getValue());
        }       
        // check required
        if (!getAssetTypes().containsAll(AssetType.requiredValues())) {
            throw new IllegalArgumentException();
        }
    }
    
    public DeploymentAsset putAsset(String name, byte[] content) {
        
        DeploymentAsset asset;
        
        asset = assets.get(name);
        if (asset == null) {
            asset = new DeploymentAsset(this, name, content);
            assets.put(name, asset);
        } else {
            asset.getContent().update(content);
        }
        if (asset.getType().isConfigurable()) {
            for (Configuration configuration : configurations) {
                if (!configuration.hasAsset(name)) {
                    configuration.putAsset(name, content);
                }
            }
        }
        return asset;
    }

    public Set<Deployment> getDependencyDeployments() {
        return dependencies
            .stream()
            .map(e -> e.getDependency())
            .collect(Collectors.toUnmodifiableSet());
    }

    public Deployment getDependencyDeployment(String processId) {
        return dependencies
            .stream()
            .map(e -> e.getDependency())
            .filter(e -> e.getProcessId().equals(processId))
            .findFirst()
            .orElse(null);
    }

    public Set<Deployment> getDependantDeployments() {
        return Collections.unmodifiableSet(dependants
            .stream()
            .map(e -> e.getDependant())
            .collect(Collectors.toSet()));
    }

    public Set<Deployment> getDependantDeployments(String organization) {
        return Collections.unmodifiableSet(dependants
            .stream()
            .map(e -> e.getDependant())
            .filter(e -> e.isShared() || e.getOrganization().equals(organization))
            .collect(Collectors.toSet()));
    }

    public Set<DeploymentDependency> getDependencies() {
        return Collections.unmodifiableSet(dependencies);
    }

    public void setDependencies(Set<DeploymentDependency> dependencies) {
        
        this.dependencies.retainAll(dependencies);
        for (DeploymentDependency dependency : dependencies) {
            if (!this.equals(dependency.getDependant())) {
                throw new IllegalArgumentException();
            }
            if (!this.dependencies.add(dependency)) {
                // already present, "just" update variable keys
                this.dependencies
                    .stream()
                    .filter(d -> d.equals(dependency))
                    .findFirst()
                    .get()
                    .setVariableKeys(dependency.getVariableKeys());
            }
        }
    }
    
    public Set<Event> getEvents() {
        return Collections.unmodifiableSet(events
            .stream()
            .map(e -> e.getEvent())
            .collect(Collectors.toSet()));
    }
    
    public void setEvents(Set<Event> events) {
        
        Set<DeploymentEvent> set;
        
        set = events
            .stream()
            .map(e -> new DeploymentEvent(this, e))
            .collect(Collectors.toSet());
        
        this.events.retainAll(set);
        this.events.addAll(set);
    }
    
    public DeploymentAsset getDefinition() {
        return getAsset(AssetType.definition);
    }
    
    public Set<Deployment> getUnitDeployments() {
        return computeUnitDeployments(Sets.newHashSet(this));
    }

    private Set<Deployment> computeUnitDeployments(Set<Deployment> deployments) {
        getDependencyDeployments().forEach(deployment -> {
            if (!deployments.contains(deployment)) {
                deployments.add(deployment);
                deployments.addAll(deployment.computeUnitDeployments(deployments));
            }
        });
        return deployments;
    }
    
    public Settings getSettings() throws IOException {
        
        Asset asset;
        
        asset = getAsset(AssetType.settings);
        if (asset != null) {
            return asset.getContent().asJsonAssetContent(Settings.class);
        } else {
            throw new IllegalStateException();
        }
    }    
   
    public Map<String, Object> getDefaultVariables() throws IOException {
        return Collections.unmodifiableMap(getSettings().getMap(Settings.KEY_BUFFER_DEFAULTS));
    }
    
    public boolean isOwned(AuthenticationContext context) {
        return context.getOrganization().equals(organization) && context.isAdmin();
    }

    public boolean isOwned(String organization) {
        return this.organization.equals(organization);
    }
 
    public boolean isConfigured() {
        return !configurations.isEmpty();
    }

    public boolean isConfigured(String organization, String profile) {
        return getConfiguration(organization, profile) != null;
    }
    
    public Collection<Configuration> getConfigurations() {
        return Collections.unmodifiableCollection(configurations);
    }

    public Collection<Configuration> getConfigurations(String organization) {
        return configurations
            .stream()
            .filter(c -> c.getOrganization().equals(organization))
            .collect(Collectors.toUnmodifiableList());
    }

    public Configuration getConfiguration(String organization, String profile) {
        for (Configuration configuration : configurations) {
            if (configuration.getOrganization().equals(organization) && 
                configuration.getProfile().equals(profile)) {
                return configuration;
            }
        }
        return null;
    }

    public Configuration addConfiguration(String organization, String profile) {
        
        Configuration configuration;
        
        configuration = getConfiguration(organization, profile);
        if (configuration == null) {
            configuration = new Configuration(this, organization, profile);
            configurations.add(configuration);
        }
        return configuration;
    }
}
