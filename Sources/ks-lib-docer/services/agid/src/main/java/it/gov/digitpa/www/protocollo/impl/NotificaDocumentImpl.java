/*
 * An XML document type.
 * Localname: Notifica
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.NotificaDocument
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * A document containing one Notifica(@http://www.digitPa.gov.it/protocollo/) element.
 *
 * This is a complex type.
 */
public class NotificaDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.NotificaDocument
{
    
    public NotificaDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName NOTIFICA$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Notifica");
    
    
    /**
     * Gets the "Notifica" element
     */
    public it.gov.digitpa.www.protocollo.NotificaType getNotifica()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.NotificaType target = null;
            target = (it.gov.digitpa.www.protocollo.NotificaType)get_store().find_element_user(NOTIFICA$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Notifica" element
     */
    public void setNotifica(it.gov.digitpa.www.protocollo.NotificaType notifica)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.NotificaType target = null;
            target = (it.gov.digitpa.www.protocollo.NotificaType)get_store().find_element_user(NOTIFICA$0, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.NotificaType)get_store().add_element_user(NOTIFICA$0);
            }
            target.set(notifica);
        }
    }
    
    /**
     * Appends and returns a new empty "Notifica" element
     */
    public it.gov.digitpa.www.protocollo.NotificaType addNewNotifica()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.NotificaType target = null;
            target = (it.gov.digitpa.www.protocollo.NotificaType)get_store().add_element_user(NOTIFICA$0);
            return target;
        }
    }
}
