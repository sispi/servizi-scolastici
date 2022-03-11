/*
 * An XML document type.
 * Localname: CodiceRegistro
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.CodiceRegistroDocument
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * A document containing one CodiceRegistro(@http://www.digitPa.gov.it/protocollo/) element.
 *
 * This is a complex type.
 */
public class CodiceRegistroDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.CodiceRegistroDocument
{
    
    public CodiceRegistroDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName CODICEREGISTRO$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "CodiceRegistro");
    
    
    /**
     * Gets the "CodiceRegistro" element
     */
    public it.gov.digitpa.www.protocollo.CodiceRegistro getCodiceRegistro()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.CodiceRegistro target = null;
            target = (it.gov.digitpa.www.protocollo.CodiceRegistro)get_store().find_element_user(CODICEREGISTRO$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "CodiceRegistro" element
     */
    public void setCodiceRegistro(it.gov.digitpa.www.protocollo.CodiceRegistro codiceRegistro)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.CodiceRegistro target = null;
            target = (it.gov.digitpa.www.protocollo.CodiceRegistro)get_store().find_element_user(CODICEREGISTRO$0, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.CodiceRegistro)get_store().add_element_user(CODICEREGISTRO$0);
            }
            target.set(codiceRegistro);
        }
    }
    
    /**
     * Appends and returns a new empty "CodiceRegistro" element
     */
    public it.gov.digitpa.www.protocollo.CodiceRegistro addNewCodiceRegistro()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.CodiceRegistro target = null;
            target = (it.gov.digitpa.www.protocollo.CodiceRegistro)get_store().add_element_user(CODICEREGISTRO$0);
            return target;
        }
    }
}
