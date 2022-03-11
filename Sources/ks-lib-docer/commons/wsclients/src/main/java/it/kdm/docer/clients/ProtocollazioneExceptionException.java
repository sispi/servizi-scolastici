
/**
 * ProtocollazioneExceptionException.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

package it.kdm.docer.clients;

public class ProtocollazioneExceptionException extends java.lang.Exception{

    private static final long serialVersionUID = 1401206492057L;
    
    private it.kdm.docer.clients.WSProtocollazioneStub.ProtocollazioneException faultMessage;

    
        public ProtocollazioneExceptionException() {
            super("ProtocollazioneExceptionException");
        }

        public ProtocollazioneExceptionException(java.lang.String s) {
           super(s);
        }

        public ProtocollazioneExceptionException(java.lang.String s, java.lang.Throwable ex) {
          super(s, ex);
        }

        public ProtocollazioneExceptionException(java.lang.Throwable cause) {
            super(cause);
        }
    

    public void setFaultMessage(it.kdm.docer.clients.WSProtocollazioneStub.ProtocollazioneException msg){
       faultMessage = msg;
    }
    
    public it.kdm.docer.clients.WSProtocollazioneStub.ProtocollazioneException getFaultMessage(){
       return faultMessage;
    }
}
    