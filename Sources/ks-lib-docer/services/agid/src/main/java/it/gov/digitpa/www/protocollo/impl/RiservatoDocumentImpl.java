/*
 * An XML document type.
 * Localname: Riservato
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.RiservatoDocument
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * A document containing one Riservato(@http://www.digitPa.gov.it/protocollo/) element.
 *
 * This is a complex type.
 */
public class RiservatoDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.RiservatoDocument
{
    
    public RiservatoDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName RISERVATO$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Riservato");
    
    
    /**
     * Gets the "Riservato" element
     */
    public it.gov.digitpa.www.protocollo.Riservato getRiservato()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Riservato target = null;
            target = (it.gov.digitpa.www.protocollo.Riservato)get_store().find_element_user(RISERVATO$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Riservato" element
     */
    public void setRiservato(it.gov.digitpa.www.protocollo.Riservato riservato)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Riservato target = null;
            target = (it.gov.digitpa.www.protocollo.Riservato)get_store().find_element_user(RISERVATO$0, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.Riservato)get_store().add_element_user(RISERVATO$0);
            }
            target.set(riservato);
        }
    }
    
    /**
     * Appends and returns a new empty "Riservato" element
     */
    public it.gov.digitpa.www.protocollo.Riservato addNewRiservato()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Riservato target = null;
            target = (it.gov.digitpa.www.protocollo.Riservato)get_store().add_element_user(RISERVATO$0);
            return target;
        }
    }
}
