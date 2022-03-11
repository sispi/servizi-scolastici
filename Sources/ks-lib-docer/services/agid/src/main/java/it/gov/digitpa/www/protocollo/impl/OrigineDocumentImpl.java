/*
 * An XML document type.
 * Localname: Origine
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.OrigineDocument
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * A document containing one Origine(@http://www.digitPa.gov.it/protocollo/) element.
 *
 * This is a complex type.
 */
public class OrigineDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.OrigineDocument
{
    
    public OrigineDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName ORIGINE$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Origine");
    
    
    /**
     * Gets the "Origine" element
     */
    public it.gov.digitpa.www.protocollo.Origine getOrigine()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Origine target = null;
            target = (it.gov.digitpa.www.protocollo.Origine)get_store().find_element_user(ORIGINE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Origine" element
     */
    public void setOrigine(it.gov.digitpa.www.protocollo.Origine origine)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Origine target = null;
            target = (it.gov.digitpa.www.protocollo.Origine)get_store().find_element_user(ORIGINE$0, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.Origine)get_store().add_element_user(ORIGINE$0);
            }
            target.set(origine);
        }
    }
    
    /**
     * Appends and returns a new empty "Origine" element
     */
    public it.gov.digitpa.www.protocollo.Origine addNewOrigine()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Origine target = null;
            target = (it.gov.digitpa.www.protocollo.Origine)get_store().add_element_user(ORIGINE$0);
            return target;
        }
    }
}
