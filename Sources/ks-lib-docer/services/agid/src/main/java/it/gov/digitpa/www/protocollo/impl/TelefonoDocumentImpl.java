/*
 * An XML document type.
 * Localname: Telefono
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.TelefonoDocument
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * A document containing one Telefono(@http://www.digitPa.gov.it/protocollo/) element.
 *
 * This is a complex type.
 */
public class TelefonoDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.TelefonoDocument
{
    
    public TelefonoDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName TELEFONO$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Telefono");
    
    
    /**
     * Gets the "Telefono" element
     */
    public it.gov.digitpa.www.protocollo.Telefono getTelefono()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Telefono target = null;
            target = (it.gov.digitpa.www.protocollo.Telefono)get_store().find_element_user(TELEFONO$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Telefono" element
     */
    public void setTelefono(it.gov.digitpa.www.protocollo.Telefono telefono)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Telefono target = null;
            target = (it.gov.digitpa.www.protocollo.Telefono)get_store().find_element_user(TELEFONO$0, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.Telefono)get_store().add_element_user(TELEFONO$0);
            }
            target.set(telefono);
        }
    }
    
    /**
     * Appends and returns a new empty "Telefono" element
     */
    public it.gov.digitpa.www.protocollo.Telefono addNewTelefono()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Telefono target = null;
            target = (it.gov.digitpa.www.protocollo.Telefono)get_store().add_element_user(TELEFONO$0);
            return target;
        }
    }
}
