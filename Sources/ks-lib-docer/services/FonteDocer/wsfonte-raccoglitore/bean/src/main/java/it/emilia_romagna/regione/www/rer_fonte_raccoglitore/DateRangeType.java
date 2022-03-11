/**
 * DateRangeType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.emilia_romagna.regione.www.rer_fonte_raccoglitore;

public class DateRangeType  implements java.io.Serializable {
    private java.util.Date minDate;

    private java.util.Date maxDate;

    private boolean isLeftRangeClosed;

    private boolean isRightRangeClosed;

    public DateRangeType() {
    }

    public DateRangeType(
           java.util.Date minDate,
           java.util.Date maxDate,
           boolean isLeftRangeClosed,
           boolean isRightRangeClosed) {
           this.minDate = minDate;
           this.maxDate = maxDate;
           this.isLeftRangeClosed = isLeftRangeClosed;
           this.isRightRangeClosed = isRightRangeClosed;
    }


    /**
     * Gets the minDate value for this DateRangeType.
     * 
     * @return minDate
     */
    public java.util.Date getMinDate() {
        return minDate;
    }


    /**
     * Sets the minDate value for this DateRangeType.
     * 
     * @param minDate
     */
    public void setMinDate(java.util.Date minDate) {
        this.minDate = minDate;
    }


    /**
     * Gets the maxDate value for this DateRangeType.
     * 
     * @return maxDate
     */
    public java.util.Date getMaxDate() {
        return maxDate;
    }


    /**
     * Sets the maxDate value for this DateRangeType.
     * 
     * @param maxDate
     */
    public void setMaxDate(java.util.Date maxDate) {
        this.maxDate = maxDate;
    }


    /**
     * Gets the isLeftRangeClosed value for this DateRangeType.
     * 
     * @return isLeftRangeClosed
     */
    public boolean isIsLeftRangeClosed() {
        return isLeftRangeClosed;
    }


    /**
     * Sets the isLeftRangeClosed value for this DateRangeType.
     * 
     * @param isLeftRangeClosed
     */
    public void setIsLeftRangeClosed(boolean isLeftRangeClosed) {
        this.isLeftRangeClosed = isLeftRangeClosed;
    }


    /**
     * Gets the isRightRangeClosed value for this DateRangeType.
     * 
     * @return isRightRangeClosed
     */
    public boolean isIsRightRangeClosed() {
        return isRightRangeClosed;
    }


    /**
     * Sets the isRightRangeClosed value for this DateRangeType.
     * 
     * @param isRightRangeClosed
     */
    public void setIsRightRangeClosed(boolean isRightRangeClosed) {
        this.isRightRangeClosed = isRightRangeClosed;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DateRangeType)) return false;
        DateRangeType other = (DateRangeType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.minDate==null && other.getMinDate()==null) || 
             (this.minDate!=null &&
              this.minDate.equals(other.getMinDate()))) &&
            ((this.maxDate==null && other.getMaxDate()==null) || 
             (this.maxDate!=null &&
              this.maxDate.equals(other.getMaxDate()))) &&
            this.isLeftRangeClosed == other.isIsLeftRangeClosed() &&
            this.isRightRangeClosed == other.isIsRightRangeClosed();
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
        if (getMinDate() != null) {
            _hashCode += getMinDate().hashCode();
        }
        if (getMaxDate() != null) {
            _hashCode += getMaxDate().hashCode();
        }
        _hashCode += (isIsLeftRangeClosed() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isIsRightRangeClosed() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DateRangeType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "DateRangeType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("minDate");
        elemField.setXmlName(new javax.xml.namespace.QName("", "minDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "date"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("maxDate");
        elemField.setXmlName(new javax.xml.namespace.QName("", "maxDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "date"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("isLeftRangeClosed");
        elemField.setXmlName(new javax.xml.namespace.QName("", "isLeftRangeClosed"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("isRightRangeClosed");
        elemField.setXmlName(new javax.xml.namespace.QName("", "isRightRangeClosed"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
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
