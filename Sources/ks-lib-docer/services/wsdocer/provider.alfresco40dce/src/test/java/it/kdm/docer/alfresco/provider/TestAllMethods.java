package it.kdm.docer.alfresco.provider;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import it.kdm.docer.sdk.EnumACLRights;
import it.kdm.docer.sdk.EnumBoolean;
import it.kdm.docer.sdk.EnumSearchOrder;
import it.kdm.docer.sdk.classes.AOOId;
import it.kdm.docer.sdk.classes.AOOInfo;
import it.kdm.docer.sdk.classes.CustomItemInfo;
import it.kdm.docer.sdk.classes.EnteId;
import it.kdm.docer.sdk.classes.EnteInfo;
import it.kdm.docer.sdk.classes.FascicoloId;
import it.kdm.docer.sdk.classes.FascicoloInfo;
import it.kdm.docer.sdk.classes.GroupProfileInfo;
import it.kdm.docer.sdk.classes.KeyValuePair;
import it.kdm.docer.sdk.classes.LoggedUserInfo;
import it.kdm.docer.sdk.classes.TitolarioId;
import it.kdm.docer.sdk.classes.TitolarioInfo;
import it.kdm.docer.sdk.classes.UserProfileInfo;
import it.kdm.docer.sdk.exceptions.DocerException;
import it.kdm.docer.sdk.interfaces.IGroupInfo;
import it.kdm.docer.sdk.interfaces.IGroupProfileInfo;
import it.kdm.docer.sdk.interfaces.ILoggedUserInfo;
import it.kdm.docer.sdk.interfaces.IUserInfo;
import it.kdm.docer.sdk.interfaces.IUserProfileInfo;
import it.kdm.utils.DataRow;
import it.kdm.utils.DataTable;

public class TestAllMethods {

    @Test
    public void testSOLR() {

    	KeyValuePair kvp = new KeyValuePair();
    	kvp.setKey("test");
    	kvp.setValue("sss");
    	
    	System.out.println(kvp.getKey());
    	System.out.println(kvp.getValue());

    }

    @Test
    public void testHashMap() {

	Map<String,String> map = new HashMap<String, String>();
	
	for(int i=0; i<800000; i++){
	    map.put(String.valueOf(i), i +" string");
	}

	System.out.println("size: " +map.size());

	long maxtime = 0;
	for(int i=0; i<100000; i++){
	    long start = System.currentTimeMillis();
	    String val = map.get(String.valueOf(i));
	    long end = System.currentTimeMillis();
	    //System.out.println(val +" --> " +(end-start) +" msec");
	    long thistime = (end-start);
	    if(thistime>maxtime){
		maxtime = thistime;
	    }
	}
	
	
	System.out.println("maxtime --> " +(maxtime) +" msec");

	
	
    }
    
    @Test
    public void testCreateEnte() {

	Provider provider = null;
	try {

	    provider = new Provider();

	    ILoggedUserInfo uinfo = new LoggedUserInfo();
	    String ticket = provider.login("admin", "admin", null);

	    uinfo.setUserId("admin");
	    uinfo.setCodiceEnte(null);
	    uinfo.setTicket(ticket);

	    provider.setCurrentUser(uinfo);

	    EnteInfo enteInfo = new EnteInfo();
	    enteInfo.setCodiceEnte("ENTETEST");
	    enteInfo.setDescrizione("ente test");
	    provider.createEnte(enteInfo);

	    Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
	    searchCriteria.put("COD_ENTE", Arrays.asList("ENTETEST"));

	    long start = new Date().getTime();
	    List<Map<String, String>> searchResult = new ArrayList<Map<String,String>>();
	    while(searchResult.size()==0){
		searchResult = provider.searchAnagrafiche("ENTE", searchCriteria, Arrays.asList("COD_ENTE", "DES_ENTE", "ENABLED"));
	    }
	    long end = new Date().getTime();
	    
	    System.out.println("indicizzato dopo " +(end-start) +" msec");

	    if (searchResult.size() != 1) {
		throw new Exception("trovati " + searchResult.size() + " ente ENTETEST");
	    }

	    for (String p : searchResult.get(0).keySet()) {
		System.out.println(p + "=" + searchResult.get(0).get(p));
	    }

	    assertTrue(true);
	}
	catch (Exception e) {

	    System.out.println(e.getMessage());
	    assertTrue(false);
	}
	finally {
	    try {
		provider.logout();
	    }
	    catch (DocerException e) {
	    }
	}

    }

    @Test
    public void testUpdateEnte() {

	Provider provider = null;
	try {

	    provider = new Provider();

	    ILoggedUserInfo uinfo = new LoggedUserInfo();
	    String ticket = provider.login("admin", "admin", null);

	    uinfo.setUserId("admin");
	    uinfo.setCodiceEnte(null);
	    uinfo.setTicket(ticket);

	    provider.setCurrentUser(uinfo);

	    EnteId enteId = new EnteId();
	    enteId.setCodiceEnte("ENTETEST");

	    EnteInfo enteInfo = new EnteInfo();
	    enteInfo.setDescrizione("new ente test");
	    enteInfo.setEnabled(EnumBoolean.TRUE);

	    provider.updateEnte(enteId, enteInfo);

	    Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
	    searchCriteria.put("COD_ENTE", Arrays.asList("ENTETEST"));

	    List<Map<String, String>> searchResult = provider.searchAnagrafiche("ENTE", searchCriteria, Arrays.asList("COD_ENTE", "DES_ENTE", "ENABLED"));

	    if (searchResult.size() != 1) {
		throw new Exception("trovati " + searchResult.size() + " ente ENTETEST");
	    }

	    for (String p : searchResult.get(0).keySet()) {
		System.out.println(p + "=" + searchResult.get(0).get(p));
	    }

	    assertTrue(true);
	}
	catch (Exception e) {

	    System.out.println(e.getMessage());
	    assertTrue(false);
	}
	finally {
	    try {
		provider.logout();
	    }
	    catch (DocerException e) {
	    }
	}

    }

    @Test
    public void testCreateAoo() {

	Provider provider = null;
	try {

	    provider = new Provider();

	    ILoggedUserInfo uinfo = new LoggedUserInfo();
	    String ticket = provider.login("admin", "admin", null);

	    uinfo.setUserId("admin");
	    uinfo.setCodiceEnte(null);
	    uinfo.setTicket(ticket);

	    provider.setCurrentUser(uinfo);

	    AOOInfo aooInfo = new AOOInfo();
	    aooInfo.setCodiceEnte("ENTETEST");
	    aooInfo.setCodiceAOO("AOOTEST");
	    aooInfo.setDescrizione("aoo test");
	    provider.createAOO(aooInfo);

	    Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
	    searchCriteria.put("COD_ENTE", Arrays.asList("ENTETEST"));
	    searchCriteria.put("COD_AOO", Arrays.asList("AOOTEST"));

	    List<String> returnProperties = Arrays.asList("COD_ENTE", "COD_AOO", "DES_AOO", "ENABLED");
	    List<Map<String, String>> searchResult = provider.searchAnagrafiche("AOO", searchCriteria, returnProperties);

	    if (searchResult.size() != 1) {
		throw new Exception("trovati " + searchResult.size() + " aoo AOOTEST");
	    }

	    for (String p : searchResult.get(0).keySet()) {
		System.out.println(p + "=" + searchResult.get(0).get(p));
	    }

	    assertTrue(true);
	}
	catch (Exception e) {

	    System.out.println(e.getMessage());
	    assertTrue(false);
	}
	finally {
	    try {
		provider.logout();
	    }
	    catch (DocerException e) {
	    }
	}

    }

    @Test
    public void testUpdateAOO() {

	Provider provider = null;
	try {

	    provider = new Provider();

	    ILoggedUserInfo uinfo = new LoggedUserInfo();
	    String ticket = provider.login("admin", "admin", null);

	    uinfo.setUserId("admin");
	    uinfo.setCodiceEnte(null);
	    uinfo.setTicket(ticket);

	    provider.setCurrentUser(uinfo);

	    AOOId aooId = new AOOId();
	    aooId.setCodiceEnte("ENTETEST");
	    aooId.setCodiceAOO("AOOTEST");

	    AOOInfo aooInfo = new AOOInfo();
	    aooInfo.setDescrizione("new aoo test");
	    aooInfo.setEnabled(EnumBoolean.TRUE);

	    provider.updateAOO(aooId, aooInfo);

	    Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
	    searchCriteria.put("COD_ENTE", Arrays.asList("ENTETEST"));
	    searchCriteria.put("COD_AOO", Arrays.asList("AOOTEST"));

	    List<String> returnProperties = Arrays.asList("COD_ENTE", "COD_AOO", "DES_AOO", "ENABLED");
	    List<Map<String, String>> searchResult = provider.searchAnagrafiche("AOO", searchCriteria, returnProperties);

	    if (searchResult.size() != 1) {
		throw new Exception("trovati " + searchResult.size() + " aoo AOOTEST");
	    }

	    for (String p : searchResult.get(0).keySet()) {
		System.out.println(p + "=" + searchResult.get(0).get(p));
	    }

	}
	catch (Exception e) {

	    System.out.println(e.getMessage());
	    assertTrue(false);
	}
	finally {
	    try {
		provider.logout();
	    }
	    catch (DocerException e) {
	    }
	}

    }

