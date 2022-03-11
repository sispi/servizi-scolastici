/*
 * An XML document type.
 * Localname: MetadatiInterni
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.MetadatiInterniDocument
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * A document containing one MetadatiInterni(@http://www.digitPa.gov.it/protocollo/) element.
 *
 * This is a complex type.
 */
public class MetadatiInterniDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.MetadatiInterniDocument
{
    
    public MetadatiInterniDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName METADATIINTERNI$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "MetadatiInterni");
    
    
    /**
     * Gets the "MetadatiInterni" element
     */
    public it.gov.digitpa.www.protocollo.MetadatiInterni getMetadatiInterni()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.MetadatiInterni target = null;
            target = (it.gov.digitpa.www.protocollo.MetadatiInterni)get_store().find_element_user(METADATIINTERNI$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "MetadatiInterni" element
     */
    public void setMetadatiInterni(it.gov.digitpa.www.protocollo.MetadatiInterni metadatiInterni)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.MetadatiInterni target = null;
            target = (it.gov.digitpa.www.protocollo.MetadatiInterni)get_store().find_element_user(METADATIINTERNI$0, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.MetadatiInterni)get_store().add_element_user(METADATIINTERNI$0);
            }
            target.set(metadatiInterni);
        }
    }
    
    /**
     * Appends and returns a new empty "MetadatiInterni" element
     */
    public it.gov.digitpa.www.protocollo.MetadatiInterni addNewMetadatiInterni()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.MetadatiInterni target = null;
            target = (it.gov.digitpa.www.protocollo.MetadatiInterni)get_store().add_element_user(METADATIINTERNI$0);
            return target;
        }
    }
}
