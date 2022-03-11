/*
 * XML Type:  Destinatario
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.Destinatario
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo;


/**
 * An XML Destinatario(@http://www.digitPa.gov.it/protocollo/).
 *
 * This is a complex type.
 */
public interface Destinatario extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(Destinatario.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s88C7D26292FFA5D2830FCC663DD80458").resolveHandle("destinatario1043type");
    
    /**
     * Gets the "Amministrazione" element
     */
    it.gov.digitpa.www.protocollo.Amministrazione getAmministrazione();
    
    /**
     * True if has "Amministrazione" element
     */
    boolean isSetAmministrazione();
    
    /**
     * Sets the "Amministrazione" element
     */
    void setAmministrazione(it.gov.digitpa.www.protocollo.Amministrazione amministrazione);
    
    /**
     * Appends and returns a new empty "Amministrazione" element
     */
    it.gov.digitpa.www.protocollo.Amministrazione addNewAmministrazione();
    
    /**
     * Unsets the "Amministrazione" element
     */
    void unsetAmministrazione();
    
    /**
     * Gets the "AOO" element
     */
    it.gov.digitpa.www.protocollo.AOO getAOO();
    
    /**
     * True if has "AOO" element
     */
    boolean isSetAOO();
    
    /**
     * Sets the "AOO" element
     */
    void setAOO(it.gov.digitpa.www.protocollo.AOO aoo);
    
    /**
     * Appends and returns a new empty "AOO" element
     */
    it.gov.digitpa.www.protocollo.AOO addNewAOO();
    
    /**
     * Unsets the "AOO" element
     */
    void unsetAOO();
    
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
     * Gets array of all "Persona" elements
     */
    it.gov.digitpa.www.protocollo.Persona[] getPersonaArray();
    
    /**
     * Gets ith "Persona" element
     */
    it.gov.digitpa.www.protocollo.Persona getPersonaArray(int i);
    
    /**
     * Returns number of "Persona" element
     */
    int sizeOfPersonaArray();
    
    /**
     * Sets array of all "Persona" element
     */
    void setPersonaArray(it.gov.digitpa.www.protocollo.Persona[] personaArray);
    
    /**
     * Sets ith "Persona" element
     */
    void setPersonaArray(int i, it.gov.digitpa.www.protocollo.Persona persona);
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "Persona" element
     */
    it.gov.digitpa.www.protocollo.Persona insertNewPersona(int i);
    
    /**
     * Appends and returns a new empty value (as xml) as the last "Persona" element
     */
    it.gov.digitpa.www.protocollo.Persona addNewPersona();
    
    /**
     * Removes the ith "Persona" element
     */
    void removePersona(int i);
    
    /**
     * Gets the "IndirizzoTelematico" element
     */
    it.gov.digitpa.www.protocollo.IndirizzoTelematico getIndirizzoTelematico();
    
    /**
     * True if has "IndirizzoTelematico" element
     */
    boolean isSetIndirizzoTelematico();
    
    /**
     * Sets the "IndirizzoTelematico" element
     */
    void setIndirizzoTelematico(it.gov.digitpa.www.protocollo.IndirizzoTelematico indirizzoTelematico);
    
    /**
     * Appends and returns a new empty "IndirizzoTelematico" element
     */
    it.gov.digitpa.www.protocollo.IndirizzoTelematico addNewIndirizzoTelematico();
    
    /**
     * Unsets the "IndirizzoTelematico" element
     */
    void unsetIndirizzoTelematico();
    
    /**
     * Gets array of all "Telefono" elements
     */
    it.gov.digitpa.www.protocollo.Telefono[] getTelefonoArray();
    
    /**
     * Gets ith "Telefono" element
     */
    it.gov.digitpa.www.protocollo.Telefono getTelefonoArray(int i);
    
    /**
     * Returns number of "Telefono" element
     */
    int sizeOfTelefonoArray();
    
    /**
     * Sets array of all "Telefono" element
     */
    void setTelefonoArray(it.gov.digitpa.www.protocollo.Telefono[] telefonoArray);
    
    /**
     * Sets ith "Telefono" element
     */
    void setTelefonoArray(int i, it.gov.digitpa.www.protocollo.Telefono telefono);
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "Telefono" element
     */
    it.gov.digitpa.www.protocollo.Telefono insertNewTelefono(int i);
    
    /**
     * Appends and returns a new empty value (as xml) as the last "Telefono" element
     */
    it.gov.digitpa.www.protocollo.Telefono addNewTelefono();
    
    /**
     * Removes the ith "Telefono" element
     */
    void removeTelefono(int i);
    
    /**
     * Gets array of all "Fax" elements
     */
    it.gov.digitpa.www.protocollo.Fax[] getFaxArray();
    
    /**
     * Gets ith "Fax" element
     */
    it.gov.digitpa.www.protocollo.Fax getFaxArray(int i);
    
    /**
     * Returns number of "Fax" element
     */
    int sizeOfFaxArray();
    
    /**
     * Sets array of all "Fax" element
     */
    void setFaxArray(it.gov.digitpa.www.protocollo.Fax[] faxArray);
    
    /**
     * Sets ith "Fax" element
     */
    void setFaxArray(int i, it.gov.digitpa.www.protocollo.Fax fax);
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "Fax" element
     */
    it.gov.digitpa.www.protocollo.Fax insertNewFax(int i);
    
    /**
     * Appends and returns a new empty value (as xml) as the last "Fax" element
     */
    it.gov.digitpa.www.protocollo.Fax addNewFax();
    
    /**
     * Removes the ith "Fax" element
     */
    void removeFax(int i);
    
    /**
     * Gets the "IndirizzoPostale" element
     */
    it.gov.digitpa.www.protocollo.IndirizzoPostale getIndirizzoPostale();
    
    /**
     * True if has "IndirizzoPostale" element
     */
    boolean isSetIndirizzoPostale();
    
    /**
     * Sets the "IndirizzoPostale" element
     */
    void setIndirizzoPostale(it.gov.digitpa.www.protocollo.IndirizzoPostale indirizzoPostale);
    
    /**
     * Appends and returns a new empty "IndirizzoPostale" element
     */
    it.gov.digitpa.www.protocollo.IndirizzoPostale addNewIndirizzoPostale();
    
    /**
     * Unsets the "IndirizzoPostale" element
     */
    void unsetIndirizzoPostale();
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static it.gov.digitpa.www.protocollo.Destinatario newInstance() {
          return (it.gov.digitpa.www.protocollo.Destinatario) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static it.gov.digitpa.www.protocollo.Destinatario newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (it.gov.digitpa.www.protocollo.Destinatario) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static it.gov.digitpa.www.protocollo.Destinatario parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (it.gov.digitpa.www.protocollo.Destinatario) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static it.gov.digitpa.www.protocollo.Destinatario parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (it.gov.digitpa.www.protocollo.Destinatario) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static it.gov.digitpa.www.protocollo.Destinatario parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.Destinatario) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static it.gov.digitpa.www.protocollo.Destinatario parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.Destinatario) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static it.gov.digitpa.www.protocollo.Destinatario parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.Destinatario) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static it.gov.digitpa.www.protocollo.Destinatario parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.Destinatario) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static it.gov.digitpa.www.protocollo.Destinatario parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.Destinatario) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static it.gov.digitpa.www.protocollo.Destinatario parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.Destinatario) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static it.gov.digitpa.www.protocollo.Destinatario parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.Destinatario) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static it.gov.digitpa.www.protocollo.Destinatario parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.Destinatario) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static it.gov.digitpa.www.protocollo.Destinatario parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (it.gov.digitpa.www.protocollo.Destinatario) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static it.gov.digitpa.www.protocollo.Destinatario parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (it.gov.digitpa.www.protocollo.Destinatario) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static it.gov.digitpa.www.protocollo.Destinatario parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (it.gov.digitpa.www.protocollo.Destinatario) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static it.gov.digitpa.www.protocollo.Destinatario parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (it.gov.digitpa.www.protocollo.Destinatario) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static it.gov.digitpa.www.protocollo.Destinatario parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (it.gov.digitpa.www.protocollo.Destinatario) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static it.gov.digitpa.www.protocollo.Destinatario parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (it.gov.digitpa.www.protocollo.Destinatario) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
