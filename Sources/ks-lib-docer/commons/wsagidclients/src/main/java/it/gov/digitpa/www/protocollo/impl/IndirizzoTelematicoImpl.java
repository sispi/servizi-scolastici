/*
 * XML Type:  IndirizzoTelematico
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.IndirizzoTelematico
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * An XML IndirizzoTelematico(@http://www.digitPa.gov.it/protocollo/).
 *
 * This is a complex type.
 */
public class IndirizzoTelematicoImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.IndirizzoTelematico
{
    
    public IndirizzoTelematicoImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName TIPO$0 = 
        new javax.xml.namespace.QName("", "tipo");
    private static final javax.xml.namespace.QName NOTE$2 = 
        new javax.xml.namespace.QName("", "note");
    
    
    /**
     * Gets the "tipo" attribute
     */
    public it.gov.digitpa.www.protocollo.IndirizzoTelematico.Tipo.Enum getTipo()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(TIPO$0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(TIPO$0);
            }
            if (target == null)
            {
                return null;
            }
            return (it.gov.digitpa.www.protocollo.IndirizzoTelematico.Tipo.Enum)target.getEnumValue();
        }
    }
    
    /**
     * Gets (as xml) the "tipo" attribute
     */
    public it.gov.digitpa.www.protocollo.IndirizzoTelematico.Tipo xgetTipo()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.IndirizzoTelematico.Tipo target = null;
            target = (it.gov.digitpa.www.protocollo.IndirizzoTelematico.Tipo)get_store().find_attribute_user(TIPO$0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.IndirizzoTelematico.Tipo)get_default_attribute_value(TIPO$0);
            }
            return target;
        }
    }
    
    /**
     * True if has "tipo" attribute
     */
    public boolean isSetTipo()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().find_attribute_user(TIPO$0) != null;
        }
    }
    
    /**
     * Sets the "tipo" attribute
     */
    public void setTipo(it.gov.digitpa.www.protocollo.IndirizzoTelematico.Tipo.Enum tipo)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(TIPO$0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(TIPO$0);
            }
            target.setEnumValue(tipo);
        }
    }
    
    /**
     * Sets (as xml) the "tipo" attribute
     */
    public void xsetTipo(it.gov.digitpa.www.protocollo.IndirizzoTelematico.Tipo tipo)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.IndirizzoTelematico.Tipo target = null;
            target = (it.gov.digitpa.www.protocollo.IndirizzoTelematico.Tipo)get_store().find_attribute_user(TIPO$0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.IndirizzoTelematico.Tipo)get_store().add_attribute_user(TIPO$0);
            }
            target.set(tipo);
        }
    }
    
    /**
     * Unsets the "tipo" attribute
     */
    public void unsetTipo()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_attribute(TIPO$0);
        }
    }
    
    /**
     * Gets the "note" attribute
     */
    public org.apache.xmlbeans.XmlAnySimpleType getNote()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlAnySimpleType target = null;
            target = (org.apache.xmlbeans.XmlAnySimpleType)get_store().find_attribute_user(NOTE$2);
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
            return get_store().find_attribute_user(NOTE$2) != null;
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
            target = (org.apache.xmlbeans.XmlAnySimpleType)get_store().find_attribute_user(NOTE$2);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlAnySimpleType)get_store().add_attribute_user(NOTE$2);
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
            target = (org.apache.xmlbeans.XmlAnySimpleType)get_store().add_attribute_user(NOTE$2);
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
            get_store().remove_attribute(NOTE$2);
        }
    }
    /**
     * An XML tipo(@).
     *
     * This is an atomic type that is a restriction of it.gov.digitpa.www.protocollo.IndirizzoTelematico$Tipo.
     */
    public static class TipoImpl extends org.apache.xmlbeans.impl.values.JavaStringEnumerationHolderEx implements it.gov.digitpa.www.protocollo.IndirizzoTelematico.Tipo
    {
        
        public TipoImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType, false);
        }
        
        protected TipoImpl(org.apache.xmlbeans.SchemaType sType, boolean b)
        {
            super(sType, b);
        }
    }
}
