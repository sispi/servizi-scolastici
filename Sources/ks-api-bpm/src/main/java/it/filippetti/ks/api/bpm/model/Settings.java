/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.model;

import com.google.common.collect.Lists;
import it.filippetti.ks.api.bpm.configuration.ApplicationProperties;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author marco.mazzocchetti
 */
public class Settings extends JsonAssetContent {
    
    private static final Logger log = LoggerFactory.getLogger(Settings.class);

    public static final String KEY_CONFIGURATION = "configuration";    
    public static final String KEY_BUFFER_DEFAULTS = "bufferDefaults";
    public static final String KEY_EVENTS = "events";
    
    
    protected Settings(AssetContent assetContent) throws IOException {
        super(assetContent);
        
        if (assetContent.getAsset().getType() != AssetType.settings) {
            throw new IllegalArgumentException();
        }
    }    

    public Settings merge(Settings settings) throws InvalidObjectException {
        
        for (String key : Lists.newArrayList(KEY_CONFIGURATION, KEY_BUFFER_DEFAULTS)) {
            try {
                getMap(key).putAll(settings.getMap(key));
            } catch (Throwable t) {    
                log.error(t.getMessage(), t);
                throw new InvalidObjectException(String.format(
                    "Failed to merge settings key '%s'", 
                    key));
            }
        }
        return this;
    }

    public Settings setup(Configuration configuration) throws InvalidObjectException {
        
        try {
            setValue(configuration.getDeployment().getTenant(), KEY_CONFIGURATION, "ente");
            setValue(configuration.getOrganization(), KEY_CONFIGURATION, "aoo");
            if (!hasValue(KEY_CONFIGURATION, "respUo")) {
                setValue(configuration.getOrganization(), KEY_CONFIGURATION, "respUo");
            }    
        } catch (Throwable t) {  
            log.error(t.getMessage(), t);
            throw new InvalidObjectException("Failed to setup settings");
        }
        return this;        
    }

    public Boolean getRunnable() throws InvalidObjectException {
        
        try {
            return getBoolean(KEY_CONFIGURATION, "runnable");
        } catch (Throwable t) { 
            log.error(t.getMessage(), t);
            throw new InvalidObjectException("Failed to get runnable from settings"); 
        }
    }
    
    public String getCategory() throws InvalidObjectException {
        
        try {
            return getString(KEY_CONFIGURATION, "category");
        } catch (Throwable t) {       
            log.error(t.getMessage(), t);
            throw new InvalidObjectException("Failed to get category from settings"); 
        }
    }
    
    public String getRole(Configuration configuration) throws InvalidObjectException {

        String startRole;
        Object value;

        try {
            startRole = getString(KEY_CONFIGURATION, "startRole");
            if (startRole != null) { 
                if (startRole.equals("Administrators")) {
                    return ApplicationProperties.get().adminRole();
                }
                else if (startRole.equals("INSTANCE_ENTE") || startRole.equals("{INSTANCE_ENTE}")) {
                    return configuration.getDeployment().getTenant();
                }
                else if (startRole.equals("INSTANCE_AOO") || startRole.equals("{INSTANCE_AOO}")) {
                    return configuration.getOrganization();
                }
                else {                 
                    value = getValue(KEY_BUFFER_DEFAULTS, startRole);
                    if (value instanceof Map) {
                         return getString(KEY_BUFFER_DEFAULTS, startRole, "identity");
                    } else if (value instanceof String) {
                         return value.toString();
                    }
                }
            } 
        } catch (Throwable t) {    
            log.error(t.getMessage(), t);
            throw new InvalidObjectException("Failed to extract role from settings"); 
        }
        return null;
    }
    
    public Map<String, Object> getDefaultInput() {
        return getMap(KEY_BUFFER_DEFAULTS);
    }
    
    public Settings setDefaultInput(Map<String, Object> defaultInput) {
        setValue(defaultInput, KEY_BUFFER_DEFAULTS);
        return this;
    }
}
