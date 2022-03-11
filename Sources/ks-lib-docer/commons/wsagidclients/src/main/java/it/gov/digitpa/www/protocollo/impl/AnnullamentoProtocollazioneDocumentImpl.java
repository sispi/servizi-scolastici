/*
 * An XML document type.
 * Localname: AnnullamentoProtocollazione
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.AnnullamentoProtocollazioneDocument
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * A document containing one AnnullamentoProtocollazione(@http://www.digitPa.gov.it/protocollo/) element.
 *
 * This is a complex type.
 */
public class AnnullamentoProtocollazioneDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.AnnullamentoProtocollazioneDocument
{
    
    public AnnullamentoProtocollazioneDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName ANNULLAMENTOPROTOCOLLAZIONE$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "AnnullamentoProtocollazione");
    
    
    /**
     * Gets the "AnnullamentoProtocollazione" element
     */
    public it.gov.digitpa.www.protocollo.AnnullamentoProtocollazione getAnnullamentoProtocollazione()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.AnnullamentoProtocollazione target = null;
            target = (it.gov.digitpa.www.protocollo.AnnullamentoProtocollazione)get_store().find_element_user(ANNULLAMENTOPROTOCOLLAZIONE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "AnnullamentoProtocollazione" element
     */
    public void setAnnullamentoProtocollazione(it.gov.digitpa.www.protocollo.AnnullamentoProtocollazione annullamentoProtocollazione)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.AnnullamentoProtocollazione target = null;
            target = (it.gov.digitpa.www.protocollo.AnnullamentoProtocollazione)get_store().find_element_user(ANNULLAMENTOPROTOCOLLAZIONE$0, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.AnnullamentoProtocollazione)get_store().add_element_user(ANNULLAMENTOPROTOCOLLAZIONE$0);
            }
            target.set(annullamentoProtocollazione);
        }
    }
    
    /**
     * Appends and returns a new empty "AnnullamentoProtocollazione" element
     */
    public it.gov.digitpa.www.protocollo.AnnullamentoProtocollazione addNewAnnullamentoProtocollazione()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.AnnullamentoProtocollazione target = null;
            target = (it.gov.digitpa.www.protocollo.AnnullamentoProtocollazione)get_store().add_element_user(ANNULLAMENTOPROTOCOLLAZIONE$0);
            return target;
        }
    }
}
