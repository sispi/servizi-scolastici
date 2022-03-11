
/**
 * ExceptionException.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

package it.kdm.docer.clients;

public class ExceptionException extends java.lang.Exception{

    private static final long serialVersionUID = 1401205918135L;
    
    private it.kdm.docer.clients.AuthenticationServiceStub.Exception faultMessage;

    
        public ExceptionException() {
            super("ExceptionException");
        }

        public ExceptionException(java.lang.String s) {
           super(s);
        }

        public ExceptionException(java.lang.String s, java.lang.Throwable ex) {
          super(s, ex);
        }

        public ExceptionException(java.lang.Throwable cause) {
            super(cause);
        }
    

    public void setFaultMessage(it.kdm.docer.clients.AuthenticationServiceStub.Exception msg){
       faultMessage = msg;
    }
    
    public it.kdm.docer.clients.AuthenticationServiceStub.Exception getFaultMessage(){
       return faultMessage;
    }
}
    