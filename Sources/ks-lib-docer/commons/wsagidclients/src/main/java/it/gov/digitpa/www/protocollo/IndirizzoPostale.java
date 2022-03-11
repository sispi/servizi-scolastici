/*
 * XML Type:  IndirizzoPostale
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.IndirizzoPostale
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo;


/**
 * An XML IndirizzoPostale(@http://www.digitPa.gov.it/protocollo/).
 *
 * This is a complex type.
 */
public interface IndirizzoPostale extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(IndirizzoPostale.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s0CE6FBAEFE3D9BB34FDB0E4A28280207").resolveHandle("indirizzopostale655etype");
    
    /**
     * Gets the "Denominazione" element
     */
    it.gov.digitpa.www.protocollo.Denominazione getDenominazione();
    
    /**
     * True if has "Denominazione" element
     */
    boolean isSetDenominazione();
    
    /**
     * Sets the "Denominazione" element
     */
    void setDenominazione(it.gov.digitpa.www.protocollo.Denominazione denominazione);
    
    /**
     * Appends and returns a new empty "Denominazione" element
     */
    it.gov.digitpa.www.protocollo.Denominazione addNewDenominazione();
    
    /**
     * Unsets the "Denominazione" element
     */
    void unsetDenominazione();
    
    /**
     * Gets the "Indirizzo" element
     */
    it.gov.digitpa.www.protocollo.Indirizzo getIndirizzo();
    
    /**
     * True if has "Indirizzo" element
     */
    boolean isSetIndirizzo();
    
    /**
     * Sets the "Indirizzo" element
     */
    void setIndirizzo(it.gov.digitpa.www.protocollo.Indirizzo indirizzo);
    
    /**
     * Appends and returns a new empty "Indirizzo" element
     */
    it.gov.digitpa.www.protocollo.Indirizzo addNewIndirizzo();
    
    /**
     * Unsets the "Indirizzo" element
     */
    void unsetIndirizzo();
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static it.gov.digitpa.www.protocollo.IndirizzoPostale newInstance() {
          return (it.gov.digitpa.www.protocollo.IndirizzoPostale) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static it.gov.digitpa.www.protocollo.IndirizzoPostale newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (it.gov.digitpa.www.protocollo.IndirizzoPostale) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static it.gov.digitpa.www.protocollo.IndirizzoPostale parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (it.gov.digitpa.www.protocollo.IndirizzoPostale) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static it.gov.digitpa.www.protocollo.IndirizzoPostale parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (it.gov.digitpa.www.protocollo.IndirizzoPostale) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static it.gov.digitpa.www.protocollo.IndirizzoPostale parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.IndirizzoPostale) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static it.gov.digitpa.www.protocollo.IndirizzoPostale parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.IndirizzoPostale) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static it.gov.digitpa.www.protocollo.IndirizzoPostale parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.IndirizzoPostale) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static it.gov.digitpa.www.protocollo.IndirizzoPostale parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.IndirizzoPostale) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static it.gov.digitpa.www.protocollo.IndirizzoPostale parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.IndirizzoPostale) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static it.gov.digitpa.www.protocollo.IndirizzoPostale parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.IndirizzoPostale) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static it.gov.digitpa.www.protocollo.IndirizzoPostale parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.IndirizzoPostale) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static it.gov.digitpa.www.protocollo.IndirizzoPostale parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.IndirizzoPostale) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static it.gov.digitpa.www.protocollo.IndirizzoPostale parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (it.gov.digitpa.www.protocollo.IndirizzoPostale) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static it.gov.digitpa.www.protocollo.IndirizzoPostale parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (it.gov.digitpa.www.protocollo.IndirizzoPostale) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static it.gov.digitpa.www.protocollo.IndirizzoPostale parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (it.gov.digitpa.www.protocollo.IndirizzoPostale) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static it.gov.digitpa.www.protocollo.IndirizzoPostale parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (it.gov.digitpa.www.protocollo.IndirizzoPostale) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static it.gov.digitpa.www.protocollo.IndirizzoPostale parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (it.gov.digitpa.www.protocollo.IndirizzoPostale) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static it.gov.digitpa.www.protocollo.IndirizzoPostale parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (it.gov.digitpa.www.protocollo.IndirizzoPostale) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
