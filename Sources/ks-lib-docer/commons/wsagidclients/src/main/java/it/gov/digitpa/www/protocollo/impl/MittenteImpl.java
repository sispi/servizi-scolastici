/*
 * XML Type:  Mittente
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.Mittente
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * An XML Mittente(@http://www.digitPa.gov.it/protocollo/).
 *
 * This is a complex type.
 */
public class MittenteImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.Mittente
{
    
    public MittenteImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName AMMINISTRAZIONE$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Amministrazione");
    private static final javax.xml.namespace.QName AOO$2 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "AOO");
    
    
    /**
     * Gets the "Amministrazione" element
     */
    public it.gov.digitpa.www.protocollo.Amministrazione getAmministrazione()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Amministrazione target = null;
            target = (it.gov.digitpa.www.protocollo.Amministrazione)get_store().find_element_user(AMMINISTRAZIONE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Amministrazione" element
     */
    public void setAmministrazione(it.gov.digitpa.www.protocollo.Amministrazione amministrazione)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Amministrazione target = null;
            target = (it.gov.digitpa.www.protocollo.Amministrazione)get_store().find_element_user(AMMINISTRAZIONE$0, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.Amministrazione)get_store().add_element_user(AMMINISTRAZIONE$0);
            }
            target.set(amministrazione);
        }
    }
    
    /**
     * Appends and returns a new empty "Amministrazione" element
     */
    public it.gov.digitpa.www.protocollo.Amministrazione addNewAmministrazione()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Amministrazione target = null;
            target = (it.gov.digitpa.www.protocollo.Amministrazione)get_store().add_element_user(AMMINISTRAZIONE$0);
            return target;
        }
    }
    
    /**
     * Gets the "AOO" element
     */
    public it.gov.digitpa.www.protocollo.AOO getAOO()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.AOO target = null;
            target = (it.gov.digitpa.www.protocollo.AOO)get_store().find_element_user(AOO$2, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "AOO" element
     */
    public void setAOO(it.gov.digitpa.www.protocollo.AOO aoo)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.AOO target = null;
            target = (it.gov.digitpa.www.protocollo.AOO)get_store().find_element_user(AOO$2, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.AOO)get_store().add_element_user(AOO$2);
            }
            target.set(aoo);
        }
    }
    
    /**
     * Appends and returns a new empty "AOO" element
     */
    public it.gov.digitpa.www.protocollo.AOO addNewAOO()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.AOO target = null;
            target = (it.gov.digitpa.www.protocollo.AOO)get_store().add_element_user(AOO$2);
            return target;
        }
    }
}
