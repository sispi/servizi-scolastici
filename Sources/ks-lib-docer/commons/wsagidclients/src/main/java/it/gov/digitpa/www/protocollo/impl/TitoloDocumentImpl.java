/*
 * An XML document type.
 * Localname: Titolo
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.TitoloDocument
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * A document containing one Titolo(@http://www.digitPa.gov.it/protocollo/) element.
 *
 * This is a complex type.
 */
public class TitoloDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.TitoloDocument
{
    
    public TitoloDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName TITOLO$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Titolo");
    
    
    /**
     * Gets the "Titolo" element
     */
    public it.gov.digitpa.www.protocollo.Titolo getTitolo()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Titolo target = null;
            target = (it.gov.digitpa.www.protocollo.Titolo)get_store().find_element_user(TITOLO$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Titolo" element
     */
    public void setTitolo(it.gov.digitpa.www.protocollo.Titolo titolo)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Titolo target = null;
            target = (it.gov.digitpa.www.protocollo.Titolo)get_store().find_element_user(TITOLO$0, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.Titolo)get_store().add_element_user(TITOLO$0);
            }
            target.set(titolo);
        }
    }
    
    /**
     * Appends and returns a new empty "Titolo" element
     */
    public it.gov.digitpa.www.protocollo.Titolo addNewTitolo()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Titolo target = null;
            target = (it.gov.digitpa.www.protocollo.Titolo)get_store().add_element_user(TITOLO$0);
            return target;
        }
    }
}
