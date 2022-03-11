/*
 * XML Type:  NotificaType
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.NotificaType
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo.impl;
/**
 * An XML NotificaType(@http://www.digitPa.gov.it/protocollo/).
 *
 * This is a complex type.
 */
public class NotificaTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements it.gov.digitpa.www.protocollo.NotificaType
{
    
    public NotificaTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName CONFERMARICEZIONE$0 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "ConfermaRicezione");
    private static final javax.xml.namespace.QName CONFERMAAGGIORNAMENTO$2 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "ConfermaAggiornamento");
    private static final javax.xml.namespace.QName NOTIFICAECCEZIONE$4 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "NotificaEccezione");
    private static final javax.xml.namespace.QName ANNULLAMENTOPROTOCOLLAZIONE$6 = 
        new javax.xml.namespace.QName("http://www.digitPa.gov.it/protocollo/", "AnnullamentoProtocollazione");
    
    
    /**
     * Gets the "ConfermaRicezione" element
     */
    public it.gov.digitpa.www.protocollo.ConfermaRicezione getConfermaRicezione()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.ConfermaRicezione target = null;
            target = (it.gov.digitpa.www.protocollo.ConfermaRicezione)get_store().find_element_user(CONFERMARICEZIONE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "ConfermaRicezione" element
     */
    public boolean isSetConfermaRicezione()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(CONFERMARICEZIONE$0) != 0;
        }
    }
    
    /**
     * Sets the "ConfermaRicezione" element
     */
    public void setConfermaRicezione(it.gov.digitpa.www.protocollo.ConfermaRicezione confermaRicezione)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.ConfermaRicezione target = null;
            target = (it.gov.digitpa.www.protocollo.ConfermaRicezione)get_store().find_element_user(CONFERMARICEZIONE$0, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.ConfermaRicezione)get_store().add_element_user(CONFERMARICEZIONE$0);
            }
            target.set(confermaRicezione);
        }
    }
    
    /**
     * Appends and returns a new empty "ConfermaRicezione" element
     */
    public it.gov.digitpa.www.protocollo.ConfermaRicezione addNewConfermaRicezione()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.ConfermaRicezione target = null;
            target = (it.gov.digitpa.www.protocollo.ConfermaRicezione)get_store().add_element_user(CONFERMARICEZIONE$0);
            return target;
        }
    }
    
    /**
     * Unsets the "ConfermaRicezione" element
     */
    public void unsetConfermaRicezione()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(CONFERMARICEZIONE$0, 0);
        }
    }
    
    /**
     * Gets the "ConfermaAggiornamento" element
     */
    public it.gov.digitpa.www.protocollo.ConfermaAggiornamentoType getConfermaAggiornamento()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.ConfermaAggiornamentoType target = null;
            target = (it.gov.digitpa.www.protocollo.ConfermaAggiornamentoType)get_store().find_element_user(CONFERMAAGGIORNAMENTO$2, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "ConfermaAggiornamento" element
     */
    public boolean isSetConfermaAggiornamento()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(CONFERMAAGGIORNAMENTO$2) != 0;
        }
    }
    
    /**
     * Sets the "ConfermaAggiornamento" element
     */
    public void setConfermaAggiornamento(it.gov.digitpa.www.protocollo.ConfermaAggiornamentoType confermaAggiornamento)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.ConfermaAggiornamentoType target = null;
            target = (it.gov.digitpa.www.protocollo.ConfermaAggiornamentoType)get_store().find_element_user(CONFERMAAGGIORNAMENTO$2, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.ConfermaAggiornamentoType)get_store().add_element_user(CONFERMAAGGIORNAMENTO$2);
            }
            target.set(confermaAggiornamento);
        }
    }
    
    /**
     * Appends and returns a new empty "ConfermaAggiornamento" element
     */
    public it.gov.digitpa.www.protocollo.ConfermaAggiornamentoType addNewConfermaAggiornamento()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.ConfermaAggiornamentoType target = null;
            target = (it.gov.digitpa.www.protocollo.ConfermaAggiornamentoType)get_store().add_element_user(CONFERMAAGGIORNAMENTO$2);
            return target;
        }
    }
    
    /**
     * Unsets the "ConfermaAggiornamento" element
     */
    public void unsetConfermaAggiornamento()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(CONFERMAAGGIORNAMENTO$2, 0);
        }
    }
    
    /**
     * Gets the "NotificaEccezione" element
     */
    public it.gov.digitpa.www.protocollo.NotificaEccezione getNotificaEccezione()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.NotificaEccezione target = null;
            target = (it.gov.digitpa.www.protocollo.NotificaEccezione)get_store().find_element_user(NOTIFICAECCEZIONE$4, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "NotificaEccezione" element
     */
    public boolean isSetNotificaEccezione()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(NOTIFICAECCEZIONE$4) != 0;
        }
    }
    
    /**
     * Sets the "NotificaEccezione" element
     */
    public void setNotificaEccezione(it.gov.digitpa.www.protocollo.NotificaEccezione notificaEccezione)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.NotificaEccezione target = null;
            target = (it.gov.digitpa.www.protocollo.NotificaEccezione)get_store().find_element_user(NOTIFICAECCEZIONE$4, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.NotificaEccezione)get_store().add_element_user(NOTIFICAECCEZIONE$4);
            }
            target.set(notificaEccezione);
        }
    }
    
    /**
     * Appends and returns a new empty "NotificaEccezione" element
     */
    public it.gov.digitpa.www.protocollo.NotificaEccezione addNewNotificaEccezione()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.NotificaEccezione target = null;
            target = (it.gov.digitpa.www.protocollo.NotificaEccezione)get_store().add_element_user(NOTIFICAECCEZIONE$4);
            return target;
        }
    }
    
    /**
     * Unsets the "NotificaEccezione" element
     */
    public void unsetNotificaEccezione()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(NOTIFICAECCEZIONE$4, 0);
        }
    }
    
    /**
     * Gets the "AnnullamentoProtocollazione" element
     */
    public it.gov.digitpa.www.protocollo.AnnullamentoProtocollazione getAnnullamentoProtocollazione()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.AnnullamentoProtocollazione target = null;
            target = (it.gov.digitpa.www.protocollo.AnnullamentoProtocollazione)get_store().find_element_user(ANNULLAMENTOPROTOCOLLAZIONE$6, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "AnnullamentoProtocollazione" element
     */
    public boolean isSetAnnullamentoProtocollazione()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(ANNULLAMENTOPROTOCOLLAZIONE$6) != 0;
        }
    }
    
    /**
     * Sets the "AnnullamentoProtocollazione" element
     */
    public void setAnnullamentoProtocollazione(it.gov.digitpa.www.protocollo.AnnullamentoProtocollazione annullamentoProtocollazione)
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.AnnullamentoProtocollazione target = null;
            target = (it.gov.digitpa.www.protocollo.AnnullamentoProtocollazione)get_store().find_element_user(ANNULLAMENTOPROTOCOLLAZIONE$6, 0);
            if (target == null)
            {
                target = (it.gov.digitpa.www.protocollo.AnnullamentoProtocollazione)get_store().add_element_user(ANNULLAMENTOPROTOCOLLAZIONE$6);
            }
            target.set(annullamentoProtocollazione);
        }
    }
    
    /**
     * Appends and returns a new empty "AnnullamentoProtocollazione" element
     */
    public it.gov.digitpa.www.protocollo.AnnullamentoProtocollazione addNewAnnullamentoProtocollazione()
    {
        synchronized (monitor())
        {
            check_orphaned();
            it.gov.digitpa.www.protocollo.AnnullamentoProtocollazione target = null;
            target = (it.gov.digitpa.www.protocollo.AnnullamentoProtocollazione)get_store().add_element_user(ANNULLAMENTOPROTOCOLLAZIONE$6);
            return target;
        }
    }
    
    /**
     * Unsets the "AnnullamentoProtocollazione" element
     */
    public void unsetAnnullamentoProtocollazione()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(ANNULLAMENTOPROTOCOLLAZIONE$6, 0);
        }
    }
}
