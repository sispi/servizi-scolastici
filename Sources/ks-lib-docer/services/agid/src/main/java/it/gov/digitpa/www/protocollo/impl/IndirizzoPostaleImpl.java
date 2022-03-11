/*
 * XML Type:  IndirizzoPostale
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.IndirizzoPostale
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * An XML IndirizzoPostale(@http://www.digitPa.gov.it/protocollo/).
 *
 * This is a complex type.
 */
public class IndirizzoPostaleImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.IndirizzoPostale
{
    
    public IndirizzoPostaleImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName DENOMINAZIONE$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Denominazione");
    private static final javax.xml.namespace.QName INDIRIZZO$2 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Indirizzo");
    
    
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
     * True if has "Denominazione" element
     */
    public boolean isSetDenominazione()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(DENOMINAZIONE$0) != 0;
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
     * Unsets the "Denominazione" element
     */
    public void unsetDenominazione()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(DENOMINAZIONE$0, 0);
        }
    }
    
    /**
     * Gets the "Indirizzo" element
     */
    public it.gov.digitpa.www.protocollo.Indirizzo getIndirizzo()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Indirizzo target = null;
            target = (it.gov.digitpa.www.protocollo.Indirizzo)get_store().find_element_user(INDIRIZZO$2, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "Indirizzo" element
     */
    public boolean isSetIndirizzo()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(INDIRIZZO$2) != 0;
        }
    }
    
    /**
     * Sets the "Indirizzo" element
     */
    public void setIndirizzo(it.gov.digitpa.www.protocollo.Indirizzo indirizzo)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Indirizzo target = null;
            target = (it.gov.digitpa.www.protocollo.Indirizzo)get_store().find_element_user(INDIRIZZO$2, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.Indirizzo)get_store().add_element_user(INDIRIZZO$2);
            }
            target.set(indirizzo);
        }
    }
    
    /**
     * Appends and returns a new empty "Indirizzo" element
     */
    public it.gov.digitpa.www.protocollo.Indirizzo addNewIndirizzo()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Indirizzo target = null;
            target = (it.gov.digitpa.www.protocollo.Indirizzo)get_store().add_element_user(INDIRIZZO$2);
            return target;
        }
    }
    
    /**
     * Unsets the "Indirizzo" element
     */
    public void unsetIndirizzo()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(INDIRIZZO$2, 0);
        }
    }
}
