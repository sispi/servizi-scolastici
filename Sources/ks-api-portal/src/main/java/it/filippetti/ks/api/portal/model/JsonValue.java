/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.portal.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.filippetti.ks.api.portal.ApplicationContextHolder;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author marco.mazzocchetti
 */
public abstract class JsonValue implements Serializable {
    
    protected abstract String value();

    protected final ObjectMapper getObjectMapper() {
        return ApplicationContextHolder.getBean(ObjectMapper.class, "internalObjectMapper");
    }
    
    protected final String serialize(Object value) throws IllegalArgumentException {
        try {
            if (value == null) {
                return null;
            } else {
                // remove stacktrace of exceptions
                if (value instanceof Throwable) {
                    ((Throwable) value).setStackTrace(new StackTraceElement[]{});
                }
                return getObjectMapper().writeValueAsString(value);
            }
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException();
        }
    }
    
    protected final <T> T deserialize(Class<T> type) throws IllegalStateException {
        try {
            if (value() == null) {
                return null;
            } else {
                return getObjectMapper().readValue(value(), type);
            }
        } catch (JsonProcessingException e) {
            throw new IllegalStateException();
        }
    }
    
    public boolean isNull() {
        return value() == null;
    }

    public String asJson() {
        return value() == null ? "null" : value();
    }
    
    public String asString() {
        return deserialize(String.class);
    }

    public Integer asInteger() {
        return deserialize(Integer.class);
    }

    public Long asLong() {
        return deserialize(Long.class);
    }

    public Float asFloat() {
        return deserialize(Float.class);
    }

    public Double asDouble() {
        return deserialize(Double.class);
    }

    public Boolean asBoolean() {
        return deserialize(Boolean.class);
    }

    public Map<String, Object> asMap() {
        return deserialize(Map.class);
    }

    public List asList() {
        return deserialize(List.class);
    }
    
    public Object asObject() {
        return deserialize(Object.class);
    }

    public <T> T as(Class<T> type) {
        return deserialize(type);
    }
 
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 43 * hash + Objects.hashCode(this.value());
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
        final JsonValue other = (JsonValue) obj;
        return Objects.equals(this.value(), other.value());
    }   
}
