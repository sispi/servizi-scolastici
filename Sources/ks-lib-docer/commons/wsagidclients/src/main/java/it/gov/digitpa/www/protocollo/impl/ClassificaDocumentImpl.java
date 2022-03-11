/*
 * An XML document type.
 * Localname: Classifica
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.ClassificaDocument
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * A document containing one Classifica(@http://www.digitPa.gov.it/protocollo/) element.
 *
 * This is a complex type.
 */
public class ClassificaDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.ClassificaDocument
{
    
    public ClassificaDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName CLASSIFICA$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Classifica");
    
    
    /**
     * Gets the "Classifica" element
     */
    public it.gov.digitpa.www.protocollo.Classifica getClassifica()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Classifica target = null;
            target = (it.gov.digitpa.www.protocollo.Classifica)get_store().find_element_user(CLASSIFICA$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Classifica" element
     */
    public void setClassifica(it.gov.digitpa.www.protocollo.Classifica classifica)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Classifica target = null;
            target = (it.gov.digitpa.www.protocollo.Classifica)get_store().find_element_user(CLASSIFICA$0, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.Classifica)get_store().add_element_user(CLASSIFICA$0);
            }
            target.set(classifica);
        }
    }
    
    /**
     * Appends and returns a new empty "Classifica" element
     */
    public it.gov.digitpa.www.protocollo.Classifica addNewClassifica()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Classifica target = null;
            target = (it.gov.digitpa.www.protocollo.Classifica)get_store().add_element_user(CLASSIFICA$0);
            return target;
        }
    }
}
