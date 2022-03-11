/*
 * An XML document type.
 * Localname: IndirizzoTelematico
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.IndirizzoTelematicoDocument
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * A document containing one IndirizzoTelematico(@http://www.digitPa.gov.it/protocollo/) element.
 *
 * This is a complex type.
 */
public class IndirizzoTelematicoDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.IndirizzoTelematicoDocument
{
    
    public IndirizzoTelematicoDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName INDIRIZZOTELEMATICO$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "IndirizzoTelematico");
    
    
    /**
     * Gets the "IndirizzoTelematico" element
     */
    public it.gov.digitpa.www.protocollo.IndirizzoTelematico getIndirizzoTelematico()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.IndirizzoTelematico target = null;
            target = (it.gov.digitpa.www.protocollo.IndirizzoTelematico)get_store().find_element_user(INDIRIZZOTELEMATICO$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "IndirizzoTelematico" element
     */
    public void setIndirizzoTelematico(it.gov.digitpa.www.protocollo.IndirizzoTelematico indirizzoTelematico)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.IndirizzoTelematico target = null;
            target = (it.gov.digitpa.www.protocollo.IndirizzoTelematico)get_store().find_element_user(INDIRIZZOTELEMATICO$0, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.IndirizzoTelematico)get_store().add_element_user(INDIRIZZOTELEMATICO$0);
            }
            target.set(indirizzoTelematico);
        }
    }
    
    /**
     * Appends and returns a new empty "IndirizzoTelematico" element
     */
    public it.gov.digitpa.www.protocollo.IndirizzoTelematico addNewIndirizzoTelematico()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.IndirizzoTelematico target = null;
            target = (it.gov.digitpa.www.protocollo.IndirizzoTelematico)get_store().add_element_user(INDIRIZZOTELEMATICO$0);
            return target;
        }
    }
}
