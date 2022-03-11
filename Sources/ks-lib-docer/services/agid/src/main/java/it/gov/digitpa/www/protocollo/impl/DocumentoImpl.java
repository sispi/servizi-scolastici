/*
 * XML Type:  Documento
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.Documento
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * An XML Documento(@http://www.digitPa.gov.it/protocollo/).
 *
 * This is a complex type.
 */
public class DocumentoImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.Documento
{
    
    public DocumentoImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName COLLOCAZIONETELEMATICA$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "CollocazioneTelematica");
    private static final javax.xml.namespace.QName IMPRONTA$2 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Impronta");
    private static final javax.xml.namespace.QName TITOLODOCUMENTO$4 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "TitoloDocumento");
    private static final javax.xml.namespace.QName PRIMAREGISTRAZIONE$6 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "PrimaRegistrazione");
    private static final javax.xml.namespace.QName TIPODOCUMENTO$8 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "TipoDocumento");
    private static final javax.xml.namespace.QName OGGETTO$10 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Oggetto");
    private static final javax.xml.namespace.QName CLASSIFICA$12 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Classifica");
    private static final javax.xml.namespace.QName NUMEROPAGINE$14 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "NumeroPagine");
    private static final javax.xml.namespace.QName NOTE$16 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Note");
    private static final javax.xml.namespace.QName PIUINFO$18 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "PiuInfo");
    private static final javax.xml.namespace.QName ID$20 = 
        new javax.xml.namespace.QName("", "id");
    private static final javax.xml.namespace.QName RIFE$22 = 
        new javax.xml.namespace.QName("", "rife");
    private static final javax.xml.namespace.QName NOME$24 = 
        new javax.xml.namespace.QName("", "nome");
    private static final javax.xml.namespace.QName TIPOMIME$26 = 
        new javax.xml.namespace.QName("", "tipoMIME");
    private static final javax.xml.namespace.QName TIPORIFERIMENTO$28 = 
        new javax.xml.namespace.QName("", "tipoRiferimento");
    
    
    /**
     * Gets the "CollocazioneTelematica" element
     */
    public it.gov.digitpa.www.protocollo.CollocazioneTelematica getCollocazioneTelematica()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.CollocazioneTelematica target = null;
            target = (it.gov.digitpa.www.protocollo.CollocazioneTelematica)get_store().find_element_user(COLLOCAZIONETELEMATICA$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "CollocazioneTelematica" element
     */
    public boolean isSetCollocazioneTelematica()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(COLLOCAZIONETELEMATICA$0) != 0;
        }
    }
    
    /**
     * Sets the "CollocazioneTelematica" element
     */
    public void setCollocazioneTelematica(it.gov.digitpa.www.protocollo.CollocazioneTelematica collocazioneTelematica)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.CollocazioneTelematica target = null;
            target = (it.gov.digitpa.www.protocollo.CollocazioneTelematica)get_store().find_element_user(COLLOCAZIONETELEMATICA$0, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.CollocazioneTelematica)get_store().add_element_user(COLLOCAZIONETELEMATICA$0);
            }
            target.set(collocazioneTelematica);
        }
    }
    
    /**
     * Appends and returns a new empty "CollocazioneTelematica" element
     */
    public it.gov.digitpa.www.protocollo.CollocazioneTelematica addNewCollocazioneTelematica()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.CollocazioneTelematica target = null;
            target = (it.gov.digitpa.www.protocollo.CollocazioneTelematica)get_store().add_element_user(COLLOCAZIONETELEMATICA$0);
            return target;
        }
    }
    
    /**
     * Unsets the "CollocazioneTelematica" element
     */
    public void unsetCollocazioneTelematica()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(COLLOCAZIONETELEMATICA$0, 0);
        }
    }
    
    /**
     * Gets the "Impronta" element
     */
    public it.gov.digitpa.www.protocollo.Impronta getImpronta()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Impronta target = null;
            target = (it.gov.digitpa.www.protocollo.Impronta)get_store().find_element_user(IMPRONTA$2, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "Impronta" element
     */
    public boolean isSetImpronta()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(IMPRONTA$2) != 0;
        }
    }
    
    /**
     * Sets the "Impronta" element
     */
    public void setImpronta(it.gov.digitpa.www.protocollo.Impronta impronta)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Impronta target = null;
            target = (it.gov.digitpa.www.protocollo.Impronta)get_store().find_element_user(IMPRONTA$2, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.Impronta)get_store().add_element_user(IMPRONTA$2);
            }
            target.set(impronta);
        }
    }
    
    /**
     * Appends and returns a new empty "Impronta" element
     */
    public it.gov.digitpa.www.protocollo.Impronta addNewImpronta()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Impronta target = null;
            target = (it.gov.digitpa.www.protocollo.Impronta)get_store().add_element_user(IMPRONTA$2);
            return target;
        }
    }
    
    /**
     * Unsets the "Impronta" element
     */
    public void unsetImpronta()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(IMPRONTA$2, 0);
        }
    }
    
    /**
     * Gets the "TitoloDocumento" element
     */
    public it.gov.digitpa.www.protocollo.TitoloDocumento getTitoloDocumento()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.TitoloDocumento target = null;
            target = (it.gov.digitpa.www.protocollo.TitoloDocumento)get_store().find_element_user(TITOLODOCUMENTO$4, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "TitoloDocumento" element
     */
    public boolean isSetTitoloDocumento()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(TITOLODOCUMENTO$4) != 0;
        }
    }
    
    /**
     * Sets the "TitoloDocumento" element
     */
    public void setTitoloDocumento(it.gov.digitpa.www.protocollo.TitoloDocumento titoloDocumento)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.TitoloDocumento target = null;
            target = (it.gov.digitpa.www.protocollo.TitoloDocumento)get_store().find_element_user(TITOLODOCUMENTO$4, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.TitoloDocumento)get_store().add_element_user(TITOLODOCUMENTO$4);
            }
            target.set(titoloDocumento);
        }
    }
    
    /**
     * Appends and returns a new empty "TitoloDocumento" element
     */
    public it.gov.digitpa.www.protocollo.TitoloDocumento addNewTitoloDocumento()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.TitoloDocumento target = null;
            target = (it.gov.digitpa.www.protocollo.TitoloDocumento)get_store().add_element_user(TITOLODOCUMENTO$4);
            return target;
        }
    }
    
    /**
     * Unsets the "TitoloDocumento" element
     */
    public void unsetTitoloDocumento()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(TITOLODOCUMENTO$4, 0);
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
            target = (it.gov.digitpa.www.protocollo.PrimaRegistrazione)get_store().find_element_user(PRIMAREGISTRAZIONE$6, 0);
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
            return get_store().count_elements(PRIMAREGISTRAZIONE$6) != 0;
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
            target = (it.gov.digitpa.www.protocollo.PrimaRegistrazione)get_store().find_element_user(PRIMAREGISTRAZIONE$6, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.PrimaRegistrazione)get_store().add_element_user(PRIMAREGISTRAZIONE$6);
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
            target = (it.gov.digitpa.www.protocollo.PrimaRegistrazione)get_store().add_element_user(PRIMAREGISTRAZIONE$6);
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
            get_store().remove_element(PRIMAREGISTRAZIONE$6, 0);
        }
    }
    
    /**
     * Gets the "TipoDocumento" element
     */
    public it.gov.digitpa.www.protocollo.TipoDocumento getTipoDocumento()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.TipoDocumento target = null;
            target = (it.gov.digitpa.www.protocollo.TipoDocumento)get_store().find_element_user(TIPODOCUMENTO$8, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "TipoDocumento" element
     */
    public boolean isSetTipoDocumento()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(TIPODOCUMENTO$8) != 0;
        }
    }
    
    /**
     * Sets the "TipoDocumento" element
     */
    public void setTipoDocumento(it.gov.digitpa.www.protocollo.TipoDocumento tipoDocumento)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.TipoDocumento target = null;
            target = (it.gov.digitpa.www.protocollo.TipoDocumento)get_store().find_element_user(TIPODOCUMENTO$8, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.TipoDocumento)get_store().add_element_user(TIPODOCUMENTO$8);
            }
            target.set(tipoDocumento);
        }
    }
    
    /**
     * Appends and returns a new empty "TipoDocumento" element
     */
    public it.gov.digitpa.www.protocollo.TipoDocumento addNewTipoDocumento()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.TipoDocumento target = null;
            target = (it.gov.digitpa.www.protocollo.TipoDocumento)get_store().add_element_user(TIPODOCUMENTO$8);
            return target;
        }
    }
    
    /**
     * Unsets the "TipoDocumento" element
     */
    public void unsetTipoDocumento()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(TIPODOCUMENTO$8, 0);
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
            target = (it.gov.digitpa.www.protocollo.Oggetto)get_store().find_element_user(OGGETTO$10, 0);
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
            return get_store().count_elements(OGGETTO$10) != 0;
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
            target = (it.gov.digitpa.www.protocollo.Oggetto)get_store().find_element_user(OGGETTO$10, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.Oggetto)get_store().add_element_user(OGGETTO$10);
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
            target = (it.gov.digitpa.www.protocollo.Oggetto)get_store().add_element_user(OGGETTO$10);
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
            get_store().remove_element(OGGETTO$10, 0);
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
            get_store().find_all_element_users(CLASSIFICA$12, targetList);
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
            target = (it.gov.digitpa.www.protocollo.Classifica)get_store().find_element_user(CLASSIFICA$12, i);
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
            return get_store().count_elements(CLASSIFICA$12);
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
            arraySetterHelper(classificaArray, CLASSIFICA$12);
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
            target = (it.gov.digitpa.www.protocollo.Classifica)get_store().find_element_user(CLASSIFICA$12, i);
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
            target = (it.gov.digitpa.www.protocollo.Classifica)get_store().insert_element_user(CLASSIFICA$12, i);
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
            target = (it.gov.digitpa.www.protocollo.Classifica)get_store().add_element_user(CLASSIFICA$12);
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
            get_store().remove_element(CLASSIFICA$12, i);
        }
    }
    
    /**
     * Gets the "NumeroPagine" element
     */
    public it.gov.digitpa.www.protocollo.NumeroPagine getNumeroPagine()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.NumeroPagine target = null;
            target = (it.gov.digitpa.www.protocollo.NumeroPagine)get_store().find_element_user(NUMEROPAGINE$14, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "NumeroPagine" element
     */
    public boolean isSetNumeroPagine()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(NUMEROPAGINE$14) != 0;
        }
    }
    
    /**
     * Sets the "NumeroPagine" element
     */
    public void setNumeroPagine(it.gov.digitpa.www.protocollo.NumeroPagine numeroPagine)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.NumeroPagine target = null;
            target = (it.gov.digitpa.www.protocollo.NumeroPagine)get_store().find_element_user(NUMEROPAGINE$14, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.NumeroPagine)get_store().add_element_user(NUMEROPAGINE$14);
            }
            target.set(numeroPagine);
        }
    }
    
    /**
     * Appends and returns a new empty "NumeroPagine" element
     */
    public it.gov.digitpa.www.protocollo.NumeroPagine addNewNumeroPagine()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.NumeroPagine target = null;
            target = (it.gov.digitpa.www.protocollo.NumeroPagine)get_store().add_element_user(NUMEROPAGINE$14);
            return target;
        }
    }
    
    /**
     * Unsets the "NumeroPagine" element
     */
    public void unsetNumeroPagine()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(NUMEROPAGINE$14, 0);
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
            target = (it.gov.digitpa.www.protocollo.Note)get_store().find_element_user(NOTE$16, 0);
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
            return get_store().count_elements(NOTE$16) != 0;
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
            target = (it.gov.digitpa.www.protocollo.Note)get_store().find_element_user(NOTE$16, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.Note)get_store().add_element_user(NOTE$16);
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
            target = (it.gov.digitpa.www.protocollo.Note)get_store().add_element_user(NOTE$16);
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
            get_store().remove_element(NOTE$16, 0);
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
            target = (it.gov.digitpa.www.protocollo.PiuInfo)get_store().find_element_user(PIUINFO$18, 0);
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
            return get_store().count_elements(PIUINFO$18) != 0;
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
            target = (it.gov.digitpa.www.protocollo.PiuInfo)get_store().find_element_user(PIUINFO$18, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.PiuInfo)get_store().add_element_user(PIUINFO$18);
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
            target = (it.gov.digitpa.www.protocollo.PiuInfo)get_store().add_element_user(PIUINFO$18);
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
            get_store().remove_element(PIUINFO$18, 0);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(ID$20);
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
            target = (org.apache.xmlbeans.XmlID)get_store().find_attribute_user(ID$20);
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
            return get_store().find_attribute_user(ID$20) != null;
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(ID$20);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(ID$20);
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
            target = (org.apache.xmlbeans.XmlID)get_store().find_attribute_user(ID$20);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlID)get_store().add_attribute_user(ID$20);
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
            get_store().remove_attribute(ID$20);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(RIFE$22);
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
            target = (org.apache.xmlbeans.XmlIDREF)get_store().find_attribute_user(RIFE$22);
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
            return get_store().find_attribute_user(RIFE$22) != null;
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(RIFE$22);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(RIFE$22);
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
            target = (org.apache.xmlbeans.XmlIDREF)get_store().find_attribute_user(RIFE$22);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlIDREF)get_store().add_attribute_user(RIFE$22);
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
            get_store().remove_attribute(RIFE$22);
        }
    }
    
    /**
     * Gets the "nome" attribute
     */
    public org.apache.xmlbeans.XmlAnySimpleType getNome()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlAnySimpleType target = null;
            target = (org.apache.xmlbeans.XmlAnySimpleType)get_store().find_attribute_user(NOME$24);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "nome" attribute
     */
    public boolean isSetNome()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().find_attribute_user(NOME$24) != null;
        }
    }
    
    /**
     * Sets the "nome" attribute
     */
    public void setNome(org.apache.xmlbeans.XmlAnySimpleType nome)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlAnySimpleType target = null;
            target = (org.apache.xmlbeans.XmlAnySimpleType)get_store().find_attribute_user(NOME$24);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlAnySimpleType)get_store().add_attribute_user(NOME$24);
            }
            target.set(nome);
        }
    }
    
    /**
     * Appends and returns a new empty "nome" attribute
     */
    public org.apache.xmlbeans.XmlAnySimpleType addNewNome()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlAnySimpleType target = null;
            target = (org.apache.xmlbeans.XmlAnySimpleType)get_store().add_attribute_user(NOME$24);
            return target;
        }
    }
    
    /**
     * Unsets the "nome" attribute
     */
    public void unsetNome()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_attribute(NOME$24);
        }
    }
    
    /**
     * Gets the "tipoMIME" attribute
     */
    public org.apache.xmlbeans.XmlAnySimpleType getTipoMIME()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlAnySimpleType target = null;
            target = (org.apache.xmlbeans.XmlAnySimpleType)get_store().find_attribute_user(TIPOMIME$26);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "tipoMIME" attribute
     */
    public boolean isSetTipoMIME()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().find_attribute_user(TIPOMIME$26) != null;
        }
    }
    
    /**
     * Sets the "tipoMIME" attribute
     */
    public void setTipoMIME(org.apache.xmlbeans.XmlAnySimpleType tipoMIME)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlAnySimpleType target = null;
            target = (org.apache.xmlbeans.XmlAnySimpleType)get_store().find_attribute_user(TIPOMIME$26);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlAnySimpleType)get_store().add_attribute_user(TIPOMIME$26);
            }
            target.set(tipoMIME);
        }
    }
    
    /**
     * Appends and returns a new empty "tipoMIME" attribute
     */
    public org.apache.xmlbeans.XmlAnySimpleType addNewTipoMIME()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlAnySimpleType target = null;
            target = (org.apache.xmlbeans.XmlAnySimpleType)get_store().add_attribute_user(TIPOMIME$26);
            return target;
        }
    }
    
    /**
     * Unsets the "tipoMIME" attribute
     */
    public void unsetTipoMIME()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_attribute(TIPOMIME$26);
        }
    }
    
    /**
     * Gets the "tipoRiferimento" attribute
     */
    public it.gov.digitpa.www.protocollo.Documento.TipoRiferimento.Enum getTipoRiferimento()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(TIPORIFERIMENTO$28);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(TIPORIFERIMENTO$28);
            }
            if (target == null)
            {
                return null;
            }
            return (it.gov.digitpa.www.protocollo.Documento.TipoRiferimento.Enum)target.getEnumValue();
        }
    }
    
    /**
     * Gets (as xml) the "tipoRiferimento" attribute
     */
    public it.gov.digitpa.www.protocollo.Documento.TipoRiferimento xgetTipoRiferimento()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Documento.TipoRiferimento target = null;
            target = (it.gov.digitpa.www.protocollo.Documento.TipoRiferimento)get_store().find_attribute_user(TIPORIFERIMENTO$28);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.Documento.TipoRiferimento)get_default_attribute_value(TIPORIFERIMENTO$28);
            }
            return target;
        }
    }
    
    /**
     * True if has "tipoRiferimento" attribute
     */
    public boolean isSetTipoRiferimento()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().find_attribute_user(TIPORIFERIMENTO$28) != null;
        }
    }
    
    /**
     * Sets the "tipoRiferimento" attribute
     */
    public void setTipoRiferimento(it.gov.digitpa.www.protocollo.Documento.TipoRiferimento.Enum tipoRiferimento)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(TIPORIFERIMENTO$28);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(TIPORIFERIMENTO$28);
            }
            target.setEnumValue(tipoRiferimento);
        }
    }
    
    /**
     * Sets (as xml) the "tipoRiferimento" attribute
     */
    public void xsetTipoRiferimento(it.gov.digitpa.www.protocollo.Documento.TipoRiferimento tipoRiferimento)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Documento.TipoRiferimento target = null;
            target = (it.gov.digitpa.www.protocollo.Documento.TipoRiferimento)get_store().find_attribute_user(TIPORIFERIMENTO$28);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.Documento.TipoRiferimento)get_store().add_attribute_user(TIPORIFERIMENTO$28);
            }
            target.set(tipoRiferimento);
        }
    }
    
    /**
     * Unsets the "tipoRiferimento" attribute
     */
    public void unsetTipoRiferimento()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_attribute(TIPORIFERIMENTO$28);
        }
    }
    /**
     * An XML tipoRiferimento(@).
     *
     * This is an atomic type that is a restriction of it.gov.digitpa.www.protocollo.Documento$TipoRiferimento.
     */
    public static class TipoRiferimentoImpl extends org.apache.xmlbeans.impl.values.JavaStringEnumerationHolderEx implements it.gov.digitpa.www.protocollo.Documento.TipoRiferimento
    {
        
        public TipoRiferimentoImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType, false);
        }
        
        protected TipoRiferimentoImpl(org.apache.xmlbeans.SchemaType sType, boolean b)
        {
            super(sType, b);
        }
    }
}
