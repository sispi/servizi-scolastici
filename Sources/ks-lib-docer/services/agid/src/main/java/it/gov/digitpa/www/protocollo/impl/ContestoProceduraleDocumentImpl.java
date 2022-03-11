/*
 * An XML document type.
 * Localname: ContestoProcedurale
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.ContestoProceduraleDocument
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * A document containing one ContestoProcedurale(@http://www.digitPa.gov.it/protocollo/) element.
 *
 * This is a complex type.
 */
public class ContestoProceduraleDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.ContestoProceduraleDocument
{
    
    public ContestoProceduraleDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName CONTESTOPROCEDURALE$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "ContestoProcedurale");
    
    
    /**
     * Gets the "ContestoProcedurale" element
     */
    public it.gov.digitpa.www.protocollo.ContestoProcedurale getContestoProcedurale()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.ContestoProcedurale target = null;
            target = (it.gov.digitpa.www.protocollo.ContestoProcedurale)get_store().find_element_user(CONTESTOPROCEDURALE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "ContestoProcedurale" element
     */
    public void setContestoProcedurale(it.gov.digitpa.www.protocollo.ContestoProcedurale contestoProcedurale)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.ContestoProcedurale target = null;
            target = (it.gov.digitpa.www.protocollo.ContestoProcedurale)get_store().find_element_user(CONTESTOPROCEDURALE$0, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.ContestoProcedurale)get_store().add_element_user(CONTESTOPROCEDURALE$0);
            }
            target.set(contestoProcedurale);
        }
    }
    
    /**
     * Appends and returns a new empty "ContestoProcedurale" element
     */
    public it.gov.digitpa.www.protocollo.ContestoProcedurale addNewContestoProcedurale()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.ContestoProcedurale target = null;
            target = (it.gov.digitpa.www.protocollo.ContestoProcedurale)get_store().add_element_user(CONTESTOPROCEDURALE$0);
            return target;
        }
    }
}
