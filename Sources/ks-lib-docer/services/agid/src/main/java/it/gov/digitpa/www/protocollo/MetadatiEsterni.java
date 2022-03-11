/*
 * XML Type:  MetadatiEsterni
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.MetadatiEsterni
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo;


/**
 * An XML MetadatiEsterni(@http://www.digitPa.gov.it/protocollo/).
 *
 * This is a complex type.
 */
public interface MetadatiEsterni extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(MetadatiEsterni.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s88C7D26292FFA5D2830FCC663DD80458").resolveHandle("metadatiesterni1337type");
    
    /**
     * Gets the "NomeFile" element
     */
    it.gov.digitpa.www.protocollo.NomeFile getNomeFile();
    
    /**
     * Sets the "NomeFile" element
     */
    void setNomeFile(it.gov.digitpa.www.protocollo.NomeFile nomeFile);
    
    /**
     * Appends and returns a new empty "NomeFile" element
     */
    it.gov.digitpa.www.protocollo.NomeFile addNewNomeFile();
    
    /**
     * Gets the "Impronta" element
     */
    it.gov.digitpa.www.protocollo.Impronta getImpronta();
    
    /**
     * True if has "Impronta" element
     */
    boolean isSetImpronta();
    
    /**
     * Sets the "Impronta" element
     */
    void setImpronta(it.gov.digitpa.www.protocollo.Impronta impronta);
    
    /**
     * Appends and returns a new empty "Impronta" element
     */
    it.gov.digitpa.www.protocollo.Impronta addNewImpronta();
    
    /**
     * Unsets the "Impronta" element
     */
    void unsetImpronta();
    
    /**
     * Gets the "codifica" attribute
     */
    it.gov.digitpa.www.protocollo.MetadatiEsterni.Codifica.Enum getCodifica();
    
    /**
     * Gets (as xml) the "codifica" attribute
     */
    it.gov.digitpa.www.protocollo.MetadatiEsterni.Codifica xgetCodifica();
    
    /**
     * Sets the "codifica" attribute
     */
    void setCodifica(it.gov.digitpa.www.protocollo.MetadatiEsterni.Codifica.Enum codifica);
    
    /**
     * Sets (as xml) the "codifica" attribute
     */
    void xsetCodifica(it.gov.digitpa.www.protocollo.MetadatiEsterni.Codifica codifica);
    
    /**
     * Gets the "estensione" attribute
     */
    java.lang.String getEstensione();
    
    /**
     * Gets (as xml) the "estensione" attribute
     */
    org.apache.xmlbeans.XmlNMTOKEN xgetEstensione();
    
    /**
     * True if has "estensione" attribute
     */
    boolean isSetEstensione();
    
    /**
     * Sets the "estensione" attribute
     */
    void setEstensione(java.lang.String estensione);
    
    /**
     * Sets (as xml) the "estensione" attribute
     */
    void xsetEstensione(org.apache.xmlbeans.XmlNMTOKEN estensione);
    
    /**
     * Unsets the "estensione" attribute
     */
    void unsetEstensione();
    
    /**
     * Gets the "formato" attribute
     */
    org.apache.xmlbeans.XmlAnySimpleType getFormato();
    
    /**
     * Sets the "formato" attribute
     */
    void setFormato(org.apache.xmlbeans.XmlAnySimpleType formato);
    
    /**
     * Appends and returns a new empty "formato" attribute
     */
    org.apache.xmlbeans.XmlAnySimpleType addNewFormato();
    
    /**
     * An XML codifica(@).
     *
     * This is an atomic type that is a restriction of it.gov.digitpa.www.protocollo.MetadatiEsterni$Codifica.
     */
    public interface Codifica extends org.apache.xmlbeans.XmlNMTOKEN
    {
        public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
            org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(Codifica.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s88C7D26292FFA5D2830FCC663DD80458").resolveHandle("codifica0329attrtype");
        
        org.apache.xmlbeans.StringEnumAbstractBase enumValue();
        void set(org.apache.xmlbeans.StringEnumAbstractBase e);
        
        static final Enum BINARY = Enum.forString("binary");
        static final Enum XTOKEN = Enum.forString("xtoken");
        static final Enum QUOTEDPRINTABLE = Enum.forString("quotedprintable");
        static final Enum X_7_BIT = Enum.forString("7bit");
        static final Enum BASE_64 = Enum.forString("base64");
        static final Enum X_8_BIT = Enum.forString("8bit");
        
        static final int INT_BINARY = Enum.INT_BINARY;
        static final int INT_XTOKEN = Enum.INT_XTOKEN;
        static final int INT_QUOTEDPRINTABLE = Enum.INT_QUOTEDPRINTABLE;
        static final int INT_X_7_BIT = Enum.INT_X_7_BIT;
        static final int INT_BASE_64 = Enum.INT_BASE_64;
        static final int INT_X_8_BIT = Enum.INT_X_8_BIT;
        
        /**
         * Enumeration value class for it.gov.digitpa.www.protocollo.MetadatiEsterni$Codifica.
         * These enum values can be used as follows:
         * <pre>
         * enum.toString(); // returns the string value of the enum
         * enum.intValue(); // returns an int value, useful for switches
         * // e.g., case Enum.INT_BINARY
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
            
            static final int INT_BINARY = 1;
            static final int INT_XTOKEN = 2;
            static final int INT_QUOTEDPRINTABLE = 3;
            static final int INT_X_7_BIT = 4;
            static final int INT_BASE_64 = 5;
            static final int INT_X_8_BIT = 6;
            
            public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table =
                new org.apache.xmlbeans.StringEnumAbstractBase.Table
            (
                new Enum[]
                {
                    new Enum("binary", INT_BINARY),
                    new Enum("xtoken", INT_XTOKEN),
                    new Enum("quotedprintable", INT_QUOTEDPRINTABLE),
                    new Enum("7bit", INT_X_7_BIT),
                    new Enum("base64", INT_BASE_64),
                    new Enum("8bit", INT_X_8_BIT),
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
            public static it.gov.digitpa.www.protocollo.MetadatiEsterni.Codifica newValue(java.lang.Object obj) {
              return (it.gov.digitpa.www.protocollo.MetadatiEsterni.Codifica) type.newValue( obj ); }
            
            public static it.gov.digitpa.www.protocollo.MetadatiEsterni.Codifica newInstance() {
              return (it.gov.digitpa.www.protocollo.MetadatiEsterni.Codifica) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
            
            public static it.gov.digitpa.www.protocollo.MetadatiEsterni.Codifica newInstance(org.apache.xmlbeans.XmlOptions options) {
              return (it.gov.digitpa.www.protocollo.MetadatiEsterni.Codifica) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
            
            private Factory() { } // No instance of this class allowed
        }
    }
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static it.gov.digitpa.www.protocollo.MetadatiEsterni newInstance() {
          return (it.gov.digitpa.www.protocollo.MetadatiEsterni) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static it.gov.digitpa.www.protocollo.MetadatiEsterni newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (it.gov.digitpa.www.protocollo.MetadatiEsterni) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static it.gov.digitpa.www.protocollo.MetadatiEsterni parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (it.gov.digitpa.www.protocollo.MetadatiEsterni) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static it.gov.digitpa.www.protocollo.MetadatiEsterni parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (it.gov.digitpa.www.protocollo.MetadatiEsterni) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static it.gov.digitpa.www.protocollo.MetadatiEsterni parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.MetadatiEsterni) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static it.gov.digitpa.www.protocollo.MetadatiEsterni parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.MetadatiEsterni) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static it.gov.digitpa.www.protocollo.MetadatiEsterni parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.MetadatiEsterni) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static it.gov.digitpa.www.protocollo.MetadatiEsterni parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.MetadatiEsterni) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static it.gov.digitpa.www.protocollo.MetadatiEsterni parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.MetadatiEsterni) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static it.gov.digitpa.www.protocollo.MetadatiEsterni parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.MetadatiEsterni) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static it.gov.digitpa.www.protocollo.MetadatiEsterni parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.MetadatiEsterni) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static it.gov.digitpa.www.protocollo.MetadatiEsterni parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.MetadatiEsterni) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static it.gov.digitpa.www.protocollo.MetadatiEsterni parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (it.gov.digitpa.www.protocollo.MetadatiEsterni) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static it.gov.digitpa.www.protocollo.MetadatiEsterni parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (it.gov.digitpa.www.protocollo.MetadatiEsterni) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static it.gov.digitpa.www.protocollo.MetadatiEsterni parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (it.gov.digitpa.www.protocollo.MetadatiEsterni) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static it.gov.digitpa.www.protocollo.MetadatiEsterni parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (it.gov.digitpa.www.protocollo.MetadatiEsterni) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static it.gov.digitpa.www.protocollo.MetadatiEsterni parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (it.gov.digitpa.www.protocollo.MetadatiEsterni) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static it.gov.digitpa.www.protocollo.MetadatiEsterni parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (it.gov.digitpa.www.protocollo.MetadatiEsterni) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
