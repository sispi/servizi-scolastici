/**
 * Document.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.emilia_romagna.regione.www.rer_fonte_raccoglitore;

public class Document  implements java.io.Serializable {
    private java.lang.String id;

    private int version;

    private java.lang.String titolo;

    private java.lang.String tipoEntita;

    private java.util.Calendar dataAggiornamento;

    private java.lang.String comune;

    private it.emilia_romagna.regione.www.rer_fonte_raccoglitore.DocumentRelatedDocument[] relatedDocument;

    public Document() {
    }

    public Document(
           java.lang.String id,
           int version,
           java.lang.String titolo,
           java.lang.String tipoEntita,
           java.util.Calendar dataAggiornamento,
           java.lang.String comune,
           it.emilia_romagna.regione.www.rer_fonte_raccoglitore.DocumentRelatedDocument[] relatedDocument) {
           this.id = id;
           this.version = version;
           this.titolo = titolo;
           this.tipoEntita = tipoEntita;
           this.dataAggiornamento = dataAggiornamento;
           this.comune = comune;
           this.relatedDocument = relatedDocument;
    }


    /**
     * Gets the id value for this Document.
     * 
     * @return id
     */
    public java.lang.String getId() {
        return id;
    }


    /**
     * Sets the id value for this Document.
     * 
     * @param id
     */
    public void setId(java.lang.String id) {
        this.id = id;
    }


    /**
     * Gets the version value for this Document.
     * 
     * @return version
     */
    public int getVersion() {
        return version;
    }


    /**
     * Sets the version value for this Document.
     * 
     * @param version
     */
    public void setVersion(int version) {
        this.version = version;
    }


    /**
     * Gets the titolo value for this Document.
     * 
     * @return titolo
     */
    public java.lang.String getTitolo() {
        return titolo;
    }


    /**
     * Sets the titolo value for this Document.
     * 
     * @param titolo
     */
    public void setTitolo(java.lang.String titolo) {
        this.titolo = titolo;
    }


    /**
     * Gets the tipoEntita value for this Document.
     * 
     * @return tipoEntita
     */
    public java.lang.String getTipoEntita() {
        return tipoEntita;
    }


    /**
     * Sets the tipoEntita value for this Document.
     * 
     * @param tipoEntita
     */
    public void setTipoEntita(java.lang.String tipoEntita) {
        this.tipoEntita = tipoEntita;
    }


    /**
     * Gets the dataAggiornamento value for this Document.
     * 
     * @return dataAggiornamento
     */
    public java.util.Calendar getDataAggiornamento() {
        return dataAggiornamento;
    }


    /**
     * Sets the dataAggiornamento value for this Document.
     * 
     * @param dataAggiornamento
     */
    public void setDataAggiornamento(java.util.Calendar dataAggiornamento) {
        this.dataAggiornamento = dataAggiornamento;
    }


    /**
     * Gets the comune value for this Document.
     * 
     * @return comune
     */
    public java.lang.String getComune() {
        return comune;
    }


    /**
     * Sets the comune value for this Document.
     * 
     * @param comune
     */
    public void setComune(java.lang.String comune) {
        this.comune = comune;
    }


    /**
     * Gets the relatedDocument value for this Document.
     * 
     * @return relatedDocument
     */
    public it.emilia_romagna.regione.www.rer_fonte_raccoglitore.DocumentRelatedDocument[] getRelatedDocument() {
        return relatedDocument;
    }


    /**
     * Sets the relatedDocument value for this Document.
     * 
     * @param relatedDocument
     */
    public void setRelatedDocument(it.emilia_romagna.regione.www.rer_fonte_raccoglitore.DocumentRelatedDocument[] relatedDocument) {
        this.relatedDocument = relatedDocument;
    }

    public it.emilia_romagna.regione.www.rer_fonte_raccoglitore.DocumentRelatedDocument getRelatedDocument(int i) {
        return this.relatedDocument[i];
    }

    public void setRelatedDocument(int i, it.emilia_romagna.regione.www.rer_fonte_raccoglitore.DocumentRelatedDocument _value) {
        this.relatedDocument[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Document)) return false;
        Document other = (Document) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.id==null && other.getId()==null) || 
             (this.id!=null &&
              this.id.equals(other.getId()))) &&
            this.version == other.getVersion() &&
            ((this.titolo==null && other.getTitolo()==null) || 
             (this.titolo!=null &&
              this.titolo.equals(other.getTitolo()))) &&
            ((this.tipoEntita==null && other.getTipoEntita()==null) || 
             (this.tipoEntita!=null &&
              this.tipoEntita.equals(other.getTipoEntita()))) &&
            ((this.dataAggiornamento==null && other.getDataAggiornamento()==null) || 
             (this.dataAggiornamento!=null &&
              this.dataAggiornamento.equals(other.getDataAggiornamento()))) &&
            ((this.comune==null && other.getComune()==null) || 
             (this.comune!=null &&
              this.comune.equals(other.getComune()))) &&
            ((this.relatedDocument==null && other.getRelatedDocument()==null) || 
             (this.relatedDocument!=null &&
              java.util.Arrays.equals(this.relatedDocument, other.getRelatedDocument())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getId() != null) {
            _hashCode += getId().hashCode();
        }
        _hashCode += getVersion();
        if (getTitolo() != null) {
            _hashCode += getTitolo().hashCode();
        }
        if (getTipoEntita() != null) {
            _hashCode += getTipoEntita().hashCode();
        }
        if (getDataAggiornamento() != null) {
            _hashCode += getDataAggiornamento().hashCode();
        }
        if (getComune() != null) {
            _hashCode += getComune().hashCode();
        }
        if (getRelatedDocument() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getRelatedDocument());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getRelatedDocument(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Document.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "Document"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("version");
        elemField.setXmlName(new javax.xml.namespace.QName("", "version"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("titolo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "titolo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipoEntita");
        elemField.setXmlName(new javax.xml.namespace.QName("", "tipoEntita"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataAggiornamento");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dataAggiornamento"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("comune");
        elemField.setXmlName(new javax.xml.namespace.QName("", "comune"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("relatedDocument");
        elemField.setXmlName(new javax.xml.namespace.QName("", "relatedDocument"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", ">Document>relatedDocument"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