    @Test
    public void testCreateTitolario() {

	Provider provider = null;
	try {

	    provider = new Provider();

	    ILoggedUserInfo uinfo = new LoggedUserInfo();
	    String ticket = provider.login("admin", "admin", null);

	    uinfo.setUserId("admin");
	    uinfo.setCodiceEnte(null);
	    uinfo.setTicket(ticket);

	    provider.setCurrentUser(uinfo);

	    List<String> returnProperties = Arrays.asList("COD_ENTE", "COD_AOO", "PARENT_CLASSIFICA", "CLASSIFICA", "DES_TITOLARIO", "ENABLED");

	    Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
	    searchCriteria.put("COD_ENTE", Arrays.asList("ENTETEST"));
	    searchCriteria.put("COD_AOO", Arrays.asList("AOO_TEST"));
	    searchCriteria.put("CLASSIFICA", Arrays.asList("CLASSIFICATEST"));

	    List<Map<String, String>> searchResult = provider.searchAnagrafiche("TITOLARIO", searchCriteria, returnProperties);

	    TitolarioInfo titolarioInfo = null;

	    if (searchResult.size() == 0) {
		titolarioInfo = new TitolarioInfo();
		titolarioInfo.setCodiceEnte("ENTETEST");
		titolarioInfo.setCodiceAOO("AOO_TEST");
		titolarioInfo.setClassifica("CLASSIFICATEST");
		titolarioInfo.setCodiceTitolario("CLASSIFICATEST");
		titolarioInfo.setDescrizione("titoaraio test");
		provider.createTitolario(titolarioInfo);

		searchResult = provider.searchAnagrafiche("TITOLARIO", searchCriteria, returnProperties);

		if (searchResult.size() != 1) {
		    throw new Exception("trovati " + searchResult.size() + " titolario CLASSIFICATEST");
		}

		for (String p : searchResult.get(0).keySet()) {
		    System.out.println(p + "=" + searchResult.get(0).get(p));
		}
	    }
	    else {
		System.out.println("CLASSIFICATEST esistente");
	    }

	    titolarioInfo = new TitolarioInfo();
	    titolarioInfo.setCodiceEnte("ENTETEST");
	    titolarioInfo.setCodiceAOO("AOO_TEST");
	    titolarioInfo.setParentClassifica("CLASSIFICATEST");
	    titolarioInfo.setClassifica("CLASSIFICATEST.FIGLIO");
	    titolarioInfo.setCodiceTitolario("CLASSIFICATEST.FIGLIO");
	    titolarioInfo.setDescrizione("titoaraio test figlio");
	    provider.createTitolario(titolarioInfo);

	    searchCriteria.clear();
	    searchCriteria.put("COD_ENTE", Arrays.asList("ENTETEST"));
	    searchCriteria.put("COD_AOO", Arrays.asList("AOO_TEST"));
	    searchCriteria.put("CLASSIFICA", Arrays.asList("CLASSIFICATEST.FIGLIO"));

	    returnProperties = Arrays.asList("COD_ENTE", "COD_AOO", "CLASSIFICA", "PARENT_CLASSIFICA", "DES_TITOLARIO", "ENABLED");

	    searchResult = provider.searchAnagrafiche("TITOLARIO", searchCriteria, returnProperties);

	    if (searchResult.size() != 1) {
		throw new Exception("trovati " + searchResult.size() + " titolario CLASSIFICATEST.FIGLIO");
	    }

	    for (String p : searchResult.get(0).keySet()) {
		System.out.println(p + "=" + searchResult.get(0).get(p));
	    }

	    assertTrue(true);
	}
	catch (Exception e) {

	    System.out.println(e.getMessage());
	    assertTrue(false);
	}
	finally {
	    try {
		provider.logout();
	    }
	    catch (DocerException e) {
	    }
	}

    }

    @Test
    public void testCreateFascicolo() {

	Provider provider = null;
	try {

	    provider = new Provider();

	    ILoggedUserInfo uinfo = new LoggedUserInfo();
	    String ticket = provider.login("admin", "admin", null);

	    uinfo.setUserId("admin");
	    uinfo.setCodiceEnte(null);
	    uinfo.setTicket(ticket);

	    provider.setCurrentUser(uinfo);

	    Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
	    searchCriteria.put("COD_ENTE", Arrays.asList("ENTETEST"));
	    searchCriteria.put("COD_AOO", Arrays.asList("AOO_TEST"));
	    searchCriteria.put("CLASSIFICA", Arrays.asList("CLASSIFICATEST"));
	    searchCriteria.put("ANNO_FASCICOLO", Arrays.asList("2014"));
	    searchCriteria.put("PROGR_FASCICOLO", Arrays.asList("FASCICOLOTEST"));

	    List<String> returnProperties = Arrays.asList("COD_ENTE", "COD_AOO", "CLASSIFICA", "ANNO_FASCICOLO", "PARENT_PROGR_FASCICOLO", "PROGR_FASCICOLO", "DES_FASCICOLO", "ENABLED");

	    List<Map<String, String>> searchResult = provider.searchAnagrafiche("FASCICOLO", searchCriteria, returnProperties);

	    if (searchResult.size() == 0) {
		FascicoloInfo fascicoloInfo = new FascicoloInfo();
		fascicoloInfo.setCodiceEnte("ENTETEST");
		fascicoloInfo.setCodiceAOO("AOO_TEST");
		fascicoloInfo.setClassifica("CLASSIFICATEST");
		fascicoloInfo.getExtraInfo().put("COD_TITOLARIO", "CLASSIFICATEST");
		fascicoloInfo.setAnnoFascicolo("2014");
		fascicoloInfo.setProgressivo("FASCICOLOTEST");
		fascicoloInfo.setNumeroFascicolo("FASCICOLOTEST");
		fascicoloInfo.setDescrizione("fascicolo test");
		provider.createFascicolo(fascicoloInfo);

		long start = new Date().getTime();
		while(searchResult.size()==0){
			searchResult = provider.searchAnagrafiche("FASCICOLO", searchCriteria, returnProperties);		    
		}

		long end = new Date().getTime();
		
		System.out.println("indicizzato dopo " +(end-start) +" msec");

//		for (String p : searchResult.get(0).keySet()) {
//		    System.out.println(p + "=" + searchResult.get(0).get(p));
//		}
	    }
	    else {
		System.out.println("Fascicolo esistente");
	    }

	    searchCriteria.put("PROGR_FASCICOLO", Arrays.asList("FASCICOLOTEST/FIGLIO"));

	    searchResult = provider.searchAnagrafiche("FASCICOLO", searchCriteria, returnProperties);

	    if (searchResult.size() == 0) {
		FascicoloInfo fascicoloInfo = new FascicoloInfo();
		fascicoloInfo.setCodiceEnte("ENTETEST");
		fascicoloInfo.setCodiceAOO("AOO_TEST");
		fascicoloInfo.setClassifica("CLASSIFICATEST");
		fascicoloInfo.getExtraInfo().put("COD_TITOLARIO", "CLASSIFICATEST");
		fascicoloInfo.setAnnoFascicolo("2014");
		fascicoloInfo.setParentProgressivo("FASCICOLOTEST");
		fascicoloInfo.setProgressivo("FASCICOLOTEST/FIGLIO");
		fascicoloInfo.setNumeroFascicolo("FASCICOLOTEST/FIGLIO");
		fascicoloInfo.setDescrizione("fascicolo test figlio");

		provider.createFascicolo(fascicoloInfo);

		long start = new Date().getTime();
		while(searchResult.size()==0){
			searchResult = provider.searchAnagrafiche("FASCICOLO", searchCriteria, returnProperties);		    
		}

		long end = new Date().getTime();
		
		System.out.println("indicizzato dopo " +(end-start) +" msec");
//
//		
//		searchResult = provider.searchAnagrafiche("FASCICOLO", searchCriteria, returnProperties);
//
//		for (String p : searchResult.get(0).keySet()) {
//		    System.out.println(p + "=" + searchResult.get(0).get(p));
//		}
	    }
	    else {
		System.out.println("Fascicolo esistente");
	    }

	    assertTrue(true);
	}
	catch (Exception e) {

	    System.out.println(e.getMessage());
	    assertTrue(false);
	}
	finally {
	    try {
		provider.logout();
	    }
	    catch (DocerException e) {
	    }
	}

    }

