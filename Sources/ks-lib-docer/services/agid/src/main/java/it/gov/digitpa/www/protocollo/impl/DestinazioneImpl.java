/*
 * XML Type:  Destinazione
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.Destinazione
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * An XML Destinazione(@http://www.digitPa.gov.it/protocollo/).
 *
 * This is a complex type.
 */
public class DestinazioneImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.Destinazione
{
    
    public DestinazioneImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName INDIRIZZOTELEMATICO$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "IndirizzoTelematico");
    private static final javax.xml.namespace.QName DESTINATARIO$2 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Destinatario");
    private static final javax.xml.namespace.QName CONFERMARICEZIONE$4 = 
        new javax.xml.namespace.QName("", "confermaRicezione");
    
    
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
     * Gets array of all "Destinatario" elements
     */
    public it.gov.digitpa.www.protocollo.Destinatario[] getDestinatarioArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(DESTINATARIO$2, targetList);
            it.gov.digitpa.www.protocollo.Destinatario[] result = new it.gov.digitpa.www.protocollo.Destinatario[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "Destinatario" element
     */
    public it.gov.digitpa.www.protocollo.Destinatario getDestinatarioArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Destinatario target = null;
            target = (it.gov.digitpa.www.protocollo.Destinatario)get_store().find_element_user(DESTINATARIO$2, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "Destinatario" element
     */
    public int sizeOfDestinatarioArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(DESTINATARIO$2);
        }
    }
    
    /**
     * Sets array of all "Destinatario" element
     */
    public void setDestinatarioArray(it.gov.digitpa.www.protocollo.Destinatario[] destinatarioArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(destinatarioArray, DESTINATARIO$2);
        }
    }
    
    /**
     * Sets ith "Destinatario" element
     */
    public void setDestinatarioArray(int i, it.gov.digitpa.www.protocollo.Destinatario destinatario)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Destinatario target = null;
            target = (it.gov.digitpa.www.protocollo.Destinatario)get_store().find_element_user(DESTINATARIO$2, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(destinatario);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "Destinatario" element
     */
    public it.gov.digitpa.www.protocollo.Destinatario insertNewDestinatario(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Destinatario target = null;
            target = (it.gov.digitpa.www.protocollo.Destinatario)get_store().insert_element_user(DESTINATARIO$2, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "Destinatario" element
     */
    public it.gov.digitpa.www.protocollo.Destinatario addNewDestinatario()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Destinatario target = null;
            target = (it.gov.digitpa.www.protocollo.Destinatario)get_store().add_element_user(DESTINATARIO$2);
            return target;
        }
    }
    
    /**
     * Removes the ith "Destinatario" element
     */
    public void removeDestinatario(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(DESTINATARIO$2, i);
        }
    }
    
    /**
     * Gets the "confermaRicezione" attribute
     */
    public it.gov.digitpa.www.protocollo.Destinazione.ConfermaRicezione.Enum getConfermaRicezione()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(CONFERMARICEZIONE$4);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(CONFERMARICEZIONE$4);
            }
            if (target == null)
            {
                return null;
            }
            return (it.gov.digitpa.www.protocollo.Destinazione.ConfermaRicezione.Enum)target.getEnumValue();
        }
    }
    
    /**
     * Gets (as xml) the "confermaRicezione" attribute
     */
    public it.gov.digitpa.www.protocollo.Destinazione.ConfermaRicezione xgetConfermaRicezione()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Destinazione.ConfermaRicezione target = null;
            target = (it.gov.digitpa.www.protocollo.Destinazione.ConfermaRicezione)get_store().find_attribute_user(CONFERMARICEZIONE$4);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.Destinazione.ConfermaRicezione)get_default_attribute_value(CONFERMARICEZIONE$4);
            }
            return target;
        }
    }
    
    /**
     * True if has "confermaRicezione" attribute
     */
    public boolean isSetConfermaRicezione()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().find_attribute_user(CONFERMARICEZIONE$4) != null;
        }
    }
    
    /**
     * Sets the "confermaRicezione" attribute
     */
    public void setConfermaRicezione(it.gov.digitpa.www.protocollo.Destinazione.ConfermaRicezione.Enum confermaRicezione)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(CONFERMARICEZIONE$4);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(CONFERMARICEZIONE$4);
            }
            target.setEnumValue(confermaRicezione);
        }
    }
    
    /**
     * Sets (as xml) the "confermaRicezione" attribute
     */
    public void xsetConfermaRicezione(it.gov.digitpa.www.protocollo.Destinazione.ConfermaRicezione confermaRicezione)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Destinazione.ConfermaRicezione target = null;
            target = (it.gov.digitpa.www.protocollo.Destinazione.ConfermaRicezione)get_store().find_attribute_user(CONFERMARICEZIONE$4);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.Destinazione.ConfermaRicezione)get_store().add_attribute_user(CONFERMARICEZIONE$4);
            }
            target.set(confermaRicezione);
        }
    }
    
    /**
     * Unsets the "confermaRicezione" attribute
     */
    public void unsetConfermaRicezione()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_attribute(CONFERMARICEZIONE$4);
        }
    }
    /**
     * An XML confermaRicezione(@).
     *
     * This is an atomic type that is a restriction of it.gov.digitpa.www.protocollo.Destinazione$ConfermaRicezione.
     */
    public static class ConfermaRicezioneImpl extends org.apache.xmlbeans.impl.values.JavaStringEnumerationHolderEx implements it.gov.digitpa.www.protocollo.Destinazione.ConfermaRicezione
    {
        
        public ConfermaRicezioneImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType, false);
        }
        
        protected ConfermaRicezioneImpl(org.apache.xmlbeans.SchemaType sType, boolean b)
        {
            super(sType, b);
        }
    }
}
