
/**
 * WSConservazioneBatchConservazioneExceptionException.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

package it.kdm.docer.clients;

public class WSConservazioneBatchConservazioneExceptionException extends java.lang.Exception{

    private static final long serialVersionUID = 1434991742986L;
    
    private it.kdm.docer.clients.WSConservazioneBatchStub.WSConservazioneBatchConservazioneException faultMessage;

    
        public WSConservazioneBatchConservazioneExceptionException() {
            super("WSConservazioneBatchConservazioneExceptionException");
        }

        public WSConservazioneBatchConservazioneExceptionException(java.lang.String s) {
           super(s);
        }

        public WSConservazioneBatchConservazioneExceptionException(java.lang.String s, java.lang.Throwable ex) {
          super(s, ex);
        }

        public WSConservazioneBatchConservazioneExceptionException(java.lang.Throwable cause) {
            super(cause);
        }
    

    public void setFaultMessage(it.kdm.docer.clients.WSConservazioneBatchStub.WSConservazioneBatchConservazioneException msg){
       faultMessage = msg;
    }
    
    public it.kdm.docer.clients.WSConservazioneBatchStub.WSConservazioneBatchConservazioneException getFaultMessage(){
       return faultMessage;
    }
}
    