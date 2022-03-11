/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.filippetti.ks.api.bpm.ApplicationContextHolder;
import it.filippetti.ks.api.bpm.service.JsonAssetContentService;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.drools.core.util.MVELSafeHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author marco.mazzocchetti
 */
public class JsonAssetContent {
    
    private static final Logger log = LoggerFactory.getLogger(JsonAssetContent.class);
    
    private static final String EMPTY_STRING = "";
    
    protected AssetContent assetContent;
    
    private Map<String, Object> map;
    
    protected JsonAssetContent(AssetContent assetContent) throws IOException {
        this.assetContent = assetContent;
        this.map = getCache().get(assetContent);
    }    

    private JsonAssetContent(AssetContent assetContent, Map<String, Object> map) {
        this.assetContent = assetContent;
        this.map = map;
    }
    
    public JsonAssetContent copy() throws IOException {
        return new JsonAssetContent(
            assetContent,
            getObjectMapper().readValue(getObjectMapper().writeValueAsBytes(map), Map.class));
    }
    
    public JsonAssetContent setMap(Map<String, Object> map) throws IOException {
        this.map = map;
        return this;
    }
    
    public Map<String, Object> getMap() {
        return map;
    }

    public boolean hasValue(String... key) {
        return getValue(key) != null;
    }

    public JsonAssetContent setValue(Object value, String... key) {
        
        Map _map;

        _map = this.map;
        for (int i = 0; i < key.length - 1; i++) {
            _map = (Map) _map.get(key[i]);
        }
        _map.put(key[key.length - 1], value);
        return this;
    }

    public <T> T getValue(Class<T> type, String... key) {
        
        Map _map;
        Object value;
        
        _map = this.map;
        for (int i = 0; i < key.length - 1; i++) {
            try {
                _map = (Map) _map.get(key[i]);
                if (_map == null) {
                    return null;
                }
            } catch (ClassCastException e) {
                return null;
            }
        }
        value = _map.get(key[key.length - 1]);
        if (value == null || value.equals(EMPTY_STRING)) {
            return null;
        } else {
            return type.cast(value);
        }
    }

    public Object getValue(String... key) {
        return getValue(Object.class, key);
    }    

    public Map<String, Object> getMap(String... key) {
        return getValue(Map.class, key);
    }

    public List getList(String... key) {
        return getValue(List.class, key);
    }

    public String getString(String... key) {
        return getValue(String.class, key);
    }
 
    public Number getNumber(String... key) {
        return getValue(Number.class, key);
    }

    public Boolean getBoolean(String... key) {
        return getValue(Boolean.class, key);
    }
    
    public Object evaluate(String expression) {
        return MVELSafeHelper.getEvaluator().eval(expression, map);
    }
    
    public <T> T  evaluate(Class<T> type, String expression) {
        return MVELSafeHelper.getEvaluator().eval(expression, map, type);
    }
    
    public void flush() throws IOException {
        assetContent.update(getObjectMapper().writeValueAsBytes(map));        
    }        

    private static final ObjectMapper getObjectMapper() {
        return ApplicationContextHolder.getBean(ObjectMapper.class, "internalObjectMapper");
    }    
    
    private static final JsonAssetContentService getCache() {
        return ApplicationContextHolder.getBean(JsonAssetContentService.class);
    }      
}
