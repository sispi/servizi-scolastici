
/**
 * WSDemoneStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */
package it.kdm.docer.clients;

        

        /*
        *  WSDemoneStub java implementation
        */


public class WSDemoneStub extends org.apache.axis2.client.Stub
{
    protected org.apache.axis2.description.AxisOperation[] _operations;

    //hashmaps to keep the fault mapping
    private java.util.HashMap faultExceptionNameMap = new java.util.HashMap();
    private java.util.HashMap faultExceptionClassNameMap = new java.util.HashMap();
    private java.util.HashMap faultMessageMap = new java.util.HashMap();

    private static int counter = 0;

    private static synchronized java.lang.String getUniqueSuffix(){
        // reset the counter if it is greater than 99999
        if (counter > 99999){
            counter = 0;
        }
        counter = counter + 1;
        return java.lang.Long.toString(java.lang.System.currentTimeMillis()) + "_" + counter;
    }


    private void populateAxisService() throws org.apache.axis2.AxisFault {

        //creating the Service with a unique name
        _service = new org.apache.axis2.description.AxisService("WSDemone" + getUniqueSuffix());
        addAnonymousOperations();

        //creating the operations
        org.apache.axis2.description.AxisOperation __operation;

        _operations = new org.apache.axis2.description.AxisOperation[13];

        __operation = new org.apache.axis2.description.OutInAxisOperation();


        __operation.setName(new javax.xml.namespace.QName("http://webservice.demone.conservazione", "searchJob"));
        _service.addOperation(__operation);




        _operations[0]=__operation;


        __operation = new org.apache.axis2.description.OutInAxisOperation();


        __operation.setName(new javax.xml.namespace.QName("http://webservice.demone.conservazione", "getJobGson"));
        _service.addOperation(__operation);




        _operations[1]=__operation;


        __operation = new org.apache.axis2.description.OutInAxisOperation();


        __operation.setName(new javax.xml.namespace.QName("http://webservice.demone.conservazione", "searchJobEstesa"));
        _service.addOperation(__operation);




        _operations[2]=__operation;


        __operation = new org.apache.axis2.description.OutInAxisOperation();


        __operation.setName(new javax.xml.namespace.QName("http://webservice.demone.conservazione", "readLogsByIdJob"));
        _service.addOperation(__operation);




        _operations[3]=__operation;


        __operation = new org.apache.axis2.description.OutInAxisOperation();


        __operation.setName(new javax.xml.namespace.QName("http://webservice.demone.conservazione", "readLogGson_byIdJob"));
        _service.addOperation(__operation);




        _operations[4]=__operation;


        __operation = new org.apache.axis2.description.OutInAxisOperation();


        __operation.setName(new javax.xml.namespace.QName("http://webservice.demone.conservazione", "deleteJob_ByIdDoc"));
        _service.addOperation(__operation);




        _operations[5]=__operation;


        __operation = new org.apache.axis2.description.OutInAxisOperation();


        __operation.setName(new javax.xml.namespace.QName("http://webservice.demone.conservazione", "getJob"));
        _service.addOperation(__operation);




        _operations[6]=__operation;


        __operation = new org.apache.axis2.description.OutInAxisOperation();


        __operation.setName(new javax.xml.namespace.QName("http://webservice.demone.conservazione", "searchLogEstesa"));
        _service.addOperation(__operation);




        _operations[7]=__operation;


        __operation = new org.apache.axis2.description.OutInAxisOperation();


        __operation.setName(new javax.xml.namespace.QName("http://webservice.demone.conservazione", "getJobByDocId"));
        _service.addOperation(__operation);




        _operations[8]=__operation;


        __operation = new org.apache.axis2.description.OutInAxisOperation();


        __operation.setName(new javax.xml.namespace.QName("http://webservice.demone.conservazione", "deleteJob_byIdJob"));
        _service.addOperation(__operation);




        _operations[9]=__operation;


        __operation = new org.apache.axis2.description.OutInAxisOperation();


        __operation.setName(new javax.xml.namespace.QName("http://webservice.demone.conservazione", "updateJob"));
        _service.addOperation(__operation);




        _operations[10]=__operation;


        __operation = new org.apache.axis2.description.OutInAxisOperation();


        __operation.setName(new javax.xml.namespace.QName("http://webservice.demone.conservazione", "readLogsByIdDoc"));
        _service.addOperation(__operation);




        _operations[11]=__operation;


        __operation = new org.apache.axis2.description.OutInAxisOperation();


        __operation.setName(new javax.xml.namespace.QName("http://webservice.demone.conservazione", "addJob"));
        _service.addOperation(__operation);




        _operations[12]=__operation;


    }

    //populates the faults
    private void populateFaults(){

        faultExceptionNameMap.put(new org.apache.axis2.client.FaultMapKey(new javax.xml.namespace.QName("http://webservice.demone.conservazione","WSDemoneException"), "searchJob"),"it.kdm.docer.clients.WSDemoneExceptionException");
        faultExceptionClassNameMap.put(new org.apache.axis2.client.FaultMapKey(new javax.xml.namespace.QName("http://webservice.demone.conservazione","WSDemoneException"), "searchJob"),"it.kdm.docer.clients.WSDemoneExceptionException");
        faultMessageMap.put(new org.apache.axis2.client.FaultMapKey(new javax.xml.namespace.QName("http://webservice.demone.conservazione","WSDemoneException"), "searchJob"),"it.kdm.docer.clients.WSDemoneStub$WSDemoneException");

        faultExceptionNameMap.put(new org.apache.axis2.client.FaultMapKey(new javax.xml.namespace.QName("http://webservice.demone.conservazione","WSDemoneException"), "getJobGson"),"it.kdm.docer.clients.WSDemoneExceptionException");
        faultExceptionClassNameMap.put(new org.apache.axis2.client.FaultMapKey(new javax.xml.namespace.QName("http://webservice.demone.conservazione","WSDemoneException"), "getJobGson"),"it.kdm.docer.clients.WSDemoneExceptionException");
        faultMessageMap.put(new org.apache.axis2.client.FaultMapKey(new javax.xml.namespace.QName("http://webservice.demone.conservazione","WSDemoneException"), "getJobGson"),"it.kdm.docer.clients.WSDemoneStub$WSDemoneException");

        faultExceptionNameMap.put(new org.apache.axis2.client.FaultMapKey(new javax.xml.namespace.QName("http://webservice.demone.conservazione","WSDemoneException"), "readLogsByIdJob"),"it.kdm.docer.clients.WSDemoneExceptionException");
        faultExceptionClassNameMap.put(new org.apache.axis2.client.FaultMapKey(new javax.xml.namespace.QName("http://webservice.demone.conservazione","WSDemoneException"), "readLogsByIdJob"),"it.kdm.docer.clients.WSDemoneExceptionException");
        faultMessageMap.put(new org.apache.axis2.client.FaultMapKey(new javax.xml.namespace.QName("http://webservice.demone.conservazione","WSDemoneException"), "readLogsByIdJob"),"it.kdm.docer.clients.WSDemoneStub$WSDemoneException");

        faultExceptionNameMap.put(new org.apache.axis2.client.FaultMapKey(new javax.xml.namespace.QName("http://webservice.demone.conservazione","WSDemoneException"), "readLogGson_byIdJob"),"it.kdm.docer.clients.WSDemoneExceptionException");
        faultExceptionClassNameMap.put(new org.apache.axis2.client.FaultMapKey(new javax.xml.namespace.QName("http://webservice.demone.conservazione","WSDemoneException"), "readLogGson_byIdJob"),"it.kdm.docer.clients.WSDemoneExceptionException");
        faultMessageMap.put(new org.apache.axis2.client.FaultMapKey(new javax.xml.namespace.QName("http://webservice.demone.conservazione","WSDemoneException"), "readLogGson_byIdJob"),"it.kdm.docer.clients.WSDemoneStub$WSDemoneException");

        faultExceptionNameMap.put(new org.apache.axis2.client.FaultMapKey(new javax.xml.namespace.QName("http://webservice.demone.conservazione","WSDemoneException"), "deleteJob_ByIdDoc"),"it.kdm.docer.clients.WSDemoneExceptionException");
        faultExceptionClassNameMap.put(new org.apache.axis2.client.FaultMapKey(new javax.xml.namespace.QName("http://webservice.demone.conservazione","WSDemoneException"), "deleteJob_ByIdDoc"),"it.kdm.docer.clients.WSDemoneExceptionException");
        faultMessageMap.put(new org.apache.axis2.client.FaultMapKey(new javax.xml.namespace.QName("http://webservice.demone.conservazione","WSDemoneException"), "deleteJob_ByIdDoc"),"it.kdm.docer.clients.WSDemoneStub$WSDemoneException");

        faultExceptionNameMap.put(new org.apache.axis2.client.FaultMapKey(new javax.xml.namespace.QName("http://webservice.demone.conservazione","WSDemoneException"), "getJob"),"it.kdm.docer.clients.WSDemoneExceptionException");
        faultExceptionClassNameMap.put(new org.apache.axis2.client.FaultMapKey(new javax.xml.namespace.QName("http://webservice.demone.conservazione","WSDemoneException"), "getJob"),"it.kdm.docer.clients.WSDemoneExceptionException");
        faultMessageMap.put(new org.apache.axis2.client.FaultMapKey(new javax.xml.namespace.QName("http://webservice.demone.conservazione","WSDemoneException"), "getJob"),"it.kdm.docer.clients.WSDemoneStub$WSDemoneException");

        faultExceptionNameMap.put(new org.apache.axis2.client.FaultMapKey(new javax.xml.namespace.QName("http://webservice.demone.conservazione","WSDemoneException"), "searchLogEstesa"),"it.kdm.docer.clients.WSDemoneExceptionException");
        faultExceptionClassNameMap.put(new org.apache.axis2.client.FaultMapKey(new javax.xml.namespace.QName("http://webservice.demone.conservazione","WSDemoneException"), "searchLogEstesa"),"it.kdm.docer.clients.WSDemoneExceptionException");
        faultMessageMap.put(new org.apache.axis2.client.FaultMapKey(new javax.xml.namespace.QName("http://webservice.demone.conservazione","WSDemoneException"), "searchLogEstesa"),"it.kdm.docer.clients.WSDemoneStub$WSDemoneException");

        faultExceptionNameMap.put(new org.apache.axis2.client.FaultMapKey(new javax.xml.namespace.QName("http://webservice.demone.conservazione","WSDemoneException"), "getJobByDocId"),"it.kdm.docer.clients.WSDemoneExceptionException");
        faultExceptionClassNameMap.put(new org.apache.axis2.client.FaultMapKey(new javax.xml.namespace.QName("http://webservice.demone.conservazione","WSDemoneException"), "getJobByDocId"),"it.kdm.docer.clients.WSDemoneExceptionException");
        faultMessageMap.put(new org.apache.axis2.client.FaultMapKey(new javax.xml.namespace.QName("http://webservice.demone.conservazione","WSDemoneException"), "getJobByDocId"),"it.kdm.docer.clients.WSDemoneStub$WSDemoneException");

        faultExceptionNameMap.put(new org.apache.axis2.client.FaultMapKey(new javax.xml.namespace.QName("http://webservice.demone.conservazione","WSDemoneException"), "deleteJob_byIdJob"),"it.kdm.docer.clients.WSDemoneExceptionException");
        faultExceptionClassNameMap.put(new org.apache.axis2.client.FaultMapKey(new javax.xml.namespace.QName("http://webservice.demone.conservazione","WSDemoneException"), "deleteJob_byIdJob"),"it.kdm.docer.clients.WSDemoneExceptionException");
        faultMessageMap.put(new org.apache.axis2.client.FaultMapKey(new javax.xml.namespace.QName("http://webservice.demone.conservazione","WSDemoneException"), "deleteJob_byIdJob"),"it.kdm.docer.clients.WSDemoneStub$WSDemoneException");

        faultExceptionNameMap.put(new org.apache.axis2.client.FaultMapKey(new javax.xml.namespace.QName("http://webservice.demone.conservazione","WSDemoneException"), "updateJob"),"it.kdm.docer.clients.WSDemoneExceptionException");
        faultExceptionClassNameMap.put(new org.apache.axis2.client.FaultMapKey(new javax.xml.namespace.QName("http://webservice.demone.conservazione","WSDemoneException"), "updateJob"),"it.kdm.docer.clients.WSDemoneExceptionException");
        faultMessageMap.put(new org.apache.axis2.client.FaultMapKey(new javax.xml.namespace.QName("http://webservice.demone.conservazione","WSDemoneException"), "updateJob"),"it.kdm.docer.clients.WSDemoneStub$WSDemoneException");

        faultExceptionNameMap.put(new org.apache.axis2.client.FaultMapKey(new javax.xml.namespace.QName("http://webservice.demone.conservazione","WSDemoneException"), "readLogsByIdDoc"),"it.kdm.docer.clients.WSDemoneExceptionException");
        faultExceptionClassNameMap.put(new org.apache.axis2.client.FaultMapKey(new javax.xml.namespace.QName("http://webservice.demone.conservazione","WSDemoneException"), "readLogsByIdDoc"),"it.kdm.docer.clients.WSDemoneExceptionException");
        faultMessageMap.put(new org.apache.axis2.client.FaultMapKey(new javax.xml.namespace.QName("http://webservice.demone.conservazione","WSDemoneException"), "readLogsByIdDoc"),"it.kdm.docer.clients.WSDemoneStub$WSDemoneException");

        faultExceptionNameMap.put(new org.apache.axis2.client.FaultMapKey(new javax.xml.namespace.QName("http://webservice.demone.conservazione","WSDemoneException"), "addJob"),"it.kdm.docer.clients.WSDemoneExceptionException");
        faultExceptionClassNameMap.put(new org.apache.axis2.client.FaultMapKey(new javax.xml.namespace.QName("http://webservice.demone.conservazione","WSDemoneException"), "addJob"),"it.kdm.docer.clients.WSDemoneExceptionException");
        faultMessageMap.put(new org.apache.axis2.client.FaultMapKey(new javax.xml.namespace.QName("http://webservice.demone.conservazione","WSDemoneException"), "addJob"),"it.kdm.docer.clients.WSDemoneStub$WSDemoneException");



    }

    /**
     *Constructor that takes in a configContext
     */

    public WSDemoneStub(org.apache.axis2.context.ConfigurationContext configurationContext,
                        java.lang.String targetEndpoint)
            throws org.apache.axis2.AxisFault {
        this(configurationContext,targetEndpoint,false);
    }


    /**
     * Constructor that takes in a configContext  and useseperate listner
     */
    public WSDemoneStub(org.apache.axis2.context.ConfigurationContext configurationContext,
                        java.lang.String targetEndpoint, boolean useSeparateListener)
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
    public WSDemoneStub(org.apache.axis2.context.ConfigurationContext configurationContext) throws org.apache.axis2.AxisFault {

        this(configurationContext,"http://localhost:8080/services/WSDemone.WSDemoneHttpSoap12Endpoint/" );

    }

    /**
     * Default Constructor
     */
    public WSDemoneStub() throws org.apache.axis2.AxisFault {

        this("http://localhost:8080/services/WSDemone.WSDemoneHttpSoap12Endpoint/" );

    }

    /**
     * Constructor taking the target endpoint
     */
    public WSDemoneStub(java.lang.String targetEndpoint) throws org.apache.axis2.AxisFault {
        this(null,targetEndpoint);
    }




    /**
     * Auto generated method signature
     *
     * @see it.kdm.docer.clients.WSDemone#searchJob
     * @param searchJob

     * @throws it.kdm.docer.clients.WSDemoneExceptionException : 
     */



    public  it.kdm.docer.clients.WSDemoneStub.SearchJobResponse searchJob(

            it.kdm.docer.clients.WSDemoneStub.SearchJob searchJob)


            throws java.rmi.RemoteException


            ,it.kdm.docer.clients.WSDemoneExceptionException{
        org.apache.axis2.context.MessageContext _messageContext = null;
        try{
            org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[0].getName());
            _operationClient.getOptions().setAction("urn:searchJob");
            _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);



            addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");


            // create a message context
            _messageContext = new org.apache.axis2.context.MessageContext();



            // create SOAP envelope with that payload
            org.apache.axiom.soap.SOAPEnvelope env = null;


            env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                    searchJob,
                    optimizeContent(new javax.xml.namespace.QName("http://webservice.demone.conservazione",
                            "searchJob")), new javax.xml.namespace.QName("http://webservice.demone.conservazione",
                            "searchJob"));

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


            java.lang.Object object = fromOM(
                    _returnEnv.getBody().getFirstElement() ,
                    it.kdm.docer.clients.WSDemoneStub.SearchJobResponse.class,
                    getEnvelopeNamespaces(_returnEnv));


            return (it.kdm.docer.clients.WSDemoneStub.SearchJobResponse)object;

        }catch(org.apache.axis2.AxisFault f){

            org.apache.axiom.om.OMElement faultElt = f.getDetail();
            if (faultElt!=null){
                if (faultExceptionNameMap.containsKey(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"searchJob"))){
                    //make the fault by reflection
                    try{
                        java.lang.String exceptionClassName = (java.lang.String)faultExceptionClassNameMap.get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"searchJob"));
                        java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
                        java.lang.reflect.Constructor constructor = exceptionClass.getConstructor(String.class);
                        java.lang.Exception ex = (java.lang.Exception) constructor.newInstance(f.getMessage());
                        //message class
                        java.lang.String messageClassName = (java.lang.String)faultMessageMap.get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"searchJob"));
                        java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
                        java.lang.Object messageObject = fromOM(faultElt,messageClass,null);
                        java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
                                new java.lang.Class[]{messageClass});
                        m.invoke(ex,new java.lang.Object[]{messageObject});

                        if (ex instanceof it.kdm.docer.clients.WSDemoneExceptionException){
                            throw (it.kdm.docer.clients.WSDemoneExceptionException)ex;
                        }


                        throw new java.rmi.RemoteException(ex.getMessage(), ex);
                    }catch(java.lang.ClassCastException e){
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.ClassNotFoundException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }catch (java.lang.NoSuchMethodException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.reflect.InvocationTargetException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }  catch (java.lang.IllegalAccessException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }   catch (java.lang.InstantiationException e) {
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
     * @see it.kdm.docer.clients.WSDemone#getJobGson
     * @param getJobGson

     * @throws it.kdm.docer.clients.WSDemoneExceptionException : 
     */



    public  it.kdm.docer.clients.WSDemoneStub.GetJobGsonResponse getJobGson(

            it.kdm.docer.clients.WSDemoneStub.GetJobGson getJobGson)


            throws java.rmi.RemoteException


            ,it.kdm.docer.clients.WSDemoneExceptionException{
        org.apache.axis2.context.MessageContext _messageContext = null;
        try{
            org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[1].getName());
            _operationClient.getOptions().setAction("urn:getJobGson");
            _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);



            addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");


            // create a message context
            _messageContext = new org.apache.axis2.context.MessageContext();



            // create SOAP envelope with that payload
            org.apache.axiom.soap.SOAPEnvelope env = null;


            env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                    getJobGson,
                    optimizeContent(new javax.xml.namespace.QName("http://webservice.demone.conservazione",
                            "getJobGson")), new javax.xml.namespace.QName("http://webservice.demone.conservazione",
                            "getJobGson"));

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


            java.lang.Object object = fromOM(
                    _returnEnv.getBody().getFirstElement() ,
                    it.kdm.docer.clients.WSDemoneStub.GetJobGsonResponse.class,
                    getEnvelopeNamespaces(_returnEnv));


            return (it.kdm.docer.clients.WSDemoneStub.GetJobGsonResponse)object;

        }catch(org.apache.axis2.AxisFault f){

            org.apache.axiom.om.OMElement faultElt = f.getDetail();
            if (faultElt!=null){
                if (faultExceptionNameMap.containsKey(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"getJobGson"))){
                    //make the fault by reflection
                    try{
                        java.lang.String exceptionClassName = (java.lang.String)faultExceptionClassNameMap.get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"getJobGson"));
                        java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
                        java.lang.reflect.Constructor constructor = exceptionClass.getConstructor(String.class);
                        java.lang.Exception ex = (java.lang.Exception) constructor.newInstance(f.getMessage());
                        //message class
                        java.lang.String messageClassName = (java.lang.String)faultMessageMap.get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"getJobGson"));
                        java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
                        java.lang.Object messageObject = fromOM(faultElt,messageClass,null);
                        java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
                                new java.lang.Class[]{messageClass});
                        m.invoke(ex,new java.lang.Object[]{messageObject});

                        if (ex instanceof it.kdm.docer.clients.WSDemoneExceptionException){
                            throw (it.kdm.docer.clients.WSDemoneExceptionException)ex;
                        }


                        throw new java.rmi.RemoteException(ex.getMessage(), ex);
                    }catch(java.lang.ClassCastException e){
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.ClassNotFoundException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }catch (java.lang.NoSuchMethodException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.reflect.InvocationTargetException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }  catch (java.lang.IllegalAccessException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }   catch (java.lang.InstantiationException e) {
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
     * @see it.kdm.docer.clients.WSDemone#searchJobEstesa
     * @param searchJobEstesa

     */



    public  it.kdm.docer.clients.WSDemoneStub.SearchJobEstesaResponse searchJobEstesa(

            it.kdm.docer.clients.WSDemoneStub.SearchJobEstesa searchJobEstesa)


            throws java.rmi.RemoteException

    {
        org.apache.axis2.context.MessageContext _messageContext = null;
        try{
            org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[2].getName());
            _operationClient.getOptions().setAction("urn:searchJobEstesa");
            _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);



            addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");


            // create a message context
            _messageContext = new org.apache.axis2.context.MessageContext();



            // create SOAP envelope with that payload
            org.apache.axiom.soap.SOAPEnvelope env = null;


            env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                    searchJobEstesa,
                    optimizeContent(new javax.xml.namespace.QName("http://webservice.demone.conservazione",
                            "searchJobEstesa")), new javax.xml.namespace.QName("http://webservice.demone.conservazione",
                            "searchJobEstesa"));

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


            java.lang.Object object = fromOM(
                    _returnEnv.getBody().getFirstElement() ,
                    it.kdm.docer.clients.WSDemoneStub.SearchJobEstesaResponse.class,
                    getEnvelopeNamespaces(_returnEnv));


            return (it.kdm.docer.clients.WSDemoneStub.SearchJobEstesaResponse)object;

        }catch(org.apache.axis2.AxisFault f){

            org.apache.axiom.om.OMElement faultElt = f.getDetail();
            if (faultElt!=null){
                if (faultExceptionNameMap.containsKey(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"searchJobEstesa"))){
                    //make the fault by reflection
                    try{
                        java.lang.String exceptionClassName = (java.lang.String)faultExceptionClassNameMap.get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"searchJobEstesa"));
                        java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
                        java.lang.reflect.Constructor constructor = exceptionClass.getConstructor(String.class);
                        java.lang.Exception ex = (java.lang.Exception) constructor.newInstance(f.getMessage());
                        //message class
                        java.lang.String messageClassName = (java.lang.String)faultMessageMap.get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"searchJobEstesa"));
                        java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
                        java.lang.Object messageObject = fromOM(faultElt,messageClass,null);
                        java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
                                new java.lang.Class[]{messageClass});
                        m.invoke(ex,new java.lang.Object[]{messageObject});


                        throw new java.rmi.RemoteException(ex.getMessage(), ex);
                    }catch(java.lang.ClassCastException e){
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.ClassNotFoundException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }catch (java.lang.NoSuchMethodException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.reflect.InvocationTargetException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }  catch (java.lang.IllegalAccessException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }   catch (java.lang.InstantiationException e) {
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
     * @see it.kdm.docer.clients.WSDemone#readLogsByIdJob
     * @param readLogsByIdJob

     * @throws it.kdm.docer.clients.WSDemoneExceptionException : 
     */



    public  it.kdm.docer.clients.WSDemoneStub.ReadLogsByIdJobResponse readLogsByIdJob(

            it.kdm.docer.clients.WSDemoneStub.ReadLogsByIdJob readLogsByIdJob)


            throws java.rmi.RemoteException


            ,it.kdm.docer.clients.WSDemoneExceptionException{
        org.apache.axis2.context.MessageContext _messageContext = null;
        try{
            org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[3].getName());
            _operationClient.getOptions().setAction("urn:readLogsByIdJob");
            _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);



            addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");


            // create a message context
            _messageContext = new org.apache.axis2.context.MessageContext();



            // create SOAP envelope with that payload
            org.apache.axiom.soap.SOAPEnvelope env = null;


            env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                    readLogsByIdJob,
                    optimizeContent(new javax.xml.namespace.QName("http://webservice.demone.conservazione",
                            "readLogsByIdJob")), new javax.xml.namespace.QName("http://webservice.demone.conservazione",
                            "readLogsByIdJob"));

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


            java.lang.Object object = fromOM(
                    _returnEnv.getBody().getFirstElement() ,
                    it.kdm.docer.clients.WSDemoneStub.ReadLogsByIdJobResponse.class,
                    getEnvelopeNamespaces(_returnEnv));


            return (it.kdm.docer.clients.WSDemoneStub.ReadLogsByIdJobResponse)object;

        }catch(org.apache.axis2.AxisFault f){

            org.apache.axiom.om.OMElement faultElt = f.getDetail();
            if (faultElt!=null){
                if (faultExceptionNameMap.containsKey(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"readLogsByIdJob"))){
                    //make the fault by reflection
                    try{
                        java.lang.String exceptionClassName = (java.lang.String)faultExceptionClassNameMap.get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"readLogsByIdJob"));
                        java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
                        java.lang.reflect.Constructor constructor = exceptionClass.getConstructor(String.class);
                        java.lang.Exception ex = (java.lang.Exception) constructor.newInstance(f.getMessage());
                        //message class
                        java.lang.String messageClassName = (java.lang.String)faultMessageMap.get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"readLogsByIdJob"));
                        java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
                        java.lang.Object messageObject = fromOM(faultElt,messageClass,null);
                        java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
                                new java.lang.Class[]{messageClass});
                        m.invoke(ex,new java.lang.Object[]{messageObject});

                        if (ex instanceof it.kdm.docer.clients.WSDemoneExceptionException){
                            throw (it.kdm.docer.clients.WSDemoneExceptionException)ex;
                        }


                        throw new java.rmi.RemoteException(ex.getMessage(), ex);
                    }catch(java.lang.ClassCastException e){
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.ClassNotFoundException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }catch (java.lang.NoSuchMethodException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.reflect.InvocationTargetException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }  catch (java.lang.IllegalAccessException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }   catch (java.lang.InstantiationException e) {
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
     * @see it.kdm.docer.clients.WSDemone#readLogGson_byIdJob
     * @param readLogGson_byIdJob

     * @throws it.kdm.docer.clients.WSDemoneExceptionException : 
     */



    public  it.kdm.docer.clients.WSDemoneStub.ReadLogGson_byIdJobResponse readLogGson_byIdJob(

            it.kdm.docer.clients.WSDemoneStub.ReadLogGson_byIdJob readLogGson_byIdJob)


            throws java.rmi.RemoteException


            ,it.kdm.docer.clients.WSDemoneExceptionException{
        org.apache.axis2.context.MessageContext _messageContext = null;
        try{
            org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[4].getName());
            _operationClient.getOptions().setAction("urn:readLogGson_byIdJob");
            _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);



            addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");


            // create a message context
            _messageContext = new org.apache.axis2.context.MessageContext();



            // create SOAP envelope with that payload
            org.apache.axiom.soap.SOAPEnvelope env = null;


            env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                    readLogGson_byIdJob,
                    optimizeContent(new javax.xml.namespace.QName("http://webservice.demone.conservazione",
                            "readLogGson_byIdJob")), new javax.xml.namespace.QName("http://webservice.demone.conservazione",
                            "readLogGson_byIdJob"));

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


            java.lang.Object object = fromOM(
                    _returnEnv.getBody().getFirstElement() ,
                    it.kdm.docer.clients.WSDemoneStub.ReadLogGson_byIdJobResponse.class,
                    getEnvelopeNamespaces(_returnEnv));


            return (it.kdm.docer.clients.WSDemoneStub.ReadLogGson_byIdJobResponse)object;

        }catch(org.apache.axis2.AxisFault f){

            org.apache.axiom.om.OMElement faultElt = f.getDetail();
            if (faultElt!=null){
                if (faultExceptionNameMap.containsKey(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"readLogGson_byIdJob"))){
                    //make the fault by reflection
                    try{
                        java.lang.String exceptionClassName = (java.lang.String)faultExceptionClassNameMap.get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"readLogGson_byIdJob"));
                        java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
                        java.lang.reflect.Constructor constructor = exceptionClass.getConstructor(String.class);
                        java.lang.Exception ex = (java.lang.Exception) constructor.newInstance(f.getMessage());
                        //message class
                        java.lang.String messageClassName = (java.lang.String)faultMessageMap.get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"readLogGson_byIdJob"));
                        java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
                        java.lang.Object messageObject = fromOM(faultElt,messageClass,null);
                        java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
                                new java.lang.Class[]{messageClass});
                        m.invoke(ex,new java.lang.Object[]{messageObject});

                        if (ex instanceof it.kdm.docer.clients.WSDemoneExceptionException){
                            throw (it.kdm.docer.clients.WSDemoneExceptionException)ex;
                        }


                        throw new java.rmi.RemoteException(ex.getMessage(), ex);
                    }catch(java.lang.ClassCastException e){
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.ClassNotFoundException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }catch (java.lang.NoSuchMethodException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.reflect.InvocationTargetException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }  catch (java.lang.IllegalAccessException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }   catch (java.lang.InstantiationException e) {
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
     * @see it.kdm.docer.clients.WSDemone#deleteJob_ByIdDoc
     * @param deleteJob_ByIdDoc

     * @throws it.kdm.docer.clients.WSDemoneExceptionException : 
     */



    public  it.kdm.docer.clients.WSDemoneStub.DeleteJob_ByIdDocResponse deleteJob_ByIdDoc(

            it.kdm.docer.clients.WSDemoneStub.DeleteJob_ByIdDoc deleteJob_ByIdDoc)


            throws java.rmi.RemoteException


            ,it.kdm.docer.clients.WSDemoneExceptionException{
        org.apache.axis2.context.MessageContext _messageContext = null;
        try{
            org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[5].getName());
            _operationClient.getOptions().setAction("urn:deleteJob_ByIdDoc");
            _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);



            addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");


            // create a message context
            _messageContext = new org.apache.axis2.context.MessageContext();



            // create SOAP envelope with that payload
            org.apache.axiom.soap.SOAPEnvelope env = null;


            env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                    deleteJob_ByIdDoc,
                    optimizeContent(new javax.xml.namespace.QName("http://webservice.demone.conservazione",
                            "deleteJob_ByIdDoc")), new javax.xml.namespace.QName("http://webservice.demone.conservazione",
                            "deleteJob_ByIdDoc"));

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


            java.lang.Object object = fromOM(
                    _returnEnv.getBody().getFirstElement() ,
                    it.kdm.docer.clients.WSDemoneStub.DeleteJob_ByIdDocResponse.class,
                    getEnvelopeNamespaces(_returnEnv));


            return (it.kdm.docer.clients.WSDemoneStub.DeleteJob_ByIdDocResponse)object;

        }catch(org.apache.axis2.AxisFault f){

            org.apache.axiom.om.OMElement faultElt = f.getDetail();
            if (faultElt!=null){
                if (faultExceptionNameMap.containsKey(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"deleteJob_ByIdDoc"))){
                    //make the fault by reflection
                    try{
                        java.lang.String exceptionClassName = (java.lang.String)faultExceptionClassNameMap.get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"deleteJob_ByIdDoc"));
                        java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
                        java.lang.reflect.Constructor constructor = exceptionClass.getConstructor(String.class);
                        java.lang.Exception ex = (java.lang.Exception) constructor.newInstance(f.getMessage());
                        //message class
                        java.lang.String messageClassName = (java.lang.String)faultMessageMap.get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"deleteJob_ByIdDoc"));
                        java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
                        java.lang.Object messageObject = fromOM(faultElt,messageClass,null);
                        java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
                                new java.lang.Class[]{messageClass});
                        m.invoke(ex,new java.lang.Object[]{messageObject});

                        if (ex instanceof it.kdm.docer.clients.WSDemoneExceptionException){
                            throw (it.kdm.docer.clients.WSDemoneExceptionException)ex;
                        }


                        throw new java.rmi.RemoteException(ex.getMessage(), ex);
                    }catch(java.lang.ClassCastException e){
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.ClassNotFoundException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }catch (java.lang.NoSuchMethodException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.reflect.InvocationTargetException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }  catch (java.lang.IllegalAccessException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }   catch (java.lang.InstantiationException e) {
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
     * @see it.kdm.docer.clients.WSDemone#getJob
     * @param getJob

     * @throws it.kdm.docer.clients.WSDemoneExceptionException : 
     */



    public  it.kdm.docer.clients.WSDemoneStub.GetJobResponse getJob(

            it.kdm.docer.clients.WSDemoneStub.GetJob getJob)


            throws java.rmi.RemoteException


            ,it.kdm.docer.clients.WSDemoneExceptionException{
        org.apache.axis2.context.MessageContext _messageContext = null;
        try{
            org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[6].getName());
            _operationClient.getOptions().setAction("urn:getJob");
            _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);



            addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");


            // create a message context
            _messageContext = new org.apache.axis2.context.MessageContext();



            // create SOAP envelope with that payload
            org.apache.axiom.soap.SOAPEnvelope env = null;


            env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                    getJob,
                    optimizeContent(new javax.xml.namespace.QName("http://webservice.demone.conservazione",
                            "getJob")), new javax.xml.namespace.QName("http://webservice.demone.conservazione",
                            "getJob"));

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


            java.lang.Object object = fromOM(
                    _returnEnv.getBody().getFirstElement() ,
                    it.kdm.docer.clients.WSDemoneStub.GetJobResponse.class,
                    getEnvelopeNamespaces(_returnEnv));


            return (it.kdm.docer.clients.WSDemoneStub.GetJobResponse)object;

        }catch(org.apache.axis2.AxisFault f){

            org.apache.axiom.om.OMElement faultElt = f.getDetail();
            if (faultElt!=null){
                if (faultExceptionNameMap.containsKey(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"getJob"))){
                    //make the fault by reflection
                    try{
                        java.lang.String exceptionClassName = (java.lang.String)faultExceptionClassNameMap.get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"getJob"));
                        java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
                        java.lang.reflect.Constructor constructor = exceptionClass.getConstructor(String.class);
                        java.lang.Exception ex = (java.lang.Exception) constructor.newInstance(f.getMessage());
                        //message class
                        java.lang.String messageClassName = (java.lang.String)faultMessageMap.get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"getJob"));
                        java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
                        java.lang.Object messageObject = fromOM(faultElt,messageClass,null);
                        java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
                                new java.lang.Class[]{messageClass});
                        m.invoke(ex,new java.lang.Object[]{messageObject});

                        if (ex instanceof it.kdm.docer.clients.WSDemoneExceptionException){
                            throw (it.kdm.docer.clients.WSDemoneExceptionException)ex;
                        }


                        throw new java.rmi.RemoteException(ex.getMessage(), ex);
                    }catch(java.lang.ClassCastException e){
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.ClassNotFoundException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }catch (java.lang.NoSuchMethodException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.reflect.InvocationTargetException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }  catch (java.lang.IllegalAccessException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }   catch (java.lang.InstantiationException e) {
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
     * @see it.kdm.docer.clients.WSDemone#searchLogEstesa
     * @param searchLogEstesa

     * @throws it.kdm.docer.clients.WSDemoneExceptionException : 
     */



    public  it.kdm.docer.clients.WSDemoneStub.SearchLogEstesaResponse searchLogEstesa(

            it.kdm.docer.clients.WSDemoneStub.SearchLogEstesa searchLogEstesa)


            throws java.rmi.RemoteException


            ,it.kdm.docer.clients.WSDemoneExceptionException{
        org.apache.axis2.context.MessageContext _messageContext = null;
        try{
            org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[7].getName());
            _operationClient.getOptions().setAction("urn:searchLogEstesa");
            _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);



            addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");


            // create a message context
            _messageContext = new org.apache.axis2.context.MessageContext();



            // create SOAP envelope with that payload
            org.apache.axiom.soap.SOAPEnvelope env = null;


            env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                    searchLogEstesa,
                    optimizeContent(new javax.xml.namespace.QName("http://webservice.demone.conservazione",
                            "searchLogEstesa")), new javax.xml.namespace.QName("http://webservice.demone.conservazione",
                            "searchLogEstesa"));

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


            java.lang.Object object = fromOM(
                    _returnEnv.getBody().getFirstElement() ,
                    it.kdm.docer.clients.WSDemoneStub.SearchLogEstesaResponse.class,
                    getEnvelopeNamespaces(_returnEnv));


            return (it.kdm.docer.clients.WSDemoneStub.SearchLogEstesaResponse)object;

        }catch(org.apache.axis2.AxisFault f){

            org.apache.axiom.om.OMElement faultElt = f.getDetail();
            if (faultElt!=null){
                if (faultExceptionNameMap.containsKey(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"searchLogEstesa"))){
                    //make the fault by reflection
                    try{
                        java.lang.String exceptionClassName = (java.lang.String)faultExceptionClassNameMap.get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"searchLogEstesa"));
                        java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
                        java.lang.reflect.Constructor constructor = exceptionClass.getConstructor(String.class);
                        java.lang.Exception ex = (java.lang.Exception) constructor.newInstance(f.getMessage());
                        //message class
                        java.lang.String messageClassName = (java.lang.String)faultMessageMap.get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"searchLogEstesa"));
                        java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
                        java.lang.Object messageObject = fromOM(faultElt,messageClass,null);
                        java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
                                new java.lang.Class[]{messageClass});
                        m.invoke(ex,new java.lang.Object[]{messageObject});

                        if (ex instanceof it.kdm.docer.clients.WSDemoneExceptionException){
                            throw (it.kdm.docer.clients.WSDemoneExceptionException)ex;
                        }


                        throw new java.rmi.RemoteException(ex.getMessage(), ex);
                    }catch(java.lang.ClassCastException e){
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.ClassNotFoundException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }catch (java.lang.NoSuchMethodException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.reflect.InvocationTargetException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }  catch (java.lang.IllegalAccessException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }   catch (java.lang.InstantiationException e) {
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
     * @see it.kdm.docer.clients.WSDemone#getJobByDocId
     * @param getJobByDocId

     * @throws it.kdm.docer.clients.WSDemoneExceptionException : 
     */



    public  it.kdm.docer.clients.WSDemoneStub.GetJobByDocIdResponse getJobByDocId(

            it.kdm.docer.clients.WSDemoneStub.GetJobByDocId getJobByDocId)


            throws java.rmi.RemoteException


            ,it.kdm.docer.clients.WSDemoneExceptionException{
        org.apache.axis2.context.MessageContext _messageContext = null;
        try{
            org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[8].getName());
            _operationClient.getOptions().setAction("urn:getJobByDocId");
            _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);



            addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");


            // create a message context
            _messageContext = new org.apache.axis2.context.MessageContext();



            // create SOAP envelope with that payload
            org.apache.axiom.soap.SOAPEnvelope env = null;


            env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                    getJobByDocId,
                    optimizeContent(new javax.xml.namespace.QName("http://webservice.demone.conservazione",
                            "getJobByDocId")), new javax.xml.namespace.QName("http://webservice.demone.conservazione",
                            "getJobByDocId"));

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


            java.lang.Object object = fromOM(
                    _returnEnv.getBody().getFirstElement() ,
                    it.kdm.docer.clients.WSDemoneStub.GetJobByDocIdResponse.class,
                    getEnvelopeNamespaces(_returnEnv));


            return (it.kdm.docer.clients.WSDemoneStub.GetJobByDocIdResponse)object;

        }catch(org.apache.axis2.AxisFault f){

            org.apache.axiom.om.OMElement faultElt = f.getDetail();
            if (faultElt!=null){
                if (faultExceptionNameMap.containsKey(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"getJobByDocId"))){
                    //make the fault by reflection
                    try{
                        java.lang.String exceptionClassName = (java.lang.String)faultExceptionClassNameMap.get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"getJobByDocId"));
                        java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
                        java.lang.reflect.Constructor constructor = exceptionClass.getConstructor(String.class);
                        java.lang.Exception ex = (java.lang.Exception) constructor.newInstance(f.getMessage());
                        //message class
                        java.lang.String messageClassName = (java.lang.String)faultMessageMap.get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"getJobByDocId"));
                        java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
                        java.lang.Object messageObject = fromOM(faultElt,messageClass,null);
                        java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
                                new java.lang.Class[]{messageClass});
                        m.invoke(ex,new java.lang.Object[]{messageObject});

                        if (ex instanceof it.kdm.docer.clients.WSDemoneExceptionException){
                            throw (it.kdm.docer.clients.WSDemoneExceptionException)ex;
                        }


                        throw new java.rmi.RemoteException(ex.getMessage(), ex);
                    }catch(java.lang.ClassCastException e){
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.ClassNotFoundException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }catch (java.lang.NoSuchMethodException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.reflect.InvocationTargetException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }  catch (java.lang.IllegalAccessException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }   catch (java.lang.InstantiationException e) {
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
     * @see it.kdm.docer.clients.WSDemone#deleteJob_byIdJob
     * @param deleteJob_byIdJob

     * @throws it.kdm.docer.clients.WSDemoneExceptionException : 
     */



    public  it.kdm.docer.clients.WSDemoneStub.DeleteJob_byIdJobResponse deleteJob_byIdJob(

            it.kdm.docer.clients.WSDemoneStub.DeleteJob_byIdJob deleteJob_byIdJob)


            throws java.rmi.RemoteException


            ,it.kdm.docer.clients.WSDemoneExceptionException{
        org.apache.axis2.context.MessageContext _messageContext = null;
        try{
            org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[9].getName());
            _operationClient.getOptions().setAction("urn:deleteJob_byIdJob");
            _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);



            addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");


            // create a message context
            _messageContext = new org.apache.axis2.context.MessageContext();



            // create SOAP envelope with that payload
            org.apache.axiom.soap.SOAPEnvelope env = null;


            env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                    deleteJob_byIdJob,
                    optimizeContent(new javax.xml.namespace.QName("http://webservice.demone.conservazione",
                            "deleteJob_byIdJob")), new javax.xml.namespace.QName("http://webservice.demone.conservazione",
                            "deleteJob_byIdJob"));

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


            java.lang.Object object = fromOM(
                    _returnEnv.getBody().getFirstElement() ,
                    it.kdm.docer.clients.WSDemoneStub.DeleteJob_byIdJobResponse.class,
                    getEnvelopeNamespaces(_returnEnv));


            return (it.kdm.docer.clients.WSDemoneStub.DeleteJob_byIdJobResponse)object;

        }catch(org.apache.axis2.AxisFault f){

            org.apache.axiom.om.OMElement faultElt = f.getDetail();
            if (faultElt!=null){
                if (faultExceptionNameMap.containsKey(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"deleteJob_byIdJob"))){
                    //make the fault by reflection
                    try{
                        java.lang.String exceptionClassName = (java.lang.String)faultExceptionClassNameMap.get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"deleteJob_byIdJob"));
                        java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
                        java.lang.reflect.Constructor constructor = exceptionClass.getConstructor(String.class);
                        java.lang.Exception ex = (java.lang.Exception) constructor.newInstance(f.getMessage());
                        //message class
                        java.lang.String messageClassName = (java.lang.String)faultMessageMap.get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"deleteJob_byIdJob"));
                        java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
                        java.lang.Object messageObject = fromOM(faultElt,messageClass,null);
                        java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
                                new java.lang.Class[]{messageClass});
                        m.invoke(ex,new java.lang.Object[]{messageObject});

                        if (ex instanceof it.kdm.docer.clients.WSDemoneExceptionException){
                            throw (it.kdm.docer.clients.WSDemoneExceptionException)ex;
                        }


                        throw new java.rmi.RemoteException(ex.getMessage(), ex);
                    }catch(java.lang.ClassCastException e){
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.ClassNotFoundException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }catch (java.lang.NoSuchMethodException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.reflect.InvocationTargetException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }  catch (java.lang.IllegalAccessException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }   catch (java.lang.InstantiationException e) {
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
     * @see it.kdm.docer.clients.WSDemone#updateJob
     * @param updateJob

     * @throws it.kdm.docer.clients.WSDemoneExceptionException : 
     */



    public  it.kdm.docer.clients.WSDemoneStub.UpdateJobResponse updateJob(

            it.kdm.docer.clients.WSDemoneStub.UpdateJob updateJob)


            throws java.rmi.RemoteException


            ,it.kdm.docer.clients.WSDemoneExceptionException{
        org.apache.axis2.context.MessageContext _messageContext = null;
        try{
            org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[10].getName());
            _operationClient.getOptions().setAction("urn:updateJob");
            _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);



            addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");


            // create a message context
            _messageContext = new org.apache.axis2.context.MessageContext();



            // create SOAP envelope with that payload
            org.apache.axiom.soap.SOAPEnvelope env = null;


            env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                    updateJob,
                    optimizeContent(new javax.xml.namespace.QName("http://webservice.demone.conservazione",
                            "updateJob")), new javax.xml.namespace.QName("http://webservice.demone.conservazione",
                            "updateJob"));

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


            java.lang.Object object = fromOM(
                    _returnEnv.getBody().getFirstElement() ,
                    it.kdm.docer.clients.WSDemoneStub.UpdateJobResponse.class,
                    getEnvelopeNamespaces(_returnEnv));


            return (it.kdm.docer.clients.WSDemoneStub.UpdateJobResponse)object;

        }catch(org.apache.axis2.AxisFault f){

            org.apache.axiom.om.OMElement faultElt = f.getDetail();
            if (faultElt!=null){
                if (faultExceptionNameMap.containsKey(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"updateJob"))){
                    //make the fault by reflection
                    try{
                        java.lang.String exceptionClassName = (java.lang.String)faultExceptionClassNameMap.get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"updateJob"));
                        java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
                        java.lang.reflect.Constructor constructor = exceptionClass.getConstructor(String.class);
                        java.lang.Exception ex = (java.lang.Exception) constructor.newInstance(f.getMessage());
                        //message class
                        java.lang.String messageClassName = (java.lang.String)faultMessageMap.get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"updateJob"));
                        java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
                        java.lang.Object messageObject = fromOM(faultElt,messageClass,null);
                        java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
                                new java.lang.Class[]{messageClass});
                        m.invoke(ex,new java.lang.Object[]{messageObject});

                        if (ex instanceof it.kdm.docer.clients.WSDemoneExceptionException){
                            throw (it.kdm.docer.clients.WSDemoneExceptionException)ex;
                        }


                        throw new java.rmi.RemoteException(ex.getMessage(), ex);
                    }catch(java.lang.ClassCastException e){
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.ClassNotFoundException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }catch (java.lang.NoSuchMethodException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.reflect.InvocationTargetException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }  catch (java.lang.IllegalAccessException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }   catch (java.lang.InstantiationException e) {
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
     * @see it.kdm.docer.clients.WSDemone#readLogsByIdDoc
     * @param readLogsByIdDoc

     * @throws it.kdm.docer.clients.WSDemoneExceptionException : 
     */



    public  it.kdm.docer.clients.WSDemoneStub.ReadLogsByIdDocResponse readLogsByIdDoc(

            it.kdm.docer.clients.WSDemoneStub.ReadLogsByIdDoc readLogsByIdDoc)


            throws java.rmi.RemoteException


            ,it.kdm.docer.clients.WSDemoneExceptionException{
        org.apache.axis2.context.MessageContext _messageContext = null;
        try{
            org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[11].getName());
            _operationClient.getOptions().setAction("urn:readLogsByIdDoc");
            _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);



            addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");


            // create a message context
            _messageContext = new org.apache.axis2.context.MessageContext();



            // create SOAP envelope with that payload
            org.apache.axiom.soap.SOAPEnvelope env = null;


            env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                    readLogsByIdDoc,
                    optimizeContent(new javax.xml.namespace.QName("http://webservice.demone.conservazione",
                            "readLogsByIdDoc")), new javax.xml.namespace.QName("http://webservice.demone.conservazione",
                            "readLogsByIdDoc"));

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


            java.lang.Object object = fromOM(
                    _returnEnv.getBody().getFirstElement() ,
                    it.kdm.docer.clients.WSDemoneStub.ReadLogsByIdDocResponse.class,
                    getEnvelopeNamespaces(_returnEnv));


            return (it.kdm.docer.clients.WSDemoneStub.ReadLogsByIdDocResponse)object;

        }catch(org.apache.axis2.AxisFault f){

            org.apache.axiom.om.OMElement faultElt = f.getDetail();
            if (faultElt!=null){
                if (faultExceptionNameMap.containsKey(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"readLogsByIdDoc"))){
                    //make the fault by reflection
                    try{
                        java.lang.String exceptionClassName = (java.lang.String)faultExceptionClassNameMap.get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"readLogsByIdDoc"));
                        java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
                        java.lang.reflect.Constructor constructor = exceptionClass.getConstructor(String.class);
                        java.lang.Exception ex = (java.lang.Exception) constructor.newInstance(f.getMessage());
                        //message class
                        java.lang.String messageClassName = (java.lang.String)faultMessageMap.get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"readLogsByIdDoc"));
                        java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
                        java.lang.Object messageObject = fromOM(faultElt,messageClass,null);
                        java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
                                new java.lang.Class[]{messageClass});
                        m.invoke(ex,new java.lang.Object[]{messageObject});

                        if (ex instanceof it.kdm.docer.clients.WSDemoneExceptionException){
                            throw (it.kdm.docer.clients.WSDemoneExceptionException)ex;
                        }


                        throw new java.rmi.RemoteException(ex.getMessage(), ex);
                    }catch(java.lang.ClassCastException e){
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.ClassNotFoundException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }catch (java.lang.NoSuchMethodException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.reflect.InvocationTargetException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }  catch (java.lang.IllegalAccessException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }   catch (java.lang.InstantiationException e) {
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
     * @see it.kdm.docer.clients.WSDemone#addJob
     * @param addJob

     * @throws it.kdm.docer.clients.WSDemoneExceptionException : 
     */



    public  it.kdm.docer.clients.WSDemoneStub.AddJobResponse addJob(

            it.kdm.docer.clients.WSDemoneStub.AddJob addJob)


            throws java.rmi.RemoteException


            ,it.kdm.docer.clients.WSDemoneExceptionException{
        org.apache.axis2.context.MessageContext _messageContext = null;
        try{
            org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[12].getName());
            _operationClient.getOptions().setAction("urn:addJob");
            _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);



            addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");


            // create a message context
            _messageContext = new org.apache.axis2.context.MessageContext();



            // create SOAP envelope with that payload
            org.apache.axiom.soap.SOAPEnvelope env = null;


            env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                    addJob,
                    optimizeContent(new javax.xml.namespace.QName("http://webservice.demone.conservazione",
                            "addJob")), new javax.xml.namespace.QName("http://webservice.demone.conservazione",
                            "addJob"));

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


            java.lang.Object object = fromOM(
                    _returnEnv.getBody().getFirstElement() ,
                    it.kdm.docer.clients.WSDemoneStub.AddJobResponse.class,
                    getEnvelopeNamespaces(_returnEnv));


            return (it.kdm.docer.clients.WSDemoneStub.AddJobResponse)object;

        }catch(org.apache.axis2.AxisFault f){

            org.apache.axiom.om.OMElement faultElt = f.getDetail();
            if (faultElt!=null){
                if (faultExceptionNameMap.containsKey(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"addJob"))){
                    //make the fault by reflection
                    try{
                        java.lang.String exceptionClassName = (java.lang.String)faultExceptionClassNameMap.get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"addJob"));
                        java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
                        java.lang.reflect.Constructor constructor = exceptionClass.getConstructor(String.class);
                        java.lang.Exception ex = (java.lang.Exception) constructor.newInstance(f.getMessage());
                        //message class
                        java.lang.String messageClassName = (java.lang.String)faultMessageMap.get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"addJob"));
                        java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
                        java.lang.Object messageObject = fromOM(faultElt,messageClass,null);
                        java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
                                new java.lang.Class[]{messageClass});
                        m.invoke(ex,new java.lang.Object[]{messageObject});

                        if (ex instanceof it.kdm.docer.clients.WSDemoneExceptionException){
                            throw (it.kdm.docer.clients.WSDemoneExceptionException)ex;
                        }


                        throw new java.rmi.RemoteException(ex.getMessage(), ex);
                    }catch(java.lang.ClassCastException e){
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.ClassNotFoundException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }catch (java.lang.NoSuchMethodException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.reflect.InvocationTargetException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }  catch (java.lang.IllegalAccessException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }   catch (java.lang.InstantiationException e) {
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
    //http://localhost:8080/services/WSDemone.WSDemoneHttpSoap12Endpoint/
    public static class SearchLogEstesaResponse
            implements org.apache.axis2.databinding.ADBBean{

        public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName(
                "http://webservice.demone.conservazione",
                "searchLogEstesaResponse",
                "ns3");



        /**
         * field for _return
         * This was an Array!
         */


        protected Log[] local_return ;

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
         * @return Log[]
         */
        public  Log[] get_return(){
            return local_return;
        }






        /**
         * validate the array for _return
         */
        protected void validate_return(Log[] param){

        }


        /**
         * Auto generated setter method
         * @param param _return
         */
        public void set_return(Log[] param){

            validate_return(param);

            local_returnTracker = true;

            this.local_return=param;
        }



        /**
         * Auto generated add method for the array for convenience
         * @param param Log
         */
        public void add_return(Log param){
            if (local_return == null){
                local_return = new Log[]{};
            }


            //update the setting tracker
            local_returnTracker = true;


            java.util.List list =
                    org.apache.axis2.databinding.utils.ConverterUtil.toList(local_return);
            list.add(param);
            this.local_return =
                    (Log[])list.toArray(
                            new Log[list.size()]);

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




            java.lang.String prefix = null;
            java.lang.String namespace = null;


            prefix = parentQName.getPrefix();
            namespace = parentQName.getNamespaceURI();
            writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

            if (serializeType){


                java.lang.String namespacePrefix = registerPrefix(xmlWriter,"http://webservice.demone.conservazione");
                if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            namespacePrefix+":searchLogEstesaResponse",
                            xmlWriter);
                } else {
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            "searchLogEstesaResponse",
                            xmlWriter);
                }


            }
            if (local_returnTracker){
                if (local_return!=null){
                    for (int i = 0;i < local_return.length;i++){
                        if (local_return[i] != null){
                            local_return[i].serialize(new javax.xml.namespace.QName("http://webservice.demone.conservazione","return"),
                                    xmlWriter);
                        } else {

                            writeStartElement(null, "http://webservice.demone.conservazione", "return", xmlWriter);

                            // write the nil attribute
                            writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                            xmlWriter.writeEndElement();

                        }

                    }
                } else {

                    writeStartElement(null, "http://webservice.demone.conservazione", "return", xmlWriter);

                    // write the nil attribute
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                    xmlWriter.writeEndElement();

                }
            }
            xmlWriter.writeEndElement();


        }

        private static java.lang.String generatePrefix(java.lang.String namespace) {
            if(namespace.equals("http://webservice.demone.conservazione")){
                return "ns3";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
         * Utility method to write an element start tag.
         */
        private void writeStartElement(java.lang.String prefix, java.lang.String namespace, java.lang.String localPart,
                                       javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String writerPrefix = xmlWriter.getPrefix(namespace);
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
        private void writeAttribute(java.lang.String prefix,java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (xmlWriter.getPrefix(namespace) == null) {
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            xmlWriter.writeAttribute(namespace,attName,attValue);
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeAttribute(java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
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
        private void writeQNameAttribute(java.lang.String namespace, java.lang.String attName,
                                         javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            java.lang.String attributeNamespace = qname.getNamespaceURI();
            java.lang.String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
            if (attributePrefix == null) {
                attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
            }
            java.lang.String attributeValue;
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
            java.lang.String namespaceURI = qname.getNamespaceURI();
            if (namespaceURI != null) {
                java.lang.String prefix = xmlWriter.getPrefix(namespaceURI);
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
                java.lang.StringBuffer stringToWrite = new java.lang.StringBuffer();
                java.lang.String namespaceURI = null;
                java.lang.String prefix = null;

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
        private java.lang.String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, java.lang.String namespace) throws javax.xml.stream.XMLStreamException {
            java.lang.String prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null) {
                prefix = generatePrefix(namespace);
                javax.xml.namespace.NamespaceContext nsContext = xmlWriter.getNamespaceContext();
                while (true) {
                    java.lang.String uri = nsContext.getNamespaceURI(prefix);
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
                if (local_return!=null) {
                    for (int i = 0;i < local_return.length;i++){

                        if (local_return[i] != null){
                            elementList.add(new javax.xml.namespace.QName("http://webservice.demone.conservazione",
                                    "return"));
                            elementList.add(local_return[i]);
                        } else {

                            elementList.add(new javax.xml.namespace.QName("http://webservice.demone.conservazione",
                                    "return"));
                            elementList.add(null);

                        }

                    }
                } else {

                    elementList.add(new javax.xml.namespace.QName("http://webservice.demone.conservazione",
                            "return"));
                    elementList.add(local_return);

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
            public static SearchLogEstesaResponse parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{
                SearchLogEstesaResponse object =
                        new SearchLogEstesaResponse();

                int event;
                java.lang.String nillableValue = null;
                java.lang.String prefix ="";
                java.lang.String namespaceuri ="";
                try {

                    while (!reader.isStartElement() && !reader.isEndElement())
                        reader.next();


                    if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","type")!=null){
                        java.lang.String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                                "type");
                        if (fullTypeName!=null){
                            java.lang.String nsPrefix = null;
                            if (fullTypeName.indexOf(":") > -1){
                                nsPrefix = fullTypeName.substring(0,fullTypeName.indexOf(":"));
                            }
                            nsPrefix = nsPrefix==null?"":nsPrefix;

                            java.lang.String type = fullTypeName.substring(fullTypeName.indexOf(":")+1);

                            if (!"searchLogEstesaResponse".equals(type)){
                                //find namespace for the prefix
                                java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (SearchLogEstesaResponse)ExtensionMapper.getTypeObject(
                                        nsUri,type,reader);
                            }


                        }


                    }




                    // Note all attributes that were handled. Used to differ normal attributes
                    // from anyAttributes.
                    java.util.Vector handledAttributes = new java.util.Vector();




                    reader.next();

                    java.util.ArrayList list1 = new java.util.ArrayList();


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://webservice.demone.conservazione","return").equals(reader.getName())){



                        // Process the array and step past its final element's end.

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                            list1.add(null);
                            reader.next();
                        } else {
                            list1.add(Log.Factory.parse(reader));
                        }
                        //loop until we find a start element that is not part of this array
                        boolean loopDone1 = false;
                        while(!loopDone1){
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
                                loopDone1 = true;
                            } else {
                                if (new javax.xml.namespace.QName("http://webservice.demone.conservazione","return").equals(reader.getName())){

                                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                    if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                        list1.add(null);
                                        reader.next();
                                    } else {
                                        list1.add(Log.Factory.parse(reader));
                                    }
                                }else{
                                    loopDone1 = true;
                                }
                            }
                        }
                        // call the converter utility  to convert and set the array

                        object.set_return((Log[])
                                org.apache.axis2.databinding.utils.ConverterUtil.convertToArray(
                                        Log.class,
                                        list1));

                    }  // End of if for expected property start element

                    else {

                    }

                    while (!reader.isStartElement() && !reader.isEndElement())
                        reader.next();

                    if (reader.isStartElement())
                        // A start element we are not expecting indicates a trailing invalid property
                        throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());




                } catch (javax.xml.stream.XMLStreamException e) {
                    throw new java.lang.Exception(e);
                }

                return object;
            }

        }//end of factory class



    }


    public static class SearchJob
            implements org.apache.axis2.databinding.ADBBean{

        public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName(
                "http://webservice.demone.conservazione",
                "searchJob",
                "ns3");



        /**
         * field for DataInizio
         */


        protected java.util.Calendar localDataInizio ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localDataInizioTracker = false ;

        public boolean isDataInizioSpecified(){
            return localDataInizioTracker;
        }



        /**
         * Auto generated getter method
         * @return java.util.Calendar
         */
        public  java.util.Calendar getDataInizio(){
            return localDataInizio;
        }



        /**
         * Auto generated setter method
         * @param param DataInizio
         */
        public void setDataInizio(java.util.Calendar param){
            localDataInizioTracker = true;

            this.localDataInizio=param;


        }


        /**
         * field for DataFine
         */


        protected java.util.Calendar localDataFine ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localDataFineTracker = false ;

        public boolean isDataFineSpecified(){
            return localDataFineTracker;
        }



        /**
         * Auto generated getter method
         * @return java.util.Calendar
         */
        public  java.util.Calendar getDataFine(){
            return localDataFine;
        }



        /**
         * Auto generated setter method
         * @param param DataFine
         */
        public void setDataFine(java.util.Calendar param){
            localDataFineTracker = true;

            this.localDataFine=param;


        }


        /**
         * field for Esito
         */


        protected java.lang.Object localEsito ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localEsitoTracker = false ;

        public boolean isEsitoSpecified(){
            return localEsitoTracker;
        }



        /**
         * Auto generated getter method
         * @return java.lang.Object
         */
        public  java.lang.Object getEsito(){
            return localEsito;
        }



        /**
         * Auto generated setter method
         * @param param Esito
         */
        public void setEsito(java.lang.Object param){
            localEsitoTracker = true;

            this.localEsito=param;


        }


        /**
         * field for DocId
         */


        protected java.lang.String localDocId ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localDocIdTracker = false ;

        public boolean isDocIdSpecified(){
            return localDocIdTracker;
        }



        /**
         * Auto generated getter method
         * @return java.lang.String
         */
        public  java.lang.String getDocId(){
            return localDocId;
        }



        /**
         * Auto generated setter method
         * @param param DocId
         */
        public void setDocId(java.lang.String param){
            localDocIdTracker = true;

            this.localDocId=param;


        }


        /**
         * field for DocType
         */


        protected java.lang.String localDocType ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localDocTypeTracker = false ;

        public boolean isDocTypeSpecified(){
            return localDocTypeTracker;
        }



        /**
         * Auto generated getter method
         * @return java.lang.String
         */
        public  java.lang.String getDocType(){
            return localDocType;
        }



        /**
         * Auto generated setter method
         * @param param DocType
         */
        public void setDocType(java.lang.String param){
            localDocTypeTracker = true;

            this.localDocType=param;


        }


        /**
         * field for ErrCode
         */


        protected java.lang.String localErrCode ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localErrCodeTracker = false ;

        public boolean isErrCodeSpecified(){
            return localErrCodeTracker;
        }



        /**
         * Auto generated getter method
         * @return java.lang.String
         */
        public  java.lang.String getErrCode(){
            return localErrCode;
        }



        /**
         * Auto generated setter method
         * @param param ErrCode
         */
        public void setErrCode(java.lang.String param){
            localErrCodeTracker = true;

            this.localErrCode=param;


        }


        /**
         * field for Ente
         */


        protected java.lang.String localEnte ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localEnteTracker = false ;

        public boolean isEnteSpecified(){
            return localEnteTracker;
        }



        /**
         * Auto generated getter method
         * @return java.lang.String
         */
        public  java.lang.String getEnte(){
            return localEnte;
        }



        /**
         * Auto generated setter method
         * @param param Ente
         */
        public void setEnte(java.lang.String param){
            localEnteTracker = true;

            this.localEnte=param;


        }


        /**
         * field for Aoo
         */


        protected java.lang.String localAoo ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localAooTracker = false ;

        public boolean isAooSpecified(){
            return localAooTracker;
        }



        /**
         * Auto generated getter method
         * @return java.lang.String
         */
        public  java.lang.String getAoo(){
            return localAoo;
        }



        /**
         * Auto generated setter method
         * @param param Aoo
         */
        public void setAoo(java.lang.String param){
            localAooTracker = true;

            this.localAoo=param;


        }


        /**
         * field for MaxRows
         */


        protected int localMaxRows ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localMaxRowsTracker = false ;

        public boolean isMaxRowsSpecified(){
            return localMaxRowsTracker;
        }



        /**
         * Auto generated getter method
         * @return int
         */
        public  int getMaxRows(){
            return localMaxRows;
        }



        /**
         * Auto generated setter method
         * @param param MaxRows
         */
        public void setMaxRows(int param){

            // setting primitive attribute tracker to true
            localMaxRowsTracker =
                    param != java.lang.Integer.MIN_VALUE;

            this.localMaxRows=param;


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




            java.lang.String prefix = null;
            java.lang.String namespace = null;


            prefix = parentQName.getPrefix();
            namespace = parentQName.getNamespaceURI();
            writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

            if (serializeType){


                java.lang.String namespacePrefix = registerPrefix(xmlWriter,"http://webservice.demone.conservazione");
                if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            namespacePrefix+":searchJob",
                            xmlWriter);
                } else {
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            "searchJob",
                            xmlWriter);
                }


            }
            if (localDataInizioTracker){
                namespace = "http://webservice.demone.conservazione";
                writeStartElement(null, namespace, "dataInizio", xmlWriter);


                if (localDataInizio==null){
                    // write the nil attribute

                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                }else{


                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDataInizio));

                }

                xmlWriter.writeEndElement();
            } if (localDataFineTracker){
                namespace = "http://webservice.demone.conservazione";
                writeStartElement(null, namespace, "dataFine", xmlWriter);


                if (localDataFine==null){
                    // write the nil attribute

                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                }else{


                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDataFine));

                }

                xmlWriter.writeEndElement();
            } if (localEsitoTracker){

                if (localEsito!=null){
                    if (localEsito instanceof org.apache.axis2.databinding.ADBBean){
                        ((org.apache.axis2.databinding.ADBBean)localEsito).serialize(
                                new javax.xml.namespace.QName("http://webservice.demone.conservazione","esito"),
                                xmlWriter,true);
                    } else {
                        writeStartElement(null, "http://webservice.demone.conservazione", "esito", xmlWriter);
                        org.apache.axis2.databinding.utils.ConverterUtil.serializeAnyType(localEsito, xmlWriter);
                        xmlWriter.writeEndElement();
                    }
                } else {

                    // write null attribute
                    writeStartElement(null, "http://webservice.demone.conservazione", "esito", xmlWriter);

                    // write the nil attribute
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                    xmlWriter.writeEndElement();

                }


            } if (localDocIdTracker){
                namespace = "http://webservice.demone.conservazione";
                writeStartElement(null, namespace, "docId", xmlWriter);


                if (localDocId==null){
                    // write the nil attribute

                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                }else{


                    xmlWriter.writeCharacters(localDocId);

                }

                xmlWriter.writeEndElement();
            } if (localDocTypeTracker){
                namespace = "http://webservice.demone.conservazione";
                writeStartElement(null, namespace, "docType", xmlWriter);


                if (localDocType==null){
                    // write the nil attribute

                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                }else{


                    xmlWriter.writeCharacters(localDocType);

                }

                xmlWriter.writeEndElement();
            } if (localErrCodeTracker){
                namespace = "http://webservice.demone.conservazione";
                writeStartElement(null, namespace, "errCode", xmlWriter);


                if (localErrCode==null){
                    // write the nil attribute

                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                }else{


                    xmlWriter.writeCharacters(localErrCode);

                }

                xmlWriter.writeEndElement();
            } if (localEnteTracker){
                namespace = "http://webservice.demone.conservazione";
                writeStartElement(null, namespace, "ente", xmlWriter);


                if (localEnte==null){
                    // write the nil attribute

                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                }else{


                    xmlWriter.writeCharacters(localEnte);

                }

                xmlWriter.writeEndElement();
            } if (localAooTracker){
                namespace = "http://webservice.demone.conservazione";
                writeStartElement(null, namespace, "aoo", xmlWriter);


                if (localAoo==null){
                    // write the nil attribute

                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                }else{


                    xmlWriter.writeCharacters(localAoo);

                }

                xmlWriter.writeEndElement();
            } if (localMaxRowsTracker){
                namespace = "http://webservice.demone.conservazione";
                writeStartElement(null, namespace, "maxRows", xmlWriter);

                if (localMaxRows==java.lang.Integer.MIN_VALUE) {

                    throw new org.apache.axis2.databinding.ADBException("maxRows cannot be null!!");

                } else {
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localMaxRows));
                }

                xmlWriter.writeEndElement();
            }
            xmlWriter.writeEndElement();


        }

        private static java.lang.String generatePrefix(java.lang.String namespace) {
            if(namespace.equals("http://webservice.demone.conservazione")){
                return "ns3";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
         * Utility method to write an element start tag.
         */
        private void writeStartElement(java.lang.String prefix, java.lang.String namespace, java.lang.String localPart,
                                       javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String writerPrefix = xmlWriter.getPrefix(namespace);
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
        private void writeAttribute(java.lang.String prefix,java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (xmlWriter.getPrefix(namespace) == null) {
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            xmlWriter.writeAttribute(namespace,attName,attValue);
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeAttribute(java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
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
        private void writeQNameAttribute(java.lang.String namespace, java.lang.String attName,
                                         javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            java.lang.String attributeNamespace = qname.getNamespaceURI();
            java.lang.String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
            if (attributePrefix == null) {
                attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
            }
            java.lang.String attributeValue;
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
            java.lang.String namespaceURI = qname.getNamespaceURI();
            if (namespaceURI != null) {
                java.lang.String prefix = xmlWriter.getPrefix(namespaceURI);
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
                java.lang.StringBuffer stringToWrite = new java.lang.StringBuffer();
                java.lang.String namespaceURI = null;
                java.lang.String prefix = null;

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
        private java.lang.String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, java.lang.String namespace) throws javax.xml.stream.XMLStreamException {
            java.lang.String prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null) {
                prefix = generatePrefix(namespace);
                javax.xml.namespace.NamespaceContext nsContext = xmlWriter.getNamespaceContext();
                while (true) {
                    java.lang.String uri = nsContext.getNamespaceURI(prefix);
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

            if (localDataInizioTracker){
                elementList.add(new javax.xml.namespace.QName("http://webservice.demone.conservazione",
                        "dataInizio"));

                elementList.add(localDataInizio==null?null:
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDataInizio));
            } if (localDataFineTracker){
                elementList.add(new javax.xml.namespace.QName("http://webservice.demone.conservazione",
                        "dataFine"));

                elementList.add(localDataFine==null?null:
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDataFine));
            } if (localEsitoTracker){
                elementList.add(new javax.xml.namespace.QName("http://webservice.demone.conservazione",
                        "esito"));


                elementList.add(localEsito==null?null:
                        localEsito);
            } if (localDocIdTracker){
                elementList.add(new javax.xml.namespace.QName("http://webservice.demone.conservazione",
                        "docId"));

                elementList.add(localDocId==null?null:
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDocId));
            } if (localDocTypeTracker){
                elementList.add(new javax.xml.namespace.QName("http://webservice.demone.conservazione",
                        "docType"));

                elementList.add(localDocType==null?null:
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDocType));
            } if (localErrCodeTracker){
                elementList.add(new javax.xml.namespace.QName("http://webservice.demone.conservazione",
                        "errCode"));

                elementList.add(localErrCode==null?null:
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localErrCode));
            } if (localEnteTracker){
                elementList.add(new javax.xml.namespace.QName("http://webservice.demone.conservazione",
                        "ente"));

                elementList.add(localEnte==null?null:
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localEnte));
            } if (localAooTracker){
                elementList.add(new javax.xml.namespace.QName("http://webservice.demone.conservazione",
                        "aoo"));

                elementList.add(localAoo==null?null:
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localAoo));
            } if (localMaxRowsTracker){
                elementList.add(new javax.xml.namespace.QName("http://webservice.demone.conservazione",
                        "maxRows"));

                elementList.add(
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localMaxRows));
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
            public static SearchJob parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{
                SearchJob object =
                        new SearchJob();

                int event;
                java.lang.String nillableValue = null;
                java.lang.String prefix ="";
                java.lang.String namespaceuri ="";
                try {

                    while (!reader.isStartElement() && !reader.isEndElement())
                        reader.next();


                    if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","type")!=null){
                        java.lang.String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                                "type");
                        if (fullTypeName!=null){
                            java.lang.String nsPrefix = null;
                            if (fullTypeName.indexOf(":") > -1){
                                nsPrefix = fullTypeName.substring(0,fullTypeName.indexOf(":"));
                            }
                            nsPrefix = nsPrefix==null?"":nsPrefix;

                            java.lang.String type = fullTypeName.substring(fullTypeName.indexOf(":")+1);

                            if (!"searchJob".equals(type)){
                                //find namespace for the prefix
                                java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (SearchJob)ExtensionMapper.getTypeObject(
                                        nsUri,type,reader);
                            }


                        }


                    }




                    // Note all attributes that were handled. Used to differ normal attributes
                    // from anyAttributes.
                    java.util.Vector handledAttributes = new java.util.Vector();




                    reader.next();


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://webservice.demone.conservazione","dataInizio").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                            java.lang.String content = reader.getElementText();

                            object.setDataInizio(
                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToDateTime(content));

                        } else {


                            reader.getElementText(); // throw away text nodes if any.
                        }

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                    }


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://webservice.demone.conservazione","dataFine").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                            java.lang.String content = reader.getElementText();

                            object.setDataFine(
                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToDateTime(content));

                        } else {


                            reader.getElementText(); // throw away text nodes if any.
                        }

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                    }


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://webservice.demone.conservazione","esito").equals(reader.getName())){

                        object.setEsito(org.apache.axis2.databinding.utils.ConverterUtil.getAnyTypeObject(reader,
                                ExtensionMapper.class));

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                    }


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://webservice.demone.conservazione","docId").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                            java.lang.String content = reader.getElementText();

                            object.setDocId(
                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                        } else {


                            reader.getElementText(); // throw away text nodes if any.
                        }

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                    }


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://webservice.demone.conservazione","docType").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                            java.lang.String content = reader.getElementText();

                            object.setDocType(
                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                        } else {


                            reader.getElementText(); // throw away text nodes if any.
                        }

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                    }


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://webservice.demone.conservazione","errCode").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                            java.lang.String content = reader.getElementText();

                            object.setErrCode(
                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                        } else {


                            reader.getElementText(); // throw away text nodes if any.
                        }

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                    }


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://webservice.demone.conservazione","ente").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                            java.lang.String content = reader.getElementText();

                            object.setEnte(
                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                        } else {


                            reader.getElementText(); // throw away text nodes if any.
                        }

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                    }


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://webservice.demone.conservazione","aoo").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                            java.lang.String content = reader.getElementText();

                            object.setAoo(
                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                        } else {


                            reader.getElementText(); // throw away text nodes if any.
                        }

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                    }


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://webservice.demone.conservazione","maxRows").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                            throw new org.apache.axis2.databinding.ADBException("The element: "+"maxRows" +"  cannot be null");
                        }


                        java.lang.String content = reader.getElementText();

                        object.setMaxRows(
                                org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                        object.setMaxRows(java.lang.Integer.MIN_VALUE);

                    }

                    while (!reader.isStartElement() && !reader.isEndElement())
                        reader.next();

                    if (reader.isStartElement())
                        // A start element we are not expecting indicates a trailing invalid property
                        throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());




                } catch (javax.xml.stream.XMLStreamException e) {
                    throw new java.lang.Exception(e);
                }

                return object;
            }

        }//end of factory class



    }


    public static class GetJobGsonResponse
            implements org.apache.axis2.databinding.ADBBean{

        public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName(
                "http://webservice.demone.conservazione",
                "getJobGsonResponse",
                "ns3");



        /**
         * field for _return
         */


        protected java.lang.String local_return ;

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
        public  java.lang.String get_return(){
            return local_return;
        }



        /**
         * Auto generated setter method
         * @param param _return
         */
        public void set_return(java.lang.String param){
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




            java.lang.String prefix = null;
            java.lang.String namespace = null;


            prefix = parentQName.getPrefix();
            namespace = parentQName.getNamespaceURI();
            writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

            if (serializeType){


                java.lang.String namespacePrefix = registerPrefix(xmlWriter,"http://webservice.demone.conservazione");
                if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            namespacePrefix+":getJobGsonResponse",
                            xmlWriter);
                } else {
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            "getJobGsonResponse",
                            xmlWriter);
                }


            }
            if (local_returnTracker){
                namespace = "http://webservice.demone.conservazione";
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

        private static java.lang.String generatePrefix(java.lang.String namespace) {
            if(namespace.equals("http://webservice.demone.conservazione")){
                return "ns3";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
         * Utility method to write an element start tag.
         */
        private void writeStartElement(java.lang.String prefix, java.lang.String namespace, java.lang.String localPart,
                                       javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String writerPrefix = xmlWriter.getPrefix(namespace);
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
        private void writeAttribute(java.lang.String prefix,java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (xmlWriter.getPrefix(namespace) == null) {
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            xmlWriter.writeAttribute(namespace,attName,attValue);
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeAttribute(java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
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
        private void writeQNameAttribute(java.lang.String namespace, java.lang.String attName,
                                         javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            java.lang.String attributeNamespace = qname.getNamespaceURI();
            java.lang.String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
            if (attributePrefix == null) {
                attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
            }
            java.lang.String attributeValue;
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
            java.lang.String namespaceURI = qname.getNamespaceURI();
            if (namespaceURI != null) {
                java.lang.String prefix = xmlWriter.getPrefix(namespaceURI);
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
                java.lang.StringBuffer stringToWrite = new java.lang.StringBuffer();
                java.lang.String namespaceURI = null;
                java.lang.String prefix = null;

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
        private java.lang.String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, java.lang.String namespace) throws javax.xml.stream.XMLStreamException {
            java.lang.String prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null) {
                prefix = generatePrefix(namespace);
                javax.xml.namespace.NamespaceContext nsContext = xmlWriter.getNamespaceContext();
                while (true) {
                    java.lang.String uri = nsContext.getNamespaceURI(prefix);
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
                elementList.add(new javax.xml.namespace.QName("http://webservice.demone.conservazione",
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
            public static GetJobGsonResponse parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{
                GetJobGsonResponse object =
                        new GetJobGsonResponse();

                int event;
                java.lang.String nillableValue = null;
                java.lang.String prefix ="";
                java.lang.String namespaceuri ="";
                try {

                    while (!reader.isStartElement() && !reader.isEndElement())
                        reader.next();


                    if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","type")!=null){
                        java.lang.String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                                "type");
                        if (fullTypeName!=null){
                            java.lang.String nsPrefix = null;
                            if (fullTypeName.indexOf(":") > -1){
                                nsPrefix = fullTypeName.substring(0,fullTypeName.indexOf(":"));
                            }
                            nsPrefix = nsPrefix==null?"":nsPrefix;

                            java.lang.String type = fullTypeName.substring(fullTypeName.indexOf(":")+1);

                            if (!"getJobGsonResponse".equals(type)){
                                //find namespace for the prefix
                                java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (GetJobGsonResponse)ExtensionMapper.getTypeObject(
                                        nsUri,type,reader);
                            }


                        }


                    }




                    // Note all attributes that were handled. Used to differ normal attributes
                    // from anyAttributes.
                    java.util.Vector handledAttributes = new java.util.Vector();




                    reader.next();


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://webservice.demone.conservazione","return").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                            java.lang.String content = reader.getElementText();

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
                    throw new java.lang.Exception(e);
                }

                return object;
            }

        }//end of factory class



    }


    public static class UpdateJobResponse
            implements org.apache.axis2.databinding.ADBBean{

        public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName(
                "http://webservice.demone.conservazione",
                "updateJobResponse",
                "ns3");



        /**
         * field for _return
         */


        protected boolean local_return ;

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
         * @return boolean
         */
        public  boolean get_return(){
            return local_return;
        }



        /**
         * Auto generated setter method
         * @param param _return
         */
        public void set_return(boolean param){

            // setting primitive attribute tracker to true
            local_returnTracker =
                    true;

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




            java.lang.String prefix = null;
            java.lang.String namespace = null;


            prefix = parentQName.getPrefix();
            namespace = parentQName.getNamespaceURI();
            writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

            if (serializeType){


                java.lang.String namespacePrefix = registerPrefix(xmlWriter,"http://webservice.demone.conservazione");
                if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            namespacePrefix+":updateJobResponse",
                            xmlWriter);
                } else {
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            "updateJobResponse",
                            xmlWriter);
                }


            }
            if (local_returnTracker){
                namespace = "http://webservice.demone.conservazione";
                writeStartElement(null, namespace, "return", xmlWriter);

                if (false) {

                    throw new org.apache.axis2.databinding.ADBException("return cannot be null!!");

                } else {
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(local_return));
                }

                xmlWriter.writeEndElement();
            }
            xmlWriter.writeEndElement();


        }

        private static java.lang.String generatePrefix(java.lang.String namespace) {
            if(namespace.equals("http://webservice.demone.conservazione")){
                return "ns3";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
         * Utility method to write an element start tag.
         */
        private void writeStartElement(java.lang.String prefix, java.lang.String namespace, java.lang.String localPart,
                                       javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String writerPrefix = xmlWriter.getPrefix(namespace);
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
        private void writeAttribute(java.lang.String prefix,java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (xmlWriter.getPrefix(namespace) == null) {
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            xmlWriter.writeAttribute(namespace,attName,attValue);
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeAttribute(java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
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
        private void writeQNameAttribute(java.lang.String namespace, java.lang.String attName,
                                         javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            java.lang.String attributeNamespace = qname.getNamespaceURI();
            java.lang.String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
            if (attributePrefix == null) {
                attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
            }
            java.lang.String attributeValue;
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
            java.lang.String namespaceURI = qname.getNamespaceURI();
            if (namespaceURI != null) {
                java.lang.String prefix = xmlWriter.getPrefix(namespaceURI);
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
                java.lang.StringBuffer stringToWrite = new java.lang.StringBuffer();
                java.lang.String namespaceURI = null;
                java.lang.String prefix = null;

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
        private java.lang.String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, java.lang.String namespace) throws javax.xml.stream.XMLStreamException {
            java.lang.String prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null) {
                prefix = generatePrefix(namespace);
                javax.xml.namespace.NamespaceContext nsContext = xmlWriter.getNamespaceContext();
                while (true) {
                    java.lang.String uri = nsContext.getNamespaceURI(prefix);
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
                elementList.add(new javax.xml.namespace.QName("http://webservice.demone.conservazione",
                        "return"));

                elementList.add(
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
            public static UpdateJobResponse parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{
                UpdateJobResponse object =
                        new UpdateJobResponse();

                int event;
                java.lang.String nillableValue = null;
                java.lang.String prefix ="";
                java.lang.String namespaceuri ="";
                try {

                    while (!reader.isStartElement() && !reader.isEndElement())
                        reader.next();


                    if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","type")!=null){
                        java.lang.String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                                "type");
                        if (fullTypeName!=null){
                            java.lang.String nsPrefix = null;
                            if (fullTypeName.indexOf(":") > -1){
                                nsPrefix = fullTypeName.substring(0,fullTypeName.indexOf(":"));
                            }
                            nsPrefix = nsPrefix==null?"":nsPrefix;

                            java.lang.String type = fullTypeName.substring(fullTypeName.indexOf(":")+1);

                            if (!"updateJobResponse".equals(type)){
                                //find namespace for the prefix
                                java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (UpdateJobResponse)ExtensionMapper.getTypeObject(
                                        nsUri,type,reader);
                            }


                        }


                    }




                    // Note all attributes that were handled. Used to differ normal attributes
                    // from anyAttributes.
                    java.util.Vector handledAttributes = new java.util.Vector();




                    reader.next();


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://webservice.demone.conservazione","return").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                            throw new org.apache.axis2.databinding.ADBException("The element: "+"return" +"  cannot be null");
                        }


                        java.lang.String content = reader.getElementText();

                        object.set_return(
                                org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));

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
                    throw new java.lang.Exception(e);
                }

                return object;
            }

        }//end of factory class



    }


    public static class ReadLogsByIdDocResponse
            implements org.apache.axis2.databinding.ADBBean{

        public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName(
                "http://webservice.demone.conservazione",
                "readLogsByIdDocResponse",
                "ns3");



        /**
         * field for _return
         * This was an Array!
         */


        protected Log[] local_return ;

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
         * @return Log[]
         */
        public  Log[] get_return(){
            return local_return;
        }






        /**
         * validate the array for _return
         */
        protected void validate_return(Log[] param){

        }


        /**
         * Auto generated setter method
         * @param param _return
         */
        public void set_return(Log[] param){

            validate_return(param);

            local_returnTracker = true;

            this.local_return=param;
        }



        /**
         * Auto generated add method for the array for convenience
         * @param param Log
         */
        public void add_return(Log param){
            if (local_return == null){
                local_return = new Log[]{};
            }


            //update the setting tracker
            local_returnTracker = true;


            java.util.List list =
                    org.apache.axis2.databinding.utils.ConverterUtil.toList(local_return);
            list.add(param);
            this.local_return =
                    (Log[])list.toArray(
                            new Log[list.size()]);

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




            java.lang.String prefix = null;
            java.lang.String namespace = null;


            prefix = parentQName.getPrefix();
            namespace = parentQName.getNamespaceURI();
            writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

            if (serializeType){


                java.lang.String namespacePrefix = registerPrefix(xmlWriter,"http://webservice.demone.conservazione");
                if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            namespacePrefix+":readLogsByIdDocResponse",
                            xmlWriter);
                } else {
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            "readLogsByIdDocResponse",
                            xmlWriter);
                }


            }
            if (local_returnTracker){
                if (local_return!=null){
                    for (int i = 0;i < local_return.length;i++){
                        if (local_return[i] != null){
                            local_return[i].serialize(new javax.xml.namespace.QName("http://webservice.demone.conservazione","return"),
                                    xmlWriter);
                        } else {

                            writeStartElement(null, "http://webservice.demone.conservazione", "return", xmlWriter);

                            // write the nil attribute
                            writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                            xmlWriter.writeEndElement();

                        }

                    }
                } else {

                    writeStartElement(null, "http://webservice.demone.conservazione", "return", xmlWriter);

                    // write the nil attribute
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                    xmlWriter.writeEndElement();

                }
            }
            xmlWriter.writeEndElement();


        }

        private static java.lang.String generatePrefix(java.lang.String namespace) {
            if(namespace.equals("http://webservice.demone.conservazione")){
                return "ns3";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
         * Utility method to write an element start tag.
         */
        private void writeStartElement(java.lang.String prefix, java.lang.String namespace, java.lang.String localPart,
                                       javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String writerPrefix = xmlWriter.getPrefix(namespace);
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
        private void writeAttribute(java.lang.String prefix,java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (xmlWriter.getPrefix(namespace) == null) {
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            xmlWriter.writeAttribute(namespace,attName,attValue);
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeAttribute(java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
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
        private void writeQNameAttribute(java.lang.String namespace, java.lang.String attName,
                                         javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            java.lang.String attributeNamespace = qname.getNamespaceURI();
            java.lang.String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
            if (attributePrefix == null) {
                attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
            }
            java.lang.String attributeValue;
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
            java.lang.String namespaceURI = qname.getNamespaceURI();
            if (namespaceURI != null) {
                java.lang.String prefix = xmlWriter.getPrefix(namespaceURI);
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
                java.lang.StringBuffer stringToWrite = new java.lang.StringBuffer();
                java.lang.String namespaceURI = null;
                java.lang.String prefix = null;

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
        private java.lang.String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, java.lang.String namespace) throws javax.xml.stream.XMLStreamException {
            java.lang.String prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null) {
                prefix = generatePrefix(namespace);
                javax.xml.namespace.NamespaceContext nsContext = xmlWriter.getNamespaceContext();
                while (true) {
                    java.lang.String uri = nsContext.getNamespaceURI(prefix);
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
                if (local_return!=null) {
                    for (int i = 0;i < local_return.length;i++){

                        if (local_return[i] != null){
                            elementList.add(new javax.xml.namespace.QName("http://webservice.demone.conservazione",
                                    "return"));
                            elementList.add(local_return[i]);
                        } else {

                            elementList.add(new javax.xml.namespace.QName("http://webservice.demone.conservazione",
                                    "return"));
                            elementList.add(null);

                        }

                    }
                } else {

                    elementList.add(new javax.xml.namespace.QName("http://webservice.demone.conservazione",
                            "return"));
                    elementList.add(local_return);

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
            public static ReadLogsByIdDocResponse parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{
                ReadLogsByIdDocResponse object =
                        new ReadLogsByIdDocResponse();

                int event;
                java.lang.String nillableValue = null;
                java.lang.String prefix ="";
                java.lang.String namespaceuri ="";
                try {

                    while (!reader.isStartElement() && !reader.isEndElement())
                        reader.next();


                    if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","type")!=null){
                        java.lang.String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                                "type");
                        if (fullTypeName!=null){
                            java.lang.String nsPrefix = null;
                            if (fullTypeName.indexOf(":") > -1){
                                nsPrefix = fullTypeName.substring(0,fullTypeName.indexOf(":"));
                            }
                            nsPrefix = nsPrefix==null?"":nsPrefix;

                            java.lang.String type = fullTypeName.substring(fullTypeName.indexOf(":")+1);

                            if (!"readLogsByIdDocResponse".equals(type)){
                                //find namespace for the prefix
                                java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (ReadLogsByIdDocResponse)ExtensionMapper.getTypeObject(
                                        nsUri,type,reader);
                            }


                        }


                    }




                    // Note all attributes that were handled. Used to differ normal attributes
                    // from anyAttributes.
                    java.util.Vector handledAttributes = new java.util.Vector();




                    reader.next();

                    java.util.ArrayList list1 = new java.util.ArrayList();


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://webservice.demone.conservazione","return").equals(reader.getName())){



                        // Process the array and step past its final element's end.

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                            list1.add(null);
                            reader.next();
                        } else {
                            list1.add(Log.Factory.parse(reader));
                        }
                        //loop until we find a start element that is not part of this array
                        boolean loopDone1 = false;
                        while(!loopDone1){
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
                                loopDone1 = true;
                            } else {
                                if (new javax.xml.namespace.QName("http://webservice.demone.conservazione","return").equals(reader.getName())){

                                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                    if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                        list1.add(null);
                                        reader.next();
                                    } else {
                                        list1.add(Log.Factory.parse(reader));
                                    }
                                }else{
                                    loopDone1 = true;
                                }
                            }
                        }
                        // call the converter utility  to convert and set the array

                        object.set_return((Log[])
                                org.apache.axis2.databinding.utils.ConverterUtil.convertToArray(
                                        Log.class,
                                        list1));

                    }  // End of if for expected property start element

                    else {

                    }

                    while (!reader.isStartElement() && !reader.isEndElement())
                        reader.next();

                    if (reader.isStartElement())
                        // A start element we are not expecting indicates a trailing invalid property
                        throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());




                } catch (javax.xml.stream.XMLStreamException e) {
                    throw new java.lang.Exception(e);
                }

                return object;
            }

        }//end of factory class



    }


    public static class DeleteJob_byIdJob
            implements org.apache.axis2.databinding.ADBBean{

        public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName(
                "http://webservice.demone.conservazione",
                "deleteJob_byIdJob",
                "ns3");



        /**
         * field for Id_job
         */


        protected long localId_job ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localId_jobTracker = false ;

        public boolean isId_jobSpecified(){
            return localId_jobTracker;
        }



        /**
         * Auto generated getter method
         * @return long
         */
        public  long getId_job(){
            return localId_job;
        }



        /**
         * Auto generated setter method
         * @param param Id_job
         */
        public void setId_job(long param){

            // setting primitive attribute tracker to true
            localId_jobTracker =
                    param != java.lang.Long.MIN_VALUE;

            this.localId_job=param;


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




            java.lang.String prefix = null;
            java.lang.String namespace = null;


            prefix = parentQName.getPrefix();
            namespace = parentQName.getNamespaceURI();
            writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

            if (serializeType){


                java.lang.String namespacePrefix = registerPrefix(xmlWriter,"http://webservice.demone.conservazione");
                if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            namespacePrefix+":deleteJob_byIdJob",
                            xmlWriter);
                } else {
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            "deleteJob_byIdJob",
                            xmlWriter);
                }


            }
            if (localId_jobTracker){
                namespace = "http://webservice.demone.conservazione";
                writeStartElement(null, namespace, "id_job", xmlWriter);

                if (localId_job==java.lang.Long.MIN_VALUE) {

                    throw new org.apache.axis2.databinding.ADBException("id_job cannot be null!!");

                } else {
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localId_job));
                }

                xmlWriter.writeEndElement();
            }
            xmlWriter.writeEndElement();


        }

        private static java.lang.String generatePrefix(java.lang.String namespace) {
            if(namespace.equals("http://webservice.demone.conservazione")){
                return "ns3";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
         * Utility method to write an element start tag.
         */
        private void writeStartElement(java.lang.String prefix, java.lang.String namespace, java.lang.String localPart,
                                       javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String writerPrefix = xmlWriter.getPrefix(namespace);
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
        private void writeAttribute(java.lang.String prefix,java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (xmlWriter.getPrefix(namespace) == null) {
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            xmlWriter.writeAttribute(namespace,attName,attValue);
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeAttribute(java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
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
        private void writeQNameAttribute(java.lang.String namespace, java.lang.String attName,
                                         javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            java.lang.String attributeNamespace = qname.getNamespaceURI();
            java.lang.String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
            if (attributePrefix == null) {
                attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
            }
            java.lang.String attributeValue;
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
            java.lang.String namespaceURI = qname.getNamespaceURI();
            if (namespaceURI != null) {
                java.lang.String prefix = xmlWriter.getPrefix(namespaceURI);
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
                java.lang.StringBuffer stringToWrite = new java.lang.StringBuffer();
                java.lang.String namespaceURI = null;
                java.lang.String prefix = null;

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
        private java.lang.String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, java.lang.String namespace) throws javax.xml.stream.XMLStreamException {
            java.lang.String prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null) {
                prefix = generatePrefix(namespace);
                javax.xml.namespace.NamespaceContext nsContext = xmlWriter.getNamespaceContext();
                while (true) {
                    java.lang.String uri = nsContext.getNamespaceURI(prefix);
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

            if (localId_jobTracker){
                elementList.add(new javax.xml.namespace.QName("http://webservice.demone.conservazione",
                        "id_job"));

                elementList.add(
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localId_job));
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
            public static DeleteJob_byIdJob parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{
                DeleteJob_byIdJob object =
                        new DeleteJob_byIdJob();

                int event;
                java.lang.String nillableValue = null;
                java.lang.String prefix ="";
                java.lang.String namespaceuri ="";
                try {

                    while (!reader.isStartElement() && !reader.isEndElement())
                        reader.next();


                    if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","type")!=null){
                        java.lang.String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                                "type");
                        if (fullTypeName!=null){
                            java.lang.String nsPrefix = null;
                            if (fullTypeName.indexOf(":") > -1){
                                nsPrefix = fullTypeName.substring(0,fullTypeName.indexOf(":"));
                            }
                            nsPrefix = nsPrefix==null?"":nsPrefix;

                            java.lang.String type = fullTypeName.substring(fullTypeName.indexOf(":")+1);

                            if (!"deleteJob_byIdJob".equals(type)){
                                //find namespace for the prefix
                                java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (DeleteJob_byIdJob)ExtensionMapper.getTypeObject(
                                        nsUri,type,reader);
                            }


                        }


                    }




                    // Note all attributes that were handled. Used to differ normal attributes
                    // from anyAttributes.
                    java.util.Vector handledAttributes = new java.util.Vector();




                    reader.next();


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://webservice.demone.conservazione","id_job").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                            throw new org.apache.axis2.databinding.ADBException("The element: "+"id_job" +"  cannot be null");
                        }


                        java.lang.String content = reader.getElementText();

                        object.setId_job(
                                org.apache.axis2.databinding.utils.ConverterUtil.convertToLong(content));

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                        object.setId_job(java.lang.Long.MIN_VALUE);

                    }

                    while (!reader.isStartElement() && !reader.isEndElement())
                        reader.next();

                    if (reader.isStartElement())
                        // A start element we are not expecting indicates a trailing invalid property
                        throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());




                } catch (javax.xml.stream.XMLStreamException e) {
                    throw new java.lang.Exception(e);
                }

                return object;
            }

        }//end of factory class



    }


    public static class ExtensionMapper{

        public static java.lang.Object getTypeObject(java.lang.String namespaceURI,
                                                     java.lang.String typeName,
                                                     javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{


            if (
                    "http://database.daemon.parer.docer.kdm.it/xsd".equals(namespaceURI) &&
                            "Job".equals(typeName)){

                return  Job.Factory.parse(reader);


            }


            if (
                    "http://webservice.demone.conservazione".equals(namespaceURI) &&
                            "Exception".equals(typeName)){

                return  Exception.Factory.parse(reader);


            }


            if (
                    "http://database.daemon.parer.docer.kdm.it/xsd".equals(namespaceURI) &&
                            "Log".equals(typeName)){

                return  Log.Factory.parse(reader);


            }


            if (
                    "http://classes.sdk.docer.kdm.it/xsd".equals(namespaceURI) &&
                            "KeyValuePair".equals(typeName)){

                return  KeyValuePair.Factory.parse(reader);


            }


            throw new org.apache.axis2.databinding.ADBException("Unsupported type " + namespaceURI + " " + typeName);
        }

    }

    public static class GetJobGson
            implements org.apache.axis2.databinding.ADBBean{

        public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName(
                "http://webservice.demone.conservazione",
                "getJobGson",
                "ns3");



        /**
         * field for Id_job
         */


        protected java.lang.String localId_job ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localId_jobTracker = false ;

        public boolean isId_jobSpecified(){
            return localId_jobTracker;
        }



        /**
         * Auto generated getter method
         * @return java.lang.String
         */
        public  java.lang.String getId_job(){
            return localId_job;
        }



        /**
         * Auto generated setter method
         * @param param Id_job
         */
        public void setId_job(java.lang.String param){
            localId_jobTracker = true;

            this.localId_job=param;


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




            java.lang.String prefix = null;
            java.lang.String namespace = null;


            prefix = parentQName.getPrefix();
            namespace = parentQName.getNamespaceURI();
            writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

            if (serializeType){


                java.lang.String namespacePrefix = registerPrefix(xmlWriter,"http://webservice.demone.conservazione");
                if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            namespacePrefix+":getJobGson",
                            xmlWriter);
                } else {
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            "getJobGson",
                            xmlWriter);
                }


            }
            if (localId_jobTracker){
                namespace = "http://webservice.demone.conservazione";
                writeStartElement(null, namespace, "id_job", xmlWriter);


                if (localId_job==null){
                    // write the nil attribute

                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                }else{


                    xmlWriter.writeCharacters(localId_job);

                }

                xmlWriter.writeEndElement();
            }
            xmlWriter.writeEndElement();


        }

        private static java.lang.String generatePrefix(java.lang.String namespace) {
            if(namespace.equals("http://webservice.demone.conservazione")){
                return "ns3";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
         * Utility method to write an element start tag.
         */
        private void writeStartElement(java.lang.String prefix, java.lang.String namespace, java.lang.String localPart,
                                       javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String writerPrefix = xmlWriter.getPrefix(namespace);
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
        private void writeAttribute(java.lang.String prefix,java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (xmlWriter.getPrefix(namespace) == null) {
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            xmlWriter.writeAttribute(namespace,attName,attValue);
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeAttribute(java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
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
        private void writeQNameAttribute(java.lang.String namespace, java.lang.String attName,
                                         javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            java.lang.String attributeNamespace = qname.getNamespaceURI();
            java.lang.String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
            if (attributePrefix == null) {
                attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
            }
            java.lang.String attributeValue;
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
            java.lang.String namespaceURI = qname.getNamespaceURI();
            if (namespaceURI != null) {
                java.lang.String prefix = xmlWriter.getPrefix(namespaceURI);
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
                java.lang.StringBuffer stringToWrite = new java.lang.StringBuffer();
                java.lang.String namespaceURI = null;
                java.lang.String prefix = null;

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
        private java.lang.String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, java.lang.String namespace) throws javax.xml.stream.XMLStreamException {
            java.lang.String prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null) {
                prefix = generatePrefix(namespace);
                javax.xml.namespace.NamespaceContext nsContext = xmlWriter.getNamespaceContext();
                while (true) {
                    java.lang.String uri = nsContext.getNamespaceURI(prefix);
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

            if (localId_jobTracker){
                elementList.add(new javax.xml.namespace.QName("http://webservice.demone.conservazione",
                        "id_job"));

                elementList.add(localId_job==null?null:
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localId_job));
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
            public static GetJobGson parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{
                GetJobGson object =
                        new GetJobGson();

                int event;
                java.lang.String nillableValue = null;
                java.lang.String prefix ="";
                java.lang.String namespaceuri ="";
                try {

                    while (!reader.isStartElement() && !reader.isEndElement())
                        reader.next();


                    if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","type")!=null){
                        java.lang.String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                                "type");
                        if (fullTypeName!=null){
                            java.lang.String nsPrefix = null;
                            if (fullTypeName.indexOf(":") > -1){
                                nsPrefix = fullTypeName.substring(0,fullTypeName.indexOf(":"));
                            }
                            nsPrefix = nsPrefix==null?"":nsPrefix;

                            java.lang.String type = fullTypeName.substring(fullTypeName.indexOf(":")+1);

                            if (!"getJobGson".equals(type)){
                                //find namespace for the prefix
                                java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (GetJobGson)ExtensionMapper.getTypeObject(
                                        nsUri,type,reader);
                            }


                        }


                    }




                    // Note all attributes that were handled. Used to differ normal attributes
                    // from anyAttributes.
                    java.util.Vector handledAttributes = new java.util.Vector();




                    reader.next();


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://webservice.demone.conservazione","id_job").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                            java.lang.String content = reader.getElementText();

                            object.setId_job(
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
                    throw new java.lang.Exception(e);
                }

                return object;
            }

        }//end of factory class



    }


    public static class ReadLogGson_byIdJob
            implements org.apache.axis2.databinding.ADBBean{

        public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName(
                "http://webservice.demone.conservazione",
                "readLogGson_byIdJob",
                "ns3");



        /**
         * field for Id_job
         */


        protected java.lang.String localId_job ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localId_jobTracker = false ;

        public boolean isId_jobSpecified(){
            return localId_jobTracker;
        }



        /**
         * Auto generated getter method
         * @return java.lang.String
         */
        public  java.lang.String getId_job(){
            return localId_job;
        }



        /**
         * Auto generated setter method
         * @param param Id_job
         */
        public void setId_job(java.lang.String param){
            localId_jobTracker = true;

            this.localId_job=param;


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




            java.lang.String prefix = null;
            java.lang.String namespace = null;


            prefix = parentQName.getPrefix();
            namespace = parentQName.getNamespaceURI();
            writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

            if (serializeType){


                java.lang.String namespacePrefix = registerPrefix(xmlWriter,"http://webservice.demone.conservazione");
                if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            namespacePrefix+":readLogGson_byIdJob",
                            xmlWriter);
                } else {
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            "readLogGson_byIdJob",
                            xmlWriter);
                }


            }
            if (localId_jobTracker){
                namespace = "http://webservice.demone.conservazione";
                writeStartElement(null, namespace, "id_job", xmlWriter);


                if (localId_job==null){
                    // write the nil attribute

                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                }else{


                    xmlWriter.writeCharacters(localId_job);

                }

                xmlWriter.writeEndElement();
            }
            xmlWriter.writeEndElement();


        }

        private static java.lang.String generatePrefix(java.lang.String namespace) {
            if(namespace.equals("http://webservice.demone.conservazione")){
                return "ns3";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
         * Utility method to write an element start tag.
         */
        private void writeStartElement(java.lang.String prefix, java.lang.String namespace, java.lang.String localPart,
                                       javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String writerPrefix = xmlWriter.getPrefix(namespace);
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
        private void writeAttribute(java.lang.String prefix,java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (xmlWriter.getPrefix(namespace) == null) {
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            xmlWriter.writeAttribute(namespace,attName,attValue);
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeAttribute(java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
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
        private void writeQNameAttribute(java.lang.String namespace, java.lang.String attName,
                                         javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            java.lang.String attributeNamespace = qname.getNamespaceURI();
            java.lang.String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
            if (attributePrefix == null) {
                attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
            }
            java.lang.String attributeValue;
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
            java.lang.String namespaceURI = qname.getNamespaceURI();
            if (namespaceURI != null) {
                java.lang.String prefix = xmlWriter.getPrefix(namespaceURI);
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
                java.lang.StringBuffer stringToWrite = new java.lang.StringBuffer();
                java.lang.String namespaceURI = null;
                java.lang.String prefix = null;

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
        private java.lang.String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, java.lang.String namespace) throws javax.xml.stream.XMLStreamException {
            java.lang.String prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null) {
                prefix = generatePrefix(namespace);
                javax.xml.namespace.NamespaceContext nsContext = xmlWriter.getNamespaceContext();
                while (true) {
                    java.lang.String uri = nsContext.getNamespaceURI(prefix);
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

            if (localId_jobTracker){
                elementList.add(new javax.xml.namespace.QName("http://webservice.demone.conservazione",
                        "id_job"));

                elementList.add(localId_job==null?null:
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localId_job));
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
            public static ReadLogGson_byIdJob parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{
                ReadLogGson_byIdJob object =
                        new ReadLogGson_byIdJob();

                int event;
                java.lang.String nillableValue = null;
                java.lang.String prefix ="";
                java.lang.String namespaceuri ="";
                try {

                    while (!reader.isStartElement() && !reader.isEndElement())
                        reader.next();


                    if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","type")!=null){
                        java.lang.String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                                "type");
                        if (fullTypeName!=null){
                            java.lang.String nsPrefix = null;
                            if (fullTypeName.indexOf(":") > -1){
                                nsPrefix = fullTypeName.substring(0,fullTypeName.indexOf(":"));
                            }
                            nsPrefix = nsPrefix==null?"":nsPrefix;

                            java.lang.String type = fullTypeName.substring(fullTypeName.indexOf(":")+1);

                            if (!"readLogGson_byIdJob".equals(type)){
                                //find namespace for the prefix
                                java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (ReadLogGson_byIdJob)ExtensionMapper.getTypeObject(
                                        nsUri,type,reader);
                            }


                        }


                    }




                    // Note all attributes that were handled. Used to differ normal attributes
                    // from anyAttributes.
                    java.util.Vector handledAttributes = new java.util.Vector();




                    reader.next();


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://webservice.demone.conservazione","id_job").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                            java.lang.String content = reader.getElementText();

                            object.setId_job(
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
                    throw new java.lang.Exception(e);
                }

                return object;
            }

        }//end of factory class



    }


    public static class Job
            implements org.apache.axis2.databinding.ADBBean{
        /* This type was generated from the piece of schema that had
                name = Job
                Namespace URI = http://database.daemon.parer.docer.kdm.it/xsd
                Namespace Prefix = ns1
                */


        /**
         * field for AppChiamante
         */


        protected java.lang.String localAppChiamante ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localAppChiamanteTracker = false ;

        public boolean isAppChiamanteSpecified(){
            return localAppChiamanteTracker;
        }



        /**
         * Auto generated getter method
         * @return java.lang.String
         */
        public  java.lang.String getAppChiamante(){
            return localAppChiamante;
        }



        /**
         * Auto generated setter method
         * @param param AppChiamante
         */
        public void setAppChiamante(java.lang.String param){
            localAppChiamanteTracker = true;

            this.localAppChiamante=param;


        }


        /**
         * field for Azione
         */


        protected java.lang.String localAzione ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localAzioneTracker = false ;

        public boolean isAzioneSpecified(){
            return localAzioneTracker;
        }



        /**
         * Auto generated getter method
         * @return java.lang.String
         */
        public  java.lang.String getAzione(){
            return localAzione;
        }



        /**
         * Auto generated setter method
         * @param param Azione
         */
        public void setAzione(java.lang.String param){
            localAzioneTracker = param != null;

            this.localAzione=param;


        }


        /**
         * field for ChiaveDoc
         */


        protected java.lang.String localChiaveDoc ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localChiaveDocTracker = false ;

        public boolean isChiaveDocSpecified(){
            return localChiaveDocTracker;
        }



        /**
         * Auto generated getter method
         * @return java.lang.String
         */
        public  java.lang.String getChiaveDoc(){
            return localChiaveDoc;
        }



        /**
         * Auto generated setter method
         * @param param ChiaveDoc
         */
        public void setChiaveDoc(java.lang.String param){
            localChiaveDocTracker = true;

            this.localChiaveDoc=param;


        }


        /**
         * field for CodAoo
         */


        protected java.lang.String localCodAoo ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localCodAooTracker = false ;

        public boolean isCodAooSpecified(){
            return localCodAooTracker;
        }



        /**
         * Auto generated getter method
         * @return java.lang.String
         */
        public  java.lang.String getCodAoo(){
            return localCodAoo;
        }



        /**
         * Auto generated setter method
         * @param param CodAoo
         */
        public void setCodAoo(java.lang.String param){
            localCodAooTracker = true;

            this.localCodAoo=param;


        }


        /**
         * field for CodEnte
         */


        protected java.lang.String localCodEnte ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localCodEnteTracker = false ;

        public boolean isCodEnteSpecified(){
            return localCodEnteTracker;
        }



        /**
         * Auto generated getter method
         * @return java.lang.String
         */
        public  java.lang.String getCodEnte(){
            return localCodEnte;
        }



        /**
         * Auto generated setter method
         * @param param CodEnte
         */
        public void setCodEnte(java.lang.String param){
            localCodEnteTracker = true;

            this.localCodEnte=param;


        }


        /**
         * field for DataChiamata
         */


        protected java.util.Calendar localDataChiamata ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localDataChiamataTracker = false ;

        public boolean isDataChiamataSpecified(){
            return localDataChiamataTracker;
        }



        /**
         * Auto generated getter method
         * @return java.util.Calendar
         */
        public  java.util.Calendar getDataChiamata(){
            return localDataChiamata;
        }



        /**
         * Auto generated setter method
         * @param param DataChiamata
         */
        public void setDataChiamata(java.util.Calendar param){
            localDataChiamataTracker = true;

            this.localDataChiamata=param;


        }


        /**
         * field for DataInserimento
         */


        protected java.util.Calendar localDataInserimento ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localDataInserimentoTracker = false ;

        public boolean isDataInserimentoSpecified(){
            return localDataInserimentoTracker;
        }



        /**
         * Auto generated getter method
         * @return java.util.Calendar
         */
        public  java.util.Calendar getDataInserimento(){
            return localDataInserimento;
        }



        /**
         * Auto generated setter method
         * @param param DataInserimento
         */
        public void setDataInserimento(java.util.Calendar param){
            localDataInserimentoTracker = true;

            this.localDataInserimento=param;


        }


        /**
         * field for DataRegistrazione
         */


        protected java.util.Date localDataRegistrazione ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localDataRegistrazioneTracker = false ;

        public boolean isDataRegistrazioneSpecified(){
            return localDataRegistrazioneTracker;
        }



        /**
         * Auto generated getter method
         * @return java.util.Date
         */
        public  java.util.Date getDataRegistrazione(){
            return localDataRegistrazione;
        }



        /**
         * Auto generated setter method
         * @param param DataRegistrazione
         */
        public void setDataRegistrazione(java.util.Date param){
            localDataRegistrazioneTracker = true;

            this.localDataRegistrazione=param;


        }


        /**
         * field for DataUltimaModifica
         */


        protected java.util.Calendar localDataUltimaModifica ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localDataUltimaModificaTracker = false ;

        public boolean isDataUltimaModificaSpecified(){
            return localDataUltimaModificaTracker;
        }



        /**
         * Auto generated getter method
         * @return java.util.Calendar
         */
        public  java.util.Calendar getDataUltimaModifica(){
            return localDataUltimaModifica;
        }



        /**
         * Auto generated setter method
         * @param param DataUltimaModifica
         */
        public void setDataUltimaModifica(java.util.Calendar param){
            localDataUltimaModificaTracker = true;

            this.localDataUltimaModifica=param;


        }


        /**
         * field for DocId
         */


        protected java.lang.String localDocId ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localDocIdTracker = false ;

        public boolean isDocIdSpecified(){
            return localDocIdTracker;
        }



        /**
         * Auto generated getter method
         * @return java.lang.String
         */
        public  java.lang.String getDocId(){
            return localDocId;
        }



        /**
         * Auto generated setter method
         * @param param DocId
         */
        public void setDocId(java.lang.String param){
            localDocIdTracker = true;

            this.localDocId=param;


        }


        /**
         * field for ErrorCode
         */


        protected java.lang.String localErrorCode ;

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
         * @return java.lang.String
         */
        public  java.lang.String getErrorCode(){
            return localErrorCode;
        }



        /**
         * Auto generated setter method
         * @param param ErrorCode
         */
        public void setErrorCode(java.lang.String param){
            localErrorCodeTracker = true;

            this.localErrorCode=param;


        }


        /**
         * field for ErrorMessage
         */


        protected java.lang.String localErrorMessage ;

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
        public  java.lang.String getErrorMessage(){
            return localErrorMessage;
        }



        /**
         * Auto generated setter method
         * @param param ErrorMessage
         */
        public void setErrorMessage(java.lang.String param){
            localErrorMessageTracker = true;

            this.localErrorMessage=param;


        }


        /**
         * field for Files
         */


        protected java.lang.String localFiles ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localFilesTracker = false ;

        public boolean isFilesSpecified(){
            return localFilesTracker;
        }



        /**
         * Auto generated getter method
         * @return java.lang.String
         */
        public  java.lang.String getFiles(){
            return localFiles;
        }



        /**
         * Auto generated setter method
         * @param param Files
         */
        public void setFiles(java.lang.String param){
            localFilesTracker = true;

            this.localFiles=param;


        }


        /**
         * field for ForzaAccettazione
         */


        protected boolean localForzaAccettazione ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localForzaAccettazioneTracker = false ;

        public boolean isForzaAccettazioneSpecified(){
            return localForzaAccettazioneTracker;
        }



        /**
         * Auto generated getter method
         * @return boolean
         */
        public  boolean getForzaAccettazione(){
            return localForzaAccettazione;
        }



        /**
         * Auto generated setter method
         * @param param ForzaAccettazione
         */
        public void setForzaAccettazione(boolean param){
            localForzaAccettazioneTracker = true;

            this.localForzaAccettazione=param;


        }


        /**
         * field for ForzaCollegamento
         */


        protected boolean localForzaCollegamento ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localForzaCollegamentoTracker = false ;

        public boolean isForzaCollegamentoSpecified(){
            return localForzaCollegamentoTracker;
        }



        /**
         * Auto generated getter method
         * @return boolean
         */
        public  boolean getForzaCollegamento(){
            return localForzaCollegamento;
        }



        /**
         * Auto generated setter method
         * @param param ForzaCollegamento
         */
        public void setForzaCollegamento(boolean param){
            localForzaCollegamentoTracker = true;

            this.localForzaCollegamento=param;


        }


        /**
         * field for ForzaConservazione
         */


        protected boolean localForzaConservazione ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localForzaConservazioneTracker = false ;

        public boolean isForzaConservazioneSpecified(){
            return localForzaConservazioneTracker;
        }



        /**
         * Auto generated getter method
         * @return boolean
         */
        public  boolean getForzaConservazione(){
            return localForzaConservazione;
        }



        /**
         * Auto generated setter method
         * @param param ForzaConservazione
         */
        public void setForzaConservazione(boolean param){
            localForzaConservazioneTracker = true;

            this.localForzaConservazione=param;


        }


        /**
         * field for Id
         */


        protected long localId ;

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
         * @return long
         */
        public  long getId(){
            return localId;
        }



        /**
         * Auto generated setter method
         * @param param Id
         */
        public void setId(long param){
            localIdTracker = true;

            this.localId=param;


        }


        /**
         * field for Stato
         */


        protected java.lang.String localStato ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localStatoTracker = false ;

        public boolean isStatoSpecified(){
            return localStatoTracker;
        }



        /**
         * Auto generated getter method
         * @return java.lang.String
         */
        public  java.lang.String getStato(){
            return localStato;
        }



        /**
         * Auto generated setter method
         * @param param Stato
         */
        public void setStato(java.lang.String param){
            localStatoTracker = param != null;

            this.localStato=param;


        }


        /**
         * field for TipoConservazione
         */


        protected java.lang.String localTipoConservazione ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localTipoConservazioneTracker = false ;

        public boolean isTipoConservazioneSpecified(){
            return localTipoConservazioneTracker;
        }



        /**
         * Auto generated getter method
         * @return java.lang.String
         */
        public  java.lang.String getTipoConservazione(){
            return localTipoConservazione;
        }



        /**
         * Auto generated setter method
         * @param param TipoConservazione
         */
        public void setTipoConservazione(java.lang.String param){
            localTipoConservazioneTracker = true;

            this.localTipoConservazione=param;


        }


        /**
         * field for TipoDocumento
         */


        protected java.lang.String localTipoDocumento ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localTipoDocumentoTracker = false ;

        public boolean isTipoDocumentoSpecified(){
            return localTipoDocumentoTracker;
        }



        /**
         * Auto generated getter method
         * @return java.lang.String
         */
        public  java.lang.String getTipoDocumento(){
            return localTipoDocumento;
        }



        /**
         * Auto generated setter method
         * @param param TipoDocumento
         */
        public void setTipoDocumento(java.lang.String param){
            localTipoDocumentoTracker = true;

            this.localTipoDocumento=param;


        }


        /**
         * field for Version
         */


        protected int localVersion ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localVersionTracker = false ;

        public boolean isVersionSpecified(){
            return localVersionTracker;
        }



        /**
         * Auto generated getter method
         * @return int
         */
        public  int getVersion(){
            return localVersion;
        }



        /**
         * Auto generated setter method
         * @param param Version
         */
        public void setVersion(int param){
            localVersionTracker = true;

            this.localVersion=param;


        }


        /**
         * field for WebService
         */


        protected java.lang.String localWebService ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localWebServiceTracker = false ;

        public boolean isWebServiceSpecified(){
            return localWebServiceTracker;
        }



        /**
         * Auto generated getter method
         * @return java.lang.String
         */
        public  java.lang.String getWebService(){
            return localWebService;
        }



        /**
         * Auto generated setter method
         * @param param WebService
         */
        public void setWebService(java.lang.String param){
            localWebServiceTracker = true;

            this.localWebService=param;


        }


        /**
         * field for XmlAllegati
         */


        protected java.lang.String localXmlAllegati ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localXmlAllegatiTracker = false ;

        public boolean isXmlAllegatiSpecified(){
            return localXmlAllegatiTracker;
        }



        /**
         * Auto generated getter method
         * @return java.lang.String
         */
        public  java.lang.String getXmlAllegati(){
            return localXmlAllegati;
        }



        /**
         * Auto generated setter method
         * @param param XmlAllegati
         */
        public void setXmlAllegati(java.lang.String param){
            localXmlAllegatiTracker = true;

            this.localXmlAllegati=param;


        }


        /**
         * field for XmlDocumento
         */


        protected java.lang.String localXmlDocumento ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localXmlDocumentoTracker = false ;

        public boolean isXmlDocumentoSpecified(){
            return localXmlDocumentoTracker;
        }



        /**
         * Auto generated getter method
         * @return java.lang.String
         */
        public  java.lang.String getXmlDocumento(){
            return localXmlDocumento;
        }



        /**
         * Auto generated setter method
         * @param param XmlDocumento
         */
        public void setXmlDocumento(java.lang.String param){
            localXmlDocumentoTracker = true;

            this.localXmlDocumento=param;


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




            java.lang.String prefix = null;
            java.lang.String namespace = null;


            prefix = parentQName.getPrefix();
            namespace = parentQName.getNamespaceURI();
            writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

            if (serializeType){


                java.lang.String namespacePrefix = registerPrefix(xmlWriter,"http://database.daemon.parer.docer.kdm.it/xsd");
                if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            namespacePrefix+":Job",
                            xmlWriter);
                } else {
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            "Job",
                            xmlWriter);
                }


            }
            if (localAppChiamanteTracker){
                namespace = "http://database.daemon.parer.docer.kdm.it/xsd";
                writeStartElement(null, namespace, "appChiamante", xmlWriter);


                if (localAppChiamante==null){
                    // write the nil attribute

                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                }else{


                    xmlWriter.writeCharacters(localAppChiamante);

                }

                xmlWriter.writeEndElement();
            } if (localAzioneTracker){
                namespace = "http://database.daemon.parer.docer.kdm.it/xsd";
                writeStartElement(null, namespace, "azione", xmlWriter);


                if (localAzione==null){
                    // write the nil attribute

                    throw new org.apache.axis2.databinding.ADBException("azione cannot be null!!");

                }else{


                    xmlWriter.writeCharacters(localAzione);

                }

                xmlWriter.writeEndElement();
            } if (localChiaveDocTracker){
                namespace = "http://database.daemon.parer.docer.kdm.it/xsd";
                writeStartElement(null, namespace, "chiaveDoc", xmlWriter);


                if (localChiaveDoc==null){
                    // write the nil attribute

                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                }else{


                    xmlWriter.writeCharacters(localChiaveDoc);

                }

                xmlWriter.writeEndElement();
            } if (localCodAooTracker){
                namespace = "http://database.daemon.parer.docer.kdm.it/xsd";
                writeStartElement(null, namespace, "codAoo", xmlWriter);


                if (localCodAoo==null){
                    // write the nil attribute

                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                }else{


                    xmlWriter.writeCharacters(localCodAoo);

                }

                xmlWriter.writeEndElement();
            } if (localCodEnteTracker){
                namespace = "http://database.daemon.parer.docer.kdm.it/xsd";
                writeStartElement(null, namespace, "codEnte", xmlWriter);


                if (localCodEnte==null){
                    // write the nil attribute

                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                }else{


                    xmlWriter.writeCharacters(localCodEnte);

                }

                xmlWriter.writeEndElement();
            } if (localDataChiamataTracker){
                namespace = "http://database.daemon.parer.docer.kdm.it/xsd";
                writeStartElement(null, namespace, "dataChiamata", xmlWriter);


                if (localDataChiamata==null){
                    // write the nil attribute

                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                }else{


                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDataChiamata));

                }

                xmlWriter.writeEndElement();
            } if (localDataInserimentoTracker){
                namespace = "http://database.daemon.parer.docer.kdm.it/xsd";
                writeStartElement(null, namespace, "dataInserimento", xmlWriter);


                if (localDataInserimento==null){
                    // write the nil attribute

                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                }else{


                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDataInserimento));

                }

                xmlWriter.writeEndElement();
            } if (localDataRegistrazioneTracker){
                namespace = "http://database.daemon.parer.docer.kdm.it/xsd";
                writeStartElement(null, namespace, "dataRegistrazione", xmlWriter);


                if (localDataRegistrazione==null){
                    // write the nil attribute

                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                }else{


                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDataRegistrazione));

                }

                xmlWriter.writeEndElement();
            } if (localDataUltimaModificaTracker){
                namespace = "http://database.daemon.parer.docer.kdm.it/xsd";
                writeStartElement(null, namespace, "dataUltimaModifica", xmlWriter);


                if (localDataUltimaModifica==null){
                    // write the nil attribute

                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                }else{


                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDataUltimaModifica));

                }

                xmlWriter.writeEndElement();
            } if (localDocIdTracker){
                namespace = "http://database.daemon.parer.docer.kdm.it/xsd";
                writeStartElement(null, namespace, "docId", xmlWriter);


                if (localDocId==null){
                    // write the nil attribute

                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                }else{


                    xmlWriter.writeCharacters(localDocId);

                }

                xmlWriter.writeEndElement();
            } if (localErrorCodeTracker){
                namespace = "http://database.daemon.parer.docer.kdm.it/xsd";
                writeStartElement(null, namespace, "errorCode", xmlWriter);


                if (localErrorCode==null){
                    // write the nil attribute

                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                }else{


                    xmlWriter.writeCharacters(localErrorCode);

                }

                xmlWriter.writeEndElement();
            } if (localErrorMessageTracker){
                namespace = "http://database.daemon.parer.docer.kdm.it/xsd";
                writeStartElement(null, namespace, "errorMessage", xmlWriter);


                if (localErrorMessage==null){
                    // write the nil attribute

                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                }else{


                    xmlWriter.writeCharacters(localErrorMessage);

                }

                xmlWriter.writeEndElement();
            } if (localFilesTracker){
                namespace = "http://database.daemon.parer.docer.kdm.it/xsd";
                writeStartElement(null, namespace, "files", xmlWriter);


                if (localFiles==null){
                    // write the nil attribute

                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                }else{


                    xmlWriter.writeCharacters(localFiles);

                }

                xmlWriter.writeEndElement();
            } if (localForzaAccettazioneTracker){
                namespace = "http://database.daemon.parer.docer.kdm.it/xsd";
                writeStartElement(null, namespace, "forzaAccettazione", xmlWriter);

                if (false) {

                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                } else {
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localForzaAccettazione));
                }

                xmlWriter.writeEndElement();
            } if (localForzaCollegamentoTracker){
                namespace = "http://database.daemon.parer.docer.kdm.it/xsd";
                writeStartElement(null, namespace, "forzaCollegamento", xmlWriter);

                if (false) {

                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                } else {
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localForzaCollegamento));
                }

                xmlWriter.writeEndElement();
            } if (localForzaConservazioneTracker){
                namespace = "http://database.daemon.parer.docer.kdm.it/xsd";
                writeStartElement(null, namespace, "forzaConservazione", xmlWriter);

                if (false) {

                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                } else {
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localForzaConservazione));
                }

                xmlWriter.writeEndElement();
            } if (localIdTracker){
                namespace = "http://database.daemon.parer.docer.kdm.it/xsd";
                writeStartElement(null, namespace, "id", xmlWriter);

                if (localId==java.lang.Long.MIN_VALUE) {

                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                } else {
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localId));
                }

                xmlWriter.writeEndElement();
            } if (localStatoTracker){
                namespace = "http://database.daemon.parer.docer.kdm.it/xsd";
                writeStartElement(null, namespace, "stato", xmlWriter);


                if (localStato==null){
                    // write the nil attribute

                    throw new org.apache.axis2.databinding.ADBException("stato cannot be null!!");

                }else{


                    xmlWriter.writeCharacters(localStato);

                }

                xmlWriter.writeEndElement();
            } if (localTipoConservazioneTracker){
                namespace = "http://database.daemon.parer.docer.kdm.it/xsd";
                writeStartElement(null, namespace, "tipoConservazione", xmlWriter);


                if (localTipoConservazione==null){
                    // write the nil attribute

                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                }else{


                    xmlWriter.writeCharacters(localTipoConservazione);

                }

                xmlWriter.writeEndElement();
            } if (localTipoDocumentoTracker){
                namespace = "http://database.daemon.parer.docer.kdm.it/xsd";
                writeStartElement(null, namespace, "tipoDocumento", xmlWriter);


                if (localTipoDocumento==null){
                    // write the nil attribute

                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                }else{


                    xmlWriter.writeCharacters(localTipoDocumento);

                }

                xmlWriter.writeEndElement();
            } if (localVersionTracker){
                namespace = "http://database.daemon.parer.docer.kdm.it/xsd";
                writeStartElement(null, namespace, "version", xmlWriter);

                if (localVersion==java.lang.Integer.MIN_VALUE) {

                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                } else {
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localVersion));
                }

                xmlWriter.writeEndElement();
            } if (localWebServiceTracker){
                namespace = "http://database.daemon.parer.docer.kdm.it/xsd";
                writeStartElement(null, namespace, "webService", xmlWriter);


                if (localWebService==null){
                    // write the nil attribute

                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                }else{


                    xmlWriter.writeCharacters(localWebService);

                }

                xmlWriter.writeEndElement();
            } if (localXmlAllegatiTracker){
                namespace = "http://database.daemon.parer.docer.kdm.it/xsd";
                writeStartElement(null, namespace, "xmlAllegati", xmlWriter);


                if (localXmlAllegati==null){
                    // write the nil attribute

                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                }else{


                    xmlWriter.writeCharacters(localXmlAllegati);

                }

                xmlWriter.writeEndElement();
            } if (localXmlDocumentoTracker){
                namespace = "http://database.daemon.parer.docer.kdm.it/xsd";
                writeStartElement(null, namespace, "xmlDocumento", xmlWriter);


                if (localXmlDocumento==null){
                    // write the nil attribute

                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                }else{


                    xmlWriter.writeCharacters(localXmlDocumento);

                }

                xmlWriter.writeEndElement();
            }
            xmlWriter.writeEndElement();


        }

        private static java.lang.String generatePrefix(java.lang.String namespace) {
            if(namespace.equals("http://database.daemon.parer.docer.kdm.it/xsd")){
                return "ns1";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
         * Utility method to write an element start tag.
         */
        private void writeStartElement(java.lang.String prefix, java.lang.String namespace, java.lang.String localPart,
                                       javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String writerPrefix = xmlWriter.getPrefix(namespace);
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
        private void writeAttribute(java.lang.String prefix,java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (xmlWriter.getPrefix(namespace) == null) {
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            xmlWriter.writeAttribute(namespace,attName,attValue);
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeAttribute(java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
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
        private void writeQNameAttribute(java.lang.String namespace, java.lang.String attName,
                                         javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            java.lang.String attributeNamespace = qname.getNamespaceURI();
            java.lang.String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
            if (attributePrefix == null) {
                attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
            }
            java.lang.String attributeValue;
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
            java.lang.String namespaceURI = qname.getNamespaceURI();
            if (namespaceURI != null) {
                java.lang.String prefix = xmlWriter.getPrefix(namespaceURI);
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
                java.lang.StringBuffer stringToWrite = new java.lang.StringBuffer();
                java.lang.String namespaceURI = null;
                java.lang.String prefix = null;

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
        private java.lang.String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, java.lang.String namespace) throws javax.xml.stream.XMLStreamException {
            java.lang.String prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null) {
                prefix = generatePrefix(namespace);
                javax.xml.namespace.NamespaceContext nsContext = xmlWriter.getNamespaceContext();
                while (true) {
                    java.lang.String uri = nsContext.getNamespaceURI(prefix);
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

            if (localAppChiamanteTracker){
                elementList.add(new javax.xml.namespace.QName("http://database.daemon.parer.docer.kdm.it/xsd",
                        "appChiamante"));

                elementList.add(localAppChiamante==null?null:
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localAppChiamante));
            } if (localAzioneTracker){
                elementList.add(new javax.xml.namespace.QName("http://database.daemon.parer.docer.kdm.it/xsd",
                        "azione"));

                if (localAzione != null){
                    elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localAzione));
                } else {
                    throw new org.apache.axis2.databinding.ADBException("azione cannot be null!!");
                }
            } if (localChiaveDocTracker){
                elementList.add(new javax.xml.namespace.QName("http://database.daemon.parer.docer.kdm.it/xsd",
                        "chiaveDoc"));

                elementList.add(localChiaveDoc==null?null:
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localChiaveDoc));
            } if (localCodAooTracker){
                elementList.add(new javax.xml.namespace.QName("http://database.daemon.parer.docer.kdm.it/xsd",
                        "codAoo"));

                elementList.add(localCodAoo==null?null:
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localCodAoo));
            } if (localCodEnteTracker){
                elementList.add(new javax.xml.namespace.QName("http://database.daemon.parer.docer.kdm.it/xsd",
                        "codEnte"));

                elementList.add(localCodEnte==null?null:
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localCodEnte));
            } if (localDataChiamataTracker){
                elementList.add(new javax.xml.namespace.QName("http://database.daemon.parer.docer.kdm.it/xsd",
                        "dataChiamata"));

                elementList.add(localDataChiamata==null?null:
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDataChiamata));
            } if (localDataInserimentoTracker){
                elementList.add(new javax.xml.namespace.QName("http://database.daemon.parer.docer.kdm.it/xsd",
                        "dataInserimento"));

                elementList.add(localDataInserimento==null?null:
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDataInserimento));
            } if (localDataRegistrazioneTracker){
                elementList.add(new javax.xml.namespace.QName("http://database.daemon.parer.docer.kdm.it/xsd",
                        "dataRegistrazione"));

                elementList.add(localDataRegistrazione==null?null:
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDataRegistrazione));
            } if (localDataUltimaModificaTracker){
                elementList.add(new javax.xml.namespace.QName("http://database.daemon.parer.docer.kdm.it/xsd",
                        "dataUltimaModifica"));

                elementList.add(localDataUltimaModifica==null?null:
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDataUltimaModifica));
            } if (localDocIdTracker){
                elementList.add(new javax.xml.namespace.QName("http://database.daemon.parer.docer.kdm.it/xsd",
                        "docId"));

                elementList.add(localDocId==null?null:
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDocId));
            } if (localErrorCodeTracker){
                elementList.add(new javax.xml.namespace.QName("http://database.daemon.parer.docer.kdm.it/xsd",
                        "errorCode"));

                elementList.add(localErrorCode==null?null:
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localErrorCode));
            } if (localErrorMessageTracker){
                elementList.add(new javax.xml.namespace.QName("http://database.daemon.parer.docer.kdm.it/xsd",
                        "errorMessage"));

                elementList.add(localErrorMessage==null?null:
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localErrorMessage));
            } if (localFilesTracker){
                elementList.add(new javax.xml.namespace.QName("http://database.daemon.parer.docer.kdm.it/xsd",
                        "files"));

                elementList.add(localFiles==null?null:
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localFiles));
            } if (localForzaAccettazioneTracker){
                elementList.add(new javax.xml.namespace.QName("http://database.daemon.parer.docer.kdm.it/xsd",
                        "forzaAccettazione"));

                elementList.add(
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localForzaAccettazione));
            } if (localForzaCollegamentoTracker){
                elementList.add(new javax.xml.namespace.QName("http://database.daemon.parer.docer.kdm.it/xsd",
                        "forzaCollegamento"));

                elementList.add(
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localForzaCollegamento));
            } if (localForzaConservazioneTracker){
                elementList.add(new javax.xml.namespace.QName("http://database.daemon.parer.docer.kdm.it/xsd",
                        "forzaConservazione"));

                elementList.add(
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localForzaConservazione));
            } if (localIdTracker){
                elementList.add(new javax.xml.namespace.QName("http://database.daemon.parer.docer.kdm.it/xsd",
                        "id"));

                elementList.add(
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localId));
            } if (localStatoTracker){
                elementList.add(new javax.xml.namespace.QName("http://database.daemon.parer.docer.kdm.it/xsd",
                        "stato"));

                if (localStato != null){
                    elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localStato));
                } else {
                    throw new org.apache.axis2.databinding.ADBException("stato cannot be null!!");
                }
            } if (localTipoConservazioneTracker){
                elementList.add(new javax.xml.namespace.QName("http://database.daemon.parer.docer.kdm.it/xsd",
                        "tipoConservazione"));

                elementList.add(localTipoConservazione==null?null:
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localTipoConservazione));
            } if (localTipoDocumentoTracker){
                elementList.add(new javax.xml.namespace.QName("http://database.daemon.parer.docer.kdm.it/xsd",
                        "tipoDocumento"));

                elementList.add(localTipoDocumento==null?null:
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localTipoDocumento));
            } if (localVersionTracker){
                elementList.add(new javax.xml.namespace.QName("http://database.daemon.parer.docer.kdm.it/xsd",
                        "version"));

                elementList.add(
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localVersion));
            } if (localWebServiceTracker){
                elementList.add(new javax.xml.namespace.QName("http://database.daemon.parer.docer.kdm.it/xsd",
                        "webService"));

                elementList.add(localWebService==null?null:
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localWebService));
            } if (localXmlAllegatiTracker){
                elementList.add(new javax.xml.namespace.QName("http://database.daemon.parer.docer.kdm.it/xsd",
                        "xmlAllegati"));

                elementList.add(localXmlAllegati==null?null:
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localXmlAllegati));
            } if (localXmlDocumentoTracker){
                elementList.add(new javax.xml.namespace.QName("http://database.daemon.parer.docer.kdm.it/xsd",
                        "xmlDocumento"));

                elementList.add(localXmlDocumento==null?null:
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localXmlDocumento));
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
            public static Job parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{
                Job object =
                        new Job();

                int event;
                java.lang.String nillableValue = null;
                java.lang.String prefix ="";
                java.lang.String namespaceuri ="";
                try {

                    while (!reader.isStartElement() && !reader.isEndElement())
                        reader.next();


                    if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","type")!=null){
                        java.lang.String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                                "type");
                        if (fullTypeName!=null){
                            java.lang.String nsPrefix = null;
                            if (fullTypeName.indexOf(":") > -1){
                                nsPrefix = fullTypeName.substring(0,fullTypeName.indexOf(":"));
                            }
                            nsPrefix = nsPrefix==null?"":nsPrefix;

                            java.lang.String type = fullTypeName.substring(fullTypeName.indexOf(":")+1);

                            if (!"Job".equals(type)){
                                //find namespace for the prefix
                                java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (Job)ExtensionMapper.getTypeObject(
                                        nsUri,type,reader);
                            }


                        }


                    }




                    // Note all attributes that were handled. Used to differ normal attributes
                    // from anyAttributes.
                    java.util.Vector handledAttributes = new java.util.Vector();




                    reader.next();


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://database.daemon.parer.docer.kdm.it/xsd","appChiamante").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                            java.lang.String content = reader.getElementText();

                            object.setAppChiamante(
                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                        } else {


                            reader.getElementText(); // throw away text nodes if any.
                        }

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                    }


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://database.daemon.parer.docer.kdm.it/xsd","azione").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                            throw new org.apache.axis2.databinding.ADBException("The element: "+"azione" +"  cannot be null");
                        }


                        java.lang.String content = reader.getElementText();

                        object.setAzione(
                                org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                    }


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://database.daemon.parer.docer.kdm.it/xsd","chiaveDoc").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                            java.lang.String content = reader.getElementText();

                            object.setChiaveDoc(
                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                        } else {


                            reader.getElementText(); // throw away text nodes if any.
                        }

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                    }


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://database.daemon.parer.docer.kdm.it/xsd","codAoo").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                            java.lang.String content = reader.getElementText();

                            object.setCodAoo(
                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                        } else {


                            reader.getElementText(); // throw away text nodes if any.
                        }

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                    }


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://database.daemon.parer.docer.kdm.it/xsd","codEnte").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                            java.lang.String content = reader.getElementText();

                            object.setCodEnte(
                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                        } else {


                            reader.getElementText(); // throw away text nodes if any.
                        }

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                    }


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://database.daemon.parer.docer.kdm.it/xsd","dataChiamata").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                            java.lang.String content = reader.getElementText();

                            object.setDataChiamata(
                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToDateTime(content));

                        } else {


                            reader.getElementText(); // throw away text nodes if any.
                        }

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                    }


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://database.daemon.parer.docer.kdm.it/xsd","dataInserimento").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                            java.lang.String content = reader.getElementText();

                            object.setDataInserimento(
                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToDateTime(content));

                        } else {


                            reader.getElementText(); // throw away text nodes if any.
                        }

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                    }


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://database.daemon.parer.docer.kdm.it/xsd","dataRegistrazione").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                            java.lang.String content = reader.getElementText();

                            object.setDataRegistrazione(
                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToDate(content));

                        } else {


                            reader.getElementText(); // throw away text nodes if any.
                        }

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                    }


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://database.daemon.parer.docer.kdm.it/xsd","dataUltimaModifica").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                            java.lang.String content = reader.getElementText();

                            object.setDataUltimaModifica(
                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToDateTime(content));

                        } else {


                            reader.getElementText(); // throw away text nodes if any.
                        }

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                    }


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://database.daemon.parer.docer.kdm.it/xsd","docId").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                            java.lang.String content = reader.getElementText();

                            object.setDocId(
                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                        } else {


                            reader.getElementText(); // throw away text nodes if any.
                        }

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                    }


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://database.daemon.parer.docer.kdm.it/xsd","errorCode").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                            java.lang.String content = reader.getElementText();

                            object.setErrorCode(
                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                        } else {


                            reader.getElementText(); // throw away text nodes if any.
                        }

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                    }


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://database.daemon.parer.docer.kdm.it/xsd","errorMessage").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                            java.lang.String content = reader.getElementText();

                            object.setErrorMessage(
                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                        } else {


                            reader.getElementText(); // throw away text nodes if any.
                        }

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                    }


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://database.daemon.parer.docer.kdm.it/xsd","files").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                            java.lang.String content = reader.getElementText();

                            object.setFiles(
                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                        } else {


                            reader.getElementText(); // throw away text nodes if any.
                        }

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                    }


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://database.daemon.parer.docer.kdm.it/xsd","forzaAccettazione").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                            java.lang.String content = reader.getElementText();

                            object.setForzaAccettazione(
                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));

                        } else {


                            reader.getElementText(); // throw away text nodes if any.
                        }

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                    }


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://database.daemon.parer.docer.kdm.it/xsd","forzaCollegamento").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                            java.lang.String content = reader.getElementText();

                            object.setForzaCollegamento(
                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));

                        } else {


                            reader.getElementText(); // throw away text nodes if any.
                        }

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                    }


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://database.daemon.parer.docer.kdm.it/xsd","forzaConservazione").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                            java.lang.String content = reader.getElementText();

                            object.setForzaConservazione(
                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));

                        } else {


                            reader.getElementText(); // throw away text nodes if any.
                        }

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                    }


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://database.daemon.parer.docer.kdm.it/xsd","id").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                            java.lang.String content = reader.getElementText();

                            object.setId(
                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToLong(content));

                        } else {


                            object.setId(java.lang.Long.MIN_VALUE);

                            reader.getElementText(); // throw away text nodes if any.
                        }

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                        object.setId(java.lang.Long.MIN_VALUE);

                    }


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://database.daemon.parer.docer.kdm.it/xsd","stato").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                            throw new org.apache.axis2.databinding.ADBException("The element: "+"stato" +"  cannot be null");
                        }


                        java.lang.String content = reader.getElementText();

                        object.setStato(
                                org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                    }


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://database.daemon.parer.docer.kdm.it/xsd","tipoConservazione").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                            java.lang.String content = reader.getElementText();

                            object.setTipoConservazione(
                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                        } else {


                            reader.getElementText(); // throw away text nodes if any.
                        }

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                    }


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://database.daemon.parer.docer.kdm.it/xsd","tipoDocumento").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                            java.lang.String content = reader.getElementText();

                            object.setTipoDocumento(
                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                        } else {


                            reader.getElementText(); // throw away text nodes if any.
                        }

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                    }


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://database.daemon.parer.docer.kdm.it/xsd","version").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                            java.lang.String content = reader.getElementText();

                            object.setVersion(
                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));

                        } else {


                            object.setVersion(java.lang.Integer.MIN_VALUE);

                            reader.getElementText(); // throw away text nodes if any.
                        }

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                        object.setVersion(java.lang.Integer.MIN_VALUE);

                    }


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://database.daemon.parer.docer.kdm.it/xsd","webService").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                            java.lang.String content = reader.getElementText();

                            object.setWebService(
                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                        } else {


                            reader.getElementText(); // throw away text nodes if any.
                        }

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                    }


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://database.daemon.parer.docer.kdm.it/xsd","xmlAllegati").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                            java.lang.String content = reader.getElementText();

                            object.setXmlAllegati(
                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                        } else {


                            reader.getElementText(); // throw away text nodes if any.
                        }

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                    }


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://database.daemon.parer.docer.kdm.it/xsd","xmlDocumento").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                            java.lang.String content = reader.getElementText();

                            object.setXmlDocumento(
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
                    throw new java.lang.Exception(e);
                }

                return object;
            }

        }//end of factory class



    }


    public static class GetJob
            implements org.apache.axis2.databinding.ADBBean{

        public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName(
                "http://webservice.demone.conservazione",
                "getJob",
                "ns3");



        /**
         * field for Id_job
         */


        protected long localId_job ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localId_jobTracker = false ;

        public boolean isId_jobSpecified(){
            return localId_jobTracker;
        }



        /**
         * Auto generated getter method
         * @return long
         */
        public  long getId_job(){
            return localId_job;
        }



        /**
         * Auto generated setter method
         * @param param Id_job
         */
        public void setId_job(long param){

            // setting primitive attribute tracker to true
            localId_jobTracker =
                    param != java.lang.Long.MIN_VALUE;

            this.localId_job=param;


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




            java.lang.String prefix = null;
            java.lang.String namespace = null;


            prefix = parentQName.getPrefix();
            namespace = parentQName.getNamespaceURI();
            writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

            if (serializeType){


                java.lang.String namespacePrefix = registerPrefix(xmlWriter,"http://webservice.demone.conservazione");
                if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            namespacePrefix+":getJob",
                            xmlWriter);
                } else {
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            "getJob",
                            xmlWriter);
                }


            }
            if (localId_jobTracker){
                namespace = "http://webservice.demone.conservazione";
                writeStartElement(null, namespace, "id_job", xmlWriter);

                if (localId_job==java.lang.Long.MIN_VALUE) {

                    throw new org.apache.axis2.databinding.ADBException("id_job cannot be null!!");

                } else {
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localId_job));
                }

                xmlWriter.writeEndElement();
            }
            xmlWriter.writeEndElement();


        }

        private static java.lang.String generatePrefix(java.lang.String namespace) {
            if(namespace.equals("http://webservice.demone.conservazione")){
                return "ns3";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
         * Utility method to write an element start tag.
         */
        private void writeStartElement(java.lang.String prefix, java.lang.String namespace, java.lang.String localPart,
                                       javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String writerPrefix = xmlWriter.getPrefix(namespace);
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
        private void writeAttribute(java.lang.String prefix,java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (xmlWriter.getPrefix(namespace) == null) {
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            xmlWriter.writeAttribute(namespace,attName,attValue);
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeAttribute(java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
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
        private void writeQNameAttribute(java.lang.String namespace, java.lang.String attName,
                                         javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            java.lang.String attributeNamespace = qname.getNamespaceURI();
            java.lang.String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
            if (attributePrefix == null) {
                attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
            }
            java.lang.String attributeValue;
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
            java.lang.String namespaceURI = qname.getNamespaceURI();
            if (namespaceURI != null) {
                java.lang.String prefix = xmlWriter.getPrefix(namespaceURI);
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
                java.lang.StringBuffer stringToWrite = new java.lang.StringBuffer();
                java.lang.String namespaceURI = null;
                java.lang.String prefix = null;

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
        private java.lang.String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, java.lang.String namespace) throws javax.xml.stream.XMLStreamException {
            java.lang.String prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null) {
                prefix = generatePrefix(namespace);
                javax.xml.namespace.NamespaceContext nsContext = xmlWriter.getNamespaceContext();
                while (true) {
                    java.lang.String uri = nsContext.getNamespaceURI(prefix);
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

            if (localId_jobTracker){
                elementList.add(new javax.xml.namespace.QName("http://webservice.demone.conservazione",
                        "id_job"));

                elementList.add(
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localId_job));
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
            public static GetJob parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{
                GetJob object =
                        new GetJob();

                int event;
                java.lang.String nillableValue = null;
                java.lang.String prefix ="";
                java.lang.String namespaceuri ="";
                try {

                    while (!reader.isStartElement() && !reader.isEndElement())
                        reader.next();


                    if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","type")!=null){
                        java.lang.String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                                "type");
                        if (fullTypeName!=null){
                            java.lang.String nsPrefix = null;
                            if (fullTypeName.indexOf(":") > -1){
                                nsPrefix = fullTypeName.substring(0,fullTypeName.indexOf(":"));
                            }
                            nsPrefix = nsPrefix==null?"":nsPrefix;

                            java.lang.String type = fullTypeName.substring(fullTypeName.indexOf(":")+1);

                            if (!"getJob".equals(type)){
                                //find namespace for the prefix
                                java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (GetJob)ExtensionMapper.getTypeObject(
                                        nsUri,type,reader);
                            }


                        }


                    }




                    // Note all attributes that were handled. Used to differ normal attributes
                    // from anyAttributes.
                    java.util.Vector handledAttributes = new java.util.Vector();




                    reader.next();


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://webservice.demone.conservazione","id_job").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                            throw new org.apache.axis2.databinding.ADBException("The element: "+"id_job" +"  cannot be null");
                        }


                        java.lang.String content = reader.getElementText();

                        object.setId_job(
                                org.apache.axis2.databinding.utils.ConverterUtil.convertToLong(content));

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                        object.setId_job(java.lang.Long.MIN_VALUE);

                    }

                    while (!reader.isStartElement() && !reader.isEndElement())
                        reader.next();

                    if (reader.isStartElement())
                        // A start element we are not expecting indicates a trailing invalid property
                        throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());




                } catch (javax.xml.stream.XMLStreamException e) {
                    throw new java.lang.Exception(e);
                }

                return object;
            }

        }//end of factory class



    }


    public static class ReadLogsByIdJob
            implements org.apache.axis2.databinding.ADBBean{

        public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName(
                "http://webservice.demone.conservazione",
                "readLogsByIdJob",
                "ns3");



        /**
         * field for Id_job
         */


        protected long localId_job ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localId_jobTracker = false ;

        public boolean isId_jobSpecified(){
            return localId_jobTracker;
        }



        /**
         * Auto generated getter method
         * @return long
         */
        public  long getId_job(){
            return localId_job;
        }



        /**
         * Auto generated setter method
         * @param param Id_job
         */
        public void setId_job(long param){

            // setting primitive attribute tracker to true
            localId_jobTracker =
                    param != java.lang.Long.MIN_VALUE;

            this.localId_job=param;


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




            java.lang.String prefix = null;
            java.lang.String namespace = null;


            prefix = parentQName.getPrefix();
            namespace = parentQName.getNamespaceURI();
            writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

            if (serializeType){


                java.lang.String namespacePrefix = registerPrefix(xmlWriter,"http://webservice.demone.conservazione");
                if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            namespacePrefix+":readLogsByIdJob",
                            xmlWriter);
                } else {
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            "readLogsByIdJob",
                            xmlWriter);
                }


            }
            if (localId_jobTracker){
                namespace = "http://webservice.demone.conservazione";
                writeStartElement(null, namespace, "id_job", xmlWriter);

                if (localId_job==java.lang.Long.MIN_VALUE) {

                    throw new org.apache.axis2.databinding.ADBException("id_job cannot be null!!");

                } else {
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localId_job));
                }

                xmlWriter.writeEndElement();
            }
            xmlWriter.writeEndElement();


        }

        private static java.lang.String generatePrefix(java.lang.String namespace) {
            if(namespace.equals("http://webservice.demone.conservazione")){
                return "ns3";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
         * Utility method to write an element start tag.
         */
        private void writeStartElement(java.lang.String prefix, java.lang.String namespace, java.lang.String localPart,
                                       javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String writerPrefix = xmlWriter.getPrefix(namespace);
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
        private void writeAttribute(java.lang.String prefix,java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (xmlWriter.getPrefix(namespace) == null) {
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            xmlWriter.writeAttribute(namespace,attName,attValue);
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeAttribute(java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
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
        private void writeQNameAttribute(java.lang.String namespace, java.lang.String attName,
                                         javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            java.lang.String attributeNamespace = qname.getNamespaceURI();
            java.lang.String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
            if (attributePrefix == null) {
                attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
            }
            java.lang.String attributeValue;
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
            java.lang.String namespaceURI = qname.getNamespaceURI();
            if (namespaceURI != null) {
                java.lang.String prefix = xmlWriter.getPrefix(namespaceURI);
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
                java.lang.StringBuffer stringToWrite = new java.lang.StringBuffer();
                java.lang.String namespaceURI = null;
                java.lang.String prefix = null;

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
        private java.lang.String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, java.lang.String namespace) throws javax.xml.stream.XMLStreamException {
            java.lang.String prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null) {
                prefix = generatePrefix(namespace);
                javax.xml.namespace.NamespaceContext nsContext = xmlWriter.getNamespaceContext();
                while (true) {
                    java.lang.String uri = nsContext.getNamespaceURI(prefix);
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

            if (localId_jobTracker){
                elementList.add(new javax.xml.namespace.QName("http://webservice.demone.conservazione",
                        "id_job"));

                elementList.add(
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localId_job));
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
            public static ReadLogsByIdJob parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{
                ReadLogsByIdJob object =
                        new ReadLogsByIdJob();

                int event;
                java.lang.String nillableValue = null;
                java.lang.String prefix ="";
                java.lang.String namespaceuri ="";
                try {

                    while (!reader.isStartElement() && !reader.isEndElement())
                        reader.next();


                    if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","type")!=null){
                        java.lang.String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                                "type");
                        if (fullTypeName!=null){
                            java.lang.String nsPrefix = null;
                            if (fullTypeName.indexOf(":") > -1){
                                nsPrefix = fullTypeName.substring(0,fullTypeName.indexOf(":"));
                            }
                            nsPrefix = nsPrefix==null?"":nsPrefix;

                            java.lang.String type = fullTypeName.substring(fullTypeName.indexOf(":")+1);

                            if (!"readLogsByIdJob".equals(type)){
                                //find namespace for the prefix
                                java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (ReadLogsByIdJob)ExtensionMapper.getTypeObject(
                                        nsUri,type,reader);
                            }


                        }


                    }




                    // Note all attributes that were handled. Used to differ normal attributes
                    // from anyAttributes.
                    java.util.Vector handledAttributes = new java.util.Vector();




                    reader.next();


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://webservice.demone.conservazione","id_job").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                            throw new org.apache.axis2.databinding.ADBException("The element: "+"id_job" +"  cannot be null");
                        }


                        java.lang.String content = reader.getElementText();

                        object.setId_job(
                                org.apache.axis2.databinding.utils.ConverterUtil.convertToLong(content));

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                        object.setId_job(java.lang.Long.MIN_VALUE);

                    }

                    while (!reader.isStartElement() && !reader.isEndElement())
                        reader.next();

                    if (reader.isStartElement())
                        // A start element we are not expecting indicates a trailing invalid property
                        throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());




                } catch (javax.xml.stream.XMLStreamException e) {
                    throw new java.lang.Exception(e);
                }

                return object;
            }

        }//end of factory class



    }


    public static class KeyValuePair
            implements org.apache.axis2.databinding.ADBBean{
        /* This type was generated from the piece of schema that had
                name = KeyValuePair
                Namespace URI = http://classes.sdk.docer.kdm.it/xsd
                Namespace Prefix = ns2
                */


        /**
         * field for Key
         */


        protected java.lang.String localKey ;

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
        public  java.lang.String getKey(){
            return localKey;
        }



        /**
         * Auto generated setter method
         * @param param Key
         */
        public void setKey(java.lang.String param){
            localKeyTracker = true;

            this.localKey=param;


        }


        /**
         * field for Value
         */


        protected java.lang.String localValue ;

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
        public  java.lang.String getValue(){
            return localValue;
        }



        /**
         * Auto generated setter method
         * @param param Value
         */
        public void setValue(java.lang.String param){
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




            java.lang.String prefix = null;
            java.lang.String namespace = null;


            prefix = parentQName.getPrefix();
            namespace = parentQName.getNamespaceURI();
            writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

            if (serializeType){


                java.lang.String namespacePrefix = registerPrefix(xmlWriter,"http://classes.sdk.docer.kdm.it/xsd");
                if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            namespacePrefix+":KeyValuePair",
                            xmlWriter);
                } else {
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            "KeyValuePair",
                            xmlWriter);
                }


            }
            if (localKeyTracker){
                namespace = "http://classes.sdk.docer.kdm.it/xsd";
                writeStartElement(null, namespace, "key", xmlWriter);


                if (localKey==null){
                    // write the nil attribute

                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                }else{


                    xmlWriter.writeCharacters(localKey);

                }

                xmlWriter.writeEndElement();
            } if (localValueTracker){
                namespace = "http://classes.sdk.docer.kdm.it/xsd";
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

        private static java.lang.String generatePrefix(java.lang.String namespace) {
            if(namespace.equals("http://classes.sdk.docer.kdm.it/xsd")){
                return "ns2";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
         * Utility method to write an element start tag.
         */
        private void writeStartElement(java.lang.String prefix, java.lang.String namespace, java.lang.String localPart,
                                       javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String writerPrefix = xmlWriter.getPrefix(namespace);
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
        private void writeAttribute(java.lang.String prefix,java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (xmlWriter.getPrefix(namespace) == null) {
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            xmlWriter.writeAttribute(namespace,attName,attValue);
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeAttribute(java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
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
        private void writeQNameAttribute(java.lang.String namespace, java.lang.String attName,
                                         javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            java.lang.String attributeNamespace = qname.getNamespaceURI();
            java.lang.String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
            if (attributePrefix == null) {
                attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
            }
            java.lang.String attributeValue;
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
            java.lang.String namespaceURI = qname.getNamespaceURI();
            if (namespaceURI != null) {
                java.lang.String prefix = xmlWriter.getPrefix(namespaceURI);
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
                java.lang.StringBuffer stringToWrite = new java.lang.StringBuffer();
                java.lang.String namespaceURI = null;
                java.lang.String prefix = null;

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
        private java.lang.String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, java.lang.String namespace) throws javax.xml.stream.XMLStreamException {
            java.lang.String prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null) {
                prefix = generatePrefix(namespace);
                javax.xml.namespace.NamespaceContext nsContext = xmlWriter.getNamespaceContext();
                while (true) {
                    java.lang.String uri = nsContext.getNamespaceURI(prefix);
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
                elementList.add(new javax.xml.namespace.QName("http://classes.sdk.docer.kdm.it/xsd",
                        "key"));

                elementList.add(localKey==null?null:
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localKey));
            } if (localValueTracker){
                elementList.add(new javax.xml.namespace.QName("http://classes.sdk.docer.kdm.it/xsd",
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
            public static KeyValuePair parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{
                KeyValuePair object =
                        new KeyValuePair();

                int event;
                java.lang.String nillableValue = null;
                java.lang.String prefix ="";
                java.lang.String namespaceuri ="";
                try {

                    while (!reader.isStartElement() && !reader.isEndElement())
                        reader.next();


                    if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","type")!=null){
                        java.lang.String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                                "type");
                        if (fullTypeName!=null){
                            java.lang.String nsPrefix = null;
                            if (fullTypeName.indexOf(":") > -1){
                                nsPrefix = fullTypeName.substring(0,fullTypeName.indexOf(":"));
                            }
                            nsPrefix = nsPrefix==null?"":nsPrefix;

                            java.lang.String type = fullTypeName.substring(fullTypeName.indexOf(":")+1);

                            if (!"KeyValuePair".equals(type)){
                                //find namespace for the prefix
                                java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (KeyValuePair)ExtensionMapper.getTypeObject(
                                        nsUri,type,reader);
                            }


                        }


                    }




                    // Note all attributes that were handled. Used to differ normal attributes
                    // from anyAttributes.
                    java.util.Vector handledAttributes = new java.util.Vector();




                    reader.next();


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://classes.sdk.docer.kdm.it/xsd","key").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                            java.lang.String content = reader.getElementText();

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

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://classes.sdk.docer.kdm.it/xsd","value").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                            java.lang.String content = reader.getElementText();

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
                    throw new java.lang.Exception(e);
                }

                return object;
            }

        }//end of factory class



    }


    public static class SearchJobEstesaResponse
            implements org.apache.axis2.databinding.ADBBean{

        public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName(
                "http://webservice.demone.conservazione",
                "searchJobEstesaResponse",
                "ns3");



        /**
         * field for _return
         * This was an Array!
         */


        protected Job[] local_return ;

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
         * @return Job[]
         */
        public  Job[] get_return(){
            return local_return;
        }






        /**
         * validate the array for _return
         */
        protected void validate_return(Job[] param){

        }


        /**
         * Auto generated setter method
         * @param param _return
         */
        public void set_return(Job[] param){

            validate_return(param);

            local_returnTracker = true;

            this.local_return=param;
        }



        /**
         * Auto generated add method for the array for convenience
         * @param param Job
         */
        public void add_return(Job param){
            if (local_return == null){
                local_return = new Job[]{};
            }


            //update the setting tracker
            local_returnTracker = true;


            java.util.List list =
                    org.apache.axis2.databinding.utils.ConverterUtil.toList(local_return);
            list.add(param);
            this.local_return =
                    (Job[])list.toArray(
                            new Job[list.size()]);

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




            java.lang.String prefix = null;
            java.lang.String namespace = null;


            prefix = parentQName.getPrefix();
            namespace = parentQName.getNamespaceURI();
            writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

            if (serializeType){


                java.lang.String namespacePrefix = registerPrefix(xmlWriter,"http://webservice.demone.conservazione");
                if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            namespacePrefix+":searchJobEstesaResponse",
                            xmlWriter);
                } else {
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            "searchJobEstesaResponse",
                            xmlWriter);
                }


            }
            if (local_returnTracker){
                if (local_return!=null){
                    for (int i = 0;i < local_return.length;i++){
                        if (local_return[i] != null){
                            local_return[i].serialize(new javax.xml.namespace.QName("http://webservice.demone.conservazione","return"),
                                    xmlWriter);
                        } else {

                            writeStartElement(null, "http://webservice.demone.conservazione", "return", xmlWriter);

                            // write the nil attribute
                            writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                            xmlWriter.writeEndElement();

                        }

                    }
                } else {

                    writeStartElement(null, "http://webservice.demone.conservazione", "return", xmlWriter);

                    // write the nil attribute
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                    xmlWriter.writeEndElement();

                }
            }
            xmlWriter.writeEndElement();


        }

        private static java.lang.String generatePrefix(java.lang.String namespace) {
            if(namespace.equals("http://webservice.demone.conservazione")){
                return "ns3";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
         * Utility method to write an element start tag.
         */
        private void writeStartElement(java.lang.String prefix, java.lang.String namespace, java.lang.String localPart,
                                       javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String writerPrefix = xmlWriter.getPrefix(namespace);
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
        private void writeAttribute(java.lang.String prefix,java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (xmlWriter.getPrefix(namespace) == null) {
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            xmlWriter.writeAttribute(namespace,attName,attValue);
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeAttribute(java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
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
        private void writeQNameAttribute(java.lang.String namespace, java.lang.String attName,
                                         javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            java.lang.String attributeNamespace = qname.getNamespaceURI();
            java.lang.String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
            if (attributePrefix == null) {
                attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
            }
            java.lang.String attributeValue;
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
            java.lang.String namespaceURI = qname.getNamespaceURI();
            if (namespaceURI != null) {
                java.lang.String prefix = xmlWriter.getPrefix(namespaceURI);
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
                java.lang.StringBuffer stringToWrite = new java.lang.StringBuffer();
                java.lang.String namespaceURI = null;
                java.lang.String prefix = null;

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
        private java.lang.String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, java.lang.String namespace) throws javax.xml.stream.XMLStreamException {
            java.lang.String prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null) {
                prefix = generatePrefix(namespace);
                javax.xml.namespace.NamespaceContext nsContext = xmlWriter.getNamespaceContext();
                while (true) {
                    java.lang.String uri = nsContext.getNamespaceURI(prefix);
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
                if (local_return!=null) {
                    for (int i = 0;i < local_return.length;i++){

                        if (local_return[i] != null){
                            elementList.add(new javax.xml.namespace.QName("http://webservice.demone.conservazione",
                                    "return"));
                            elementList.add(local_return[i]);
                        } else {

                            elementList.add(new javax.xml.namespace.QName("http://webservice.demone.conservazione",
                                    "return"));
                            elementList.add(null);

                        }

                    }
                } else {

                    elementList.add(new javax.xml.namespace.QName("http://webservice.demone.conservazione",
                            "return"));
                    elementList.add(local_return);

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
            public static SearchJobEstesaResponse parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{
                SearchJobEstesaResponse object =
                        new SearchJobEstesaResponse();

                int event;
                java.lang.String nillableValue = null;
                java.lang.String prefix ="";
                java.lang.String namespaceuri ="";
                try {

                    while (!reader.isStartElement() && !reader.isEndElement())
                        reader.next();


                    if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","type")!=null){
                        java.lang.String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                                "type");
                        if (fullTypeName!=null){
                            java.lang.String nsPrefix = null;
                            if (fullTypeName.indexOf(":") > -1){
                                nsPrefix = fullTypeName.substring(0,fullTypeName.indexOf(":"));
                            }
                            nsPrefix = nsPrefix==null?"":nsPrefix;

                            java.lang.String type = fullTypeName.substring(fullTypeName.indexOf(":")+1);

                            if (!"searchJobEstesaResponse".equals(type)){
                                //find namespace for the prefix
                                java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (SearchJobEstesaResponse)ExtensionMapper.getTypeObject(
                                        nsUri,type,reader);
                            }


                        }


                    }




                    // Note all attributes that were handled. Used to differ normal attributes
                    // from anyAttributes.
                    java.util.Vector handledAttributes = new java.util.Vector();




                    reader.next();

                    java.util.ArrayList list1 = new java.util.ArrayList();


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://webservice.demone.conservazione","return").equals(reader.getName())){



                        // Process the array and step past its final element's end.

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                            list1.add(null);
                            reader.next();
                        } else {
                            list1.add(Job.Factory.parse(reader));
                        }
                        //loop until we find a start element that is not part of this array
                        boolean loopDone1 = false;
                        while(!loopDone1){
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
                                loopDone1 = true;
                            } else {
                                if (new javax.xml.namespace.QName("http://webservice.demone.conservazione","return").equals(reader.getName())){

                                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                    if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                        list1.add(null);
                                        reader.next();
                                    } else {
                                        list1.add(Job.Factory.parse(reader));
                                    }
                                }else{
                                    loopDone1 = true;
                                }
                            }
                        }
                        // call the converter utility  to convert and set the array

                        object.set_return((Job[])
                                org.apache.axis2.databinding.utils.ConverterUtil.convertToArray(
                                        Job.class,
                                        list1));

                    }  // End of if for expected property start element

                    else {

                    }

                    while (!reader.isStartElement() && !reader.isEndElement())
                        reader.next();

                    if (reader.isStartElement())
                        // A start element we are not expecting indicates a trailing invalid property
                        throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());




                } catch (javax.xml.stream.XMLStreamException e) {
                    throw new java.lang.Exception(e);
                }

                return object;
            }

        }//end of factory class



    }


    public static class DeleteJob_ByIdDoc
            implements org.apache.axis2.databinding.ADBBean{

        public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName(
                "http://webservice.demone.conservazione",
                "deleteJob_ByIdDoc",
                "ns3");



        /**
         * field for Id_doc
         */


        protected java.lang.String localId_doc ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localId_docTracker = false ;

        public boolean isId_docSpecified(){
            return localId_docTracker;
        }



        /**
         * Auto generated getter method
         * @return java.lang.String
         */
        public  java.lang.String getId_doc(){
            return localId_doc;
        }



        /**
         * Auto generated setter method
         * @param param Id_doc
         */
        public void setId_doc(java.lang.String param){
            localId_docTracker = true;

            this.localId_doc=param;


        }


        /**
         * field for Azione
         */


        protected java.lang.String localAzione ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localAzioneTracker = false ;

        public boolean isAzioneSpecified(){
            return localAzioneTracker;
        }



        /**
         * Auto generated getter method
         * @return java.lang.String
         */
        public  java.lang.String getAzione(){
            return localAzione;
        }



        /**
         * Auto generated setter method
         * @param param Azione
         */
        public void setAzione(java.lang.String param){
            localAzioneTracker = true;

            this.localAzione=param;


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




            java.lang.String prefix = null;
            java.lang.String namespace = null;


            prefix = parentQName.getPrefix();
            namespace = parentQName.getNamespaceURI();
            writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

            if (serializeType){


                java.lang.String namespacePrefix = registerPrefix(xmlWriter,"http://webservice.demone.conservazione");
                if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            namespacePrefix+":deleteJob_ByIdDoc",
                            xmlWriter);
                } else {
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            "deleteJob_ByIdDoc",
                            xmlWriter);
                }


            }
            if (localId_docTracker){
                namespace = "http://webservice.demone.conservazione";
                writeStartElement(null, namespace, "id_doc", xmlWriter);


                if (localId_doc==null){
                    // write the nil attribute

                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                }else{


                    xmlWriter.writeCharacters(localId_doc);

                }

                xmlWriter.writeEndElement();
            } if (localAzioneTracker){
                namespace = "http://webservice.demone.conservazione";
                writeStartElement(null, namespace, "azione", xmlWriter);


                if (localAzione==null){
                    // write the nil attribute

                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                }else{


                    xmlWriter.writeCharacters(localAzione);

                }

                xmlWriter.writeEndElement();
            }
            xmlWriter.writeEndElement();


        }

        private static java.lang.String generatePrefix(java.lang.String namespace) {
            if(namespace.equals("http://webservice.demone.conservazione")){
                return "ns3";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
         * Utility method to write an element start tag.
         */
        private void writeStartElement(java.lang.String prefix, java.lang.String namespace, java.lang.String localPart,
                                       javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String writerPrefix = xmlWriter.getPrefix(namespace);
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
        private void writeAttribute(java.lang.String prefix,java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (xmlWriter.getPrefix(namespace) == null) {
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            xmlWriter.writeAttribute(namespace,attName,attValue);
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeAttribute(java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
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
        private void writeQNameAttribute(java.lang.String namespace, java.lang.String attName,
                                         javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            java.lang.String attributeNamespace = qname.getNamespaceURI();
            java.lang.String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
            if (attributePrefix == null) {
                attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
            }
            java.lang.String attributeValue;
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
            java.lang.String namespaceURI = qname.getNamespaceURI();
            if (namespaceURI != null) {
                java.lang.String prefix = xmlWriter.getPrefix(namespaceURI);
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
                java.lang.StringBuffer stringToWrite = new java.lang.StringBuffer();
                java.lang.String namespaceURI = null;
                java.lang.String prefix = null;

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
        private java.lang.String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, java.lang.String namespace) throws javax.xml.stream.XMLStreamException {
            java.lang.String prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null) {
                prefix = generatePrefix(namespace);
                javax.xml.namespace.NamespaceContext nsContext = xmlWriter.getNamespaceContext();
                while (true) {
                    java.lang.String uri = nsContext.getNamespaceURI(prefix);
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

            if (localId_docTracker){
                elementList.add(new javax.xml.namespace.QName("http://webservice.demone.conservazione",
                        "id_doc"));

                elementList.add(localId_doc==null?null:
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localId_doc));
            } if (localAzioneTracker){
                elementList.add(new javax.xml.namespace.QName("http://webservice.demone.conservazione",
                        "azione"));

                elementList.add(localAzione==null?null:
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localAzione));
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
            public static DeleteJob_ByIdDoc parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{
                DeleteJob_ByIdDoc object =
                        new DeleteJob_ByIdDoc();

                int event;
                java.lang.String nillableValue = null;
                java.lang.String prefix ="";
                java.lang.String namespaceuri ="";
                try {

                    while (!reader.isStartElement() && !reader.isEndElement())
                        reader.next();


                    if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","type")!=null){
                        java.lang.String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                                "type");
                        if (fullTypeName!=null){
                            java.lang.String nsPrefix = null;
                            if (fullTypeName.indexOf(":") > -1){
                                nsPrefix = fullTypeName.substring(0,fullTypeName.indexOf(":"));
                            }
                            nsPrefix = nsPrefix==null?"":nsPrefix;

                            java.lang.String type = fullTypeName.substring(fullTypeName.indexOf(":")+1);

                            if (!"deleteJob_ByIdDoc".equals(type)){
                                //find namespace for the prefix
                                java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (DeleteJob_ByIdDoc)ExtensionMapper.getTypeObject(
                                        nsUri,type,reader);
                            }


                        }


                    }




                    // Note all attributes that were handled. Used to differ normal attributes
                    // from anyAttributes.
                    java.util.Vector handledAttributes = new java.util.Vector();




                    reader.next();


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://webservice.demone.conservazione","id_doc").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                            java.lang.String content = reader.getElementText();

                            object.setId_doc(
                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                        } else {


                            reader.getElementText(); // throw away text nodes if any.
                        }

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                    }


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://webservice.demone.conservazione","azione").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                            java.lang.String content = reader.getElementText();

                            object.setAzione(
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
                    throw new java.lang.Exception(e);
                }

                return object;
            }

        }//end of factory class



    }


    public static class WSDemoneException
            implements org.apache.axis2.databinding.ADBBean{

        public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName(
                "http://webservice.demone.conservazione",
                "WSDemoneException",
                "ns3");



        /**
         * field for WSDemoneException
         */


        protected Exception localWSDemoneException ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localWSDemoneExceptionTracker = false ;

        public boolean isWSDemoneExceptionSpecified(){
            return localWSDemoneExceptionTracker;
        }



        /**
         * Auto generated getter method
         * @return Exception
         */
        public  Exception getWSDemoneException(){
            return localWSDemoneException;
        }



        /**
         * Auto generated setter method
         * @param param WSDemoneException
         */
        public void setWSDemoneException(Exception param){
            localWSDemoneExceptionTracker = true;

            this.localWSDemoneException=param;


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




            java.lang.String prefix = null;
            java.lang.String namespace = null;


            prefix = parentQName.getPrefix();
            namespace = parentQName.getNamespaceURI();
            writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

            if (serializeType){


                java.lang.String namespacePrefix = registerPrefix(xmlWriter,"http://webservice.demone.conservazione");
                if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            namespacePrefix+":WSDemoneException",
                            xmlWriter);
                } else {
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            "WSDemoneException",
                            xmlWriter);
                }


            }
            if (localWSDemoneExceptionTracker){
                if (localWSDemoneException==null){

                    writeStartElement(null, "http://webservice.demone.conservazione", "WSDemoneException", xmlWriter);

                    // write the nil attribute
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                    xmlWriter.writeEndElement();
                }else{
                    localWSDemoneException.serialize(new javax.xml.namespace.QName("http://webservice.demone.conservazione","WSDemoneException"),
                            xmlWriter);
                }
            }
            xmlWriter.writeEndElement();


        }

        private static java.lang.String generatePrefix(java.lang.String namespace) {
            if(namespace.equals("http://webservice.demone.conservazione")){
                return "ns3";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
         * Utility method to write an element start tag.
         */
        private void writeStartElement(java.lang.String prefix, java.lang.String namespace, java.lang.String localPart,
                                       javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String writerPrefix = xmlWriter.getPrefix(namespace);
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
        private void writeAttribute(java.lang.String prefix,java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (xmlWriter.getPrefix(namespace) == null) {
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            xmlWriter.writeAttribute(namespace,attName,attValue);
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeAttribute(java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
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
        private void writeQNameAttribute(java.lang.String namespace, java.lang.String attName,
                                         javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            java.lang.String attributeNamespace = qname.getNamespaceURI();
            java.lang.String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
            if (attributePrefix == null) {
                attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
            }
            java.lang.String attributeValue;
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
            java.lang.String namespaceURI = qname.getNamespaceURI();
            if (namespaceURI != null) {
                java.lang.String prefix = xmlWriter.getPrefix(namespaceURI);
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
                java.lang.StringBuffer stringToWrite = new java.lang.StringBuffer();
                java.lang.String namespaceURI = null;
                java.lang.String prefix = null;

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
        private java.lang.String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, java.lang.String namespace) throws javax.xml.stream.XMLStreamException {
            java.lang.String prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null) {
                prefix = generatePrefix(namespace);
                javax.xml.namespace.NamespaceContext nsContext = xmlWriter.getNamespaceContext();
                while (true) {
                    java.lang.String uri = nsContext.getNamespaceURI(prefix);
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

            if (localWSDemoneExceptionTracker){
                elementList.add(new javax.xml.namespace.QName("http://webservice.demone.conservazione",
                        "WSDemoneException"));


                elementList.add(localWSDemoneException==null?null:
                        localWSDemoneException);
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
            public static WSDemoneException parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{
                WSDemoneException object =
                        new WSDemoneException();

                int event;
                java.lang.String nillableValue = null;
                java.lang.String prefix ="";
                java.lang.String namespaceuri ="";
                try {

                    while (!reader.isStartElement() && !reader.isEndElement())
                        reader.next();


                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                    if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                        // Skip the element and report the null value.  It cannot have subelements.
                        while (!reader.isEndElement())
                            reader.next();

                        return null;


                    }

                    if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","type")!=null){
                        java.lang.String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                                "type");
                        if (fullTypeName!=null){
                            java.lang.String nsPrefix = null;
                            if (fullTypeName.indexOf(":") > -1){
                                nsPrefix = fullTypeName.substring(0,fullTypeName.indexOf(":"));
                            }
                            nsPrefix = nsPrefix==null?"":nsPrefix;

                            java.lang.String type = fullTypeName.substring(fullTypeName.indexOf(":")+1);

                            if (!"WSDemoneException".equals(type)){
                                //find namespace for the prefix
                                java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (WSDemoneException)ExtensionMapper.getTypeObject(
                                        nsUri,type,reader);
                            }


                        }


                    }




                    // Note all attributes that were handled. Used to differ normal attributes
                    // from anyAttributes.
                    java.util.Vector handledAttributes = new java.util.Vector();




                    reader.next();


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://webservice.demone.conservazione","WSDemoneException").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                            object.setWSDemoneException(null);
                            reader.next();

                            reader.next();

                        }else{

                            object.setWSDemoneException(Exception.Factory.parse(reader));

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
                    throw new java.lang.Exception(e);
                }

                return object;
            }

        }//end of factory class



    }


    public static class ReadLogGson_byIdJobResponse
            implements org.apache.axis2.databinding.ADBBean{

        public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName(
                "http://webservice.demone.conservazione",
                "readLogGson_byIdJobResponse",
                "ns3");



        /**
         * field for _return
         */


        protected java.lang.String local_return ;

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
        public  java.lang.String get_return(){
            return local_return;
        }



        /**
         * Auto generated setter method
         * @param param _return
         */
        public void set_return(java.lang.String param){
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




            java.lang.String prefix = null;
            java.lang.String namespace = null;


            prefix = parentQName.getPrefix();
            namespace = parentQName.getNamespaceURI();
            writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

            if (serializeType){


                java.lang.String namespacePrefix = registerPrefix(xmlWriter,"http://webservice.demone.conservazione");
                if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            namespacePrefix+":readLogGson_byIdJobResponse",
                            xmlWriter);
                } else {
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            "readLogGson_byIdJobResponse",
                            xmlWriter);
                }


            }
            if (local_returnTracker){
                namespace = "http://webservice.demone.conservazione";
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

        private static java.lang.String generatePrefix(java.lang.String namespace) {
            if(namespace.equals("http://webservice.demone.conservazione")){
                return "ns3";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
         * Utility method to write an element start tag.
         */
        private void writeStartElement(java.lang.String prefix, java.lang.String namespace, java.lang.String localPart,
                                       javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String writerPrefix = xmlWriter.getPrefix(namespace);
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
        private void writeAttribute(java.lang.String prefix,java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (xmlWriter.getPrefix(namespace) == null) {
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            xmlWriter.writeAttribute(namespace,attName,attValue);
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeAttribute(java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
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
        private void writeQNameAttribute(java.lang.String namespace, java.lang.String attName,
                                         javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            java.lang.String attributeNamespace = qname.getNamespaceURI();
            java.lang.String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
            if (attributePrefix == null) {
                attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
            }
            java.lang.String attributeValue;
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
            java.lang.String namespaceURI = qname.getNamespaceURI();
            if (namespaceURI != null) {
                java.lang.String prefix = xmlWriter.getPrefix(namespaceURI);
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
                java.lang.StringBuffer stringToWrite = new java.lang.StringBuffer();
                java.lang.String namespaceURI = null;
                java.lang.String prefix = null;

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
        private java.lang.String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, java.lang.String namespace) throws javax.xml.stream.XMLStreamException {
            java.lang.String prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null) {
                prefix = generatePrefix(namespace);
                javax.xml.namespace.NamespaceContext nsContext = xmlWriter.getNamespaceContext();
                while (true) {
                    java.lang.String uri = nsContext.getNamespaceURI(prefix);
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
                elementList.add(new javax.xml.namespace.QName("http://webservice.demone.conservazione",
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
            public static ReadLogGson_byIdJobResponse parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{
                ReadLogGson_byIdJobResponse object =
                        new ReadLogGson_byIdJobResponse();

                int event;
                java.lang.String nillableValue = null;
                java.lang.String prefix ="";
                java.lang.String namespaceuri ="";
                try {

                    while (!reader.isStartElement() && !reader.isEndElement())
                        reader.next();


                    if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","type")!=null){
                        java.lang.String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                                "type");
                        if (fullTypeName!=null){
                            java.lang.String nsPrefix = null;
                            if (fullTypeName.indexOf(":") > -1){
                                nsPrefix = fullTypeName.substring(0,fullTypeName.indexOf(":"));
                            }
                            nsPrefix = nsPrefix==null?"":nsPrefix;

                            java.lang.String type = fullTypeName.substring(fullTypeName.indexOf(":")+1);

                            if (!"readLogGson_byIdJobResponse".equals(type)){
                                //find namespace for the prefix
                                java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (ReadLogGson_byIdJobResponse)ExtensionMapper.getTypeObject(
                                        nsUri,type,reader);
                            }


                        }


                    }




                    // Note all attributes that were handled. Used to differ normal attributes
                    // from anyAttributes.
                    java.util.Vector handledAttributes = new java.util.Vector();




                    reader.next();


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://webservice.demone.conservazione","return").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                            java.lang.String content = reader.getElementText();

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
                    throw new java.lang.Exception(e);
                }

                return object;
            }

        }//end of factory class



    }


    public static class SearchLogEstesa
            implements org.apache.axis2.databinding.ADBBean{

        public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName(
                "http://webservice.demone.conservazione",
                "searchLogEstesa",
                "ns3");



        /**
         * field for SearchCriteria
         * This was an Array!
         */


        protected KeyValuePair[] localSearchCriteria ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localSearchCriteriaTracker = false ;

        public boolean isSearchCriteriaSpecified(){
            return localSearchCriteriaTracker;
        }



        /**
         * Auto generated getter method
         * @return KeyValuePair[]
         */
        public  KeyValuePair[] getSearchCriteria(){
            return localSearchCriteria;
        }






        /**
         * validate the array for SearchCriteria
         */
        protected void validateSearchCriteria(KeyValuePair[] param){

        }


        /**
         * Auto generated setter method
         * @param param SearchCriteria
         */
        public void setSearchCriteria(KeyValuePair[] param){

            validateSearchCriteria(param);

            localSearchCriteriaTracker = true;

            this.localSearchCriteria=param;
        }



        /**
         * Auto generated add method for the array for convenience
         * @param param KeyValuePair
         */
        public void addSearchCriteria(KeyValuePair param){
            if (localSearchCriteria == null){
                localSearchCriteria = new KeyValuePair[]{};
            }


            //update the setting tracker
            localSearchCriteriaTracker = true;


            java.util.List list =
                    org.apache.axis2.databinding.utils.ConverterUtil.toList(localSearchCriteria);
            list.add(param);
            this.localSearchCriteria =
                    (KeyValuePair[])list.toArray(
                            new KeyValuePair[list.size()]);

        }


        /**
         * field for MaxRows
         */


        protected int localMaxRows ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localMaxRowsTracker = false ;

        public boolean isMaxRowsSpecified(){
            return localMaxRowsTracker;
        }



        /**
         * Auto generated getter method
         * @return int
         */
        public  int getMaxRows(){
            return localMaxRows;
        }



        /**
         * Auto generated setter method
         * @param param MaxRows
         */
        public void setMaxRows(int param){

            // setting primitive attribute tracker to true
            localMaxRowsTracker =
                    param != java.lang.Integer.MIN_VALUE;

            this.localMaxRows=param;


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




            java.lang.String prefix = null;
            java.lang.String namespace = null;


            prefix = parentQName.getPrefix();
            namespace = parentQName.getNamespaceURI();
            writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

            if (serializeType){


                java.lang.String namespacePrefix = registerPrefix(xmlWriter,"http://webservice.demone.conservazione");
                if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            namespacePrefix+":searchLogEstesa",
                            xmlWriter);
                } else {
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            "searchLogEstesa",
                            xmlWriter);
                }


            }
            if (localSearchCriteriaTracker){
                if (localSearchCriteria!=null){
                    for (int i = 0;i < localSearchCriteria.length;i++){
                        if (localSearchCriteria[i] != null){
                            localSearchCriteria[i].serialize(new javax.xml.namespace.QName("http://webservice.demone.conservazione","searchCriteria"),
                                    xmlWriter);
                        } else {

                            writeStartElement(null, "http://webservice.demone.conservazione", "searchCriteria", xmlWriter);

                            // write the nil attribute
                            writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                            xmlWriter.writeEndElement();

                        }

                    }
                } else {

                    writeStartElement(null, "http://webservice.demone.conservazione", "searchCriteria", xmlWriter);

                    // write the nil attribute
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                    xmlWriter.writeEndElement();

                }
            } if (localMaxRowsTracker){
                namespace = "http://webservice.demone.conservazione";
                writeStartElement(null, namespace, "maxRows", xmlWriter);

                if (localMaxRows==java.lang.Integer.MIN_VALUE) {

                    throw new org.apache.axis2.databinding.ADBException("maxRows cannot be null!!");

                } else {
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localMaxRows));
                }

                xmlWriter.writeEndElement();
            }
            xmlWriter.writeEndElement();


        }

        private static java.lang.String generatePrefix(java.lang.String namespace) {
            if(namespace.equals("http://webservice.demone.conservazione")){
                return "ns3";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
         * Utility method to write an element start tag.
         */
        private void writeStartElement(java.lang.String prefix, java.lang.String namespace, java.lang.String localPart,
                                       javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String writerPrefix = xmlWriter.getPrefix(namespace);
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
        private void writeAttribute(java.lang.String prefix,java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (xmlWriter.getPrefix(namespace) == null) {
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            xmlWriter.writeAttribute(namespace,attName,attValue);
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeAttribute(java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
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
        private void writeQNameAttribute(java.lang.String namespace, java.lang.String attName,
                                         javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            java.lang.String attributeNamespace = qname.getNamespaceURI();
            java.lang.String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
            if (attributePrefix == null) {
                attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
            }
            java.lang.String attributeValue;
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
            java.lang.String namespaceURI = qname.getNamespaceURI();
            if (namespaceURI != null) {
                java.lang.String prefix = xmlWriter.getPrefix(namespaceURI);
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
                java.lang.StringBuffer stringToWrite = new java.lang.StringBuffer();
                java.lang.String namespaceURI = null;
                java.lang.String prefix = null;

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
        private java.lang.String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, java.lang.String namespace) throws javax.xml.stream.XMLStreamException {
            java.lang.String prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null) {
                prefix = generatePrefix(namespace);
                javax.xml.namespace.NamespaceContext nsContext = xmlWriter.getNamespaceContext();
                while (true) {
                    java.lang.String uri = nsContext.getNamespaceURI(prefix);
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

            if (localSearchCriteriaTracker){
                if (localSearchCriteria!=null) {
                    for (int i = 0;i < localSearchCriteria.length;i++){

                        if (localSearchCriteria[i] != null){
                            elementList.add(new javax.xml.namespace.QName("http://webservice.demone.conservazione",
                                    "searchCriteria"));
                            elementList.add(localSearchCriteria[i]);
                        } else {

                            elementList.add(new javax.xml.namespace.QName("http://webservice.demone.conservazione",
                                    "searchCriteria"));
                            elementList.add(null);

                        }

                    }
                } else {

                    elementList.add(new javax.xml.namespace.QName("http://webservice.demone.conservazione",
                            "searchCriteria"));
                    elementList.add(localSearchCriteria);

                }

            } if (localMaxRowsTracker){
                elementList.add(new javax.xml.namespace.QName("http://webservice.demone.conservazione",
                        "maxRows"));

                elementList.add(
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localMaxRows));
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
            public static SearchLogEstesa parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{
                SearchLogEstesa object =
                        new SearchLogEstesa();

                int event;
                java.lang.String nillableValue = null;
                java.lang.String prefix ="";
                java.lang.String namespaceuri ="";
                try {

                    while (!reader.isStartElement() && !reader.isEndElement())
                        reader.next();


                    if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","type")!=null){
                        java.lang.String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                                "type");
                        if (fullTypeName!=null){
                            java.lang.String nsPrefix = null;
                            if (fullTypeName.indexOf(":") > -1){
                                nsPrefix = fullTypeName.substring(0,fullTypeName.indexOf(":"));
                            }
                            nsPrefix = nsPrefix==null?"":nsPrefix;

                            java.lang.String type = fullTypeName.substring(fullTypeName.indexOf(":")+1);

                            if (!"searchLogEstesa".equals(type)){
                                //find namespace for the prefix
                                java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (SearchLogEstesa)ExtensionMapper.getTypeObject(
                                        nsUri,type,reader);
                            }


                        }


                    }




                    // Note all attributes that were handled. Used to differ normal attributes
                    // from anyAttributes.
                    java.util.Vector handledAttributes = new java.util.Vector();




                    reader.next();

                    java.util.ArrayList list1 = new java.util.ArrayList();


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://webservice.demone.conservazione","searchCriteria").equals(reader.getName())){



                        // Process the array and step past its final element's end.

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                            list1.add(null);
                            reader.next();
                        } else {
                            list1.add(KeyValuePair.Factory.parse(reader));
                        }
                        //loop until we find a start element that is not part of this array
                        boolean loopDone1 = false;
                        while(!loopDone1){
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
                                loopDone1 = true;
                            } else {
                                if (new javax.xml.namespace.QName("http://webservice.demone.conservazione","searchCriteria").equals(reader.getName())){

                                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                    if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                        list1.add(null);
                                        reader.next();
                                    } else {
                                        list1.add(KeyValuePair.Factory.parse(reader));
                                    }
                                }else{
                                    loopDone1 = true;
                                }
                            }
                        }
                        // call the converter utility  to convert and set the array

                        object.setSearchCriteria((KeyValuePair[])
                                org.apache.axis2.databinding.utils.ConverterUtil.convertToArray(
                                        KeyValuePair.class,
                                        list1));

                    }  // End of if for expected property start element

                    else {

                    }


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://webservice.demone.conservazione","maxRows").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                            throw new org.apache.axis2.databinding.ADBException("The element: "+"maxRows" +"  cannot be null");
                        }


                        java.lang.String content = reader.getElementText();

                        object.setMaxRows(
                                org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                        object.setMaxRows(java.lang.Integer.MIN_VALUE);

                    }

                    while (!reader.isStartElement() && !reader.isEndElement())
                        reader.next();

                    if (reader.isStartElement())
                        // A start element we are not expecting indicates a trailing invalid property
                        throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());




                } catch (javax.xml.stream.XMLStreamException e) {
                    throw new java.lang.Exception(e);
                }

                return object;
            }

        }//end of factory class



    }


    public static class ReadLogsByIdJobResponse
            implements org.apache.axis2.databinding.ADBBean{

        public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName(
                "http://webservice.demone.conservazione",
                "readLogsByIdJobResponse",
                "ns3");



        /**
         * field for _return
         * This was an Array!
         */


        protected Log[] local_return ;

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
         * @return Log[]
         */
        public  Log[] get_return(){
            return local_return;
        }






        /**
         * validate the array for _return
         */
        protected void validate_return(Log[] param){

        }


        /**
         * Auto generated setter method
         * @param param _return
         */
        public void set_return(Log[] param){

            validate_return(param);

            local_returnTracker = true;

            this.local_return=param;
        }



        /**
         * Auto generated add method for the array for convenience
         * @param param Log
         */
        public void add_return(Log param){
            if (local_return == null){
                local_return = new Log[]{};
            }


            //update the setting tracker
            local_returnTracker = true;


            java.util.List list =
                    org.apache.axis2.databinding.utils.ConverterUtil.toList(local_return);
            list.add(param);
            this.local_return =
                    (Log[])list.toArray(
                            new Log[list.size()]);

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




            java.lang.String prefix = null;
            java.lang.String namespace = null;


            prefix = parentQName.getPrefix();
            namespace = parentQName.getNamespaceURI();
            writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

            if (serializeType){


                java.lang.String namespacePrefix = registerPrefix(xmlWriter,"http://webservice.demone.conservazione");
                if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            namespacePrefix+":readLogsByIdJobResponse",
                            xmlWriter);
                } else {
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            "readLogsByIdJobResponse",
                            xmlWriter);
                }


            }
            if (local_returnTracker){
                if (local_return!=null){
                    for (int i = 0;i < local_return.length;i++){
                        if (local_return[i] != null){
                            local_return[i].serialize(new javax.xml.namespace.QName("http://webservice.demone.conservazione","return"),
                                    xmlWriter);
                        } else {

                            writeStartElement(null, "http://webservice.demone.conservazione", "return", xmlWriter);

                            // write the nil attribute
                            writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                            xmlWriter.writeEndElement();

                        }

                    }
                } else {

                    writeStartElement(null, "http://webservice.demone.conservazione", "return", xmlWriter);

                    // write the nil attribute
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                    xmlWriter.writeEndElement();

                }
            }
            xmlWriter.writeEndElement();


        }

        private static java.lang.String generatePrefix(java.lang.String namespace) {
            if(namespace.equals("http://webservice.demone.conservazione")){
                return "ns3";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
         * Utility method to write an element start tag.
         */
        private void writeStartElement(java.lang.String prefix, java.lang.String namespace, java.lang.String localPart,
                                       javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String writerPrefix = xmlWriter.getPrefix(namespace);
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
        private void writeAttribute(java.lang.String prefix,java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (xmlWriter.getPrefix(namespace) == null) {
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            xmlWriter.writeAttribute(namespace,attName,attValue);
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeAttribute(java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
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
        private void writeQNameAttribute(java.lang.String namespace, java.lang.String attName,
                                         javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            java.lang.String attributeNamespace = qname.getNamespaceURI();
            java.lang.String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
            if (attributePrefix == null) {
                attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
            }
            java.lang.String attributeValue;
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
            java.lang.String namespaceURI = qname.getNamespaceURI();
            if (namespaceURI != null) {
                java.lang.String prefix = xmlWriter.getPrefix(namespaceURI);
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
                java.lang.StringBuffer stringToWrite = new java.lang.StringBuffer();
                java.lang.String namespaceURI = null;
                java.lang.String prefix = null;

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
        private java.lang.String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, java.lang.String namespace) throws javax.xml.stream.XMLStreamException {
            java.lang.String prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null) {
                prefix = generatePrefix(namespace);
                javax.xml.namespace.NamespaceContext nsContext = xmlWriter.getNamespaceContext();
                while (true) {
                    java.lang.String uri = nsContext.getNamespaceURI(prefix);
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
                if (local_return!=null) {
                    for (int i = 0;i < local_return.length;i++){

                        if (local_return[i] != null){
                            elementList.add(new javax.xml.namespace.QName("http://webservice.demone.conservazione",
                                    "return"));
                            elementList.add(local_return[i]);
                        } else {

                            elementList.add(new javax.xml.namespace.QName("http://webservice.demone.conservazione",
                                    "return"));
                            elementList.add(null);

                        }

                    }
                } else {

                    elementList.add(new javax.xml.namespace.QName("http://webservice.demone.conservazione",
                            "return"));
                    elementList.add(local_return);

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
            public static ReadLogsByIdJobResponse parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{
                ReadLogsByIdJobResponse object =
                        new ReadLogsByIdJobResponse();

                int event;
                java.lang.String nillableValue = null;
                java.lang.String prefix ="";
                java.lang.String namespaceuri ="";
                try {

                    while (!reader.isStartElement() && !reader.isEndElement())
                        reader.next();


                    if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","type")!=null){
                        java.lang.String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                                "type");
                        if (fullTypeName!=null){
                            java.lang.String nsPrefix = null;
                            if (fullTypeName.indexOf(":") > -1){
                                nsPrefix = fullTypeName.substring(0,fullTypeName.indexOf(":"));
                            }
                            nsPrefix = nsPrefix==null?"":nsPrefix;

                            java.lang.String type = fullTypeName.substring(fullTypeName.indexOf(":")+1);

                            if (!"readLogsByIdJobResponse".equals(type)){
                                //find namespace for the prefix
                                java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (ReadLogsByIdJobResponse)ExtensionMapper.getTypeObject(
                                        nsUri,type,reader);
                            }


                        }


                    }




                    // Note all attributes that were handled. Used to differ normal attributes
                    // from anyAttributes.
                    java.util.Vector handledAttributes = new java.util.Vector();




                    reader.next();

                    java.util.ArrayList list1 = new java.util.ArrayList();


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://webservice.demone.conservazione","return").equals(reader.getName())){



                        // Process the array and step past its final element's end.

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                            list1.add(null);
                            reader.next();
                        } else {
                            list1.add(Log.Factory.parse(reader));
                        }
                        //loop until we find a start element that is not part of this array
                        boolean loopDone1 = false;
                        while(!loopDone1){
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
                                loopDone1 = true;
                            } else {
                                if (new javax.xml.namespace.QName("http://webservice.demone.conservazione","return").equals(reader.getName())){

                                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                    if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                        list1.add(null);
                                        reader.next();
                                    } else {
                                        list1.add(Log.Factory.parse(reader));
                                    }
                                }else{
                                    loopDone1 = true;
                                }
                            }
                        }
                        // call the converter utility  to convert and set the array

                        object.set_return((Log[])
                                org.apache.axis2.databinding.utils.ConverterUtil.convertToArray(
                                        Log.class,
                                        list1));

                    }  // End of if for expected property start element

                    else {

                    }

                    while (!reader.isStartElement() && !reader.isEndElement())
                        reader.next();

                    if (reader.isStartElement())
                        // A start element we are not expecting indicates a trailing invalid property
                        throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());




                } catch (javax.xml.stream.XMLStreamException e) {
                    throw new java.lang.Exception(e);
                }

                return object;
            }

        }//end of factory class



    }


    public static class DeleteJob_byIdJobResponse
            implements org.apache.axis2.databinding.ADBBean{

        public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName(
                "http://webservice.demone.conservazione",
                "deleteJob_byIdJobResponse",
                "ns3");



        /**
         * field for _return
         */


        protected boolean local_return ;

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
         * @return boolean
         */
        public  boolean get_return(){
            return local_return;
        }



        /**
         * Auto generated setter method
         * @param param _return
         */
        public void set_return(boolean param){

            // setting primitive attribute tracker to true
            local_returnTracker =
                    true;

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




            java.lang.String prefix = null;
            java.lang.String namespace = null;


            prefix = parentQName.getPrefix();
            namespace = parentQName.getNamespaceURI();
            writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

            if (serializeType){


                java.lang.String namespacePrefix = registerPrefix(xmlWriter,"http://webservice.demone.conservazione");
                if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            namespacePrefix+":deleteJob_byIdJobResponse",
                            xmlWriter);
                } else {
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            "deleteJob_byIdJobResponse",
                            xmlWriter);
                }


            }
            if (local_returnTracker){
                namespace = "http://webservice.demone.conservazione";
                writeStartElement(null, namespace, "return", xmlWriter);

                if (false) {

                    throw new org.apache.axis2.databinding.ADBException("return cannot be null!!");

                } else {
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(local_return));
                }

                xmlWriter.writeEndElement();
            }
            xmlWriter.writeEndElement();


        }

        private static java.lang.String generatePrefix(java.lang.String namespace) {
            if(namespace.equals("http://webservice.demone.conservazione")){
                return "ns3";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
         * Utility method to write an element start tag.
         */
        private void writeStartElement(java.lang.String prefix, java.lang.String namespace, java.lang.String localPart,
                                       javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String writerPrefix = xmlWriter.getPrefix(namespace);
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
        private void writeAttribute(java.lang.String prefix,java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (xmlWriter.getPrefix(namespace) == null) {
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            xmlWriter.writeAttribute(namespace,attName,attValue);
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeAttribute(java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
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
        private void writeQNameAttribute(java.lang.String namespace, java.lang.String attName,
                                         javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            java.lang.String attributeNamespace = qname.getNamespaceURI();
            java.lang.String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
            if (attributePrefix == null) {
                attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
            }
            java.lang.String attributeValue;
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
            java.lang.String namespaceURI = qname.getNamespaceURI();
            if (namespaceURI != null) {
                java.lang.String prefix = xmlWriter.getPrefix(namespaceURI);
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
                java.lang.StringBuffer stringToWrite = new java.lang.StringBuffer();
                java.lang.String namespaceURI = null;
                java.lang.String prefix = null;

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
        private java.lang.String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, java.lang.String namespace) throws javax.xml.stream.XMLStreamException {
            java.lang.String prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null) {
                prefix = generatePrefix(namespace);
                javax.xml.namespace.NamespaceContext nsContext = xmlWriter.getNamespaceContext();
                while (true) {
                    java.lang.String uri = nsContext.getNamespaceURI(prefix);
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
                elementList.add(new javax.xml.namespace.QName("http://webservice.demone.conservazione",
                        "return"));

                elementList.add(
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
            public static DeleteJob_byIdJobResponse parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{
                DeleteJob_byIdJobResponse object =
                        new DeleteJob_byIdJobResponse();

                int event;
                java.lang.String nillableValue = null;
                java.lang.String prefix ="";
                java.lang.String namespaceuri ="";
                try {

                    while (!reader.isStartElement() && !reader.isEndElement())
                        reader.next();


                    if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","type")!=null){
                        java.lang.String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                                "type");
                        if (fullTypeName!=null){
                            java.lang.String nsPrefix = null;
                            if (fullTypeName.indexOf(":") > -1){
                                nsPrefix = fullTypeName.substring(0,fullTypeName.indexOf(":"));
                            }
                            nsPrefix = nsPrefix==null?"":nsPrefix;

                            java.lang.String type = fullTypeName.substring(fullTypeName.indexOf(":")+1);

                            if (!"deleteJob_byIdJobResponse".equals(type)){
                                //find namespace for the prefix
                                java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (DeleteJob_byIdJobResponse)ExtensionMapper.getTypeObject(
                                        nsUri,type,reader);
                            }


                        }


                    }




                    // Note all attributes that were handled. Used to differ normal attributes
                    // from anyAttributes.
                    java.util.Vector handledAttributes = new java.util.Vector();




                    reader.next();


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://webservice.demone.conservazione","return").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                            throw new org.apache.axis2.databinding.ADBException("The element: "+"return" +"  cannot be null");
                        }


                        java.lang.String content = reader.getElementText();

                        object.set_return(
                                org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));

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
                    throw new java.lang.Exception(e);
                }

                return object;
            }

        }//end of factory class



    }


    public static class GetJobByDocIdResponse
            implements org.apache.axis2.databinding.ADBBean{

        public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName(
                "http://webservice.demone.conservazione",
                "getJobByDocIdResponse",
                "ns3");



        /**
         * field for _return
         */


        protected Job local_return ;

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
         * @return Job
         */
        public  Job get_return(){
            return local_return;
        }



        /**
         * Auto generated setter method
         * @param param _return
         */
        public void set_return(Job param){
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




            java.lang.String prefix = null;
            java.lang.String namespace = null;


            prefix = parentQName.getPrefix();
            namespace = parentQName.getNamespaceURI();
            writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

            if (serializeType){


                java.lang.String namespacePrefix = registerPrefix(xmlWriter,"http://webservice.demone.conservazione");
                if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            namespacePrefix+":getJobByDocIdResponse",
                            xmlWriter);
                } else {
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            "getJobByDocIdResponse",
                            xmlWriter);
                }


            }
            if (local_returnTracker){
                if (local_return==null){

                    writeStartElement(null, "http://webservice.demone.conservazione", "return", xmlWriter);

                    // write the nil attribute
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                    xmlWriter.writeEndElement();
                }else{
                    local_return.serialize(new javax.xml.namespace.QName("http://webservice.demone.conservazione","return"),
                            xmlWriter);
                }
            }
            xmlWriter.writeEndElement();


        }

        private static java.lang.String generatePrefix(java.lang.String namespace) {
            if(namespace.equals("http://webservice.demone.conservazione")){
                return "ns3";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
         * Utility method to write an element start tag.
         */
        private void writeStartElement(java.lang.String prefix, java.lang.String namespace, java.lang.String localPart,
                                       javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String writerPrefix = xmlWriter.getPrefix(namespace);
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
        private void writeAttribute(java.lang.String prefix,java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (xmlWriter.getPrefix(namespace) == null) {
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            xmlWriter.writeAttribute(namespace,attName,attValue);
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeAttribute(java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
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
        private void writeQNameAttribute(java.lang.String namespace, java.lang.String attName,
                                         javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            java.lang.String attributeNamespace = qname.getNamespaceURI();
            java.lang.String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
            if (attributePrefix == null) {
                attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
            }
            java.lang.String attributeValue;
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
            java.lang.String namespaceURI = qname.getNamespaceURI();
            if (namespaceURI != null) {
                java.lang.String prefix = xmlWriter.getPrefix(namespaceURI);
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
                java.lang.StringBuffer stringToWrite = new java.lang.StringBuffer();
                java.lang.String namespaceURI = null;
                java.lang.String prefix = null;

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
        private java.lang.String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, java.lang.String namespace) throws javax.xml.stream.XMLStreamException {
            java.lang.String prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null) {
                prefix = generatePrefix(namespace);
                javax.xml.namespace.NamespaceContext nsContext = xmlWriter.getNamespaceContext();
                while (true) {
                    java.lang.String uri = nsContext.getNamespaceURI(prefix);
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
                elementList.add(new javax.xml.namespace.QName("http://webservice.demone.conservazione",
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
            public static GetJobByDocIdResponse parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{
                GetJobByDocIdResponse object =
                        new GetJobByDocIdResponse();

                int event;
                java.lang.String nillableValue = null;
                java.lang.String prefix ="";
                java.lang.String namespaceuri ="";
                try {

                    while (!reader.isStartElement() && !reader.isEndElement())
                        reader.next();


                    if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","type")!=null){
                        java.lang.String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                                "type");
                        if (fullTypeName!=null){
                            java.lang.String nsPrefix = null;
                            if (fullTypeName.indexOf(":") > -1){
                                nsPrefix = fullTypeName.substring(0,fullTypeName.indexOf(":"));
                            }
                            nsPrefix = nsPrefix==null?"":nsPrefix;

                            java.lang.String type = fullTypeName.substring(fullTypeName.indexOf(":")+1);

                            if (!"getJobByDocIdResponse".equals(type)){
                                //find namespace for the prefix
                                java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (GetJobByDocIdResponse)ExtensionMapper.getTypeObject(
                                        nsUri,type,reader);
                            }


                        }


                    }




                    // Note all attributes that were handled. Used to differ normal attributes
                    // from anyAttributes.
                    java.util.Vector handledAttributes = new java.util.Vector();




                    reader.next();


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://webservice.demone.conservazione","return").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                            object.set_return(null);
                            reader.next();

                            reader.next();

                        }else{

                            object.set_return(Job.Factory.parse(reader));

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
                    throw new java.lang.Exception(e);
                }

                return object;
            }

        }//end of factory class



    }


    public static class GetJobResponse
            implements org.apache.axis2.databinding.ADBBean{

        public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName(
                "http://webservice.demone.conservazione",
                "getJobResponse",
                "ns3");



        /**
         * field for _return
         */


        protected Job local_return ;

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
         * @return Job
         */
        public  Job get_return(){
            return local_return;
        }



        /**
         * Auto generated setter method
         * @param param _return
         */
        public void set_return(Job param){
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




            java.lang.String prefix = null;
            java.lang.String namespace = null;


            prefix = parentQName.getPrefix();
            namespace = parentQName.getNamespaceURI();
            writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

            if (serializeType){


                java.lang.String namespacePrefix = registerPrefix(xmlWriter,"http://webservice.demone.conservazione");
                if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            namespacePrefix+":getJobResponse",
                            xmlWriter);
                } else {
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            "getJobResponse",
                            xmlWriter);
                }


            }
            if (local_returnTracker){
                if (local_return==null){

                    writeStartElement(null, "http://webservice.demone.conservazione", "return", xmlWriter);

                    // write the nil attribute
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                    xmlWriter.writeEndElement();
                }else{
                    local_return.serialize(new javax.xml.namespace.QName("http://webservice.demone.conservazione","return"),
                            xmlWriter);
                }
            }
            xmlWriter.writeEndElement();


        }

        private static java.lang.String generatePrefix(java.lang.String namespace) {
            if(namespace.equals("http://webservice.demone.conservazione")){
                return "ns3";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
         * Utility method to write an element start tag.
         */
        private void writeStartElement(java.lang.String prefix, java.lang.String namespace, java.lang.String localPart,
                                       javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String writerPrefix = xmlWriter.getPrefix(namespace);
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
        private void writeAttribute(java.lang.String prefix,java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (xmlWriter.getPrefix(namespace) == null) {
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            xmlWriter.writeAttribute(namespace,attName,attValue);
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeAttribute(java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
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
        private void writeQNameAttribute(java.lang.String namespace, java.lang.String attName,
                                         javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            java.lang.String attributeNamespace = qname.getNamespaceURI();
            java.lang.String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
            if (attributePrefix == null) {
                attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
            }
            java.lang.String attributeValue;
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
            java.lang.String namespaceURI = qname.getNamespaceURI();
            if (namespaceURI != null) {
                java.lang.String prefix = xmlWriter.getPrefix(namespaceURI);
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
                java.lang.StringBuffer stringToWrite = new java.lang.StringBuffer();
                java.lang.String namespaceURI = null;
                java.lang.String prefix = null;

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
        private java.lang.String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, java.lang.String namespace) throws javax.xml.stream.XMLStreamException {
            java.lang.String prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null) {
                prefix = generatePrefix(namespace);
                javax.xml.namespace.NamespaceContext nsContext = xmlWriter.getNamespaceContext();
                while (true) {
                    java.lang.String uri = nsContext.getNamespaceURI(prefix);
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
                elementList.add(new javax.xml.namespace.QName("http://webservice.demone.conservazione",
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
            public static GetJobResponse parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{
                GetJobResponse object =
                        new GetJobResponse();

                int event;
                java.lang.String nillableValue = null;
                java.lang.String prefix ="";
                java.lang.String namespaceuri ="";
                try {

                    while (!reader.isStartElement() && !reader.isEndElement())
                        reader.next();


                    if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","type")!=null){
                        java.lang.String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                                "type");
                        if (fullTypeName!=null){
                            java.lang.String nsPrefix = null;
                            if (fullTypeName.indexOf(":") > -1){
                                nsPrefix = fullTypeName.substring(0,fullTypeName.indexOf(":"));
                            }
                            nsPrefix = nsPrefix==null?"":nsPrefix;

                            java.lang.String type = fullTypeName.substring(fullTypeName.indexOf(":")+1);

                            if (!"getJobResponse".equals(type)){
                                //find namespace for the prefix
                                java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (GetJobResponse)ExtensionMapper.getTypeObject(
                                        nsUri,type,reader);
                            }


                        }


                    }




                    // Note all attributes that were handled. Used to differ normal attributes
                    // from anyAttributes.
                    java.util.Vector handledAttributes = new java.util.Vector();




                    reader.next();


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://webservice.demone.conservazione","return").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                            object.set_return(null);
                            reader.next();

                            reader.next();

                        }else{

                            object.set_return(Job.Factory.parse(reader));

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
                    throw new java.lang.Exception(e);
                }

                return object;
            }

        }//end of factory class



    }


    public static class AddJob
            implements org.apache.axis2.databinding.ADBBean{

        public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName(
                "http://webservice.demone.conservazione",
                "addJob",
                "ns3");



        /**
         * field for Web_service
         */


        protected java.lang.String localWeb_service ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localWeb_serviceTracker = false ;

        public boolean isWeb_serviceSpecified(){
            return localWeb_serviceTracker;
        }



        /**
         * Auto generated getter method
         * @return java.lang.String
         */
        public  java.lang.String getWeb_service(){
            return localWeb_service;
        }



        /**
         * Auto generated setter method
         * @param param Web_service
         */
        public void setWeb_service(java.lang.String param){
            localWeb_serviceTracker = true;

            this.localWeb_service=param;


        }


        /**
         * field for DocId
         */


        protected java.lang.String localDocId ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localDocIdTracker = false ;

        public boolean isDocIdSpecified(){
            return localDocIdTracker;
        }



        /**
         * Auto generated getter method
         * @return java.lang.String
         */
        public  java.lang.String getDocId(){
            return localDocId;
        }



        /**
         * Auto generated setter method
         * @param param DocId
         */
        public void setDocId(java.lang.String param){
            localDocIdTracker = true;

            this.localDocId=param;


        }


        /**
         * field for DocumentoPrincipale
         */


        protected java.lang.String localDocumentoPrincipale ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localDocumentoPrincipaleTracker = false ;

        public boolean isDocumentoPrincipaleSpecified(){
            return localDocumentoPrincipaleTracker;
        }



        /**
         * Auto generated getter method
         * @return java.lang.String
         */
        public  java.lang.String getDocumentoPrincipale(){
            return localDocumentoPrincipale;
        }



        /**
         * Auto generated setter method
         * @param param DocumentoPrincipale
         */
        public void setDocumentoPrincipale(java.lang.String param){
            localDocumentoPrincipaleTracker = true;

            this.localDocumentoPrincipale=param;


        }


        /**
         * field for Allegati
         */


        protected java.lang.String localAllegati ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localAllegatiTracker = false ;

        public boolean isAllegatiSpecified(){
            return localAllegatiTracker;
        }



        /**
         * Auto generated getter method
         * @return java.lang.String
         */
        public  java.lang.String getAllegati(){
            return localAllegati;
        }



        /**
         * Auto generated setter method
         * @param param Allegati
         */
        public void setAllegati(java.lang.String param){
            localAllegatiTracker = true;

            this.localAllegati=param;


        }


        /**
         * field for Files
         */


        protected java.lang.String localFiles ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localFilesTracker = false ;

        public boolean isFilesSpecified(){
            return localFilesTracker;
        }



        /**
         * Auto generated getter method
         * @return java.lang.String
         */
        public  java.lang.String getFiles(){
            return localFiles;
        }



        /**
         * Auto generated setter method
         * @param param Files
         */
        public void setFiles(java.lang.String param){
            localFilesTracker = true;

            this.localFiles=param;


        }


        /**
         * field for TipoConservazione
         */


        protected java.lang.String localTipoConservazione ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localTipoConservazioneTracker = false ;

        public boolean isTipoConservazioneSpecified(){
            return localTipoConservazioneTracker;
        }



        /**
         * Auto generated getter method
         * @return java.lang.String
         */
        public  java.lang.String getTipoConservazione(){
            return localTipoConservazione;
        }



        /**
         * Auto generated setter method
         * @param param TipoConservazione
         */
        public void setTipoConservazione(java.lang.String param){
            localTipoConservazioneTracker = true;

            this.localTipoConservazione=param;


        }


        /**
         * field for TipoDocumento
         */


        protected java.lang.String localTipoDocumento ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localTipoDocumentoTracker = false ;

        public boolean isTipoDocumentoSpecified(){
            return localTipoDocumentoTracker;
        }



        /**
         * Auto generated getter method
         * @return java.lang.String
         */
        public  java.lang.String getTipoDocumento(){
            return localTipoDocumento;
        }



        /**
         * Auto generated setter method
         * @param param TipoDocumento
         */
        public void setTipoDocumento(java.lang.String param){
            localTipoDocumentoTracker = true;

            this.localTipoDocumento=param;


        }


        /**
         * field for ApplicazioneChiamante
         */


        protected java.lang.String localApplicazioneChiamante ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localApplicazioneChiamanteTracker = false ;

        public boolean isApplicazioneChiamanteSpecified(){
            return localApplicazioneChiamanteTracker;
        }



        /**
         * Auto generated getter method
         * @return java.lang.String
         */
        public  java.lang.String getApplicazioneChiamante(){
            return localApplicazioneChiamante;
        }



        /**
         * Auto generated setter method
         * @param param ApplicazioneChiamante
         */
        public void setApplicazioneChiamante(java.lang.String param){
            localApplicazioneChiamanteTracker = true;

            this.localApplicazioneChiamante=param;


        }


        /**
         * field for ForzaCollegamento
         */


        protected boolean localForzaCollegamento ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localForzaCollegamentoTracker = false ;

        public boolean isForzaCollegamentoSpecified(){
            return localForzaCollegamentoTracker;
        }



        /**
         * Auto generated getter method
         * @return boolean
         */
        public  boolean getForzaCollegamento(){
            return localForzaCollegamento;
        }



        /**
         * Auto generated setter method
         * @param param ForzaCollegamento
         */
        public void setForzaCollegamento(boolean param){

            // setting primitive attribute tracker to true
            localForzaCollegamentoTracker =
                    true;

            this.localForzaCollegamento=param;


        }


        /**
         * field for ForzaAccettazione
         */


        protected boolean localForzaAccettazione ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localForzaAccettazioneTracker = false ;

        public boolean isForzaAccettazioneSpecified(){
            return localForzaAccettazioneTracker;
        }



        /**
         * Auto generated getter method
         * @return boolean
         */
        public  boolean getForzaAccettazione(){
            return localForzaAccettazione;
        }



        /**
         * Auto generated setter method
         * @param param ForzaAccettazione
         */
        public void setForzaAccettazione(boolean param){

            // setting primitive attribute tracker to true
            localForzaAccettazioneTracker =
                    true;

            this.localForzaAccettazione=param;


        }


        /**
         * field for ForzaConservazione
         */


        protected boolean localForzaConservazione ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localForzaConservazioneTracker = false ;

        public boolean isForzaConservazioneSpecified(){
            return localForzaConservazioneTracker;
        }



        /**
         * Auto generated getter method
         * @return boolean
         */
        public  boolean getForzaConservazione(){
            return localForzaConservazione;
        }



        /**
         * Auto generated setter method
         * @param param ForzaConservazione
         */
        public void setForzaConservazione(boolean param){

            // setting primitive attribute tracker to true
            localForzaConservazioneTracker =
                    true;

            this.localForzaConservazione=param;


        }


        /**
         * field for DataRegistrazione
         */


        protected java.util.Calendar localDataRegistrazione ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localDataRegistrazioneTracker = false ;

        public boolean isDataRegistrazioneSpecified(){
            return localDataRegistrazioneTracker;
        }



        /**
         * Auto generated getter method
         * @return java.util.Calendar
         */
        public  java.util.Calendar getDataRegistrazione(){
            return localDataRegistrazione;
        }



        /**
         * Auto generated setter method
         * @param param DataRegistrazione
         */
        public void setDataRegistrazione(java.util.Calendar param){
            localDataRegistrazioneTracker = true;

            this.localDataRegistrazione=param;


        }


        /**
         * field for Chiave_doc
         */


        protected java.lang.String localChiave_doc ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localChiave_docTracker = false ;

        public boolean isChiave_docSpecified(){
            return localChiave_docTracker;
        }



        /**
         * Auto generated getter method
         * @return java.lang.String
         */
        public  java.lang.String getChiave_doc(){
            return localChiave_doc;
        }



        /**
         * Auto generated setter method
         * @param param Chiave_doc
         */
        public void setChiave_doc(java.lang.String param){
            localChiave_docTracker = true;

            this.localChiave_doc=param;


        }


        /**
         * field for CodEnte
         */


        protected java.lang.String localCodEnte ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localCodEnteTracker = false ;

        public boolean isCodEnteSpecified(){
            return localCodEnteTracker;
        }



        /**
         * Auto generated getter method
         * @return java.lang.String
         */
        public  java.lang.String getCodEnte(){
            return localCodEnte;
        }



        /**
         * Auto generated setter method
         * @param param CodEnte
         */
        public void setCodEnte(java.lang.String param){
            localCodEnteTracker = true;

            this.localCodEnte=param;


        }


        /**
         * field for CodAoo
         */


        protected java.lang.String localCodAoo ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localCodAooTracker = false ;

        public boolean isCodAooSpecified(){
            return localCodAooTracker;
        }



        /**
         * Auto generated getter method
         * @return java.lang.String
         */
        public  java.lang.String getCodAoo(){
            return localCodAoo;
        }



        /**
         * Auto generated setter method
         * @param param CodAoo
         */
        public void setCodAoo(java.lang.String param){
            localCodAooTracker = true;

            this.localCodAoo=param;


        }


        /**
         * field for Azione
         */


        protected java.lang.String localAzione ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localAzioneTracker = false ;

        public boolean isAzioneSpecified(){
            return localAzioneTracker;
        }



        /**
         * Auto generated getter method
         * @return java.lang.String
         */
        public  java.lang.String getAzione(){
            return localAzione;
        }



        /**
         * Auto generated setter method
         * @param param Azione
         */
        public void setAzione(java.lang.String param){
            localAzioneTracker = true;

            this.localAzione=param;


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




            java.lang.String prefix = null;
            java.lang.String namespace = null;


            prefix = parentQName.getPrefix();
            namespace = parentQName.getNamespaceURI();
            writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

            if (serializeType){


                java.lang.String namespacePrefix = registerPrefix(xmlWriter,"http://webservice.demone.conservazione");
                if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            namespacePrefix+":addJob",
                            xmlWriter);
                } else {
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            "addJob",
                            xmlWriter);
                }


            }
            if (localWeb_serviceTracker){
                namespace = "http://webservice.demone.conservazione";
                writeStartElement(null, namespace, "web_service", xmlWriter);


                if (localWeb_service==null){
                    // write the nil attribute

                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                }else{


                    xmlWriter.writeCharacters(localWeb_service);

                }

                xmlWriter.writeEndElement();
            } if (localDocIdTracker){
                namespace = "http://webservice.demone.conservazione";
                writeStartElement(null, namespace, "docId", xmlWriter);


                if (localDocId==null){
                    // write the nil attribute

                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                }else{


                    xmlWriter.writeCharacters(localDocId);

                }

                xmlWriter.writeEndElement();
            } if (localDocumentoPrincipaleTracker){
                namespace = "http://webservice.demone.conservazione";
                writeStartElement(null, namespace, "documentoPrincipale", xmlWriter);


                if (localDocumentoPrincipale==null){
                    // write the nil attribute

                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                }else{


                    xmlWriter.writeCharacters(localDocumentoPrincipale);

                }

                xmlWriter.writeEndElement();
            } if (localAllegatiTracker){
                namespace = "http://webservice.demone.conservazione";
                writeStartElement(null, namespace, "allegati", xmlWriter);


                if (localAllegati==null){
                    // write the nil attribute

                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                }else{


                    xmlWriter.writeCharacters(localAllegati);

                }

                xmlWriter.writeEndElement();
            } if (localFilesTracker){
                namespace = "http://webservice.demone.conservazione";
                writeStartElement(null, namespace, "files", xmlWriter);


                if (localFiles==null){
                    // write the nil attribute

                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                }else{


                    xmlWriter.writeCharacters(localFiles);

                }

                xmlWriter.writeEndElement();
            } if (localTipoConservazioneTracker){
                namespace = "http://webservice.demone.conservazione";
                writeStartElement(null, namespace, "tipoConservazione", xmlWriter);


                if (localTipoConservazione==null){
                    // write the nil attribute

                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                }else{


                    xmlWriter.writeCharacters(localTipoConservazione);

                }

                xmlWriter.writeEndElement();
            } if (localTipoDocumentoTracker){
                namespace = "http://webservice.demone.conservazione";
                writeStartElement(null, namespace, "tipoDocumento", xmlWriter);


                if (localTipoDocumento==null){
                    // write the nil attribute

                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                }else{


                    xmlWriter.writeCharacters(localTipoDocumento);

                }

                xmlWriter.writeEndElement();
            } if (localApplicazioneChiamanteTracker){
                namespace = "http://webservice.demone.conservazione";
                writeStartElement(null, namespace, "applicazioneChiamante", xmlWriter);


                if (localApplicazioneChiamante==null){
                    // write the nil attribute

                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                }else{


                    xmlWriter.writeCharacters(localApplicazioneChiamante);

                }

                xmlWriter.writeEndElement();
            } if (localForzaCollegamentoTracker){
                namespace = "http://webservice.demone.conservazione";
                writeStartElement(null, namespace, "forzaCollegamento", xmlWriter);

                if (false) {

                    throw new org.apache.axis2.databinding.ADBException("forzaCollegamento cannot be null!!");

                } else {
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localForzaCollegamento));
                }

                xmlWriter.writeEndElement();
            } if (localForzaAccettazioneTracker){
                namespace = "http://webservice.demone.conservazione";
                writeStartElement(null, namespace, "forzaAccettazione", xmlWriter);

                if (false) {

                    throw new org.apache.axis2.databinding.ADBException("forzaAccettazione cannot be null!!");

                } else {
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localForzaAccettazione));
                }

                xmlWriter.writeEndElement();
            } if (localForzaConservazioneTracker){
                namespace = "http://webservice.demone.conservazione";
                writeStartElement(null, namespace, "forzaConservazione", xmlWriter);

                if (false) {

                    throw new org.apache.axis2.databinding.ADBException("forzaConservazione cannot be null!!");

                } else {
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localForzaConservazione));
                }

                xmlWriter.writeEndElement();
            } if (localDataRegistrazioneTracker){
                namespace = "http://webservice.demone.conservazione";
                writeStartElement(null, namespace, "dataRegistrazione", xmlWriter);


                if (localDataRegistrazione==null){
                    // write the nil attribute

                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                }else{


                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDataRegistrazione));

                }

                xmlWriter.writeEndElement();
            } if (localChiave_docTracker){
                namespace = "http://webservice.demone.conservazione";
                writeStartElement(null, namespace, "chiave_doc", xmlWriter);


                if (localChiave_doc==null){
                    // write the nil attribute

                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                }else{


                    xmlWriter.writeCharacters(localChiave_doc);

                }

                xmlWriter.writeEndElement();
            } if (localCodEnteTracker){
                namespace = "http://webservice.demone.conservazione";
                writeStartElement(null, namespace, "codEnte", xmlWriter);


                if (localCodEnte==null){
                    // write the nil attribute

                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                }else{


                    xmlWriter.writeCharacters(localCodEnte);

                }

                xmlWriter.writeEndElement();
            } if (localCodAooTracker){
                namespace = "http://webservice.demone.conservazione";
                writeStartElement(null, namespace, "codAoo", xmlWriter);


                if (localCodAoo==null){
                    // write the nil attribute

                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                }else{


                    xmlWriter.writeCharacters(localCodAoo);

                }

                xmlWriter.writeEndElement();
            } if (localAzioneTracker){
                namespace = "http://webservice.demone.conservazione";
                writeStartElement(null, namespace, "azione", xmlWriter);


                if (localAzione==null){
                    // write the nil attribute

                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                }else{


                    xmlWriter.writeCharacters(localAzione);

                }

                xmlWriter.writeEndElement();
            }
            xmlWriter.writeEndElement();


        }

        private static java.lang.String generatePrefix(java.lang.String namespace) {
            if(namespace.equals("http://webservice.demone.conservazione")){
                return "ns3";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
         * Utility method to write an element start tag.
         */
        private void writeStartElement(java.lang.String prefix, java.lang.String namespace, java.lang.String localPart,
                                       javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String writerPrefix = xmlWriter.getPrefix(namespace);
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
        private void writeAttribute(java.lang.String prefix,java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (xmlWriter.getPrefix(namespace) == null) {
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            xmlWriter.writeAttribute(namespace,attName,attValue);
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeAttribute(java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
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
        private void writeQNameAttribute(java.lang.String namespace, java.lang.String attName,
                                         javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            java.lang.String attributeNamespace = qname.getNamespaceURI();
            java.lang.String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
            if (attributePrefix == null) {
                attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
            }
            java.lang.String attributeValue;
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
            java.lang.String namespaceURI = qname.getNamespaceURI();
            if (namespaceURI != null) {
                java.lang.String prefix = xmlWriter.getPrefix(namespaceURI);
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
                java.lang.StringBuffer stringToWrite = new java.lang.StringBuffer();
                java.lang.String namespaceURI = null;
                java.lang.String prefix = null;

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
        private java.lang.String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, java.lang.String namespace) throws javax.xml.stream.XMLStreamException {
            java.lang.String prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null) {
                prefix = generatePrefix(namespace);
                javax.xml.namespace.NamespaceContext nsContext = xmlWriter.getNamespaceContext();
                while (true) {
                    java.lang.String uri = nsContext.getNamespaceURI(prefix);
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

            if (localWeb_serviceTracker){
                elementList.add(new javax.xml.namespace.QName("http://webservice.demone.conservazione",
                        "web_service"));

                elementList.add(localWeb_service==null?null:
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localWeb_service));
            } if (localDocIdTracker){
                elementList.add(new javax.xml.namespace.QName("http://webservice.demone.conservazione",
                        "docId"));

                elementList.add(localDocId==null?null:
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDocId));
            } if (localDocumentoPrincipaleTracker){
                elementList.add(new javax.xml.namespace.QName("http://webservice.demone.conservazione",
                        "documentoPrincipale"));

                elementList.add(localDocumentoPrincipale==null?null:
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDocumentoPrincipale));
            } if (localAllegatiTracker){
                elementList.add(new javax.xml.namespace.QName("http://webservice.demone.conservazione",
                        "allegati"));

                elementList.add(localAllegati==null?null:
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localAllegati));
            } if (localFilesTracker){
                elementList.add(new javax.xml.namespace.QName("http://webservice.demone.conservazione",
                        "files"));

                elementList.add(localFiles==null?null:
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localFiles));
            } if (localTipoConservazioneTracker){
                elementList.add(new javax.xml.namespace.QName("http://webservice.demone.conservazione",
                        "tipoConservazione"));

                elementList.add(localTipoConservazione==null?null:
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localTipoConservazione));
            } if (localTipoDocumentoTracker){
                elementList.add(new javax.xml.namespace.QName("http://webservice.demone.conservazione",
                        "tipoDocumento"));

                elementList.add(localTipoDocumento==null?null:
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localTipoDocumento));
            } if (localApplicazioneChiamanteTracker){
                elementList.add(new javax.xml.namespace.QName("http://webservice.demone.conservazione",
                        "applicazioneChiamante"));

                elementList.add(localApplicazioneChiamante==null?null:
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localApplicazioneChiamante));
            } if (localForzaCollegamentoTracker){
                elementList.add(new javax.xml.namespace.QName("http://webservice.demone.conservazione",
                        "forzaCollegamento"));

                elementList.add(
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localForzaCollegamento));
            } if (localForzaAccettazioneTracker){
                elementList.add(new javax.xml.namespace.QName("http://webservice.demone.conservazione",
                        "forzaAccettazione"));

                elementList.add(
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localForzaAccettazione));
            } if (localForzaConservazioneTracker){
                elementList.add(new javax.xml.namespace.QName("http://webservice.demone.conservazione",
                        "forzaConservazione"));

                elementList.add(
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localForzaConservazione));
            } if (localDataRegistrazioneTracker){
                elementList.add(new javax.xml.namespace.QName("http://webservice.demone.conservazione",
                        "dataRegistrazione"));

                elementList.add(localDataRegistrazione==null?null:
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDataRegistrazione));
            } if (localChiave_docTracker){
                elementList.add(new javax.xml.namespace.QName("http://webservice.demone.conservazione",
                        "chiave_doc"));

                elementList.add(localChiave_doc==null?null:
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localChiave_doc));
            } if (localCodEnteTracker){
                elementList.add(new javax.xml.namespace.QName("http://webservice.demone.conservazione",
                        "codEnte"));

                elementList.add(localCodEnte==null?null:
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localCodEnte));
            } if (localCodAooTracker){
                elementList.add(new javax.xml.namespace.QName("http://webservice.demone.conservazione",
                        "codAoo"));

                elementList.add(localCodAoo==null?null:
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localCodAoo));
            } if (localAzioneTracker){
                elementList.add(new javax.xml.namespace.QName("http://webservice.demone.conservazione",
                        "azione"));

                elementList.add(localAzione==null?null:
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localAzione));
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
            public static AddJob parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{
                AddJob object =
                        new AddJob();

                int event;
                java.lang.String nillableValue = null;
                java.lang.String prefix ="";
                java.lang.String namespaceuri ="";
                try {

                    while (!reader.isStartElement() && !reader.isEndElement())
                        reader.next();


                    if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","type")!=null){
                        java.lang.String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                                "type");
                        if (fullTypeName!=null){
                            java.lang.String nsPrefix = null;
                            if (fullTypeName.indexOf(":") > -1){
                                nsPrefix = fullTypeName.substring(0,fullTypeName.indexOf(":"));
                            }
                            nsPrefix = nsPrefix==null?"":nsPrefix;

                            java.lang.String type = fullTypeName.substring(fullTypeName.indexOf(":")+1);

                            if (!"addJob".equals(type)){
                                //find namespace for the prefix
                                java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (AddJob)ExtensionMapper.getTypeObject(
                                        nsUri,type,reader);
                            }


                        }


                    }




                    // Note all attributes that were handled. Used to differ normal attributes
                    // from anyAttributes.
                    java.util.Vector handledAttributes = new java.util.Vector();




                    reader.next();


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://webservice.demone.conservazione","web_service").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                            java.lang.String content = reader.getElementText();

                            object.setWeb_service(
                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                        } else {


                            reader.getElementText(); // throw away text nodes if any.
                        }

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                    }


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://webservice.demone.conservazione","docId").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                            java.lang.String content = reader.getElementText();

                            object.setDocId(
                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                        } else {


                            reader.getElementText(); // throw away text nodes if any.
                        }

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                    }


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://webservice.demone.conservazione","documentoPrincipale").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                            java.lang.String content = reader.getElementText();

                            object.setDocumentoPrincipale(
                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                        } else {


                            reader.getElementText(); // throw away text nodes if any.
                        }

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                    }


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://webservice.demone.conservazione","allegati").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                            java.lang.String content = reader.getElementText();

                            object.setAllegati(
                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                        } else {


                            reader.getElementText(); // throw away text nodes if any.
                        }

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                    }


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://webservice.demone.conservazione","files").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                            java.lang.String content = reader.getElementText();

                            object.setFiles(
                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                        } else {


                            reader.getElementText(); // throw away text nodes if any.
                        }

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                    }


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://webservice.demone.conservazione","tipoConservazione").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                            java.lang.String content = reader.getElementText();

                            object.setTipoConservazione(
                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                        } else {


                            reader.getElementText(); // throw away text nodes if any.
                        }

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                    }


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://webservice.demone.conservazione","tipoDocumento").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                            java.lang.String content = reader.getElementText();

                            object.setTipoDocumento(
                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                        } else {


                            reader.getElementText(); // throw away text nodes if any.
                        }

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                    }


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://webservice.demone.conservazione","applicazioneChiamante").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                            java.lang.String content = reader.getElementText();

                            object.setApplicazioneChiamante(
                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                        } else {


                            reader.getElementText(); // throw away text nodes if any.
                        }

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                    }


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://webservice.demone.conservazione","forzaCollegamento").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                            throw new org.apache.axis2.databinding.ADBException("The element: "+"forzaCollegamento" +"  cannot be null");
                        }


                        java.lang.String content = reader.getElementText();

                        object.setForzaCollegamento(
                                org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                    }


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://webservice.demone.conservazione","forzaAccettazione").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                            throw new org.apache.axis2.databinding.ADBException("The element: "+"forzaAccettazione" +"  cannot be null");
                        }


                        java.lang.String content = reader.getElementText();

                        object.setForzaAccettazione(
                                org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                    }


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://webservice.demone.conservazione","forzaConservazione").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                            throw new org.apache.axis2.databinding.ADBException("The element: "+"forzaConservazione" +"  cannot be null");
                        }


                        java.lang.String content = reader.getElementText();

                        object.setForzaConservazione(
                                org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                    }


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://webservice.demone.conservazione","dataRegistrazione").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                            java.lang.String content = reader.getElementText();

                            object.setDataRegistrazione(
                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToDateTime(content));

                        } else {


                            reader.getElementText(); // throw away text nodes if any.
                        }

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                    }


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://webservice.demone.conservazione","chiave_doc").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                            java.lang.String content = reader.getElementText();

                            object.setChiave_doc(
                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                        } else {


                            reader.getElementText(); // throw away text nodes if any.
                        }

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                    }


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://webservice.demone.conservazione","codEnte").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                            java.lang.String content = reader.getElementText();

                            object.setCodEnte(
                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                        } else {


                            reader.getElementText(); // throw away text nodes if any.
                        }

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                    }


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://webservice.demone.conservazione","codAoo").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                            java.lang.String content = reader.getElementText();

                            object.setCodAoo(
                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                        } else {


                            reader.getElementText(); // throw away text nodes if any.
                        }

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                    }


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://webservice.demone.conservazione","azione").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                            java.lang.String content = reader.getElementText();

                            object.setAzione(
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
                    throw new java.lang.Exception(e);
                }

                return object;
            }

        }//end of factory class



    }


    public static class GetJobByDocId
            implements org.apache.axis2.databinding.ADBBean{

        public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName(
                "http://webservice.demone.conservazione",
                "getJobByDocId",
                "ns3");



        /**
         * field for Id_doc
         */


        protected java.lang.String localId_doc ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localId_docTracker = false ;

        public boolean isId_docSpecified(){
            return localId_docTracker;
        }



        /**
         * Auto generated getter method
         * @return java.lang.String
         */
        public  java.lang.String getId_doc(){
            return localId_doc;
        }



        /**
         * Auto generated setter method
         * @param param Id_doc
         */
        public void setId_doc(java.lang.String param){
            localId_docTracker = true;

            this.localId_doc=param;


        }


        /**
         * field for Azione
         */


        protected java.lang.String localAzione ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localAzioneTracker = false ;

        public boolean isAzioneSpecified(){
            return localAzioneTracker;
        }



        /**
         * Auto generated getter method
         * @return java.lang.String
         */
        public  java.lang.String getAzione(){
            return localAzione;
        }



        /**
         * Auto generated setter method
         * @param param Azione
         */
        public void setAzione(java.lang.String param){
            localAzioneTracker = true;

            this.localAzione=param;


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




            java.lang.String prefix = null;
            java.lang.String namespace = null;


            prefix = parentQName.getPrefix();
            namespace = parentQName.getNamespaceURI();
            writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

            if (serializeType){


                java.lang.String namespacePrefix = registerPrefix(xmlWriter,"http://webservice.demone.conservazione");
                if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            namespacePrefix+":getJobByDocId",
                            xmlWriter);
                } else {
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            "getJobByDocId",
                            xmlWriter);
                }


            }
            if (localId_docTracker){
                namespace = "http://webservice.demone.conservazione";
                writeStartElement(null, namespace, "id_doc", xmlWriter);


                if (localId_doc==null){
                    // write the nil attribute

                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                }else{


                    xmlWriter.writeCharacters(localId_doc);

                }

                xmlWriter.writeEndElement();
            } if (localAzioneTracker){
                namespace = "http://webservice.demone.conservazione";
                writeStartElement(null, namespace, "azione", xmlWriter);


                if (localAzione==null){
                    // write the nil attribute

                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                }else{


                    xmlWriter.writeCharacters(localAzione);

                }

                xmlWriter.writeEndElement();
            }
            xmlWriter.writeEndElement();


        }

        private static java.lang.String generatePrefix(java.lang.String namespace) {
            if(namespace.equals("http://webservice.demone.conservazione")){
                return "ns3";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
         * Utility method to write an element start tag.
         */
        private void writeStartElement(java.lang.String prefix, java.lang.String namespace, java.lang.String localPart,
                                       javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String writerPrefix = xmlWriter.getPrefix(namespace);
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
        private void writeAttribute(java.lang.String prefix,java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (xmlWriter.getPrefix(namespace) == null) {
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            xmlWriter.writeAttribute(namespace,attName,attValue);
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeAttribute(java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
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
        private void writeQNameAttribute(java.lang.String namespace, java.lang.String attName,
                                         javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            java.lang.String attributeNamespace = qname.getNamespaceURI();
            java.lang.String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
            if (attributePrefix == null) {
                attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
            }
            java.lang.String attributeValue;
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
            java.lang.String namespaceURI = qname.getNamespaceURI();
            if (namespaceURI != null) {
                java.lang.String prefix = xmlWriter.getPrefix(namespaceURI);
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
                java.lang.StringBuffer stringToWrite = new java.lang.StringBuffer();
                java.lang.String namespaceURI = null;
                java.lang.String prefix = null;

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
        private java.lang.String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, java.lang.String namespace) throws javax.xml.stream.XMLStreamException {
            java.lang.String prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null) {
                prefix = generatePrefix(namespace);
                javax.xml.namespace.NamespaceContext nsContext = xmlWriter.getNamespaceContext();
                while (true) {
                    java.lang.String uri = nsContext.getNamespaceURI(prefix);
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

            if (localId_docTracker){
                elementList.add(new javax.xml.namespace.QName("http://webservice.demone.conservazione",
                        "id_doc"));

                elementList.add(localId_doc==null?null:
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localId_doc));
            } if (localAzioneTracker){
                elementList.add(new javax.xml.namespace.QName("http://webservice.demone.conservazione",
                        "azione"));

                elementList.add(localAzione==null?null:
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localAzione));
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
            public static GetJobByDocId parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{
                GetJobByDocId object =
                        new GetJobByDocId();

                int event;
                java.lang.String nillableValue = null;
                java.lang.String prefix ="";
                java.lang.String namespaceuri ="";
                try {

                    while (!reader.isStartElement() && !reader.isEndElement())
                        reader.next();


                    if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","type")!=null){
                        java.lang.String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                                "type");
                        if (fullTypeName!=null){
                            java.lang.String nsPrefix = null;
                            if (fullTypeName.indexOf(":") > -1){
                                nsPrefix = fullTypeName.substring(0,fullTypeName.indexOf(":"));
                            }
                            nsPrefix = nsPrefix==null?"":nsPrefix;

                            java.lang.String type = fullTypeName.substring(fullTypeName.indexOf(":")+1);

                            if (!"getJobByDocId".equals(type)){
                                //find namespace for the prefix
                                java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (GetJobByDocId)ExtensionMapper.getTypeObject(
                                        nsUri,type,reader);
                            }


                        }


                    }




                    // Note all attributes that were handled. Used to differ normal attributes
                    // from anyAttributes.
                    java.util.Vector handledAttributes = new java.util.Vector();




                    reader.next();


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://webservice.demone.conservazione","id_doc").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                            java.lang.String content = reader.getElementText();

                            object.setId_doc(
                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                        } else {


                            reader.getElementText(); // throw away text nodes if any.
                        }

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                    }


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://webservice.demone.conservazione","azione").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                            java.lang.String content = reader.getElementText();

                            object.setAzione(
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
                    throw new java.lang.Exception(e);
                }

                return object;
            }

        }//end of factory class



    }


    public static class AddJobResponse
            implements org.apache.axis2.databinding.ADBBean{

        public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName(
                "http://webservice.demone.conservazione",
                "addJobResponse",
                "ns3");



        /**
         * field for _return
         */


        protected boolean local_return ;

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
         * @return boolean
         */
        public  boolean get_return(){
            return local_return;
        }



        /**
         * Auto generated setter method
         * @param param _return
         */
        public void set_return(boolean param){

            // setting primitive attribute tracker to true
            local_returnTracker =
                    true;

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




            java.lang.String prefix = null;
            java.lang.String namespace = null;


            prefix = parentQName.getPrefix();
            namespace = parentQName.getNamespaceURI();
            writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

            if (serializeType){


                java.lang.String namespacePrefix = registerPrefix(xmlWriter,"http://webservice.demone.conservazione");
                if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            namespacePrefix+":addJobResponse",
                            xmlWriter);
                } else {
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            "addJobResponse",
                            xmlWriter);
                }


            }
            if (local_returnTracker){
                namespace = "http://webservice.demone.conservazione";
                writeStartElement(null, namespace, "return", xmlWriter);

                if (false) {

                    throw new org.apache.axis2.databinding.ADBException("return cannot be null!!");

                } else {
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(local_return));
                }

                xmlWriter.writeEndElement();
            }
            xmlWriter.writeEndElement();


        }

        private static java.lang.String generatePrefix(java.lang.String namespace) {
            if(namespace.equals("http://webservice.demone.conservazione")){
                return "ns3";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
         * Utility method to write an element start tag.
         */
        private void writeStartElement(java.lang.String prefix, java.lang.String namespace, java.lang.String localPart,
                                       javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String writerPrefix = xmlWriter.getPrefix(namespace);
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
        private void writeAttribute(java.lang.String prefix,java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (xmlWriter.getPrefix(namespace) == null) {
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            xmlWriter.writeAttribute(namespace,attName,attValue);
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeAttribute(java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
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
        private void writeQNameAttribute(java.lang.String namespace, java.lang.String attName,
                                         javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            java.lang.String attributeNamespace = qname.getNamespaceURI();
            java.lang.String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
            if (attributePrefix == null) {
                attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
            }
            java.lang.String attributeValue;
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
            java.lang.String namespaceURI = qname.getNamespaceURI();
            if (namespaceURI != null) {
                java.lang.String prefix = xmlWriter.getPrefix(namespaceURI);
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
                java.lang.StringBuffer stringToWrite = new java.lang.StringBuffer();
                java.lang.String namespaceURI = null;
                java.lang.String prefix = null;

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
        private java.lang.String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, java.lang.String namespace) throws javax.xml.stream.XMLStreamException {
            java.lang.String prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null) {
                prefix = generatePrefix(namespace);
                javax.xml.namespace.NamespaceContext nsContext = xmlWriter.getNamespaceContext();
                while (true) {
                    java.lang.String uri = nsContext.getNamespaceURI(prefix);
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
                elementList.add(new javax.xml.namespace.QName("http://webservice.demone.conservazione",
                        "return"));

                elementList.add(
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
            public static AddJobResponse parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{
                AddJobResponse object =
                        new AddJobResponse();

                int event;
                java.lang.String nillableValue = null;
                java.lang.String prefix ="";
                java.lang.String namespaceuri ="";
                try {

                    while (!reader.isStartElement() && !reader.isEndElement())
                        reader.next();


                    if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","type")!=null){
                        java.lang.String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                                "type");
                        if (fullTypeName!=null){
                            java.lang.String nsPrefix = null;
                            if (fullTypeName.indexOf(":") > -1){
                                nsPrefix = fullTypeName.substring(0,fullTypeName.indexOf(":"));
                            }
                            nsPrefix = nsPrefix==null?"":nsPrefix;

                            java.lang.String type = fullTypeName.substring(fullTypeName.indexOf(":")+1);

                            if (!"addJobResponse".equals(type)){
                                //find namespace for the prefix
                                java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (AddJobResponse)ExtensionMapper.getTypeObject(
                                        nsUri,type,reader);
                            }


                        }


                    }




                    // Note all attributes that were handled. Used to differ normal attributes
                    // from anyAttributes.
                    java.util.Vector handledAttributes = new java.util.Vector();




                    reader.next();


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://webservice.demone.conservazione","return").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                            throw new org.apache.axis2.databinding.ADBException("The element: "+"return" +"  cannot be null");
                        }


                        java.lang.String content = reader.getElementText();

                        object.set_return(
                                org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));

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
                    throw new java.lang.Exception(e);
                }

                return object;
            }

        }//end of factory class



    }


    public static class SearchJobResponse
            implements org.apache.axis2.databinding.ADBBean{

        public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName(
                "http://webservice.demone.conservazione",
                "searchJobResponse",
                "ns3");



        /**
         * field for _return
         */


        protected java.lang.String local_return ;

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
        public  java.lang.String get_return(){
            return local_return;
        }



        /**
         * Auto generated setter method
         * @param param _return
         */
        public void set_return(java.lang.String param){
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




            java.lang.String prefix = null;
            java.lang.String namespace = null;


            prefix = parentQName.getPrefix();
            namespace = parentQName.getNamespaceURI();
            writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

            if (serializeType){


                java.lang.String namespacePrefix = registerPrefix(xmlWriter,"http://webservice.demone.conservazione");
                if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            namespacePrefix+":searchJobResponse",
                            xmlWriter);
                } else {
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            "searchJobResponse",
                            xmlWriter);
                }


            }
            if (local_returnTracker){
                namespace = "http://webservice.demone.conservazione";
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

        private static java.lang.String generatePrefix(java.lang.String namespace) {
            if(namespace.equals("http://webservice.demone.conservazione")){
                return "ns3";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
         * Utility method to write an element start tag.
         */
        private void writeStartElement(java.lang.String prefix, java.lang.String namespace, java.lang.String localPart,
                                       javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String writerPrefix = xmlWriter.getPrefix(namespace);
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
        private void writeAttribute(java.lang.String prefix,java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (xmlWriter.getPrefix(namespace) == null) {
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            xmlWriter.writeAttribute(namespace,attName,attValue);
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeAttribute(java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
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
        private void writeQNameAttribute(java.lang.String namespace, java.lang.String attName,
                                         javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            java.lang.String attributeNamespace = qname.getNamespaceURI();
            java.lang.String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
            if (attributePrefix == null) {
                attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
            }
            java.lang.String attributeValue;
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
            java.lang.String namespaceURI = qname.getNamespaceURI();
            if (namespaceURI != null) {
                java.lang.String prefix = xmlWriter.getPrefix(namespaceURI);
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
                java.lang.StringBuffer stringToWrite = new java.lang.StringBuffer();
                java.lang.String namespaceURI = null;
                java.lang.String prefix = null;

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
        private java.lang.String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, java.lang.String namespace) throws javax.xml.stream.XMLStreamException {
            java.lang.String prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null) {
                prefix = generatePrefix(namespace);
                javax.xml.namespace.NamespaceContext nsContext = xmlWriter.getNamespaceContext();
                while (true) {
                    java.lang.String uri = nsContext.getNamespaceURI(prefix);
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
                elementList.add(new javax.xml.namespace.QName("http://webservice.demone.conservazione",
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
            public static SearchJobResponse parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{
                SearchJobResponse object =
                        new SearchJobResponse();

                int event;
                java.lang.String nillableValue = null;
                java.lang.String prefix ="";
                java.lang.String namespaceuri ="";
                try {

                    while (!reader.isStartElement() && !reader.isEndElement())
                        reader.next();


                    if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","type")!=null){
                        java.lang.String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                                "type");
                        if (fullTypeName!=null){
                            java.lang.String nsPrefix = null;
                            if (fullTypeName.indexOf(":") > -1){
                                nsPrefix = fullTypeName.substring(0,fullTypeName.indexOf(":"));
                            }
                            nsPrefix = nsPrefix==null?"":nsPrefix;

                            java.lang.String type = fullTypeName.substring(fullTypeName.indexOf(":")+1);

                            if (!"searchJobResponse".equals(type)){
                                //find namespace for the prefix
                                java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (SearchJobResponse)ExtensionMapper.getTypeObject(
                                        nsUri,type,reader);
                            }


                        }


                    }




                    // Note all attributes that were handled. Used to differ normal attributes
                    // from anyAttributes.
                    java.util.Vector handledAttributes = new java.util.Vector();




                    reader.next();


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://webservice.demone.conservazione","return").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                            java.lang.String content = reader.getElementText();

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
                    throw new java.lang.Exception(e);
                }

                return object;
            }

        }//end of factory class



    }


    public static class SearchJobEstesa
            implements org.apache.axis2.databinding.ADBBean{

        public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName(
                "http://webservice.demone.conservazione",
                "searchJobEstesa",
                "ns3");



        /**
         * field for SearchCriteria
         * This was an Array!
         */


        protected KeyValuePair[] localSearchCriteria ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localSearchCriteriaTracker = false ;

        public boolean isSearchCriteriaSpecified(){
            return localSearchCriteriaTracker;
        }



        /**
         * Auto generated getter method
         * @return KeyValuePair[]
         */
        public  KeyValuePair[] getSearchCriteria(){
            return localSearchCriteria;
        }






        /**
         * validate the array for SearchCriteria
         */
        protected void validateSearchCriteria(KeyValuePair[] param){

        }


        /**
         * Auto generated setter method
         * @param param SearchCriteria
         */
        public void setSearchCriteria(KeyValuePair[] param){

            validateSearchCriteria(param);

            localSearchCriteriaTracker = true;

            this.localSearchCriteria=param;
        }



        /**
         * Auto generated add method for the array for convenience
         * @param param KeyValuePair
         */
        public void addSearchCriteria(KeyValuePair param){
            if (localSearchCriteria == null){
                localSearchCriteria = new KeyValuePair[]{};
            }


            //update the setting tracker
            localSearchCriteriaTracker = true;


            java.util.List list =
                    org.apache.axis2.databinding.utils.ConverterUtil.toList(localSearchCriteria);
            list.add(param);
            this.localSearchCriteria =
                    (KeyValuePair[])list.toArray(
                            new KeyValuePair[list.size()]);

        }


        /**
         * field for MaxRows
         */


        protected int localMaxRows ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localMaxRowsTracker = false ;

        public boolean isMaxRowsSpecified(){
            return localMaxRowsTracker;
        }



        /**
         * Auto generated getter method
         * @return int
         */
        public  int getMaxRows(){
            return localMaxRows;
        }



        /**
         * Auto generated setter method
         * @param param MaxRows
         */
        public void setMaxRows(int param){

            // setting primitive attribute tracker to true
            localMaxRowsTracker =
                    param != java.lang.Integer.MIN_VALUE;

            this.localMaxRows=param;


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




            java.lang.String prefix = null;
            java.lang.String namespace = null;


            prefix = parentQName.getPrefix();
            namespace = parentQName.getNamespaceURI();
            writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

            if (serializeType){


                java.lang.String namespacePrefix = registerPrefix(xmlWriter,"http://webservice.demone.conservazione");
                if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            namespacePrefix+":searchJobEstesa",
                            xmlWriter);
                } else {
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            "searchJobEstesa",
                            xmlWriter);
                }


            }
            if (localSearchCriteriaTracker){
                if (localSearchCriteria!=null){
                    for (int i = 0;i < localSearchCriteria.length;i++){
                        if (localSearchCriteria[i] != null){
                            localSearchCriteria[i].serialize(new javax.xml.namespace.QName("http://webservice.demone.conservazione","searchCriteria"),
                                    xmlWriter);
                        } else {

                            writeStartElement(null, "http://webservice.demone.conservazione", "searchCriteria", xmlWriter);

                            // write the nil attribute
                            writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                            xmlWriter.writeEndElement();

                        }

                    }
                } else {

                    writeStartElement(null, "http://webservice.demone.conservazione", "searchCriteria", xmlWriter);

                    // write the nil attribute
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                    xmlWriter.writeEndElement();

                }
            } if (localMaxRowsTracker){
                namespace = "http://webservice.demone.conservazione";
                writeStartElement(null, namespace, "maxRows", xmlWriter);

                if (localMaxRows==java.lang.Integer.MIN_VALUE) {

                    throw new org.apache.axis2.databinding.ADBException("maxRows cannot be null!!");

                } else {
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localMaxRows));
                }

                xmlWriter.writeEndElement();
            }
            xmlWriter.writeEndElement();


        }

        private static java.lang.String generatePrefix(java.lang.String namespace) {
            if(namespace.equals("http://webservice.demone.conservazione")){
                return "ns3";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
         * Utility method to write an element start tag.
         */
        private void writeStartElement(java.lang.String prefix, java.lang.String namespace, java.lang.String localPart,
                                       javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String writerPrefix = xmlWriter.getPrefix(namespace);
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
        private void writeAttribute(java.lang.String prefix,java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (xmlWriter.getPrefix(namespace) == null) {
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            xmlWriter.writeAttribute(namespace,attName,attValue);
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeAttribute(java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
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
        private void writeQNameAttribute(java.lang.String namespace, java.lang.String attName,
                                         javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            java.lang.String attributeNamespace = qname.getNamespaceURI();
            java.lang.String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
            if (attributePrefix == null) {
                attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
            }
            java.lang.String attributeValue;
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
            java.lang.String namespaceURI = qname.getNamespaceURI();
            if (namespaceURI != null) {
                java.lang.String prefix = xmlWriter.getPrefix(namespaceURI);
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
                java.lang.StringBuffer stringToWrite = new java.lang.StringBuffer();
                java.lang.String namespaceURI = null;
                java.lang.String prefix = null;

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
        private java.lang.String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, java.lang.String namespace) throws javax.xml.stream.XMLStreamException {
            java.lang.String prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null) {
                prefix = generatePrefix(namespace);
                javax.xml.namespace.NamespaceContext nsContext = xmlWriter.getNamespaceContext();
                while (true) {
                    java.lang.String uri = nsContext.getNamespaceURI(prefix);
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

            if (localSearchCriteriaTracker){
                if (localSearchCriteria!=null) {
                    for (int i = 0;i < localSearchCriteria.length;i++){

                        if (localSearchCriteria[i] != null){
                            elementList.add(new javax.xml.namespace.QName("http://webservice.demone.conservazione",
                                    "searchCriteria"));
                            elementList.add(localSearchCriteria[i]);
                        } else {

                            elementList.add(new javax.xml.namespace.QName("http://webservice.demone.conservazione",
                                    "searchCriteria"));
                            elementList.add(null);

                        }

                    }
                } else {

                    elementList.add(new javax.xml.namespace.QName("http://webservice.demone.conservazione",
                            "searchCriteria"));
                    elementList.add(localSearchCriteria);

                }

            } if (localMaxRowsTracker){
                elementList.add(new javax.xml.namespace.QName("http://webservice.demone.conservazione",
                        "maxRows"));

                elementList.add(
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localMaxRows));
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
            public static SearchJobEstesa parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{
                SearchJobEstesa object =
                        new SearchJobEstesa();

                int event;
                java.lang.String nillableValue = null;
                java.lang.String prefix ="";
                java.lang.String namespaceuri ="";
                try {

                    while (!reader.isStartElement() && !reader.isEndElement())
                        reader.next();


                    if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","type")!=null){
                        java.lang.String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                                "type");
                        if (fullTypeName!=null){
                            java.lang.String nsPrefix = null;
                            if (fullTypeName.indexOf(":") > -1){
                                nsPrefix = fullTypeName.substring(0,fullTypeName.indexOf(":"));
                            }
                            nsPrefix = nsPrefix==null?"":nsPrefix;

                            java.lang.String type = fullTypeName.substring(fullTypeName.indexOf(":")+1);

                            if (!"searchJobEstesa".equals(type)){
                                //find namespace for the prefix
                                java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (SearchJobEstesa)ExtensionMapper.getTypeObject(
                                        nsUri,type,reader);
                            }


                        }


                    }




                    // Note all attributes that were handled. Used to differ normal attributes
                    // from anyAttributes.
                    java.util.Vector handledAttributes = new java.util.Vector();




                    reader.next();

                    java.util.ArrayList list1 = new java.util.ArrayList();


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://webservice.demone.conservazione","searchCriteria").equals(reader.getName())){



                        // Process the array and step past its final element's end.

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                            list1.add(null);
                            reader.next();
                        } else {
                            list1.add(KeyValuePair.Factory.parse(reader));
                        }
                        //loop until we find a start element that is not part of this array
                        boolean loopDone1 = false;
                        while(!loopDone1){
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
                                loopDone1 = true;
                            } else {
                                if (new javax.xml.namespace.QName("http://webservice.demone.conservazione","searchCriteria").equals(reader.getName())){

                                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                    if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                        list1.add(null);
                                        reader.next();
                                    } else {
                                        list1.add(KeyValuePair.Factory.parse(reader));
                                    }
                                }else{
                                    loopDone1 = true;
                                }
                            }
                        }
                        // call the converter utility  to convert and set the array

                        object.setSearchCriteria((KeyValuePair[])
                                org.apache.axis2.databinding.utils.ConverterUtil.convertToArray(
                                        KeyValuePair.class,
                                        list1));

                    }  // End of if for expected property start element

                    else {

                    }


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://webservice.demone.conservazione","maxRows").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                            throw new org.apache.axis2.databinding.ADBException("The element: "+"maxRows" +"  cannot be null");
                        }


                        java.lang.String content = reader.getElementText();

                        object.setMaxRows(
                                org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                        object.setMaxRows(java.lang.Integer.MIN_VALUE);

                    }

                    while (!reader.isStartElement() && !reader.isEndElement())
                        reader.next();

                    if (reader.isStartElement())
                        // A start element we are not expecting indicates a trailing invalid property
                        throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());




                } catch (javax.xml.stream.XMLStreamException e) {
                    throw new java.lang.Exception(e);
                }

                return object;
            }

        }//end of factory class



    }


    public static class DeleteJob_ByIdDocResponse
            implements org.apache.axis2.databinding.ADBBean{

        public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName(
                "http://webservice.demone.conservazione",
                "deleteJob_ByIdDocResponse",
                "ns3");



        /**
         * field for _return
         */


        protected boolean local_return ;

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
         * @return boolean
         */
        public  boolean get_return(){
            return local_return;
        }



        /**
         * Auto generated setter method
         * @param param _return
         */
        public void set_return(boolean param){

            // setting primitive attribute tracker to true
            local_returnTracker =
                    true;

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




            java.lang.String prefix = null;
            java.lang.String namespace = null;


            prefix = parentQName.getPrefix();
            namespace = parentQName.getNamespaceURI();
            writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

            if (serializeType){


                java.lang.String namespacePrefix = registerPrefix(xmlWriter,"http://webservice.demone.conservazione");
                if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            namespacePrefix+":deleteJob_ByIdDocResponse",
                            xmlWriter);
                } else {
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            "deleteJob_ByIdDocResponse",
                            xmlWriter);
                }


            }
            if (local_returnTracker){
                namespace = "http://webservice.demone.conservazione";
                writeStartElement(null, namespace, "return", xmlWriter);

                if (false) {

                    throw new org.apache.axis2.databinding.ADBException("return cannot be null!!");

                } else {
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(local_return));
                }

                xmlWriter.writeEndElement();
            }
            xmlWriter.writeEndElement();


        }

        private static java.lang.String generatePrefix(java.lang.String namespace) {
            if(namespace.equals("http://webservice.demone.conservazione")){
                return "ns3";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
         * Utility method to write an element start tag.
         */
        private void writeStartElement(java.lang.String prefix, java.lang.String namespace, java.lang.String localPart,
                                       javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String writerPrefix = xmlWriter.getPrefix(namespace);
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
        private void writeAttribute(java.lang.String prefix,java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (xmlWriter.getPrefix(namespace) == null) {
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            xmlWriter.writeAttribute(namespace,attName,attValue);
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeAttribute(java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
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
        private void writeQNameAttribute(java.lang.String namespace, java.lang.String attName,
                                         javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            java.lang.String attributeNamespace = qname.getNamespaceURI();
            java.lang.String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
            if (attributePrefix == null) {
                attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
            }
            java.lang.String attributeValue;
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
            java.lang.String namespaceURI = qname.getNamespaceURI();
            if (namespaceURI != null) {
                java.lang.String prefix = xmlWriter.getPrefix(namespaceURI);
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
                java.lang.StringBuffer stringToWrite = new java.lang.StringBuffer();
                java.lang.String namespaceURI = null;
                java.lang.String prefix = null;

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
        private java.lang.String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, java.lang.String namespace) throws javax.xml.stream.XMLStreamException {
            java.lang.String prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null) {
                prefix = generatePrefix(namespace);
                javax.xml.namespace.NamespaceContext nsContext = xmlWriter.getNamespaceContext();
                while (true) {
                    java.lang.String uri = nsContext.getNamespaceURI(prefix);
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
                elementList.add(new javax.xml.namespace.QName("http://webservice.demone.conservazione",
                        "return"));

                elementList.add(
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
            public static DeleteJob_ByIdDocResponse parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{
                DeleteJob_ByIdDocResponse object =
                        new DeleteJob_ByIdDocResponse();

                int event;
                java.lang.String nillableValue = null;
                java.lang.String prefix ="";
                java.lang.String namespaceuri ="";
                try {

                    while (!reader.isStartElement() && !reader.isEndElement())
                        reader.next();


                    if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","type")!=null){
                        java.lang.String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                                "type");
                        if (fullTypeName!=null){
                            java.lang.String nsPrefix = null;
                            if (fullTypeName.indexOf(":") > -1){
                                nsPrefix = fullTypeName.substring(0,fullTypeName.indexOf(":"));
                            }
                            nsPrefix = nsPrefix==null?"":nsPrefix;

                            java.lang.String type = fullTypeName.substring(fullTypeName.indexOf(":")+1);

                            if (!"deleteJob_ByIdDocResponse".equals(type)){
                                //find namespace for the prefix
                                java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (DeleteJob_ByIdDocResponse)ExtensionMapper.getTypeObject(
                                        nsUri,type,reader);
                            }


                        }


                    }




                    // Note all attributes that were handled. Used to differ normal attributes
                    // from anyAttributes.
                    java.util.Vector handledAttributes = new java.util.Vector();




                    reader.next();


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://webservice.demone.conservazione","return").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                            throw new org.apache.axis2.databinding.ADBException("The element: "+"return" +"  cannot be null");
                        }


                        java.lang.String content = reader.getElementText();

                        object.set_return(
                                org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));

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
                    throw new java.lang.Exception(e);
                }

                return object;
            }

        }//end of factory class



    }


    public static class UpdateJob
            implements org.apache.axis2.databinding.ADBBean{

        public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName(
                "http://webservice.demone.conservazione",
                "updateJob",
                "ns3");



        /**
         * field for Id_job
         */


        protected long localId_job ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localId_jobTracker = false ;

        public boolean isId_jobSpecified(){
            return localId_jobTracker;
        }



        /**
         * Auto generated getter method
         * @return long
         */
        public  long getId_job(){
            return localId_job;
        }



        /**
         * Auto generated setter method
         * @param param Id_job
         */
        public void setId_job(long param){

            // setting primitive attribute tracker to true
            localId_jobTracker =
                    param != java.lang.Long.MIN_VALUE;

            this.localId_job=param;


        }


        /**
         * field for Stato
         */


        protected java.lang.Object localStato ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localStatoTracker = false ;

        public boolean isStatoSpecified(){
            return localStatoTracker;
        }



        /**
         * Auto generated getter method
         * @return java.lang.Object
         */
        public  java.lang.Object getStato(){
            return localStato;
        }



        /**
         * Auto generated setter method
         * @param param Stato
         */
        public void setStato(java.lang.Object param){
            localStatoTracker = true;

            this.localStato=param;


        }


        /**
         * field for ForzaCollegamento
         */


        protected boolean localForzaCollegamento ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localForzaCollegamentoTracker = false ;

        public boolean isForzaCollegamentoSpecified(){
            return localForzaCollegamentoTracker;
        }



        /**
         * Auto generated getter method
         * @return boolean
         */
        public  boolean getForzaCollegamento(){
            return localForzaCollegamento;
        }



        /**
         * Auto generated setter method
         * @param param ForzaCollegamento
         */
        public void setForzaCollegamento(boolean param){

            // setting primitive attribute tracker to true
            localForzaCollegamentoTracker =
                    true;

            this.localForzaCollegamento=param;


        }


        /**
         * field for ForzaAccettazione
         */


        protected boolean localForzaAccettazione ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localForzaAccettazioneTracker = false ;

        public boolean isForzaAccettazioneSpecified(){
            return localForzaAccettazioneTracker;
        }



        /**
         * Auto generated getter method
         * @return boolean
         */
        public  boolean getForzaAccettazione(){
            return localForzaAccettazione;
        }



        /**
         * Auto generated setter method
         * @param param ForzaAccettazione
         */
        public void setForzaAccettazione(boolean param){

            // setting primitive attribute tracker to true
            localForzaAccettazioneTracker =
                    true;

            this.localForzaAccettazione=param;


        }


        /**
         * field for ForzaConservazione
         */


        protected boolean localForzaConservazione ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localForzaConservazioneTracker = false ;

        public boolean isForzaConservazioneSpecified(){
            return localForzaConservazioneTracker;
        }



        /**
         * Auto generated getter method
         * @return boolean
         */
        public  boolean getForzaConservazione(){
            return localForzaConservazione;
        }



        /**
         * Auto generated setter method
         * @param param ForzaConservazione
         */
        public void setForzaConservazione(boolean param){

            // setting primitive attribute tracker to true
            localForzaConservazioneTracker =
                    true;

            this.localForzaConservazione=param;


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




            java.lang.String prefix = null;
            java.lang.String namespace = null;


            prefix = parentQName.getPrefix();
            namespace = parentQName.getNamespaceURI();
            writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

            if (serializeType){


                java.lang.String namespacePrefix = registerPrefix(xmlWriter,"http://webservice.demone.conservazione");
                if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            namespacePrefix+":updateJob",
                            xmlWriter);
                } else {
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            "updateJob",
                            xmlWriter);
                }


            }
            if (localId_jobTracker){
                namespace = "http://webservice.demone.conservazione";
                writeStartElement(null, namespace, "id_job", xmlWriter);

                if (localId_job==java.lang.Long.MIN_VALUE) {

                    throw new org.apache.axis2.databinding.ADBException("id_job cannot be null!!");

                } else {
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localId_job));
                }

                xmlWriter.writeEndElement();
            } if (localStatoTracker){

                if (localStato!=null){
                    if (localStato instanceof org.apache.axis2.databinding.ADBBean){
                        ((org.apache.axis2.databinding.ADBBean)localStato).serialize(
                                new javax.xml.namespace.QName("http://webservice.demone.conservazione","stato"),
                                xmlWriter,true);
                    } else {
                        writeStartElement(null, "http://webservice.demone.conservazione", "stato", xmlWriter);
                        org.apache.axis2.databinding.utils.ConverterUtil.serializeAnyType(localStato, xmlWriter);
                        xmlWriter.writeEndElement();
                    }
                } else {

                    // write null attribute
                    writeStartElement(null, "http://webservice.demone.conservazione", "stato", xmlWriter);

                    // write the nil attribute
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                    xmlWriter.writeEndElement();

                }


            } if (localForzaCollegamentoTracker){
                namespace = "http://webservice.demone.conservazione";
                writeStartElement(null, namespace, "forzaCollegamento", xmlWriter);

                if (false) {

                    throw new org.apache.axis2.databinding.ADBException("forzaCollegamento cannot be null!!");

                } else {
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localForzaCollegamento));
                }

                xmlWriter.writeEndElement();
            } if (localForzaAccettazioneTracker){
                namespace = "http://webservice.demone.conservazione";
                writeStartElement(null, namespace, "forzaAccettazione", xmlWriter);

                if (false) {

                    throw new org.apache.axis2.databinding.ADBException("forzaAccettazione cannot be null!!");

                } else {
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localForzaAccettazione));
                }

                xmlWriter.writeEndElement();
            } if (localForzaConservazioneTracker){
                namespace = "http://webservice.demone.conservazione";
                writeStartElement(null, namespace, "forzaConservazione", xmlWriter);

                if (false) {

                    throw new org.apache.axis2.databinding.ADBException("forzaConservazione cannot be null!!");

                } else {
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localForzaConservazione));
                }

                xmlWriter.writeEndElement();
            }
            xmlWriter.writeEndElement();


        }

        private static java.lang.String generatePrefix(java.lang.String namespace) {
            if(namespace.equals("http://webservice.demone.conservazione")){
                return "ns3";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
         * Utility method to write an element start tag.
         */
        private void writeStartElement(java.lang.String prefix, java.lang.String namespace, java.lang.String localPart,
                                       javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String writerPrefix = xmlWriter.getPrefix(namespace);
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
        private void writeAttribute(java.lang.String prefix,java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (xmlWriter.getPrefix(namespace) == null) {
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            xmlWriter.writeAttribute(namespace,attName,attValue);
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeAttribute(java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
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
        private void writeQNameAttribute(java.lang.String namespace, java.lang.String attName,
                                         javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            java.lang.String attributeNamespace = qname.getNamespaceURI();
            java.lang.String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
            if (attributePrefix == null) {
                attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
            }
            java.lang.String attributeValue;
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
            java.lang.String namespaceURI = qname.getNamespaceURI();
            if (namespaceURI != null) {
                java.lang.String prefix = xmlWriter.getPrefix(namespaceURI);
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
                java.lang.StringBuffer stringToWrite = new java.lang.StringBuffer();
                java.lang.String namespaceURI = null;
                java.lang.String prefix = null;

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
        private java.lang.String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, java.lang.String namespace) throws javax.xml.stream.XMLStreamException {
            java.lang.String prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null) {
                prefix = generatePrefix(namespace);
                javax.xml.namespace.NamespaceContext nsContext = xmlWriter.getNamespaceContext();
                while (true) {
                    java.lang.String uri = nsContext.getNamespaceURI(prefix);
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

            if (localId_jobTracker){
                elementList.add(new javax.xml.namespace.QName("http://webservice.demone.conservazione",
                        "id_job"));

                elementList.add(
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localId_job));
            } if (localStatoTracker){
                elementList.add(new javax.xml.namespace.QName("http://webservice.demone.conservazione",
                        "stato"));


                elementList.add(localStato==null?null:
                        localStato);
            } if (localForzaCollegamentoTracker){
                elementList.add(new javax.xml.namespace.QName("http://webservice.demone.conservazione",
                        "forzaCollegamento"));

                elementList.add(
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localForzaCollegamento));
            } if (localForzaAccettazioneTracker){
                elementList.add(new javax.xml.namespace.QName("http://webservice.demone.conservazione",
                        "forzaAccettazione"));

                elementList.add(
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localForzaAccettazione));
            } if (localForzaConservazioneTracker){
                elementList.add(new javax.xml.namespace.QName("http://webservice.demone.conservazione",
                        "forzaConservazione"));

                elementList.add(
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localForzaConservazione));
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
            public static UpdateJob parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{
                UpdateJob object =
                        new UpdateJob();

                int event;
                java.lang.String nillableValue = null;
                java.lang.String prefix ="";
                java.lang.String namespaceuri ="";
                try {

                    while (!reader.isStartElement() && !reader.isEndElement())
                        reader.next();


                    if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","type")!=null){
                        java.lang.String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                                "type");
                        if (fullTypeName!=null){
                            java.lang.String nsPrefix = null;
                            if (fullTypeName.indexOf(":") > -1){
                                nsPrefix = fullTypeName.substring(0,fullTypeName.indexOf(":"));
                            }
                            nsPrefix = nsPrefix==null?"":nsPrefix;

                            java.lang.String type = fullTypeName.substring(fullTypeName.indexOf(":")+1);

                            if (!"updateJob".equals(type)){
                                //find namespace for the prefix
                                java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (UpdateJob)ExtensionMapper.getTypeObject(
                                        nsUri,type,reader);
                            }


                        }


                    }




                    // Note all attributes that were handled. Used to differ normal attributes
                    // from anyAttributes.
                    java.util.Vector handledAttributes = new java.util.Vector();




                    reader.next();


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://webservice.demone.conservazione","id_job").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                            throw new org.apache.axis2.databinding.ADBException("The element: "+"id_job" +"  cannot be null");
                        }


                        java.lang.String content = reader.getElementText();

                        object.setId_job(
                                org.apache.axis2.databinding.utils.ConverterUtil.convertToLong(content));

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                        object.setId_job(java.lang.Long.MIN_VALUE);

                    }


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://webservice.demone.conservazione","stato").equals(reader.getName())){

                        object.setStato(org.apache.axis2.databinding.utils.ConverterUtil.getAnyTypeObject(reader,
                                ExtensionMapper.class));

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                    }


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://webservice.demone.conservazione","forzaCollegamento").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                            throw new org.apache.axis2.databinding.ADBException("The element: "+"forzaCollegamento" +"  cannot be null");
                        }


                        java.lang.String content = reader.getElementText();

                        object.setForzaCollegamento(
                                org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                    }


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://webservice.demone.conservazione","forzaAccettazione").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                            throw new org.apache.axis2.databinding.ADBException("The element: "+"forzaAccettazione" +"  cannot be null");
                        }


                        java.lang.String content = reader.getElementText();

                        object.setForzaAccettazione(
                                org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                    }


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://webservice.demone.conservazione","forzaConservazione").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                            throw new org.apache.axis2.databinding.ADBException("The element: "+"forzaConservazione" +"  cannot be null");
                        }


                        java.lang.String content = reader.getElementText();

                        object.setForzaConservazione(
                                org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));

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
                    throw new java.lang.Exception(e);
                }

                return object;
            }

        }//end of factory class



    }


    public static class ReadLogsByIdDoc
            implements org.apache.axis2.databinding.ADBBean{

        public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName(
                "http://webservice.demone.conservazione",
                "readLogsByIdDoc",
                "ns3");



        /**
         * field for Id_doc
         */


        protected java.lang.String localId_doc ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localId_docTracker = false ;

        public boolean isId_docSpecified(){
            return localId_docTracker;
        }



        /**
         * Auto generated getter method
         * @return java.lang.String
         */
        public  java.lang.String getId_doc(){
            return localId_doc;
        }



        /**
         * Auto generated setter method
         * @param param Id_doc
         */
        public void setId_doc(java.lang.String param){
            localId_docTracker = true;

            this.localId_doc=param;


        }


        /**
         * field for Azione
         */


        protected java.lang.String localAzione ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localAzioneTracker = false ;

        public boolean isAzioneSpecified(){
            return localAzioneTracker;
        }



        /**
         * Auto generated getter method
         * @return java.lang.String
         */
        public  java.lang.String getAzione(){
            return localAzione;
        }



        /**
         * Auto generated setter method
         * @param param Azione
         */
        public void setAzione(java.lang.String param){
            localAzioneTracker = true;

            this.localAzione=param;


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




            java.lang.String prefix = null;
            java.lang.String namespace = null;


            prefix = parentQName.getPrefix();
            namespace = parentQName.getNamespaceURI();
            writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

            if (serializeType){


                java.lang.String namespacePrefix = registerPrefix(xmlWriter,"http://webservice.demone.conservazione");
                if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            namespacePrefix+":readLogsByIdDoc",
                            xmlWriter);
                } else {
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            "readLogsByIdDoc",
                            xmlWriter);
                }


            }
            if (localId_docTracker){
                namespace = "http://webservice.demone.conservazione";
                writeStartElement(null, namespace, "id_doc", xmlWriter);


                if (localId_doc==null){
                    // write the nil attribute

                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                }else{


                    xmlWriter.writeCharacters(localId_doc);

                }

                xmlWriter.writeEndElement();
            } if (localAzioneTracker){
                namespace = "http://webservice.demone.conservazione";
                writeStartElement(null, namespace, "azione", xmlWriter);


                if (localAzione==null){
                    // write the nil attribute

                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                }else{


                    xmlWriter.writeCharacters(localAzione);

                }

                xmlWriter.writeEndElement();
            }
            xmlWriter.writeEndElement();


        }

        private static java.lang.String generatePrefix(java.lang.String namespace) {
            if(namespace.equals("http://webservice.demone.conservazione")){
                return "ns3";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
         * Utility method to write an element start tag.
         */
        private void writeStartElement(java.lang.String prefix, java.lang.String namespace, java.lang.String localPart,
                                       javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String writerPrefix = xmlWriter.getPrefix(namespace);
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
        private void writeAttribute(java.lang.String prefix,java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (xmlWriter.getPrefix(namespace) == null) {
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            xmlWriter.writeAttribute(namespace,attName,attValue);
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeAttribute(java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
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
        private void writeQNameAttribute(java.lang.String namespace, java.lang.String attName,
                                         javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            java.lang.String attributeNamespace = qname.getNamespaceURI();
            java.lang.String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
            if (attributePrefix == null) {
                attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
            }
            java.lang.String attributeValue;
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
            java.lang.String namespaceURI = qname.getNamespaceURI();
            if (namespaceURI != null) {
                java.lang.String prefix = xmlWriter.getPrefix(namespaceURI);
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
                java.lang.StringBuffer stringToWrite = new java.lang.StringBuffer();
                java.lang.String namespaceURI = null;
                java.lang.String prefix = null;

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
        private java.lang.String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, java.lang.String namespace) throws javax.xml.stream.XMLStreamException {
            java.lang.String prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null) {
                prefix = generatePrefix(namespace);
                javax.xml.namespace.NamespaceContext nsContext = xmlWriter.getNamespaceContext();
                while (true) {
                    java.lang.String uri = nsContext.getNamespaceURI(prefix);
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

            if (localId_docTracker){
                elementList.add(new javax.xml.namespace.QName("http://webservice.demone.conservazione",
                        "id_doc"));

                elementList.add(localId_doc==null?null:
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localId_doc));
            } if (localAzioneTracker){
                elementList.add(new javax.xml.namespace.QName("http://webservice.demone.conservazione",
                        "azione"));

                elementList.add(localAzione==null?null:
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localAzione));
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
            public static ReadLogsByIdDoc parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{
                ReadLogsByIdDoc object =
                        new ReadLogsByIdDoc();

                int event;
                java.lang.String nillableValue = null;
                java.lang.String prefix ="";
                java.lang.String namespaceuri ="";
                try {

                    while (!reader.isStartElement() && !reader.isEndElement())
                        reader.next();


                    if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","type")!=null){
                        java.lang.String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                                "type");
                        if (fullTypeName!=null){
                            java.lang.String nsPrefix = null;
                            if (fullTypeName.indexOf(":") > -1){
                                nsPrefix = fullTypeName.substring(0,fullTypeName.indexOf(":"));
                            }
                            nsPrefix = nsPrefix==null?"":nsPrefix;

                            java.lang.String type = fullTypeName.substring(fullTypeName.indexOf(":")+1);

                            if (!"readLogsByIdDoc".equals(type)){
                                //find namespace for the prefix
                                java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (ReadLogsByIdDoc)ExtensionMapper.getTypeObject(
                                        nsUri,type,reader);
                            }


                        }


                    }




                    // Note all attributes that were handled. Used to differ normal attributes
                    // from anyAttributes.
                    java.util.Vector handledAttributes = new java.util.Vector();




                    reader.next();


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://webservice.demone.conservazione","id_doc").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                            java.lang.String content = reader.getElementText();

                            object.setId_doc(
                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                        } else {


                            reader.getElementText(); // throw away text nodes if any.
                        }

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                    }


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://webservice.demone.conservazione","azione").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                            java.lang.String content = reader.getElementText();

                            object.setAzione(
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
                    throw new java.lang.Exception(e);
                }

                return object;
            }

        }//end of factory class



    }


    public static class Exception
            implements org.apache.axis2.databinding.ADBBean{
        /* This type was generated from the piece of schema that had
                name = Exception
                Namespace URI = http://webservice.demone.conservazione
                Namespace Prefix = ns3
                */


        /**
         * field for Message
         */


        protected java.lang.String localMessage ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localMessageTracker = false ;

        public boolean isMessageSpecified(){
            return localMessageTracker;
        }



        /**
         * Auto generated getter method
         * @return java.lang.String
         */
        public  java.lang.String getMessage(){
            return localMessage;
        }



        /**
         * Auto generated setter method
         * @param param Message
         */
        public void setMessage(java.lang.String param){
            localMessageTracker = true;

            this.localMessage=param;


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




            java.lang.String prefix = null;
            java.lang.String namespace = null;


            prefix = parentQName.getPrefix();
            namespace = parentQName.getNamespaceURI();
            writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

            if (serializeType){


                java.lang.String namespacePrefix = registerPrefix(xmlWriter,"http://webservice.demone.conservazione");
                if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            namespacePrefix+":Exception",
                            xmlWriter);
                } else {
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            "Exception",
                            xmlWriter);
                }


            }
            if (localMessageTracker){
                namespace = "http://webservice.demone.conservazione";
                writeStartElement(null, namespace, "Message", xmlWriter);


                if (localMessage==null){
                    // write the nil attribute

                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                }else{


                    xmlWriter.writeCharacters(localMessage);

                }

                xmlWriter.writeEndElement();
            }
            xmlWriter.writeEndElement();


        }

        private static java.lang.String generatePrefix(java.lang.String namespace) {
            if(namespace.equals("http://webservice.demone.conservazione")){
                return "ns3";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
         * Utility method to write an element start tag.
         */
        private void writeStartElement(java.lang.String prefix, java.lang.String namespace, java.lang.String localPart,
                                       javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String writerPrefix = xmlWriter.getPrefix(namespace);
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
        private void writeAttribute(java.lang.String prefix,java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (xmlWriter.getPrefix(namespace) == null) {
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            xmlWriter.writeAttribute(namespace,attName,attValue);
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeAttribute(java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
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
        private void writeQNameAttribute(java.lang.String namespace, java.lang.String attName,
                                         javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            java.lang.String attributeNamespace = qname.getNamespaceURI();
            java.lang.String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
            if (attributePrefix == null) {
                attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
            }
            java.lang.String attributeValue;
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
            java.lang.String namespaceURI = qname.getNamespaceURI();
            if (namespaceURI != null) {
                java.lang.String prefix = xmlWriter.getPrefix(namespaceURI);
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
                java.lang.StringBuffer stringToWrite = new java.lang.StringBuffer();
                java.lang.String namespaceURI = null;
                java.lang.String prefix = null;

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
        private java.lang.String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, java.lang.String namespace) throws javax.xml.stream.XMLStreamException {
            java.lang.String prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null) {
                prefix = generatePrefix(namespace);
                javax.xml.namespace.NamespaceContext nsContext = xmlWriter.getNamespaceContext();
                while (true) {
                    java.lang.String uri = nsContext.getNamespaceURI(prefix);
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

            if (localMessageTracker){
                elementList.add(new javax.xml.namespace.QName("http://webservice.demone.conservazione",
                        "Message"));

                elementList.add(localMessage==null?null:
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localMessage));
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
            public static Exception parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{
                Exception object =
                        new Exception();

                int event;
                java.lang.String nillableValue = null;
                java.lang.String prefix ="";
                java.lang.String namespaceuri ="";
                try {

                    while (!reader.isStartElement() && !reader.isEndElement())
                        reader.next();


                    if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","type")!=null){
                        java.lang.String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                                "type");
                        if (fullTypeName!=null){
                            java.lang.String nsPrefix = null;
                            if (fullTypeName.indexOf(":") > -1){
                                nsPrefix = fullTypeName.substring(0,fullTypeName.indexOf(":"));
                            }
                            nsPrefix = nsPrefix==null?"":nsPrefix;

                            java.lang.String type = fullTypeName.substring(fullTypeName.indexOf(":")+1);

                            if (!"Exception".equals(type)){
                                //find namespace for the prefix
                                java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (Exception)ExtensionMapper.getTypeObject(
                                        nsUri,type,reader);
                            }


                        }


                    }




                    // Note all attributes that were handled. Used to differ normal attributes
                    // from anyAttributes.
                    java.util.Vector handledAttributes = new java.util.Vector();




                    reader.next();


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://webservice.demone.conservazione","Message").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                            java.lang.String content = reader.getElementText();

                            object.setMessage(
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
                    throw new java.lang.Exception(e);
                }

                return object;
            }

        }//end of factory class



    }


    public static class Log
            implements org.apache.axis2.databinding.ADBBean{
        /* This type was generated from the piece of schema that had
                name = Log
                Namespace URI = http://database.daemon.parer.docer.kdm.it/xsd
                Namespace Prefix = ns1
                */


        /**
         * field for DataChiamata
         */


        protected java.util.Calendar localDataChiamata ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localDataChiamataTracker = false ;

        public boolean isDataChiamataSpecified(){
            return localDataChiamataTracker;
        }



        /**
         * Auto generated getter method
         * @return java.util.Calendar
         */
        public  java.util.Calendar getDataChiamata(){
            return localDataChiamata;
        }



        /**
         * Auto generated setter method
         * @param param DataChiamata
         */
        public void setDataChiamata(java.util.Calendar param){
            localDataChiamataTracker = true;

            this.localDataChiamata=param;


        }


        /**
         * field for DataRegistrazione
         */


        protected java.util.Calendar localDataRegistrazione ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localDataRegistrazioneTracker = false ;

        public boolean isDataRegistrazioneSpecified(){
            return localDataRegistrazioneTracker;
        }



        /**
         * Auto generated getter method
         * @return java.util.Calendar
         */
        public  java.util.Calendar getDataRegistrazione(){
            return localDataRegistrazione;
        }



        /**
         * Auto generated setter method
         * @param param DataRegistrazione
         */
        public void setDataRegistrazione(java.util.Calendar param){
            localDataRegistrazioneTracker = true;

            this.localDataRegistrazione=param;


        }


        /**
         * field for DataRisposta
         */


        protected java.util.Calendar localDataRisposta ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localDataRispostaTracker = false ;

        public boolean isDataRispostaSpecified(){
            return localDataRispostaTracker;
        }



        /**
         * Auto generated getter method
         * @return java.util.Calendar
         */
        public  java.util.Calendar getDataRisposta(){
            return localDataRisposta;
        }



        /**
         * Auto generated setter method
         * @param param DataRisposta
         */
        public void setDataRisposta(java.util.Calendar param){
            localDataRispostaTracker = true;

            this.localDataRisposta=param;


        }


        /**
         * field for ErrorCode
         */


        protected java.lang.String localErrorCode ;

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
         * @return java.lang.String
         */
        public  java.lang.String getErrorCode(){
            return localErrorCode;
        }



        /**
         * Auto generated setter method
         * @param param ErrorCode
         */
        public void setErrorCode(java.lang.String param){
            localErrorCodeTracker = true;

            this.localErrorCode=param;


        }


        /**
         * field for Esito
         */


        protected java.lang.String localEsito ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localEsitoTracker = false ;

        public boolean isEsitoSpecified(){
            return localEsitoTracker;
        }



        /**
         * Auto generated getter method
         * @return java.lang.String
         */
        public  java.lang.String getEsito(){
            return localEsito;
        }



        /**
         * Auto generated setter method
         * @param param Esito
         */
        public void setEsito(java.lang.String param){
            localEsitoTracker = param != null;

            this.localEsito=param;


        }


        /**
         * field for ForzaAccettazione
         */


        protected boolean localForzaAccettazione ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localForzaAccettazioneTracker = false ;

        public boolean isForzaAccettazioneSpecified(){
            return localForzaAccettazioneTracker;
        }



        /**
         * Auto generated getter method
         * @return boolean
         */
        public  boolean getForzaAccettazione(){
            return localForzaAccettazione;
        }



        /**
         * Auto generated setter method
         * @param param ForzaAccettazione
         */
        public void setForzaAccettazione(boolean param){
            localForzaAccettazioneTracker = true;

            this.localForzaAccettazione=param;


        }


        /**
         * field for ForzaCollegamento
         */


        protected boolean localForzaCollegamento ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localForzaCollegamentoTracker = false ;

        public boolean isForzaCollegamentoSpecified(){
            return localForzaCollegamentoTracker;
        }



        /**
         * Auto generated getter method
         * @return boolean
         */
        public  boolean getForzaCollegamento(){
            return localForzaCollegamento;
        }



        /**
         * Auto generated setter method
         * @param param ForzaCollegamento
         */
        public void setForzaCollegamento(boolean param){
            localForzaCollegamentoTracker = true;

            this.localForzaCollegamento=param;


        }


        /**
         * field for ForzaConservazione
         */


        protected boolean localForzaConservazione ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localForzaConservazioneTracker = false ;

        public boolean isForzaConservazioneSpecified(){
            return localForzaConservazioneTracker;
        }



        /**
         * Auto generated getter method
         * @return boolean
         */
        public  boolean getForzaConservazione(){
            return localForzaConservazione;
        }



        /**
         * Auto generated setter method
         * @param param ForzaConservazione
         */
        public void setForzaConservazione(boolean param){
            localForzaConservazioneTracker = true;

            this.localForzaConservazione=param;


        }


        /**
         * field for Id
         */


        protected long localId ;

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
         * @return long
         */
        public  long getId(){
            return localId;
        }



        /**
         * Auto generated setter method
         * @param param Id
         */
        public void setId(long param){
            localIdTracker = true;

            this.localId=param;


        }


        /**
         * field for Job
         */


        protected Job localJob ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localJobTracker = false ;

        public boolean isJobSpecified(){
            return localJobTracker;
        }



        /**
         * Auto generated getter method
         * @return Job
         */
        public  Job getJob(){
            return localJob;
        }



        /**
         * Auto generated setter method
         * @param param Job
         */
        public void setJob(Job param){
            localJobTracker = true;

            this.localJob=param;


        }


        /**
         * field for Message
         */


        protected java.lang.String localMessage ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localMessageTracker = false ;

        public boolean isMessageSpecified(){
            return localMessageTracker;
        }



        /**
         * Auto generated getter method
         * @return java.lang.String
         */
        public  java.lang.String getMessage(){
            return localMessage;
        }



        /**
         * Auto generated setter method
         * @param param Message
         */
        public void setMessage(java.lang.String param){
            localMessageTracker = true;

            this.localMessage=param;


        }


        /**
         * field for Version
         */


        protected int localVersion ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localVersionTracker = false ;

        public boolean isVersionSpecified(){
            return localVersionTracker;
        }



        /**
         * Auto generated getter method
         * @return int
         */
        public  int getVersion(){
            return localVersion;
        }



        /**
         * Auto generated setter method
         * @param param Version
         */
        public void setVersion(int param){
            localVersionTracker = true;

            this.localVersion=param;


        }


        /**
         * field for XmlEsito
         */


        protected java.lang.String localXmlEsito ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localXmlEsitoTracker = false ;

        public boolean isXmlEsitoSpecified(){
            return localXmlEsitoTracker;
        }



        /**
         * Auto generated getter method
         * @return java.lang.String
         */
        public  java.lang.String getXmlEsito(){
            return localXmlEsito;
        }



        /**
         * Auto generated setter method
         * @param param XmlEsito
         */
        public void setXmlEsito(java.lang.String param){
            localXmlEsitoTracker = true;

            this.localXmlEsito=param;


        }


        /**
         * field for XmlRichiesta
         */


        protected java.lang.String localXmlRichiesta ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localXmlRichiestaTracker = false ;

        public boolean isXmlRichiestaSpecified(){
            return localXmlRichiestaTracker;
        }



        /**
         * Auto generated getter method
         * @return java.lang.String
         */
        public  java.lang.String getXmlRichiesta(){
            return localXmlRichiesta;
        }



        /**
         * Auto generated setter method
         * @param param XmlRichiesta
         */
        public void setXmlRichiesta(java.lang.String param){
            localXmlRichiestaTracker = true;

            this.localXmlRichiesta=param;


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




            java.lang.String prefix = null;
            java.lang.String namespace = null;


            prefix = parentQName.getPrefix();
            namespace = parentQName.getNamespaceURI();
            writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

            if (serializeType){


                java.lang.String namespacePrefix = registerPrefix(xmlWriter,"http://database.daemon.parer.docer.kdm.it/xsd");
                if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            namespacePrefix+":Log",
                            xmlWriter);
                } else {
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            "Log",
                            xmlWriter);
                }


            }
            if (localDataChiamataTracker){
                namespace = "http://database.daemon.parer.docer.kdm.it/xsd";
                writeStartElement(null, namespace, "dataChiamata", xmlWriter);


                if (localDataChiamata==null){
                    // write the nil attribute

                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                }else{


                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDataChiamata));

                }

                xmlWriter.writeEndElement();
            } if (localDataRegistrazioneTracker){
                namespace = "http://database.daemon.parer.docer.kdm.it/xsd";
                writeStartElement(null, namespace, "dataRegistrazione", xmlWriter);


                if (localDataRegistrazione==null){
                    // write the nil attribute

                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                }else{


                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDataRegistrazione));

                }

                xmlWriter.writeEndElement();
            } if (localDataRispostaTracker){
                namespace = "http://database.daemon.parer.docer.kdm.it/xsd";
                writeStartElement(null, namespace, "dataRisposta", xmlWriter);


                if (localDataRisposta==null){
                    // write the nil attribute

                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                }else{


                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDataRisposta));

                }

                xmlWriter.writeEndElement();
            } if (localErrorCodeTracker){
                namespace = "http://database.daemon.parer.docer.kdm.it/xsd";
                writeStartElement(null, namespace, "errorCode", xmlWriter);


                if (localErrorCode==null){
                    // write the nil attribute

                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                }else{


                    xmlWriter.writeCharacters(localErrorCode);

                }

                xmlWriter.writeEndElement();
            } if (localEsitoTracker){
                namespace = "http://database.daemon.parer.docer.kdm.it/xsd";
                writeStartElement(null, namespace, "esito", xmlWriter);


                if (localEsito==null){
                    // write the nil attribute

                    throw new org.apache.axis2.databinding.ADBException("esito cannot be null!!");

                }else{


                    xmlWriter.writeCharacters(localEsito);

                }

                xmlWriter.writeEndElement();
            } if (localForzaAccettazioneTracker){
                namespace = "http://database.daemon.parer.docer.kdm.it/xsd";
                writeStartElement(null, namespace, "forzaAccettazione", xmlWriter);

                if (false) {

                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                } else {
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localForzaAccettazione));
                }

                xmlWriter.writeEndElement();
            } if (localForzaCollegamentoTracker){
                namespace = "http://database.daemon.parer.docer.kdm.it/xsd";
                writeStartElement(null, namespace, "forzaCollegamento", xmlWriter);

                if (false) {

                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                } else {
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localForzaCollegamento));
                }

                xmlWriter.writeEndElement();
            } if (localForzaConservazioneTracker){
                namespace = "http://database.daemon.parer.docer.kdm.it/xsd";
                writeStartElement(null, namespace, "forzaConservazione", xmlWriter);

                if (false) {

                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                } else {
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localForzaConservazione));
                }

                xmlWriter.writeEndElement();
            } if (localIdTracker){
                namespace = "http://database.daemon.parer.docer.kdm.it/xsd";
                writeStartElement(null, namespace, "id", xmlWriter);

                if (localId==java.lang.Long.MIN_VALUE) {

                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                } else {
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localId));
                }

                xmlWriter.writeEndElement();
            } if (localJobTracker){
                if (localJob==null){

                    writeStartElement(null, "http://database.daemon.parer.docer.kdm.it/xsd", "job", xmlWriter);

                    // write the nil attribute
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                    xmlWriter.writeEndElement();
                }else{
                    localJob.serialize(new javax.xml.namespace.QName("http://database.daemon.parer.docer.kdm.it/xsd","job"),
                            xmlWriter);
                }
            } if (localMessageTracker){
                namespace = "http://database.daemon.parer.docer.kdm.it/xsd";
                writeStartElement(null, namespace, "message", xmlWriter);


                if (localMessage==null){
                    // write the nil attribute

                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                }else{


                    xmlWriter.writeCharacters(localMessage);

                }

                xmlWriter.writeEndElement();
            } if (localVersionTracker){
                namespace = "http://database.daemon.parer.docer.kdm.it/xsd";
                writeStartElement(null, namespace, "version", xmlWriter);

                if (localVersion==java.lang.Integer.MIN_VALUE) {

                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                } else {
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localVersion));
                }

                xmlWriter.writeEndElement();
            } if (localXmlEsitoTracker){
                namespace = "http://database.daemon.parer.docer.kdm.it/xsd";
                writeStartElement(null, namespace, "xmlEsito", xmlWriter);


                if (localXmlEsito==null){
                    // write the nil attribute

                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                }else{


                    xmlWriter.writeCharacters(localXmlEsito);

                }

                xmlWriter.writeEndElement();
            } if (localXmlRichiestaTracker){
                namespace = "http://database.daemon.parer.docer.kdm.it/xsd";
                writeStartElement(null, namespace, "xmlRichiesta", xmlWriter);


                if (localXmlRichiesta==null){
                    // write the nil attribute

                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                }else{


                    xmlWriter.writeCharacters(localXmlRichiesta);

                }

                xmlWriter.writeEndElement();
            }
            xmlWriter.writeEndElement();


        }

        private static java.lang.String generatePrefix(java.lang.String namespace) {
            if(namespace.equals("http://database.daemon.parer.docer.kdm.it/xsd")){
                return "ns1";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
         * Utility method to write an element start tag.
         */
        private void writeStartElement(java.lang.String prefix, java.lang.String namespace, java.lang.String localPart,
                                       javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String writerPrefix = xmlWriter.getPrefix(namespace);
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
        private void writeAttribute(java.lang.String prefix,java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (xmlWriter.getPrefix(namespace) == null) {
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            xmlWriter.writeAttribute(namespace,attName,attValue);
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeAttribute(java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
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
        private void writeQNameAttribute(java.lang.String namespace, java.lang.String attName,
                                         javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            java.lang.String attributeNamespace = qname.getNamespaceURI();
            java.lang.String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
            if (attributePrefix == null) {
                attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
            }
            java.lang.String attributeValue;
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
            java.lang.String namespaceURI = qname.getNamespaceURI();
            if (namespaceURI != null) {
                java.lang.String prefix = xmlWriter.getPrefix(namespaceURI);
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
                java.lang.StringBuffer stringToWrite = new java.lang.StringBuffer();
                java.lang.String namespaceURI = null;
                java.lang.String prefix = null;

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
        private java.lang.String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, java.lang.String namespace) throws javax.xml.stream.XMLStreamException {
            java.lang.String prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null) {
                prefix = generatePrefix(namespace);
                javax.xml.namespace.NamespaceContext nsContext = xmlWriter.getNamespaceContext();
                while (true) {
                    java.lang.String uri = nsContext.getNamespaceURI(prefix);
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

            if (localDataChiamataTracker){
                elementList.add(new javax.xml.namespace.QName("http://database.daemon.parer.docer.kdm.it/xsd",
                        "dataChiamata"));

                elementList.add(localDataChiamata==null?null:
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDataChiamata));
            } if (localDataRegistrazioneTracker){
                elementList.add(new javax.xml.namespace.QName("http://database.daemon.parer.docer.kdm.it/xsd",
                        "dataRegistrazione"));

                elementList.add(localDataRegistrazione==null?null:
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDataRegistrazione));
            } if (localDataRispostaTracker){
                elementList.add(new javax.xml.namespace.QName("http://database.daemon.parer.docer.kdm.it/xsd",
                        "dataRisposta"));

                elementList.add(localDataRisposta==null?null:
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDataRisposta));
            } if (localErrorCodeTracker){
                elementList.add(new javax.xml.namespace.QName("http://database.daemon.parer.docer.kdm.it/xsd",
                        "errorCode"));

                elementList.add(localErrorCode==null?null:
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localErrorCode));
            } if (localEsitoTracker){
                elementList.add(new javax.xml.namespace.QName("http://database.daemon.parer.docer.kdm.it/xsd",
                        "esito"));

                if (localEsito != null){
                    elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localEsito));
                } else {
                    throw new org.apache.axis2.databinding.ADBException("esito cannot be null!!");
                }
            } if (localForzaAccettazioneTracker){
                elementList.add(new javax.xml.namespace.QName("http://database.daemon.parer.docer.kdm.it/xsd",
                        "forzaAccettazione"));

                elementList.add(
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localForzaAccettazione));
            } if (localForzaCollegamentoTracker){
                elementList.add(new javax.xml.namespace.QName("http://database.daemon.parer.docer.kdm.it/xsd",
                        "forzaCollegamento"));

                elementList.add(
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localForzaCollegamento));
            } if (localForzaConservazioneTracker){
                elementList.add(new javax.xml.namespace.QName("http://database.daemon.parer.docer.kdm.it/xsd",
                        "forzaConservazione"));

                elementList.add(
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localForzaConservazione));
            } if (localIdTracker){
                elementList.add(new javax.xml.namespace.QName("http://database.daemon.parer.docer.kdm.it/xsd",
                        "id"));

                elementList.add(
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localId));
            } if (localJobTracker){
                elementList.add(new javax.xml.namespace.QName("http://database.daemon.parer.docer.kdm.it/xsd",
                        "job"));


                elementList.add(localJob==null?null:
                        localJob);
            } if (localMessageTracker){
                elementList.add(new javax.xml.namespace.QName("http://database.daemon.parer.docer.kdm.it/xsd",
                        "message"));

                elementList.add(localMessage==null?null:
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localMessage));
            } if (localVersionTracker){
                elementList.add(new javax.xml.namespace.QName("http://database.daemon.parer.docer.kdm.it/xsd",
                        "version"));

                elementList.add(
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localVersion));
            } if (localXmlEsitoTracker){
                elementList.add(new javax.xml.namespace.QName("http://database.daemon.parer.docer.kdm.it/xsd",
                        "xmlEsito"));

                elementList.add(localXmlEsito==null?null:
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localXmlEsito));
            } if (localXmlRichiestaTracker){
                elementList.add(new javax.xml.namespace.QName("http://database.daemon.parer.docer.kdm.it/xsd",
                        "xmlRichiesta"));

                elementList.add(localXmlRichiesta==null?null:
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localXmlRichiesta));
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
            public static Log parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{
                Log object =
                        new Log();

                int event;
                java.lang.String nillableValue = null;
                java.lang.String prefix ="";
                java.lang.String namespaceuri ="";
                try {

                    while (!reader.isStartElement() && !reader.isEndElement())
                        reader.next();


                    if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","type")!=null){
                        java.lang.String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                                "type");
                        if (fullTypeName!=null){
                            java.lang.String nsPrefix = null;
                            if (fullTypeName.indexOf(":") > -1){
                                nsPrefix = fullTypeName.substring(0,fullTypeName.indexOf(":"));
                            }
                            nsPrefix = nsPrefix==null?"":nsPrefix;

                            java.lang.String type = fullTypeName.substring(fullTypeName.indexOf(":")+1);

                            if (!"Log".equals(type)){
                                //find namespace for the prefix
                                java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (Log)ExtensionMapper.getTypeObject(
                                        nsUri,type,reader);
                            }


                        }


                    }




                    // Note all attributes that were handled. Used to differ normal attributes
                    // from anyAttributes.
                    java.util.Vector handledAttributes = new java.util.Vector();




                    reader.next();


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://database.daemon.parer.docer.kdm.it/xsd","dataChiamata").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                            java.lang.String content = reader.getElementText();

                            object.setDataChiamata(
                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToDateTime(content));

                        } else {


                            reader.getElementText(); // throw away text nodes if any.
                        }

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                    }


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://database.daemon.parer.docer.kdm.it/xsd","dataRegistrazione").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                            java.lang.String content = reader.getElementText();

                            object.setDataRegistrazione(
                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToDateTime(content));

                        } else {


                            reader.getElementText(); // throw away text nodes if any.
                        }

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                    }


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://database.daemon.parer.docer.kdm.it/xsd","dataRisposta").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                            java.lang.String content = reader.getElementText();

                            object.setDataRisposta(
                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToDateTime(content));

                        } else {


                            reader.getElementText(); // throw away text nodes if any.
                        }

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                    }


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://database.daemon.parer.docer.kdm.it/xsd","errorCode").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                            java.lang.String content = reader.getElementText();

                            object.setErrorCode(
                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                        } else {


                            reader.getElementText(); // throw away text nodes if any.
                        }

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                    }


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://database.daemon.parer.docer.kdm.it/xsd","esito").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                            throw new org.apache.axis2.databinding.ADBException("The element: "+"esito" +"  cannot be null");
                        }


                        java.lang.String content = reader.getElementText();

                        object.setEsito(
                                org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                    }


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://database.daemon.parer.docer.kdm.it/xsd","forzaAccettazione").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                            java.lang.String content = reader.getElementText();

                            object.setForzaAccettazione(
                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));

                        } else {


                            reader.getElementText(); // throw away text nodes if any.
                        }

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                    }


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://database.daemon.parer.docer.kdm.it/xsd","forzaCollegamento").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                            java.lang.String content = reader.getElementText();

                            object.setForzaCollegamento(
                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));

                        } else {


                            reader.getElementText(); // throw away text nodes if any.
                        }

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                    }


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://database.daemon.parer.docer.kdm.it/xsd","forzaConservazione").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                            java.lang.String content = reader.getElementText();

                            object.setForzaConservazione(
                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));

                        } else {


                            reader.getElementText(); // throw away text nodes if any.
                        }

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                    }


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://database.daemon.parer.docer.kdm.it/xsd","id").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                            java.lang.String content = reader.getElementText();

                            object.setId(
                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToLong(content));

                        } else {


                            object.setId(java.lang.Long.MIN_VALUE);

                            reader.getElementText(); // throw away text nodes if any.
                        }

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                        object.setId(java.lang.Long.MIN_VALUE);

                    }


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://database.daemon.parer.docer.kdm.it/xsd","job").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                            object.setJob(null);
                            reader.next();

                            reader.next();

                        }else{

                            object.setJob(Job.Factory.parse(reader));

                            reader.next();
                        }
                    }  // End of if for expected property start element

                    else {

                    }


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://database.daemon.parer.docer.kdm.it/xsd","message").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                            java.lang.String content = reader.getElementText();

                            object.setMessage(
                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                        } else {


                            reader.getElementText(); // throw away text nodes if any.
                        }

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                    }


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://database.daemon.parer.docer.kdm.it/xsd","version").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                            java.lang.String content = reader.getElementText();

                            object.setVersion(
                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));

                        } else {


                            object.setVersion(java.lang.Integer.MIN_VALUE);

                            reader.getElementText(); // throw away text nodes if any.
                        }

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                        object.setVersion(java.lang.Integer.MIN_VALUE);

                    }


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://database.daemon.parer.docer.kdm.it/xsd","xmlEsito").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                            java.lang.String content = reader.getElementText();

                            object.setXmlEsito(
                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                        } else {


                            reader.getElementText(); // throw away text nodes if any.
                        }

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                    }


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://database.daemon.parer.docer.kdm.it/xsd","xmlRichiesta").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                            java.lang.String content = reader.getElementText();

                            object.setXmlRichiesta(
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
                    throw new java.lang.Exception(e);
                }

                return object;
            }

        }//end of factory class



    }


    private  org.apache.axiom.om.OMElement  toOM(it.kdm.docer.clients.WSDemoneStub.SearchJob param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {


        try{
            return param.getOMElement(it.kdm.docer.clients.WSDemoneStub.SearchJob.MY_QNAME,
                    org.apache.axiom.om.OMAbstractFactory.getOMFactory());
        } catch(org.apache.axis2.databinding.ADBException e){
            throw org.apache.axis2.AxisFault.makeFault(e);
        }


    }

    private  org.apache.axiom.om.OMElement  toOM(it.kdm.docer.clients.WSDemoneStub.SearchJobResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {


        try{
            return param.getOMElement(it.kdm.docer.clients.WSDemoneStub.SearchJobResponse.MY_QNAME,
                    org.apache.axiom.om.OMAbstractFactory.getOMFactory());
        } catch(org.apache.axis2.databinding.ADBException e){
            throw org.apache.axis2.AxisFault.makeFault(e);
        }


    }

    private  org.apache.axiom.om.OMElement  toOM(it.kdm.docer.clients.WSDemoneStub.WSDemoneException param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {


        try{
            return param.getOMElement(it.kdm.docer.clients.WSDemoneStub.WSDemoneException.MY_QNAME,
                    org.apache.axiom.om.OMAbstractFactory.getOMFactory());
        } catch(org.apache.axis2.databinding.ADBException e){
            throw org.apache.axis2.AxisFault.makeFault(e);
        }


    }

    private  org.apache.axiom.om.OMElement  toOM(it.kdm.docer.clients.WSDemoneStub.GetJobGson param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {


        try{
            return param.getOMElement(it.kdm.docer.clients.WSDemoneStub.GetJobGson.MY_QNAME,
                    org.apache.axiom.om.OMAbstractFactory.getOMFactory());
        } catch(org.apache.axis2.databinding.ADBException e){
            throw org.apache.axis2.AxisFault.makeFault(e);
        }


    }

    private  org.apache.axiom.om.OMElement  toOM(it.kdm.docer.clients.WSDemoneStub.GetJobGsonResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {


        try{
            return param.getOMElement(it.kdm.docer.clients.WSDemoneStub.GetJobGsonResponse.MY_QNAME,
                    org.apache.axiom.om.OMAbstractFactory.getOMFactory());
        } catch(org.apache.axis2.databinding.ADBException e){
            throw org.apache.axis2.AxisFault.makeFault(e);
        }


    }

    private  org.apache.axiom.om.OMElement  toOM(it.kdm.docer.clients.WSDemoneStub.SearchJobEstesa param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {


        try{
            return param.getOMElement(it.kdm.docer.clients.WSDemoneStub.SearchJobEstesa.MY_QNAME,
                    org.apache.axiom.om.OMAbstractFactory.getOMFactory());
        } catch(org.apache.axis2.databinding.ADBException e){
            throw org.apache.axis2.AxisFault.makeFault(e);
        }


    }

    private  org.apache.axiom.om.OMElement  toOM(it.kdm.docer.clients.WSDemoneStub.SearchJobEstesaResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {


        try{
            return param.getOMElement(it.kdm.docer.clients.WSDemoneStub.SearchJobEstesaResponse.MY_QNAME,
                    org.apache.axiom.om.OMAbstractFactory.getOMFactory());
        } catch(org.apache.axis2.databinding.ADBException e){
            throw org.apache.axis2.AxisFault.makeFault(e);
        }


    }

    private  org.apache.axiom.om.OMElement  toOM(it.kdm.docer.clients.WSDemoneStub.ReadLogsByIdJob param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {


        try{
            return param.getOMElement(it.kdm.docer.clients.WSDemoneStub.ReadLogsByIdJob.MY_QNAME,
                    org.apache.axiom.om.OMAbstractFactory.getOMFactory());
        } catch(org.apache.axis2.databinding.ADBException e){
            throw org.apache.axis2.AxisFault.makeFault(e);
        }


    }

    private  org.apache.axiom.om.OMElement  toOM(it.kdm.docer.clients.WSDemoneStub.ReadLogsByIdJobResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {


        try{
            return param.getOMElement(it.kdm.docer.clients.WSDemoneStub.ReadLogsByIdJobResponse.MY_QNAME,
                    org.apache.axiom.om.OMAbstractFactory.getOMFactory());
        } catch(org.apache.axis2.databinding.ADBException e){
            throw org.apache.axis2.AxisFault.makeFault(e);
        }


    }

    private  org.apache.axiom.om.OMElement  toOM(it.kdm.docer.clients.WSDemoneStub.ReadLogGson_byIdJob param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {


        try{
            return param.getOMElement(it.kdm.docer.clients.WSDemoneStub.ReadLogGson_byIdJob.MY_QNAME,
                    org.apache.axiom.om.OMAbstractFactory.getOMFactory());
        } catch(org.apache.axis2.databinding.ADBException e){
            throw org.apache.axis2.AxisFault.makeFault(e);
        }


    }

    private  org.apache.axiom.om.OMElement  toOM(it.kdm.docer.clients.WSDemoneStub.ReadLogGson_byIdJobResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {


        try{
            return param.getOMElement(it.kdm.docer.clients.WSDemoneStub.ReadLogGson_byIdJobResponse.MY_QNAME,
                    org.apache.axiom.om.OMAbstractFactory.getOMFactory());
        } catch(org.apache.axis2.databinding.ADBException e){
            throw org.apache.axis2.AxisFault.makeFault(e);
        }


    }

    private  org.apache.axiom.om.OMElement  toOM(it.kdm.docer.clients.WSDemoneStub.DeleteJob_ByIdDoc param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {


        try{
            return param.getOMElement(it.kdm.docer.clients.WSDemoneStub.DeleteJob_ByIdDoc.MY_QNAME,
                    org.apache.axiom.om.OMAbstractFactory.getOMFactory());
        } catch(org.apache.axis2.databinding.ADBException e){
            throw org.apache.axis2.AxisFault.makeFault(e);
        }


    }

    private  org.apache.axiom.om.OMElement  toOM(it.kdm.docer.clients.WSDemoneStub.DeleteJob_ByIdDocResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {


        try{
            return param.getOMElement(it.kdm.docer.clients.WSDemoneStub.DeleteJob_ByIdDocResponse.MY_QNAME,
                    org.apache.axiom.om.OMAbstractFactory.getOMFactory());
        } catch(org.apache.axis2.databinding.ADBException e){
            throw org.apache.axis2.AxisFault.makeFault(e);
        }


    }

    private  org.apache.axiom.om.OMElement  toOM(it.kdm.docer.clients.WSDemoneStub.GetJob param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {


        try{
            return param.getOMElement(it.kdm.docer.clients.WSDemoneStub.GetJob.MY_QNAME,
                    org.apache.axiom.om.OMAbstractFactory.getOMFactory());
        } catch(org.apache.axis2.databinding.ADBException e){
            throw org.apache.axis2.AxisFault.makeFault(e);
        }


    }

    private  org.apache.axiom.om.OMElement  toOM(it.kdm.docer.clients.WSDemoneStub.GetJobResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {


        try{
            return param.getOMElement(it.kdm.docer.clients.WSDemoneStub.GetJobResponse.MY_QNAME,
                    org.apache.axiom.om.OMAbstractFactory.getOMFactory());
        } catch(org.apache.axis2.databinding.ADBException e){
            throw org.apache.axis2.AxisFault.makeFault(e);
        }


    }

    private  org.apache.axiom.om.OMElement  toOM(it.kdm.docer.clients.WSDemoneStub.SearchLogEstesa param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {


        try{
            return param.getOMElement(it.kdm.docer.clients.WSDemoneStub.SearchLogEstesa.MY_QNAME,
                    org.apache.axiom.om.OMAbstractFactory.getOMFactory());
        } catch(org.apache.axis2.databinding.ADBException e){
            throw org.apache.axis2.AxisFault.makeFault(e);
        }


    }

    private  org.apache.axiom.om.OMElement  toOM(it.kdm.docer.clients.WSDemoneStub.SearchLogEstesaResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {


        try{
            return param.getOMElement(it.kdm.docer.clients.WSDemoneStub.SearchLogEstesaResponse.MY_QNAME,
                    org.apache.axiom.om.OMAbstractFactory.getOMFactory());
        } catch(org.apache.axis2.databinding.ADBException e){
            throw org.apache.axis2.AxisFault.makeFault(e);
        }


    }

    private  org.apache.axiom.om.OMElement  toOM(it.kdm.docer.clients.WSDemoneStub.GetJobByDocId param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {


        try{
            return param.getOMElement(it.kdm.docer.clients.WSDemoneStub.GetJobByDocId.MY_QNAME,
                    org.apache.axiom.om.OMAbstractFactory.getOMFactory());
        } catch(org.apache.axis2.databinding.ADBException e){
            throw org.apache.axis2.AxisFault.makeFault(e);
        }


    }

    private  org.apache.axiom.om.OMElement  toOM(it.kdm.docer.clients.WSDemoneStub.GetJobByDocIdResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {


        try{
            return param.getOMElement(it.kdm.docer.clients.WSDemoneStub.GetJobByDocIdResponse.MY_QNAME,
                    org.apache.axiom.om.OMAbstractFactory.getOMFactory());
        } catch(org.apache.axis2.databinding.ADBException e){
            throw org.apache.axis2.AxisFault.makeFault(e);
        }


    }

    private  org.apache.axiom.om.OMElement  toOM(it.kdm.docer.clients.WSDemoneStub.DeleteJob_byIdJob param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {


        try{
            return param.getOMElement(it.kdm.docer.clients.WSDemoneStub.DeleteJob_byIdJob.MY_QNAME,
                    org.apache.axiom.om.OMAbstractFactory.getOMFactory());
        } catch(org.apache.axis2.databinding.ADBException e){
            throw org.apache.axis2.AxisFault.makeFault(e);
        }


    }

    private  org.apache.axiom.om.OMElement  toOM(it.kdm.docer.clients.WSDemoneStub.DeleteJob_byIdJobResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {


        try{
            return param.getOMElement(it.kdm.docer.clients.WSDemoneStub.DeleteJob_byIdJobResponse.MY_QNAME,
                    org.apache.axiom.om.OMAbstractFactory.getOMFactory());
        } catch(org.apache.axis2.databinding.ADBException e){
            throw org.apache.axis2.AxisFault.makeFault(e);
        }


    }

    private  org.apache.axiom.om.OMElement  toOM(it.kdm.docer.clients.WSDemoneStub.UpdateJob param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {


        try{
            return param.getOMElement(it.kdm.docer.clients.WSDemoneStub.UpdateJob.MY_QNAME,
                    org.apache.axiom.om.OMAbstractFactory.getOMFactory());
        } catch(org.apache.axis2.databinding.ADBException e){
            throw org.apache.axis2.AxisFault.makeFault(e);
        }


    }

    private  org.apache.axiom.om.OMElement  toOM(it.kdm.docer.clients.WSDemoneStub.UpdateJobResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {


        try{
            return param.getOMElement(it.kdm.docer.clients.WSDemoneStub.UpdateJobResponse.MY_QNAME,
                    org.apache.axiom.om.OMAbstractFactory.getOMFactory());
        } catch(org.apache.axis2.databinding.ADBException e){
            throw org.apache.axis2.AxisFault.makeFault(e);
        }


    }

    private  org.apache.axiom.om.OMElement  toOM(it.kdm.docer.clients.WSDemoneStub.ReadLogsByIdDoc param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {


        try{
            return param.getOMElement(it.kdm.docer.clients.WSDemoneStub.ReadLogsByIdDoc.MY_QNAME,
                    org.apache.axiom.om.OMAbstractFactory.getOMFactory());
        } catch(org.apache.axis2.databinding.ADBException e){
            throw org.apache.axis2.AxisFault.makeFault(e);
        }


    }

    private  org.apache.axiom.om.OMElement  toOM(it.kdm.docer.clients.WSDemoneStub.ReadLogsByIdDocResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {


        try{
            return param.getOMElement(it.kdm.docer.clients.WSDemoneStub.ReadLogsByIdDocResponse.MY_QNAME,
                    org.apache.axiom.om.OMAbstractFactory.getOMFactory());
        } catch(org.apache.axis2.databinding.ADBException e){
            throw org.apache.axis2.AxisFault.makeFault(e);
        }


    }

    private  org.apache.axiom.om.OMElement  toOM(it.kdm.docer.clients.WSDemoneStub.AddJob param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {


        try{
            return param.getOMElement(it.kdm.docer.clients.WSDemoneStub.AddJob.MY_QNAME,
                    org.apache.axiom.om.OMAbstractFactory.getOMFactory());
        } catch(org.apache.axis2.databinding.ADBException e){
            throw org.apache.axis2.AxisFault.makeFault(e);
        }


    }

    private  org.apache.axiom.om.OMElement  toOM(it.kdm.docer.clients.WSDemoneStub.AddJobResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {


        try{
            return param.getOMElement(it.kdm.docer.clients.WSDemoneStub.AddJobResponse.MY_QNAME,
                    org.apache.axiom.om.OMAbstractFactory.getOMFactory());
        } catch(org.apache.axis2.databinding.ADBException e){
            throw org.apache.axis2.AxisFault.makeFault(e);
        }


    }


    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, it.kdm.docer.clients.WSDemoneStub.SearchJob param, boolean optimizeContent, javax.xml.namespace.QName methodQName)
            throws org.apache.axis2.AxisFault{


        try{

            org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(it.kdm.docer.clients.WSDemoneStub.SearchJob.MY_QNAME,factory));
            return emptyEnvelope;
        } catch(org.apache.axis2.databinding.ADBException e){
            throw org.apache.axis2.AxisFault.makeFault(e);
        }


    }
                                
                             
                             /* methods to provide back word compatibility */



    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, it.kdm.docer.clients.WSDemoneStub.GetJobGson param, boolean optimizeContent, javax.xml.namespace.QName methodQName)
            throws org.apache.axis2.AxisFault{


        try{

            org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(it.kdm.docer.clients.WSDemoneStub.GetJobGson.MY_QNAME,factory));
            return emptyEnvelope;
        } catch(org.apache.axis2.databinding.ADBException e){
            throw org.apache.axis2.AxisFault.makeFault(e);
        }


    }
                                
                             
                             /* methods to provide back word compatibility */



    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, it.kdm.docer.clients.WSDemoneStub.SearchJobEstesa param, boolean optimizeContent, javax.xml.namespace.QName methodQName)
            throws org.apache.axis2.AxisFault{


        try{

            org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(it.kdm.docer.clients.WSDemoneStub.SearchJobEstesa.MY_QNAME,factory));
            return emptyEnvelope;
        } catch(org.apache.axis2.databinding.ADBException e){
            throw org.apache.axis2.AxisFault.makeFault(e);
        }


    }
                                
                             
                             /* methods to provide back word compatibility */



    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, it.kdm.docer.clients.WSDemoneStub.ReadLogsByIdJob param, boolean optimizeContent, javax.xml.namespace.QName methodQName)
            throws org.apache.axis2.AxisFault{


        try{

            org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(it.kdm.docer.clients.WSDemoneStub.ReadLogsByIdJob.MY_QNAME,factory));
            return emptyEnvelope;
        } catch(org.apache.axis2.databinding.ADBException e){
            throw org.apache.axis2.AxisFault.makeFault(e);
        }


    }
                                
                             
                             /* methods to provide back word compatibility */



    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, it.kdm.docer.clients.WSDemoneStub.ReadLogGson_byIdJob param, boolean optimizeContent, javax.xml.namespace.QName methodQName)
            throws org.apache.axis2.AxisFault{


        try{

            org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(it.kdm.docer.clients.WSDemoneStub.ReadLogGson_byIdJob.MY_QNAME,factory));
            return emptyEnvelope;
        } catch(org.apache.axis2.databinding.ADBException e){
            throw org.apache.axis2.AxisFault.makeFault(e);
        }


    }
                                
                             
                             /* methods to provide back word compatibility */



    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, it.kdm.docer.clients.WSDemoneStub.DeleteJob_ByIdDoc param, boolean optimizeContent, javax.xml.namespace.QName methodQName)
            throws org.apache.axis2.AxisFault{


        try{

            org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(it.kdm.docer.clients.WSDemoneStub.DeleteJob_ByIdDoc.MY_QNAME,factory));
            return emptyEnvelope;
        } catch(org.apache.axis2.databinding.ADBException e){
            throw org.apache.axis2.AxisFault.makeFault(e);
        }


    }
                                
                             
                             /* methods to provide back word compatibility */



    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, it.kdm.docer.clients.WSDemoneStub.GetJob param, boolean optimizeContent, javax.xml.namespace.QName methodQName)
            throws org.apache.axis2.AxisFault{


        try{

            org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(it.kdm.docer.clients.WSDemoneStub.GetJob.MY_QNAME,factory));
            return emptyEnvelope;
        } catch(org.apache.axis2.databinding.ADBException e){
            throw org.apache.axis2.AxisFault.makeFault(e);
        }


    }
                                
                             
                             /* methods to provide back word compatibility */



    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, it.kdm.docer.clients.WSDemoneStub.SearchLogEstesa param, boolean optimizeContent, javax.xml.namespace.QName methodQName)
            throws org.apache.axis2.AxisFault{


        try{

            org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(it.kdm.docer.clients.WSDemoneStub.SearchLogEstesa.MY_QNAME,factory));
            return emptyEnvelope;
        } catch(org.apache.axis2.databinding.ADBException e){
            throw org.apache.axis2.AxisFault.makeFault(e);
        }


    }
                                
                             
                             /* methods to provide back word compatibility */



    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, it.kdm.docer.clients.WSDemoneStub.GetJobByDocId param, boolean optimizeContent, javax.xml.namespace.QName methodQName)
            throws org.apache.axis2.AxisFault{


        try{

            org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(it.kdm.docer.clients.WSDemoneStub.GetJobByDocId.MY_QNAME,factory));
            return emptyEnvelope;
        } catch(org.apache.axis2.databinding.ADBException e){
            throw org.apache.axis2.AxisFault.makeFault(e);
        }


    }
                                
                             
                             /* methods to provide back word compatibility */



    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, it.kdm.docer.clients.WSDemoneStub.DeleteJob_byIdJob param, boolean optimizeContent, javax.xml.namespace.QName methodQName)
            throws org.apache.axis2.AxisFault{


        try{

            org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(it.kdm.docer.clients.WSDemoneStub.DeleteJob_byIdJob.MY_QNAME,factory));
            return emptyEnvelope;
        } catch(org.apache.axis2.databinding.ADBException e){
            throw org.apache.axis2.AxisFault.makeFault(e);
        }


    }
                                
                             
                             /* methods to provide back word compatibility */



    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, it.kdm.docer.clients.WSDemoneStub.UpdateJob param, boolean optimizeContent, javax.xml.namespace.QName methodQName)
            throws org.apache.axis2.AxisFault{


        try{

            org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(it.kdm.docer.clients.WSDemoneStub.UpdateJob.MY_QNAME,factory));
            return emptyEnvelope;
        } catch(org.apache.axis2.databinding.ADBException e){
            throw org.apache.axis2.AxisFault.makeFault(e);
        }


    }
                                
                             
                             /* methods to provide back word compatibility */



    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, it.kdm.docer.clients.WSDemoneStub.ReadLogsByIdDoc param, boolean optimizeContent, javax.xml.namespace.QName methodQName)
            throws org.apache.axis2.AxisFault{


        try{

            org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(it.kdm.docer.clients.WSDemoneStub.ReadLogsByIdDoc.MY_QNAME,factory));
            return emptyEnvelope;
        } catch(org.apache.axis2.databinding.ADBException e){
            throw org.apache.axis2.AxisFault.makeFault(e);
        }


    }
                                
                             
                             /* methods to provide back word compatibility */



    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, it.kdm.docer.clients.WSDemoneStub.AddJob param, boolean optimizeContent, javax.xml.namespace.QName methodQName)
            throws org.apache.axis2.AxisFault{


        try{

            org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(it.kdm.docer.clients.WSDemoneStub.AddJob.MY_QNAME,factory));
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


    private  java.lang.Object fromOM(
            org.apache.axiom.om.OMElement param,
            java.lang.Class type,
            java.util.Map extraNamespaces) throws org.apache.axis2.AxisFault{

        try {

            if (it.kdm.docer.clients.WSDemoneStub.SearchJob.class.equals(type)){

                return it.kdm.docer.clients.WSDemoneStub.SearchJob.Factory.parse(param.getXMLStreamReaderWithoutCaching());


            }

            if (it.kdm.docer.clients.WSDemoneStub.SearchJobResponse.class.equals(type)){

                return it.kdm.docer.clients.WSDemoneStub.SearchJobResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());


            }

            if (it.kdm.docer.clients.WSDemoneStub.WSDemoneException.class.equals(type)){

                return it.kdm.docer.clients.WSDemoneStub.WSDemoneException.Factory.parse(param.getXMLStreamReaderWithoutCaching());


            }

            if (it.kdm.docer.clients.WSDemoneStub.GetJobGson.class.equals(type)){

                return it.kdm.docer.clients.WSDemoneStub.GetJobGson.Factory.parse(param.getXMLStreamReaderWithoutCaching());


            }

            if (it.kdm.docer.clients.WSDemoneStub.GetJobGsonResponse.class.equals(type)){

                return it.kdm.docer.clients.WSDemoneStub.GetJobGsonResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());


            }

            if (it.kdm.docer.clients.WSDemoneStub.WSDemoneException.class.equals(type)){

                return it.kdm.docer.clients.WSDemoneStub.WSDemoneException.Factory.parse(param.getXMLStreamReaderWithoutCaching());


            }

            if (it.kdm.docer.clients.WSDemoneStub.SearchJobEstesa.class.equals(type)){

                return it.kdm.docer.clients.WSDemoneStub.SearchJobEstesa.Factory.parse(param.getXMLStreamReaderWithoutCaching());


            }

            if (it.kdm.docer.clients.WSDemoneStub.SearchJobEstesaResponse.class.equals(type)){

                return it.kdm.docer.clients.WSDemoneStub.SearchJobEstesaResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());


            }

            if (it.kdm.docer.clients.WSDemoneStub.ReadLogsByIdJob.class.equals(type)){

                return it.kdm.docer.clients.WSDemoneStub.ReadLogsByIdJob.Factory.parse(param.getXMLStreamReaderWithoutCaching());


            }

            if (it.kdm.docer.clients.WSDemoneStub.ReadLogsByIdJobResponse.class.equals(type)){

                return it.kdm.docer.clients.WSDemoneStub.ReadLogsByIdJobResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());


            }

            if (it.kdm.docer.clients.WSDemoneStub.WSDemoneException.class.equals(type)){

                return it.kdm.docer.clients.WSDemoneStub.WSDemoneException.Factory.parse(param.getXMLStreamReaderWithoutCaching());


            }

            if (it.kdm.docer.clients.WSDemoneStub.ReadLogGson_byIdJob.class.equals(type)){

                return it.kdm.docer.clients.WSDemoneStub.ReadLogGson_byIdJob.Factory.parse(param.getXMLStreamReaderWithoutCaching());


            }

            if (it.kdm.docer.clients.WSDemoneStub.ReadLogGson_byIdJobResponse.class.equals(type)){

                return it.kdm.docer.clients.WSDemoneStub.ReadLogGson_byIdJobResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());


            }

            if (it.kdm.docer.clients.WSDemoneStub.WSDemoneException.class.equals(type)){

                return it.kdm.docer.clients.WSDemoneStub.WSDemoneException.Factory.parse(param.getXMLStreamReaderWithoutCaching());


            }

            if (it.kdm.docer.clients.WSDemoneStub.DeleteJob_ByIdDoc.class.equals(type)){

                return it.kdm.docer.clients.WSDemoneStub.DeleteJob_ByIdDoc.Factory.parse(param.getXMLStreamReaderWithoutCaching());


            }

            if (it.kdm.docer.clients.WSDemoneStub.DeleteJob_ByIdDocResponse.class.equals(type)){

                return it.kdm.docer.clients.WSDemoneStub.DeleteJob_ByIdDocResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());


            }

            if (it.kdm.docer.clients.WSDemoneStub.WSDemoneException.class.equals(type)){

                return it.kdm.docer.clients.WSDemoneStub.WSDemoneException.Factory.parse(param.getXMLStreamReaderWithoutCaching());


            }

            if (it.kdm.docer.clients.WSDemoneStub.GetJob.class.equals(type)){

                return it.kdm.docer.clients.WSDemoneStub.GetJob.Factory.parse(param.getXMLStreamReaderWithoutCaching());


            }

            if (it.kdm.docer.clients.WSDemoneStub.GetJobResponse.class.equals(type)){

                return it.kdm.docer.clients.WSDemoneStub.GetJobResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());


            }

            if (it.kdm.docer.clients.WSDemoneStub.WSDemoneException.class.equals(type)){

                return it.kdm.docer.clients.WSDemoneStub.WSDemoneException.Factory.parse(param.getXMLStreamReaderWithoutCaching());


            }

            if (it.kdm.docer.clients.WSDemoneStub.SearchLogEstesa.class.equals(type)){

                return it.kdm.docer.clients.WSDemoneStub.SearchLogEstesa.Factory.parse(param.getXMLStreamReaderWithoutCaching());


            }

            if (it.kdm.docer.clients.WSDemoneStub.SearchLogEstesaResponse.class.equals(type)){

                return it.kdm.docer.clients.WSDemoneStub.SearchLogEstesaResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());


            }

            if (it.kdm.docer.clients.WSDemoneStub.WSDemoneException.class.equals(type)){

                return it.kdm.docer.clients.WSDemoneStub.WSDemoneException.Factory.parse(param.getXMLStreamReaderWithoutCaching());


            }

            if (it.kdm.docer.clients.WSDemoneStub.GetJobByDocId.class.equals(type)){

                return it.kdm.docer.clients.WSDemoneStub.GetJobByDocId.Factory.parse(param.getXMLStreamReaderWithoutCaching());


            }

            if (it.kdm.docer.clients.WSDemoneStub.GetJobByDocIdResponse.class.equals(type)){

                return it.kdm.docer.clients.WSDemoneStub.GetJobByDocIdResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());


            }

            if (it.kdm.docer.clients.WSDemoneStub.WSDemoneException.class.equals(type)){

                return it.kdm.docer.clients.WSDemoneStub.WSDemoneException.Factory.parse(param.getXMLStreamReaderWithoutCaching());


            }

            if (it.kdm.docer.clients.WSDemoneStub.DeleteJob_byIdJob.class.equals(type)){

                return it.kdm.docer.clients.WSDemoneStub.DeleteJob_byIdJob.Factory.parse(param.getXMLStreamReaderWithoutCaching());


            }

            if (it.kdm.docer.clients.WSDemoneStub.DeleteJob_byIdJobResponse.class.equals(type)){

                return it.kdm.docer.clients.WSDemoneStub.DeleteJob_byIdJobResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());


            }

            if (it.kdm.docer.clients.WSDemoneStub.WSDemoneException.class.equals(type)){

                return it.kdm.docer.clients.WSDemoneStub.WSDemoneException.Factory.parse(param.getXMLStreamReaderWithoutCaching());


            }

            if (it.kdm.docer.clients.WSDemoneStub.UpdateJob.class.equals(type)){

                return it.kdm.docer.clients.WSDemoneStub.UpdateJob.Factory.parse(param.getXMLStreamReaderWithoutCaching());


            }

            if (it.kdm.docer.clients.WSDemoneStub.UpdateJobResponse.class.equals(type)){

                return it.kdm.docer.clients.WSDemoneStub.UpdateJobResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());


            }

            if (it.kdm.docer.clients.WSDemoneStub.WSDemoneException.class.equals(type)){

                return it.kdm.docer.clients.WSDemoneStub.WSDemoneException.Factory.parse(param.getXMLStreamReaderWithoutCaching());


            }

            if (it.kdm.docer.clients.WSDemoneStub.ReadLogsByIdDoc.class.equals(type)){

                return it.kdm.docer.clients.WSDemoneStub.ReadLogsByIdDoc.Factory.parse(param.getXMLStreamReaderWithoutCaching());


            }

            if (it.kdm.docer.clients.WSDemoneStub.ReadLogsByIdDocResponse.class.equals(type)){

                return it.kdm.docer.clients.WSDemoneStub.ReadLogsByIdDocResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());


            }

            if (it.kdm.docer.clients.WSDemoneStub.WSDemoneException.class.equals(type)){

                return it.kdm.docer.clients.WSDemoneStub.WSDemoneException.Factory.parse(param.getXMLStreamReaderWithoutCaching());


            }

            if (it.kdm.docer.clients.WSDemoneStub.AddJob.class.equals(type)){

                return it.kdm.docer.clients.WSDemoneStub.AddJob.Factory.parse(param.getXMLStreamReaderWithoutCaching());


            }

            if (it.kdm.docer.clients.WSDemoneStub.AddJobResponse.class.equals(type)){

                return it.kdm.docer.clients.WSDemoneStub.AddJobResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());


            }

            if (it.kdm.docer.clients.WSDemoneStub.WSDemoneException.class.equals(type)){

                return it.kdm.docer.clients.WSDemoneStub.WSDemoneException.Factory.parse(param.getXMLStreamReaderWithoutCaching());


            }

        } catch (java.lang.Exception e) {
            throw org.apache.axis2.AxisFault.makeFault(e);
        }
        return null;
    }




}
   