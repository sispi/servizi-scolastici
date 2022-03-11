/*
 * XML Type:  Segnatura
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.Segnatura
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * An XML Segnatura(@http://www.digitPa.gov.it/protocollo/).
 *
 * This is a complex type.
 */
public class SegnaturaImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.Segnatura
{
    
    public SegnaturaImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName INTESTAZIONE$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Intestazione");
    private static final javax.xml.namespace.QName RIFERIMENTI$2 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Riferimenti");
    private static final javax.xml.namespace.QName DESCRIZIONE$4 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Descrizione");
    private static final javax.xml.namespace.QName PIUINFO$6 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "PiuInfo");
    private static final javax.xml.namespace.QName VERSIONE$8 = 
        new javax.xml.namespace.QName("", "versione");
    private static final javax.xml.namespace.QName XMLLANG$10 = 
        new javax.xml.namespace.QName("", "xml-lang");
    
    
    /**
     * Gets the "Intestazione" element
     */
    public it.gov.digitpa.www.protocollo.Intestazione getIntestazione()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Intestazione target = null;
            target = (it.gov.digitpa.www.protocollo.Intestazione)get_store().find_element_user(INTESTAZIONE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Intestazione" element
     */
    public void setIntestazione(it.gov.digitpa.www.protocollo.Intestazione intestazione)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Intestazione target = null;
            target = (it.gov.digitpa.www.protocollo.Intestazione)get_store().find_element_user(INTESTAZIONE$0, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.Intestazione)get_store().add_element_user(INTESTAZIONE$0);
            }
            target.set(intestazione);
        }
    }
    
    /**
     * Appends and returns a new empty "Intestazione" element
     */
    public it.gov.digitpa.www.protocollo.Intestazione addNewIntestazione()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Intestazione target = null;
            target = (it.gov.digitpa.www.protocollo.Intestazione)get_store().add_element_user(INTESTAZIONE$0);
            return target;
        }
    }
    
    /**
     * Gets the "Riferimenti" element
     */
    public it.gov.digitpa.www.protocollo.Riferimenti getRiferimenti()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Riferimenti target = null;
            target = (it.gov.digitpa.www.protocollo.Riferimenti)get_store().find_element_user(RIFERIMENTI$2, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "Riferimenti" element
     */
    public boolean isSetRiferimenti()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(RIFERIMENTI$2) != 0;
        }
    }
    
    /**
     * Sets the "Riferimenti" element
     */
    public void setRiferimenti(it.gov.digitpa.www.protocollo.Riferimenti riferimenti)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Riferimenti target = null;
            target = (it.gov.digitpa.www.protocollo.Riferimenti)get_store().find_element_user(RIFERIMENTI$2, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.Riferimenti)get_store().add_element_user(RIFERIMENTI$2);
            }
            target.set(riferimenti);
        }
    }
    
    /**
     * Appends and returns a new empty "Riferimenti" element
     */
    public it.gov.digitpa.www.protocollo.Riferimenti addNewRiferimenti()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Riferimenti target = null;
            target = (it.gov.digitpa.www.protocollo.Riferimenti)get_store().add_element_user(RIFERIMENTI$2);
            return target;
        }
    }
    
    /**
     * Unsets the "Riferimenti" element
     */
    public void unsetRiferimenti()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(RIFERIMENTI$2, 0);
        }
    }
    
    /**
     * Gets the "Descrizione" element
     */
    public it.gov.digitpa.www.protocollo.Descrizione getDescrizione()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Descrizione target = null;
            target = (it.gov.digitpa.www.protocollo.Descrizione)get_store().find_element_user(DESCRIZIONE$4, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Descrizione" element
     */
    public void setDescrizione(it.gov.digitpa.www.protocollo.Descrizione descrizione)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Descrizione target = null;
            target = (it.gov.digitpa.www.protocollo.Descrizione)get_store().find_element_user(DESCRIZIONE$4, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.Descrizione)get_store().add_element_user(DESCRIZIONE$4);
            }
            target.set(descrizione);
        }
    }
    
    /**
     * Appends and returns a new empty "Descrizione" element
     */
    public it.gov.digitpa.www.protocollo.Descrizione addNewDescrizione()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Descrizione target = null;
            target = (it.gov.digitpa.www.protocollo.Descrizione)get_store().add_element_user(DESCRIZIONE$4);
            return target;
        }
    }
    
    /**
     * Gets the "PiuInfo" element
     */
    public it.gov.digitpa.www.protocollo.PiuInfo getPiuInfo()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.PiuInfo target = null;
            target = (it.gov.digitpa.www.protocollo.PiuInfo)get_store().find_element_user(PIUINFO$6, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "PiuInfo" element
     */
    public boolean isSetPiuInfo()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(PIUINFO$6) != 0;
        }
    }
    
    /**
     * Sets the "PiuInfo" element
     */
    public void setPiuInfo(it.gov.digitpa.www.protocollo.PiuInfo piuInfo)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.PiuInfo target = null;
            target = (it.gov.digitpa.www.protocollo.PiuInfo)get_store().find_element_user(PIUINFO$6, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.PiuInfo)get_store().add_element_user(PIUINFO$6);
            }
            target.set(piuInfo);
        }
    }
    
    /**
     * Appends and returns a new empty "PiuInfo" element
     */
    public it.gov.digitpa.www.protocollo.PiuInfo addNewPiuInfo()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.PiuInfo target = null;
            target = (it.gov.digitpa.www.protocollo.PiuInfo)get_store().add_element_user(PIUINFO$6);
            return target;
        }
    }
    
    /**
     * Unsets the "PiuInfo" element
     */
    public void unsetPiuInfo()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(PIUINFO$6, 0);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(VERSIONE$8);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(VERSIONE$8);
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
            target = (org.apache.xmlbeans.XmlNMTOKEN)get_store().find_attribute_user(VERSIONE$8);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlNMTOKEN)get_default_attribute_value(VERSIONE$8);
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
            return get_store().find_attribute_user(VERSIONE$8) != null;
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(VERSIONE$8);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(VERSIONE$8);
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
            target = (org.apache.xmlbeans.XmlNMTOKEN)get_store().find_attribute_user(VERSIONE$8);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlNMTOKEN)get_store().add_attribute_user(VERSIONE$8);
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
            get_store().remove_attribute(VERSIONE$8);
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
            target = (org.apache.xmlbeans.XmlAnySimpleType)get_store().find_attribute_user(XMLLANG$10);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlAnySimpleType)get_default_attribute_value(XMLLANG$10);
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
            return get_store().find_attribute_user(XMLLANG$10) != null;
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
            target = (org.apache.xmlbeans.XmlAnySimpleType)get_store().find_attribute_user(XMLLANG$10);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlAnySimpleType)get_store().add_attribute_user(XMLLANG$10);
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
            target = (org.apache.xmlbeans.XmlAnySimpleType)get_store().add_attribute_user(XMLLANG$10);
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
            get_store().remove_attribute(XMLLANG$10);
        }
    }
}
