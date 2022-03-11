/**
 * FilterQuery.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.emilia_romagna.regione.www.rer_fonte_raccoglitore;

public class FilterQuery  implements java.io.Serializable {
    private org.apache.axis.types.NMToken fieldName;

    private java.lang.String fieldQuery;

    public FilterQuery() {
    }

    public FilterQuery(
           org.apache.axis.types.NMToken fieldName,
           java.lang.String fieldQuery) {
           this.fieldName = fieldName;
           this.fieldQuery = fieldQuery;
    }


    /**
     * Gets the fieldName value for this FilterQuery.
     * 
     * @return fieldName
     */
    public org.apache.axis.types.NMToken getFieldName() {
        return fieldName;
    }


    /**
     * Sets the fieldName value for this FilterQuery.
     * 
     * @param fieldName
     */
    public void setFieldName(org.apache.axis.types.NMToken fieldName) {
        this.fieldName = fieldName;
    }


    /**
     * Gets the fieldQuery value for this FilterQuery.
     * 
     * @return fieldQuery
     */
    public java.lang.String getFieldQuery() {
        return fieldQuery;
    }


    /**
     * Sets the fieldQuery value for this FilterQuery.
     * 
     * @param fieldQuery
     */
    public void setFieldQuery(java.lang.String fieldQuery) {
        this.fieldQuery = fieldQuery;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof FilterQuery)) return false;
        FilterQuery other = (FilterQuery) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.fieldName==null && other.getFieldName()==null) || 
             (this.fieldName!=null &&
              this.fieldName.equals(other.getFieldName()))) &&
            ((this.fieldQuery==null && other.getFieldQuery()==null) || 
             (this.fieldQuery!=null &&
              this.fieldQuery.equals(other.getFieldQuery())));
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
        if (getFieldName() != null) {
            _hashCode += getFieldName().hashCode();
        }
        if (getFieldQuery() != null) {
            _hashCode += getFieldQuery().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(FilterQuery.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "FilterQuery"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fieldName");
        elemField.setXmlName(new javax.xml.namespace.QName("", "fieldName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "NMTOKEN"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fieldQuery");
        elemField.setXmlName(new javax.xml.namespace.QName("", "fieldQuery"));
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
