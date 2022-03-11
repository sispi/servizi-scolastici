/**
 * RerFonteLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.emilia_romagna.regione.www.rer_fonte_raccoglitore;

public class RerFonteLocator extends org.apache.axis.client.Service implements it.emilia_romagna.regione.www.rer_fonte_raccoglitore.RerFonte {

    public RerFonteLocator() {
    }


    public RerFonteLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public RerFonteLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for RerFonteSOAP
    private java.lang.String RerFonteSOAP_address = "http://localhost:9080/FonteRaccoglitoreWeb/services/rer-fonteSOAP";

    public java.lang.String getRerFonteSOAPAddress() {
        return RerFonteSOAP_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String RerFonteSOAPWSDDServiceName = "rer-fonteSOAP";

    public java.lang.String getRerFonteSOAPWSDDServiceName() {
        return RerFonteSOAPWSDDServiceName;
    }

    public void setRerFonteSOAPWSDDServiceName(java.lang.String name) {
        RerFonteSOAPWSDDServiceName = name;
    }

    public it.emilia_romagna.regione.www.rer_fonte_raccoglitore.RerFonteRaccoglitore getRerFonteSOAP() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(RerFonteSOAP_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getRerFonteSOAP(endpoint);
    }

    public it.emilia_romagna.regione.www.rer_fonte_raccoglitore.RerFonteRaccoglitore getRerFonteSOAP(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            it.emilia_romagna.regione.www.rer_fonte_raccoglitore.RerFonteSOAPStub _stub = new it.emilia_romagna.regione.www.rer_fonte_raccoglitore.RerFonteSOAPStub(portAddress, this);
            _stub.setPortName(getRerFonteSOAPWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setRerFonteSOAPEndpointAddress(java.lang.String address) {
        RerFonteSOAP_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (it.emilia_romagna.regione.www.rer_fonte_raccoglitore.RerFonteRaccoglitore.class.isAssignableFrom(serviceEndpointInterface)) {
                it.emilia_romagna.regione.www.rer_fonte_raccoglitore.RerFonteSOAPStub _stub = new it.emilia_romagna.regione.www.rer_fonte_raccoglitore.RerFonteSOAPStub(new java.net.URL(RerFonteSOAP_address), this);
                _stub.setPortName(getRerFonteSOAPWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("rer-fonteSOAP".equals(inputPortName)) {
            return getRerFonteSOAP();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "rer-fonte");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://www.regione.emilia-romagna.it/rer-fonte-raccoglitore/", "rer-fonteSOAP"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("RerFonteSOAP".equals(portName)) {
            setRerFonteSOAPEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
