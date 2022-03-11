/*
 * An XML document type.
 * Localname: Procedimento
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.ProcedimentoDocument
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * A document containing one Procedimento(@http://www.digitPa.gov.it/protocollo/) element.
 *
 * This is a complex type.
 */
public class ProcedimentoDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.ProcedimentoDocument
{
    
    public ProcedimentoDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName PROCEDIMENTO$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Procedimento");
    
    
    /**
     * Gets the "Procedimento" element
     */
    public it.gov.digitpa.www.protocollo.Procedimento getProcedimento()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Procedimento target = null;
            target = (it.gov.digitpa.www.protocollo.Procedimento)get_store().find_element_user(PROCEDIMENTO$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Procedimento" element
     */
    public void setProcedimento(it.gov.digitpa.www.protocollo.Procedimento procedimento)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Procedimento target = null;
            target = (it.gov.digitpa.www.protocollo.Procedimento)get_store().find_element_user(PROCEDIMENTO$0, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.Procedimento)get_store().add_element_user(PROCEDIMENTO$0);
            }
            target.set(procedimento);
        }
    }
    
    /**
     * Appends and returns a new empty "Procedimento" element
     */
    public it.gov.digitpa.www.protocollo.Procedimento addNewProcedimento()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Procedimento target = null;
            target = (it.gov.digitpa.www.protocollo.Procedimento)get_store().add_element_user(PROCEDIMENTO$0);
            return target;
        }
    }
}
