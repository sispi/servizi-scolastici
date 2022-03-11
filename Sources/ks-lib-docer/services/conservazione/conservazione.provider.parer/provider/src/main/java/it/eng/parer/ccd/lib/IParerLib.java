package it.eng.parer.ccd.lib;

import it.eng.parer.ws.versamento.dto.FileBinario;
import it.eng.parer.ws.xml.versReq.UnitaDocAggAllegati;
import it.eng.parer.ws.xml.versReq.UnitaDocumentaria;
import it.eng.parer.ws.xml.versResp.EsitoVersAggAllegati;
import it.eng.parer.ws.xml.versResp.EsitoVersamento;

import java.util.Vector;

/**
 * Interfaccia per il servizio di invio e reperimento dei documenti in conservazione al PARER
 *   
 * @author 
 *
 */
public interface IParerLib {

	/**
	 * Esegue l'invio di una unita' documentaria al PARER
	 * 
	 * @param UnitaDocumentaria: xml di chiamata; deve essere non nullo e valorizzato
	 * @param Vector<FileBinario>: Vettore dei file da riversare; deve essere non nullo e valorizzato
	 * 
	 * @throws Exception: Saranno sollevate eccezioni solo in caso di disservizio del PARER, quali http status 500 o 404.
	 */
	public EsitoVersamento versamento (String loginname, String password, UnitaDocumentaria versamento, Vector<FileBinario> fileBinari)
		throws Exception;

	public EsitoVersAggAllegati modificaMetadati(String username, String password,
			UnitaDocAggAllegati ud) throws Exception;
	
	public EsitoVersAggAllegati aggiungiDocumento(String username, String password,
			UnitaDocAggAllegati ud, FileBinario file) throws Exception;
}