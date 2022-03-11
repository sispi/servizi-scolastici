/*
 * An XML document type.
 * Localname: NumeroRegistrazione
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.NumeroRegistrazioneDocument
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * A document containing one NumeroRegistrazione(@http://www.digitPa.gov.it/protocollo/) element.
 *
 * This is a complex type.
 */
public class NumeroRegistrazioneDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.NumeroRegistrazioneDocument
{
    
    public NumeroRegistrazioneDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName NUMEROREGISTRAZIONE$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "NumeroRegistrazione");
    
    
    /**
     * Gets the "NumeroRegistrazione" element
     */
    public it.gov.digitpa.www.protocollo.NumeroRegistrazione getNumeroRegistrazione()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.NumeroRegistrazione target = null;
            target = (it.gov.digitpa.www.protocollo.NumeroRegistrazione)get_store().find_element_user(NUMEROREGISTRAZIONE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "NumeroRegistrazione" element
     */
    public void setNumeroRegistrazione(it.gov.digitpa.www.protocollo.NumeroRegistrazione numeroRegistrazione)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.NumeroRegistrazione target = null;
            target = (it.gov.digitpa.www.protocollo.NumeroRegistrazione)get_store().find_element_user(NUMEROREGISTRAZIONE$0, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.NumeroRegistrazione)get_store().add_element_user(NUMEROREGISTRAZIONE$0);
            }
            target.set(numeroRegistrazione);
        }
    }
    
    /**
     * Appends and returns a new empty "NumeroRegistrazione" element
     */
    public it.gov.digitpa.www.protocollo.NumeroRegistrazione addNewNumeroRegistrazione()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.NumeroRegistrazione target = null;
            target = (it.gov.digitpa.www.protocollo.NumeroRegistrazione)get_store().add_element_user(NUMEROREGISTRAZIONE$0);
            return target;
        }
    }
}
