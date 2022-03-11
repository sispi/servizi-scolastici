package it.kdm.docer.webservices.exceptions;

import java.io.Serializable;


public class DocerException extends Exception {//implements Serializable {

	private static final long serialVersionUID = 8270780286503142164L;

		private int errorNumber;

	    private java.lang.String errorDescription;

	    public DocerException(
	           int errorNumber,
	           String errorDescription) {
	    	super(errorDescription);

	    	this.errorNumber = errorNumber;
	        this.errorDescription = errorDescription;
	    }


	    /**
	     * Gets the errorNumber value for this DocerEx.
	     * 
	     * @return errorCode
	     */
	    public int getErrorNumber() {
	        return errorNumber;
	    }


	    /**
	     * Sets the errorCode value for this DocerEx.
	     * 
	     * @param errorCode
	     */
	    public void setErrorNumber(int errorNumber) {
	        this.errorNumber = errorNumber;
	    }


	    /**
	     * Gets the errorDescription value for this DocerEx.
	     * 
	     * @return errorDescription
	     */
	    public java.lang.String getErrorDescription() {
	        return errorDescription;
	    }


	    /**
	     * Sets the message1 value for this DocerEx.
	     * 
	     * @param message1
	     */
	    public void setErrorDescription(java.lang.String errorDescription) {
	        this.errorDescription = errorDescription;
	    }

	    
//	    private java.lang.Object __equalsCalc = null;
//	    public synchronized boolean equals(java.lang.Object obj) {
//	        if (!(obj instanceof DocerEx)) return false;
//	        DocerEx other = (DocerEx) obj;
//	        if (obj == null) return false;
//	        if (this == obj) return true;
//	        if (__equalsCalc != null) {
//	            return (__equalsCalc == obj);
//	        }
//	        __equalsCalc = obj;
//	        boolean _equals;
//	        _equals = true && 
//	            this.errorNumber == other.getErrorNumber() &&
//	            ((this.errorDescription==null && other.getErrorDescription()==null) || 
//	             (this.errorDescription!=null &&
//	              this.errorDescription.equals(other.getErrorDescription())));
//	        __equalsCalc = null;
//	        return _equals;
//	    }
//
//	    private boolean __hashCodeCalc = false;
//	    public synchronized int hashCode() {
//	        if (__hashCodeCalc) {
//	            return 0;
//	        }
//	        __hashCodeCalc = true;
//	        int _hashCode = 1;
//	        _hashCode += getErrorNumber();
//	        if (getErrorDescription() != null) {
//	            _hashCode += getErrorDescription().hashCode();
//	        }
//	        __hashCodeCalc = false;
//	        return _hashCode;
//	    }
//
//	    // Type metadata
//	    private static org.apache.axis.description.TypeDesc typeDesc =
//	        new org.apache.axis.description.TypeDesc(DocerEx.class, true);
//
//	    static {
//	        typeDesc.setXmlType(new javax.xml.namespace.QName("http://exceptions.docer.kdm.it", "DocerEx"));
//	        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
//	        elemField.setFieldName("errorNumber");
//	        elemField.setXmlName(new javax.xml.namespace.QName("http://www.alfresco.org/ws/service/repository/1.0", "errorNumber"));
//	        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
//	        elemField.setNillable(false);
//	        typeDesc.addFieldDesc(elemField);
//	        elemField = new org.apache.axis.description.ElementDesc();
//	        elemField.setFieldName("errorDescription");
//	        elemField.setXmlName(new javax.xml.namespace.QName("http://www.alfresco.org/ws/service/repository/1.0", "errorDescription"));
//	        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
//	        elemField.setNillable(false);
//	        typeDesc.addFieldDesc(elemField);
//	    }
//
//	    /**
//	     * Return type metadata object
//	     */
//	    public static org.apache.axis.description.TypeDesc getTypeDesc() {
//	        return typeDesc;
//	    }
//
//	    /**
//	     * Get Custom Serializer
//	     */
//	    public static org.apache.axis.encoding.Serializer getSerializer(
//	           java.lang.String mechType, 
//	           java.lang.Class _javaType,  
//	           javax.xml.namespace.QName _xmlType) {
//	        return 
//	          new  org.apache.axis.encoding.ser.BeanSerializer(
//	            _javaType, _xmlType, typeDesc);
//	    }
//
//	    /**
//	     * Get Custom Deserializer
//	     */
//	    public static org.apache.axis.encoding.Deserializer getDeserializer(
//	           java.lang.String mechType, 
//	           java.lang.Class _javaType,  
//	           javax.xml.namespace.QName _xmlType) {
//	        return 
//	          new  org.apache.axis.encoding.ser.BeanDeserializer(
//	            _javaType, _xmlType, typeDesc);
//	    }
//
//
//	    /**
//	     * Writes the exception data to the faultDetails
//	     */
//	    public void writeDetails(javax.xml.namespace.QName qname, org.apache.axis.encoding.SerializationContext context) throws java.io.IOException {
//	        context.serialize(qname, null, this);
//	    }
	    
}
