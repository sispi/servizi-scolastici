
/**
 * DocerExceptionException.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

package it.kdm.docer.clients;

public class DocerExceptionException extends java.lang.Exception{

    private static final long serialVersionUID = 1401205383171L;
    
    private it.kdm.docer.clients.DocerServicesStub.DocerException faultMessage;

    
        public DocerExceptionException() {
            super("DocerExceptionException");
        }

        public DocerExceptionException(java.lang.String s) {
           super(s);
        }

        public DocerExceptionException(java.lang.String s, java.lang.Throwable ex) {
          super(s, ex);
        }

        public DocerExceptionException(java.lang.Throwable cause) {
            super(cause);
        }
    

    public void setFaultMessage(it.kdm.docer.clients.DocerServicesStub.DocerException msg){
       faultMessage = msg;
    }
    
    public it.kdm.docer.clients.DocerServicesStub.DocerException getFaultMessage(){
       return faultMessage;
    }
}
    