    @Test
    public void testCreateDocumentFascicolato() {

	Provider provider = null;
	try {

	    provider = new Provider();

	    ILoggedUserInfo uinfo = new LoggedUserInfo();
	    String ticket = provider.login("luca.biasin", "luca.biasin", null);

	    uinfo.setUserId("luca.biasin");
	    uinfo.setCodiceEnte(null);
	    uinfo.setTicket(ticket);

	    provider.setCurrentUser(uinfo);

	    Map<String, String> info = new HashMap<String, String>();
	    info.put("COD_ENTE", "ENTETEST");
	    info.put("COD_AOO", "AOO_TEST");
	    info.put("TYPE_ID", "documento");
	    // info.put("CREATION_DATE", "2007-03-02T15:10:43.359+01:00");
	    // info.put("CLASSIFICA", "CLASSIFICATEST");
	    // info.put("COD_TITOLARIO", "CLASSIFICATEST");
	    // info.put("ANNO_FASCICOLO", "2014");
	    // info.put("NUM_FASCICOLO", "FASCICOLOTEST/FIGLIO");
	    // info.put("PROGR_FASCICOLO", "FASCICOLOTEST/FIGLIO");
	    //
	    InputStream is = null;

	    String docnum = "110";
	    info.put("DOCNAME", docnum + ".txt");
	    info.put("DOCNUM", docnum);
	    try {
		// is = this.getClass().getResourceAsStream("/Import/text.txt");
		// provider.createDocument("documento", info, is);
		//
		// System.out.println("creato docnum " +docnum);

		info.put("CLASSIFICA", "CLASSIFICATEST");
		info.put("COD_TITOLARIO", "CLASSIFICATEST");
		info.put("ANNO_FASCICOLO", "2014");
		info.put("NUM_FASCICOLO", "FASCICOLOTEST/FIGLIO");
		info.put("PROGR_FASCICOLO", "FASCICOLOTEST/FIGLIO");
		// info.put("PROGR_FASCICOLO", "");

		provider.updateProfileDocument("110", info);

		System.out.println("fascicolato docnum " + docnum);

	    }
	    catch (Exception e) {
		System.out.println(e.getMessage());
		assertTrue(false);
		return;
	    }
	    finally {
		if (is != null) {
		    is.close();
		}
	    }

	    assertTrue(true);
	}
	catch (Exception e) {
	    System.out.println(e.getMessage());
	    assertTrue(false);
	}

    }

    @Test
    public void testCreateDocument() {

	Provider provider = null;
	try {

	    provider = new Provider();

	    ILoggedUserInfo uinfo = new LoggedUserInfo();
	    String ticket = provider.login("admin", "admin", null);

	    uinfo.setUserId("admin");
	    uinfo.setCodiceEnte(null);
	    uinfo.setTicket(ticket);

	    provider.setCurrentUser(uinfo);

	    Map<String, String> info = new HashMap<String, String>();
	    info.put("COD_ENTE", "P_BO");
	    info.put("COD_AOO", "AOOPBO");
	    info.put("TYPE_ID", "documento");
	    // info.put("CREATION_DATE", "2007-03-02T15:10:43.359+01:00");
	    info.put("DOCNAME", "test.txt");
	    
	    InputStream is = null;

	    String docnum = "101";
	    //info.put("DOCNAME", docnum + ".txt");
	    //info.put("DOCNUM", docnum);
	    try {
		is = this.getClass().getResourceAsStream("/Import/text.txt");
		provider.createDocument("documento", info, is);
		System.out.println("creato docnum " + docnum);
	    }
	    catch (Exception e) {
		System.out.println(e.getMessage());
		assertTrue(false);
		return;
	    }
	    finally {
		if (is != null) {
		    is.close();
		}
	    }

//	    docnum = "102";
//	    info.put("DOCNAME", docnum + ".txt");
//	    info.put("DOCNUM", docnum);
//	    try {
//		is = this.getClass().getResourceAsStream("/Import/text.txt");
//		provider.createDocument("documento", info, is);
//		System.out.println("creato docnum " + docnum);
//	    }
//	    catch (Exception e) {
//		System.out.println(e.getMessage());
//		assertTrue(false);
//		return;
//	    }
//	    finally {
//		if (is != null) {
//		    is.close();
//		}
//	    }
//
//	    docnum = "103";
//	    info.put("DOCNAME", docnum + ".txt");
//	    info.put("DOCNUM", docnum);
//	    try {
//		is = this.getClass().getResourceAsStream("/Import/text.txt");
//		provider.createDocument("documento", info, is);
//		System.out.println("creato docnum " + docnum);
//	    }
//	    catch (Exception e) {
//		System.out.println(e.getMessage());
//		assertTrue(false);
//		return;
//	    }
//	    finally {
//		if (is != null) {
//		    is.close();
//		}
//	    }
//
//	    docnum = "104";
//	    info.put("DOCNAME", docnum + ".txt");
//	    info.put("DOCNUM", docnum);
//	    try {
//		is = this.getClass().getResourceAsStream("/Import/text.txt");
//		provider.createDocument("documento", info, is);
//		System.out.println("creato docnum " + docnum);
//	    }
//	    catch (Exception e) {
//		System.out.println(e.getMessage());
//		assertTrue(false);
//		return;
//	    }
//	    finally {
//		if (is != null) {
//		    is.close();
//		}
//	    }
//
//	    docnum = "105";
//	    info.put("DOCNAME", docnum + ".txt");
//	    info.put("DOCNUM", docnum);
//	    try {
//		is = this.getClass().getResourceAsStream("/Import/text.txt");
//		provider.createDocument("documento", info, is);
//		System.out.println("creato docnum " + docnum);
//	    }
//	    catch (Exception e) {
//		System.out.println(e.getMessage());
//		assertTrue(false);
//		return;
//	    }
//	    finally {
//		if (is != null) {
//		    is.close();
//		}
//	    }
//
//	    docnum = "106";
//	    info.put("DOCNAME", docnum + ".txt");
//	    info.put("DOCNUM", docnum);
//	    try {
//		is = this.getClass().getResourceAsStream("/Import/text.txt");
//		provider.createDocument("documento", info, is);
//		System.out.println("creato docnum " + docnum);
//	    }
//	    catch (Exception e) {
//		System.out.println(e.getMessage());
//		assertTrue(false);
//		return;
//	    }
//	    finally {
//		if (is != null) {
//		    is.close();
//		}
//	    }
	    assertTrue(true);
	}
	catch (Exception e) {
	    System.out.println(e.getMessage());
	    assertTrue(false);
	}

    }

