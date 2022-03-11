/*
 * XML Type:  Destinatario
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.Destinatario
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * An XML Destinatario(@http://www.digitPa.gov.it/protocollo/).
 *
 * This is a complex type.
 */
public class DestinatarioImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.Destinatario
{
    
    public DestinatarioImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName AMMINISTRAZIONE$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Amministrazione");
    private static final javax.xml.namespace.QName AOO$2 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "AOO");
    private static final javax.xml.namespace.QName DENOMINAZIONE$4 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Denominazione");
    private static final javax.xml.namespace.QName PERSONA$6 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Persona");
    private static final javax.xml.namespace.QName INDIRIZZOTELEMATICO$8 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "IndirizzoTelematico");
    private static final javax.xml.namespace.QName TELEFONO$10 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Telefono");
    private static final javax.xml.namespace.QName FAX$12 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Fax");
    private static final javax.xml.namespace.QName INDIRIZZOPOSTALE$14 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "IndirizzoPostale");
    
    
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
     * True if has "Amministrazione" element
     */
    public boolean isSetAmministrazione()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(AMMINISTRAZIONE$0) != 0;
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
     * Unsets the "Amministrazione" element
     */
    public void unsetAmministrazione()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(AMMINISTRAZIONE$0, 0);
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
     * True if has "AOO" element
     */
    public boolean isSetAOO()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(AOO$2) != 0;
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
    
    /**
     * Unsets the "AOO" element
     */
    public void unsetAOO()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(AOO$2, 0);
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
     * Gets array of all "Persona" elements
     */
    public it.gov.digitpa.www.protocollo.Persona[] getPersonaArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(PERSONA$6, targetList);
            it.gov.digitpa.www.protocollo.Persona[] result = new it.gov.digitpa.www.protocollo.Persona[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "Persona" element
     */
    public it.gov.digitpa.www.protocollo.Persona getPersonaArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Persona target = null;
            target = (it.gov.digitpa.www.protocollo.Persona)get_store().find_element_user(PERSONA$6, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "Persona" element
     */
    public int sizeOfPersonaArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(PERSONA$6);
        }
    }
    
    /**
     * Sets array of all "Persona" element
     */
    public void setPersonaArray(it.gov.digitpa.www.protocollo.Persona[] personaArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(personaArray, PERSONA$6);
        }
    }
    
    /**
     * Sets ith "Persona" element
     */
    public void setPersonaArray(int i, it.gov.digitpa.www.protocollo.Persona persona)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Persona target = null;
            target = (it.gov.digitpa.www.protocollo.Persona)get_store().find_element_user(PERSONA$6, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(persona);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "Persona" element
     */
    public it.gov.digitpa.www.protocollo.Persona insertNewPersona(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Persona target = null;
            target = (it.gov.digitpa.www.protocollo.Persona)get_store().insert_element_user(PERSONA$6, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "Persona" element
     */
    public it.gov.digitpa.www.protocollo.Persona addNewPersona()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Persona target = null;
            target = (it.gov.digitpa.www.protocollo.Persona)get_store().add_element_user(PERSONA$6);
            return target;
        }
    }
    
    /**
     * Removes the ith "Persona" element
     */
    public void removePersona(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(PERSONA$6, i);
        }
    }
    
    /**
     * Gets the "IndirizzoTelematico" element
     */
    public it.gov.digitpa.www.protocollo.IndirizzoTelematico getIndirizzoTelematico()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.IndirizzoTelematico target = null;
            target = (it.gov.digitpa.www.protocollo.IndirizzoTelematico)get_store().find_element_user(INDIRIZZOTELEMATICO$8, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "IndirizzoTelematico" element
     */
    public boolean isSetIndirizzoTelematico()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(INDIRIZZOTELEMATICO$8) != 0;
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
            target = (it.gov.digitpa.www.protocollo.IndirizzoTelematico)get_store().find_element_user(INDIRIZZOTELEMATICO$8, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.IndirizzoTelematico)get_store().add_element_user(INDIRIZZOTELEMATICO$8);
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
            target = (it.gov.digitpa.www.protocollo.IndirizzoTelematico)get_store().add_element_user(INDIRIZZOTELEMATICO$8);
            return target;
        }
    }
    
    /**
     * Unsets the "IndirizzoTelematico" element
     */
    public void unsetIndirizzoTelematico()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(INDIRIZZOTELEMATICO$8, 0);
        }
    }
    
    /**
     * Gets array of all "Telefono" elements
     */
    public it.gov.digitpa.www.protocollo.Telefono[] getTelefonoArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(TELEFONO$10, targetList);
            it.gov.digitpa.www.protocollo.Telefono[] result = new it.gov.digitpa.www.protocollo.Telefono[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "Telefono" element
     */
    public it.gov.digitpa.www.protocollo.Telefono getTelefonoArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Telefono target = null;
            target = (it.gov.digitpa.www.protocollo.Telefono)get_store().find_element_user(TELEFONO$10, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "Telefono" element
     */
    public int sizeOfTelefonoArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(TELEFONO$10);
        }
    }
    
    /**
     * Sets array of all "Telefono" element
     */
    public void setTelefonoArray(it.gov.digitpa.www.protocollo.Telefono[] telefonoArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(telefonoArray, TELEFONO$10);
        }
    }
    
    /**
     * Sets ith "Telefono" element
     */
    public void setTelefonoArray(int i, it.gov.digitpa.www.protocollo.Telefono telefono)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Telefono target = null;
            target = (it.gov.digitpa.www.protocollo.Telefono)get_store().find_element_user(TELEFONO$10, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(telefono);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "Telefono" element
     */
    public it.gov.digitpa.www.protocollo.Telefono insertNewTelefono(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Telefono target = null;
            target = (it.gov.digitpa.www.protocollo.Telefono)get_store().insert_element_user(TELEFONO$10, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "Telefono" element
     */
    public it.gov.digitpa.www.protocollo.Telefono addNewTelefono()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Telefono target = null;
            target = (it.gov.digitpa.www.protocollo.Telefono)get_store().add_element_user(TELEFONO$10);
            return target;
        }
    }
    
    /**
     * Removes the ith "Telefono" element
     */
    public void removeTelefono(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(TELEFONO$10, i);
        }
    }
    
    /**
     * Gets array of all "Fax" elements
     */
    public it.gov.digitpa.www.protocollo.Fax[] getFaxArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(FAX$12, targetList);
            it.gov.digitpa.www.protocollo.Fax[] result = new it.gov.digitpa.www.protocollo.Fax[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "Fax" element
     */
    public it.gov.digitpa.www.protocollo.Fax getFaxArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Fax target = null;
            target = (it.gov.digitpa.www.protocollo.Fax)get_store().find_element_user(FAX$12, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "Fax" element
     */
    public int sizeOfFaxArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(FAX$12);
        }
    }
    
    /**
     * Sets array of all "Fax" element
     */
    public void setFaxArray(it.gov.digitpa.www.protocollo.Fax[] faxArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(faxArray, FAX$12);
        }
    }
    
    /**
     * Sets ith "Fax" element
     */
    public void setFaxArray(int i, it.gov.digitpa.www.protocollo.Fax fax)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Fax target = null;
            target = (it.gov.digitpa.www.protocollo.Fax)get_store().find_element_user(FAX$12, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(fax);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "Fax" element
     */
    public it.gov.digitpa.www.protocollo.Fax insertNewFax(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Fax target = null;
            target = (it.gov.digitpa.www.protocollo.Fax)get_store().insert_element_user(FAX$12, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "Fax" element
     */
    public it.gov.digitpa.www.protocollo.Fax addNewFax()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Fax target = null;
            target = (it.gov.digitpa.www.protocollo.Fax)get_store().add_element_user(FAX$12);
            return target;
        }
    }
    
    /**
     * Removes the ith "Fax" element
     */
    public void removeFax(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(FAX$12, i);
        }
    }
    
    /**
     * Gets the "IndirizzoPostale" element
     */
    public it.gov.digitpa.www.protocollo.IndirizzoPostale getIndirizzoPostale()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.IndirizzoPostale target = null;
            target = (it.gov.digitpa.www.protocollo.IndirizzoPostale)get_store().find_element_user(INDIRIZZOPOSTALE$14, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "IndirizzoPostale" element
     */
    public boolean isSetIndirizzoPostale()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(INDIRIZZOPOSTALE$14) != 0;
        }
    }
    
    /**
     * Sets the "IndirizzoPostale" element
     */
    public void setIndirizzoPostale(it.gov.digitpa.www.protocollo.IndirizzoPostale indirizzoPostale)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.IndirizzoPostale target = null;
            target = (it.gov.digitpa.www.protocollo.IndirizzoPostale)get_store().find_element_user(INDIRIZZOPOSTALE$14, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.IndirizzoPostale)get_store().add_element_user(INDIRIZZOPOSTALE$14);
            }
            target.set(indirizzoPostale);
        }
    }
    
    /**
     * Appends and returns a new empty "IndirizzoPostale" element
     */
    public it.gov.digitpa.www.protocollo.IndirizzoPostale addNewIndirizzoPostale()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.IndirizzoPostale target = null;
            target = (it.gov.digitpa.www.protocollo.IndirizzoPostale)get_store().add_element_user(INDIRIZZOPOSTALE$14);
            return target;
        }
    }
    
    /**
     * Unsets the "IndirizzoPostale" element
     */
    public void unsetIndirizzoPostale()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(INDIRIZZOPOSTALE$14, 0);
        }
    }
}
