/**
 * SearchEntitaInformativaAvanzataRequest.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.emilia_romagna.regione.www.rer_fonte_raccoglitore;

public class SearchEntitaInformativaAvanzataRequest  implements java.io.Serializable {
    private java.lang.String codiceFonte;

    private it.emilia_romagna.regione.www.rer_fonte_raccoglitore.GetEntitaInformativaRequestDelegheDelega[] deleghe;

    private it.emilia_romagna.regione.www.rer_fonte_raccoglitore.AttributoBooleanoType[] attributiBoolean;

    private it.emilia_romagna.regione.www.rer_fonte_raccoglitore.AttributoDataRangeType[] attributiDataRange;

    private it.emilia_romagna.regione.www.rer_fonte_raccoglitore.AttributoDataType[] attributiData;

    private it.emilia_romagna.regione.www.rer_fonte_raccoglitore.AttributoNumeroType[] attributiNumero;

    private it.emilia_romagna.regione.www.rer_fonte_raccoglitore.AttributoTestoType[] attributiTesto;

    public SearchEntitaInformativaAvanzataRequest() {
    }

    public SearchEntitaInformativaAvanzataRequest(
           java.lang.String codiceFonte,
           it.emilia_romagna.regione.www.rer_fonte_raccoglitore.GetEntitaInformativaRequestDelegheDelega[] deleghe,
           it.emilia_romagna.regione.www.rer_fonte_raccoglitore.AttributoBooleanoType[] attributiBoolean,
           it.emilia_romagna.regione.www.rer_fonte_raccoglitore.AttributoDataRangeType[] attributiDataRange,
           it.emilia_romagna.regione.www.rer_fonte_raccoglitore.AttributoDataType[] attributiData,
           it.emilia_romagna.regione.www.rer_fonte_raccoglitore.AttributoNumeroType[] attributiNumero,
           it.emilia_romagna.regione.www.rer_fonte_raccoglitore.AttributoTestoType[] attributiTesto) {
           this.codiceFonte = codiceFonte;
           this.deleghe = deleghe;
           this.attributiBoolean = attributiBoolean;
           this.attributiDataRange = attributiDataRange;
           this.attributiData = attributiData;
           this.attributiNumero = attributiNumero;
           this.attributiTesto = attributiTesto;
    }


    /**
     * Gets the codiceFonte value for this SearchEntitaInformativaAvanzataRequest.
     * 
     * @return codiceFonte
     */
    public java.lang.String getCodiceFonte() {
        return codiceFonte;
    }


    /**
     * Sets the codiceFonte value for this SearchEntitaInformativaAvanzataRequest.
     * 
     * @param codiceFonte
     */
    public void setCodiceFonte(java.lang.String codiceFonte) {
        this.codiceFonte = codiceFonte;
    }


    /**
     * Gets the deleghe value for this SearchEntitaInformativaAvanzataRequest.
     * 
     * @return deleghe
     */
    public it.emilia_romagna.regione.www.rer_fonte_raccoglitore.GetEntitaInformativaRequestDelegheDelega[] getDeleghe() {
        return deleghe;
    }


    /**
     * Sets the deleghe value for this SearchEntitaInformativaAvanzataRequest.
     * 
     * @param deleghe
     */
    public void setDeleghe(it.emilia_romagna.regione.www.rer_fonte_raccoglitore.GetEntitaInformativaRequestDelegheDelega[] deleghe) {
        this.deleghe = deleghe;
    }


    /**
     * Gets the attributiBoolean value for this SearchEntitaInformativaAvanzataRequest.
     * 
     * @return attributiBoolean
     */
    public it.emilia_romagna.regione.www.rer_fonte_raccoglitore.AttributoBooleanoType[] getAttributiBoolean() {
        return attributiBoolean;
    }


    /**
     * Sets the attributiBoolean value for this SearchEntitaInformativaAvanzataRequest.
     * 
     * @param attributiBoolean
     */
    public void setAttributiBoolean(it.emilia_romagna.regione.www.rer_fonte_raccoglitore.AttributoBooleanoType[] attributiBoolean) {
        this.attributiBoolean = attributiBoolean;
    }

    public it.emilia_romagna.regione.www.rer_fonte_raccoglitore.AttributoBooleanoType getAttributiBoolean(int i) {
        return this.attributiBoolean[i];
    }

    public void setAttributiBoolean(int i, it.emilia_romagna.regione.www.rer_fonte_raccoglitore.AttributoBooleanoType _value) {
        this.attributiBoolean[i] = _value;
    }


    /**
     * Gets the attributiDataRange value for this SearchEntitaInformativaAvanzataRequest.
     * 
     * @return attributiDataRange
     */
    public it.emilia_romagna.regione.www.rer_fonte_raccoglitore.AttributoDataRangeType[] getAttributiDataRange() {
        return attributiDataRange;
    }


    /**
     * Sets the attributiDataRange value for this SearchEntitaInformativaAvanzataRequest.
     * 
     * @param attributiDataRange
     */
    public void setAttributiDataRange(it.emilia_romagna.regione.www.rer_fonte_raccoglitore.AttributoDataRangeType[] attributiDataRange) {
        this.attributiDataRange = attributiDataRange;
    }

    public it.emilia_romagna.regione.www.rer_fonte_raccoglitore.AttributoDataRangeType getAttributiDataRange(int i) {
        return this.attributiDataRange[i];
    }

    public void setAttributiDataRange(int i, it.emilia_romagna.regione.www.rer_fonte_raccoglitore.AttributoDataRangeType _value) {
        this.attributiDataRange[i] = _value;
    }


    /**
     * Gets the attributiData value for this SearchEntitaInformativaAvanzataRequest.
     * 
     * @return attributiData
     */
    public it.emilia_romagna.regione.www.rer_fonte_raccoglitore.AttributoDataType[] getAttributiData() {
        return attributiData;
    }


    /**
     * Sets the attributiData value for this SearchEntitaInformativaAvanzataRequest.
     * 
     * @param attributiData
     */
    public void setAttributiData(it.emilia_romagna.regione.www.rer_fonte_raccoglitore.AttributoDataType[] attributiData) {
        this.attributiData = attributiData;
    }

    public it.emilia_romagna.regione.www.rer_fonte_raccoglitore.AttributoDataType getAttributiData(int i) {
        return this.attributiData[i];
    }

    public void setAttributiData(int i, it.emilia_romagna.regione.www.rer_fonte_raccoglitore.AttributoDataType _value) {
        this.attributiData[i] = _value;
    }


    /**
     * Gets the attributiNumero value for this SearchEntitaInformativaAvanzataRequest.
     * 
     * @return attributiNumero
     */
    public it.emilia_romagna.regione.www.rer_fonte_raccoglitore.AttributoNumeroType[] getAttributiNumero() {
        return attributiNumero;
    }


    /**
     * Sets the attributiNumero value for this SearchEntitaInformativaAvanzataRequest.
     * 
     * @param attributiNumero
     */
    public void setAttributiNumero(it.emilia_romagna.regione.www.rer_fonte_raccoglitore.AttributoNumeroType[] attributiNumero) {
        this.attributiNumero = attributiNumero;
    }

    public it.emilia_romagna.regione.www.rer_fonte_raccoglitore.AttributoNumeroType getAttributiNumero(int i) {
        return this.attributiNumero[i];
    }

    public void setAttributiNumero(int i, it.emilia_romagna.regione.www.rer_fonte_raccoglitore.AttributoNumeroType _value) {
        this.attributiNumero[i] = _value;
    }


    /**
     * Gets the attributiTesto value for this SearchEntitaInformativaAvanzataRequest.
     * 
     * @return attributiTesto
     */
    public it.emilia_romagna.regione.www.rer_fonte_raccoglitore.AttributoTestoType[] getAttributiTesto() {
        return attributiTesto;
    }


    /**
     * Sets the attributiTesto value for this SearchEntitaInformativaAvanzataRequest.
     * 
     * @param attributiTesto
     */
    public void setAttributiTesto(it.emilia_romagna.regione.www.rer_fonte_raccoglitore.AttributoTestoType[] attributiTesto) {
        this.attributiTesto = attributiTesto;
    }

    public it.emilia_romagna.regione.www.rer_fonte_raccoglitore.AttributoTestoType getAttributiTesto(int i) {
        return this.attributiTesto[i];
    }

    public void setAttributiTesto(int i, it.emilia_romagna.regione.www.rer_fonte_raccoglitore.AttributoTestoType _value) {
        this.attributiTesto[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SearchEntitaInformativaAvanzataRequest)) return false;
        SearchEntitaInformativaAvanzataRequest other = (SearchEntitaInformativaAvanzataRequest) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.codiceFonte==null && other.getCodiceFonte()==null) || 
             (this.codiceFonte!=null &&
              this.codiceFonte.equals(other.getCodiceFonte()))) &&
            ((this.deleghe==null && other.getDeleghe()==null) || 
             (this.deleghe!=null &&
              java.util.Arrays.equals(this.deleghe, other.getDeleghe()))) &&
            ((this.attributiBoolean==null && other.getAttributiBoolean()==null) || 
             (this.attributiBoolean!=null &&
              java.util.Arrays.equals(this.attributiBoolean, other.getAttributiBoolean()))) &&
            ((this.attributiDataRange==null && other.getAttributiDataRange()==null) || 
             (this.attributiDataRange!=null &&
              java.util.Arrays.equals(this.attributiDataRange, other.getAttributiDataRange()))) &&
            ((this.attributiData==null && other.getAttributiData()==null) || 
             (this.attributiData!=null &&
              java.util.Arrays.equals(this.attributiData, other.getAttributiData()))) &&
            ((this.attributiNumero==null && other.getAttributiNumero()==null) || 
             (this.attributiNumero!=null &&
              java.util.Arrays.equals(this.attributiNumero, other.getAttributiNumero()))) &&
            ((this.attributiTesto==null && other.getAttributiTesto()==null) || 
             (this.attributiTesto!=null &&
              java.util.Arrays.equals(this.attributiTesto, other.getAttributiTesto())));
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
        if (getCodiceFonte() != null) {
            _hashCode += getCodiceFonte().hashCode();
        }
        if (getDeleghe() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getDeleghe());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getDeleghe(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getAttributiBoolean() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getAttributiBoolean());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getAttributiBoolean(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getAttributiDataRange() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getAttributiDataRange());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getAttributiDataRange(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getAttributiData() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getAttributiData());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getAttributiData(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getAttributiNumero() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getAttributiNumero());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getAttributiNumero(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getAttributiTesto() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getAttributiTesto());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getAttributiTesto(), i);
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
        new org.apache.axis.description.TypeDesc(SearchEntitaInformativaAvanzataRequest.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", ">searchEntitaInformativaAvanzataRequest"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codiceFonte");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codiceFonte"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("deleghe");
        elemField.setXmlName(new javax.xml.namespace.QName("", "deleghe"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "GetEntitaInformativaRequestDelegheDelega"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("", "delega"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("attributiBoolean");
        elemField.setXmlName(new javax.xml.namespace.QName("", "attributiBoolean"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "AttributoBooleanoType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("attributiDataRange");
        elemField.setXmlName(new javax.xml.namespace.QName("", "attributiDataRange"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "AttributoDataRangeType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("attributiData");
        elemField.setXmlName(new javax.xml.namespace.QName("", "attributiData"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "AttributoDataType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("attributiNumero");
        elemField.setXmlName(new javax.xml.namespace.QName("", "attributiNumero"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "AttributoNumeroType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("attributiTesto");
        elemField.setXmlName(new javax.xml.namespace.QName("", "attributiTesto"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "AttributoTestoType"));
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
