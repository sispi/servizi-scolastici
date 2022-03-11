/*
 * An XML document type.
 * Localname: Amministrazione
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.AmministrazioneDocument
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * A document containing one Amministrazione(@http://www.digitPa.gov.it/protocollo/) element.
 *
 * This is a complex type.
 */
public class AmministrazioneDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.AmministrazioneDocument
{
    
    public AmministrazioneDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName AMMINISTRAZIONE$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Amministrazione");
    
    
    /**
     * Gets the "Amministrazione" element
     */
    public it.gov.digitpa.www.protocollo.Amministrazione getAmministrazione()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Amministrazione target = null;
            target = (it.gov.digitpa.www.protocollo.Amministrazione)get_store().find_element_user(AMMINISTRAZIONE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Amministrazione" element
     */
    public void setAmministrazione(it.gov.digitpa.www.protocollo.Amministrazione amministrazione)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Amministrazione target = null;
            target = (it.gov.digitpa.www.protocollo.Amministrazione)get_store().find_element_user(AMMINISTRAZIONE$0, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.Amministrazione)get_store().add_element_user(AMMINISTRAZIONE$0);
            }
            target.set(amministrazione);
        }
    }
    
    /**
     * Appends and returns a new empty "Amministrazione" element
     */
    public it.gov.digitpa.www.protocollo.Amministrazione addNewAmministrazione()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Amministrazione target = null;
            target = (it.gov.digitpa.www.protocollo.Amministrazione)get_store().add_element_user(AMMINISTRAZIONE$0);
            return target;
        }
    }
}
