/*
 * An XML document type.
 * Localname: SegnaturaEnvelope
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.SegnaturaEnvelopeDocument
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * A document containing one SegnaturaEnvelope(@http://www.digitPa.gov.it/protocollo/) element.
 *
 * This is a complex type.
 */
public class SegnaturaEnvelopeDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.SegnaturaEnvelopeDocument
{
    
    public SegnaturaEnvelopeDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName SEGNATURAENVELOPE$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "SegnaturaEnvelope");
    
    
    /**
     * Gets the "SegnaturaEnvelope" element
     */
    public it.gov.digitpa.www.protocollo.SegnaturaEnvelopeType getSegnaturaEnvelope()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.SegnaturaEnvelopeType target = null;
            target = (it.gov.digitpa.www.protocollo.SegnaturaEnvelopeType)get_store().find_element_user(SEGNATURAENVELOPE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "SegnaturaEnvelope" element
     */
    public void setSegnaturaEnvelope(it.gov.digitpa.www.protocollo.SegnaturaEnvelopeType segnaturaEnvelope)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.SegnaturaEnvelopeType target = null;
            target = (it.gov.digitpa.www.protocollo.SegnaturaEnvelopeType)get_store().find_element_user(SEGNATURAENVELOPE$0, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.SegnaturaEnvelopeType)get_store().add_element_user(SEGNATURAENVELOPE$0);
            }
            target.set(segnaturaEnvelope);
        }
    }
    
    /**
     * Appends and returns a new empty "SegnaturaEnvelope" element
     */
    public it.gov.digitpa.www.protocollo.SegnaturaEnvelopeType addNewSegnaturaEnvelope()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.SegnaturaEnvelopeType target = null;
            target = (it.gov.digitpa.www.protocollo.SegnaturaEnvelopeType)get_store().add_element_user(SEGNATURAENVELOPE$0);
            return target;
        }
    }
}