    @Test
    public void testCreateUserSOLR() {

	Provider provider = null;
	try {

	    provider = new Provider();

	    ILoggedUserInfo uinfo = new LoggedUserInfo();
	    String ticket = provider.login("admin", "admin", null);

	    uinfo.setUserId("admin");
	    uinfo.setCodiceEnte(null);
	    uinfo.setTicket(ticket);

	    provider.setCurrentUser(uinfo);


	    String userid = "user1";
	    
	    IUserProfileInfo userProfileInfo = new UserProfileInfo();
	    userProfileInfo.setUserId(userid);
	    userProfileInfo.setUserPassword(userid);
	    userProfileInfo.setFullName(userid);

	    try {
		provider.createUser(userProfileInfo);
		System.out.println("creato utente" +userid);
		
		long start = new Date().getTime();
		
		Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();

		searchCriteria.put("USER_ID", Arrays.asList(userid));
		
		List<String> returnProperties = new ArrayList<String>();
		returnProperties.add("USER_ID");
		
		List<IUserProfileInfo> results = new ArrayList<IUserProfileInfo>(); 
		
		while(results.size()==0){
		    results = provider.searchUsers(searchCriteria);
		}

		long end = new Date().getTime();
			
		System.out.println("Indicizzato dopo " +(end-start) + " msec");
		
		
		
	    }
	    catch (Exception e) {
		System.out.println(e.getMessage());
		assertTrue(false);
		return;
	    }
	    finally {
		
	    }

	}
	catch (Exception e) {
	    System.out.println(e.getMessage());
	    assertTrue(false);
	}

    }

    
    @Test
    public void testSearchDocument() {

	Provider provider = null;
	try {

	    provider = new Provider();

	    ILoggedUserInfo uinfo = new LoggedUserInfo();
	    String ticket = provider.login("admin", "admin", null);

	    uinfo.setUserId("admin");
	    uinfo.setCodiceEnte(null);
	    uinfo.setTicket(ticket);

	    provider.setCurrentUser(uinfo);


	    try {
		
		Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();

		searchCriteria.put("ANNO_PG", Arrays.asList("*"));
		
		List<String> returnProperties = new ArrayList<String>();
		returnProperties.add("DOCNAME");
		returnProperties.add("DOCNUM");
		returnProperties.add("COD_ENTE");
		DataTable<String> results = provider.searchDocuments(searchCriteria, null, returnProperties, -1, null);   
		
		System.out.println("TROVATI: " + results.getRows().size() + " risultati");
		for (int index = 0; index < results.getRows().size(); index++) {

		    DataRow<?> row = results.getRow(index);
		    System.out.println("row " + index + ":");
		    for (String nomeColonna : results.getColumnNames()) {

			System.out.println(nomeColonna + " = " + row.get(nomeColonna));
		    }
		    System.out.println("   --------- ");
		}
		
		assertTrue(true);
		
	    }
	    catch (Exception e) {
		System.out.println(e.getMessage());
		assertTrue(false);
		return;
	    }

	}
	catch (Exception e) {
	    System.out.println(e.getMessage());
	    assertTrue(false);
	}

    }
    
    
    @Test
    public void testCreateDocumentSOLR() {

	Provider provider = null;
	try {

	    provider = new Provider();

	    ILoggedUserInfo uinfo = new LoggedUserInfo();
	    String ticket = provider.login("admin", "admin", null);

	    uinfo.setUserId("admin");
	    uinfo.setCodiceEnte(null);
	    uinfo.setTicket(ticket);

	    provider.setCurrentUser(uinfo);

	    Map<String, String> info = new HashMap<String, String>();
	    info.put("COD_ENTE", "ENTE_LUCA");
	    info.put("COD_AOO", "AOO_LUCA_TEST");
	    info.put("TYPE_ID", "documento");
	    // info.put("CREATION_DATE", "2007-03-02T15:10:43.359+01:00");
	    info.put("DOCNAME", "test.txt");
	    //info.put("DOCNUM", docnum);
	    
	    InputStream is = null;


	    try {
		is = this.getClass().getResourceAsStream("/Import/text.txt");
		String docnum = provider.createDocument("documento", info, is);
		System.out.println("creato docnum " +docnum);
		
		long start = new Date().getTime();
		
		Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();

		searchCriteria.put("DOCNUM", Arrays.asList(docnum));
		
		List<String> returnProperties = new ArrayList<String>();
		returnProperties.add("DOCNUM");
		
		DataTable<String> results = new DataTable<String>(); 
		
		while(results.getRows().size()==0){
		    results = provider.searchDocuments(searchCriteria, null, returnProperties, -1, null);   
		    Thread.sleep(1000);
		}
		long end = new Date().getTime();
			
		System.out.println("Indicizzato dopo " +(end-start) + " msec");
		
		
		
	    }
	    catch (Exception e) {
		System.out.println(e.getMessage());
		assertTrue(false);
		return;
	    }
	    finally {
		if (is != null) {
		    is.close();
		}
	    }

	}
	catch (Exception e) {
	    System.out.println(e.getMessage());
	    assertTrue(false);
	}

    }

