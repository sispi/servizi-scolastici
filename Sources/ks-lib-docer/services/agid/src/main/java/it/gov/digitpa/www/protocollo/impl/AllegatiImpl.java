/*
 * XML Type:  Allegati
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.Allegati
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * An XML Allegati(@http://www.digitPa.gov.it/protocollo/).
 *
 * This is a complex type.
 */
public class AllegatiImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.Allegati
{
    
    public AllegatiImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName DOCUMENTO$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Documento");
    private static final javax.xml.namespace.QName FASCICOLO$2 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Fascicolo");
    
    
    /**
     * Gets array of all "Documento" elements
     */
    public it.gov.digitpa.www.protocollo.Documento[] getDocumentoArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(DOCUMENTO$0, targetList);
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
            target = (it.gov.digitpa.www.protocollo.Documento)get_store().find_element_user(DOCUMENTO$0, i);
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
            return get_store().count_elements(DOCUMENTO$0);
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
            arraySetterHelper(documentoArray, DOCUMENTO$0);
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
            target = (it.gov.digitpa.www.protocollo.Documento)get_store().find_element_user(DOCUMENTO$0, i);
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
            target = (it.gov.digitpa.www.protocollo.Documento)get_store().insert_element_user(DOCUMENTO$0, i);
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
            target = (it.gov.digitpa.www.protocollo.Documento)get_store().add_element_user(DOCUMENTO$0);
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
            get_store().remove_element(DOCUMENTO$0, i);
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
            get_store().find_all_element_users(FASCICOLO$2, targetList);
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
            target = (it.gov.digitpa.www.protocollo.Fascicolo)get_store().find_element_user(FASCICOLO$2, i);
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
            return get_store().count_elements(FASCICOLO$2);
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
            arraySetterHelper(fascicoloArray, FASCICOLO$2);
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
            target = (it.gov.digitpa.www.protocollo.Fascicolo)get_store().find_element_user(FASCICOLO$2, i);
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
            target = (it.gov.digitpa.www.protocollo.Fascicolo)get_store().insert_element_user(FASCICOLO$2, i);
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
            target = (it.gov.digitpa.www.protocollo.Fascicolo)get_store().add_element_user(FASCICOLO$2);
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
            get_store().remove_element(FASCICOLO$2, i);
        }
    }
}
