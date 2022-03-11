/*
 * An XML document type.
 * Localname: Persona
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.PersonaDocument
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * A document containing one Persona(@http://www.digitPa.gov.it/protocollo/) element.
 *
 * This is a complex type.
 */
public class PersonaDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.PersonaDocument
{
    
    public PersonaDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName PERSONA$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Persona");
    
    
    /**
     * Gets the "Persona" element
     */
    public it.gov.digitpa.www.protocollo.Persona getPersona()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Persona target = null;
            target = (it.gov.digitpa.www.protocollo.Persona)get_store().find_element_user(PERSONA$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Persona" element
     */
    public void setPersona(it.gov.digitpa.www.protocollo.Persona persona)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Persona target = null;
            target = (it.gov.digitpa.www.protocollo.Persona)get_store().find_element_user(PERSONA$0, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.Persona)get_store().add_element_user(PERSONA$0);
            }
            target.set(persona);
        }
    }
    
    /**
     * Appends and returns a new empty "Persona" element
     */
    public it.gov.digitpa.www.protocollo.Persona addNewPersona()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Persona target = null;
            target = (it.gov.digitpa.www.protocollo.Persona)get_store().add_element_user(PERSONA$0);
            return target;
        }
    }
}
