/*
 * XML Type:  Intestazione
 * Namespace: http://www.digitPa.gov.it/protocollo/
 * Java type: it.gov.digitpa.www.protocollo.Intestazione
 *
 * Automatically generated - do not modify.
 */
package it.gov.digitpa.www.protocollo;


/**
 * An XML Intestazione(@http://www.digitPa.gov.it/protocollo/).
 *
 * This is a complex type.
 */
public interface Intestazione extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(Intestazione.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s88C7D26292FFA5D2830FCC663DD80458").resolveHandle("intestazione007ftype");
    
    /**
     * Gets the "Identificatore" element
     */
    it.gov.digitpa.www.protocollo.Identificatore getIdentificatore();
    
    /**
     * Sets the "Identificatore" element
     */
    void setIdentificatore(it.gov.digitpa.www.protocollo.Identificatore identificatore);
    
    /**
     * Appends and returns a new empty "Identificatore" element
     */
    it.gov.digitpa.www.protocollo.Identificatore addNewIdentificatore();
    
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
     * Gets the "OraRegistrazione" element
     */
    it.gov.digitpa.www.protocollo.OraRegistrazione getOraRegistrazione();
    
    /**
     * True if has "OraRegistrazione" element
     */
    boolean isSetOraRegistrazione();
    
    /**
     * Sets the "OraRegistrazione" element
     */
    void setOraRegistrazione(it.gov.digitpa.www.protocollo.OraRegistrazione oraRegistrazione);
    
    /**
     * Appends and returns a new empty "OraRegistrazione" element
     */
    it.gov.digitpa.www.protocollo.OraRegistrazione addNewOraRegistrazione();
    
    /**
     * Unsets the "OraRegistrazione" element
     */
    void unsetOraRegistrazione();
    
    /**
     * Gets the "Origine" element
     */
    it.gov.digitpa.www.protocollo.Origine getOrigine();
    
    /**
     * Sets the "Origine" element
     */
    void setOrigine(it.gov.digitpa.www.protocollo.Origine origine);
    
    /**
     * Appends and returns a new empty "Origine" element
     */
    it.gov.digitpa.www.protocollo.Origine addNewOrigine();
    
    /**
     * Gets array of all "Destinazione" elements
     */
    it.gov.digitpa.www.protocollo.Destinazione[] getDestinazioneArray();
    
    /**
     * Gets ith "Destinazione" element
     */
    it.gov.digitpa.www.protocollo.Destinazione getDestinazioneArray(int i);
    
    /**
     * Returns number of "Destinazione" element
     */
    int sizeOfDestinazioneArray();
    
    /**
     * Sets array of all "Destinazione" element
     */
    void setDestinazioneArray(it.gov.digitpa.www.protocollo.Destinazione[] destinazioneArray);
    
    /**
     * Sets ith "Destinazione" element
     */
    void setDestinazioneArray(int i, it.gov.digitpa.www.protocollo.Destinazione destinazione);
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "Destinazione" element
     */
    it.gov.digitpa.www.protocollo.Destinazione insertNewDestinazione(int i);
    
    /**
     * Appends and returns a new empty value (as xml) as the last "Destinazione" element
     */
    it.gov.digitpa.www.protocollo.Destinazione addNewDestinazione();
    
    /**
     * Removes the ith "Destinazione" element
     */
    void removeDestinazione(int i);
    
    /**
     * Gets array of all "PerConoscenza" elements
     */
    it.gov.digitpa.www.protocollo.PerConoscenza[] getPerConoscenzaArray();
    
    /**
     * Gets ith "PerConoscenza" element
     */
    it.gov.digitpa.www.protocollo.PerConoscenza getPerConoscenzaArray(int i);
    
    /**
     * Returns number of "PerConoscenza" element
     */
    int sizeOfPerConoscenzaArray();
    
    /**
     * Sets array of all "PerConoscenza" element
     */
    void setPerConoscenzaArray(it.gov.digitpa.www.protocollo.PerConoscenza[] perConoscenzaArray);
    
    /**
     * Sets ith "PerConoscenza" element
     */
    void setPerConoscenzaArray(int i, it.gov.digitpa.www.protocollo.PerConoscenza perConoscenza);
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "PerConoscenza" element
     */
    it.gov.digitpa.www.protocollo.PerConoscenza insertNewPerConoscenza(int i);
    
    /**
     * Appends and returns a new empty value (as xml) as the last "PerConoscenza" element
     */
    it.gov.digitpa.www.protocollo.PerConoscenza addNewPerConoscenza();
    
    /**
     * Removes the ith "PerConoscenza" element
     */
    void removePerConoscenza(int i);
    
    /**
     * Gets the "Risposta" element
     */
    it.gov.digitpa.www.protocollo.Risposta getRisposta();
    
    /**
     * True if has "Risposta" element
     */
    boolean isSetRisposta();
    
    /**
     * Sets the "Risposta" element
     */
    void setRisposta(it.gov.digitpa.www.protocollo.Risposta risposta);
    
    /**
     * Appends and returns a new empty "Risposta" element
     */
    it.gov.digitpa.www.protocollo.Risposta addNewRisposta();
    
    /**
     * Unsets the "Risposta" element
     */
    void unsetRisposta();
    
    /**
     * Gets the "Riservato" element
     */
    it.gov.digitpa.www.protocollo.Riservato getRiservato();
    
    /**
     * True if has "Riservato" element
     */
    boolean isSetRiservato();
    
    /**
     * Sets the "Riservato" element
     */
    void setRiservato(it.gov.digitpa.www.protocollo.Riservato riservato);
    
    /**
     * Appends and returns a new empty "Riservato" element
     */
    it.gov.digitpa.www.protocollo.Riservato addNewRiservato();
    
    /**
     * Unsets the "Riservato" element
     */
    void unsetRiservato();
    
    /**
     * Gets the "InterventoOperatore" element
     */
    it.gov.digitpa.www.protocollo.InterventoOperatore getInterventoOperatore();
    
    /**
     * True if has "InterventoOperatore" element
     */
    boolean isSetInterventoOperatore();
    
    /**
     * Sets the "InterventoOperatore" element
     */
    void setInterventoOperatore(it.gov.digitpa.www.protocollo.InterventoOperatore interventoOperatore);
    
    /**
     * Appends and returns a new empty "InterventoOperatore" element
     */
    it.gov.digitpa.www.protocollo.InterventoOperatore addNewInterventoOperatore();
    
    /**
     * Unsets the "InterventoOperatore" element
     */
    void unsetInterventoOperatore();
    
    /**
     * Gets the "RiferimentoDocumentiCartacei" element
     */
    it.gov.digitpa.www.protocollo.RiferimentoDocumentiCartacei getRiferimentoDocumentiCartacei();
    
    /**
     * True if has "RiferimentoDocumentiCartacei" element
     */
    boolean isSetRiferimentoDocumentiCartacei();
    
    /**
     * Sets the "RiferimentoDocumentiCartacei" element
     */
    void setRiferimentoDocumentiCartacei(it.gov.digitpa.www.protocollo.RiferimentoDocumentiCartacei riferimentoDocumentiCartacei);
    
    /**
     * Appends and returns a new empty "RiferimentoDocumentiCartacei" element
     */
    it.gov.digitpa.www.protocollo.RiferimentoDocumentiCartacei addNewRiferimentoDocumentiCartacei();
    
    /**
     * Unsets the "RiferimentoDocumentiCartacei" element
     */
    void unsetRiferimentoDocumentiCartacei();
    
    /**
     * Gets the "RiferimentiTelematici" element
     */
    it.gov.digitpa.www.protocollo.RiferimentiTelematici getRiferimentiTelematici();
    
    /**
     * True if has "RiferimentiTelematici" element
     */
    boolean isSetRiferimentiTelematici();
    
    /**
     * Sets the "RiferimentiTelematici" element
     */
    void setRiferimentiTelematici(it.gov.digitpa.www.protocollo.RiferimentiTelematici riferimentiTelematici);
    
    /**
     * Appends and returns a new empty "RiferimentiTelematici" element
     */
    it.gov.digitpa.www.protocollo.RiferimentiTelematici addNewRiferimentiTelematici();
    
    /**
     * Unsets the "RiferimentiTelematici" element
     */
    void unsetRiferimentiTelematici();
    
    /**
     * Gets the "Oggetto" element
     */
    it.gov.digitpa.www.protocollo.Oggetto getOggetto();
    
    /**
     * Sets the "Oggetto" element
     */
    void setOggetto(it.gov.digitpa.www.protocollo.Oggetto oggetto);
    
    /**
     * Appends and returns a new empty "Oggetto" element
     */
    it.gov.digitpa.www.protocollo.Oggetto addNewOggetto();
    
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
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static it.gov.digitpa.www.protocollo.Intestazione newInstance() {
          return (it.gov.digitpa.www.protocollo.Intestazione) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static it.gov.digitpa.www.protocollo.Intestazione newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (it.gov.digitpa.www.protocollo.Intestazione) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static it.gov.digitpa.www.protocollo.Intestazione parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (it.gov.digitpa.www.protocollo.Intestazione) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static it.gov.digitpa.www.protocollo.Intestazione parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (it.gov.digitpa.www.protocollo.Intestazione) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static it.gov.digitpa.www.protocollo.Intestazione parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.Intestazione) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static it.gov.digitpa.www.protocollo.Intestazione parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.Intestazione) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static it.gov.digitpa.www.protocollo.Intestazione parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.Intestazione) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static it.gov.digitpa.www.protocollo.Intestazione parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.Intestazione) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static it.gov.digitpa.www.protocollo.Intestazione parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.Intestazione) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static it.gov.digitpa.www.protocollo.Intestazione parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.Intestazione) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static it.gov.digitpa.www.protocollo.Intestazione parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.Intestazione) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static it.gov.digitpa.www.protocollo.Intestazione parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (it.gov.digitpa.www.protocollo.Intestazione) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static it.gov.digitpa.www.protocollo.Intestazione parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (it.gov.digitpa.www.protocollo.Intestazione) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static it.gov.digitpa.www.protocollo.Intestazione parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (it.gov.digitpa.www.protocollo.Intestazione) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static it.gov.digitpa.www.protocollo.Intestazione parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (it.gov.digitpa.www.protocollo.Intestazione) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static it.gov.digitpa.www.protocollo.Intestazione parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (it.gov.digitpa.www.protocollo.Intestazione) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static it.gov.digitpa.www.protocollo.Intestazione parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (it.gov.digitpa.www.protocollo.Intestazione) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static it.gov.digitpa.www.protocollo.Intestazione parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (it.gov.digitpa.www.protocollo.Intestazione) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
