
/**
 * BusinessLogicExceptionException.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.5.6  Built on : Aug 30, 2011 (10:00:16 CEST)
 */

package it.kdm.docer.clients;

public class BusinessLogicExceptionException extends java.lang.Exception{
    
    private it.kdm.docer.clients.WSTimbroDigitaleStub.BusinessLogicException faultMessage;

    
        public BusinessLogicExceptionException() {
            super("BusinessLogicExceptionException");
        }

        public BusinessLogicExceptionException(java.lang.String s) {
           super(s);
        }

        public BusinessLogicExceptionException(java.lang.String s, java.lang.Throwable ex) {
          super(s, ex);
        }

        public BusinessLogicExceptionException(java.lang.Throwable cause) {
            super(cause);
        }
    

    public void setFaultMessage(it.kdm.docer.clients.WSTimbroDigitaleStub.BusinessLogicException msg){
       faultMessage = msg;
    }
    
    public it.kdm.docer.clients.WSTimbroDigitaleStub.BusinessLogicException getFaultMessage(){
       return faultMessage;
    }
}
    