/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.portal.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import org.hibernate.annotations.Formula;

/**
 *
 * @author marco.mazzocchetti
 */
@Embeddable
public class JsonString extends JsonValue {
    
    // workaround to init embedded in case of null value
    @Formula("0")
    private int _0_;    

    @Basic
    @Column(length = 4096)
    private String value;

    public JsonString() {
    }

    public JsonString(Object value) throws IllegalArgumentException {
        this.value = serialize(value);
    }

    @Override
    protected String value() {
        return value;
    }
}
