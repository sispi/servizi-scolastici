/**
 * TipoAttributoType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.emilia_romagna.regione.www.rer_fonte_raccoglitore;

public class TipoAttributoType implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected TipoAttributoType(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _TESTO = "TESTO";
    public static final java.lang.String _DATA = "DATA";
    public static final java.lang.String _DATARANGE = "DATARANGE";
    public static final java.lang.String _NUMERO = "NUMERO";
    public static final java.lang.String _DOMINIO = "DOMINIO";
    public static final java.lang.String _BOOLEAN = "BOOLEAN";
    public static final TipoAttributoType TESTO = new TipoAttributoType(_TESTO);
    public static final TipoAttributoType DATA = new TipoAttributoType(_DATA);
    public static final TipoAttributoType DATARANGE = new TipoAttributoType(_DATARANGE);
    public static final TipoAttributoType NUMERO = new TipoAttributoType(_NUMERO);
    public static final TipoAttributoType DOMINIO = new TipoAttributoType(_DOMINIO);
    public static final TipoAttributoType BOOLEAN = new TipoAttributoType(_BOOLEAN);
    public java.lang.String getValue() { return _value_;}
    public static TipoAttributoType fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        TipoAttributoType enumeration = (TipoAttributoType)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static TipoAttributoType fromString(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        return fromValue(value);
    }
    public boolean equals(java.lang.Object obj) {return (obj == this);}
    public int hashCode() { return toString().hashCode();}
    public java.lang.String toString() { return _value_;}
    public java.lang.Object readResolve() throws java.io.ObjectStreamException { return fromValue(_value_);}
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new org.apache.axis.encoding.ser.EnumSerializer(
            _javaType, _xmlType);
    }
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new org.apache.axis.encoding.ser.EnumDeserializer(
            _javaType, _xmlType);
    }
    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TipoAttributoType.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "TipoAttributoType"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
