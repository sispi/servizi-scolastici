package it.emilia_romagna.regione.www.rer_fonte_raccoglitore.businesslogic;

import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.AuthorizationDeniedError;
import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.ObjectNotFoundError;
import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.RerFonteError;

public interface IGetEntitaInformativa {
	
	void preExecute(it.emilia_romagna.regione.www.rer_fonte_raccoglitore.GetEntitaInformativaRequest request,String middlewareData)throws RerFonteError,ObjectNotFoundError,AuthorizationDeniedError;
	
	it.emilia_romagna.regione.www.rer_fonte_raccoglitore.GetEntitaInformativaResponse execute(it.emilia_romagna.regione.www.rer_fonte_raccoglitore.GetEntitaInformativaRequest request,String middlewareData)throws RerFonteError,ObjectNotFoundError,AuthorizationDeniedError;

}
