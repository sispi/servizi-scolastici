/*
 * An XML document type.
 * Localname: DataRegistrazione
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.DataRegistrazioneDocument
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * A document containing one DataRegistrazione(@http://www.digitPa.gov.it/protocollo/) element.
 *
 * This is a complex type.
 */
public class DataRegistrazioneDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.DataRegistrazioneDocument
{
    
    public DataRegistrazioneDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName DATAREGISTRAZIONE$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "DataRegistrazione");
    
    
    /**
     * Gets the "DataRegistrazione" element
     */
    public it.gov.digitpa.www.protocollo.DataRegistrazione getDataRegistrazione()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.DataRegistrazione target = null;
            target = (it.gov.digitpa.www.protocollo.DataRegistrazione)get_store().find_element_user(DATAREGISTRAZIONE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "DataRegistrazione" element
     */
    public void setDataRegistrazione(it.gov.digitpa.www.protocollo.DataRegistrazione dataRegistrazione)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.DataRegistrazione target = null;
            target = (it.gov.digitpa.www.protocollo.DataRegistrazione)get_store().find_element_user(DATAREGISTRAZIONE$0, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.DataRegistrazione)get_store().add_element_user(DATAREGISTRAZIONE$0);
            }
            target.set(dataRegistrazione);
        }
    }
    
    /**
     * Appends and returns a new empty "DataRegistrazione" element
     */
    public it.gov.digitpa.www.protocollo.DataRegistrazione addNewDataRegistrazione()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.DataRegistrazione target = null;
            target = (it.gov.digitpa.www.protocollo.DataRegistrazione)get_store().add_element_user(DATAREGISTRAZIONE$0);
            return target;
        }
    }
}
