/*
 * An XML document type.
 * Localname: RiferimentiTelematici
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.RiferimentiTelematiciDocument
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * A document containing one RiferimentiTelematici(@http://www.digitPa.gov.it/protocollo/) element.
 *
 * This is a complex type.
 */
public class RiferimentiTelematiciDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.RiferimentiTelematiciDocument
{
    
    public RiferimentiTelematiciDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName RIFERIMENTITELEMATICI$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "RiferimentiTelematici");
    
    
    /**
     * Gets the "RiferimentiTelematici" element
     */
    public it.gov.digitpa.www.protocollo.RiferimentiTelematici getRiferimentiTelematici()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.RiferimentiTelematici target = null;
            target = (it.gov.digitpa.www.protocollo.RiferimentiTelematici)get_store().find_element_user(RIFERIMENTITELEMATICI$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "RiferimentiTelematici" element
     */
    public void setRiferimentiTelematici(it.gov.digitpa.www.protocollo.RiferimentiTelematici riferimentiTelematici)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.RiferimentiTelematici target = null;
            target = (it.gov.digitpa.www.protocollo.RiferimentiTelematici)get_store().find_element_user(RIFERIMENTITELEMATICI$0, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.RiferimentiTelematici)get_store().add_element_user(RIFERIMENTITELEMATICI$0);
            }
            target.set(riferimentiTelematici);
        }
    }
    
    /**
     * Appends and returns a new empty "RiferimentiTelematici" element
     */
    public it.gov.digitpa.www.protocollo.RiferimentiTelematici addNewRiferimentiTelematici()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.RiferimentiTelematici target = null;
            target = (it.gov.digitpa.www.protocollo.RiferimentiTelematici)get_store().add_element_user(RIFERIMENTITELEMATICI$0);
            return target;
        }
    }
}
