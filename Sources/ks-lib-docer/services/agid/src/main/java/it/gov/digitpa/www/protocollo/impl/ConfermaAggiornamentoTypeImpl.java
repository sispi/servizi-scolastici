/*
 * XML Type:  ConfermaAggiornamentoType
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.ConfermaAggiornamentoType
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * An XML ConfermaAggiornamentoType(@http://www.digitPa.gov.it/protocollo/).
 *
 * This is a complex type.
 */
public class ConfermaAggiornamentoTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.ConfermaAggiornamentoType
{
    
    public ConfermaAggiornamentoTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName AGGIORNAMENTOCONFERMA$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "AggiornamentoConferma");
    private static final javax.xml.namespace.QName DOCUMENTOAGGIORNATO$2 = 
        new javax.xml.namespace.QName("", "DocumentoAggiornato");
    
    
    /**
     * Gets the "AggiornamentoConferma" element
     */
    public it.gov.digitpa.www.protocollo.AggiornamentoConferma getAggiornamentoConferma()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.AggiornamentoConferma target = null;
            target = (it.gov.digitpa.www.protocollo.AggiornamentoConferma)get_store().find_element_user(AGGIORNAMENTOCONFERMA$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "AggiornamentoConferma" element
     */
    public void setAggiornamentoConferma(it.gov.digitpa.www.protocollo.AggiornamentoConferma aggiornamentoConferma)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.AggiornamentoConferma target = null;
            target = (it.gov.digitpa.www.protocollo.AggiornamentoConferma)get_store().find_element_user(AGGIORNAMENTOCONFERMA$0, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.AggiornamentoConferma)get_store().add_element_user(AGGIORNAMENTOCONFERMA$0);
            }
            target.set(aggiornamentoConferma);
        }
    }
    
    /**
     * Appends and returns a new empty "AggiornamentoConferma" element
     */
    public it.gov.digitpa.www.protocollo.AggiornamentoConferma addNewAggiornamentoConferma()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.AggiornamentoConferma target = null;
            target = (it.gov.digitpa.www.protocollo.AggiornamentoConferma)get_store().add_element_user(AGGIORNAMENTOCONFERMA$0);
            return target;
        }
    }
    
    /**
     * Gets the "DocumentoAggiornato" element
     */
    public byte[] getDocumentoAggiornato()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DOCUMENTOAGGIORNATO$2, 0);
            if (target == null)
            {
                return null;
            }
            return target.getByteArrayValue();
        }
    }
    
    /**
     * Gets (as xml) the "DocumentoAggiornato" element
     */
    public org.apache.xmlbeans.XmlBase64Binary xgetDocumentoAggiornato()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlBase64Binary target = null;
            target = (org.apache.xmlbeans.XmlBase64Binary)get_store().find_element_user(DOCUMENTOAGGIORNATO$2, 0);
            return target;
        }
    }
    
    /**
     * True if has "DocumentoAggiornato" element
     */
    public boolean isSetDocumentoAggiornato()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(DOCUMENTOAGGIORNATO$2) != 0;
        }
    }
    
    /**
     * Sets the "DocumentoAggiornato" element
     */
    public void setDocumentoAggiornato(byte[] documentoAggiornato)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DOCUMENTOAGGIORNATO$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(DOCUMENTOAGGIORNATO$2);
            }
            target.setByteArrayValue(documentoAggiornato);
        }
    }
    
    /**
     * Sets (as xml) the "DocumentoAggiornato" element
     */
    public void xsetDocumentoAggiornato(org.apache.xmlbeans.XmlBase64Binary documentoAggiornato)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlBase64Binary target = null;
            target = (org.apache.xmlbeans.XmlBase64Binary)get_store().find_element_user(DOCUMENTOAGGIORNATO$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlBase64Binary)get_store().add_element_user(DOCUMENTOAGGIORNATO$2);
            }
            target.set(documentoAggiornato);
        }
    }
    
    /**
     * Unsets the "DocumentoAggiornato" element
     */
    public void unsetDocumentoAggiornato()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(DOCUMENTOAGGIORNATO$2, 0);
        }
    }
}
