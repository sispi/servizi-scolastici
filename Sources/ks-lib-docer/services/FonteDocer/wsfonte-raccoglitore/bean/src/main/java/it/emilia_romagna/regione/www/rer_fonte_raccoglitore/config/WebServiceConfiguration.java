package it.emilia_romagna.regione.www.rer_fonte_raccoglitore.config;

import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.AuthorizationDeniedError;
import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.GetAttributiRicercaRequest;
import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.GetAttributiRicercaResponse;
import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.GetEntitaInformativaRequest;
import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.GetEntitaInformativaResponse;
import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.GetMetadatiEntitaInformativaRequest;
import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.GetMetadatiEntitaInformativaResponse;
import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.ObjectNotFoundError;
import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.RerFonteError;
import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.SearchEntitaInformativaAvanzataRequest;
import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.SearchEntitaInformativaAvanzataResponse;
import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.SearchEntitaInformativaRequest;
import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.SearchEntitaInformativaResponse;
import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.businesslogic.IGetAttributiRicerca;
import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.businesslogic.IGetEntitaInformativa;
import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.businesslogic.IGetMetadatiEntitaInformativa;
import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.businesslogic.ISearchEntitaInformativa;
import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.businesslogic.ISearchEntitaInformativaAvanzata;

import java.rmi.RemoteException;
import java.util.Properties;

import org.slf4j.Logger;

public class WebServiceConfiguration {
	
private static final String FONTE_RACCOGLITORE_CONF_PROPERTIES = "fonte_raccoglitore_conf.properties";
	
	private static final String GET_ENTITA_INFORMATIVA = "getEntitaInformativa";
	private static final String SEARCH_ENTITA_INFORMATIVA = "searchEntitaInformativa";
	private static final String SEARCH_ENTITA_INFORMATIVA_AVANZATA = "searchEntitaInformativaAvanzata";
	private static final String GET_ATTRIBUTI_RICERCA = "getAttributiRicerca";
	private static final String GET_METADAI_ENTITA_INFORMATIVA = "getMetadatiEntitaInformativa";
	
	private static Class getEntitaInformativaClass;
	private static Class getMetadatiEntitaInformativaClass;
	private static Class searchEntitaInformativaClass;
	private static Class searchEntitaInformativaAvanzataClass;
	private static Class getAttributiRicercaClass;
	

	public final static Logger LOG = org.slf4j.LoggerFactory.getLogger(WebServiceConfiguration.class);
	
	private static Properties configuration = new Properties();
	
	
	static {
		try{
			LOG.info("carico file di properties "+FONTE_RACCOGLITORE_CONF_PROPERTIES+" dal classloader");
			configuration.load(WebServiceConfiguration.class.getClassLoader().getResourceAsStream(FONTE_RACCOGLITORE_CONF_PROPERTIES));
			getEntitaInformativaClass = carica(GET_ENTITA_INFORMATIVA,IGetEntitaInformativa.class);
			getMetadatiEntitaInformativaClass = carica(GET_METADAI_ENTITA_INFORMATIVA,IGetMetadatiEntitaInformativa.class);
			searchEntitaInformativaClass = carica(SEARCH_ENTITA_INFORMATIVA,ISearchEntitaInformativa.class);
			searchEntitaInformativaAvanzataClass = carica(SEARCH_ENTITA_INFORMATIVA_AVANZATA,ISearchEntitaInformativaAvanzata.class);
			getAttributiRicercaClass = carica(GET_ATTRIBUTI_RICERCA,IGetAttributiRicerca.class);
			
		}catch (Exception e) {
			throw new RuntimeException("");
		}
	}

