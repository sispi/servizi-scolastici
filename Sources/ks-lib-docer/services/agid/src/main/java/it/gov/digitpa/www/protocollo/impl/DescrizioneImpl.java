/*
 * XML Type:  Descrizione
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.Descrizione
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * An XML Descrizione(@http://www.digitPa.gov.it/protocollo/).
 *
 * This is a complex type.
 */
public class DescrizioneImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.Descrizione
{
    
    public DescrizioneImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName DOCUMENTO$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Documento");
    private static final javax.xml.namespace.QName TESTODELMESSAGGIO$2 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "TestoDelMessaggio");
    private static final javax.xml.namespace.QName ALLEGATI$4 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Allegati");
    private static final javax.xml.namespace.QName NOTE$6 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Note");
    
    
    /**
     * Gets the "Documento" element
     */
    public it.gov.digitpa.www.protocollo.Documento getDocumento()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Documento target = null;
            target = (it.gov.digitpa.www.protocollo.Documento)get_store().find_element_user(DOCUMENTO$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "Documento" element
     */
    public boolean isSetDocumento()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(DOCUMENTO$0) != 0;
        }
    }
    
    /**
     * Sets the "Documento" element
     */
    public void setDocumento(it.gov.digitpa.www.protocollo.Documento documento)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Documento target = null;
            target = (it.gov.digitpa.www.protocollo.Documento)get_store().find_element_user(DOCUMENTO$0, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.Documento)get_store().add_element_user(DOCUMENTO$0);
            }
            target.set(documento);
        }
    }
    
    /**
     * Appends and returns a new empty "Documento" element
     */
    public it.gov.digitpa.www.protocollo.Documento addNewDocumento()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Documento target = null;
            target = (it.gov.digitpa.www.protocollo.Documento)get_store().add_element_user(DOCUMENTO$0);
            return target;
        }
    }
    
    /**
     * Unsets the "Documento" element
     */
    public void unsetDocumento()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(DOCUMENTO$0, 0);
        }
    }
    
    /**
     * Gets the "TestoDelMessaggio" element
     */
    public it.gov.digitpa.www.protocollo.TestoDelMessaggio getTestoDelMessaggio()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.TestoDelMessaggio target = null;
            target = (it.gov.digitpa.www.protocollo.TestoDelMessaggio)get_store().find_element_user(TESTODELMESSAGGIO$2, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "TestoDelMessaggio" element
     */
    public boolean isSetTestoDelMessaggio()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(TESTODELMESSAGGIO$2) != 0;
        }
    }
    
    /**
     * Sets the "TestoDelMessaggio" element
     */
    public void setTestoDelMessaggio(it.gov.digitpa.www.protocollo.TestoDelMessaggio testoDelMessaggio)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.TestoDelMessaggio target = null;
            target = (it.gov.digitpa.www.protocollo.TestoDelMessaggio)get_store().find_element_user(TESTODELMESSAGGIO$2, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.TestoDelMessaggio)get_store().add_element_user(TESTODELMESSAGGIO$2);
            }
            target.set(testoDelMessaggio);
        }
    }
    
    /**
     * Appends and returns a new empty "TestoDelMessaggio" element
     */
    public it.gov.digitpa.www.protocollo.TestoDelMessaggio addNewTestoDelMessaggio()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.TestoDelMessaggio target = null;
            target = (it.gov.digitpa.www.protocollo.TestoDelMessaggio)get_store().add_element_user(TESTODELMESSAGGIO$2);
            return target;
        }
    }
    
    /**
     * Unsets the "TestoDelMessaggio" element
     */
    public void unsetTestoDelMessaggio()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(TESTODELMESSAGGIO$2, 0);
        }
    }
    
    /**
     * Gets the "Allegati" element
     */
    public it.gov.digitpa.www.protocollo.Allegati getAllegati()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Allegati target = null;
            target = (it.gov.digitpa.www.protocollo.Allegati)get_store().find_element_user(ALLEGATI$4, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "Allegati" element
     */
    public boolean isSetAllegati()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(ALLEGATI$4) != 0;
        }
    }
    
    /**
     * Sets the "Allegati" element
     */
    public void setAllegati(it.gov.digitpa.www.protocollo.Allegati allegati)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Allegati target = null;
            target = (it.gov.digitpa.www.protocollo.Allegati)get_store().find_element_user(ALLEGATI$4, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.Allegati)get_store().add_element_user(ALLEGATI$4);
            }
            target.set(allegati);
        }
    }
    
    /**
     * Appends and returns a new empty "Allegati" element
     */
    public it.gov.digitpa.www.protocollo.Allegati addNewAllegati()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Allegati target = null;
            target = (it.gov.digitpa.www.protocollo.Allegati)get_store().add_element_user(ALLEGATI$4);
            return target;
        }
    }
    
    /**
     * Unsets the "Allegati" element
     */
    public void unsetAllegati()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(ALLEGATI$4, 0);
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
            target = (it.gov.digitpa.www.protocollo.Note)get_store().find_element_user(NOTE$6, 0);
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
            return get_store().count_elements(NOTE$6) != 0;
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
            target = (it.gov.digitpa.www.protocollo.Note)get_store().find_element_user(NOTE$6, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.Note)get_store().add_element_user(NOTE$6);
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
            target = (it.gov.digitpa.www.protocollo.Note)get_store().add_element_user(NOTE$6);
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
            get_store().remove_element(NOTE$6, 0);
        }
    }
}
