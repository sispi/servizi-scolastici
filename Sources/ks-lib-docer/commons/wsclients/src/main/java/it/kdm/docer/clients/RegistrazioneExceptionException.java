
/**
 * RegistrazioneExceptionException.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

package it.kdm.docer.clients;

public class RegistrazioneExceptionException extends java.lang.Exception{

    private static final long serialVersionUID = 1401206768034L;
    
    private it.kdm.docer.clients.WSRegistrazioneStub.RegistrazioneException faultMessage;

    
        public RegistrazioneExceptionException() {
            super("RegistrazioneExceptionException");
        }

        public RegistrazioneExceptionException(java.lang.String s) {
           super(s);
        }

        public RegistrazioneExceptionException(java.lang.String s, java.lang.Throwable ex) {
          super(s, ex);
        }

        public RegistrazioneExceptionException(java.lang.Throwable cause) {
            super(cause);
        }
    

    public void setFaultMessage(it.kdm.docer.clients.WSRegistrazioneStub.RegistrazioneException msg){
       faultMessage = msg;
    }
    
    public it.kdm.docer.clients.WSRegistrazioneStub.RegistrazioneException getFaultMessage(){
       return faultMessage;
    }
}
    