/**
 * SearchEntitaInformativaAvanzataResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.emilia_romagna.regione.www.rer_fonte_raccoglitore;

public class SearchEntitaInformativaAvanzataResponse  implements java.io.Serializable {
    private int queryTime;

    private long numFound;

    private float maxScore;

    private java.lang.String[] id;

    public SearchEntitaInformativaAvanzataResponse() {
    }

    public SearchEntitaInformativaAvanzataResponse(
           int queryTime,
           long numFound,
           float maxScore,
           java.lang.String[] id) {
           this.queryTime = queryTime;
           this.numFound = numFound;
           this.maxScore = maxScore;
           this.id = id;
    }


    /**
     * Gets the queryTime value for this SearchEntitaInformativaAvanzataResponse.
     * 
     * @return queryTime
     */
    public int getQueryTime() {
        return queryTime;
    }


    /**
     * Sets the queryTime value for this SearchEntitaInformativaAvanzataResponse.
     * 
     * @param queryTime
     */
    public void setQueryTime(int queryTime) {
        this.queryTime = queryTime;
    }


    /**
     * Gets the numFound value for this SearchEntitaInformativaAvanzataResponse.
     * 
     * @return numFound
     */
    public long getNumFound() {
        return numFound;
    }


    /**
     * Sets the numFound value for this SearchEntitaInformativaAvanzataResponse.
     * 
     * @param numFound
     */
    public void setNumFound(long numFound) {
        this.numFound = numFound;
    }


    /**
     * Gets the maxScore value for this SearchEntitaInformativaAvanzataResponse.
     * 
     * @return maxScore
     */
    public float getMaxScore() {
        return maxScore;
    }


    /**
     * Sets the maxScore value for this SearchEntitaInformativaAvanzataResponse.
     * 
     * @param maxScore
     */
    public void setMaxScore(float maxScore) {
        this.maxScore = maxScore;
    }


    /**
     * Gets the id value for this SearchEntitaInformativaAvanzataResponse.
     * 
     * @return id
     */
    public java.lang.String[] getId() {
        return id;
    }


    /**
     * Sets the id value for this SearchEntitaInformativaAvanzataResponse.
     * 
     * @param id
     */
    public void setId(java.lang.String[] id) {
        this.id = id;
    }

    public java.lang.String getId(int i) {
        return this.id[i];
    }

    public void setId(int i, java.lang.String _value) {
        this.id[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SearchEntitaInformativaAvanzataResponse)) return false;
        SearchEntitaInformativaAvanzataResponse other = (SearchEntitaInformativaAvanzataResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.queryTime == other.getQueryTime() &&
            this.numFound == other.getNumFound() &&
            this.maxScore == other.getMaxScore() &&
            ((this.id==null && other.getId()==null) || 
             (this.id!=null &&
              java.util.Arrays.equals(this.id, other.getId())));
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
        _hashCode += getQueryTime();
        _hashCode += new Long(getNumFound()).hashCode();
        _hashCode += new Float(getMaxScore()).hashCode();
        if (getId() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getId());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getId(), i);
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
        new org.apache.axis.description.TypeDesc(SearchEntitaInformativaAvanzataResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", ">searchEntitaInformativaAvanzataResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("queryTime");
        elemField.setXmlName(new javax.xml.namespace.QName("", "queryTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numFound");
        elemField.setXmlName(new javax.xml.namespace.QName("", "numFound"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("maxScore");
        elemField.setXmlName(new javax.xml.namespace.QName("", "maxScore"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "float"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
