/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.spa.form.configuration;

import it.filippetti.ks.spa.form.ApplicationContextHolder;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 *
 * @author marco.mazzocchetti
 */
@Configuration
@ConfigurationProperties(prefix = "env")
public class ApplicationProperties {
    
    private static final Logger log = LoggerFactory.getLogger(ApplicationProperties.class);
    
    @Autowired
    private ConfigurableEnvironment environment;
    
    private List<Map<String, String>> systemProperties;
    
    private ApplicationProfile profile;
 
    @Value("${spring.application.name}")    
    private String name;
  
    @Value("${spring.application.baseUrl:}")    
    private String baseUrl;
    
    @Value("${application.adminRole:admins}")    
    private String adminRole;

    @Value("${application.adminUser:admin}")    
    private String adminUser;
    
    @Value("${application.systemUser:system}")    
    private String systemUser;
    
    @Value("${application.operation.expireAfter:5}")
    private Integer operationExpireAfter;
    
    @Value("${application.operation.cleaner.delay:1}")
    private Integer operationCleanerDelay;     

    @Value("${application.maxBackups:10}")
    private Integer maxBackups; 

    @Value("${application.templateCache.minSize:100}")
    private Integer templateCacheMinSize; 

    @Value("${application.templateCache.maxSize:1000}")
    private Integer templateCacheMaxSize; 
    
    @Value("${application.ftlPath}")
    private String ftlPath; 
    
    @PostConstruct
    public void init() {

        for (String value : environment.getActiveProfiles()) {
            try {
                profile = ApplicationProfile.valueOf(value);       
            } catch (IllegalArgumentException e) {
            }
        }
        if (profile == null) {
            throw new ExceptionInInitializerError("No application profile active");
        }
        
        // init/map system properties
        systemProperties.forEach(entry -> {
            environment.getSystemProperties().put(
                entry.get("name"), 
                entry.get("value").startsWith("$") ? 
                    environment.getProperty(entry.get("value").substring(1)) : 
                    entry.get("value"));
        });
    }
    
    public ApplicationProfile profile() {
        return profile;
    }

    public List<Map<String, String>> getSystemProperties() {
        return systemProperties;
    }

    public void setSystemProperties(List<Map<String, String>> systemProperties) {
        this.systemProperties = systemProperties;
    }

    public String name() {
        return name;
    }

    public String baseUrl() {
        return baseUrl;
    }

    public String adminRole() {
        return adminRole;
    }

    public String adminUser() {
        return adminUser;
    }

    public String systemUser() {
        return systemUser;
    }

    public Integer operationExpireAfter() {
        return operationExpireAfter;
    }

    public Integer operationCleanerDelay() {
        return operationCleanerDelay;
    }

    public Integer maxBackups() {
        return maxBackups;
    }

    public Integer templateCacheMinSize() {
        return templateCacheMinSize;
    }

    public Integer templateCacheMaxSize() {
        return templateCacheMaxSize;
    }

    public String ftlPath() {
        return ftlPath;
    }

    public static final ApplicationProperties get() {
        return ApplicationContextHolder.getBean(ApplicationProperties.class);
    }
}
