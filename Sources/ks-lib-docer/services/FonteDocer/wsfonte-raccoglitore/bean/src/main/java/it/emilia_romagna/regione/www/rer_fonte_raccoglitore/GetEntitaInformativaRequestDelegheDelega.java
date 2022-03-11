/**
 * GetEntitaInformativaRequestDelegheDelega.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.emilia_romagna.regione.www.rer_fonte_raccoglitore;

public class GetEntitaInformativaRequestDelegheDelega  implements java.io.Serializable {
    private java.lang.String tipoRelazione;

    private java.lang.String tipoId;

    private java.lang.String id;

    public GetEntitaInformativaRequestDelegheDelega() {
    }

    public GetEntitaInformativaRequestDelegheDelega(
           java.lang.String tipoRelazione,
           java.lang.String tipoId,
           java.lang.String id) {
           this.tipoRelazione = tipoRelazione;
           this.tipoId = tipoId;
           this.id = id;
    }


    /**
     * Gets the tipoRelazione value for this GetEntitaInformativaRequestDelegheDelega.
     * 
     * @return tipoRelazione
     */
    public java.lang.String getTipoRelazione() {
        return tipoRelazione;
    }


    /**
     * Sets the tipoRelazione value for this GetEntitaInformativaRequestDelegheDelega.
     * 
     * @param tipoRelazione
     */
    public void setTipoRelazione(java.lang.String tipoRelazione) {
        this.tipoRelazione = tipoRelazione;
    }


    /**
     * Gets the tipoId value for this GetEntitaInformativaRequestDelegheDelega.
     * 
     * @return tipoId
     */
    public java.lang.String getTipoId() {
        return tipoId;
    }


    /**
     * Sets the tipoId value for this GetEntitaInformativaRequestDelegheDelega.
     * 
     * @param tipoId
     */
    public void setTipoId(java.lang.String tipoId) {
        this.tipoId = tipoId;
    }


    /**
     * Gets the id value for this GetEntitaInformativaRequestDelegheDelega.
     * 
     * @return id
     */
    public java.lang.String getId() {
        return id;
    }


    /**
     * Sets the id value for this GetEntitaInformativaRequestDelegheDelega.
     * 
     * @param id
     */
    public void setId(java.lang.String id) {
        this.id = id;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetEntitaInformativaRequestDelegheDelega)) return false;
        GetEntitaInformativaRequestDelegheDelega other = (GetEntitaInformativaRequestDelegheDelega) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.tipoRelazione==null && other.getTipoRelazione()==null) || 
             (this.tipoRelazione!=null &&
              this.tipoRelazione.equals(other.getTipoRelazione()))) &&
            ((this.tipoId==null && other.getTipoId()==null) || 
             (this.tipoId!=null &&
              this.tipoId.equals(other.getTipoId()))) &&
            ((this.id==null && other.getId()==null) || 
             (this.id!=null &&
              this.id.equals(other.getId())));
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
        if (getTipoRelazione() != null) {
            _hashCode += getTipoRelazione().hashCode();
        }
        if (getTipoId() != null) {
            _hashCode += getTipoId().hashCode();
        }
        if (getId() != null) {
            _hashCode += getId().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetEntitaInformativaRequestDelegheDelega.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "GetEntitaInformativaRequestDelegheDelega"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipoRelazione");
        elemField.setXmlName(new javax.xml.namespace.QName("", "tipoRelazione"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipoId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "tipoId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
