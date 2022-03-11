/*
 * An XML document type.
 * Localname: Segnatura
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.SegnaturaDocument
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * A document containing one Segnatura(@http://www.digitPa.gov.it/protocollo/) element.
 *
 * This is a complex type.
 */
public class SegnaturaDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.SegnaturaDocument
{
    
    public SegnaturaDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName SEGNATURA$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Segnatura");
    
    
    /**
     * Gets the "Segnatura" element
     */
    public it.gov.digitpa.www.protocollo.Segnatura getSegnatura()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Segnatura target = null;
            target = (it.gov.digitpa.www.protocollo.Segnatura)get_store().find_element_user(SEGNATURA$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Segnatura" element
     */
    public void setSegnatura(it.gov.digitpa.www.protocollo.Segnatura segnatura)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Segnatura target = null;
            target = (it.gov.digitpa.www.protocollo.Segnatura)get_store().find_element_user(SEGNATURA$0, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.Segnatura)get_store().add_element_user(SEGNATURA$0);
            }
            target.set(segnatura);
        }
    }
    
    /**
     * Appends and returns a new empty "Segnatura" element
     */
    public it.gov.digitpa.www.protocollo.Segnatura addNewSegnatura()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Segnatura target = null;
            target = (it.gov.digitpa.www.protocollo.Segnatura)get_store().add_element_user(SEGNATURA$0);
            return target;
        }
    }
}
