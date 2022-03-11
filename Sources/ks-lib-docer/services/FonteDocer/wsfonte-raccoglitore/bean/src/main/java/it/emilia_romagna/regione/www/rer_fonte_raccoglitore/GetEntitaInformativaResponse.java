/**
 * GetEntitaInformativaResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.emilia_romagna.regione.www.rer_fonte_raccoglitore;

public class GetEntitaInformativaResponse  implements java.io.Serializable {
    private java.lang.String title;

    private java.lang.String mimteType;

    private byte[] entitaInformativa;

    public GetEntitaInformativaResponse() {
    }

    public GetEntitaInformativaResponse(
           java.lang.String title,
           java.lang.String mimteType,
           byte[] entitaInformativa) {
           this.title = title;
           this.mimteType = mimteType;
           this.entitaInformativa = entitaInformativa;
    }


    /**
     * Gets the title value for this GetEntitaInformativaResponse.
     * 
     * @return title
     */
    public java.lang.String getTitle() {
        return title;
    }


    /**
     * Sets the title value for this GetEntitaInformativaResponse.
     * 
     * @param title
     */
    public void setTitle(java.lang.String title) {
        this.title = title;
    }


    /**
     * Gets the mimteType value for this GetEntitaInformativaResponse.
     * 
     * @return mimteType
     */
    public java.lang.String getMimteType() {
        return mimteType;
    }


    /**
     * Sets the mimteType value for this GetEntitaInformativaResponse.
     * 
     * @param mimteType
     */
    public void setMimteType(java.lang.String mimteType) {
        this.mimteType = mimteType;
    }


    /**
     * Gets the entitaInformativa value for this GetEntitaInformativaResponse.
     * 
     * @return entitaInformativa
     */
    public byte[] getEntitaInformativa() {
        return entitaInformativa;
    }


    /**
     * Sets the entitaInformativa value for this GetEntitaInformativaResponse.
     * 
     * @param entitaInformativa
     */
    public void setEntitaInformativa(byte[] entitaInformativa) {
        this.entitaInformativa = entitaInformativa;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetEntitaInformativaResponse)) return false;
        GetEntitaInformativaResponse other = (GetEntitaInformativaResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.title==null && other.getTitle()==null) || 
             (this.title!=null &&
              this.title.equals(other.getTitle()))) &&
            ((this.mimteType==null && other.getMimteType()==null) || 
             (this.mimteType!=null &&
              this.mimteType.equals(other.getMimteType()))) &&
            ((this.entitaInformativa==null && other.getEntitaInformativa()==null) || 
             (this.entitaInformativa!=null &&
              java.util.Arrays.equals(this.entitaInformativa, other.getEntitaInformativa())));
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
        if (getTitle() != null) {
            _hashCode += getTitle().hashCode();
        }
        if (getMimteType() != null) {
            _hashCode += getMimteType().hashCode();
        }
        if (getEntitaInformativa() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getEntitaInformativa());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getEntitaInformativa(), i);
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
        new org.apache.axis.description.TypeDesc(GetEntitaInformativaResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", ">getEntitaInformativaResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("title");
        elemField.setXmlName(new javax.xml.namespace.QName("", "title"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("mimteType");
        elemField.setXmlName(new javax.xml.namespace.QName("", "mimteType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("entitaInformativa");
        elemField.setXmlName(new javax.xml.namespace.QName("", "entitaInformativa"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"));
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
