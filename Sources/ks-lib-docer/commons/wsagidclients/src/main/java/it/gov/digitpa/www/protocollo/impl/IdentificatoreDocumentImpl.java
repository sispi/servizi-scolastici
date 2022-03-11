/*
 * An XML document type.
 * Localname: Identificatore
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.IdentificatoreDocument
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * A document containing one Identificatore(@http://www.digitPa.gov.it/protocollo/) element.
 *
 * This is a complex type.
 */
public class IdentificatoreDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.IdentificatoreDocument
{
    
    public IdentificatoreDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName IDENTIFICATORE$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Identificatore");
    
    
    /**
     * Gets the "Identificatore" element
     */
    public it.gov.digitpa.www.protocollo.Identificatore getIdentificatore()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Identificatore target = null;
            target = (it.gov.digitpa.www.protocollo.Identificatore)get_store().find_element_user(IDENTIFICATORE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Identificatore" element
     */
    public void setIdentificatore(it.gov.digitpa.www.protocollo.Identificatore identificatore)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Identificatore target = null;
            target = (it.gov.digitpa.www.protocollo.Identificatore)get_store().find_element_user(IDENTIFICATORE$0, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.Identificatore)get_store().add_element_user(IDENTIFICATORE$0);
            }
            target.set(identificatore);
        }
    }
    
    /**
     * Appends and returns a new empty "Identificatore" element
     */
    public it.gov.digitpa.www.protocollo.Identificatore addNewIdentificatore()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Identificatore target = null;
            target = (it.gov.digitpa.www.protocollo.Identificatore)get_store().add_element_user(IDENTIFICATORE$0);
            return target;
        }
    }
}
