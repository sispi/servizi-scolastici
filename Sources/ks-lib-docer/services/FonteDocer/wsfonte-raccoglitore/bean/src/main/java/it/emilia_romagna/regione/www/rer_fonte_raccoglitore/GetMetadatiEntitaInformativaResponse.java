/**
 * GetMetadatiEntitaInformativaResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.emilia_romagna.regione.www.rer_fonte_raccoglitore;

public class GetMetadatiEntitaInformativaResponse  implements java.io.Serializable {
    private java.lang.String xmlMetadata;

    public GetMetadatiEntitaInformativaResponse() {
    }

    public GetMetadatiEntitaInformativaResponse(
           java.lang.String xmlMetadata) {
           this.xmlMetadata = xmlMetadata;
    }


    /**
     * Gets the xmlMetadata value for this GetMetadatiEntitaInformativaResponse.
     * 
     * @return xmlMetadata
     */
    public java.lang.String getXmlMetadata() {
        return xmlMetadata;
    }


    /**
     * Sets the xmlMetadata value for this GetMetadatiEntitaInformativaResponse.
     * 
     * @param xmlMetadata
     */
    public void setXmlMetadata(java.lang.String xmlMetadata) {
        this.xmlMetadata = xmlMetadata;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetMetadatiEntitaInformativaResponse)) return false;
        GetMetadatiEntitaInformativaResponse other = (GetMetadatiEntitaInformativaResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.xmlMetadata==null && other.getXmlMetadata()==null) || 
             (this.xmlMetadata!=null &&
              this.xmlMetadata.equals(other.getXmlMetadata())));
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
        if (getXmlMetadata() != null) {
            _hashCode += getXmlMetadata().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetMetadatiEntitaInformativaResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", ">getMetadatiEntitaInformativaResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("xmlMetadata");
        elemField.setXmlName(new javax.xml.namespace.QName("", "xmlMetadata"));
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
