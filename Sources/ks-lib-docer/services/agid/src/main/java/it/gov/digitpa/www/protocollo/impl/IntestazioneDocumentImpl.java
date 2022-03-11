/*
 * An XML document type.
 * Localname: Intestazione
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.IntestazioneDocument
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * A document containing one Intestazione(@http://www.digitPa.gov.it/protocollo/) element.
 *
 * This is a complex type.
 */
public class IntestazioneDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.IntestazioneDocument
{
    
    public IntestazioneDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName INTESTAZIONE$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Intestazione");
    
    
    /**
     * Gets the "Intestazione" element
     */
    public it.gov.digitpa.www.protocollo.Intestazione getIntestazione()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Intestazione target = null;
            target = (it.gov.digitpa.www.protocollo.Intestazione)get_store().find_element_user(INTESTAZIONE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Intestazione" element
     */
    public void setIntestazione(it.gov.digitpa.www.protocollo.Intestazione intestazione)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Intestazione target = null;
            target = (it.gov.digitpa.www.protocollo.Intestazione)get_store().find_element_user(INTESTAZIONE$0, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.Intestazione)get_store().add_element_user(INTESTAZIONE$0);
            }
            target.set(intestazione);
        }
    }
    
    /**
     * Appends and returns a new empty "Intestazione" element
     */
    public it.gov.digitpa.www.protocollo.Intestazione addNewIntestazione()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Intestazione target = null;
            target = (it.gov.digitpa.www.protocollo.Intestazione)get_store().add_element_user(INTESTAZIONE$0);
            return target;
        }
    }
}
