/*
 * XML Type:  OraRegistrazione
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.OraRegistrazione
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * An XML OraRegistrazione(@http://www.digitPa.gov.it/protocollo/).
 *
 * This is a complex type.
 */
public class OraRegistrazioneImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.OraRegistrazione
{
    
    public OraRegistrazioneImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName TEMPO$0 = 
        new javax.xml.namespace.QName("", "tempo");
    
    
    /**
     * Gets the "tempo" attribute
     */
    public it.gov.digitpa.www.protocollo.OraRegistrazione.Tempo.Enum getTempo()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(TEMPO$0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(TEMPO$0);
            }
            if (target == null)
            {
                return null;
            }
            return (it.gov.digitpa.www.protocollo.OraRegistrazione.Tempo.Enum)target.getEnumValue();
        }
    }
    
    /**
     * Gets (as xml) the "tempo" attribute
     */
    public it.gov.digitpa.www.protocollo.OraRegistrazione.Tempo xgetTempo()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.OraRegistrazione.Tempo target = null;
            target = (it.gov.digitpa.www.protocollo.OraRegistrazione.Tempo)get_store().find_attribute_user(TEMPO$0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.OraRegistrazione.Tempo)get_default_attribute_value(TEMPO$0);
            }
            return target;
        }
    }
    
    /**
     * True if has "tempo" attribute
     */
    public boolean isSetTempo()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().find_attribute_user(TEMPO$0) != null;
        }
    }
    
    /**
     * Sets the "tempo" attribute
     */
    public void setTempo(it.gov.digitpa.www.protocollo.OraRegistrazione.Tempo.Enum tempo)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(TEMPO$0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(TEMPO$0);
            }
            target.setEnumValue(tempo);
        }
    }
    
    /**
     * Sets (as xml) the "tempo" attribute
     */
    public void xsetTempo(it.gov.digitpa.www.protocollo.OraRegistrazione.Tempo tempo)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.OraRegistrazione.Tempo target = null;
            target = (it.gov.digitpa.www.protocollo.OraRegistrazione.Tempo)get_store().find_attribute_user(TEMPO$0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.OraRegistrazione.Tempo)get_store().add_attribute_user(TEMPO$0);
            }
            target.set(tempo);
        }
    }
    
    /**
     * Unsets the "tempo" attribute
     */
    public void unsetTempo()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_attribute(TEMPO$0);
        }
    }
    /**
     * An XML tempo(@).
     *
     * This is an atomic type that is a restriction of it.gov.digitpa.www.protocollo.OraRegistrazione$Tempo.
     */
    public static class TempoImpl extends org.apache.xmlbeans.impl.values.JavaStringEnumerationHolderEx implements it.gov.digitpa.www.protocollo.OraRegistrazione.Tempo
    {
        
        public TempoImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType, false);
        }
        
        protected TempoImpl(org.apache.xmlbeans.SchemaType sType, boolean b)
        {
            super(sType, b);
        }
    }
}
