/*
 * An XML document type.
 * Localname: TipoDocumento
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.TipoDocumentoDocument
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * A document containing one TipoDocumento(@http://www.digitPa.gov.it/protocollo/) element.
 *
 * This is a complex type.
 */
public class TipoDocumentoDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.TipoDocumentoDocument
{
    
    public TipoDocumentoDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName TIPODOCUMENTO$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "TipoDocumento");
    
    
    /**
     * Gets the "TipoDocumento" element
     */
    public it.gov.digitpa.www.protocollo.TipoDocumento getTipoDocumento()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.TipoDocumento target = null;
            target = (it.gov.digitpa.www.protocollo.TipoDocumento)get_store().find_element_user(TIPODOCUMENTO$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "TipoDocumento" element
     */
    public void setTipoDocumento(it.gov.digitpa.www.protocollo.TipoDocumento tipoDocumento)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.TipoDocumento target = null;
            target = (it.gov.digitpa.www.protocollo.TipoDocumento)get_store().find_element_user(TIPODOCUMENTO$0, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.TipoDocumento)get_store().add_element_user(TIPODOCUMENTO$0);
            }
            target.set(tipoDocumento);
        }
    }
    
    /**
     * Appends and returns a new empty "TipoDocumento" element
     */
    public it.gov.digitpa.www.protocollo.TipoDocumento addNewTipoDocumento()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.TipoDocumento target = null;
            target = (it.gov.digitpa.www.protocollo.TipoDocumento)get_store().add_element_user(TIPODOCUMENTO$0);
            return target;
        }
    }
}
