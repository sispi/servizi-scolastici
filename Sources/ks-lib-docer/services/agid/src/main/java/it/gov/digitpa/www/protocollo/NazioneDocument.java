/*
 * An XML document type.
 * Localname: Nazione
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.NazioneDocument
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo;


/**
 * A document containing one Nazione(@http://www.digitPa.gov.it/protocollo/) element.
 *
 * This is a complex type.
 */
public interface NazioneDocument extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(NazioneDocument.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s88C7D26292FFA5D2830FCC663DD80458").resolveHandle("nazioneb0f4doctype");
    
    /**
     * Gets the "Nazione" element
     */
    it.gov.digitpa.www.protocollo.Nazione getNazione();
    
    /**
     * Sets the "Nazione" element
     */
    void setNazione(it.gov.digitpa.www.protocollo.Nazione nazione);
    
    /**
     * Appends and returns a new empty "Nazione" element
     */
    it.gov.digitpa.www.protocollo.Nazione addNewNazione();
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static it.gov.digitpa.www.protocollo.NazioneDocument newInstance() {
          return (it.gov.digitpa.www.protocollo.NazioneDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static it.gov.digitpa.www.protocollo.NazioneDocument newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (it.gov.digitpa.www.protocollo.NazioneDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static it.gov.digitpa.www.protocollo.NazioneDocument parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (it.gov.digitpa.www.protocollo.NazioneDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static it.gov.digitpa.www.protocollo.NazioneDocument parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (it.gov.digitpa.www.protocollo.NazioneDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static it.gov.digitpa.www.protocollo.NazioneDocument parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.NazioneDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static it.gov.digitpa.www.protocollo.NazioneDocument parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.NazioneDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static it.gov.digitpa.www.protocollo.NazioneDocument parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.NazioneDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static it.gov.digitpa.www.protocollo.NazioneDocument parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.NazioneDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static it.gov.digitpa.www.protocollo.NazioneDocument parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.NazioneDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static it.gov.digitpa.www.protocollo.NazioneDocument parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.NazioneDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static it.gov.digitpa.www.protocollo.NazioneDocument parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.NazioneDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static it.gov.digitpa.www.protocollo.NazioneDocument parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.NazioneDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static it.gov.digitpa.www.protocollo.NazioneDocument parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (it.gov.digitpa.www.protocollo.NazioneDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static it.gov.digitpa.www.protocollo.NazioneDocument parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (it.gov.digitpa.www.protocollo.NazioneDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static it.gov.digitpa.www.protocollo.NazioneDocument parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (it.gov.digitpa.www.protocollo.NazioneDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static it.gov.digitpa.www.protocollo.NazioneDocument parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (it.gov.digitpa.www.protocollo.NazioneDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static it.gov.digitpa.www.protocollo.NazioneDocument parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (it.gov.digitpa.www.protocollo.NazioneDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static it.gov.digitpa.www.protocollo.NazioneDocument parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (it.gov.digitpa.www.protocollo.NazioneDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}