
/**
 * Time4EIDAuthenticationServiceCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

    package com.intesigroup.ws.time4eid;

    /**
     *  Time4EIDAuthenticationServiceCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class Time4EIDAuthenticationServiceCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public Time4EIDAuthenticationServiceCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public Time4EIDAuthenticationServiceCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for authenticate method
            * override this method for handling normal response from authenticate operation
            */
           public void receiveResultauthenticate(
                    com.intesigroup.ws.time4eid.Time4EIDAuthenticationServiceStub.AuthenticateResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from authenticate operation
           */
            public void receiveErrorauthenticate(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for syncToken method
            * override this method for handling normal response from syncToken operation
            */
           public void receiveResultsyncToken(
                    com.intesigroup.ws.time4eid.Time4EIDAuthenticationServiceStub.SyncTokenResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from syncToken operation
           */
            public void receiveErrorsyncToken(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for pushOTP method
            * override this method for handling normal response from pushOTP operation
            */
           public void receiveResultpushOTP(
                    com.intesigroup.ws.time4eid.Time4EIDAuthenticationServiceStub.PushOTPResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from pushOTP operation
           */
            public void receiveErrorpushOTP(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for activateToken method
            * override this method for handling normal response from activateToken operation
            */
           public void receiveResultactivateToken(
                    com.intesigroup.ws.time4eid.Time4EIDAuthenticationServiceStub.ActivateTokenResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from activateToken operation
           */
            public void receiveErroractivateToken(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for authenticateByUser method
            * override this method for handling normal response from authenticateByUser operation
            */
           public void receiveResultauthenticateByUser(
                    com.intesigroup.ws.time4eid.Time4EIDAuthenticationServiceStub.AuthenticateByUserResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from authenticateByUser operation
           */
            public void receiveErrorauthenticateByUser(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for generateChallenge method
            * override this method for handling normal response from generateChallenge operation
            */
           public void receiveResultgenerateChallenge(
                    com.intesigroup.ws.time4eid.Time4EIDAuthenticationServiceStub.GenerateChallengeResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from generateChallenge operation
           */
            public void receiveErrorgenerateChallenge(java.lang.Exception e) {
            }
                


    }
    