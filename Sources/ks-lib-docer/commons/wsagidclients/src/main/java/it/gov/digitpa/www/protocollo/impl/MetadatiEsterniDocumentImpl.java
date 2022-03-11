/*
 * An XML document type.
 * Localname: MetadatiEsterni
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.MetadatiEsterniDocument
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * A document containing one MetadatiEsterni(@http://www.digitPa.gov.it/protocollo/) element.
 *
 * This is a complex type.
 */
public class MetadatiEsterniDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.MetadatiEsterniDocument
{
    
    public MetadatiEsterniDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName METADATIESTERNI$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "MetadatiEsterni");
    
    
    /**
     * Gets the "MetadatiEsterni" element
     */
    public it.gov.digitpa.www.protocollo.MetadatiEsterni getMetadatiEsterni()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.MetadatiEsterni target = null;
            target = (it.gov.digitpa.www.protocollo.MetadatiEsterni)get_store().find_element_user(METADATIESTERNI$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "MetadatiEsterni" element
     */
    public void setMetadatiEsterni(it.gov.digitpa.www.protocollo.MetadatiEsterni metadatiEsterni)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.MetadatiEsterni target = null;
            target = (it.gov.digitpa.www.protocollo.MetadatiEsterni)get_store().find_element_user(METADATIESTERNI$0, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.MetadatiEsterni)get_store().add_element_user(METADATIESTERNI$0);
            }
            target.set(metadatiEsterni);
        }
    }
    
    /**
     * Appends and returns a new empty "MetadatiEsterni" element
     */
    public it.gov.digitpa.www.protocollo.MetadatiEsterni addNewMetadatiEsterni()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.MetadatiEsterni target = null;
            target = (it.gov.digitpa.www.protocollo.MetadatiEsterni)get_store().add_element_user(METADATIESTERNI$0);
            return target;
        }
    }
}
