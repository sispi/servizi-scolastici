/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.portal.model;

import javax.persistence.Basic;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import org.hibernate.annotations.Formula;

/**
 *
 * @author marco.mazzocchetti
 */
@Embeddable
public class JsonLob extends JsonValue {
    
    // workaround to init embedded in case of null value
    @Formula("0")
    private int _0_;    

    @Lob
    @Basic(fetch = FetchType.EAGER)
    private String value;

    public JsonLob() {
    }

    public JsonLob(Object value) throws IllegalArgumentException {
        this.value = serialize(value);
    }

    @Override
    protected String value() {
        return value;
    }
}
