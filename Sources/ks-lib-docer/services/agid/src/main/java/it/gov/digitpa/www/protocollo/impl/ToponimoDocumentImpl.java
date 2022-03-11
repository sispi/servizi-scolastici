/*
 * An XML document type.
 * Localname: Toponimo
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.ToponimoDocument
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * A document containing one Toponimo(@http://www.digitPa.gov.it/protocollo/) element.
 *
 * This is a complex type.
 */
public class ToponimoDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.ToponimoDocument
{
    
    public ToponimoDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName TOPONIMO$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Toponimo");
    
    
    /**
     * Gets the "Toponimo" element
     */
    public it.gov.digitpa.www.protocollo.Toponimo getToponimo()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Toponimo target = null;
            target = (it.gov.digitpa.www.protocollo.Toponimo)get_store().find_element_user(TOPONIMO$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Toponimo" element
     */
    public void setToponimo(it.gov.digitpa.www.protocollo.Toponimo toponimo)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Toponimo target = null;
            target = (it.gov.digitpa.www.protocollo.Toponimo)get_store().find_element_user(TOPONIMO$0, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.Toponimo)get_store().add_element_user(TOPONIMO$0);
            }
            target.set(toponimo);
        }
    }
    
    /**
     * Appends and returns a new empty "Toponimo" element
     */
    public it.gov.digitpa.www.protocollo.Toponimo addNewToponimo()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Toponimo target = null;
            target = (it.gov.digitpa.www.protocollo.Toponimo)get_store().add_element_user(TOPONIMO$0);
            return target;
        }
    }
}
