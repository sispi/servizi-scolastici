/*
 * An XML document type.
 * Localname: Allegati
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.AllegatiDocument
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * A document containing one Allegati(@http://www.digitPa.gov.it/protocollo/) element.
 *
 * This is a complex type.
 */
public class AllegatiDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.AllegatiDocument
{
    
    public AllegatiDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName ALLEGATI$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Allegati");
    
    
    /**
     * Gets the "Allegati" element
     */
    public it.gov.digitpa.www.protocollo.Allegati getAllegati()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Allegati target = null;
            target = (it.gov.digitpa.www.protocollo.Allegati)get_store().find_element_user(ALLEGATI$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Allegati" element
     */
    public void setAllegati(it.gov.digitpa.www.protocollo.Allegati allegati)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Allegati target = null;
            target = (it.gov.digitpa.www.protocollo.Allegati)get_store().find_element_user(ALLEGATI$0, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.Allegati)get_store().add_element_user(ALLEGATI$0);
            }
            target.set(allegati);
        }
    }
    
    /**
     * Appends and returns a new empty "Allegati" element
     */
    public it.gov.digitpa.www.protocollo.Allegati addNewAllegati()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Allegati target = null;
            target = (it.gov.digitpa.www.protocollo.Allegati)get_store().add_element_user(ALLEGATI$0);
            return target;
        }
    }
}
