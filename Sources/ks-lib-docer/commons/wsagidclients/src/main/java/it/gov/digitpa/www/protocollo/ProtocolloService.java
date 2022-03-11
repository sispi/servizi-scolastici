

/**
 * ProtocolloService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.5.6  Built on : Aug 30, 2011 (10:00:16 CEST)
 */

    package it.gov.digitpa.www.protocollo;

    /*
     *  ProtocolloService java interface
     */

    public interface ProtocolloService {
          

        /**
          * Auto generated method signature
          * 
                    * @param segnaturaEnvelope39
                
         */

         
                     public it.gov.digitpa.www.protocollo.EsitoConsegnaDocument consegna(

                        it.gov.digitpa.www.protocollo.SegnaturaEnvelopeDocument segnaturaEnvelope39)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param segnaturaEnvelope39
            
          */
        public void startconsegna(

            it.gov.digitpa.www.protocollo.SegnaturaEnvelopeDocument segnaturaEnvelope39,

            final it.gov.digitpa.www.protocollo.ProtocolloServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        
       //
       }
    