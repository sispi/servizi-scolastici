/*
 * XML Type:  Persona
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.Persona
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * An XML Persona(@http://www.digitPa.gov.it/protocollo/).
 *
 * This is a complex type.
 */
public class PersonaImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.Persona
{
    
    public PersonaImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName DENOMINAZIONE$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Denominazione");
    private static final javax.xml.namespace.QName NOME$2 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Nome");
    private static final javax.xml.namespace.QName COGNOME$4 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Cognome");
    private static final javax.xml.namespace.QName TITOLO$6 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Titolo");
    private static final javax.xml.namespace.QName CODICEFISCALE$8 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "CodiceFiscale");
    private static final javax.xml.namespace.QName IDENTIFICATIVO$10 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Identificativo");
    private static final javax.xml.namespace.QName ID$12 = 
        new javax.xml.namespace.QName("", "id");
    private static final javax.xml.namespace.QName RIFE$14 = 
        new javax.xml.namespace.QName("", "rife");
    
    
    /**
     * Gets the "Denominazione" element
     */
    public it.gov.digitpa.www.protocollo.Denominazione getDenominazione()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Denominazione target = null;
            target = (it.gov.digitpa.www.protocollo.Denominazione)get_store().find_element_user(DENOMINAZIONE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "Denominazione" element
     */
    public boolean isSetDenominazione()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(DENOMINAZIONE$0) != 0;
        }
    }
    
    /**
     * Sets the "Denominazione" element
     */
    public void setDenominazione(it.gov.digitpa.www.protocollo.Denominazione denominazione)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Denominazione target = null;
            target = (it.gov.digitpa.www.protocollo.Denominazione)get_store().find_element_user(DENOMINAZIONE$0, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.Denominazione)get_store().add_element_user(DENOMINAZIONE$0);
            }
            target.set(denominazione);
        }
    }
    
    /**
     * Appends and returns a new empty "Denominazione" element
     */
    public it.gov.digitpa.www.protocollo.Denominazione addNewDenominazione()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Denominazione target = null;
            target = (it.gov.digitpa.www.protocollo.Denominazione)get_store().add_element_user(DENOMINAZIONE$0);
            return target;
        }
    }
    
    /**
     * Unsets the "Denominazione" element
     */
    public void unsetDenominazione()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(DENOMINAZIONE$0, 0);
        }
    }
    
    /**
     * Gets the "Nome" element
     */
    public it.gov.digitpa.www.protocollo.Nome getNome()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Nome target = null;
            target = (it.gov.digitpa.www.protocollo.Nome)get_store().find_element_user(NOME$2, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "Nome" element
     */
    public boolean isSetNome()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(NOME$2) != 0;
        }
    }
    
    /**
     * Sets the "Nome" element
     */
    public void setNome(it.gov.digitpa.www.protocollo.Nome nome)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Nome target = null;
            target = (it.gov.digitpa.www.protocollo.Nome)get_store().find_element_user(NOME$2, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.Nome)get_store().add_element_user(NOME$2);
            }
            target.set(nome);
        }
    }
    
    /**
     * Appends and returns a new empty "Nome" element
     */
    public it.gov.digitpa.www.protocollo.Nome addNewNome()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Nome target = null;
            target = (it.gov.digitpa.www.protocollo.Nome)get_store().add_element_user(NOME$2);
            return target;
        }
    }
    
    /**
     * Unsets the "Nome" element
     */
    public void unsetNome()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(NOME$2, 0);
        }
    }
    
    /**
     * Gets the "Cognome" element
     */
    public it.gov.digitpa.www.protocollo.Cognome getCognome()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Cognome target = null;
            target = (it.gov.digitpa.www.protocollo.Cognome)get_store().find_element_user(COGNOME$4, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "Cognome" element
     */
    public boolean isSetCognome()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(COGNOME$4) != 0;
        }
    }
    
    /**
     * Sets the "Cognome" element
     */
    public void setCognome(it.gov.digitpa.www.protocollo.Cognome cognome)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Cognome target = null;
            target = (it.gov.digitpa.www.protocollo.Cognome)get_store().find_element_user(COGNOME$4, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.Cognome)get_store().add_element_user(COGNOME$4);
            }
            target.set(cognome);
        }
    }
    
    /**
     * Appends and returns a new empty "Cognome" element
     */
    public it.gov.digitpa.www.protocollo.Cognome addNewCognome()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Cognome target = null;
            target = (it.gov.digitpa.www.protocollo.Cognome)get_store().add_element_user(COGNOME$4);
            return target;
        }
    }
    
    /**
     * Unsets the "Cognome" element
     */
    public void unsetCognome()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(COGNOME$4, 0);
        }
    }
    
    /**
     * Gets the "Titolo" element
     */
    public it.gov.digitpa.www.protocollo.Titolo getTitolo()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Titolo target = null;
            target = (it.gov.digitpa.www.protocollo.Titolo)get_store().find_element_user(TITOLO$6, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "Titolo" element
     */
    public boolean isSetTitolo()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(TITOLO$6) != 0;
        }
    }
    
    /**
     * Sets the "Titolo" element
     */
    public void setTitolo(it.gov.digitpa.www.protocollo.Titolo titolo)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Titolo target = null;
            target = (it.gov.digitpa.www.protocollo.Titolo)get_store().find_element_user(TITOLO$6, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.Titolo)get_store().add_element_user(TITOLO$6);
            }
            target.set(titolo);
        }
    }
    
    /**
     * Appends and returns a new empty "Titolo" element
     */
    public it.gov.digitpa.www.protocollo.Titolo addNewTitolo()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Titolo target = null;
            target = (it.gov.digitpa.www.protocollo.Titolo)get_store().add_element_user(TITOLO$6);
            return target;
        }
    }
    
    /**
     * Unsets the "Titolo" element
     */
    public void unsetTitolo()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(TITOLO$6, 0);
        }
    }
    
    /**
     * Gets the "CodiceFiscale" element
     */
    public it.gov.digitpa.www.protocollo.CodiceFiscale getCodiceFiscale()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.CodiceFiscale target = null;
            target = (it.gov.digitpa.www.protocollo.CodiceFiscale)get_store().find_element_user(CODICEFISCALE$8, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "CodiceFiscale" element
     */
    public boolean isSetCodiceFiscale()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(CODICEFISCALE$8) != 0;
        }
    }
    
    /**
     * Sets the "CodiceFiscale" element
     */
    public void setCodiceFiscale(it.gov.digitpa.www.protocollo.CodiceFiscale codiceFiscale)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.CodiceFiscale target = null;
            target = (it.gov.digitpa.www.protocollo.CodiceFiscale)get_store().find_element_user(CODICEFISCALE$8, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.CodiceFiscale)get_store().add_element_user(CODICEFISCALE$8);
            }
            target.set(codiceFiscale);
        }
    }
    
    /**
     * Appends and returns a new empty "CodiceFiscale" element
     */
    public it.gov.digitpa.www.protocollo.CodiceFiscale addNewCodiceFiscale()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.CodiceFiscale target = null;
            target = (it.gov.digitpa.www.protocollo.CodiceFiscale)get_store().add_element_user(CODICEFISCALE$8);
            return target;
        }
    }
    
    /**
     * Unsets the "CodiceFiscale" element
     */
    public void unsetCodiceFiscale()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(CODICEFISCALE$8, 0);
        }
    }
    
    /**
     * Gets the "Identificativo" element
     */
    public it.gov.digitpa.www.protocollo.Identificativo getIdentificativo()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Identificativo target = null;
            target = (it.gov.digitpa.www.protocollo.Identificativo)get_store().find_element_user(IDENTIFICATIVO$10, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "Identificativo" element
     */
    public boolean isSetIdentificativo()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(IDENTIFICATIVO$10) != 0;
        }
    }
    
    /**
     * Sets the "Identificativo" element
     */
    public void setIdentificativo(it.gov.digitpa.www.protocollo.Identificativo identificativo)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Identificativo target = null;
            target = (it.gov.digitpa.www.protocollo.Identificativo)get_store().find_element_user(IDENTIFICATIVO$10, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.Identificativo)get_store().add_element_user(IDENTIFICATIVO$10);
            }
            target.set(identificativo);
        }
    }
    
    /**
     * Appends and returns a new empty "Identificativo" element
     */
    public it.gov.digitpa.www.protocollo.Identificativo addNewIdentificativo()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Identificativo target = null;
            target = (it.gov.digitpa.www.protocollo.Identificativo)get_store().add_element_user(IDENTIFICATIVO$10);
            return target;
        }
    }
    
    /**
     * Unsets the "Identificativo" element
     */
    public void unsetIdentificativo()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(IDENTIFICATIVO$10, 0);
        }
    }
    
    /**
     * Gets the "id" attribute
     */
    public java.lang.String getId()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(ID$12);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "id" attribute
     */
    public org.apache.xmlbeans.XmlID xgetId()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlID target = null;
            target = (org.apache.xmlbeans.XmlID)get_store().find_attribute_user(ID$12);
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
            return get_store().find_attribute_user(ID$12) != null;
        }
    }
    
    /**
     * Sets the "id" attribute
     */
    public void setId(java.lang.String id)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(ID$12);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(ID$12);
            }
            target.setStringValue(id);
        }
    }
    
    /**
     * Sets (as xml) the "id" attribute
     */
    public void xsetId(org.apache.xmlbeans.XmlID id)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlID target = null;
            target = (org.apache.xmlbeans.XmlID)get_store().find_attribute_user(ID$12);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlID)get_store().add_attribute_user(ID$12);
            }
            target.set(id);
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
            get_store().remove_attribute(ID$12);
        }
    }
    
    /**
     * Gets the "rife" attribute
     */
    public java.lang.String getRife()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(RIFE$14);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "rife" attribute
     */
    public org.apache.xmlbeans.XmlIDREF xgetRife()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlIDREF target = null;
            target = (org.apache.xmlbeans.XmlIDREF)get_store().find_attribute_user(RIFE$14);
            return target;
        }
    }
    
    /**
     * True if has "rife" attribute
     */
    public boolean isSetRife()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().find_attribute_user(RIFE$14) != null;
        }
    }
    
    /**
     * Sets the "rife" attribute
     */
    public void setRife(java.lang.String rife)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(RIFE$14);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(RIFE$14);
            }
            target.setStringValue(rife);
        }
    }
    
    /**
     * Sets (as xml) the "rife" attribute
     */
    public void xsetRife(org.apache.xmlbeans.XmlIDREF rife)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlIDREF target = null;
            target = (org.apache.xmlbeans.XmlIDREF)get_store().find_attribute_user(RIFE$14);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlIDREF)get_store().add_attribute_user(RIFE$14);
            }
            target.set(rife);
        }
    }
    
    /**
     * Unsets the "rife" attribute
     */
    public void unsetRife()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_attribute(RIFE$14);
        }
    }
}
