/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.model;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author marco.mazzocchetti
 */
public class ProcessMetadata {
    
    private String name;
    private String version;
    private Integer versionNumber;
    private String processId;
    
    public ProcessMetadata() {
    }

    public void read(InputStream inputStream) 
        throws IllegalArgumentException, IOException {
        
        Properties properties;
        
        properties = new Properties();
        
        properties.load(inputStream);
        
        name = properties.getProperty("name");
        if (StringUtils.isBlank(name) || name.contains(":")) {
            throw new IllegalArgumentException();
        }
        
        version = properties.getProperty("version");
        if (StringUtils.isBlank(version)) {
            throw new IllegalArgumentException();
        }
        
        versionNumber = computeVersionNumber(version);  
        
        processId = String.format("%s%s", name, version);
    }


    public String name() {
        return name;
    }

    public String version() {
        return version;
    }

    public Integer versionNumber() {
        return versionNumber;
    }

    public String processId() {
        return processId;
    }
    
    public static Integer computeVersionNumber(String version) throws IllegalArgumentException {
        
        String[] parts;
        Integer major, minor;
        
        parts = version.split("\\.");
        if (parts.length != 2) {
            throw new IllegalArgumentException();
        }
        try {
            major = Integer.parseInt(parts[0]);
            minor = Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException();
        }
        if (major < 0 || minor < 0 || minor > 999) {
            throw new IllegalArgumentException();
        }
        return 1000 * major + minor;
    }        
}
