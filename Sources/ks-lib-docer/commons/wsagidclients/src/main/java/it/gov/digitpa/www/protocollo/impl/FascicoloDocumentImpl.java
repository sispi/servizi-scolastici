/*
 * An XML document type.
 * Localname: Fascicolo
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.FascicoloDocument
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * A document containing one Fascicolo(@http://www.digitPa.gov.it/protocollo/) element.
 *
 * This is a complex type.
 */
public class FascicoloDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.FascicoloDocument
{
    
    public FascicoloDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName FASCICOLO$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Fascicolo");
    
    
    /**
     * Gets the "Fascicolo" element
     */
    public it.gov.digitpa.www.protocollo.Fascicolo getFascicolo()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Fascicolo target = null;
            target = (it.gov.digitpa.www.protocollo.Fascicolo)get_store().find_element_user(FASCICOLO$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Fascicolo" element
     */
    public void setFascicolo(it.gov.digitpa.www.protocollo.Fascicolo fascicolo)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Fascicolo target = null;
            target = (it.gov.digitpa.www.protocollo.Fascicolo)get_store().find_element_user(FASCICOLO$0, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.Fascicolo)get_store().add_element_user(FASCICOLO$0);
            }
            target.set(fascicolo);
        }
    }
    
    /**
     * Appends and returns a new empty "Fascicolo" element
     */
    public it.gov.digitpa.www.protocollo.Fascicolo addNewFascicolo()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Fascicolo target = null;
            target = (it.gov.digitpa.www.protocollo.Fascicolo)get_store().add_element_user(FASCICOLO$0);
            return target;
        }
    }
}
