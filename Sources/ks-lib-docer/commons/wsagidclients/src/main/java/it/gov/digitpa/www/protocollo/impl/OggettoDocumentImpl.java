/*
 * An XML document type.
 * Localname: Oggetto
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.OggettoDocument
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * A document containing one Oggetto(@http://www.digitPa.gov.it/protocollo/) element.
 *
 * This is a complex type.
 */
public class OggettoDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.OggettoDocument
{
    
    public OggettoDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName OGGETTO$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Oggetto");
    
    
    /**
     * Gets the "Oggetto" element
     */
    public it.gov.digitpa.www.protocollo.Oggetto getOggetto()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Oggetto target = null;
            target = (it.gov.digitpa.www.protocollo.Oggetto)get_store().find_element_user(OGGETTO$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Oggetto" element
     */
    public void setOggetto(it.gov.digitpa.www.protocollo.Oggetto oggetto)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Oggetto target = null;
            target = (it.gov.digitpa.www.protocollo.Oggetto)get_store().find_element_user(OGGETTO$0, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.Oggetto)get_store().add_element_user(OGGETTO$0);
            }
            target.set(oggetto);
        }
    }
    
    /**
     * Appends and returns a new empty "Oggetto" element
     */
    public it.gov.digitpa.www.protocollo.Oggetto addNewOggetto()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Oggetto target = null;
            target = (it.gov.digitpa.www.protocollo.Oggetto)get_store().add_element_user(OGGETTO$0);
            return target;
        }
    }
}
