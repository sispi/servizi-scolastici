/*
 * An XML document type.
 * Localname: Risposta
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.RispostaDocument
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * A document containing one Risposta(@http://www.digitPa.gov.it/protocollo/) element.
 *
 * This is a complex type.
 */
public class RispostaDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.RispostaDocument
{
    
    public RispostaDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName RISPOSTA$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Risposta");
    
    
    /**
     * Gets the "Risposta" element
     */
    public it.gov.digitpa.www.protocollo.Risposta getRisposta()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Risposta target = null;
            target = (it.gov.digitpa.www.protocollo.Risposta)get_store().find_element_user(RISPOSTA$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Risposta" element
     */
    public void setRisposta(it.gov.digitpa.www.protocollo.Risposta risposta)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Risposta target = null;
            target = (it.gov.digitpa.www.protocollo.Risposta)get_store().find_element_user(RISPOSTA$0, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.Risposta)get_store().add_element_user(RISPOSTA$0);
            }
            target.set(risposta);
        }
    }
    
    /**
     * Appends and returns a new empty "Risposta" element
     */
    public it.gov.digitpa.www.protocollo.Risposta addNewRisposta()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Risposta target = null;
            target = (it.gov.digitpa.www.protocollo.Risposta)get_store().add_element_user(RISPOSTA$0);
            return target;
        }
    }
}
