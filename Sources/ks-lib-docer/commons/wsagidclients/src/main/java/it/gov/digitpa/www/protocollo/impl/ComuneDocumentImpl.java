/*
 * An XML document type.
 * Localname: Comune
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.ComuneDocument
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * A document containing one Comune(@http://www.digitPa.gov.it/protocollo/) element.
 *
 * This is a complex type.
 */
public class ComuneDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.ComuneDocument
{
    
    public ComuneDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName COMUNE$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Comune");
    
    
    /**
     * Gets the "Comune" element
     */
    public it.gov.digitpa.www.protocollo.Comune getComune()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Comune target = null;
            target = (it.gov.digitpa.www.protocollo.Comune)get_store().find_element_user(COMUNE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Comune" element
     */
    public void setComune(it.gov.digitpa.www.protocollo.Comune comune)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Comune target = null;
            target = (it.gov.digitpa.www.protocollo.Comune)get_store().find_element_user(COMUNE$0, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.Comune)get_store().add_element_user(COMUNE$0);
            }
            target.set(comune);
        }
    }
    
    /**
     * Appends and returns a new empty "Comune" element
     */
    public it.gov.digitpa.www.protocollo.Comune addNewComune()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Comune target = null;
            target = (it.gov.digitpa.www.protocollo.Comune)get_store().add_element_user(COMUNE$0);
            return target;
        }
    }
}
