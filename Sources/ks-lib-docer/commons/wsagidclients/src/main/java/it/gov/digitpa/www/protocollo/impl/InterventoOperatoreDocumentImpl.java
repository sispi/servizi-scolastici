/*
 * An XML document type.
 * Localname: InterventoOperatore
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.InterventoOperatoreDocument
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * A document containing one InterventoOperatore(@http://www.digitPa.gov.it/protocollo/) element.
 *
 * This is a complex type.
 */
public class InterventoOperatoreDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.InterventoOperatoreDocument
{
    
    public InterventoOperatoreDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName INTERVENTOOPERATORE$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "InterventoOperatore");
    
    
    /**
     * Gets the "InterventoOperatore" element
     */
    public it.gov.digitpa.www.protocollo.InterventoOperatore getInterventoOperatore()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.InterventoOperatore target = null;
            target = (it.gov.digitpa.www.protocollo.InterventoOperatore)get_store().find_element_user(INTERVENTOOPERATORE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "InterventoOperatore" element
     */
    public void setInterventoOperatore(it.gov.digitpa.www.protocollo.InterventoOperatore interventoOperatore)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.InterventoOperatore target = null;
            target = (it.gov.digitpa.www.protocollo.InterventoOperatore)get_store().find_element_user(INTERVENTOOPERATORE$0, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.InterventoOperatore)get_store().add_element_user(INTERVENTOOPERATORE$0);
            }
            target.set(interventoOperatore);
        }
    }
    
    /**
     * Appends and returns a new empty "InterventoOperatore" element
     */
    public it.gov.digitpa.www.protocollo.InterventoOperatore addNewInterventoOperatore()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.InterventoOperatore target = null;
            target = (it.gov.digitpa.www.protocollo.InterventoOperatore)get_store().add_element_user(INTERVENTOOPERATORE$0);
            return target;
        }
    }
}
