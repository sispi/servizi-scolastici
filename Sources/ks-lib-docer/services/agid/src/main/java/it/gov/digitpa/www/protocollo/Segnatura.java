/*
 * XML Type:  Segnatura
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.Segnatura
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo;


/**
 * An XML Segnatura(@http://www.digitPa.gov.it/protocollo/).
 *
 * This is a complex type.
 */
public interface Segnatura extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(Segnatura.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s88C7D26292FFA5D2830FCC663DD80458").resolveHandle("segnaturae278type");
    
    /**
     * Gets the "Intestazione" element
     */
    it.gov.digitpa.www.protocollo.Intestazione getIntestazione();
    
    /**
     * Sets the "Intestazione" element
     */
    void setIntestazione(it.gov.digitpa.www.protocollo.Intestazione intestazione);
    
    /**
     * Appends and returns a new empty "Intestazione" element
     */
    it.gov.digitpa.www.protocollo.Intestazione addNewIntestazione();
    
    /**
     * Gets the "Riferimenti" element
     */
    it.gov.digitpa.www.protocollo.Riferimenti getRiferimenti();
    
    /**
     * True if has "Riferimenti" element
     */
    boolean isSetRiferimenti();
    
    /**
     * Sets the "Riferimenti" element
     */
    void setRiferimenti(it.gov.digitpa.www.protocollo.Riferimenti riferimenti);
    
    /**
     * Appends and returns a new empty "Riferimenti" element
     */
    it.gov.digitpa.www.protocollo.Riferimenti addNewRiferimenti();
    
    /**
     * Unsets the "Riferimenti" element
     */
    void unsetRiferimenti();
    
    /**
     * Gets the "Descrizione" element
     */
    it.gov.digitpa.www.protocollo.Descrizione getDescrizione();
    
    /**
     * Sets the "Descrizione" element
     */
    void setDescrizione(it.gov.digitpa.www.protocollo.Descrizione descrizione);
    
    /**
     * Appends and returns a new empty "Descrizione" element
     */
    it.gov.digitpa.www.protocollo.Descrizione addNewDescrizione();
    
    /**
     * Gets the "PiuInfo" element
     */
    it.gov.digitpa.www.protocollo.PiuInfo getPiuInfo();
    
    /**
     * True if has "PiuInfo" element
     */
    boolean isSetPiuInfo();
    
    /**
     * Sets the "PiuInfo" element
     */
    void setPiuInfo(it.gov.digitpa.www.protocollo.PiuInfo piuInfo);
    
    /**
     * Appends and returns a new empty "PiuInfo" element
     */
    it.gov.digitpa.www.protocollo.PiuInfo addNewPiuInfo();
    
    /**
     * Unsets the "PiuInfo" element
     */
    void unsetPiuInfo();
    
    /**
     * Gets the "versione" attribute
     */
    java.lang.String getVersione();
    
    /**
     * Gets (as xml) the "versione" attribute
     */
    org.apache.xmlbeans.XmlNMTOKEN xgetVersione();
    
    /**
     * True if has "versione" attribute
     */
    boolean isSetVersione();
    
    /**
     * Sets the "versione" attribute
     */
    void setVersione(java.lang.String versione);
    
    /**
     * Sets (as xml) the "versione" attribute
     */
    void xsetVersione(org.apache.xmlbeans.XmlNMTOKEN versione);
    
    /**
     * Unsets the "versione" attribute
     */
    void unsetVersione();
    
    /**
     * Gets the "xml-lang" attribute
     */
    org.apache.xmlbeans.XmlAnySimpleType getXmlLang();
    
    /**
     * True if has "xml-lang" attribute
     */
    boolean isSetXmlLang();
    
    /**
     * Sets the "xml-lang" attribute
     */
    void setXmlLang(org.apache.xmlbeans.XmlAnySimpleType xmlLang);
    
    /**
     * Appends and returns a new empty "xml-lang" attribute
     */
    org.apache.xmlbeans.XmlAnySimpleType addNewXmlLang();
    
    /**
     * Unsets the "xml-lang" attribute
     */
    void unsetXmlLang();
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static it.gov.digitpa.www.protocollo.Segnatura newInstance() {
          return (it.gov.digitpa.www.protocollo.Segnatura) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static it.gov.digitpa.www.protocollo.Segnatura newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (it.gov.digitpa.www.protocollo.Segnatura) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static it.gov.digitpa.www.protocollo.Segnatura parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (it.gov.digitpa.www.protocollo.Segnatura) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static it.gov.digitpa.www.protocollo.Segnatura parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (it.gov.digitpa.www.protocollo.Segnatura) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static it.gov.digitpa.www.protocollo.Segnatura parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.Segnatura) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static it.gov.digitpa.www.protocollo.Segnatura parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.Segnatura) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static it.gov.digitpa.www.protocollo.Segnatura parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.Segnatura) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static it.gov.digitpa.www.protocollo.Segnatura parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.Segnatura) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static it.gov.digitpa.www.protocollo.Segnatura parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.Segnatura) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static it.gov.digitpa.www.protocollo.Segnatura parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.Segnatura) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static it.gov.digitpa.www.protocollo.Segnatura parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.Segnatura) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static it.gov.digitpa.www.protocollo.Segnatura parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.Segnatura) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static it.gov.digitpa.www.protocollo.Segnatura parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (it.gov.digitpa.www.protocollo.Segnatura) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static it.gov.digitpa.www.protocollo.Segnatura parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (it.gov.digitpa.www.protocollo.Segnatura) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static it.gov.digitpa.www.protocollo.Segnatura parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (it.gov.digitpa.www.protocollo.Segnatura) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static it.gov.digitpa.www.protocollo.Segnatura parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (it.gov.digitpa.www.protocollo.Segnatura) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static it.gov.digitpa.www.protocollo.Segnatura parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (it.gov.digitpa.www.protocollo.Segnatura) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static it.gov.digitpa.www.protocollo.Segnatura parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (it.gov.digitpa.www.protocollo.Segnatura) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
