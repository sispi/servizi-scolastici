/*
 * An XML document type.
 * Localname: AggiornamentoConferma
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.AggiornamentoConfermaDocument
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * A document containing one AggiornamentoConferma(@http://www.digitPa.gov.it/protocollo/) element.
 *
 * This is a complex type.
 */
public class AggiornamentoConfermaDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.AggiornamentoConfermaDocument
{
    
    public AggiornamentoConfermaDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName AGGIORNAMENTOCONFERMA$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "AggiornamentoConferma");
    
    
    /**
     * Gets the "AggiornamentoConferma" element
     */
    public it.gov.digitpa.www.protocollo.AggiornamentoConferma getAggiornamentoConferma()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.AggiornamentoConferma target = null;
            target = (it.gov.digitpa.www.protocollo.AggiornamentoConferma)get_store().find_element_user(AGGIORNAMENTOCONFERMA$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "AggiornamentoConferma" element
     */
    public void setAggiornamentoConferma(it.gov.digitpa.www.protocollo.AggiornamentoConferma aggiornamentoConferma)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.AggiornamentoConferma target = null;
            target = (it.gov.digitpa.www.protocollo.AggiornamentoConferma)get_store().find_element_user(AGGIORNAMENTOCONFERMA$0, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.AggiornamentoConferma)get_store().add_element_user(AGGIORNAMENTOCONFERMA$0);
            }
            target.set(aggiornamentoConferma);
        }
    }
    
    /**
     * Appends and returns a new empty "AggiornamentoConferma" element
     */
    public it.gov.digitpa.www.protocollo.AggiornamentoConferma addNewAggiornamentoConferma()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.AggiornamentoConferma target = null;
            target = (it.gov.digitpa.www.protocollo.AggiornamentoConferma)get_store().add_element_user(AGGIORNAMENTOCONFERMA$0);
            return target;
        }
    }
}
