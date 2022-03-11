/*
 * An XML document type.
 * Localname: TipoContestoProcedurale
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.TipoContestoProceduraleDocument
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * A document containing one TipoContestoProcedurale(@http://www.digitPa.gov.it/protocollo/) element.
 *
 * This is a complex type.
 */
public class TipoContestoProceduraleDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.TipoContestoProceduraleDocument
{
    
    public TipoContestoProceduraleDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName TIPOCONTESTOPROCEDURALE$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "TipoContestoProcedurale");
    
    
    /**
     * Gets the "TipoContestoProcedurale" element
     */
    public it.gov.digitpa.www.protocollo.TipoContestoProcedurale getTipoContestoProcedurale()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.TipoContestoProcedurale target = null;
            target = (it.gov.digitpa.www.protocollo.TipoContestoProcedurale)get_store().find_element_user(TIPOCONTESTOPROCEDURALE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "TipoContestoProcedurale" element
     */
    public void setTipoContestoProcedurale(it.gov.digitpa.www.protocollo.TipoContestoProcedurale tipoContestoProcedurale)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.TipoContestoProcedurale target = null;
            target = (it.gov.digitpa.www.protocollo.TipoContestoProcedurale)get_store().find_element_user(TIPOCONTESTOPROCEDURALE$0, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.TipoContestoProcedurale)get_store().add_element_user(TIPOCONTESTOPROCEDURALE$0);
            }
            target.set(tipoContestoProcedurale);
        }
    }
    
    /**
     * Appends and returns a new empty "TipoContestoProcedurale" element
     */
    public it.gov.digitpa.www.protocollo.TipoContestoProcedurale addNewTipoContestoProcedurale()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.TipoContestoProcedurale target = null;
            target = (it.gov.digitpa.www.protocollo.TipoContestoProcedurale)get_store().add_element_user(TIPOCONTESTOPROCEDURALE$0);
            return target;
        }
    }
}
