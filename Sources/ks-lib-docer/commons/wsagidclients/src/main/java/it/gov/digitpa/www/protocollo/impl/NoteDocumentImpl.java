/*
 * An XML document type.
 * Localname: Note
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.NoteDocument
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * A document containing one Note(@http://www.digitPa.gov.it/protocollo/) element.
 *
 * This is a complex type.
 */
public class NoteDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.NoteDocument
{
    
    public NoteDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName NOTE$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "Note");
    
    
    /**
     * Gets the "Note" element
     */
    public it.gov.digitpa.www.protocollo.Note getNote()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Note target = null;
            target = (it.gov.digitpa.www.protocollo.Note)get_store().find_element_user(NOTE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Note" element
     */
    public void setNote(it.gov.digitpa.www.protocollo.Note note)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Note target = null;
            target = (it.gov.digitpa.www.protocollo.Note)get_store().find_element_user(NOTE$0, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.Note)get_store().add_element_user(NOTE$0);
            }
            target.set(note);
        }
    }
    
    /**
     * Appends and returns a new empty "Note" element
     */
    public it.gov.digitpa.www.protocollo.Note addNewNote()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.Note target = null;
            target = (it.gov.digitpa.www.protocollo.Note)get_store().add_element_user(NOTE$0);
            return target;
        }
    }
}
