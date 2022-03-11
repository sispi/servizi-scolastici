/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.model;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
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
    name = "KS_INSTANCE_NODE_IO"
)
public class InstanceNodeIO extends Auditable {
    
    @Id
    private Long id;
    
    @MapsId 
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id")    
    private InstanceNode node;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(
            name = "value", 
            column = @Column(name = "input"))
    })     
    private JsonLob input;
    
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(
            name = "value", 
            column = @Column(name = "output"))
    })     
    private JsonLob output;

    public InstanceNodeIO() {
    }

    public InstanceNodeIO(InstanceNode node, Object input) {
        this.node = node;
        this.input = new JsonLob(input);
    }

    @Override
    public Long getId() {
        return id;
    }

    public InstanceNode getNode() {
        return node;
    }

    public JsonValue getInput() {
        return input;
    }

    public JsonValue getOutput() {
        return output;
    }

    protected void setOutput(Object output) {
        this.output = new JsonLob(output);
    }
}
