
/**
 * ProtocolloRicezioneEsitiServiceCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.5.6  Built on : Aug 30, 2011 (10:00:16 CEST)
 */

    package it.gov.digitpa.www.protocollo;

    /**
     *  ProtocolloRicezioneEsitiServiceCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class ProtocolloRicezioneEsitiServiceCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public ProtocolloRicezioneEsitiServiceCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public ProtocolloRicezioneEsitiServiceCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for ricezioneEsiti method
            * override this method for handling normal response from ricezioneEsiti operation
            */
           public void receiveResultricezioneEsiti(
                    it.gov.digitpa.www.protocollo.EsitoConsegnaDocument result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from ricezioneEsiti operation
           */
            public void receiveErrorricezioneEsiti(java.lang.Exception e) {
            }
                


    }
    