/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.spa.form.model;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

/**
 *
 * @author marco.mazzocchetti
 */
@Entity
@Table(
    name = "KS_FORM_SECTION"
)
public class FormSection extends Identifiable {
    
    @EmbeddedId
    private Id id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("formId")
    private Form form;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, optional = false)
    @MapsId("sectionId")
    private Section section;

    public FormSection() {
    }
    
    protected FormSection(
        Form form, 
        Section section) {
        this.id = new Id(form.getId(), section.getId());
        this.form = form;
        this.section = section;
    }

    @Override
    public Id getId() {
        return id;
    }

    public Form getForm() {
        return form;
    }

    public Section getSection() {
        return section;
    }
    
    @Embeddable
    public static final class Id implements Serializable {
        
        private Form.Id formId;
        private Section.Id sectionId;

        public Id() {
        }

        public Id(Form.Id formId, Section.Id sectionId) {
            if (!formId.getTenant().equals(sectionId.getTenant()) || 
                !formId.getOrganization().equals(sectionId.getOrganization())) {
                throw new IllegalArgumentException();
            }
            this.formId = formId;
            this.sectionId = sectionId;
        }

        public Form.Id getFormId() {
            return formId;
        }

        public Section.Id getSectionId() {
            return sectionId;
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 67 * hash + Objects.hashCode(this.formId);
            hash = 67 * hash + Objects.hashCode(this.sectionId);
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
            final Id other = (Id) obj;
            if (!Objects.equals(this.formId, other.formId)) {
                return false;
            }
            return Objects.equals(this.sectionId, other.sectionId);
        }
    }    
}

