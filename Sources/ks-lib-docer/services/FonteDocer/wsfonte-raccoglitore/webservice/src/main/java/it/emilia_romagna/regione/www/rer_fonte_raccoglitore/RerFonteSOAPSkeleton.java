/**
 * RerFonteSOAPSkeleton.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.emilia_romagna.regione.www.rer_fonte_raccoglitore;

public class RerFonteSOAPSkeleton implements it.emilia_romagna.regione.www.rer_fonte_raccoglitore.RerFonteRaccoglitore, org.apache.axis.wsdl.Skeleton {
    private it.emilia_romagna.regione.www.rer_fonte_raccoglitore.RerFonteRaccoglitore impl;
    private static java.util.Map _myOperations = new java.util.Hashtable();
    private static java.util.Collection _myOperationsList = new java.util.ArrayList();

    /**
    * Returns List of OperationDesc objects with this name
    */
    public static java.util.List getOperationDescByName(java.lang.String methodName) {
        return (java.util.List)_myOperations.get(methodName);
    }

    /**
    * Returns Collection of OperationDescs
    */
    public static java.util.Collection getOperationDescs() {
        return _myOperationsList;
    }

    static {
        org.apache.axis.description.OperationDesc _oper;
        org.apache.axis.description.FaultDesc _fault;
        org.apache.axis.description.ParameterDesc [] _params;
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "getMetadatiEntitaInformativaRequest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", ">getMetadatiEntitaInformativaRequest"), it.emilia_romagna.regione.www.rer_fonte_raccoglitore.GetMetadatiEntitaInformativaRequest.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getMetadatiEntitaInformativa", _params, new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "getMetadatiEntitaInformativaResponse"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", ">getMetadatiEntitaInformativaResponse"));
        _oper.setElementQName(new javax.xml.namespace.QName("", "getMetadatiEntitaInformativa"));
        _oper.setSoapAction("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/getMetadatiEntitaInformativa");
        _myOperationsList.add(_oper);
        if (_myOperations.get("getMetadatiEntitaInformativa") == null) {
            _myOperations.put("getMetadatiEntitaInformativa", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getMetadatiEntitaInformativa")).add(_oper);
        _fault = new org.apache.axis.description.FaultDesc();
        _fault.setName("objectNotFoundError");
        _fault.setQName(new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "objectNotFoundError"));
        _fault.setClassName("it.emilia_romagna.regione.www.rer_fonte_raccoglitore.ObjectNotFoundError");
        _fault.setXmlType(new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "GenericFaultType"));
        _oper.addFault(_fault);
        _fault = new org.apache.axis.description.FaultDesc();
        _fault.setName("authorizationDeniedError");
        _fault.setQName(new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "authorizationDeniedError"));
        _fault.setClassName("it.emilia_romagna.regione.www.rer_fonte_raccoglitore.AuthorizationDeniedError");
        _fault.setXmlType(new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "GenericFaultType"));
        _oper.addFault(_fault);
        _fault = new org.apache.axis.description.FaultDesc();
        _fault.setName("rerFonteError");
        _fault.setQName(new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "rerFonteError"));
        _fault.setClassName("it.emilia_romagna.regione.www.rer_fonte_raccoglitore.RerFonteError");
        _fault.setXmlType(new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "GenericFaultType"));
        _oper.addFault(_fault);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "getEntitaInformativaRequest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", ">getEntitaInformativaRequest"), it.emilia_romagna.regione.www.rer_fonte_raccoglitore.GetEntitaInformativaRequest.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getEntitaInformativa", _params, new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "getEntitaInformativaResponse"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", ">getEntitaInformativaResponse"));
        _oper.setElementQName(new javax.xml.namespace.QName("", "getEntitaInformativa"));
        _oper.setSoapAction("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/getEntitaInformativa");
        _myOperationsList.add(_oper);
        if (_myOperations.get("getEntitaInformativa") == null) {
            _myOperations.put("getEntitaInformativa", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getEntitaInformativa")).add(_oper);
        _fault = new org.apache.axis.description.FaultDesc();
        _fault.setName("objectNotFoundError");
        _fault.setQName(new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "objectNotFoundError"));
        _fault.setClassName("it.emilia_romagna.regione.www.rer_fonte_raccoglitore.ObjectNotFoundError");
        _fault.setXmlType(new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "GenericFaultType"));
        _oper.addFault(_fault);
        _fault = new org.apache.axis.description.FaultDesc();
        _fault.setName("authorizationDeniedError");
        _fault.setQName(new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "authorizationDeniedError"));
        _fault.setClassName("it.emilia_romagna.regione.www.rer_fonte_raccoglitore.AuthorizationDeniedError");
        _fault.setXmlType(new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "GenericFaultType"));
        _oper.addFault(_fault);
        _fault = new org.apache.axis.description.FaultDesc();
        _fault.setName("rerFonteError");
        _fault.setQName(new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "rerFonteError"));
        _fault.setClassName("it.emilia_romagna.regione.www.rer_fonte_raccoglitore.RerFonteError");
        _fault.setXmlType(new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "GenericFaultType"));
        _oper.addFault(_fault);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "searchEntitaInformativaRequest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", ">searchEntitaInformativaRequest"), it.emilia_romagna.regione.www.rer_fonte_raccoglitore.SearchEntitaInformativaRequest.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("searchEntitaInformativa", _params, new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "searchEntitaInformativaResponse"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", ">searchEntitaInformativaResponse"));
        _oper.setElementQName(new javax.xml.namespace.QName("", "searchEntitaInformativa"));
        _oper.setSoapAction("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/searchEntitaInformativa");
        _myOperationsList.add(_oper);
        if (_myOperations.get("searchEntitaInformativa") == null) {
            _myOperations.put("searchEntitaInformativa", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("searchEntitaInformativa")).add(_oper);
        _fault = new org.apache.axis.description.FaultDesc();
        _fault.setName("objectNotFoundError");
        _fault.setQName(new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "objectNotFoundError"));
        _fault.setClassName("it.emilia_romagna.regione.www.rer_fonte_raccoglitore.ObjectNotFoundError");
        _fault.setXmlType(new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "GenericFaultType"));
        _oper.addFault(_fault);
        _fault = new org.apache.axis.description.FaultDesc();
        _fault.setName("authorizationDeniedError");
        _fault.setQName(new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "authorizationDeniedError"));
        _fault.setClassName("it.emilia_romagna.regione.www.rer_fonte_raccoglitore.AuthorizationDeniedError");
        _fault.setXmlType(new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "GenericFaultType"));
        _oper.addFault(_fault);
        _fault = new org.apache.axis.description.FaultDesc();
        _fault.setName("rerFonteError");
        _fault.setQName(new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "rerFonteError"));
        _fault.setClassName("it.emilia_romagna.regione.www.rer_fonte_raccoglitore.RerFonteError");
        _fault.setXmlType(new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "GenericFaultType"));
        _oper.addFault(_fault);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "searchEntitaInformativaAvanzataRequest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", ">searchEntitaInformativaAvanzataRequest"), it.emilia_romagna.regione.www.rer_fonte_raccoglitore.SearchEntitaInformativaAvanzataRequest.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("searchEntitaInformativaAvanzata", _params, new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "searchEntitaInformativaAvanzataResponse"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", ">searchEntitaInformativaAvanzataResponse"));
        _oper.setElementQName(new javax.xml.namespace.QName("", "searchEntitaInformativaAvanzata"));
        _oper.setSoapAction("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/searchEntitaInformativaAvanzata");
        _myOperationsList.add(_oper);
        if (_myOperations.get("searchEntitaInformativaAvanzata") == null) {
            _myOperations.put("searchEntitaInformativaAvanzata", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("searchEntitaInformativaAvanzata")).add(_oper);
        _fault = new org.apache.axis.description.FaultDesc();
        _fault.setName("objectNotFoundError");
        _fault.setQName(new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "objectNotFoundError"));
        _fault.setClassName("it.emilia_romagna.regione.www.rer_fonte_raccoglitore.ObjectNotFoundError");
        _fault.setXmlType(new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "GenericFaultType"));
        _oper.addFault(_fault);
        _fault = new org.apache.axis.description.FaultDesc();
        _fault.setName("authorizationDeniedError");
        _fault.setQName(new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "authorizationDeniedError"));
        _fault.setClassName("it.emilia_romagna.regione.www.rer_fonte_raccoglitore.AuthorizationDeniedError");
        _fault.setXmlType(new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "GenericFaultType"));
        _oper.addFault(_fault);
        _fault = new org.apache.axis.description.FaultDesc();
        _fault.setName("rerFonteError");
        _fault.setQName(new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "rerFonteError"));
        _fault.setClassName("it.emilia_romagna.regione.www.rer_fonte_raccoglitore.RerFonteError");
        _fault.setXmlType(new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "GenericFaultType"));
        _oper.addFault(_fault);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "getAttributiRicercaRequest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", ">getAttributiRicercaRequest"), it.emilia_romagna.regione.www.rer_fonte_raccoglitore.GetAttributiRicercaRequest.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getAttributiRicerca", _params, new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "getAttributiRicercaResponse"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", ">getAttributiRicercaResponse"));
        _oper.setElementQName(new javax.xml.namespace.QName("", "getAttributiRicerca"));
        _oper.setSoapAction("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/getAttributiRicerca");
        _myOperationsList.add(_oper);
        if (_myOperations.get("getAttributiRicerca") == null) {
            _myOperations.put("getAttributiRicerca", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getAttributiRicerca")).add(_oper);
        _fault = new org.apache.axis.description.FaultDesc();
        _fault.setName("objectNotFoundError");
        _fault.setQName(new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "objectNotFoundError"));
        _fault.setClassName("it.emilia_romagna.regione.www.rer_fonte_raccoglitore.ObjectNotFoundError");
        _fault.setXmlType(new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "GenericFaultType"));
        _oper.addFault(_fault);
        _fault = new org.apache.axis.description.FaultDesc();
        _fault.setName("authorizationDeniedError");
        _fault.setQName(new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "authorizationDeniedError"));
        _fault.setClassName("it.emilia_romagna.regione.www.rer_fonte_raccoglitore.AuthorizationDeniedError");
        _fault.setXmlType(new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "GenericFaultType"));
        _oper.addFault(_fault);
        _fault = new org.apache.axis.description.FaultDesc();
        _fault.setName("rerFonteError");
        _fault.setQName(new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "rerFonteError"));
        _fault.setClassName("it.emilia_romagna.regione.www.rer_fonte_raccoglitore.RerFonteError");
        _fault.setXmlType(new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "GenericFaultType"));
        _oper.addFault(_fault);
    }

    public RerFonteSOAPSkeleton() {
        this.impl = new it.emilia_romagna.regione.www.rer_fonte_raccoglitore.RerFonteSOAPImpl();
    }

    public RerFonteSOAPSkeleton(it.emilia_romagna.regione.www.rer_fonte_raccoglitore.RerFonteRaccoglitore impl) {
        this.impl = impl;
    }
    public it.emilia_romagna.regione.www.rer_fonte_raccoglitore.GetMetadatiEntitaInformativaResponse getMetadatiEntitaInformativa(it.emilia_romagna.regione.www.rer_fonte_raccoglitore.GetMetadatiEntitaInformativaRequest getMetadatiEntitaInformativaRequest) throws java.rmi.RemoteException, it.emilia_romagna.regione.www.rer_fonte_raccoglitore.ObjectNotFoundError, it.emilia_romagna.regione.www.rer_fonte_raccoglitore.AuthorizationDeniedError, it.emilia_romagna.regione.www.rer_fonte_raccoglitore.RerFonteError
    {
        it.emilia_romagna.regione.www.rer_fonte_raccoglitore.GetMetadatiEntitaInformativaResponse ret = impl.getMetadatiEntitaInformativa(getMetadatiEntitaInformativaRequest);
        return ret;
    }

    public it.emilia_romagna.regione.www.rer_fonte_raccoglitore.GetEntitaInformativaResponse getEntitaInformativa(it.emilia_romagna.regione.www.rer_fonte_raccoglitore.GetEntitaInformativaRequest getEntitaInformativaRequest) throws java.rmi.RemoteException, it.emilia_romagna.regione.www.rer_fonte_raccoglitore.ObjectNotFoundError, it.emilia_romagna.regione.www.rer_fonte_raccoglitore.AuthorizationDeniedError, it.emilia_romagna.regione.www.rer_fonte_raccoglitore.RerFonteError
    {
        it.emilia_romagna.regione.www.rer_fonte_raccoglitore.GetEntitaInformativaResponse ret = impl.getEntitaInformativa(getEntitaInformativaRequest);
        return ret;
    }

    public it.emilia_romagna.regione.www.rer_fonte_raccoglitore.SearchEntitaInformativaResponse searchEntitaInformativa(it.emilia_romagna.regione.www.rer_fonte_raccoglitore.SearchEntitaInformativaRequest searchEntitaInformativaRequest) throws java.rmi.RemoteException, it.emilia_romagna.regione.www.rer_fonte_raccoglitore.ObjectNotFoundError, it.emilia_romagna.regione.www.rer_fonte_raccoglitore.AuthorizationDeniedError, it.emilia_romagna.regione.www.rer_fonte_raccoglitore.RerFonteError
    {
        it.emilia_romagna.regione.www.rer_fonte_raccoglitore.SearchEntitaInformativaResponse ret = impl.searchEntitaInformativa(searchEntitaInformativaRequest);
        return ret;
    }

    public it.emilia_romagna.regione.www.rer_fonte_raccoglitore.SearchEntitaInformativaAvanzataResponse searchEntitaInformativaAvanzata(it.emilia_romagna.regione.www.rer_fonte_raccoglitore.SearchEntitaInformativaAvanzataRequest searchEntitaInformativaAvanzataRequest) throws java.rmi.RemoteException, it.emilia_romagna.regione.www.rer_fonte_raccoglitore.ObjectNotFoundError, it.emilia_romagna.regione.www.rer_fonte_raccoglitore.AuthorizationDeniedError, it.emilia_romagna.regione.www.rer_fonte_raccoglitore.RerFonteError
    {
        it.emilia_romagna.regione.www.rer_fonte_raccoglitore.SearchEntitaInformativaAvanzataResponse ret = impl.searchEntitaInformativaAvanzata(searchEntitaInformativaAvanzataRequest);
        return ret;
    }

    public it.emilia_romagna.regione.www.rer_fonte_raccoglitore.GetAttributiRicercaResponse getAttributiRicerca(it.emilia_romagna.regione.www.rer_fonte_raccoglitore.GetAttributiRicercaRequest getAttributiRicercaRequest) throws java.rmi.RemoteException, it.emilia_romagna.regione.www.rer_fonte_raccoglitore.ObjectNotFoundError, it.emilia_romagna.regione.www.rer_fonte_raccoglitore.AuthorizationDeniedError, it.emilia_romagna.regione.www.rer_fonte_raccoglitore.RerFonteError
    {
        it.emilia_romagna.regione.www.rer_fonte_raccoglitore.GetAttributiRicercaResponse ret = impl.getAttributiRicerca(getAttributiRicercaRequest);
        return ret;
    }

}
