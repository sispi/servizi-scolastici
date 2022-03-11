/**
 * SearchEntitaInformativaRequest.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.emilia_romagna.regione.www.rer_fonte_raccoglitore;

public class SearchEntitaInformativaRequest  implements java.io.Serializable {
    private java.lang.String codiceFonte;

    private it.emilia_romagna.regione.www.rer_fonte_raccoglitore.GetEntitaInformativaRequestDelegheDelega[] deleghe;

    private java.lang.String testo;

    private it.emilia_romagna.regione.www.rer_fonte_raccoglitore.DateRangeType dateRange;

    private java.lang.String numeroRegistrazione;

    public SearchEntitaInformativaRequest() {
    }

    public SearchEntitaInformativaRequest(
           java.lang.String codiceFonte,
           it.emilia_romagna.regione.www.rer_fonte_raccoglitore.GetEntitaInformativaRequestDelegheDelega[] deleghe,
           java.lang.String testo,
           it.emilia_romagna.regione.www.rer_fonte_raccoglitore.DateRangeType dateRange,
           java.lang.String numeroRegistrazione) {
           this.codiceFonte = codiceFonte;
           this.deleghe = deleghe;
           this.testo = testo;
           this.dateRange = dateRange;
           this.numeroRegistrazione = numeroRegistrazione;
    }


    /**
     * Gets the codiceFonte value for this SearchEntitaInformativaRequest.
     * 
     * @return codiceFonte
     */
    public java.lang.String getCodiceFonte() {
        return codiceFonte;
    }


    /**
     * Sets the codiceFonte value for this SearchEntitaInformativaRequest.
     * 
     * @param codiceFonte
     */
    public void setCodiceFonte(java.lang.String codiceFonte) {
        this.codiceFonte = codiceFonte;
    }


    /**
     * Gets the deleghe value for this SearchEntitaInformativaRequest.
     * 
     * @return deleghe
     */
    public it.emilia_romagna.regione.www.rer_fonte_raccoglitore.GetEntitaInformativaRequestDelegheDelega[] getDeleghe() {
        return deleghe;
    }


    /**
     * Sets the deleghe value for this SearchEntitaInformativaRequest.
     * 
     * @param deleghe
     */
    public void setDeleghe(it.emilia_romagna.regione.www.rer_fonte_raccoglitore.GetEntitaInformativaRequestDelegheDelega[] deleghe) {
        this.deleghe = deleghe;
    }


    /**
     * Gets the testo value for this SearchEntitaInformativaRequest.
     * 
     * @return testo
     */
    public java.lang.String getTesto() {
        return testo;
    }


    /**
     * Sets the testo value for this SearchEntitaInformativaRequest.
     * 
     * @param testo
     */
    public void setTesto(java.lang.String testo) {
        this.testo = testo;
    }


    /**
     * Gets the dateRange value for this SearchEntitaInformativaRequest.
     * 
     * @return dateRange
     */
    public it.emilia_romagna.regione.www.rer_fonte_raccoglitore.DateRangeType getDateRange() {
        return dateRange;
    }


    /**
     * Sets the dateRange value for this SearchEntitaInformativaRequest.
     * 
     * @param dateRange
     */
    public void setDateRange(it.emilia_romagna.regione.www.rer_fonte_raccoglitore.DateRangeType dateRange) {
        this.dateRange = dateRange;
    }


    /**
     * Gets the numeroRegistrazione value for this SearchEntitaInformativaRequest.
     * 
     * @return numeroRegistrazione
     */
    public java.lang.String getNumeroRegistrazione() {
        return numeroRegistrazione;
    }


    /**
     * Sets the numeroRegistrazione value for this SearchEntitaInformativaRequest.
     * 
     * @param numeroRegistrazione
     */
    public void setNumeroRegistrazione(java.lang.String numeroRegistrazione) {
        this.numeroRegistrazione = numeroRegistrazione;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SearchEntitaInformativaRequest)) return false;
        SearchEntitaInformativaRequest other = (SearchEntitaInformativaRequest) obj;
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
            ((this.testo==null && other.getTesto()==null) || 
             (this.testo!=null &&
              this.testo.equals(other.getTesto()))) &&
            ((this.dateRange==null && other.getDateRange()==null) || 
             (this.dateRange!=null &&
              this.dateRange.equals(other.getDateRange()))) &&
            ((this.numeroRegistrazione==null && other.getNumeroRegistrazione()==null) || 
             (this.numeroRegistrazione!=null &&
              this.numeroRegistrazione.equals(other.getNumeroRegistrazione())));
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
        if (getTesto() != null) {
            _hashCode += getTesto().hashCode();
        }
        if (getDateRange() != null) {
            _hashCode += getDateRange().hashCode();
        }
        if (getNumeroRegistrazione() != null) {
            _hashCode += getNumeroRegistrazione().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SearchEntitaInformativaRequest.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", ">searchEntitaInformativaRequest"));
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
        elemField.setFieldName("testo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "testo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dateRange");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dateRange"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "DateRangeType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numeroRegistrazione");
        elemField.setXmlName(new javax.xml.namespace.QName("", "numeroRegistrazione"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
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
