/*
 * An XML document type.
 * Localname: Nazione
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.NazioneDocument
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * A document containing one Nazione(@http://www.digitPa.gov.it/protocollo/) element.
 *
 * This is a complex type.
 */
public class NazioneDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.NazioneDocument
{
    
    public NazioneDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName NAZIONE$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Nazione");
    
    
    /**
     * Gets the "Nazione" element
     */
    public it.gov.digitpa.www.protocollo.Nazione getNazione()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Nazione target = null;
            target = (it.gov.digitpa.www.protocollo.Nazione)get_store().find_element_user(NAZIONE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Nazione" element
     */
    public void setNazione(it.gov.digitpa.www.protocollo.Nazione nazione)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Nazione target = null;
            target = (it.gov.digitpa.www.protocollo.Nazione)get_store().find_element_user(NAZIONE$0, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.Nazione)get_store().add_element_user(NAZIONE$0);
            }
            target.set(nazione);
        }
    }
    
    /**
     * Appends and returns a new empty "Nazione" element
     */
    public it.gov.digitpa.www.protocollo.Nazione addNewNazione()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Nazione target = null;
            target = (it.gov.digitpa.www.protocollo.Nazione)get_store().add_element_user(NAZIONE$0);
            return target;
        }
    }
}
