/*
 * An XML document type.
 * Localname: TestoDelMessaggio
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.TestoDelMessaggioDocument
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * A document containing one TestoDelMessaggio(@http://www.digitPa.gov.it/protocollo/) element.
 *
 * This is a complex type.
 */
public class TestoDelMessaggioDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.TestoDelMessaggioDocument
{
    
    public TestoDelMessaggioDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName TESTODELMESSAGGIO$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "TestoDelMessaggio");
    
    
    /**
     * Gets the "TestoDelMessaggio" element
     */
    public it.gov.digitpa.www.protocollo.TestoDelMessaggio getTestoDelMessaggio()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.TestoDelMessaggio target = null;
            target = (it.gov.digitpa.www.protocollo.TestoDelMessaggio)get_store().find_element_user(TESTODELMESSAGGIO$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "TestoDelMessaggio" element
     */
    public void setTestoDelMessaggio(it.gov.digitpa.www.protocollo.TestoDelMessaggio testoDelMessaggio)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.TestoDelMessaggio target = null;
            target = (it.gov.digitpa.www.protocollo.TestoDelMessaggio)get_store().find_element_user(TESTODELMESSAGGIO$0, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.TestoDelMessaggio)get_store().add_element_user(TESTODELMESSAGGIO$0);
            }
            target.set(testoDelMessaggio);
        }
    }
    
    /**
     * Appends and returns a new empty "TestoDelMessaggio" element
     */
    public it.gov.digitpa.www.protocollo.TestoDelMessaggio addNewTestoDelMessaggio()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.TestoDelMessaggio target = null;
            target = (it.gov.digitpa.www.protocollo.TestoDelMessaggio)get_store().add_element_user(TESTODELMESSAGGIO$0);
            return target;
        }
    }
}