    @Test
    public void testVersion() {

	InputStream is = null;

	Provider provider = null;
	try {

	    provider = new Provider();

	    ILoggedUserInfo uinfo = new LoggedUserInfo();
	    String ticket = provider.login("admin", "admin", null);

	    uinfo.setUserId("admin");
	    uinfo.setCodiceEnte(null);
	    uinfo.setTicket(ticket);

	    provider.setCurrentUser(uinfo);

	    
	    byte[] b = provider.downloadLastVersion("101", "C:/windows/temp/wsdocer", 10000000);
	    if (b != null)
		System.out.println("last version di 101 ha dimensione " + b.length + " Bytes");

	    is = this.getClass().getResourceAsStream("/Import/text2.txt");

	    String version = provider.addNewVersion("101", is);
	    System.out.println("creata versione " + version);

	    b = provider.downloadVersion("101", version, "C:/windows/temp/wsdocer", 10000000);
	    if (b != null)
		System.out.println("versione " + version + " ha dimensione " + b.length + " Bytes");

	    System.out.println("recupero versioni di 101");
	    List<String> versions = provider.getVersions("101");
	    for (String v : versions) {
		System.out.println("versione: " + v);
	    }

	    assertTrue(false);
	}
	catch (Exception e) {
	    System.out.println(e.getMessage());
	    assertTrue(false);
	}
	finally {
	    if (is != null) {
		try {
		    is.close();
		}
		catch (IOException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
	    }
	}

    }

    // @Test
    // public void testRelated() {
    //
    // String filePath = "C:\\test.txt";
    //
    // FileInputStream fileInputStream = null;
    //
    // File file = new File(filePath);
    // try {
    // fileInputStream = new FileInputStream(file);
    // }
    // catch (FileNotFoundException e1) {
    //
    // System.out.println(e1);
    // return;
    // }
    //
    // Provider provider = null;
    // try {
    //
    // provider = new Provider();
    //
    // ILoggedUserInfo uinfo = new LoggedUserInfo();
    // String ticket = provider.login("admin", "admin", null);
    //
    // uinfo.setUserId("admin");
    // uinfo.setCodiceEnte(null);
    // uinfo.setTicket(ticket);
    //
    // provider.setCurrentUser(uinfo);
    //
    // Map<String, String> info = new HashMap<String, String>();
    // info.put("COD_ENTE", "ENTETEST");
    // info.put("COD_AOO", "AOOTEST");
    // info.put("DOCNAME", "test.txt");
    // info.put("TYPE_ID", "documento");
    // // info.put("CREATION_DATE", "2007-03-02T15:10:43.359+01:00");
    //
    // String docid_1 = provider.createDocument("documento", info,
    // fileInputStream);
    // System.out.println("creato " + docid_1);
    //
    // filePath = "C:\\error.log";
    //
    // fileInputStream = null;
    //
    // file = new File(filePath);
    // try {
    // fileInputStream = new FileInputStream(file);
    // }
    // catch (FileNotFoundException e1) {
    //
    // System.out.println(e1);
    // return;
    // }
    //
    // info.clear();
    // info.put("COD_ENTE", "ENTETEST");
    // info.put("COD_AOO", "AOOTEST");
    // info.put("DOCNAME", "test2.txt");
    // info.put("TYPE_ID", "documento");
    //
    // String docid_2 = provider.createDocument("documento", info,
    // fileInputStream);
    // System.out.println("creato " + docid_2);
    //
    // filePath = "C:\\error.log";
    //
    // fileInputStream = null;
    //
    // file = new File(filePath);
    // try {
    // fileInputStream = new FileInputStream(file);
    // }
    // catch (FileNotFoundException e1) {
    //
    // System.out.println(e1);
    // return;
    // }
    //
    // info.clear();
    // info.put("COD_ENTE", "ENTETEST");
    // info.put("COD_AOO", "AOOTEST");
    // info.put("DOCNAME", "test3.txt");
    // info.put("TYPE_ID", "documento");
    //
    // String docid_3 = provider.createDocument("documento", info,
    // fileInputStream);
    // System.out.println("creato " + docid_3);
    //
    // provider.addRelatedDocuments(docid_1, Arrays.asList(docid_2, docid_3));
    //
    // List<String> related = provider.getRelatedDocuments(docid_1);
    // for (String r : related) {
    // System.out.println("related: " + r);
    // }
    //
    // }
    // catch (Exception e) {
    //
    // try {
    // if (fileInputStream != null)
    // fileInputStream.close();
    // }
    // catch (Exception e2) {
    // }
    //
    // System.out.println(e.getMessage());
    // // assertTrue(false);
    // }
    //
    // }

    @Test
    public void testAddRemoveRelated() {

	Provider provider = null;
	try {

	    provider = new Provider();

	    ILoggedUserInfo uinfo = new LoggedUserInfo();
	    String ticket = provider.login("admin", "admin", null);

	    uinfo.setUserId("admin");
	    uinfo.setCodiceEnte(null);
	    uinfo.setTicket(ticket);

	    provider.setCurrentUser(uinfo);

	    System.out.println("104 add related 105, 106");
	    provider.addRelatedDocuments("104", Arrays.asList("105", "106"));

	    List<String> related = provider.getRelatedDocuments("104");
	    System.out.println("related di 104: " + related.toString());
	    related = provider.getRelatedDocuments("105");
	    System.out.println("related di 105: " + related.toString());
	    related = provider.getRelatedDocuments("106");
	    System.out.println("related di 106: " + related.toString());

	    System.out.println("101 add related 102, 103");
	    provider.addRelatedDocuments("101", Arrays.asList("101", "102", "103"));
	    related = provider.getRelatedDocuments("101");
	    System.out.println("related di 101: " + related.toString());
	    related = provider.getRelatedDocuments("102");
	    System.out.println("related di 102: " + related.toString());
	    related = provider.getRelatedDocuments("103");
	    System.out.println("related di 103: " + related.toString());

	    System.out.println("106 add related 101");
	    provider.addRelatedDocuments("106", Arrays.asList("101", "101"));

	    related = provider.getRelatedDocuments("101");
	    System.out.println("related di 101: " + related.toString());
	    related = provider.getRelatedDocuments("102");
	    System.out.println("related di 102: " + related.toString());
	    related = provider.getRelatedDocuments("103");
	    System.out.println("related di 103: " + related.toString());
	    related = provider.getRelatedDocuments("104");
	    System.out.println("related di 104: " + related.toString());
	    related = provider.getRelatedDocuments("105");
	    System.out.println("related di 105: " + related.toString());
	    related = provider.getRelatedDocuments("106");
	    System.out.println("related di 106: " + related.toString());

	    System.out.println("remove all");
	    provider.removeRelatedDocuments("101", Arrays.asList("101", "102", "103", "104", "105", "106"));
	    related = provider.getRelatedDocuments("101");
	    System.out.println("related di 101: " + related.toString());
	    provider.removeRelatedDocuments("102", Arrays.asList("101", "102", "103", "104", "105", "106"));
	    related = provider.getRelatedDocuments("102");
	    System.out.println("related di 102: " + related.toString());
	    provider.removeRelatedDocuments("103", Arrays.asList("101", "102", "103", "104", "105", "106"));
	    related = provider.getRelatedDocuments("103");
	    System.out.println("related di 103: " + related.toString());
	    provider.removeRelatedDocuments("104", Arrays.asList("101", "102", "103", "104", "105", "106"));
	    related = provider.getRelatedDocuments("104");
	    System.out.println("related di 104: " + related.toString());
	    provider.removeRelatedDocuments("105", Arrays.asList("101", "102", "103", "104", "105", "106"));
	    related = provider.getRelatedDocuments("105");
	    System.out.println("related di 105: " + related.toString());
	    provider.removeRelatedDocuments("106", Arrays.asList("101", "102", "103", "104", "105", "106"));
	    related = provider.getRelatedDocuments("106");
	    System.out.println("related di 106: " + related.toString());

	    assertTrue(true);
	}
	catch (Exception e) {

	    System.out.println(e.getMessage());
	    assertTrue(false);
	}

    }

    @Test
    public void testAdvancedVersion() {

	String filePath = "C:\\test.txt";

	FileInputStream fileInputStream = null;

	File file = new File(filePath);
	try {
	    fileInputStream = new FileInputStream(file);
	}
	catch (FileNotFoundException e1) {

	    System.out.println(e1);
	    return;
	}

	Provider provider = null;
	try {

	    provider = new Provider();

	    ILoggedUserInfo uinfo = new LoggedUserInfo();
	    String ticket = provider.login("admin", "admin", null);

	    uinfo.setUserId("admin");
	    uinfo.setCodiceEnte(null);
	    uinfo.setTicket(ticket);

	    provider.setCurrentUser(uinfo);

	    Map<String, String> info = new HashMap<String, String>();
	    info.put("COD_ENTE", "ENTETEST");
	    info.put("COD_AOO", "AOOTEST");
	    info.put("DOCNAME", "test.txt");
	    info.put("TYPE_ID", "documento");
	    // info.put("CREATION_DATE", "2007-03-02T15:10:43.359+01:00");

	    String docid_1 = provider.createDocument("documento", info, fileInputStream);
	    System.out.println("creato " + docid_1);

	    filePath = "C:\\error.log";

	    fileInputStream = null;

	    file = new File(filePath);
	    try {
		fileInputStream = new FileInputStream(file);
	    }
	    catch (FileNotFoundException e1) {

		System.out.println(e1);
		return;
	    }

	    info.clear();
	    info.put("COD_ENTE", "ENTETEST");
	    info.put("COD_AOO", "AOOTEST");
	    info.put("DOCNAME", "test2.txt");
	    info.put("TYPE_ID", "documento");
	    // info.put("CREATION_DATE", "2007-03-02T15:10:43.359+01:00");

	    String docid_2 = provider.createDocument("documento", info, fileInputStream);
	    System.out.println("creato " + docid_2);

	    provider.addNewAdvancedVersion(docid_1, docid_2);

	    List<String> versions = provider.getAdvancedVersions(docid_1);
	    for (String v : versions) {
		System.out.println("versione avanzata di: " + docid_1 + ": " + v);
	    }

	}
	catch (Exception e) {

	    try {
		if (fileInputStream != null)
		    fileInputStream.close();
	    }
	    catch (Exception e2) {
	    }

	    System.out.println(e.getMessage());
	    // assertTrue(false);
	}

    }

    @Test
    public void testSearchDocuments() {

	try {
	    Provider provider = new Provider();
	    String token = provider.login("admin", "admin", "");

	    ILoggedUserInfo lui = new LoggedUserInfo("admin", "", token);

	    provider.setCurrentUser(lui);

	    try {

		Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();

		searchCriteria.put("DOCNUM", Arrays.asList("2899"));

		List<String> returnProperties = Arrays.asList("content_size", "docnum", "stato_pantarei", "stato_business", "docname", "abstract", "default_extension", "type_id", "cod_ente",
			"cod_aoo", "classifica", "anno_fascicolo", "progr_fascicolo", "cod_titolario", "num_fascicolo", "anno_pg", "num_pg", "oggetto_pg", "registro_pg", "data_pg", "author_id",
			"typist_id", "creation_date", "fasc_secondari", "d_registraz", "n_registraz", "a_registraz", "o_registraz", "id_registro", "stato_conserv", "usa_d_co_cer", "t_d_contr_cer",
			"d_co_cer", "forza_coll", "forza_accettaz", "forza_conserv", "flag_conserv", "t_conserv", "doc_url", "archive_type", "app_versante", "ms_t_registraz", "tipo_componente",
			"docnum_princ", "registro_pub", "pubblicato", "numero_pub", "anno_pub", "oggetto_pub", "data_inizio_pub", "data_fine_pub", "stato_archivistico", "mittenti", "destinatari",
			"tipo_protocollazione", "tipo_firma", "firmatario", "docnum_record", "ud_version", "doc_hash", "num_pg_mittente", "data_pg_mittente", "cod_ente_mittente", "cod_aoo_mittente",
			"classifica_mittente", "fascicolo_mittente", "annullato_pg", "d_annull_pg", "m_annull_pg", "annull_registraz", "d_annull_registraz", "m_annull_registraz");

		// Map<String, EnumSearchOrder> orderby = new HashMap<String,
		// EnumSearchOrder>();
		// orderby.put("DOCNUM", EnumSearchOrder.DESC);
		DataTable<?> results = provider.searchDocuments(searchCriteria, null, returnProperties, -1, null);

		System.out.println("TROVATI: " + results.getRows().size() + " risultati");
		for (int index = 0; index < results.getRows().size(); index++) {

		    DataRow<?> row = results.getRow(index);
		    System.out.println("row " + index + ":");
		    for (String nomeColonna : results.getColumnNames()) {

			System.out.println(nomeColonna + " = " + row.get(nomeColonna));
		    }
		    System.out.println("   --------- ");
		}

	    }
	    catch (DocerException e) {
		System.out.println("ERRORE: " + e.getMessage());
	    }

	    if (token != null) {
		provider.logout();
	    }
	}
	catch (Exception e) {
	    System.out.println("error: " + e);
	}
    }

    @Test
    public void testSearchFolders() {

	try {
	    Provider provider = new Provider();
	    String token = provider.login("admin", "admin", "");

	    ILoggedUserInfo lui = new LoggedUserInfo("admin", "", token);

	    provider.setCurrentUser(lui);

	    try {

		Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();

		// searchCriteria.put("", Arrays.asList("2899"));

		List<String> returnProperties = Arrays.asList("folder_id", "folder_name", "parent_folder_id", "des_folder", "FOLDER_OWNER");

		// Map<String, EnumSearchOrder> orderby = new HashMap<String,
		// EnumSearchOrder>();
		// orderby.put("DOCNUM", EnumSearchOrder.DESC);
		DataTable<?> results = provider.searchFolders(searchCriteria, returnProperties, -1, null);

		System.out.println("TROVATI: " + results.getRows().size() + " risultati");
		for (int index = 0; index < results.getRows().size(); index++) {

		    DataRow<?> row = results.getRow(index);
		    System.out.println("row " + index + ":");
		    for (String nomeColonna : results.getColumnNames()) {

			System.out.println(nomeColonna + " = " + row.get(nomeColonna));
		    }
		    System.out.println("   --------- ");
		}

	    }
	    catch (DocerException e) {
		System.out.println("ERRORE: " + e.getMessage());
	    }

	    if (token != null) {
		provider.logout();
	    }
	}
	catch (Exception e) {
	    System.out.println("error: " + e);
	}
    }

    @Test
    public void testGetUser() {

	Provider provider = null;
	try {

	    provider = new Provider();

	    ILoggedUserInfo uinfo = new LoggedUserInfo();
	    String ticket = provider.login("admin", "admin", null);

	    uinfo.setUserId("admin");
	    uinfo.setCodiceEnte(null);
	    uinfo.setTicket(ticket);

	    provider.setCurrentUser(uinfo);

	    IUserProfileInfo userProfile1 = new UserProfileInfo();
	    userProfile1.setUserId("test1");
	    userProfile1.setUserPassword("test1");
	    userProfile1.setFirstName("test1 first name");
	    userProfile1.setLastName("test1 last name");
	    userProfile1.setFullName("test1 full name");
	    userProfile1.setFullName("test1 full name");

	    try {
		provider.createUser(userProfile1);
	    }
	    catch (Exception e1) {
		System.out.println(e1.getMessage());
	    }

	    IUserInfo userinfo = provider.getUser("test1");

	    if (userinfo == null) {
		throw new Exception("utente test1 non trovato");
	    }
	    System.out.println("------------------");
	    System.out.println(userinfo.getProfileInfo().getUserId());
	    System.out.println(userinfo.getProfileInfo().getEmailAddress());
	    System.out.println(userinfo.getProfileInfo().getFirstName());
	    System.out.println(userinfo.getProfileInfo().getLastName());
	    System.out.println(userinfo.getProfileInfo().getFullName());
	    System.out.println(userinfo.getProfileInfo().getUserPassword());
	    for (String key : userinfo.getProfileInfo().getExtraInfo().keySet()) {
		System.out.println(key + "=" + userinfo.getProfileInfo().getExtraInfo().get(key));
	    }

	    System.out.println("------------------");

	    IUserProfileInfo userProfile2 = new UserProfileInfo();
	    userProfile2.setUserId("test2");
	    userProfile2.setUserPassword("test2");
	    userProfile2.setFirstName("test2 first name");
	    userProfile2.setLastName("test2 last name");
	    userProfile2.setFullName("test2 full name");

	    try {
		provider.createUser(userProfile2);
	    }
	    catch (Exception e1) {
		System.out.println(e1.getMessage());
	    }
	    userinfo = provider.getUser("test2");

	    if (userinfo == null) {
		throw new Exception("utente test2 non trovato");
	    }
	    System.out.println("------------------");
	    System.out.println(userinfo.getProfileInfo().getUserId());
	    System.out.println(userinfo.getProfileInfo().getEmailAddress());
	    System.out.println(userinfo.getProfileInfo().getFirstName());
	    System.out.println(userinfo.getProfileInfo().getLastName());
	    System.out.println(userinfo.getProfileInfo().getFullName());
	    System.out.println(userinfo.getProfileInfo().getUserPassword());
	    for (String key : userinfo.getProfileInfo().getExtraInfo().keySet()) {
		System.out.println(key + "=" + userinfo.getProfileInfo().getExtraInfo().get(key));
	    }

	    assertTrue(true);
	}
	catch (Exception e) {

	    System.out.println(e.getMessage());
	    assertTrue(false);
	}

    }

    @Test
    public void testUsers() {

	Provider provider = null;
	try {

	    provider = new Provider();

	    ILoggedUserInfo uinfo = new LoggedUserInfo();
	    String ticket = provider.login("admin", "admin", null);

	    uinfo.setUserId("admin");
	    uinfo.setCodiceEnte(null);
	    uinfo.setTicket(ticket);

	    provider.setCurrentUser(uinfo);

	    IUserProfileInfo userProfile1 = new UserProfileInfo();
	    userProfile1.setUserId("test1");
	    userProfile1.setUserPassword("test1");
	    userProfile1.setFirstName("test1 first name");
	    userProfile1.setLastName("test1 last name");
	    userProfile1.setFullName("test1 full name");
	    userProfile1.getExtraInfo().put("COD_ENTE", "ENTEPROVA");
	    userProfile1.getExtraInfo().put("DES_ENTE", "des ente prova");

	    IUserInfo userinfo = null;

	    try {
		userinfo = provider.getUser(userProfile1.getUserId());
	    }
	    catch (Exception e1) {

		System.out.println(e1.getMessage());
	    }

	    if (userinfo == null) {
		provider.createUser(userProfile1);
	    }

	    userinfo = provider.getUser("test1");

	    if (userinfo == null) {
		throw new Exception("utente test1 non trovato");
	    }
	    System.out.println("------------------");
	    System.out.println("USER_ID: " + userinfo.getProfileInfo().getUserId());
	    System.out.println("EMAIL_ADDRESS: " + userinfo.getProfileInfo().getEmailAddress());
	    System.out.println("FIRST_NAME: " + userinfo.getProfileInfo().getFirstName());
	    System.out.println("LAST_NAME: " + userinfo.getProfileInfo().getLastName());
	    System.out.println("FULL_NAME: " + userinfo.getProfileInfo().getFullName());
	    System.out.println("USER_PASSWORD: " + userinfo.getProfileInfo().getUserPassword());
	    for (String key : userinfo.getProfileInfo().getExtraInfo().keySet()) {
		System.out.println(key + "=" + userinfo.getProfileInfo().getExtraInfo().get(key));
	    }

	    System.out.println("------------------");

	    IUserProfileInfo userProfile2 = new UserProfileInfo();
	    userProfile2.setUserId("test2");
	    userProfile2.setUserPassword("test2");
	    userProfile2.setFirstName("test2 first name");
	    userProfile2.setLastName("test2 last name");
	    userProfile2.setFullName("test2 full name");

	    try {
		provider.createUser(userProfile2);
	    }
	    catch (Exception e1) {
		System.out.println(e1.getMessage());
	    }
	    userinfo = provider.getUser("test2");

	    if (userinfo == null) {
		throw new Exception("utente test2 non trovato");
	    }
	    System.out.println("------------------");
	    System.out.println(userinfo.getProfileInfo().getUserId());
	    System.out.println(userinfo.getProfileInfo().getEmailAddress());
	    System.out.println(userinfo.getProfileInfo().getFirstName());
	    System.out.println(userinfo.getProfileInfo().getLastName());
	    System.out.println(userinfo.getProfileInfo().getFullName());
	    System.out.println(userinfo.getProfileInfo().getUserPassword());
	    for (String key : userinfo.getProfileInfo().getExtraInfo().keySet()) {
		System.out.println(key + "=" + userinfo.getProfileInfo().getExtraInfo().get(key));
	    }

	    assertTrue(true);
	}
	catch (Exception e) {

	    System.out.println(e.getMessage());
	    assertTrue(false);
	}

    }

    @Test
    public void testCreateGroup() {

	Provider provider = null;

	try {

	    provider = new Provider();

	    ILoggedUserInfo uinfo = new LoggedUserInfo();
	    String ticket = provider.login("admin", "admin", null);

	    uinfo.setUserId("admin");
	    uinfo.setCodiceEnte(null);
	    uinfo.setTicket(ticket);

	    provider.setCurrentUser(uinfo);

	    IGroupProfileInfo gpinfo = new GroupProfileInfo();
	    gpinfo.setGroupId("G1");
	    gpinfo.setGroupName("group name G1");
	    gpinfo.setParentGroupId("");
	    gpinfo.getExtraInfo().put("GRUPPO_STRUTTURA", "false");
	    gpinfo.getExtraInfo().put("DES_ENTE", "test des ente");
	    try {
		provider.createGroup(gpinfo);
	    }
	    catch (Exception e1) {
		System.out.println(e1.getMessage());
	    }
	    IGroupInfo groupinfo = provider.getGroup("G1");

	    if (groupinfo == null) {
		throw new Exception("gruppo G1 non trovato");
	    }

	    gpinfo = groupinfo.getProfileInfo();
	    System.out.println("-----------------");
	    System.out.println("group_id: " + gpinfo.getGroupId());
	    System.out.println("group_name: " + gpinfo.getGroupName());
	    System.out.println("parent_group_id: " + gpinfo.getParentGroupId());
	    System.out.println("enabled: " + gpinfo.getEnabled());
	    for (String key : gpinfo.getExtraInfo().keySet()) {
		System.out.println(key + "=" + gpinfo.getExtraInfo().get(key));
	    }

	    gpinfo = new GroupProfileInfo();
	    gpinfo.setGroupId("G2");
	    gpinfo.setGroupName("group name G2");
	    gpinfo.setParentGroupId("G1");
	    gpinfo.getExtraInfo().put("GRUPPO_STRUTTURA", "false");
	    try {
		provider.createGroup(gpinfo);
	    }
	    catch (Exception e1) {
		System.out.println(e1.getMessage());
	    }
	    groupinfo = provider.getGroup("G2");

	    if (groupinfo == null) {
		throw new Exception("gruppo G2 non trovato");
	    }

	    gpinfo = groupinfo.getProfileInfo();
	    System.out.println("-----------------");
	    System.out.println("group_id: " + gpinfo.getGroupId());
	    System.out.println("group_name: " + gpinfo.getGroupName());
	    System.out.println("parent_group_id: " + gpinfo.getParentGroupId());
	    System.out.println("enabled: " + gpinfo.getEnabled());
	    for (String key : gpinfo.getExtraInfo().keySet()) {
		System.out.println(key + "=" + gpinfo.getExtraInfo().get(key));
	    }

	    gpinfo = new GroupProfileInfo();
	    gpinfo.setGroupId("GS1");
	    gpinfo.setGroupName("group name GS1");
	    gpinfo.setParentGroupId("");
	    gpinfo.getExtraInfo().put("GRUPPO_STRUTTURA", "true");
	    try {
		provider.createGroup(gpinfo);
	    }
	    catch (Exception e1) {
		System.out.println(e1.getMessage());
	    }
	    groupinfo = provider.getGroup("GS1");

	    if (groupinfo == null) {
		throw new Exception("gruppo GS1 non trovato");
	    }

	    gpinfo = groupinfo.getProfileInfo();
	    System.out.println("-----------------");
	    System.out.println("group_id: " + gpinfo.getGroupId());
	    System.out.println("group_name: " + gpinfo.getGroupName());
	    System.out.println("parent_group_id: " + gpinfo.getParentGroupId());
	    System.out.println("enabled: " + gpinfo.getEnabled());
	    for (String key : gpinfo.getExtraInfo().keySet()) {
		System.out.println(key + "=" + gpinfo.getExtraInfo().get(key));
	    }

	    gpinfo = new GroupProfileInfo();
	    gpinfo.setGroupId("GS2");
	    gpinfo.setGroupName("group name GS2");
	    gpinfo.setParentGroupId("GS1");
	    gpinfo.getExtraInfo().put("GRUPPO_STRUTTURA", "true");
	    try {
		provider.createGroup(gpinfo);
	    }
	    catch (Exception e1) {
		System.out.println(e1.getMessage());
	    }
	    groupinfo = provider.getGroup("GS2");

	    if (groupinfo == null) {
		throw new Exception("gruppo GS2 non trovato");
	    }

	    gpinfo = groupinfo.getProfileInfo();
	    System.out.println("-----------------");
	    System.out.println("group_id: " + gpinfo.getGroupId());
	    System.out.println("group_name: " + gpinfo.getGroupName());
	    System.out.println("parent_group_id: " + gpinfo.getParentGroupId());
	    System.out.println("enabled: " + gpinfo.getEnabled());
	    for (String key : gpinfo.getExtraInfo().keySet()) {
		System.out.println(key + "=" + gpinfo.getExtraInfo().get(key));
	    }

	    assertTrue(true);
	}
	catch (Exception e) {

	    System.out.println(e.getMessage());
	    assertTrue(false);
	}
	finally {
	    try {
		provider.logout();
	    }
	    catch (DocerException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}

    }

    @Test
    public void testAddUsersToGroup() {

	Provider provider = null;

	try {

	    provider = new Provider();

	    ILoggedUserInfo uinfo = new LoggedUserInfo();
	    String ticket = provider.login("admin", "admin", null);

	    uinfo.setUserId("admin");
	    uinfo.setCodiceEnte(null);
	    uinfo.setTicket(ticket);

	    provider.setCurrentUser(uinfo);

	    provider.addUsersToGroup("G1", Arrays.asList("test1", "test2"));

	    System.out.println("UTENTI del gruppo G1");

	    IGroupInfo groupinfo = provider.getGroup("G1");
	    for (String userid : groupinfo.getUsers()) {
		System.out.println("utente: " + userid);
	    }

	}
	catch (Exception e) {

	    System.out.println(e.getMessage());
	    assertTrue(false);
	}
	finally {
	    try {
		provider.logout();
	    }
	    catch (DocerException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}

    }

    @Test
    public void testSetACLDocument() {

	Provider provider = null;

	try {

	    provider = new Provider();

	    ILoggedUserInfo uinfo = new LoggedUserInfo();
	    String ticket = provider.login("admin", "admin", null);

	    uinfo.setUserId("admin");
	    uinfo.setCodiceEnte(null);
	    uinfo.setTicket(ticket);

	    provider.setCurrentUser(uinfo);

	    Map<String, EnumACLRights> acls = new HashMap<String, EnumACLRights>();
	    // acls.put("ADMINS_ENTE_ENTE8", EnumACLRights.normalAccess);
	    // acls.put("ADMINS_AOO_ENTE8-AOO11", EnumACLRights.readOnly);
	    // acls.put("SYS_ADMINS", EnumACLRights.readOnly);
	    // acls.put("luca.biasin", EnumACLRights.readOnly);
	    // acls.put("EMR", EnumACLRights.fullAccess);
	    acls.put("test1", EnumACLRights.fullAccess);
	    acls.put("G1", EnumACLRights.readOnly);

	    provider.setACLDocument("1319", acls);

	    System.out.println("done");
	}
	catch (Exception e) {

	    System.out.println(e.getMessage());
	    assertTrue(false);
	}
	finally {
	    try {
		provider.logout();
	    }
	    catch (DocerException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}

    }

    @Test
    public void testSetACLTitolario() {

	Provider provider = null;
	try {

	    provider = new Provider();

	    ILoggedUserInfo uinfo = new LoggedUserInfo();
	    String ticket = provider.login("admin", "admin", null);

	    uinfo.setUserId("admin");
	    uinfo.setCodiceEnte(null);
	    uinfo.setTicket(ticket);

	    provider.setCurrentUser(uinfo);

	    TitolarioId titolarioId = new TitolarioId();
	    titolarioId.setCodiceEnte("ENTETEST");
	    titolarioId.setCodiceAOO("AOOTEST");
	    titolarioId.setClassifica("CLASSIFICATEST");

	    Map<String, EnumACLRights> acls = new HashMap<String, EnumACLRights>();
	    acls.put("test1", EnumACLRights.normalAccess);
	    acls.put("test2", EnumACLRights.readOnly);
	    acls.put("G1", EnumACLRights.readOnly);
	    acls.put("GS2", EnumACLRights.readOnly);
	    provider.setACLTitolario(titolarioId, acls);

	    acls = provider.getACLTitolario(titolarioId);
	    System.out.println("ACL titolario:");
	    for (String userOrGroup : acls.keySet()) {
		System.out.println(userOrGroup + "=" + acls.get(userOrGroup));
	    }

	}
	catch (Exception e) {

	    System.out.println(e.getMessage());
	    assertTrue(false);
	}
	finally {
	    try {
		provider.logout();
	    }
	    catch (DocerException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}

    }

    @Test
    public void testSetACLFascicolo() {

	Provider provider = null;
	try {

	    provider = new Provider();

	    ILoggedUserInfo uinfo = new LoggedUserInfo();
	    String ticket = provider.login("admin", "admin", null);

	    uinfo.setUserId("admin");
	    uinfo.setCodiceEnte(null);
	    uinfo.setTicket(ticket);

	    provider.setCurrentUser(uinfo);

	    FascicoloId fascicoloId = new FascicoloId();
	    fascicoloId.setCodiceEnte("ENTETEST");
	    fascicoloId.setCodiceAOO("AOOTEST");
	    fascicoloId.setClassifica("CLASSIFICATEST");
	    fascicoloId.setAnnoFascicolo("2014");
	    fascicoloId.setProgressivo("FASCICOLOTEST");

	    Map<String, EnumACLRights> acls = new HashMap<String, EnumACLRights>();
	    acls.put("G1", EnumACLRights.readOnly);
	    acls.put("G2", EnumACLRights.normalAccess);
	    acls.put("GS1", EnumACLRights.fullAccess);
	    acls.put("test1", EnumACLRights.normalAccess);
	    acls.put("test2", EnumACLRights.fullAccess);

	    try {
		provider.setACLFascicolo(fascicoloId, acls);
		System.out.println("ok 1");
	    }
	    catch (Exception e) {
		System.out.println(e.getMessage());
		System.out.println("KO 1");
	    }

	    acls = provider.getACLFascicolo(fascicoloId);

	    for (String userOrGroup : acls.keySet()) {
		System.out.println(userOrGroup + "=" + acls.get(userOrGroup));
	    }

	    // try {
	    // provider.setACLFascicolo(fascicoloId, acls);
	    // System.out.println("ok 1");
	    // }
	    // catch (Exception e) {
	    // System.out.println(e.getMessage());
	    // System.out.println("KO 1");
	    // }
	    //
	    // acls.clear();
	    //
	    // fascicoloId.setProgressivo("38");
	    //
	    // //acls.put("SYS_ADMINS", EnumACLRights.readOnly);
	    // acls.put("AOOPROVA", EnumACLRights.readOnly);
	    //
	    // try {
	    //
	    // provider.setACLFascicolo(fascicoloId, acls);
	    // System.out.println("ok 2");
	    // }
	    // catch (Exception e) {
	    // System.out.println(e.getMessage());
	    // System.out.println("KO 2");
	    // }

	}
	catch (Exception e) {

	    System.out.println(e.getMessage());
	    assertTrue(false);
	}
	finally {
	    try {
		if (provider != null)
		    provider.logout();
	    }
	    catch (DocerException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}

    }

    // @Test
    // public void testUsers() {
    //
    //
    // Provider provider = null;
    // try {
    //
    // provider = new Provider();
    //
    // ILoggedUserInfo uinfo = new LoggedUserInfo();
    // String ticket = provider.login("admin", "admin", null);
    //
    // uinfo.setUserId("admin");
    // uinfo.setCodiceEnte(null);
    // uinfo.setTicket(ticket);
    //
    // provider.setCurrentUser(uinfo);
    //
    // Map<String, String> info = new HashMap<String, String>();
    // info.put("COD_ENTE", "ENTETEST");
    // info.put("COD_AOO", "AOOTEST");
    // info.put("DOCNAME", "test.txt");
    // info.put("TYPE_ID", "documento");
    // // info.put("CREATION_DATE", "2007-03-02T15:10:43.359+01:00");
    //
    // String docid_1 = provider.createUser(userProfile)K
    // System.out.println("creato " + docid_1);
    //
    // filePath = "C:\\error.log";
    //
    // fileInputStream = null;
    //
    // file = new File(filePath);
    // try {
    // fileInputStream = new FileInputStream(file);
    // }
    // catch (FileNotFoundException e1) {
    //
    // System.out.println(e1);
    // return;
    // }
    //
    //
    // info.clear();
    // info.put("COD_ENTE", "ENTETEST");
    // info.put("COD_AOO", "AOOTEST");
    // info.put("DOCNAME", "test2.txt");
    // info.put("TYPE_ID", "documento");
    // // info.put("CREATION_DATE", "2007-03-02T15:10:43.359+01:00");
    //
    // String docid_2 = provider.createDocument("documento", info,
    // fileInputStream);
    // System.out.println("creato " + docid_2);
    //
    //
    // provider.addNewAdvancedVersion(docid_1, docid_2);
    //
    // List<String> versions = provider.getAdvancedVersions(docid_1);
    // for(String v : versions){
    // System.out.println("versione avanzata di: " +docid_1 +": "+v);
    // }
    //
    // }
    // catch (Exception e) {
    //
    // try {
    // if (fileInputStream != null)
    // fileInputStream.close();
    // }
    // catch (Exception e2) {
    // }
    //
    // System.out.println(e.getMessage());
    // // assertTrue(false);
    // }
    //
    // }

    @Test
    public void testFascicolaDocumento() {

	Provider provider = null;
	try {

	    provider = new Provider();

	    ILoggedUserInfo uinfo = new LoggedUserInfo();
	    String ticket = provider.login("admin", "admin", null);

	    uinfo.setUserId("admin");
	    uinfo.setCodiceEnte(null);
	    uinfo.setTicket(ticket);

	    provider.setCurrentUser(uinfo);

	    String docnum = "101";
	    Map<String, String> info = new HashMap<String, String>();
	    info.put("COD_ENTE", "ENTETEST");
	    info.put("COD_AOO", "AOOTEST");
	    info.put("CLASSIFICA", "CLASSIFICATEST");
	    info.put("ANNO_FASCICOLO", "2014");
	    info.put("PROGR_FASCICOLO", "FASCICOLOTEST");
	    // info.put("CREATION_DATE", "2007-03-02T15:10:43.359+01:00");

	    provider.updateProfileDocument(docnum, info);
	    System.out.println("aggiornato docnum " + docnum);

	    Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();

	    searchCriteria.put("DOCNUM", Arrays.asList(docnum));

	    List<String> returnProperties = Arrays.asList("content_size", "docnum", "stato_pantarei", "stato_business", "docname", "abstract", "default_extension", "type_id", "cod_ente", "cod_aoo",
		    "classifica", "anno_fascicolo", "progr_fascicolo", "cod_titolario", "num_fascicolo", "anno_pg", "num_pg", "oggetto_pg", "registro_pg", "data_pg", "author_id", "typist_id",
		    "creation_date", "fasc_secondari", "d_registraz", "n_registraz", "a_registraz", "o_registraz", "id_registro", "stato_conserv", "usa_d_co_cer", "t_d_contr_cer", "d_co_cer",
		    "forza_coll", "forza_accettaz", "forza_conserv", "flag_conserv", "t_conserv", "doc_url", "archive_type", "app_versante", "ms_t_registraz", "tipo_componente", "docnum_princ",
		    "registro_pub", "pubblicato", "numero_pub", "anno_pub", "oggetto_pub", "data_inizio_pub", "data_fine_pub", "stato_archivistico", "mittenti", "destinatari", "tipo_protocollazione",
		    "tipo_firma", "firmatario", "docnum_record", "ud_version", "doc_hash", "num_pg_mittente", "data_pg_mittente", "cod_ente_mittente", "cod_aoo_mittente", "classifica_mittente",
		    "fascicolo_mittente", "annullato_pg", "d_annull_pg", "m_annull_pg", "annull_registraz", "d_annull_registraz", "m_annull_registraz");

	    // Map<String, EnumSearchOrder> orderby = new HashMap<String,
	    // EnumSearchOrder>();
	    // orderby.put("DOCNUM", EnumSearchOrder.DESC);
	    DataTable<?> results = provider.searchDocuments(searchCriteria, null, returnProperties, -1, null);

	    System.out.println("TROVATI: " + results.getRows().size() + " risultati");
	    for (int index = 0; index < results.getRows().size(); index++) {

		DataRow<?> row = results.getRow(index);
		System.out.println("row " + index + ":");
		for (String nomeColonna : results.getColumnNames()) {

		    System.out.println(nomeColonna + " = " + row.get(nomeColonna));
		}
		System.out.println("   --------- ");
	    }

	    assertTrue(true);
	}
	catch (Exception e) {

	    System.out.println(e.getMessage());
	    assertTrue(false);
	}

    }

    @Test
    public void testCustomAnagrafica() {

	Provider provider = null;
	try {

	    provider = new Provider();

	    ILoggedUserInfo uinfo = new LoggedUserInfo();
	    String ticket = provider.login("admin", "Kdm.2001", null);

	    uinfo.setUserId("admin");
	    uinfo.setCodiceEnte(null);
	    uinfo.setTicket(ticket);

	    provider.setCurrentUser(uinfo);

	    CustomItemInfo customItemInfo = new CustomItemInfo();
	    customItemInfo.setCodiceEnte("ENTE_DATAGRAPH");
	    customItemInfo.setCodiceAOO("AOO_DATAGRAPH");
	    customItemInfo.setCodiceCustom("TEST_KDM");
	    customItemInfo.setDescrizione("des TEST_KDM");
	    customItemInfo.setType("LISTA_REGISTRI");

	    provider.createCustomItem(customItemInfo);

	    Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
	    searchCriteria.put("COD_ENTE", Arrays.asList("ENTE_DATAGRAPH"));
	    searchCriteria.put("COD_AOO", Arrays.asList("AOO_DATAGRAPH"));
	    searchCriteria.put("COD_REGISTRO", Arrays.asList("TEST_KDM"));

	    List<Map<String, String>> searchResult = provider.searchAnagrafiche("LISTA_REGISTRI", searchCriteria, Arrays.asList("COD_ENTE", "DES_ENTE", "ENABLED"));

	    if (searchResult.size() != 1) {
		throw new Exception("trovati " + searchResult.size() + " LISTA_REGISTRI");
	    }

	    for (String p : searchResult.get(0).keySet()) {
		System.out.println(p + "=" + searchResult.get(0).get(p));
	    }

	    assertTrue(true);
	}
	catch (Exception e) {

	    System.out.println(e.getMessage());
	    assertTrue(false);
	}
	finally {
	    try {
		provider.logout();
	    }
	    catch (DocerException e) {
	    }
	}

    }

}
