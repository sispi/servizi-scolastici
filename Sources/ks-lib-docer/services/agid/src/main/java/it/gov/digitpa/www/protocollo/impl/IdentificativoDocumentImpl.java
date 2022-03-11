/*
 * An XML document type.
 * Localname: Identificativo
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.IdentificativoDocument
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * A document containing one Identificativo(@http://www.digitPa.gov.it/protocollo/) element.
 *
 * This is a complex type.
 */
public class IdentificativoDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.IdentificativoDocument
{
    
    public IdentificativoDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName IDENTIFICATIVO$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Identificativo");
    
    
    /**
     * Gets the "Identificativo" element
     */
    public it.gov.digitpa.www.protocollo.Identificativo getIdentificativo()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Identificativo target = null;
            target = (it.gov.digitpa.www.protocollo.Identificativo)get_store().find_element_user(IDENTIFICATIVO$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Identificativo" element
     */
    public void setIdentificativo(it.gov.digitpa.www.protocollo.Identificativo identificativo)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Identificativo target = null;
            target = (it.gov.digitpa.www.protocollo.Identificativo)get_store().find_element_user(IDENTIFICATIVO$0, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.Identificativo)get_store().add_element_user(IDENTIFICATIVO$0);
            }
            target.set(identificativo);
        }
    }
    
    /**
     * Appends and returns a new empty "Identificativo" element
     */
    public it.gov.digitpa.www.protocollo.Identificativo addNewIdentificativo()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Identificativo target = null;
            target = (it.gov.digitpa.www.protocollo.Identificativo)get_store().add_element_user(IDENTIFICATIVO$0);
            return target;
        }
    }
}
