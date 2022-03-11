
/**
 * VerificaDocumentoExceptionException.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

package it.kdm.docer.clients;

public class VerificaDocumentoExceptionException extends java.lang.Exception{

    private static final long serialVersionUID = 1401269537595L;
    
    private it.kdm.docer.clients.WSVerificaDocumentiStub.VerificaDocumentoException faultMessage;

    
        public VerificaDocumentoExceptionException() {
            super("VerificaDocumentoExceptionException");
        }

        public VerificaDocumentoExceptionException(java.lang.String s) {
           super(s);
        }

        public VerificaDocumentoExceptionException(java.lang.String s, java.lang.Throwable ex) {
          super(s, ex);
        }

        public VerificaDocumentoExceptionException(java.lang.Throwable cause) {
            super(cause);
        }
    

    public void setFaultMessage(it.kdm.docer.clients.WSVerificaDocumentiStub.VerificaDocumentoException msg){
       faultMessage = msg;
    }
    
    public it.kdm.docer.clients.WSVerificaDocumentiStub.VerificaDocumentoException getFaultMessage(){
       return faultMessage;
    }
}
    