/*package it.kdm.docer.conservazione.batch.ConservazioneTest;

import it.kdm.docer.conservazione.batch.Conservazione;
import it.kdm.docer.conservazione.batch.DocerLib;
import it.kdm.docer.conservazione.wsclient.WSConservazioneBatchStub.ConservazioneResult;
import it.kdm.utils.DataTable;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class ConservazioneTest {
	
	
	private static Conservazione conservazioneLib;
	private static DocerLib docerLib;
	private static String token;
	
	@BeforeClass
	public static void testLogin() throws Exception {
		conservazioneLib = new Conservazione();
		docerLib = new DocerLib();
		
		Properties props = new Properties();
		props.load(ConservazioneTest.class.getResourceAsStream("/test.properties"));
		
		String docerEpr = props.getProperty("docer.epr");
		conservazioneLib.setEprDocer(docerEpr);
		docerLib.setEprDocer(docerEpr);
		
		String conservazioneEpr = props.getProperty("conservazione.epr");
		conservazioneLib.setEprConservazione(conservazioneEpr);
		docerLib.setEprConservazione(conservazioneEpr);
		
		String username = props.getProperty("username");
		String password = props.getProperty("password");
		String library = props.getProperty("library");
		token = conservazioneLib.login(username, password, library);
	}
	
	@Test
	public void testConservazione() throws Exception {
		List<String> listRetColumns = new ArrayList<String>();

		listRetColumns.add("DOCNUM");
		listRetColumns.add("STATO_PANTAREI");
		listRetColumns.add("DOCNAME");
		listRetColumns.add("ABSTRACT");
		listRetColumns.add("TYPE_ID");
		listRetColumns.add("COD_ENTE");
		listRetColumns.add("COD_AOO");
		listRetColumns.add("COD_AREA");
		listRetColumns.add("COD_TITOLARIO");
		listRetColumns.add("ANNO_FASCICOLO");
		listRetColumns.add("NUM_FASCICOLO");
		listRetColumns.add("ANNO_PG");
		listRetColumns.add("NUM_PG");
		listRetColumns.add("OGGETTO_PG");
		listRetColumns.add("REGISTRO_PG");
		listRetColumns.add("AUTHOR_ID");
		listRetColumns.add("TYPIST_ID");
		listRetColumns.add("DEFAULT_EXTENSION");
		listRetColumns.add("CREATION_DATE");
		listRetColumns.add("SCHEMA_ID");

		Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
		
		searchCriteria.put("COD_ENTE", new ArrayList<String>());
		searchCriteria.get("COD_ENTE").add("Ente1");
		
		//DataTable<String> dt = docerLib.search(token, searchCriteria, listRetColumns);
		//ConservazioneResult ret = conservazioneLib.versamento(token, dt.getRow(0), new DataTable<String>(),
		//		new URI[0], Conservazione.TipoConservazione.SOSTITUTIVA, true, true, true);
		
		//Assert.assertEquals(0, ret.getErrorCode());
	}
}
*/