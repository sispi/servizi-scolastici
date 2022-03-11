
/**
 * Time4MindAdminUserManagementT4MExceptionException.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

package com.intesigroup.ws.time4user;

public class Time4MindAdminUserManagementT4MExceptionException extends java.lang.Exception{

    private static final long serialVersionUID = 1426719171537L;
    
    private com.intesigroup.ws.time4user.Time4MindAdminUserManagementStub.Time4MindAdminUserManagementT4MException faultMessage;

    
        public Time4MindAdminUserManagementT4MExceptionException() {
            super("Time4MindAdminUserManagementT4MExceptionException");
        }

        public Time4MindAdminUserManagementT4MExceptionException(java.lang.String s) {
           super(s);
        }

        public Time4MindAdminUserManagementT4MExceptionException(java.lang.String s, java.lang.Throwable ex) {
          super(s, ex);
        }

        public Time4MindAdminUserManagementT4MExceptionException(java.lang.Throwable cause) {
            super(cause);
        }
    

    public void setFaultMessage(com.intesigroup.ws.time4user.Time4MindAdminUserManagementStub.Time4MindAdminUserManagementT4MException msg){
       faultMessage = msg;
    }
    
    public com.intesigroup.ws.time4user.Time4MindAdminUserManagementStub.Time4MindAdminUserManagementT4MException getFaultMessage(){
       return faultMessage;
    }
}
    