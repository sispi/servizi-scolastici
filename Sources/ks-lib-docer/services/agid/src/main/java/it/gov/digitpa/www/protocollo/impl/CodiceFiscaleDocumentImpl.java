/*
 * An XML document type.
 * Localname: CodiceFiscale
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.CodiceFiscaleDocument
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * A document containing one CodiceFiscale(@http://www.digitPa.gov.it/protocollo/) element.
 *
 * This is a complex type.
 */
public class CodiceFiscaleDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.CodiceFiscaleDocument
{
    
    public CodiceFiscaleDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName CODICEFISCALE$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "CodiceFiscale");
    
    
    /**
     * Gets the "CodiceFiscale" element
     */
    public it.gov.digitpa.www.protocollo.CodiceFiscale getCodiceFiscale()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.CodiceFiscale target = null;
            target = (it.gov.digitpa.www.protocollo.CodiceFiscale)get_store().find_element_user(CODICEFISCALE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "CodiceFiscale" element
     */
    public void setCodiceFiscale(it.gov.digitpa.www.protocollo.CodiceFiscale codiceFiscale)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.CodiceFiscale target = null;
            target = (it.gov.digitpa.www.protocollo.CodiceFiscale)get_store().find_element_user(CODICEFISCALE$0, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.CodiceFiscale)get_store().add_element_user(CODICEFISCALE$0);
            }
            target.set(codiceFiscale);
        }
    }
    
    /**
     * Appends and returns a new empty "CodiceFiscale" element
     */
    public it.gov.digitpa.www.protocollo.CodiceFiscale addNewCodiceFiscale()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.CodiceFiscale target = null;
            target = (it.gov.digitpa.www.protocollo.CodiceFiscale)get_store().add_element_user(CODICEFISCALE$0);
            return target;
        }
    }
}
