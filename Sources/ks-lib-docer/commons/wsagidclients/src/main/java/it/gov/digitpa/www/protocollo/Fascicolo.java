/*
 * XML Type:  Fascicolo
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.Fascicolo
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo;


/**
 * An XML Fascicolo(@http://www.digitPa.gov.it/protocollo/).
 *
 * This is a complex type.
 */
public interface Fascicolo extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(Fascicolo.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s0CE6FBAEFE3D9BB34FDB0E4A28280207").resolveHandle("fascicolo1eedtype");
    
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
     * Gets the "Oggetto" element
     */
    it.gov.digitpa.www.protocollo.Oggetto getOggetto();
    
    /**
     * True if has "Oggetto" element
     */
    boolean isSetOggetto();
    
    /**
     * Sets the "Oggetto" element
     */
    void setOggetto(it.gov.digitpa.www.protocollo.Oggetto oggetto);
    
    /**
     * Appends and returns a new empty "Oggetto" element
     */
    it.gov.digitpa.www.protocollo.Oggetto addNewOggetto();
    
    /**
     * Unsets the "Oggetto" element
     */
    void unsetOggetto();
    
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
     * Gets array of all "Classifica" elements
     */
    it.gov.digitpa.www.protocollo.Classifica[] getClassificaArray();
    
    /**
     * Gets ith "Classifica" element
     */
    it.gov.digitpa.www.protocollo.Classifica getClassificaArray(int i);
    
    /**
     * Returns number of "Classifica" element
     */
    int sizeOfClassificaArray();
    
    /**
     * Sets array of all "Classifica" element
     */
    void setClassificaArray(it.gov.digitpa.www.protocollo.Classifica[] classificaArray);
    
    /**
     * Sets ith "Classifica" element
     */
    void setClassificaArray(int i, it.gov.digitpa.www.protocollo.Classifica classifica);
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "Classifica" element
     */
    it.gov.digitpa.www.protocollo.Classifica insertNewClassifica(int i);
    
    /**
     * Appends and returns a new empty value (as xml) as the last "Classifica" element
     */
    it.gov.digitpa.www.protocollo.Classifica addNewClassifica();
    
    /**
     * Removes the ith "Classifica" element
     */
    void removeClassifica(int i);
    
    /**
     * Gets the "Note" element
     */
    it.gov.digitpa.www.protocollo.Note getNote();
    
    /**
     * True if has "Note" element
     */
    boolean isSetNote();
    
    /**
     * Sets the "Note" element
     */
    void setNote(it.gov.digitpa.www.protocollo.Note note);
    
    /**
     * Appends and returns a new empty "Note" element
     */
    it.gov.digitpa.www.protocollo.Note addNewNote();
    
    /**
     * Unsets the "Note" element
     */
    void unsetNote();
    
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
     * Gets array of all "Documento" elements
     */
    it.gov.digitpa.www.protocollo.Documento[] getDocumentoArray();
    
    /**
     * Gets ith "Documento" element
     */
    it.gov.digitpa.www.protocollo.Documento getDocumentoArray(int i);
    
    /**
     * Returns number of "Documento" element
     */
    int sizeOfDocumentoArray();
    
    /**
     * Sets array of all "Documento" element
     */
    void setDocumentoArray(it.gov.digitpa.www.protocollo.Documento[] documentoArray);
    
    /**
     * Sets ith "Documento" element
     */
    void setDocumentoArray(int i, it.gov.digitpa.www.protocollo.Documento documento);
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "Documento" element
     */
    it.gov.digitpa.www.protocollo.Documento insertNewDocumento(int i);
    
    /**
     * Appends and returns a new empty value (as xml) as the last "Documento" element
     */
    it.gov.digitpa.www.protocollo.Documento addNewDocumento();
    
    /**
     * Removes the ith "Documento" element
     */
    void removeDocumento(int i);
    
    /**
     * Gets array of all "Fascicolo" elements
     */
    it.gov.digitpa.www.protocollo.Fascicolo[] getFascicoloArray();
    
    /**
     * Gets ith "Fascicolo" element
     */
    it.gov.digitpa.www.protocollo.Fascicolo getFascicoloArray(int i);
    
    /**
     * Returns number of "Fascicolo" element
     */
    int sizeOfFascicoloArray();
    
    /**
     * Sets array of all "Fascicolo" element
     */
    void setFascicoloArray(it.gov.digitpa.www.protocollo.Fascicolo[] fascicoloArray);
    
    /**
     * Sets ith "Fascicolo" element
     */
    void setFascicoloArray(int i, it.gov.digitpa.www.protocollo.Fascicolo fascicolo);
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "Fascicolo" element
     */
    it.gov.digitpa.www.protocollo.Fascicolo insertNewFascicolo(int i);
    
    /**
     * Appends and returns a new empty value (as xml) as the last "Fascicolo" element
     */
    it.gov.digitpa.www.protocollo.Fascicolo addNewFascicolo();
    
    /**
     * Removes the ith "Fascicolo" element
     */
    void removeFascicolo(int i);
    
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
        public static it.gov.digitpa.www.protocollo.Fascicolo newInstance() {
          return (it.gov.digitpa.www.protocollo.Fascicolo) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static it.gov.digitpa.www.protocollo.Fascicolo newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (it.gov.digitpa.www.protocollo.Fascicolo) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static it.gov.digitpa.www.protocollo.Fascicolo parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (it.gov.digitpa.www.protocollo.Fascicolo) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static it.gov.digitpa.www.protocollo.Fascicolo parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (it.gov.digitpa.www.protocollo.Fascicolo) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static it.gov.digitpa.www.protocollo.Fascicolo parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.Fascicolo) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static it.gov.digitpa.www.protocollo.Fascicolo parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.Fascicolo) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static it.gov.digitpa.www.protocollo.Fascicolo parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.Fascicolo) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static it.gov.digitpa.www.protocollo.Fascicolo parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.Fascicolo) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static it.gov.digitpa.www.protocollo.Fascicolo parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.Fascicolo) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static it.gov.digitpa.www.protocollo.Fascicolo parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.Fascicolo) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static it.gov.digitpa.www.protocollo.Fascicolo parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.Fascicolo) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static it.gov.digitpa.www.protocollo.Fascicolo parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.Fascicolo) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static it.gov.digitpa.www.protocollo.Fascicolo parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (it.gov.digitpa.www.protocollo.Fascicolo) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static it.gov.digitpa.www.protocollo.Fascicolo parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (it.gov.digitpa.www.protocollo.Fascicolo) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static it.gov.digitpa.www.protocollo.Fascicolo parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (it.gov.digitpa.www.protocollo.Fascicolo) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static it.gov.digitpa.www.protocollo.Fascicolo parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (it.gov.digitpa.www.protocollo.Fascicolo) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static it.gov.digitpa.www.protocollo.Fascicolo parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (it.gov.digitpa.www.protocollo.Fascicolo) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static it.gov.digitpa.www.protocollo.Fascicolo parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (it.gov.digitpa.www.protocollo.Fascicolo) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
