/*
 * An XML document type.
 * Localname: AOO
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.AOODocument
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * A document containing one AOO(@http://www.digitPa.gov.it/protocollo/) element.
 *
 * This is a complex type.
 */
public class AOODocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.AOODocument
{
    
    public AOODocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName AOO$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "AOO");
    
    
    /**
     * Gets the "AOO" element
     */
    public it.gov.digitpa.www.protocollo.AOO getAOO()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.AOO target = null;
            target = (it.gov.digitpa.www.protocollo.AOO)get_store().find_element_user(AOO$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "AOO" element
     */
    public void setAOO(it.gov.digitpa.www.protocollo.AOO aoo)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.AOO target = null;
            target = (it.gov.digitpa.www.protocollo.AOO)get_store().find_element_user(AOO$0, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.AOO)get_store().add_element_user(AOO$0);
            }
            target.set(aoo);
        }
    }
    
    /**
     * Appends and returns a new empty "AOO" element
     */
    public it.gov.digitpa.www.protocollo.AOO addNewAOO()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.AOO target = null;
            target = (it.gov.digitpa.www.protocollo.AOO)get_store().add_element_user(AOO$0);
            return target;
        }
    }
}
