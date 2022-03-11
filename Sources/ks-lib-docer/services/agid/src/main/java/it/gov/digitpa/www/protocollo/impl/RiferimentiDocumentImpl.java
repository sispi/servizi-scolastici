/*
 * An XML document type.
 * Localname: Riferimenti
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.RiferimentiDocument
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * A document containing one Riferimenti(@http://www.digitPa.gov.it/protocollo/) element.
 *
 * This is a complex type.
 */
public class RiferimentiDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.RiferimentiDocument
{
    
    public RiferimentiDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName RIFERIMENTI$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Riferimenti");
    
    
    /**
     * Gets the "Riferimenti" element
     */
    public it.gov.digitpa.www.protocollo.Riferimenti getRiferimenti()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Riferimenti target = null;
            target = (it.gov.digitpa.www.protocollo.Riferimenti)get_store().find_element_user(RIFERIMENTI$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Riferimenti" element
     */
    public void setRiferimenti(it.gov.digitpa.www.protocollo.Riferimenti riferimenti)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Riferimenti target = null;
            target = (it.gov.digitpa.www.protocollo.Riferimenti)get_store().find_element_user(RIFERIMENTI$0, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.Riferimenti)get_store().add_element_user(RIFERIMENTI$0);
            }
            target.set(riferimenti);
        }
    }
    
    /**
     * Appends and returns a new empty "Riferimenti" element
     */
    public it.gov.digitpa.www.protocollo.Riferimenti addNewRiferimenti()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Riferimenti target = null;
            target = (it.gov.digitpa.www.protocollo.Riferimenti)get_store().add_element_user(RIFERIMENTI$0);
            return target;
        }
    }
}
