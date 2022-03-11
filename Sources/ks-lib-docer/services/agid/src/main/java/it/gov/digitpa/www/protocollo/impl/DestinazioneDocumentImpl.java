/*
 * An XML document type.
 * Localname: Destinazione
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.DestinazioneDocument
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * A document containing one Destinazione(@http://www.digitPa.gov.it/protocollo/) element.
 *
 * This is a complex type.
 */
public class DestinazioneDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.DestinazioneDocument
{
    
    public DestinazioneDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName DESTINAZIONE$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Destinazione");
    
    
    /**
     * Gets the "Destinazione" element
     */
    public it.gov.digitpa.www.protocollo.Destinazione getDestinazione()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Destinazione target = null;
            target = (it.gov.digitpa.www.protocollo.Destinazione)get_store().find_element_user(DESTINAZIONE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Destinazione" element
     */
    public void setDestinazione(it.gov.digitpa.www.protocollo.Destinazione destinazione)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Destinazione target = null;
            target = (it.gov.digitpa.www.protocollo.Destinazione)get_store().find_element_user(DESTINAZIONE$0, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.Destinazione)get_store().add_element_user(DESTINAZIONE$0);
            }
            target.set(destinazione);
        }
    }
    
    /**
     * Appends and returns a new empty "Destinazione" element
     */
    public it.gov.digitpa.www.protocollo.Destinazione addNewDestinazione()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Destinazione target = null;
            target = (it.gov.digitpa.www.protocollo.Destinazione)get_store().add_element_user(DESTINAZIONE$0);
            return target;
        }
    }
}
