/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.model;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author marco.mazzocchetti
 */
@Entity
@Table(
    name = "KS_INSTANCE_VARIABLE_VALUE"
)
public class InstanceVariableValue extends Auditable {
    
    @Id
    private Long id;
    
    @MapsId 
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id")    
    private InstanceVariable variable;
    
    @Embedded
    private JsonLob value;
    
    public InstanceVariableValue() {
    }

    public InstanceVariableValue(InstanceVariable variable) {
        this.variable = variable;
        this.value = new JsonLob();
    }

    @Override
    public Long getId() {
        return id;
    }

    public InstanceVariable getVariable() {
        return variable;
    }

    public JsonValue getValue() {
        return value;
    }

    protected void update(Object value) {
        this.value = new JsonLob(value);
    }
}
