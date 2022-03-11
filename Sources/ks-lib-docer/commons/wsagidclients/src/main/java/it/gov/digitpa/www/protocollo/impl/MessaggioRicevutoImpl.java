/*
 * XML Type:  MessaggioRicevuto
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.MessaggioRicevuto
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * An XML MessaggioRicevuto(@http://www.digitPa.gov.it/protocollo/).
 *
 * This is a complex type.
 */
public class MessaggioRicevutoImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.MessaggioRicevuto
{
    
    public MessaggioRicevutoImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName IDENTIFICATORE$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Identificatore");
    private static final javax.xml.namespace.QName PRIMAREGISTRAZIONE$2 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "PrimaRegistrazione");
    private static final javax.xml.namespace.QName DESCRIZIONEMESSAGGIO$4 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "DescrizioneMessaggio");
    
    
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
     * True if has "Identificatore" element
     */
    public boolean isSetIdentificatore()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(IDENTIFICATORE$0) != 0;
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
     * Unsets the "Identificatore" element
     */
    public void unsetIdentificatore()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(IDENTIFICATORE$0, 0);
        }
    }
    
    /**
     * Gets the "PrimaRegistrazione" element
     */
    public it.gov.digitpa.www.protocollo.PrimaRegistrazione getPrimaRegistrazione()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.PrimaRegistrazione target = null;
            target = (it.gov.digitpa.www.protocollo.PrimaRegistrazione)get_store().find_element_user(PRIMAREGISTRAZIONE$2, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "PrimaRegistrazione" element
     */
    public boolean isSetPrimaRegistrazione()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(PRIMAREGISTRAZIONE$2) != 0;
        }
    }
    
    /**
     * Sets the "PrimaRegistrazione" element
     */
    public void setPrimaRegistrazione(it.gov.digitpa.www.protocollo.PrimaRegistrazione primaRegistrazione)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.PrimaRegistrazione target = null;
            target = (it.gov.digitpa.www.protocollo.PrimaRegistrazione)get_store().find_element_user(PRIMAREGISTRAZIONE$2, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.PrimaRegistrazione)get_store().add_element_user(PRIMAREGISTRAZIONE$2);
            }
            target.set(primaRegistrazione);
        }
    }
    
    /**
     * Appends and returns a new empty "PrimaRegistrazione" element
     */
    public it.gov.digitpa.www.protocollo.PrimaRegistrazione addNewPrimaRegistrazione()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.PrimaRegistrazione target = null;
            target = (it.gov.digitpa.www.protocollo.PrimaRegistrazione)get_store().add_element_user(PRIMAREGISTRAZIONE$2);
            return target;
        }
    }
    
    /**
     * Unsets the "PrimaRegistrazione" element
     */
    public void unsetPrimaRegistrazione()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(PRIMAREGISTRAZIONE$2, 0);
        }
    }
    
    /**
     * Gets the "DescrizioneMessaggio" element
     */
    public it.gov.digitpa.www.protocollo.DescrizioneMessaggio getDescrizioneMessaggio()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.DescrizioneMessaggio target = null;
            target = (it.gov.digitpa.www.protocollo.DescrizioneMessaggio)get_store().find_element_user(DESCRIZIONEMESSAGGIO$4, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "DescrizioneMessaggio" element
     */
    public boolean isSetDescrizioneMessaggio()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(DESCRIZIONEMESSAGGIO$4) != 0;
        }
    }
    
    /**
     * Sets the "DescrizioneMessaggio" element
     */
    public void setDescrizioneMessaggio(it.gov.digitpa.www.protocollo.DescrizioneMessaggio descrizioneMessaggio)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.DescrizioneMessaggio target = null;
            target = (it.gov.digitpa.www.protocollo.DescrizioneMessaggio)get_store().find_element_user(DESCRIZIONEMESSAGGIO$4, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.DescrizioneMessaggio)get_store().add_element_user(DESCRIZIONEMESSAGGIO$4);
            }
            target.set(descrizioneMessaggio);
        }
    }
    
    /**
     * Appends and returns a new empty "DescrizioneMessaggio" element
     */
    public it.gov.digitpa.www.protocollo.DescrizioneMessaggio addNewDescrizioneMessaggio()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.DescrizioneMessaggio target = null;
            target = (it.gov.digitpa.www.protocollo.DescrizioneMessaggio)get_store().add_element_user(DESCRIZIONEMESSAGGIO$4);
            return target;
        }
    }
    
    /**
     * Unsets the "DescrizioneMessaggio" element
     */
    public void unsetDescrizioneMessaggio()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(DESCRIZIONEMESSAGGIO$4, 0);
        }
    }
}
