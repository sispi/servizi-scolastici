package it.kdm.docer.services.agid.utils;

import java.io.File;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import it.kdm.docer.commons.configuration.ConfigurationLoadingException;

/**
 * Created by Łukasz Kwasek on 28/05/15.
 */
public class ConfigurationUtils {
    private static PropertiesConfiguration conf;

    public static PropertiesConfiguration getConf() throws ConfigurationException {
        if (conf == null) {
        	
        	try {
        		File confFile = it.kdm.docer.commons.configuration.ConfigurationUtils.loadResourceFile("configuration.properties");
        		conf = new PropertiesConfiguration(confFile);
        	} catch (ConfigurationLoadingException e) {
				throw new ConfigurationException(e);
			}
            
        }

        return conf;
    }
}
