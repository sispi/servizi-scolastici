/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.portal.model;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

/**
 *
 * @author marco.mazzocchetti
 */
@Entity
@Table(
    name = "KS_OPERATION_RESULT",
    indexes = {
        @Index(columnList = "lastModifiedTs")
    }
)
public class OperationResult extends Auditable {
    
    @Id
    private String id;
    
    @Embedded
    private JsonLob value;

    public OperationResult() {
    }

    public OperationResult(String id, Object value) {
        this.id = id;
        this.value = new JsonLob(value);
    }

    @Override
    public String getId() {
        return id;
    }

    public JsonLob getValue() {
        return value;
    }
}
