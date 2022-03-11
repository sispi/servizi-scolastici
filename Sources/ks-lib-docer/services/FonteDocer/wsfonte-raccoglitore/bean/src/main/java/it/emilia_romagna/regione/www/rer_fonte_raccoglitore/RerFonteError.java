/**
 * RerFonteError.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.emilia_romagna.regione.www.rer_fonte_raccoglitore;

public class RerFonteError extends org.apache.axis.AxisFault {
    public java.lang.String[] parameters;
    public java.lang.String[] getParameters() {
        return this.parameters;
    }

    public RerFonteError() {
    }

    public RerFonteError(java.lang.Exception target) {
        super(target);
    }

    public RerFonteError(java.lang.String message, java.lang.Throwable t) {
        super(message, t);
    }

      public RerFonteError(java.lang.String[] parameters) {
        this.parameters = parameters;
    }

    /**
     * Writes the exception data to the faultDetails
     */
    public void writeDetails(javax.xml.namespace.QName qname, org.apache.axis.encoding.SerializationContext context) throws java.io.IOException {
        context.serialize(qname, null, parameters);
    }
}
