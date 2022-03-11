/*
 * An XML document type.
 * Localname: CAP
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.CAPDocument
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * A document containing one CAP(@http://www.digitPa.gov.it/protocollo/) element.
 *
 * This is a complex type.
 */
public class CAPDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.CAPDocument
{
    
    public CAPDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName CAP$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "CAP");
    
    
    /**
     * Gets the "CAP" element
     */
    public it.gov.digitpa.www.protocollo.CAP getCAP()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.CAP target = null;
            target = (it.gov.digitpa.www.protocollo.CAP)get_store().find_element_user(CAP$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "CAP" element
     */
    public void setCAP(it.gov.digitpa.www.protocollo.CAP cap)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.CAP target = null;
            target = (it.gov.digitpa.www.protocollo.CAP)get_store().find_element_user(CAP$0, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.CAP)get_store().add_element_user(CAP$0);
            }
            target.set(cap);
        }
    }
    
    /**
     * Appends and returns a new empty "CAP" element
     */
    public it.gov.digitpa.www.protocollo.CAP addNewCAP()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.CAP target = null;
            target = (it.gov.digitpa.www.protocollo.CAP)get_store().add_element_user(CAP$0);
            return target;
        }
    }
}
