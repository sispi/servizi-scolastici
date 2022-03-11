

/**
 * ProtocolloRicezioneEsitiService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.5.6  Built on : Aug 30, 2011 (10:00:16 CEST)
 */

    package it.gov.digitpa.www.protocollo;

    /*
     *  ProtocolloRicezioneEsitiService java interface
     */

    public interface ProtocolloRicezioneEsitiService {
          

        /**
          * Auto generated method signature
          * 
                    * @param notifica27
                
         */

         
                     public it.gov.digitpa.www.protocollo.EsitoConsegnaDocument ricezioneEsiti(

                        it.gov.digitpa.www.protocollo.NotificaDocument notifica27)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param notifica27
            
          */
        public void startricezioneEsiti(

            it.gov.digitpa.www.protocollo.NotificaDocument notifica27,

            final it.gov.digitpa.www.protocollo.ProtocolloRicezioneEsitiServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        
       //
       }
    