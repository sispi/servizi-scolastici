/*
 * An XML document type.
 * Localname: ConfermaRicezione
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.ConfermaRicezioneDocument
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * A document containing one ConfermaRicezione(@http://www.digitPa.gov.it/protocollo/) element.
 *
 * This is a complex type.
 */
public class ConfermaRicezioneDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.ConfermaRicezioneDocument
{
    
    public ConfermaRicezioneDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName CONFERMARICEZIONE$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "ConfermaRicezione");
    
    
    /**
     * Gets the "ConfermaRicezione" element
     */
    public it.gov.digitpa.www.protocollo.ConfermaRicezione getConfermaRicezione()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.ConfermaRicezione target = null;
            target = (it.gov.digitpa.www.protocollo.ConfermaRicezione)get_store().find_element_user(CONFERMARICEZIONE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "ConfermaRicezione" element
     */
    public void setConfermaRicezione(it.gov.digitpa.www.protocollo.ConfermaRicezione confermaRicezione)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.ConfermaRicezione target = null;
            target = (it.gov.digitpa.www.protocollo.ConfermaRicezione)get_store().find_element_user(CONFERMARICEZIONE$0, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.ConfermaRicezione)get_store().add_element_user(CONFERMARICEZIONE$0);
            }
            target.set(confermaRicezione);
        }
    }
    
    /**
     * Appends and returns a new empty "ConfermaRicezione" element
     */
    public it.gov.digitpa.www.protocollo.ConfermaRicezione addNewConfermaRicezione()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.ConfermaRicezione target = null;
            target = (it.gov.digitpa.www.protocollo.ConfermaRicezione)get_store().add_element_user(CONFERMARICEZIONE$0);
            return target;
        }
    }
}
