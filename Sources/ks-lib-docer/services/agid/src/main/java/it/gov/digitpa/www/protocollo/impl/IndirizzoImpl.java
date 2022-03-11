/*
 * XML Type:  Indirizzo
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.Indirizzo
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * An XML Indirizzo(@http://www.digitPa.gov.it/protocollo/).
 *
 * This is a complex type.
 */
public class IndirizzoImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.Indirizzo
{
    
    public IndirizzoImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName TOPONIMO$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Toponimo");
    private static final javax.xml.namespace.QName CIVICO$2 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Civico");
    private static final javax.xml.namespace.QName CAP$4 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "CAP");
    private static final javax.xml.namespace.QName COMUNE$6 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Comune");
    private static final javax.xml.namespace.QName PROVINCIA$8 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Provincia");
    private static final javax.xml.namespace.QName NAZIONE$10 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Nazione");
    
    
    /**
     * Gets the "Toponimo" element
     */
    public it.gov.digitpa.www.protocollo.Toponimo getToponimo()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Toponimo target = null;
            target = (it.gov.digitpa.www.protocollo.Toponimo)get_store().find_element_user(TOPONIMO$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Toponimo" element
     */
    public void setToponimo(it.gov.digitpa.www.protocollo.Toponimo toponimo)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Toponimo target = null;
            target = (it.gov.digitpa.www.protocollo.Toponimo)get_store().find_element_user(TOPONIMO$0, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.Toponimo)get_store().add_element_user(TOPONIMO$0);
            }
            target.set(toponimo);
        }
    }
    
    /**
     * Appends and returns a new empty "Toponimo" element
     */
    public it.gov.digitpa.www.protocollo.Toponimo addNewToponimo()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Toponimo target = null;
            target = (it.gov.digitpa.www.protocollo.Toponimo)get_store().add_element_user(TOPONIMO$0);
            return target;
        }
    }
    
    /**
     * Gets the "Civico" element
     */
    public it.gov.digitpa.www.protocollo.Civico getCivico()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Civico target = null;
            target = (it.gov.digitpa.www.protocollo.Civico)get_store().find_element_user(CIVICO$2, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Civico" element
     */
    public void setCivico(it.gov.digitpa.www.protocollo.Civico civico)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Civico target = null;
            target = (it.gov.digitpa.www.protocollo.Civico)get_store().find_element_user(CIVICO$2, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.Civico)get_store().add_element_user(CIVICO$2);
            }
            target.set(civico);
        }
    }
    
    /**
     * Appends and returns a new empty "Civico" element
     */
    public it.gov.digitpa.www.protocollo.Civico addNewCivico()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Civico target = null;
            target = (it.gov.digitpa.www.protocollo.Civico)get_store().add_element_user(CIVICO$2);
            return target;
        }
    }
    
    /**
     * Gets the "CAP" element
     */
    public it.gov.digitpa.www.protocollo.CAP getCAP()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.CAP target = null;
            target = (it.gov.digitpa.www.protocollo.CAP)get_store().find_element_user(CAP$4, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "CAP" element
     */
    public void setCAP(it.gov.digitpa.www.protocollo.CAP cap)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.CAP target = null;
            target = (it.gov.digitpa.www.protocollo.CAP)get_store().find_element_user(CAP$4, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.CAP)get_store().add_element_user(CAP$4);
            }
            target.set(cap);
        }
    }
    
    /**
     * Appends and returns a new empty "CAP" element
     */
    public it.gov.digitpa.www.protocollo.CAP addNewCAP()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.CAP target = null;
            target = (it.gov.digitpa.www.protocollo.CAP)get_store().add_element_user(CAP$4);
            return target;
        }
    }
    
    /**
     * Gets the "Comune" element
     */
    public it.gov.digitpa.www.protocollo.Comune getComune()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Comune target = null;
            target = (it.gov.digitpa.www.protocollo.Comune)get_store().find_element_user(COMUNE$6, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Comune" element
     */
    public void setComune(it.gov.digitpa.www.protocollo.Comune comune)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Comune target = null;
            target = (it.gov.digitpa.www.protocollo.Comune)get_store().find_element_user(COMUNE$6, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.Comune)get_store().add_element_user(COMUNE$6);
            }
            target.set(comune);
        }
    }
    
    /**
     * Appends and returns a new empty "Comune" element
     */
    public it.gov.digitpa.www.protocollo.Comune addNewComune()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Comune target = null;
            target = (it.gov.digitpa.www.protocollo.Comune)get_store().add_element_user(COMUNE$6);
            return target;
        }
    }
    
    /**
     * Gets the "Provincia" element
     */
    public it.gov.digitpa.www.protocollo.Provincia getProvincia()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Provincia target = null;
            target = (it.gov.digitpa.www.protocollo.Provincia)get_store().find_element_user(PROVINCIA$8, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Provincia" element
     */
    public void setProvincia(it.gov.digitpa.www.protocollo.Provincia provincia)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Provincia target = null;
            target = (it.gov.digitpa.www.protocollo.Provincia)get_store().find_element_user(PROVINCIA$8, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.Provincia)get_store().add_element_user(PROVINCIA$8);
            }
            target.set(provincia);
        }
    }
    
    /**
     * Appends and returns a new empty "Provincia" element
     */
    public it.gov.digitpa.www.protocollo.Provincia addNewProvincia()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Provincia target = null;
            target = (it.gov.digitpa.www.protocollo.Provincia)get_store().add_element_user(PROVINCIA$8);
            return target;
        }
    }
    
    /**
     * Gets the "Nazione" element
     */
    public it.gov.digitpa.www.protocollo.Nazione getNazione()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Nazione target = null;
            target = (it.gov.digitpa.www.protocollo.Nazione)get_store().find_element_user(NAZIONE$10, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "Nazione" element
     */
    public boolean isSetNazione()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(NAZIONE$10) != 0;
        }
    }
    
    /**
     * Sets the "Nazione" element
     */
    public void setNazione(it.gov.digitpa.www.protocollo.Nazione nazione)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Nazione target = null;
            target = (it.gov.digitpa.www.protocollo.Nazione)get_store().find_element_user(NAZIONE$10, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.Nazione)get_store().add_element_user(NAZIONE$10);
            }
            target.set(nazione);
        }
    }
    
    /**
     * Appends and returns a new empty "Nazione" element
     */
    public it.gov.digitpa.www.protocollo.Nazione addNewNazione()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Nazione target = null;
            target = (it.gov.digitpa.www.protocollo.Nazione)get_store().add_element_user(NAZIONE$10);
            return target;
        }
    }
    
    /**
     * Unsets the "Nazione" element
     */
    public void unsetNazione()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(NAZIONE$10, 0);
        }
    }
}
