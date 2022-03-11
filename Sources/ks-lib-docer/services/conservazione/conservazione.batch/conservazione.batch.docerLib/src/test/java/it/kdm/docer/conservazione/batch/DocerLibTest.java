package it.kdm.docer.conservazione.batch;

import java.net.URL;
import java.util.Properties;

import org.junit.BeforeClass;
import org.junit.Test;

public class DocerLibTest {

	private static DocerLib docerLib;
	private static String token;

	@BeforeClass
	public static void testLogin() throws Exception {
		docerLib = new DocerLib();
		Properties props = new Properties();
		props.load(DocerLibTest.class.getResourceAsStream("/test.properties"));
		
		String docerEpr = props.getProperty("docer.epr");
		docerLib.setEprDocer(docerEpr);
		
		String conservazioneEpr = props.getProperty("conservazione.epr");
		docerLib.setEprConservazione(conservazioneEpr);
		
		String username = props.getProperty("username");
		String password = props.getProperty("password");
		String library = props.getProperty("library");
		token = docerLib.login(username, password, library);
	}

	/*@Test
	public void testSearch() throws Exception {

		List<String> listRetColumns = new ArrayList<String>();

		listRetColumns.add("DOCNUM");
		listRetColumns.add("DOCNAME");
		listRetColumns.add("ABSTRACT");
		listRetColumns.add("TYPE_ID");
		listRetColumns.add("STATO_CONSERVAZIONE");
		listRetColumns.add("COD_ENTE");

		Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
		searchCriteria.put("DOCNAME", new ArrayList<String>());
		searchCriteria.get("DOCNAME").add("IphoneModem");
		searchCriteria.get("DOCNAME").add("test");
		
		searchCriteria.put("COD_ENTE", new ArrayList<String>());
		searchCriteria.get("COD_ENTE").add("ENTE1");
		
		DataTable<String> dt = docerLib.search(token, searchCriteria, listRetColumns);
		Assert.assertEquals(1, dt.getRows().size());
		Assert.assertEquals("IphoneModem", dt.getRow(0).get("DOCNAME"));
		Assert.assertEquals("559", dt.getRow(0).get("DOCNUM"));
	}
	
	@Test
	public void testSearchAllegati() throws Exception {

		List<String> listRetColumns = new ArrayList<String>();

		listRetColumns.add("DOCNUM");
		listRetColumns.add("DOCNAME");
		listRetColumns.add("ABSTRACT");
		listRetColumns.add("TYPE_ID");
		listRetColumns.add("STATO_CONSERVAZIONE");

		DataTable<String> dt = docerLib.searchAllegati(token, "559", listRetColumns);
		Assert.assertEquals(0, dt.getRows().size());
	}
	
	@Test
	public void testGetDocuments() throws Exception {
		URI uri = docerLib.getDocuments(token, "559");
		System.out.println(uri);
		File file = new File(uri);
		Assert.assertTrue(file.exists());
	}*/
	
	@Test
	public void testPreparaConservazione() throws Exception {
		URL url = this.getClass().getResource("/batchData.xml");
		docerLib.preparaConservazione(token, url.toURI());
	}
}
