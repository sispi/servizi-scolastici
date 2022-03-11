
/**
 * WSConservazioneConservazioneExceptionException.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

package it.kdm.docer.clients;

public class WSConservazioneConservazioneExceptionException extends java.lang.Exception{

    private static final long serialVersionUID = 1434991377784L;
    
    private it.kdm.docer.clients.WSConservazioneStub.WSConservazioneConservazioneException faultMessage;

    
        public WSConservazioneConservazioneExceptionException() {
            super("WSConservazioneConservazioneExceptionException");
        }

        public WSConservazioneConservazioneExceptionException(java.lang.String s) {
           super(s);
        }

        public WSConservazioneConservazioneExceptionException(java.lang.String s, java.lang.Throwable ex) {
          super(s, ex);
        }

        public WSConservazioneConservazioneExceptionException(java.lang.Throwable cause) {
            super(cause);
        }
    

    public void setFaultMessage(it.kdm.docer.clients.WSConservazioneStub.WSConservazioneConservazioneException msg){
       faultMessage = msg;
    }
    
    public it.kdm.docer.clients.WSConservazioneStub.WSConservazioneConservazioneException getFaultMessage(){
       return faultMessage;
    }
}
    