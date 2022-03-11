/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.spa.form.model;

import com.fasterxml.jackson.databind.node.ObjectNode;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
    name = "KS_FORM_SCHEMA"
)
public class FormSchema extends Identifiable {

    @EmbeddedId
    private Form.Id id;
    
    @MapsId 
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id")    
    private Form form;

    @Embedded
    private JsonLob value;

    public FormSchema() {
    }

    protected FormSchema(Form form, ObjectNode value) {
        this.form = form;
        update(value);
    }

    @Override
    public Form.Id getId() {
        return id;
    }

    public Form getForm() {
        return form;
    }

    public ObjectNode getValue() {
        return (ObjectNode) value.asJsonNode();
    }

    protected void update(ObjectNode value) {
        this.value = new JsonLob(value);
    }        
}
