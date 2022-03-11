
/**
 * ProtocollazioneServiceStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */
        package it.kdm.docer.protocollazione.wsclient;

        

        /*
        *  ProtocollazioneServiceStub java implementation
        */

        
        public class ProtocollazioneServiceStub extends org.apache.axis2.client.Stub
        {
        protected org.apache.axis2.description.AxisOperation[] _operations;

        //hashmaps to keep the fault mapping
        private java.util.HashMap faultExceptionNameMap = new java.util.HashMap();
        private java.util.HashMap faultExceptionClassNameMap = new java.util.HashMap();
        private java.util.HashMap faultMessageMap = new java.util.HashMap();

        private static int counter = 0;

        private static synchronized String getUniqueSuffix(){
            // reset the counter if it is greater than 99999
            if (counter > 99999){
                counter = 0;
            }
            counter = counter + 1; 
            return Long.toString(System.currentTimeMillis()) + "_" + counter;
        }


    private void populateAxisService() throws org.apache.axis2.AxisFault {

     //creating the Service with a unique name
     _service = new org.apache.axis2.description.AxisService("ProtocollazioneService" + getUniqueSuffix());
     addAnonymousOperations();

        //creating the operations
        org.apache.axis2.description.AxisOperation __operation;

        _operations = new org.apache.axis2.description.AxisOperation[4];

                   __operation = new org.apache.axis2.description.OutInAxisOperation();


            __operation.setName(new javax.xml.namespace.QName("http://ws.idsunitelm.it/", "login"));
	    _service.addOperation(__operation);




            _operations[0]=__operation;


                   __operation = new org.apache.axis2.description.OutInAxisOperation();


            __operation.setName(new javax.xml.namespace.QName("http://ws.idsunitelm.it/", "protocollazioneDocumento"));
	    _service.addOperation(__operation);




            _operations[1]=__operation;


                   __operation = new org.apache.axis2.description.OutInAxisOperation();


            __operation.setName(new javax.xml.namespace.QName("http://ws.idsunitelm.it/", "creazioneFascicolo"));
	    _service.addOperation(__operation);




            _operations[2]=__operation;


                   __operation = new org.apache.axis2.description.OutInAxisOperation();


            __operation.setName(new javax.xml.namespace.QName("http://ws.idsunitelm.it/", "recuperoDocumento"));
	    _service.addOperation(__operation);




            _operations[3]=__operation;


        }

    //populates the faults
    private void populateFaults(){



    }

    /**
      *Constructor that takes in a configContext
      */

    public ProtocollazioneServiceStub(org.apache.axis2.context.ConfigurationContext configurationContext,
                                      String targetEndpoint)
       throws org.apache.axis2.AxisFault {
         this(configurationContext,targetEndpoint,false);
   }


   /**
     * Constructor that takes in a configContext  and useseperate listner
     */
   public ProtocollazioneServiceStub(org.apache.axis2.context.ConfigurationContext configurationContext,
                                     String targetEndpoint, boolean useSeparateListener)
        throws org.apache.axis2.AxisFault {
         //To populate AxisService
         populateAxisService();
         populateFaults();

        _serviceClient = new org.apache.axis2.client.ServiceClient(configurationContext,_service);


        _serviceClient.getOptions().setTo(new org.apache.axis2.addressing.EndpointReference(
                targetEndpoint));
        _serviceClient.getOptions().setUseSeparateListener(useSeparateListener);


    }

    /**
     * Default Constructor
     */
    public ProtocollazioneServiceStub(org.apache.axis2.context.ConfigurationContext configurationContext) throws org.apache.axis2.AxisFault {

                    this(configurationContext,"http://TestAttiWeb.provincia.bologna.it:8080/WSArchiPRO/ProtocollazioneService/ProtocollazioneService" );

    }

    /**
     * Default Constructor
     */
    public ProtocollazioneServiceStub() throws org.apache.axis2.AxisFault {

                    this("http://TestAttiWeb.provincia.bologna.it:8080/WSArchiPRO/ProtocollazioneService/ProtocollazioneService" );

    }

    /**
     * Constructor taking the target endpoint
     */
    public ProtocollazioneServiceStub(String targetEndpoint) throws org.apache.axis2.AxisFault {
        this(null,targetEndpoint);
    }




                    /**
                     * Auto generated method signature
                     *
                     * @see it.kdm.docer.clients.ProtocollazioneService#login
                     * @param login

                     */



                            public  ProtocollazioneServiceStub.LoginResponseE login(

                            ProtocollazioneServiceStub.LoginE login)


                    throws java.rmi.RemoteException

                    {
              org.apache.axis2.context.MessageContext _messageContext = null;
              try{
               org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[0].getName());
              _operationClient.getOptions().setAction("http://ws.idsunitelm.it/IProtocollazioneService/login");
              _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);



                  addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");


              // create a message context
              _messageContext = new org.apache.axis2.context.MessageContext();



              // create SOAP envelope with that payload
              org.apache.axiom.soap.SOAPEnvelope env = null;


                                                    env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                                                    login,
                                                    optimizeContent(new javax.xml.namespace.QName("http://ws.idsunitelm.it/",
                                                    "login")), new javax.xml.namespace.QName("http://ws.idsunitelm.it/",
                                                    "login"));

        //adding SOAP soap_headers
         _serviceClient.addHeadersToEnvelope(env);
        // set the message context with that soap envelope
        _messageContext.setEnvelope(env);

        // add the message contxt to the operation client
        _operationClient.addMessageContext(_messageContext);

        //execute the operation client
        _operationClient.execute(true);


               org.apache.axis2.context.MessageContext _returnMessageContext = _operationClient.getMessageContext(
                                           org.apache.axis2.wsdl.WSDLConstants.MESSAGE_LABEL_IN_VALUE);
                org.apache.axiom.soap.SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();


                                Object object = fromOM(
                                             _returnEnv.getBody().getFirstElement() ,
                                             ProtocollazioneServiceStub.LoginResponseE.class,
                                              getEnvelopeNamespaces(_returnEnv));


                                        return (ProtocollazioneServiceStub.LoginResponseE)object;

         }catch(org.apache.axis2.AxisFault f){

            org.apache.axiom.om.OMElement faultElt = f.getDetail();
            if (faultElt!=null){
                if (faultExceptionNameMap.containsKey(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"login"))){
                    //make the fault by reflection
                    try{
                        String exceptionClassName = (String)faultExceptionClassNameMap.get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"login"));
                        Class exceptionClass = Class.forName(exceptionClassName);
                        java.lang.reflect.Constructor constructor = exceptionClass.getConstructor(String.class);
                        Exception ex = (Exception) constructor.newInstance(f.getMessage());
                        //message class
                        String messageClassName = (String)faultMessageMap.get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"login"));
                        Class messageClass = Class.forName(messageClassName);
                        Object messageObject = fromOM(faultElt,messageClass,null);
                        java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
                                   new Class[]{messageClass});
                        m.invoke(ex,new Object[]{messageObject});


                        throw new java.rmi.RemoteException(ex.getMessage(), ex);
                    }catch(ClassCastException e){
                       // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (ClassNotFoundException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }catch (NoSuchMethodException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.reflect.InvocationTargetException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }  catch (IllegalAccessException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }   catch (InstantiationException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }
                }else{
                    throw f;
                }
            }else{
                throw f;
            }
            } finally {
                if (_messageContext.getTransportOut() != null) {
                      _messageContext.getTransportOut().getSender().cleanup(_messageContext);
                }
            }
        }

                    /**
                     * Auto generated method signature
                     *
                     * @see it.kdm.docer.clients.ProtocollazioneService#protocollazioneDocumento
                     * @param protocollazioneDocumento

                     */



                            public  ProtocollazioneServiceStub.ProtocollazioneDocumentoResponseE protocollazioneDocumento(

                            ProtocollazioneServiceStub.ProtocollazioneDocumentoE protocollazioneDocumento)


                    throws java.rmi.RemoteException

                    {
              org.apache.axis2.context.MessageContext _messageContext = null;
              try{
               org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[1].getName());
              _operationClient.getOptions().setAction("http://ws.idsunitelm.it/IProtocollazioneService/protocollazioneDocumento");
              _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);



                  addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");


              // create a message context
              _messageContext = new org.apache.axis2.context.MessageContext();



              // create SOAP envelope with that payload
              org.apache.axiom.soap.SOAPEnvelope env = null;


                                                    env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                                                    protocollazioneDocumento,
                                                    optimizeContent(new javax.xml.namespace.QName("http://ws.idsunitelm.it/",
                                                    "protocollazioneDocumento")), new javax.xml.namespace.QName("http://ws.idsunitelm.it/",
                                                    "protocollazioneDocumento"));

        //adding SOAP soap_headers
         _serviceClient.addHeadersToEnvelope(env);
        // set the message context with that soap envelope
        _messageContext.setEnvelope(env);

        // add the message contxt to the operation client
        _operationClient.addMessageContext(_messageContext);

        //execute the operation client
        _operationClient.execute(true);


               org.apache.axis2.context.MessageContext _returnMessageContext = _operationClient.getMessageContext(
                                           org.apache.axis2.wsdl.WSDLConstants.MESSAGE_LABEL_IN_VALUE);
                org.apache.axiom.soap.SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();


                                Object object = fromOM(
                                             _returnEnv.getBody().getFirstElement() ,
                                             ProtocollazioneServiceStub.ProtocollazioneDocumentoResponseE.class,
                                              getEnvelopeNamespaces(_returnEnv));


                                        return (ProtocollazioneServiceStub.ProtocollazioneDocumentoResponseE)object;

         }catch(org.apache.axis2.AxisFault f){

            org.apache.axiom.om.OMElement faultElt = f.getDetail();
            if (faultElt!=null){
                if (faultExceptionNameMap.containsKey(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"protocollazioneDocumento"))){
                    //make the fault by reflection
                    try{
                        String exceptionClassName = (String)faultExceptionClassNameMap.get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"protocollazioneDocumento"));
                        Class exceptionClass = Class.forName(exceptionClassName);
                        java.lang.reflect.Constructor constructor = exceptionClass.getConstructor(String.class);
                        Exception ex = (Exception) constructor.newInstance(f.getMessage());
                        //message class
                        String messageClassName = (String)faultMessageMap.get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"protocollazioneDocumento"));
                        Class messageClass = Class.forName(messageClassName);
                        Object messageObject = fromOM(faultElt,messageClass,null);
                        java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
                                   new Class[]{messageClass});
                        m.invoke(ex,new Object[]{messageObject});


                        throw new java.rmi.RemoteException(ex.getMessage(), ex);
                    }catch(ClassCastException e){
                       // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (ClassNotFoundException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }catch (NoSuchMethodException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.reflect.InvocationTargetException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }  catch (IllegalAccessException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }   catch (InstantiationException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }
                }else{
                    throw f;
                }
            }else{
                throw f;
            }
            } finally {
                if (_messageContext.getTransportOut() != null) {
                      _messageContext.getTransportOut().getSender().cleanup(_messageContext);
                }
            }
        }

                    /**
                     * Auto generated method signature
                     *
                     * @see it.kdm.docer.clients.ProtocollazioneService#creazioneFascicolo
                     * @param creazioneFascicolo

                     */



                            public  ProtocollazioneServiceStub.CreazioneFascicoloResponseE creazioneFascicolo(

                            ProtocollazioneServiceStub.CreazioneFascicoloE creazioneFascicolo)


                    throws java.rmi.RemoteException

                    {
              org.apache.axis2.context.MessageContext _messageContext = null;
              try{
               org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[2].getName());
              _operationClient.getOptions().setAction("http://ws.idsunitelm.it/IProtocollazioneService/creazioneFascicolo");
              _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);



                  addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");


              // create a message context
              _messageContext = new org.apache.axis2.context.MessageContext();



              // create SOAP envelope with that payload
              org.apache.axiom.soap.SOAPEnvelope env = null;


                                                    env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                                                    creazioneFascicolo,
                                                    optimizeContent(new javax.xml.namespace.QName("http://ws.idsunitelm.it/",
                                                    "creazioneFascicolo")), new javax.xml.namespace.QName("http://ws.idsunitelm.it/",
                                                    "creazioneFascicolo"));

        //adding SOAP soap_headers
         _serviceClient.addHeadersToEnvelope(env);
        // set the message context with that soap envelope
        _messageContext.setEnvelope(env);

        // add the message contxt to the operation client
        _operationClient.addMessageContext(_messageContext);

        //execute the operation client
        _operationClient.execute(true);


               org.apache.axis2.context.MessageContext _returnMessageContext = _operationClient.getMessageContext(
                                           org.apache.axis2.wsdl.WSDLConstants.MESSAGE_LABEL_IN_VALUE);
                org.apache.axiom.soap.SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();


                                Object object = fromOM(
                                             _returnEnv.getBody().getFirstElement() ,
                                             ProtocollazioneServiceStub.CreazioneFascicoloResponseE.class,
                                              getEnvelopeNamespaces(_returnEnv));


                                        return (ProtocollazioneServiceStub.CreazioneFascicoloResponseE)object;

         }catch(org.apache.axis2.AxisFault f){

            org.apache.axiom.om.OMElement faultElt = f.getDetail();
            if (faultElt!=null){
                if (faultExceptionNameMap.containsKey(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"creazioneFascicolo"))){
                    //make the fault by reflection
                    try{
                        String exceptionClassName = (String)faultExceptionClassNameMap.get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"creazioneFascicolo"));
                        Class exceptionClass = Class.forName(exceptionClassName);
                        java.lang.reflect.Constructor constructor = exceptionClass.getConstructor(String.class);
                        Exception ex = (Exception) constructor.newInstance(f.getMessage());
                        //message class
                        String messageClassName = (String)faultMessageMap.get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"creazioneFascicolo"));
                        Class messageClass = Class.forName(messageClassName);
                        Object messageObject = fromOM(faultElt,messageClass,null);
                        java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
                                   new Class[]{messageClass});
                        m.invoke(ex,new Object[]{messageObject});


                        throw new java.rmi.RemoteException(ex.getMessage(), ex);
                    }catch(ClassCastException e){
                       // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (ClassNotFoundException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }catch (NoSuchMethodException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.reflect.InvocationTargetException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }  catch (IllegalAccessException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }   catch (InstantiationException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }
                }else{
                    throw f;
                }
            }else{
                throw f;
            }
            } finally {
                if (_messageContext.getTransportOut() != null) {
                      _messageContext.getTransportOut().getSender().cleanup(_messageContext);
                }
            }
        }

                    /**
                     * Auto generated method signature
                     *
                     * @see it.kdm.docer.clients.ProtocollazioneService#recuperoDocumento
                     * @param recuperoDocumento

                     */



                            public  ProtocollazioneServiceStub.RecuperoDocumentoResponseE recuperoDocumento(

                            ProtocollazioneServiceStub.RecuperoDocumentoE recuperoDocumento)


                    throws java.rmi.RemoteException

                    {
              org.apache.axis2.context.MessageContext _messageContext = null;
              try{
               org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[3].getName());
              _operationClient.getOptions().setAction("http://ws.idsunitelm.it/IProtocollazioneService/recuperoDocumento");
              _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);



                  addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");


              // create a message context
              _messageContext = new org.apache.axis2.context.MessageContext();



              // create SOAP envelope with that payload
              org.apache.axiom.soap.SOAPEnvelope env = null;


                                                    env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                                                    recuperoDocumento,
                                                    optimizeContent(new javax.xml.namespace.QName("http://ws.idsunitelm.it/",
                                                    "recuperoDocumento")), new javax.xml.namespace.QName("http://ws.idsunitelm.it/",
                                                    "recuperoDocumento"));

        //adding SOAP soap_headers
         _serviceClient.addHeadersToEnvelope(env);
        // set the message context with that soap envelope
        _messageContext.setEnvelope(env);

        // add the message contxt to the operation client
        _operationClient.addMessageContext(_messageContext);

        //execute the operation client
        _operationClient.execute(true);


               org.apache.axis2.context.MessageContext _returnMessageContext = _operationClient.getMessageContext(
                                           org.apache.axis2.wsdl.WSDLConstants.MESSAGE_LABEL_IN_VALUE);
                org.apache.axiom.soap.SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();


                                Object object = fromOM(
                                             _returnEnv.getBody().getFirstElement() ,
                                             ProtocollazioneServiceStub.RecuperoDocumentoResponseE.class,
                                              getEnvelopeNamespaces(_returnEnv));


                                        return (ProtocollazioneServiceStub.RecuperoDocumentoResponseE)object;

         }catch(org.apache.axis2.AxisFault f){

            org.apache.axiom.om.OMElement faultElt = f.getDetail();
            if (faultElt!=null){
                if (faultExceptionNameMap.containsKey(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"recuperoDocumento"))){
                    //make the fault by reflection
                    try{
                        String exceptionClassName = (String)faultExceptionClassNameMap.get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"recuperoDocumento"));
                        Class exceptionClass = Class.forName(exceptionClassName);
                        java.lang.reflect.Constructor constructor = exceptionClass.getConstructor(String.class);
                        Exception ex = (Exception) constructor.newInstance(f.getMessage());
                        //message class
                        String messageClassName = (String)faultMessageMap.get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"recuperoDocumento"));
                        Class messageClass = Class.forName(messageClassName);
                        Object messageObject = fromOM(faultElt,messageClass,null);
                        java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
                                   new Class[]{messageClass});
                        m.invoke(ex,new Object[]{messageObject});


                        throw new java.rmi.RemoteException(ex.getMessage(), ex);
                    }catch(ClassCastException e){
                       // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (ClassNotFoundException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }catch (NoSuchMethodException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.reflect.InvocationTargetException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }  catch (IllegalAccessException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }   catch (InstantiationException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }
                }else{
                    throw f;
                }
            }else{
                throw f;
            }
            } finally {
                if (_messageContext.getTransportOut() != null) {
                      _messageContext.getTransportOut().getSender().cleanup(_messageContext);
                }
            }
        }



       /**
        *  A utility method that copies the namepaces from the SOAPEnvelope
        */
       private java.util.Map getEnvelopeNamespaces(org.apache.axiom.soap.SOAPEnvelope env){
        java.util.Map returnMap = new java.util.HashMap();
        java.util.Iterator namespaceIterator = env.getAllDeclaredNamespaces();
        while (namespaceIterator.hasNext()) {
            org.apache.axiom.om.OMNamespace ns = (org.apache.axiom.om.OMNamespace) namespaceIterator.next();
            returnMap.put(ns.getPrefix(),ns.getNamespaceURI());
        }
       return returnMap;
    }



    private javax.xml.namespace.QName[] opNameArray = null;
    private boolean optimizeContent(javax.xml.namespace.QName opName) {


        if (opNameArray == null) {
            return false;
        }
        for (int i = 0; i < opNameArray.length; i++) {
            if (opName.equals(opNameArray[i])) {
                return true;
            }
        }
        return false;
    }
     //http://TestAttiWeb.provincia.bologna.it:8080/WSArchiPRO/ProtocollazioneService/ProtocollazioneService
        public static class CreazioneFascicoloResponse
        implements org.apache.axis2.databinding.ADBBean{
        /* This type was generated from the piece of schema that had
                name = creazioneFascicoloResponse
                Namespace URI = http://ws.idsunitelm.it/
                Namespace Prefix = ns1
                */


                        /**
                        * field for _return
                        */


                                    protected FascicoloResult local_return ;

                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean local_returnTracker = false ;

                           public boolean is_returnSpecified(){
                               return local_returnTracker;
                           }



                           /**
                           * Auto generated getter method
                           * @return FascicoloResult
                           */
                           public  FascicoloResult get_return(){
                               return local_return;
                           }



                            /**
                               * Auto generated setter method
                               * @param param _return
                               */
                               public void set_return(FascicoloResult param){
                            local_returnTracker = param != null;

                                            this.local_return=param;


                               }




        /**
        *
        * @param parentQName
        * @param factory
        * @return org.apache.axiom.om.OMElement
        */
       public org.apache.axiom.om.OMElement getOMElement (
               final javax.xml.namespace.QName parentQName,
               final org.apache.axiom.om.OMFactory factory) throws org.apache.axis2.databinding.ADBException{



               org.apache.axiom.om.OMDataSource dataSource =
                       new org.apache.axis2.databinding.ADBDataSource(this,parentQName);
               return factory.createOMElement(dataSource,parentQName);

        }

         public void serialize(final javax.xml.namespace.QName parentQName,
                                       javax.xml.stream.XMLStreamWriter xmlWriter)
                                throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{
                           serialize(parentQName,xmlWriter,false);
         }

         public void serialize(final javax.xml.namespace.QName parentQName,
                               javax.xml.stream.XMLStreamWriter xmlWriter,
                               boolean serializeType)
            throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{




                String prefix = null;
                String namespace = null;


                    prefix = parentQName.getPrefix();
                    namespace = parentQName.getNamespaceURI();
                    writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

                  if (serializeType){


                   String namespacePrefix = registerPrefix(xmlWriter,"http://ws.idsunitelm.it/");
                   if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                       writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                           namespacePrefix+":creazioneFascicoloResponse",
                           xmlWriter);
                   } else {
                       writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                           "creazioneFascicoloResponse",
                           xmlWriter);
                   }


                   }
                if (local_returnTracker){
                                            if (local_return==null){
                                                 throw new org.apache.axis2.databinding.ADBException("return cannot be null!!");
                                            }
                                           local_return.serialize(new javax.xml.namespace.QName("","return"),
                                               xmlWriter);
                                        }
                    xmlWriter.writeEndElement();


        }

        private static String generatePrefix(String namespace) {
            if(namespace.equals("http://ws.idsunitelm.it/")){
                return "ns1";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
         * Utility method to write an element start tag.
         */
        private void writeStartElement(String prefix, String namespace, String localPart,
                                       javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            String writerPrefix = xmlWriter.getPrefix(namespace);
            if (writerPrefix != null) {
                xmlWriter.writeStartElement(namespace, localPart);
            } else {
                if (namespace.length() == 0) {
                    prefix = "";
                } else if (prefix == null) {
                    prefix = generatePrefix(namespace);
                }

                xmlWriter.writeStartElement(prefix, localPart, namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
        }

        /**
         * Util method to write an attribute with the ns prefix
         */
        private void writeAttribute(String prefix,String namespace,String attName,
                                    String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (xmlWriter.getPrefix(namespace) == null) {
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            xmlWriter.writeAttribute(namespace,attName,attValue);
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeAttribute(String namespace,String attName,
                                    String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (namespace.equals("")) {
                xmlWriter.writeAttribute(attName,attValue);
            } else {
                registerPrefix(xmlWriter, namespace);
                xmlWriter.writeAttribute(namespace,attName,attValue);
            }
        }


           /**
             * Util method to write an attribute without the ns prefix
             */
            private void writeQNameAttribute(String namespace, String attName,
                                             javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

                String attributeNamespace = qname.getNamespaceURI();
                String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
                if (attributePrefix == null) {
                    attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
                }
                String attributeValue;
                if (attributePrefix.trim().length() > 0) {
                    attributeValue = attributePrefix + ":" + qname.getLocalPart();
                } else {
                    attributeValue = qname.getLocalPart();
                }

                if (namespace.equals("")) {
                    xmlWriter.writeAttribute(attName, attributeValue);
                } else {
                    registerPrefix(xmlWriter, namespace);
                    xmlWriter.writeAttribute(namespace, attName, attributeValue);
                }
            }
        /**
         *  method to handle Qnames
         */

        private void writeQName(javax.xml.namespace.QName qname,
                                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            String namespaceURI = qname.getNamespaceURI();
            if (namespaceURI != null) {
                String prefix = xmlWriter.getPrefix(namespaceURI);
                if (prefix == null) {
                    prefix = generatePrefix(namespaceURI);
                    xmlWriter.writeNamespace(prefix, namespaceURI);
                    xmlWriter.setPrefix(prefix,namespaceURI);
                }

                if (prefix.trim().length() > 0){
                    xmlWriter.writeCharacters(prefix + ":" + org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                } else {
                    // i.e this is the default namespace
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                }

            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
            }
        }

        private void writeQNames(javax.xml.namespace.QName[] qnames,
                                 javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            if (qnames != null) {
                // we have to store this data until last moment since it is not possible to write any
                // namespace data after writing the charactor data
                StringBuffer stringToWrite = new StringBuffer();
                String namespaceURI = null;
                String prefix = null;

                for (int i = 0; i < qnames.length; i++) {
                    if (i > 0) {
                        stringToWrite.append(" ");
                    }
                    namespaceURI = qnames[i].getNamespaceURI();
                    if (namespaceURI != null) {
                        prefix = xmlWriter.getPrefix(namespaceURI);
                        if ((prefix == null) || (prefix.length() == 0)) {
                            prefix = generatePrefix(namespaceURI);
                            xmlWriter.writeNamespace(prefix, namespaceURI);
                            xmlWriter.setPrefix(prefix,namespaceURI);
                        }

                        if (prefix.trim().length() > 0){
                            stringToWrite.append(prefix).append(":").append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        } else {
                            stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        }
                    } else {
                        stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                    }
                }
                xmlWriter.writeCharacters(stringToWrite.toString());
            }

        }


        /**
         * Register a namespace prefix
         */
        private String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, String namespace) throws javax.xml.stream.XMLStreamException {
            String prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null) {
                prefix = generatePrefix(namespace);
                javax.xml.namespace.NamespaceContext nsContext = xmlWriter.getNamespaceContext();
                while (true) {
                    String uri = nsContext.getNamespaceURI(prefix);
                    if (uri == null || uri.length() == 0) {
                        break;
                    }
                    prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
                }
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            return prefix;
        }



        /**
        * databinding method to get an XML representation of this object
        *
        */
        public javax.xml.stream.XMLStreamReader getPullParser(javax.xml.namespace.QName qName)
                    throws org.apache.axis2.databinding.ADBException{



                 java.util.ArrayList elementList = new java.util.ArrayList();
                 java.util.ArrayList attribList = new java.util.ArrayList();

                 if (local_returnTracker){
                            elementList.add(new javax.xml.namespace.QName("",
                                                                      "return"));


                                    if (local_return==null){
                                         throw new org.apache.axis2.databinding.ADBException("return cannot be null!!");
                                    }
                                    elementList.add(local_return);
                                }

                return new org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl(qName, elementList.toArray(), attribList.toArray());



        }



     /**
      *  Factory class that keeps the parse method
      */
    public static class Factory{




        /**
        * static method to create the object
        * Precondition:  If this object is an element, the current or next start element starts this object and any intervening reader events are ignorable
        *                If this object is not an element, it is a complex type and the reader is at the event just after the outer start element
        * Postcondition: If this object is an element, the reader is positioned at its end element
        *                If this object is a complex type, the reader is positioned at the end element of its outer element
        */
        public static CreazioneFascicoloResponse parse(javax.xml.stream.XMLStreamReader reader) throws Exception{
            CreazioneFascicoloResponse object =
                new CreazioneFascicoloResponse();

            int event;
            String nillableValue = null;
            String prefix ="";
            String namespaceuri ="";
            try {

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();


                if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","type")!=null){
                  String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                        "type");
                  if (fullTypeName!=null){
                    String nsPrefix = null;
                    if (fullTypeName.indexOf(":") > -1){
                        nsPrefix = fullTypeName.substring(0,fullTypeName.indexOf(":"));
                    }
                    nsPrefix = nsPrefix==null?"":nsPrefix;

                    String type = fullTypeName.substring(fullTypeName.indexOf(":")+1);

                            if (!"creazioneFascicoloResponse".equals(type)){
                                //find namespace for the prefix
                                String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (CreazioneFascicoloResponse) ExtensionMapper.getTypeObject(
                                        nsUri, type, reader);
                              }


                  }


                }




                // Note all attributes that were handled. Used to differ normal attributes
                // from anyAttributes.
                java.util.Vector handledAttributes = new java.util.Vector();




                    reader.next();


                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                                    if (reader.isStartElement() && new javax.xml.namespace.QName("","return").equals(reader.getName())){

                                                object.set_return(FascicoloResult.Factory.parse(reader));

                                        reader.next();

                              }  // End of if for expected property start element

                                    else {

                                    }

                            while (!reader.isStartElement() && !reader.isEndElement())
                                reader.next();

                                if (reader.isStartElement())
                                // A start element we are not expecting indicates a trailing invalid property
                                throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());




            } catch (javax.xml.stream.XMLStreamException e) {
                throw new Exception(e);
            }

            return object;
        }

        }//end of factory class



        }


        public static class RecuperoDocumentoE
        implements org.apache.axis2.databinding.ADBBean{

                public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName(
                "http://ws.idsunitelm.it/",
                "recuperoDocumento",
                "ns1");



                        /**
                        * field for RecuperoDocumento
                        */


                                    protected RecuperoDocumento localRecuperoDocumento ;


                           /**
                           * Auto generated getter method
                           * @return RecuperoDocumento
                           */
                           public  RecuperoDocumento getRecuperoDocumento(){
                               return localRecuperoDocumento;
                           }



                            /**
                               * Auto generated setter method
                               * @param param RecuperoDocumento
                               */
                               public void setRecuperoDocumento(RecuperoDocumento param){

                                            this.localRecuperoDocumento=param;


                               }




        /**
        *
        * @param parentQName
        * @param factory
        * @return org.apache.axiom.om.OMElement
        */
       public org.apache.axiom.om.OMElement getOMElement (
               final javax.xml.namespace.QName parentQName,
               final org.apache.axiom.om.OMFactory factory) throws org.apache.axis2.databinding.ADBException{



               org.apache.axiom.om.OMDataSource dataSource =
                       new org.apache.axis2.databinding.ADBDataSource(this,MY_QNAME);
               return factory.createOMElement(dataSource,MY_QNAME);

        }

         public void serialize(final javax.xml.namespace.QName parentQName,
                                       javax.xml.stream.XMLStreamWriter xmlWriter)
                                throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{
                           serialize(parentQName,xmlWriter,false);
         }

         public void serialize(final javax.xml.namespace.QName parentQName,
                               javax.xml.stream.XMLStreamWriter xmlWriter,
                               boolean serializeType)
            throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{


                //We can safely assume an element has only one type associated with it

                                 if (localRecuperoDocumento==null){
                                   throw new org.apache.axis2.databinding.ADBException("recuperoDocumento cannot be null!");
                                 }
                                 localRecuperoDocumento.serialize(MY_QNAME,xmlWriter);


        }

        private static String generatePrefix(String namespace) {
            if(namespace.equals("http://ws.idsunitelm.it/")){
                return "ns1";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
         * Utility method to write an element start tag.
         */
        private void writeStartElement(String prefix, String namespace, String localPart,
                                       javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            String writerPrefix = xmlWriter.getPrefix(namespace);
            if (writerPrefix != null) {
                xmlWriter.writeStartElement(namespace, localPart);
            } else {
                if (namespace.length() == 0) {
                    prefix = "";
                } else if (prefix == null) {
                    prefix = generatePrefix(namespace);
                }

                xmlWriter.writeStartElement(prefix, localPart, namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
        }

        /**
         * Util method to write an attribute with the ns prefix
         */
        private void writeAttribute(String prefix,String namespace,String attName,
                                    String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (xmlWriter.getPrefix(namespace) == null) {
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            xmlWriter.writeAttribute(namespace,attName,attValue);
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeAttribute(String namespace,String attName,
                                    String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (namespace.equals("")) {
                xmlWriter.writeAttribute(attName,attValue);
            } else {
                registerPrefix(xmlWriter, namespace);
                xmlWriter.writeAttribute(namespace,attName,attValue);
            }
        }


           /**
             * Util method to write an attribute without the ns prefix
             */
            private void writeQNameAttribute(String namespace, String attName,
                                             javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

                String attributeNamespace = qname.getNamespaceURI();
                String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
                if (attributePrefix == null) {
                    attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
                }
                String attributeValue;
                if (attributePrefix.trim().length() > 0) {
                    attributeValue = attributePrefix + ":" + qname.getLocalPart();
                } else {
                    attributeValue = qname.getLocalPart();
                }

                if (namespace.equals("")) {
                    xmlWriter.writeAttribute(attName, attributeValue);
                } else {
                    registerPrefix(xmlWriter, namespace);
                    xmlWriter.writeAttribute(namespace, attName, attributeValue);
                }
            }
        /**
         *  method to handle Qnames
         */

        private void writeQName(javax.xml.namespace.QName qname,
                                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            String namespaceURI = qname.getNamespaceURI();
            if (namespaceURI != null) {
                String prefix = xmlWriter.getPrefix(namespaceURI);
                if (prefix == null) {
                    prefix = generatePrefix(namespaceURI);
                    xmlWriter.writeNamespace(prefix, namespaceURI);
                    xmlWriter.setPrefix(prefix,namespaceURI);
                }

                if (prefix.trim().length() > 0){
                    xmlWriter.writeCharacters(prefix + ":" + org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                } else {
                    // i.e this is the default namespace
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                }

            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
            }
        }

        private void writeQNames(javax.xml.namespace.QName[] qnames,
                                 javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            if (qnames != null) {
                // we have to store this data until last moment since it is not possible to write any
                // namespace data after writing the charactor data
                StringBuffer stringToWrite = new StringBuffer();
                String namespaceURI = null;
                String prefix = null;

                for (int i = 0; i < qnames.length; i++) {
                    if (i > 0) {
                        stringToWrite.append(" ");
                    }
                    namespaceURI = qnames[i].getNamespaceURI();
                    if (namespaceURI != null) {
                        prefix = xmlWriter.getPrefix(namespaceURI);
                        if ((prefix == null) || (prefix.length() == 0)) {
                            prefix = generatePrefix(namespaceURI);
                            xmlWriter.writeNamespace(prefix, namespaceURI);
                            xmlWriter.setPrefix(prefix,namespaceURI);
                        }

                        if (prefix.trim().length() > 0){
                            stringToWrite.append(prefix).append(":").append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        } else {
                            stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        }
                    } else {
                        stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                    }
                }
                xmlWriter.writeCharacters(stringToWrite.toString());
            }

        }


        /**
         * Register a namespace prefix
         */
        private String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, String namespace) throws javax.xml.stream.XMLStreamException {
            String prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null) {
                prefix = generatePrefix(namespace);
                javax.xml.namespace.NamespaceContext nsContext = xmlWriter.getNamespaceContext();
                while (true) {
                    String uri = nsContext.getNamespaceURI(prefix);
                    if (uri == null || uri.length() == 0) {
                        break;
                    }
                    prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
                }
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            return prefix;
        }



        /**
        * databinding method to get an XML representation of this object
        *
        */
        public javax.xml.stream.XMLStreamReader getPullParser(javax.xml.namespace.QName qName)
                    throws org.apache.axis2.databinding.ADBException{




                //We can safely assume an element has only one type associated with it
                return localRecuperoDocumento.getPullParser(MY_QNAME);

        }



     /**
      *  Factory class that keeps the parse method
      */
    public static class Factory{




        /**
        * static method to create the object
        * Precondition:  If this object is an element, the current or next start element starts this object and any intervening reader events are ignorable
        *                If this object is not an element, it is a complex type and the reader is at the event just after the outer start element
        * Postcondition: If this object is an element, the reader is positioned at its end element
        *                If this object is a complex type, the reader is positioned at the end element of its outer element
        */
        public static RecuperoDocumentoE parse(javax.xml.stream.XMLStreamReader reader) throws Exception{
            RecuperoDocumentoE object =
                new RecuperoDocumentoE();

            int event;
            String nillableValue = null;
            String prefix ="";
            String namespaceuri ="";
            try {

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();




                // Note all attributes that were handled. Used to differ normal attributes
                // from anyAttributes.
                java.util.Vector handledAttributes = new java.util.Vector();



                while(!reader.isEndElement()) {
                    if (reader.isStartElement() ){

                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://ws.idsunitelm.it/","recuperoDocumento").equals(reader.getName())){

                                                object.setRecuperoDocumento(RecuperoDocumento.Factory.parse(reader));

                              }  // End of if for expected property start element

                             else{
                                        // A start element we are not expecting indicates an invalid parameter was passed
                                        throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                             }

                             } else {
                                reader.next();
                             }
                           }  // end of while loop




            } catch (javax.xml.stream.XMLStreamException e) {
                throw new Exception(e);
            }

            return object;
        }

        }//end of factory class



        }


        public static class LoginResponseE
        implements org.apache.axis2.databinding.ADBBean{

                public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName(
                "http://ws.idsunitelm.it/",
                "loginResponse",
                "ns1");



                        /**
                        * field for LoginResponse
                        */


                                    protected LoginResponse localLoginResponse ;


                           /**
                           * Auto generated getter method
                           * @return LoginResponse
                           */
                           public  LoginResponse getLoginResponse(){
                               return localLoginResponse;
                           }



                            /**
                               * Auto generated setter method
                               * @param param LoginResponse
                               */
                               public void setLoginResponse(LoginResponse param){

                                            this.localLoginResponse=param;


                               }




        /**
        *
        * @param parentQName
        * @param factory
        * @return org.apache.axiom.om.OMElement
        */
       public org.apache.axiom.om.OMElement getOMElement (
               final javax.xml.namespace.QName parentQName,
               final org.apache.axiom.om.OMFactory factory) throws org.apache.axis2.databinding.ADBException{



               org.apache.axiom.om.OMDataSource dataSource =
                       new org.apache.axis2.databinding.ADBDataSource(this,MY_QNAME);
               return factory.createOMElement(dataSource,MY_QNAME);

        }

         public void serialize(final javax.xml.namespace.QName parentQName,
                                       javax.xml.stream.XMLStreamWriter xmlWriter)
                                throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{
                           serialize(parentQName,xmlWriter,false);
         }

         public void serialize(final javax.xml.namespace.QName parentQName,
                               javax.xml.stream.XMLStreamWriter xmlWriter,
                               boolean serializeType)
            throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{


                //We can safely assume an element has only one type associated with it

                                 if (localLoginResponse==null){
                                   throw new org.apache.axis2.databinding.ADBException("loginResponse cannot be null!");
                                 }
                                 localLoginResponse.serialize(MY_QNAME,xmlWriter);


        }

        private static String generatePrefix(String namespace) {
            if(namespace.equals("http://ws.idsunitelm.it/")){
                return "ns1";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
         * Utility method to write an element start tag.
         */
        private void writeStartElement(String prefix, String namespace, String localPart,
                                       javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            String writerPrefix = xmlWriter.getPrefix(namespace);
            if (writerPrefix != null) {
                xmlWriter.writeStartElement(namespace, localPart);
            } else {
                if (namespace.length() == 0) {
                    prefix = "";
                } else if (prefix == null) {
                    prefix = generatePrefix(namespace);
                }

                xmlWriter.writeStartElement(prefix, localPart, namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
        }

        /**
         * Util method to write an attribute with the ns prefix
         */
        private void writeAttribute(String prefix,String namespace,String attName,
                                    String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (xmlWriter.getPrefix(namespace) == null) {
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            xmlWriter.writeAttribute(namespace,attName,attValue);
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeAttribute(String namespace,String attName,
                                    String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (namespace.equals("")) {
                xmlWriter.writeAttribute(attName,attValue);
            } else {
                registerPrefix(xmlWriter, namespace);
                xmlWriter.writeAttribute(namespace,attName,attValue);
            }
        }


           /**
             * Util method to write an attribute without the ns prefix
             */
            private void writeQNameAttribute(String namespace, String attName,
                                             javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

                String attributeNamespace = qname.getNamespaceURI();
                String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
                if (attributePrefix == null) {
                    attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
                }
                String attributeValue;
                if (attributePrefix.trim().length() > 0) {
                    attributeValue = attributePrefix + ":" + qname.getLocalPart();
                } else {
                    attributeValue = qname.getLocalPart();
                }

                if (namespace.equals("")) {
                    xmlWriter.writeAttribute(attName, attributeValue);
                } else {
                    registerPrefix(xmlWriter, namespace);
                    xmlWriter.writeAttribute(namespace, attName, attributeValue);
                }
            }
        /**
         *  method to handle Qnames
         */

        private void writeQName(javax.xml.namespace.QName qname,
                                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            String namespaceURI = qname.getNamespaceURI();
            if (namespaceURI != null) {
                String prefix = xmlWriter.getPrefix(namespaceURI);
                if (prefix == null) {
                    prefix = generatePrefix(namespaceURI);
                    xmlWriter.writeNamespace(prefix, namespaceURI);
                    xmlWriter.setPrefix(prefix,namespaceURI);
                }

                if (prefix.trim().length() > 0){
                    xmlWriter.writeCharacters(prefix + ":" + org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                } else {
                    // i.e this is the default namespace
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                }

            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
            }
        }

        private void writeQNames(javax.xml.namespace.QName[] qnames,
                                 javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            if (qnames != null) {
                // we have to store this data until last moment since it is not possible to write any
                // namespace data after writing the charactor data
                StringBuffer stringToWrite = new StringBuffer();
                String namespaceURI = null;
                String prefix = null;

                for (int i = 0; i < qnames.length; i++) {
                    if (i > 0) {
                        stringToWrite.append(" ");
                    }
                    namespaceURI = qnames[i].getNamespaceURI();
                    if (namespaceURI != null) {
                        prefix = xmlWriter.getPrefix(namespaceURI);
                        if ((prefix == null) || (prefix.length() == 0)) {
                            prefix = generatePrefix(namespaceURI);
                            xmlWriter.writeNamespace(prefix, namespaceURI);
                            xmlWriter.setPrefix(prefix,namespaceURI);
                        }

                        if (prefix.trim().length() > 0){
                            stringToWrite.append(prefix).append(":").append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        } else {
                            stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        }
                    } else {
                        stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                    }
                }
                xmlWriter.writeCharacters(stringToWrite.toString());
            }

        }


        /**
         * Register a namespace prefix
         */
        private String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, String namespace) throws javax.xml.stream.XMLStreamException {
            String prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null) {
                prefix = generatePrefix(namespace);
                javax.xml.namespace.NamespaceContext nsContext = xmlWriter.getNamespaceContext();
                while (true) {
                    String uri = nsContext.getNamespaceURI(prefix);
                    if (uri == null || uri.length() == 0) {
                        break;
                    }
                    prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
                }
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            return prefix;
        }



        /**
        * databinding method to get an XML representation of this object
        *
        */
        public javax.xml.stream.XMLStreamReader getPullParser(javax.xml.namespace.QName qName)
                    throws org.apache.axis2.databinding.ADBException{




                //We can safely assume an element has only one type associated with it
                return localLoginResponse.getPullParser(MY_QNAME);

        }



     /**
      *  Factory class that keeps the parse method
      */
    public static class Factory{




        /**
        * static method to create the object
        * Precondition:  If this object is an element, the current or next start element starts this object and any intervening reader events are ignorable
        *                If this object is not an element, it is a complex type and the reader is at the event just after the outer start element
        * Postcondition: If this object is an element, the reader is positioned at its end element
        *                If this object is a complex type, the reader is positioned at the end element of its outer element
        */
        public static LoginResponseE parse(javax.xml.stream.XMLStreamReader reader) throws Exception{
            LoginResponseE object =
                new LoginResponseE();

            int event;
            String nillableValue = null;
            String prefix ="";
            String namespaceuri ="";
            try {

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();




                // Note all attributes that were handled. Used to differ normal attributes
                // from anyAttributes.
                java.util.Vector handledAttributes = new java.util.Vector();



                while(!reader.isEndElement()) {
                    if (reader.isStartElement() ){

                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://ws.idsunitelm.it/","loginResponse").equals(reader.getName())){

                                                object.setLoginResponse(LoginResponse.Factory.parse(reader));

                              }  // End of if for expected property start element

                             else{
                                        // A start element we are not expecting indicates an invalid parameter was passed
                                        throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                             }

                             } else {
                                reader.next();
                             }
                           }  // end of while loop




            } catch (javax.xml.stream.XMLStreamException e) {
                throw new Exception(e);
            }

            return object;
        }

        }//end of factory class



        }


        public static class RecuperoDocumentoResponseE
        implements org.apache.axis2.databinding.ADBBean{

                public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName(
                "http://ws.idsunitelm.it/",
                "recuperoDocumentoResponse",
                "ns1");



                        /**
                        * field for RecuperoDocumentoResponse
                        */


                                    protected RecuperoDocumentoResponse localRecuperoDocumentoResponse ;


                           /**
                           * Auto generated getter method
                           * @return RecuperoDocumentoResponse
                           */
                           public  RecuperoDocumentoResponse getRecuperoDocumentoResponse(){
                               return localRecuperoDocumentoResponse;
                           }



                            /**
                               * Auto generated setter method
                               * @param param RecuperoDocumentoResponse
                               */
                               public void setRecuperoDocumentoResponse(RecuperoDocumentoResponse param){

                                            this.localRecuperoDocumentoResponse=param;


                               }




        /**
        *
        * @param parentQName
        * @param factory
        * @return org.apache.axiom.om.OMElement
        */
       public org.apache.axiom.om.OMElement getOMElement (
               final javax.xml.namespace.QName parentQName,
               final org.apache.axiom.om.OMFactory factory) throws org.apache.axis2.databinding.ADBException{



               org.apache.axiom.om.OMDataSource dataSource =
                       new org.apache.axis2.databinding.ADBDataSource(this,MY_QNAME);
               return factory.createOMElement(dataSource,MY_QNAME);

        }

         public void serialize(final javax.xml.namespace.QName parentQName,
                                       javax.xml.stream.XMLStreamWriter xmlWriter)
                                throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{
                           serialize(parentQName,xmlWriter,false);
         }

         public void serialize(final javax.xml.namespace.QName parentQName,
                               javax.xml.stream.XMLStreamWriter xmlWriter,
                               boolean serializeType)
            throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{


                //We can safely assume an element has only one type associated with it

                                 if (localRecuperoDocumentoResponse==null){
                                   throw new org.apache.axis2.databinding.ADBException("recuperoDocumentoResponse cannot be null!");
                                 }
                                 localRecuperoDocumentoResponse.serialize(MY_QNAME,xmlWriter);


        }

        private static String generatePrefix(String namespace) {
            if(namespace.equals("http://ws.idsunitelm.it/")){
                return "ns1";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
         * Utility method to write an element start tag.
         */
        private void writeStartElement(String prefix, String namespace, String localPart,
                                       javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            String writerPrefix = xmlWriter.getPrefix(namespace);
            if (writerPrefix != null) {
                xmlWriter.writeStartElement(namespace, localPart);
            } else {
                if (namespace.length() == 0) {
                    prefix = "";
                } else if (prefix == null) {
                    prefix = generatePrefix(namespace);
                }

                xmlWriter.writeStartElement(prefix, localPart, namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
        }

        /**
         * Util method to write an attribute with the ns prefix
         */
        private void writeAttribute(String prefix,String namespace,String attName,
                                    String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (xmlWriter.getPrefix(namespace) == null) {
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            xmlWriter.writeAttribute(namespace,attName,attValue);
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeAttribute(String namespace,String attName,
                                    String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (namespace.equals("")) {
                xmlWriter.writeAttribute(attName,attValue);
            } else {
                registerPrefix(xmlWriter, namespace);
                xmlWriter.writeAttribute(namespace,attName,attValue);
            }
        }


           /**
             * Util method to write an attribute without the ns prefix
             */
            private void writeQNameAttribute(String namespace, String attName,
                                             javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

                String attributeNamespace = qname.getNamespaceURI();
                String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
                if (attributePrefix == null) {
                    attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
                }
                String attributeValue;
                if (attributePrefix.trim().length() > 0) {
                    attributeValue = attributePrefix + ":" + qname.getLocalPart();
                } else {
                    attributeValue = qname.getLocalPart();
                }

                if (namespace.equals("")) {
                    xmlWriter.writeAttribute(attName, attributeValue);
                } else {
                    registerPrefix(xmlWriter, namespace);
                    xmlWriter.writeAttribute(namespace, attName, attributeValue);
                }
            }
        /**
         *  method to handle Qnames
         */

        private void writeQName(javax.xml.namespace.QName qname,
                                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            String namespaceURI = qname.getNamespaceURI();
            if (namespaceURI != null) {
                String prefix = xmlWriter.getPrefix(namespaceURI);
                if (prefix == null) {
                    prefix = generatePrefix(namespaceURI);
                    xmlWriter.writeNamespace(prefix, namespaceURI);
                    xmlWriter.setPrefix(prefix,namespaceURI);
                }

                if (prefix.trim().length() > 0){
                    xmlWriter.writeCharacters(prefix + ":" + org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                } else {
                    // i.e this is the default namespace
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                }

            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
            }
        }

        private void writeQNames(javax.xml.namespace.QName[] qnames,
                                 javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            if (qnames != null) {
                // we have to store this data until last moment since it is not possible to write any
                // namespace data after writing the charactor data
                StringBuffer stringToWrite = new StringBuffer();
                String namespaceURI = null;
                String prefix = null;

                for (int i = 0; i < qnames.length; i++) {
                    if (i > 0) {
                        stringToWrite.append(" ");
                    }
                    namespaceURI = qnames[i].getNamespaceURI();
                    if (namespaceURI != null) {
                        prefix = xmlWriter.getPrefix(namespaceURI);
                        if ((prefix == null) || (prefix.length() == 0)) {
                            prefix = generatePrefix(namespaceURI);
                            xmlWriter.writeNamespace(prefix, namespaceURI);
                            xmlWriter.setPrefix(prefix,namespaceURI);
                        }

                        if (prefix.trim().length() > 0){
                            stringToWrite.append(prefix).append(":").append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        } else {
                            stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        }
                    } else {
                        stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                    }
                }
                xmlWriter.writeCharacters(stringToWrite.toString());
            }

        }


        /**
         * Register a namespace prefix
         */
        private String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, String namespace) throws javax.xml.stream.XMLStreamException {
            String prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null) {
                prefix = generatePrefix(namespace);
                javax.xml.namespace.NamespaceContext nsContext = xmlWriter.getNamespaceContext();
                while (true) {
                    String uri = nsContext.getNamespaceURI(prefix);
                    if (uri == null || uri.length() == 0) {
                        break;
                    }
                    prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
                }
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            return prefix;
        }



        /**
        * databinding method to get an XML representation of this object
        *
        */
        public javax.xml.stream.XMLStreamReader getPullParser(javax.xml.namespace.QName qName)
                    throws org.apache.axis2.databinding.ADBException{




                //We can safely assume an element has only one type associated with it
                return localRecuperoDocumentoResponse.getPullParser(MY_QNAME);

        }



     /**
      *  Factory class that keeps the parse method
      */
    public static class Factory{




        /**
        * static method to create the object
        * Precondition:  If this object is an element, the current or next start element starts this object and any intervening reader events are ignorable
        *                If this object is not an element, it is a complex type and the reader is at the event just after the outer start element
        * Postcondition: If this object is an element, the reader is positioned at its end element
        *                If this object is a complex type, the reader is positioned at the end element of its outer element
        */
        public static RecuperoDocumentoResponseE parse(javax.xml.stream.XMLStreamReader reader) throws Exception{
            RecuperoDocumentoResponseE object =
                new RecuperoDocumentoResponseE();

            int event;
            String nillableValue = null;
            String prefix ="";
            String namespaceuri ="";
            try {

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();




                // Note all attributes that were handled. Used to differ normal attributes
                // from anyAttributes.
                java.util.Vector handledAttributes = new java.util.Vector();



                while(!reader.isEndElement()) {
                    if (reader.isStartElement() ){

                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://ws.idsunitelm.it/","recuperoDocumentoResponse").equals(reader.getName())){

                                                object.setRecuperoDocumentoResponse(RecuperoDocumentoResponse.Factory.parse(reader));

                              }  // End of if for expected property start element

                             else{
                                        // A start element we are not expecting indicates an invalid parameter was passed
                                        throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                             }

                             } else {
                                reader.next();
                             }
                           }  // end of while loop




            } catch (javax.xml.stream.XMLStreamException e) {
                throw new Exception(e);
            }

            return object;
        }

        }//end of factory class



        }


        public static class LoginResponse
        implements org.apache.axis2.databinding.ADBBean{
        /* This type was generated from the piece of schema that had
                name = loginResponse
                Namespace URI = http://ws.idsunitelm.it/
                Namespace Prefix = ns1
                */


                        /**
                        * field for _return
                        */


                                    protected LoginResult local_return ;

                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean local_returnTracker = false ;

                           public boolean is_returnSpecified(){
                               return local_returnTracker;
                           }



                           /**
                           * Auto generated getter method
                           * @return LoginResult
                           */
                           public  LoginResult get_return(){
                               return local_return;
                           }



                            /**
                               * Auto generated setter method
                               * @param param _return
                               */
                               public void set_return(LoginResult param){
                            local_returnTracker = param != null;

                                            this.local_return=param;


                               }




        /**
        *
        * @param parentQName
        * @param factory
        * @return org.apache.axiom.om.OMElement
        */
       public org.apache.axiom.om.OMElement getOMElement (
               final javax.xml.namespace.QName parentQName,
               final org.apache.axiom.om.OMFactory factory) throws org.apache.axis2.databinding.ADBException{



               org.apache.axiom.om.OMDataSource dataSource =
                       new org.apache.axis2.databinding.ADBDataSource(this,parentQName);
               return factory.createOMElement(dataSource,parentQName);

        }

         public void serialize(final javax.xml.namespace.QName parentQName,
                                       javax.xml.stream.XMLStreamWriter xmlWriter)
                                throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{
                           serialize(parentQName,xmlWriter,false);
         }

         public void serialize(final javax.xml.namespace.QName parentQName,
                               javax.xml.stream.XMLStreamWriter xmlWriter,
                               boolean serializeType)
            throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{




                String prefix = null;
                String namespace = null;


                    prefix = parentQName.getPrefix();
                    namespace = parentQName.getNamespaceURI();
                    writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

                  if (serializeType){


                   String namespacePrefix = registerPrefix(xmlWriter,"http://ws.idsunitelm.it/");
                   if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                       writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                           namespacePrefix+":loginResponse",
                           xmlWriter);
                   } else {
                       writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                           "loginResponse",
                           xmlWriter);
                   }


                   }
                if (local_returnTracker){
                                            if (local_return==null){
                                                 throw new org.apache.axis2.databinding.ADBException("return cannot be null!!");
                                            }
                                           local_return.serialize(new javax.xml.namespace.QName("","return"),
                                               xmlWriter);
                                        }
                    xmlWriter.writeEndElement();


        }

        private static String generatePrefix(String namespace) {
            if(namespace.equals("http://ws.idsunitelm.it/")){
                return "ns1";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
         * Utility method to write an element start tag.
         */
        private void writeStartElement(String prefix, String namespace, String localPart,
                                       javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            String writerPrefix = xmlWriter.getPrefix(namespace);
            if (writerPrefix != null) {
                xmlWriter.writeStartElement(namespace, localPart);
            } else {
                if (namespace.length() == 0) {
                    prefix = "";
                } else if (prefix == null) {
                    prefix = generatePrefix(namespace);
                }

                xmlWriter.writeStartElement(prefix, localPart, namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
        }

        /**
         * Util method to write an attribute with the ns prefix
         */
        private void writeAttribute(String prefix,String namespace,String attName,
                                    String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (xmlWriter.getPrefix(namespace) == null) {
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            xmlWriter.writeAttribute(namespace,attName,attValue);
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeAttribute(String namespace,String attName,
                                    String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (namespace.equals("")) {
                xmlWriter.writeAttribute(attName,attValue);
            } else {
                registerPrefix(xmlWriter, namespace);
                xmlWriter.writeAttribute(namespace,attName,attValue);
            }
        }


           /**
             * Util method to write an attribute without the ns prefix
             */
            private void writeQNameAttribute(String namespace, String attName,
                                             javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

                String attributeNamespace = qname.getNamespaceURI();
                String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
                if (attributePrefix == null) {
                    attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
                }
                String attributeValue;
                if (attributePrefix.trim().length() > 0) {
                    attributeValue = attributePrefix + ":" + qname.getLocalPart();
                } else {
                    attributeValue = qname.getLocalPart();
                }

                if (namespace.equals("")) {
                    xmlWriter.writeAttribute(attName, attributeValue);
                } else {
                    registerPrefix(xmlWriter, namespace);
                    xmlWriter.writeAttribute(namespace, attName, attributeValue);
                }
            }
        /**
         *  method to handle Qnames
         */

        private void writeQName(javax.xml.namespace.QName qname,
                                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            String namespaceURI = qname.getNamespaceURI();
            if (namespaceURI != null) {
                String prefix = xmlWriter.getPrefix(namespaceURI);
                if (prefix == null) {
                    prefix = generatePrefix(namespaceURI);
                    xmlWriter.writeNamespace(prefix, namespaceURI);
                    xmlWriter.setPrefix(prefix,namespaceURI);
                }

                if (prefix.trim().length() > 0){
                    xmlWriter.writeCharacters(prefix + ":" + org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                } else {
                    // i.e this is the default namespace
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                }

            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
            }
        }

        private void writeQNames(javax.xml.namespace.QName[] qnames,
                                 javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            if (qnames != null) {
                // we have to store this data until last moment since it is not possible to write any
                // namespace data after writing the charactor data
                StringBuffer stringToWrite = new StringBuffer();
                String namespaceURI = null;
                String prefix = null;

                for (int i = 0; i < qnames.length; i++) {
                    if (i > 0) {
                        stringToWrite.append(" ");
                    }
                    namespaceURI = qnames[i].getNamespaceURI();
                    if (namespaceURI != null) {
                        prefix = xmlWriter.getPrefix(namespaceURI);
                        if ((prefix == null) || (prefix.length() == 0)) {
                            prefix = generatePrefix(namespaceURI);
                            xmlWriter.writeNamespace(prefix, namespaceURI);
                            xmlWriter.setPrefix(prefix,namespaceURI);
                        }

                        if (prefix.trim().length() > 0){
                            stringToWrite.append(prefix).append(":").append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        } else {
                            stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        }
                    } else {
                        stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                    }
                }
                xmlWriter.writeCharacters(stringToWrite.toString());
            }

        }


        /**
         * Register a namespace prefix
         */
        private String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, String namespace) throws javax.xml.stream.XMLStreamException {
            String prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null) {
                prefix = generatePrefix(namespace);
                javax.xml.namespace.NamespaceContext nsContext = xmlWriter.getNamespaceContext();
                while (true) {
                    String uri = nsContext.getNamespaceURI(prefix);
                    if (uri == null || uri.length() == 0) {
                        break;
                    }
                    prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
                }
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            return prefix;
        }



        /**
        * databinding method to get an XML representation of this object
        *
        */
        public javax.xml.stream.XMLStreamReader getPullParser(javax.xml.namespace.QName qName)
                    throws org.apache.axis2.databinding.ADBException{



                 java.util.ArrayList elementList = new java.util.ArrayList();
                 java.util.ArrayList attribList = new java.util.ArrayList();

                 if (local_returnTracker){
                            elementList.add(new javax.xml.namespace.QName("",
                                                                      "return"));


                                    if (local_return==null){
                                         throw new org.apache.axis2.databinding.ADBException("return cannot be null!!");
                                    }
                                    elementList.add(local_return);
                                }

                return new org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl(qName, elementList.toArray(), attribList.toArray());



        }



     /**
      *  Factory class that keeps the parse method
      */
    public static class Factory{




        /**
        * static method to create the object
        * Precondition:  If this object is an element, the current or next start element starts this object and any intervening reader events are ignorable
        *                If this object is not an element, it is a complex type and the reader is at the event just after the outer start element
        * Postcondition: If this object is an element, the reader is positioned at its end element
        *                If this object is a complex type, the reader is positioned at the end element of its outer element
        */
        public static LoginResponse parse(javax.xml.stream.XMLStreamReader reader) throws Exception{
            LoginResponse object =
                new LoginResponse();

            int event;
            String nillableValue = null;
            String prefix ="";
            String namespaceuri ="";
            try {

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();


                if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","type")!=null){
                  String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                        "type");
                  if (fullTypeName!=null){
                    String nsPrefix = null;
                    if (fullTypeName.indexOf(":") > -1){
                        nsPrefix = fullTypeName.substring(0,fullTypeName.indexOf(":"));
                    }
                    nsPrefix = nsPrefix==null?"":nsPrefix;

                    String type = fullTypeName.substring(fullTypeName.indexOf(":")+1);

                            if (!"loginResponse".equals(type)){
                                //find namespace for the prefix
                                String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (LoginResponse) ExtensionMapper.getTypeObject(
                                        nsUri, type, reader);
                              }


                  }


                }




                // Note all attributes that were handled. Used to differ normal attributes
                // from anyAttributes.
                java.util.Vector handledAttributes = new java.util.Vector();




                    reader.next();


                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                                    if (reader.isStartElement() && new javax.xml.namespace.QName("","return").equals(reader.getName())){

                                                object.set_return(LoginResult.Factory.parse(reader));

                                        reader.next();

                              }  // End of if for expected property start element

                                    else {

                                    }

                            while (!reader.isStartElement() && !reader.isEndElement())
                                reader.next();

                                if (reader.isStartElement())
                                // A start element we are not expecting indicates a trailing invalid property
                                throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());




            } catch (javax.xml.stream.XMLStreamException e) {
                throw new Exception(e);
            }

            return object;
        }

        }//end of factory class



        }


        public static class CreazioneFascicoloE
        implements org.apache.axis2.databinding.ADBBean{

                public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName(
                "http://ws.idsunitelm.it/",
                "creazioneFascicolo",
                "ns1");



                        /**
                        * field for CreazioneFascicolo
                        */


                                    protected CreazioneFascicolo localCreazioneFascicolo ;


                           /**
                           * Auto generated getter method
                           * @return CreazioneFascicolo
                           */
                           public  CreazioneFascicolo getCreazioneFascicolo(){
                               return localCreazioneFascicolo;
                           }



                            /**
                               * Auto generated setter method
                               * @param param CreazioneFascicolo
                               */
                               public void setCreazioneFascicolo(CreazioneFascicolo param){

                                            this.localCreazioneFascicolo=param;


                               }




        /**
        *
        * @param parentQName
        * @param factory
        * @return org.apache.axiom.om.OMElement
        */
       public org.apache.axiom.om.OMElement getOMElement (
               final javax.xml.namespace.QName parentQName,
               final org.apache.axiom.om.OMFactory factory) throws org.apache.axis2.databinding.ADBException{



               org.apache.axiom.om.OMDataSource dataSource =
                       new org.apache.axis2.databinding.ADBDataSource(this,MY_QNAME);
               return factory.createOMElement(dataSource,MY_QNAME);

        }

         public void serialize(final javax.xml.namespace.QName parentQName,
                                       javax.xml.stream.XMLStreamWriter xmlWriter)
                                throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{
                           serialize(parentQName,xmlWriter,false);
         }

         public void serialize(final javax.xml.namespace.QName parentQName,
                               javax.xml.stream.XMLStreamWriter xmlWriter,
                               boolean serializeType)
            throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{


                //We can safely assume an element has only one type associated with it

                                 if (localCreazioneFascicolo==null){
                                   throw new org.apache.axis2.databinding.ADBException("creazioneFascicolo cannot be null!");
                                 }
                                 localCreazioneFascicolo.serialize(MY_QNAME,xmlWriter);


        }

        private static String generatePrefix(String namespace) {
            if(namespace.equals("http://ws.idsunitelm.it/")){
                return "ns1";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
         * Utility method to write an element start tag.
         */
        private void writeStartElement(String prefix, String namespace, String localPart,
                                       javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            String writerPrefix = xmlWriter.getPrefix(namespace);
            if (writerPrefix != null) {
                xmlWriter.writeStartElement(namespace, localPart);
            } else {
                if (namespace.length() == 0) {
                    prefix = "";
                } else if (prefix == null) {
                    prefix = generatePrefix(namespace);
                }

                xmlWriter.writeStartElement(prefix, localPart, namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
        }

        /**
         * Util method to write an attribute with the ns prefix
         */
        private void writeAttribute(String prefix,String namespace,String attName,
                                    String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (xmlWriter.getPrefix(namespace) == null) {
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            xmlWriter.writeAttribute(namespace,attName,attValue);
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeAttribute(String namespace,String attName,
                                    String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (namespace.equals("")) {
                xmlWriter.writeAttribute(attName,attValue);
            } else {
                registerPrefix(xmlWriter, namespace);
                xmlWriter.writeAttribute(namespace,attName,attValue);
            }
        }


           /**
             * Util method to write an attribute without the ns prefix
             */
            private void writeQNameAttribute(String namespace, String attName,
                                             javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

                String attributeNamespace = qname.getNamespaceURI();
                String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
                if (attributePrefix == null) {
                    attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
                }
                String attributeValue;
                if (attributePrefix.trim().length() > 0) {
                    attributeValue = attributePrefix + ":" + qname.getLocalPart();
                } else {
                    attributeValue = qname.getLocalPart();
                }

                if (namespace.equals("")) {
                    xmlWriter.writeAttribute(attName, attributeValue);
                } else {
                    registerPrefix(xmlWriter, namespace);
                    xmlWriter.writeAttribute(namespace, attName, attributeValue);
                }
            }
        /**
         *  method to handle Qnames
         */

        private void writeQName(javax.xml.namespace.QName qname,
                                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            String namespaceURI = qname.getNamespaceURI();
            if (namespaceURI != null) {
                String prefix = xmlWriter.getPrefix(namespaceURI);
                if (prefix == null) {
                    prefix = generatePrefix(namespaceURI);
                    xmlWriter.writeNamespace(prefix, namespaceURI);
                    xmlWriter.setPrefix(prefix,namespaceURI);
                }

                if (prefix.trim().length() > 0){
                    xmlWriter.writeCharacters(prefix + ":" + org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                } else {
                    // i.e this is the default namespace
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                }

            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
            }
        }

        private void writeQNames(javax.xml.namespace.QName[] qnames,
                                 javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            if (qnames != null) {
                // we have to store this data until last moment since it is not possible to write any
                // namespace data after writing the charactor data
                StringBuffer stringToWrite = new StringBuffer();
                String namespaceURI = null;
                String prefix = null;

                for (int i = 0; i < qnames.length; i++) {
                    if (i > 0) {
                        stringToWrite.append(" ");
                    }
                    namespaceURI = qnames[i].getNamespaceURI();
                    if (namespaceURI != null) {
                        prefix = xmlWriter.getPrefix(namespaceURI);
                        if ((prefix == null) || (prefix.length() == 0)) {
                            prefix = generatePrefix(namespaceURI);
                            xmlWriter.writeNamespace(prefix, namespaceURI);
                            xmlWriter.setPrefix(prefix,namespaceURI);
                        }

                        if (prefix.trim().length() > 0){
                            stringToWrite.append(prefix).append(":").append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        } else {
                            stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        }
                    } else {
                        stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                    }
                }
                xmlWriter.writeCharacters(stringToWrite.toString());
            }

        }


        /**
         * Register a namespace prefix
         */
        private String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, String namespace) throws javax.xml.stream.XMLStreamException {
            String prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null) {
                prefix = generatePrefix(namespace);
                javax.xml.namespace.NamespaceContext nsContext = xmlWriter.getNamespaceContext();
                while (true) {
                    String uri = nsContext.getNamespaceURI(prefix);
                    if (uri == null || uri.length() == 0) {
                        break;
                    }
                    prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
                }
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            return prefix;
        }



        /**
        * databinding method to get an XML representation of this object
        *
        */
        public javax.xml.stream.XMLStreamReader getPullParser(javax.xml.namespace.QName qName)
                    throws org.apache.axis2.databinding.ADBException{




                //We can safely assume an element has only one type associated with it
                return localCreazioneFascicolo.getPullParser(MY_QNAME);

        }



     /**
      *  Factory class that keeps the parse method
      */
    public static class Factory{




        /**
        * static method to create the object
        * Precondition:  If this object is an element, the current or next start element starts this object and any intervening reader events are ignorable
        *                If this object is not an element, it is a complex type and the reader is at the event just after the outer start element
        * Postcondition: If this object is an element, the reader is positioned at its end element
        *                If this object is a complex type, the reader is positioned at the end element of its outer element
        */
        public static CreazioneFascicoloE parse(javax.xml.stream.XMLStreamReader reader) throws Exception{
            CreazioneFascicoloE object =
                new CreazioneFascicoloE();

            int event;
            String nillableValue = null;
            String prefix ="";
            String namespaceuri ="";
            try {

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();




                // Note all attributes that were handled. Used to differ normal attributes
                // from anyAttributes.
                java.util.Vector handledAttributes = new java.util.Vector();



                while(!reader.isEndElement()) {
                    if (reader.isStartElement() ){

                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://ws.idsunitelm.it/","creazioneFascicolo").equals(reader.getName())){

                                                object.setCreazioneFascicolo(CreazioneFascicolo.Factory.parse(reader));

                              }  // End of if for expected property start element

                             else{
                                        // A start element we are not expecting indicates an invalid parameter was passed
                                        throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                             }

                             } else {
                                reader.next();
                             }
                           }  // end of while loop




            } catch (javax.xml.stream.XMLStreamException e) {
                throw new Exception(e);
            }

            return object;
        }

        }//end of factory class



        }


        public static class InserimentoResult
        implements org.apache.axis2.databinding.ADBBean{
        /* This type was generated from the piece of schema that had
                name = inserimentoResult
                Namespace URI = http://ws.idsunitelm.it/
                Namespace Prefix = ns1
                */


                        /**
                        * field for AnnoProt
                        */


                                    protected long localAnnoProt ;


                           /**
                           * Auto generated getter method
                           * @return long
                           */
                           public  long getAnnoProt(){
                               return localAnnoProt;
                           }



                            /**
                               * Auto generated setter method
                               * @param param AnnoProt
                               */
                               public void setAnnoProt(long param){

                                            this.localAnnoProt=param;


                               }


                        /**
                        * field for DataProt
                        */


                                    protected String localDataProt ;

                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localDataProtTracker = false ;

                           public boolean isDataProtSpecified(){
                               return localDataProtTracker;
                           }



                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  String getDataProt(){
                               return localDataProt;
                           }



                            /**
                               * Auto generated setter method
                               * @param param DataProt
                               */
                               public void setDataProt(String param){
                            localDataProtTracker = param != null;

                                            this.localDataProt=param;


                               }


                        /**
                        * field for Error_description
                        */


                                    protected String localError_description ;

                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localError_descriptionTracker = false ;

                           public boolean isError_descriptionSpecified(){
                               return localError_descriptionTracker;
                           }



                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  String getError_description(){
                               return localError_description;
                           }



                            /**
                               * Auto generated setter method
                               * @param param Error_description
                               */
                               public void setError_description(String param){
                            localError_descriptionTracker = param != null;

                                            this.localError_description=param;


                               }


                        /**
                        * field for Error_number
                        */


                                    protected int localError_number ;


                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getError_number(){
                               return localError_number;
                           }



                            /**
                               * Auto generated setter method
                               * @param param Error_number
                               */
                               public void setError_number(int param){

                                            this.localError_number=param;


                               }


                        /**
                        * field for NumProt
                        */


                                    protected long localNumProt ;


                           /**
                           * Auto generated getter method
                           * @return long
                           */
                           public  long getNumProt(){
                               return localNumProt;
                           }



                            /**
                               * Auto generated setter method
                               * @param param NumProt
                               */
                               public void setNumProt(long param){

                                            this.localNumProt=param;


                               }




        /**
        *
        * @param parentQName
        * @param factory
        * @return org.apache.axiom.om.OMElement
        */
       public org.apache.axiom.om.OMElement getOMElement (
               final javax.xml.namespace.QName parentQName,
               final org.apache.axiom.om.OMFactory factory) throws org.apache.axis2.databinding.ADBException{



               org.apache.axiom.om.OMDataSource dataSource =
                       new org.apache.axis2.databinding.ADBDataSource(this,parentQName);
               return factory.createOMElement(dataSource,parentQName);

        }

         public void serialize(final javax.xml.namespace.QName parentQName,
                                       javax.xml.stream.XMLStreamWriter xmlWriter)
                                throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{
                           serialize(parentQName,xmlWriter,false);
         }

         public void serialize(final javax.xml.namespace.QName parentQName,
                               javax.xml.stream.XMLStreamWriter xmlWriter,
                               boolean serializeType)
            throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{




                String prefix = null;
                String namespace = null;


                    prefix = parentQName.getPrefix();
                    namespace = parentQName.getNamespaceURI();
                    writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

                  if (serializeType){


                   String namespacePrefix = registerPrefix(xmlWriter,"http://ws.idsunitelm.it/");
                   if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                       writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                           namespacePrefix+":inserimentoResult",
                           xmlWriter);
                   } else {
                       writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                           "inserimentoResult",
                           xmlWriter);
                   }


                   }

                                    namespace = "";
                                    writeStartElement(null, namespace, "annoProt", xmlWriter);

                                               if (localAnnoProt== Long.MIN_VALUE) {

                                                         throw new org.apache.axis2.databinding.ADBException("annoProt cannot be null!!");

                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localAnnoProt));
                                               }

                                   xmlWriter.writeEndElement();
                              if (localDataProtTracker){
                                    namespace = "";
                                    writeStartElement(null, namespace, "dataProt", xmlWriter);


                                          if (localDataProt==null){
                                              // write the nil attribute

                                                     throw new org.apache.axis2.databinding.ADBException("dataProt cannot be null!!");

                                          }else{


                                                   xmlWriter.writeCharacters(localDataProt);

                                          }

                                   xmlWriter.writeEndElement();
                             } if (localError_descriptionTracker){
                                    namespace = "";
                                    writeStartElement(null, namespace, "error_description", xmlWriter);


                                          if (localError_description==null){
                                              // write the nil attribute

                                                     throw new org.apache.axis2.databinding.ADBException("error_description cannot be null!!");

                                          }else{


                                                   xmlWriter.writeCharacters(localError_description);

                                          }

                                   xmlWriter.writeEndElement();
                             }
                                    namespace = "";
                                    writeStartElement(null, namespace, "error_number", xmlWriter);

                                               if (localError_number== Integer.MIN_VALUE) {

                                                         throw new org.apache.axis2.databinding.ADBException("error_number cannot be null!!");

                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localError_number));
                                               }

                                   xmlWriter.writeEndElement();

                                    namespace = "";
                                    writeStartElement(null, namespace, "numProt", xmlWriter);

                                               if (localNumProt== Long.MIN_VALUE) {

                                                         throw new org.apache.axis2.databinding.ADBException("numProt cannot be null!!");

                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localNumProt));
                                               }

                                   xmlWriter.writeEndElement();

                    xmlWriter.writeEndElement();


        }

        private static String generatePrefix(String namespace) {
            if(namespace.equals("http://ws.idsunitelm.it/")){
                return "ns1";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
         * Utility method to write an element start tag.
         */
        private void writeStartElement(String prefix, String namespace, String localPart,
                                       javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            String writerPrefix = xmlWriter.getPrefix(namespace);
            if (writerPrefix != null) {
                xmlWriter.writeStartElement(namespace, localPart);
            } else {
                if (namespace.length() == 0) {
                    prefix = "";
                } else if (prefix == null) {
                    prefix = generatePrefix(namespace);
                }

                xmlWriter.writeStartElement(prefix, localPart, namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
        }

        /**
         * Util method to write an attribute with the ns prefix
         */
        private void writeAttribute(String prefix,String namespace,String attName,
                                    String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (xmlWriter.getPrefix(namespace) == null) {
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            xmlWriter.writeAttribute(namespace,attName,attValue);
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeAttribute(String namespace,String attName,
                                    String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (namespace.equals("")) {
                xmlWriter.writeAttribute(attName,attValue);
            } else {
                registerPrefix(xmlWriter, namespace);
                xmlWriter.writeAttribute(namespace,attName,attValue);
            }
        }


           /**
             * Util method to write an attribute without the ns prefix
             */
            private void writeQNameAttribute(String namespace, String attName,
                                             javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

                String attributeNamespace = qname.getNamespaceURI();
                String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
                if (attributePrefix == null) {
                    attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
                }
                String attributeValue;
                if (attributePrefix.trim().length() > 0) {
                    attributeValue = attributePrefix + ":" + qname.getLocalPart();
                } else {
                    attributeValue = qname.getLocalPart();
                }

                if (namespace.equals("")) {
                    xmlWriter.writeAttribute(attName, attributeValue);
                } else {
                    registerPrefix(xmlWriter, namespace);
                    xmlWriter.writeAttribute(namespace, attName, attributeValue);
                }
            }
        /**
         *  method to handle Qnames
         */

        private void writeQName(javax.xml.namespace.QName qname,
                                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            String namespaceURI = qname.getNamespaceURI();
            if (namespaceURI != null) {
                String prefix = xmlWriter.getPrefix(namespaceURI);
                if (prefix == null) {
                    prefix = generatePrefix(namespaceURI);
                    xmlWriter.writeNamespace(prefix, namespaceURI);
                    xmlWriter.setPrefix(prefix,namespaceURI);
                }

                if (prefix.trim().length() > 0){
                    xmlWriter.writeCharacters(prefix + ":" + org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                } else {
                    // i.e this is the default namespace
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                }

            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
            }
        }

        private void writeQNames(javax.xml.namespace.QName[] qnames,
                                 javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            if (qnames != null) {
                // we have to store this data until last moment since it is not possible to write any
                // namespace data after writing the charactor data
                StringBuffer stringToWrite = new StringBuffer();
                String namespaceURI = null;
                String prefix = null;

                for (int i = 0; i < qnames.length; i++) {
                    if (i > 0) {
                        stringToWrite.append(" ");
                    }
                    namespaceURI = qnames[i].getNamespaceURI();
                    if (namespaceURI != null) {
                        prefix = xmlWriter.getPrefix(namespaceURI);
                        if ((prefix == null) || (prefix.length() == 0)) {
                            prefix = generatePrefix(namespaceURI);
                            xmlWriter.writeNamespace(prefix, namespaceURI);
                            xmlWriter.setPrefix(prefix,namespaceURI);
                        }

                        if (prefix.trim().length() > 0){
                            stringToWrite.append(prefix).append(":").append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        } else {
                            stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        }
                    } else {
                        stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                    }
                }
                xmlWriter.writeCharacters(stringToWrite.toString());
            }

        }


        /**
         * Register a namespace prefix
         */
        private String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, String namespace) throws javax.xml.stream.XMLStreamException {
            String prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null) {
                prefix = generatePrefix(namespace);
                javax.xml.namespace.NamespaceContext nsContext = xmlWriter.getNamespaceContext();
                while (true) {
                    String uri = nsContext.getNamespaceURI(prefix);
                    if (uri == null || uri.length() == 0) {
                        break;
                    }
                    prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
                }
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            return prefix;
        }



        /**
        * databinding method to get an XML representation of this object
        *
        */
        public javax.xml.stream.XMLStreamReader getPullParser(javax.xml.namespace.QName qName)
                    throws org.apache.axis2.databinding.ADBException{



                 java.util.ArrayList elementList = new java.util.ArrayList();
                 java.util.ArrayList attribList = new java.util.ArrayList();


                                      elementList.add(new javax.xml.namespace.QName("",
                                                                      "annoProt"));

                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localAnnoProt));
                             if (localDataProtTracker){
                                      elementList.add(new javax.xml.namespace.QName("",
                                                                      "dataProt"));

                                        if (localDataProt != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDataProt));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("dataProt cannot be null!!");
                                        }
                                    } if (localError_descriptionTracker){
                                      elementList.add(new javax.xml.namespace.QName("",
                                                                      "error_description"));

                                        if (localError_description != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localError_description));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("error_description cannot be null!!");
                                        }
                                    }
                                      elementList.add(new javax.xml.namespace.QName("",
                                                                      "error_number"));

                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localError_number));

                                      elementList.add(new javax.xml.namespace.QName("",
                                                                      "numProt"));

                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localNumProt));


                return new org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl(qName, elementList.toArray(), attribList.toArray());



        }



     /**
      *  Factory class that keeps the parse method
      */
    public static class Factory{




        /**
        * static method to create the object
        * Precondition:  If this object is an element, the current or next start element starts this object and any intervening reader events are ignorable
        *                If this object is not an element, it is a complex type and the reader is at the event just after the outer start element
        * Postcondition: If this object is an element, the reader is positioned at its end element
        *                If this object is a complex type, the reader is positioned at the end element of its outer element
        */
        public static InserimentoResult parse(javax.xml.stream.XMLStreamReader reader) throws Exception{
            InserimentoResult object =
                new InserimentoResult();

            int event;
            String nillableValue = null;
            String prefix ="";
            String namespaceuri ="";
            try {

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();


                if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","type")!=null){
                  String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                        "type");
                  if (fullTypeName!=null){
                    String nsPrefix = null;
                    if (fullTypeName.indexOf(":") > -1){
                        nsPrefix = fullTypeName.substring(0,fullTypeName.indexOf(":"));
                    }
                    nsPrefix = nsPrefix==null?"":nsPrefix;

                    String type = fullTypeName.substring(fullTypeName.indexOf(":")+1);

                            if (!"inserimentoResult".equals(type)){
                                //find namespace for the prefix
                                String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (InserimentoResult) ExtensionMapper.getTypeObject(
                                        nsUri, type, reader);
                              }


                  }


                }




                // Note all attributes that were handled. Used to differ normal attributes
                // from anyAttributes.
                java.util.Vector handledAttributes = new java.util.Vector();




                    reader.next();


                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                                    if (reader.isStartElement() && new javax.xml.namespace.QName("","annoProt").equals(reader.getName())){

                                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                    if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                        throw new org.apache.axis2.databinding.ADBException("The element: "+"annoProt" +"  cannot be null");
                                    }


                                    String content = reader.getElementText();

                                              object.setAnnoProt(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToLong(content));

                                        reader.next();

                              }  // End of if for expected property start element

                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                                }


                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                                    if (reader.isStartElement() && new javax.xml.namespace.QName("","dataProt").equals(reader.getName())){

                                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                    if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                        throw new org.apache.axis2.databinding.ADBException("The element: "+"dataProt" +"  cannot be null");
                                    }


                                    String content = reader.getElementText();

                                              object.setDataProt(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                                        reader.next();

                              }  // End of if for expected property start element

                                    else {

                                    }


                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                                    if (reader.isStartElement() && new javax.xml.namespace.QName("","error_description").equals(reader.getName())){

                                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                    if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                        throw new org.apache.axis2.databinding.ADBException("The element: "+"error_description" +"  cannot be null");
                                    }


                                    String content = reader.getElementText();

                                              object.setError_description(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                                        reader.next();

                              }  // End of if for expected property start element

                                    else {

                                    }


                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                                    if (reader.isStartElement() && new javax.xml.namespace.QName("","error_number").equals(reader.getName())){

                                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                    if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                        throw new org.apache.axis2.databinding.ADBException("The element: "+"error_number" +"  cannot be null");
                                    }


                                    String content = reader.getElementText();

                                              object.setError_number(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));

                                        reader.next();

                              }  // End of if for expected property start element

                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                                }


                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                                    if (reader.isStartElement() && new javax.xml.namespace.QName("","numProt").equals(reader.getName())){

                                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                    if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                        throw new org.apache.axis2.databinding.ADBException("The element: "+"numProt" +"  cannot be null");
                                    }


                                    String content = reader.getElementText();

                                              object.setNumProt(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToLong(content));

                                        reader.next();

                              }  // End of if for expected property start element

                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                                }

                            while (!reader.isStartElement() && !reader.isEndElement())
                                reader.next();

                                if (reader.isStartElement())
                                // A start element we are not expecting indicates a trailing invalid property
                                throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());




            } catch (javax.xml.stream.XMLStreamException e) {
                throw new Exception(e);
            }

            return object;
        }

        }//end of factory class



        }


        public static class Attach
        implements org.apache.axis2.databinding.ADBBean{
        /* This type was generated from the piece of schema that had
                name = attach
                Namespace URI = http://ws.idsunitelm.it/
                Namespace Prefix = ns1
                */


                        /**
                        * field for FileContent
                        */


                                    protected javax.activation.DataHandler localFileContent ;

                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localFileContentTracker = false ;

                           public boolean isFileContentSpecified(){
                               return localFileContentTracker;
                           }



                           /**
                           * Auto generated getter method
                           * @return javax.activation.DataHandler
                           */
                           public  javax.activation.DataHandler getFileContent(){
                               return localFileContent;
                           }



                            /**
                               * Auto generated setter method
                               * @param param FileContent
                               */
                               public void setFileContent(javax.activation.DataHandler param){
                            localFileContentTracker = param != null;

                                            this.localFileContent=param;


                               }


                        /**
                        * field for FileName
                        */


                                    protected String localFileName ;

                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localFileNameTracker = false ;

                           public boolean isFileNameSpecified(){
                               return localFileNameTracker;
                           }



                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  String getFileName(){
                               return localFileName;
                           }



                            /**
                               * Auto generated setter method
                               * @param param FileName
                               */
                               public void setFileName(String param){
                            localFileNameTracker = param != null;

                                            this.localFileName=param;


                               }


                        /**
                        * field for Id
                        */


                                    protected String localId ;

                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localIdTracker = false ;

                           public boolean isIdSpecified(){
                               return localIdTracker;
                           }



                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  String getId(){
                               return localId;
                           }



                            /**
                               * Auto generated setter method
                               * @param param Id
                               */
                               public void setId(String param){
                            localIdTracker = param != null;

                                            this.localId=param;


                               }




        /**
        *
        * @param parentQName
        * @param factory
        * @return org.apache.axiom.om.OMElement
        */
       public org.apache.axiom.om.OMElement getOMElement (
               final javax.xml.namespace.QName parentQName,
               final org.apache.axiom.om.OMFactory factory) throws org.apache.axis2.databinding.ADBException{



               org.apache.axiom.om.OMDataSource dataSource =
                       new org.apache.axis2.databinding.ADBDataSource(this,parentQName);
               return factory.createOMElement(dataSource,parentQName);

        }

         public void serialize(final javax.xml.namespace.QName parentQName,
                                       javax.xml.stream.XMLStreamWriter xmlWriter)
                                throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{
                           serialize(parentQName,xmlWriter,false);
         }

         public void serialize(final javax.xml.namespace.QName parentQName,
                               javax.xml.stream.XMLStreamWriter xmlWriter,
                               boolean serializeType)
            throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{




                String prefix = null;
                String namespace = null;


                    prefix = parentQName.getPrefix();
                    namespace = parentQName.getNamespaceURI();
                    writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

                  if (serializeType){


                   String namespacePrefix = registerPrefix(xmlWriter,"http://ws.idsunitelm.it/");
                   if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                       writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                           namespacePrefix+":attach",
                           xmlWriter);
                   } else {
                       writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                           "attach",
                           xmlWriter);
                   }


                   }
                if (localFileContentTracker){
                                    namespace = "";
                                    writeStartElement(null, namespace, "fileContent", xmlWriter);


                                    if (localFileContent!=null)  {
                                       try {
                                           org.apache.axiom.util.stax.XMLStreamWriterUtils.writeDataHandler(xmlWriter, localFileContent, null, true);
                                       } catch (java.io.IOException ex) {
                                           throw new javax.xml.stream.XMLStreamException("Unable to read data handler for fileContent", ex);
                                       }
                                    } else {

                                    }

                                   xmlWriter.writeEndElement();
                             } if (localFileNameTracker){
                                    namespace = "";
                                    writeStartElement(null, namespace, "fileName", xmlWriter);


                                          if (localFileName==null){
                                              // write the nil attribute

                                                     throw new org.apache.axis2.databinding.ADBException("fileName cannot be null!!");

                                          }else{


                                                   xmlWriter.writeCharacters(localFileName);

                                          }

                                   xmlWriter.writeEndElement();
                             } if (localIdTracker){
                                    namespace = "";
                                    writeStartElement(null, namespace, "id", xmlWriter);


                                          if (localId==null){
                                              // write the nil attribute

                                                     throw new org.apache.axis2.databinding.ADBException("id cannot be null!!");

                                          }else{


                                                   xmlWriter.writeCharacters(localId);

                                          }

                                   xmlWriter.writeEndElement();
                             }
                    xmlWriter.writeEndElement();


        }

        private static String generatePrefix(String namespace) {
            if(namespace.equals("http://ws.idsunitelm.it/")){
                return "ns1";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
         * Utility method to write an element start tag.
         */
        private void writeStartElement(String prefix, String namespace, String localPart,
                                       javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            String writerPrefix = xmlWriter.getPrefix(namespace);
            if (writerPrefix != null) {
                xmlWriter.writeStartElement(namespace, localPart);
            } else {
                if (namespace.length() == 0) {
                    prefix = "";
                } else if (prefix == null) {
                    prefix = generatePrefix(namespace);
                }

                xmlWriter.writeStartElement(prefix, localPart, namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
        }

        /**
         * Util method to write an attribute with the ns prefix
         */
        private void writeAttribute(String prefix,String namespace,String attName,
                                    String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (xmlWriter.getPrefix(namespace) == null) {
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            xmlWriter.writeAttribute(namespace,attName,attValue);
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeAttribute(String namespace,String attName,
                                    String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (namespace.equals("")) {
                xmlWriter.writeAttribute(attName,attValue);
            } else {
                registerPrefix(xmlWriter, namespace);
                xmlWriter.writeAttribute(namespace,attName,attValue);
            }
        }


           /**
             * Util method to write an attribute without the ns prefix
             */
            private void writeQNameAttribute(String namespace, String attName,
                                             javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

                String attributeNamespace = qname.getNamespaceURI();
                String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
                if (attributePrefix == null) {
                    attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
                }
                String attributeValue;
                if (attributePrefix.trim().length() > 0) {
                    attributeValue = attributePrefix + ":" + qname.getLocalPart();
                } else {
                    attributeValue = qname.getLocalPart();
                }

                if (namespace.equals("")) {
                    xmlWriter.writeAttribute(attName, attributeValue);
                } else {
                    registerPrefix(xmlWriter, namespace);
                    xmlWriter.writeAttribute(namespace, attName, attributeValue);
                }
            }
        /**
         *  method to handle Qnames
         */

        private void writeQName(javax.xml.namespace.QName qname,
                                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            String namespaceURI = qname.getNamespaceURI();
            if (namespaceURI != null) {
                String prefix = xmlWriter.getPrefix(namespaceURI);
                if (prefix == null) {
                    prefix = generatePrefix(namespaceURI);
                    xmlWriter.writeNamespace(prefix, namespaceURI);
                    xmlWriter.setPrefix(prefix,namespaceURI);
                }

                if (prefix.trim().length() > 0){
                    xmlWriter.writeCharacters(prefix + ":" + org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                } else {
                    // i.e this is the default namespace
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                }

            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
            }
        }

        private void writeQNames(javax.xml.namespace.QName[] qnames,
                                 javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            if (qnames != null) {
                // we have to store this data until last moment since it is not possible to write any
                // namespace data after writing the charactor data
                StringBuffer stringToWrite = new StringBuffer();
                String namespaceURI = null;
                String prefix = null;

                for (int i = 0; i < qnames.length; i++) {
                    if (i > 0) {
                        stringToWrite.append(" ");
                    }
                    namespaceURI = qnames[i].getNamespaceURI();
                    if (namespaceURI != null) {
                        prefix = xmlWriter.getPrefix(namespaceURI);
                        if ((prefix == null) || (prefix.length() == 0)) {
                            prefix = generatePrefix(namespaceURI);
                            xmlWriter.writeNamespace(prefix, namespaceURI);
                            xmlWriter.setPrefix(prefix,namespaceURI);
                        }

                        if (prefix.trim().length() > 0){
                            stringToWrite.append(prefix).append(":").append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        } else {
                            stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        }
                    } else {
                        stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                    }
                }
                xmlWriter.writeCharacters(stringToWrite.toString());
            }

        }


        /**
         * Register a namespace prefix
         */
        private String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, String namespace) throws javax.xml.stream.XMLStreamException {
            String prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null) {
                prefix = generatePrefix(namespace);
                javax.xml.namespace.NamespaceContext nsContext = xmlWriter.getNamespaceContext();
                while (true) {
                    String uri = nsContext.getNamespaceURI(prefix);
                    if (uri == null || uri.length() == 0) {
                        break;
                    }
                    prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
                }
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            return prefix;
        }



        /**
        * databinding method to get an XML representation of this object
        *
        */
        public javax.xml.stream.XMLStreamReader getPullParser(javax.xml.namespace.QName qName)
                    throws org.apache.axis2.databinding.ADBException{



                 java.util.ArrayList elementList = new java.util.ArrayList();
                 java.util.ArrayList attribList = new java.util.ArrayList();

                 if (localFileContentTracker){
                                      elementList.add(new javax.xml.namespace.QName("",
                                        "fileContent"));

                            elementList.add(localFileContent);
                        } if (localFileNameTracker){
                                      elementList.add(new javax.xml.namespace.QName("",
                                                                      "fileName"));

                                        if (localFileName != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localFileName));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("fileName cannot be null!!");
                                        }
                                    } if (localIdTracker){
                                      elementList.add(new javax.xml.namespace.QName("",
                                                                      "id"));

                                        if (localId != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localId));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("id cannot be null!!");
                                        }
                                    }

                return new org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl(qName, elementList.toArray(), attribList.toArray());



        }



     /**
      *  Factory class that keeps the parse method
      */
    public static class Factory{




        /**
        * static method to create the object
        * Precondition:  If this object is an element, the current or next start element starts this object and any intervening reader events are ignorable
        *                If this object is not an element, it is a complex type and the reader is at the event just after the outer start element
        * Postcondition: If this object is an element, the reader is positioned at its end element
        *                If this object is a complex type, the reader is positioned at the end element of its outer element
        */
        public static Attach parse(javax.xml.stream.XMLStreamReader reader) throws Exception{
            Attach object =
                new Attach();

            int event;
            String nillableValue = null;
            String prefix ="";
            String namespaceuri ="";
            try {

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();


                if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","type")!=null){
                  String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                        "type");
                  if (fullTypeName!=null){
                    String nsPrefix = null;
                    if (fullTypeName.indexOf(":") > -1){
                        nsPrefix = fullTypeName.substring(0,fullTypeName.indexOf(":"));
                    }
                    nsPrefix = nsPrefix==null?"":nsPrefix;

                    String type = fullTypeName.substring(fullTypeName.indexOf(":")+1);

                            if (!"attach".equals(type)){
                                //find namespace for the prefix
                                String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (Attach) ExtensionMapper.getTypeObject(
                                        nsUri, type, reader);
                              }


                  }


                }




                // Note all attributes that were handled. Used to differ normal attributes
                // from anyAttributes.
                java.util.Vector handledAttributes = new java.util.Vector();




                    reader.next();


                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                                    if (reader.isStartElement() && new javax.xml.namespace.QName("","fileContent").equals(reader.getName())){

                                            object.setFileContent(org.apache.axiom.util.stax.XMLStreamReaderUtils.getDataHandlerFromElement(reader));

                                        reader.next();

                              }  // End of if for expected property start element

                                    else {

                                    }


                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                                    if (reader.isStartElement() && new javax.xml.namespace.QName("","fileName").equals(reader.getName())){

                                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                    if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                        throw new org.apache.axis2.databinding.ADBException("The element: "+"fileName" +"  cannot be null");
                                    }


                                    String content = reader.getElementText();

                                              object.setFileName(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                                        reader.next();

                              }  // End of if for expected property start element

                                    else {

                                    }


                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                                    if (reader.isStartElement() && new javax.xml.namespace.QName("","id").equals(reader.getName())){

                                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                    if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                        throw new org.apache.axis2.databinding.ADBException("The element: "+"id" +"  cannot be null");
                                    }


                                    String content = reader.getElementText();

                                              object.setId(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                                        reader.next();

                              }  // End of if for expected property start element

                                    else {

                                    }

                            while (!reader.isStartElement() && !reader.isEndElement())
                                reader.next();

                                if (reader.isStartElement())
                                // A start element we are not expecting indicates a trailing invalid property
                                throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());




            } catch (javax.xml.stream.XMLStreamException e) {
                throw new Exception(e);
            }

            return object;
        }

        }//end of factory class



        }


        public static class RecuperoFileResult
        implements org.apache.axis2.databinding.ADBBean{
        /* This type was generated from the piece of schema that had
                name = recuperoFileResult
                Namespace URI = http://ws.idsunitelm.it/
                Namespace Prefix = ns1
                */


                        /**
                        * field for ContentType
                        */


                                    protected String localContentType ;

                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localContentTypeTracker = false ;

                           public boolean isContentTypeSpecified(){
                               return localContentTypeTracker;
                           }



                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  String getContentType(){
                               return localContentType;
                           }



                            /**
                               * Auto generated setter method
                               * @param param ContentType
                               */
                               public void setContentType(String param){
                            localContentTypeTracker = param != null;

                                            this.localContentType=param;


                               }


                        /**
                        * field for ContenutoFile
                        */


                                    protected javax.activation.DataHandler localContenutoFile ;

                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localContenutoFileTracker = false ;

                           public boolean isContenutoFileSpecified(){
                               return localContenutoFileTracker;
                           }



                           /**
                           * Auto generated getter method
                           * @return javax.activation.DataHandler
                           */
                           public  javax.activation.DataHandler getContenutoFile(){
                               return localContenutoFile;
                           }



                            /**
                               * Auto generated setter method
                               * @param param ContenutoFile
                               */
                               public void setContenutoFile(javax.activation.DataHandler param){
                            localContenutoFileTracker = param != null;

                                            this.localContenutoFile=param;


                               }


                        /**
                        * field for Error_description
                        */


                                    protected String localError_description ;

                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localError_descriptionTracker = false ;

                           public boolean isError_descriptionSpecified(){
                               return localError_descriptionTracker;
                           }



                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  String getError_description(){
                               return localError_description;
                           }



                            /**
                               * Auto generated setter method
                               * @param param Error_description
                               */
                               public void setError_description(String param){
                            localError_descriptionTracker = param != null;

                                            this.localError_description=param;


                               }


                        /**
                        * field for Error_number
                        */


                                    protected int localError_number ;


                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getError_number(){
                               return localError_number;
                           }



                            /**
                               * Auto generated setter method
                               * @param param Error_number
                               */
                               public void setError_number(int param){

                                            this.localError_number=param;


                               }


                        /**
                        * field for NomeFile
                        */


                                    protected String localNomeFile ;

                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localNomeFileTracker = false ;

                           public boolean isNomeFileSpecified(){
                               return localNomeFileTracker;
                           }



                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  String getNomeFile(){
                               return localNomeFile;
                           }



                            /**
                               * Auto generated setter method
                               * @param param NomeFile
                               */
                               public void setNomeFile(String param){
                            localNomeFileTracker = param != null;

                                            this.localNomeFile=param;


                               }




        /**
        *
        * @param parentQName
        * @param factory
        * @return org.apache.axiom.om.OMElement
        */
       public org.apache.axiom.om.OMElement getOMElement (
               final javax.xml.namespace.QName parentQName,
               final org.apache.axiom.om.OMFactory factory) throws org.apache.axis2.databinding.ADBException{



               org.apache.axiom.om.OMDataSource dataSource =
                       new org.apache.axis2.databinding.ADBDataSource(this,parentQName);
               return factory.createOMElement(dataSource,parentQName);

        }

         public void serialize(final javax.xml.namespace.QName parentQName,
                                       javax.xml.stream.XMLStreamWriter xmlWriter)
                                throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{
                           serialize(parentQName,xmlWriter,false);
         }

         public void serialize(final javax.xml.namespace.QName parentQName,
                               javax.xml.stream.XMLStreamWriter xmlWriter,
                               boolean serializeType)
            throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{




                String prefix = null;
                String namespace = null;


                    prefix = parentQName.getPrefix();
                    namespace = parentQName.getNamespaceURI();
                    writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

                  if (serializeType){


                   String namespacePrefix = registerPrefix(xmlWriter,"http://ws.idsunitelm.it/");
                   if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                       writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                           namespacePrefix+":recuperoFileResult",
                           xmlWriter);
                   } else {
                       writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                           "recuperoFileResult",
                           xmlWriter);
                   }


                   }
                if (localContentTypeTracker){
                                    namespace = "";
                                    writeStartElement(null, namespace, "contentType", xmlWriter);


                                          if (localContentType==null){
                                              // write the nil attribute

                                                     throw new org.apache.axis2.databinding.ADBException("contentType cannot be null!!");

                                          }else{


                                                   xmlWriter.writeCharacters(localContentType);

                                          }

                                   xmlWriter.writeEndElement();
                             } if (localContenutoFileTracker){
                                    namespace = "";
                                    writeStartElement(null, namespace, "contenutoFile", xmlWriter);


                                    if (localContenutoFile!=null)  {
                                       try {
                                           org.apache.axiom.util.stax.XMLStreamWriterUtils.writeDataHandler(xmlWriter, localContenutoFile, null, true);
                                       } catch (java.io.IOException ex) {
                                           throw new javax.xml.stream.XMLStreamException("Unable to read data handler for contenutoFile", ex);
                                       }
                                    } else {

                                    }

                                   xmlWriter.writeEndElement();
                             } if (localError_descriptionTracker){
                                    namespace = "";
                                    writeStartElement(null, namespace, "error_description", xmlWriter);


                                          if (localError_description==null){
                                              // write the nil attribute

                                                     throw new org.apache.axis2.databinding.ADBException("error_description cannot be null!!");

                                          }else{


                                                   xmlWriter.writeCharacters(localError_description);

                                          }

                                   xmlWriter.writeEndElement();
                             }
                                    namespace = "";
                                    writeStartElement(null, namespace, "error_number", xmlWriter);

                                               if (localError_number== Integer.MIN_VALUE) {

                                                         throw new org.apache.axis2.databinding.ADBException("error_number cannot be null!!");

                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localError_number));
                                               }

                                   xmlWriter.writeEndElement();
                              if (localNomeFileTracker){
                                    namespace = "";
                                    writeStartElement(null, namespace, "nomeFile", xmlWriter);


                                          if (localNomeFile==null){
                                              // write the nil attribute

                                                     throw new org.apache.axis2.databinding.ADBException("nomeFile cannot be null!!");

                                          }else{


                                                   xmlWriter.writeCharacters(localNomeFile);

                                          }

                                   xmlWriter.writeEndElement();
                             }
                    xmlWriter.writeEndElement();


        }

        private static String generatePrefix(String namespace) {
            if(namespace.equals("http://ws.idsunitelm.it/")){
                return "ns1";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
         * Utility method to write an element start tag.
         */
        private void writeStartElement(String prefix, String namespace, String localPart,
                                       javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            String writerPrefix = xmlWriter.getPrefix(namespace);
            if (writerPrefix != null) {
                xmlWriter.writeStartElement(namespace, localPart);
            } else {
                if (namespace.length() == 0) {
                    prefix = "";
                } else if (prefix == null) {
                    prefix = generatePrefix(namespace);
                }

                xmlWriter.writeStartElement(prefix, localPart, namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
        }

        /**
         * Util method to write an attribute with the ns prefix
         */
        private void writeAttribute(String prefix,String namespace,String attName,
                                    String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (xmlWriter.getPrefix(namespace) == null) {
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            xmlWriter.writeAttribute(namespace,attName,attValue);
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeAttribute(String namespace,String attName,
                                    String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (namespace.equals("")) {
                xmlWriter.writeAttribute(attName,attValue);
            } else {
                registerPrefix(xmlWriter, namespace);
                xmlWriter.writeAttribute(namespace,attName,attValue);
            }
        }


           /**
             * Util method to write an attribute without the ns prefix
             */
            private void writeQNameAttribute(String namespace, String attName,
                                             javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

                String attributeNamespace = qname.getNamespaceURI();
                String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
                if (attributePrefix == null) {
                    attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
                }
                String attributeValue;
                if (attributePrefix.trim().length() > 0) {
                    attributeValue = attributePrefix + ":" + qname.getLocalPart();
                } else {
                    attributeValue = qname.getLocalPart();
                }

                if (namespace.equals("")) {
                    xmlWriter.writeAttribute(attName, attributeValue);
                } else {
                    registerPrefix(xmlWriter, namespace);
                    xmlWriter.writeAttribute(namespace, attName, attributeValue);
                }
            }
        /**
         *  method to handle Qnames
         */

        private void writeQName(javax.xml.namespace.QName qname,
                                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            String namespaceURI = qname.getNamespaceURI();
            if (namespaceURI != null) {
                String prefix = xmlWriter.getPrefix(namespaceURI);
                if (prefix == null) {
                    prefix = generatePrefix(namespaceURI);
                    xmlWriter.writeNamespace(prefix, namespaceURI);
                    xmlWriter.setPrefix(prefix,namespaceURI);
                }

                if (prefix.trim().length() > 0){
                    xmlWriter.writeCharacters(prefix + ":" + org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                } else {
                    // i.e this is the default namespace
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                }

            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
            }
        }

        private void writeQNames(javax.xml.namespace.QName[] qnames,
                                 javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            if (qnames != null) {
                // we have to store this data until last moment since it is not possible to write any
                // namespace data after writing the charactor data
                StringBuffer stringToWrite = new StringBuffer();
                String namespaceURI = null;
                String prefix = null;

                for (int i = 0; i < qnames.length; i++) {
                    if (i > 0) {
                        stringToWrite.append(" ");
                    }
                    namespaceURI = qnames[i].getNamespaceURI();
                    if (namespaceURI != null) {
                        prefix = xmlWriter.getPrefix(namespaceURI);
                        if ((prefix == null) || (prefix.length() == 0)) {
                            prefix = generatePrefix(namespaceURI);
                            xmlWriter.writeNamespace(prefix, namespaceURI);
                            xmlWriter.setPrefix(prefix,namespaceURI);
                        }

                        if (prefix.trim().length() > 0){
                            stringToWrite.append(prefix).append(":").append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        } else {
                            stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        }
                    } else {
                        stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                    }
                }
                xmlWriter.writeCharacters(stringToWrite.toString());
            }

        }


        /**
         * Register a namespace prefix
         */
        private String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, String namespace) throws javax.xml.stream.XMLStreamException {
            String prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null) {
                prefix = generatePrefix(namespace);
                javax.xml.namespace.NamespaceContext nsContext = xmlWriter.getNamespaceContext();
                while (true) {
                    String uri = nsContext.getNamespaceURI(prefix);
                    if (uri == null || uri.length() == 0) {
                        break;
                    }
                    prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
                }
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            return prefix;
        }



        /**
        * databinding method to get an XML representation of this object
        *
        */
        public javax.xml.stream.XMLStreamReader getPullParser(javax.xml.namespace.QName qName)
                    throws org.apache.axis2.databinding.ADBException{



                 java.util.ArrayList elementList = new java.util.ArrayList();
                 java.util.ArrayList attribList = new java.util.ArrayList();

                 if (localContentTypeTracker){
                                      elementList.add(new javax.xml.namespace.QName("",
                                                                      "contentType"));

                                        if (localContentType != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localContentType));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("contentType cannot be null!!");
                                        }
                                    } if (localContenutoFileTracker){
                                      elementList.add(new javax.xml.namespace.QName("",
                                        "contenutoFile"));

                            elementList.add(localContenutoFile);
                        } if (localError_descriptionTracker){
                                      elementList.add(new javax.xml.namespace.QName("",
                                                                      "error_description"));

                                        if (localError_description != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localError_description));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("error_description cannot be null!!");
                                        }
                                    }
                                      elementList.add(new javax.xml.namespace.QName("",
                                                                      "error_number"));

                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localError_number));
                             if (localNomeFileTracker){
                                      elementList.add(new javax.xml.namespace.QName("",
                                                                      "nomeFile"));

                                        if (localNomeFile != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localNomeFile));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("nomeFile cannot be null!!");
                                        }
                                    }

                return new org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl(qName, elementList.toArray(), attribList.toArray());



        }



     /**
      *  Factory class that keeps the parse method
      */
    public static class Factory{




        /**
        * static method to create the object
        * Precondition:  If this object is an element, the current or next start element starts this object and any intervening reader events are ignorable
        *                If this object is not an element, it is a complex type and the reader is at the event just after the outer start element
        * Postcondition: If this object is an element, the reader is positioned at its end element
        *                If this object is a complex type, the reader is positioned at the end element of its outer element
        */
        public static RecuperoFileResult parse(javax.xml.stream.XMLStreamReader reader) throws Exception{
            RecuperoFileResult object =
                new RecuperoFileResult();

            int event;
            String nillableValue = null;
            String prefix ="";
            String namespaceuri ="";
            try {

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();


                if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","type")!=null){
                  String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                        "type");
                  if (fullTypeName!=null){
                    String nsPrefix = null;
                    if (fullTypeName.indexOf(":") > -1){
                        nsPrefix = fullTypeName.substring(0,fullTypeName.indexOf(":"));
                    }
                    nsPrefix = nsPrefix==null?"":nsPrefix;

                    String type = fullTypeName.substring(fullTypeName.indexOf(":")+1);

                            if (!"recuperoFileResult".equals(type)){
                                //find namespace for the prefix
                                String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (RecuperoFileResult) ExtensionMapper.getTypeObject(
                                        nsUri, type, reader);
                              }


                  }


                }




                // Note all attributes that were handled. Used to differ normal attributes
                // from anyAttributes.
                java.util.Vector handledAttributes = new java.util.Vector();




                    reader.next();


                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                                    if (reader.isStartElement() && new javax.xml.namespace.QName("","contentType").equals(reader.getName())){

                                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                    if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                        throw new org.apache.axis2.databinding.ADBException("The element: "+"contentType" +"  cannot be null");
                                    }


                                    String content = reader.getElementText();

                                              object.setContentType(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                                        reader.next();

                              }  // End of if for expected property start element

                                    else {

                                    }


                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                                    if (reader.isStartElement() && new javax.xml.namespace.QName("","contenutoFile").equals(reader.getName())){

                                            object.setContenutoFile(org.apache.axiom.util.stax.XMLStreamReaderUtils.getDataHandlerFromElement(reader));

                                        reader.next();

                              }  // End of if for expected property start element

                                    else {

                                    }


                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                                    if (reader.isStartElement() && new javax.xml.namespace.QName("","error_description").equals(reader.getName())){

                                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                    if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                        throw new org.apache.axis2.databinding.ADBException("The element: "+"error_description" +"  cannot be null");
                                    }


                                    String content = reader.getElementText();

                                              object.setError_description(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                                        reader.next();

                              }  // End of if for expected property start element

                                    else {

                                    }


                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                                    if (reader.isStartElement() && new javax.xml.namespace.QName("","error_number").equals(reader.getName())){

                                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                    if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                        throw new org.apache.axis2.databinding.ADBException("The element: "+"error_number" +"  cannot be null");
                                    }


                                    String content = reader.getElementText();

                                              object.setError_number(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));

                                        reader.next();

                              }  // End of if for expected property start element

                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                                }


                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                                    if (reader.isStartElement() && new javax.xml.namespace.QName("","nomeFile").equals(reader.getName())){

                                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                    if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                        throw new org.apache.axis2.databinding.ADBException("The element: "+"nomeFile" +"  cannot be null");
                                    }


                                    String content = reader.getElementText();

                                              object.setNomeFile(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                                        reader.next();

                              }  // End of if for expected property start element

                                    else {

                                    }

                            while (!reader.isStartElement() && !reader.isEndElement())
                                reader.next();

                                if (reader.isStartElement())
                                // A start element we are not expecting indicates a trailing invalid property
                                throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());




            } catch (javax.xml.stream.XMLStreamException e) {
                throw new Exception(e);
            }

            return object;
        }

        }//end of factory class



        }


        public static class ProtocollazioneDocumentoResponse
        implements org.apache.axis2.databinding.ADBBean{
        /* This type was generated from the piece of schema that had
                name = protocollazioneDocumentoResponse
                Namespace URI = http://ws.idsunitelm.it/
                Namespace Prefix = ns1
                */


                        /**
                        * field for _return
                        */


                                    protected InserimentoResult local_return ;

                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean local_returnTracker = false ;

                           public boolean is_returnSpecified(){
                               return local_returnTracker;
                           }



                           /**
                           * Auto generated getter method
                           * @return InserimentoResult
                           */
                           public  InserimentoResult get_return(){
                               return local_return;
                           }



                            /**
                               * Auto generated setter method
                               * @param param _return
                               */
                               public void set_return(InserimentoResult param){
                            local_returnTracker = param != null;

                                            this.local_return=param;


                               }




        /**
        *
        * @param parentQName
        * @param factory
        * @return org.apache.axiom.om.OMElement
        */
       public org.apache.axiom.om.OMElement getOMElement (
               final javax.xml.namespace.QName parentQName,
               final org.apache.axiom.om.OMFactory factory) throws org.apache.axis2.databinding.ADBException{



               org.apache.axiom.om.OMDataSource dataSource =
                       new org.apache.axis2.databinding.ADBDataSource(this,parentQName);
               return factory.createOMElement(dataSource,parentQName);

        }

         public void serialize(final javax.xml.namespace.QName parentQName,
                                       javax.xml.stream.XMLStreamWriter xmlWriter)
                                throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{
                           serialize(parentQName,xmlWriter,false);
         }

         public void serialize(final javax.xml.namespace.QName parentQName,
                               javax.xml.stream.XMLStreamWriter xmlWriter,
                               boolean serializeType)
            throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{




                String prefix = null;
                String namespace = null;


                    prefix = parentQName.getPrefix();
                    namespace = parentQName.getNamespaceURI();
                    writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

                  if (serializeType){


                   String namespacePrefix = registerPrefix(xmlWriter,"http://ws.idsunitelm.it/");
                   if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                       writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                           namespacePrefix+":protocollazioneDocumentoResponse",
                           xmlWriter);
                   } else {
                       writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                           "protocollazioneDocumentoResponse",
                           xmlWriter);
                   }


                   }
                if (local_returnTracker){
                                            if (local_return==null){
                                                 throw new org.apache.axis2.databinding.ADBException("return cannot be null!!");
                                            }
                                           local_return.serialize(new javax.xml.namespace.QName("","return"),
                                               xmlWriter);
                                        }
                    xmlWriter.writeEndElement();


        }

        private static String generatePrefix(String namespace) {
            if(namespace.equals("http://ws.idsunitelm.it/")){
                return "ns1";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
         * Utility method to write an element start tag.
         */
        private void writeStartElement(String prefix, String namespace, String localPart,
                                       javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            String writerPrefix = xmlWriter.getPrefix(namespace);
            if (writerPrefix != null) {
                xmlWriter.writeStartElement(namespace, localPart);
            } else {
                if (namespace.length() == 0) {
                    prefix = "";
                } else if (prefix == null) {
                    prefix = generatePrefix(namespace);
                }

                xmlWriter.writeStartElement(prefix, localPart, namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
        }

        /**
         * Util method to write an attribute with the ns prefix
         */
        private void writeAttribute(String prefix,String namespace,String attName,
                                    String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (xmlWriter.getPrefix(namespace) == null) {
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            xmlWriter.writeAttribute(namespace,attName,attValue);
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeAttribute(String namespace,String attName,
                                    String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (namespace.equals("")) {
                xmlWriter.writeAttribute(attName,attValue);
            } else {
                registerPrefix(xmlWriter, namespace);
                xmlWriter.writeAttribute(namespace,attName,attValue);
            }
        }


           /**
             * Util method to write an attribute without the ns prefix
             */
            private void writeQNameAttribute(String namespace, String attName,
                                             javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

                String attributeNamespace = qname.getNamespaceURI();
                String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
                if (attributePrefix == null) {
                    attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
                }
                String attributeValue;
                if (attributePrefix.trim().length() > 0) {
                    attributeValue = attributePrefix + ":" + qname.getLocalPart();
                } else {
                    attributeValue = qname.getLocalPart();
                }

                if (namespace.equals("")) {
                    xmlWriter.writeAttribute(attName, attributeValue);
                } else {
                    registerPrefix(xmlWriter, namespace);
                    xmlWriter.writeAttribute(namespace, attName, attributeValue);
                }
            }
        /**
         *  method to handle Qnames
         */

        private void writeQName(javax.xml.namespace.QName qname,
                                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            String namespaceURI = qname.getNamespaceURI();
            if (namespaceURI != null) {
                String prefix = xmlWriter.getPrefix(namespaceURI);
                if (prefix == null) {
                    prefix = generatePrefix(namespaceURI);
                    xmlWriter.writeNamespace(prefix, namespaceURI);
                    xmlWriter.setPrefix(prefix,namespaceURI);
                }

                if (prefix.trim().length() > 0){
                    xmlWriter.writeCharacters(prefix + ":" + org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                } else {
                    // i.e this is the default namespace
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                }

            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
            }
        }

        private void writeQNames(javax.xml.namespace.QName[] qnames,
                                 javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            if (qnames != null) {
                // we have to store this data until last moment since it is not possible to write any
                // namespace data after writing the charactor data
                StringBuffer stringToWrite = new StringBuffer();
                String namespaceURI = null;
                String prefix = null;

                for (int i = 0; i < qnames.length; i++) {
                    if (i > 0) {
                        stringToWrite.append(" ");
                    }
                    namespaceURI = qnames[i].getNamespaceURI();
                    if (namespaceURI != null) {
                        prefix = xmlWriter.getPrefix(namespaceURI);
                        if ((prefix == null) || (prefix.length() == 0)) {
                            prefix = generatePrefix(namespaceURI);
                            xmlWriter.writeNamespace(prefix, namespaceURI);
                            xmlWriter.setPrefix(prefix,namespaceURI);
                        }

                        if (prefix.trim().length() > 0){
                            stringToWrite.append(prefix).append(":").append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        } else {
                            stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        }
                    } else {
                        stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                    }
                }
                xmlWriter.writeCharacters(stringToWrite.toString());
            }

        }


        /**
         * Register a namespace prefix
         */
        private String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, String namespace) throws javax.xml.stream.XMLStreamException {
            String prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null) {
                prefix = generatePrefix(namespace);
                javax.xml.namespace.NamespaceContext nsContext = xmlWriter.getNamespaceContext();
                while (true) {
                    String uri = nsContext.getNamespaceURI(prefix);
                    if (uri == null || uri.length() == 0) {
                        break;
                    }
                    prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
                }
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            return prefix;
        }



        /**
        * databinding method to get an XML representation of this object
        *
        */
        public javax.xml.stream.XMLStreamReader getPullParser(javax.xml.namespace.QName qName)
                    throws org.apache.axis2.databinding.ADBException{



                 java.util.ArrayList elementList = new java.util.ArrayList();
                 java.util.ArrayList attribList = new java.util.ArrayList();

                 if (local_returnTracker){
                            elementList.add(new javax.xml.namespace.QName("",
                                                                      "return"));


                                    if (local_return==null){
                                         throw new org.apache.axis2.databinding.ADBException("return cannot be null!!");
                                    }
                                    elementList.add(local_return);
                                }

                return new org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl(qName, elementList.toArray(), attribList.toArray());



        }



     /**
      *  Factory class that keeps the parse method
      */
    public static class Factory{




        /**
        * static method to create the object
        * Precondition:  If this object is an element, the current or next start element starts this object and any intervening reader events are ignorable
        *                If this object is not an element, it is a complex type and the reader is at the event just after the outer start element
        * Postcondition: If this object is an element, the reader is positioned at its end element
        *                If this object is a complex type, the reader is positioned at the end element of its outer element
        */
        public static ProtocollazioneDocumentoResponse parse(javax.xml.stream.XMLStreamReader reader) throws Exception{
            ProtocollazioneDocumentoResponse object =
                new ProtocollazioneDocumentoResponse();

            int event;
            String nillableValue = null;
            String prefix ="";
            String namespaceuri ="";
            try {

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();


                if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","type")!=null){
                  String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                        "type");
                  if (fullTypeName!=null){
                    String nsPrefix = null;
                    if (fullTypeName.indexOf(":") > -1){
                        nsPrefix = fullTypeName.substring(0,fullTypeName.indexOf(":"));
                    }
                    nsPrefix = nsPrefix==null?"":nsPrefix;

                    String type = fullTypeName.substring(fullTypeName.indexOf(":")+1);

                            if (!"protocollazioneDocumentoResponse".equals(type)){
                                //find namespace for the prefix
                                String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (ProtocollazioneDocumentoResponse) ExtensionMapper.getTypeObject(
                                        nsUri, type, reader);
                              }


                  }


                }




                // Note all attributes that were handled. Used to differ normal attributes
                // from anyAttributes.
                java.util.Vector handledAttributes = new java.util.Vector();




                    reader.next();


                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                                    if (reader.isStartElement() && new javax.xml.namespace.QName("","return").equals(reader.getName())){

                                                object.set_return(InserimentoResult.Factory.parse(reader));

                                        reader.next();

                              }  // End of if for expected property start element

                                    else {

                                    }

                            while (!reader.isStartElement() && !reader.isEndElement())
                                reader.next();

                                if (reader.isStartElement())
                                // A start element we are not expecting indicates a trailing invalid property
                                throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());




            } catch (javax.xml.stream.XMLStreamException e) {
                throw new Exception(e);
            }

            return object;
        }

        }//end of factory class



        }


        public static class Login
        implements org.apache.axis2.databinding.ADBBean{
        /* This type was generated from the piece of schema that had
                name = login
                Namespace URI = http://ws.idsunitelm.it/
                Namespace Prefix = ns1
                */


                        /**
                        * field for Username
                        */


                                    protected String localUsername ;

                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localUsernameTracker = false ;

                           public boolean isUsernameSpecified(){
                               return localUsernameTracker;
                           }



                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  String getUsername(){
                               return localUsername;
                           }



                            /**
                               * Auto generated setter method
                               * @param param Username
                               */
                               public void setUsername(String param){
                            localUsernameTracker = param != null;

                                            this.localUsername=param;


                               }


                        /**
                        * field for Password
                        */


                                    protected String localPassword ;

                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localPasswordTracker = false ;

                           public boolean isPasswordSpecified(){
                               return localPasswordTracker;
                           }



                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  String getPassword(){
                               return localPassword;
                           }



                            /**
                               * Auto generated setter method
                               * @param param Password
                               */
                               public void setPassword(String param){
                            localPasswordTracker = param != null;

                                            this.localPassword=param;


                               }




        /**
        *
        * @param parentQName
        * @param factory
        * @return org.apache.axiom.om.OMElement
        */
       public org.apache.axiom.om.OMElement getOMElement (
               final javax.xml.namespace.QName parentQName,
               final org.apache.axiom.om.OMFactory factory) throws org.apache.axis2.databinding.ADBException{



               org.apache.axiom.om.OMDataSource dataSource =
                       new org.apache.axis2.databinding.ADBDataSource(this,parentQName);
               return factory.createOMElement(dataSource,parentQName);

        }

         public void serialize(final javax.xml.namespace.QName parentQName,
                                       javax.xml.stream.XMLStreamWriter xmlWriter)
                                throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{
                           serialize(parentQName,xmlWriter,false);
         }

         public void serialize(final javax.xml.namespace.QName parentQName,
                               javax.xml.stream.XMLStreamWriter xmlWriter,
                               boolean serializeType)
            throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{




                String prefix = null;
                String namespace = null;


                    prefix = parentQName.getPrefix();
                    namespace = parentQName.getNamespaceURI();
                    writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

                  if (serializeType){


                   String namespacePrefix = registerPrefix(xmlWriter,"http://ws.idsunitelm.it/");
                   if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                       writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                           namespacePrefix+":login",
                           xmlWriter);
                   } else {
                       writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                           "login",
                           xmlWriter);
                   }


                   }
                if (localUsernameTracker){
                                    namespace = "";
                                    writeStartElement(null, namespace, "username", xmlWriter);


                                          if (localUsername==null){
                                              // write the nil attribute

                                                     throw new org.apache.axis2.databinding.ADBException("username cannot be null!!");

                                          }else{


                                                   xmlWriter.writeCharacters(localUsername);

                                          }

                                   xmlWriter.writeEndElement();
                             } if (localPasswordTracker){
                                    namespace = "";
                                    writeStartElement(null, namespace, "password", xmlWriter);


                                          if (localPassword==null){
                                              // write the nil attribute

                                                     throw new org.apache.axis2.databinding.ADBException("password cannot be null!!");

                                          }else{


                                                   xmlWriter.writeCharacters(localPassword);

                                          }

                                   xmlWriter.writeEndElement();
                             }
                    xmlWriter.writeEndElement();


        }

        private static String generatePrefix(String namespace) {
            if(namespace.equals("http://ws.idsunitelm.it/")){
                return "ns1";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
         * Utility method to write an element start tag.
         */
        private void writeStartElement(String prefix, String namespace, String localPart,
                                       javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            String writerPrefix = xmlWriter.getPrefix(namespace);
            if (writerPrefix != null) {
                xmlWriter.writeStartElement(namespace, localPart);
            } else {
                if (namespace.length() == 0) {
                    prefix = "";
                } else if (prefix == null) {
                    prefix = generatePrefix(namespace);
                }

                xmlWriter.writeStartElement(prefix, localPart, namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
        }

        /**
         * Util method to write an attribute with the ns prefix
         */
        private void writeAttribute(String prefix,String namespace,String attName,
                                    String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (xmlWriter.getPrefix(namespace) == null) {
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            xmlWriter.writeAttribute(namespace,attName,attValue);
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeAttribute(String namespace,String attName,
                                    String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (namespace.equals("")) {
                xmlWriter.writeAttribute(attName,attValue);
            } else {
                registerPrefix(xmlWriter, namespace);
                xmlWriter.writeAttribute(namespace,attName,attValue);
            }
        }


           /**
             * Util method to write an attribute without the ns prefix
             */
            private void writeQNameAttribute(String namespace, String attName,
                                             javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

                String attributeNamespace = qname.getNamespaceURI();
                String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
                if (attributePrefix == null) {
                    attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
                }
                String attributeValue;
                if (attributePrefix.trim().length() > 0) {
                    attributeValue = attributePrefix + ":" + qname.getLocalPart();
                } else {
                    attributeValue = qname.getLocalPart();
                }

                if (namespace.equals("")) {
                    xmlWriter.writeAttribute(attName, attributeValue);
                } else {
                    registerPrefix(xmlWriter, namespace);
                    xmlWriter.writeAttribute(namespace, attName, attributeValue);
                }
            }
        /**
         *  method to handle Qnames
         */

        private void writeQName(javax.xml.namespace.QName qname,
                                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            String namespaceURI = qname.getNamespaceURI();
            if (namespaceURI != null) {
                String prefix = xmlWriter.getPrefix(namespaceURI);
                if (prefix == null) {
                    prefix = generatePrefix(namespaceURI);
                    xmlWriter.writeNamespace(prefix, namespaceURI);
                    xmlWriter.setPrefix(prefix,namespaceURI);
                }

                if (prefix.trim().length() > 0){
                    xmlWriter.writeCharacters(prefix + ":" + org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                } else {
                    // i.e this is the default namespace
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                }

            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
            }
        }

        private void writeQNames(javax.xml.namespace.QName[] qnames,
                                 javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            if (qnames != null) {
                // we have to store this data until last moment since it is not possible to write any
                // namespace data after writing the charactor data
                StringBuffer stringToWrite = new StringBuffer();
                String namespaceURI = null;
                String prefix = null;

                for (int i = 0; i < qnames.length; i++) {
                    if (i > 0) {
                        stringToWrite.append(" ");
                    }
                    namespaceURI = qnames[i].getNamespaceURI();
                    if (namespaceURI != null) {
                        prefix = xmlWriter.getPrefix(namespaceURI);
                        if ((prefix == null) || (prefix.length() == 0)) {
                            prefix = generatePrefix(namespaceURI);
                            xmlWriter.writeNamespace(prefix, namespaceURI);
                            xmlWriter.setPrefix(prefix,namespaceURI);
                        }

                        if (prefix.trim().length() > 0){
                            stringToWrite.append(prefix).append(":").append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        } else {
                            stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        }
                    } else {
                        stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                    }
                }
                xmlWriter.writeCharacters(stringToWrite.toString());
            }

        }


        /**
         * Register a namespace prefix
         */
        private String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, String namespace) throws javax.xml.stream.XMLStreamException {
            String prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null) {
                prefix = generatePrefix(namespace);
                javax.xml.namespace.NamespaceContext nsContext = xmlWriter.getNamespaceContext();
                while (true) {
                    String uri = nsContext.getNamespaceURI(prefix);
                    if (uri == null || uri.length() == 0) {
                        break;
                    }
                    prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
                }
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            return prefix;
        }



        /**
        * databinding method to get an XML representation of this object
        *
        */
        public javax.xml.stream.XMLStreamReader getPullParser(javax.xml.namespace.QName qName)
                    throws org.apache.axis2.databinding.ADBException{



                 java.util.ArrayList elementList = new java.util.ArrayList();
                 java.util.ArrayList attribList = new java.util.ArrayList();

                 if (localUsernameTracker){
                                      elementList.add(new javax.xml.namespace.QName("",
                                                                      "username"));

                                        if (localUsername != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localUsername));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("username cannot be null!!");
                                        }
                                    } if (localPasswordTracker){
                                      elementList.add(new javax.xml.namespace.QName("",
                                                                      "password"));

                                        if (localPassword != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localPassword));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("password cannot be null!!");
                                        }
                                    }

                return new org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl(qName, elementList.toArray(), attribList.toArray());



        }



     /**
      *  Factory class that keeps the parse method
      */
    public static class Factory{




        /**
        * static method to create the object
        * Precondition:  If this object is an element, the current or next start element starts this object and any intervening reader events are ignorable
        *                If this object is not an element, it is a complex type and the reader is at the event just after the outer start element
        * Postcondition: If this object is an element, the reader is positioned at its end element
        *                If this object is a complex type, the reader is positioned at the end element of its outer element
        */
        public static Login parse(javax.xml.stream.XMLStreamReader reader) throws Exception{
            Login object =
                new Login();

            int event;
            String nillableValue = null;
            String prefix ="";
            String namespaceuri ="";
            try {

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();


                if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","type")!=null){
                  String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                        "type");
                  if (fullTypeName!=null){
                    String nsPrefix = null;
                    if (fullTypeName.indexOf(":") > -1){
                        nsPrefix = fullTypeName.substring(0,fullTypeName.indexOf(":"));
                    }
                    nsPrefix = nsPrefix==null?"":nsPrefix;

                    String type = fullTypeName.substring(fullTypeName.indexOf(":")+1);

                            if (!"login".equals(type)){
                                //find namespace for the prefix
                                String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (Login) ExtensionMapper.getTypeObject(
                                        nsUri, type, reader);
                              }


                  }


                }




                // Note all attributes that were handled. Used to differ normal attributes
                // from anyAttributes.
                java.util.Vector handledAttributes = new java.util.Vector();




                    reader.next();


                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                                    if (reader.isStartElement() && new javax.xml.namespace.QName("","username").equals(reader.getName())){

                                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                    if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                        throw new org.apache.axis2.databinding.ADBException("The element: "+"username" +"  cannot be null");
                                    }


                                    String content = reader.getElementText();

                                              object.setUsername(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                                        reader.next();

                              }  // End of if for expected property start element

                                    else {

                                    }


                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                                    if (reader.isStartElement() && new javax.xml.namespace.QName("","password").equals(reader.getName())){

                                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                    if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                        throw new org.apache.axis2.databinding.ADBException("The element: "+"password" +"  cannot be null");
                                    }


                                    String content = reader.getElementText();

                                              object.setPassword(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                                        reader.next();

                              }  // End of if for expected property start element

                                    else {

                                    }

                            while (!reader.isStartElement() && !reader.isEndElement())
                                reader.next();

                                if (reader.isStartElement())
                                // A start element we are not expecting indicates a trailing invalid property
                                throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());




            } catch (javax.xml.stream.XMLStreamException e) {
                throw new Exception(e);
            }

            return object;
        }

        }//end of factory class



        }


        public static class CreazioneFascicoloResponseE
        implements org.apache.axis2.databinding.ADBBean{

                public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName(
                "http://ws.idsunitelm.it/",
                "creazioneFascicoloResponse",
                "ns1");



                        /**
                        * field for CreazioneFascicoloResponse
                        */


                                    protected CreazioneFascicoloResponse localCreazioneFascicoloResponse ;


                           /**
                           * Auto generated getter method
                           * @return CreazioneFascicoloResponse
                           */
                           public  CreazioneFascicoloResponse getCreazioneFascicoloResponse(){
                               return localCreazioneFascicoloResponse;
                           }



                            /**
                               * Auto generated setter method
                               * @param param CreazioneFascicoloResponse
                               */
                               public void setCreazioneFascicoloResponse(CreazioneFascicoloResponse param){

                                            this.localCreazioneFascicoloResponse=param;


                               }




        /**
        *
        * @param parentQName
        * @param factory
        * @return org.apache.axiom.om.OMElement
        */
       public org.apache.axiom.om.OMElement getOMElement (
               final javax.xml.namespace.QName parentQName,
               final org.apache.axiom.om.OMFactory factory) throws org.apache.axis2.databinding.ADBException{



               org.apache.axiom.om.OMDataSource dataSource =
                       new org.apache.axis2.databinding.ADBDataSource(this,MY_QNAME);
               return factory.createOMElement(dataSource,MY_QNAME);

        }

         public void serialize(final javax.xml.namespace.QName parentQName,
                                       javax.xml.stream.XMLStreamWriter xmlWriter)
                                throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{
                           serialize(parentQName,xmlWriter,false);
         }

         public void serialize(final javax.xml.namespace.QName parentQName,
                               javax.xml.stream.XMLStreamWriter xmlWriter,
                               boolean serializeType)
            throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{


                //We can safely assume an element has only one type associated with it

                                 if (localCreazioneFascicoloResponse==null){
                                   throw new org.apache.axis2.databinding.ADBException("creazioneFascicoloResponse cannot be null!");
                                 }
                                 localCreazioneFascicoloResponse.serialize(MY_QNAME,xmlWriter);


        }

        private static String generatePrefix(String namespace) {
            if(namespace.equals("http://ws.idsunitelm.it/")){
                return "ns1";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
         * Utility method to write an element start tag.
         */
        private void writeStartElement(String prefix, String namespace, String localPart,
                                       javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            String writerPrefix = xmlWriter.getPrefix(namespace);
            if (writerPrefix != null) {
                xmlWriter.writeStartElement(namespace, localPart);
            } else {
                if (namespace.length() == 0) {
                    prefix = "";
                } else if (prefix == null) {
                    prefix = generatePrefix(namespace);
                }

                xmlWriter.writeStartElement(prefix, localPart, namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
        }

        /**
         * Util method to write an attribute with the ns prefix
         */
        private void writeAttribute(String prefix,String namespace,String attName,
                                    String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (xmlWriter.getPrefix(namespace) == null) {
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            xmlWriter.writeAttribute(namespace,attName,attValue);
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeAttribute(String namespace,String attName,
                                    String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (namespace.equals("")) {
                xmlWriter.writeAttribute(attName,attValue);
            } else {
                registerPrefix(xmlWriter, namespace);
                xmlWriter.writeAttribute(namespace,attName,attValue);
            }
        }


           /**
             * Util method to write an attribute without the ns prefix
             */
            private void writeQNameAttribute(String namespace, String attName,
                                             javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

                String attributeNamespace = qname.getNamespaceURI();
                String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
                if (attributePrefix == null) {
                    attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
                }
                String attributeValue;
                if (attributePrefix.trim().length() > 0) {
                    attributeValue = attributePrefix + ":" + qname.getLocalPart();
                } else {
                    attributeValue = qname.getLocalPart();
                }

                if (namespace.equals("")) {
                    xmlWriter.writeAttribute(attName, attributeValue);
                } else {
                    registerPrefix(xmlWriter, namespace);
                    xmlWriter.writeAttribute(namespace, attName, attributeValue);
                }
            }
        /**
         *  method to handle Qnames
         */

        private void writeQName(javax.xml.namespace.QName qname,
                                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            String namespaceURI = qname.getNamespaceURI();
            if (namespaceURI != null) {
                String prefix = xmlWriter.getPrefix(namespaceURI);
                if (prefix == null) {
                    prefix = generatePrefix(namespaceURI);
                    xmlWriter.writeNamespace(prefix, namespaceURI);
                    xmlWriter.setPrefix(prefix,namespaceURI);
                }

                if (prefix.trim().length() > 0){
                    xmlWriter.writeCharacters(prefix + ":" + org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                } else {
                    // i.e this is the default namespace
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                }

            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
            }
        }

        private void writeQNames(javax.xml.namespace.QName[] qnames,
                                 javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            if (qnames != null) {
                // we have to store this data until last moment since it is not possible to write any
                // namespace data after writing the charactor data
                StringBuffer stringToWrite = new StringBuffer();
                String namespaceURI = null;
                String prefix = null;

                for (int i = 0; i < qnames.length; i++) {
                    if (i > 0) {
                        stringToWrite.append(" ");
                    }
                    namespaceURI = qnames[i].getNamespaceURI();
                    if (namespaceURI != null) {
                        prefix = xmlWriter.getPrefix(namespaceURI);
                        if ((prefix == null) || (prefix.length() == 0)) {
                            prefix = generatePrefix(namespaceURI);
                            xmlWriter.writeNamespace(prefix, namespaceURI);
                            xmlWriter.setPrefix(prefix,namespaceURI);
                        }

                        if (prefix.trim().length() > 0){
                            stringToWrite.append(prefix).append(":").append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        } else {
                            stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        }
                    } else {
                        stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                    }
                }
                xmlWriter.writeCharacters(stringToWrite.toString());
            }

        }


        /**
         * Register a namespace prefix
         */
        private String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, String namespace) throws javax.xml.stream.XMLStreamException {
            String prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null) {
                prefix = generatePrefix(namespace);
                javax.xml.namespace.NamespaceContext nsContext = xmlWriter.getNamespaceContext();
                while (true) {
                    String uri = nsContext.getNamespaceURI(prefix);
                    if (uri == null || uri.length() == 0) {
                        break;
                    }
                    prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
                }
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            return prefix;
        }



        /**
        * databinding method to get an XML representation of this object
        *
        */
        public javax.xml.stream.XMLStreamReader getPullParser(javax.xml.namespace.QName qName)
                    throws org.apache.axis2.databinding.ADBException{




                //We can safely assume an element has only one type associated with it
                return localCreazioneFascicoloResponse.getPullParser(MY_QNAME);

        }



     /**
      *  Factory class that keeps the parse method
      */
    public static class Factory{




        /**
        * static method to create the object
        * Precondition:  If this object is an element, the current or next start element starts this object and any intervening reader events are ignorable
        *                If this object is not an element, it is a complex type and the reader is at the event just after the outer start element
        * Postcondition: If this object is an element, the reader is positioned at its end element
        *                If this object is a complex type, the reader is positioned at the end element of its outer element
        */
        public static CreazioneFascicoloResponseE parse(javax.xml.stream.XMLStreamReader reader) throws Exception{
            CreazioneFascicoloResponseE object =
                new CreazioneFascicoloResponseE();

            int event;
            String nillableValue = null;
            String prefix ="";
            String namespaceuri ="";
            try {

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();




                // Note all attributes that were handled. Used to differ normal attributes
                // from anyAttributes.
                java.util.Vector handledAttributes = new java.util.Vector();



                while(!reader.isEndElement()) {
                    if (reader.isStartElement() ){

                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://ws.idsunitelm.it/","creazioneFascicoloResponse").equals(reader.getName())){

                                                object.setCreazioneFascicoloResponse(CreazioneFascicoloResponse.Factory.parse(reader));

                              }  // End of if for expected property start element

                             else{
                                        // A start element we are not expecting indicates an invalid parameter was passed
                                        throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                             }

                             } else {
                                reader.next();
                             }
                           }  // end of while loop




            } catch (javax.xml.stream.XMLStreamException e) {
                throw new Exception(e);
            }

            return object;
        }

        }//end of factory class



        }


        public static class ExtensionMapper{

          public static Object getTypeObject(String namespaceURI,
                                                       String typeName,
                                                       javax.xml.stream.XMLStreamReader reader) throws Exception{


                  if (
                  "http://ws.idsunitelm.it/".equals(namespaceURI) &&
                  "loginResponse".equals(typeName)){

                            return  LoginResponse.Factory.parse(reader);


                  }


                  if (
                  "http://ws.idsunitelm.it/".equals(namespaceURI) &&
                  "creazioneFascicoloResponse".equals(typeName)){

                            return  CreazioneFascicoloResponse.Factory.parse(reader);


                  }


                  if (
                  "http://ws.idsunitelm.it/".equals(namespaceURI) &&
                  "recuperoFileResult".equals(typeName)){

                            return  RecuperoFileResult.Factory.parse(reader);


                  }


                  if (
                  "http://ws.idsunitelm.it/".equals(namespaceURI) &&
                  "fascicoloResult".equals(typeName)){

                            return  FascicoloResult.Factory.parse(reader);


                  }


                  if (
                  "http://ws.idsunitelm.it/".equals(namespaceURI) &&
                  "attach".equals(typeName)){

                            return  Attach.Factory.parse(reader);


                  }


                  if (
                  "http://ws.idsunitelm.it/".equals(namespaceURI) &&
                  "recuperoDocumento".equals(typeName)){

                            return  RecuperoDocumento.Factory.parse(reader);


                  }


                  if (
                  "http://ws.idsunitelm.it/".equals(namespaceURI) &&
                  "recuperoDocumentoResponse".equals(typeName)){

                            return  RecuperoDocumentoResponse.Factory.parse(reader);


                  }


                  if (
                  "http://ws.idsunitelm.it/".equals(namespaceURI) &&
                  "loginResult".equals(typeName)){

                            return  LoginResult.Factory.parse(reader);


                  }


                  if (
                  "http://ws.idsunitelm.it/".equals(namespaceURI) &&
                  "protocollazioneDocumentoResponse".equals(typeName)){

                            return  ProtocollazioneDocumentoResponse.Factory.parse(reader);


                  }


                  if (
                  "http://ws.idsunitelm.it/".equals(namespaceURI) &&
                  "creazioneFascicolo".equals(typeName)){

                            return  CreazioneFascicolo.Factory.parse(reader);


                  }


                  if (
                  "http://ws.idsunitelm.it/".equals(namespaceURI) &&
                  "protocollazioneDocumento".equals(typeName)){

                            return  ProtocollazioneDocumento.Factory.parse(reader);


                  }


                  if (
                  "http://ws.idsunitelm.it/".equals(namespaceURI) &&
                  "inserimentoResult".equals(typeName)){

                            return  InserimentoResult.Factory.parse(reader);


                  }


                  if (
                  "http://ws.idsunitelm.it/".equals(namespaceURI) &&
                  "login".equals(typeName)){

                            return  Login.Factory.parse(reader);


                  }


             throw new org.apache.axis2.databinding.ADBException("Unsupported type " + namespaceURI + " " + typeName);
          }

        }

        public static class CreazioneFascicolo
        implements org.apache.axis2.databinding.ADBBean{
        /* This type was generated from the piece of schema that had
                name = creazioneFascicolo
                Namespace URI = http://ws.idsunitelm.it/
                Namespace Prefix = ns1
                */


                        /**
                        * field for DST
                        */


                                    protected String localDST ;

                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localDSTTracker = false ;

                           public boolean isDSTSpecified(){
                               return localDSTTracker;
                           }



                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  String getDST(){
                               return localDST;
                           }



                            /**
                               * Auto generated setter method
                               * @param param DST
                               */
                               public void setDST(String param){
                            localDSTTracker = param != null;

                                            this.localDST=param;


                               }


                        /**
                        * field for Arg1
                        */


                                    protected javax.activation.DataHandler localArg1 ;

                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localArg1Tracker = false ;

                           public boolean isArg1Specified(){
                               return localArg1Tracker;
                           }



                           /**
                           * Auto generated getter method
                           * @return javax.activation.DataHandler
                           */
                           public  javax.activation.DataHandler getArg1(){
                               return localArg1;
                           }



                            /**
                               * Auto generated setter method
                               * @param param Arg1
                               */
                               public void setArg1(javax.activation.DataHandler param){
                            localArg1Tracker = param != null;

                                            this.localArg1=param;


                               }




        /**
        *
        * @param parentQName
        * @param factory
        * @return org.apache.axiom.om.OMElement
        */
       public org.apache.axiom.om.OMElement getOMElement (
               final javax.xml.namespace.QName parentQName,
               final org.apache.axiom.om.OMFactory factory) throws org.apache.axis2.databinding.ADBException{



               org.apache.axiom.om.OMDataSource dataSource =
                       new org.apache.axis2.databinding.ADBDataSource(this,parentQName);
               return factory.createOMElement(dataSource,parentQName);

        }

         public void serialize(final javax.xml.namespace.QName parentQName,
                                       javax.xml.stream.XMLStreamWriter xmlWriter)
                                throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{
                           serialize(parentQName,xmlWriter,false);
         }

         public void serialize(final javax.xml.namespace.QName parentQName,
                               javax.xml.stream.XMLStreamWriter xmlWriter,
                               boolean serializeType)
            throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{




                String prefix = null;
                String namespace = null;


                    prefix = parentQName.getPrefix();
                    namespace = parentQName.getNamespaceURI();
                    writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

                  if (serializeType){


                   String namespacePrefix = registerPrefix(xmlWriter,"http://ws.idsunitelm.it/");
                   if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                       writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                           namespacePrefix+":creazioneFascicolo",
                           xmlWriter);
                   } else {
                       writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                           "creazioneFascicolo",
                           xmlWriter);
                   }


                   }
                if (localDSTTracker){
                                    namespace = "";
                                    writeStartElement(null, namespace, "DST", xmlWriter);


                                          if (localDST==null){
                                              // write the nil attribute

                                                     throw new org.apache.axis2.databinding.ADBException("DST cannot be null!!");

                                          }else{


                                                   xmlWriter.writeCharacters(localDST);

                                          }

                                   xmlWriter.writeEndElement();
                             } if (localArg1Tracker){
                                    namespace = "";
                                    writeStartElement(null, namespace, "arg1", xmlWriter);


                                    if (localArg1!=null)  {
                                       try {
                                           org.apache.axiom.util.stax.XMLStreamWriterUtils.writeDataHandler(xmlWriter, localArg1, null, true);
                                       } catch (java.io.IOException ex) {
                                           throw new javax.xml.stream.XMLStreamException("Unable to read data handler for arg1", ex);
                                       }
                                    } else {

                                    }

                                   xmlWriter.writeEndElement();
                             }
                    xmlWriter.writeEndElement();


        }

        private static String generatePrefix(String namespace) {
            if(namespace.equals("http://ws.idsunitelm.it/")){
                return "ns1";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
         * Utility method to write an element start tag.
         */
        private void writeStartElement(String prefix, String namespace, String localPart,
                                       javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            String writerPrefix = xmlWriter.getPrefix(namespace);
            if (writerPrefix != null) {
                xmlWriter.writeStartElement(namespace, localPart);
            } else {
                if (namespace.length() == 0) {
                    prefix = "";
                } else if (prefix == null) {
                    prefix = generatePrefix(namespace);
                }

                xmlWriter.writeStartElement(prefix, localPart, namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
        }

        /**
         * Util method to write an attribute with the ns prefix
         */
        private void writeAttribute(String prefix,String namespace,String attName,
                                    String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (xmlWriter.getPrefix(namespace) == null) {
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            xmlWriter.writeAttribute(namespace,attName,attValue);
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeAttribute(String namespace,String attName,
                                    String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (namespace.equals("")) {
                xmlWriter.writeAttribute(attName,attValue);
            } else {
                registerPrefix(xmlWriter, namespace);
                xmlWriter.writeAttribute(namespace,attName,attValue);
            }
        }


           /**
             * Util method to write an attribute without the ns prefix
             */
            private void writeQNameAttribute(String namespace, String attName,
                                             javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

                String attributeNamespace = qname.getNamespaceURI();
                String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
                if (attributePrefix == null) {
                    attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
                }
                String attributeValue;
                if (attributePrefix.trim().length() > 0) {
                    attributeValue = attributePrefix + ":" + qname.getLocalPart();
                } else {
                    attributeValue = qname.getLocalPart();
                }

                if (namespace.equals("")) {
                    xmlWriter.writeAttribute(attName, attributeValue);
                } else {
                    registerPrefix(xmlWriter, namespace);
                    xmlWriter.writeAttribute(namespace, attName, attributeValue);
                }
            }
        /**
         *  method to handle Qnames
         */

        private void writeQName(javax.xml.namespace.QName qname,
                                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            String namespaceURI = qname.getNamespaceURI();
            if (namespaceURI != null) {
                String prefix = xmlWriter.getPrefix(namespaceURI);
                if (prefix == null) {
                    prefix = generatePrefix(namespaceURI);
                    xmlWriter.writeNamespace(prefix, namespaceURI);
                    xmlWriter.setPrefix(prefix,namespaceURI);
                }

                if (prefix.trim().length() > 0){
                    xmlWriter.writeCharacters(prefix + ":" + org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                } else {
                    // i.e this is the default namespace
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                }

            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
            }
        }

        private void writeQNames(javax.xml.namespace.QName[] qnames,
                                 javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            if (qnames != null) {
                // we have to store this data until last moment since it is not possible to write any
                // namespace data after writing the charactor data
                StringBuffer stringToWrite = new StringBuffer();
                String namespaceURI = null;
                String prefix = null;

                for (int i = 0; i < qnames.length; i++) {
                    if (i > 0) {
                        stringToWrite.append(" ");
                    }
                    namespaceURI = qnames[i].getNamespaceURI();
                    if (namespaceURI != null) {
                        prefix = xmlWriter.getPrefix(namespaceURI);
                        if ((prefix == null) || (prefix.length() == 0)) {
                            prefix = generatePrefix(namespaceURI);
                            xmlWriter.writeNamespace(prefix, namespaceURI);
                            xmlWriter.setPrefix(prefix,namespaceURI);
                        }

                        if (prefix.trim().length() > 0){
                            stringToWrite.append(prefix).append(":").append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        } else {
                            stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        }
                    } else {
                        stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                    }
                }
                xmlWriter.writeCharacters(stringToWrite.toString());
            }

        }


        /**
         * Register a namespace prefix
         */
        private String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, String namespace) throws javax.xml.stream.XMLStreamException {
            String prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null) {
                prefix = generatePrefix(namespace);
                javax.xml.namespace.NamespaceContext nsContext = xmlWriter.getNamespaceContext();
                while (true) {
                    String uri = nsContext.getNamespaceURI(prefix);
                    if (uri == null || uri.length() == 0) {
                        break;
                    }
                    prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
                }
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            return prefix;
        }



        /**
        * databinding method to get an XML representation of this object
        *
        */
        public javax.xml.stream.XMLStreamReader getPullParser(javax.xml.namespace.QName qName)
                    throws org.apache.axis2.databinding.ADBException{



                 java.util.ArrayList elementList = new java.util.ArrayList();
                 java.util.ArrayList attribList = new java.util.ArrayList();

                 if (localDSTTracker){
                                      elementList.add(new javax.xml.namespace.QName("",
                                                                      "DST"));

                                        if (localDST != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDST));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("DST cannot be null!!");
                                        }
                                    } if (localArg1Tracker){
                                      elementList.add(new javax.xml.namespace.QName("",
                                        "arg1"));

                            elementList.add(localArg1);
                        }

                return new org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl(qName, elementList.toArray(), attribList.toArray());



        }



     /**
      *  Factory class that keeps the parse method
      */
    public static class Factory{




        /**
        * static method to create the object
        * Precondition:  If this object is an element, the current or next start element starts this object and any intervening reader events are ignorable
        *                If this object is not an element, it is a complex type and the reader is at the event just after the outer start element
        * Postcondition: If this object is an element, the reader is positioned at its end element
        *                If this object is a complex type, the reader is positioned at the end element of its outer element
        */
        public static CreazioneFascicolo parse(javax.xml.stream.XMLStreamReader reader) throws Exception{
            CreazioneFascicolo object =
                new CreazioneFascicolo();

            int event;
            String nillableValue = null;
            String prefix ="";
            String namespaceuri ="";
            try {

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();


                if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","type")!=null){
                  String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                        "type");
                  if (fullTypeName!=null){
                    String nsPrefix = null;
                    if (fullTypeName.indexOf(":") > -1){
                        nsPrefix = fullTypeName.substring(0,fullTypeName.indexOf(":"));
                    }
                    nsPrefix = nsPrefix==null?"":nsPrefix;

                    String type = fullTypeName.substring(fullTypeName.indexOf(":")+1);

                            if (!"creazioneFascicolo".equals(type)){
                                //find namespace for the prefix
                                String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (CreazioneFascicolo) ExtensionMapper.getTypeObject(
                                        nsUri, type, reader);
                              }


                  }


                }




                // Note all attributes that were handled. Used to differ normal attributes
                // from anyAttributes.
                java.util.Vector handledAttributes = new java.util.Vector();




                    reader.next();


                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                                    if (reader.isStartElement() && new javax.xml.namespace.QName("","DST").equals(reader.getName())){

                                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                    if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                        throw new org.apache.axis2.databinding.ADBException("The element: "+"DST" +"  cannot be null");
                                    }


                                    String content = reader.getElementText();

                                              object.setDST(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                                        reader.next();

                              }  // End of if for expected property start element

                                    else {

                                    }


                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                                    if (reader.isStartElement() && new javax.xml.namespace.QName("","arg1").equals(reader.getName())){

                                            object.setArg1(org.apache.axiom.util.stax.XMLStreamReaderUtils.getDataHandlerFromElement(reader));

                                        reader.next();

                              }  // End of if for expected property start element

                                    else {

                                    }

                            while (!reader.isStartElement() && !reader.isEndElement())
                                reader.next();

                                if (reader.isStartElement())
                                // A start element we are not expecting indicates a trailing invalid property
                                throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());




            } catch (javax.xml.stream.XMLStreamException e) {
                throw new Exception(e);
            }

            return object;
        }

        }//end of factory class



        }


        public static class RecuperoDocumento
        implements org.apache.axis2.databinding.ADBBean{
        /* This type was generated from the piece of schema that had
                name = recuperoDocumento
                Namespace URI = http://ws.idsunitelm.it/
                Namespace Prefix = ns1
                */


                        /**
                        * field for DST
                        */


                                    protected String localDST ;

                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localDSTTracker = false ;

                           public boolean isDSTSpecified(){
                               return localDSTTracker;
                           }



                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  String getDST(){
                               return localDST;
                           }



                            /**
                               * Auto generated setter method
                               * @param param DST
                               */
                               public void setDST(String param){
                            localDSTTracker = param != null;

                                            this.localDST=param;


                               }


                        /**
                        * field for LngAnnoPG
                        */


                                    protected long localLngAnnoPG ;


                           /**
                           * Auto generated getter method
                           * @return long
                           */
                           public  long getLngAnnoPG(){
                               return localLngAnnoPG;
                           }



                            /**
                               * Auto generated setter method
                               * @param param LngAnnoPG
                               */
                               public void setLngAnnoPG(long param){

                                            this.localLngAnnoPG=param;


                               }


                        /**
                        * field for LngNumPG
                        */


                                    protected long localLngNumPG ;


                           /**
                           * Auto generated getter method
                           * @return long
                           */
                           public  long getLngNumPG(){
                               return localLngNumPG;
                           }



                            /**
                               * Auto generated setter method
                               * @param param LngNumPG
                               */
                               public void setLngNumPG(long param){

                                            this.localLngNumPG=param;


                               }




        /**
        *
        * @param parentQName
        * @param factory
        * @return org.apache.axiom.om.OMElement
        */
       public org.apache.axiom.om.OMElement getOMElement (
               final javax.xml.namespace.QName parentQName,
               final org.apache.axiom.om.OMFactory factory) throws org.apache.axis2.databinding.ADBException{



               org.apache.axiom.om.OMDataSource dataSource =
                       new org.apache.axis2.databinding.ADBDataSource(this,parentQName);
               return factory.createOMElement(dataSource,parentQName);

        }

         public void serialize(final javax.xml.namespace.QName parentQName,
                                       javax.xml.stream.XMLStreamWriter xmlWriter)
                                throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{
                           serialize(parentQName,xmlWriter,false);
         }

         public void serialize(final javax.xml.namespace.QName parentQName,
                               javax.xml.stream.XMLStreamWriter xmlWriter,
                               boolean serializeType)
            throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{




                String prefix = null;
                String namespace = null;


                    prefix = parentQName.getPrefix();
                    namespace = parentQName.getNamespaceURI();
                    writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

                  if (serializeType){


                   String namespacePrefix = registerPrefix(xmlWriter,"http://ws.idsunitelm.it/");
                   if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                       writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                           namespacePrefix+":recuperoDocumento",
                           xmlWriter);
                   } else {
                       writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                           "recuperoDocumento",
                           xmlWriter);
                   }


                   }
                if (localDSTTracker){
                                    namespace = "";
                                    writeStartElement(null, namespace, "DST", xmlWriter);


                                          if (localDST==null){
                                              // write the nil attribute

                                                     throw new org.apache.axis2.databinding.ADBException("DST cannot be null!!");

                                          }else{


                                                   xmlWriter.writeCharacters(localDST);

                                          }

                                   xmlWriter.writeEndElement();
                             }
                                    namespace = "";
                                    writeStartElement(null, namespace, "lngAnnoPG", xmlWriter);

                                               if (localLngAnnoPG== Long.MIN_VALUE) {

                                                         throw new org.apache.axis2.databinding.ADBException("lngAnnoPG cannot be null!!");

                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localLngAnnoPG));
                                               }

                                   xmlWriter.writeEndElement();

                                    namespace = "";
                                    writeStartElement(null, namespace, "lngNumPG", xmlWriter);

                                               if (localLngNumPG== Long.MIN_VALUE) {

                                                         throw new org.apache.axis2.databinding.ADBException("lngNumPG cannot be null!!");

                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localLngNumPG));
                                               }

                                   xmlWriter.writeEndElement();

                    xmlWriter.writeEndElement();


        }

        private static String generatePrefix(String namespace) {
            if(namespace.equals("http://ws.idsunitelm.it/")){
                return "ns1";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
         * Utility method to write an element start tag.
         */
        private void writeStartElement(String prefix, String namespace, String localPart,
                                       javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            String writerPrefix = xmlWriter.getPrefix(namespace);
            if (writerPrefix != null) {
                xmlWriter.writeStartElement(namespace, localPart);
            } else {
                if (namespace.length() == 0) {
                    prefix = "";
                } else if (prefix == null) {
                    prefix = generatePrefix(namespace);
                }

                xmlWriter.writeStartElement(prefix, localPart, namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
        }

        /**
         * Util method to write an attribute with the ns prefix
         */
        private void writeAttribute(String prefix,String namespace,String attName,
                                    String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (xmlWriter.getPrefix(namespace) == null) {
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            xmlWriter.writeAttribute(namespace,attName,attValue);
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeAttribute(String namespace,String attName,
                                    String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (namespace.equals("")) {
                xmlWriter.writeAttribute(attName,attValue);
            } else {
                registerPrefix(xmlWriter, namespace);
                xmlWriter.writeAttribute(namespace,attName,attValue);
            }
        }


           /**
             * Util method to write an attribute without the ns prefix
             */
            private void writeQNameAttribute(String namespace, String attName,
                                             javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

                String attributeNamespace = qname.getNamespaceURI();
                String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
                if (attributePrefix == null) {
                    attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
                }
                String attributeValue;
                if (attributePrefix.trim().length() > 0) {
                    attributeValue = attributePrefix + ":" + qname.getLocalPart();
                } else {
                    attributeValue = qname.getLocalPart();
                }

                if (namespace.equals("")) {
                    xmlWriter.writeAttribute(attName, attributeValue);
                } else {
                    registerPrefix(xmlWriter, namespace);
                    xmlWriter.writeAttribute(namespace, attName, attributeValue);
                }
            }
        /**
         *  method to handle Qnames
         */

        private void writeQName(javax.xml.namespace.QName qname,
                                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            String namespaceURI = qname.getNamespaceURI();
            if (namespaceURI != null) {
                String prefix = xmlWriter.getPrefix(namespaceURI);
                if (prefix == null) {
                    prefix = generatePrefix(namespaceURI);
                    xmlWriter.writeNamespace(prefix, namespaceURI);
                    xmlWriter.setPrefix(prefix,namespaceURI);
                }

                if (prefix.trim().length() > 0){
                    xmlWriter.writeCharacters(prefix + ":" + org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                } else {
                    // i.e this is the default namespace
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                }

            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
            }
        }

        private void writeQNames(javax.xml.namespace.QName[] qnames,
                                 javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            if (qnames != null) {
                // we have to store this data until last moment since it is not possible to write any
                // namespace data after writing the charactor data
                StringBuffer stringToWrite = new StringBuffer();
                String namespaceURI = null;
                String prefix = null;

                for (int i = 0; i < qnames.length; i++) {
                    if (i > 0) {
                        stringToWrite.append(" ");
                    }
                    namespaceURI = qnames[i].getNamespaceURI();
                    if (namespaceURI != null) {
                        prefix = xmlWriter.getPrefix(namespaceURI);
                        if ((prefix == null) || (prefix.length() == 0)) {
                            prefix = generatePrefix(namespaceURI);
                            xmlWriter.writeNamespace(prefix, namespaceURI);
                            xmlWriter.setPrefix(prefix,namespaceURI);
                        }

                        if (prefix.trim().length() > 0){
                            stringToWrite.append(prefix).append(":").append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        } else {
                            stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        }
                    } else {
                        stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                    }
                }
                xmlWriter.writeCharacters(stringToWrite.toString());
            }

        }


        /**
         * Register a namespace prefix
         */
        private String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, String namespace) throws javax.xml.stream.XMLStreamException {
            String prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null) {
                prefix = generatePrefix(namespace);
                javax.xml.namespace.NamespaceContext nsContext = xmlWriter.getNamespaceContext();
                while (true) {
                    String uri = nsContext.getNamespaceURI(prefix);
                    if (uri == null || uri.length() == 0) {
                        break;
                    }
                    prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
                }
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            return prefix;
        }



        /**
        * databinding method to get an XML representation of this object
        *
        */
        public javax.xml.stream.XMLStreamReader getPullParser(javax.xml.namespace.QName qName)
                    throws org.apache.axis2.databinding.ADBException{



                 java.util.ArrayList elementList = new java.util.ArrayList();
                 java.util.ArrayList attribList = new java.util.ArrayList();

                 if (localDSTTracker){
                                      elementList.add(new javax.xml.namespace.QName("",
                                                                      "DST"));

                                        if (localDST != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDST));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("DST cannot be null!!");
                                        }
                                    }
                                      elementList.add(new javax.xml.namespace.QName("",
                                                                      "lngAnnoPG"));

                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localLngAnnoPG));

                                      elementList.add(new javax.xml.namespace.QName("",
                                                                      "lngNumPG"));

                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localLngNumPG));


                return new org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl(qName, elementList.toArray(), attribList.toArray());



        }



     /**
      *  Factory class that keeps the parse method
      */
    public static class Factory{




        /**
        * static method to create the object
        * Precondition:  If this object is an element, the current or next start element starts this object and any intervening reader events are ignorable
        *                If this object is not an element, it is a complex type and the reader is at the event just after the outer start element
        * Postcondition: If this object is an element, the reader is positioned at its end element
        *                If this object is a complex type, the reader is positioned at the end element of its outer element
        */
        public static RecuperoDocumento parse(javax.xml.stream.XMLStreamReader reader) throws Exception{
            RecuperoDocumento object =
                new RecuperoDocumento();

            int event;
            String nillableValue = null;
            String prefix ="";
            String namespaceuri ="";
            try {

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();


                if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","type")!=null){
                  String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                        "type");
                  if (fullTypeName!=null){
                    String nsPrefix = null;
                    if (fullTypeName.indexOf(":") > -1){
                        nsPrefix = fullTypeName.substring(0,fullTypeName.indexOf(":"));
                    }
                    nsPrefix = nsPrefix==null?"":nsPrefix;

                    String type = fullTypeName.substring(fullTypeName.indexOf(":")+1);

                            if (!"recuperoDocumento".equals(type)){
                                //find namespace for the prefix
                                String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (RecuperoDocumento) ExtensionMapper.getTypeObject(
                                        nsUri, type, reader);
                              }


                  }


                }




                // Note all attributes that were handled. Used to differ normal attributes
                // from anyAttributes.
                java.util.Vector handledAttributes = new java.util.Vector();




                    reader.next();


                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                                    if (reader.isStartElement() && new javax.xml.namespace.QName("","DST").equals(reader.getName())){

                                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                    if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                        throw new org.apache.axis2.databinding.ADBException("The element: "+"DST" +"  cannot be null");
                                    }


                                    String content = reader.getElementText();

                                              object.setDST(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                                        reader.next();

                              }  // End of if for expected property start element

                                    else {

                                    }


                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                                    if (reader.isStartElement() && new javax.xml.namespace.QName("","lngAnnoPG").equals(reader.getName())){

                                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                    if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                        throw new org.apache.axis2.databinding.ADBException("The element: "+"lngAnnoPG" +"  cannot be null");
                                    }


                                    String content = reader.getElementText();

                                              object.setLngAnnoPG(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToLong(content));

                                        reader.next();

                              }  // End of if for expected property start element

                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                                }


                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                                    if (reader.isStartElement() && new javax.xml.namespace.QName("","lngNumPG").equals(reader.getName())){

                                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                    if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                        throw new org.apache.axis2.databinding.ADBException("The element: "+"lngNumPG" +"  cannot be null");
                                    }


                                    String content = reader.getElementText();

                                              object.setLngNumPG(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToLong(content));

                                        reader.next();

                              }  // End of if for expected property start element

                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                                }

                            while (!reader.isStartElement() && !reader.isEndElement())
                                reader.next();

                                if (reader.isStartElement())
                                // A start element we are not expecting indicates a trailing invalid property
                                throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());




            } catch (javax.xml.stream.XMLStreamException e) {
                throw new Exception(e);
            }

            return object;
        }

        }//end of factory class



        }


        public static class LoginE
        implements org.apache.axis2.databinding.ADBBean{

                public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName(
                "http://ws.idsunitelm.it/",
                "login",
                "ns1");



                        /**
                        * field for Login
                        */


                                    protected Login localLogin ;


                           /**
                           * Auto generated getter method
                           * @return Login
                           */
                           public  Login getLogin(){
                               return localLogin;
                           }



                            /**
                               * Auto generated setter method
                               * @param param Login
                               */
                               public void setLogin(Login param){

                                            this.localLogin=param;


                               }




        /**
        *
        * @param parentQName
        * @param factory
        * @return org.apache.axiom.om.OMElement
        */
       public org.apache.axiom.om.OMElement getOMElement (
               final javax.xml.namespace.QName parentQName,
               final org.apache.axiom.om.OMFactory factory) throws org.apache.axis2.databinding.ADBException{



               org.apache.axiom.om.OMDataSource dataSource =
                       new org.apache.axis2.databinding.ADBDataSource(this,MY_QNAME);
               return factory.createOMElement(dataSource,MY_QNAME);

        }

         public void serialize(final javax.xml.namespace.QName parentQName,
                                       javax.xml.stream.XMLStreamWriter xmlWriter)
                                throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{
                           serialize(parentQName,xmlWriter,false);
         }

         public void serialize(final javax.xml.namespace.QName parentQName,
                               javax.xml.stream.XMLStreamWriter xmlWriter,
                               boolean serializeType)
            throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{


                //We can safely assume an element has only one type associated with it

                                 if (localLogin==null){
                                   throw new org.apache.axis2.databinding.ADBException("login cannot be null!");
                                 }
                                 localLogin.serialize(MY_QNAME,xmlWriter);


        }

        private static String generatePrefix(String namespace) {
            if(namespace.equals("http://ws.idsunitelm.it/")){
                return "ns1";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
         * Utility method to write an element start tag.
         */
        private void writeStartElement(String prefix, String namespace, String localPart,
                                       javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            String writerPrefix = xmlWriter.getPrefix(namespace);
            if (writerPrefix != null) {
                xmlWriter.writeStartElement(namespace, localPart);
            } else {
                if (namespace.length() == 0) {
                    prefix = "";
                } else if (prefix == null) {
                    prefix = generatePrefix(namespace);
                }

                xmlWriter.writeStartElement(prefix, localPart, namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
        }

        /**
         * Util method to write an attribute with the ns prefix
         */
        private void writeAttribute(String prefix,String namespace,String attName,
                                    String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (xmlWriter.getPrefix(namespace) == null) {
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            xmlWriter.writeAttribute(namespace,attName,attValue);
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeAttribute(String namespace,String attName,
                                    String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (namespace.equals("")) {
                xmlWriter.writeAttribute(attName,attValue);
            } else {
                registerPrefix(xmlWriter, namespace);
                xmlWriter.writeAttribute(namespace,attName,attValue);
            }
        }


           /**
             * Util method to write an attribute without the ns prefix
             */
            private void writeQNameAttribute(String namespace, String attName,
                                             javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

                String attributeNamespace = qname.getNamespaceURI();
                String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
                if (attributePrefix == null) {
                    attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
                }
                String attributeValue;
                if (attributePrefix.trim().length() > 0) {
                    attributeValue = attributePrefix + ":" + qname.getLocalPart();
                } else {
                    attributeValue = qname.getLocalPart();
                }

                if (namespace.equals("")) {
                    xmlWriter.writeAttribute(attName, attributeValue);
                } else {
                    registerPrefix(xmlWriter, namespace);
                    xmlWriter.writeAttribute(namespace, attName, attributeValue);
                }
            }
        /**
         *  method to handle Qnames
         */

        private void writeQName(javax.xml.namespace.QName qname,
                                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            String namespaceURI = qname.getNamespaceURI();
            if (namespaceURI != null) {
                String prefix = xmlWriter.getPrefix(namespaceURI);
                if (prefix == null) {
                    prefix = generatePrefix(namespaceURI);
                    xmlWriter.writeNamespace(prefix, namespaceURI);
                    xmlWriter.setPrefix(prefix,namespaceURI);
                }

                if (prefix.trim().length() > 0){
                    xmlWriter.writeCharacters(prefix + ":" + org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                } else {
                    // i.e this is the default namespace
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                }

            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
            }
        }

        private void writeQNames(javax.xml.namespace.QName[] qnames,
                                 javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            if (qnames != null) {
                // we have to store this data until last moment since it is not possible to write any
                // namespace data after writing the charactor data
                StringBuffer stringToWrite = new StringBuffer();
                String namespaceURI = null;
                String prefix = null;

                for (int i = 0; i < qnames.length; i++) {
                    if (i > 0) {
                        stringToWrite.append(" ");
                    }
                    namespaceURI = qnames[i].getNamespaceURI();
                    if (namespaceURI != null) {
                        prefix = xmlWriter.getPrefix(namespaceURI);
                        if ((prefix == null) || (prefix.length() == 0)) {
                            prefix = generatePrefix(namespaceURI);
                            xmlWriter.writeNamespace(prefix, namespaceURI);
                            xmlWriter.setPrefix(prefix,namespaceURI);
                        }

                        if (prefix.trim().length() > 0){
                            stringToWrite.append(prefix).append(":").append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        } else {
                            stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        }
                    } else {
                        stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                    }
                }
                xmlWriter.writeCharacters(stringToWrite.toString());
            }

        }


        /**
         * Register a namespace prefix
         */
        private String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, String namespace) throws javax.xml.stream.XMLStreamException {
            String prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null) {
                prefix = generatePrefix(namespace);
                javax.xml.namespace.NamespaceContext nsContext = xmlWriter.getNamespaceContext();
                while (true) {
                    String uri = nsContext.getNamespaceURI(prefix);
                    if (uri == null || uri.length() == 0) {
                        break;
                    }
                    prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
                }
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            return prefix;
        }



        /**
        * databinding method to get an XML representation of this object
        *
        */
        public javax.xml.stream.XMLStreamReader getPullParser(javax.xml.namespace.QName qName)
                    throws org.apache.axis2.databinding.ADBException{




                //We can safely assume an element has only one type associated with it
                return localLogin.getPullParser(MY_QNAME);

        }



     /**
      *  Factory class that keeps the parse method
      */
    public static class Factory{




        /**
        * static method to create the object
        * Precondition:  If this object is an element, the current or next start element starts this object and any intervening reader events are ignorable
        *                If this object is not an element, it is a complex type and the reader is at the event just after the outer start element
        * Postcondition: If this object is an element, the reader is positioned at its end element
        *                If this object is a complex type, the reader is positioned at the end element of its outer element
        */
        public static LoginE parse(javax.xml.stream.XMLStreamReader reader) throws Exception{
            LoginE object =
                new LoginE();

            int event;
            String nillableValue = null;
            String prefix ="";
            String namespaceuri ="";
            try {

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();




                // Note all attributes that were handled. Used to differ normal attributes
                // from anyAttributes.
                java.util.Vector handledAttributes = new java.util.Vector();



                while(!reader.isEndElement()) {
                    if (reader.isStartElement() ){

                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://ws.idsunitelm.it/","login").equals(reader.getName())){

                                                object.setLogin(Login.Factory.parse(reader));

                              }  // End of if for expected property start element

                             else{
                                        // A start element we are not expecting indicates an invalid parameter was passed
                                        throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                             }

                             } else {
                                reader.next();
                             }
                           }  // end of while loop




            } catch (javax.xml.stream.XMLStreamException e) {
                throw new Exception(e);
            }

            return object;
        }

        }//end of factory class



        }


        public static class RecuperoDocumentoResponse
        implements org.apache.axis2.databinding.ADBBean{
        /* This type was generated from the piece of schema that had
                name = recuperoDocumentoResponse
                Namespace URI = http://ws.idsunitelm.it/
                Namespace Prefix = ns1
                */


                        /**
                        * field for _return
                        */


                                    protected RecuperoFileResult local_return ;

                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean local_returnTracker = false ;

                           public boolean is_returnSpecified(){
                               return local_returnTracker;
                           }



                           /**
                           * Auto generated getter method
                           * @return RecuperoFileResult
                           */
                           public  RecuperoFileResult get_return(){
                               return local_return;
                           }



                            /**
                               * Auto generated setter method
                               * @param param _return
                               */
                               public void set_return(RecuperoFileResult param){
                            local_returnTracker = param != null;

                                            this.local_return=param;


                               }




        /**
        *
        * @param parentQName
        * @param factory
        * @return org.apache.axiom.om.OMElement
        */
       public org.apache.axiom.om.OMElement getOMElement (
               final javax.xml.namespace.QName parentQName,
               final org.apache.axiom.om.OMFactory factory) throws org.apache.axis2.databinding.ADBException{



               org.apache.axiom.om.OMDataSource dataSource =
                       new org.apache.axis2.databinding.ADBDataSource(this,parentQName);
               return factory.createOMElement(dataSource,parentQName);

        }

         public void serialize(final javax.xml.namespace.QName parentQName,
                                       javax.xml.stream.XMLStreamWriter xmlWriter)
                                throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{
                           serialize(parentQName,xmlWriter,false);
         }

         public void serialize(final javax.xml.namespace.QName parentQName,
                               javax.xml.stream.XMLStreamWriter xmlWriter,
                               boolean serializeType)
            throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{




                String prefix = null;
                String namespace = null;


                    prefix = parentQName.getPrefix();
                    namespace = parentQName.getNamespaceURI();
                    writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

                  if (serializeType){


                   String namespacePrefix = registerPrefix(xmlWriter,"http://ws.idsunitelm.it/");
                   if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                       writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                           namespacePrefix+":recuperoDocumentoResponse",
                           xmlWriter);
                   } else {
                       writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                           "recuperoDocumentoResponse",
                           xmlWriter);
                   }


                   }
                if (local_returnTracker){
                                            if (local_return==null){
                                                 throw new org.apache.axis2.databinding.ADBException("return cannot be null!!");
                                            }
                                           local_return.serialize(new javax.xml.namespace.QName("","return"),
                                               xmlWriter);
                                        }
                    xmlWriter.writeEndElement();


        }

        private static String generatePrefix(String namespace) {
            if(namespace.equals("http://ws.idsunitelm.it/")){
                return "ns1";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
         * Utility method to write an element start tag.
         */
        private void writeStartElement(String prefix, String namespace, String localPart,
                                       javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            String writerPrefix = xmlWriter.getPrefix(namespace);
            if (writerPrefix != null) {
                xmlWriter.writeStartElement(namespace, localPart);
            } else {
                if (namespace.length() == 0) {
                    prefix = "";
                } else if (prefix == null) {
                    prefix = generatePrefix(namespace);
                }

                xmlWriter.writeStartElement(prefix, localPart, namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
        }

        /**
         * Util method to write an attribute with the ns prefix
         */
        private void writeAttribute(String prefix,String namespace,String attName,
                                    String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (xmlWriter.getPrefix(namespace) == null) {
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            xmlWriter.writeAttribute(namespace,attName,attValue);
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeAttribute(String namespace,String attName,
                                    String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (namespace.equals("")) {
                xmlWriter.writeAttribute(attName,attValue);
            } else {
                registerPrefix(xmlWriter, namespace);
                xmlWriter.writeAttribute(namespace,attName,attValue);
            }
        }


           /**
             * Util method to write an attribute without the ns prefix
             */
            private void writeQNameAttribute(String namespace, String attName,
                                             javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

                String attributeNamespace = qname.getNamespaceURI();
                String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
                if (attributePrefix == null) {
                    attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
                }
                String attributeValue;
                if (attributePrefix.trim().length() > 0) {
                    attributeValue = attributePrefix + ":" + qname.getLocalPart();
                } else {
                    attributeValue = qname.getLocalPart();
                }

                if (namespace.equals("")) {
                    xmlWriter.writeAttribute(attName, attributeValue);
                } else {
                    registerPrefix(xmlWriter, namespace);
                    xmlWriter.writeAttribute(namespace, attName, attributeValue);
                }
            }
        /**
         *  method to handle Qnames
         */

        private void writeQName(javax.xml.namespace.QName qname,
                                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            String namespaceURI = qname.getNamespaceURI();
            if (namespaceURI != null) {
                String prefix = xmlWriter.getPrefix(namespaceURI);
                if (prefix == null) {
                    prefix = generatePrefix(namespaceURI);
                    xmlWriter.writeNamespace(prefix, namespaceURI);
                    xmlWriter.setPrefix(prefix,namespaceURI);
                }

                if (prefix.trim().length() > 0){
                    xmlWriter.writeCharacters(prefix + ":" + org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                } else {
                    // i.e this is the default namespace
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                }

            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
            }
        }

        private void writeQNames(javax.xml.namespace.QName[] qnames,
                                 javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            if (qnames != null) {
                // we have to store this data until last moment since it is not possible to write any
                // namespace data after writing the charactor data
                StringBuffer stringToWrite = new StringBuffer();
                String namespaceURI = null;
                String prefix = null;

                for (int i = 0; i < qnames.length; i++) {
                    if (i > 0) {
                        stringToWrite.append(" ");
                    }
                    namespaceURI = qnames[i].getNamespaceURI();
                    if (namespaceURI != null) {
                        prefix = xmlWriter.getPrefix(namespaceURI);
                        if ((prefix == null) || (prefix.length() == 0)) {
                            prefix = generatePrefix(namespaceURI);
                            xmlWriter.writeNamespace(prefix, namespaceURI);
                            xmlWriter.setPrefix(prefix,namespaceURI);
                        }

                        if (prefix.trim().length() > 0){
                            stringToWrite.append(prefix).append(":").append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        } else {
                            stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        }
                    } else {
                        stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                    }
                }
                xmlWriter.writeCharacters(stringToWrite.toString());
            }

        }


        /**
         * Register a namespace prefix
         */
        private String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, String namespace) throws javax.xml.stream.XMLStreamException {
            String prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null) {
                prefix = generatePrefix(namespace);
                javax.xml.namespace.NamespaceContext nsContext = xmlWriter.getNamespaceContext();
                while (true) {
                    String uri = nsContext.getNamespaceURI(prefix);
                    if (uri == null || uri.length() == 0) {
                        break;
                    }
                    prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
                }
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            return prefix;
        }



        /**
        * databinding method to get an XML representation of this object
        *
        */
        public javax.xml.stream.XMLStreamReader getPullParser(javax.xml.namespace.QName qName)
                    throws org.apache.axis2.databinding.ADBException{



                 java.util.ArrayList elementList = new java.util.ArrayList();
                 java.util.ArrayList attribList = new java.util.ArrayList();

                 if (local_returnTracker){
                            elementList.add(new javax.xml.namespace.QName("",
                                                                      "return"));


                                    if (local_return==null){
                                         throw new org.apache.axis2.databinding.ADBException("return cannot be null!!");
                                    }
                                    elementList.add(local_return);
                                }

                return new org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl(qName, elementList.toArray(), attribList.toArray());



        }



     /**
      *  Factory class that keeps the parse method
      */
    public static class Factory{




        /**
        * static method to create the object
        * Precondition:  If this object is an element, the current or next start element starts this object and any intervening reader events are ignorable
        *                If this object is not an element, it is a complex type and the reader is at the event just after the outer start element
        * Postcondition: If this object is an element, the reader is positioned at its end element
        *                If this object is a complex type, the reader is positioned at the end element of its outer element
        */
        public static RecuperoDocumentoResponse parse(javax.xml.stream.XMLStreamReader reader) throws Exception{
            RecuperoDocumentoResponse object =
                new RecuperoDocumentoResponse();

            int event;
            String nillableValue = null;
            String prefix ="";
            String namespaceuri ="";
            try {

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();


                if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","type")!=null){
                  String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                        "type");
                  if (fullTypeName!=null){
                    String nsPrefix = null;
                    if (fullTypeName.indexOf(":") > -1){
                        nsPrefix = fullTypeName.substring(0,fullTypeName.indexOf(":"));
                    }
                    nsPrefix = nsPrefix==null?"":nsPrefix;

                    String type = fullTypeName.substring(fullTypeName.indexOf(":")+1);

                            if (!"recuperoDocumentoResponse".equals(type)){
                                //find namespace for the prefix
                                String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (RecuperoDocumentoResponse) ExtensionMapper.getTypeObject(
                                        nsUri, type, reader);
                              }


                  }


                }




                // Note all attributes that were handled. Used to differ normal attributes
                // from anyAttributes.
                java.util.Vector handledAttributes = new java.util.Vector();




                    reader.next();


                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                                    if (reader.isStartElement() && new javax.xml.namespace.QName("","return").equals(reader.getName())){

                                                object.set_return(RecuperoFileResult.Factory.parse(reader));

                                        reader.next();

                              }  // End of if for expected property start element

                                    else {

                                    }

                            while (!reader.isStartElement() && !reader.isEndElement())
                                reader.next();

                                if (reader.isStartElement())
                                // A start element we are not expecting indicates a trailing invalid property
                                throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());




            } catch (javax.xml.stream.XMLStreamException e) {
                throw new Exception(e);
            }

            return object;
        }

        }//end of factory class



        }


        public static class LoginResult
        implements org.apache.axis2.databinding.ADBBean{
        /* This type was generated from the piece of schema that had
                name = loginResult
                Namespace URI = http://ws.idsunitelm.it/
                Namespace Prefix = ns1
                */


                        /**
                        * field for DST
                        */


                                    protected String localDST ;

                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localDSTTracker = false ;

                           public boolean isDSTSpecified(){
                               return localDSTTracker;
                           }



                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  String getDST(){
                               return localDST;
                           }



                            /**
                               * Auto generated setter method
                               * @param param DST
                               */
                               public void setDST(String param){
                            localDSTTracker = param != null;

                                            this.localDST=param;


                               }


                        /**
                        * field for Error_description
                        */


                                    protected String localError_description ;

                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localError_descriptionTracker = false ;

                           public boolean isError_descriptionSpecified(){
                               return localError_descriptionTracker;
                           }



                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  String getError_description(){
                               return localError_description;
                           }



                            /**
                               * Auto generated setter method
                               * @param param Error_description
                               */
                               public void setError_description(String param){
                            localError_descriptionTracker = param != null;

                                            this.localError_description=param;


                               }


                        /**
                        * field for Error_number
                        */


                                    protected int localError_number ;


                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getError_number(){
                               return localError_number;
                           }



                            /**
                               * Auto generated setter method
                               * @param param Error_number
                               */
                               public void setError_number(int param){

                                            this.localError_number=param;


                               }




        /**
        *
        * @param parentQName
        * @param factory
        * @return org.apache.axiom.om.OMElement
        */
       public org.apache.axiom.om.OMElement getOMElement (
               final javax.xml.namespace.QName parentQName,
               final org.apache.axiom.om.OMFactory factory) throws org.apache.axis2.databinding.ADBException{



               org.apache.axiom.om.OMDataSource dataSource =
                       new org.apache.axis2.databinding.ADBDataSource(this,parentQName);
               return factory.createOMElement(dataSource,parentQName);

        }

         public void serialize(final javax.xml.namespace.QName parentQName,
                                       javax.xml.stream.XMLStreamWriter xmlWriter)
                                throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{
                           serialize(parentQName,xmlWriter,false);
         }

         public void serialize(final javax.xml.namespace.QName parentQName,
                               javax.xml.stream.XMLStreamWriter xmlWriter,
                               boolean serializeType)
            throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{




                String prefix = null;
                String namespace = null;


                    prefix = parentQName.getPrefix();
                    namespace = parentQName.getNamespaceURI();
                    writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

                  if (serializeType){


                   String namespacePrefix = registerPrefix(xmlWriter,"http://ws.idsunitelm.it/");
                   if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                       writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                           namespacePrefix+":loginResult",
                           xmlWriter);
                   } else {
                       writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                           "loginResult",
                           xmlWriter);
                   }


                   }
                if (localDSTTracker){
                                    namespace = "";
                                    writeStartElement(null, namespace, "DST", xmlWriter);


                                          if (localDST==null){
                                              // write the nil attribute

                                                     throw new org.apache.axis2.databinding.ADBException("DST cannot be null!!");

                                          }else{


                                                   xmlWriter.writeCharacters(localDST);

                                          }

                                   xmlWriter.writeEndElement();
                             } if (localError_descriptionTracker){
                                    namespace = "";
                                    writeStartElement(null, namespace, "error_description", xmlWriter);


                                          if (localError_description==null){
                                              // write the nil attribute

                                                     throw new org.apache.axis2.databinding.ADBException("error_description cannot be null!!");

                                          }else{


                                                   xmlWriter.writeCharacters(localError_description);

                                          }

                                   xmlWriter.writeEndElement();
                             }
                                    namespace = "";
                                    writeStartElement(null, namespace, "error_number", xmlWriter);

                                               if (localError_number== Integer.MIN_VALUE) {

                                                         throw new org.apache.axis2.databinding.ADBException("error_number cannot be null!!");

                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localError_number));
                                               }

                                   xmlWriter.writeEndElement();

                    xmlWriter.writeEndElement();


        }

        private static String generatePrefix(String namespace) {
            if(namespace.equals("http://ws.idsunitelm.it/")){
                return "ns1";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
         * Utility method to write an element start tag.
         */
        private void writeStartElement(String prefix, String namespace, String localPart,
                                       javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            String writerPrefix = xmlWriter.getPrefix(namespace);
            if (writerPrefix != null) {
                xmlWriter.writeStartElement(namespace, localPart);
            } else {
                if (namespace.length() == 0) {
                    prefix = "";
                } else if (prefix == null) {
                    prefix = generatePrefix(namespace);
                }

                xmlWriter.writeStartElement(prefix, localPart, namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
        }

        /**
         * Util method to write an attribute with the ns prefix
         */
        private void writeAttribute(String prefix,String namespace,String attName,
                                    String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (xmlWriter.getPrefix(namespace) == null) {
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            xmlWriter.writeAttribute(namespace,attName,attValue);
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeAttribute(String namespace,String attName,
                                    String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (namespace.equals("")) {
                xmlWriter.writeAttribute(attName,attValue);
            } else {
                registerPrefix(xmlWriter, namespace);
                xmlWriter.writeAttribute(namespace,attName,attValue);
            }
        }


           /**
             * Util method to write an attribute without the ns prefix
             */
            private void writeQNameAttribute(String namespace, String attName,
                                             javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

                String attributeNamespace = qname.getNamespaceURI();
                String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
                if (attributePrefix == null) {
                    attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
                }
                String attributeValue;
                if (attributePrefix.trim().length() > 0) {
                    attributeValue = attributePrefix + ":" + qname.getLocalPart();
                } else {
                    attributeValue = qname.getLocalPart();
                }

                if (namespace.equals("")) {
                    xmlWriter.writeAttribute(attName, attributeValue);
                } else {
                    registerPrefix(xmlWriter, namespace);
                    xmlWriter.writeAttribute(namespace, attName, attributeValue);
                }
            }
        /**
         *  method to handle Qnames
         */

        private void writeQName(javax.xml.namespace.QName qname,
                                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            String namespaceURI = qname.getNamespaceURI();
            if (namespaceURI != null) {
                String prefix = xmlWriter.getPrefix(namespaceURI);
                if (prefix == null) {
                    prefix = generatePrefix(namespaceURI);
                    xmlWriter.writeNamespace(prefix, namespaceURI);
                    xmlWriter.setPrefix(prefix,namespaceURI);
                }

                if (prefix.trim().length() > 0){
                    xmlWriter.writeCharacters(prefix + ":" + org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                } else {
                    // i.e this is the default namespace
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                }

            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
            }
        }

        private void writeQNames(javax.xml.namespace.QName[] qnames,
                                 javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            if (qnames != null) {
                // we have to store this data until last moment since it is not possible to write any
                // namespace data after writing the charactor data
                StringBuffer stringToWrite = new StringBuffer();
                String namespaceURI = null;
                String prefix = null;

                for (int i = 0; i < qnames.length; i++) {
                    if (i > 0) {
                        stringToWrite.append(" ");
                    }
                    namespaceURI = qnames[i].getNamespaceURI();
                    if (namespaceURI != null) {
                        prefix = xmlWriter.getPrefix(namespaceURI);
                        if ((prefix == null) || (prefix.length() == 0)) {
                            prefix = generatePrefix(namespaceURI);
                            xmlWriter.writeNamespace(prefix, namespaceURI);
                            xmlWriter.setPrefix(prefix,namespaceURI);
                        }

                        if (prefix.trim().length() > 0){
                            stringToWrite.append(prefix).append(":").append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        } else {
                            stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        }
                    } else {
                        stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                    }
                }
                xmlWriter.writeCharacters(stringToWrite.toString());
            }

        }


        /**
         * Register a namespace prefix
         */
        private String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, String namespace) throws javax.xml.stream.XMLStreamException {
            String prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null) {
                prefix = generatePrefix(namespace);
                javax.xml.namespace.NamespaceContext nsContext = xmlWriter.getNamespaceContext();
                while (true) {
                    String uri = nsContext.getNamespaceURI(prefix);
                    if (uri == null || uri.length() == 0) {
                        break;
                    }
                    prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
                }
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            return prefix;
        }



        /**
        * databinding method to get an XML representation of this object
        *
        */
        public javax.xml.stream.XMLStreamReader getPullParser(javax.xml.namespace.QName qName)
                    throws org.apache.axis2.databinding.ADBException{



                 java.util.ArrayList elementList = new java.util.ArrayList();
                 java.util.ArrayList attribList = new java.util.ArrayList();

                 if (localDSTTracker){
                                      elementList.add(new javax.xml.namespace.QName("",
                                                                      "DST"));

                                        if (localDST != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDST));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("DST cannot be null!!");
                                        }
                                    } if (localError_descriptionTracker){
                                      elementList.add(new javax.xml.namespace.QName("",
                                                                      "error_description"));

                                        if (localError_description != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localError_description));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("error_description cannot be null!!");
                                        }
                                    }
                                      elementList.add(new javax.xml.namespace.QName("",
                                                                      "error_number"));

                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localError_number));


                return new org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl(qName, elementList.toArray(), attribList.toArray());



        }



     /**
      *  Factory class that keeps the parse method
      */
    public static class Factory{




        /**
        * static method to create the object
        * Precondition:  If this object is an element, the current or next start element starts this object and any intervening reader events are ignorable
        *                If this object is not an element, it is a complex type and the reader is at the event just after the outer start element
        * Postcondition: If this object is an element, the reader is positioned at its end element
        *                If this object is a complex type, the reader is positioned at the end element of its outer element
        */
        public static LoginResult parse(javax.xml.stream.XMLStreamReader reader) throws Exception{
            LoginResult object =
                new LoginResult();

            int event;
            String nillableValue = null;
            String prefix ="";
            String namespaceuri ="";
            try {

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();


                if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","type")!=null){
                  String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                        "type");
                  if (fullTypeName!=null){
                    String nsPrefix = null;
                    if (fullTypeName.indexOf(":") > -1){
                        nsPrefix = fullTypeName.substring(0,fullTypeName.indexOf(":"));
                    }
                    nsPrefix = nsPrefix==null?"":nsPrefix;

                    String type = fullTypeName.substring(fullTypeName.indexOf(":")+1);

                            if (!"loginResult".equals(type)){
                                //find namespace for the prefix
                                String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (LoginResult) ExtensionMapper.getTypeObject(
                                        nsUri, type, reader);
                              }


                  }


                }




                // Note all attributes that were handled. Used to differ normal attributes
                // from anyAttributes.
                java.util.Vector handledAttributes = new java.util.Vector();




                    reader.next();


                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                                    if (reader.isStartElement() && new javax.xml.namespace.QName("","DST").equals(reader.getName())){

                                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                    if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                        throw new org.apache.axis2.databinding.ADBException("The element: "+"DST" +"  cannot be null");
                                    }


                                    String content = reader.getElementText();

                                              object.setDST(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                                        reader.next();

                              }  // End of if for expected property start element

                                    else {

                                    }


                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                                    if (reader.isStartElement() && new javax.xml.namespace.QName("","error_description").equals(reader.getName())){

                                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                    if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                        throw new org.apache.axis2.databinding.ADBException("The element: "+"error_description" +"  cannot be null");
                                    }


                                    String content = reader.getElementText();

                                              object.setError_description(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                                        reader.next();

                              }  // End of if for expected property start element

                                    else {

                                    }


                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                                    if (reader.isStartElement() && new javax.xml.namespace.QName("","error_number").equals(reader.getName())){

                                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                    if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                        throw new org.apache.axis2.databinding.ADBException("The element: "+"error_number" +"  cannot be null");
                                    }


                                    String content = reader.getElementText();

                                              object.setError_number(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));

                                        reader.next();

                              }  // End of if for expected property start element

                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                                }

                            while (!reader.isStartElement() && !reader.isEndElement())
                                reader.next();

                                if (reader.isStartElement())
                                // A start element we are not expecting indicates a trailing invalid property
                                throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());




            } catch (javax.xml.stream.XMLStreamException e) {
                throw new Exception(e);
            }

            return object;
        }

        }//end of factory class



        }


        public static class FascicoloResult
        implements org.apache.axis2.databinding.ADBBean{
        /* This type was generated from the piece of schema that had
                name = fascicoloResult
                Namespace URI = http://ws.idsunitelm.it/
                Namespace Prefix = ns1
                */


                        /**
                        * field for AnnoFasc
                        */


                                    protected long localAnnoFasc ;


                           /**
                           * Auto generated getter method
                           * @return long
                           */
                           public  long getAnnoFasc(){
                               return localAnnoFasc;
                           }



                            /**
                               * Auto generated setter method
                               * @param param AnnoFasc
                               */
                               public void setAnnoFasc(long param){

                                            this.localAnnoFasc=param;


                               }


                        /**
                        * field for Error_description
                        */


                                    protected String localError_description ;

                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localError_descriptionTracker = false ;

                           public boolean isError_descriptionSpecified(){
                               return localError_descriptionTracker;
                           }



                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  String getError_description(){
                               return localError_description;
                           }



                            /**
                               * Auto generated setter method
                               * @param param Error_description
                               */
                               public void setError_description(String param){
                            localError_descriptionTracker = param != null;

                                            this.localError_description=param;


                               }


                        /**
                        * field for Error_number
                        */


                                    protected int localError_number ;


                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getError_number(){
                               return localError_number;
                           }



                            /**
                               * Auto generated setter method
                               * @param param Error_number
                               */
                               public void setError_number(int param){

                                            this.localError_number=param;


                               }


                        /**
                        * field for NumFasc
                        */


                                    protected long localNumFasc ;


                           /**
                           * Auto generated getter method
                           * @return long
                           */
                           public  long getNumFasc(){
                               return localNumFasc;
                           }



                            /**
                               * Auto generated setter method
                               * @param param NumFasc
                               */
                               public void setNumFasc(long param){

                                            this.localNumFasc=param;


                               }




        /**
        *
        * @param parentQName
        * @param factory
        * @return org.apache.axiom.om.OMElement
        */
       public org.apache.axiom.om.OMElement getOMElement (
               final javax.xml.namespace.QName parentQName,
               final org.apache.axiom.om.OMFactory factory) throws org.apache.axis2.databinding.ADBException{



               org.apache.axiom.om.OMDataSource dataSource =
                       new org.apache.axis2.databinding.ADBDataSource(this,parentQName);
               return factory.createOMElement(dataSource,parentQName);

        }

         public void serialize(final javax.xml.namespace.QName parentQName,
                                       javax.xml.stream.XMLStreamWriter xmlWriter)
                                throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{
                           serialize(parentQName,xmlWriter,false);
         }

         public void serialize(final javax.xml.namespace.QName parentQName,
                               javax.xml.stream.XMLStreamWriter xmlWriter,
                               boolean serializeType)
            throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{




                String prefix = null;
                String namespace = null;


                    prefix = parentQName.getPrefix();
                    namespace = parentQName.getNamespaceURI();
                    writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

                  if (serializeType){


                   String namespacePrefix = registerPrefix(xmlWriter,"http://ws.idsunitelm.it/");
                   if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                       writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                           namespacePrefix+":fascicoloResult",
                           xmlWriter);
                   } else {
                       writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                           "fascicoloResult",
                           xmlWriter);
                   }


                   }

                                    namespace = "";
                                    writeStartElement(null, namespace, "annoFasc", xmlWriter);

                                               if (localAnnoFasc== Long.MIN_VALUE) {

                                                         throw new org.apache.axis2.databinding.ADBException("annoFasc cannot be null!!");

                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localAnnoFasc));
                                               }

                                   xmlWriter.writeEndElement();
                              if (localError_descriptionTracker){
                                    namespace = "";
                                    writeStartElement(null, namespace, "error_description", xmlWriter);


                                          if (localError_description==null){
                                              // write the nil attribute

                                                     throw new org.apache.axis2.databinding.ADBException("error_description cannot be null!!");

                                          }else{


                                                   xmlWriter.writeCharacters(localError_description);

                                          }

                                   xmlWriter.writeEndElement();
                             }
                                    namespace = "";
                                    writeStartElement(null, namespace, "error_number", xmlWriter);

                                               if (localError_number== Integer.MIN_VALUE) {

                                                         throw new org.apache.axis2.databinding.ADBException("error_number cannot be null!!");

                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localError_number));
                                               }

                                   xmlWriter.writeEndElement();

                                    namespace = "";
                                    writeStartElement(null, namespace, "numFasc", xmlWriter);

                                               if (localNumFasc== Long.MIN_VALUE) {

                                                         throw new org.apache.axis2.databinding.ADBException("numFasc cannot be null!!");

                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localNumFasc));
                                               }

                                   xmlWriter.writeEndElement();

                    xmlWriter.writeEndElement();


        }

        private static String generatePrefix(String namespace) {
            if(namespace.equals("http://ws.idsunitelm.it/")){
                return "ns1";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
         * Utility method to write an element start tag.
         */
        private void writeStartElement(String prefix, String namespace, String localPart,
                                       javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            String writerPrefix = xmlWriter.getPrefix(namespace);
            if (writerPrefix != null) {
                xmlWriter.writeStartElement(namespace, localPart);
            } else {
                if (namespace.length() == 0) {
                    prefix = "";
                } else if (prefix == null) {
                    prefix = generatePrefix(namespace);
                }

                xmlWriter.writeStartElement(prefix, localPart, namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
        }

        /**
         * Util method to write an attribute with the ns prefix
         */
        private void writeAttribute(String prefix,String namespace,String attName,
                                    String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (xmlWriter.getPrefix(namespace) == null) {
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            xmlWriter.writeAttribute(namespace,attName,attValue);
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeAttribute(String namespace,String attName,
                                    String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (namespace.equals("")) {
                xmlWriter.writeAttribute(attName,attValue);
            } else {
                registerPrefix(xmlWriter, namespace);
                xmlWriter.writeAttribute(namespace,attName,attValue);
            }
        }


           /**
             * Util method to write an attribute without the ns prefix
             */
            private void writeQNameAttribute(String namespace, String attName,
                                             javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

                String attributeNamespace = qname.getNamespaceURI();
                String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
                if (attributePrefix == null) {
                    attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
                }
                String attributeValue;
                if (attributePrefix.trim().length() > 0) {
                    attributeValue = attributePrefix + ":" + qname.getLocalPart();
                } else {
                    attributeValue = qname.getLocalPart();
                }

                if (namespace.equals("")) {
                    xmlWriter.writeAttribute(attName, attributeValue);
                } else {
                    registerPrefix(xmlWriter, namespace);
                    xmlWriter.writeAttribute(namespace, attName, attributeValue);
                }
            }
        /**
         *  method to handle Qnames
         */

        private void writeQName(javax.xml.namespace.QName qname,
                                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            String namespaceURI = qname.getNamespaceURI();
            if (namespaceURI != null) {
                String prefix = xmlWriter.getPrefix(namespaceURI);
                if (prefix == null) {
                    prefix = generatePrefix(namespaceURI);
                    xmlWriter.writeNamespace(prefix, namespaceURI);
                    xmlWriter.setPrefix(prefix,namespaceURI);
                }

                if (prefix.trim().length() > 0){
                    xmlWriter.writeCharacters(prefix + ":" + org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                } else {
                    // i.e this is the default namespace
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                }

            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
            }
        }

        private void writeQNames(javax.xml.namespace.QName[] qnames,
                                 javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            if (qnames != null) {
                // we have to store this data until last moment since it is not possible to write any
                // namespace data after writing the charactor data
                StringBuffer stringToWrite = new StringBuffer();
                String namespaceURI = null;
                String prefix = null;

                for (int i = 0; i < qnames.length; i++) {
                    if (i > 0) {
                        stringToWrite.append(" ");
                    }
                    namespaceURI = qnames[i].getNamespaceURI();
                    if (namespaceURI != null) {
                        prefix = xmlWriter.getPrefix(namespaceURI);
                        if ((prefix == null) || (prefix.length() == 0)) {
                            prefix = generatePrefix(namespaceURI);
                            xmlWriter.writeNamespace(prefix, namespaceURI);
                            xmlWriter.setPrefix(prefix,namespaceURI);
                        }

                        if (prefix.trim().length() > 0){
                            stringToWrite.append(prefix).append(":").append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        } else {
                            stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        }
                    } else {
                        stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                    }
                }
                xmlWriter.writeCharacters(stringToWrite.toString());
            }

        }


        /**
         * Register a namespace prefix
         */
        private String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, String namespace) throws javax.xml.stream.XMLStreamException {
            String prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null) {
                prefix = generatePrefix(namespace);
                javax.xml.namespace.NamespaceContext nsContext = xmlWriter.getNamespaceContext();
                while (true) {
                    String uri = nsContext.getNamespaceURI(prefix);
                    if (uri == null || uri.length() == 0) {
                        break;
                    }
                    prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
                }
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            return prefix;
        }



        /**
        * databinding method to get an XML representation of this object
        *
        */
        public javax.xml.stream.XMLStreamReader getPullParser(javax.xml.namespace.QName qName)
                    throws org.apache.axis2.databinding.ADBException{



                 java.util.ArrayList elementList = new java.util.ArrayList();
                 java.util.ArrayList attribList = new java.util.ArrayList();


                                      elementList.add(new javax.xml.namespace.QName("",
                                                                      "annoFasc"));

                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localAnnoFasc));
                             if (localError_descriptionTracker){
                                      elementList.add(new javax.xml.namespace.QName("",
                                                                      "error_description"));

                                        if (localError_description != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localError_description));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("error_description cannot be null!!");
                                        }
                                    }
                                      elementList.add(new javax.xml.namespace.QName("",
                                                                      "error_number"));

                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localError_number));

                                      elementList.add(new javax.xml.namespace.QName("",
                                                                      "numFasc"));

                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localNumFasc));


                return new org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl(qName, elementList.toArray(), attribList.toArray());



        }



     /**
      *  Factory class that keeps the parse method
      */
    public static class Factory{




        /**
        * static method to create the object
        * Precondition:  If this object is an element, the current or next start element starts this object and any intervening reader events are ignorable
        *                If this object is not an element, it is a complex type and the reader is at the event just after the outer start element
        * Postcondition: If this object is an element, the reader is positioned at its end element
        *                If this object is a complex type, the reader is positioned at the end element of its outer element
        */
        public static FascicoloResult parse(javax.xml.stream.XMLStreamReader reader) throws Exception{
            FascicoloResult object =
                new FascicoloResult();

            int event;
            String nillableValue = null;
            String prefix ="";
            String namespaceuri ="";
            try {

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();


                if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","type")!=null){
                  String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                        "type");
                  if (fullTypeName!=null){
                    String nsPrefix = null;
                    if (fullTypeName.indexOf(":") > -1){
                        nsPrefix = fullTypeName.substring(0,fullTypeName.indexOf(":"));
                    }
                    nsPrefix = nsPrefix==null?"":nsPrefix;

                    String type = fullTypeName.substring(fullTypeName.indexOf(":")+1);

                            if (!"fascicoloResult".equals(type)){
                                //find namespace for the prefix
                                String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (FascicoloResult) ExtensionMapper.getTypeObject(
                                        nsUri, type, reader);
                              }


                  }


                }




                // Note all attributes that were handled. Used to differ normal attributes
                // from anyAttributes.
                java.util.Vector handledAttributes = new java.util.Vector();




                    reader.next();


                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                                    if (reader.isStartElement() && new javax.xml.namespace.QName("","annoFasc").equals(reader.getName())){

                                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                    if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                        throw new org.apache.axis2.databinding.ADBException("The element: "+"annoFasc" +"  cannot be null");
                                    }


                                    String content = reader.getElementText();

                                              object.setAnnoFasc(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToLong(content));

                                        reader.next();

                              }  // End of if for expected property start element

                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                                }


                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                                    if (reader.isStartElement() && new javax.xml.namespace.QName("","error_description").equals(reader.getName())){

                                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                    if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                        throw new org.apache.axis2.databinding.ADBException("The element: "+"error_description" +"  cannot be null");
                                    }


                                    String content = reader.getElementText();

                                              object.setError_description(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                                        reader.next();

                              }  // End of if for expected property start element

                                    else {

                                    }


                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                                    if (reader.isStartElement() && new javax.xml.namespace.QName("","error_number").equals(reader.getName())){

                                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                    if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                        throw new org.apache.axis2.databinding.ADBException("The element: "+"error_number" +"  cannot be null");
                                    }


                                    String content = reader.getElementText();

                                              object.setError_number(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));

                                        reader.next();

                              }  // End of if for expected property start element

                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                                }


                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                                    if (reader.isStartElement() && new javax.xml.namespace.QName("","numFasc").equals(reader.getName())){

                                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                    if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                        throw new org.apache.axis2.databinding.ADBException("The element: "+"numFasc" +"  cannot be null");
                                    }


                                    String content = reader.getElementText();

                                              object.setNumFasc(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToLong(content));

                                        reader.next();

                              }  // End of if for expected property start element

                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                                }

                            while (!reader.isStartElement() && !reader.isEndElement())
                                reader.next();

                                if (reader.isStartElement())
                                // A start element we are not expecting indicates a trailing invalid property
                                throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());




            } catch (javax.xml.stream.XMLStreamException e) {
                throw new Exception(e);
            }

            return object;
        }

        }//end of factory class



        }


        public static class ProtocollazioneDocumento
        implements org.apache.axis2.databinding.ADBBean{
        /* This type was generated from the piece of schema that had
                name = protocollazioneDocumento
                Namespace URI = http://ws.idsunitelm.it/
                Namespace Prefix = ns1
                */


                        /**
                        * field for DST
                        */


                                    protected String localDST ;

                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localDSTTracker = false ;

                           public boolean isDSTSpecified(){
                               return localDSTTracker;
                           }



                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  String getDST(){
                               return localDST;
                           }



                            /**
                               * Auto generated setter method
                               * @param param DST
                               */
                               public void setDST(String param){
                            localDSTTracker = param != null;

                                            this.localDST=param;


                               }


                        /**
                        * field for DataXML
                        */


                                    protected javax.activation.DataHandler localDataXML ;

                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localDataXMLTracker = false ;

                           public boolean isDataXMLSpecified(){
                               return localDataXMLTracker;
                           }



                           /**
                           * Auto generated getter method
                           * @return javax.activation.DataHandler
                           */
                           public  javax.activation.DataHandler getDataXML(){
                               return localDataXML;
                           }



                            /**
                               * Auto generated setter method
                               * @param param DataXML
                               */
                               public void setDataXML(javax.activation.DataHandler param){
                            localDataXMLTracker = param != null;

                                            this.localDataXML=param;


                               }


                        /**
                        * field for DocPrinc
                        */


                                    protected javax.activation.DataHandler localDocPrinc ;

                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localDocPrincTracker = false ;

                           public boolean isDocPrincSpecified(){
                               return localDocPrincTracker;
                           }



                           /**
                           * Auto generated getter method
                           * @return javax.activation.DataHandler
                           */
                           public  javax.activation.DataHandler getDocPrinc(){
                               return localDocPrinc;
                           }



                            /**
                               * Auto generated setter method
                               * @param param DocPrinc
                               */
                               public void setDocPrinc(javax.activation.DataHandler param){
                            localDocPrincTracker = param != null;

                                            this.localDocPrinc=param;


                               }


                        /**
                        * field for ListaAllegati
                        * This was an Array!
                        */


                                    protected Attach[] localListaAllegati ;

                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localListaAllegatiTracker = false ;

                           public boolean isListaAllegatiSpecified(){
                               return localListaAllegatiTracker;
                           }



                           /**
                           * Auto generated getter method
                           * @return Attach[]
                           */
                           public  Attach[] getListaAllegati(){
                               return localListaAllegati;
                           }






                              /**
                               * validate the array for ListaAllegati
                               */
                              protected void validateListaAllegati(Attach[] param){

                              }


                             /**
                              * Auto generated setter method
                              * @param param ListaAllegati
                              */
                              public void setListaAllegati(Attach[] param){

                                   validateListaAllegati(param);

                               localListaAllegatiTracker = param != null;

                                      this.localListaAllegati=param;
                              }



                             /**
                             * Auto generated add method for the array for convenience
                             * @param param Attach
                             */
                             public void addListaAllegati(Attach param){
                                   if (localListaAllegati == null){
                                   localListaAllegati = new Attach[]{};
                                   }


                                 //update the setting tracker
                                localListaAllegatiTracker = true;


                               java.util.List list =
                            org.apache.axis2.databinding.utils.ConverterUtil.toList(localListaAllegati);
                               list.add(param);
                               this.localListaAllegati =
                             (Attach[])list.toArray(
                            new Attach[list.size()]);

                             }




        /**
        *
        * @param parentQName
        * @param factory
        * @return org.apache.axiom.om.OMElement
        */
       public org.apache.axiom.om.OMElement getOMElement (
               final javax.xml.namespace.QName parentQName,
               final org.apache.axiom.om.OMFactory factory) throws org.apache.axis2.databinding.ADBException{



               org.apache.axiom.om.OMDataSource dataSource =
                       new org.apache.axis2.databinding.ADBDataSource(this,parentQName);
               return factory.createOMElement(dataSource,parentQName);

        }

         public void serialize(final javax.xml.namespace.QName parentQName,
                                       javax.xml.stream.XMLStreamWriter xmlWriter)
                                throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{
                           serialize(parentQName,xmlWriter,false);
         }

         public void serialize(final javax.xml.namespace.QName parentQName,
                               javax.xml.stream.XMLStreamWriter xmlWriter,
                               boolean serializeType)
            throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{




                String prefix = null;
                String namespace = null;


                    prefix = parentQName.getPrefix();
                    namespace = parentQName.getNamespaceURI();
                    writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

                  if (serializeType){


                   String namespacePrefix = registerPrefix(xmlWriter,"http://ws.idsunitelm.it/");
                   if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                       writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                           namespacePrefix+":protocollazioneDocumento",
                           xmlWriter);
                   } else {
                       writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                           "protocollazioneDocumento",
                           xmlWriter);
                   }


                   }
                if (localDSTTracker){
                                    namespace = "";
                                    writeStartElement(null, namespace, "DST", xmlWriter);


                                          if (localDST==null){
                                              // write the nil attribute

                                                     throw new org.apache.axis2.databinding.ADBException("DST cannot be null!!");

                                          }else{


                                                   xmlWriter.writeCharacters(localDST);

                                          }

                                   xmlWriter.writeEndElement();
                             } if (localDataXMLTracker){
                                    namespace = "";
                                    writeStartElement(null, namespace, "dataXML", xmlWriter);


                                    if (localDataXML!=null)  {
                                       try {
                                           org.apache.axiom.util.stax.XMLStreamWriterUtils.writeDataHandler(xmlWriter, localDataXML, null, true);
                                       } catch (java.io.IOException ex) {
                                           throw new javax.xml.stream.XMLStreamException("Unable to read data handler for dataXML", ex);
                                       }
                                    } else {

                                    }

                                   xmlWriter.writeEndElement();
                             } if (localDocPrincTracker){
                                    namespace = "";
                                    writeStartElement(null, namespace, "docPrinc", xmlWriter);


                                    if (localDocPrinc!=null)  {
                                       try {
                                           org.apache.axiom.util.stax.XMLStreamWriterUtils.writeDataHandler(xmlWriter, localDocPrinc, null, true);
                                       } catch (java.io.IOException ex) {
                                           throw new javax.xml.stream.XMLStreamException("Unable to read data handler for docPrinc", ex);
                                       }
                                    } else {

                                    }

                                   xmlWriter.writeEndElement();
                             } if (localListaAllegatiTracker){
                                       if (localListaAllegati!=null){
                                            for (int i = 0;i < localListaAllegati.length;i++){
                                                if (localListaAllegati[i] != null){
                                                 localListaAllegati[i].serialize(new javax.xml.namespace.QName("","listaAllegati"),
                                                           xmlWriter);
                                                } else {

                                                        // we don't have to do any thing since minOccures is zero

                                                }

                                            }
                                     } else {

                                               throw new org.apache.axis2.databinding.ADBException("listaAllegati cannot be null!!");

                                    }
                                 }
                    xmlWriter.writeEndElement();


        }

        private static String generatePrefix(String namespace) {
            if(namespace.equals("http://ws.idsunitelm.it/")){
                return "ns1";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
         * Utility method to write an element start tag.
         */
        private void writeStartElement(String prefix, String namespace, String localPart,
                                       javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            String writerPrefix = xmlWriter.getPrefix(namespace);
            if (writerPrefix != null) {
                xmlWriter.writeStartElement(namespace, localPart);
            } else {
                if (namespace.length() == 0) {
                    prefix = "";
                } else if (prefix == null) {
                    prefix = generatePrefix(namespace);
                }

                xmlWriter.writeStartElement(prefix, localPart, namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
        }

        /**
         * Util method to write an attribute with the ns prefix
         */
        private void writeAttribute(String prefix,String namespace,String attName,
                                    String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (xmlWriter.getPrefix(namespace) == null) {
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            xmlWriter.writeAttribute(namespace,attName,attValue);
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeAttribute(String namespace,String attName,
                                    String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (namespace.equals("")) {
                xmlWriter.writeAttribute(attName,attValue);
            } else {
                registerPrefix(xmlWriter, namespace);
                xmlWriter.writeAttribute(namespace,attName,attValue);
            }
        }


           /**
             * Util method to write an attribute without the ns prefix
             */
            private void writeQNameAttribute(String namespace, String attName,
                                             javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

                String attributeNamespace = qname.getNamespaceURI();
                String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
                if (attributePrefix == null) {
                    attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
                }
                String attributeValue;
                if (attributePrefix.trim().length() > 0) {
                    attributeValue = attributePrefix + ":" + qname.getLocalPart();
                } else {
                    attributeValue = qname.getLocalPart();
                }

                if (namespace.equals("")) {
                    xmlWriter.writeAttribute(attName, attributeValue);
                } else {
                    registerPrefix(xmlWriter, namespace);
                    xmlWriter.writeAttribute(namespace, attName, attributeValue);
                }
            }
        /**
         *  method to handle Qnames
         */

        private void writeQName(javax.xml.namespace.QName qname,
                                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            String namespaceURI = qname.getNamespaceURI();
            if (namespaceURI != null) {
                String prefix = xmlWriter.getPrefix(namespaceURI);
                if (prefix == null) {
                    prefix = generatePrefix(namespaceURI);
                    xmlWriter.writeNamespace(prefix, namespaceURI);
                    xmlWriter.setPrefix(prefix,namespaceURI);
                }

                if (prefix.trim().length() > 0){
                    xmlWriter.writeCharacters(prefix + ":" + org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                } else {
                    // i.e this is the default namespace
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                }

            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
            }
        }

        private void writeQNames(javax.xml.namespace.QName[] qnames,
                                 javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            if (qnames != null) {
                // we have to store this data until last moment since it is not possible to write any
                // namespace data after writing the charactor data
                StringBuffer stringToWrite = new StringBuffer();
                String namespaceURI = null;
                String prefix = null;

                for (int i = 0; i < qnames.length; i++) {
                    if (i > 0) {
                        stringToWrite.append(" ");
                    }
                    namespaceURI = qnames[i].getNamespaceURI();
                    if (namespaceURI != null) {
                        prefix = xmlWriter.getPrefix(namespaceURI);
                        if ((prefix == null) || (prefix.length() == 0)) {
                            prefix = generatePrefix(namespaceURI);
                            xmlWriter.writeNamespace(prefix, namespaceURI);
                            xmlWriter.setPrefix(prefix,namespaceURI);
                        }

                        if (prefix.trim().length() > 0){
                            stringToWrite.append(prefix).append(":").append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        } else {
                            stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        }
                    } else {
                        stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                    }
                }
                xmlWriter.writeCharacters(stringToWrite.toString());
            }

        }


        /**
         * Register a namespace prefix
         */
        private String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, String namespace) throws javax.xml.stream.XMLStreamException {
            String prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null) {
                prefix = generatePrefix(namespace);
                javax.xml.namespace.NamespaceContext nsContext = xmlWriter.getNamespaceContext();
                while (true) {
                    String uri = nsContext.getNamespaceURI(prefix);
                    if (uri == null || uri.length() == 0) {
                        break;
                    }
                    prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
                }
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            return prefix;
        }



        /**
        * databinding method to get an XML representation of this object
        *
        */
        public javax.xml.stream.XMLStreamReader getPullParser(javax.xml.namespace.QName qName)
                    throws org.apache.axis2.databinding.ADBException{



                 java.util.ArrayList elementList = new java.util.ArrayList();
                 java.util.ArrayList attribList = new java.util.ArrayList();

                 if (localDSTTracker){
                                      elementList.add(new javax.xml.namespace.QName("",
                                                                      "DST"));

                                        if (localDST != null){
                                            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDST));
                                        } else {
                                           throw new org.apache.axis2.databinding.ADBException("DST cannot be null!!");
                                        }
                                    } if (localDataXMLTracker){
                                      elementList.add(new javax.xml.namespace.QName("",
                                        "dataXML"));

                            elementList.add(localDataXML);
                        } if (localDocPrincTracker){
                                      elementList.add(new javax.xml.namespace.QName("",
                                        "docPrinc"));

                            elementList.add(localDocPrinc);
                        } if (localListaAllegatiTracker){
                             if (localListaAllegati!=null) {
                                 for (int i = 0;i < localListaAllegati.length;i++){

                                    if (localListaAllegati[i] != null){
                                         elementList.add(new javax.xml.namespace.QName("",
                                                                          "listaAllegati"));
                                         elementList.add(localListaAllegati[i]);
                                    } else {

                                                // nothing to do

                                    }

                                 }
                             } else {

                                        throw new org.apache.axis2.databinding.ADBException("listaAllegati cannot be null!!");

                             }

                        }

                return new org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl(qName, elementList.toArray(), attribList.toArray());



        }



     /**
      *  Factory class that keeps the parse method
      */
    public static class Factory{




        /**
        * static method to create the object
        * Precondition:  If this object is an element, the current or next start element starts this object and any intervening reader events are ignorable
        *                If this object is not an element, it is a complex type and the reader is at the event just after the outer start element
        * Postcondition: If this object is an element, the reader is positioned at its end element
        *                If this object is a complex type, the reader is positioned at the end element of its outer element
        */
        public static ProtocollazioneDocumento parse(javax.xml.stream.XMLStreamReader reader) throws Exception{
            ProtocollazioneDocumento object =
                new ProtocollazioneDocumento();

            int event;
            String nillableValue = null;
            String prefix ="";
            String namespaceuri ="";
            try {

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();


                if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","type")!=null){
                  String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                        "type");
                  if (fullTypeName!=null){
                    String nsPrefix = null;
                    if (fullTypeName.indexOf(":") > -1){
                        nsPrefix = fullTypeName.substring(0,fullTypeName.indexOf(":"));
                    }
                    nsPrefix = nsPrefix==null?"":nsPrefix;

                    String type = fullTypeName.substring(fullTypeName.indexOf(":")+1);

                            if (!"protocollazioneDocumento".equals(type)){
                                //find namespace for the prefix
                                String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (ProtocollazioneDocumento) ExtensionMapper.getTypeObject(
                                        nsUri, type, reader);
                              }


                  }


                }




                // Note all attributes that were handled. Used to differ normal attributes
                // from anyAttributes.
                java.util.Vector handledAttributes = new java.util.Vector();




                    reader.next();

                        java.util.ArrayList list4 = new java.util.ArrayList();


                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                                    if (reader.isStartElement() && new javax.xml.namespace.QName("","DST").equals(reader.getName())){

                                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                    if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                        throw new org.apache.axis2.databinding.ADBException("The element: "+"DST" +"  cannot be null");
                                    }


                                    String content = reader.getElementText();

                                              object.setDST(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                                        reader.next();

                              }  // End of if for expected property start element

                                    else {

                                    }


                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                                    if (reader.isStartElement() && new javax.xml.namespace.QName("","dataXML").equals(reader.getName())){

                                            object.setDataXML(org.apache.axiom.util.stax.XMLStreamReaderUtils.getDataHandlerFromElement(reader));

                                        reader.next();

                              }  // End of if for expected property start element

                                    else {

                                    }


                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                                    if (reader.isStartElement() && new javax.xml.namespace.QName("","docPrinc").equals(reader.getName())){

                                            object.setDocPrinc(org.apache.axiom.util.stax.XMLStreamReaderUtils.getDataHandlerFromElement(reader));

                                        reader.next();

                              }  // End of if for expected property start element

                                    else {

                                    }


                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                                    if (reader.isStartElement() && new javax.xml.namespace.QName("","listaAllegati").equals(reader.getName())){



                                    // Process the array and step past its final element's end.
                                    list4.add(Attach.Factory.parse(reader));

                                                        //loop until we find a start element that is not part of this array
                                                        boolean loopDone4 = false;
                                                        while(!loopDone4){
                                                            // We should be at the end element, but make sure
                                                            while (!reader.isEndElement())
                                                                reader.next();
                                                            // Step out of this element
                                                            reader.next();
                                                            // Step to next element event.
                                                            while (!reader.isStartElement() && !reader.isEndElement())
                                                                reader.next();
                                                            if (reader.isEndElement()){
                                                                //two continuous end elements means we are exiting the xml structure
                                                                loopDone4 = true;
                                                            } else {
                                                                if (new javax.xml.namespace.QName("","listaAllegati").equals(reader.getName())){
                                                                    list4.add(Attach.Factory.parse(reader));

                                                                }else{
                                                                    loopDone4 = true;
                                                                }
                                                            }
                                                        }
                                                        // call the converter utility  to convert and set the array

                                                        object.setListaAllegati((Attach[])
                                                            org.apache.axis2.databinding.utils.ConverterUtil.convertToArray(
                                                                Attach.class,
                                                                list4));

                              }  // End of if for expected property start element

                                    else {

                                    }

                            while (!reader.isStartElement() && !reader.isEndElement())
                                reader.next();

                                if (reader.isStartElement())
                                // A start element we are not expecting indicates a trailing invalid property
                                throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());




            } catch (javax.xml.stream.XMLStreamException e) {
                throw new Exception(e);
            }

            return object;
        }

        }//end of factory class



        }


        public static class ProtocollazioneDocumentoResponseE
        implements org.apache.axis2.databinding.ADBBean{

                public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName(
                "http://ws.idsunitelm.it/",
                "protocollazioneDocumentoResponse",
                "ns1");



                        /**
                        * field for ProtocollazioneDocumentoResponse
                        */


                                    protected ProtocollazioneDocumentoResponse localProtocollazioneDocumentoResponse ;


                           /**
                           * Auto generated getter method
                           * @return ProtocollazioneDocumentoResponse
                           */
                           public  ProtocollazioneDocumentoResponse getProtocollazioneDocumentoResponse(){
                               return localProtocollazioneDocumentoResponse;
                           }



                            /**
                               * Auto generated setter method
                               * @param param ProtocollazioneDocumentoResponse
                               */
                               public void setProtocollazioneDocumentoResponse(ProtocollazioneDocumentoResponse param){

                                            this.localProtocollazioneDocumentoResponse=param;


                               }




        /**
        *
        * @param parentQName
        * @param factory
        * @return org.apache.axiom.om.OMElement
        */
       public org.apache.axiom.om.OMElement getOMElement (
               final javax.xml.namespace.QName parentQName,
               final org.apache.axiom.om.OMFactory factory) throws org.apache.axis2.databinding.ADBException{



               org.apache.axiom.om.OMDataSource dataSource =
                       new org.apache.axis2.databinding.ADBDataSource(this,MY_QNAME);
               return factory.createOMElement(dataSource,MY_QNAME);

        }

         public void serialize(final javax.xml.namespace.QName parentQName,
                                       javax.xml.stream.XMLStreamWriter xmlWriter)
                                throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{
                           serialize(parentQName,xmlWriter,false);
         }

         public void serialize(final javax.xml.namespace.QName parentQName,
                               javax.xml.stream.XMLStreamWriter xmlWriter,
                               boolean serializeType)
            throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{


                //We can safely assume an element has only one type associated with it

                                 if (localProtocollazioneDocumentoResponse==null){
                                   throw new org.apache.axis2.databinding.ADBException("protocollazioneDocumentoResponse cannot be null!");
                                 }
                                 localProtocollazioneDocumentoResponse.serialize(MY_QNAME,xmlWriter);


        }

        private static String generatePrefix(String namespace) {
            if(namespace.equals("http://ws.idsunitelm.it/")){
                return "ns1";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
         * Utility method to write an element start tag.
         */
        private void writeStartElement(String prefix, String namespace, String localPart,
                                       javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            String writerPrefix = xmlWriter.getPrefix(namespace);
            if (writerPrefix != null) {
                xmlWriter.writeStartElement(namespace, localPart);
            } else {
                if (namespace.length() == 0) {
                    prefix = "";
                } else if (prefix == null) {
                    prefix = generatePrefix(namespace);
                }

                xmlWriter.writeStartElement(prefix, localPart, namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
        }

        /**
         * Util method to write an attribute with the ns prefix
         */
        private void writeAttribute(String prefix,String namespace,String attName,
                                    String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (xmlWriter.getPrefix(namespace) == null) {
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            xmlWriter.writeAttribute(namespace,attName,attValue);
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeAttribute(String namespace,String attName,
                                    String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (namespace.equals("")) {
                xmlWriter.writeAttribute(attName,attValue);
            } else {
                registerPrefix(xmlWriter, namespace);
                xmlWriter.writeAttribute(namespace,attName,attValue);
            }
        }


           /**
             * Util method to write an attribute without the ns prefix
             */
            private void writeQNameAttribute(String namespace, String attName,
                                             javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

                String attributeNamespace = qname.getNamespaceURI();
                String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
                if (attributePrefix == null) {
                    attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
                }
                String attributeValue;
                if (attributePrefix.trim().length() > 0) {
                    attributeValue = attributePrefix + ":" + qname.getLocalPart();
                } else {
                    attributeValue = qname.getLocalPart();
                }

                if (namespace.equals("")) {
                    xmlWriter.writeAttribute(attName, attributeValue);
                } else {
                    registerPrefix(xmlWriter, namespace);
                    xmlWriter.writeAttribute(namespace, attName, attributeValue);
                }
            }
        /**
         *  method to handle Qnames
         */

        private void writeQName(javax.xml.namespace.QName qname,
                                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            String namespaceURI = qname.getNamespaceURI();
            if (namespaceURI != null) {
                String prefix = xmlWriter.getPrefix(namespaceURI);
                if (prefix == null) {
                    prefix = generatePrefix(namespaceURI);
                    xmlWriter.writeNamespace(prefix, namespaceURI);
                    xmlWriter.setPrefix(prefix,namespaceURI);
                }

                if (prefix.trim().length() > 0){
                    xmlWriter.writeCharacters(prefix + ":" + org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                } else {
                    // i.e this is the default namespace
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                }

            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
            }
        }

        private void writeQNames(javax.xml.namespace.QName[] qnames,
                                 javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            if (qnames != null) {
                // we have to store this data until last moment since it is not possible to write any
                // namespace data after writing the charactor data
                StringBuffer stringToWrite = new StringBuffer();
                String namespaceURI = null;
                String prefix = null;

                for (int i = 0; i < qnames.length; i++) {
                    if (i > 0) {
                        stringToWrite.append(" ");
                    }
                    namespaceURI = qnames[i].getNamespaceURI();
                    if (namespaceURI != null) {
                        prefix = xmlWriter.getPrefix(namespaceURI);
                        if ((prefix == null) || (prefix.length() == 0)) {
                            prefix = generatePrefix(namespaceURI);
                            xmlWriter.writeNamespace(prefix, namespaceURI);
                            xmlWriter.setPrefix(prefix,namespaceURI);
                        }

                        if (prefix.trim().length() > 0){
                            stringToWrite.append(prefix).append(":").append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        } else {
                            stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        }
                    } else {
                        stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                    }
                }
                xmlWriter.writeCharacters(stringToWrite.toString());
            }

        }


        /**
         * Register a namespace prefix
         */
        private String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, String namespace) throws javax.xml.stream.XMLStreamException {
            String prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null) {
                prefix = generatePrefix(namespace);
                javax.xml.namespace.NamespaceContext nsContext = xmlWriter.getNamespaceContext();
                while (true) {
                    String uri = nsContext.getNamespaceURI(prefix);
                    if (uri == null || uri.length() == 0) {
                        break;
                    }
                    prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
                }
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            return prefix;
        }



        /**
        * databinding method to get an XML representation of this object
        *
        */
        public javax.xml.stream.XMLStreamReader getPullParser(javax.xml.namespace.QName qName)
                    throws org.apache.axis2.databinding.ADBException{




                //We can safely assume an element has only one type associated with it
                return localProtocollazioneDocumentoResponse.getPullParser(MY_QNAME);

        }



     /**
      *  Factory class that keeps the parse method
      */
    public static class Factory{




        /**
        * static method to create the object
        * Precondition:  If this object is an element, the current or next start element starts this object and any intervening reader events are ignorable
        *                If this object is not an element, it is a complex type and the reader is at the event just after the outer start element
        * Postcondition: If this object is an element, the reader is positioned at its end element
        *                If this object is a complex type, the reader is positioned at the end element of its outer element
        */
        public static ProtocollazioneDocumentoResponseE parse(javax.xml.stream.XMLStreamReader reader) throws Exception{
            ProtocollazioneDocumentoResponseE object =
                new ProtocollazioneDocumentoResponseE();

            int event;
            String nillableValue = null;
            String prefix ="";
            String namespaceuri ="";
            try {

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();




                // Note all attributes that were handled. Used to differ normal attributes
                // from anyAttributes.
                java.util.Vector handledAttributes = new java.util.Vector();



                while(!reader.isEndElement()) {
                    if (reader.isStartElement() ){

                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://ws.idsunitelm.it/","protocollazioneDocumentoResponse").equals(reader.getName())){

                                                object.setProtocollazioneDocumentoResponse(ProtocollazioneDocumentoResponse.Factory.parse(reader));

                              }  // End of if for expected property start element

                             else{
                                        // A start element we are not expecting indicates an invalid parameter was passed
                                        throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                             }

                             } else {
                                reader.next();
                             }
                           }  // end of while loop




            } catch (javax.xml.stream.XMLStreamException e) {
                throw new Exception(e);
            }

            return object;
        }

        }//end of factory class



        }


        public static class ProtocollazioneDocumentoE
        implements org.apache.axis2.databinding.ADBBean{

                public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName(
                "http://ws.idsunitelm.it/",
                "protocollazioneDocumento",
                "ns1");



                        /**
                        * field for ProtocollazioneDocumento
                        */


                                    protected ProtocollazioneDocumento localProtocollazioneDocumento ;


                           /**
                           * Auto generated getter method
                           * @return ProtocollazioneDocumento
                           */
                           public  ProtocollazioneDocumento getProtocollazioneDocumento(){
                               return localProtocollazioneDocumento;
                           }



                            /**
                               * Auto generated setter method
                               * @param param ProtocollazioneDocumento
                               */
                               public void setProtocollazioneDocumento(ProtocollazioneDocumento param){

                                            this.localProtocollazioneDocumento=param;


                               }




        /**
        *
        * @param parentQName
        * @param factory
        * @return org.apache.axiom.om.OMElement
        */
       public org.apache.axiom.om.OMElement getOMElement (
               final javax.xml.namespace.QName parentQName,
               final org.apache.axiom.om.OMFactory factory) throws org.apache.axis2.databinding.ADBException{



               org.apache.axiom.om.OMDataSource dataSource =
                       new org.apache.axis2.databinding.ADBDataSource(this,MY_QNAME);
               return factory.createOMElement(dataSource,MY_QNAME);

        }

         public void serialize(final javax.xml.namespace.QName parentQName,
                                       javax.xml.stream.XMLStreamWriter xmlWriter)
                                throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{
                           serialize(parentQName,xmlWriter,false);
         }

         public void serialize(final javax.xml.namespace.QName parentQName,
                               javax.xml.stream.XMLStreamWriter xmlWriter,
                               boolean serializeType)
            throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{


                //We can safely assume an element has only one type associated with it

                                 if (localProtocollazioneDocumento==null){
                                   throw new org.apache.axis2.databinding.ADBException("protocollazioneDocumento cannot be null!");
                                 }
                                 localProtocollazioneDocumento.serialize(MY_QNAME,xmlWriter);


        }

        private static String generatePrefix(String namespace) {
            if(namespace.equals("http://ws.idsunitelm.it/")){
                return "ns1";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
         * Utility method to write an element start tag.
         */
        private void writeStartElement(String prefix, String namespace, String localPart,
                                       javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            String writerPrefix = xmlWriter.getPrefix(namespace);
            if (writerPrefix != null) {
                xmlWriter.writeStartElement(namespace, localPart);
            } else {
                if (namespace.length() == 0) {
                    prefix = "";
                } else if (prefix == null) {
                    prefix = generatePrefix(namespace);
                }

                xmlWriter.writeStartElement(prefix, localPart, namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
        }

        /**
         * Util method to write an attribute with the ns prefix
         */
        private void writeAttribute(String prefix,String namespace,String attName,
                                    String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (xmlWriter.getPrefix(namespace) == null) {
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            xmlWriter.writeAttribute(namespace,attName,attValue);
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeAttribute(String namespace,String attName,
                                    String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (namespace.equals("")) {
                xmlWriter.writeAttribute(attName,attValue);
            } else {
                registerPrefix(xmlWriter, namespace);
                xmlWriter.writeAttribute(namespace,attName,attValue);
            }
        }


           /**
             * Util method to write an attribute without the ns prefix
             */
            private void writeQNameAttribute(String namespace, String attName,
                                             javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

                String attributeNamespace = qname.getNamespaceURI();
                String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
                if (attributePrefix == null) {
                    attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
                }
                String attributeValue;
                if (attributePrefix.trim().length() > 0) {
                    attributeValue = attributePrefix + ":" + qname.getLocalPart();
                } else {
                    attributeValue = qname.getLocalPart();
                }

                if (namespace.equals("")) {
                    xmlWriter.writeAttribute(attName, attributeValue);
                } else {
                    registerPrefix(xmlWriter, namespace);
                    xmlWriter.writeAttribute(namespace, attName, attributeValue);
                }
            }
        /**
         *  method to handle Qnames
         */

        private void writeQName(javax.xml.namespace.QName qname,
                                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            String namespaceURI = qname.getNamespaceURI();
            if (namespaceURI != null) {
                String prefix = xmlWriter.getPrefix(namespaceURI);
                if (prefix == null) {
                    prefix = generatePrefix(namespaceURI);
                    xmlWriter.writeNamespace(prefix, namespaceURI);
                    xmlWriter.setPrefix(prefix,namespaceURI);
                }

                if (prefix.trim().length() > 0){
                    xmlWriter.writeCharacters(prefix + ":" + org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                } else {
                    // i.e this is the default namespace
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                }

            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
            }
        }

        private void writeQNames(javax.xml.namespace.QName[] qnames,
                                 javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            if (qnames != null) {
                // we have to store this data until last moment since it is not possible to write any
                // namespace data after writing the charactor data
                StringBuffer stringToWrite = new StringBuffer();
                String namespaceURI = null;
                String prefix = null;

                for (int i = 0; i < qnames.length; i++) {
                    if (i > 0) {
                        stringToWrite.append(" ");
                    }
                    namespaceURI = qnames[i].getNamespaceURI();
                    if (namespaceURI != null) {
                        prefix = xmlWriter.getPrefix(namespaceURI);
                        if ((prefix == null) || (prefix.length() == 0)) {
                            prefix = generatePrefix(namespaceURI);
                            xmlWriter.writeNamespace(prefix, namespaceURI);
                            xmlWriter.setPrefix(prefix,namespaceURI);
                        }

                        if (prefix.trim().length() > 0){
                            stringToWrite.append(prefix).append(":").append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        } else {
                            stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        }
                    } else {
                        stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                    }
                }
                xmlWriter.writeCharacters(stringToWrite.toString());
            }

        }


        /**
         * Register a namespace prefix
         */
        private String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, String namespace) throws javax.xml.stream.XMLStreamException {
            String prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null) {
                prefix = generatePrefix(namespace);
                javax.xml.namespace.NamespaceContext nsContext = xmlWriter.getNamespaceContext();
                while (true) {
                    String uri = nsContext.getNamespaceURI(prefix);
                    if (uri == null || uri.length() == 0) {
                        break;
                    }
                    prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
                }
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            return prefix;
        }



        /**
        * databinding method to get an XML representation of this object
        *
        */
        public javax.xml.stream.XMLStreamReader getPullParser(javax.xml.namespace.QName qName)
                    throws org.apache.axis2.databinding.ADBException{




                //We can safely assume an element has only one type associated with it
                return localProtocollazioneDocumento.getPullParser(MY_QNAME);

        }



     /**
      *  Factory class that keeps the parse method
      */
    public static class Factory{




        /**
        * static method to create the object
        * Precondition:  If this object is an element, the current or next start element starts this object and any intervening reader events are ignorable
        *                If this object is not an element, it is a complex type and the reader is at the event just after the outer start element
        * Postcondition: If this object is an element, the reader is positioned at its end element
        *                If this object is a complex type, the reader is positioned at the end element of its outer element
        */
        public static ProtocollazioneDocumentoE parse(javax.xml.stream.XMLStreamReader reader) throws Exception{
            ProtocollazioneDocumentoE object =
                new ProtocollazioneDocumentoE();

            int event;
            String nillableValue = null;
            String prefix ="";
            String namespaceuri ="";
            try {

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();




                // Note all attributes that were handled. Used to differ normal attributes
                // from anyAttributes.
                java.util.Vector handledAttributes = new java.util.Vector();



                while(!reader.isEndElement()) {
                    if (reader.isStartElement() ){

                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://ws.idsunitelm.it/","protocollazioneDocumento").equals(reader.getName())){

                                                object.setProtocollazioneDocumento(ProtocollazioneDocumento.Factory.parse(reader));

                              }  // End of if for expected property start element

                             else{
                                        // A start element we are not expecting indicates an invalid parameter was passed
                                        throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                             }

                             } else {
                                reader.next();
                             }
                           }  // end of while loop




            } catch (javax.xml.stream.XMLStreamException e) {
                throw new Exception(e);
            }

            return object;
        }

        }//end of factory class



        }


            private  org.apache.axiom.om.OMElement  toOM(ProtocollazioneServiceStub.LoginE param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {


                        try{
                             return param.getOMElement(ProtocollazioneServiceStub.LoginE.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }


            }

            private  org.apache.axiom.om.OMElement  toOM(ProtocollazioneServiceStub.LoginResponseE param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {


                        try{
                             return param.getOMElement(ProtocollazioneServiceStub.LoginResponseE.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }


            }

            private  org.apache.axiom.om.OMElement  toOM(ProtocollazioneServiceStub.ProtocollazioneDocumentoE param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {


                        try{
                             return param.getOMElement(ProtocollazioneServiceStub.ProtocollazioneDocumentoE.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }


            }

            private  org.apache.axiom.om.OMElement  toOM(ProtocollazioneServiceStub.ProtocollazioneDocumentoResponseE param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {


                        try{
                             return param.getOMElement(ProtocollazioneServiceStub.ProtocollazioneDocumentoResponseE.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }


            }

            private  org.apache.axiom.om.OMElement  toOM(ProtocollazioneServiceStub.CreazioneFascicoloE param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {


                        try{
                             return param.getOMElement(ProtocollazioneServiceStub.CreazioneFascicoloE.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }


            }

            private  org.apache.axiom.om.OMElement  toOM(ProtocollazioneServiceStub.CreazioneFascicoloResponseE param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {


                        try{
                             return param.getOMElement(ProtocollazioneServiceStub.CreazioneFascicoloResponseE.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }


            }

            private  org.apache.axiom.om.OMElement  toOM(ProtocollazioneServiceStub.RecuperoDocumentoE param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {


                        try{
                             return param.getOMElement(ProtocollazioneServiceStub.RecuperoDocumentoE.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }


            }

            private  org.apache.axiom.om.OMElement  toOM(ProtocollazioneServiceStub.RecuperoDocumentoResponseE param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {


                        try{
                             return param.getOMElement(ProtocollazioneServiceStub.RecuperoDocumentoResponseE.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }


            }


                                        private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, ProtocollazioneServiceStub.LoginE param, boolean optimizeContent, javax.xml.namespace.QName methodQName)
                                        throws org.apache.axis2.AxisFault{


                                                    try{

                                                            org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                                                            emptyEnvelope.getBody().addChild(param.getOMElement(ProtocollazioneServiceStub.LoginE.MY_QNAME,factory));
                                                            return emptyEnvelope;
                                                        } catch(org.apache.axis2.databinding.ADBException e){
                                                            throw org.apache.axis2.AxisFault.makeFault(e);
                                                        }


                                        }


                             /* methods to provide back word compatibility */



                                        private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, ProtocollazioneServiceStub.ProtocollazioneDocumentoE param, boolean optimizeContent, javax.xml.namespace.QName methodQName)
                                        throws org.apache.axis2.AxisFault{


                                                    try{

                                                            org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                                                            emptyEnvelope.getBody().addChild(param.getOMElement(ProtocollazioneServiceStub.ProtocollazioneDocumentoE.MY_QNAME,factory));
                                                            return emptyEnvelope;
                                                        } catch(org.apache.axis2.databinding.ADBException e){
                                                            throw org.apache.axis2.AxisFault.makeFault(e);
                                                        }


                                        }


                             /* methods to provide back word compatibility */



                                        private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, ProtocollazioneServiceStub.CreazioneFascicoloE param, boolean optimizeContent, javax.xml.namespace.QName methodQName)
                                        throws org.apache.axis2.AxisFault{


                                                    try{

                                                            org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                                                            emptyEnvelope.getBody().addChild(param.getOMElement(ProtocollazioneServiceStub.CreazioneFascicoloE.MY_QNAME,factory));
                                                            return emptyEnvelope;
                                                        } catch(org.apache.axis2.databinding.ADBException e){
                                                            throw org.apache.axis2.AxisFault.makeFault(e);
                                                        }


                                        }


                             /* methods to provide back word compatibility */



                                        private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, ProtocollazioneServiceStub.RecuperoDocumentoE param, boolean optimizeContent, javax.xml.namespace.QName methodQName)
                                        throws org.apache.axis2.AxisFault{


                                                    try{

                                                            org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                                                            emptyEnvelope.getBody().addChild(param.getOMElement(ProtocollazioneServiceStub.RecuperoDocumentoE.MY_QNAME,factory));
                                                            return emptyEnvelope;
                                                        } catch(org.apache.axis2.databinding.ADBException e){
                                                            throw org.apache.axis2.AxisFault.makeFault(e);
                                                        }


                                        }


                             /* methods to provide back word compatibility */




        /**
        *  get the default envelope
        */
        private org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory){
        return factory.getDefaultEnvelope();
        }


        private  Object fromOM(
        org.apache.axiom.om.OMElement param,
        Class type,
        java.util.Map extraNamespaces) throws org.apache.axis2.AxisFault{

        try {

                if (ProtocollazioneServiceStub.LoginE.class.equals(type)){

                           return ProtocollazioneServiceStub.LoginE.Factory.parse(param.getXMLStreamReaderWithoutCaching());


                }

                if (ProtocollazioneServiceStub.LoginResponseE.class.equals(type)){

                           return ProtocollazioneServiceStub.LoginResponseE.Factory.parse(param.getXMLStreamReaderWithoutCaching());


                }

                if (ProtocollazioneServiceStub.ProtocollazioneDocumentoE.class.equals(type)){

                           return ProtocollazioneServiceStub.ProtocollazioneDocumentoE.Factory.parse(param.getXMLStreamReaderWithoutCaching());


                }

                if (ProtocollazioneServiceStub.ProtocollazioneDocumentoResponseE.class.equals(type)){

                           return ProtocollazioneServiceStub.ProtocollazioneDocumentoResponseE.Factory.parse(param.getXMLStreamReaderWithoutCaching());


                }

                if (ProtocollazioneServiceStub.CreazioneFascicoloE.class.equals(type)){

                           return ProtocollazioneServiceStub.CreazioneFascicoloE.Factory.parse(param.getXMLStreamReaderWithoutCaching());


                }

                if (ProtocollazioneServiceStub.CreazioneFascicoloResponseE.class.equals(type)){

                           return ProtocollazioneServiceStub.CreazioneFascicoloResponseE.Factory.parse(param.getXMLStreamReaderWithoutCaching());


                }

                if (ProtocollazioneServiceStub.RecuperoDocumentoE.class.equals(type)){

                           return ProtocollazioneServiceStub.RecuperoDocumentoE.Factory.parse(param.getXMLStreamReaderWithoutCaching());


                }

                if (ProtocollazioneServiceStub.RecuperoDocumentoResponseE.class.equals(type)){

                           return ProtocollazioneServiceStub.RecuperoDocumentoResponseE.Factory.parse(param.getXMLStreamReaderWithoutCaching());


                }

        } catch (Exception e) {
        throw org.apache.axis2.AxisFault.makeFault(e);
        }
           return null;
        }



    
   }
   