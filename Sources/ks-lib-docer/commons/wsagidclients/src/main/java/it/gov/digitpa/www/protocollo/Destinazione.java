/*
 * XML Type:  Destinazione
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.Destinazione
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo;


/**
 * An XML Destinazione(@http://www.digitPa.gov.it/protocollo/).
 *
 * This is a complex type.
 */
public interface Destinazione extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(Destinazione.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s0CE6FBAEFE3D9BB34FDB0E4A28280207").resolveHandle("destinazionedc8ftype");
    
    /**
     * Gets the "IndirizzoTelematico" element
     */
    it.gov.digitpa.www.protocollo.IndirizzoTelematico getIndirizzoTelematico();
    
    /**
     * Sets the "IndirizzoTelematico" element
     */
    void setIndirizzoTelematico(it.gov.digitpa.www.protocollo.IndirizzoTelematico indirizzoTelematico);
    
    /**
     * Appends and returns a new empty "IndirizzoTelematico" element
     */
    it.gov.digitpa.www.protocollo.IndirizzoTelematico addNewIndirizzoTelematico();
    
    /**
     * Gets array of all "Destinatario" elements
     */
    it.gov.digitpa.www.protocollo.Destinatario[] getDestinatarioArray();
    
    /**
     * Gets ith "Destinatario" element
     */
    it.gov.digitpa.www.protocollo.Destinatario getDestinatarioArray(int i);
    
    /**
     * Returns number of "Destinatario" element
     */
    int sizeOfDestinatarioArray();
    
    /**
     * Sets array of all "Destinatario" element
     */
    void setDestinatarioArray(it.gov.digitpa.www.protocollo.Destinatario[] destinatarioArray);
    
    /**
     * Sets ith "Destinatario" element
     */
    void setDestinatarioArray(int i, it.gov.digitpa.www.protocollo.Destinatario destinatario);
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "Destinatario" element
     */
    it.gov.digitpa.www.protocollo.Destinatario insertNewDestinatario(int i);
    
    /**
     * Appends and returns a new empty value (as xml) as the last "Destinatario" element
     */
    it.gov.digitpa.www.protocollo.Destinatario addNewDestinatario();
    
    /**
     * Removes the ith "Destinatario" element
     */
    void removeDestinatario(int i);
    
    /**
     * Gets the "confermaRicezione" attribute
     */
    it.gov.digitpa.www.protocollo.Destinazione.ConfermaRicezione.Enum getConfermaRicezione();
    
    /**
     * Gets (as xml) the "confermaRicezione" attribute
     */
    it.gov.digitpa.www.protocollo.Destinazione.ConfermaRicezione xgetConfermaRicezione();
    
    /**
     * True if has "confermaRicezione" attribute
     */
    boolean isSetConfermaRicezione();
    
    /**
     * Sets the "confermaRicezione" attribute
     */
    void setConfermaRicezione(it.gov.digitpa.www.protocollo.Destinazione.ConfermaRicezione.Enum confermaRicezione);
    
    /**
     * Sets (as xml) the "confermaRicezione" attribute
     */
    void xsetConfermaRicezione(it.gov.digitpa.www.protocollo.Destinazione.ConfermaRicezione confermaRicezione);
    
    /**
     * Unsets the "confermaRicezione" attribute
     */
    void unsetConfermaRicezione();
    
    /**
     * An XML confermaRicezione(@).
     *
     * This is an atomic type that is a restriction of it.gov.digitpa.www.protocollo.Destinazione$ConfermaRicezione.
     */
    public interface ConfermaRicezione extends org.apache.xmlbeans.XmlNMTOKEN
    {
        public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
            org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(ConfermaRicezione.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s0CE6FBAEFE3D9BB34FDB0E4A28280207").resolveHandle("confermaricezionef6ceattrtype");
        
        org.apache.xmlbeans.StringEnumAbstractBase enumValue();
        void set(org.apache.xmlbeans.StringEnumAbstractBase e);
        
        static final Enum SI = Enum.forString("si");
        static final Enum NO = Enum.forString("no");
        
        static final int INT_SI = Enum.INT_SI;
        static final int INT_NO = Enum.INT_NO;
        
        /**
         * Enumeration value class for it.gov.digitpa.www.protocollo.Destinazione$ConfermaRicezione.
         * These enum values can be used as follows:
         * <pre>
         * enum.toString(); // returns the string value of the enum
         * enum.intValue(); // returns an int value, useful for switches
         * // e.g., case Enum.INT_SI
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
            
            static final int INT_SI = 1;
            static final int INT_NO = 2;
            
            public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table =
                new org.apache.xmlbeans.StringEnumAbstractBase.Table
            (
                new Enum[]
                {
                    new Enum("si", INT_SI),
                    new Enum("no", INT_NO),
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
            public static it.gov.digitpa.www.protocollo.Destinazione.ConfermaRicezione newValue(java.lang.Object obj) {
              return (it.gov.digitpa.www.protocollo.Destinazione.ConfermaRicezione) type.newValue( obj ); }
            
            public static it.gov.digitpa.www.protocollo.Destinazione.ConfermaRicezione newInstance() {
              return (it.gov.digitpa.www.protocollo.Destinazione.ConfermaRicezione) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
            
            public static it.gov.digitpa.www.protocollo.Destinazione.ConfermaRicezione newInstance(org.apache.xmlbeans.XmlOptions options) {
              return (it.gov.digitpa.www.protocollo.Destinazione.ConfermaRicezione) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
            
            private Factory() { } // No instance of this class allowed
        }
    }
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static it.gov.digitpa.www.protocollo.Destinazione newInstance() {
          return (it.gov.digitpa.www.protocollo.Destinazione) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static it.gov.digitpa.www.protocollo.Destinazione newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (it.gov.digitpa.www.protocollo.Destinazione) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static it.gov.digitpa.www.protocollo.Destinazione parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (it.gov.digitpa.www.protocollo.Destinazione) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static it.gov.digitpa.www.protocollo.Destinazione parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (it.gov.digitpa.www.protocollo.Destinazione) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static it.gov.digitpa.www.protocollo.Destinazione parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.Destinazione) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static it.gov.digitpa.www.protocollo.Destinazione parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.Destinazione) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static it.gov.digitpa.www.protocollo.Destinazione parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.Destinazione) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static it.gov.digitpa.www.protocollo.Destinazione parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.Destinazione) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static it.gov.digitpa.www.protocollo.Destinazione parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.Destinazione) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static it.gov.digitpa.www.protocollo.Destinazione parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.Destinazione) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static it.gov.digitpa.www.protocollo.Destinazione parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.Destinazione) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static it.gov.digitpa.www.protocollo.Destinazione parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.Destinazione) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static it.gov.digitpa.www.protocollo.Destinazione parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (it.gov.digitpa.www.protocollo.Destinazione) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static it.gov.digitpa.www.protocollo.Destinazione parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (it.gov.digitpa.www.protocollo.Destinazione) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static it.gov.digitpa.www.protocollo.Destinazione parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (it.gov.digitpa.www.protocollo.Destinazione) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static it.gov.digitpa.www.protocollo.Destinazione parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (it.gov.digitpa.www.protocollo.Destinazione) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static it.gov.digitpa.www.protocollo.Destinazione parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (it.gov.digitpa.www.protocollo.Destinazione) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static it.gov.digitpa.www.protocollo.Destinazione parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (it.gov.digitpa.www.protocollo.Destinazione) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
