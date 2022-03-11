/*
 * An XML document type.
 * Localname: PerConoscenza
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.PerConoscenzaDocument
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * A document containing one PerConoscenza(@http://www.digitPa.gov.it/protocollo/) element.
 *
 * This is a complex type.
 */
public class PerConoscenzaDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.PerConoscenzaDocument
{
    
    public PerConoscenzaDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName PERCONOSCENZA$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "PerConoscenza");
    
    
    /**
     * Gets the "PerConoscenza" element
     */
    public it.gov.digitpa.www.protocollo.PerConoscenza getPerConoscenza()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.PerConoscenza target = null;
            target = (it.gov.digitpa.www.protocollo.PerConoscenza)get_store().find_element_user(PERCONOSCENZA$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "PerConoscenza" element
     */
    public void setPerConoscenza(it.gov.digitpa.www.protocollo.PerConoscenza perConoscenza)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.PerConoscenza target = null;
            target = (it.gov.digitpa.www.protocollo.PerConoscenza)get_store().find_element_user(PERCONOSCENZA$0, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.PerConoscenza)get_store().add_element_user(PERCONOSCENZA$0);
            }
            target.set(perConoscenza);
        }
    }
    
    /**
     * Appends and returns a new empty "PerConoscenza" element
     */
    public it.gov.digitpa.www.protocollo.PerConoscenza addNewPerConoscenza()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.PerConoscenza target = null;
            target = (it.gov.digitpa.www.protocollo.PerConoscenza)get_store().add_element_user(PERCONOSCENZA$0);
            return target;
        }
    }
}
