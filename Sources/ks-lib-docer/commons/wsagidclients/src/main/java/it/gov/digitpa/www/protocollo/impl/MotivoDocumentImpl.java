/*
 * An XML document type.
 * Localname: Motivo
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.MotivoDocument
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * A document containing one Motivo(@http://www.digitPa.gov.it/protocollo/) element.
 *
 * This is a complex type.
 */
public class MotivoDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.MotivoDocument
{
    
    public MotivoDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName MOTIVO$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Motivo");
    
    
    /**
     * Gets the "Motivo" element
     */
    public it.gov.digitpa.www.protocollo.Motivo getMotivo()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Motivo target = null;
            target = (it.gov.digitpa.www.protocollo.Motivo)get_store().find_element_user(MOTIVO$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Motivo" element
     */
    public void setMotivo(it.gov.digitpa.www.protocollo.Motivo motivo)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Motivo target = null;
            target = (it.gov.digitpa.www.protocollo.Motivo)get_store().find_element_user(MOTIVO$0, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.Motivo)get_store().add_element_user(MOTIVO$0);
            }
            target.set(motivo);
        }
    }
    
    /**
     * Appends and returns a new empty "Motivo" element
     */
    public it.gov.digitpa.www.protocollo.Motivo addNewMotivo()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Motivo target = null;
            target = (it.gov.digitpa.www.protocollo.Motivo)get_store().add_element_user(MOTIVO$0);
            return target;
        }
    }
}
