/*
 * An XML document type.
 * Localname: Provincia
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.ProvinciaDocument
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * A document containing one Provincia(@http://www.digitPa.gov.it/protocollo/) element.
 *
 * This is a complex type.
 */
public class ProvinciaDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.ProvinciaDocument
{
    
    public ProvinciaDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName PROVINCIA$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Provincia");
    
    
    /**
     * Gets the "Provincia" element
     */
    public it.gov.digitpa.www.protocollo.Provincia getProvincia()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Provincia target = null;
            target = (it.gov.digitpa.www.protocollo.Provincia)get_store().find_element_user(PROVINCIA$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Provincia" element
     */
    public void setProvincia(it.gov.digitpa.www.protocollo.Provincia provincia)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Provincia target = null;
            target = (it.gov.digitpa.www.protocollo.Provincia)get_store().find_element_user(PROVINCIA$0, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.Provincia)get_store().add_element_user(PROVINCIA$0);
            }
            target.set(provincia);
        }
    }
    
    /**
     * Appends and returns a new empty "Provincia" element
     */
    public it.gov.digitpa.www.protocollo.Provincia addNewProvincia()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Provincia target = null;
            target = (it.gov.digitpa.www.protocollo.Provincia)get_store().add_element_user(PROVINCIA$0);
            return target;
        }
    }
}
