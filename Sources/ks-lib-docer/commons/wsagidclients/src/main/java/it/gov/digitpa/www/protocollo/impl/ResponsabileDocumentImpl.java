/*
 * An XML document type.
 * Localname: Responsabile
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.ResponsabileDocument
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * A document containing one Responsabile(@http://www.digitPa.gov.it/protocollo/) element.
 *
 * This is a complex type.
 */
public class ResponsabileDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.ResponsabileDocument
{
    
    public ResponsabileDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName RESPONSABILE$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Responsabile");
    
    
    /**
     * Gets the "Responsabile" element
     */
    public it.gov.digitpa.www.protocollo.Responsabile getResponsabile()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Responsabile target = null;
            target = (it.gov.digitpa.www.protocollo.Responsabile)get_store().find_element_user(RESPONSABILE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Responsabile" element
     */
    public void setResponsabile(it.gov.digitpa.www.protocollo.Responsabile responsabile)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Responsabile target = null;
            target = (it.gov.digitpa.www.protocollo.Responsabile)get_store().find_element_user(RESPONSABILE$0, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.Responsabile)get_store().add_element_user(RESPONSABILE$0);
            }
            target.set(responsabile);
        }
    }
    
    /**
     * Appends and returns a new empty "Responsabile" element
     */
    public it.gov.digitpa.www.protocollo.Responsabile addNewResponsabile()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Responsabile target = null;
            target = (it.gov.digitpa.www.protocollo.Responsabile)get_store().add_element_user(RESPONSABILE$0);
            return target;
        }
    }
}
