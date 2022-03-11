/*
 * XML Type:  UnitaOrganizzativa
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.UnitaOrganizzativa
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo;


/**
 * An XML UnitaOrganizzativa(@http://www.digitPa.gov.it/protocollo/).
 *
 * This is a complex type.
 */
public interface UnitaOrganizzativa extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(UnitaOrganizzativa.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s88C7D26292FFA5D2830FCC663DD80458").resolveHandle("unitaorganizzativa581etype");
    
    /**
     * Gets the "Denominazione" element
     */
    it.gov.digitpa.www.protocollo.Denominazione getDenominazione();
    
    /**
     * Sets the "Denominazione" element
     */
    void setDenominazione(it.gov.digitpa.www.protocollo.Denominazione denominazione);
    
    /**
     * Appends and returns a new empty "Denominazione" element
     */
    it.gov.digitpa.www.protocollo.Denominazione addNewDenominazione();
    
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
     * Gets the "UnitaOrganizzativa" element
     */
    it.gov.digitpa.www.protocollo.UnitaOrganizzativa getUnitaOrganizzativa();
    
    /**
     * True if has "UnitaOrganizzativa" element
     */
    boolean isSetUnitaOrganizzativa();
    
    /**
     * Sets the "UnitaOrganizzativa" element
     */
    void setUnitaOrganizzativa(it.gov.digitpa.www.protocollo.UnitaOrganizzativa unitaOrganizzativa);
    
    /**
     * Appends and returns a new empty "UnitaOrganizzativa" element
     */
    it.gov.digitpa.www.protocollo.UnitaOrganizzativa addNewUnitaOrganizzativa();
    
    /**
     * Unsets the "UnitaOrganizzativa" element
     */
    void unsetUnitaOrganizzativa();
    
    /**
     * Gets array of all "Ruolo" elements
     */
    it.gov.digitpa.www.protocollo.Ruolo[] getRuoloArray();
    
    /**
     * Gets ith "Ruolo" element
     */
    it.gov.digitpa.www.protocollo.Ruolo getRuoloArray(int i);
    
    /**
     * Returns number of "Ruolo" element
     */
    int sizeOfRuoloArray();
    
    /**
     * Sets array of all "Ruolo" element
     */
    void setRuoloArray(it.gov.digitpa.www.protocollo.Ruolo[] ruoloArray);
    
    /**
     * Sets ith "Ruolo" element
     */
    void setRuoloArray(int i, it.gov.digitpa.www.protocollo.Ruolo ruolo);
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "Ruolo" element
     */
    it.gov.digitpa.www.protocollo.Ruolo insertNewRuolo(int i);
    
    /**
     * Appends and returns a new empty value (as xml) as the last "Ruolo" element
     */
    it.gov.digitpa.www.protocollo.Ruolo addNewRuolo();
    
    /**
     * Removes the ith "Ruolo" element
     */
    void removeRuolo(int i);
    
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
     * Gets array of all "IndirizzoTelematico" elements
     */
    it.gov.digitpa.www.protocollo.IndirizzoTelematico[] getIndirizzoTelematicoArray();
    
    /**
     * Gets ith "IndirizzoTelematico" element
     */
    it.gov.digitpa.www.protocollo.IndirizzoTelematico getIndirizzoTelematicoArray(int i);
    
    /**
     * Returns number of "IndirizzoTelematico" element
     */
    int sizeOfIndirizzoTelematicoArray();
    
    /**
     * Sets array of all "IndirizzoTelematico" element
     */
    void setIndirizzoTelematicoArray(it.gov.digitpa.www.protocollo.IndirizzoTelematico[] indirizzoTelematicoArray);
    
    /**
     * Sets ith "IndirizzoTelematico" element
     */
    void setIndirizzoTelematicoArray(int i, it.gov.digitpa.www.protocollo.IndirizzoTelematico indirizzoTelematico);
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "IndirizzoTelematico" element
     */
    it.gov.digitpa.www.protocollo.IndirizzoTelematico insertNewIndirizzoTelematico(int i);
    
    /**
     * Appends and returns a new empty value (as xml) as the last "IndirizzoTelematico" element
     */
    it.gov.digitpa.www.protocollo.IndirizzoTelematico addNewIndirizzoTelematico();
    
    /**
     * Removes the ith "IndirizzoTelematico" element
     */
    void removeIndirizzoTelematico(int i);
    
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
     * Gets the "tipo" attribute
     */
    it.gov.digitpa.www.protocollo.UnitaOrganizzativa.Tipo.Enum getTipo();
    
    /**
     * Gets (as xml) the "tipo" attribute
     */
    it.gov.digitpa.www.protocollo.UnitaOrganizzativa.Tipo xgetTipo();
    
    /**
     * True if has "tipo" attribute
     */
    boolean isSetTipo();
    
    /**
     * Sets the "tipo" attribute
     */
    void setTipo(it.gov.digitpa.www.protocollo.UnitaOrganizzativa.Tipo.Enum tipo);
    
    /**
     * Sets (as xml) the "tipo" attribute
     */
    void xsetTipo(it.gov.digitpa.www.protocollo.UnitaOrganizzativa.Tipo tipo);
    
    /**
     * Unsets the "tipo" attribute
     */
    void unsetTipo();
    
    /**
     * An XML tipo(@).
     *
     * This is an atomic type that is a restriction of it.gov.digitpa.www.protocollo.UnitaOrganizzativa$Tipo.
     */
    public interface Tipo extends org.apache.xmlbeans.XmlNMTOKEN
    {
        public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
            org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(Tipo.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s88C7D26292FFA5D2830FCC663DD80458").resolveHandle("tipo064aattrtype");
        
        org.apache.xmlbeans.StringEnumAbstractBase enumValue();
        void set(org.apache.xmlbeans.StringEnumAbstractBase e);
        
        static final Enum PERMANENTE = Enum.forString("permanente");
        static final Enum TEMPORANEA = Enum.forString("temporanea");
        
        static final int INT_PERMANENTE = Enum.INT_PERMANENTE;
        static final int INT_TEMPORANEA = Enum.INT_TEMPORANEA;
        
        /**
         * Enumeration value class for it.gov.digitpa.www.protocollo.UnitaOrganizzativa$Tipo.
         * These enum values can be used as follows:
         * <pre>
         * enum.toString(); // returns the string value of the enum
         * enum.intValue(); // returns an int value, useful for switches
         * // e.g., case Enum.INT_PERMANENTE
         * Enum.forString(s); // returns the enum value for a string
         * Enum.forInt(i); // returns the enum value for an int
         * </pre>
         * Enumeration objects are immutable singleton objects that
         * can be compared using == object equality. They have no
         * public constructor. See the constants defined within this
         * class for all the valid values.
         */
        static final class Enum extends org.apache.xmlbeans.StringEnumAbstractBase
        {
            /**
             * Returns the enum value for a string, or null if none.
             */
            public static Enum forString(java.lang.String s)
                { return (Enum)table.forString(s); }
            /**
             * Returns the enum value corresponding to an int, or null if none.
             */
            public static Enum forInt(int i)
                { return (Enum)table.forInt(i); }
            
            private Enum(java.lang.String s, int i)
                { super(s, i); }
            
            static final int INT_PERMANENTE = 1;
            static final int INT_TEMPORANEA = 2;
            
            public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table =
                new org.apache.xmlbeans.StringEnumAbstractBase.Table
            (
                new Enum[]
                {
                    new Enum("permanente", INT_PERMANENTE),
                    new Enum("temporanea", INT_TEMPORANEA),
                }
            );
            private static final long serialVersionUID = 1L;
            private java.lang.Object readResolve() { return forInt(intValue()); } 
        }
        
        /**
         * A factory class with static methods for creating instances
         * of this type.
         */
        
        public static final class Factory
        {
            public static it.gov.digitpa.www.protocollo.UnitaOrganizzativa.Tipo newValue(java.lang.Object obj) {
              return (it.gov.digitpa.www.protocollo.UnitaOrganizzativa.Tipo) type.newValue( obj ); }
            
            public static it.gov.digitpa.www.protocollo.UnitaOrganizzativa.Tipo newInstance() {
              return (it.gov.digitpa.www.protocollo.UnitaOrganizzativa.Tipo) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
            
            public static it.gov.digitpa.www.protocollo.UnitaOrganizzativa.Tipo newInstance(org.apache.xmlbeans.XmlOptions options) {
              return (it.gov.digitpa.www.protocollo.UnitaOrganizzativa.Tipo) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
            
            private Factory() { } // No instance of this class allowed
        }
    }
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static it.gov.digitpa.www.protocollo.UnitaOrganizzativa newInstance() {
          return (it.gov.digitpa.www.protocollo.UnitaOrganizzativa) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static it.gov.digitpa.www.protocollo.UnitaOrganizzativa newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (it.gov.digitpa.www.protocollo.UnitaOrganizzativa) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static it.gov.digitpa.www.protocollo.UnitaOrganizzativa parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (it.gov.digitpa.www.protocollo.UnitaOrganizzativa) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static it.gov.digitpa.www.protocollo.UnitaOrganizzativa parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (it.gov.digitpa.www.protocollo.UnitaOrganizzativa) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static it.gov.digitpa.www.protocollo.UnitaOrganizzativa parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.UnitaOrganizzativa) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static it.gov.digitpa.www.protocollo.UnitaOrganizzativa parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.UnitaOrganizzativa) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static it.gov.digitpa.www.protocollo.UnitaOrganizzativa parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.UnitaOrganizzativa) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static it.gov.digitpa.www.protocollo.UnitaOrganizzativa parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.UnitaOrganizzativa) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static it.gov.digitpa.www.protocollo.UnitaOrganizzativa parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.UnitaOrganizzativa) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static it.gov.digitpa.www.protocollo.UnitaOrganizzativa parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.UnitaOrganizzativa) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static it.gov.digitpa.www.protocollo.UnitaOrganizzativa parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.UnitaOrganizzativa) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static it.gov.digitpa.www.protocollo.UnitaOrganizzativa parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.UnitaOrganizzativa) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static it.gov.digitpa.www.protocollo.UnitaOrganizzativa parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (it.gov.digitpa.www.protocollo.UnitaOrganizzativa) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static it.gov.digitpa.www.protocollo.UnitaOrganizzativa parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (it.gov.digitpa.www.protocollo.UnitaOrganizzativa) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static it.gov.digitpa.www.protocollo.UnitaOrganizzativa parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (it.gov.digitpa.www.protocollo.UnitaOrganizzativa) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static it.gov.digitpa.www.protocollo.UnitaOrganizzativa parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (it.gov.digitpa.www.protocollo.UnitaOrganizzativa) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static it.gov.digitpa.www.protocollo.UnitaOrganizzativa parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (it.gov.digitpa.www.protocollo.UnitaOrganizzativa) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static it.gov.digitpa.www.protocollo.UnitaOrganizzativa parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (it.gov.digitpa.www.protocollo.UnitaOrganizzativa) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
