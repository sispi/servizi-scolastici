/*
 * An XML document type.
 * Localname: UnitaOrganizzativa
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.UnitaOrganizzativaDocument
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * A document containing one UnitaOrganizzativa(@http://www.digitPa.gov.it/protocollo/) element.
 *
 * This is a complex type.
 */
public class UnitaOrganizzativaDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.UnitaOrganizzativaDocument
{
    
    public UnitaOrganizzativaDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName UNITAORGANIZZATIVA$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "UnitaOrganizzativa");
    
    
    /**
     * Gets the "UnitaOrganizzativa" element
     */
    public it.gov.digitpa.www.protocollo.UnitaOrganizzativa getUnitaOrganizzativa()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.UnitaOrganizzativa target = null;
            target = (it.gov.digitpa.www.protocollo.UnitaOrganizzativa)get_store().find_element_user(UNITAORGANIZZATIVA$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "UnitaOrganizzativa" element
     */
    public void setUnitaOrganizzativa(it.gov.digitpa.www.protocollo.UnitaOrganizzativa unitaOrganizzativa)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.UnitaOrganizzativa target = null;
            target = (it.gov.digitpa.www.protocollo.UnitaOrganizzativa)get_store().find_element_user(UNITAORGANIZZATIVA$0, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.UnitaOrganizzativa)get_store().add_element_user(UNITAORGANIZZATIVA$0);
            }
            target.set(unitaOrganizzativa);
        }
    }
    
    /**
     * Appends and returns a new empty "UnitaOrganizzativa" element
     */
    public it.gov.digitpa.www.protocollo.UnitaOrganizzativa addNewUnitaOrganizzativa()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.UnitaOrganizzativa target = null;
            target = (it.gov.digitpa.www.protocollo.UnitaOrganizzativa)get_store().add_element_user(UNITAORGANIZZATIVA$0);
            return target;
        }
    }
}
