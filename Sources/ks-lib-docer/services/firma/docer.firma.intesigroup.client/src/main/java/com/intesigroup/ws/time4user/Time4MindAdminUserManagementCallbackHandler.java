
/**
 * Time4MindAdminUserManagementCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

    package com.intesigroup.ws.time4user;

    /**
     *  Time4MindAdminUserManagementCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class Time4MindAdminUserManagementCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public Time4MindAdminUserManagementCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public Time4MindAdminUserManagementCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for resendActivationEmail method
            * override this method for handling normal response from resendActivationEmail operation
            */
           public void receiveResultresendActivationEmail(
                    com.intesigroup.ws.time4user.Time4MindAdminUserManagementStub.ResendActivationEmailResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from resendActivationEmail operation
           */
            public void receiveErrorresendActivationEmail(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for sendEmailResetPassword method
            * override this method for handling normal response from sendEmailResetPassword operation
            */
           public void receiveResultsendEmailResetPassword(
                    com.intesigroup.ws.time4user.Time4MindAdminUserManagementStub.SendEmailResetPasswordResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from sendEmailResetPassword operation
           */
            public void receiveErrorsendEmailResetPassword(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getTransactionId method
            * override this method for handling normal response from getTransactionId operation
            */
           public void receiveResultgetTransactionId(
                    com.intesigroup.ws.time4user.Time4MindAdminUserManagementStub.GetTransactionIdResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getTransactionId operation
           */
            public void receiveErrorgetTransactionId(java.lang.Exception e) {
            }
                
               // No methods generated for meps other than in-out
                
           /**
            * auto generated Axis2 call back method for getServiceProfile method
            * override this method for handling normal response from getServiceProfile operation
            */
           public void receiveResultgetServiceProfile(
                    com.intesigroup.ws.time4user.Time4MindAdminUserManagementStub.GetServiceProfileResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getServiceProfile operation
           */
            public void receiveErrorgetServiceProfile(java.lang.Exception e) {
            }
                
               // No methods generated for meps other than in-out
                
               // No methods generated for meps other than in-out
                
               // No methods generated for meps other than in-out
                
           /**
            * auto generated Axis2 call back method for createAccount method
            * override this method for handling normal response from createAccount operation
            */
           public void receiveResultcreateAccount(
                    com.intesigroup.ws.time4user.Time4MindAdminUserManagementStub.CreateAccountResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from createAccount operation
           */
            public void receiveErrorcreateAccount(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for listDevices method
            * override this method for handling normal response from listDevices operation
            */
           public void receiveResultlistDevices(
                    com.intesigroup.ws.time4user.Time4MindAdminUserManagementStub.ListDevicesResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from listDevices operation
           */
            public void receiveErrorlistDevices(java.lang.Exception e) {
            }
                


    }
    