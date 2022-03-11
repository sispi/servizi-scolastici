package it.emilia_romagna.regione.www.rer_fonte_raccoglitore.businesslogic;

import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.AuthorizationDeniedError;
import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.ObjectNotFoundError;
import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.RerFonteError;

public interface ISearchEntitaInformativaAvanzata {
	
	void preExecute(it.emilia_romagna.regione.www.rer_fonte_raccoglitore.SearchEntitaInformativaAvanzataRequest request,String middlewareData)throws RerFonteError,ObjectNotFoundError,AuthorizationDeniedError;
	
	it.emilia_romagna.regione.www.rer_fonte_raccoglitore.SearchEntitaInformativaAvanzataResponse execute(it.emilia_romagna.regione.www.rer_fonte_raccoglitore.SearchEntitaInformativaAvanzataRequest request,String middlewareData)throws RerFonteError,ObjectNotFoundError,AuthorizationDeniedError;
	
	

}
