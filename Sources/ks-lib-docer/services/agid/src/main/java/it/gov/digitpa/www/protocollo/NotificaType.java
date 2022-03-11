/*
 * XML Type:  NotificaType
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.NotificaType
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo;


/**
 * An XML NotificaType(@http://www.digitPa.gov.it/protocollo/).
 *
 * This is a complex type.
 */
public interface NotificaType extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(NotificaType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s88C7D26292FFA5D2830FCC663DD80458").resolveHandle("notificatype1ba3type");
    
    /**
     * Gets the "ConfermaRicezione" element
     */
    it.gov.digitpa.www.protocollo.ConfermaRicezione getConfermaRicezione();
    
    /**
     * True if has "ConfermaRicezione" element
     */
    boolean isSetConfermaRicezione();
    
    /**
     * Sets the "ConfermaRicezione" element
     */
    void setConfermaRicezione(it.gov.digitpa.www.protocollo.ConfermaRicezione confermaRicezione);
    
    /**
     * Appends and returns a new empty "ConfermaRicezione" element
     */
    it.gov.digitpa.www.protocollo.ConfermaRicezione addNewConfermaRicezione();
    
    /**
     * Unsets the "ConfermaRicezione" element
     */
    void unsetConfermaRicezione();
    
    /**
     * Gets the "ConfermaAggiornamento" element
     */
    it.gov.digitpa.www.protocollo.ConfermaAggiornamentoType getConfermaAggiornamento();
    
    /**
     * True if has "ConfermaAggiornamento" element
     */
    boolean isSetConfermaAggiornamento();
    
    /**
     * Sets the "ConfermaAggiornamento" element
     */
    void setConfermaAggiornamento(it.gov.digitpa.www.protocollo.ConfermaAggiornamentoType confermaAggiornamento);
    
    /**
     * Appends and returns a new empty "ConfermaAggiornamento" element
     */
    it.gov.digitpa.www.protocollo.ConfermaAggiornamentoType addNewConfermaAggiornamento();
    
    /**
     * Unsets the "ConfermaAggiornamento" element
     */
    void unsetConfermaAggiornamento();
    
    /**
     * Gets the "NotificaEccezione" element
     */
    it.gov.digitpa.www.protocollo.NotificaEccezione getNotificaEccezione();
    
    /**
     * True if has "NotificaEccezione" element
     */
    boolean isSetNotificaEccezione();
    
    /**
     * Sets the "NotificaEccezione" element
     */
    void setNotificaEccezione(it.gov.digitpa.www.protocollo.NotificaEccezione notificaEccezione);
    
    /**
     * Appends and returns a new empty "NotificaEccezione" element
     */
    it.gov.digitpa.www.protocollo.NotificaEccezione addNewNotificaEccezione();
    
    /**
     * Unsets the "NotificaEccezione" element
     */
    void unsetNotificaEccezione();
    
    /**
     * Gets the "AnnullamentoProtocollazione" element
     */
    it.gov.digitpa.www.protocollo.AnnullamentoProtocollazione getAnnullamentoProtocollazione();
    
    /**
     * True if has "AnnullamentoProtocollazione" element
     */
    boolean isSetAnnullamentoProtocollazione();
    
    /**
     * Sets the "AnnullamentoProtocollazione" element
     */
    void setAnnullamentoProtocollazione(it.gov.digitpa.www.protocollo.AnnullamentoProtocollazione annullamentoProtocollazione);
    
    /**
     * Appends and returns a new empty "AnnullamentoProtocollazione" element
     */
    it.gov.digitpa.www.protocollo.AnnullamentoProtocollazione addNewAnnullamentoProtocollazione();
    
    /**
     * Unsets the "AnnullamentoProtocollazione" element
     */
    void unsetAnnullamentoProtocollazione();
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static it.gov.digitpa.www.protocollo.NotificaType newInstance() {
          return (it.gov.digitpa.www.protocollo.NotificaType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static it.gov.digitpa.www.protocollo.NotificaType newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (it.gov.digitpa.www.protocollo.NotificaType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static it.gov.digitpa.www.protocollo.NotificaType parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (it.gov.digitpa.www.protocollo.NotificaType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static it.gov.digitpa.www.protocollo.NotificaType parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (it.gov.digitpa.www.protocollo.NotificaType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static it.gov.digitpa.www.protocollo.NotificaType parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.NotificaType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static it.gov.digitpa.www.protocollo.NotificaType parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.NotificaType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static it.gov.digitpa.www.protocollo.NotificaType parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.NotificaType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static it.gov.digitpa.www.protocollo.NotificaType parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.NotificaType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static it.gov.digitpa.www.protocollo.NotificaType parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.NotificaType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static it.gov.digitpa.www.protocollo.NotificaType parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.NotificaType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static it.gov.digitpa.www.protocollo.NotificaType parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.NotificaType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static it.gov.digitpa.www.protocollo.NotificaType parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.NotificaType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static it.gov.digitpa.www.protocollo.NotificaType parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (it.gov.digitpa.www.protocollo.NotificaType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static it.gov.digitpa.www.protocollo.NotificaType parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (it.gov.digitpa.www.protocollo.NotificaType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static it.gov.digitpa.www.protocollo.NotificaType parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (it.gov.digitpa.www.protocollo.NotificaType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static it.gov.digitpa.www.protocollo.NotificaType parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (it.gov.digitpa.www.protocollo.NotificaType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static it.gov.digitpa.www.protocollo.NotificaType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (it.gov.digitpa.www.protocollo.NotificaType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static it.gov.digitpa.www.protocollo.NotificaType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (it.gov.digitpa.www.protocollo.NotificaType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
