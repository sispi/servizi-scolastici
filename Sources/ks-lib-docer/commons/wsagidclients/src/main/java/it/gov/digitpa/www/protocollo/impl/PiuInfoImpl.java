/*
 * XML Type:  PiuInfo
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.PiuInfo
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * An XML PiuInfo(@http://www.digitPa.gov.it/protocollo/).
 *
 * This is a complex type.
 */
public class PiuInfoImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.PiuInfo
{
    
    public PiuInfoImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName METADATIINTERNI$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "MetadatiInterni");
    private static final javax.xml.namespace.QName METADATIESTERNI$2 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "MetadatiEsterni");
    private static final javax.xml.namespace.QName XMLSCHEMA$4 = 
        new javax.xml.namespace.QName("", "XMLSchema");
    
    
    /**
     * Gets the "MetadatiInterni" element
     */
    public it.gov.digitpa.www.protocollo.MetadatiInterni getMetadatiInterni()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.MetadatiInterni target = null;
            target = (it.gov.digitpa.www.protocollo.MetadatiInterni)get_store().find_element_user(METADATIINTERNI$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "MetadatiInterni" element
     */
    public boolean isSetMetadatiInterni()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(METADATIINTERNI$0) != 0;
        }
    }
    
    /**
     * Sets the "MetadatiInterni" element
     */
    public void setMetadatiInterni(it.gov.digitpa.www.protocollo.MetadatiInterni metadatiInterni)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.MetadatiInterni target = null;
            target = (it.gov.digitpa.www.protocollo.MetadatiInterni)get_store().find_element_user(METADATIINTERNI$0, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.MetadatiInterni)get_store().add_element_user(METADATIINTERNI$0);
            }
            target.set(metadatiInterni);
        }
    }
    
    /**
     * Appends and returns a new empty "MetadatiInterni" element
     */
    public it.gov.digitpa.www.protocollo.MetadatiInterni addNewMetadatiInterni()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.MetadatiInterni target = null;
            target = (it.gov.digitpa.www.protocollo.MetadatiInterni)get_store().add_element_user(METADATIINTERNI$0);
            return target;
        }
    }
    
    /**
     * Unsets the "MetadatiInterni" element
     */
    public void unsetMetadatiInterni()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(METADATIINTERNI$0, 0);
        }
    }
    
    /**
     * Gets the "MetadatiEsterni" element
     */
    public it.gov.digitpa.www.protocollo.MetadatiEsterni getMetadatiEsterni()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.MetadatiEsterni target = null;
            target = (it.gov.digitpa.www.protocollo.MetadatiEsterni)get_store().find_element_user(METADATIESTERNI$2, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "MetadatiEsterni" element
     */
    public boolean isSetMetadatiEsterni()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(METADATIESTERNI$2) != 0;
        }
    }
    
    /**
     * Sets the "MetadatiEsterni" element
     */
    public void setMetadatiEsterni(it.gov.digitpa.www.protocollo.MetadatiEsterni metadatiEsterni)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.MetadatiEsterni target = null;
            target = (it.gov.digitpa.www.protocollo.MetadatiEsterni)get_store().find_element_user(METADATIESTERNI$2, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.MetadatiEsterni)get_store().add_element_user(METADATIESTERNI$2);
            }
            target.set(metadatiEsterni);
        }
    }
    
    /**
     * Appends and returns a new empty "MetadatiEsterni" element
     */
    public it.gov.digitpa.www.protocollo.MetadatiEsterni addNewMetadatiEsterni()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.MetadatiEsterni target = null;
            target = (it.gov.digitpa.www.protocollo.MetadatiEsterni)get_store().add_element_user(METADATIESTERNI$2);
            return target;
        }
    }
    
    /**
     * Unsets the "MetadatiEsterni" element
     */
    public void unsetMetadatiEsterni()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(METADATIESTERNI$2, 0);
        }
    }
    
    /**
     * Gets the "XMLSchema" attribute
     */
    public java.lang.String getXMLSchema()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(XMLSCHEMA$4);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "XMLSchema" attribute
     */
    public org.apache.xmlbeans.XmlNMTOKEN xgetXMLSchema()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlNMTOKEN target = null;
            target = (org.apache.xmlbeans.XmlNMTOKEN)get_store().find_attribute_user(XMLSCHEMA$4);
            return target;
        }
    }
    
    /**
     * Sets the "XMLSchema" attribute
     */
    public void setXMLSchema(java.lang.String xmlSchema)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(XMLSCHEMA$4);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(XMLSCHEMA$4);
            }
            target.setStringValue(xmlSchema);
        }
    }
    
    /**
     * Sets (as xml) the "XMLSchema" attribute
     */
    public void xsetXMLSchema(org.apache.xmlbeans.XmlNMTOKEN xmlSchema)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlNMTOKEN target = null;
            target = (org.apache.xmlbeans.XmlNMTOKEN)get_store().find_attribute_user(XMLSCHEMA$4);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlNMTOKEN)get_store().add_attribute_user(XMLSCHEMA$4);
            }
            target.set(xmlSchema);
        }
    }
}
