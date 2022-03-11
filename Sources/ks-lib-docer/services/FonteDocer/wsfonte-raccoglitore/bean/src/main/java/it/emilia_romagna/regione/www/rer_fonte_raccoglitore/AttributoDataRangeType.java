/**
 * AttributoDataRangeType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.emilia_romagna.regione.www.rer_fonte_raccoglitore;

public class AttributoDataRangeType  implements java.io.Serializable {
    private java.lang.String codice;

    private it.emilia_romagna.regione.www.rer_fonte_raccoglitore.DateRangeType valore;

    public AttributoDataRangeType() {
    }

    public AttributoDataRangeType(
           java.lang.String codice,
           it.emilia_romagna.regione.www.rer_fonte_raccoglitore.DateRangeType valore) {
           this.codice = codice;
           this.valore = valore;
    }


    /**
     * Gets the codice value for this AttributoDataRangeType.
     * 
     * @return codice
     */
    public java.lang.String getCodice() {
        return codice;
    }


    /**
     * Sets the codice value for this AttributoDataRangeType.
     * 
     * @param codice
     */
    public void setCodice(java.lang.String codice) {
        this.codice = codice;
    }


    /**
     * Gets the valore value for this AttributoDataRangeType.
     * 
     * @return valore
     */
    public it.emilia_romagna.regione.www.rer_fonte_raccoglitore.DateRangeType getValore() {
        return valore;
    }


    /**
     * Sets the valore value for this AttributoDataRangeType.
     * 
     * @param valore
     */
    public void setValore(it.emilia_romagna.regione.www.rer_fonte_raccoglitore.DateRangeType valore) {
        this.valore = valore;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof AttributoDataRangeType)) return false;
        AttributoDataRangeType other = (AttributoDataRangeType) obj;
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
            ((this.valore==null && other.getValore()==null) || 
             (this.valore!=null &&
              this.valore.equals(other.getValore())));
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
        if (getValore() != null) {
            _hashCode += getValore().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(AttributoDataRangeType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "AttributoDataRangeType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codice");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codice"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("valore");
        elemField.setXmlName(new javax.xml.namespace.QName("", "valore"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "DateRangeType"));
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
