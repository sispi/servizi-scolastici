/*
 * XML Type:  UnitaOrganizzativa
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.UnitaOrganizzativa
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * An XML UnitaOrganizzativa(@http://www.digitPa.gov.it/protocollo/).
 *
 * This is a complex type.
 */
public class UnitaOrganizzativaImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.UnitaOrganizzativa
{
    
    public UnitaOrganizzativaImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName DENOMINAZIONE$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Denominazione");
    private static final javax.xml.namespace.QName IDENTIFICATIVO$2 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Identificativo");
    private static final javax.xml.namespace.QName UNITAORGANIZZATIVA$4 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "UnitaOrganizzativa");
    private static final javax.xml.namespace.QName RUOLO$6 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Ruolo");
    private static final javax.xml.namespace.QName PERSONA$8 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Persona");
    private static final javax.xml.namespace.QName INDIRIZZOPOSTALE$10 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "IndirizzoPostale");
    private static final javax.xml.namespace.QName INDIRIZZOTELEMATICO$12 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "IndirizzoTelematico");
    private static final javax.xml.namespace.QName TELEFONO$14 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Telefono");
    private static final javax.xml.namespace.QName FAX$16 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Fax");
    private static final javax.xml.namespace.QName TIPO$18 = 
        new javax.xml.namespace.QName("", "tipo");
    
    
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
     * Gets the "Identificativo" element
     */
    public it.gov.digitpa.www.protocollo.Identificativo getIdentificativo()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Identificativo target = null;
            target = (it.gov.digitpa.www.protocollo.Identificativo)get_store().find_element_user(IDENTIFICATIVO$2, 0);
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
            return get_store().count_elements(IDENTIFICATIVO$2) != 0;
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
            target = (it.gov.digitpa.www.protocollo.Identificativo)get_store().find_element_user(IDENTIFICATIVO$2, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.Identificativo)get_store().add_element_user(IDENTIFICATIVO$2);
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
            target = (it.gov.digitpa.www.protocollo.Identificativo)get_store().add_element_user(IDENTIFICATIVO$2);
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
            get_store().remove_element(IDENTIFICATIVO$2, 0);
        }
    }
    
    /**
     * Gets the "UnitaOrganizzativa" element
     */
    public it.gov.digitpa.www.protocollo.UnitaOrganizzativa getUnitaOrganizzativa()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.UnitaOrganizzativa target = null;
            target = (it.gov.digitpa.www.protocollo.UnitaOrganizzativa)get_store().find_element_user(UNITAORGANIZZATIVA$4, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "UnitaOrganizzativa" element
     */
    public boolean isSetUnitaOrganizzativa()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(UNITAORGANIZZATIVA$4) != 0;
        }
    }
    
    /**
     * Sets the "UnitaOrganizzativa" element
     */
    public void setUnitaOrganizzativa(it.gov.digitpa.www.protocollo.UnitaOrganizzativa unitaOrganizzativa)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.UnitaOrganizzativa target = null;
            target = (it.gov.digitpa.www.protocollo.UnitaOrganizzativa)get_store().find_element_user(UNITAORGANIZZATIVA$4, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.UnitaOrganizzativa)get_store().add_element_user(UNITAORGANIZZATIVA$4);
            }
            target.set(unitaOrganizzativa);
        }
    }
    
    /**
     * Appends and returns a new empty "UnitaOrganizzativa" element
     */
    public it.gov.digitpa.www.protocollo.UnitaOrganizzativa addNewUnitaOrganizzativa()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.UnitaOrganizzativa target = null;
            target = (it.gov.digitpa.www.protocollo.UnitaOrganizzativa)get_store().add_element_user(UNITAORGANIZZATIVA$4);
            return target;
        }
    }
    
    /**
     * Unsets the "UnitaOrganizzativa" element
     */
    public void unsetUnitaOrganizzativa()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(UNITAORGANIZZATIVA$4, 0);
        }
    }
    
    /**
     * Gets array of all "Ruolo" elements
     */
    public it.gov.digitpa.www.protocollo.Ruolo[] getRuoloArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(RUOLO$6, targetList);
            it.gov.digitpa.www.protocollo.Ruolo[] result = new it.gov.digitpa.www.protocollo.Ruolo[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "Ruolo" element
     */
    public it.gov.digitpa.www.protocollo.Ruolo getRuoloArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Ruolo target = null;
            target = (it.gov.digitpa.www.protocollo.Ruolo)get_store().find_element_user(RUOLO$6, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "Ruolo" element
     */
    public int sizeOfRuoloArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(RUOLO$6);
        }
    }
    
    /**
     * Sets array of all "Ruolo" element
     */
    public void setRuoloArray(it.gov.digitpa.www.protocollo.Ruolo[] ruoloArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(ruoloArray, RUOLO$6);
        }
    }
    
    /**
     * Sets ith "Ruolo" element
     */
    public void setRuoloArray(int i, it.gov.digitpa.www.protocollo.Ruolo ruolo)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Ruolo target = null;
            target = (it.gov.digitpa.www.protocollo.Ruolo)get_store().find_element_user(RUOLO$6, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(ruolo);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "Ruolo" element
     */
    public it.gov.digitpa.www.protocollo.Ruolo insertNewRuolo(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Ruolo target = null;
            target = (it.gov.digitpa.www.protocollo.Ruolo)get_store().insert_element_user(RUOLO$6, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "Ruolo" element
     */
    public it.gov.digitpa.www.protocollo.Ruolo addNewRuolo()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Ruolo target = null;
            target = (it.gov.digitpa.www.protocollo.Ruolo)get_store().add_element_user(RUOLO$6);
            return target;
        }
    }
    
    /**
     * Removes the ith "Ruolo" element
     */
    public void removeRuolo(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(RUOLO$6, i);
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
            get_store().find_all_element_users(PERSONA$8, targetList);
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
            target = (it.gov.digitpa.www.protocollo.Persona)get_store().find_element_user(PERSONA$8, i);
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
            return get_store().count_elements(PERSONA$8);
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
            arraySetterHelper(personaArray, PERSONA$8);
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
            target = (it.gov.digitpa.www.protocollo.Persona)get_store().find_element_user(PERSONA$8, i);
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
            target = (it.gov.digitpa.www.protocollo.Persona)get_store().insert_element_user(PERSONA$8, i);
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
            target = (it.gov.digitpa.www.protocollo.Persona)get_store().add_element_user(PERSONA$8);
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
            get_store().remove_element(PERSONA$8, i);
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
            target = (it.gov.digitpa.www.protocollo.IndirizzoPostale)get_store().find_element_user(INDIRIZZOPOSTALE$10, 0);
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
            return get_store().count_elements(INDIRIZZOPOSTALE$10) != 0;
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
            target = (it.gov.digitpa.www.protocollo.IndirizzoPostale)get_store().find_element_user(INDIRIZZOPOSTALE$10, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.IndirizzoPostale)get_store().add_element_user(INDIRIZZOPOSTALE$10);
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
            target = (it.gov.digitpa.www.protocollo.IndirizzoPostale)get_store().add_element_user(INDIRIZZOPOSTALE$10);
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
            get_store().remove_element(INDIRIZZOPOSTALE$10, 0);
        }
    }
    
    /**
     * Gets array of all "IndirizzoTelematico" elements
     */
    public it.gov.digitpa.www.protocollo.IndirizzoTelematico[] getIndirizzoTelematicoArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(INDIRIZZOTELEMATICO$12, targetList);
            it.gov.digitpa.www.protocollo.IndirizzoTelematico[] result = new it.gov.digitpa.www.protocollo.IndirizzoTelematico[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "IndirizzoTelematico" element
     */
    public it.gov.digitpa.www.protocollo.IndirizzoTelematico getIndirizzoTelematicoArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.IndirizzoTelematico target = null;
            target = (it.gov.digitpa.www.protocollo.IndirizzoTelematico)get_store().find_element_user(INDIRIZZOTELEMATICO$12, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "IndirizzoTelematico" element
     */
    public int sizeOfIndirizzoTelematicoArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(INDIRIZZOTELEMATICO$12);
        }
    }
    
    /**
     * Sets array of all "IndirizzoTelematico" element
     */
    public void setIndirizzoTelematicoArray(it.gov.digitpa.www.protocollo.IndirizzoTelematico[] indirizzoTelematicoArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(indirizzoTelematicoArray, INDIRIZZOTELEMATICO$12);
        }
    }
    
    /**
     * Sets ith "IndirizzoTelematico" element
     */
    public void setIndirizzoTelematicoArray(int i, it.gov.digitpa.www.protocollo.IndirizzoTelematico indirizzoTelematico)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.IndirizzoTelematico target = null;
            target = (it.gov.digitpa.www.protocollo.IndirizzoTelematico)get_store().find_element_user(INDIRIZZOTELEMATICO$12, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(indirizzoTelematico);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "IndirizzoTelematico" element
     */
    public it.gov.digitpa.www.protocollo.IndirizzoTelematico insertNewIndirizzoTelematico(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.IndirizzoTelematico target = null;
            target = (it.gov.digitpa.www.protocollo.IndirizzoTelematico)get_store().insert_element_user(INDIRIZZOTELEMATICO$12, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "IndirizzoTelematico" element
     */
    public it.gov.digitpa.www.protocollo.IndirizzoTelematico addNewIndirizzoTelematico()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.IndirizzoTelematico target = null;
            target = (it.gov.digitpa.www.protocollo.IndirizzoTelematico)get_store().add_element_user(INDIRIZZOTELEMATICO$12);
            return target;
        }
    }
    
    /**
     * Removes the ith "IndirizzoTelematico" element
     */
    public void removeIndirizzoTelematico(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(INDIRIZZOTELEMATICO$12, i);
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
            get_store().find_all_element_users(TELEFONO$14, targetList);
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
            target = (it.gov.digitpa.www.protocollo.Telefono)get_store().find_element_user(TELEFONO$14, i);
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
            return get_store().count_elements(TELEFONO$14);
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
            arraySetterHelper(telefonoArray, TELEFONO$14);
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
            target = (it.gov.digitpa.www.protocollo.Telefono)get_store().find_element_user(TELEFONO$14, i);
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
            target = (it.gov.digitpa.www.protocollo.Telefono)get_store().insert_element_user(TELEFONO$14, i);
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
            target = (it.gov.digitpa.www.protocollo.Telefono)get_store().add_element_user(TELEFONO$14);
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
            get_store().remove_element(TELEFONO$14, i);
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
            get_store().find_all_element_users(FAX$16, targetList);
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
            target = (it.gov.digitpa.www.protocollo.Fax)get_store().find_element_user(FAX$16, i);
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
            return get_store().count_elements(FAX$16);
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
            arraySetterHelper(faxArray, FAX$16);
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
            target = (it.gov.digitpa.www.protocollo.Fax)get_store().find_element_user(FAX$16, i);
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
            target = (it.gov.digitpa.www.protocollo.Fax)get_store().insert_element_user(FAX$16, i);
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
            target = (it.gov.digitpa.www.protocollo.Fax)get_store().add_element_user(FAX$16);
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
            get_store().remove_element(FAX$16, i);
        }
    }
    
    /**
     * Gets the "tipo" attribute
     */
    public it.gov.digitpa.www.protocollo.UnitaOrganizzativa.Tipo.Enum getTipo()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(TIPO$18);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(TIPO$18);
            }
            if (target == null)
            {
                return null;
            }
            return (it.gov.digitpa.www.protocollo.UnitaOrganizzativa.Tipo.Enum)target.getEnumValue();
        }
    }
    
    /**
     * Gets (as xml) the "tipo" attribute
     */
    public it.gov.digitpa.www.protocollo.UnitaOrganizzativa.Tipo xgetTipo()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.UnitaOrganizzativa.Tipo target = null;
            target = (it.gov.digitpa.www.protocollo.UnitaOrganizzativa.Tipo)get_store().find_attribute_user(TIPO$18);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.UnitaOrganizzativa.Tipo)get_default_attribute_value(TIPO$18);
            }
            return target;
        }
    }
    
    /**
     * True if has "tipo" attribute
     */
    public boolean isSetTipo()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().find_attribute_user(TIPO$18) != null;
        }
    }
    
    /**
     * Sets the "tipo" attribute
     */
    public void setTipo(it.gov.digitpa.www.protocollo.UnitaOrganizzativa.Tipo.Enum tipo)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(TIPO$18);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(TIPO$18);
            }
            target.setEnumValue(tipo);
        }
    }
    
    /**
     * Sets (as xml) the "tipo" attribute
     */
    public void xsetTipo(it.gov.digitpa.www.protocollo.UnitaOrganizzativa.Tipo tipo)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.UnitaOrganizzativa.Tipo target = null;
            target = (it.gov.digitpa.www.protocollo.UnitaOrganizzativa.Tipo)get_store().find_attribute_user(TIPO$18);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.UnitaOrganizzativa.Tipo)get_store().add_attribute_user(TIPO$18);
            }
            target.set(tipo);
        }
    }
    
    /**
     * Unsets the "tipo" attribute
     */
    public void unsetTipo()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_attribute(TIPO$18);
        }
    }
    /**
     * An XML tipo(@).
     *
     * This is an atomic type that is a restriction of it.gov.digitpa.www.protocollo.UnitaOrganizzativa$Tipo.
     */
    public static class TipoImpl extends org.apache.xmlbeans.impl.values.JavaStringEnumerationHolderEx implements it.gov.digitpa.www.protocollo.UnitaOrganizzativa.Tipo
    {
        
        public TipoImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType, false);
        }
        
        protected TipoImpl(org.apache.xmlbeans.SchemaType sType, boolean b)
        {
            super(sType, b);
        }
    }
}
