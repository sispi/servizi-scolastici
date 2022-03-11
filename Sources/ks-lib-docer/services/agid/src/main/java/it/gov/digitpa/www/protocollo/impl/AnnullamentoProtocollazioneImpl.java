/*
 * XML Type:  AnnullamentoProtocollazione
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.AnnullamentoProtocollazione
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * An XML AnnullamentoProtocollazione(@http://www.digitPa.gov.it/protocollo/).
 *
 * This is a complex type.
 */
public class AnnullamentoProtocollazioneImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.AnnullamentoProtocollazione
{
    
    public AnnullamentoProtocollazioneImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName IDENTIFICATORE$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Identificatore");
    private static final javax.xml.namespace.QName MOTIVO$2 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Motivo");
    private static final javax.xml.namespace.QName PROVVEDIMENTO$4 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Provvedimento");
    private static final javax.xml.namespace.QName VERSIONE$6 = 
        new javax.xml.namespace.QName("", "versione");
    private static final javax.xml.namespace.QName XMLLANG$8 = 
        new javax.xml.namespace.QName("", "xml-lang");
    
    
    /**
     * Gets the "Identificatore" element
     */
    public it.gov.digitpa.www.protocollo.Identificatore getIdentificatore()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Identificatore target = null;
            target = (it.gov.digitpa.www.protocollo.Identificatore)get_store().find_element_user(IDENTIFICATORE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Identificatore" element
     */
    public void setIdentificatore(it.gov.digitpa.www.protocollo.Identificatore identificatore)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Identificatore target = null;
            target = (it.gov.digitpa.www.protocollo.Identificatore)get_store().find_element_user(IDENTIFICATORE$0, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.Identificatore)get_store().add_element_user(IDENTIFICATORE$0);
            }
            target.set(identificatore);
        }
    }
    
    /**
     * Appends and returns a new empty "Identificatore" element
     */
    public it.gov.digitpa.www.protocollo.Identificatore addNewIdentificatore()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Identificatore target = null;
            target = (it.gov.digitpa.www.protocollo.Identificatore)get_store().add_element_user(IDENTIFICATORE$0);
            return target;
        }
    }
    
    /**
     * Gets the "Motivo" element
     */
    public it.gov.digitpa.www.protocollo.Motivo getMotivo()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Motivo target = null;
            target = (it.gov.digitpa.www.protocollo.Motivo)get_store().find_element_user(MOTIVO$2, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Motivo" element
     */
    public void setMotivo(it.gov.digitpa.www.protocollo.Motivo motivo)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Motivo target = null;
            target = (it.gov.digitpa.www.protocollo.Motivo)get_store().find_element_user(MOTIVO$2, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.Motivo)get_store().add_element_user(MOTIVO$2);
            }
            target.set(motivo);
        }
    }
    
    /**
     * Appends and returns a new empty "Motivo" element
     */
    public it.gov.digitpa.www.protocollo.Motivo addNewMotivo()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Motivo target = null;
            target = (it.gov.digitpa.www.protocollo.Motivo)get_store().add_element_user(MOTIVO$2);
            return target;
        }
    }
    
    /**
     * Gets the "Provvedimento" element
     */
    public it.gov.digitpa.www.protocollo.Provvedimento getProvvedimento()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Provvedimento target = null;
            target = (it.gov.digitpa.www.protocollo.Provvedimento)get_store().find_element_user(PROVVEDIMENTO$4, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Provvedimento" element
     */
    public void setProvvedimento(it.gov.digitpa.www.protocollo.Provvedimento provvedimento)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Provvedimento target = null;
            target = (it.gov.digitpa.www.protocollo.Provvedimento)get_store().find_element_user(PROVVEDIMENTO$4, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.Provvedimento)get_store().add_element_user(PROVVEDIMENTO$4);
            }
            target.set(provvedimento);
        }
    }
    
    /**
     * Appends and returns a new empty "Provvedimento" element
     */
    public it.gov.digitpa.www.protocollo.Provvedimento addNewProvvedimento()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Provvedimento target = null;
            target = (it.gov.digitpa.www.protocollo.Provvedimento)get_store().add_element_user(PROVVEDIMENTO$4);
            return target;
        }
    }
    
    /**
     * Gets the "versione" attribute
     */
    public java.lang.String getVersione()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(VERSIONE$6);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(VERSIONE$6);
            }
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "versione" attribute
     */
    public org.apache.xmlbeans.XmlNMTOKEN xgetVersione()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlNMTOKEN target = null;
            target = (org.apache.xmlbeans.XmlNMTOKEN)get_store().find_attribute_user(VERSIONE$6);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlNMTOKEN)get_default_attribute_value(VERSIONE$6);
            }
            return target;
        }
    }
    
    /**
     * True if has "versione" attribute
     */
    public boolean isSetVersione()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().find_attribute_user(VERSIONE$6) != null;
        }
    }
    
    /**
     * Sets the "versione" attribute
     */
    public void setVersione(java.lang.String versione)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(VERSIONE$6);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(VERSIONE$6);
            }
            target.setStringValue(versione);
        }
    }
    
    /**
     * Sets (as xml) the "versione" attribute
     */
    public void xsetVersione(org.apache.xmlbeans.XmlNMTOKEN versione)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlNMTOKEN target = null;
            target = (org.apache.xmlbeans.XmlNMTOKEN)get_store().find_attribute_user(VERSIONE$6);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlNMTOKEN)get_store().add_attribute_user(VERSIONE$6);
            }
            target.set(versione);
        }
    }
    
    /**
     * Unsets the "versione" attribute
     */
    public void unsetVersione()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_attribute(VERSIONE$6);
        }
    }
    
    /**
     * Gets the "xml-lang" attribute
     */
    public org.apache.xmlbeans.XmlAnySimpleType getXmlLang()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlAnySimpleType target = null;
            target = (org.apache.xmlbeans.XmlAnySimpleType)get_store().find_attribute_user(XMLLANG$8);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlAnySimpleType)get_default_attribute_value(XMLLANG$8);
            }
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "xml-lang" attribute
     */
    public boolean isSetXmlLang()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().find_attribute_user(XMLLANG$8) != null;
        }
    }
    
    /**
     * Sets the "xml-lang" attribute
     */
    public void setXmlLang(org.apache.xmlbeans.XmlAnySimpleType xmlLang)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlAnySimpleType target = null;
            target = (org.apache.xmlbeans.XmlAnySimpleType)get_store().find_attribute_user(XMLLANG$8);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlAnySimpleType)get_store().add_attribute_user(XMLLANG$8);
            }
            target.set(xmlLang);
        }
    }
    
    /**
     * Appends and returns a new empty "xml-lang" attribute
     */
    public org.apache.xmlbeans.XmlAnySimpleType addNewXmlLang()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlAnySimpleType target = null;
            target = (org.apache.xmlbeans.XmlAnySimpleType)get_store().add_attribute_user(XMLLANG$8);
            return target;
        }
    }
    
    /**
     * Unsets the "xml-lang" attribute
     */
    public void unsetXmlLang()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_attribute(XMLLANG$8);
        }
    }
}
