/**
 * AttributoType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.emilia_romagna.regione.www.rer_fonte_raccoglitore;

public class AttributoType  implements java.io.Serializable {
    private java.lang.String codice;

    private java.lang.String descrizione;

    private java.lang.String descrizioneLunga;

    private it.emilia_romagna.regione.www.rer_fonte_raccoglitore.TipoAttributoType tipoAttributo;

    private java.lang.String infoAttributo;

    private java.lang.Boolean obbligatorio;

    private java.lang.String visibilita;

    public AttributoType() {
    }

    public AttributoType(
           java.lang.String codice,
           java.lang.String descrizione,
           java.lang.String descrizioneLunga,
           it.emilia_romagna.regione.www.rer_fonte_raccoglitore.TipoAttributoType tipoAttributo,
           java.lang.String infoAttributo,
           java.lang.Boolean obbligatorio,
           java.lang.String visibilita) {
           this.codice = codice;
           this.descrizione = descrizione;
           this.descrizioneLunga = descrizioneLunga;
           this.tipoAttributo = tipoAttributo;
           this.infoAttributo = infoAttributo;
           this.obbligatorio = obbligatorio;
           this.visibilita = visibilita;
    }


    /**
     * Gets the codice value for this AttributoType.
     * 
     * @return codice
     */
    public java.lang.String getCodice() {
        return codice;
    }


    /**
     * Sets the codice value for this AttributoType.
     * 
     * @param codice
     */
    public void setCodice(java.lang.String codice) {
        this.codice = codice;
    }


    /**
     * Gets the descrizione value for this AttributoType.
     * 
     * @return descrizione
     */
    public java.lang.String getDescrizione() {
        return descrizione;
    }


    /**
     * Sets the descrizione value for this AttributoType.
     * 
     * @param descrizione
     */
    public void setDescrizione(java.lang.String descrizione) {
        this.descrizione = descrizione;
    }


    /**
     * Gets the descrizioneLunga value for this AttributoType.
     * 
     * @return descrizioneLunga
     */
    public java.lang.String getDescrizioneLunga() {
        return descrizioneLunga;
    }


    /**
     * Sets the descrizioneLunga value for this AttributoType.
     * 
     * @param descrizioneLunga
     */
    public void setDescrizioneLunga(java.lang.String descrizioneLunga) {
        this.descrizioneLunga = descrizioneLunga;
    }


    /**
     * Gets the tipoAttributo value for this AttributoType.
     * 
     * @return tipoAttributo
     */
    public it.emilia_romagna.regione.www.rer_fonte_raccoglitore.TipoAttributoType getTipoAttributo() {
        return tipoAttributo;
    }


    /**
     * Sets the tipoAttributo value for this AttributoType.
     * 
     * @param tipoAttributo
     */
    public void setTipoAttributo(it.emilia_romagna.regione.www.rer_fonte_raccoglitore.TipoAttributoType tipoAttributo) {
        this.tipoAttributo = tipoAttributo;
    }


    /**
     * Gets the infoAttributo value for this AttributoType.
     * 
     * @return infoAttributo
     */
    public java.lang.String getInfoAttributo() {
        return infoAttributo;
    }


    /**
     * Sets the infoAttributo value for this AttributoType.
     * 
     * @param infoAttributo
     */
    public void setInfoAttributo(java.lang.String infoAttributo) {
        this.infoAttributo = infoAttributo;
    }


    /**
     * Gets the obbligatorio value for this AttributoType.
     * 
     * @return obbligatorio
     */
    public java.lang.Boolean getObbligatorio() {
        return obbligatorio;
    }


    /**
     * Sets the obbligatorio value for this AttributoType.
     * 
     * @param obbligatorio
     */
    public void setObbligatorio(java.lang.Boolean obbligatorio) {
        this.obbligatorio = obbligatorio;
    }


    /**
     * Gets the visibilita value for this AttributoType.
     * 
     * @return visibilita
     */
    public java.lang.String getVisibilita() {
        return visibilita;
    }


    /**
     * Sets the visibilita value for this AttributoType.
     * 
     * @param visibilita
     */
    public void setVisibilita(java.lang.String visibilita) {
        this.visibilita = visibilita;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof AttributoType)) return false;
        AttributoType other = (AttributoType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.codice==null && other.getCodice()==null) || 
             (this.codice!=null &&
              this.codice.equals(other.getCodice()))) &&
            ((this.descrizione==null && other.getDescrizione()==null) || 
             (this.descrizione!=null &&
              this.descrizione.equals(other.getDescrizione()))) &&
            ((this.descrizioneLunga==null && other.getDescrizioneLunga()==null) || 
             (this.descrizioneLunga!=null &&
              this.descrizioneLunga.equals(other.getDescrizioneLunga()))) &&
            ((this.tipoAttributo==null && other.getTipoAttributo()==null) || 
             (this.tipoAttributo!=null &&
              this.tipoAttributo.equals(other.getTipoAttributo()))) &&
            ((this.infoAttributo==null && other.getInfoAttributo()==null) || 
             (this.infoAttributo!=null &&
              this.infoAttributo.equals(other.getInfoAttributo()))) &&
            ((this.obbligatorio==null && other.getObbligatorio()==null) || 
             (this.obbligatorio!=null &&
              this.obbligatorio.equals(other.getObbligatorio()))) &&
            ((this.visibilita==null && other.getVisibilita()==null) || 
             (this.visibilita!=null &&
              this.visibilita.equals(other.getVisibilita())));
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
        if (getCodice() != null) {
            _hashCode += getCodice().hashCode();
        }
        if (getDescrizione() != null) {
            _hashCode += getDescrizione().hashCode();
        }
        if (getDescrizioneLunga() != null) {
            _hashCode += getDescrizioneLunga().hashCode();
        }
        if (getTipoAttributo() != null) {
            _hashCode += getTipoAttributo().hashCode();
        }
        if (getInfoAttributo() != null) {
            _hashCode += getInfoAttributo().hashCode();
        }
        if (getObbligatorio() != null) {
            _hashCode += getObbligatorio().hashCode();
        }
        if (getVisibilita() != null) {
            _hashCode += getVisibilita().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(AttributoType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "AttributoType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codice");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codice"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("descrizione");
        elemField.setXmlName(new javax.xml.namespace.QName("", "descrizione"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("descrizioneLunga");
        elemField.setXmlName(new javax.xml.namespace.QName("", "descrizioneLunga"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipoAttributo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "tipoAttributo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "TipoAttributoType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("infoAttributo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "infoAttributo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("obbligatorio");
        elemField.setXmlName(new javax.xml.namespace.QName("", "obbligatorio"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("visibilita");
        elemField.setXmlName(new javax.xml.namespace.QName("", "visibilita"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
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
