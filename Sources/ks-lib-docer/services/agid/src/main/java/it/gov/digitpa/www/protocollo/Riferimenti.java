/*
 * XML Type:  Riferimenti
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.Riferimenti
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo;


/**
 * An XML Riferimenti(@http://www.digitPa.gov.it/protocollo/).
 *
 * This is a complex type.
 */
public interface Riferimenti extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(Riferimenti.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s88C7D26292FFA5D2830FCC663DD80458").resolveHandle("riferimenti7ffetype");
    
    /**
     * Gets array of all "Messaggio" elements
     */
    it.gov.digitpa.www.protocollo.Messaggio[] getMessaggioArray();
    
    /**
     * Gets ith "Messaggio" element
     */
    it.gov.digitpa.www.protocollo.Messaggio getMessaggioArray(int i);
    
    /**
     * Returns number of "Messaggio" element
     */
    int sizeOfMessaggioArray();
    
    /**
     * Sets array of all "Messaggio" element
     */
    void setMessaggioArray(it.gov.digitpa.www.protocollo.Messaggio[] messaggioArray);
    
    /**
     * Sets ith "Messaggio" element
     */
    void setMessaggioArray(int i, it.gov.digitpa.www.protocollo.Messaggio messaggio);
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "Messaggio" element
     */
    it.gov.digitpa.www.protocollo.Messaggio insertNewMessaggio(int i);
    
    /**
     * Appends and returns a new empty value (as xml) as the last "Messaggio" element
     */
    it.gov.digitpa.www.protocollo.Messaggio addNewMessaggio();
    
    /**
     * Removes the ith "Messaggio" element
     */
    void removeMessaggio(int i);
    
    /**
     * Gets array of all "ContestoProcedurale" elements
     */
    it.gov.digitpa.www.protocollo.ContestoProcedurale[] getContestoProceduraleArray();
    
    /**
     * Gets ith "ContestoProcedurale" element
     */
    it.gov.digitpa.www.protocollo.ContestoProcedurale getContestoProceduraleArray(int i);
    
    /**
     * Returns number of "ContestoProcedurale" element
     */
    int sizeOfContestoProceduraleArray();
    
    /**
     * Sets array of all "ContestoProcedurale" element
     */
    void setContestoProceduraleArray(it.gov.digitpa.www.protocollo.ContestoProcedurale[] contestoProceduraleArray);
    
    /**
     * Sets ith "ContestoProcedurale" element
     */
    void setContestoProceduraleArray(int i, it.gov.digitpa.www.protocollo.ContestoProcedurale contestoProcedurale);
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "ContestoProcedurale" element
     */
    it.gov.digitpa.www.protocollo.ContestoProcedurale insertNewContestoProcedurale(int i);
    
    /**
     * Appends and returns a new empty value (as xml) as the last "ContestoProcedurale" element
     */
    it.gov.digitpa.www.protocollo.ContestoProcedurale addNewContestoProcedurale();
    
    /**
     * Removes the ith "ContestoProcedurale" element
     */
    void removeContestoProcedurale(int i);
    
    /**
     * Gets array of all "Procedimento" elements
     */
    it.gov.digitpa.www.protocollo.Procedimento[] getProcedimentoArray();
    
    /**
     * Gets ith "Procedimento" element
     */
    it.gov.digitpa.www.protocollo.Procedimento getProcedimentoArray(int i);
    
    /**
     * Returns number of "Procedimento" element
     */
    int sizeOfProcedimentoArray();
    
    /**
     * Sets array of all "Procedimento" element
     */
    void setProcedimentoArray(it.gov.digitpa.www.protocollo.Procedimento[] procedimentoArray);
    
    /**
     * Sets ith "Procedimento" element
     */
    void setProcedimentoArray(int i, it.gov.digitpa.www.protocollo.Procedimento procedimento);
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "Procedimento" element
     */
    it.gov.digitpa.www.protocollo.Procedimento insertNewProcedimento(int i);
    
    /**
     * Appends and returns a new empty value (as xml) as the last "Procedimento" element
     */
    it.gov.digitpa.www.protocollo.Procedimento addNewProcedimento();
    
    /**
     * Removes the ith "Procedimento" element
     */
    void removeProcedimento(int i);
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static it.gov.digitpa.www.protocollo.Riferimenti newInstance() {
          return (it.gov.digitpa.www.protocollo.Riferimenti) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static it.gov.digitpa.www.protocollo.Riferimenti newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (it.gov.digitpa.www.protocollo.Riferimenti) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static it.gov.digitpa.www.protocollo.Riferimenti parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (it.gov.digitpa.www.protocollo.Riferimenti) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static it.gov.digitpa.www.protocollo.Riferimenti parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (it.gov.digitpa.www.protocollo.Riferimenti) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static it.gov.digitpa.www.protocollo.Riferimenti parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.Riferimenti) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static it.gov.digitpa.www.protocollo.Riferimenti parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.Riferimenti) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static it.gov.digitpa.www.protocollo.Riferimenti parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.Riferimenti) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static it.gov.digitpa.www.protocollo.Riferimenti parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.Riferimenti) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static it.gov.digitpa.www.protocollo.Riferimenti parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.Riferimenti) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static it.gov.digitpa.www.protocollo.Riferimenti parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.Riferimenti) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static it.gov.digitpa.www.protocollo.Riferimenti parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.Riferimenti) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static it.gov.digitpa.www.protocollo.Riferimenti parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.Riferimenti) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static it.gov.digitpa.www.protocollo.Riferimenti parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (it.gov.digitpa.www.protocollo.Riferimenti) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static it.gov.digitpa.www.protocollo.Riferimenti parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (it.gov.digitpa.www.protocollo.Riferimenti) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static it.gov.digitpa.www.protocollo.Riferimenti parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (it.gov.digitpa.www.protocollo.Riferimenti) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static it.gov.digitpa.www.protocollo.Riferimenti parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (it.gov.digitpa.www.protocollo.Riferimenti) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static it.gov.digitpa.www.protocollo.Riferimenti parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (it.gov.digitpa.www.protocollo.Riferimenti) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static it.gov.digitpa.www.protocollo.Riferimenti parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (it.gov.digitpa.www.protocollo.Riferimenti) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
