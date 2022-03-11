/*
 * An XML document type.
 * Localname: DescrizioneMessaggio
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.DescrizioneMessaggioDocument
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * A document containing one DescrizioneMessaggio(@http://www.digitPa.gov.it/protocollo/) element.
 *
 * This is a complex type.
 */
public class DescrizioneMessaggioDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.DescrizioneMessaggioDocument
{
    
    public DescrizioneMessaggioDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName DESCRIZIONEMESSAGGIO$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "DescrizioneMessaggio");
    
    
    /**
     * Gets the "DescrizioneMessaggio" element
     */
    public it.gov.digitpa.www.protocollo.DescrizioneMessaggio getDescrizioneMessaggio()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.DescrizioneMessaggio target = null;
            target = (it.gov.digitpa.www.protocollo.DescrizioneMessaggio)get_store().find_element_user(DESCRIZIONEMESSAGGIO$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "DescrizioneMessaggio" element
     */
    public void setDescrizioneMessaggio(it.gov.digitpa.www.protocollo.DescrizioneMessaggio descrizioneMessaggio)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.DescrizioneMessaggio target = null;
            target = (it.gov.digitpa.www.protocollo.DescrizioneMessaggio)get_store().find_element_user(DESCRIZIONEMESSAGGIO$0, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.DescrizioneMessaggio)get_store().add_element_user(DESCRIZIONEMESSAGGIO$0);
            }
            target.set(descrizioneMessaggio);
        }
    }
    
    /**
     * Appends and returns a new empty "DescrizioneMessaggio" element
     */
    public it.gov.digitpa.www.protocollo.DescrizioneMessaggio addNewDescrizioneMessaggio()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.DescrizioneMessaggio target = null;
            target = (it.gov.digitpa.www.protocollo.DescrizioneMessaggio)get_store().add_element_user(DESCRIZIONEMESSAGGIO$0);
            return target;
        }
    }
}
