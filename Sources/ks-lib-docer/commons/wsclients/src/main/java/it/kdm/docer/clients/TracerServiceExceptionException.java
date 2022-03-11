
/**
 * TracerServiceExceptionException.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

package it.kdm.docer.clients;

public class TracerServiceExceptionException extends java.lang.Exception{

    private static final long serialVersionUID = 1437753482651L;
    
    private it.kdm.docer.clients.TracerServiceStub.TracerServiceException faultMessage;

    
        public TracerServiceExceptionException() {
            super("TracerServiceExceptionException");
        }

        public TracerServiceExceptionException(java.lang.String s) {
           super(s);
        }

        public TracerServiceExceptionException(java.lang.String s, java.lang.Throwable ex) {
          super(s, ex);
        }

        public TracerServiceExceptionException(java.lang.Throwable cause) {
            super(cause);
        }
    

    public void setFaultMessage(it.kdm.docer.clients.TracerServiceStub.TracerServiceException msg){
       faultMessage = msg;
    }
    
    public it.kdm.docer.clients.TracerServiceStub.TracerServiceException getFaultMessage(){
       return faultMessage;
    }
}
    