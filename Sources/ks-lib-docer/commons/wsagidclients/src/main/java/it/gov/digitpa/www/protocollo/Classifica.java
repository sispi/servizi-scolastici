/*
 * XML Type:  Classifica
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.Classifica
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo;


/**
 * An XML Classifica(@http://www.digitPa.gov.it/protocollo/).
 *
 * This is a complex type.
 */
public interface Classifica extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(Classifica.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s0CE6FBAEFE3D9BB34FDB0E4A28280207").resolveHandle("classifica79c4type");
    
    /**
     * Gets the "CodiceAmministrazione" element
     */
    it.gov.digitpa.www.protocollo.CodiceAmministrazione getCodiceAmministrazione();
    
    /**
     * True if has "CodiceAmministrazione" element
     */
    boolean isSetCodiceAmministrazione();
    
    /**
     * Sets the "CodiceAmministrazione" element
     */
    void setCodiceAmministrazione(it.gov.digitpa.www.protocollo.CodiceAmministrazione codiceAmministrazione);
    
    /**
     * Appends and returns a new empty "CodiceAmministrazione" element
     */
    it.gov.digitpa.www.protocollo.CodiceAmministrazione addNewCodiceAmministrazione();
    
    /**
     * Unsets the "CodiceAmministrazione" element
     */
    void unsetCodiceAmministrazione();
    
    /**
     * Gets the "CodiceAOO" element
     */
    it.gov.digitpa.www.protocollo.CodiceAOO getCodiceAOO();
    
    /**
     * True if has "CodiceAOO" element
     */
    boolean isSetCodiceAOO();
    
    /**
     * Sets the "CodiceAOO" element
     */
    void setCodiceAOO(it.gov.digitpa.www.protocollo.CodiceAOO codiceAOO);
    
    /**
     * Appends and returns a new empty "CodiceAOO" element
     */
    it.gov.digitpa.www.protocollo.CodiceAOO addNewCodiceAOO();
    
    /**
     * Unsets the "CodiceAOO" element
     */
    void unsetCodiceAOO();
    
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
     * Gets array of all "Livello" elements
     */
    it.gov.digitpa.www.protocollo.Livello[] getLivelloArray();
    
    /**
     * Gets ith "Livello" element
     */
    it.gov.digitpa.www.protocollo.Livello getLivelloArray(int i);
    
    /**
     * Returns number of "Livello" element
     */
    int sizeOfLivelloArray();
    
    /**
     * Sets array of all "Livello" element
     */
    void setLivelloArray(it.gov.digitpa.www.protocollo.Livello[] livelloArray);
    
    /**
     * Sets ith "Livello" element
     */
    void setLivelloArray(int i, it.gov.digitpa.www.protocollo.Livello livello);
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "Livello" element
     */
    it.gov.digitpa.www.protocollo.Livello insertNewLivello(int i);
    
    /**
     * Appends and returns a new empty value (as xml) as the last "Livello" element
     */
    it.gov.digitpa.www.protocollo.Livello addNewLivello();
    
    /**
     * Removes the ith "Livello" element
     */
    void removeLivello(int i);
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static it.gov.digitpa.www.protocollo.Classifica newInstance() {
          return (it.gov.digitpa.www.protocollo.Classifica) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static it.gov.digitpa.www.protocollo.Classifica newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (it.gov.digitpa.www.protocollo.Classifica) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static it.gov.digitpa.www.protocollo.Classifica parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (it.gov.digitpa.www.protocollo.Classifica) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static it.gov.digitpa.www.protocollo.Classifica parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (it.gov.digitpa.www.protocollo.Classifica) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static it.gov.digitpa.www.protocollo.Classifica parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.Classifica) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static it.gov.digitpa.www.protocollo.Classifica parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.Classifica) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static it.gov.digitpa.www.protocollo.Classifica parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.Classifica) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static it.gov.digitpa.www.protocollo.Classifica parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.Classifica) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static it.gov.digitpa.www.protocollo.Classifica parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.Classifica) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static it.gov.digitpa.www.protocollo.Classifica parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.Classifica) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static it.gov.digitpa.www.protocollo.Classifica parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.Classifica) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static it.gov.digitpa.www.protocollo.Classifica parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.Classifica) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static it.gov.digitpa.www.protocollo.Classifica parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (it.gov.digitpa.www.protocollo.Classifica) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static it.gov.digitpa.www.protocollo.Classifica parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (it.gov.digitpa.www.protocollo.Classifica) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static it.gov.digitpa.www.protocollo.Classifica parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (it.gov.digitpa.www.protocollo.Classifica) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static it.gov.digitpa.www.protocollo.Classifica parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (it.gov.digitpa.www.protocollo.Classifica) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static it.gov.digitpa.www.protocollo.Classifica parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (it.gov.digitpa.www.protocollo.Classifica) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static it.gov.digitpa.www.protocollo.Classifica parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (it.gov.digitpa.www.protocollo.Classifica) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
