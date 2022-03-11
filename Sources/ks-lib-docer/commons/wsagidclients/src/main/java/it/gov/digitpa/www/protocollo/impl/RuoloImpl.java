/*
 * XML Type:  Ruolo
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.Ruolo
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * An XML Ruolo(@http://www.digitPa.gov.it/protocollo/).
 *
 * This is a complex type.
 */
public class RuoloImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.Ruolo
{
    
    public RuoloImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName DENOMINAZIONE$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Denominazione");
    private static final javax.xml.namespace.QName IDENTIFICATIVO$2 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Identificativo");
    private static final javax.xml.namespace.QName PERSONA$4 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Persona");
    
    
    /**
     * Gets the "Denominazione" element
     */
    public it.gov.digitpa.www.protocollo.Denominazione getDenominazione()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Denominazione target = null;
            target = (it.gov.digitpa.www.protocollo.Denominazione)get_store().find_element_user(DENOMINAZIONE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Denominazione" element
     */
    public void setDenominazione(it.gov.digitpa.www.protocollo.Denominazione denominazione)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Denominazione target = null;
            target = (it.gov.digitpa.www.protocollo.Denominazione)get_store().find_element_user(DENOMINAZIONE$0, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.Denominazione)get_store().add_element_user(DENOMINAZIONE$0);
            }
            target.set(denominazione);
        }
    }
    
    /**
     * Appends and returns a new empty "Denominazione" element
     */
    public it.gov.digitpa.www.protocollo.Denominazione addNewDenominazione()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Denominazione target = null;
            target = (it.gov.digitpa.www.protocollo.Denominazione)get_store().add_element_user(DENOMINAZIONE$0);
            return target;
        }
    }
    
    /**
     * Gets the "Identificativo" element
     */
    public it.gov.digitpa.www.protocollo.Identificativo getIdentificativo()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Identificativo target = null;
            target = (it.gov.digitpa.www.protocollo.Identificativo)get_store().find_element_user(IDENTIFICATIVO$2, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "Identificativo" element
     */
    public boolean isSetIdentificativo()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(IDENTIFICATIVO$2) != 0;
        }
    }
    
    /**
     * Sets the "Identificativo" element
     */
    public void setIdentificativo(it.gov.digitpa.www.protocollo.Identificativo identificativo)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Identificativo target = null;
            target = (it.gov.digitpa.www.protocollo.Identificativo)get_store().find_element_user(IDENTIFICATIVO$2, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.Identificativo)get_store().add_element_user(IDENTIFICATIVO$2);
            }
            target.set(identificativo);
        }
    }
    
    /**
     * Appends and returns a new empty "Identificativo" element
     */
    public it.gov.digitpa.www.protocollo.Identificativo addNewIdentificativo()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Identificativo target = null;
            target = (it.gov.digitpa.www.protocollo.Identificativo)get_store().add_element_user(IDENTIFICATIVO$2);
            return target;
        }
    }
    
    /**
     * Unsets the "Identificativo" element
     */
    public void unsetIdentificativo()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(IDENTIFICATIVO$2, 0);
        }
    }
    
    /**
     * Gets the "Persona" element
     */
    public it.gov.digitpa.www.protocollo.Persona getPersona()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Persona target = null;
            target = (it.gov.digitpa.www.protocollo.Persona)get_store().find_element_user(PERSONA$4, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "Persona" element
     */
    public boolean isSetPersona()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(PERSONA$4) != 0;
        }
    }
    
    /**
     * Sets the "Persona" element
     */
    public void setPersona(it.gov.digitpa.www.protocollo.Persona persona)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Persona target = null;
            target = (it.gov.digitpa.www.protocollo.Persona)get_store().find_element_user(PERSONA$4, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.Persona)get_store().add_element_user(PERSONA$4);
            }
            target.set(persona);
        }
    }
    
    /**
     * Appends and returns a new empty "Persona" element
     */
    public it.gov.digitpa.www.protocollo.Persona addNewPersona()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Persona target = null;
            target = (it.gov.digitpa.www.protocollo.Persona)get_store().add_element_user(PERSONA$4);
            return target;
        }
    }
    
    /**
     * Unsets the "Persona" element
     */
    public void unsetPersona()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(PERSONA$4, 0);
        }
    }
}
