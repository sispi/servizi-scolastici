/*
 * An XML document type.
 * Localname: Descrizione
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.DescrizioneDocument
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * A document containing one Descrizione(@http://www.digitPa.gov.it/protocollo/) element.
 *
 * This is a complex type.
 */
public class DescrizioneDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.DescrizioneDocument
{
    
    public DescrizioneDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName DESCRIZIONE$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Descrizione");
    
    
    /**
     * Gets the "Descrizione" element
     */
    public it.gov.digitpa.www.protocollo.Descrizione getDescrizione()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Descrizione target = null;
            target = (it.gov.digitpa.www.protocollo.Descrizione)get_store().find_element_user(DESCRIZIONE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Descrizione" element
     */
    public void setDescrizione(it.gov.digitpa.www.protocollo.Descrizione descrizione)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Descrizione target = null;
            target = (it.gov.digitpa.www.protocollo.Descrizione)get_store().find_element_user(DESCRIZIONE$0, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.Descrizione)get_store().add_element_user(DESCRIZIONE$0);
            }
            target.set(descrizione);
        }
    }
    
    /**
     * Appends and returns a new empty "Descrizione" element
     */
    public it.gov.digitpa.www.protocollo.Descrizione addNewDescrizione()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Descrizione target = null;
            target = (it.gov.digitpa.www.protocollo.Descrizione)get_store().add_element_user(DESCRIZIONE$0);
            return target;
        }
    }
}
