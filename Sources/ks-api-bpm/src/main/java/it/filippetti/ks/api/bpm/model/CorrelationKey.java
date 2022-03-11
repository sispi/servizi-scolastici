/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.model;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;

/**
 *
 * @author marco.mazzocchetti
 */
@Embeddable
public class CorrelationKey implements Serializable {

    private static final HashFunction hashFunction = Hashing.murmur3_128();

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(
            name = "value", 
            column = @Column(name = "correlationKey", nullable = false, length = 4000))
    })     
    private JsonString value;

    @Basic
    @Column(name = "correlationKeyHash", nullable = false, length = 32)
    private String hash;

    public CorrelationKey() {
    }

    public CorrelationKey(Object value) {
        this.value = new JsonString(value);
        this.hash = hashFunction
            .hashString(this.value.asJson(), StandardCharsets.UTF_8)
            .toString();
    }

    public JsonValue getValue() {
        return value;
    }

    public String getHash() {
        return hash;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.hash);
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
        final CorrelationKey other = (CorrelationKey) obj;
        if (!Objects.equals(this.hash, other.hash)) {
            return false;
        }
        return true;
    }
}
