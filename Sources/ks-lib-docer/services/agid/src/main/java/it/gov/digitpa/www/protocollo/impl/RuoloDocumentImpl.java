/*
 * An XML document type.
 * Localname: Ruolo
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.RuoloDocument
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * A document containing one Ruolo(@http://www.digitPa.gov.it/protocollo/) element.
 *
 * This is a complex type.
 */
public class RuoloDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.RuoloDocument
{
    
    public RuoloDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName RUOLO$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Ruolo");
    
    
    /**
     * Gets the "Ruolo" element
     */
    public it.gov.digitpa.www.protocollo.Ruolo getRuolo()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Ruolo target = null;
            target = (it.gov.digitpa.www.protocollo.Ruolo)get_store().find_element_user(RUOLO$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Ruolo" element
     */
    public void setRuolo(it.gov.digitpa.www.protocollo.Ruolo ruolo)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Ruolo target = null;
            target = (it.gov.digitpa.www.protocollo.Ruolo)get_store().find_element_user(RUOLO$0, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.Ruolo)get_store().add_element_user(RUOLO$0);
            }
            target.set(ruolo);
        }
    }
    
    /**
     * Appends and returns a new empty "Ruolo" element
     */
    public it.gov.digitpa.www.protocollo.Ruolo addNewRuolo()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Ruolo target = null;
            target = (it.gov.digitpa.www.protocollo.Ruolo)get_store().add_element_user(RUOLO$0);
            return target;
        }
    }
}
