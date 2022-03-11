/*
 * XML Type:  Classifica
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.Classifica
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * An XML Classifica(@http://www.digitPa.gov.it/protocollo/).
 *
 * This is a complex type.
 */
public class ClassificaImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.Classifica
{
    
    public ClassificaImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName CODICEAMMINISTRAZIONE$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "CodiceAmministrazione");
    private static final javax.xml.namespace.QName CODICEAOO$2 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "CodiceAOO");
    private static final javax.xml.namespace.QName DENOMINAZIONE$4 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Denominazione");
    private static final javax.xml.namespace.QName LIVELLO$6 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Livello");
    
    
    /**
     * Gets the "CodiceAmministrazione" element
     */
    public it.gov.digitpa.www.protocollo.CodiceAmministrazione getCodiceAmministrazione()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.CodiceAmministrazione target = null;
            target = (it.gov.digitpa.www.protocollo.CodiceAmministrazione)get_store().find_element_user(CODICEAMMINISTRAZIONE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "CodiceAmministrazione" element
     */
    public boolean isSetCodiceAmministrazione()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(CODICEAMMINISTRAZIONE$0) != 0;
        }
    }
    
    /**
     * Sets the "CodiceAmministrazione" element
     */
    public void setCodiceAmministrazione(it.gov.digitpa.www.protocollo.CodiceAmministrazione codiceAmministrazione)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.CodiceAmministrazione target = null;
            target = (it.gov.digitpa.www.protocollo.CodiceAmministrazione)get_store().find_element_user(CODICEAMMINISTRAZIONE$0, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.CodiceAmministrazione)get_store().add_element_user(CODICEAMMINISTRAZIONE$0);
            }
            target.set(codiceAmministrazione);
        }
    }
    
    /**
     * Appends and returns a new empty "CodiceAmministrazione" element
     */
    public it.gov.digitpa.www.protocollo.CodiceAmministrazione addNewCodiceAmministrazione()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.CodiceAmministrazione target = null;
            target = (it.gov.digitpa.www.protocollo.CodiceAmministrazione)get_store().add_element_user(CODICEAMMINISTRAZIONE$0);
            return target;
        }
    }
    
    /**
     * Unsets the "CodiceAmministrazione" element
     */
    public void unsetCodiceAmministrazione()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(CODICEAMMINISTRAZIONE$0, 0);
        }
    }
    
    /**
     * Gets the "CodiceAOO" element
     */
    public it.gov.digitpa.www.protocollo.CodiceAOO getCodiceAOO()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.CodiceAOO target = null;
            target = (it.gov.digitpa.www.protocollo.CodiceAOO)get_store().find_element_user(CODICEAOO$2, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "CodiceAOO" element
     */
    public boolean isSetCodiceAOO()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(CODICEAOO$2) != 0;
        }
    }
    
    /**
     * Sets the "CodiceAOO" element
     */
    public void setCodiceAOO(it.gov.digitpa.www.protocollo.CodiceAOO codiceAOO)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.CodiceAOO target = null;
            target = (it.gov.digitpa.www.protocollo.CodiceAOO)get_store().find_element_user(CODICEAOO$2, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.CodiceAOO)get_store().add_element_user(CODICEAOO$2);
            }
            target.set(codiceAOO);
        }
    }
    
    /**
     * Appends and returns a new empty "CodiceAOO" element
     */
    public it.gov.digitpa.www.protocollo.CodiceAOO addNewCodiceAOO()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.CodiceAOO target = null;
            target = (it.gov.digitpa.www.protocollo.CodiceAOO)get_store().add_element_user(CODICEAOO$2);
            return target;
        }
    }
    
    /**
     * Unsets the "CodiceAOO" element
     */
    public void unsetCodiceAOO()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(CODICEAOO$2, 0);
        }
    }
    
    /**
     * Gets the "Denominazione" element
     */
    public it.gov.digitpa.www.protocollo.Denominazione getDenominazione()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Denominazione target = null;
            target = (it.gov.digitpa.www.protocollo.Denominazione)get_store().find_element_user(DENOMINAZIONE$4, 0);
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
            return get_store().count_elements(DENOMINAZIONE$4) != 0;
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
            target = (it.gov.digitpa.www.protocollo.Denominazione)get_store().find_element_user(DENOMINAZIONE$4, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.Denominazione)get_store().add_element_user(DENOMINAZIONE$4);
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
            target = (it.gov.digitpa.www.protocollo.Denominazione)get_store().add_element_user(DENOMINAZIONE$4);
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
            get_store().remove_element(DENOMINAZIONE$4, 0);
        }
    }
    
    /**
     * Gets array of all "Livello" elements
     */
    public it.gov.digitpa.www.protocollo.Livello[] getLivelloArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(LIVELLO$6, targetList);
            it.gov.digitpa.www.protocollo.Livello[] result = new it.gov.digitpa.www.protocollo.Livello[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "Livello" element
     */
    public it.gov.digitpa.www.protocollo.Livello getLivelloArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Livello target = null;
            target = (it.gov.digitpa.www.protocollo.Livello)get_store().find_element_user(LIVELLO$6, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "Livello" element
     */
    public int sizeOfLivelloArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(LIVELLO$6);
        }
    }
    
    /**
     * Sets array of all "Livello" element
     */
    public void setLivelloArray(it.gov.digitpa.www.protocollo.Livello[] livelloArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(livelloArray, LIVELLO$6);
        }
    }
    
    /**
     * Sets ith "Livello" element
     */
    public void setLivelloArray(int i, it.gov.digitpa.www.protocollo.Livello livello)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Livello target = null;
            target = (it.gov.digitpa.www.protocollo.Livello)get_store().find_element_user(LIVELLO$6, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(livello);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "Livello" element
     */
    public it.gov.digitpa.www.protocollo.Livello insertNewLivello(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Livello target = null;
            target = (it.gov.digitpa.www.protocollo.Livello)get_store().insert_element_user(LIVELLO$6, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "Livello" element
     */
    public it.gov.digitpa.www.protocollo.Livello addNewLivello()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Livello target = null;
            target = (it.gov.digitpa.www.protocollo.Livello)get_store().add_element_user(LIVELLO$6);
            return target;
        }
    }
    
    /**
     * Removes the ith "Livello" element
     */
    public void removeLivello(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(LIVELLO$6, i);
        }
    }
}
