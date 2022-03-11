
/**
 * HandlerQRBExceptionException.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

package it.kdm.docer.timbro.provider.intesi.stub;

public class HandlerQRBExceptionException extends Exception{

    private static final long serialVersionUID = 1502358379287L;

    private it.kdm.docer.timbro.provider.intesi.stub.HandlerStub.HandlerQRBException faultMessage;


        public HandlerQRBExceptionException() {
            super("HandlerQRBExceptionException");
        }

        public HandlerQRBExceptionException(String s) {
           super(s);
        }

        public HandlerQRBExceptionException(String s, Throwable ex) {
          super(s, ex);
        }

        public HandlerQRBExceptionException(Throwable cause) {
            super(cause);
        }


    public void setFaultMessage(it.kdm.docer.timbro.provider.intesi.stub.HandlerStub.HandlerQRBException msg){
       faultMessage = msg;
    }

    public it.kdm.docer.timbro.provider.intesi.stub.HandlerStub.HandlerQRBException getFaultMessage(){
       return faultMessage;
    }
}
    