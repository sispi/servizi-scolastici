/*
 * An XML document type.
 * Localname: ConfermaAggiornamento
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.ConfermaAggiornamentoDocument
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * A document containing one ConfermaAggiornamento(@http://www.digitPa.gov.it/protocollo/) element.
 *
 * This is a complex type.
 */
public class ConfermaAggiornamentoDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.ConfermaAggiornamentoDocument
{
    
    public ConfermaAggiornamentoDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName CONFERMAAGGIORNAMENTO$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "ConfermaAggiornamento");
    
    
    /**
     * Gets the "ConfermaAggiornamento" element
     */
    public it.gov.digitpa.www.protocollo.ConfermaAggiornamentoType getConfermaAggiornamento()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.ConfermaAggiornamentoType target = null;
            target = (it.gov.digitpa.www.protocollo.ConfermaAggiornamentoType)get_store().find_element_user(CONFERMAAGGIORNAMENTO$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "ConfermaAggiornamento" element
     */
    public void setConfermaAggiornamento(it.gov.digitpa.www.protocollo.ConfermaAggiornamentoType confermaAggiornamento)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.ConfermaAggiornamentoType target = null;
            target = (it.gov.digitpa.www.protocollo.ConfermaAggiornamentoType)get_store().find_element_user(CONFERMAAGGIORNAMENTO$0, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.ConfermaAggiornamentoType)get_store().add_element_user(CONFERMAAGGIORNAMENTO$0);
            }
            target.set(confermaAggiornamento);
        }
    }
    
    /**
     * Appends and returns a new empty "ConfermaAggiornamento" element
     */
    public it.gov.digitpa.www.protocollo.ConfermaAggiornamentoType addNewConfermaAggiornamento()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.ConfermaAggiornamentoType target = null;
            target = (it.gov.digitpa.www.protocollo.ConfermaAggiornamentoType)get_store().add_element_user(CONFERMAAGGIORNAMENTO$0);
            return target;
        }
    }
}
