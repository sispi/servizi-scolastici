/**
 * RerFonteSOAPStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.emilia_romagna.regione.www.rer_fonte_raccoglitore;

public class RerFonteSOAPStub extends org.apache.axis.client.Stub implements it.emilia_romagna.regione.www.rer_fonte_raccoglitore.RerFonteRaccoglitore {
    private java.util.Vector cachedSerClasses = new java.util.Vector();
    private java.util.Vector cachedSerQNames = new java.util.Vector();
    private java.util.Vector cachedSerFactories = new java.util.Vector();
    private java.util.Vector cachedDeserFactories = new java.util.Vector();

    static org.apache.axis.description.OperationDesc [] _operations;

    static {
        _operations = new org.apache.axis.description.OperationDesc[5];
        _initOperationDesc1();
    }

    private static void _initOperationDesc1(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getMetadatiEntitaInformativa");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "getMetadatiEntitaInformativaRequest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", ">getMetadatiEntitaInformativaRequest"), it.emilia_romagna.regione.www.rer_fonte_raccoglitore.GetMetadatiEntitaInformativaRequest.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", ">getMetadatiEntitaInformativaResponse"));
        oper.setReturnClass(it.emilia_romagna.regione.www.rer_fonte_raccoglitore.GetMetadatiEntitaInformativaResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "getMetadatiEntitaInformativaResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "objectNotFoundError"),
                      "it.emilia_romagna.regione.www.rer_fonte_raccoglitore.ObjectNotFoundError",
                      new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "GenericFaultType"), 
                      false
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "authorizationDeniedError"),
                      "it.emilia_romagna.regione.www.rer_fonte_raccoglitore.AuthorizationDeniedError",
                      new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "GenericFaultType"), 
                      false
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "rerFonteError"),
                      "it.emilia_romagna.regione.www.rer_fonte_raccoglitore.RerFonteError",
                      new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "GenericFaultType"), 
                      false
                     ));
        _operations[0] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getEntitaInformativa");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "getEntitaInformativaRequest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", ">getEntitaInformativaRequest"), it.emilia_romagna.regione.www.rer_fonte_raccoglitore.GetEntitaInformativaRequest.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", ">getEntitaInformativaResponse"));
        oper.setReturnClass(it.emilia_romagna.regione.www.rer_fonte_raccoglitore.GetEntitaInformativaResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "getEntitaInformativaResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "objectNotFoundError"),
                      "it.emilia_romagna.regione.www.rer_fonte_raccoglitore.ObjectNotFoundError",
                      new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "GenericFaultType"), 
                      false
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "authorizationDeniedError"),
                      "it.emilia_romagna.regione.www.rer_fonte_raccoglitore.AuthorizationDeniedError",
                      new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "GenericFaultType"), 
                      false
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "rerFonteError"),
                      "it.emilia_romagna.regione.www.rer_fonte_raccoglitore.RerFonteError",
                      new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "GenericFaultType"), 
                      false
                     ));
        _operations[1] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("searchEntitaInformativa");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "searchEntitaInformativaRequest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", ">searchEntitaInformativaRequest"), it.emilia_romagna.regione.www.rer_fonte_raccoglitore.SearchEntitaInformativaRequest.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", ">searchEntitaInformativaResponse"));
        oper.setReturnClass(it.emilia_romagna.regione.www.rer_fonte_raccoglitore.SearchEntitaInformativaResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "searchEntitaInformativaResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "objectNotFoundError"),
                      "it.emilia_romagna.regione.www.rer_fonte_raccoglitore.ObjectNotFoundError",
                      new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "GenericFaultType"), 
                      false
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "authorizationDeniedError"),
                      "it.emilia_romagna.regione.www.rer_fonte_raccoglitore.AuthorizationDeniedError",
                      new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "GenericFaultType"), 
                      false
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "rerFonteError"),
                      "it.emilia_romagna.regione.www.rer_fonte_raccoglitore.RerFonteError",
                      new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "GenericFaultType"), 
                      false
                     ));
        _operations[2] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("searchEntitaInformativaAvanzata");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "searchEntitaInformativaAvanzataRequest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", ">searchEntitaInformativaAvanzataRequest"), it.emilia_romagna.regione.www.rer_fonte_raccoglitore.SearchEntitaInformativaAvanzataRequest.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", ">searchEntitaInformativaAvanzataResponse"));
        oper.setReturnClass(it.emilia_romagna.regione.www.rer_fonte_raccoglitore.SearchEntitaInformativaAvanzataResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "searchEntitaInformativaAvanzataResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "objectNotFoundError"),
                      "it.emilia_romagna.regione.www.rer_fonte_raccoglitore.ObjectNotFoundError",
                      new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "GenericFaultType"), 
                      false
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "authorizationDeniedError"),
                      "it.emilia_romagna.regione.www.rer_fonte_raccoglitore.AuthorizationDeniedError",
                      new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "GenericFaultType"), 
                      false
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "rerFonteError"),
                      "it.emilia_romagna.regione.www.rer_fonte_raccoglitore.RerFonteError",
                      new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "GenericFaultType"), 
                      false
                     ));
        _operations[3] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getAttributiRicerca");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "getAttributiRicercaRequest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", ">getAttributiRicercaRequest"), it.emilia_romagna.regione.www.rer_fonte_raccoglitore.GetAttributiRicercaRequest.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", ">getAttributiRicercaResponse"));
        oper.setReturnClass(it.emilia_romagna.regione.www.rer_fonte_raccoglitore.GetAttributiRicercaResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "getAttributiRicercaResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "objectNotFoundError"),
                      "it.emilia_romagna.regione.www.rer_fonte_raccoglitore.ObjectNotFoundError",
                      new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "GenericFaultType"), 
                      false
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "authorizationDeniedError"),
                      "it.emilia_romagna.regione.www.rer_fonte_raccoglitore.AuthorizationDeniedError",
                      new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "GenericFaultType"), 
                      false
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "rerFonteError"),
                      "it.emilia_romagna.regione.www.rer_fonte_raccoglitore.RerFonteError",
                      new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "GenericFaultType"), 
                      false
                     ));
        _operations[4] = oper;

    }

    public RerFonteSOAPStub() throws org.apache.axis.AxisFault {
         this(null);
    }

    public RerFonteSOAPStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
         this(service);
         super.cachedEndpoint = endpointURL;
    }

    public RerFonteSOAPStub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
        if (service == null) {
            super.service = new org.apache.axis.client.Service();
        } else {
            super.service = service;
        }
        ((org.apache.axis.client.Service)super.service).setTypeMappingVersion("1.2");
            java.lang.Class cls;
            javax.xml.namespace.QName qName;
            javax.xml.namespace.QName qName2;
            java.lang.Class beansf = org.apache.axis.encoding.ser.BeanSerializerFactory.class;
            java.lang.Class beandf = org.apache.axis.encoding.ser.BeanDeserializerFactory.class;
            java.lang.Class enumsf = org.apache.axis.encoding.ser.EnumSerializerFactory.class;
            java.lang.Class enumdf = org.apache.axis.encoding.ser.EnumDeserializerFactory.class;
            java.lang.Class arraysf = org.apache.axis.encoding.ser.ArraySerializerFactory.class;
            java.lang.Class arraydf = org.apache.axis.encoding.ser.ArrayDeserializerFactory.class;
            java.lang.Class simplesf = org.apache.axis.encoding.ser.SimpleSerializerFactory.class;
            java.lang.Class simpledf = org.apache.axis.encoding.ser.SimpleDeserializerFactory.class;
            java.lang.Class simplelistsf = org.apache.axis.encoding.ser.SimpleListSerializerFactory.class;
            java.lang.Class simplelistdf = org.apache.axis.encoding.ser.SimpleListDeserializerFactory.class;
            qName = new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", ">Document>relatedDocument");
            cachedSerQNames.add(qName);
            cls = it.emilia_romagna.regione.www.rer_fonte_raccoglitore.DocumentRelatedDocument.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", ">FacetField>facetFieldValue");
            cachedSerQNames.add(qName);
            cls = it.emilia_romagna.regione.www.rer_fonte_raccoglitore.FacetFieldFacetFieldValue.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", ">getAttributiRicercaRequest");
            cachedSerQNames.add(qName);
            cls = it.emilia_romagna.regione.www.rer_fonte_raccoglitore.GetAttributiRicercaRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", ">getAttributiRicercaResponse");
            cachedSerQNames.add(qName);
            cls = it.emilia_romagna.regione.www.rer_fonte_raccoglitore.GetAttributiRicercaResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", ">getEntitaInformativaRequest");
            cachedSerQNames.add(qName);
            cls = it.emilia_romagna.regione.www.rer_fonte_raccoglitore.GetEntitaInformativaRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", ">getEntitaInformativaResponse");
            cachedSerQNames.add(qName);
            cls = it.emilia_romagna.regione.www.rer_fonte_raccoglitore.GetEntitaInformativaResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", ">getMetadatiEntitaInformativaRequest");
            cachedSerQNames.add(qName);
            cls = it.emilia_romagna.regione.www.rer_fonte_raccoglitore.GetMetadatiEntitaInformativaRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", ">getMetadatiEntitaInformativaResponse");
            cachedSerQNames.add(qName);
            cls = it.emilia_romagna.regione.www.rer_fonte_raccoglitore.GetMetadatiEntitaInformativaResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", ">searchEntitaInformativaAvanzataRequest");
            cachedSerQNames.add(qName);
            cls = it.emilia_romagna.regione.www.rer_fonte_raccoglitore.SearchEntitaInformativaAvanzataRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", ">searchEntitaInformativaAvanzataResponse");
            cachedSerQNames.add(qName);
            cls = it.emilia_romagna.regione.www.rer_fonte_raccoglitore.SearchEntitaInformativaAvanzataResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", ">searchEntitaInformativaRequest");
            cachedSerQNames.add(qName);
            cls = it.emilia_romagna.regione.www.rer_fonte_raccoglitore.SearchEntitaInformativaRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", ">searchEntitaInformativaResponse");
            cachedSerQNames.add(qName);
            cls = it.emilia_romagna.regione.www.rer_fonte_raccoglitore.SearchEntitaInformativaResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "AttributoBooleanoType");
            cachedSerQNames.add(qName);
            cls = it.emilia_romagna.regione.www.rer_fonte_raccoglitore.AttributoBooleanoType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "AttributoDataRangeType");
            cachedSerQNames.add(qName);
            cls = it.emilia_romagna.regione.www.rer_fonte_raccoglitore.AttributoDataRangeType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "AttributoDataType");
            cachedSerQNames.add(qName);
            cls = it.emilia_romagna.regione.www.rer_fonte_raccoglitore.AttributoDataType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "AttributoNumeroType");
            cachedSerQNames.add(qName);
            cls = it.emilia_romagna.regione.www.rer_fonte_raccoglitore.AttributoNumeroType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "AttributoTestoType");
            cachedSerQNames.add(qName);
            cls = it.emilia_romagna.regione.www.rer_fonte_raccoglitore.AttributoTestoType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "AttributoType");
            cachedSerQNames.add(qName);
            cls = it.emilia_romagna.regione.www.rer_fonte_raccoglitore.AttributoType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "DateRangeType");
            cachedSerQNames.add(qName);
            cls = it.emilia_romagna.regione.www.rer_fonte_raccoglitore.DateRangeType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "DelegheType");
            cachedSerQNames.add(qName);
            cls = it.emilia_romagna.regione.www.rer_fonte_raccoglitore.GetEntitaInformativaRequestDelegheDelega[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "GetEntitaInformativaRequestDelegheDelega");
            qName2 = new javax.xml.namespace.QName("", "delega");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "Document");
            cachedSerQNames.add(qName);
            cls = it.emilia_romagna.regione.www.rer_fonte_raccoglitore.Document.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "FacetField");
            cachedSerQNames.add(qName);
            cls = it.emilia_romagna.regione.www.rer_fonte_raccoglitore.FacetField.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "FacetFieldGroup");
            cachedSerQNames.add(qName);
            cls = it.emilia_romagna.regione.www.rer_fonte_raccoglitore.FacetField[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "FacetField");
            qName2 = new javax.xml.namespace.QName("", "facetField");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "FilterQuery");
            cachedSerQNames.add(qName);
            cls = it.emilia_romagna.regione.www.rer_fonte_raccoglitore.FilterQuery.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "GenericFaultType");
            cachedSerQNames.add(qName);
            cls = java.lang.String[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string");
            qName2 = new javax.xml.namespace.QName("", "messaggioErrore");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "GetEntitaInformativaRequestDelegheDelega");
            cachedSerQNames.add(qName);
            cls = it.emilia_romagna.regione.www.rer_fonte_raccoglitore.GetEntitaInformativaRequestDelegheDelega.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "TipoAttributoType");
            cachedSerQNames.add(qName);
            cls = it.emilia_romagna.regione.www.rer_fonte_raccoglitore.TipoAttributoType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

    }

    protected org.apache.axis.client.Call createCall() throws java.rmi.RemoteException {
        try {
            org.apache.axis.client.Call _call = super._createCall();
            if (super.maintainSessionSet) {
                _call.setMaintainSession(super.maintainSession);
            }
            if (super.cachedUsername != null) {
                _call.setUsername(super.cachedUsername);
            }
            if (super.cachedPassword != null) {
                _call.setPassword(super.cachedPassword);
            }
            if (super.cachedEndpoint != null) {
                _call.setTargetEndpointAddress(super.cachedEndpoint);
            }
            if (super.cachedTimeout != null) {
                _call.setTimeout(super.cachedTimeout);
            }
            if (super.cachedPortName != null) {
                _call.setPortName(super.cachedPortName);
            }
            java.util.Enumeration keys = super.cachedProperties.keys();
            while (keys.hasMoreElements()) {
                java.lang.String key = (java.lang.String) keys.nextElement();
                _call.setProperty(key, super.cachedProperties.get(key));
            }
            // All the type mapping information is registered
            // when the first call is made.
            // The type mapping information is actually registered in
            // the TypeMappingRegistry of the service, which
            // is the reason why registration is only needed for the first call.
            synchronized (this) {
                if (firstCall()) {
                    // must set encoding style before registering serializers
                    _call.setEncodingStyle(null);
                    for (int i = 0; i < cachedSerFactories.size(); ++i) {
                        java.lang.Class cls = (java.lang.Class) cachedSerClasses.get(i);
                        javax.xml.namespace.QName qName =
                                (javax.xml.namespace.QName) cachedSerQNames.get(i);
                        java.lang.Object x = cachedSerFactories.get(i);
                        if (x instanceof Class) {
                            java.lang.Class sf = (java.lang.Class)
                                 cachedSerFactories.get(i);
                            java.lang.Class df = (java.lang.Class)
                                 cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        }
                        else if (x instanceof javax.xml.rpc.encoding.SerializerFactory) {
                            org.apache.axis.encoding.SerializerFactory sf = (org.apache.axis.encoding.SerializerFactory)
                                 cachedSerFactories.get(i);
                            org.apache.axis.encoding.DeserializerFactory df = (org.apache.axis.encoding.DeserializerFactory)
                                 cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        }
                    }
                }
            }
            return _call;
        }
        catch (java.lang.Throwable _t) {
            throw new org.apache.axis.AxisFault("Failure trying to get the Call object", _t);
        }
    }

    public it.emilia_romagna.regione.www.rer_fonte_raccoglitore.GetMetadatiEntitaInformativaResponse getMetadatiEntitaInformativa(it.emilia_romagna.regione.www.rer_fonte_raccoglitore.GetMetadatiEntitaInformativaRequest getMetadatiEntitaInformativaRequest) throws java.rmi.RemoteException, it.emilia_romagna.regione.www.rer_fonte_raccoglitore.ObjectNotFoundError, it.emilia_romagna.regione.www.rer_fonte_raccoglitore.AuthorizationDeniedError, it.emilia_romagna.regione.www.rer_fonte_raccoglitore.RerFonteError {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[0]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/getMetadatiEntitaInformativa");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "getMetadatiEntitaInformativa"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {getMetadatiEntitaInformativaRequest});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (it.emilia_romagna.regione.www.rer_fonte_raccoglitore.GetMetadatiEntitaInformativaResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (it.emilia_romagna.regione.www.rer_fonte_raccoglitore.GetMetadatiEntitaInformativaResponse) org.apache.axis.utils.JavaUtils.convert(_resp, it.emilia_romagna.regione.www.rer_fonte_raccoglitore.GetMetadatiEntitaInformativaResponse.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof it.emilia_romagna.regione.www.rer_fonte_raccoglitore.ObjectNotFoundError) {
              throw (it.emilia_romagna.regione.www.rer_fonte_raccoglitore.ObjectNotFoundError) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof it.emilia_romagna.regione.www.rer_fonte_raccoglitore.AuthorizationDeniedError) {
              throw (it.emilia_romagna.regione.www.rer_fonte_raccoglitore.AuthorizationDeniedError) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof it.emilia_romagna.regione.www.rer_fonte_raccoglitore.RerFonteError) {
              throw (it.emilia_romagna.regione.www.rer_fonte_raccoglitore.RerFonteError) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public it.emilia_romagna.regione.www.rer_fonte_raccoglitore.GetEntitaInformativaResponse getEntitaInformativa(it.emilia_romagna.regione.www.rer_fonte_raccoglitore.GetEntitaInformativaRequest getEntitaInformativaRequest) throws java.rmi.RemoteException, it.emilia_romagna.regione.www.rer_fonte_raccoglitore.ObjectNotFoundError, it.emilia_romagna.regione.www.rer_fonte_raccoglitore.AuthorizationDeniedError, it.emilia_romagna.regione.www.rer_fonte_raccoglitore.RerFonteError {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[1]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/getEntitaInformativa");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "getEntitaInformativa"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {getEntitaInformativaRequest});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (it.emilia_romagna.regione.www.rer_fonte_raccoglitore.GetEntitaInformativaResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (it.emilia_romagna.regione.www.rer_fonte_raccoglitore.GetEntitaInformativaResponse) org.apache.axis.utils.JavaUtils.convert(_resp, it.emilia_romagna.regione.www.rer_fonte_raccoglitore.GetEntitaInformativaResponse.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof it.emilia_romagna.regione.www.rer_fonte_raccoglitore.ObjectNotFoundError) {
              throw (it.emilia_romagna.regione.www.rer_fonte_raccoglitore.ObjectNotFoundError) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof it.emilia_romagna.regione.www.rer_fonte_raccoglitore.AuthorizationDeniedError) {
              throw (it.emilia_romagna.regione.www.rer_fonte_raccoglitore.AuthorizationDeniedError) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof it.emilia_romagna.regione.www.rer_fonte_raccoglitore.RerFonteError) {
              throw (it.emilia_romagna.regione.www.rer_fonte_raccoglitore.RerFonteError) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public it.emilia_romagna.regione.www.rer_fonte_raccoglitore.SearchEntitaInformativaResponse searchEntitaInformativa(it.emilia_romagna.regione.www.rer_fonte_raccoglitore.SearchEntitaInformativaRequest searchEntitaInformativaRequest) throws java.rmi.RemoteException, it.emilia_romagna.regione.www.rer_fonte_raccoglitore.ObjectNotFoundError, it.emilia_romagna.regione.www.rer_fonte_raccoglitore.AuthorizationDeniedError, it.emilia_romagna.regione.www.rer_fonte_raccoglitore.RerFonteError {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[2]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/searchEntitaInformativa");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "searchEntitaInformativa"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {searchEntitaInformativaRequest});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (it.emilia_romagna.regione.www.rer_fonte_raccoglitore.SearchEntitaInformativaResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (it.emilia_romagna.regione.www.rer_fonte_raccoglitore.SearchEntitaInformativaResponse) org.apache.axis.utils.JavaUtils.convert(_resp, it.emilia_romagna.regione.www.rer_fonte_raccoglitore.SearchEntitaInformativaResponse.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof it.emilia_romagna.regione.www.rer_fonte_raccoglitore.ObjectNotFoundError) {
              throw (it.emilia_romagna.regione.www.rer_fonte_raccoglitore.ObjectNotFoundError) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof it.emilia_romagna.regione.www.rer_fonte_raccoglitore.AuthorizationDeniedError) {
              throw (it.emilia_romagna.regione.www.rer_fonte_raccoglitore.AuthorizationDeniedError) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof it.emilia_romagna.regione.www.rer_fonte_raccoglitore.RerFonteError) {
              throw (it.emilia_romagna.regione.www.rer_fonte_raccoglitore.RerFonteError) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public it.emilia_romagna.regione.www.rer_fonte_raccoglitore.SearchEntitaInformativaAvanzataResponse searchEntitaInformativaAvanzata(it.emilia_romagna.regione.www.rer_fonte_raccoglitore.SearchEntitaInformativaAvanzataRequest searchEntitaInformativaAvanzataRequest) throws java.rmi.RemoteException, it.emilia_romagna.regione.www.rer_fonte_raccoglitore.ObjectNotFoundError, it.emilia_romagna.regione.www.rer_fonte_raccoglitore.AuthorizationDeniedError, it.emilia_romagna.regione.www.rer_fonte_raccoglitore.RerFonteError {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[3]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/searchEntitaInformativaAvanzata");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "searchEntitaInformativaAvanzata"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {searchEntitaInformativaAvanzataRequest});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (it.emilia_romagna.regione.www.rer_fonte_raccoglitore.SearchEntitaInformativaAvanzataResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (it.emilia_romagna.regione.www.rer_fonte_raccoglitore.SearchEntitaInformativaAvanzataResponse) org.apache.axis.utils.JavaUtils.convert(_resp, it.emilia_romagna.regione.www.rer_fonte_raccoglitore.SearchEntitaInformativaAvanzataResponse.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof it.emilia_romagna.regione.www.rer_fonte_raccoglitore.ObjectNotFoundError) {
              throw (it.emilia_romagna.regione.www.rer_fonte_raccoglitore.ObjectNotFoundError) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof it.emilia_romagna.regione.www.rer_fonte_raccoglitore.AuthorizationDeniedError) {
              throw (it.emilia_romagna.regione.www.rer_fonte_raccoglitore.AuthorizationDeniedError) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof it.emilia_romagna.regione.www.rer_fonte_raccoglitore.RerFonteError) {
              throw (it.emilia_romagna.regione.www.rer_fonte_raccoglitore.RerFonteError) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public it.emilia_romagna.regione.www.rer_fonte_raccoglitore.GetAttributiRicercaResponse getAttributiRicerca(it.emilia_romagna.regione.www.rer_fonte_raccoglitore.GetAttributiRicercaRequest getAttributiRicercaRequest) throws java.rmi.RemoteException, it.emilia_romagna.regione.www.rer_fonte_raccoglitore.ObjectNotFoundError, it.emilia_romagna.regione.www.rer_fonte_raccoglitore.AuthorizationDeniedError, it.emilia_romagna.regione.www.rer_fonte_raccoglitore.RerFonteError {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[4]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/getAttributiRicerca");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "getAttributiRicerca"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {getAttributiRicercaRequest});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (it.emilia_romagna.regione.www.rer_fonte_raccoglitore.GetAttributiRicercaResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (it.emilia_romagna.regione.www.rer_fonte_raccoglitore.GetAttributiRicercaResponse) org.apache.axis.utils.JavaUtils.convert(_resp, it.emilia_romagna.regione.www.rer_fonte_raccoglitore.GetAttributiRicercaResponse.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof it.emilia_romagna.regione.www.rer_fonte_raccoglitore.ObjectNotFoundError) {
              throw (it.emilia_romagna.regione.www.rer_fonte_raccoglitore.ObjectNotFoundError) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof it.emilia_romagna.regione.www.rer_fonte_raccoglitore.AuthorizationDeniedError) {
              throw (it.emilia_romagna.regione.www.rer_fonte_raccoglitore.AuthorizationDeniedError) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof it.emilia_romagna.regione.www.rer_fonte_raccoglitore.RerFonteError) {
              throw (it.emilia_romagna.regione.www.rer_fonte_raccoglitore.RerFonteError) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

}
