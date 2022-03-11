/*
 * An XML document type.
 * Localname: Provvedimento
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.ProvvedimentoDocument
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * A document containing one Provvedimento(@http://www.digitPa.gov.it/protocollo/) element.
 *
 * This is a complex type.
 */
public class ProvvedimentoDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.ProvvedimentoDocument
{
    
    public ProvvedimentoDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName PROVVEDIMENTO$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Provvedimento");
    
    
    /**
     * Gets the "Provvedimento" element
     */
    public it.gov.digitpa.www.protocollo.Provvedimento getProvvedimento()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Provvedimento target = null;
            target = (it.gov.digitpa.www.protocollo.Provvedimento)get_store().find_element_user(PROVVEDIMENTO$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Provvedimento" element
     */
    public void setProvvedimento(it.gov.digitpa.www.protocollo.Provvedimento provvedimento)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Provvedimento target = null;
            target = (it.gov.digitpa.www.protocollo.Provvedimento)get_store().find_element_user(PROVVEDIMENTO$0, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.Provvedimento)get_store().add_element_user(PROVVEDIMENTO$0);
            }
            target.set(provvedimento);
        }
    }
    
    /**
     * Appends and returns a new empty "Provvedimento" element
     */
    public it.gov.digitpa.www.protocollo.Provvedimento addNewProvvedimento()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Provvedimento target = null;
            target = (it.gov.digitpa.www.protocollo.Provvedimento)get_store().add_element_user(PROVVEDIMENTO$0);
            return target;
        }
    }
}
