/*
 * An XML document type.
 * Localname: MessaggioRicevuto
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.MessaggioRicevutoDocument
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * A document containing one MessaggioRicevuto(@http://www.digitPa.gov.it/protocollo/) element.
 *
 * This is a complex type.
 */
public class MessaggioRicevutoDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.MessaggioRicevutoDocument
{
    
    public MessaggioRicevutoDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName MESSAGGIORICEVUTO$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "MessaggioRicevuto");
    
    
    /**
     * Gets the "MessaggioRicevuto" element
     */
    public it.gov.digitpa.www.protocollo.MessaggioRicevuto getMessaggioRicevuto()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.MessaggioRicevuto target = null;
            target = (it.gov.digitpa.www.protocollo.MessaggioRicevuto)get_store().find_element_user(MESSAGGIORICEVUTO$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "MessaggioRicevuto" element
     */
    public void setMessaggioRicevuto(it.gov.digitpa.www.protocollo.MessaggioRicevuto messaggioRicevuto)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.MessaggioRicevuto target = null;
            target = (it.gov.digitpa.www.protocollo.MessaggioRicevuto)get_store().find_element_user(MESSAGGIORICEVUTO$0, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.MessaggioRicevuto)get_store().add_element_user(MESSAGGIORICEVUTO$0);
            }
            target.set(messaggioRicevuto);
        }
    }
    
    /**
     * Appends and returns a new empty "MessaggioRicevuto" element
     */
    public it.gov.digitpa.www.protocollo.MessaggioRicevuto addNewMessaggioRicevuto()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.MessaggioRicevuto target = null;
            target = (it.gov.digitpa.www.protocollo.MessaggioRicevuto)get_store().add_element_user(MESSAGGIORICEVUTO$0);
            return target;
        }
    }
}
