/**
 * FacetField.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.emilia_romagna.regione.www.rer_fonte_raccoglitore;

public class FacetField  implements java.io.Serializable {
    private org.apache.axis.types.NMToken facetFieldName;

    private it.emilia_romagna.regione.www.rer_fonte_raccoglitore.FacetFieldFacetFieldValue[] facetFieldValue;

    public FacetField() {
    }

    public FacetField(
           org.apache.axis.types.NMToken facetFieldName,
           it.emilia_romagna.regione.www.rer_fonte_raccoglitore.FacetFieldFacetFieldValue[] facetFieldValue) {
           this.facetFieldName = facetFieldName;
           this.facetFieldValue = facetFieldValue;
    }


    /**
     * Gets the facetFieldName value for this FacetField.
     * 
     * @return facetFieldName
     */
    public org.apache.axis.types.NMToken getFacetFieldName() {
        return facetFieldName;
    }


    /**
     * Sets the facetFieldName value for this FacetField.
     * 
     * @param facetFieldName
     */
    public void setFacetFieldName(org.apache.axis.types.NMToken facetFieldName) {
        this.facetFieldName = facetFieldName;
    }


    /**
     * Gets the facetFieldValue value for this FacetField.
     * 
     * @return facetFieldValue
     */
    public it.emilia_romagna.regione.www.rer_fonte_raccoglitore.FacetFieldFacetFieldValue[] getFacetFieldValue() {
        return facetFieldValue;
    }


    /**
     * Sets the facetFieldValue value for this FacetField.
     * 
     * @param facetFieldValue
     */
    public void setFacetFieldValue(it.emilia_romagna.regione.www.rer_fonte_raccoglitore.FacetFieldFacetFieldValue[] facetFieldValue) {
        this.facetFieldValue = facetFieldValue;
    }

    public it.emilia_romagna.regione.www.rer_fonte_raccoglitore.FacetFieldFacetFieldValue getFacetFieldValue(int i) {
        return this.facetFieldValue[i];
    }

    public void setFacetFieldValue(int i, it.emilia_romagna.regione.www.rer_fonte_raccoglitore.FacetFieldFacetFieldValue _value) {
        this.facetFieldValue[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof FacetField)) return false;
        FacetField other = (FacetField) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.facetFieldName==null && other.getFacetFieldName()==null) || 
             (this.facetFieldName!=null &&
              this.facetFieldName.equals(other.getFacetFieldName()))) &&
            ((this.facetFieldValue==null && other.getFacetFieldValue()==null) || 
             (this.facetFieldValue!=null &&
              java.util.Arrays.equals(this.facetFieldValue, other.getFacetFieldValue())));
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
        if (getFacetFieldName() != null) {
            _hashCode += getFacetFieldName().hashCode();
        }
        if (getFacetFieldValue() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getFacetFieldValue());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getFacetFieldValue(), i);
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
        new org.apache.axis.description.TypeDesc(FacetField.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "FacetField"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("facetFieldName");
        elemField.setXmlName(new javax.xml.namespace.QName("", "facetFieldName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "NMTOKEN"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("facetFieldValue");
        elemField.setXmlName(new javax.xml.namespace.QName("", "facetFieldValue"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", ">FacetField>facetFieldValue"));
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
