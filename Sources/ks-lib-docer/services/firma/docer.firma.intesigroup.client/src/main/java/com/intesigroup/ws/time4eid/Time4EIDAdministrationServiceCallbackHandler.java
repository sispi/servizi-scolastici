
/**
 * Time4EIDAdministrationServiceCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

    package com.intesigroup.ws.time4eid;

    /**
     *  Time4EIDAdministrationServiceCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class Time4EIDAdministrationServiceCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public Time4EIDAdministrationServiceCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public Time4EIDAdministrationServiceCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for deleteToken method
            * override this method for handling normal response from deleteToken operation
            */
           public void receiveResultdeleteToken(
                    com.intesigroup.ws.time4eid.Time4EIDAdministrationServiceStub.DeleteTokenResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from deleteToken operation
           */
            public void receiveErrordeleteToken(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for lockToken method
            * override this method for handling normal response from lockToken operation
            */
           public void receiveResultlockToken(
                    com.intesigroup.ws.time4eid.Time4EIDAdministrationServiceStub.LockTokenResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from lockToken operation
           */
            public void receiveErrorlockToken(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for resetPIN method
            * override this method for handling normal response from resetPIN operation
            */
           public void receiveResultresetPIN(
                    com.intesigroup.ws.time4eid.Time4EIDAdministrationServiceStub.ResetPINResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from resetPIN operation
           */
            public void receiveErrorresetPIN(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getQRCodeLegacy method
            * override this method for handling normal response from getQRCodeLegacy operation
            */
           public void receiveResultgetQRCodeLegacy(
                    com.intesigroup.ws.time4eid.Time4EIDAdministrationServiceStub.GetQRCodeLegacyResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getQRCodeLegacy operation
           */
            public void receiveErrorgetQRCodeLegacy(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for updateToken method
            * override this method for handling normal response from updateToken operation
            */
           public void receiveResultupdateToken(
                    com.intesigroup.ws.time4eid.Time4EIDAdministrationServiceStub.UpdateTokenResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from updateToken operation
           */
            public void receiveErrorupdateToken(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getSrvTokenInfo method
            * override this method for handling normal response from getSrvTokenInfo operation
            */
           public void receiveResultgetSrvTokenInfo(
                    com.intesigroup.ws.time4eid.Time4EIDAdministrationServiceStub.GetSrvTokenInfoResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getSrvTokenInfo operation
           */
            public void receiveErrorgetSrvTokenInfo(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for regenerateSrvToken method
            * override this method for handling normal response from regenerateSrvToken operation
            */
           public void receiveResultregenerateSrvToken(
                    com.intesigroup.ws.time4eid.Time4EIDAdministrationServiceStub.RegenerateSrvTokenResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from regenerateSrvToken operation
           */
            public void receiveErrorregenerateSrvToken(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for resetPINRequest method
            * override this method for handling normal response from resetPINRequest operation
            */
           public void receiveResultresetPINRequest(
                    com.intesigroup.ws.time4eid.Time4EIDAdministrationServiceStub.ResetPINRequestResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from resetPINRequest operation
           */
            public void receiveErrorresetPINRequest(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getQRCode method
            * override this method for handling normal response from getQRCode operation
            */
           public void receiveResultgetQRCode(
                    com.intesigroup.ws.time4eid.Time4EIDAdministrationServiceStub.GetQRCodeResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getQRCode operation
           */
            public void receiveErrorgetQRCode(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for deleteSrvToken method
            * override this method for handling normal response from deleteSrvToken operation
            */
           public void receiveResultdeleteSrvToken(
                    com.intesigroup.ws.time4eid.Time4EIDAdministrationServiceStub.DeleteSrvTokenResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from deleteSrvToken operation
           */
            public void receiveErrordeleteSrvToken(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for unlockToken method
            * override this method for handling normal response from unlockToken operation
            */
           public void receiveResultunlockToken(
                    com.intesigroup.ws.time4eid.Time4EIDAdministrationServiceStub.UnlockTokenResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from unlockToken operation
           */
            public void receiveErrorunlockToken(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getTokenInfo method
            * override this method for handling normal response from getTokenInfo operation
            */
           public void receiveResultgetTokenInfo(
                    com.intesigroup.ws.time4eid.Time4EIDAdministrationServiceStub.GetTokenInfoResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getTokenInfo operation
           */
            public void receiveErrorgetTokenInfo(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for listToken method
            * override this method for handling normal response from listToken operation
            */
           public void receiveResultlistToken(
                    com.intesigroup.ws.time4eid.Time4EIDAdministrationServiceStub.ListTokenResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from listToken operation
           */
            public void receiveErrorlistToken(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for initToken method
            * override this method for handling normal response from initToken operation
            */
           public void receiveResultinitToken(
                    com.intesigroup.ws.time4eid.Time4EIDAdministrationServiceStub.InitTokenResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from initToken operation
           */
            public void receiveErrorinitToken(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getSeed method
            * override this method for handling normal response from getSeed operation
            */
           public void receiveResultgetSeed(
                    com.intesigroup.ws.time4eid.Time4EIDAdministrationServiceStub.GetSeedResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getSeed operation
           */
            public void receiveErrorgetSeed(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for renewToken method
            * override this method for handling normal response from renewToken operation
            */
           public void receiveResultrenewToken(
                    com.intesigroup.ws.time4eid.Time4EIDAdministrationServiceStub.RenewTokenResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from renewToken operation
           */
            public void receiveErrorrenewToken(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for resetToken method
            * override this method for handling normal response from resetToken operation
            */
           public void receiveResultresetToken(
                    com.intesigroup.ws.time4eid.Time4EIDAdministrationServiceStub.ResetTokenResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from resetToken operation
           */
            public void receiveErrorresetToken(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for sendNotification method
            * override this method for handling normal response from sendNotification operation
            */
           public void receiveResultsendNotification(
                    com.intesigroup.ws.time4eid.Time4EIDAdministrationServiceStub.SendNotificationResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from sendNotification operation
           */
            public void receiveErrorsendNotification(java.lang.Exception e) {
            }
                


    }
    