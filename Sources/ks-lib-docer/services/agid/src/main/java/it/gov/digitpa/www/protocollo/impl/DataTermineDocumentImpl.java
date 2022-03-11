/*
 * An XML document type.
 * Localname: DataTermine
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.DataTermineDocument
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * A document containing one DataTermine(@http://www.digitPa.gov.it/protocollo/) element.
 *
 * This is a complex type.
 */
public class DataTermineDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.DataTermineDocument
{
    
    public DataTermineDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName DATATERMINE$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "DataTermine");
    
    
    /**
     * Gets the "DataTermine" element
     */
    public it.gov.digitpa.www.protocollo.DataTermine getDataTermine()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.DataTermine target = null;
            target = (it.gov.digitpa.www.protocollo.DataTermine)get_store().find_element_user(DATATERMINE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "DataTermine" element
     */
    public void setDataTermine(it.gov.digitpa.www.protocollo.DataTermine dataTermine)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.DataTermine target = null;
            target = (it.gov.digitpa.www.protocollo.DataTermine)get_store().find_element_user(DATATERMINE$0, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.DataTermine)get_store().add_element_user(DATATERMINE$0);
            }
            target.set(dataTermine);
        }
    }
    
    /**
     * Appends and returns a new empty "DataTermine" element
     */
    public it.gov.digitpa.www.protocollo.DataTermine addNewDataTermine()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.DataTermine target = null;
            target = (it.gov.digitpa.www.protocollo.DataTermine)get_store().add_element_user(DATATERMINE$0);
            return target;
        }
    }
}
