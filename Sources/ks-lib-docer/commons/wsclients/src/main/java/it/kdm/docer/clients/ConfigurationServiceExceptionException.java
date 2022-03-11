
/**
 * ConfigurationServiceExceptionException.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

package it.kdm.docer.clients;

public class ConfigurationServiceExceptionException extends java.lang.Exception{

    private static final long serialVersionUID = 1418818084743L;
    
    private it.kdm.docer.clients.ConfigurationServiceStub.ConfigurationServiceException faultMessage;

    
        public ConfigurationServiceExceptionException() {
            super("ConfigurationServiceExceptionException");
        }

        public ConfigurationServiceExceptionException(java.lang.String s) {
           super(s);
        }

        public ConfigurationServiceExceptionException(java.lang.String s, java.lang.Throwable ex) {
          super(s, ex);
        }

        public ConfigurationServiceExceptionException(java.lang.Throwable cause) {
            super(cause);
        }
    

    public void setFaultMessage(it.kdm.docer.clients.ConfigurationServiceStub.ConfigurationServiceException msg){
       faultMessage = msg;
    }
    
    public it.kdm.docer.clients.ConfigurationServiceStub.ConfigurationServiceException getFaultMessage(){
       return faultMessage;
    }
}
    