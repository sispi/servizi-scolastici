/*
 * XML Type:  Riferimenti
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.Riferimenti
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * An XML Riferimenti(@http://www.digitPa.gov.it/protocollo/).
 *
 * This is a complex type.
 */
public class RiferimentiImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.Riferimenti
{
    
    public RiferimentiImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName MESSAGGIO$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Messaggio");
    private static final javax.xml.namespace.QName CONTESTOPROCEDURALE$2 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "ContestoProcedurale");
    private static final javax.xml.namespace.QName PROCEDIMENTO$4 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Procedimento");
    
    
    /**
     * Gets array of all "Messaggio" elements
     */
    public it.gov.digitpa.www.protocollo.Messaggio[] getMessaggioArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(MESSAGGIO$0, targetList);
            it.gov.digitpa.www.protocollo.Messaggio[] result = new it.gov.digitpa.www.protocollo.Messaggio[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "Messaggio" element
     */
    public it.gov.digitpa.www.protocollo.Messaggio getMessaggioArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Messaggio target = null;
            target = (it.gov.digitpa.www.protocollo.Messaggio)get_store().find_element_user(MESSAGGIO$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "Messaggio" element
     */
    public int sizeOfMessaggioArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(MESSAGGIO$0);
        }
    }
    
    /**
     * Sets array of all "Messaggio" element
     */
    public void setMessaggioArray(it.gov.digitpa.www.protocollo.Messaggio[] messaggioArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(messaggioArray, MESSAGGIO$0);
        }
    }
    
    /**
     * Sets ith "Messaggio" element
     */
    public void setMessaggioArray(int i, it.gov.digitpa.www.protocollo.Messaggio messaggio)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Messaggio target = null;
            target = (it.gov.digitpa.www.protocollo.Messaggio)get_store().find_element_user(MESSAGGIO$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(messaggio);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "Messaggio" element
     */
    public it.gov.digitpa.www.protocollo.Messaggio insertNewMessaggio(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Messaggio target = null;
            target = (it.gov.digitpa.www.protocollo.Messaggio)get_store().insert_element_user(MESSAGGIO$0, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "Messaggio" element
     */
    public it.gov.digitpa.www.protocollo.Messaggio addNewMessaggio()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Messaggio target = null;
            target = (it.gov.digitpa.www.protocollo.Messaggio)get_store().add_element_user(MESSAGGIO$0);
            return target;
        }
    }
    
    /**
     * Removes the ith "Messaggio" element
     */
    public void removeMessaggio(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(MESSAGGIO$0, i);
        }
    }
    
    /**
     * Gets array of all "ContestoProcedurale" elements
     */
    public it.gov.digitpa.www.protocollo.ContestoProcedurale[] getContestoProceduraleArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(CONTESTOPROCEDURALE$2, targetList);
            it.gov.digitpa.www.protocollo.ContestoProcedurale[] result = new it.gov.digitpa.www.protocollo.ContestoProcedurale[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "ContestoProcedurale" element
     */
    public it.gov.digitpa.www.protocollo.ContestoProcedurale getContestoProceduraleArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.ContestoProcedurale target = null;
            target = (it.gov.digitpa.www.protocollo.ContestoProcedurale)get_store().find_element_user(CONTESTOPROCEDURALE$2, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "ContestoProcedurale" element
     */
    public int sizeOfContestoProceduraleArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(CONTESTOPROCEDURALE$2);
        }
    }
    
    /**
     * Sets array of all "ContestoProcedurale" element
     */
    public void setContestoProceduraleArray(it.gov.digitpa.www.protocollo.ContestoProcedurale[] contestoProceduraleArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(contestoProceduraleArray, CONTESTOPROCEDURALE$2);
        }
    }
    
    /**
     * Sets ith "ContestoProcedurale" element
     */
    public void setContestoProceduraleArray(int i, it.gov.digitpa.www.protocollo.ContestoProcedurale contestoProcedurale)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.ContestoProcedurale target = null;
            target = (it.gov.digitpa.www.protocollo.ContestoProcedurale)get_store().find_element_user(CONTESTOPROCEDURALE$2, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(contestoProcedurale);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "ContestoProcedurale" element
     */
    public it.gov.digitpa.www.protocollo.ContestoProcedurale insertNewContestoProcedurale(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.ContestoProcedurale target = null;
            target = (it.gov.digitpa.www.protocollo.ContestoProcedurale)get_store().insert_element_user(CONTESTOPROCEDURALE$2, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "ContestoProcedurale" element
     */
    public it.gov.digitpa.www.protocollo.ContestoProcedurale addNewContestoProcedurale()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.ContestoProcedurale target = null;
            target = (it.gov.digitpa.www.protocollo.ContestoProcedurale)get_store().add_element_user(CONTESTOPROCEDURALE$2);
            return target;
        }
    }
    
    /**
     * Removes the ith "ContestoProcedurale" element
     */
    public void removeContestoProcedurale(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(CONTESTOPROCEDURALE$2, i);
        }
    }
    
    /**
     * Gets array of all "Procedimento" elements
     */
    public it.gov.digitpa.www.protocollo.Procedimento[] getProcedimentoArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(PROCEDIMENTO$4, targetList);
            it.gov.digitpa.www.protocollo.Procedimento[] result = new it.gov.digitpa.www.protocollo.Procedimento[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "Procedimento" element
     */
    public it.gov.digitpa.www.protocollo.Procedimento getProcedimentoArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Procedimento target = null;
            target = (it.gov.digitpa.www.protocollo.Procedimento)get_store().find_element_user(PROCEDIMENTO$4, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "Procedimento" element
     */
    public int sizeOfProcedimentoArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(PROCEDIMENTO$4);
        }
    }
    
    /**
     * Sets array of all "Procedimento" element
     */
    public void setProcedimentoArray(it.gov.digitpa.www.protocollo.Procedimento[] procedimentoArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(procedimentoArray, PROCEDIMENTO$4);
        }
    }
    
    /**
     * Sets ith "Procedimento" element
     */
    public void setProcedimentoArray(int i, it.gov.digitpa.www.protocollo.Procedimento procedimento)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Procedimento target = null;
            target = (it.gov.digitpa.www.protocollo.Procedimento)get_store().find_element_user(PROCEDIMENTO$4, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(procedimento);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "Procedimento" element
     */
    public it.gov.digitpa.www.protocollo.Procedimento insertNewProcedimento(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Procedimento target = null;
            target = (it.gov.digitpa.www.protocollo.Procedimento)get_store().insert_element_user(PROCEDIMENTO$4, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "Procedimento" element
     */
    public it.gov.digitpa.www.protocollo.Procedimento addNewProcedimento()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Procedimento target = null;
            target = (it.gov.digitpa.www.protocollo.Procedimento)get_store().add_element_user(PROCEDIMENTO$4);
            return target;
        }
    }
    
    /**
     * Removes the ith "Procedimento" element
     */
    public void removeProcedimento(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(PROCEDIMENTO$4, i);
        }
    }
}
