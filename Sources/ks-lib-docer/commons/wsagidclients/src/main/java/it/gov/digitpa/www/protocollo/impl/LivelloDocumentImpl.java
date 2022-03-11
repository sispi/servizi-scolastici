/*
 * An XML document type.
 * Localname: Livello
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.LivelloDocument
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * A document containing one Livello(@http://www.digitPa.gov.it/protocollo/) element.
 *
 * This is a complex type.
 */
public class LivelloDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.LivelloDocument
{
    
    public LivelloDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName LIVELLO$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Livello");
    
    
    /**
     * Gets the "Livello" element
     */
    public it.gov.digitpa.www.protocollo.Livello getLivello()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Livello target = null;
            target = (it.gov.digitpa.www.protocollo.Livello)get_store().find_element_user(LIVELLO$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Livello" element
     */
    public void setLivello(it.gov.digitpa.www.protocollo.Livello livello)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Livello target = null;
            target = (it.gov.digitpa.www.protocollo.Livello)get_store().find_element_user(LIVELLO$0, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.Livello)get_store().add_element_user(LIVELLO$0);
            }
            target.set(livello);
        }
    }
    
    /**
     * Appends and returns a new empty "Livello" element
     */
    public it.gov.digitpa.www.protocollo.Livello addNewLivello()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Livello target = null;
            target = (it.gov.digitpa.www.protocollo.Livello)get_store().add_element_user(LIVELLO$0);
            return target;
        }
    }
}
