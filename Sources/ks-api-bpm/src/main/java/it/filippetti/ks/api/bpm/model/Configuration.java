/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.model;

import it.filippetti.ks.api.bpm.configuration.ApplicationProperties;
import java.io.IOException;
import java.io.InvalidObjectException;
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
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author marco.mazzocchetti
 */
@Entity
@Table(
    name = "KS_CONFIGURATION",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"organization", "profile", "deployment_id"})
    },
    indexes = {
        @Index(columnList = "id, searchable"),
        @Index(columnList = "organization, runnable")
    }    
)
@Sortables(
    defaultSort = "deployment.name:ASC,deployment.version:ASC",
    value = {
        @Sortable(property = "deployment.name"),
        @Sortable(property = "deployment.version", pathExpression = "deployment.versionNumber")    
    }
)
@Fetchables(
    defaultFetch = "deployment", 
    value = {
        @Fetchable(property = "deployment", type = Deployment.class),
        @Fetchable(property = "defaultInput"),        
        @Fetchable(property = "assets", type = Asset.class),
        @Fetchable(property = "permissions", type = Permission.class),
        @Fetchable(property = "retentionPolicy", type = RetentionPolicy.class),
        @Fetchable(property = "authorizations")    
    }
)
public class Configuration extends Auditable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)    
    private Long id;

    @Basic
    @Column(nullable = false)    
    private String organization;

    @Basic
    @Column(nullable = false)    
    private String profile;
    
    @Basic
    @Column(nullable = false)
    private String category;

    @Basic
    @Column(nullable = false)    
    private Boolean active;

    @Basic
    @Column(nullable = false)    
    private Boolean runnable;

    @Basic
    @Column(nullable = false)    
    private Boolean searchable;    
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false)    
    private Deployment deployment;
    
    @MapKey(name = "name")
    @OneToMany(mappedBy = "configuration", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Map<String, ConfigurationAsset> assets;    

    @OneToMany(mappedBy = "configuration", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Permission> permissions;    
    
    @Embedded
    private RetentionPolicy retentionPolicy;
    
    public Configuration() {
    }

    protected Configuration(Deployment deployment, String organization, String profile) {
        this.deployment = deployment;
        this.organization = organization;
        this.profile = profile;
        this.active = false;
        this.runnable = false;
        this.searchable = false;      
        this.category = ApplicationProperties.get().defaultCategory();
        this.assets = new HashMap<>();
        this.permissions = new HashSet<>();
        this.retentionPolicy = new RetentionPolicy();
        setAssets(deployment);
    }
    
    @Override
    public Long getId() {
        return id;
    }

    public String getProfile() {
        return profile;
    }

    public String getOrganization() {
        return organization;
    }

    public Deployment getDeployment() {
        return deployment;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = StringUtils.abbreviate(category, 255);
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isRunnable() {
        return runnable;
    }

    public void setRunnable(boolean runnable) {
        this.runnable = runnable;
    }

    public boolean isSearchable() {
        return searchable;
    }

    public void setSearchable(boolean searchable) {
        this.searchable = searchable;
    }

    public boolean hasAsset(String name) {
        return assets.containsKey(name);
    }
    
    public ConfigurationAsset getAsset(String name) {
        return assets.get(name);
    }

    public ConfigurationAsset getAsset(AssetType type) {
        if (type.isMultiple()) {
            throw new IllegalArgumentException();
        }
        for (ConfigurationAsset asset: assets.values()) {
            if (asset.getType() == type) {
                return asset;
            }
        }
        return null;
    }
    
    public Collection<ConfigurationAsset> getAssets(AssetType type) {
        
        List<ConfigurationAsset> assets = new ArrayList<>();
        
        for (ConfigurationAsset asset: this.assets.values()) {
            if (asset.getType() == type) {
                assets.add(asset);
            }
        }
        return Collections.unmodifiableCollection(assets);
    }

    public Map<String, ConfigurationAsset> getAssets() {
        return Collections.unmodifiableMap(assets);
    }

    public void setAssets(Deployment deployment) {
        // remove obsolete
        assets
            .keySet()
            .removeIf(key -> !deployment.getAssets().containsKey(key));
        // add/replace current        
        for (Asset asset : deployment.getAssets().values()) {
            if (asset.getType().isConfigurable()) {
                putAsset(asset.getName(), asset.getContent().asBytes());
            }
        }    
    }
    
    public ConfigurationAsset putAsset(String name, byte[] content) {
        
        AssetType type = AssetType.valueFor(name);
        ConfigurationAsset asset;
        
        if (!type.isConfigurable()) {
            throw new IllegalArgumentException();
        }
        asset = assets.get(name);
        if (asset == null) {
            asset = new ConfigurationAsset(this, name, content);
            assets.put(name, asset);
        } else {
            asset.getContent().update(content);
        }
        return asset;
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
    
    public Set<Permission> getPermissions() {
        return Collections.unmodifiableSet(permissions);
    }    
   
    public Set<Permission> getPermissions(PermissionType type) {
        return Collections.unmodifiableSet(permissions
            .stream()
            .filter(p -> p.getType().equals(type))
            .collect(Collectors.toSet()));
    }    

    public boolean hasPermission(PermissionType type, AuthenticationContext context) {
        return 
            context.isAdmin() || 
            permissions
                .stream()
                .anyMatch(p -> 
                     type.equals(p.getType()) && 
                     context.getIdentities().contains(p.getIdentity()));
    }    

    public boolean hasPermission(PermissionType type, String identity) {
        return permissions
            .stream()
            .anyMatch(p -> 
                 type.equals(p.getType()) && 
                 identity.equals(p.getIdentity()));
    }    

    public void addPermission(PermissionType type, String identity) {
        
        if (!hasPermission(type, identity)) {
            permissions.add(new Permission(this, type, identity));
        }
    }  

    public void setPermissions(PermissionType type, Set<String> identities) {
        // remove obsolete
        permissions
            .removeIf(p -> p.getType().equals(type) && !identities.contains(p.getIdentity()));        
        // add
        for (String identity : identities) {
            addPermission(type, identity);
        }
    }  

    public Set<ConfigurationAuthorization> getAuthorizations(AuthenticationContext context) {
        
        Set<ConfigurationAuthorization> authorizations;
        
        authorizations = new HashSet<>();
        for (ConfigurationAuthorization authorization : ConfigurationAuthorization.values()) {
            try {
                if (hasPermission(PermissionType.valueOf(authorization.name()), context)) {
                    authorizations.add(authorization);
                }
            } catch (IllegalArgumentException e) {
                throw new IncompatibleClassChangeError();
            }
        }
        return Collections.unmodifiableSet(authorizations);
    }
    
    public boolean hasAuthorization(AuthenticationContext context, ConfigurationAuthorization authorization) {
        return getAuthorizations(context).contains(authorization);
    }
    
    public RetentionPolicy getRetentionPolicy() {
        return retentionPolicy;
    }
    
    public void merge(Configuration configuration) throws InvalidObjectException, IOException {
        
        getSettings().merge(configuration.getSettings()).flush();
    }
    
    public void setup() throws InvalidObjectException, IOException {

        Settings settings;
        String role;
        
        settings = getSettings();
        
        settings.setup(this).flush();
        
        role = settings.getRole(this);
        if (role != null) {
            for (PermissionType permissionName : PermissionType.values()) {
                setPermissions(permissionName, Set.of(role));
            }        
        }
        
        category = settings.getCategory();
        if (category == null) {
            category = ApplicationProperties.get().defaultCategory();
        }
        
        runnable = settings.getRunnable();
        if (runnable == null) {
            runnable = role != null;
        }
        
        searchable = runnable;
    }    
}
