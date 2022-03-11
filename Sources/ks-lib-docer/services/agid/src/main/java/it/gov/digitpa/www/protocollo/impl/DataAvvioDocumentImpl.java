/*
 * An XML document type.
 * Localname: DataAvvio
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.DataAvvioDocument
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * A document containing one DataAvvio(@http://www.digitPa.gov.it/protocollo/) element.
 *
 * This is a complex type.
 */
public class DataAvvioDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.DataAvvioDocument
{
    
    public DataAvvioDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName DATAAVVIO$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "DataAvvio");
    
    
    /**
     * Gets the "DataAvvio" element
     */
    public it.gov.digitpa.www.protocollo.DataAvvio getDataAvvio()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.DataAvvio target = null;
            target = (it.gov.digitpa.www.protocollo.DataAvvio)get_store().find_element_user(DATAAVVIO$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "DataAvvio" element
     */
    public void setDataAvvio(it.gov.digitpa.www.protocollo.DataAvvio dataAvvio)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.DataAvvio target = null;
            target = (it.gov.digitpa.www.protocollo.DataAvvio)get_store().find_element_user(DATAAVVIO$0, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.DataAvvio)get_store().add_element_user(DATAAVVIO$0);
            }
            target.set(dataAvvio);
        }
    }
    
    /**
     * Appends and returns a new empty "DataAvvio" element
     */
    public it.gov.digitpa.www.protocollo.DataAvvio addNewDataAvvio()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.DataAvvio target = null;
            target = (it.gov.digitpa.www.protocollo.DataAvvio)get_store().add_element_user(DATAAVVIO$0);
            return target;
        }
    }
}
