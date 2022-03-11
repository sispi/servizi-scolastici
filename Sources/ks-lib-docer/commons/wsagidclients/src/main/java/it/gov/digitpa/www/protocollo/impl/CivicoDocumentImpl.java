/*
 * An XML document type.
 * Localname: Civico
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.CivicoDocument
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * A document containing one Civico(@http://www.digitPa.gov.it/protocollo/) element.
 *
 * This is a complex type.
 */
public class CivicoDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.CivicoDocument
{
    
    public CivicoDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName CIVICO$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Civico");
    
    
    /**
     * Gets the "Civico" element
     */
    public it.gov.digitpa.www.protocollo.Civico getCivico()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Civico target = null;
            target = (it.gov.digitpa.www.protocollo.Civico)get_store().find_element_user(CIVICO$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Civico" element
     */
    public void setCivico(it.gov.digitpa.www.protocollo.Civico civico)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Civico target = null;
            target = (it.gov.digitpa.www.protocollo.Civico)get_store().find_element_user(CIVICO$0, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.Civico)get_store().add_element_user(CIVICO$0);
            }
            target.set(civico);
        }
    }
    
    /**
     * Appends and returns a new empty "Civico" element
     */
    public it.gov.digitpa.www.protocollo.Civico addNewCivico()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Civico target = null;
            target = (it.gov.digitpa.www.protocollo.Civico)get_store().add_element_user(CIVICO$0);
            return target;
        }
    }
}
