/*
 * An XML document type.
 * Localname: Mittente
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.MittenteDocument
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * A document containing one Mittente(@http://www.digitPa.gov.it/protocollo/) element.
 *
 * This is a complex type.
 */
public class MittenteDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.MittenteDocument
{
    
    public MittenteDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName MITTENTE$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Mittente");
    
    
    /**
     * Gets the "Mittente" element
     */
    public it.gov.digitpa.www.protocollo.Mittente getMittente()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Mittente target = null;
            target = (it.gov.digitpa.www.protocollo.Mittente)get_store().find_element_user(MITTENTE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Mittente" element
     */
    public void setMittente(it.gov.digitpa.www.protocollo.Mittente mittente)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Mittente target = null;
            target = (it.gov.digitpa.www.protocollo.Mittente)get_store().find_element_user(MITTENTE$0, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.Mittente)get_store().add_element_user(MITTENTE$0);
            }
            target.set(mittente);
        }
    }
    
    /**
     * Appends and returns a new empty "Mittente" element
     */
    public it.gov.digitpa.www.protocollo.Mittente addNewMittente()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Mittente target = null;
            target = (it.gov.digitpa.www.protocollo.Mittente)get_store().add_element_user(MITTENTE$0);
            return target;
        }
    }
}
