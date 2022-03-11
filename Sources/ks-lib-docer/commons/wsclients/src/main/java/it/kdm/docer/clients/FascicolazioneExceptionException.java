
/**
 * FascicolazioneExceptionException.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

package it.kdm.docer.clients;

public class FascicolazioneExceptionException extends java.lang.Exception{

    private static final long serialVersionUID = 1401206316505L;
    
    private it.kdm.docer.clients.WSFascicolazioneStub.FascicolazioneException faultMessage;

    
        public FascicolazioneExceptionException() {
            super("FascicolazioneExceptionException");
        }

        public FascicolazioneExceptionException(java.lang.String s) {
           super(s);
        }

        public FascicolazioneExceptionException(java.lang.String s, java.lang.Throwable ex) {
          super(s, ex);
        }

        public FascicolazioneExceptionException(java.lang.Throwable cause) {
            super(cause);
        }
    

    public void setFaultMessage(it.kdm.docer.clients.WSFascicolazioneStub.FascicolazioneException msg){
       faultMessage = msg;
    }
    
    public it.kdm.docer.clients.WSFascicolazioneStub.FascicolazioneException getFaultMessage(){
       return faultMessage;
    }
}
    