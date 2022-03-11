package it.kdm.docarea.alfresco.provider;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

//import it.kdm.docarea.alfresco.provider.AlfrescoAOOInfo;
//import it.kdm.docarea.alfresco.provider.vo.AlfrescoAreaTematicaInfo;
//import it.kdm.docarea.alfresco.provider.vo.AlfrescoEnteInfo;
import it.kdm.docarea.sdk.EnumDocumentDeleteType;
import it.kdm.docarea.sdk.exceptions.DocAreaException;
import junit.framework.TestCase;

import org.alfresco.webservice.util.Constants;
public class ProviderTest extends TestCase{

	private ProviderAlfresco33ce provider;
	
	@Override
	protected void setUp() throws Exception {
		provider = new ProviderAlfresco33ce();
	}
	
	@Test
	public void testLogin() throws DocAreaException{
		String ticket = provider.login("admin", "admin", null);
		assertNotNull(ticket);
	}
	
//	public void testLogout(){
//		String ticket = provider.login("admin", "admin", null);
//		try {
//			provider.Logout();
//		} catch (Exception e) {
//			fail();
//		}
//	}
	
//	public void testIsManager(){
//		String ticket = provider.login("admin", "admin", null);
//		boolean isManager = false;
//		try {
//			isManager = provider.isManager();
//		} catch (Exception e) {
//			fail();
//		}
//		assertEquals(true, isManager);
//	}
	
//	public void testCreateEnte(){
//		provider.login("admin", "admin", "DOCAREA");
//		AlfrescoEnteInfo enteInfo = provider.createEnte("enteKdmNuovo", "ente di prova");
//		assertEquals("enteKdmNuovo",enteInfo.getCodiceEnte());
//		assertEquals("ente di prova", enteInfo.getDescrizione());
//	}
//	
//	public void testCreateAOO(){
//		provider.login("admin", "admin", "DOCAREA");
//		AlfrescoAOOInfo aooInfo = provider.createAOO("AOO2", "aoo di prova", "enteKdmNuovo");
//		assertEquals("AOO2",aooInfo.getCodiceAOO());
//		assertEquals("aoo di prova", aooInfo.getDescrizione());
//		assertEquals("enteKdmNuovo",aooInfo.getCodiceEnte());
//	}
//	
//	public void testCreateAreaTematica(){
//		provider.login("admin", "admin", "DOCAREA");
//		AlfrescoAreaTematicaInfo areaTematicaInfo = provider.createAreaTematica("Area Tem 1", "Area tematica 1", "AOO2", "enteKdmNuovo");
//		assertEquals("Area Tem 1",areaTematicaInfo.getCodiceAreaTematica());
//		assertEquals("Area tematica 1", areaTematicaInfo.getDescrizione());
//		assertEquals("AOO2",areaTematicaInfo.getCodiceAOO());
//		assertEquals("enteKdmNuovo",areaTematicaInfo.getCodiceEnte());
//		
//	}
//	
	public void testCreateDocument() throws FileNotFoundException{
		provider.login("admin", "admin", "DOCAREA");
		Map<String,String> properties = new HashMap<String, String>();
		properties.put("DOCNAME", "Luca");
		properties.put("TYPE_ID", "DOCUMENTO");
		properties.put("COD_ENTE", "enteKdmNuovo");
		properties.put("COD_AOO", "AOO2");
		String filePath = "C:\\claudio.txt";
		File file = new File(filePath);
		FileInputStream fileInputStream = new FileInputStream(file);
		//InputStream is = this.getClass().getResourceAsStream(fileInputStream);
		long update = provider.CreateDocument("DEF_DOC_GENERICO", properties, fileInputStream);
	}
	
//	public void testLockDocument() throws FileNotFoundException{
//		provider.login("admin", "admin", "docarea");
//		long docId = 697;
//		provider.LockDocument(docId);
//	}
//	
//	public void testUnLockDocument() throws FileNotFoundException{
//		provider.login("admin", "admin", "docarea");
//		long docId = 697;
//		provider.UnLockDocument(docId);
//	}
//	
//	public void testdeleteDocument() throws FileNotFoundException{
//		provider.login("admin", "admin", "docarea");
//		long docId = 697;
//		provider.DeleteDocument(docId,EnumDocumentDeleteType.all);
//	}
	
	
	
}
