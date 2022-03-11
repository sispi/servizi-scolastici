/*
 * An XML document type.
 * Localname: TitoloDocumento
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.TitoloDocumentoDocument
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * A document containing one TitoloDocumento(@http://www.digitPa.gov.it/protocollo/) element.
 *
 * This is a complex type.
 */
public class TitoloDocumentoDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.TitoloDocumentoDocument
{
    
    public TitoloDocumentoDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName TITOLODOCUMENTO$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "TitoloDocumento");
    
    
    /**
     * Gets the "TitoloDocumento" element
     */
    public it.gov.digitpa.www.protocollo.TitoloDocumento getTitoloDocumento()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.TitoloDocumento target = null;
            target = (it.gov.digitpa.www.protocollo.TitoloDocumento)get_store().find_element_user(TITOLODOCUMENTO$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "TitoloDocumento" element
     */
    public void setTitoloDocumento(it.gov.digitpa.www.protocollo.TitoloDocumento titoloDocumento)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.TitoloDocumento target = null;
            target = (it.gov.digitpa.www.protocollo.TitoloDocumento)get_store().find_element_user(TITOLODOCUMENTO$0, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.TitoloDocumento)get_store().add_element_user(TITOLODOCUMENTO$0);
            }
            target.set(titoloDocumento);
        }
    }
    
    /**
     * Appends and returns a new empty "TitoloDocumento" element
     */
    public it.gov.digitpa.www.protocollo.TitoloDocumento addNewTitoloDocumento()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.TitoloDocumento target = null;
            target = (it.gov.digitpa.www.protocollo.TitoloDocumento)get_store().add_element_user(TITOLODOCUMENTO$0);
            return target;
        }
    }
}
