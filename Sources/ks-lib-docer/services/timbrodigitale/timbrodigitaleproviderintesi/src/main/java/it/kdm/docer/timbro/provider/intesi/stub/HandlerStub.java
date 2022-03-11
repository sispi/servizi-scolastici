
/**
 * HandlerStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */
        package it.kdm.docer.timbro.provider.intesi.stub;

        

        /*
        *  HandlerStub java implementation
        */

        
        public class HandlerStub extends org.apache.axis2.client.Stub
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
     _service = new org.apache.axis2.description.AxisService("Handler" + getUniqueSuffix());
     addAnonymousOperations();

        //creating the operations
        org.apache.axis2.description.AxisOperation __operation;

        _operations = new org.apache.axis2.description.AxisOperation[4];

                   __operation = new org.apache.axis2.description.OutInAxisOperation();


            __operation.setName(new javax.xml.namespace.QName("http://soap.qrbox.intesigroup.com", "getPkBoxClientVersion"));
	    _service.addOperation(__operation);




            _operations[0]=__operation;


                   __operation = new org.apache.axis2.description.OutInAxisOperation();


            __operation.setName(new javax.xml.namespace.QName("http://soap.qrbox.intesigroup.com", "addStampAndSign"));
	    _service.addOperation(__operation);




            _operations[1]=__operation;


                   __operation = new org.apache.axis2.description.OutInAxisOperation();


            __operation.setName(new javax.xml.namespace.QName("http://soap.qrbox.intesigroup.com", "flushCache"));
	    _service.addOperation(__operation);




            _operations[2]=__operation;


                   __operation = new org.apache.axis2.description.OutInAxisOperation();


            __operation.setName(new javax.xml.namespace.QName("http://soap.qrbox.intesigroup.com", "getVersion"));
	    _service.addOperation(__operation);




            _operations[3]=__operation;


        }

    //populates the faults
    private void populateFaults(){

              faultExceptionNameMap.put(new org.apache.axis2.client.FaultMapKey(new javax.xml.namespace.QName("http://soap.qrbox.intesigroup.com","handlerQRBException"), "AddStampAndSign"),"it.kdm.docer.timbro.provider.intesi.stub.HandlerQRBExceptionException");
              faultExceptionClassNameMap.put(new org.apache.axis2.client.FaultMapKey(new javax.xml.namespace.QName("http://soap.qrbox.intesigroup.com","handlerQRBException"), "AddStampAndSign"),"it.kdm.docer.timbro.provider.intesi.stub.HandlerQRBExceptionException");
              faultMessageMap.put(new org.apache.axis2.client.FaultMapKey(new javax.xml.namespace.QName("http://soap.qrbox.intesigroup.com","handlerQRBException"), "AddStampAndSign"),"it.kdm.docer.timbro.provider.intesi.stub.HandlerStub$HandlerQRBException");

              faultExceptionNameMap.put(new org.apache.axis2.client.FaultMapKey(new javax.xml.namespace.QName("http://soap.qrbox.intesigroup.com","handlerQRBException"), "flushCache"),"it.kdm.docer.timbro.provider.intesi.stub.HandlerQRBExceptionException");
              faultExceptionClassNameMap.put(new org.apache.axis2.client.FaultMapKey(new javax.xml.namespace.QName("http://soap.qrbox.intesigroup.com","handlerQRBException"), "flushCache"),"it.kdm.docer.timbro.provider.intesi.stub.HandlerQRBExceptionException");
              faultMessageMap.put(new org.apache.axis2.client.FaultMapKey(new javax.xml.namespace.QName("http://soap.qrbox.intesigroup.com","handlerQRBException"), "flushCache"),"it.kdm.docer.timbro.provider.intesi.stub.HandlerStub$HandlerQRBException");



    }

    /**
      *Constructor that takes in a configContext
      */

    public HandlerStub(org.apache.axis2.context.ConfigurationContext configurationContext,
       String targetEndpoint)
       throws org.apache.axis2.AxisFault {
         this(configurationContext,targetEndpoint,false);
   }


   /**
     * Constructor that takes in a configContext  and useseperate listner
     */
   public HandlerStub(org.apache.axis2.context.ConfigurationContext configurationContext,
        String targetEndpoint, boolean useSeparateListener)
        throws org.apache.axis2.AxisFault {
         //To populate AxisService
         populateAxisService();
         populateFaults();

        _serviceClient = new org.apache.axis2.client.ServiceClient(configurationContext,_service);


        _serviceClient.getOptions().setTo(new org.apache.axis2.addressing.EndpointReference(
                targetEndpoint));
        _serviceClient.getOptions().setUseSeparateListener(useSeparateListener);

            //Set the soap version
            _serviceClient.getOptions().setSoapVersionURI(org.apache.axiom.soap.SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI);


    }

    /**
     * Default Constructor
     */
    public HandlerStub(org.apache.axis2.context.ConfigurationContext configurationContext) throws org.apache.axis2.AxisFault {

                    this(configurationContext,"http://192.168.0.58:8080/qrbox-server/services/handler.handlerHttpSoap12Endpoint/" );

    }

    /**
     * Default Constructor
     */
    public HandlerStub() throws org.apache.axis2.AxisFault {

                    this("http://192.168.0.58:8080/qrbox-server/services/handler.handlerHttpSoap12Endpoint/" );

    }

    /**
     * Constructor taking the target endpoint
     */
    public HandlerStub(String targetEndpoint) throws org.apache.axis2.AxisFault {
        this(null,targetEndpoint);
    }




                    /**
                     * Auto generated method signature
                     *
                     * @see it.kdm.docer.timbro.provider.intesi.stub.Handler#getPkBoxClientVersion
                     * @param getPkBoxClientVersion

                     */



                            public  it.kdm.docer.timbro.provider.intesi.stub.HandlerStub.GetPkBoxClientVersionResponse getPkBoxClientVersion(

                            it.kdm.docer.timbro.provider.intesi.stub.HandlerStub.GetPkBoxClientVersion getPkBoxClientVersion)


                    throws java.rmi.RemoteException

                    {
              org.apache.axis2.context.MessageContext _messageContext = null;
              try{
               org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[0].getName());
              _operationClient.getOptions().setAction("urn:getPkBoxClientVersion");
              _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);



                  addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");


              // create a message context
              _messageContext = new org.apache.axis2.context.MessageContext();



              // create SOAP envelope with that payload
              org.apache.axiom.soap.SOAPEnvelope env = null;


                                                    env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                                                    getPkBoxClientVersion,
                                                    optimizeContent(new javax.xml.namespace.QName("http://soap.qrbox.intesigroup.com",
                                                    "getPkBoxClientVersion")), new javax.xml.namespace.QName("http://soap.qrbox.intesigroup.com",
                                                    "getPkBoxClientVersion"));

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
                                             it.kdm.docer.timbro.provider.intesi.stub.HandlerStub.GetPkBoxClientVersionResponse.class,
                                              getEnvelopeNamespaces(_returnEnv));


                                        return (it.kdm.docer.timbro.provider.intesi.stub.HandlerStub.GetPkBoxClientVersionResponse)object;

         }catch(org.apache.axis2.AxisFault f){

            org.apache.axiom.om.OMElement faultElt = f.getDetail();
            if (faultElt!=null){
                if (faultExceptionNameMap.containsKey(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"getPkBoxClientVersion"))){
                    //make the fault by reflection
                    try{
                        String exceptionClassName = (String)faultExceptionClassNameMap.get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"getPkBoxClientVersion"));
                        Class exceptionClass = Class.forName(exceptionClassName);
                        java.lang.reflect.Constructor constructor = exceptionClass.getConstructor(String.class);
                        Exception ex = (Exception) constructor.newInstance(f.getMessage());
                        //message class
                        String messageClassName = (String)faultMessageMap.get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"getPkBoxClientVersion"));
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
                     * @see it.kdm.docer.timbro.provider.intesi.stub.Handler#addStampAndSign
                     * @param addStampAndSign

                     * @throws it.kdm.docer.timbro.provider.intesi.stub.HandlerQRBExceptionException :
                     */



                            public  it.kdm.docer.timbro.provider.intesi.stub.HandlerStub.AddStampAndSignResponseE addStampAndSign(

                            it.kdm.docer.timbro.provider.intesi.stub.HandlerStub.AddStampAndSign addStampAndSign)


                    throws java.rmi.RemoteException


                        ,it.kdm.docer.timbro.provider.intesi.stub.HandlerQRBExceptionException{
              org.apache.axis2.context.MessageContext _messageContext = null;
              try{
               org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[1].getName());
              _operationClient.getOptions().setAction("urn:AddStampAndSign");
              _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);



                  addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");


              // create a message context
              _messageContext = new org.apache.axis2.context.MessageContext();



              // create SOAP envelope with that payload
              org.apache.axiom.soap.SOAPEnvelope env = null;


                                                    env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                                                    addStampAndSign,
                                                    optimizeContent(new javax.xml.namespace.QName("http://soap.qrbox.intesigroup.com",
                                                    "addStampAndSign")), new javax.xml.namespace.QName("http://soap.qrbox.intesigroup.com",
                                                    "addStampAndSign"));

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
                                             it.kdm.docer.timbro.provider.intesi.stub.HandlerStub.AddStampAndSignResponseE.class,
                                              getEnvelopeNamespaces(_returnEnv));


                                        return (it.kdm.docer.timbro.provider.intesi.stub.HandlerStub.AddStampAndSignResponseE)object;

         }catch(org.apache.axis2.AxisFault f){

            org.apache.axiom.om.OMElement faultElt = f.getDetail();
            if (faultElt!=null){
                if (faultExceptionNameMap.containsKey(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"AddStampAndSign"))){
                    //make the fault by reflection
                    try{
                        String exceptionClassName = (String)faultExceptionClassNameMap.get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"AddStampAndSign"));
                        Class exceptionClass = Class.forName(exceptionClassName);
                        java.lang.reflect.Constructor constructor = exceptionClass.getConstructor(String.class);
                        Exception ex = (Exception) constructor.newInstance(f.getMessage());
                        //message class
                        String messageClassName = (String)faultMessageMap.get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"AddStampAndSign"));
                        Class messageClass = Class.forName(messageClassName);
                        Object messageObject = fromOM(faultElt,messageClass,null);
                        java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
                                   new Class[]{messageClass});
                        m.invoke(ex,new Object[]{messageObject});

                        if (ex instanceof it.kdm.docer.timbro.provider.intesi.stub.HandlerQRBExceptionException){
                          throw (it.kdm.docer.timbro.provider.intesi.stub.HandlerQRBExceptionException)ex;
                        }


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
                     * @see it.kdm.docer.timbro.provider.intesi.stub.Handler#flushCache
                     * @param flushCache

                     * @throws it.kdm.docer.timbro.provider.intesi.stub.HandlerQRBExceptionException :
                     */



                            public  void flushCache(

                            it.kdm.docer.timbro.provider.intesi.stub.HandlerStub.FlushCache flushCache)


                    throws java.rmi.RemoteException


                        ,it.kdm.docer.timbro.provider.intesi.stub.HandlerQRBExceptionException{
              org.apache.axis2.context.MessageContext _messageContext = null;
              try{
               org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[2].getName());
              _operationClient.getOptions().setAction("urn:flushCache");
              _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);



                  addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");


              // create a message context
              _messageContext = new org.apache.axis2.context.MessageContext();



              // create SOAP envelope with that payload
              org.apache.axiom.soap.SOAPEnvelope env = null;


                                                    env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                                                    flushCache,
                                                    optimizeContent(new javax.xml.namespace.QName("http://soap.qrbox.intesigroup.com",
                                                    "flushCache")), new javax.xml.namespace.QName("http://soap.qrbox.intesigroup.com",
                                                    "flushCache"));

        //adding SOAP soap_headers
         _serviceClient.addHeadersToEnvelope(env);
        // set the message context with that soap envelope
        _messageContext.setEnvelope(env);

        // add the message contxt to the operation client
        _operationClient.addMessageContext(_messageContext);

        //execute the operation client
        _operationClient.execute(true);


                return;

         }catch(org.apache.axis2.AxisFault f){

            org.apache.axiom.om.OMElement faultElt = f.getDetail();
            if (faultElt!=null){
                if (faultExceptionNameMap.containsKey(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"flushCache"))){
                    //make the fault by reflection
                    try{
                        String exceptionClassName = (String)faultExceptionClassNameMap.get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"flushCache"));
                        Class exceptionClass = Class.forName(exceptionClassName);
                        java.lang.reflect.Constructor constructor = exceptionClass.getConstructor(String.class);
                        Exception ex = (Exception) constructor.newInstance(f.getMessage());
                        //message class
                        String messageClassName = (String)faultMessageMap.get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"flushCache"));
                        Class messageClass = Class.forName(messageClassName);
                        Object messageObject = fromOM(faultElt,messageClass,null);
                        java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
                                   new Class[]{messageClass});
                        m.invoke(ex,new Object[]{messageObject});

                        if (ex instanceof it.kdm.docer.timbro.provider.intesi.stub.HandlerQRBExceptionException){
                          throw (it.kdm.docer.timbro.provider.intesi.stub.HandlerQRBExceptionException)ex;
                        }


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
                     * @see it.kdm.docer.timbro.provider.intesi.stub.Handler#getVersion
                     * @param getVersion

                     */



                            public  it.kdm.docer.timbro.provider.intesi.stub.HandlerStub.GetVersionResponse getVersion(

                            it.kdm.docer.timbro.provider.intesi.stub.HandlerStub.GetVersion getVersion)


                    throws java.rmi.RemoteException

                    {
              org.apache.axis2.context.MessageContext _messageContext = null;
              try{
               org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[3].getName());
              _operationClient.getOptions().setAction("urn:getVersion");
              _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);



                  addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");


              // create a message context
              _messageContext = new org.apache.axis2.context.MessageContext();



              // create SOAP envelope with that payload
              org.apache.axiom.soap.SOAPEnvelope env = null;


                                                    env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                                                    getVersion,
                                                    optimizeContent(new javax.xml.namespace.QName("http://soap.qrbox.intesigroup.com",
                                                    "getVersion")), new javax.xml.namespace.QName("http://soap.qrbox.intesigroup.com",
                                                    "getVersion"));

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
                                             it.kdm.docer.timbro.provider.intesi.stub.HandlerStub.GetVersionResponse.class,
                                              getEnvelopeNamespaces(_returnEnv));


                                        return (it.kdm.docer.timbro.provider.intesi.stub.HandlerStub.GetVersionResponse)object;

         }catch(org.apache.axis2.AxisFault f){

            org.apache.axiom.om.OMElement faultElt = f.getDetail();
            if (faultElt!=null){
                if (faultExceptionNameMap.containsKey(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"getVersion"))){
                    //make the fault by reflection
                    try{
                        String exceptionClassName = (String)faultExceptionClassNameMap.get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"getVersion"));
                        Class exceptionClass = Class.forName(exceptionClassName);
                        java.lang.reflect.Constructor constructor = exceptionClass.getConstructor(String.class);
                        Exception ex = (Exception) constructor.newInstance(f.getMessage());
                        //message class
                        String messageClassName = (String)faultMessageMap.get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"getVersion"));
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
     //http://192.168.0.58:8080/qrbox-server/services/handler.handlerHttpSoap12Endpoint/
        public static class AddStampAndSignResponse
        implements org.apache.axis2.databinding.ADBBean{
        /* This type was generated from the piece of schema that had
                name = AddStampAndSignResponse
                Namespace URI = http://response.soap.qrbox.intesigroup.com/xsd
                Namespace Prefix = ns4
                */


                        /**
                        * field for Attachment
                        */


                                    protected javax.activation.DataHandler localAttachment ;

                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localAttachmentTracker = false ;

                           public boolean isAttachmentSpecified(){
                               return localAttachmentTracker;
                           }



                           /**
                           * Auto generated getter method
                           * @return javax.activation.DataHandler
                           */
                           public  javax.activation.DataHandler getAttachment(){
                               return localAttachment;
                           }



                            /**
                               * Auto generated setter method
                               * @param param Attachment
                               */
                               public void setAttachment(javax.activation.DataHandler param){
                            localAttachmentTracker = true;

                                            this.localAttachment=param;


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


                   String namespacePrefix = registerPrefix(xmlWriter,"http://response.soap.qrbox.intesigroup.com/xsd");
                   if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                       writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                           namespacePrefix+":AddStampAndSignResponse",
                           xmlWriter);
                   } else {
                       writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                           "AddStampAndSignResponse",
                           xmlWriter);
                   }


                   }
                if (localAttachmentTracker){
                                    namespace = "http://response.soap.qrbox.intesigroup.com/xsd";
                                    writeStartElement(null, namespace, "attachment", xmlWriter);


                                    if (localAttachment!=null)  {
                                       try {
                                           org.apache.axiom.util.stax.XMLStreamWriterUtils.writeDataHandler(xmlWriter, localAttachment, null, true);
                                       } catch (java.io.IOException ex) {
                                           throw new javax.xml.stream.XMLStreamException("Unable to read data handler for attachment", ex);
                                       }
                                    } else {

                                             writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                                    }

                                   xmlWriter.writeEndElement();
                             }
                    xmlWriter.writeEndElement();


        }

        private static String generatePrefix(String namespace) {
            if(namespace.equals("http://response.soap.qrbox.intesigroup.com/xsd")){
                return "ns4";
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

                 if (localAttachmentTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://response.soap.qrbox.intesigroup.com/xsd",
                                        "attachment"));

                            elementList.add(localAttachment);
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
        public static AddStampAndSignResponse parse(javax.xml.stream.XMLStreamReader reader) throws Exception{
            AddStampAndSignResponse object =
                new AddStampAndSignResponse();

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

                            if (!"AddStampAndSignResponse".equals(type)){
                                //find namespace for the prefix
                                String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (AddStampAndSignResponse) ExtensionMapper.getTypeObject(
                                        nsUri, type, reader);
                              }


                  }


                }




                // Note all attributes that were handled. Used to differ normal attributes
                // from anyAttributes.
                java.util.Vector handledAttributes = new java.util.Vector();




                    reader.next();


                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://response.soap.qrbox.intesigroup.com/xsd","attachment").equals(reader.getName())){

                                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                        if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                             object.setAttachment(null);
                                             reader.next();
                                        } else {

                                            object.setAttachment(org.apache.axiom.util.stax.XMLStreamReaderUtils.getDataHandlerFromElement(reader));

                                        }

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


        public static class AddStampAndSign
        implements org.apache.axis2.databinding.ADBBean{

                public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName(
                "http://soap.qrbox.intesigroup.com",
                "AddStampAndSign",
                "ns5");



                        /**
                        * field for Request
                        */


                                    protected AddStampAndSignRequest localRequest ;

                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localRequestTracker = false ;

                           public boolean isRequestSpecified(){
                               return localRequestTracker;
                           }



                           /**
                           * Auto generated getter method
                           * @return AddStampAndSignRequest
                           */
                           public  AddStampAndSignRequest getRequest(){
                               return localRequest;
                           }



                            /**
                               * Auto generated setter method
                               * @param param Request
                               */
                               public void setRequest(AddStampAndSignRequest param){
                            localRequestTracker = true;

                                            this.localRequest=param;


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




                String prefix = null;
                String namespace = null;


                    prefix = parentQName.getPrefix();
                    namespace = parentQName.getNamespaceURI();
                    writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

                  if (serializeType){


                   String namespacePrefix = registerPrefix(xmlWriter,"http://soap.qrbox.intesigroup.com");
                   if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                       writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                           namespacePrefix+":AddStampAndSign",
                           xmlWriter);
                   } else {
                       writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                           "AddStampAndSign",
                           xmlWriter);
                   }


                   }
                if (localRequestTracker){
                                    if (localRequest==null){

                                        writeStartElement(null, "http://soap.qrbox.intesigroup.com", "request", xmlWriter);

                                       // write the nil attribute
                                      writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                      xmlWriter.writeEndElement();
                                    }else{
                                     localRequest.serialize(new javax.xml.namespace.QName("http://soap.qrbox.intesigroup.com","request"),
                                        xmlWriter);
                                    }
                                }
                    xmlWriter.writeEndElement();


        }

        private static String generatePrefix(String namespace) {
            if(namespace.equals("http://soap.qrbox.intesigroup.com")){
                return "ns5";
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

                 if (localRequestTracker){
                            elementList.add(new javax.xml.namespace.QName("http://soap.qrbox.intesigroup.com",
                                                                      "request"));


                                    elementList.add(localRequest==null?null:
                                    localRequest);
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
        public static AddStampAndSign parse(javax.xml.stream.XMLStreamReader reader) throws Exception{
            AddStampAndSign object =
                new AddStampAndSign();

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

                            if (!"AddStampAndSign".equals(type)){
                                //find namespace for the prefix
                                String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (AddStampAndSign) ExtensionMapper.getTypeObject(
                                        nsUri, type, reader);
                              }


                  }


                }




                // Note all attributes that were handled. Used to differ normal attributes
                // from anyAttributes.
                java.util.Vector handledAttributes = new java.util.Vector();




                    reader.next();


                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://soap.qrbox.intesigroup.com","request").equals(reader.getName())){

                                      nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                      if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                          object.setRequest(null);
                                          reader.next();

                                            reader.next();

                                      }else{

                                                object.setRequest(AddStampAndSignRequest.Factory.parse(reader));

                                        reader.next();
                                    }
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


        public static class GetPkBoxClientVersionResponse
        implements org.apache.axis2.databinding.ADBBean{

                public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName(
                "http://soap.qrbox.intesigroup.com",
                "getPkBoxClientVersionResponse",
                "ns5");



                        /**
                        * field for _return
                        */


                                    protected String local_return ;

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
                           * @return java.lang.String
                           */
                           public  String get_return(){
                               return local_return;
                           }



                            /**
                               * Auto generated setter method
                               * @param param _return
                               */
                               public void set_return(String param){
                            local_returnTracker = true;

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




                String prefix = null;
                String namespace = null;


                    prefix = parentQName.getPrefix();
                    namespace = parentQName.getNamespaceURI();
                    writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

                  if (serializeType){


                   String namespacePrefix = registerPrefix(xmlWriter,"http://soap.qrbox.intesigroup.com");
                   if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                       writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                           namespacePrefix+":getPkBoxClientVersionResponse",
                           xmlWriter);
                   } else {
                       writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                           "getPkBoxClientVersionResponse",
                           xmlWriter);
                   }


                   }
                if (local_returnTracker){
                                    namespace = "http://soap.qrbox.intesigroup.com";
                                    writeStartElement(null, namespace, "return", xmlWriter);


                                          if (local_return==null){
                                              // write the nil attribute

                                                     writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                                          }else{


                                                   xmlWriter.writeCharacters(local_return);

                                          }

                                   xmlWriter.writeEndElement();
                             }
                    xmlWriter.writeEndElement();


        }

        private static String generatePrefix(String namespace) {
            if(namespace.equals("http://soap.qrbox.intesigroup.com")){
                return "ns5";
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
                                      elementList.add(new javax.xml.namespace.QName("http://soap.qrbox.intesigroup.com",
                                                                      "return"));

                                         elementList.add(local_return==null?null:
                                         org.apache.axis2.databinding.utils.ConverterUtil.convertToString(local_return));
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
        public static GetPkBoxClientVersionResponse parse(javax.xml.stream.XMLStreamReader reader) throws Exception{
            GetPkBoxClientVersionResponse object =
                new GetPkBoxClientVersionResponse();

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

                            if (!"getPkBoxClientVersionResponse".equals(type)){
                                //find namespace for the prefix
                                String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (GetPkBoxClientVersionResponse) ExtensionMapper.getTypeObject(
                                        nsUri, type, reader);
                              }


                  }


                }




                // Note all attributes that were handled. Used to differ normal attributes
                // from anyAttributes.
                java.util.Vector handledAttributes = new java.util.Vector();




                    reader.next();


                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://soap.qrbox.intesigroup.com","return").equals(reader.getName())){

                                       nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                       if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                                    String content = reader.getElementText();

                                              object.set_return(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                                       } else {


                                           reader.getElementText(); // throw away text nodes if any.
                                       }

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


        public static class GetVersion
        implements org.apache.axis2.databinding.ADBBean{

                public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName(
                "http://soap.qrbox.intesigroup.com",
                "getVersion",
                "ns5");





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




                String prefix = null;
                String namespace = null;


                    prefix = parentQName.getPrefix();
                    namespace = parentQName.getNamespaceURI();
                    writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

                  if (serializeType){


                   String namespacePrefix = registerPrefix(xmlWriter,"http://soap.qrbox.intesigroup.com");
                   if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                       writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                           namespacePrefix+":getVersion",
                           xmlWriter);
                   } else {
                       writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                           "getVersion",
                           xmlWriter);
                   }


                   }

                    xmlWriter.writeEndElement();


        }

        private static String generatePrefix(String namespace) {
            if(namespace.equals("http://soap.qrbox.intesigroup.com")){
                return "ns5";
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
        public static GetVersion parse(javax.xml.stream.XMLStreamReader reader) throws Exception{
            GetVersion object =
                new GetVersion();

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

                            if (!"getVersion".equals(type)){
                                //find namespace for the prefix
                                String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (GetVersion) ExtensionMapper.getTypeObject(
                                        nsUri, type, reader);
                              }


                  }


                }




                // Note all attributes that were handled. Used to differ normal attributes
                // from anyAttributes.
                java.util.Vector handledAttributes = new java.util.Vector();




                    reader.next();

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


        public static class GetPkBoxClientVersion
        implements org.apache.axis2.databinding.ADBBean{

                public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName(
                "http://soap.qrbox.intesigroup.com",
                "getPkBoxClientVersion",
                "ns5");





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




                String prefix = null;
                String namespace = null;


                    prefix = parentQName.getPrefix();
                    namespace = parentQName.getNamespaceURI();
                    writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

                  if (serializeType){


                   String namespacePrefix = registerPrefix(xmlWriter,"http://soap.qrbox.intesigroup.com");
                   if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                       writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                           namespacePrefix+":getPkBoxClientVersion",
                           xmlWriter);
                   } else {
                       writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                           "getPkBoxClientVersion",
                           xmlWriter);
                   }


                   }

                    xmlWriter.writeEndElement();


        }

        private static String generatePrefix(String namespace) {
            if(namespace.equals("http://soap.qrbox.intesigroup.com")){
                return "ns5";
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
        public static GetPkBoxClientVersion parse(javax.xml.stream.XMLStreamReader reader) throws Exception{
            GetPkBoxClientVersion object =
                new GetPkBoxClientVersion();

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

                            if (!"getPkBoxClientVersion".equals(type)){
                                //find namespace for the prefix
                                String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (GetPkBoxClientVersion) ExtensionMapper.getTypeObject(
                                        nsUri, type, reader);
                              }


                  }


                }




                // Note all attributes that were handled. Used to differ normal attributes
                // from anyAttributes.
                java.util.Vector handledAttributes = new java.util.Vector();




                    reader.next();

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


        public static class KeyValueData
        implements org.apache.axis2.databinding.ADBBean{
        /* This type was generated from the piece of schema that had
                name = KeyValueData
                Namespace URI = http://dataobject.qrbox.intesigroup.com/xsd
                Namespace Prefix = ns2
                */


                        /**
                        * field for Key
                        */


                                    protected String localKey ;

                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localKeyTracker = false ;

                           public boolean isKeySpecified(){
                               return localKeyTracker;
                           }



                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  String getKey(){
                               return localKey;
                           }



                            /**
                               * Auto generated setter method
                               * @param param Key
                               */
                               public void setKey(String param){
                            localKeyTracker = true;

                                            this.localKey=param;


                               }


                        /**
                        * field for Value
                        */


                                    protected String localValue ;

                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localValueTracker = false ;

                           public boolean isValueSpecified(){
                               return localValueTracker;
                           }



                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  String getValue(){
                               return localValue;
                           }



                            /**
                               * Auto generated setter method
                               * @param param Value
                               */
                               public void setValue(String param){
                            localValueTracker = true;

                                            this.localValue=param;


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


                   String namespacePrefix = registerPrefix(xmlWriter,"http://dataobject.qrbox.intesigroup.com/xsd");
                   if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                       writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                           namespacePrefix+":KeyValueData",
                           xmlWriter);
                   } else {
                       writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                           "KeyValueData",
                           xmlWriter);
                   }


                   }
                if (localKeyTracker){
                                    namespace = "http://dataobject.qrbox.intesigroup.com/xsd";
                                    writeStartElement(null, namespace, "key", xmlWriter);


                                          if (localKey==null){
                                              // write the nil attribute

                                                     writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                                          }else{


                                                   xmlWriter.writeCharacters(localKey);

                                          }

                                   xmlWriter.writeEndElement();
                             } if (localValueTracker){
                                    namespace = "http://dataobject.qrbox.intesigroup.com/xsd";
                                    writeStartElement(null, namespace, "value", xmlWriter);


                                          if (localValue==null){
                                              // write the nil attribute

                                                     writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                                          }else{


                                                   xmlWriter.writeCharacters(localValue);

                                          }

                                   xmlWriter.writeEndElement();
                             }
                    xmlWriter.writeEndElement();


        }

        private static String generatePrefix(String namespace) {
            if(namespace.equals("http://dataobject.qrbox.intesigroup.com/xsd")){
                return "ns2";
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

                 if (localKeyTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://dataobject.qrbox.intesigroup.com/xsd",
                                                                      "key"));

                                         elementList.add(localKey==null?null:
                                         org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localKey));
                                    } if (localValueTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://dataobject.qrbox.intesigroup.com/xsd",
                                                                      "value"));

                                         elementList.add(localValue==null?null:
                                         org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localValue));
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
        public static KeyValueData parse(javax.xml.stream.XMLStreamReader reader) throws Exception{
            KeyValueData object =
                new KeyValueData();

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

                            if (!"KeyValueData".equals(type)){
                                //find namespace for the prefix
                                String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (KeyValueData) ExtensionMapper.getTypeObject(
                                        nsUri, type, reader);
                              }


                  }


                }




                // Note all attributes that were handled. Used to differ normal attributes
                // from anyAttributes.
                java.util.Vector handledAttributes = new java.util.Vector();




                    reader.next();


                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://dataobject.qrbox.intesigroup.com/xsd","key").equals(reader.getName())){

                                       nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                       if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                                    String content = reader.getElementText();

                                              object.setKey(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                                       } else {


                                           reader.getElementText(); // throw away text nodes if any.
                                       }

                                        reader.next();

                              }  // End of if for expected property start element

                                    else {

                                    }


                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://dataobject.qrbox.intesigroup.com/xsd","value").equals(reader.getName())){

                                       nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                       if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                                    String content = reader.getElementText();

                                              object.setValue(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                                       } else {


                                           reader.getElementText(); // throw away text nodes if any.
                                       }

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


        public static class ExtensionMapper{

          public static Object getTypeObject(String namespaceURI,
                                                       String typeName,
                                                       javax.xml.stream.XMLStreamReader reader) throws Exception{


                  if (
                  "http://dataobject.qrbox.intesigroup.com/xsd".equals(namespaceURI) &&
                  "KeyValueData".equals(typeName)){

                            return  KeyValueData.Factory.parse(reader);


                  }


                  if (
                  "http://response.soap.qrbox.intesigroup.com/xsd".equals(namespaceURI) &&
                  "AddStampAndSignResponse".equals(typeName)){

                            return  AddStampAndSignResponse.Factory.parse(reader);


                  }


                  if (
                  "http://request.soap.qrbox.intesigroup.com/xsd".equals(namespaceURI) &&
                  "AddStampAndSignRequest".equals(typeName)){

                            return  AddStampAndSignRequest.Factory.parse(reader);


                  }


                  if (
                  "http://exception.core.qrbox.intesigroup.com/xsd".equals(namespaceURI) &&
                  "QRBException".equals(typeName)){

                            return  QRBException.Factory.parse(reader);


                  }


             throw new org.apache.axis2.databinding.ADBException("Unsupported type " + namespaceURI + " " + typeName);
          }

        }

        public static class FlushCache
        implements org.apache.axis2.databinding.ADBBean{

                public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName(
                "http://soap.qrbox.intesigroup.com",
                "flushCache",
                "ns5");



                        /**
                        * field for Cache
                        */


                                    protected int localCache ;

                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localCacheTracker = false ;

                           public boolean isCacheSpecified(){
                               return localCacheTracker;
                           }



                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getCache(){
                               return localCache;
                           }



                            /**
                               * Auto generated setter method
                               * @param param Cache
                               */
                               public void setCache(int param){

                                       // setting primitive attribute tracker to true
                                       localCacheTracker =
                                       param != Integer.MIN_VALUE;

                                            this.localCache=param;


                               }


                        /**
                        * field for Key
                        */


                                    protected String localKey ;

                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localKeyTracker = false ;

                           public boolean isKeySpecified(){
                               return localKeyTracker;
                           }



                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  String getKey(){
                               return localKey;
                           }



                            /**
                               * Auto generated setter method
                               * @param param Key
                               */
                               public void setKey(String param){
                            localKeyTracker = true;

                                            this.localKey=param;


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




                String prefix = null;
                String namespace = null;


                    prefix = parentQName.getPrefix();
                    namespace = parentQName.getNamespaceURI();
                    writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

                  if (serializeType){


                   String namespacePrefix = registerPrefix(xmlWriter,"http://soap.qrbox.intesigroup.com");
                   if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                       writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                           namespacePrefix+":flushCache",
                           xmlWriter);
                   } else {
                       writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                           "flushCache",
                           xmlWriter);
                   }


                   }
                if (localCacheTracker){
                                    namespace = "http://soap.qrbox.intesigroup.com";
                                    writeStartElement(null, namespace, "cache", xmlWriter);

                                               if (localCache== Integer.MIN_VALUE) {

                                                         throw new org.apache.axis2.databinding.ADBException("cache cannot be null!!");

                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localCache));
                                               }

                                   xmlWriter.writeEndElement();
                             } if (localKeyTracker){
                                    namespace = "http://soap.qrbox.intesigroup.com";
                                    writeStartElement(null, namespace, "key", xmlWriter);


                                          if (localKey==null){
                                              // write the nil attribute

                                                     writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                                          }else{


                                                   xmlWriter.writeCharacters(localKey);

                                          }

                                   xmlWriter.writeEndElement();
                             }
                    xmlWriter.writeEndElement();


        }

        private static String generatePrefix(String namespace) {
            if(namespace.equals("http://soap.qrbox.intesigroup.com")){
                return "ns5";
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

                 if (localCacheTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://soap.qrbox.intesigroup.com",
                                                                      "cache"));

                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localCache));
                            } if (localKeyTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://soap.qrbox.intesigroup.com",
                                                                      "key"));

                                         elementList.add(localKey==null?null:
                                         org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localKey));
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
        public static FlushCache parse(javax.xml.stream.XMLStreamReader reader) throws Exception{
            FlushCache object =
                new FlushCache();

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

                            if (!"flushCache".equals(type)){
                                //find namespace for the prefix
                                String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (FlushCache) ExtensionMapper.getTypeObject(
                                        nsUri, type, reader);
                              }


                  }


                }




                // Note all attributes that were handled. Used to differ normal attributes
                // from anyAttributes.
                java.util.Vector handledAttributes = new java.util.Vector();




                    reader.next();


                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://soap.qrbox.intesigroup.com","cache").equals(reader.getName())){

                                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                    if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                        throw new org.apache.axis2.databinding.ADBException("The element: "+"cache" +"  cannot be null");
                                    }


                                    String content = reader.getElementText();

                                              object.setCache(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));

                                        reader.next();

                              }  // End of if for expected property start element

                                    else {

                                               object.setCache(Integer.MIN_VALUE);

                                    }


                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://soap.qrbox.intesigroup.com","key").equals(reader.getName())){

                                       nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                       if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                                    String content = reader.getElementText();

                                              object.setKey(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                                       } else {


                                           reader.getElementText(); // throw away text nodes if any.
                                       }

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


        public static class HandlerQRBException
        implements org.apache.axis2.databinding.ADBBean{

                public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName(
                "http://soap.qrbox.intesigroup.com",
                "handlerQRBException",
                "ns5");



                        /**
                        * field for QRBException
                        */


                                    protected QRBException localQRBException ;

                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localQRBExceptionTracker = false ;

                           public boolean isQRBExceptionSpecified(){
                               return localQRBExceptionTracker;
                           }



                           /**
                           * Auto generated getter method
                           * @return QRBException
                           */
                           public  QRBException getQRBException(){
                               return localQRBException;
                           }



                            /**
                               * Auto generated setter method
                               * @param param QRBException
                               */
                               public void setQRBException(QRBException param){
                            localQRBExceptionTracker = true;

                                            this.localQRBException=param;


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




                String prefix = null;
                String namespace = null;


                    prefix = parentQName.getPrefix();
                    namespace = parentQName.getNamespaceURI();
                    writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

                  if (serializeType){


                   String namespacePrefix = registerPrefix(xmlWriter,"http://soap.qrbox.intesigroup.com");
                   if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                       writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                           namespacePrefix+":handlerQRBException",
                           xmlWriter);
                   } else {
                       writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                           "handlerQRBException",
                           xmlWriter);
                   }


                   }
                if (localQRBExceptionTracker){
                                    if (localQRBException==null){

                                        writeStartElement(null, "http://soap.qrbox.intesigroup.com", "QRBException", xmlWriter);

                                       // write the nil attribute
                                      writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                      xmlWriter.writeEndElement();
                                    }else{
                                     localQRBException.serialize(new javax.xml.namespace.QName("http://soap.qrbox.intesigroup.com","QRBException"),
                                        xmlWriter);
                                    }
                                }
                    xmlWriter.writeEndElement();


        }

        private static String generatePrefix(String namespace) {
            if(namespace.equals("http://soap.qrbox.intesigroup.com")){
                return "ns5";
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

                 if (localQRBExceptionTracker){
                            elementList.add(new javax.xml.namespace.QName("http://soap.qrbox.intesigroup.com",
                                                                      "QRBException"));


                                    elementList.add(localQRBException==null?null:
                                    localQRBException);
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
        public static HandlerQRBException parse(javax.xml.stream.XMLStreamReader reader) throws Exception{
            HandlerQRBException object =
                new HandlerQRBException();

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

                            if (!"handlerQRBException".equals(type)){
                                //find namespace for the prefix
                                String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (HandlerQRBException) ExtensionMapper.getTypeObject(
                                        nsUri, type, reader);
                              }


                  }


                }




                // Note all attributes that were handled. Used to differ normal attributes
                // from anyAttributes.
                java.util.Vector handledAttributes = new java.util.Vector();




                    reader.next();


                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://soap.qrbox.intesigroup.com","QRBException").equals(reader.getName())){

                                      nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                      if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                          object.setQRBException(null);
                                          reader.next();

                                            reader.next();

                                      }else{

                                                object.setQRBException(QRBException.Factory.parse(reader));

                                        reader.next();
                                    }
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


        public static class GetVersionResponse
        implements org.apache.axis2.databinding.ADBBean{

                public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName(
                "http://soap.qrbox.intesigroup.com",
                "getVersionResponse",
                "ns5");



                        /**
                        * field for _return
                        */


                                    protected String local_return ;

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
                           * @return java.lang.String
                           */
                           public  String get_return(){
                               return local_return;
                           }



                            /**
                               * Auto generated setter method
                               * @param param _return
                               */
                               public void set_return(String param){
                            local_returnTracker = true;

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




                String prefix = null;
                String namespace = null;


                    prefix = parentQName.getPrefix();
                    namespace = parentQName.getNamespaceURI();
                    writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

                  if (serializeType){


                   String namespacePrefix = registerPrefix(xmlWriter,"http://soap.qrbox.intesigroup.com");
                   if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                       writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                           namespacePrefix+":getVersionResponse",
                           xmlWriter);
                   } else {
                       writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                           "getVersionResponse",
                           xmlWriter);
                   }


                   }
                if (local_returnTracker){
                                    namespace = "http://soap.qrbox.intesigroup.com";
                                    writeStartElement(null, namespace, "return", xmlWriter);


                                          if (local_return==null){
                                              // write the nil attribute

                                                     writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                                          }else{


                                                   xmlWriter.writeCharacters(local_return);

                                          }

                                   xmlWriter.writeEndElement();
                             }
                    xmlWriter.writeEndElement();


        }

        private static String generatePrefix(String namespace) {
            if(namespace.equals("http://soap.qrbox.intesigroup.com")){
                return "ns5";
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
                                      elementList.add(new javax.xml.namespace.QName("http://soap.qrbox.intesigroup.com",
                                                                      "return"));

                                         elementList.add(local_return==null?null:
                                         org.apache.axis2.databinding.utils.ConverterUtil.convertToString(local_return));
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
        public static GetVersionResponse parse(javax.xml.stream.XMLStreamReader reader) throws Exception{
            GetVersionResponse object =
                new GetVersionResponse();

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

                            if (!"getVersionResponse".equals(type)){
                                //find namespace for the prefix
                                String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (GetVersionResponse) ExtensionMapper.getTypeObject(
                                        nsUri, type, reader);
                              }


                  }


                }




                // Note all attributes that were handled. Used to differ normal attributes
                // from anyAttributes.
                java.util.Vector handledAttributes = new java.util.Vector();




                    reader.next();


                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://soap.qrbox.intesigroup.com","return").equals(reader.getName())){

                                       nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                       if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                                    String content = reader.getElementText();

                                              object.set_return(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                                       } else {


                                           reader.getElementText(); // throw away text nodes if any.
                                       }

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


        public static class AddStampAndSignResponseE
        implements org.apache.axis2.databinding.ADBBean{

                public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName(
                "http://soap.qrbox.intesigroup.com",
                "AddStampAndSignResponse",
                "ns5");



                        /**
                        * field for _return
                        */


                                    protected AddStampAndSignResponse local_return ;

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
                           * @return AddStampAndSignResponse
                           */
                           public  AddStampAndSignResponse get_return(){
                               return local_return;
                           }



                            /**
                               * Auto generated setter method
                               * @param param _return
                               */
                               public void set_return(AddStampAndSignResponse param){
                            local_returnTracker = true;

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




                String prefix = null;
                String namespace = null;


                    prefix = parentQName.getPrefix();
                    namespace = parentQName.getNamespaceURI();
                    writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

                  if (serializeType){


                   String namespacePrefix = registerPrefix(xmlWriter,"http://soap.qrbox.intesigroup.com");
                   if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                       writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                           namespacePrefix+":AddStampAndSignResponse",
                           xmlWriter);
                   } else {
                       writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                           "AddStampAndSignResponse",
                           xmlWriter);
                   }


                   }
                if (local_returnTracker){
                                    if (local_return==null){

                                        writeStartElement(null, "http://soap.qrbox.intesigroup.com", "return", xmlWriter);

                                       // write the nil attribute
                                      writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                      xmlWriter.writeEndElement();
                                    }else{
                                     local_return.serialize(new javax.xml.namespace.QName("http://soap.qrbox.intesigroup.com","return"),
                                        xmlWriter);
                                    }
                                }
                    xmlWriter.writeEndElement();


        }

        private static String generatePrefix(String namespace) {
            if(namespace.equals("http://soap.qrbox.intesigroup.com")){
                return "ns5";
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
                            elementList.add(new javax.xml.namespace.QName("http://soap.qrbox.intesigroup.com",
                                                                      "return"));


                                    elementList.add(local_return==null?null:
                                    local_return);
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
        public static AddStampAndSignResponseE parse(javax.xml.stream.XMLStreamReader reader) throws Exception{
            AddStampAndSignResponseE object =
                new AddStampAndSignResponseE();

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

                            if (!"AddStampAndSignResponse".equals(type)){
                                //find namespace for the prefix
                                String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (AddStampAndSignResponseE) ExtensionMapper.getTypeObject(
                                        nsUri, type, reader);
                              }


                  }


                }




                // Note all attributes that were handled. Used to differ normal attributes
                // from anyAttributes.
                java.util.Vector handledAttributes = new java.util.Vector();




                    reader.next();


                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://soap.qrbox.intesigroup.com","return").equals(reader.getName())){

                                      nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                      if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                          object.set_return(null);
                                          reader.next();

                                            reader.next();

                                      }else{

                                                object.set_return(AddStampAndSignResponse.Factory.parse(reader));

                                        reader.next();
                                    }
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


        public static class AddStampAndSignRequest
        implements org.apache.axis2.databinding.ADBBean{
        /* This type was generated from the piece of schema that had
                name = AddStampAndSignRequest
                Namespace URI = http://request.soap.qrbox.intesigroup.com/xsd
                Namespace Prefix = ns3
                */


                        /**
                        * field for Action
                        */


                                    protected int localAction ;

                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localActionTracker = false ;

                           public boolean isActionSpecified(){
                               return localActionTracker;
                           }



                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getAction(){
                               return localAction;
                           }



                            /**
                               * Auto generated setter method
                               * @param param Action
                               */
                               public void setAction(int param){
                            localActionTracker = true;

                                            this.localAction=param;


                               }


                        /**
                        * field for ContentID
                        */


                                    protected String localContentID ;

                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localContentIDTracker = false ;

                           public boolean isContentIDSpecified(){
                               return localContentIDTracker;
                           }



                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  String getContentID(){
                               return localContentID;
                           }



                            /**
                               * Auto generated setter method
                               * @param param ContentID
                               */
                               public void setContentID(String param){
                            localContentIDTracker = true;

                                            this.localContentID=param;


                               }


                        /**
                        * field for CorrectionLevel
                        */


                                    protected String localCorrectionLevel ;

                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localCorrectionLevelTracker = false ;

                           public boolean isCorrectionLevelSpecified(){
                               return localCorrectionLevelTracker;
                           }



                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  String getCorrectionLevel(){
                               return localCorrectionLevel;
                           }



                            /**
                               * Auto generated setter method
                               * @param param CorrectionLevel
                               */
                               public void setCorrectionLevel(String param){
                            localCorrectionLevelTracker = true;

                                            this.localCorrectionLevel=param;


                               }


                        /**
                        * field for Document
                        */


                                    protected javax.activation.DataHandler localDocument ;

                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localDocumentTracker = false ;

                           public boolean isDocumentSpecified(){
                               return localDocumentTracker;
                           }



                           /**
                           * Auto generated getter method
                           * @return javax.activation.DataHandler
                           */
                           public  javax.activation.DataHandler getDocument(){
                               return localDocument;
                           }



                            /**
                               * Auto generated setter method
                               * @param param Document
                               */
                               public void setDocument(javax.activation.DataHandler param){
                            localDocumentTracker = true;

                                            this.localDocument=param;


                               }


                        /**
                        * field for ExDate
                        */


                                    protected String localExDate ;

                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localExDateTracker = false ;

                           public boolean isExDateSpecified(){
                               return localExDateTracker;
                           }



                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  String getExDate(){
                               return localExDate;
                           }



                            /**
                               * Auto generated setter method
                               * @param param ExDate
                               */
                               public void setExDate(String param){
                            localExDateTracker = true;

                                            this.localExDate=param;


                               }


                        /**
                        * field for IsShortnedUrl
                        */


                                    protected boolean localIsShortnedUrl ;

                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localIsShortnedUrlTracker = false ;

                           public boolean isIsShortnedUrlSpecified(){
                               return localIsShortnedUrlTracker;
                           }



                           /**
                           * Auto generated getter method
                           * @return boolean
                           */
                           public  boolean getIsShortnedUrl(){
                               return localIsShortnedUrl;
                           }



                            /**
                               * Auto generated setter method
                               * @param param IsShortnedUrl
                               */
                               public void setIsShortnedUrl(boolean param){
                            localIsShortnedUrlTracker = true;

                                            this.localIsShortnedUrl=param;


                               }


                        /**
                        * field for Locale
                        */


                                    protected String localLocale ;

                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localLocaleTracker = false ;

                           public boolean isLocaleSpecified(){
                               return localLocaleTracker;
                           }



                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  String getLocale(){
                               return localLocale;
                           }



                            /**
                               * Auto generated setter method
                               * @param param Locale
                               */
                               public void setLocale(String param){
                            localLocaleTracker = true;

                                            this.localLocale=param;


                               }


                        /**
                        * field for Metadata
                        * This was an Array!
                        */


                                    protected KeyValueData[] localMetadata ;

                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localMetadataTracker = false ;

                           public boolean isMetadataSpecified(){
                               return localMetadataTracker;
                           }



                           /**
                           * Auto generated getter method
                           * @return KeyValueData[]
                           */
                           public  KeyValueData[] getMetadata(){
                               return localMetadata;
                           }






                              /**
                               * validate the array for Metadata
                               */
                              protected void validateMetadata(KeyValueData[] param){

                              }


                             /**
                              * Auto generated setter method
                              * @param param Metadata
                              */
                              public void setMetadata(KeyValueData[] param){

                                   validateMetadata(param);

                               localMetadataTracker = true;

                                      this.localMetadata=param;
                              }



                             /**
                             * Auto generated add method for the array for convenience
                             * @param param KeyValueData
                             */
                             public void addMetadata(KeyValueData param){
                                   if (localMetadata == null){
                                   localMetadata = new KeyValueData[]{};
                                   }


                                 //update the setting tracker
                                localMetadataTracker = true;


                               java.util.List list =
                            org.apache.axis2.databinding.utils.ConverterUtil.toList(localMetadata);
                               list.add(param);
                               this.localMetadata =
                             (KeyValueData[])list.toArray(
                            new KeyValueData[list.size()]);

                             }


                        /**
                        * field for Otp
                        */


                                    protected String localOtp ;

                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localOtpTracker = false ;

                           public boolean isOtpSpecified(){
                               return localOtpTracker;
                           }



                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  String getOtp(){
                               return localOtp;
                           }



                            /**
                               * Auto generated setter method
                               * @param param Otp
                               */
                               public void setOtp(String param){
                            localOtpTracker = true;

                                            this.localOtp=param;


                               }


                        /**
                        * field for Pin
                        */


                                    protected String localPin ;

                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localPinTracker = false ;

                           public boolean isPinSpecified(){
                               return localPinTracker;
                           }



                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  String getPin(){
                               return localPin;
                           }



                            /**
                               * Auto generated setter method
                               * @param param Pin
                               */
                               public void setPin(String param){
                            localPinTracker = true;

                                            this.localPin=param;


                               }


                        /**
                        * field for PositionTag
                        */


                                    protected String localPositionTag ;

                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localPositionTagTracker = false ;

                           public boolean isPositionTagSpecified(){
                               return localPositionTagTracker;
                           }



                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  String getPositionTag(){
                               return localPositionTag;
                           }



                            /**
                               * Auto generated setter method
                               * @param param PositionTag
                               */
                               public void setPositionTag(String param){
                            localPositionTagTracker = true;

                                            this.localPositionTag=param;


                               }


                        /**
                        * field for PositionX
                        */


                                    protected int localPositionX ;

                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localPositionXTracker = false ;

                           public boolean isPositionXSpecified(){
                               return localPositionXTracker;
                           }



                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getPositionX(){
                               return localPositionX;
                           }



                            /**
                               * Auto generated setter method
                               * @param param PositionX
                               */
                               public void setPositionX(int param){
                            localPositionXTracker = true;

                                            this.localPositionX=param;


                               }


                        /**
                        * field for PositionY
                        */


                                    protected int localPositionY ;

                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localPositionYTracker = false ;

                           public boolean isPositionYSpecified(){
                               return localPositionYTracker;
                           }



                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getPositionY(){
                               return localPositionY;
                           }



                            /**
                               * Auto generated setter method
                               * @param param PositionY
                               */
                               public void setPositionY(int param){
                            localPositionYTracker = true;

                                            this.localPositionY=param;


                               }


                        /**
                        * field for QrSize
                        */


                                    protected int localQrSize ;

                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localQrSizeTracker = false ;

                           public boolean isQrSizeSpecified(){
                               return localQrSizeTracker;
                           }



                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getQrSize(){
                               return localQrSize;
                           }



                            /**
                               * Auto generated setter method
                               * @param param QrSize
                               */
                               public void setQrSize(int param){
                            localQrSizeTracker = true;

                                            this.localQrSize=param;


                               }


                        /**
                        * field for Signer
                        */


                                    protected String localSigner ;

                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localSignerTracker = false ;

                           public boolean isSignerSpecified(){
                               return localSignerTracker;
                           }



                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  String getSigner(){
                               return localSigner;
                           }



                            /**
                               * Auto generated setter method
                               * @param param Signer
                               */
                               public void setSigner(String param){
                            localSignerTracker = true;

                                            this.localSigner=param;


                               }


                        /**
                        * field for StampPage
                        */


                                    protected int localStampPage ;

                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localStampPageTracker = false ;

                           public boolean isStampPageSpecified(){
                               return localStampPageTracker;
                           }



                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getStampPage(){
                               return localStampPage;
                           }



                            /**
                               * Auto generated setter method
                               * @param param StampPage
                               */
                               public void setStampPage(int param){
                            localStampPageTracker = true;

                                            this.localStampPage=param;


                               }


                        /**
                        * field for TemplateName
                        */


                                    protected String localTemplateName ;

                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localTemplateNameTracker = false ;

                           public boolean isTemplateNameSpecified(){
                               return localTemplateNameTracker;
                           }



                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  String getTemplateName(){
                               return localTemplateName;
                           }



                            /**
                               * Auto generated setter method
                               * @param param TemplateName
                               */
                               public void setTemplateName(String param){
                            localTemplateNameTracker = true;

                                            this.localTemplateName=param;


                               }


                        /**
                        * field for Title
                        */


                                    protected String localTitle ;

                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localTitleTracker = false ;

                           public boolean isTitleSpecified(){
                               return localTitleTracker;
                           }



                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  String getTitle(){
                               return localTitle;
                           }



                            /**
                               * Auto generated setter method
                               * @param param Title
                               */
                               public void setTitle(String param){
                            localTitleTracker = true;

                                            this.localTitle=param;


                               }


                        /**
                        * field for TypeDoc
                        */


                                    protected String localTypeDoc ;

                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localTypeDocTracker = false ;

                           public boolean isTypeDocSpecified(){
                               return localTypeDocTracker;
                           }



                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  String getTypeDoc(){
                               return localTypeDoc;
                           }



                            /**
                               * Auto generated setter method
                               * @param param TypeDoc
                               */
                               public void setTypeDoc(String param){
                            localTypeDocTracker = true;

                                            this.localTypeDoc=param;


                               }


                        /**
                        * field for Url
                        */


                                    protected String localUrl ;

                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localUrlTracker = false ;

                           public boolean isUrlSpecified(){
                               return localUrlTracker;
                           }



                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  String getUrl(){
                               return localUrl;
                           }



                            /**
                               * Auto generated setter method
                               * @param param Url
                               */
                               public void setUrl(String param){
                            localUrlTracker = true;

                                            this.localUrl=param;


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


                   String namespacePrefix = registerPrefix(xmlWriter,"http://request.soap.qrbox.intesigroup.com/xsd");
                   if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                       writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                           namespacePrefix+":AddStampAndSignRequest",
                           xmlWriter);
                   } else {
                       writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                           "AddStampAndSignRequest",
                           xmlWriter);
                   }


                   }
                if (localActionTracker){
                                    namespace = "http://request.soap.qrbox.intesigroup.com/xsd";
                                    writeStartElement(null, namespace, "action", xmlWriter);

                                               if (localAction== Integer.MIN_VALUE) {

                                                         writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localAction));
                                               }

                                   xmlWriter.writeEndElement();
                             } if (localContentIDTracker){
                                    namespace = "http://request.soap.qrbox.intesigroup.com/xsd";
                                    writeStartElement(null, namespace, "contentID", xmlWriter);


                                          if (localContentID==null){
                                              // write the nil attribute

                                                     writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                                          }else{


                                                   xmlWriter.writeCharacters(localContentID);

                                          }

                                   xmlWriter.writeEndElement();
                             } if (localCorrectionLevelTracker){
                                    namespace = "http://request.soap.qrbox.intesigroup.com/xsd";
                                    writeStartElement(null, namespace, "correctionLevel", xmlWriter);


                                          if (localCorrectionLevel==null){
                                              // write the nil attribute

                                                     writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                                          }else{


                                                   xmlWriter.writeCharacters(localCorrectionLevel);

                                          }

                                   xmlWriter.writeEndElement();
                             } if (localDocumentTracker){
                                    namespace = "http://request.soap.qrbox.intesigroup.com/xsd";
                                    writeStartElement(null, namespace, "document", xmlWriter);


                                    if (localDocument!=null)  {
                                       try {
                                           org.apache.axiom.util.stax.XMLStreamWriterUtils.writeDataHandler(xmlWriter, localDocument, null, true);
                                       } catch (java.io.IOException ex) {
                                           throw new javax.xml.stream.XMLStreamException("Unable to read data handler for document", ex);
                                       }
                                    } else {

                                             writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                                    }

                                   xmlWriter.writeEndElement();
                             } if (localExDateTracker){
                                    namespace = "http://request.soap.qrbox.intesigroup.com/xsd";
                                    writeStartElement(null, namespace, "exDate", xmlWriter);


                                          if (localExDate==null){
                                              // write the nil attribute

                                                     writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                                          }else{


                                                   xmlWriter.writeCharacters(localExDate);

                                          }

                                   xmlWriter.writeEndElement();
                             } if (localIsShortnedUrlTracker){
                                    namespace = "http://request.soap.qrbox.intesigroup.com/xsd";
                                    writeStartElement(null, namespace, "isShortnedUrl", xmlWriter);

                                               if (false) {

                                                         writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localIsShortnedUrl));
                                               }

                                   xmlWriter.writeEndElement();
                             } if (localLocaleTracker){
                                    namespace = "http://request.soap.qrbox.intesigroup.com/xsd";
                                    writeStartElement(null, namespace, "locale", xmlWriter);


                                          if (localLocale==null){
                                              // write the nil attribute

                                                     writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                                          }else{


                                                   xmlWriter.writeCharacters(localLocale);

                                          }

                                   xmlWriter.writeEndElement();
                             } if (localMetadataTracker){
                                       if (localMetadata!=null){
                                            for (int i = 0;i < localMetadata.length;i++){
                                                if (localMetadata[i] != null){
                                                 localMetadata[i].serialize(new javax.xml.namespace.QName("http://request.soap.qrbox.intesigroup.com/xsd","metadata"),
                                                           xmlWriter);
                                                } else {

                                                            writeStartElement(null, "http://request.soap.qrbox.intesigroup.com/xsd", "metadata", xmlWriter);

                                                           // write the nil attribute
                                                           writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                                           xmlWriter.writeEndElement();

                                                }

                                            }
                                     } else {

                                                writeStartElement(null, "http://request.soap.qrbox.intesigroup.com/xsd", "metadata", xmlWriter);

                                               // write the nil attribute
                                               writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                                               xmlWriter.writeEndElement();

                                    }
                                 } if (localOtpTracker){
                                    namespace = "http://request.soap.qrbox.intesigroup.com/xsd";
                                    writeStartElement(null, namespace, "otp", xmlWriter);


                                          if (localOtp==null){
                                              // write the nil attribute

                                                     writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                                          }else{


                                                   xmlWriter.writeCharacters(localOtp);

                                          }

                                   xmlWriter.writeEndElement();
                             } if (localPinTracker){
                                    namespace = "http://request.soap.qrbox.intesigroup.com/xsd";
                                    writeStartElement(null, namespace, "pin", xmlWriter);


                                          if (localPin==null){
                                              // write the nil attribute

                                                     writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                                          }else{


                                                   xmlWriter.writeCharacters(localPin);

                                          }

                                   xmlWriter.writeEndElement();
                             } if (localPositionTagTracker){
                                    namespace = "http://request.soap.qrbox.intesigroup.com/xsd";
                                    writeStartElement(null, namespace, "positionTag", xmlWriter);


                                          if (localPositionTag==null){
                                              // write the nil attribute

                                                     writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                                          }else{


                                                   xmlWriter.writeCharacters(localPositionTag);

                                          }

                                   xmlWriter.writeEndElement();
                             } if (localPositionXTracker){
                                    namespace = "http://request.soap.qrbox.intesigroup.com/xsd";
                                    writeStartElement(null, namespace, "positionX", xmlWriter);

                                               if (localPositionX== Integer.MIN_VALUE) {

                                                         writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localPositionX));
                                               }

                                   xmlWriter.writeEndElement();
                             } if (localPositionYTracker){
                                    namespace = "http://request.soap.qrbox.intesigroup.com/xsd";
                                    writeStartElement(null, namespace, "positionY", xmlWriter);

                                               if (localPositionY== Integer.MIN_VALUE) {

                                                         writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localPositionY));
                                               }

                                   xmlWriter.writeEndElement();
                             } if (localQrSizeTracker){
                                    namespace = "http://request.soap.qrbox.intesigroup.com/xsd";
                                    writeStartElement(null, namespace, "qrSize", xmlWriter);

                                               if (localQrSize== Integer.MIN_VALUE) {

                                                         writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localQrSize));
                                               }

                                   xmlWriter.writeEndElement();
                             } if (localSignerTracker){
                                    namespace = "http://request.soap.qrbox.intesigroup.com/xsd";
                                    writeStartElement(null, namespace, "signer", xmlWriter);


                                          if (localSigner==null){
                                              // write the nil attribute

                                                     writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                                          }else{


                                                   xmlWriter.writeCharacters(localSigner);

                                          }

                                   xmlWriter.writeEndElement();
                             } if (localStampPageTracker){
                                    namespace = "http://request.soap.qrbox.intesigroup.com/xsd";
                                    writeStartElement(null, namespace, "stampPage", xmlWriter);

                                               if (localStampPage== Integer.MIN_VALUE) {

                                                         writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localStampPage));
                                               }

                                   xmlWriter.writeEndElement();
                             } if (localTemplateNameTracker){
                                    namespace = "http://request.soap.qrbox.intesigroup.com/xsd";
                                    writeStartElement(null, namespace, "templateName", xmlWriter);


                                          if (localTemplateName==null){
                                              // write the nil attribute

                                                     writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                                          }else{


                                                   xmlWriter.writeCharacters(localTemplateName);

                                          }

                                   xmlWriter.writeEndElement();
                             } if (localTitleTracker){
                                    namespace = "http://request.soap.qrbox.intesigroup.com/xsd";
                                    writeStartElement(null, namespace, "title", xmlWriter);


                                          if (localTitle==null){
                                              // write the nil attribute

                                                     writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                                          }else{


                                                   xmlWriter.writeCharacters(localTitle);

                                          }

                                   xmlWriter.writeEndElement();
                             } if (localTypeDocTracker){
                                    namespace = "http://request.soap.qrbox.intesigroup.com/xsd";
                                    writeStartElement(null, namespace, "typeDoc", xmlWriter);


                                          if (localTypeDoc==null){
                                              // write the nil attribute

                                                     writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                                          }else{


                                                   xmlWriter.writeCharacters(localTypeDoc);

                                          }

                                   xmlWriter.writeEndElement();
                             } if (localUrlTracker){
                                    namespace = "http://request.soap.qrbox.intesigroup.com/xsd";
                                    writeStartElement(null, namespace, "url", xmlWriter);


                                          if (localUrl==null){
                                              // write the nil attribute

                                                     writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                                          }else{


                                                   xmlWriter.writeCharacters(localUrl);

                                          }

                                   xmlWriter.writeEndElement();
                             }
                    xmlWriter.writeEndElement();


        }

        private static String generatePrefix(String namespace) {
            if(namespace.equals("http://request.soap.qrbox.intesigroup.com/xsd")){
                return "ns3";
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

                 if (localActionTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://request.soap.qrbox.intesigroup.com/xsd",
                                                                      "action"));

                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localAction));
                            } if (localContentIDTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://request.soap.qrbox.intesigroup.com/xsd",
                                                                      "contentID"));

                                         elementList.add(localContentID==null?null:
                                         org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localContentID));
                                    } if (localCorrectionLevelTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://request.soap.qrbox.intesigroup.com/xsd",
                                                                      "correctionLevel"));

                                         elementList.add(localCorrectionLevel==null?null:
                                         org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localCorrectionLevel));
                                    } if (localDocumentTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://request.soap.qrbox.intesigroup.com/xsd",
                                        "document"));

                            elementList.add(localDocument);
                        } if (localExDateTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://request.soap.qrbox.intesigroup.com/xsd",
                                                                      "exDate"));

                                         elementList.add(localExDate==null?null:
                                         org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localExDate));
                                    } if (localIsShortnedUrlTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://request.soap.qrbox.intesigroup.com/xsd",
                                                                      "isShortnedUrl"));

                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localIsShortnedUrl));
                            } if (localLocaleTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://request.soap.qrbox.intesigroup.com/xsd",
                                                                      "locale"));

                                         elementList.add(localLocale==null?null:
                                         org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localLocale));
                                    } if (localMetadataTracker){
                             if (localMetadata!=null) {
                                 for (int i = 0;i < localMetadata.length;i++){

                                    if (localMetadata[i] != null){
                                         elementList.add(new javax.xml.namespace.QName("http://request.soap.qrbox.intesigroup.com/xsd",
                                                                          "metadata"));
                                         elementList.add(localMetadata[i]);
                                    } else {

                                                elementList.add(new javax.xml.namespace.QName("http://request.soap.qrbox.intesigroup.com/xsd",
                                                                          "metadata"));
                                                elementList.add(null);

                                    }

                                 }
                             } else {

                                        elementList.add(new javax.xml.namespace.QName("http://request.soap.qrbox.intesigroup.com/xsd",
                                                                          "metadata"));
                                        elementList.add(localMetadata);

                             }

                        } if (localOtpTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://request.soap.qrbox.intesigroup.com/xsd",
                                                                      "otp"));

                                         elementList.add(localOtp==null?null:
                                         org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localOtp));
                                    } if (localPinTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://request.soap.qrbox.intesigroup.com/xsd",
                                                                      "pin"));

                                         elementList.add(localPin==null?null:
                                         org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localPin));
                                    } if (localPositionTagTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://request.soap.qrbox.intesigroup.com/xsd",
                                                                      "positionTag"));

                                         elementList.add(localPositionTag==null?null:
                                         org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localPositionTag));
                                    } if (localPositionXTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://request.soap.qrbox.intesigroup.com/xsd",
                                                                      "positionX"));

                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localPositionX));
                            } if (localPositionYTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://request.soap.qrbox.intesigroup.com/xsd",
                                                                      "positionY"));

                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localPositionY));
                            } if (localQrSizeTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://request.soap.qrbox.intesigroup.com/xsd",
                                                                      "qrSize"));

                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localQrSize));
                            } if (localSignerTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://request.soap.qrbox.intesigroup.com/xsd",
                                                                      "signer"));

                                         elementList.add(localSigner==null?null:
                                         org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localSigner));
                                    } if (localStampPageTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://request.soap.qrbox.intesigroup.com/xsd",
                                                                      "stampPage"));

                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localStampPage));
                            } if (localTemplateNameTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://request.soap.qrbox.intesigroup.com/xsd",
                                                                      "templateName"));

                                         elementList.add(localTemplateName==null?null:
                                         org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localTemplateName));
                                    } if (localTitleTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://request.soap.qrbox.intesigroup.com/xsd",
                                                                      "title"));

                                         elementList.add(localTitle==null?null:
                                         org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localTitle));
                                    } if (localTypeDocTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://request.soap.qrbox.intesigroup.com/xsd",
                                                                      "typeDoc"));

                                         elementList.add(localTypeDoc==null?null:
                                         org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localTypeDoc));
                                    } if (localUrlTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://request.soap.qrbox.intesigroup.com/xsd",
                                                                      "url"));

                                         elementList.add(localUrl==null?null:
                                         org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localUrl));
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
        public static AddStampAndSignRequest parse(javax.xml.stream.XMLStreamReader reader) throws Exception{
            AddStampAndSignRequest object =
                new AddStampAndSignRequest();

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

                            if (!"AddStampAndSignRequest".equals(type)){
                                //find namespace for the prefix
                                String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (AddStampAndSignRequest) ExtensionMapper.getTypeObject(
                                        nsUri, type, reader);
                              }


                  }


                }




                // Note all attributes that were handled. Used to differ normal attributes
                // from anyAttributes.
                java.util.Vector handledAttributes = new java.util.Vector();




                    reader.next();

                        java.util.ArrayList list8 = new java.util.ArrayList();


                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://request.soap.qrbox.intesigroup.com/xsd","action").equals(reader.getName())){

                                       nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                       if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                                    String content = reader.getElementText();

                                              object.setAction(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));

                                       } else {


                                                   object.setAction(Integer.MIN_VALUE);

                                           reader.getElementText(); // throw away text nodes if any.
                                       }

                                        reader.next();

                              }  // End of if for expected property start element

                                    else {

                                               object.setAction(Integer.MIN_VALUE);

                                    }


                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://request.soap.qrbox.intesigroup.com/xsd","contentID").equals(reader.getName())){

                                       nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                       if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                                    String content = reader.getElementText();

                                              object.setContentID(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                                       } else {


                                           reader.getElementText(); // throw away text nodes if any.
                                       }

                                        reader.next();

                              }  // End of if for expected property start element

                                    else {

                                    }


                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://request.soap.qrbox.intesigroup.com/xsd","correctionLevel").equals(reader.getName())){

                                       nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                       if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                                    String content = reader.getElementText();

                                              object.setCorrectionLevel(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                                       } else {


                                           reader.getElementText(); // throw away text nodes if any.
                                       }

                                        reader.next();

                              }  // End of if for expected property start element

                                    else {

                                    }


                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://request.soap.qrbox.intesigroup.com/xsd","document").equals(reader.getName())){

                                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                        if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                             object.setDocument(null);
                                             reader.next();
                                        } else {

                                            object.setDocument(org.apache.axiom.util.stax.XMLStreamReaderUtils.getDataHandlerFromElement(reader));

                                        }

                                        reader.next();

                              }  // End of if for expected property start element

                                    else {

                                    }


                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://request.soap.qrbox.intesigroup.com/xsd","exDate").equals(reader.getName())){

                                       nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                       if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                                    String content = reader.getElementText();

                                              object.setExDate(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                                       } else {


                                           reader.getElementText(); // throw away text nodes if any.
                                       }

                                        reader.next();

                              }  // End of if for expected property start element

                                    else {

                                    }


                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://request.soap.qrbox.intesigroup.com/xsd","isShortnedUrl").equals(reader.getName())){

                                       nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                       if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                                    String content = reader.getElementText();

                                              object.setIsShortnedUrl(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));

                                       } else {


                                           reader.getElementText(); // throw away text nodes if any.
                                       }

                                        reader.next();

                              }  // End of if for expected property start element

                                    else {

                                    }


                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://request.soap.qrbox.intesigroup.com/xsd","locale").equals(reader.getName())){

                                       nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                       if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                                    String content = reader.getElementText();

                                              object.setLocale(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                                       } else {


                                           reader.getElementText(); // throw away text nodes if any.
                                       }

                                        reader.next();

                              }  // End of if for expected property start element

                                    else {

                                    }


                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://request.soap.qrbox.intesigroup.com/xsd","metadata").equals(reader.getName())){



                                    // Process the array and step past its final element's end.

                                                          nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                                          if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                                              list8.add(null);
                                                              reader.next();
                                                          } else {
                                                        list8.add(KeyValueData.Factory.parse(reader));
                                                                }
                                                        //loop until we find a start element that is not part of this array
                                                        boolean loopDone8 = false;
                                                        while(!loopDone8){
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
                                                                loopDone8 = true;
                                                            } else {
                                                                if (new javax.xml.namespace.QName("http://request.soap.qrbox.intesigroup.com/xsd","metadata").equals(reader.getName())){

                                                                      nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                                                      if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                                                          list8.add(null);
                                                                          reader.next();
                                                                      } else {
                                                                    list8.add(KeyValueData.Factory.parse(reader));
                                                                        }
                                                                }else{
                                                                    loopDone8 = true;
                                                                }
                                                            }
                                                        }
                                                        // call the converter utility  to convert and set the array

                                                        object.setMetadata((KeyValueData[])
                                                            org.apache.axis2.databinding.utils.ConverterUtil.convertToArray(
                                                                KeyValueData.class,
                                                                list8));

                              }  // End of if for expected property start element

                                    else {

                                    }


                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://request.soap.qrbox.intesigroup.com/xsd","otp").equals(reader.getName())){

                                       nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                       if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                                    String content = reader.getElementText();

                                              object.setOtp(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                                       } else {


                                           reader.getElementText(); // throw away text nodes if any.
                                       }

                                        reader.next();

                              }  // End of if for expected property start element

                                    else {

                                    }


                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://request.soap.qrbox.intesigroup.com/xsd","pin").equals(reader.getName())){

                                       nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                       if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                                    String content = reader.getElementText();

                                              object.setPin(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                                       } else {


                                           reader.getElementText(); // throw away text nodes if any.
                                       }

                                        reader.next();

                              }  // End of if for expected property start element

                                    else {

                                    }


                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://request.soap.qrbox.intesigroup.com/xsd","positionTag").equals(reader.getName())){

                                       nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                       if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                                    String content = reader.getElementText();

                                              object.setPositionTag(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                                       } else {


                                           reader.getElementText(); // throw away text nodes if any.
                                       }

                                        reader.next();

                              }  // End of if for expected property start element

                                    else {

                                    }


                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://request.soap.qrbox.intesigroup.com/xsd","positionX").equals(reader.getName())){

                                       nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                       if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                                    String content = reader.getElementText();

                                              object.setPositionX(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));

                                       } else {


                                                   object.setPositionX(Integer.MIN_VALUE);

                                           reader.getElementText(); // throw away text nodes if any.
                                       }

                                        reader.next();

                              }  // End of if for expected property start element

                                    else {

                                               object.setPositionX(Integer.MIN_VALUE);

                                    }


                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://request.soap.qrbox.intesigroup.com/xsd","positionY").equals(reader.getName())){

                                       nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                       if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                                    String content = reader.getElementText();

                                              object.setPositionY(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));

                                       } else {


                                                   object.setPositionY(Integer.MIN_VALUE);

                                           reader.getElementText(); // throw away text nodes if any.
                                       }

                                        reader.next();

                              }  // End of if for expected property start element

                                    else {

                                               object.setPositionY(Integer.MIN_VALUE);

                                    }


                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://request.soap.qrbox.intesigroup.com/xsd","qrSize").equals(reader.getName())){

                                       nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                       if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                                    String content = reader.getElementText();

                                              object.setQrSize(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));

                                       } else {


                                                   object.setQrSize(Integer.MIN_VALUE);

                                           reader.getElementText(); // throw away text nodes if any.
                                       }

                                        reader.next();

                              }  // End of if for expected property start element

                                    else {

                                               object.setQrSize(Integer.MIN_VALUE);

                                    }


                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://request.soap.qrbox.intesigroup.com/xsd","signer").equals(reader.getName())){

                                       nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                       if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                                    String content = reader.getElementText();

                                              object.setSigner(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                                       } else {


                                           reader.getElementText(); // throw away text nodes if any.
                                       }

                                        reader.next();

                              }  // End of if for expected property start element

                                    else {

                                    }


                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://request.soap.qrbox.intesigroup.com/xsd","stampPage").equals(reader.getName())){

                                       nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                       if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                                    String content = reader.getElementText();

                                              object.setStampPage(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));

                                       } else {


                                                   object.setStampPage(Integer.MIN_VALUE);

                                           reader.getElementText(); // throw away text nodes if any.
                                       }

                                        reader.next();

                              }  // End of if for expected property start element

                                    else {

                                               object.setStampPage(Integer.MIN_VALUE);

                                    }


                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://request.soap.qrbox.intesigroup.com/xsd","templateName").equals(reader.getName())){

                                       nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                       if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                                    String content = reader.getElementText();

                                              object.setTemplateName(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                                       } else {


                                           reader.getElementText(); // throw away text nodes if any.
                                       }

                                        reader.next();

                              }  // End of if for expected property start element

                                    else {

                                    }


                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://request.soap.qrbox.intesigroup.com/xsd","title").equals(reader.getName())){

                                       nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                       if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                                    String content = reader.getElementText();

                                              object.setTitle(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                                       } else {


                                           reader.getElementText(); // throw away text nodes if any.
                                       }

                                        reader.next();

                              }  // End of if for expected property start element

                                    else {

                                    }


                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://request.soap.qrbox.intesigroup.com/xsd","typeDoc").equals(reader.getName())){

                                       nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                       if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                                    String content = reader.getElementText();

                                              object.setTypeDoc(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                                       } else {


                                           reader.getElementText(); // throw away text nodes if any.
                                       }

                                        reader.next();

                              }  // End of if for expected property start element

                                    else {

                                    }


                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://request.soap.qrbox.intesigroup.com/xsd","url").equals(reader.getName())){

                                       nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                       if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                                    String content = reader.getElementText();

                                              object.setUrl(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                                       } else {


                                           reader.getElementText(); // throw away text nodes if any.
                                       }

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


        public static class QRBException
        implements org.apache.axis2.databinding.ADBBean{
        /* This type was generated from the piece of schema that had
                name = QRBException
                Namespace URI = http://exception.core.qrbox.intesigroup.com/xsd
                Namespace Prefix = ns1
                */


                        /**
                        * field for ErrorCode
                        */


                                    protected int localErrorCode ;

                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localErrorCodeTracker = false ;

                           public boolean isErrorCodeSpecified(){
                               return localErrorCodeTracker;
                           }



                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getErrorCode(){
                               return localErrorCode;
                           }



                            /**
                               * Auto generated setter method
                               * @param param ErrorCode
                               */
                               public void setErrorCode(int param){

                                       // setting primitive attribute tracker to true
                                       localErrorCodeTracker =
                                       param != Integer.MIN_VALUE;

                                            this.localErrorCode=param;


                               }


                        /**
                        * field for ErrorMessage
                        */


                                    protected String localErrorMessage ;

                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localErrorMessageTracker = false ;

                           public boolean isErrorMessageSpecified(){
                               return localErrorMessageTracker;
                           }



                           /**
                           * Auto generated getter method
                           * @return java.lang.String
                           */
                           public  String getErrorMessage(){
                               return localErrorMessage;
                           }



                            /**
                               * Auto generated setter method
                               * @param param ErrorMessage
                               */
                               public void setErrorMessage(String param){
                            localErrorMessageTracker = true;

                                            this.localErrorMessage=param;


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


                   String namespacePrefix = registerPrefix(xmlWriter,"http://exception.core.qrbox.intesigroup.com/xsd");
                   if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                       writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                           namespacePrefix+":QRBException",
                           xmlWriter);
                   } else {
                       writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                           "QRBException",
                           xmlWriter);
                   }


                   }
                if (localErrorCodeTracker){
                                    namespace = "http://exception.core.qrbox.intesigroup.com/xsd";
                                    writeStartElement(null, namespace, "errorCode", xmlWriter);

                                               if (localErrorCode== Integer.MIN_VALUE) {

                                                         throw new org.apache.axis2.databinding.ADBException("errorCode cannot be null!!");

                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localErrorCode));
                                               }

                                   xmlWriter.writeEndElement();
                             } if (localErrorMessageTracker){
                                    namespace = "http://exception.core.qrbox.intesigroup.com/xsd";
                                    writeStartElement(null, namespace, "errorMessage", xmlWriter);


                                          if (localErrorMessage==null){
                                              // write the nil attribute

                                                     writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                                          }else{


                                                   xmlWriter.writeCharacters(localErrorMessage);

                                          }

                                   xmlWriter.writeEndElement();
                             }
                    xmlWriter.writeEndElement();


        }

        private static String generatePrefix(String namespace) {
            if(namespace.equals("http://exception.core.qrbox.intesigroup.com/xsd")){
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

                 if (localErrorCodeTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://exception.core.qrbox.intesigroup.com/xsd",
                                                                      "errorCode"));

                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localErrorCode));
                            } if (localErrorMessageTracker){
                                      elementList.add(new javax.xml.namespace.QName("http://exception.core.qrbox.intesigroup.com/xsd",
                                                                      "errorMessage"));

                                         elementList.add(localErrorMessage==null?null:
                                         org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localErrorMessage));
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
        public static QRBException parse(javax.xml.stream.XMLStreamReader reader) throws Exception{
            QRBException object =
                new QRBException();

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

                            if (!"QRBException".equals(type)){
                                //find namespace for the prefix
                                String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (QRBException) ExtensionMapper.getTypeObject(
                                        nsUri, type, reader);
                              }


                  }


                }




                // Note all attributes that were handled. Used to differ normal attributes
                // from anyAttributes.
                java.util.Vector handledAttributes = new java.util.Vector();




                    reader.next();


                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://exception.core.qrbox.intesigroup.com/xsd","errorCode").equals(reader.getName())){

                                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                    if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                        throw new org.apache.axis2.databinding.ADBException("The element: "+"errorCode" +"  cannot be null");
                                    }


                                    String content = reader.getElementText();

                                              object.setErrorCode(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));

                                        reader.next();

                              }  // End of if for expected property start element

                                    else {

                                               object.setErrorCode(Integer.MIN_VALUE);

                                    }


                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://exception.core.qrbox.intesigroup.com/xsd","errorMessage").equals(reader.getName())){

                                       nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                       if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                                    String content = reader.getElementText();

                                              object.setErrorMessage(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                                       } else {


                                           reader.getElementText(); // throw away text nodes if any.
                                       }

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


            private  org.apache.axiom.om.OMElement  toOM(it.kdm.docer.timbro.provider.intesi.stub.HandlerStub.GetPkBoxClientVersion param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {


                        try{
                             return param.getOMElement(it.kdm.docer.timbro.provider.intesi.stub.HandlerStub.GetPkBoxClientVersion.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }


            }

            private  org.apache.axiom.om.OMElement  toOM(it.kdm.docer.timbro.provider.intesi.stub.HandlerStub.GetPkBoxClientVersionResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {


                        try{
                             return param.getOMElement(it.kdm.docer.timbro.provider.intesi.stub.HandlerStub.GetPkBoxClientVersionResponse.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }


            }

            private  org.apache.axiom.om.OMElement  toOM(it.kdm.docer.timbro.provider.intesi.stub.HandlerStub.AddStampAndSign param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {


                        try{
                             return param.getOMElement(it.kdm.docer.timbro.provider.intesi.stub.HandlerStub.AddStampAndSign.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }


            }

            private  org.apache.axiom.om.OMElement  toOM(it.kdm.docer.timbro.provider.intesi.stub.HandlerStub.AddStampAndSignResponseE param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {


                        try{
                             return param.getOMElement(it.kdm.docer.timbro.provider.intesi.stub.HandlerStub.AddStampAndSignResponseE.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }


            }

            private  org.apache.axiom.om.OMElement  toOM(it.kdm.docer.timbro.provider.intesi.stub.HandlerStub.HandlerQRBException param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {


                        try{
                             return param.getOMElement(it.kdm.docer.timbro.provider.intesi.stub.HandlerStub.HandlerQRBException.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }


            }

            private  org.apache.axiom.om.OMElement  toOM(it.kdm.docer.timbro.provider.intesi.stub.HandlerStub.FlushCache param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {


                        try{
                             return param.getOMElement(it.kdm.docer.timbro.provider.intesi.stub.HandlerStub.FlushCache.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }


            }

            private  org.apache.axiom.om.OMElement  toOM(it.kdm.docer.timbro.provider.intesi.stub.HandlerStub.GetVersion param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {


                        try{
                             return param.getOMElement(it.kdm.docer.timbro.provider.intesi.stub.HandlerStub.GetVersion.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }


            }

            private  org.apache.axiom.om.OMElement  toOM(it.kdm.docer.timbro.provider.intesi.stub.HandlerStub.GetVersionResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {


                        try{
                             return param.getOMElement(it.kdm.docer.timbro.provider.intesi.stub.HandlerStub.GetVersionResponse.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }


            }


                                        private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, it.kdm.docer.timbro.provider.intesi.stub.HandlerStub.GetPkBoxClientVersion param, boolean optimizeContent, javax.xml.namespace.QName methodQName)
                                        throws org.apache.axis2.AxisFault{


                                                    try{

                                                            org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                                                            emptyEnvelope.getBody().addChild(param.getOMElement(it.kdm.docer.timbro.provider.intesi.stub.HandlerStub.GetPkBoxClientVersion.MY_QNAME,factory));
                                                            return emptyEnvelope;
                                                        } catch(org.apache.axis2.databinding.ADBException e){
                                                            throw org.apache.axis2.AxisFault.makeFault(e);
                                                        }


                                        }


                             /* methods to provide back word compatibility */



                                        private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, it.kdm.docer.timbro.provider.intesi.stub.HandlerStub.AddStampAndSign param, boolean optimizeContent, javax.xml.namespace.QName methodQName)
                                        throws org.apache.axis2.AxisFault{


                                                    try{

                                                            org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                                                            emptyEnvelope.getBody().addChild(param.getOMElement(it.kdm.docer.timbro.provider.intesi.stub.HandlerStub.AddStampAndSign.MY_QNAME,factory));
                                                            return emptyEnvelope;
                                                        } catch(org.apache.axis2.databinding.ADBException e){
                                                            throw org.apache.axis2.AxisFault.makeFault(e);
                                                        }


                                        }


                             /* methods to provide back word compatibility */



                                        private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, it.kdm.docer.timbro.provider.intesi.stub.HandlerStub.FlushCache param, boolean optimizeContent, javax.xml.namespace.QName methodQName)
                                        throws org.apache.axis2.AxisFault{


                                                    try{

                                                            org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                                                            emptyEnvelope.getBody().addChild(param.getOMElement(it.kdm.docer.timbro.provider.intesi.stub.HandlerStub.FlushCache.MY_QNAME,factory));
                                                            return emptyEnvelope;
                                                        } catch(org.apache.axis2.databinding.ADBException e){
                                                            throw org.apache.axis2.AxisFault.makeFault(e);
                                                        }


                                        }


                             /* methods to provide back word compatibility */



                                        private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, it.kdm.docer.timbro.provider.intesi.stub.HandlerStub.GetVersion param, boolean optimizeContent, javax.xml.namespace.QName methodQName)
                                        throws org.apache.axis2.AxisFault{


                                                    try{

                                                            org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                                                            emptyEnvelope.getBody().addChild(param.getOMElement(it.kdm.docer.timbro.provider.intesi.stub.HandlerStub.GetVersion.MY_QNAME,factory));
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

                if (it.kdm.docer.timbro.provider.intesi.stub.HandlerStub.GetPkBoxClientVersion.class.equals(type)){

                           return it.kdm.docer.timbro.provider.intesi.stub.HandlerStub.GetPkBoxClientVersion.Factory.parse(param.getXMLStreamReaderWithoutCaching());


                }

                if (it.kdm.docer.timbro.provider.intesi.stub.HandlerStub.GetPkBoxClientVersionResponse.class.equals(type)){

                           return it.kdm.docer.timbro.provider.intesi.stub.HandlerStub.GetPkBoxClientVersionResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());


                }

                if (it.kdm.docer.timbro.provider.intesi.stub.HandlerStub.AddStampAndSign.class.equals(type)){

                           return it.kdm.docer.timbro.provider.intesi.stub.HandlerStub.AddStampAndSign.Factory.parse(param.getXMLStreamReaderWithoutCaching());


                }

                if (it.kdm.docer.timbro.provider.intesi.stub.HandlerStub.AddStampAndSignResponseE.class.equals(type)){

                           return it.kdm.docer.timbro.provider.intesi.stub.HandlerStub.AddStampAndSignResponseE.Factory.parse(param.getXMLStreamReaderWithoutCaching());


                }

                if (it.kdm.docer.timbro.provider.intesi.stub.HandlerStub.HandlerQRBException.class.equals(type)){

                           return it.kdm.docer.timbro.provider.intesi.stub.HandlerStub.HandlerQRBException.Factory.parse(param.getXMLStreamReaderWithoutCaching());


                }

                if (it.kdm.docer.timbro.provider.intesi.stub.HandlerStub.FlushCache.class.equals(type)){

                           return it.kdm.docer.timbro.provider.intesi.stub.HandlerStub.FlushCache.Factory.parse(param.getXMLStreamReaderWithoutCaching());


                }

                if (it.kdm.docer.timbro.provider.intesi.stub.HandlerStub.HandlerQRBException.class.equals(type)){

                           return it.kdm.docer.timbro.provider.intesi.stub.HandlerStub.HandlerQRBException.Factory.parse(param.getXMLStreamReaderWithoutCaching());


                }

                if (it.kdm.docer.timbro.provider.intesi.stub.HandlerStub.GetVersion.class.equals(type)){

                           return it.kdm.docer.timbro.provider.intesi.stub.HandlerStub.GetVersion.Factory.parse(param.getXMLStreamReaderWithoutCaching());


                }

                if (it.kdm.docer.timbro.provider.intesi.stub.HandlerStub.GetVersionResponse.class.equals(type)){

                           return it.kdm.docer.timbro.provider.intesi.stub.HandlerStub.GetVersionResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());


                }

        } catch (Exception e) {
        throw org.apache.axis2.AxisFault.makeFault(e);
        }
           return null;
        }



    
   }
   