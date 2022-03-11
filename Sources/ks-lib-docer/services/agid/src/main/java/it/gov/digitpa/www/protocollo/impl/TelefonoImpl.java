/*
 * XML Type:  Telefono
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.Telefono
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * An XML Telefono(@http://www.digitPa.gov.it/protocollo/).
 *
 * This is a complex type.
 */
public class TelefonoImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.Telefono
{
    
    public TelefonoImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName NOTE$0 = 
        new javax.xml.namespace.QName("", "note");
    
    
    /**
     * Gets the "note" attribute
     */
    public org.apache.xmlbeans.XmlAnySimpleType getNote()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlAnySimpleType target = null;
            target = (org.apache.xmlbeans.XmlAnySimpleType)get_store().find_attribute_user(NOTE$0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "note" attribute
     */
    public boolean isSetNote()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().find_attribute_user(NOTE$0) != null;
        }
    }
    
    /**
     * Sets the "note" attribute
     */
    public void setNote(org.apache.xmlbeans.XmlAnySimpleType note)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlAnySimpleType target = null;
            target = (org.apache.xmlbeans.XmlAnySimpleType)get_store().find_attribute_user(NOTE$0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlAnySimpleType)get_store().add_attribute_user(NOTE$0);
            }
            target.set(note);
        }
    }
    
    /**
     * Appends and returns a new empty "note" attribute
     */
    public org.apache.xmlbeans.XmlAnySimpleType addNewNote()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlAnySimpleType target = null;
            target = (org.apache.xmlbeans.XmlAnySimpleType)get_store().add_attribute_user(NOTE$0);
            return target;
        }
    }
    
    /**
     * Unsets the "note" attribute
     */
    public void unsetNote()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_attribute(NOTE$0);
        }
    }
}
