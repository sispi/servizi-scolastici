/*
 * XML Type:  Intestazione
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.Intestazione
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * An XML Intestazione(@http://www.digitPa.gov.it/protocollo/).
 *
 * This is a complex type.
 */
public class IntestazioneImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.Intestazione
{
    
    public IntestazioneImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName IDENTIFICATORE$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Identificatore");
    private static final javax.xml.namespace.QName PRIMAREGISTRAZIONE$2 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "PrimaRegistrazione");
    private static final javax.xml.namespace.QName ORAREGISTRAZIONE$4 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "OraRegistrazione");
    private static final javax.xml.namespace.QName ORIGINE$6 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Origine");
    private static final javax.xml.namespace.QName DESTINAZIONE$8 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Destinazione");
    private static final javax.xml.namespace.QName PERCONOSCENZA$10 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "PerConoscenza");
    private static final javax.xml.namespace.QName RISPOSTA$12 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Risposta");
    private static final javax.xml.namespace.QName RISERVATO$14 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Riservato");
    private static final javax.xml.namespace.QName INTERVENTOOPERATORE$16 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "InterventoOperatore");
    private static final javax.xml.namespace.QName RIFERIMENTODOCUMENTICARTACEI$18 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "RiferimentoDocumentiCartacei");
    private static final javax.xml.namespace.QName RIFERIMENTITELEMATICI$20 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "RiferimentiTelematici");
    private static final javax.xml.namespace.QName OGGETTO$22 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Oggetto");
    private static final javax.xml.namespace.QName CLASSIFICA$24 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Classifica");
    private static final javax.xml.namespace.QName NOTE$26 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Note");
    
    
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
     * Gets the "OraRegistrazione" element
     */
    public it.gov.digitpa.www.protocollo.OraRegistrazione getOraRegistrazione()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.OraRegistrazione target = null;
            target = (it.gov.digitpa.www.protocollo.OraRegistrazione)get_store().find_element_user(ORAREGISTRAZIONE$4, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "OraRegistrazione" element
     */
    public boolean isSetOraRegistrazione()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(ORAREGISTRAZIONE$4) != 0;
        }
    }
    
    /**
     * Sets the "OraRegistrazione" element
     */
    public void setOraRegistrazione(it.gov.digitpa.www.protocollo.OraRegistrazione oraRegistrazione)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.OraRegistrazione target = null;
            target = (it.gov.digitpa.www.protocollo.OraRegistrazione)get_store().find_element_user(ORAREGISTRAZIONE$4, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.OraRegistrazione)get_store().add_element_user(ORAREGISTRAZIONE$4);
            }
            target.set(oraRegistrazione);
        }
    }
    
    /**
     * Appends and returns a new empty "OraRegistrazione" element
     */
    public it.gov.digitpa.www.protocollo.OraRegistrazione addNewOraRegistrazione()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.OraRegistrazione target = null;
            target = (it.gov.digitpa.www.protocollo.OraRegistrazione)get_store().add_element_user(ORAREGISTRAZIONE$4);
            return target;
        }
    }
    
    /**
     * Unsets the "OraRegistrazione" element
     */
    public void unsetOraRegistrazione()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(ORAREGISTRAZIONE$4, 0);
        }
    }
    
    /**
     * Gets the "Origine" element
     */
    public it.gov.digitpa.www.protocollo.Origine getOrigine()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Origine target = null;
            target = (it.gov.digitpa.www.protocollo.Origine)get_store().find_element_user(ORIGINE$6, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Origine" element
     */
    public void setOrigine(it.gov.digitpa.www.protocollo.Origine origine)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Origine target = null;
            target = (it.gov.digitpa.www.protocollo.Origine)get_store().find_element_user(ORIGINE$6, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.Origine)get_store().add_element_user(ORIGINE$6);
            }
            target.set(origine);
        }
    }
    
    /**
     * Appends and returns a new empty "Origine" element
     */
    public it.gov.digitpa.www.protocollo.Origine addNewOrigine()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Origine target = null;
            target = (it.gov.digitpa.www.protocollo.Origine)get_store().add_element_user(ORIGINE$6);
            return target;
        }
    }
    
    /**
     * Gets array of all "Destinazione" elements
     */
    public it.gov.digitpa.www.protocollo.Destinazione[] getDestinazioneArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(DESTINAZIONE$8, targetList);
            it.gov.digitpa.www.protocollo.Destinazione[] result = new it.gov.digitpa.www.protocollo.Destinazione[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "Destinazione" element
     */
    public it.gov.digitpa.www.protocollo.Destinazione getDestinazioneArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Destinazione target = null;
            target = (it.gov.digitpa.www.protocollo.Destinazione)get_store().find_element_user(DESTINAZIONE$8, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "Destinazione" element
     */
    public int sizeOfDestinazioneArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(DESTINAZIONE$8);
        }
    }
    
    /**
     * Sets array of all "Destinazione" element
     */
    public void setDestinazioneArray(it.gov.digitpa.www.protocollo.Destinazione[] destinazioneArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(destinazioneArray, DESTINAZIONE$8);
        }
    }
    
    /**
     * Sets ith "Destinazione" element
     */
    public void setDestinazioneArray(int i, it.gov.digitpa.www.protocollo.Destinazione destinazione)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Destinazione target = null;
            target = (it.gov.digitpa.www.protocollo.Destinazione)get_store().find_element_user(DESTINAZIONE$8, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(destinazione);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "Destinazione" element
     */
    public it.gov.digitpa.www.protocollo.Destinazione insertNewDestinazione(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Destinazione target = null;
            target = (it.gov.digitpa.www.protocollo.Destinazione)get_store().insert_element_user(DESTINAZIONE$8, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "Destinazione" element
     */
    public it.gov.digitpa.www.protocollo.Destinazione addNewDestinazione()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Destinazione target = null;
            target = (it.gov.digitpa.www.protocollo.Destinazione)get_store().add_element_user(DESTINAZIONE$8);
            return target;
        }
    }
    
    /**
     * Removes the ith "Destinazione" element
     */
    public void removeDestinazione(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(DESTINAZIONE$8, i);
        }
    }
    
    /**
     * Gets array of all "PerConoscenza" elements
     */
    public it.gov.digitpa.www.protocollo.PerConoscenza[] getPerConoscenzaArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(PERCONOSCENZA$10, targetList);
            it.gov.digitpa.www.protocollo.PerConoscenza[] result = new it.gov.digitpa.www.protocollo.PerConoscenza[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "PerConoscenza" element
     */
    public it.gov.digitpa.www.protocollo.PerConoscenza getPerConoscenzaArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.PerConoscenza target = null;
            target = (it.gov.digitpa.www.protocollo.PerConoscenza)get_store().find_element_user(PERCONOSCENZA$10, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "PerConoscenza" element
     */
    public int sizeOfPerConoscenzaArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(PERCONOSCENZA$10);
        }
    }
    
    /**
     * Sets array of all "PerConoscenza" element
     */
    public void setPerConoscenzaArray(it.gov.digitpa.www.protocollo.PerConoscenza[] perConoscenzaArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(perConoscenzaArray, PERCONOSCENZA$10);
        }
    }
    
    /**
     * Sets ith "PerConoscenza" element
     */
    public void setPerConoscenzaArray(int i, it.gov.digitpa.www.protocollo.PerConoscenza perConoscenza)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.PerConoscenza target = null;
            target = (it.gov.digitpa.www.protocollo.PerConoscenza)get_store().find_element_user(PERCONOSCENZA$10, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(perConoscenza);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "PerConoscenza" element
     */
    public it.gov.digitpa.www.protocollo.PerConoscenza insertNewPerConoscenza(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.PerConoscenza target = null;
            target = (it.gov.digitpa.www.protocollo.PerConoscenza)get_store().insert_element_user(PERCONOSCENZA$10, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "PerConoscenza" element
     */
    public it.gov.digitpa.www.protocollo.PerConoscenza addNewPerConoscenza()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.PerConoscenza target = null;
            target = (it.gov.digitpa.www.protocollo.PerConoscenza)get_store().add_element_user(PERCONOSCENZA$10);
            return target;
        }
    }
    
    /**
     * Removes the ith "PerConoscenza" element
     */
    public void removePerConoscenza(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(PERCONOSCENZA$10, i);
        }
    }
    
    /**
     * Gets the "Risposta" element
     */
    public it.gov.digitpa.www.protocollo.Risposta getRisposta()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Risposta target = null;
            target = (it.gov.digitpa.www.protocollo.Risposta)get_store().find_element_user(RISPOSTA$12, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "Risposta" element
     */
    public boolean isSetRisposta()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(RISPOSTA$12) != 0;
        }
    }
    
    /**
     * Sets the "Risposta" element
     */
    public void setRisposta(it.gov.digitpa.www.protocollo.Risposta risposta)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Risposta target = null;
            target = (it.gov.digitpa.www.protocollo.Risposta)get_store().find_element_user(RISPOSTA$12, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.Risposta)get_store().add_element_user(RISPOSTA$12);
            }
            target.set(risposta);
        }
    }
    
    /**
     * Appends and returns a new empty "Risposta" element
     */
    public it.gov.digitpa.www.protocollo.Risposta addNewRisposta()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Risposta target = null;
            target = (it.gov.digitpa.www.protocollo.Risposta)get_store().add_element_user(RISPOSTA$12);
            return target;
        }
    }
    
    /**
     * Unsets the "Risposta" element
     */
    public void unsetRisposta()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(RISPOSTA$12, 0);
        }
    }
    
    /**
     * Gets the "Riservato" element
     */
    public it.gov.digitpa.www.protocollo.Riservato getRiservato()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Riservato target = null;
            target = (it.gov.digitpa.www.protocollo.Riservato)get_store().find_element_user(RISERVATO$14, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "Riservato" element
     */
    public boolean isSetRiservato()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(RISERVATO$14) != 0;
        }
    }
    
    /**
     * Sets the "Riservato" element
     */
    public void setRiservato(it.gov.digitpa.www.protocollo.Riservato riservato)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Riservato target = null;
            target = (it.gov.digitpa.www.protocollo.Riservato)get_store().find_element_user(RISERVATO$14, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.Riservato)get_store().add_element_user(RISERVATO$14);
            }
            target.set(riservato);
        }
    }
    
    /**
     * Appends and returns a new empty "Riservato" element
     */
    public it.gov.digitpa.www.protocollo.Riservato addNewRiservato()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Riservato target = null;
            target = (it.gov.digitpa.www.protocollo.Riservato)get_store().add_element_user(RISERVATO$14);
            return target;
        }
    }
    
    /**
     * Unsets the "Riservato" element
     */
    public void unsetRiservato()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(RISERVATO$14, 0);
        }
    }
    
    /**
     * Gets the "InterventoOperatore" element
     */
    public it.gov.digitpa.www.protocollo.InterventoOperatore getInterventoOperatore()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.InterventoOperatore target = null;
            target = (it.gov.digitpa.www.protocollo.InterventoOperatore)get_store().find_element_user(INTERVENTOOPERATORE$16, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "InterventoOperatore" element
     */
    public boolean isSetInterventoOperatore()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(INTERVENTOOPERATORE$16) != 0;
        }
    }
    
    /**
     * Sets the "InterventoOperatore" element
     */
    public void setInterventoOperatore(it.gov.digitpa.www.protocollo.InterventoOperatore interventoOperatore)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.InterventoOperatore target = null;
            target = (it.gov.digitpa.www.protocollo.InterventoOperatore)get_store().find_element_user(INTERVENTOOPERATORE$16, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.InterventoOperatore)get_store().add_element_user(INTERVENTOOPERATORE$16);
            }
            target.set(interventoOperatore);
        }
    }
    
    /**
     * Appends and returns a new empty "InterventoOperatore" element
     */
    public it.gov.digitpa.www.protocollo.InterventoOperatore addNewInterventoOperatore()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.InterventoOperatore target = null;
            target = (it.gov.digitpa.www.protocollo.InterventoOperatore)get_store().add_element_user(INTERVENTOOPERATORE$16);
            return target;
        }
    }
    
    /**
     * Unsets the "InterventoOperatore" element
     */
    public void unsetInterventoOperatore()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(INTERVENTOOPERATORE$16, 0);
        }
    }
    
    /**
     * Gets the "RiferimentoDocumentiCartacei" element
     */
    public it.gov.digitpa.www.protocollo.RiferimentoDocumentiCartacei getRiferimentoDocumentiCartacei()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.RiferimentoDocumentiCartacei target = null;
            target = (it.gov.digitpa.www.protocollo.RiferimentoDocumentiCartacei)get_store().find_element_user(RIFERIMENTODOCUMENTICARTACEI$18, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "RiferimentoDocumentiCartacei" element
     */
    public boolean isSetRiferimentoDocumentiCartacei()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(RIFERIMENTODOCUMENTICARTACEI$18) != 0;
        }
    }
    
    /**
     * Sets the "RiferimentoDocumentiCartacei" element
     */
    public void setRiferimentoDocumentiCartacei(it.gov.digitpa.www.protocollo.RiferimentoDocumentiCartacei riferimentoDocumentiCartacei)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.RiferimentoDocumentiCartacei target = null;
            target = (it.gov.digitpa.www.protocollo.RiferimentoDocumentiCartacei)get_store().find_element_user(RIFERIMENTODOCUMENTICARTACEI$18, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.RiferimentoDocumentiCartacei)get_store().add_element_user(RIFERIMENTODOCUMENTICARTACEI$18);
            }
            target.set(riferimentoDocumentiCartacei);
        }
    }
    
    /**
     * Appends and returns a new empty "RiferimentoDocumentiCartacei" element
     */
    public it.gov.digitpa.www.protocollo.RiferimentoDocumentiCartacei addNewRiferimentoDocumentiCartacei()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.RiferimentoDocumentiCartacei target = null;
            target = (it.gov.digitpa.www.protocollo.RiferimentoDocumentiCartacei)get_store().add_element_user(RIFERIMENTODOCUMENTICARTACEI$18);
            return target;
        }
    }
    
    /**
     * Unsets the "RiferimentoDocumentiCartacei" element
     */
    public void unsetRiferimentoDocumentiCartacei()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(RIFERIMENTODOCUMENTICARTACEI$18, 0);
        }
    }
    
    /**
     * Gets the "RiferimentiTelematici" element
     */
    public it.gov.digitpa.www.protocollo.RiferimentiTelematici getRiferimentiTelematici()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.RiferimentiTelematici target = null;
            target = (it.gov.digitpa.www.protocollo.RiferimentiTelematici)get_store().find_element_user(RIFERIMENTITELEMATICI$20, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "RiferimentiTelematici" element
     */
    public boolean isSetRiferimentiTelematici()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(RIFERIMENTITELEMATICI$20) != 0;
        }
    }
    
    /**
     * Sets the "RiferimentiTelematici" element
     */
    public void setRiferimentiTelematici(it.gov.digitpa.www.protocollo.RiferimentiTelematici riferimentiTelematici)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.RiferimentiTelematici target = null;
            target = (it.gov.digitpa.www.protocollo.RiferimentiTelematici)get_store().find_element_user(RIFERIMENTITELEMATICI$20, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.RiferimentiTelematici)get_store().add_element_user(RIFERIMENTITELEMATICI$20);
            }
            target.set(riferimentiTelematici);
        }
    }
    
    /**
     * Appends and returns a new empty "RiferimentiTelematici" element
     */
    public it.gov.digitpa.www.protocollo.RiferimentiTelematici addNewRiferimentiTelematici()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.RiferimentiTelematici target = null;
            target = (it.gov.digitpa.www.protocollo.RiferimentiTelematici)get_store().add_element_user(RIFERIMENTITELEMATICI$20);
            return target;
        }
    }
    
    /**
     * Unsets the "RiferimentiTelematici" element
     */
    public void unsetRiferimentiTelematici()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(RIFERIMENTITELEMATICI$20, 0);
        }
    }
    
    /**
     * Gets the "Oggetto" element
     */
    public it.gov.digitpa.www.protocollo.Oggetto getOggetto()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Oggetto target = null;
            target = (it.gov.digitpa.www.protocollo.Oggetto)get_store().find_element_user(OGGETTO$22, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Oggetto" element
     */
    public void setOggetto(it.gov.digitpa.www.protocollo.Oggetto oggetto)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Oggetto target = null;
            target = (it.gov.digitpa.www.protocollo.Oggetto)get_store().find_element_user(OGGETTO$22, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.Oggetto)get_store().add_element_user(OGGETTO$22);
            }
            target.set(oggetto);
        }
    }
    
    /**
     * Appends and returns a new empty "Oggetto" element
     */
    public it.gov.digitpa.www.protocollo.Oggetto addNewOggetto()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Oggetto target = null;
            target = (it.gov.digitpa.www.protocollo.Oggetto)get_store().add_element_user(OGGETTO$22);
            return target;
        }
    }
    
    /**
     * Gets array of all "Classifica" elements
     */
    public it.gov.digitpa.www.protocollo.Classifica[] getClassificaArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(CLASSIFICA$24, targetList);
            it.gov.digitpa.www.protocollo.Classifica[] result = new it.gov.digitpa.www.protocollo.Classifica[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "Classifica" element
     */
    public it.gov.digitpa.www.protocollo.Classifica getClassificaArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Classifica target = null;
            target = (it.gov.digitpa.www.protocollo.Classifica)get_store().find_element_user(CLASSIFICA$24, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "Classifica" element
     */
    public int sizeOfClassificaArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(CLASSIFICA$24);
        }
    }
    
    /**
     * Sets array of all "Classifica" element
     */
    public void setClassificaArray(it.gov.digitpa.www.protocollo.Classifica[] classificaArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(classificaArray, CLASSIFICA$24);
        }
    }
    
    /**
     * Sets ith "Classifica" element
     */
    public void setClassificaArray(int i, it.gov.digitpa.www.protocollo.Classifica classifica)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Classifica target = null;
            target = (it.gov.digitpa.www.protocollo.Classifica)get_store().find_element_user(CLASSIFICA$24, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(classifica);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "Classifica" element
     */
    public it.gov.digitpa.www.protocollo.Classifica insertNewClassifica(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Classifica target = null;
            target = (it.gov.digitpa.www.protocollo.Classifica)get_store().insert_element_user(CLASSIFICA$24, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "Classifica" element
     */
    public it.gov.digitpa.www.protocollo.Classifica addNewClassifica()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Classifica target = null;
            target = (it.gov.digitpa.www.protocollo.Classifica)get_store().add_element_user(CLASSIFICA$24);
            return target;
        }
    }
    
    /**
     * Removes the ith "Classifica" element
     */
    public void removeClassifica(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(CLASSIFICA$24, i);
        }
    }
    
    /**
     * Gets the "Note" element
     */
    public it.gov.digitpa.www.protocollo.Note getNote()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Note target = null;
            target = (it.gov.digitpa.www.protocollo.Note)get_store().find_element_user(NOTE$26, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "Note" element
     */
    public boolean isSetNote()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(NOTE$26) != 0;
        }
    }
    
    /**
     * Sets the "Note" element
     */
    public void setNote(it.gov.digitpa.www.protocollo.Note note)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Note target = null;
            target = (it.gov.digitpa.www.protocollo.Note)get_store().find_element_user(NOTE$26, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.Note)get_store().add_element_user(NOTE$26);
            }
            target.set(note);
        }
    }
    
    /**
     * Appends and returns a new empty "Note" element
     */
    public it.gov.digitpa.www.protocollo.Note addNewNote()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Note target = null;
            target = (it.gov.digitpa.www.protocollo.Note)get_store().add_element_user(NOTE$26);
            return target;
        }
    }
    
    /**
     * Unsets the "Note" element
     */
    public void unsetNote()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(NOTE$26, 0);
        }
    }
}
