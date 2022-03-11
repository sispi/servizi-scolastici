/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import it.filippetti.ks.api.bpm.model.AssetContent;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 *
 * @author marco.mazzocchetti
 */
@Service
public class JsonAssetContentService {
    
    @Autowired
    @Qualifier("internalObjectMapper")
    private ObjectMapper objectMapper;
    
    private LoadingCache<Key, Map<String, Object>> cache;
    
    @PostConstruct
    public void init() {
        cache = CacheBuilder.newBuilder()
            .initialCapacity(100)
            .maximumSize(1000)
            .concurrencyLevel(8)
            .build(new CacheLoader<Key, Map<String, Object>>() {
                @Override
                public Map<String, Object> load(Key key) throws Exception {
                   return objectMapper.readValue(key.readBytes(), Map.class);
                }
            });
    }
    
    public Map<String, Object> get(AssetContent assetContent) throws IOException {
        if (!assetContent.isNew()) {
            try {
                return cache.get(new Key(assetContent));
            } catch (ExecutionException e) {
                if (e.getCause() instanceof IOException) {
                    throw (IOException) e.getCause();
                } else {
                    throw (RuntimeException) e.getCause();
                }
            }        
        } else {
            return objectMapper.readValue(assetContent.asBytes(), Map.class);
        }
    }
    
    public void invalidate(AssetContent assetContent) {
        if (!assetContent.isNew()) {
            cache.invalidate(new Key(assetContent));
        }
    }
    
    /**
     * 
     */
    private static class Key {
        
        private Long id;
        private byte[] bytes;
        
        private Key(AssetContent assetContent) {
            this.id = assetContent.getId();
            this.bytes = assetContent.asBytes();
        }

        public Long getId() {
            return id;
        }

        public byte[] readBytes() {
            
            byte[] bytes;
            
            if (this.bytes != null) {
                bytes = this.bytes;
                this.bytes = null;
                return bytes;
            } else {
                throw new IllegalStateException();
            }
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 11 * hash + Objects.hashCode(this.id);
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
            final Key other = (Key) obj;
            if (!Objects.equals(this.id, other.id)) {
                return false;
            }
            return true;
        }
    }
}
