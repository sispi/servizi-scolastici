package it.emilia_romagna.regione.www.rer_fonte_raccoglitore.testclient;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.AttributoBooleanoType;
import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.AttributoDataRangeType;
import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.AttributoDataType;
import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.AttributoNumeroType;
import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.AttributoTestoType;
import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.AttributoType;
import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.DateRangeType;
import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.Document;
import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.GetAttributiRicercaRequest;
import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.GetAttributiRicercaResponse;
import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.GetEntitaInformativaRequest;
import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.GetEntitaInformativaRequestDelegheDelega;
import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.GetEntitaInformativaResponse;
import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.GetMetadatiEntitaInformativaRequest;
import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.GetMetadatiEntitaInformativaResponse;
import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.RerFonteRaccoglitore;
import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.RerFonteRaccoglitoreProxy;
import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.SearchEntitaInformativaAvanzataRequest;
import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.SearchEntitaInformativaAvanzataResponse;
import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.SearchEntitaInformativaRequest;
import it.emilia_romagna.regione.www.rer_fonte_raccoglitore.SearchEntitaInformativaResponse;

public class RerFonteTestClient {
	
	
	
	private static final String endpoint = "http://localhost:9080/FonteRaccoglitoreWeb/services/rer-fonteSOAP";
	public static final String federaData = "<xml><body>federaTest</body></xml>";
	
	
	public static void main(String[] args) throws RemoteException {
		
		System.out.println("ESECUZIONE TEST CLASSE "+RerFonteTestClient.class.getName());
		System.out.println("----------------GetAttributiRicerca-----------------");
		testGetAttributiRicerca();
		System.out.println("-----------------GetEntitaInformativa-------------------");
		testGetEntitaInformativa();
		System.out.println("------------GetMetadatiEntitaInformativa----------------");
		testGetMetadatiEntitaInformativa();
		System.out.println("----------------SearchEntitaInformativa-----------------");
		testSearchEntitaInformativa();
		System.out.println("----------------SearchEntitaInformativaAvanzata-----------------");
		testSearchEntitaInformativaAvanzata();
		System.out.println("--------------------------------------------------------");
	}
	
	public static void testSearchEntitaInformativaAvanzata() throws RemoteException {
		RerFonteRaccoglitoreProxy rerFonteRaccoglitoreProxy = new RerFonteRaccoglitoreProxy(endpoint);
		RerFonteRaccoglitore rerFonteRaccoglitore = rerFonteRaccoglitoreProxy.getRerFonteRaccoglitore();
		
		SearchEntitaInformativaAvanzataRequest request = new SearchEntitaInformativaAvanzataRequest();
		
		List<AttributoTestoType> attributiTesto = new ArrayList<AttributoTestoType>();
		attributiTesto.add(new AttributoTestoType("nome","Marco"));
		
		List<AttributoDataRangeType> attributiDataRange = new ArrayList<AttributoDataRangeType>();
		attributiDataRange.add(new AttributoDataRangeType("date"
				, new DateRangeType(Calendar.getInstance().getTime(), Calendar.getInstance().getTime(), true, true)));
		
		List<AttributoNumeroType> attributiNumero = new ArrayList<AttributoNumeroType>();
		attributiNumero.add(new AttributoNumeroType("numero",6));
		
		List<AttributoDataType> attributiData = new ArrayList<AttributoDataType>();
		attributiData.add(new AttributoDataType("oggi", Calendar.getInstance().getTime()));
		
		List<AttributoBooleanoType> attributiBool = new ArrayList<AttributoBooleanoType>();
		attributiBool.add(new AttributoBooleanoType("si", true));
		
		request.setAttributiTesto(attributiTesto.toArray(new AttributoTestoType[attributiTesto.size()]) );
		request.setAttributiDataRange(attributiDataRange.toArray(new AttributoDataRangeType[attributiDataRange.size()]) );
		request.setAttributiNumero(attributiNumero.toArray(new AttributoNumeroType[attributiNumero.size()]) );
		request.setAttributiData(attributiData.toArray(new AttributoDataType[attributiData.size()]));
		request.setAttributiBoolean(attributiBool.toArray(new AttributoBooleanoType[attributiBool.size()]));
		
		SearchEntitaInformativaAvanzataResponse response = rerFonteRaccoglitore.searchEntitaInformativaAvanzata(request);
		if(response!=null){
			String[] ids = response.getId();
			for(String id : ids){
				System.out.println(id);
			}
		}
	}
	
