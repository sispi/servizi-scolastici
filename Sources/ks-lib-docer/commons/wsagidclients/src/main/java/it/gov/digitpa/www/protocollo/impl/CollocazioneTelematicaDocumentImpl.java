/*
 * An XML document type.
 * Localname: CollocazioneTelematica
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.CollocazioneTelematicaDocument
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * A document containing one CollocazioneTelematica(@http://www.digitPa.gov.it/protocollo/) element.
 *
 * This is a complex type.
 */
public class CollocazioneTelematicaDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.CollocazioneTelematicaDocument
{
    
    public CollocazioneTelematicaDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName COLLOCAZIONETELEMATICA$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "CollocazioneTelematica");
    
    
    /**
     * Gets the "CollocazioneTelematica" element
     */
    public it.gov.digitpa.www.protocollo.CollocazioneTelematica getCollocazioneTelematica()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.CollocazioneTelematica target = null;
            target = (it.gov.digitpa.www.protocollo.CollocazioneTelematica)get_store().find_element_user(COLLOCAZIONETELEMATICA$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "CollocazioneTelematica" element
     */
    public void setCollocazioneTelematica(it.gov.digitpa.www.protocollo.CollocazioneTelematica collocazioneTelematica)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.CollocazioneTelematica target = null;
            target = (it.gov.digitpa.www.protocollo.CollocazioneTelematica)get_store().find_element_user(COLLOCAZIONETELEMATICA$0, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.CollocazioneTelematica)get_store().add_element_user(COLLOCAZIONETELEMATICA$0);
            }
            target.set(collocazioneTelematica);
        }
    }
    
    /**
     * Appends and returns a new empty "CollocazioneTelematica" element
     */
    public it.gov.digitpa.www.protocollo.CollocazioneTelematica addNewCollocazioneTelematica()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.CollocazioneTelematica target = null;
            target = (it.gov.digitpa.www.protocollo.CollocazioneTelematica)get_store().add_element_user(COLLOCAZIONETELEMATICA$0);
            return target;
        }
    }
}
