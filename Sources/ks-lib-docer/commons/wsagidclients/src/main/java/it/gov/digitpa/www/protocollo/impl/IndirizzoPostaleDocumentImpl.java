/*
 * An XML document type.
 * Localname: IndirizzoPostale
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.IndirizzoPostaleDocument
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * A document containing one IndirizzoPostale(@http://www.digitPa.gov.it/protocollo/) element.
 *
 * This is a complex type.
 */
public class IndirizzoPostaleDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.IndirizzoPostaleDocument
{
    
    public IndirizzoPostaleDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName INDIRIZZOPOSTALE$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "IndirizzoPostale");
    
    
    /**
     * Gets the "IndirizzoPostale" element
     */
    public it.gov.digitpa.www.protocollo.IndirizzoPostale getIndirizzoPostale()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.IndirizzoPostale target = null;
            target = (it.gov.digitpa.www.protocollo.IndirizzoPostale)get_store().find_element_user(INDIRIZZOPOSTALE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "IndirizzoPostale" element
     */
    public void setIndirizzoPostale(it.gov.digitpa.www.protocollo.IndirizzoPostale indirizzoPostale)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.IndirizzoPostale target = null;
            target = (it.gov.digitpa.www.protocollo.IndirizzoPostale)get_store().find_element_user(INDIRIZZOPOSTALE$0, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.IndirizzoPostale)get_store().add_element_user(INDIRIZZOPOSTALE$0);
            }
            target.set(indirizzoPostale);
        }
    }
    
    /**
     * Appends and returns a new empty "IndirizzoPostale" element
     */
    public it.gov.digitpa.www.protocollo.IndirizzoPostale addNewIndirizzoPostale()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.IndirizzoPostale target = null;
            target = (it.gov.digitpa.www.protocollo.IndirizzoPostale)get_store().add_element_user(INDIRIZZOPOSTALE$0);
            return target;
        }
    }
}
