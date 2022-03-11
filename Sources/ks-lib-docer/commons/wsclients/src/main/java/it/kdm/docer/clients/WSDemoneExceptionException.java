
/**
 * WSDemoneExceptionException.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

package it.kdm.docer.clients;

public class WSDemoneExceptionException extends Exception{

    private static final long serialVersionUID = 1476970146916L;

    private WSDemoneStub.WSDemoneException faultMessage;


    public WSDemoneExceptionException() {
        super("WSDemoneExceptionException");
    }

    public WSDemoneExceptionException(String s) {
        super(s);
    }

    public WSDemoneExceptionException(String s, Throwable ex) {
        super(s, ex);
    }

    public WSDemoneExceptionException(Throwable cause) {
        super(cause);
    }


    public void setFaultMessage(WSDemoneStub.WSDemoneException msg){
        faultMessage = msg;
    }

    public WSDemoneStub.WSDemoneException getFaultMessage(){
        return faultMessage;
    }
}
