/*
 * An XML document type.
 * Localname: Documento
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.DocumentoDocument
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * A document containing one Documento(@http://www.digitPa.gov.it/protocollo/) element.
 *
 * This is a complex type.
 */
public class DocumentoDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.DocumentoDocument
{
    
    public DocumentoDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName DOCUMENTO$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Documento");
    
    
    /**
     * Gets the "Documento" element
     */
    public it.gov.digitpa.www.protocollo.Documento getDocumento()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Documento target = null;
            target = (it.gov.digitpa.www.protocollo.Documento)get_store().find_element_user(DOCUMENTO$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Documento" element
     */
    public void setDocumento(it.gov.digitpa.www.protocollo.Documento documento)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Documento target = null;
            target = (it.gov.digitpa.www.protocollo.Documento)get_store().find_element_user(DOCUMENTO$0, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.Documento)get_store().add_element_user(DOCUMENTO$0);
            }
            target.set(documento);
        }
    }
    
    /**
     * Appends and returns a new empty "Documento" element
     */
    public it.gov.digitpa.www.protocollo.Documento addNewDocumento()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Documento target = null;
            target = (it.gov.digitpa.www.protocollo.Documento)get_store().add_element_user(DOCUMENTO$0);
            return target;
        }
    }
}
