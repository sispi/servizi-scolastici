/*
 * XML Type:  MetadatiEsterni
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.MetadatiEsterni
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * An XML MetadatiEsterni(@http://www.digitPa.gov.it/protocollo/).
 *
 * This is a complex type.
 */
public class MetadatiEsterniImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.MetadatiEsterni
{
    
    public MetadatiEsterniImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName NOMEFILE$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "NomeFile");
    private static final javax.xml.namespace.QName IMPRONTA$2 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Impronta");
    private static final javax.xml.namespace.QName CODIFICA$4 = 
        new javax.xml.namespace.QName("", "codifica");
    private static final javax.xml.namespace.QName ESTENSIONE$6 = 
        new javax.xml.namespace.QName("", "estensione");
    private static final javax.xml.namespace.QName FORMATO$8 = 
        new javax.xml.namespace.QName("", "formato");
    
    
    /**
     * Gets the "NomeFile" element
     */
    public it.gov.digitpa.www.protocollo.NomeFile getNomeFile()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.NomeFile target = null;
            target = (it.gov.digitpa.www.protocollo.NomeFile)get_store().find_element_user(NOMEFILE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "NomeFile" element
     */
    public void setNomeFile(it.gov.digitpa.www.protocollo.NomeFile nomeFile)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.NomeFile target = null;
            target = (it.gov.digitpa.www.protocollo.NomeFile)get_store().find_element_user(NOMEFILE$0, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.NomeFile)get_store().add_element_user(NOMEFILE$0);
            }
            target.set(nomeFile);
        }
    }
    
    /**
     * Appends and returns a new empty "NomeFile" element
     */
    public it.gov.digitpa.www.protocollo.NomeFile addNewNomeFile()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.NomeFile target = null;
            target = (it.gov.digitpa.www.protocollo.NomeFile)get_store().add_element_user(NOMEFILE$0);
            return target;
        }
    }
    
    /**
     * Gets the "Impronta" element
     */
    public it.gov.digitpa.www.protocollo.Impronta getImpronta()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Impronta target = null;
            target = (it.gov.digitpa.www.protocollo.Impronta)get_store().find_element_user(IMPRONTA$2, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "Impronta" element
     */
    public boolean isSetImpronta()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(IMPRONTA$2) != 0;
        }
    }
    
    /**
     * Sets the "Impronta" element
     */
    public void setImpronta(it.gov.digitpa.www.protocollo.Impronta impronta)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Impronta target = null;
            target = (it.gov.digitpa.www.protocollo.Impronta)get_store().find_element_user(IMPRONTA$2, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.Impronta)get_store().add_element_user(IMPRONTA$2);
            }
            target.set(impronta);
        }
    }
    
    /**
     * Appends and returns a new empty "Impronta" element
     */
    public it.gov.digitpa.www.protocollo.Impronta addNewImpronta()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Impronta target = null;
            target = (it.gov.digitpa.www.protocollo.Impronta)get_store().add_element_user(IMPRONTA$2);
            return target;
        }
    }
    
    /**
     * Unsets the "Impronta" element
     */
    public void unsetImpronta()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(IMPRONTA$2, 0);
        }
    }
    
    /**
     * Gets the "codifica" attribute
     */
    public it.gov.digitpa.www.protocollo.MetadatiEsterni.Codifica.Enum getCodifica()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(CODIFICA$4);
            if (target == null)
            {
                return null;
            }
            return (it.gov.digitpa.www.protocollo.MetadatiEsterni.Codifica.Enum)target.getEnumValue();
        }
    }
    
    /**
     * Gets (as xml) the "codifica" attribute
     */
    public it.gov.digitpa.www.protocollo.MetadatiEsterni.Codifica xgetCodifica()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.MetadatiEsterni.Codifica target = null;
            target = (it.gov.digitpa.www.protocollo.MetadatiEsterni.Codifica)get_store().find_attribute_user(CODIFICA$4);
            return target;
        }
    }
    
    /**
     * Sets the "codifica" attribute
     */
    public void setCodifica(it.gov.digitpa.www.protocollo.MetadatiEsterni.Codifica.Enum codifica)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(CODIFICA$4);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(CODIFICA$4);
            }
            target.setEnumValue(codifica);
        }
    }
    
    /**
     * Sets (as xml) the "codifica" attribute
     */
    public void xsetCodifica(it.gov.digitpa.www.protocollo.MetadatiEsterni.Codifica codifica)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.MetadatiEsterni.Codifica target = null;
            target = (it.gov.digitpa.www.protocollo.MetadatiEsterni.Codifica)get_store().find_attribute_user(CODIFICA$4);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.MetadatiEsterni.Codifica)get_store().add_attribute_user(CODIFICA$4);
            }
            target.set(codifica);
        }
    }
    
    /**
     * Gets the "estensione" attribute
     */
    public java.lang.String getEstensione()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(ESTENSIONE$6);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "estensione" attribute
     */
    public org.apache.xmlbeans.XmlNMTOKEN xgetEstensione()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlNMTOKEN target = null;
            target = (org.apache.xmlbeans.XmlNMTOKEN)get_store().find_attribute_user(ESTENSIONE$6);
            return target;
        }
    }
    
    /**
     * True if has "estensione" attribute
     */
    public boolean isSetEstensione()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().find_attribute_user(ESTENSIONE$6) != null;
        }
    }
    
    /**
     * Sets the "estensione" attribute
     */
    public void setEstensione(java.lang.String estensione)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(ESTENSIONE$6);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(ESTENSIONE$6);
            }
            target.setStringValue(estensione);
        }
    }
    
    /**
     * Sets (as xml) the "estensione" attribute
     */
    public void xsetEstensione(org.apache.xmlbeans.XmlNMTOKEN estensione)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlNMTOKEN target = null;
            target = (org.apache.xmlbeans.XmlNMTOKEN)get_store().find_attribute_user(ESTENSIONE$6);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlNMTOKEN)get_store().add_attribute_user(ESTENSIONE$6);
            }
            target.set(estensione);
        }
    }
    
    /**
     * Unsets the "estensione" attribute
     */
    public void unsetEstensione()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_attribute(ESTENSIONE$6);
        }
    }
    
    /**
     * Gets the "formato" attribute
     */
    public org.apache.xmlbeans.XmlAnySimpleType getFormato()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlAnySimpleType target = null;
            target = (org.apache.xmlbeans.XmlAnySimpleType)get_store().find_attribute_user(FORMATO$8);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "formato" attribute
     */
    public void setFormato(org.apache.xmlbeans.XmlAnySimpleType formato)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlAnySimpleType target = null;
            target = (org.apache.xmlbeans.XmlAnySimpleType)get_store().find_attribute_user(FORMATO$8);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlAnySimpleType)get_store().add_attribute_user(FORMATO$8);
            }
            target.set(formato);
        }
    }
    
    /**
     * Appends and returns a new empty "formato" attribute
     */
    public org.apache.xmlbeans.XmlAnySimpleType addNewFormato()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlAnySimpleType target = null;
            target = (org.apache.xmlbeans.XmlAnySimpleType)get_store().add_attribute_user(FORMATO$8);
            return target;
        }
    }
    /**
     * An XML codifica(@).
     *
     * This is an atomic type that is a restriction of it.gov.digitpa.www.protocollo.MetadatiEsterni$Codifica.
     */
    public static class CodificaImpl extends org.apache.xmlbeans.impl.values.JavaStringEnumerationHolderEx implements it.gov.digitpa.www.protocollo.MetadatiEsterni.Codifica
    {
        
        public CodificaImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType, false);
        }
        
        protected CodificaImpl(org.apache.xmlbeans.SchemaType sType, boolean b)
        {
            super(sType, b);
        }
    }
}
