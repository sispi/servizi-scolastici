/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.configuration;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author marco.mazzocchetti
 */
@Configuration
@EnableConfigurationProperties(DatastoreConfiguration.Properties.class)
public class DatastoreConfiguration {
    
    public static final String LOCAL_DATASOURCE_NAME = "local";
    
    @Autowired
    private DatastoreConfiguration.Properties properties;

    public Map<String, Object> connection() {
        return properties.connection();
    }    
    
    public DataSource datasource(String name) {
        return properties.datasource(name);
    }

    public Collection<DataSource> datasources() {
        return properties.datasources();
    }
    
    /**
     * 
     */
    @ConfigurationProperties(prefix = "application.datastore.jdbc")
    @ConstructorBinding
    public static class Properties {
        
        private Map<String, Object> connection;
        private Map<String, DataSource> datasources;

        public Properties(
            Map<String, Object> connection, 
            Set<DataSource> datasources) {
            this.connection = connection != null ? 
                connection : 
                Collections.EMPTY_MAP;
            this.datasources = new HashMap<>();
            if (datasources != null) {
                for (DataSource datasource : datasources) {
                    this.datasources.put(datasource.name(), datasource);
                }
            }
        }

        public Map<String, Object> connection() {
            return Collections.unmodifiableMap(connection);
        }

        public DataSource datasource(String name) {
            return datasources.get(name);
        }

        public Collection<DataSource> datasources() {
            return Collections.unmodifiableCollection(datasources.values());
        }
    }
    
    /**
     * 
     */
    @ConstructorBinding
    public static class DataSource {

        private String name;
        private String url;
        private String username;
        private String password;
        private String driverClassName;
        private Map<String, Object> connection;
        private String platform;
        private String catalog;
        private String schema;

        public DataSource(
            @DefaultValue(DatastoreConfiguration.LOCAL_DATASOURCE_NAME) String name, 
            @NotNull String url, 
            @NotNull String username, 
            @NotNull String password,
            @NotNull String driverClassName,
            Map<String, Object> connection, 
            String platform, 
            String catalog, 
            String schema) {

            this.name = name;
            this.url = url;
            this.username = username;
            this.password = password;
            this.driverClassName = driverClassName;
            this.connection = connection != null ? 
                connection : 
                Collections.EMPTY_MAP;
            this.platform = platform;
            this.catalog = catalog;
            this.schema = schema;
        }

        public boolean isLocal() {
            return name.equals(LOCAL_DATASOURCE_NAME);
        }
        
        public String name() {
            return name;
        }

        public String url() {
            return url;
        }

        public String username() {
            return username;
        }

        public String password() {
            return password;
        }

        public String driverClassName() {
            return driverClassName;
        }

        public Map<String, Object> connection() {
            return Collections.unmodifiableMap(connection);
        }

        public String platform() {
            return platform;
        }

        public String catalog() {
            return catalog;
        }

        public String schema() {
            return schema;
        }        
        
        @Override
        public int hashCode() {
            int hash = 7;
            hash = 37 * hash + Objects.hashCode(this.name);
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
            final DataSource other = (DataSource) obj;
            return Objects.equals(this.name, other.name);
        }
    }
}
