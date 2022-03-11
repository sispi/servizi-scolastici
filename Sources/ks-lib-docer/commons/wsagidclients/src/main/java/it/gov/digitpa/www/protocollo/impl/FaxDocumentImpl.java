/*
 * An XML document type.
 * Localname: Fax
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.FaxDocument
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * A document containing one Fax(@http://www.digitPa.gov.it/protocollo/) element.
 *
 * This is a complex type.
 */
public class FaxDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.FaxDocument
{
    
    public FaxDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName FAX$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Fax");
    
    
    /**
     * Gets the "Fax" element
     */
    public it.gov.digitpa.www.protocollo.Fax getFax()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Fax target = null;
            target = (it.gov.digitpa.www.protocollo.Fax)get_store().find_element_user(FAX$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Fax" element
     */
    public void setFax(it.gov.digitpa.www.protocollo.Fax fax)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Fax target = null;
            target = (it.gov.digitpa.www.protocollo.Fax)get_store().find_element_user(FAX$0, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.Fax)get_store().add_element_user(FAX$0);
            }
            target.set(fax);
        }
    }
    
    /**
     * Appends and returns a new empty "Fax" element
     */
    public it.gov.digitpa.www.protocollo.Fax addNewFax()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Fax target = null;
            target = (it.gov.digitpa.www.protocollo.Fax)get_store().add_element_user(FAX$0);
            return target;
        }
    }
}
