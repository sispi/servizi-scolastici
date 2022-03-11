/*
 * XML Type:  Identificatore
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.Identificatore
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * An XML Identificatore(@http://www.digitPa.gov.it/protocollo/).
 *
 * This is a complex type.
 */
public class IdentificatoreImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.Identificatore
{
    
    public IdentificatoreImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName CODICEAMMINISTRAZIONE$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "CodiceAmministrazione");
    private static final javax.xml.namespace.QName CODICEAOO$2 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "CodiceAOO");
    private static final javax.xml.namespace.QName CODICEREGISTRO$4 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "CodiceRegistro");
    private static final javax.xml.namespace.QName NUMEROREGISTRAZIONE$6 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "NumeroRegistrazione");
    private static final javax.xml.namespace.QName DATAREGISTRAZIONE$8 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "DataRegistrazione");
    
    
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
     * Gets the "CodiceRegistro" element
     */
    public it.gov.digitpa.www.protocollo.CodiceRegistro getCodiceRegistro()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.CodiceRegistro target = null;
            target = (it.gov.digitpa.www.protocollo.CodiceRegistro)get_store().find_element_user(CODICEREGISTRO$4, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "CodiceRegistro" element
     */
    public void setCodiceRegistro(it.gov.digitpa.www.protocollo.CodiceRegistro codiceRegistro)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.CodiceRegistro target = null;
            target = (it.gov.digitpa.www.protocollo.CodiceRegistro)get_store().find_element_user(CODICEREGISTRO$4, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.CodiceRegistro)get_store().add_element_user(CODICEREGISTRO$4);
            }
            target.set(codiceRegistro);
        }
    }
    
    /**
     * Appends and returns a new empty "CodiceRegistro" element
     */
    public it.gov.digitpa.www.protocollo.CodiceRegistro addNewCodiceRegistro()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.CodiceRegistro target = null;
            target = (it.gov.digitpa.www.protocollo.CodiceRegistro)get_store().add_element_user(CODICEREGISTRO$4);
            return target;
        }
    }
    
    /**
     * Gets the "NumeroRegistrazione" element
     */
    public it.gov.digitpa.www.protocollo.NumeroRegistrazione getNumeroRegistrazione()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.NumeroRegistrazione target = null;
            target = (it.gov.digitpa.www.protocollo.NumeroRegistrazione)get_store().find_element_user(NUMEROREGISTRAZIONE$6, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "NumeroRegistrazione" element
     */
    public void setNumeroRegistrazione(it.gov.digitpa.www.protocollo.NumeroRegistrazione numeroRegistrazione)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.NumeroRegistrazione target = null;
            target = (it.gov.digitpa.www.protocollo.NumeroRegistrazione)get_store().find_element_user(NUMEROREGISTRAZIONE$6, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.NumeroRegistrazione)get_store().add_element_user(NUMEROREGISTRAZIONE$6);
            }
            target.set(numeroRegistrazione);
        }
    }
    
    /**
     * Appends and returns a new empty "NumeroRegistrazione" element
     */
    public it.gov.digitpa.www.protocollo.NumeroRegistrazione addNewNumeroRegistrazione()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.NumeroRegistrazione target = null;
            target = (it.gov.digitpa.www.protocollo.NumeroRegistrazione)get_store().add_element_user(NUMEROREGISTRAZIONE$6);
            return target;
        }
    }
    
    /**
     * Gets the "DataRegistrazione" element
     */
    public it.gov.digitpa.www.protocollo.DataRegistrazione getDataRegistrazione()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.DataRegistrazione target = null;
            target = (it.gov.digitpa.www.protocollo.DataRegistrazione)get_store().find_element_user(DATAREGISTRAZIONE$8, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "DataRegistrazione" element
     */
    public void setDataRegistrazione(it.gov.digitpa.www.protocollo.DataRegistrazione dataRegistrazione)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.DataRegistrazione target = null;
            target = (it.gov.digitpa.www.protocollo.DataRegistrazione)get_store().find_element_user(DATAREGISTRAZIONE$8, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.DataRegistrazione)get_store().add_element_user(DATAREGISTRAZIONE$8);
            }
            target.set(dataRegistrazione);
        }
    }
    
    /**
     * Appends and returns a new empty "DataRegistrazione" element
     */
    public it.gov.digitpa.www.protocollo.DataRegistrazione addNewDataRegistrazione()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.DataRegistrazione target = null;
            target = (it.gov.digitpa.www.protocollo.DataRegistrazione)get_store().add_element_user(DATAREGISTRAZIONE$8);
            return target;
        }
    }
}
