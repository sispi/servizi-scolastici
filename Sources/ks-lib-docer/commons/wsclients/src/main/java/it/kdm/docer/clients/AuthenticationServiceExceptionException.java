
/**
 * AuthenticationServiceExceptionException.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

package it.kdm.docer.clients;

public class AuthenticationServiceExceptionException extends java.lang.Exception{

    private static final long serialVersionUID = 1424355687978L;
    
    private it.kdm.docer.clients.AuthenticationServiceStub.AuthenticationServiceException faultMessage;

    
        public AuthenticationServiceExceptionException() {
            super("AuthenticationServiceExceptionException");
        }

        public AuthenticationServiceExceptionException(java.lang.String s) {
           super(s);
        }

        public AuthenticationServiceExceptionException(java.lang.String s, java.lang.Throwable ex) {
          super(s, ex);
        }

        public AuthenticationServiceExceptionException(java.lang.Throwable cause) {
            super(cause);
        }
    

    public void setFaultMessage(it.kdm.docer.clients.AuthenticationServiceStub.AuthenticationServiceException msg){
       faultMessage = msg;
    }
    
    public it.kdm.docer.clients.AuthenticationServiceStub.AuthenticationServiceException getFaultMessage(){
       return faultMessage;
    }
}
    