/*
 * XML Type:  Impronta
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.Impronta
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * An XML Impronta(@http://www.digitPa.gov.it/protocollo/).
 *
 * This is a complex type.
 */
public class ImprontaImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.Impronta
{
    
    public ImprontaImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName ALGORITMO$0 = 
        new javax.xml.namespace.QName("", "algoritmo");
    private static final javax.xml.namespace.QName CODIFICA$2 = 
        new javax.xml.namespace.QName("", "codifica");
    
    
    /**
     * Gets the "algoritmo" attribute
     */
    public org.apache.xmlbeans.XmlAnySimpleType getAlgoritmo()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlAnySimpleType target = null;
            target = (org.apache.xmlbeans.XmlAnySimpleType)get_store().find_attribute_user(ALGORITMO$0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlAnySimpleType)get_default_attribute_value(ALGORITMO$0);
            }
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "algoritmo" attribute
     */
    public boolean isSetAlgoritmo()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().find_attribute_user(ALGORITMO$0) != null;
        }
    }
    
    /**
     * Sets the "algoritmo" attribute
     */
    public void setAlgoritmo(org.apache.xmlbeans.XmlAnySimpleType algoritmo)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlAnySimpleType target = null;
            target = (org.apache.xmlbeans.XmlAnySimpleType)get_store().find_attribute_user(ALGORITMO$0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlAnySimpleType)get_store().add_attribute_user(ALGORITMO$0);
            }
            target.set(algoritmo);
        }
    }
    
    /**
     * Appends and returns a new empty "algoritmo" attribute
     */
    public org.apache.xmlbeans.XmlAnySimpleType addNewAlgoritmo()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlAnySimpleType target = null;
            target = (org.apache.xmlbeans.XmlAnySimpleType)get_store().add_attribute_user(ALGORITMO$0);
            return target;
        }
    }
    
    /**
     * Unsets the "algoritmo" attribute
     */
    public void unsetAlgoritmo()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_attribute(ALGORITMO$0);
        }
    }
    
    /**
     * Gets the "codifica" attribute
     */
    public org.apache.xmlbeans.XmlAnySimpleType getCodifica()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlAnySimpleType target = null;
            target = (org.apache.xmlbeans.XmlAnySimpleType)get_store().find_attribute_user(CODIFICA$2);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlAnySimpleType)get_default_attribute_value(CODIFICA$2);
            }
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "codifica" attribute
     */
    public boolean isSetCodifica()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().find_attribute_user(CODIFICA$2) != null;
        }
    }
    
    /**
     * Sets the "codifica" attribute
     */
    public void setCodifica(org.apache.xmlbeans.XmlAnySimpleType codifica)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlAnySimpleType target = null;
            target = (org.apache.xmlbeans.XmlAnySimpleType)get_store().find_attribute_user(CODIFICA$2);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlAnySimpleType)get_store().add_attribute_user(CODIFICA$2);
            }
            target.set(codifica);
        }
    }
    
    /**
     * Appends and returns a new empty "codifica" attribute
     */
    public org.apache.xmlbeans.XmlAnySimpleType addNewCodifica()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlAnySimpleType target = null;
            target = (org.apache.xmlbeans.XmlAnySimpleType)get_store().add_attribute_user(CODIFICA$2);
            return target;
        }
    }
    
    /**
     * Unsets the "codifica" attribute
     */
    public void unsetCodifica()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_attribute(CODIFICA$2);
        }
    }
}
