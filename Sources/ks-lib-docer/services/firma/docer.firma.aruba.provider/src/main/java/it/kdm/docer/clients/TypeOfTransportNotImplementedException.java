
/**
 * TypeOfTransportNotImplementedException.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

package it.kdm.docer.clients;

public class TypeOfTransportNotImplementedException extends java.lang.Exception{

    private static final long serialVersionUID = 1539775748827L;
    
    private it.kdm.docer.clients.ArubaSignServiceServiceStub.TypeOfTransportNotImplementedE faultMessage;

    
        public TypeOfTransportNotImplementedException() {
            super("TypeOfTransportNotImplementedException");
        }

        public TypeOfTransportNotImplementedException(java.lang.String s) {
           super(s);
        }

        public TypeOfTransportNotImplementedException(java.lang.String s, java.lang.Throwable ex) {
          super(s, ex);
        }

        public TypeOfTransportNotImplementedException(java.lang.Throwable cause) {
            super(cause);
        }
    

    public void setFaultMessage(it.kdm.docer.clients.ArubaSignServiceServiceStub.TypeOfTransportNotImplementedE msg){
       faultMessage = msg;
    }
    
    public it.kdm.docer.clients.ArubaSignServiceServiceStub.TypeOfTransportNotImplementedE getFaultMessage(){
       return faultMessage;
    }
}
    