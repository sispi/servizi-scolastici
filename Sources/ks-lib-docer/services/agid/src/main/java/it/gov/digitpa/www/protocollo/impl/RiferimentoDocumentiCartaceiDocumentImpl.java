/*
 * An XML document type.
 * Localname: RiferimentoDocumentiCartacei
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.RiferimentoDocumentiCartaceiDocument
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * A document containing one RiferimentoDocumentiCartacei(@http://www.digitPa.gov.it/protocollo/) element.
 *
 * This is a complex type.
 */
public class RiferimentoDocumentiCartaceiDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.RiferimentoDocumentiCartaceiDocument
{
    
    public RiferimentoDocumentiCartaceiDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName RIFERIMENTODOCUMENTICARTACEI$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "RiferimentoDocumentiCartacei");
    
    
    /**
     * Gets the "RiferimentoDocumentiCartacei" element
     */
    public it.gov.digitpa.www.protocollo.RiferimentoDocumentiCartacei getRiferimentoDocumentiCartacei()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.RiferimentoDocumentiCartacei target = null;
            target = (it.gov.digitpa.www.protocollo.RiferimentoDocumentiCartacei)get_store().find_element_user(RIFERIMENTODOCUMENTICARTACEI$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
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
            target = (it.gov.digitpa.www.protocollo.RiferimentoDocumentiCartacei)get_store().find_element_user(RIFERIMENTODOCUMENTICARTACEI$0, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.RiferimentoDocumentiCartacei)get_store().add_element_user(RIFERIMENTODOCUMENTICARTACEI$0);
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
            target = (it.gov.digitpa.www.protocollo.RiferimentoDocumentiCartacei)get_store().add_element_user(RIFERIMENTODOCUMENTICARTACEI$0);
            return target;
        }
    }
}
