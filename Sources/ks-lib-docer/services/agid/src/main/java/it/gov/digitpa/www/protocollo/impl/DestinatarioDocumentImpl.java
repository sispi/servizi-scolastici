/*
 * An XML document type.
 * Localname: Destinatario
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.DestinatarioDocument
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * A document containing one Destinatario(@http://www.digitPa.gov.it/protocollo/) element.
 *
 * This is a complex type.
 */
public class DestinatarioDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.DestinatarioDocument
{
    
    public DestinatarioDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName DESTINATARIO$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Destinatario");
    
    
    /**
     * Gets the "Destinatario" element
     */
    public it.gov.digitpa.www.protocollo.Destinatario getDestinatario()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Destinatario target = null;
            target = (it.gov.digitpa.www.protocollo.Destinatario)get_store().find_element_user(DESTINATARIO$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Destinatario" element
     */
    public void setDestinatario(it.gov.digitpa.www.protocollo.Destinatario destinatario)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Destinatario target = null;
            target = (it.gov.digitpa.www.protocollo.Destinatario)get_store().find_element_user(DESTINATARIO$0, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.Destinatario)get_store().add_element_user(DESTINATARIO$0);
            }
            target.set(destinatario);
        }
    }
    
    /**
     * Appends and returns a new empty "Destinatario" element
     */
    public it.gov.digitpa.www.protocollo.Destinatario addNewDestinatario()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Destinatario target = null;
            target = (it.gov.digitpa.www.protocollo.Destinatario)get_store().add_element_user(DESTINATARIO$0);
            return target;
        }
    }
}
