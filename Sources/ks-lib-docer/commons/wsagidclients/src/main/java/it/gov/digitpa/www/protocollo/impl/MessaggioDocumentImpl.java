/*
 * An XML document type.
 * Localname: Messaggio
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.MessaggioDocument
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * A document containing one Messaggio(@http://www.digitPa.gov.it/protocollo/) element.
 *
 * This is a complex type.
 */
public class MessaggioDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.MessaggioDocument
{
    
    public MessaggioDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName MESSAGGIO$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Messaggio");
    
    
    /**
     * Gets the "Messaggio" element
     */
    public it.gov.digitpa.www.protocollo.Messaggio getMessaggio()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Messaggio target = null;
            target = (it.gov.digitpa.www.protocollo.Messaggio)get_store().find_element_user(MESSAGGIO$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Messaggio" element
     */
    public void setMessaggio(it.gov.digitpa.www.protocollo.Messaggio messaggio)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Messaggio target = null;
            target = (it.gov.digitpa.www.protocollo.Messaggio)get_store().find_element_user(MESSAGGIO$0, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.Messaggio)get_store().add_element_user(MESSAGGIO$0);
            }
            target.set(messaggio);
        }
    }
    
    /**
     * Appends and returns a new empty "Messaggio" element
     */
    public it.gov.digitpa.www.protocollo.Messaggio addNewMessaggio()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Messaggio target = null;
            target = (it.gov.digitpa.www.protocollo.Messaggio)get_store().add_element_user(MESSAGGIO$0);
            return target;
        }
    }
}