	public static void testGetAttributiRicerca() throws RemoteException {
		RerFonteRaccoglitoreProxy rerFonteRaccoglitoreProxy = new RerFonteRaccoglitoreProxy(endpoint);
		RerFonteRaccoglitore rerFonteRaccoglitore = rerFonteRaccoglitoreProxy.getRerFonteRaccoglitore();
		
		GetAttributiRicercaRequest request = new GetAttributiRicercaRequest();
		request.setCodiceFonte("EMRRICERCA1");
		GetAttributiRicercaResponse response = rerFonteRaccoglitore.getAttributiRicerca(request);
		if(response!=null){
			AttributoType[] attr = response.getAttributi();
			for (int i = 0; i < attr.length; i++) {
				System.out.println("codice: "+attr[i].getCodice()+" tipo:"+attr[i].getTipoAttributo().getValue());
//				System.out.println(attr[i].getDescrizione());
//				System.out.println(attr[i].getInfoAttributo());
//				System.out.println(attr[i].getObbligatorio());
			}
		}
	}
	
	public static void testGetEntitaInformativa() throws RemoteException {
		RerFonteRaccoglitoreProxy rerFonteRaccoglitoreProxy = new RerFonteRaccoglitoreProxy(endpoint);
		RerFonteRaccoglitore rerFonteRaccoglitore = rerFonteRaccoglitoreProxy.getRerFonteRaccoglitore();
		
		GetEntitaInformativaRequest request = new GetEntitaInformativaRequest();
		request.setUidEntitaInformativa("ciao");
		/*GetEntitaInformativaRequestDelegheDelega[] deleghe = new GetEntitaInformativaRequestDelegheDelega[1];
		deleghe[0] = new GetEntitaInformativaRequestDelegheDelega();
		deleghe[0].setId("ciao");
		deleghe[0].setTipoId("ciao");
		deleghe[0].setTipoRelazione("ciao");
		request.setDeleghe(deleghe);*/
		
		GetEntitaInformativaResponse response = rerFonteRaccoglitore.getEntitaInformativa(request);
		
		if(response != null){
			System.out.println(response.getTitle());
			System.out.println(response.getMimteType());
			System.out.println(new String(response.getEntitaInformativa()));
		}
		
		
	}
	
	public static void testGetMetadatiEntitaInformativa() throws RemoteException {
		RerFonteRaccoglitoreProxy rerFonteRaccoglitoreProxy = new RerFonteRaccoglitoreProxy(endpoint);
		RerFonteRaccoglitore rerFonteRaccoglitore = rerFonteRaccoglitoreProxy.getRerFonteRaccoglitore();
		
		GetMetadatiEntitaInformativaRequest request = new GetMetadatiEntitaInformativaRequest();
		request.setUidEntitaInformativa("ciao");
		
		
		GetEntitaInformativaRequestDelegheDelega[] deleghe = new GetEntitaInformativaRequestDelegheDelega[1];
		deleghe[0] = new GetEntitaInformativaRequestDelegheDelega();
		deleghe[0].setId("ciao");
		deleghe[0].setTipoId("ciao");
		deleghe[0].setTipoRelazione("ciao");
		
		request.setDeleghe(deleghe );
		
		GetMetadatiEntitaInformativaResponse response = rerFonteRaccoglitore.getMetadatiEntitaInformativa(request);
		
		System.out.println(response.getXmlMetadata());
	}
	
	public static void testSearchEntitaInformativa() throws RemoteException {
		RerFonteRaccoglitoreProxy rerFonteRaccoglitoreProxy = new RerFonteRaccoglitoreProxy(endpoint);
		RerFonteRaccoglitore rerFonteRaccoglitore = rerFonteRaccoglitoreProxy.getRerFonteRaccoglitore();
		
		SearchEntitaInformativaRequest request = new SearchEntitaInformativaRequest();
		request.setTesto("pippo");
		request.setDateRange(new DateRangeType(Calendar.getInstance().getTime(), Calendar.getInstance().getTime(), true, true));
		rerFonteRaccoglitore.searchEntitaInformativa(request);
		
		SearchEntitaInformativaResponse response = rerFonteRaccoglitore.searchEntitaInformativa(request);
		if(response!=null){
			String[] ids = response.getId();
			for(String id : ids){
				System.out.println(id);
			}
		}
	}
}
