/*
 * An XML document type.
 * Localname: Impronta
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.ImprontaDocument
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * A document containing one Impronta(@http://www.digitPa.gov.it/protocollo/) element.
 *
 * This is a complex type.
 */
public class ImprontaDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.ImprontaDocument
{
    
    public ImprontaDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName IMPRONTA$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Impronta");
    
    
    /**
     * Gets the "Impronta" element
     */
    public it.gov.digitpa.www.protocollo.Impronta getImpronta()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Impronta target = null;
            target = (it.gov.digitpa.www.protocollo.Impronta)get_store().find_element_user(IMPRONTA$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Impronta" element
     */
    public void setImpronta(it.gov.digitpa.www.protocollo.Impronta impronta)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Impronta target = null;
            target = (it.gov.digitpa.www.protocollo.Impronta)get_store().find_element_user(IMPRONTA$0, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.Impronta)get_store().add_element_user(IMPRONTA$0);
            }
            target.set(impronta);
        }
    }
    
    /**
     * Appends and returns a new empty "Impronta" element
     */
    public it.gov.digitpa.www.protocollo.Impronta addNewImpronta()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Impronta target = null;
            target = (it.gov.digitpa.www.protocollo.Impronta)get_store().add_element_user(IMPRONTA$0);
            return target;
        }
    }
}
