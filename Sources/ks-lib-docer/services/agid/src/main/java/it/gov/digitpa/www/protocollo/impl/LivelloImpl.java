/*
 * XML Type:  Livello
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.Livello
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * An XML Livello(@http://www.digitPa.gov.it/protocollo/).
 *
 * This is a complex type.
 */
public class LivelloImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.Livello
{
    
    public LivelloImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName NOME$0 = 
        new javax.xml.namespace.QName("", "nome");
    
    
    /**
     * Gets the "nome" attribute
     */
    public org.apache.xmlbeans.XmlAnySimpleType getNome()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlAnySimpleType target = null;
            target = (org.apache.xmlbeans.XmlAnySimpleType)get_store().find_attribute_user(NOME$0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "nome" attribute
     */
    public boolean isSetNome()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().find_attribute_user(NOME$0) != null;
        }
    }
    
    /**
     * Sets the "nome" attribute
     */
    public void setNome(org.apache.xmlbeans.XmlAnySimpleType nome)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlAnySimpleType target = null;
            target = (org.apache.xmlbeans.XmlAnySimpleType)get_store().find_attribute_user(NOME$0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlAnySimpleType)get_store().add_attribute_user(NOME$0);
            }
            target.set(nome);
        }
    }
    
    /**
     * Appends and returns a new empty "nome" attribute
     */
    public org.apache.xmlbeans.XmlAnySimpleType addNewNome()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlAnySimpleType target = null;
            target = (org.apache.xmlbeans.XmlAnySimpleType)get_store().add_attribute_user(NOME$0);
            return target;
        }
    }
    
    /**
     * Unsets the "nome" attribute
     */
    public void unsetNome()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_attribute(NOME$0);
        }
    }
}
