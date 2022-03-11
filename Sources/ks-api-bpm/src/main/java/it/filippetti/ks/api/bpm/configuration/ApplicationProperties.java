/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.configuration;

import it.filippetti.ks.api.bpm.ApplicationContextHolder;
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
    
    private String node;
    
    private Boolean mainNode;

    @Value("${application.adminRole:admins}")    
    private String adminRole;

    @Value("${application.adminUser:admin}")    
    private String adminUser;
    
    @Value("${application.systemUser:system}")    
    private String systemUser;

    @Value("${application.defaultCategory:default}")
    private String defaultCategory;

    @Value("${application.backoffice.baseUrl}")
    private String backofficeBaseUrl;

    @Value("${application.document.baseUrl}")
    private String documentBaseUrl;

    @Value("${application.document.basePath}")
    private String documentBasePath;
    
    @Value("${application.deploy.synchronizer.delay:3000}")
    private Long deploySynchronizerDelay;

    @Value("${application.deploy.synchronizer.batchSize:50}")
    private Integer deploySynchronizerBatchSize;

    @Value("${application.deploy.synchronizer.async:false}")
    private Boolean deploySynchronizerAsync;

    @Value("${application.deploy.synchronizer.maxRetries:3}")
    private Integer deploySynchronizerMaxRetries;

    @Value("${application.operation.expireAfter:5}")
    private Integer operationExpireAfter;
    
    @Value("${application.operation.cleaner.delay:1}")
    private Integer operationCleanerDelay;    

    @Value("${application.instance.archiver.delay:1}")
    private Long instanceArchiverDelay;

    @Value("${application.instance.archiver.batchSize:50}")
    private Integer instanceArchiverBatchSize;

    @Value("${application.notification.email.enabled:true}")
    private Boolean notificationEmailEnabled;

    @Value("${application.notification.archiver.delay:1}")
    private Long notificationCleanerDelay;

    @Value("${application.notification.archiver.batchSize:50}")
    private Integer notificationCleanerBatchSize;
    
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
        if (profile.isCluster()) {
            try {
                node = environment.getRequiredProperty(
                    ApplicationEnvironmentVariable.DEPLOYMENT_POD_NAME.name());
                mainNode = node.endsWith("-0");
            } catch (IllegalStateException e) {
                throw new ExceptionInInitializerError(String.format(
                    "Enviroment variable %s not defined", 
                    ApplicationEnvironmentVariable.DEPLOYMENT_POD_NAME));
            }
        } else {
            node = "standalone";
            mainNode = true;
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

    public String node() {
        return node;
    }

    public boolean isMainNode() {
        return mainNode;
    }

    public List<Map<String, String>> getSystemProperties() {
        return systemProperties;
    }

    public void setSystemProperties(List<Map<String, String>> systemProperties) {
        this.systemProperties = systemProperties;
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

    public String defaultCategory() {
        return defaultCategory;
    }

    public String backofficeBaseUrl() {
        return backofficeBaseUrl;
    }

    public String documentBaseUrl() {
        return documentBaseUrl;
    }

    public String documentBasePath() {
        return documentBasePath;
    }

    public Long deploySynchronizerDelay() {
        return deploySynchronizerDelay;
    }

    public Integer deploySynchronizerBatchSize() {
        return deploySynchronizerBatchSize;
    }

    public Boolean deploySynchronizerAsync() {
        return deploySynchronizerAsync;
    }
    
    public Integer deploySynchronizerMaxRetries() {
        return deploySynchronizerMaxRetries;
    }

    public Integer operationCleanerDelay() {
        return operationCleanerDelay;
    }

    public Integer operationExpireAfter() {
        return operationExpireAfter;
    }

    public Long instanceArchiverDelay() {
        return instanceArchiverDelay;
    }

    public Integer instanceArchiverBatchSize() {
        return instanceArchiverBatchSize;
    }

    public Boolean notificationEmailEnabled() {
        return notificationEmailEnabled;
    }

    public Long notificationCleanerDelay() {
        return notificationCleanerDelay;
    }

    public Integer notificationCleanerBatchSize() {
        return notificationCleanerBatchSize;
    }
    
    public static final ApplicationProperties get() {
        return ApplicationContextHolder.getBean(ApplicationProperties.class);
    }
}
