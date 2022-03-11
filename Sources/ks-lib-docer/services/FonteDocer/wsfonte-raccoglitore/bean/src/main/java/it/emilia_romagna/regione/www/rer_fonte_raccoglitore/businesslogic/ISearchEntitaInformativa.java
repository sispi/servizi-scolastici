package it.emilia_romagna.regione.www.rer_fonte_raccoglitore.businesslogic;

import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.AuthorizationDeniedError;
import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.ObjectNotFoundError;
import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.RerFonteError;

public interface ISearchEntitaInformativa {
	
	void preExecute(it.emilia_romagna.regione.www.rer_fonte_raccoglitore.SearchEntitaInformativaRequest request,String middlewareData)throws RerFonteError,ObjectNotFoundError,AuthorizationDeniedError;
	
	it.emilia_romagna.regione.www.rer_fonte_raccoglitore.SearchEntitaInformativaResponse execute(it.emilia_romagna.regione.www.rer_fonte_raccoglitore.SearchEntitaInformativaRequest request,String middlewareData)throws RerFonteError,ObjectNotFoundError,AuthorizationDeniedError;
	
	

}
