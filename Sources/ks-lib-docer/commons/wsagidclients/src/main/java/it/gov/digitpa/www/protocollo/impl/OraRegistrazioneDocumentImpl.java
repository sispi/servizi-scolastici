/*
 * An XML document type.
 * Localname: OraRegistrazione
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.OraRegistrazioneDocument
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * A document containing one OraRegistrazione(@http://www.digitPa.gov.it/protocollo/) element.
 *
 * This is a complex type.
 */
public class OraRegistrazioneDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.OraRegistrazioneDocument
{
    
    public OraRegistrazioneDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName ORAREGISTRAZIONE$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "OraRegistrazione");
    
    
    /**
     * Gets the "OraRegistrazione" element
     */
    public it.gov.digitpa.www.protocollo.OraRegistrazione getOraRegistrazione()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.OraRegistrazione target = null;
            target = (it.gov.digitpa.www.protocollo.OraRegistrazione)get_store().find_element_user(ORAREGISTRAZIONE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "OraRegistrazione" element
     */
    public void setOraRegistrazione(it.gov.digitpa.www.protocollo.OraRegistrazione oraRegistrazione)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.OraRegistrazione target = null;
            target = (it.gov.digitpa.www.protocollo.OraRegistrazione)get_store().find_element_user(ORAREGISTRAZIONE$0, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.OraRegistrazione)get_store().add_element_user(ORAREGISTRAZIONE$0);
            }
            target.set(oraRegistrazione);
        }
    }
    
    /**
     * Appends and returns a new empty "OraRegistrazione" element
     */
    public it.gov.digitpa.www.protocollo.OraRegistrazione addNewOraRegistrazione()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.OraRegistrazione target = null;
            target = (it.gov.digitpa.www.protocollo.OraRegistrazione)get_store().add_element_user(ORAREGISTRAZIONE$0);
            return target;
        }
    }
}
