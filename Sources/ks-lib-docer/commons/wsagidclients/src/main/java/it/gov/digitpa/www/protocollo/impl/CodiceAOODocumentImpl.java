/*
 * An XML document type.
 * Localname: CodiceAOO
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.CodiceAOODocument
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * A document containing one CodiceAOO(@http://www.digitPa.gov.it/protocollo/) element.
 *
 * This is a complex type.
 */
public class CodiceAOODocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.CodiceAOODocument
{
    
    public CodiceAOODocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName CODICEAOO$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "CodiceAOO");
    
    
    /**
     * Gets the "CodiceAOO" element
     */
    public it.gov.digitpa.www.protocollo.CodiceAOO getCodiceAOO()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.CodiceAOO target = null;
            target = (it.gov.digitpa.www.protocollo.CodiceAOO)get_store().find_element_user(CODICEAOO$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "CodiceAOO" element
     */
    public void setCodiceAOO(it.gov.digitpa.www.protocollo.CodiceAOO codiceAOO)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.CodiceAOO target = null;
            target = (it.gov.digitpa.www.protocollo.CodiceAOO)get_store().find_element_user(CODICEAOO$0, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.CodiceAOO)get_store().add_element_user(CODICEAOO$0);
            }
            target.set(codiceAOO);
        }
    }
    
    /**
     * Appends and returns a new empty "CodiceAOO" element
     */
    public it.gov.digitpa.www.protocollo.CodiceAOO addNewCodiceAOO()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.CodiceAOO target = null;
            target = (it.gov.digitpa.www.protocollo.CodiceAOO)get_store().add_element_user(CODICEAOO$0);
            return target;
        }
    }
}
