/*
 * An XML document type.
 * Localname: Cognome
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.CognomeDocument
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * A document containing one Cognome(@http://www.digitPa.gov.it/protocollo/) element.
 *
 * This is a complex type.
 */
public class CognomeDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.CognomeDocument
{
    
    public CognomeDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName COGNOME$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Cognome");
    
    
    /**
     * Gets the "Cognome" element
     */
    public it.gov.digitpa.www.protocollo.Cognome getCognome()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Cognome target = null;
            target = (it.gov.digitpa.www.protocollo.Cognome)get_store().find_element_user(COGNOME$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Cognome" element
     */
    public void setCognome(it.gov.digitpa.www.protocollo.Cognome cognome)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Cognome target = null;
            target = (it.gov.digitpa.www.protocollo.Cognome)get_store().find_element_user(COGNOME$0, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.Cognome)get_store().add_element_user(COGNOME$0);
            }
            target.set(cognome);
        }
    }
    
    /**
     * Appends and returns a new empty "Cognome" element
     */
    public it.gov.digitpa.www.protocollo.Cognome addNewCognome()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Cognome target = null;
            target = (it.gov.digitpa.www.protocollo.Cognome)get_store().add_element_user(COGNOME$0);
            return target;
        }
    }
}
