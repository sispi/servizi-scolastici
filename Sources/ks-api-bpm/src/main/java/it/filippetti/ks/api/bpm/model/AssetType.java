/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.model;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.http.MediaType;

/**
 *
 * @author marco.mazzocchetti
 */
public enum AssetType {
    
    metadata        ("^config\\.properties$",       "config.properties",    MediaType.TEXT_PLAIN),
    definition      ("^flow\\.bpmn(2)?$",           "flow.bpmn",            MediaType.APPLICATION_XML),
    settings        ("^settings\\.json$",           "settings.json",        MediaType.APPLICATION_JSON),
    forms           ("^forms\\.json$",              "forms.json",           MediaType.APPLICATION_JSON),
    form_configure  ("^config\\.ftl$",              "config.ftl",           MediaType.APPLICATION_XML),
    form_start      ("^startup\\.ftl$",             "startup.ftl",          MediaType.APPLICATION_XML),
    form_info       ("^info\\.ftl$",                "info.ftl",             MediaType.APPLICATION_XML),
    form_clone      ("^clone\\.ftl$",               "clone.ftl",            MediaType.APPLICATION_XML),
    form_preview    ("^preview\\.ftl$",             "preview.ftl",          MediaType.APPLICATION_XML),
    form_task       ("^.+\\.ftl$",                  "Human Task_+.ftl$",    MediaType.APPLICATION_XML),
    preview         ("^preview/.+\\.(ftl|html)$",   "preview/+.html$",      MediaType.TEXT_HTML),
    project         ("^project\\.json$",            "project.json",         MediaType.APPLICATION_JSON);

    private static Set<AssetType> requiredValues = Stream.of(values())
        .filter(v -> v.isRequired())
        .collect(Collectors.toSet());
    
    private AssetType(String entryNameRegex, String entryName, MediaType mediaType) {
        this.entryNameRegex = entryNameRegex;
        this.entryName = entryName;
        this.mediaType = mediaType;
    }
  
    private final String entryName;    
    private final String entryNameRegex;
    private final MediaType mediaType;

    public MediaType mediaType() {
        return mediaType;
    }
    
    public String entryName() {
        return entryName;
    }

    public boolean isConfigurable() {
        return
            this == settings        || 
            this == project         || 
            this.isForm();
    }

    public boolean isMetadata() {
        return this == metadata;
    }

    public boolean isSettings() {
        return this == settings;
    }

    public boolean isForm() {
        return this.name().startsWith("form_");
    }

    public boolean isConfigurationForm() {
        return 
            this == form_configure || 
            this == form_start;
    }

    public boolean isInstanceForm() {
        return 
            this == form_info || 
            this == form_clone;
    }
    
    public boolean isMultiple() {
        return
            this == form_task ||
            this == preview;
    }
    
    public boolean isRequired() {
        return 
            this == metadata || 
            this == definition || 
            this == settings;
    }    

    public boolean isJson() {
        return mediaType == MediaType.APPLICATION_JSON;
    }

    public static AssetType valueFor(String entryName) {
        
        for (AssetType value : values()) {
            if (entryName.matches(value.entryNameRegex)) {
                return value;
            }
        }
        throw new IllegalArgumentException();
    }
    
    public static Set<AssetType> requiredValues() {
        return requiredValues;
    }
    
    public static AssetType configurationForm(String name) {
        
        AssetType type;
        
        try {
            type = valueOf(String.format("form_%s", name));
            if (type == form_configure || 
                type == form_start) {
                return type;
            }
        } catch (IllegalArgumentException e) {
            return null;
        }
        return null;
    }

    public static AssetType instanceForm(String name) {
        
        AssetType type;
        
        try {
            type = valueOf(String.format("form_%s", name));
            if (type == form_info || 
                type == form_clone) {
                return type;
            }
        } catch (IllegalArgumentException e) {
            return null;
        }
        return null;
    }    
}
