package it.emilia_romagna.regione.www.rer_fonte_raccoglitore;

import java.rmi.RemoteException;

import org.apache.axis.client.Stub;
import org.apache.axis.message.SOAPHeaderElement;

public class RerFonteRaccoglitoreProxy implements it.emilia_romagna.regione.www.rer_fonte_raccoglitore.RerFonteRaccoglitore {
  private String _endpoint = null;
  private it.emilia_romagna.regione.www.rer_fonte_raccoglitore.RerFonteRaccoglitore rerFonteRaccoglitore = null;
  

public RerFonteRaccoglitoreProxy() {
    _initRerFonteRaccoglitoreProxy();
  }
  
  public RerFonteRaccoglitoreProxy(String endpoint) {
    _endpoint = endpoint;
    _initRerFonteRaccoglitoreProxy();
  }
  
  private void _initRerFonteRaccoglitoreProxy() {
    try {
      rerFonteRaccoglitore = (new it.emilia_romagna.regione.www.rer_fonte_raccoglitore.RerFonteLocator()).getRerFonteSOAP();
      if (rerFonteRaccoglitore != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)rerFonteRaccoglitore)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)rerFonteRaccoglitore)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (rerFonteRaccoglitore != null)
      ((javax.xml.rpc.Stub)rerFonteRaccoglitore)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public it.emilia_romagna.regione.www.rer_fonte_raccoglitore.RerFonteRaccoglitore getRerFonteRaccoglitore() {
    if (rerFonteRaccoglitore == null)
      _initRerFonteRaccoglitoreProxy();
    
    return rerFonteRaccoglitore;
  }
  
  public it.emilia_romagna.regione.www.rer_fonte_raccoglitore.GetMetadatiEntitaInformativaResponse getMetadatiEntitaInformativa(it.emilia_romagna.regione.www.rer_fonte_raccoglitore.GetMetadatiEntitaInformativaRequest getMetadatiEntitaInformativaRequest) throws java.rmi.RemoteException{
    if (rerFonteRaccoglitore == null)
      _initRerFonteRaccoglitoreProxy();
    return rerFonteRaccoglitore.getMetadatiEntitaInformativa(getMetadatiEntitaInformativaRequest);
  }
  
  public it.emilia_romagna.regione.www.rer_fonte_raccoglitore.GetEntitaInformativaResponse getEntitaInformativa(it.emilia_romagna.regione.www.rer_fonte_raccoglitore.GetEntitaInformativaRequest getEntitaInformativaRequest) throws java.rmi.RemoteException{
    if (rerFonteRaccoglitore == null)
      _initRerFonteRaccoglitoreProxy();
    return rerFonteRaccoglitore.getEntitaInformativa(getEntitaInformativaRequest);
  }
  
  public it.emilia_romagna.regione.www.rer_fonte_raccoglitore.SearchEntitaInformativaResponse searchEntitaInformativa(it.emilia_romagna.regione.www.rer_fonte_raccoglitore.SearchEntitaInformativaRequest searchEntitaInformativaRequest) throws java.rmi.RemoteException{
    if (rerFonteRaccoglitore == null)
      _initRerFonteRaccoglitoreProxy();
    return rerFonteRaccoglitore.searchEntitaInformativa(searchEntitaInformativaRequest);
  }

  public SearchEntitaInformativaAvanzataResponse searchEntitaInformativaAvanzata(
		  SearchEntitaInformativaAvanzataRequest searchEntitaInformativaAvanzataRequest)
				  throws RemoteException, ObjectNotFoundError, AuthorizationDeniedError,
				  RerFonteError {
	  return rerFonteRaccoglitore
			  .searchEntitaInformativaAvanzata(searchEntitaInformativaAvanzataRequest);
  }

  public GetAttributiRicercaResponse getAttributiRicerca(
		  GetAttributiRicercaRequest getAttributiRicercaRequest)
				  throws RemoteException, ObjectNotFoundError, AuthorizationDeniedError,
				  RerFonteError {
	  return rerFonteRaccoglitore.getAttributiRicerca(getAttributiRicercaRequest);
  }
  
  
}