/*
 * An XML document type.
 * Localname: EsitoConsegna
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.EsitoConsegnaDocument
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * A document containing one EsitoConsegna(@http://www.digitPa.gov.it/protocollo/) element.
 *
 * This is a complex type.
 */
public class EsitoConsegnaDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.EsitoConsegnaDocument
{
    
    public EsitoConsegnaDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName ESITOCONSEGNA$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "EsitoConsegna");
    
    
    /**
     * Gets the "EsitoConsegna" element
     */
    public it.gov.digitpa.www.protocollo.EsitoType.Enum getEsitoConsegna()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(ESITOCONSEGNA$0, 0);
            if (target == null)
            {
                return null;
            }
            return (it.gov.digitpa.www.protocollo.EsitoType.Enum)target.getEnumValue();
        }
    }
    
    /**
     * Gets (as xml) the "EsitoConsegna" element
     */
    public it.gov.digitpa.www.protocollo.EsitoType xgetEsitoConsegna()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.EsitoType target = null;
            target = (it.gov.digitpa.www.protocollo.EsitoType)get_store().find_element_user(ESITOCONSEGNA$0, 0);
            return target;
        }
    }
    
    /**
     * Sets the "EsitoConsegna" element
     */
    public void setEsitoConsegna(it.gov.digitpa.www.protocollo.EsitoType.Enum esitoConsegna)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(ESITOCONSEGNA$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(ESITOCONSEGNA$0);
            }
            target.setEnumValue(esitoConsegna);
        }
    }
    
    /**
     * Sets (as xml) the "EsitoConsegna" element
     */
    public void xsetEsitoConsegna(it.gov.digitpa.www.protocollo.EsitoType esitoConsegna)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.EsitoType target = null;
            target = (it.gov.digitpa.www.protocollo.EsitoType)get_store().find_element_user(ESITOCONSEGNA$0, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.EsitoType)get_store().add_element_user(ESITOCONSEGNA$0);
            }
            target.set(esitoConsegna);
        }
    }
}
