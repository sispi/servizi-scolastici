/**
 * RerFonteSOAPImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.emilia_romagna.regione.www.rer_fonte_raccoglitore;

import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.config.WebServiceConfiguration;

public class RerFonteSOAPImpl implements it.emilia_romagna.regione.www.rer_fonte_raccoglitore.RerFonteRaccoglitore{
	
    public it.emilia_romagna.regione.www.rer_fonte_raccoglitore.GetMetadatiEntitaInformativaResponse getMetadatiEntitaInformativa(it.emilia_romagna.regione.www.rer_fonte_raccoglitore.GetMetadatiEntitaInformativaRequest getMetadatiEntitaInformativaRequest) throws java.rmi.RemoteException, it.emilia_romagna.regione.www.rer_fonte_raccoglitore.ObjectNotFoundError, it.emilia_romagna.regione.www.rer_fonte_raccoglitore.AuthorizationDeniedError, it.emilia_romagna.regione.www.rer_fonte_raccoglitore.RerFonteError {
    	String middlewareData = (String)org.apache.axis.MessageContext.getCurrentContext().getProperty("middlewareData");
        return WebServiceConfiguration.invokeGetMetadatiEntitaInformativa(getMetadatiEntitaInformativaRequest,middlewareData);
    }

    public it.emilia_romagna.regione.www.rer_fonte_raccoglitore.GetEntitaInformativaResponse getEntitaInformativa(it.emilia_romagna.regione.www.rer_fonte_raccoglitore.GetEntitaInformativaRequest getEntitaInformativaRequest) throws java.rmi.RemoteException, it.emilia_romagna.regione.www.rer_fonte_raccoglitore.ObjectNotFoundError, it.emilia_romagna.regione.www.rer_fonte_raccoglitore.AuthorizationDeniedError, it.emilia_romagna.regione.www.rer_fonte_raccoglitore.RerFonteError {
    	String middlewareData = (String)org.apache.axis.MessageContext.getCurrentContext().getProperty("middlewareData");
        return WebServiceConfiguration.invokeGetEntitaInformativa(getEntitaInformativaRequest, middlewareData);
    }

    public it.emilia_romagna.regione.www.rer_fonte_raccoglitore.SearchEntitaInformativaResponse searchEntitaInformativa(it.emilia_romagna.regione.www.rer_fonte_raccoglitore.SearchEntitaInformativaRequest searchEntitaInformativaRequest) throws java.rmi.RemoteException, it.emilia_romagna.regione.www.rer_fonte_raccoglitore.ObjectNotFoundError, it.emilia_romagna.regione.www.rer_fonte_raccoglitore.AuthorizationDeniedError, it.emilia_romagna.regione.www.rer_fonte_raccoglitore.RerFonteError {
    	String middlewareData = (String)org.apache.axis.MessageContext.getCurrentContext().getProperty("middlewareData");
    	return WebServiceConfiguration.invokeSearchEntitaInformativa(searchEntitaInformativaRequest, middlewareData);
    }

    public it.emilia_romagna.regione.www.rer_fonte_raccoglitore.SearchEntitaInformativaAvanzataResponse searchEntitaInformativaAvanzata(it.emilia_romagna.regione.www.rer_fonte_raccoglitore.SearchEntitaInformativaAvanzataRequest searchEntitaInformativaAvanzataRequest) throws java.rmi.RemoteException, it.emilia_romagna.regione.www.rer_fonte_raccoglitore.ObjectNotFoundError, it.emilia_romagna.regione.www.rer_fonte_raccoglitore.AuthorizationDeniedError, it.emilia_romagna.regione.www.rer_fonte_raccoglitore.RerFonteError {
    	String middlewareData = (String)org.apache.axis.MessageContext.getCurrentContext().getProperty("middlewareData");
    	return WebServiceConfiguration.invokeSearchEntitaInformativaAvanzata(searchEntitaInformativaAvanzataRequest, middlewareData);
    }

    public it.emilia_romagna.regione.www.rer_fonte_raccoglitore.GetAttributiRicercaResponse getAttributiRicerca(it.emilia_romagna.regione.www.rer_fonte_raccoglitore.GetAttributiRicercaRequest getAttributiRicercaRequest) throws java.rmi.RemoteException, it.emilia_romagna.regione.www.rer_fonte_raccoglitore.ObjectNotFoundError, it.emilia_romagna.regione.www.rer_fonte_raccoglitore.AuthorizationDeniedError, it.emilia_romagna.regione.www.rer_fonte_raccoglitore.RerFonteError {
    	String middlewareData = (String)org.apache.axis.MessageContext.getCurrentContext().getProperty("middlewareData");
    	return WebServiceConfiguration.invokeGetAttributiRicerca(getAttributiRicercaRequest, middlewareData);
    }

}
