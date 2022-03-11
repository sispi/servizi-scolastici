package it.emilia_romagna.regione.www.rer_fonte_raccoglitore.businesslogic;

import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.AuthorizationDeniedError;
import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.ObjectNotFoundError;
import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.RerFonteError;

public interface IGetAttributiRicerca {
	
	void preExecute (it.emilia_romagna.regione.www.rer_fonte_raccoglitore.GetAttributiRicercaRequest request,String middlewareData)throws RerFonteError,ObjectNotFoundError,AuthorizationDeniedError;
	
	it.emilia_romagna.regione.www.rer_fonte_raccoglitore.GetAttributiRicercaResponse execute(it.emilia_romagna.regione.www.rer_fonte_raccoglitore.GetAttributiRicercaRequest request,String middlewareData)throws RerFonteError,ObjectNotFoundError,AuthorizationDeniedError;
	
	

}
