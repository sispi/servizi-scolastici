/*
 * XML Type:  Fascicolo
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.Fascicolo
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * An XML Fascicolo(@http://www.digitPa.gov.it/protocollo/).
 *
 * This is a complex type.
 */
public class FascicoloImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.Fascicolo
{
    
    public FascicoloImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName CODICEAMMINISTRAZIONE$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "CodiceAmministrazione");
    private static final javax.xml.namespace.QName CODICEAOO$2 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "CodiceAOO");
    private static final javax.xml.namespace.QName OGGETTO$4 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Oggetto");
    private static final javax.xml.namespace.QName IDENTIFICATIVO$6 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Identificativo");
    private static final javax.xml.namespace.QName CLASSIFICA$8 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Classifica");
    private static final javax.xml.namespace.QName NOTE$10 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Note");
    private static final javax.xml.namespace.QName PIUINFO$12 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "PiuInfo");
    private static final javax.xml.namespace.QName DOCUMENTO$14 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Documento");
    private static final javax.xml.namespace.QName FASCICOLO$16 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Fascicolo");
    private static final javax.xml.namespace.QName ID$18 = 
        new javax.xml.namespace.QName("", "id");
    private static final javax.xml.namespace.QName RIFE$20 = 
        new javax.xml.namespace.QName("", "rife");
    
    
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
     * Gets the "Oggetto" element
     */
    public it.gov.digitpa.www.protocollo.Oggetto getOggetto()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Oggetto target = null;
            target = (it.gov.digitpa.www.protocollo.Oggetto)get_store().find_element_user(OGGETTO$4, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "Oggetto" element
     */
    public boolean isSetOggetto()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(OGGETTO$4) != 0;
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
            target = (it.gov.digitpa.www.protocollo.Oggetto)get_store().find_element_user(OGGETTO$4, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.Oggetto)get_store().add_element_user(OGGETTO$4);
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
            target = (it.gov.digitpa.www.protocollo.Oggetto)get_store().add_element_user(OGGETTO$4);
            return target;
        }
    }
    
    /**
     * Unsets the "Oggetto" element
     */
    public void unsetOggetto()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(OGGETTO$4, 0);
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
            target = (it.gov.digitpa.www.protocollo.Identificativo)get_store().find_element_user(IDENTIFICATIVO$6, 0);
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
            return get_store().count_elements(IDENTIFICATIVO$6) != 0;
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
            target = (it.gov.digitpa.www.protocollo.Identificativo)get_store().find_element_user(IDENTIFICATIVO$6, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.Identificativo)get_store().add_element_user(IDENTIFICATIVO$6);
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
            target = (it.gov.digitpa.www.protocollo.Identificativo)get_store().add_element_user(IDENTIFICATIVO$6);
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
            get_store().remove_element(IDENTIFICATIVO$6, 0);
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
            get_store().find_all_element_users(CLASSIFICA$8, targetList);
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
            target = (it.gov.digitpa.www.protocollo.Classifica)get_store().find_element_user(CLASSIFICA$8, i);
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
            return get_store().count_elements(CLASSIFICA$8);
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
            arraySetterHelper(classificaArray, CLASSIFICA$8);
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
            target = (it.gov.digitpa.www.protocollo.Classifica)get_store().find_element_user(CLASSIFICA$8, i);
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
            target = (it.gov.digitpa.www.protocollo.Classifica)get_store().insert_element_user(CLASSIFICA$8, i);
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
            target = (it.gov.digitpa.www.protocollo.Classifica)get_store().add_element_user(CLASSIFICA$8);
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
            get_store().remove_element(CLASSIFICA$8, i);
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
            target = (it.gov.digitpa.www.protocollo.Note)get_store().find_element_user(NOTE$10, 0);
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
            return get_store().count_elements(NOTE$10) != 0;
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
            target = (it.gov.digitpa.www.protocollo.Note)get_store().find_element_user(NOTE$10, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.Note)get_store().add_element_user(NOTE$10);
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
            target = (it.gov.digitpa.www.protocollo.Note)get_store().add_element_user(NOTE$10);
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
            get_store().remove_element(NOTE$10, 0);
        }
    }
    
    /**
     * Gets the "PiuInfo" element
     */
    public it.gov.digitpa.www.protocollo.PiuInfo getPiuInfo()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.PiuInfo target = null;
            target = (it.gov.digitpa.www.protocollo.PiuInfo)get_store().find_element_user(PIUINFO$12, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "PiuInfo" element
     */
    public boolean isSetPiuInfo()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(PIUINFO$12) != 0;
        }
    }
    
    /**
     * Sets the "PiuInfo" element
     */
    public void setPiuInfo(it.gov.digitpa.www.protocollo.PiuInfo piuInfo)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.PiuInfo target = null;
            target = (it.gov.digitpa.www.protocollo.PiuInfo)get_store().find_element_user(PIUINFO$12, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.PiuInfo)get_store().add_element_user(PIUINFO$12);
            }
            target.set(piuInfo);
        }
    }
    
    /**
     * Appends and returns a new empty "PiuInfo" element
     */
    public it.gov.digitpa.www.protocollo.PiuInfo addNewPiuInfo()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.PiuInfo target = null;
            target = (it.gov.digitpa.www.protocollo.PiuInfo)get_store().add_element_user(PIUINFO$12);
            return target;
        }
    }
    
    /**
     * Unsets the "PiuInfo" element
     */
    public void unsetPiuInfo()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(PIUINFO$12, 0);
        }
    }
    
    /**
     * Gets array of all "Documento" elements
     */
    public it.gov.digitpa.www.protocollo.Documento[] getDocumentoArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(DOCUMENTO$14, targetList);
            it.gov.digitpa.www.protocollo.Documento[] result = new it.gov.digitpa.www.protocollo.Documento[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "Documento" element
     */
    public it.gov.digitpa.www.protocollo.Documento getDocumentoArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Documento target = null;
            target = (it.gov.digitpa.www.protocollo.Documento)get_store().find_element_user(DOCUMENTO$14, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "Documento" element
     */
    public int sizeOfDocumentoArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(DOCUMENTO$14);
        }
    }
    
    /**
     * Sets array of all "Documento" element
     */
    public void setDocumentoArray(it.gov.digitpa.www.protocollo.Documento[] documentoArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(documentoArray, DOCUMENTO$14);
        }
    }
    
    /**
     * Sets ith "Documento" element
     */
    public void setDocumentoArray(int i, it.gov.digitpa.www.protocollo.Documento documento)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Documento target = null;
            target = (it.gov.digitpa.www.protocollo.Documento)get_store().find_element_user(DOCUMENTO$14, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(documento);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "Documento" element
     */
    public it.gov.digitpa.www.protocollo.Documento insertNewDocumento(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Documento target = null;
            target = (it.gov.digitpa.www.protocollo.Documento)get_store().insert_element_user(DOCUMENTO$14, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "Documento" element
     */
    public it.gov.digitpa.www.protocollo.Documento addNewDocumento()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Documento target = null;
            target = (it.gov.digitpa.www.protocollo.Documento)get_store().add_element_user(DOCUMENTO$14);
            return target;
        }
    }
    
    /**
     * Removes the ith "Documento" element
     */
    public void removeDocumento(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(DOCUMENTO$14, i);
        }
    }
    
    /**
     * Gets array of all "Fascicolo" elements
     */
    public it.gov.digitpa.www.protocollo.Fascicolo[] getFascicoloArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(FASCICOLO$16, targetList);
            it.gov.digitpa.www.protocollo.Fascicolo[] result = new it.gov.digitpa.www.protocollo.Fascicolo[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "Fascicolo" element
     */
    public it.gov.digitpa.www.protocollo.Fascicolo getFascicoloArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Fascicolo target = null;
            target = (it.gov.digitpa.www.protocollo.Fascicolo)get_store().find_element_user(FASCICOLO$16, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "Fascicolo" element
     */
    public int sizeOfFascicoloArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(FASCICOLO$16);
        }
    }
    
    /**
     * Sets array of all "Fascicolo" element
     */
    public void setFascicoloArray(it.gov.digitpa.www.protocollo.Fascicolo[] fascicoloArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(fascicoloArray, FASCICOLO$16);
        }
    }
    
    /**
     * Sets ith "Fascicolo" element
     */
    public void setFascicoloArray(int i, it.gov.digitpa.www.protocollo.Fascicolo fascicolo)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Fascicolo target = null;
            target = (it.gov.digitpa.www.protocollo.Fascicolo)get_store().find_element_user(FASCICOLO$16, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(fascicolo);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "Fascicolo" element
     */
    public it.gov.digitpa.www.protocollo.Fascicolo insertNewFascicolo(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Fascicolo target = null;
            target = (it.gov.digitpa.www.protocollo.Fascicolo)get_store().insert_element_user(FASCICOLO$16, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "Fascicolo" element
     */
    public it.gov.digitpa.www.protocollo.Fascicolo addNewFascicolo()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Fascicolo target = null;
            target = (it.gov.digitpa.www.protocollo.Fascicolo)get_store().add_element_user(FASCICOLO$16);
            return target;
        }
    }
    
    /**
     * Removes the ith "Fascicolo" element
     */
    public void removeFascicolo(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(FASCICOLO$16, i);
        }
    }
    
    /**
     * Gets the "id" attribute
     */
    public java.lang.String getId()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(ID$18);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "id" attribute
     */
    public org.apache.xmlbeans.XmlID xgetId()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlID target = null;
            target = (org.apache.xmlbeans.XmlID)get_store().find_attribute_user(ID$18);
            return target;
        }
    }
    
    /**
     * True if has "id" attribute
     */
    public boolean isSetId()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().find_attribute_user(ID$18) != null;
        }
    }
    
    /**
     * Sets the "id" attribute
     */
    public void setId(java.lang.String id)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(ID$18);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(ID$18);
            }
            target.setStringValue(id);
        }
    }
    
    /**
     * Sets (as xml) the "id" attribute
     */
    public void xsetId(org.apache.xmlbeans.XmlID id)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlID target = null;
            target = (org.apache.xmlbeans.XmlID)get_store().find_attribute_user(ID$18);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlID)get_store().add_attribute_user(ID$18);
            }
            target.set(id);
        }
    }
    
    /**
     * Unsets the "id" attribute
     */
    public void unsetId()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_attribute(ID$18);
        }
    }
    
    /**
     * Gets the "rife" attribute
     */
    public java.lang.String getRife()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(RIFE$20);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "rife" attribute
     */
    public org.apache.xmlbeans.XmlIDREF xgetRife()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlIDREF target = null;
            target = (org.apache.xmlbeans.XmlIDREF)get_store().find_attribute_user(RIFE$20);
            return target;
        }
    }
    
    /**
     * True if has "rife" attribute
     */
    public boolean isSetRife()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().find_attribute_user(RIFE$20) != null;
        }
    }
    
    /**
     * Sets the "rife" attribute
     */
    public void setRife(java.lang.String rife)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(RIFE$20);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(RIFE$20);
            }
            target.setStringValue(rife);
        }
    }
    
    /**
     * Sets (as xml) the "rife" attribute
     */
    public void xsetRife(org.apache.xmlbeans.XmlIDREF rife)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlIDREF target = null;
            target = (org.apache.xmlbeans.XmlIDREF)get_store().find_attribute_user(RIFE$20);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlIDREF)get_store().add_attribute_user(RIFE$20);
            }
            target.set(rife);
        }
    }
    
    /**
     * Unsets the "rife" attribute
     */
    public void unsetRife()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_attribute(RIFE$20);
        }
    }
}