	private static Class carica(String tipo,Class intefaccia){
		LOG.info("verifico classi di business: "+tipo);
		String clazzString = configuration.getProperty(tipo);
		Class clazz = null;
		LOG.info("carico classi di business: "+tipo+" => "+clazzString);
		if(clazzString==null){
			throw new RuntimeException("impossibile caricare la classe per "+tipo);
		}else{
			try{
				clazz = Class.forName(clazzString);
			}catch (Exception e) {
				throw new RuntimeException("impossibile caricare la classe la classe "+clazzString,e);
			}
			if(!intefaccia.isAssignableFrom(clazz)){
				throw new RuntimeException("la classe "+clazz.getName()+" non implementa l'interfaccia "+intefaccia.getName());
			}
		}
		return clazz;
	}
	
	
	public static GetMetadatiEntitaInformativaResponse invokeGetMetadatiEntitaInformativa(GetMetadatiEntitaInformativaRequest request,String middlewareData) throws RemoteException,RerFonteError,ObjectNotFoundError,AuthorizationDeniedError {
		IGetMetadatiEntitaInformativa iGetMetadatiEntitaInformativa = null;
		try{
    		iGetMetadatiEntitaInformativa = (IGetMetadatiEntitaInformativa)getMetadatiEntitaInformativaClass.newInstance();
    	}catch (Exception e) {
			throw new RemoteException("",e);
		}
		iGetMetadatiEntitaInformativa.preExecute(request,middlewareData);
		return iGetMetadatiEntitaInformativa.execute(request,middlewareData);
	}
	
	public static GetEntitaInformativaResponse invokeGetEntitaInformativa(GetEntitaInformativaRequest request,String middlewareData) throws RemoteException,RerFonteError,ObjectNotFoundError,AuthorizationDeniedError {
		IGetEntitaInformativa iGetEntitaInformativa = null;
		try{
    		iGetEntitaInformativa = (IGetEntitaInformativa)getEntitaInformativaClass.newInstance();
    	}catch (Exception e) {
			throw new RemoteException("",e);
		}
		iGetEntitaInformativa.preExecute(request,middlewareData);
		return iGetEntitaInformativa.execute(request,middlewareData);
	}
	
	
	public static SearchEntitaInformativaResponse invokeSearchEntitaInformativa(SearchEntitaInformativaRequest request,String middlewareData) throws RemoteException,RerFonteError,ObjectNotFoundError,AuthorizationDeniedError {
		ISearchEntitaInformativa iSearchEntitaInformativa = null;
		try{
			iSearchEntitaInformativa = ((ISearchEntitaInformativa)searchEntitaInformativaClass.newInstance());
    	}catch (Exception e) {
			throw new RemoteException("",e);
		}
		iSearchEntitaInformativa.preExecute(request,middlewareData);
		return iSearchEntitaInformativa.execute(request,middlewareData);
	}
	
	public static SearchEntitaInformativaAvanzataResponse invokeSearchEntitaInformativaAvanzata(SearchEntitaInformativaAvanzataRequest request,String middlewareData) throws RemoteException,RerFonteError,ObjectNotFoundError,AuthorizationDeniedError {
		ISearchEntitaInformativaAvanzata iSearchEntitaInformativaAvanzata = null;
		try{
			iSearchEntitaInformativaAvanzata = ((ISearchEntitaInformativaAvanzata)searchEntitaInformativaAvanzataClass.newInstance());
    	}catch (Exception e) {
			throw new RemoteException("",e);
		}
		iSearchEntitaInformativaAvanzata.preExecute(request,middlewareData);
		return iSearchEntitaInformativaAvanzata.execute(request,middlewareData);
	}
	
	public static GetAttributiRicercaResponse invokeGetAttributiRicerca(GetAttributiRicercaRequest request,String middlewareData) throws RemoteException,RerFonteError,ObjectNotFoundError,AuthorizationDeniedError {
		IGetAttributiRicerca iGetAttributiRicerca = null;
		try{
			iGetAttributiRicerca = ((IGetAttributiRicerca)getAttributiRicercaClass.newInstance());
    	}catch (Exception e) {
			throw new RemoteException("",e);
		}
		iGetAttributiRicerca.preExecute(request,middlewareData);
		return iGetAttributiRicerca.execute(request,middlewareData);
	}

}
