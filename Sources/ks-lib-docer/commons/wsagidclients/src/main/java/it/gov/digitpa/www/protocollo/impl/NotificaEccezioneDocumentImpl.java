/*
 * An XML document type.
 * Localname: NotificaEccezione
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.NotificaEccezioneDocument
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * A document containing one NotificaEccezione(@http://www.digitPa.gov.it/protocollo/) element.
 *
 * This is a complex type.
 */
public class NotificaEccezioneDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.NotificaEccezioneDocument
{
    
    public NotificaEccezioneDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName NOTIFICAECCEZIONE$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "NotificaEccezione");
    
    
    /**
     * Gets the "NotificaEccezione" element
     */
    public it.gov.digitpa.www.protocollo.NotificaEccezione getNotificaEccezione()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.NotificaEccezione target = null;
            target = (it.gov.digitpa.www.protocollo.NotificaEccezione)get_store().find_element_user(NOTIFICAECCEZIONE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "NotificaEccezione" element
     */
    public void setNotificaEccezione(it.gov.digitpa.www.protocollo.NotificaEccezione notificaEccezione)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.NotificaEccezione target = null;
            target = (it.gov.digitpa.www.protocollo.NotificaEccezione)get_store().find_element_user(NOTIFICAECCEZIONE$0, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.NotificaEccezione)get_store().add_element_user(NOTIFICAECCEZIONE$0);
            }
            target.set(notificaEccezione);
        }
    }
    
    /**
     * Appends and returns a new empty "NotificaEccezione" element
     */
    public it.gov.digitpa.www.protocollo.NotificaEccezione addNewNotificaEccezione()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.NotificaEccezione target = null;
            target = (it.gov.digitpa.www.protocollo.NotificaEccezione)get_store().add_element_user(NOTIFICAECCEZIONE$0);
            return target;
        }
    }
}
