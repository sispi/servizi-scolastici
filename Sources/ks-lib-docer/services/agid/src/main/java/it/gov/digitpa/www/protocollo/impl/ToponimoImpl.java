/*
 * XML Type:  Toponimo
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.Toponimo
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * An XML Toponimo(@http://www.digitPa.gov.it/protocollo/).
 *
 * This is a complex type.
 */
public class ToponimoImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.Toponimo
{
    
    public ToponimoImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName DUG$0 = 
        new javax.xml.namespace.QName("", "dug");
    
    
    /**
     * Gets the "dug" attribute
     */
    public org.apache.xmlbeans.XmlAnySimpleType getDug()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlAnySimpleType target = null;
            target = (org.apache.xmlbeans.XmlAnySimpleType)get_store().find_attribute_user(DUG$0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "dug" attribute
     */
    public boolean isSetDug()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().find_attribute_user(DUG$0) != null;
        }
    }
    
    /**
     * Sets the "dug" attribute
     */
    public void setDug(org.apache.xmlbeans.XmlAnySimpleType dug)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlAnySimpleType target = null;
            target = (org.apache.xmlbeans.XmlAnySimpleType)get_store().find_attribute_user(DUG$0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlAnySimpleType)get_store().add_attribute_user(DUG$0);
            }
            target.set(dug);
        }
    }
    
    /**
     * Appends and returns a new empty "dug" attribute
     */
    public org.apache.xmlbeans.XmlAnySimpleType addNewDug()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlAnySimpleType target = null;
            target = (org.apache.xmlbeans.XmlAnySimpleType)get_store().add_attribute_user(DUG$0);
            return target;
        }
    }
    
    /**
     * Unsets the "dug" attribute
     */
    public void unsetDug()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_attribute(DUG$0);
        }
    }
}
