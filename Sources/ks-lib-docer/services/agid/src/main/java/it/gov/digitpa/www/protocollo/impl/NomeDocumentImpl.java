/*
 * An XML document type.
 * Localname: Nome
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.NomeDocument
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * A document containing one Nome(@http://www.digitPa.gov.it/protocollo/) element.
 *
 * This is a complex type.
 */
public class NomeDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.NomeDocument
{
    
    public NomeDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName NOME$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Nome");
    
    
    /**
     * Gets the "Nome" element
     */
    public it.gov.digitpa.www.protocollo.Nome getNome()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Nome target = null;
            target = (it.gov.digitpa.www.protocollo.Nome)get_store().find_element_user(NOME$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Nome" element
     */
    public void setNome(it.gov.digitpa.www.protocollo.Nome nome)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Nome target = null;
            target = (it.gov.digitpa.www.protocollo.Nome)get_store().find_element_user(NOME$0, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.Nome)get_store().add_element_user(NOME$0);
            }
            target.set(nome);
        }
    }
    
    /**
     * Appends and returns a new empty "Nome" element
     */
    public it.gov.digitpa.www.protocollo.Nome addNewNome()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Nome target = null;
            target = (it.gov.digitpa.www.protocollo.Nome)get_store().add_element_user(NOME$0);
            return target;
        }
    }
}
