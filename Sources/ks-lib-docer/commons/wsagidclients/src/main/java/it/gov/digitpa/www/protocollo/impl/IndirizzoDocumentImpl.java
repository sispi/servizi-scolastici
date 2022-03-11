/*
 * An XML document type.
 * Localname: Indirizzo
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.IndirizzoDocument
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * A document containing one Indirizzo(@http://www.digitPa.gov.it/protocollo/) element.
 *
 * This is a complex type.
 */
public class IndirizzoDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.IndirizzoDocument
{
    
    public IndirizzoDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName INDIRIZZO$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Indirizzo");
    
    
    /**
     * Gets the "Indirizzo" element
     */
    public it.gov.digitpa.www.protocollo.Indirizzo getIndirizzo()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Indirizzo target = null;
            target = (it.gov.digitpa.www.protocollo.Indirizzo)get_store().find_element_user(INDIRIZZO$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Indirizzo" element
     */
    public void setIndirizzo(it.gov.digitpa.www.protocollo.Indirizzo indirizzo)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Indirizzo target = null;
            target = (it.gov.digitpa.www.protocollo.Indirizzo)get_store().find_element_user(INDIRIZZO$0, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.Indirizzo)get_store().add_element_user(INDIRIZZO$0);
            }
            target.set(indirizzo);
        }
    }
    
    /**
     * Appends and returns a new empty "Indirizzo" element
     */
    public it.gov.digitpa.www.protocollo.Indirizzo addNewIndirizzo()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Indirizzo target = null;
            target = (it.gov.digitpa.www.protocollo.Indirizzo)get_store().add_element_user(INDIRIZZO$0);
            return target;
        }
    }
}
