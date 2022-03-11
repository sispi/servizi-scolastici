/*
 * XML Type:  Documento
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.Documento
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo;


/**
 * An XML Documento(@http://www.digitPa.gov.it/protocollo/).
 *
 * This is a complex type.
 */
public interface Documento extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(Documento.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s0CE6FBAEFE3D9BB34FDB0E4A28280207").resolveHandle("documento52f4type");
    
    /**
     * Gets the "CollocazioneTelematica" element
     */
    it.gov.digitpa.www.protocollo.CollocazioneTelematica getCollocazioneTelematica();
    
    /**
     * True if has "CollocazioneTelematica" element
     */
    boolean isSetCollocazioneTelematica();
    
    /**
     * Sets the "CollocazioneTelematica" element
     */
    void setCollocazioneTelematica(it.gov.digitpa.www.protocollo.CollocazioneTelematica collocazioneTelematica);
    
    /**
     * Appends and returns a new empty "CollocazioneTelematica" element
     */
    it.gov.digitpa.www.protocollo.CollocazioneTelematica addNewCollocazioneTelematica();
    
    /**
     * Unsets the "CollocazioneTelematica" element
     */
    void unsetCollocazioneTelematica();
    
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
     * Gets the "TitoloDocumento" element
     */
    it.gov.digitpa.www.protocollo.TitoloDocumento getTitoloDocumento();
    
    /**
     * True if has "TitoloDocumento" element
     */
    boolean isSetTitoloDocumento();
    
    /**
     * Sets the "TitoloDocumento" element
     */
    void setTitoloDocumento(it.gov.digitpa.www.protocollo.TitoloDocumento titoloDocumento);
    
    /**
     * Appends and returns a new empty "TitoloDocumento" element
     */
    it.gov.digitpa.www.protocollo.TitoloDocumento addNewTitoloDocumento();
    
    /**
     * Unsets the "TitoloDocumento" element
     */
    void unsetTitoloDocumento();
    
    /**
     * Gets the "PrimaRegistrazione" element
     */
    it.gov.digitpa.www.protocollo.PrimaRegistrazione getPrimaRegistrazione();
    
    /**
     * True if has "PrimaRegistrazione" element
     */
    boolean isSetPrimaRegistrazione();
    
    /**
     * Sets the "PrimaRegistrazione" element
     */
    void setPrimaRegistrazione(it.gov.digitpa.www.protocollo.PrimaRegistrazione primaRegistrazione);
    
    /**
     * Appends and returns a new empty "PrimaRegistrazione" element
     */
    it.gov.digitpa.www.protocollo.PrimaRegistrazione addNewPrimaRegistrazione();
    
    /**
     * Unsets the "PrimaRegistrazione" element
     */
    void unsetPrimaRegistrazione();
    
    /**
     * Gets the "TipoDocumento" element
     */
    it.gov.digitpa.www.protocollo.TipoDocumento getTipoDocumento();
    
    /**
     * True if has "TipoDocumento" element
     */
    boolean isSetTipoDocumento();
    
    /**
     * Sets the "TipoDocumento" element
     */
    void setTipoDocumento(it.gov.digitpa.www.protocollo.TipoDocumento tipoDocumento);
    
    /**
     * Appends and returns a new empty "TipoDocumento" element
     */
    it.gov.digitpa.www.protocollo.TipoDocumento addNewTipoDocumento();
    
    /**
     * Unsets the "TipoDocumento" element
     */
    void unsetTipoDocumento();
    
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
     * Gets the "NumeroPagine" element
     */
    it.gov.digitpa.www.protocollo.NumeroPagine getNumeroPagine();
    
    /**
     * True if has "NumeroPagine" element
     */
    boolean isSetNumeroPagine();
    
    /**
     * Sets the "NumeroPagine" element
     */
    void setNumeroPagine(it.gov.digitpa.www.protocollo.NumeroPagine numeroPagine);
    
    /**
     * Appends and returns a new empty "NumeroPagine" element
     */
    it.gov.digitpa.www.protocollo.NumeroPagine addNewNumeroPagine();
    
    /**
     * Unsets the "NumeroPagine" element
     */
    void unsetNumeroPagine();
    
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
     * Gets the "nome" attribute
     */
    org.apache.xmlbeans.XmlAnySimpleType getNome();
    
    /**
     * True if has "nome" attribute
     */
    boolean isSetNome();
    
    /**
     * Sets the "nome" attribute
     */
    void setNome(org.apache.xmlbeans.XmlAnySimpleType nome);
    
    /**
     * Appends and returns a new empty "nome" attribute
     */
    org.apache.xmlbeans.XmlAnySimpleType addNewNome();
    
    /**
     * Unsets the "nome" attribute
     */
    void unsetNome();
    
    /**
     * Gets the "tipoMIME" attribute
     */
    org.apache.xmlbeans.XmlAnySimpleType getTipoMIME();
    
    /**
     * True if has "tipoMIME" attribute
     */
    boolean isSetTipoMIME();
    
    /**
     * Sets the "tipoMIME" attribute
     */
    void setTipoMIME(org.apache.xmlbeans.XmlAnySimpleType tipoMIME);
    
    /**
     * Appends and returns a new empty "tipoMIME" attribute
     */
    org.apache.xmlbeans.XmlAnySimpleType addNewTipoMIME();
    
    /**
     * Unsets the "tipoMIME" attribute
     */
    void unsetTipoMIME();
    
    /**
     * Gets the "tipoRiferimento" attribute
     */
    it.gov.digitpa.www.protocollo.Documento.TipoRiferimento.Enum getTipoRiferimento();
    
    /**
     * Gets (as xml) the "tipoRiferimento" attribute
     */
    it.gov.digitpa.www.protocollo.Documento.TipoRiferimento xgetTipoRiferimento();
    
    /**
     * True if has "tipoRiferimento" attribute
     */
    boolean isSetTipoRiferimento();
    
    /**
     * Sets the "tipoRiferimento" attribute
     */
    void setTipoRiferimento(it.gov.digitpa.www.protocollo.Documento.TipoRiferimento.Enum tipoRiferimento);
    
    /**
     * Sets (as xml) the "tipoRiferimento" attribute
     */
    void xsetTipoRiferimento(it.gov.digitpa.www.protocollo.Documento.TipoRiferimento tipoRiferimento);
    
    /**
     * Unsets the "tipoRiferimento" attribute
     */
    void unsetTipoRiferimento();
    
    /**
     * An XML tipoRiferimento(@).
     *
     * This is an atomic type that is a restriction of it.gov.digitpa.www.protocollo.Documento$TipoRiferimento.
     */
    public interface TipoRiferimento extends org.apache.xmlbeans.XmlNMTOKEN
    {
        public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
            org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(TipoRiferimento.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s0CE6FBAEFE3D9BB34FDB0E4A28280207").resolveHandle("tiporiferimentoc68cattrtype");
        
        org.apache.xmlbeans.StringEnumAbstractBase enumValue();
        void set(org.apache.xmlbeans.StringEnumAbstractBase e);
        
        static final Enum CARTACEO = Enum.forString("cartaceo");
        static final Enum TELEMATICO = Enum.forString("telematico");
        static final Enum MIME = Enum.forString("MIME");
        
        static final int INT_CARTACEO = Enum.INT_CARTACEO;
        static final int INT_TELEMATICO = Enum.INT_TELEMATICO;
        static final int INT_MIME = Enum.INT_MIME;
        
        /**
         * Enumeration value class for it.gov.digitpa.www.protocollo.Documento$TipoRiferimento.
         * These enum values can be used as follows:
         * <pre>
         * enum.toString(); // returns the string value of the enum
         * enum.intValue(); // returns an int value, useful for switches
         * // e.g., case Enum.INT_CARTACEO
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
            
            static final int INT_CARTACEO = 1;
            static final int INT_TELEMATICO = 2;
            static final int INT_MIME = 3;
            
            public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table =
                new org.apache.xmlbeans.StringEnumAbstractBase.Table
            (
                new Enum[]
                {
                    new Enum("cartaceo", INT_CARTACEO),
                    new Enum("telematico", INT_TELEMATICO),
                    new Enum("MIME", INT_MIME),
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
            public static it.gov.digitpa.www.protocollo.Documento.TipoRiferimento newValue(java.lang.Object obj) {
              return (it.gov.digitpa.www.protocollo.Documento.TipoRiferimento) type.newValue( obj ); }
            
            public static it.gov.digitpa.www.protocollo.Documento.TipoRiferimento newInstance() {
              return (it.gov.digitpa.www.protocollo.Documento.TipoRiferimento) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
            
            public static it.gov.digitpa.www.protocollo.Documento.TipoRiferimento newInstance(org.apache.xmlbeans.XmlOptions options) {
              return (it.gov.digitpa.www.protocollo.Documento.TipoRiferimento) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
            
            private Factory() { } // No instance of this class allowed
        }
    }
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static it.gov.digitpa.www.protocollo.Documento newInstance() {
          return (it.gov.digitpa.www.protocollo.Documento) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static it.gov.digitpa.www.protocollo.Documento newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (it.gov.digitpa.www.protocollo.Documento) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static it.gov.digitpa.www.protocollo.Documento parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (it.gov.digitpa.www.protocollo.Documento) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static it.gov.digitpa.www.protocollo.Documento parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (it.gov.digitpa.www.protocollo.Documento) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static it.gov.digitpa.www.protocollo.Documento parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.Documento) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static it.gov.digitpa.www.protocollo.Documento parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.Documento) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static it.gov.digitpa.www.protocollo.Documento parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.Documento) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static it.gov.digitpa.www.protocollo.Documento parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.Documento) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static it.gov.digitpa.www.protocollo.Documento parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.Documento) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static it.gov.digitpa.www.protocollo.Documento parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.Documento) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static it.gov.digitpa.www.protocollo.Documento parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.Documento) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static it.gov.digitpa.www.protocollo.Documento parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.Documento) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static it.gov.digitpa.www.protocollo.Documento parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (it.gov.digitpa.www.protocollo.Documento) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static it.gov.digitpa.www.protocollo.Documento parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (it.gov.digitpa.www.protocollo.Documento) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static it.gov.digitpa.www.protocollo.Documento parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (it.gov.digitpa.www.protocollo.Documento) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static it.gov.digitpa.www.protocollo.Documento parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (it.gov.digitpa.www.protocollo.Documento) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static it.gov.digitpa.www.protocollo.Documento parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (it.gov.digitpa.www.protocollo.Documento) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static it.gov.digitpa.www.protocollo.Documento parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (it.gov.digitpa.www.protocollo.Documento) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
