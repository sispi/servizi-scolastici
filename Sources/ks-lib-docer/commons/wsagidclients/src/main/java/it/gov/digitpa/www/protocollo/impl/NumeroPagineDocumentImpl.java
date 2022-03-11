/*
 * An XML document type.
 * Localname: NumeroPagine
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.NumeroPagineDocument
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * A document containing one NumeroPagine(@http://www.digitPa.gov.it/protocollo/) element.
 *
 * This is a complex type.
 */
public class NumeroPagineDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.NumeroPagineDocument
{
    
    public NumeroPagineDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName NUMEROPAGINE$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "NumeroPagine");
    
    
    /**
     * Gets the "NumeroPagine" element
     */
    public it.gov.digitpa.www.protocollo.NumeroPagine getNumeroPagine()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.NumeroPagine target = null;
            target = (it.gov.digitpa.www.protocollo.NumeroPagine)get_store().find_element_user(NUMEROPAGINE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "NumeroPagine" element
     */
    public void setNumeroPagine(it.gov.digitpa.www.protocollo.NumeroPagine numeroPagine)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.NumeroPagine target = null;
            target = (it.gov.digitpa.www.protocollo.NumeroPagine)get_store().find_element_user(NUMEROPAGINE$0, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.NumeroPagine)get_store().add_element_user(NUMEROPAGINE$0);
            }
            target.set(numeroPagine);
        }
    }
    
    /**
     * Appends and returns a new empty "NumeroPagine" element
     */
    public it.gov.digitpa.www.protocollo.NumeroPagine addNewNumeroPagine()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.NumeroPagine target = null;
            target = (it.gov.digitpa.www.protocollo.NumeroPagine)get_store().add_element_user(NUMEROPAGINE$0);
            return target;
        }
    }
}
