/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import org.hibernate.annotations.Formula;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author marco.mazzocchetti
 */
@Embeddable
public class JsonString extends JsonValue {
    
    private static final Logger log = LoggerFactory.getLogger(JsonString.class);
    
    // workaround to init embedded in case of null value
    @Formula("0")
    private int _0_;    

    @Basic
    @Column(length = 4000)
    private String value;

    public JsonString() {
    }

    public JsonString(Object value) throws IllegalArgumentException {
        this.value = serialize(value);
        if (this.value != null && this.value.length() > 4000) {
            log.warn("Serialized json exceeds maximum size and not will be stored");
            this.value = null;
        }
    }

    @Override
    protected String value() {
        return value;
    }
}
