/*
 * An XML document type.
 * Localname: PiuInfo
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.PiuInfoDocument
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * A document containing one PiuInfo(@http://www.digitPa.gov.it/protocollo/) element.
 *
 * This is a complex type.
 */
public class PiuInfoDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.PiuInfoDocument
{
    
    public PiuInfoDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName PIUINFO$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "PiuInfo");
    
    
    /**
     * Gets the "PiuInfo" element
     */
    public it.gov.digitpa.www.protocollo.PiuInfo getPiuInfo()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.PiuInfo target = null;
            target = (it.gov.digitpa.www.protocollo.PiuInfo)get_store().find_element_user(PIUINFO$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "PiuInfo" element
     */
    public void setPiuInfo(it.gov.digitpa.www.protocollo.PiuInfo piuInfo)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.PiuInfo target = null;
            target = (it.gov.digitpa.www.protocollo.PiuInfo)get_store().find_element_user(PIUINFO$0, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.PiuInfo)get_store().add_element_user(PIUINFO$0);
            }
            target.set(piuInfo);
        }
    }
    
    /**
     * Appends and returns a new empty "PiuInfo" element
     */
    public it.gov.digitpa.www.protocollo.PiuInfo addNewPiuInfo()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.PiuInfo target = null;
            target = (it.gov.digitpa.www.protocollo.PiuInfo)get_store().add_element_user(PIUINFO$0);
            return target;
        }
    }
}
