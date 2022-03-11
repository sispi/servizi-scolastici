/*
 * An XML document type.
 * Localname: CodiceAmministrazione
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.CodiceAmministrazioneDocument
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * A document containing one CodiceAmministrazione(@http://www.digitPa.gov.it/protocollo/) element.
 *
 * This is a complex type.
 */
public class CodiceAmministrazioneDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.CodiceAmministrazioneDocument
{
    
    public CodiceAmministrazioneDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName CODICEAMMINISTRAZIONE$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "CodiceAmministrazione");
    
    
    /**
     * Gets the "CodiceAmministrazione" element
     */
    public it.gov.digitpa.www.protocollo.CodiceAmministrazione getCodiceAmministrazione()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.CodiceAmministrazione target = null;
            target = (it.gov.digitpa.www.protocollo.CodiceAmministrazione)get_store().find_element_user(CODICEAMMINISTRAZIONE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "CodiceAmministrazione" element
     */
    public void setCodiceAmministrazione(it.gov.digitpa.www.protocollo.CodiceAmministrazione codiceAmministrazione)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.CodiceAmministrazione target = null;
            target = (it.gov.digitpa.www.protocollo.CodiceAmministrazione)get_store().find_element_user(CODICEAMMINISTRAZIONE$0, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.CodiceAmministrazione)get_store().add_element_user(CODICEAMMINISTRAZIONE$0);
            }
            target.set(codiceAmministrazione);
        }
    }
    
    /**
     * Appends and returns a new empty "CodiceAmministrazione" element
     */
    public it.gov.digitpa.www.protocollo.CodiceAmministrazione addNewCodiceAmministrazione()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.CodiceAmministrazione target = null;
            target = (it.gov.digitpa.www.protocollo.CodiceAmministrazione)get_store().add_element_user(CODICEAMMINISTRAZIONE$0);
            return target;
        }
    }
}
