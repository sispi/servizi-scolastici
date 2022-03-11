/*
 * XML Type:  Persona
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.Persona
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo;


/**
 * An XML Persona(@http://www.digitPa.gov.it/protocollo/).
 *
 * This is a complex type.
 */
public interface Persona extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(Persona.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s88C7D26292FFA5D2830FCC663DD80458").resolveHandle("personaffactype");
    
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
     * Gets the "Nome" element
     */
    it.gov.digitpa.www.protocollo.Nome getNome();
    
    /**
     * True if has "Nome" element
     */
    boolean isSetNome();
    
    /**
     * Sets the "Nome" element
     */
    void setNome(it.gov.digitpa.www.protocollo.Nome nome);
    
    /**
     * Appends and returns a new empty "Nome" element
     */
    it.gov.digitpa.www.protocollo.Nome addNewNome();
    
    /**
     * Unsets the "Nome" element
     */
    void unsetNome();
    
    /**
     * Gets the "Cognome" element
     */
    it.gov.digitpa.www.protocollo.Cognome getCognome();
    
    /**
     * True if has "Cognome" element
     */
    boolean isSetCognome();
    
    /**
     * Sets the "Cognome" element
     */
    void setCognome(it.gov.digitpa.www.protocollo.Cognome cognome);
    
    /**
     * Appends and returns a new empty "Cognome" element
     */
    it.gov.digitpa.www.protocollo.Cognome addNewCognome();
    
    /**
     * Unsets the "Cognome" element
     */
    void unsetCognome();
    
    /**
     * Gets the "Titolo" element
     */
    it.gov.digitpa.www.protocollo.Titolo getTitolo();
    
    /**
     * True if has "Titolo" element
     */
    boolean isSetTitolo();
    
    /**
     * Sets the "Titolo" element
     */
    void setTitolo(it.gov.digitpa.www.protocollo.Titolo titolo);
    
    /**
     * Appends and returns a new empty "Titolo" element
     */
    it.gov.digitpa.www.protocollo.Titolo addNewTitolo();
    
    /**
     * Unsets the "Titolo" element
     */
    void unsetTitolo();
    
    /**
     * Gets the "CodiceFiscale" element
     */
    it.gov.digitpa.www.protocollo.CodiceFiscale getCodiceFiscale();
    
    /**
     * True if has "CodiceFiscale" element
     */
    boolean isSetCodiceFiscale();
    
    /**
     * Sets the "CodiceFiscale" element
     */
    void setCodiceFiscale(it.gov.digitpa.www.protocollo.CodiceFiscale codiceFiscale);
    
    /**
     * Appends and returns a new empty "CodiceFiscale" element
     */
    it.gov.digitpa.www.protocollo.CodiceFiscale addNewCodiceFiscale();
    
    /**
     * Unsets the "CodiceFiscale" element
     */
    void unsetCodiceFiscale();
    
    /**
     * Gets the "Identificativo" element
     */
    it.gov.digitpa.www.protocollo.Identificativo getIdentificativo();
    
    /**
     * True if has "Identificativo" element
     */
    boolean isSetIdentificativo();
    
    /**
     * Sets the "Identificativo" element
     */
    void setIdentificativo(it.gov.digitpa.www.protocollo.Identificativo identificativo);
    
    /**
     * Appends and returns a new empty "Identificativo" element
     */
    it.gov.digitpa.www.protocollo.Identificativo addNewIdentificativo();
    
    /**
     * Unsets the "Identificativo" element
     */
    void unsetIdentificativo();
    
    /**
     * Gets the "id" attribute
     */
    java.lang.String getId();
    
    /**
     * Gets (as xml) the "id" attribute
     */
    org.apache.xmlbeans.XmlID xgetId();
    
    /**
     * True if has "id" attribute
     */
    boolean isSetId();
    
    /**
     * Sets the "id" attribute
     */
    void setId(java.lang.String id);
    
    /**
     * Sets (as xml) the "id" attribute
     */
    void xsetId(org.apache.xmlbeans.XmlID id);
    
    /**
     * Unsets the "id" attribute
     */
    void unsetId();
    
    /**
     * Gets the "rife" attribute
     */
    java.lang.String getRife();
    
    /**
     * Gets (as xml) the "rife" attribute
     */
    org.apache.xmlbeans.XmlIDREF xgetRife();
    
    /**
     * True if has "rife" attribute
     */
    boolean isSetRife();
    
    /**
     * Sets the "rife" attribute
     */
    void setRife(java.lang.String rife);
    
    /**
     * Sets (as xml) the "rife" attribute
     */
    void xsetRife(org.apache.xmlbeans.XmlIDREF rife);
    
    /**
     * Unsets the "rife" attribute
     */
    void unsetRife();
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static it.gov.digitpa.www.protocollo.Persona newInstance() {
          return (it.gov.digitpa.www.protocollo.Persona) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static it.gov.digitpa.www.protocollo.Persona newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (it.gov.digitpa.www.protocollo.Persona) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static it.gov.digitpa.www.protocollo.Persona parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (it.gov.digitpa.www.protocollo.Persona) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static it.gov.digitpa.www.protocollo.Persona parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (it.gov.digitpa.www.protocollo.Persona) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static it.gov.digitpa.www.protocollo.Persona parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.Persona) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static it.gov.digitpa.www.protocollo.Persona parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.Persona) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static it.gov.digitpa.www.protocollo.Persona parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.Persona) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static it.gov.digitpa.www.protocollo.Persona parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.Persona) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static it.gov.digitpa.www.protocollo.Persona parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.Persona) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static it.gov.digitpa.www.protocollo.Persona parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.Persona) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static it.gov.digitpa.www.protocollo.Persona parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.Persona) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static it.gov.digitpa.www.protocollo.Persona parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.Persona) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static it.gov.digitpa.www.protocollo.Persona parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (it.gov.digitpa.www.protocollo.Persona) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static it.gov.digitpa.www.protocollo.Persona parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (it.gov.digitpa.www.protocollo.Persona) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static it.gov.digitpa.www.protocollo.Persona parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (it.gov.digitpa.www.protocollo.Persona) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static it.gov.digitpa.www.protocollo.Persona parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (it.gov.digitpa.www.protocollo.Persona) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static it.gov.digitpa.www.protocollo.Persona parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (it.gov.digitpa.www.protocollo.Persona) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static it.gov.digitpa.www.protocollo.Persona parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (it.gov.digitpa.www.protocollo.Persona) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
