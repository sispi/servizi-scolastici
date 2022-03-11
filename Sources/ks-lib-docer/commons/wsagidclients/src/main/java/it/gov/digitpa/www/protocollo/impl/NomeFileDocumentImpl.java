/*
 * An XML document type.
 * Localname: NomeFile
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.NomeFileDocument
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * A document containing one NomeFile(@http://www.digitPa.gov.it/protocollo/) element.
 *
 * This is a complex type.
 */
public class NomeFileDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.NomeFileDocument
{
    
    public NomeFileDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName NOMEFILE$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "NomeFile");
    
    
    /**
     * Gets the "NomeFile" element
     */
    public it.gov.digitpa.www.protocollo.NomeFile getNomeFile()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.NomeFile target = null;
            target = (it.gov.digitpa.www.protocollo.NomeFile)get_store().find_element_user(NOMEFILE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "NomeFile" element
     */
    public void setNomeFile(it.gov.digitpa.www.protocollo.NomeFile nomeFile)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.NomeFile target = null;
            target = (it.gov.digitpa.www.protocollo.NomeFile)get_store().find_element_user(NOMEFILE$0, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.NomeFile)get_store().add_element_user(NOMEFILE$0);
            }
            target.set(nomeFile);
        }
    }
    
    /**
     * Appends and returns a new empty "NomeFile" element
     */
    public it.gov.digitpa.www.protocollo.NomeFile addNewNomeFile()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.NomeFile target = null;
            target = (it.gov.digitpa.www.protocollo.NomeFile)get_store().add_element_user(NOMEFILE$0);
            return target;
        }
    }
}
