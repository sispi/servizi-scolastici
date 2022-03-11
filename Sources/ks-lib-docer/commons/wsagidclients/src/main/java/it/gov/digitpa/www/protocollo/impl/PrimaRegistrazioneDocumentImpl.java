/*
 * An XML document type.
 * Localname: PrimaRegistrazione
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.PrimaRegistrazioneDocument
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * A document containing one PrimaRegistrazione(@http://www.digitPa.gov.it/protocollo/) element.
 *
 * This is a complex type.
 */
public class PrimaRegistrazioneDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.PrimaRegistrazioneDocument
{
    
    public PrimaRegistrazioneDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName PRIMAREGISTRAZIONE$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "PrimaRegistrazione");
    
    
    /**
     * Gets the "PrimaRegistrazione" element
     */
    public it.gov.digitpa.www.protocollo.PrimaRegistrazione getPrimaRegistrazione()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.PrimaRegistrazione target = null;
            target = (it.gov.digitpa.www.protocollo.PrimaRegistrazione)get_store().find_element_user(PRIMAREGISTRAZIONE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "PrimaRegistrazione" element
     */
    public void setPrimaRegistrazione(it.gov.digitpa.www.protocollo.PrimaRegistrazione primaRegistrazione)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.PrimaRegistrazione target = null;
            target = (it.gov.digitpa.www.protocollo.PrimaRegistrazione)get_store().find_element_user(PRIMAREGISTRAZIONE$0, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.PrimaRegistrazione)get_store().add_element_user(PRIMAREGISTRAZIONE$0);
            }
            target.set(primaRegistrazione);
        }
    }
    
    /**
     * Appends and returns a new empty "PrimaRegistrazione" element
     */
    public it.gov.digitpa.www.protocollo.PrimaRegistrazione addNewPrimaRegistrazione()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.PrimaRegistrazione target = null;
            target = (it.gov.digitpa.www.protocollo.PrimaRegistrazione)get_store().add_element_user(PRIMAREGISTRAZIONE$0);
            return target;
        }
    }
}
