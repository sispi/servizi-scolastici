/**
 * GetAttributiRicercaResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.emilia_romagna.regione.www.rer_fonte_raccoglitore;

public class GetAttributiRicercaResponse  implements java.io.Serializable {
    private it.emilia_romagna.regione.www.rer_fonte_raccoglitore.AttributoType[] attributi;

    private boolean esito;

    public GetAttributiRicercaResponse() {
    }

    public GetAttributiRicercaResponse(
           it.emilia_romagna.regione.www.rer_fonte_raccoglitore.AttributoType[] attributi,
           boolean esito) {
           this.attributi = attributi;
           this.esito = esito;
    }


    /**
     * Gets the attributi value for this GetAttributiRicercaResponse.
     * 
     * @return attributi
     */
    public it.emilia_romagna.regione.www.rer_fonte_raccoglitore.AttributoType[] getAttributi() {
        return attributi;
    }


    /**
     * Sets the attributi value for this GetAttributiRicercaResponse.
     * 
     * @param attributi
     */
    public void setAttributi(it.emilia_romagna.regione.www.rer_fonte_raccoglitore.AttributoType[] attributi) {
        this.attributi = attributi;
    }

    public it.emilia_romagna.regione.www.rer_fonte_raccoglitore.AttributoType getAttributi(int i) {
        return this.attributi[i];
    }

    public void setAttributi(int i, it.emilia_romagna.regione.www.rer_fonte_raccoglitore.AttributoType _value) {
        this.attributi[i] = _value;
    }


    /**
     * Gets the esito value for this GetAttributiRicercaResponse.
     * 
     * @return esito
     */
    public boolean isEsito() {
        return esito;
    }


    /**
     * Sets the esito value for this GetAttributiRicercaResponse.
     * 
     * @param esito
     */
    public void setEsito(boolean esito) {
        this.esito = esito;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetAttributiRicercaResponse)) return false;
        GetAttributiRicercaResponse other = (GetAttributiRicercaResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.attributi==null && other.getAttributi()==null) || 
             (this.attributi!=null &&
              java.util.Arrays.equals(this.attributi, other.getAttributi()))) &&
            this.esito == other.isEsito();
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
        if (getAttributi() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getAttributi());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getAttributi(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        _hashCode += (isEsito() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetAttributiRicercaResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", ">getAttributiRicercaResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("attributi");
        elemField.setXmlName(new javax.xml.namespace.QName("", "attributi"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "AttributoType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("esito");
        elemField.setXmlName(new javax.xml.namespace.QName("", "esito"));
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
