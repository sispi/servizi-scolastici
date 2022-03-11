/*
 * XML Type:  TestoDelMessaggio
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.TestoDelMessaggio
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * An XML TestoDelMessaggio(@http://www.digitPa.gov.it/protocollo/).
 *
 * This is a complex type.
 */
public class TestoDelMessaggioImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.TestoDelMessaggio
{
    
    public TestoDelMessaggioImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName ID$0 = 
        new javax.xml.namespace.QName("", "id");
    private static final javax.xml.namespace.QName TIPOMIME$2 = 
        new javax.xml.namespace.QName("", "tipoMIME");
    private static final javax.xml.namespace.QName TIPORIFERIMENTO$4 = 
        new javax.xml.namespace.QName("", "tipoRiferimento");
    
    
    /**
     * Gets the "id" attribute
     */
    public org.apache.xmlbeans.XmlAnySimpleType getId()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlAnySimpleType target = null;
            target = (org.apache.xmlbeans.XmlAnySimpleType)get_store().find_attribute_user(ID$0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "id" attribute
     */
    public boolean isSetId()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().find_attribute_user(ID$0) != null;
        }
    }
    
    /**
     * Sets the "id" attribute
     */
    public void setId(org.apache.xmlbeans.XmlAnySimpleType id)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlAnySimpleType target = null;
            target = (org.apache.xmlbeans.XmlAnySimpleType)get_store().find_attribute_user(ID$0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlAnySimpleType)get_store().add_attribute_user(ID$0);
            }
            target.set(id);
        }
    }
    
    /**
     * Appends and returns a new empty "id" attribute
     */
    public org.apache.xmlbeans.XmlAnySimpleType addNewId()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlAnySimpleType target = null;
            target = (org.apache.xmlbeans.XmlAnySimpleType)get_store().add_attribute_user(ID$0);
            return target;
        }
    }
    
    /**
     * Unsets the "id" attribute
     */
    public void unsetId()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_attribute(ID$0);
        }
    }
    
    /**
     * Gets the "tipoMIME" attribute
     */
    public org.apache.xmlbeans.XmlAnySimpleType getTipoMIME()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlAnySimpleType target = null;
            target = (org.apache.xmlbeans.XmlAnySimpleType)get_store().find_attribute_user(TIPOMIME$2);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "tipoMIME" attribute
     */
    public boolean isSetTipoMIME()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().find_attribute_user(TIPOMIME$2) != null;
        }
    }
    
    /**
     * Sets the "tipoMIME" attribute
     */
    public void setTipoMIME(org.apache.xmlbeans.XmlAnySimpleType tipoMIME)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlAnySimpleType target = null;
            target = (org.apache.xmlbeans.XmlAnySimpleType)get_store().find_attribute_user(TIPOMIME$2);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlAnySimpleType)get_store().add_attribute_user(TIPOMIME$2);
            }
            target.set(tipoMIME);
        }
    }
    
    /**
     * Appends and returns a new empty "tipoMIME" attribute
     */
    public org.apache.xmlbeans.XmlAnySimpleType addNewTipoMIME()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlAnySimpleType target = null;
            target = (org.apache.xmlbeans.XmlAnySimpleType)get_store().add_attribute_user(TIPOMIME$2);
            return target;
        }
    }
    
    /**
     * Unsets the "tipoMIME" attribute
     */
    public void unsetTipoMIME()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_attribute(TIPOMIME$2);
        }
    }
    
    /**
     * Gets the "tipoRiferimento" attribute
     */
    public java.lang.String getTipoRiferimento()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(TIPORIFERIMENTO$4);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(TIPORIFERIMENTO$4);
            }
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "tipoRiferimento" attribute
     */
    public org.apache.xmlbeans.XmlNMTOKEN xgetTipoRiferimento()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlNMTOKEN target = null;
            target = (org.apache.xmlbeans.XmlNMTOKEN)get_store().find_attribute_user(TIPORIFERIMENTO$4);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlNMTOKEN)get_default_attribute_value(TIPORIFERIMENTO$4);
            }
            return target;
        }
    }
    
    /**
     * True if has "tipoRiferimento" attribute
     */
    public boolean isSetTipoRiferimento()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().find_attribute_user(TIPORIFERIMENTO$4) != null;
        }
    }
    
    /**
     * Sets the "tipoRiferimento" attribute
     */
    public void setTipoRiferimento(java.lang.String tipoRiferimento)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(TIPORIFERIMENTO$4);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(TIPORIFERIMENTO$4);
            }
            target.setStringValue(tipoRiferimento);
        }
    }
    
    /**
     * Sets (as xml) the "tipoRiferimento" attribute
     */
    public void xsetTipoRiferimento(org.apache.xmlbeans.XmlNMTOKEN tipoRiferimento)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlNMTOKEN target = null;
            target = (org.apache.xmlbeans.XmlNMTOKEN)get_store().find_attribute_user(TIPORIFERIMENTO$4);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlNMTOKEN)get_store().add_attribute_user(TIPORIFERIMENTO$4);
            }
            target.set(tipoRiferimento);
        }
    }
    
    /**
     * Unsets the "tipoRiferimento" attribute
     */
    public void unsetTipoRiferimento()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_attribute(TIPORIFERIMENTO$4);
        }
    }
}
