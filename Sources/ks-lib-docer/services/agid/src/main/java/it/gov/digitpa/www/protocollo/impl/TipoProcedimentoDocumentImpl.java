/*
 * An XML document type.
 * Localname: TipoProcedimento
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.TipoProcedimentoDocument
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * A document containing one TipoProcedimento(@http://www.digitPa.gov.it/protocollo/) element.
 *
 * This is a complex type.
 */
public class TipoProcedimentoDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.TipoProcedimentoDocument
{
    
    public TipoProcedimentoDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName TIPOPROCEDIMENTO$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "TipoProcedimento");
    
    
    /**
     * Gets the "TipoProcedimento" element
     */
    public it.gov.digitpa.www.protocollo.TipoProcedimento getTipoProcedimento()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.TipoProcedimento target = null;
            target = (it.gov.digitpa.www.protocollo.TipoProcedimento)get_store().find_element_user(TIPOPROCEDIMENTO$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
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
            target = (it.gov.digitpa.www.protocollo.TipoProcedimento)get_store().find_element_user(TIPOPROCEDIMENTO$0, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.TipoProcedimento)get_store().add_element_user(TIPOPROCEDIMENTO$0);
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
            target = (it.gov.digitpa.www.protocollo.TipoProcedimento)get_store().add_element_user(TIPOPROCEDIMENTO$0);
            return target;
        }
    }
}
