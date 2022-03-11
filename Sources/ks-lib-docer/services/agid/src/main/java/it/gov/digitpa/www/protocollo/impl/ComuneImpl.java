/*
 * XML Type:  Comune
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.Comune
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * An XML Comune(@http://www.digitPa.gov.it/protocollo/).
 *
 * This is a complex type.
 */
public class ComuneImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.Comune
{
    
    public ComuneImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName CODICEISTAT$0 = 
        new javax.xml.namespace.QName("", "codiceISTAT");
    
    
    /**
     * Gets the "codiceISTAT" attribute
     */
    public org.apache.xmlbeans.XmlAnySimpleType getCodiceISTAT()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlAnySimpleType target = null;
            target = (org.apache.xmlbeans.XmlAnySimpleType)get_store().find_attribute_user(CODICEISTAT$0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "codiceISTAT" attribute
     */
    public boolean isSetCodiceISTAT()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().find_attribute_user(CODICEISTAT$0) != null;
        }
    }
    
    /**
     * Sets the "codiceISTAT" attribute
     */
    public void setCodiceISTAT(org.apache.xmlbeans.XmlAnySimpleType codiceISTAT)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlAnySimpleType target = null;
            target = (org.apache.xmlbeans.XmlAnySimpleType)get_store().find_attribute_user(CODICEISTAT$0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlAnySimpleType)get_store().add_attribute_user(CODICEISTAT$0);
            }
            target.set(codiceISTAT);
        }
    }
    
    /**
     * Appends and returns a new empty "codiceISTAT" attribute
     */
    public org.apache.xmlbeans.XmlAnySimpleType addNewCodiceISTAT()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlAnySimpleType target = null;
            target = (org.apache.xmlbeans.XmlAnySimpleType)get_store().add_attribute_user(CODICEISTAT$0);
            return target;
        }
    }
    
    /**
     * Unsets the "codiceISTAT" attribute
     */
    public void unsetCodiceISTAT()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_attribute(CODICEISTAT$0);
        }
    }
}
