/*
 * XML Type:  Origine
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.Origine
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * An XML Origine(@http://www.digitPa.gov.it/protocollo/).
 *
 * This is a complex type.
 */
public class OrigineImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.Origine
{
    
    public OrigineImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName INDIRIZZOTELEMATICO$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "IndirizzoTelematico");
    private static final javax.xml.namespace.QName MITTENTE$2 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Mittente");
    
    
    /**
     * Gets the "IndirizzoTelematico" element
     */
    public it.gov.digitpa.www.protocollo.IndirizzoTelematico getIndirizzoTelematico()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.IndirizzoTelematico target = null;
            target = (it.gov.digitpa.www.protocollo.IndirizzoTelematico)get_store().find_element_user(INDIRIZZOTELEMATICO$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "IndirizzoTelematico" element
     */
    public void setIndirizzoTelematico(it.gov.digitpa.www.protocollo.IndirizzoTelematico indirizzoTelematico)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.IndirizzoTelematico target = null;
            target = (it.gov.digitpa.www.protocollo.IndirizzoTelematico)get_store().find_element_user(INDIRIZZOTELEMATICO$0, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.IndirizzoTelematico)get_store().add_element_user(INDIRIZZOTELEMATICO$0);
            }
            target.set(indirizzoTelematico);
        }
    }
    
    /**
     * Appends and returns a new empty "IndirizzoTelematico" element
     */
    public it.gov.digitpa.www.protocollo.IndirizzoTelematico addNewIndirizzoTelematico()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.IndirizzoTelematico target = null;
            target = (it.gov.digitpa.www.protocollo.IndirizzoTelematico)get_store().add_element_user(INDIRIZZOTELEMATICO$0);
            return target;
        }
    }
    
    /**
     * Gets the "Mittente" element
     */
    public it.gov.digitpa.www.protocollo.Mittente getMittente()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Mittente target = null;
            target = (it.gov.digitpa.www.protocollo.Mittente)get_store().find_element_user(MITTENTE$2, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Mittente" element
     */
    public void setMittente(it.gov.digitpa.www.protocollo.Mittente mittente)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Mittente target = null;
            target = (it.gov.digitpa.www.protocollo.Mittente)get_store().find_element_user(MITTENTE$2, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.Mittente)get_store().add_element_user(MITTENTE$2);
            }
            target.set(mittente);
        }
    }
    
    /**
     * Appends and returns a new empty "Mittente" element
     */
    public it.gov.digitpa.www.protocollo.Mittente addNewMittente()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Mittente target = null;
            target = (it.gov.digitpa.www.protocollo.Mittente)get_store().add_element_user(MITTENTE$2);
            return target;
        }
    }
}
