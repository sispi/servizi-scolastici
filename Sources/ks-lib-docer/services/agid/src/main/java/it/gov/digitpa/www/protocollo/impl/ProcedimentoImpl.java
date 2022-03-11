/*
 * XML Type:  Procedimento
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.Procedimento
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * An XML Procedimento(@http://www.digitPa.gov.it/protocollo/).
 *
 * This is a complex type.
 */
public class ProcedimentoImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.Procedimento
{
    
    public ProcedimentoImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName CODICEAMMINISTRAZIONE$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "CodiceAmministrazione");
    private static final javax.xml.namespace.QName CODICEAOO$2 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "CodiceAOO");
    private static final javax.xml.namespace.QName IDENTIFICATIVO$4 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Identificativo");
    private static final javax.xml.namespace.QName TIPOPROCEDIMENTO$6 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "TipoProcedimento");
    private static final javax.xml.namespace.QName OGGETTO$8 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Oggetto");
    private static final javax.xml.namespace.QName CLASSIFICA$10 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Classifica");
    private static final javax.xml.namespace.QName RESPONSABILE$12 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Responsabile");
    private static final javax.xml.namespace.QName DATAAVVIO$14 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "DataAvvio");
    private static final javax.xml.namespace.QName DATATERMINE$16 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "DataTermine");
    private static final javax.xml.namespace.QName NOTE$18 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Note");
    private static final javax.xml.namespace.QName PIUINFO$20 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "PiuInfo");
    private static final javax.xml.namespace.QName ID$22 = 
        new javax.xml.namespace.QName("", "id");
    private static final javax.xml.namespace.QName RIFE$24 = 
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
     * Gets the "Identificativo" element
     */
    public it.gov.digitpa.www.protocollo.Identificativo getIdentificativo()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Identificativo target = null;
            target = (it.gov.digitpa.www.protocollo.Identificativo)get_store().find_element_user(IDENTIFICATIVO$4, 0);
            if (target == null)
            {
                return null;
            }
            return target;
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
            target = (it.gov.digitpa.www.protocollo.Identificativo)get_store().find_element_user(IDENTIFICATIVO$4, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.Identificativo)get_store().add_element_user(IDENTIFICATIVO$4);
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
            target = (it.gov.digitpa.www.protocollo.Identificativo)get_store().add_element_user(IDENTIFICATIVO$4);
            return target;
        }
    }
    
    /**
     * Gets the "TipoProcedimento" element
     */
    public it.gov.digitpa.www.protocollo.TipoProcedimento getTipoProcedimento()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.TipoProcedimento target = null;
            target = (it.gov.digitpa.www.protocollo.TipoProcedimento)get_store().find_element_user(TIPOPROCEDIMENTO$6, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "TipoProcedimento" element
     */
    public boolean isSetTipoProcedimento()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(TIPOPROCEDIMENTO$6) != 0;
        }
    }
    
    /**
     * Sets the "TipoProcedimento" element
     */
    public void setTipoProcedimento(it.gov.digitpa.www.protocollo.TipoProcedimento tipoProcedimento)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.TipoProcedimento target = null;
            target = (it.gov.digitpa.www.protocollo.TipoProcedimento)get_store().find_element_user(TIPOPROCEDIMENTO$6, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.TipoProcedimento)get_store().add_element_user(TIPOPROCEDIMENTO$6);
            }
            target.set(tipoProcedimento);
        }
    }
    
    /**
     * Appends and returns a new empty "TipoProcedimento" element
     */
    public it.gov.digitpa.www.protocollo.TipoProcedimento addNewTipoProcedimento()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.TipoProcedimento target = null;
            target = (it.gov.digitpa.www.protocollo.TipoProcedimento)get_store().add_element_user(TIPOPROCEDIMENTO$6);
            return target;
        }
    }
    
    /**
     * Unsets the "TipoProcedimento" element
     */
    public void unsetTipoProcedimento()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(TIPOPROCEDIMENTO$6, 0);
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
            target = (it.gov.digitpa.www.protocollo.Oggetto)get_store().find_element_user(OGGETTO$8, 0);
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
            return get_store().count_elements(OGGETTO$8) != 0;
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
            target = (it.gov.digitpa.www.protocollo.Oggetto)get_store().find_element_user(OGGETTO$8, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.Oggetto)get_store().add_element_user(OGGETTO$8);
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
            target = (it.gov.digitpa.www.protocollo.Oggetto)get_store().add_element_user(OGGETTO$8);
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
            get_store().remove_element(OGGETTO$8, 0);
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
            get_store().find_all_element_users(CLASSIFICA$10, targetList);
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
            target = (it.gov.digitpa.www.protocollo.Classifica)get_store().find_element_user(CLASSIFICA$10, i);
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
            return get_store().count_elements(CLASSIFICA$10);
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
            arraySetterHelper(classificaArray, CLASSIFICA$10);
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
            target = (it.gov.digitpa.www.protocollo.Classifica)get_store().find_element_user(CLASSIFICA$10, i);
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
            target = (it.gov.digitpa.www.protocollo.Classifica)get_store().insert_element_user(CLASSIFICA$10, i);
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
            target = (it.gov.digitpa.www.protocollo.Classifica)get_store().add_element_user(CLASSIFICA$10);
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
            get_store().remove_element(CLASSIFICA$10, i);
        }
    }
    
    /**
     * Gets the "Responsabile" element
     */
    public it.gov.digitpa.www.protocollo.Responsabile getResponsabile()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Responsabile target = null;
            target = (it.gov.digitpa.www.protocollo.Responsabile)get_store().find_element_user(RESPONSABILE$12, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "Responsabile" element
     */
    public boolean isSetResponsabile()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(RESPONSABILE$12) != 0;
        }
    }
    
    /**
     * Sets the "Responsabile" element
     */
    public void setResponsabile(it.gov.digitpa.www.protocollo.Responsabile responsabile)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Responsabile target = null;
            target = (it.gov.digitpa.www.protocollo.Responsabile)get_store().find_element_user(RESPONSABILE$12, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.Responsabile)get_store().add_element_user(RESPONSABILE$12);
            }
            target.set(responsabile);
        }
    }
    
    /**
     * Appends and returns a new empty "Responsabile" element
     */
    public it.gov.digitpa.www.protocollo.Responsabile addNewResponsabile()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Responsabile target = null;
            target = (it.gov.digitpa.www.protocollo.Responsabile)get_store().add_element_user(RESPONSABILE$12);
            return target;
        }
    }
    
    /**
     * Unsets the "Responsabile" element
     */
    public void unsetResponsabile()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(RESPONSABILE$12, 0);
        }
    }
    
    /**
     * Gets the "DataAvvio" element
     */
    public it.gov.digitpa.www.protocollo.DataAvvio getDataAvvio()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.DataAvvio target = null;
            target = (it.gov.digitpa.www.protocollo.DataAvvio)get_store().find_element_user(DATAAVVIO$14, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "DataAvvio" element
     */
    public boolean isSetDataAvvio()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(DATAAVVIO$14) != 0;
        }
    }
    
    /**
     * Sets the "DataAvvio" element
     */
    public void setDataAvvio(it.gov.digitpa.www.protocollo.DataAvvio dataAvvio)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.DataAvvio target = null;
            target = (it.gov.digitpa.www.protocollo.DataAvvio)get_store().find_element_user(DATAAVVIO$14, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.DataAvvio)get_store().add_element_user(DATAAVVIO$14);
            }
            target.set(dataAvvio);
        }
    }
    
    /**
     * Appends and returns a new empty "DataAvvio" element
     */
    public it.gov.digitpa.www.protocollo.DataAvvio addNewDataAvvio()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.DataAvvio target = null;
            target = (it.gov.digitpa.www.protocollo.DataAvvio)get_store().add_element_user(DATAAVVIO$14);
            return target;
        }
    }
    
    /**
     * Unsets the "DataAvvio" element
     */
    public void unsetDataAvvio()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(DATAAVVIO$14, 0);
        }
    }
    
    /**
     * Gets the "DataTermine" element
     */
    public it.gov.digitpa.www.protocollo.DataTermine getDataTermine()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.DataTermine target = null;
            target = (it.gov.digitpa.www.protocollo.DataTermine)get_store().find_element_user(DATATERMINE$16, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "DataTermine" element
     */
    public boolean isSetDataTermine()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(DATATERMINE$16) != 0;
        }
    }
    
    /**
     * Sets the "DataTermine" element
     */
    public void setDataTermine(it.gov.digitpa.www.protocollo.DataTermine dataTermine)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.DataTermine target = null;
            target = (it.gov.digitpa.www.protocollo.DataTermine)get_store().find_element_user(DATATERMINE$16, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.DataTermine)get_store().add_element_user(DATATERMINE$16);
            }
            target.set(dataTermine);
        }
    }
    
    /**
     * Appends and returns a new empty "DataTermine" element
     */
    public it.gov.digitpa.www.protocollo.DataTermine addNewDataTermine()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.DataTermine target = null;
            target = (it.gov.digitpa.www.protocollo.DataTermine)get_store().add_element_user(DATATERMINE$16);
            return target;
        }
    }
    
    /**
     * Unsets the "DataTermine" element
     */
    public void unsetDataTermine()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(DATATERMINE$16, 0);
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
            target = (it.gov.digitpa.www.protocollo.Note)get_store().find_element_user(NOTE$18, 0);
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
            return get_store().count_elements(NOTE$18) != 0;
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
            target = (it.gov.digitpa.www.protocollo.Note)get_store().find_element_user(NOTE$18, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.Note)get_store().add_element_user(NOTE$18);
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
            target = (it.gov.digitpa.www.protocollo.Note)get_store().add_element_user(NOTE$18);
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
            get_store().remove_element(NOTE$18, 0);
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
            target = (it.gov.digitpa.www.protocollo.PiuInfo)get_store().find_element_user(PIUINFO$20, 0);
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
            return get_store().count_elements(PIUINFO$20) != 0;
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
            target = (it.gov.digitpa.www.protocollo.PiuInfo)get_store().find_element_user(PIUINFO$20, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.PiuInfo)get_store().add_element_user(PIUINFO$20);
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
            target = (it.gov.digitpa.www.protocollo.PiuInfo)get_store().add_element_user(PIUINFO$20);
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
            get_store().remove_element(PIUINFO$20, 0);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(ID$22);
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
            target = (org.apache.xmlbeans.XmlID)get_store().find_attribute_user(ID$22);
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
            return get_store().find_attribute_user(ID$22) != null;
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(ID$22);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(ID$22);
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
            target = (org.apache.xmlbeans.XmlID)get_store().find_attribute_user(ID$22);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlID)get_store().add_attribute_user(ID$22);
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
            get_store().remove_attribute(ID$22);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(RIFE$24);
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
            target = (org.apache.xmlbeans.XmlIDREF)get_store().find_attribute_user(RIFE$24);
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
            return get_store().find_attribute_user(RIFE$24) != null;
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(RIFE$24);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(RIFE$24);
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
            target = (org.apache.xmlbeans.XmlIDREF)get_store().find_attribute_user(RIFE$24);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlIDREF)get_store().add_attribute_user(RIFE$24);
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
            get_store().remove_attribute(RIFE$24);
        }
    }
}
