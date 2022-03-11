package it.kdm.docer.alfresco.provider;

import static org.junit.Assert.*;
import it.kdm.docer.alfresco.search.DocId;
import it.kdm.docer.sdk.Constants;
import it.kdm.docer.sdk.EnumACLRights;
import it.kdm.docer.sdk.EnumSearchOrder;
import it.kdm.docer.sdk.IProvider;
import it.kdm.docer.sdk.classes.AOOInfo;
import it.kdm.docer.sdk.classes.EnteInfo;
import it.kdm.docer.sdk.classes.FascicoloId;
import it.kdm.docer.sdk.classes.FascicoloInfo;
import it.kdm.docer.sdk.classes.FolderInfo;
import it.kdm.docer.sdk.classes.GroupProfileInfo;
import it.kdm.docer.sdk.classes.KeyValuePair;
import it.kdm.docer.sdk.classes.LoggedUserInfo;
import it.kdm.docer.sdk.classes.TitolarioId;
import it.kdm.docer.sdk.classes.UserInfo;
import it.kdm.docer.sdk.classes.UserProfileInfo;
import it.kdm.docer.sdk.exceptions.DocerException;
import it.kdm.docer.sdk.frontend.utils.UserInfoCache;
import it.kdm.docer.sdk.interfaces.IAOOId;
import it.kdm.docer.sdk.interfaces.IAOOInfo;
import it.kdm.docer.sdk.interfaces.ICustomItemId;
import it.kdm.docer.sdk.interfaces.ICustomItemInfo;
import it.kdm.docer.sdk.interfaces.IEnteId;
import it.kdm.docer.sdk.interfaces.IEnteInfo;
import it.kdm.docer.sdk.interfaces.IFascicoloId;
import it.kdm.docer.sdk.interfaces.IFascicoloInfo;
import it.kdm.docer.sdk.interfaces.IFolderInfo;
import it.kdm.docer.sdk.interfaces.IGroupInfo;
import it.kdm.docer.sdk.interfaces.IGroupProfileInfo;
import it.kdm.docer.sdk.interfaces.IHistoryItem;
import it.kdm.docer.sdk.interfaces.ILockStatus;
import it.kdm.docer.sdk.interfaces.ILoggedUserInfo;
//import it.kdm.docer.sdk.interfaces.ISearchItem;
import it.kdm.docer.sdk.interfaces.ITitolarioId;
import it.kdm.docer.sdk.interfaces.ITitolarioInfo;
import it.kdm.docer.sdk.interfaces.IUserInfo;
import it.kdm.docer.sdk.interfaces.IUserProfileInfo;
import it.kdm.utils.DataRow;
import it.kdm.utils.DataTable;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.apache.log4j.helpers.ISO8601DateFormat;
import org.junit.Test;

public class TestProvider {

    static String userId = "admin";
    static String password = "admin";
    static String ente = "EMR";
    static String aoo = "AOO_EMR";
    static ILoggedUserInfo admin = null;
    static ILoggedUserInfo normal = null;
    Provider provider = null;
    static String docid_1 = "";

    
    @Test
    public void testSplit(){


	String str = "123.123.123.112";
	String[] strarr = str.split(":");
	System.out.println(strarr.length);
	System.out.println(strarr[0]);
	System.out.println(strarr[1]);

    }

    
    @Test
    public void testTime(){
	long startTime = System.nanoTime();

String a = "".replaceAll(".+", "");
	long endTime = System.nanoTime();
	double duration = (double)(endTime - startTime) / (Math.pow(10, 9));
	System.out.println("MethodName time (s) = " + duration);
    }
    
    @Test
    public void testName(){

//	String name = "test\"\"*<<\\\\\\\\***<<>?>?|::||.xml";
//
//	System.out.println(name);
//
//	name = name.trim();
//	name = name.replaceAll("[\"*\\\\><?/:|]", "");
//	
//	System.out.println(name);
//	//name = name.replaceAll("[\"\*\\\>\<\?\/\:\|]", "");

	String a = null;
	
	System.out.println((String) null);
    }
    
    @Test
    public void testInit() throws Exception{
	

	System.out.println(Boolean.valueOf(null));
	System.out.println(Boolean.valueOf("6"));
	System.out.println(Boolean.valueOf("#"));
	System.out.println(Boolean.valueOf("FALSE"));
	System.out.println(Boolean.valueOf("tRuE"));
	
//	Provider p = new Provider();
//	
//	p = new Provider();
//	
//	p = new Provider();
//	
//	p = new Provider();
    }
    
    private static final String CM_QNAME = "{http://www.alfresco.org/model/content/1.0}";
    private static final String SYS_QNAME = "{http://www.alfresco.org/model/system/1.0}";
    private static final String APP_QNAME = "{http://www.alfresco.org/model/application/1.0}";
    private static final String DOCAREA_QNAME = "{http://www.docarea.it/model/content/1.0}}";
    private static final String CM = "cm:";
    private static final String SYS = "sys:";
    private static final String APP = "app:";
    private static final String DOCAREA = "da:";
    
    
    @Test
    public void testMapAreEquals(){
	
	Map<String,String> map1 = new HashMap<String, String>();
	map1.put("b", "b");
	map1.put("a", "1");
	
	
	Map<String,String> map2 = new HashMap<String, String>();
	map2.put("a", "1");
	map2.put("b", "B");
	map2.put("c", "B");
	
	System.out.println("map1: " +map1.toString());
	System.out.println("map2: " +map2.toString());
	
	if(map1.size()!=map2.size()){
	    System.out.println(false);
	    return;
	}
	
	for(String key1 : map1.keySet()){
	    
	    if(!map2.containsKey(key1)){
		System.out.println(false);
		return;
	    }
	    
	    String value1 = String.valueOf(map1.get(key1));
	    String value2 = String.valueOf(map2.get(key1));
		
	    if(!value1.equals(value2)){
		System.out.println(false);
		return;
	    }
	    
	}
	
	System.out.println(true);
	
//	for(String key2 : map2.keySet()){
//	    
//	    if(!map2.containsKey(key1)){
//		System.out.println(false);
//		return;
//	    }
//	    
//	    if(map2.containsKey(key1)){
//		String value1 = String.valueOf(map1.get(key1));
//		String value2 = String.valueOf(map2.get(key1));
//		
//		if(!value1.equals(value2)){
//		    System.out.println(false);
//		    return;
//		}
//	    }
//	}
    
    }
    
    
    @Test
    public void testReplaceNamespace(){
	String qnameString = "/app:company_home/cm:DOCAREA/cm:EMR/cm:AOO_EMR/cm:DOCUMENTI/cm:1381663142639/cm:figlio";
	long start = new Date().getTime();
	
	String name = qnameString.replace(DOCAREA_QNAME, DOCAREA);
	if(name.length()==qnameString.length()){
	    name = qnameString.replace(CM_QNAME, CM);
	    if(name.length()==qnameString.length()){
		name = qnameString.replace(SYS_QNAME, SYS);
		if(name.length()==qnameString.length()){
		    name = qnameString.replace(APP_QNAME, APP);
		}
	    }
	}
	long end = new Date().getTime();
	System.out.println(qnameString +" --> " +name +": " +(end-start) +" msec");
	
	
	start = new Date().getTime();
	name = qnameString.replace(CM_QNAME, CM).replace(SYS_QNAME, SYS).replace(APP_QNAME, APP).replace(DOCAREA_QNAME, DOCAREA);
	end = new Date().getTime();
	System.out.println(qnameString +" --> " +name +": " +(end-start) +" msec");
	
	
	
	
	
    }
    @Test
    public void testDigest() throws Exception{
	

	    File f = new File("src/test/resources/alfresco_provider_config.xml");
	    
	    if (!f.exists()) {
		throw new Exception("file non trovato: " + f.getAbsolutePath());
	    }

	    long start = new Date().getTime();
	    long lastModified = f.lastModified();
	    long end = new Date().getTime();
	    
	    
	    System.out.println("lastModified: "+lastModified +": " +(end-start) +" msec");
		    
	    
	    start = new Date().getTime();
	    byte[] alfrescoProviderConfig = FileUtils.readFileToByteArray(f);

	    String newDigest = DigestUtils.md5Hex(alfrescoProviderConfig);

	    end = new Date().getTime();
	    
	    System.out.println("digest: " +newDigest +": " +(end-start) +" msec");	 	    
	    
    }
    @Test
    public void testDate() {

	String data_registrazione = "1940-02-10";
	String anno_pg = "";

	if (data_registrazione.matches("^[0-9]{4}[/-].+")) {
	    // formato yyyy/mm/dd
	    anno_pg = data_registrazione.replaceAll("[/-].+$", "");
	}
	else { // formato dd/mm/yyyy
	    anno_pg = data_registrazione.replaceAll("^.+[/-]", "");
	}

	System.out.println(anno_pg);
    }

    @Test
    public void testSetInherits() {

	try {
	    provider = new Provider();
	    ILoggedUserInfo uinfo = new LoggedUserInfo();
	    String ticket = provider.login("admin", "admin", null);
System.out.println(ticket);
	    uinfo.setUserId("admin");
	    uinfo.setCodiceEnte(null);
	    uinfo.setTicket(ticket);

	    provider.setCurrentUser(uinfo);

	    //provider.testSetInherits("114720", false);
	}
	catch (DocerException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

    }

    @Test
    public void testConsole() {

	System.out.println(this.getClass().getCanonicalName());
	System.out.println(this.getClass().getSimpleName());
	System.out.println(this.getClass().getName());

	long queryTime = 3900 - 3123;
	System.out.println((int) Math.ceil(queryTime / 1000));

	Pattern patternEnteFromToken = Pattern.compile(".*\\|ente\\:(EMR)\\|.*");

	String ente_login_selezionato = null;

	Matcher matcher = patternEnteFromToken.matcher("|ente:EMR|");
	if (matcher.matches()) {
	    System.out.println("matches");
	    System.out.println(matcher.group(0));
	    ente_login_selezionato = matcher.group(1);
	    System.out.println(ente_login_selezionato);
	}

	String ente = ente_login_selezionato;
	String group = "GRUPPO_CONSERV_EMR_AOO_EMR";
	int start = ("GRUPPO_CONSERV_" + ente).length();
	String aoo = group.substring(start + 1, group.length());

	System.out.println(aoo);

	// Integer.parseInt("0");
	// String sentence =
	// "</destinatario><DESTINATARIO><DESTINATARIO />< /DESTINATARI></DESTINATARI/></FIRMAtari><FIRMAtario><MiTTentI><mittenti> </mittentI>";
	// String result =
	// sentence.replaceAll("(?i)\\<[ /]*((mittenti)|(destinatari)|(firmatario))[ /]*\\>",
	// "...");
	// System.out.println("Input: " + sentence);
	// System.out.println("Output: " + result);
	//
	// Map<String, List<String>> map = new HashMap<String, List<String>>();

	// List<String> list = new ArrayList<String>(map.keySet());
	// for (String l : list)
	// System.out.println(l);
	//

	// List<String> list = Arrays.asList("a","b","c","d");
	// System.out.println(list.toString().replaceAll("^\\[",
	// "").replaceAll("\\]$", ""));

	//
	// Pattern conservRegex =
	// Pattern.compile("GRUPPO_CONSERV\\[([^\\]]+)\\]\\[([^\\]]+)\\]");
	//
	// String group = "GRUPPO_CONSERV[EMR][AOO_EMR]";
	//
	// Matcher matcher = conservRegex.matcher(group.trim());
	// if (matcher.matches()) {
	// System.out.println(matcher.group(0));
	// System.out.println(matcher.group(1));
	// System.out.println(matcher.group(2));
	// }
	//

    }

    @Test
    public void testAll() {
	testLoginAdmin();

	// long min = 100000;
	// long max = 0;
	//
	// long mm = 60000;
	// long hh = 60000 * 60;
	//
	// long startTime = new Date().getTime();
	// long end = startTime;
	// long total = 0;
	// long start = 0;
	// long time = 0;
	// List<Map<String,String>> res = null;
	//
	// while( total < (120*mm)){
	// start = new Date().getTime();
	// testCreateDocument();
	// end = new Date().getTime();
	// time = end-start;
	// total = end - startTime;
	// if(time<min){
	// min = time;
	// }
	// if(time>max){
	// max = time;
	// }
	// System.out.println("creato docnum: " +docid_1 +", tempo: " +time
	// +". Min: " +min +". Max: " +max);
	// }

	// testCreateDocument();
	// testAddToFolderDocuments();

	testLogoutAdmin();
    }

    @Test
    public void testCycle() {
	testLoginAdmin();

	try {
	    Provider provider = new Provider();
	    String token = provider.login("admin", "admin", "");

	    ILoggedUserInfo lui = new LoggedUserInfo("admin", "", token);

	    provider.setCurrentUser(lui);

	    long minCreate = 100000;
	    long maxCreate = 0;

	    long minSearch = 100000;
	    long maxSearch = 0;

	    long mm = 60000;
	    long hh = 60000 * 60;

	    long totalStart = 0;
	    long totalEnd = 0;

	    long totalTime = 0;

	    long lastTimeCreate = 0;
	    long lastTimeSearch = 0;

	    Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
	    List<String> returnProperties = Arrays.asList("DOCNUM");

	    String up = "^";
	    String down = "v";
	    String upOrDownCreate = up;
	    String upOrDownSearch = up;

	    totalStart = new Date().getTime();
	    for (int count = 0; count < 1000; count++) {

		// try{
		// start = new Date().getTime();
		// provider.searchAnagrafiche("ENTE", searchCriteria,
		// returnProperties);
		// end = new Date().getTime();
		// time = end-start;
		// total = end - startTime;
		// if(time<min){
		// min = time;
		// }
		// if(time>max){
		// max = time;
		// }
		// System.out.println("ricerca enti, tempo: " +time +". Min: "
		// +min +". Max: " +max);

		testCreateDocument();

		System.out.println(count);
	    }

	    // while (totalTime < (120 * mm)) {
	    //
	    // totalEnd = new Date().getTime();
	    // totalTime = totalEnd - totalStart;
	    //
	    // // try{
	    // // start = new Date().getTime();
	    // // provider.searchAnagrafiche("ENTE", searchCriteria,
	    // // returnProperties);
	    // // end = new Date().getTime();
	    // // time = end-start;
	    // // total = end - startTime;
	    // // if(time<min){
	    // // min = time;
	    // // }
	    // // if(time>max){
	    // // max = time;
	    // // }
	    // // System.out.println("ricerca enti, tempo: " +time +". Min: "
	    // // +min +". Max: " +max);
	    //
	    // count++;
	    //
	    // long startCreate = new Date().getTime();
	    // testCreateDocument();
	    // long endCreate = new Date().getTime();
	    // long timeCreate = endCreate - startCreate;
	    // if (timeCreate < minCreate) {
	    // minCreate = timeCreate;
	    // }
	    // if (timeCreate > maxCreate) {
	    // maxCreate = timeCreate;
	    // }
	    //
	    // // searchCriteria.put("DOCNUM", Arrays.asList(docid_1));
	    // //
	    // // long startSearch = new Date().getTime();
	    // // provider.searchDocuments(searchCriteria, null,
	    // returnProperties, -1, null);
	    // // long endSearch = new Date().getTime();
	    // // long timeSearch = endSearch - startSearch;
	    // // if (timeSearch < minSearch) {
	    // // minSearch = timeSearch;
	    // // }
	    // // if (timeSearch > maxSearch) {
	    // // maxSearch = timeSearch;
	    // // }
	    // //
	    // // if (count == 50) {
	    // // upOrDownCreate = down;
	    // // if (timeCreate > lastTimeCreate) {
	    // // upOrDownCreate = up;
	    // // }
	    // //
	    // // lastTimeCreate = timeCreate;
	    // // System.out.println("creazione tempo: " + timeCreate +
	    // ". Min: " + minCreate + ". Max: " + maxCreate + " - " +
	    // upOrDownCreate);
	    // //
	    // // upOrDownSearch = down;
	    // // if (timeSearch > lastTimeSearch) {
	    // // upOrDownSearch = up;
	    // // }
	    // // lastTimeSearch = timeSearch;
	    // //
	    // // System.out.println("ricerca - tempo: " + timeSearch +
	    // ". Min: " + minSearch + ". Max: " + maxSearch + " - " +
	    // upOrDownSearch);
	    // //
	    // // count = 0;
	    // // }
	    //
	    // // System.out.println("tempo: " +time +". Min: " +min +". Max: "
	    // // +max);
	    //
	    // // }
	    // // catch(DocerException ex){
	    // // System.out.println(ex.getErrDescription());
	    // // continue;
	    // // }
	    //
	    // }

	}
	catch (Exception e) {
	    System.out.println(e.getMessage());
	}
	finally {
	    testLogoutAdmin();
	}

    }

    @Test
    public void testCreateDocuments() {

	testLoginAdmin();

	testCreateDocument();

	// testCreateDocument();
	// testCreateDocument();
	// testCreateDocument();
	// testCreateDocument();
	// testCreateDocument();

	testLogoutAdmin();
    }

    // @Test
    // public void testWebScriptSearch(){
    //
    //
    // try {
    //
    // testLoginAdmin();
    //
    // String lucenequery =
    // "+TYPE:\"docarea:documento\" +@docarea\\:docnum:\"25342\"";
    //
    // //lucenequery = "+TYPE:\"docarea:documento\"";
    //
    // //lucenequery = "+TYPE:\"cm:folder\" +ASPECT:\"docarea:propsFolder\"";
    //
    // GetDocumentsWebScriptResult res =
    // getProvider(admin).webScriptSearch(false,lucenequery,
    // Arrays.asList("docnum","docname"),-1, "admin", true, true, true, true,
    // null);
    //
    // for(int i=0 ; i<res.getCount(); i++){
    // System.out.println(res.getAclsMap());
    // }
    //
    // }
    // catch(Exception e){
    //
    // System.out.println(e.getMessage());
    // assertTrue(false);
    // }
    // finally{
    // testLogoutAdmin();
    // }
    //
    // }

    @Test
    public void testCreateEnteAoo() {

	try {

	    testLoginAdmin();

//	     EnteInfo enteInfo = new EnteInfo();
//	     enteInfo.setCodiceEnte("ENTEPROVA2");
//	     enteInfo.setDescrizione("ente prova2");
//	     getProvider(admin).createEnte(enteInfo);

	    AOOInfo aooInfo = new AOOInfo();
	    aooInfo.setCodiceEnte("ENTEPROVA");
	    aooInfo.setCodiceAOO("AOOPROVA2");
	    aooInfo.setDescrizione("AOO di Prova 2");
	    getProvider(admin).createAOO(aooInfo);

	}
	catch (Exception e) {

	    System.out.println(e.getMessage());
	    assertTrue(false);
	}
	finally {
	    testLogoutAdmin();
	}

    }

    @Test
    public void testCreateEnteTest() {

	try {

	    provider = new Provider();

	    ILoggedUserInfo uinfo = new LoggedUserInfo();
	    String ticket = provider.login("sysadmin", "sysadmin", null);

	    uinfo.setUserId("sysadmin");
	    uinfo.setCodiceEnte(null);
	    uinfo.setTicket(ticket);

	    provider.setCurrentUser(uinfo);

	    EnteInfo enteInfo = new EnteInfo();
	    enteInfo.setCodiceEnte("ENTE6");
	    enteInfo.setDescrizione("ente 6");
	    provider.createEnte(enteInfo);

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
    public void testInitProvider() {

	try {

	    provider = new Provider();
	    String token = provider.login("admin", "admin", "");

	    provider = new Provider();
	    provider.login("admin", "admin", "");

	}
	catch (DocerException e) {
	    System.out.println(e.getMessage());
	}
    }

    @Test
    public void testSearchGroups() {

	try {

	    provider = new Provider();

	    ILoggedUserInfo uinfo = new LoggedUserInfo();
	    String ticket = provider.login("admin", "admin", null);

	    uinfo.setUserId("admin");
	    uinfo.setCodiceEnte(null);
	    uinfo.setTicket(ticket);

	    provider.setCurrentUser(uinfo);

	    Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
	    // searchCriteria.put("PARENT_GROUP_ID",
	    // Arrays.asList("ENTEPROVA","ENTE9"));
	    // searchCriteria.put("GROUP_ID",
	    // Arrays.asList("ENTEPROVA","ente9","[ENTEPROVA TO ENTEPROVA]"));
	    searchCriteria.put("GROUP_ID", Arrays.asList("[MIN TO MAX]"));

	    List<IGroupProfileInfo> res = provider.searchGroups(searchCriteria);

	    for (IGroupProfileInfo gpinfo : res) {
		System.out.println("-----------------");
		System.out.println("group_id: " + gpinfo.getGroupId());
		System.out.println("group_name: " + gpinfo.getGroupName());
		System.out.println("parent_group_id: " + gpinfo.getParentGroupId());
		System.out.println("enabled: " + gpinfo.getEnabled());
		System.out.println("gruppo_struttura: " + gpinfo.getExtraInfo().get("GRUPPO_STRUTTURA"));
		System.out.println("ExtraInfo:...");
		for (String key : gpinfo.getExtraInfo().keySet()) {
		    System.out.println(key + ": " + gpinfo.getExtraInfo().get(key));
		}
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
    public void testGetGroup() {

	try {

	    provider = new Provider();

	    ILoggedUserInfo uinfo = new LoggedUserInfo();
	    String ticket = provider.login("admin", "admin", "EMR");

	    uinfo.setUserId("admin");
	    uinfo.setCodiceEnte(null);
	    uinfo.setTicket(ticket);

	    provider.setCurrentUser(uinfo);

	    Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
	    searchCriteria.put("PARENT_GROUP_ID", Arrays.asList(""));

	    IGroupInfo res = provider.getGroup("EMR");

	    IGroupProfileInfo gpinfo = res.getProfileInfo();
	    System.out.println("-----------------");
	    System.out.println("group_id: " + gpinfo.getGroupId());
	    System.out.println("group_name: " + gpinfo.getGroupName());
	    System.out.println("parent_group_id: " + gpinfo.getParentGroupId());
	    System.out.println("enabled: " + gpinfo.getEnabled());
	    System.out.println("gruppo_struttura: " + gpinfo.getExtraInfo().get("GRUPPO_STRUTTURA"));

	    res = provider.getGroup("ALFRESCO_ADMINISTRATORS");

	    gpinfo = res.getProfileInfo();
	    System.out.println("-----------------");
	    System.out.println("group_id: " + gpinfo.getGroupId());
	    System.out.println("group_name: " + gpinfo.getGroupName());
	    System.out.println("parent_group_id: " + gpinfo.getParentGroupId());
	    System.out.println("enabled: " + gpinfo.getEnabled());
	    System.out.println("gruppo_struttura: " + gpinfo.getExtraInfo().get("GRUPPO_STRUTTURA"));

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
    public void testUpdateGroup() {

	try {

	    provider = new Provider();

	    ILoggedUserInfo uinfo = new LoggedUserInfo();
	    String ticket = provider.login("admin", "admin", null);

	    uinfo.setUserId("admin");
	    uinfo.setCodiceEnte(null);
	    uinfo.setTicket(ticket);

	    provider.setCurrentUser(uinfo);

	    IGroupProfileInfo gpinfo = new GroupProfileInfo();
	    gpinfo.setGroupId("groupid");
	    gpinfo.setGroupName("groupname");
	    gpinfo.setParentGroupId("ENTE");
	    gpinfo.getExtraInfo().put("GRUPPO_STRUTTURA", "true");

	    provider.updateGroup("groupid", gpinfo);

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
    public void testUpdateUser() {

	try {

	    provider = new Provider();

	    ILoggedUserInfo uinfo = new LoggedUserInfo();
	    String ticket = provider.login("admin", "admin", null);

	    uinfo.setUserId("admin");
	    uinfo.setCodiceEnte(null);
	    uinfo.setTicket(ticket);

	    provider.setCurrentUser(uinfo);

	    IUserProfileInfo userProfileInfo = new UserProfileInfo();
	    userProfileInfo.setUserPassword("admin");
	    
	    provider.updateUser("admin", userProfileInfo);

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
    public void testCreateGroup() {

	try {

	    provider = new Provider();

	    ILoggedUserInfo uinfo = new LoggedUserInfo();
	    String ticket = provider.login("admin", "admin", null);

	    uinfo.setUserId("admin");
	    uinfo.setCodiceEnte(null);
	    uinfo.setTicket(ticket);

	    provider.setCurrentUser(uinfo);

	    TitolarioId titolarioId = new TitolarioId();
	    titolarioId.setCodiceEnte("ENTE8");
	    titolarioId.setCodiceAOO("AOO11");
	    titolarioId.setClassifica("1.0.0");

	    Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
	    searchCriteria.put("GROUP_ID", Arrays.asList("*"));

	    IGroupProfileInfo gpinfo = new GroupProfileInfo();
	    gpinfo.setGroupId("222");
	    gpinfo.setGroupName("groupnamez");
	    gpinfo.setParentGroupId("ENTE8");
	    gpinfo.getExtraInfo().put("GRUPPO_STRUTTURA", "false");

	    provider.createGroup(gpinfo);

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

	try {

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
	    // TitolarioId titolarioId = new TitolarioId();
	    // titolarioId.setCodiceEnte("ENTE8");
	    // titolarioId.setCodiceAOO("AOO11");
	    // titolarioId.setClassifica("1.0.0");

	    Map<String, EnumACLRights> acls = new HashMap<String, EnumACLRights>();
	    acls.put("ADMINS_ENTE_ENTE8", EnumACLRights.normalAccess);
	    acls.put("ADMINS_AOO_ENTE8-AOO11", EnumACLRights.readOnly);
	    acls.put("SYS_ADMINS", EnumACLRights.readOnly);
	    acls.put("luca.biasin", EnumACLRights.readOnly);

	    System.out.println(acls.toString());
	    // provider.setACLTitolario(titolarioId, acls);

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
    public void testFascicolaSfascicolaDocumento() {

	Provider provider = null;
	try {
	    provider = new Provider();

	    String admintoken = provider.login("admin", "admin", "");

	    ILoggedUserInfo admininfo = new LoggedUserInfo("admin", "", admintoken);

	    String token = provider.login("luca.biasin", "luca.biasin", "");

	    ILoggedUserInfo userinfo = new LoggedUserInfo("luca.biasin", "", token);

	    provider.setCurrentUser(admininfo);
	    System.out.println("----------------------------");
	    Map<String, EnumACLRights> acls = provider.getACLDocument("110");
	    System.out.println("ACL attuali documento: " + acls.toString());

	    provider.setCurrentUser(admininfo);
	    Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
	    searchCriteria.put("DOCNUM", Arrays.asList("110"));
	    DataTable<String> dt = provider.searchDocuments(searchCriteria, null, Arrays.asList("ACL_EXPLICIT"), -1, null);

	    System.out.println("ACL_EXPLICIT: " + dt.getRow(0).get("ACL_EXPLICIT"));

	    provider.setCurrentUser(userinfo);

	    Map<String, String> props = new HashMap<String, String>();
	    props.put("COD_ENTE", "ENTETEST");
	    props.put("COD_AOO", "AOO_TEST");
	    props.put("CLASSIFICA", "CLASSIFICATEST");
	    props.put("ANNO_FASCICOLO", "2014");
	    props.put("PROGR_FASCICOLO", "FASCICOLOTEST");
	    props.put("FASC_SECONDARI", "CLASSIFICATEST/2014/FASCICOLOTEST/FIGLIO");

	    System.out.println("aggiorno profilo documento " + props.toString());
	    provider.updateProfileDocument("110", props);

	    
	    props.clear();
	    props.put("COD_ENTE", "ENTETEST");
	    props.put("COD_AOO", "AOO_TEST");
	    props.put("CLASSIFICA", "CLASSIFICATEST");
	    props.put("ANNO_FASCICOLO", "2014");
	    props.put("PROGR_FASCICOLO", "");
	    props.put("FASC_SECONDARI", "CLASSIFICATEST/2014/FASCICOLOTEST/FIGLIO");

	    System.out.println("aggiorno profilo documento " + props.toString());
	    provider.updateProfileDocument("110", props);

	    provider.setCurrentUser(admininfo);
	    System.out.println("----------------------------");
	    acls = provider.getACLDocument("110");
	    System.out.println("ACL finali: " + acls.toString());

	    provider.setCurrentUser(admininfo);
	    searchCriteria.clear();
	    searchCriteria.put("DOCNUM", Arrays.asList("110"));
	    dt = provider.searchDocuments(searchCriteria, null, Arrays.asList("ACL_EXPLICIT"), -1, null);

	    System.out.println("ACL_EXPLICIT: " + dt.getRow(0).get("ACL_EXPLICIT"));

	    System.out.println("ok");

	}
	catch (Exception e) {
	    System.out.println(e);
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
    public void testUpdateDocumento() {

	Provider provider = null;
	try {
	    provider = new Provider();

	    String admintoken = provider.login("admin", "admin", "");

	    ILoggedUserInfo admininfo = new LoggedUserInfo("admin", "", admintoken);

	    provider.setCurrentUser(admininfo);
	    System.out.println("----------------------------");
	    
	    Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
	    searchCriteria.put("LINGUA", Arrays.asList(""));
	    DataTable<String> dt = provider.searchDocuments(searchCriteria, null, Arrays.asList("DOCNUM"), -1, null);

	    System.out.println("trovati " + dt.getRows().size() +" documenti");
	    
	    Map<String, String> props = new HashMap<String, String>();
	    props.put("LINGUA", "Italiano");
		
	    
	    for(DataRow<String> dr : dt.getRows()){
		String docnum = dr.get("DOCNUM");
		    
		    provider.updateProfileDocument(docnum, props);
		    System.out.println("aggiorno profilo documento " + docnum);
	    }

	    
	    System.out.println("Fine");
	    
	}
	catch (Exception e) {
	    System.out.println(e);
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
    public void testAclChanged() {

	Provider provider = null;
	try {
	    String docId = "110";

	    provider = new Provider();

	    String admintoken = provider.login("admin", "admin", "");

	    ILoggedUserInfo admininfo = new LoggedUserInfo("admin", "", admintoken);

	    provider.setCurrentUser(admininfo);

	    System.out.println("----------------------------");
	    Map<String, EnumACLRights> acls = provider.getACLDocument(docId);
	    System.out.println("ACL attuali documento: " + acls.toString());

	    Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
	    searchCriteria.put("DOCNUM", Arrays.asList(docId));
	    DataTable<String> dt = provider.searchDocuments(searchCriteria, null, Arrays.asList("ACL_EXPLICIT"), -1, null);

	    System.out.println("ACL_EXPLICIT: " + dt.getRow(0).get("ACL_EXPLICIT"));

	    Map<String, String> props = new HashMap<String, String>();
	    props.put("ACL_CHANGED", "true");
	    provider.updateProfileDocument(docId, props);

	    System.out.println("----------------------------");
	    acls = provider.getACLDocument(docId);
	    System.out.println("ACL finali: " + acls.toString());

	    provider.setCurrentUser(admininfo);
	    searchCriteria.clear();
	    searchCriteria.put("DOCNUM", Arrays.asList(docId));
	    dt = provider.searchDocuments(searchCriteria, null, Arrays.asList("ACL_EXPLICIT"), -1, null);

	    System.out.println("ACL_EXPLICIT: " + dt.getRow(0).get("ACL_EXPLICIT"));

	    props.clear();
	    props.put("ACL_CHANGED", "false");
	    provider.updateProfileDocument(docId, props);

	    System.out.println("ok");

	}
	catch (Exception e) {
	    System.out.println(e);
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
    public void testGetACLDocumento() {

	Provider provider = null;
	try {
	    provider = new Provider();

	    String admintoken = provider.login("admin", "admin", "");

	    ILoggedUserInfo admininfo = new LoggedUserInfo("admin", "", admintoken);
	    
	    provider.setCurrentUser(admininfo);
	    Map<String, EnumACLRights> acls = provider.getACLDocument("7743");
	    System.out.println("ACL: " + acls.toString());

	    
	    
	}
	catch (Exception e) {
	    System.out.println(e);
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
    public void testFascicolaDocumento() {

	Provider provider = null;
	try {
	    provider = new Provider();

	    String admintoken = provider.login("admin", "admin", "");

	    ILoggedUserInfo admininfo = new LoggedUserInfo("admin", "", admintoken);

	    //String token = provider.login("luca.biasin", "luca.biasin", "");

	    //ILoggedUserInfo lui = new LoggedUserInfo("luca.biasin", "", token);

	    provider.setCurrentUser(admininfo);
	    System.out.println("----------------------------");
	    Map<String, EnumACLRights> acls = provider.getACLDocument("7743");
	    System.out.println("ACL attuali documento: " + acls.toString());

	    provider.setCurrentUser(admininfo);
	    Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
	    searchCriteria.put("DOCNUM", Arrays.asList("7743"));
	    DataTable<String> dt = provider.searchDocuments(searchCriteria, null, Arrays.asList("ACL_EXPLICIT"), -1, null);

	    System.out.println("ACL_EXPLICIT: " + dt.getRow(0).get("ACL_EXPLICIT"));

	    provider.setCurrentUser(admininfo);

	    IFascicoloId fid = new FascicoloId();
	    fid.setCodiceEnte("P_BO");
	    fid.setCodiceAOO("AOOPBO");
	    fid.setClassifica("1.1.1");
	    fid.setAnnoFascicolo("2014");
	    fid.setProgressivo("6");

	    System.out.println("----------------------------");
	    acls = provider.getACLFascicolo(fid);
	    System.out.println("ACL fascicolo 6: " + acls.toString());

	    fid = new FascicoloId();
	    fid.setCodiceEnte("P_BO");
	    fid.setCodiceAOO("AOOPBO");
	    fid.setClassifica("1.1.1");
	    fid.setAnnoFascicolo("2014");
	    fid.setProgressivo("66");

//	    System.out.println("----------------------------");
//	    System.out.println("DOCUMENTO_EREDITA_ACL_FASCICOLO=" + provider.DOCUMENTO_EREDITA_ACL);
//	    System.out.println("----------------------------");
	    acls = provider.getACLFascicolo(fid);
	    System.out.println("ACL fascicolo 66: " + acls.toString());

	    //provider.setCurrentUser(lui);
	    Map<String, String> props = new HashMap<String, String>();
	    props.put("COD_ENTE", "P_BO");
	    props.put("COD_AOO", "AOOPBO");
	    props.put("CLASSIFICA", "1.1.1");
	    props.put("ANNO_FASCICOLO", "2014");
	    props.put("PROGR_FASCICOLO", "6");
	    props.put("FASC_SECONDARI", "1.1.1/2014/66");

	    System.out.println("aggiorno profilo documento " + props.toString());
	    provider.updateProfileDocument("7743", props);

	    provider.setCurrentUser(admininfo);
	    System.out.println("----------------------------");
	    acls = provider.getACLDocument("7743");
	    System.out.println("ACL finali: " + acls.toString());

	    provider.setCurrentUser(admininfo);
	    searchCriteria.clear();
	    searchCriteria.put("DOCNUM", Arrays.asList("7743"));
	    dt = provider.searchDocuments(searchCriteria, null, Arrays.asList("ACL_EXPLICIT"), -1, null);

	    System.out.println("ACL_EXPLICIT: " + dt.getRow(0).get("ACL_EXPLICIT"));

	    System.out.println("ok");

	}
	catch (Exception e) {
	    System.out.println(e);
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
    public void testAddRemoveToFolderUnificato() {

	Provider provider = null;
	try {

	    List<String> docIds = Arrays.asList("7743");

	    String folderId = "7756";

	    provider = new Provider();

	    String admintoken = provider.login("admin", "admin", "");

	    ILoggedUserInfo userinfo = new LoggedUserInfo("admin", "", admintoken);

	    // String token = provider.login("luca.biasin", "luca.biasin", "");
	    //
	    // ILoggedUserInfo userinfo = new LoggedUserInfo("luca.biasin", "",
	    // token);

	    provider.setCurrentUser(userinfo);
	    for (String docId : docIds) {
		System.out.println("----------------------------");
		Map<String, EnumACLRights> acls = provider.getACLDocument(docId);
		System.out.println("ACL attuali documento: " + acls.toString());

	    }

	    System.out.println("----------------------------");
	    Map<String, EnumACLRights> acls = provider.getACLFolder(folderId);
	    System.out.println("ACL folder " + folderId + ": " + acls.toString());

//	    System.out.println("----------------------------");
//	    System.out.println("DOCUMENTO_EREDITA_ACL_FOLDER_PUBBLICA=" + provider.DOCUMENTO_EREDITA_ACL);
//	    System.out.println("----------------------------");

	    provider.setCurrentUser(userinfo);

	    System.out.println("add to folder docs: " + docIds);
	    provider.addToFolderDocuments(folderId, docIds);

	    for (String docId : docIds) {
		provider.setCurrentUser(userinfo);
		System.out.println("----------------------------");
		acls = provider.getACLDocument(docId);
		System.out.println("ACL dopo inserimento in folder: " + acls.toString());
	    }

	    provider.setCurrentUser(userinfo);
	    System.out.println("----------------------------");
	    System.out.println("remove from folder docIds: " + docIds);
	    provider.removeFromFolderDocuments(folderId, docIds);

	    provider.setCurrentUser(userinfo);
	    System.out.println("----------------------------");

	    for (String docId : docIds) {
		acls = provider.getACLDocument(docId);
		System.out.println("ACL finali: " + acls.toString());

		provider.setCurrentUser(userinfo);
	    }

	    System.out.println("ok");

	}
	catch (Exception e) {
	    System.out.println(e);
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
    public void testAddRemoveToFolder() {

	Provider provider = null;
	try {

	    List<String> docIds = Arrays.asList("110", "884");

	    String folderId = "895";

	    provider = new Provider();

	    String admintoken = provider.login("admin", "admin", "");

	    ILoggedUserInfo userinfo = new LoggedUserInfo("admin", "", admintoken);

	    // String token = provider.login("luca.biasin", "luca.biasin", "");
	    //
	    // ILoggedUserInfo userinfo = new LoggedUserInfo("luca.biasin", "",
	    // token);

	    provider.setCurrentUser(userinfo);
	    for (String docId : docIds) {
		System.out.println("----------------------------");
		Map<String, EnumACLRights> acls = provider.getACLDocument(docId);
		System.out.println("ACL attuali documento: " + acls.toString());

	    }

	    Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
	    DataTable<String> dt = null;
	    provider.setCurrentUser(userinfo);
	    for (String docId : docIds) {
		searchCriteria.clear();
		searchCriteria.put("DOCNUM", Arrays.asList(docId));
		dt = provider.searchDocuments(searchCriteria, null, Arrays.asList("ACL_EXPLICIT"), -1, null);
		System.out.println("ACL_EXPLICIT: " + dt.getRow(0).get("ACL_EXPLICIT"));
	    }

	    provider.setCurrentUser(userinfo);

	    System.out.println("----------------------------");
	    Map<String, EnumACLRights> acls = provider.getACLFolder(folderId);
	    System.out.println("ACL folder " + folderId + ": " + acls.toString());

//	    System.out.println("----------------------------");
//	    System.out.println("DOCUMENTO_EREDITA_ACL_FOLDER_PUBBLICA=" + provider.DOCUMENTO_EREDITA_ACL);
//	    System.out.println("----------------------------");

	    provider.setCurrentUser(userinfo);

	    System.out.println("add to folder docs: " + docIds);
	    provider.addToFolderDocuments(folderId, docIds);

	    for (String docId : docIds) {
		provider.setCurrentUser(userinfo);
		System.out.println("----------------------------");
		acls = provider.getACLDocument(docId);
		System.out.println("ACL finali: " + acls.toString());

		provider.setCurrentUser(userinfo);
		searchCriteria.clear();
		searchCriteria.put("DOCNUM", Arrays.asList(docId));
		dt = provider.searchDocuments(searchCriteria, null, Arrays.asList("ACL_EXPLICIT"), -1, null);

		System.out.println("ACL_EXPLICIT: " + dt.getRow(0).get("ACL_EXPLICIT"));

	    }

	    provider.setCurrentUser(userinfo);
	    System.out.println("----------------------------");
	    System.out.println("remove from folder docIds: " + docIds);
	    provider.removeFromFolderDocuments(folderId, docIds);

	    provider.setCurrentUser(userinfo);
	    System.out.println("----------------------------");

	    for (String docId : docIds) {
		acls = provider.getACLDocument(docId);
		System.out.println("ACL finali: " + acls.toString());

		provider.setCurrentUser(userinfo);
		searchCriteria.clear();
		searchCriteria.put("DOCNUM", Arrays.asList(docId));
		dt = provider.searchDocuments(searchCriteria, null, Arrays.asList("ACL_EXPLICIT"), -1, null);

		System.out.println("ACL_EXPLICIT: " + dt.getRow(0).get("ACL_EXPLICIT"));
	    }

	    System.out.println("ok");

	}
	catch (Exception e) {
	    System.out.println(e);
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
    public void testSetACLDocument2() {

	try {

	    String user = "luca.biasin";

	    provider = new Provider();

	    String adminticket = provider.login("admin", "admin", null);

	    ILoggedUserInfo admininfo = new LoggedUserInfo();
	    admininfo.setUserId(user);
	    admininfo.setCodiceEnte(null);
	    admininfo.setTicket(adminticket);

	    ILoggedUserInfo userinfo = new LoggedUserInfo();
	    String userticket = provider.login(user, user, null);

	    userinfo.setUserId(user);
	    userinfo.setCodiceEnte(null);
	    userinfo.setTicket(userticket);

	    provider.setCurrentUser(admininfo);

	    Map<String, EnumACLRights> acls = new HashMap<String, EnumACLRights>();

	    acls.put("admin", EnumACLRights.fullAccess);
	    acls.put("luca.biasin", EnumACLRights.fullAccess);
	    acls.put("EMR", EnumACLRights.readOnly);

	    System.out.println("----------------------------");
	    System.out.println("ACL to set: " + acls.toString());

	    provider.setACLDocument("110", acls);
	    acls = provider.getACLDocument("110");
	    System.out.println("ACL finali: " + acls.toString());

	    provider.setCurrentUser(admininfo);
	    Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
	    searchCriteria.put("DOCNUM", Arrays.asList("110"));
	    DataTable<String> dt = provider.searchDocuments(searchCriteria, null, Arrays.asList("ACL_EXPLICIT"), -1, null);

	    System.out.println("ACL_EXPLICIT: " + dt.getRow(0).get("ACL_EXPLICIT"));

	    provider.setCurrentUser(userinfo);
	    acls.clear();
	    System.out.println("----------------------------");
	    System.out.println("ACL to set: " + acls.toString());

	    provider.setACLDocument("110", acls);

	    provider.setCurrentUser(admininfo);
	    acls.clear();
	    acls = provider.getACLDocument("110");
	    System.out.println("ACL finali: " + acls.toString());

	    provider.setCurrentUser(admininfo);
	    searchCriteria = new HashMap<String, List<String>>();
	    searchCriteria.put("DOCNUM", Arrays.asList("110"));
	    dt = provider.searchDocuments(searchCriteria, null, Arrays.asList("ACL_EXPLICIT"), -1, null);

	    System.out.println("ACL_EXPLICIT: " + dt.getRow(0).get("ACL_EXPLICIT"));

	    provider.setCurrentUser(admininfo);
	    acls.clear();
	    acls.put("luca.biasin", EnumACLRights.fullAccess);
	    System.out.println("----------------------------");
	    System.out.println("acl to set: " + acls.toString());
	    provider.setACLDocument("110", acls);
	    acls = provider.getACLDocument("110");
	    System.out.println("ACL finali: " + acls.toString());

	    searchCriteria = new HashMap<String, List<String>>();
	    searchCriteria.put("DOCNUM", Arrays.asList("110"));
	    dt = provider.searchDocuments(searchCriteria, null, Arrays.asList("ACL_EXPLICIT"), -1, null);

	    System.out.println("ACL_EXPLICIT: " + dt.getRow(0).get("ACL_EXPLICIT"));

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
    public void testSetACLDocument() {

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
	    acls.put("admin", EnumACLRights.fullAccess);
	    acls.put("luca", EnumACLRights.fullAccess);

	    provider.setACLDocument("110", acls);

	    System.out.println("done");
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

    @Test
    public void testSetACLFascicolo() {

	try {

	    provider = new Provider();

	    ILoggedUserInfo uinfo = new LoggedUserInfo();
	    String ticket = provider.login("admin", "admin", null);

	    uinfo.setUserId("admin");
	    uinfo.setCodiceEnte(null);
	    uinfo.setTicket(ticket);

	    provider.setCurrentUser(uinfo);

	    Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
	    List<Map<String, String>> result = provider.searchAnagrafiche("FASCICOLO", searchCriteria, Arrays.asList("COD_ENTE", "COD_AOO", "CLASSIFICA", "PROGR_FASCICOLO", "ANNO_FASCICOLO"));

	    for (Map<String, String> fascicoloProfile : result) {
		FascicoloId fascicoloId = new FascicoloId();
		fascicoloId.setCodiceEnte(fascicoloProfile.get("COD_ENTE"));
		fascicoloId.setCodiceAOO(fascicoloProfile.get("COD_AOO"));
		fascicoloId.setClassifica(fascicoloProfile.get("CLASSIFICA"));
		fascicoloId.setAnnoFascicolo(fascicoloProfile.get("ANNO_FASCICOLO"));
		fascicoloId.setProgressivo(fascicoloProfile.get("PROGR_FASCICOLO"));

		Map<String, EnumACLRights> acls = new HashMap<String, EnumACLRights>();
		acls.put("SYS_ADMINS", EnumACLRights.readOnly);
		acls.put("admin", EnumACLRights.normalAccess);

		try {
		    provider.setACLFascicolo(fascicoloId, acls);
		    System.out.println("ok 1");
		}
		catch (Exception e) {
		    System.out.println(e.getMessage());
		    System.out.println("KO 1: " + fascicoloProfile.toString());
		}

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

    @Test
    public void testSearchDocumentsSpecialChars() {

	try {
	    Provider provider = new Provider();
	    String token = provider.login("admin", "admin", "");

	    ILoggedUserInfo lui = new LoggedUserInfo("admin", "", token);

	    provider.setCurrentUser(lui);

	    try {

		String specialchars = " &\"'<>¡¢£¤¥¦§¨©ª«¬®¯°±²³´µ¶·¸¹º»¼½¾¿×÷ÀÁÂÃÄÅÆÇÈÉÊËÌÍÎÏÐÑÒÓÔÕÖØÙÚÛÜÝÞßàáâãäåæçèéêëìíîïðñòóôõöøùúûüýþÿ";

		Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();

		searchCriteria.put("COD_ENTE", Arrays.asList(specialchars));

		List<String> returnProperties = Arrays.asList("DOCNUM");

		Map<String, EnumSearchOrder> orderby = new HashMap<String, EnumSearchOrder>();
		orderby.put("DOCNUM", EnumSearchOrder.DESC);
		DataTable<?> results = provider.searchDocuments(searchCriteria, null, returnProperties, 0, null);

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
    public void testReportCloud() {

	try {
	    Provider provider = new Provider();
	    String token = provider.login("admin", "Kdm.2001", "");

	    ILoggedUserInfo lui = new LoggedUserInfo("admin", "", token);

	    provider.setCurrentUser(lui);

	    try {

		List<String> returnProperties = Arrays.asList("COD_ENTE");
		Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
		List<Map<String, String>> results = provider.searchAnagrafiche("ENTE", searchCriteria, returnProperties);

		System.out.println("TROVATI: " + results.size() + " enti");

		String range = "[2013-10-11 TO 2013-11-11]";

		searchCriteria.put("CREATED", Arrays.asList(range));

		for (int index = 0; index < results.size(); index++) {

		    String codEnte = results.get(index).get("COD_ENTE");
		    System.out.println("ENTE: " + codEnte);

		    searchCriteria.put("COD_ENTE", Arrays.asList(codEnte));
		    DataTable<String> dt = provider.searchDocuments(searchCriteria, null, returnProperties, -1, null);
		    System.out.println("documenti creati nel range " + range + ": " + dt.getRows().size());
		    System.out.println("");
		}

	    }
	    catch (DocerException e) {
		System.out.println("ERRORE: " + e.getMessage());
	    }
	}
	catch (Exception e) {
	    System.out.println("error: " + e);
	}
    }

    @Test
    public void testSearchFolder() {

	try {
	    Provider provider = new Provider();
	    String token = provider.login("admin", "admin", "");

	    ILoggedUserInfo lui = new LoggedUserInfo("admin", "", token);

	    provider.setCurrentUser(lui);

	    String folderId = "";

	    try {

		Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
		// searchCriteria.put("COD_ENTE", Arrays.asList("*"));
		// searchCriteria.put("COD_AOO", Arrays.asList("*"));
		// searchCriteria.put("FOLDER_NAME", Arrays.asList("*"));
		// searchCriteria.put("PARENT_FOLDER_ID", Arrays.asList(""));
		// searchCriteria.put("FOLDER_ID", Arrays.asList("*"));

		searchCriteria.put("FOLDER_NAME", Arrays.asList("figlio rinominato"));
		searchCriteria.put("FOLDER_OWNER", Arrays.asList(""));

		// searchCriteria.put("FOLDER_ID",Arrays.asList("/app:company_home/cm:DOCAREA/cm:Attività fiscali"));

		// searchCriteria.put("FOLDER_ID",Arrays.asList("1872"));

		List<String> returnProperties = Arrays.asList("FOLDER_ID", "COD_ENTE", "COD_AOO", "FOLDER_NAME", "PARENT_FOLDER_ID", "FOLDER_OWNER", "DES_FOLDER", "ENABLED");

		Map<String, EnumSearchOrder> orderby = new HashMap<String, EnumSearchOrder>();
		orderby.put("FOLDER_NAME", EnumSearchOrder.ASC);
		DataTable<?> results = provider.searchFolders(searchCriteria, returnProperties, 1000, null);

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
	}
	catch (Exception e) {
	    System.out.println("error: " + e);
	}
    }

    @Test
    public void testSearchUsers() {

	try {
	    Provider provider = new Provider();
	    String token = provider.login("admin", "admin", "");

	    ILoggedUserInfo lui = new LoggedUserInfo("admin", "", token);

	    provider.setCurrentUser(lui);

	    String folderId = "";

	    try {

		Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
		// searchCriteria.put("COD_ENTE", Arrays.asList("*"));
		// searchCriteria.put("COD_AOO", Arrays.asList("*"));
		// searchCriteria.put("FOLDER_NAME", Arrays.asList("*"));
		// searchCriteria.put("PARENT_FOLDER_ID", Arrays.asList(""));
		// searchCriteria.put("USER_ID", Arrays.asList("*"));

		List<IUserProfileInfo> results = provider.searchUsers(searchCriteria);

		System.out.println("TROVATI: " + results.size() + " risultati");
		for (IUserProfileInfo profile : results) {

		    System.out.println("------------------");
		    System.out.println("user_id: " + profile.getUserId());
		    System.out.println("email_address: " + profile.getEmailAddress());
		    System.out.println("first_name: " + profile.getFirstName());
		    System.out.println("last_name: " + profile.getLastName());
		    System.out.println("full_name: " + profile.getFullName());
		    System.out.println("password: " + profile.getUserPassword());
		    System.out.println("ExtraInfo:...");
		    for (String key : profile.getExtraInfo().keySet()) {
			System.out.println(key + ": " + profile.getExtraInfo().get(key));
		    }
		    System.out.println("------------------");
		}

	    }
	    catch (DocerException e) {
		System.out.println("ERRORE: " + e.getMessage());
	    }
	}
	catch (Exception e) {
	    System.out.println("error: " + e);
	}
    }

    @Test
    public void testCreateFolders() {

	try {
	    Provider provider = new Provider();
	    String token = provider.login("admin", "admin", "EMR");

	    ILoggedUserInfo lui = new LoggedUserInfo("admin", "", token);

	    provider.setCurrentUser(lui);

	    String folderId = "";

	    IFolderInfo folderInfo = new FolderInfo();
	    // folderInfo.setCodiceEnte(ente);
	    // folderInfo.setCodiceAOO(aoo);
	    // folderInfo.setFolderName("folderName");
	    // folderInfo.setFolderOwner("admin");
	    // folderInfo.setParentFolderId("/"+ente +"/" +aoo);
	    // folderId = provider.createFolder(folderInfo);
	    //

	    try {

		folderInfo.setCodiceEnte("EMR");
		folderInfo.setCodiceAOO("AOO_EMR");
		folderInfo.setFolderName("test1-bis");
		folderInfo.setFolderOwner("admin");
		folderInfo.setParentFolderId("13127");
		folderId = provider.createFolder(folderInfo);
		System.out.println("creata folder " + folderId);
	    }
	    catch (DocerException e) {
		System.out.println("ERRORE: " + e.getMessage());
	    }

	    // try {
	    //
	    // folderInfo.setCodiceEnte(ente);
	    // folderInfo.setCodiceAOO(aoo);
	    // folderInfo.setFolderName(" 2 ");
	    // folderInfo.setFolderOwner("admin");
	    // folderInfo.setParentFolderId("57290");
	    // folderId = provider.createFolder(folderInfo);
	    // System.out.println("creata folder " + folderId);
	    // } catch (DocerException e) {
	    // System.out.println("ERRORE: " + e.getMessage());
	    // }

	    // try{
	    //
	    // folderInfo.setCodiceEnte(ente);
	    // folderInfo.setCodiceAOO(aoo);
	    // folderInfo.setFolderName("test folder pubblica");
	    // folderInfo.setFolderOwner("");
	    // folderInfo.setParentFolderId("57279");
	    // folderId = provider.createFolder(folderInfo);
	    // System.out.println("creata folder " +folderId);
	    // }catch(DocerException e){
	    // System.out.println("ERRORE: " +e.getMessage());
	    // }
	    //
	    // try{
	    //
	    // folderInfo.setCodiceEnte(ente);
	    // folderInfo.setCodiceAOO(aoo);
	    // folderInfo.setFolderName("test folder privata");
	    // folderInfo.setFolderOwner("admin");
	    // folderInfo.setParentFolderId("57279");
	    // folderId = provider.createFolder(folderInfo);
	    // System.out.println("creata folder " +folderId);
	    // }catch(DocerException e){
	    // System.out.println("ERRORE: " +e.getMessage());
	    // }

	    // try{
	    //
	    // folderInfo.setFolderName("folder 2");
	    // folderInfo.setParentFolderId("/"+ente +"/" +aoo
	    // +"/SHARED/folder 11");
	    // folderId = provider.createFolder(folderInfo);
	    // System.out.println("creata folder " +folderId);
	    // }catch(DocerException e){
	    // System.out.println("ERRORE: " +e.getMessage());
	    // }
	    //
	    // try{
	    // folderInfo.setFolderName("1");
	    // folderInfo.setParentFolderId("/"+ente +"/" +aoo +"/SHARED");
	    // folderId = provider.createFolder(folderInfo);
	    // System.out.println("creata folder " +folderId);
	    // }catch(DocerException e){
	    // System.out.println("ERRORE: " +e.getMessage());
	    // }
	    //
	    // try{
	    //
	    // folderInfo.setFolderName("1");
	    // folderInfo.setParentFolderId("/"+ente +"/" +aoo +"/SHARED/1");
	    // folderId = provider.createFolder(folderInfo);
	    // System.out.println("creata folder " +folderId);
	    // }catch(DocerException e){
	    // System.out.println("ERRORE: " +e.getMessage());
	    // }
	    //
	    //
	    // try{
	    // folderInfo.setFolderName("1");
	    // folderInfo.setParentFolderId("/"+ente +"/" +aoo +"/SHARED/1/1");
	    // folderId = provider.createFolder(folderInfo);
	    // System.out.println("creata folder " +folderId);
	    // }catch(DocerException e){
	    // System.out.println("ERRORE: " +e.getMessage());
	    // }
	    //
	    // try{
	    // folderInfo.setFolderName("1");
	    // folderInfo.setParentFolderId("/"+ente +"/" +aoo +"/SHARED");
	    // folderId = provider.createFolder(folderInfo);
	    // System.out.println("creata folder " +folderId);
	    // }catch(DocerException e){
	    // System.out.println("ERRORE: " +e.getMessage());
	    // }
	    // try{
	    // folderInfo.setParentFolderId("/"+ente +"/" +aoo +"/SHARED/1");
	    // folderId = provider.createFolder(folderInfo);
	    // System.out.println("creata folder " +folderId);
	    // }catch(DocerException e){
	    // System.out.println("ERRORE: " +e.getMessage());
	    // }
	    // try{
	    //
	    // folderInfo.setFolderName("1");
	    // folderInfo.setFolderOwner("");
	    // folderInfo.setParentFolderId("/"+ente +"/" +aoo
	    // +"/PERSONAL_FOLDERS/admin");
	    // folderId = provider.createFolder(folderInfo);
	    // System.out.println("creata folder " +folderId);
	    // }catch(DocerException e){
	    // System.out.println("ERRORE: " +e.getMessage());
	    // }
	    // try{
	    // folderInfo.setFolderOwner("admin");
	    // folderInfo.setParentFolderId("/"+ente +"/" +aoo
	    // +"/PERSONAL_FOLDERS/admin/1");
	    // folderId = provider.createFolder(folderInfo);
	    // System.out.println("creata folder " +folderId);
	    // }catch(DocerException e){
	    // System.out.println("ERRORE: " +e.getMessage());
	    // }
	}
	catch (DocerException e) {
	    System.out.println("ERRORE: " + e.getMessage());
	}
    }

    @Test
    public void testRenameFolder() {

	Provider provider = null;

	try {
	    provider = new Provider();
	    String token = provider.login("admin", "admin", "");

	    ILoggedUserInfo lui = new LoggedUserInfo("admin", "", token);

	    provider.setCurrentUser(lui);

	    String folderId = "";

	    IFolderInfo folderInfo = new FolderInfo();

	    try {

		folderInfo.setFolderName("folder 11");
		provider.updateFolder("13123", folderInfo);
		System.out.println("aggiornata folder " + folderId);
	    }
	    catch (DocerException e) {
		System.out.println("ERRORE: " + e.getMessage());
	    }

	}
	catch (DocerException e) {
	    System.out.println("ERRORE: " + e.getMessage());
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
    public void testUpdateFolders() {

	Provider provider = null;

	try {
	    provider = new Provider();
	    String token = provider.login("luca.biasin", "luca.biasin", "ente5");

	    ILoggedUserInfo lui = new LoggedUserInfo("luca.biasin", "", token);

	    provider.setCurrentUser(lui);

	    String folderId = "";

	    IFolderInfo folderInfo = new FolderInfo();

	    try {

		folderInfo.setDescrizione("folder 11");
		provider.updateFolder("57291", folderInfo);
		System.out.println("aggiornata folder " + folderId);
	    }
	    catch (DocerException e) {
		System.out.println("ERRORE: " + e.getMessage());
	    }

	}
	catch (DocerException e) {
	    System.out.println("ERRORE: " + e.getMessage());
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
    public void testSetACLFolder() {

	Provider provider = null;

	try {
	    provider = new Provider();
	    String token = provider.login("luca.biasin", "luca.biasin", "ENTE5");

	    ILoggedUserInfo lui = new LoggedUserInfo("luca.biasin", "", token);

	    provider.setCurrentUser(lui);

	    String folderId = "57298";

	    Map<String, EnumACLRights> acls = new HashMap<String, EnumACLRights>();
	    // acls.put("user1", EnumACLRights.fullAccess);
	    // acls.put("user2", EnumACLRights.normalAccess);
	    acls.put("ente5-aoo1", EnumACLRights.normalAccess);

	    provider.setACLFolder(folderId, acls);

	    acls = provider.getACLFolder(folderId);

	    for (String key : acls.keySet()) {
		System.out.println(key + "=" + acls.get(key));
	    }

	    System.out.println(provider.getEffectiveRightsFolder("", "luca.biasin"));

	}
	catch (DocerException e) {
	    System.out.println(e.getMessage());
	    System.out.println(e.getErrNumber());
	    System.out.println(e.getErrDescription());

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
    public void testGetEffectiveRights() {

	Provider provider = null;

	try {
	    provider = new Provider();
	    String token = provider.login("luca.biasin", "luca.biasin", "EMR");

	    ILoggedUserInfo lui = new LoggedUserInfo("luca.biasin", "EMR", token);

	    provider.setCurrentUser(lui);

	    String docId = "3716";

	    EnumACLRights er = provider.getEffectiveRights(docId, "luca.biasin");

	    System.out.println(er);

	    Map<String, String> id = new HashMap<String, String>();
	    id.put("COD_ENTE", "EMR");
	    id.put("COD_AOO", "AOO_EMR");
	    id.put("CLASSIFICA", "1");
	    id.put("ANNO_FASCICOLO", "2013");
	    id.put("PROGR_FASCICOLO", "1");
	    System.out.println(provider.getEffectiveRightsAnagrafiche("FASCICOLO", id, "luca.biasin"));

	    System.out.println(provider.getEffectiveRightsFolder("3706", "luca.biasin"));

	}
	catch (DocerException e) {
	    System.out.println(e.getMessage());
	    System.out.println(e.getErrNumber());
	    System.out.println(e.getErrDescription());

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
    public void testEquals() {

	List<String> list = new ArrayList<String>();
	list.add("d1");

	DocId d1 = new DocId("11");

	System.out.println(list.contains(d1));

	DocId d2 = new DocId("12");

	System.out.println("test " + d1);

    }

    @Test
    public void testDownloadDoc() {

	Provider provider = null;

	try {
	    provider = new Provider();
	    String token = provider.login("admin", "admin", "");

	    ILoggedUserInfo lui = new LoggedUserInfo("admin", "", token);

	    provider.setCurrentUser(lui);

	    byte[] file = provider.downloadLastVersion("1319", "C:\\bcklogs", 10000000);
	    if (file != null)
		System.out.println(file.length);

	    file = null;
	    file = provider.downloadVersion("57286", "0", "C:\\bcklogs", 10000000);
	    if (file != null)
		System.out.println(file.length);

	    file = null;
	    file = provider.downloadVersion("57286", "1", "C:\\bcklogs", 10000000);
	    if (file != null)
		System.out.println(file.length);

	}
	catch (DocerException e) {
	    System.out.println(e.getMessage());
	    System.out.println(e.getErrNumber());
	    System.out.println(e.getErrDescription());

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
    public void testDeleteFolder() {

	Provider provider = null;

	try {
	    provider = new Provider();
	    String token = provider.login("admin", "admin", "");

	    ILoggedUserInfo lui = new LoggedUserInfo("admin", "", token);

	    provider.setCurrentUser(lui);

	    String folderId = "/ENTE/AOO/SHARED/1/1";

	    provider.deleteFolder(folderId);

	}
	catch (DocerException e) {
	    System.out.println("ERRORE: " + e.getMessage());
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
    public void testRaplaceLastVersion() {

	Provider provider = null;

	try {
	    provider = new Provider();
	    String token = provider.login("admin", "admin", "");

	    ILoggedUserInfo lui = new LoggedUserInfo("admin", "", token);

	    provider.setCurrentUser(lui);

	    String filepath = "c:/test.txt";

	    FileDataSource source = new FileDataSource(new File(filepath));
	    DataHandler handler = new DataHandler(source);

	    String docid1;
	    try {
		provider.replaceLastVersion("18687", handler.getInputStream());

	    }
	    catch (IOException e) {
		System.out.println("errore replace documento 1 " + e.getMessage());
		return;
	    }
	    System.out.println("ok");

	}
	catch (DocerException e) {
	    System.out.println("ERRORE: " + e.getMessage());
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
    public void testAdvancedVersion() {

	Provider provider = null;

	try {
	    provider = new Provider();
	    String token = provider.login("admin", "admin", "");

	    ILoggedUserInfo lui = new LoggedUserInfo("admin", "", token);

	    provider.setCurrentUser(lui);

	    String filepath = "c:/test4.txt";

	    Map<String, String> info = new HashMap<String, String>();
	    info.put("COD_ENTE", "EMR");
	    info.put("COD_AOO", "AOO_EMR");
	    info.put("TYPE_ID", "documento");
	    info.put("DOCNAME", "doc1.txt");
	    info.put("TIPO_COMPONENTE", "PRINCIPALE");
	    info.put("ABSTRACT", "Lettera");
	    info.put("ARCHIVE_TYPE", "ARCHIVE");

	    FileDataSource source = new FileDataSource(new File(filepath));
	    DataHandler handler = new DataHandler(source);

	    String docid1;
	    try {
		docid1 = provider.createDocument("documento", info, handler.getInputStream());
		System.out.println("creato " + docid1);
	    }
	    catch (IOException e) {
		System.out.println("errore creazione documento 1 " + e.getMessage());
		return;
	    }

	    info.clear();
	    info.put("COD_ENTE", "EMR");
	    info.put("COD_AOO", "AOO_EMR");
	    info.put("TYPE_ID", "documento");
	    info.put("DOCNAME", "doc2.txt");
	    info.put("TIPO_COMPONENTE", "PRINCIPALE");
	    info.put("ABSTRACT", "Lettera");
	    info.put("ARCHIVE_TYPE", "ARCHIVE");

	    String docid2;
	    try {
		docid2 = provider.createDocument("documento", info, handler.getInputStream());
		System.out.println("creato " + docid2);
	    }
	    catch (IOException e) {
		System.out.println("errore creazione documento 2 " + e.getMessage());
		return;
	    }

	    provider.addNewAdvancedVersion(docid1, docid2);
	    System.out.println("ok");

	}
	catch (DocerException e) {
	    System.out.println("ERRORE: " + e.getMessage());
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
    public void testRiferimenti() {

	Provider provider = null;

	try {
	    provider = new Provider();
	    String token = provider.login("admin", "admin", "");

	    ILoggedUserInfo lui = new LoggedUserInfo("admin", "", token);

	    provider.setCurrentUser(lui);

	    provider.addRiferimentiDocuments("57300", Arrays.asList("57301", "57301", "57302", "57302", "57302"));
	    provider.removeRiferimentiDocuments("57300", Arrays.asList("57301", "57301", "57302", "57302", "57302"));

	    List<String> rifs = provider.getRiferimentiDocuments("57300");
	    for (String key : rifs) {
		System.out.println(key);
	    }

	    provider.addRiferimentiDocuments("57302", Arrays.asList("57303", "57304"));
	    provider.removeRiferimentiDocuments("57302", Arrays.asList("57303", "57304"));
	    rifs = provider.getRiferimentiDocuments("57302");
	    for (String key : rifs) {
		System.out.println(key);
	    }

	    provider.removeRiferimentiDocuments("57301", Arrays.asList("57300"));
	    rifs = provider.getRiferimentiDocuments("57301");

	    for (String key : rifs) {
		System.out.println(key);
	    }

	}
	catch (DocerException e) {
	    System.out.println("ERRORE: " + e.getMessage());
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
    public void testRelated() {

	Provider provider = null;

	try {
	    provider = new Provider();
	    String token = provider.login("admin", "admin", "");

	    ILoggedUserInfo lui = new LoggedUserInfo("admin", "", token);

	    provider.setCurrentUser(lui);

	    // provider.addRelatedDocuments("57300",
	    // Arrays.asList("57300","57301","57302"));
	    // provider.addRelatedDocuments("57303",
	    // Arrays.asList("57304","57305"));
	    // provider.addRelatedDocuments("57301",
	    // Arrays.asList("57300","57305"));
	    // provider.removeRelatedDocuments("57303",
	    // Arrays.asList("57301","57302"));
	    // provider.removeRelatedDocuments("57300",
	    // Arrays.asList("57300","57301","57302","57304","57305","57303"));

	    provider.addNewAdvancedVersion("57300", "57300");

	    provider.addNewAdvancedVersion("57301", "57302");

	    provider.addNewAdvancedVersion("57303", "57304");
	    provider.addNewAdvancedVersion("57303", "57305");

	    provider.addNewAdvancedVersion("57300", "57301");
	    provider.addNewAdvancedVersion("57301", "57305");
	    provider.addNewAdvancedVersion("57300", "57306");

	}
	catch (DocerException e) {
	    System.out.println("ERRORE: " + e.getMessage());
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
    public void testGetACLFolder() {

	Provider provider = null;

	try {
	    provider = new Provider();
	    String token = provider.login("admin", "admin", "");

	    ILoggedUserInfo lui = new LoggedUserInfo("admin", "", token);

	    provider.setCurrentUser(lui);

	    String folderId = "57289";

	    Map<String, EnumACLRights> acls = provider.getACLFolder(folderId);

	    for (String key : acls.keySet()) {
		System.out.println(key + "=" + acls.get(key));
	    }

	}
	catch (DocerException e) {
	    System.out.println("ERRORE: " + e.getMessage());
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
    public void testAddToFolderDocuments() {

	Provider provider = null;

	try {
	    provider = new Provider();
	    String token = provider.login("luca.biasin", "luca.biasin", "ENTE5");

	    ILoggedUserInfo lui = new LoggedUserInfo("luca.biasin", "", token);

	    provider.setCurrentUser(lui);

	    String docid = "57286";
	    String folderid = "57291";

	    // <entry
	    // key="#">{http://www.alfresco.org/model/content/1.0}creator;SV</entry>
	    // <entry
	    // key="#AUTHOR_ID">{http://www.alfresco.org/model/content/1.0}author;SV</entry>

	    Map<String, String> info = new HashMap<String, String>();
	    info.put("TYPIST_ID", "admin");
	    info.put("AUTHOR_ID", "admin");

	    provider.updateProfileDocument(docid, info);

	    FolderInfo finfo = new FolderInfo();
	    finfo.setExtraInfo(info);
	    provider.updateFolder(folderid, finfo);

	    List<String> documents = Arrays.asList(docid);
	    provider.addToFolderDocuments(folderid, documents);

	}
	catch (DocerException e) {
	    System.out.println("ERRORE: " + e.getMessage());
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
    public void testRemoveFromFolderDocuments() {

	Provider provider = null;

	try {
	    provider = new Provider();
	    String token = provider.login("admin", "admin", "");

	    ILoggedUserInfo lui = new LoggedUserInfo("admin", "", token);

	    provider.setCurrentUser(lui);

	    String folderId = "57291";

	    List<String> documents = Arrays.asList("57286");
	    provider.removeFromFolderDocuments(folderId, documents);

	}
	catch (DocerException e) {
	    System.out.println("ERRORE: " + e.getMessage());
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
    public void testGetFolderDocuments() {

	Provider provider = null;

	try {
	    provider = new Provider();
	    String token = provider.login("admin", "admin", "");

	    ILoggedUserInfo lui = new LoggedUserInfo("admin", "", token);

	    provider.setCurrentUser(lui);

	    String folderId = "/ENTE/AOO/SHARED/1/1";

	    try {

		List<String> res = provider.getFolderDocuments(folderId);
		for (String docnum : res) {
		    System.out.println("docnum " + docnum);
		}

	    }
	    catch (DocerException e) {
		System.out.println("ERRORE: " + e.getMessage());
	    }

	}
	catch (DocerException e) {
	    System.out.println("ERRORE: " + e.getMessage());
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
    public void testMatches() {

	String a = "/folder 1";
	System.out.println(a.matches(".*/.*"));
	System.out.println(a.matches("/fold.*"));
	System.out.println(a.matches("/folder 1"));
	System.out.println(a.matches(".*folder.*"));
    }

    @Test
    public void testFill() {

	String[] a = new String[] { "a", "b", "null" };
	int fromIndex = 2;
	int toIndex = 2;
	String val = "valore";

	Arrays.fill(a, fromIndex, toIndex, val);
	System.out.println(Arrays.toString(a));
    }

    @Test
    public void testDeleteUsers() {

	try {
	    Provider provider = new Provider();
	    String token = provider.login("admin", "admin", "");

	    ILoggedUserInfo lui = new LoggedUserInfo("admin", "", token);

	    provider.setCurrentUser(lui);

	    Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
	    List<String> exclude = new ArrayList<String>();
	    exclude.add("admin");
	    exclude.add("guest");
	    exclude.add("sysadmin");

	    searchCriteria.put("USER_ID", Arrays.asList("*"));

	    provider.deleteUsers(searchCriteria, exclude, -1);
	}
	catch (Exception e) {
	    System.out.println(e);
	}
    }

    @Test
    public void testDeleteGroups() {

	try {
	    Provider provider = new Provider();
	    String token = provider.login("admin", "admin", "");

	    ILoggedUserInfo lui = new LoggedUserInfo("admin", "", token);

	    provider.setCurrentUser(lui);

	    Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
	    List<String> exclude = new ArrayList<String>();
	    exclude.add("GROUP_ALFRESCO_ADMINISTRATORS");
	    exclude.add("GROUP_EMAIL_CONTRIBUTORS");
	    exclude.add("ALFRESCO_ADMINISTRATORS");
	    exclude.add("EMAIL_CONTRIBUTORS");
	    exclude.add("EVERYONE");
	    exclude.add("GROUP_EVERYONE");
	    searchCriteria.put("GROUP_ID", Arrays.asList("*"));

	    provider.deleteGroups(searchCriteria, exclude, 14);
	}
	catch (Exception e) {
	    System.out.println(e);
	}
    }

    // @Test
    // public void tsGetAlfrescoGroupsIds() {
    //
    // Provider provider = null;
    // try{
    // provider = new Provider();
    // String token = provider.login("sysadmin", "sysadmin", "");
    //
    // ILoggedUserInfo lui = new LoggedUserInfo("sysadmin","",token);
    //
    // provider.setCurrentUser(lui);
    //
    // Map<String,List<String>> searchCriteria = new HashMap<String,
    // List<String>>();
    // searchCriteria.put("COD_ENTE", Arrays.asList("*"));
    // //searchCriteria.put("PARENT_CLASSIFICA", Arrays.asList("01","02","03"));
    // //
    // //
    // List<String> groups = new ArrayList<String>();
    // groups.add("ALFRESCO_ADMINISTRATORS");
    // groups.add("EVERYONE");
    // groups.add("C_I530");
    // groups.add("C_I840");
    //
    //
    // List<String> res = provider.getCaseSensitiveGroupsIds(groups);
    //
    // for(int i=0; i<res.size(); i++){
    // System.out.println(res.get(i));
    // }
    //
    //
    // }
    // catch(Exception e){
    // System.out.println(e);
    // }
    // finally{
    // try {
    // provider.logout();
    // }
    // catch (DocerException e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
    // }
    // }


    @Test
    public void testSearch() {

	Provider provider = null;
	try {
	    provider = new Provider();
	    String token = provider.login("4@sici.it", "eae87a7beb5ed722e5132d6f08ad42c743880b80", "ENTE_STUDIO_K");

	    ILoggedUserInfo lui = new LoggedUserInfo("4@sici.it", "ENTE_STUDIO_K", token);

	    provider.setCurrentUser(lui);

	    Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
	    searchCriteria.put("STATO_ARCHIVISTICO", Arrays.asList("0"));
	    searchCriteria.put("DOCNUM", Arrays.asList("29252"));
	    
	    List<String> returnProperties = new ArrayList<String>();
	    returnProperties.add("DOCNAME");
	    returnProperties.add("DOCNUM");
	    returnProperties.add("RANKING");

	    DataTable<String> res = provider.searchDocuments(searchCriteria, null, returnProperties, -1, null);

	    System.out.println("trovati " +res.getRows().size() +" risultati");
	    
	    for (DataRow<String> row : res) {
		System.out.println("-----------------");
		for (String columnName : res.getColumnNames()) {
		    System.out.println(columnName + "=" + row.get(columnName));
		}
		System.out.println();
	    }


	}
	catch (Exception e) {
	    System.out.println(e);
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
    public void testUpdateProfile() {

	Provider provider = null;
	try {
	    provider = new Provider();
	    String token = provider.login("admin", "admin", "");

	    ILoggedUserInfo lui = new LoggedUserInfo("admin", "", token);

	    provider.setCurrentUser(lui);

	    Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
	    searchCriteria.put("DOCNUM", Arrays.asList("13447"));

	    List<String> returnProperties = new ArrayList<String>();
	    returnProperties.add("DOCNAME");
	    returnProperties.add("RANKING");

	    DataTable<String> res = provider.searchDocuments(searchCriteria, null, returnProperties, -1, null);

	    for (DataRow<String> row : res) {
		System.out.println("-----------------");
		for (String columnName : res.getColumnNames()) {
		    System.out.println(columnName + "=" + row.get(columnName));
		}
		System.out.println();
	    }

	    Map<String, String> props = new HashMap<String, String>();
	    props.put("DOCNAME", "allegati.txt");
	    props.put("DOCNAME", "allegati.txt");
	    props.put("DOCNAME", "allegati.txt");
	    props.put("DOCNAME", "allegati.txt");
	    props.put("DOCNAME", "allegati.txt");

	    // provider.renameFile("13447", props);

	    res = provider.searchDocuments(searchCriteria, null, returnProperties, -1, null);

	    for (DataRow<String> row : res) {
		System.out.println("-----------------");
		for (String columnName : res.getColumnNames()) {
		    System.out.println(columnName + "=" + row.get(columnName));
		}
		System.out.println();
	    }

	}
	catch (Exception e) {
	    System.out.println(e);
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
    public void testUpdateProfileNew() {

	Provider provider = null;
	try {
	    provider = new Provider();
	    String token = provider.login("admin", "admin", "");

	    ILoggedUserInfo lui = new LoggedUserInfo("admin", "", token);

	    provider.setCurrentUser(lui);

	    Map<String, String> props = new HashMap<String, String>();
	    props.put("ABSTRACT", "test acl");
	    provider.updateProfileDocument("110", props);

	}
	catch (Exception e) {
	    System.out.println(e);
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
    public void testGetProfile() {

	try {
	    Provider provider = new Provider();
	    String token = provider.login("admin", "admin", "");

	    ILoggedUserInfo lui = new LoggedUserInfo("admin", "", token);

	    provider.setCurrentUser(lui);

	    try {

		Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();

		searchCriteria.put("DOCNUM", Arrays.asList("1354"));

		List<String> returnProperties = Arrays.asList("ACL_EXPLICIT");

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
    public void testCreateDocumentSingle() {

	String filePath = "C:\\test.txt";

	Provider provider = null;

	try {

	    provider = new Provider();

	    ILoggedUserInfo uinfo = new LoggedUserInfo();
	    String ticket = provider.login("admin", "admin", null);

	    uinfo.setUserId("admin");
	    uinfo.setTicket(ticket);

	    long docnum = 10413;

	    Map<String, String> info = new HashMap<String, String>();
	    info.put("COD_ENTE", "EMR");
	    info.put("COD_AOO", "AOO_EMR");
	    info.put("TYPE_ID", "documento");
	    info.put("COD_TITOLARIO", "1");
	    
	    info.put("CLASSIFICA", "1");
	    info.put("ANNO_FASCICOLO", "2014");
	    info.put("PROGR_FASCICOLO", "1");
	    info.put("NUM_FASCICOLO", "1");
	    
	    info.put("NUM_PG", "1");
	    info.put("ANNO_PG", "2014");
	    info.put("REGISTRO_PG", "PG");	    
	    info.put("OGGETTO_PG", "OGGETTO_PG");
	    
	    info.put("N_REGISTRAZ", "1");
	    info.put("A_REGISTRAZ", "2014");
	    info.put("ID_REGISTRO", "REG");	    
	    info.put("O_REGISTRAZ", "oggetto reg particolare");	   
	    
	    info.put("ENABLED", "false");
	    
	    FileDataSource source = new FileDataSource(new File(filePath));
	    DataHandler handler = new DataHandler(source);

	    provider.setCurrentUser(uinfo);

	    for (int i = 0; i < 1; i++) {

		long start = new Date().getTime();
		info.put("DOCNAME", docnum + ".txt");
		String docid = provider.createDocument("documento", info, handler.getInputStream());
		System.out.println("creato " + docid);

		docnum++;
	    }

	}
	catch (Exception e) {

	    try {
		// if(fileInputStream!=null)
		// fileInputStream.close();
	    }
	    catch (Exception e2) {
	    }

	    System.out.println(e.getMessage());
	    // assertTrue(false);
	}
	finally {
	    if (provider != null) {
		try {
		    provider.logout();
		}
		catch (DocerException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
	    }

	}

    }

    @Test
    public void testSearchAnagraficheFullText() {

	Provider provider = null;
	try {
	    provider = new Provider();
	    String token = provider.login("admin", "admin", "");

	    ILoggedUserInfo lui = new LoggedUserInfo("admin", "", token);

	    provider.setCurrentUser(lui);

	    Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
	    searchCriteria.put("$KEYWORDS", Arrays.asList("XPMNNDR83T02D643M"));
	    // searchCriteria.put("PARENT_CLASSIFICA",
	    // Arrays.asList("01","02","03"));
	    //
	    //
	    List<String> returnProperties = new ArrayList<String>();
	    returnProperties.add("COD_ENTE");
	    returnProperties.add("COD_AOO");
	    returnProperties.add("ENABLED");

	    List<Map<String, String>> res = provider.searchAnagrafiche("fascicolo", searchCriteria, returnProperties);

	    for (int i = 0; i < res.size(); i++) {
		System.out.println("-------------------------");
		for (String key : res.get(i).keySet()) {
		    System.out.println(key + "=" + res.get(i).get(key));
		}
	    }

	}
	catch (Exception e) {
	    System.out.println(e);
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
    public void testSearchAnagrafiche() {

	Provider provider = null;
	try {
	    provider = new Provider();
	    String token = provider.login("admin", "admin", "");

	    ILoggedUserInfo lui = new LoggedUserInfo("admin", "", token);

	    provider.setCurrentUser(lui);

	    Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
	    searchCriteria.put("COD_ENTE", Arrays.asList("ENTEPROVA"));
	    // searchCriteria.put("PARENT_CLASSIFICA",
	    // Arrays.asList("01","02","03"));
	    //
	    //
	    List<String> returnProperties = new ArrayList<String>();
	    returnProperties.add("COD_ENTE");
	    returnProperties.add("COD_AOO");
	    returnProperties.add("ENABLED");

	    List<Map<String, String>> res = provider.searchAnagrafiche("ente", searchCriteria, returnProperties);

	    for (int i = 0; i < res.size(); i++) {
		System.out.println("-------------------------");
		for (String key : res.get(i).keySet()) {
		    System.out.println(key + "=" + res.get(i).get(key));
		}
	    }

	    searchCriteria.put("ZZZ", Arrays.asList("*"));
	    searchCriteria.put("COD_ENTE", Arrays.asList("*"));
	    searchCriteria.put("COD_AOO", Arrays.asList("*"));

	    returnProperties.add("COD_AOO");
	    returnProperties.add("DES_AOO");

	    res = provider.searchAnagrafiche("AOO", searchCriteria, returnProperties);

	    for (int i = 0; i < res.size(); i++) {
		System.out.println("-------------------------");
		for (String key : res.get(i).keySet()) {
		    System.out.println(key + "=" + res.get(i).get(key));
		}
	    }

	    res = provider.searchAnagrafiche("AREA_TEMATICA", searchCriteria, returnProperties);

	    for (int i = 0; i < res.size(); i++) {
		System.out.println("-------------------------");
		for (String key : res.get(i).keySet()) {
		    System.out.println(key + "=" + res.get(i).get(key));
		}
	    }
	    // String codEnte = "EMR";
	    // String codAOO = "AOO_EMR";
	    // String classifica = "TITOLARIO-TEST";
	    //
	    // String annoFascicolo = "2005";
	    // String progressivo = "2005.100.080.020.030.1";
	    //
	    // TitolarioId titolarioId = new TitolarioId();
	    // titolarioId.setCodiceEnte(codEnte);
	    // titolarioId.setCodiceAOO(codAOO);
	    // titolarioId.setClassifica(classifica);
	    // Map<String,EnumACLRights>acls =
	    // provider.getACLTitolario(titolarioId);
	    //
	    // for(String key:acls.keySet()){
	    // System.out.println( key +"=" +acls.get(key));
	    // }
	    //
	    // Map<String,String> id = new HashMap<String, String>();
	    // id.put("COD_ENTE",codEnte);
	    // id.put("COD_AOO",codAOO);
	    // id.put("CLASSIFICA",classifica);
	    // System.out.println(provider.getEffectiveRightsAnagrafiche("TITOLARIO",id
	    // , "admin"));
	    //
	    //
	    // FascicoloId fascicoloId = new FascicoloId();
	    // fascicoloId.setCodiceEnte(codEnte);
	    // fascicoloId.setCodiceAOO(codAOO);
	    // fascicoloId.setClassifica(classifica);
	    // fascicoloId.setAnnoFascicolo(annoFascicolo);
	    // fascicoloId.setProgressivo(progressivo);
	    // acls = provider.getACLFascicolo(fascicoloId);
	    //
	    // for(String key:acls.keySet()){
	    // System.out.println( key +"=" +acls.get(key));
	    // }
	    //
	    // id.put("COD_ENTE",codEnte);
	    // id.put("COD_AOO",codAOO);
	    // id.put("CLASSIFICA",classifica);
	    // id.put("ANNO_FASCICOLO",annoFascicolo);
	    // id.put("PROGR_FASCICOLO",progressivo);
	    // System.out.println(provider.getEffectiveRightsAnagrafiche("FASCICOLO",id
	    // , "admin"));

	}
	catch (Exception e) {
	    System.out.println(e);
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
    public void testThreads() {

	String filePath = "C:\\test.pdf";

	try {

	    Thread th = new Thread("test");
	    WorkQueue wq = new WorkQueue(100);
	    wq.execute(th);
	    // assertTrue(true);

	}
	catch (Exception e) {

	    try {
		getProvider(admin).logout();
	    }
	    catch (Exception e2) {
	    }

	    System.out.println(e.getMessage());
	    // assertTrue(false);
	}

    }

    @Test
    public void testDeleteDocument() {

	String filePath = "C:\\test.pdf";

	try {

	    String token = getProvider(null).login("admin", "admin", "");

	    ILoggedUserInfo userinfo = new LoggedUserInfo();
	    userinfo.setCodiceEnte("");
	    userinfo.setTicket(token);
	    userinfo.setUserId("admin");

	    getProvider(userinfo).deleteDocument("74055");

	    System.out.println("cancellato");
	    // assertTrue(true);

	}
	catch (Exception e) {

	    try {
		getProvider(admin).logout();
	    }
	    catch (Exception e2) {
	    }

	    System.out.println(e.getMessage());
	    // assertTrue(false);
	}

    }

    @Test
    public void testDeleteFoder() {

	String filePath = "C:\\test.pdf";

	try {

	    String token = getProvider(null).login("admin", "admin", "");

	    ILoggedUserInfo userinfo = new LoggedUserInfo();
	    userinfo.setCodiceEnte("");
	    userinfo.setTicket(token);
	    userinfo.setUserId("admin");

	    getProvider(userinfo).deleteFolder("74055");

	    System.out.println("cancellata");
	    // assertTrue(true);

	}
	catch (Exception e) {

	    try {
		getProvider(admin).logout();
	    }
	    catch (Exception e2) {
	    }

	    System.out.println(e.getMessage());
	    // assertTrue(false);
	}

    }

    @Test
    public void testVerifyTicket() {

	Provider provider = null;
	try {
	    provider = new Provider();

	    System.out.println(provider.verifyTicket("admin", "admin", "asdasdasd"));

	    String token = provider.login("admin", "admin", "");

	    System.out.println(provider.verifyTicket("admin", "EMR", token));

	    ILoggedUserInfo lui = new LoggedUserInfo("admin", "", token);

	    provider.setCurrentUser(lui);

	    // provider.logout();
	    // provider.setCurrentUser(lui);

	    Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
	    searchCriteria.put("COD_ENTE", Arrays.asList("ENTEPROVA"));
	    // searchCriteria.put("PARENT_FOLDER_ID", Arrays.asList(""));

	    List<String> returnProperties = new ArrayList<String>();
	    returnProperties.add("COD_ENTE");
	    returnProperties.add("COD_AOO");
	    returnProperties.add("CLASSIFICA");
	    returnProperties.add("DOCNUM");
	    returnProperties.add("PROGR_FASCICOLO");
	    returnProperties.add("PARENT_FOLDER_ID");

	    DataTable<String> res = provider.searchDocuments(searchCriteria, null, returnProperties, -1, null);

	    for (DataRow<String> row : res) {
		System.out.println("-----------------");
		for (String columnName : res.getColumnNames()) {
		    System.out.println(columnName + "=" + row.get(columnName));
		}
		System.out.println();
	    }

	}
	catch (Exception e) {
	    System.out.println(e);
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
    public void testSearchDocuments() {

	Provider provider = null;
	try {
	    provider = new Provider();
	    String token = provider.login("admin", "admin", "");

	    ILoggedUserInfo lui = new LoggedUserInfo("admin", "", token);

	    provider.setCurrentUser(lui);

	    // provider.setCurrentUser(lui);

	    //+TYPE:"docarea:documento" +@docarea\:codAoo:"AOO_AL" +@docarea\:tipoComponente:"PRINCIPALE" +(ISNULL:"docarea:archiveType" @docarea\:archiveType:"URL" @docarea\:archiveType:"ARCHIVE") +@cm\:created:[MIN TO 2013-12-31]
		    
	    Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
	    searchCriteria.put("COD_AOO", Arrays.asList("AOO_AL"));
	    searchCriteria.put("TIPO_COMPONENTE", Arrays.asList("PRINCIPALE"));
	    searchCriteria.put("ARCHIVE_TYPE", Arrays.asList("", "URL","ARCHIVE"));
	    searchCriteria.put("CREATED", Arrays.asList("[MIN TO 2013-12-31]"));
		    
	    // searchCriteria.put("PARENT_FOLDER_ID", Arrays.asList(""));

	    List<String> returnProperties = new ArrayList<String>();
	    returnProperties.add("COD_ENTE");
	    returnProperties.add("COD_AOO");
	    returnProperties.add("CLASSIFICA");
	    returnProperties.add("DOCNUM");
	    returnProperties.add("PROGR_FASCICOLO");
	    returnProperties.add("PARENT_FOLDER_ID");
	    returnProperties.add("MODIFIED");
	    returnProperties.add("CREATION_DATE");
	    returnProperties.add("RANKING");
	    returnProperties.add("COUNT");

	    long start = new Date().getTime();
	    DataTable<String> res = provider.searchDocuments(searchCriteria, null, returnProperties, 1000, null);

	    long end = new Date().getTime();

	    System.out.println("trovati " + res.getRows().size() + " risultati in " + (end - start) + " msec");
//	    for (DataRow<String> row : res) {
//		System.out.println("-----------------");
//		for (String columnName : res.getColumnNames()) {
//		    System.out.println(columnName + "=" + row.get(columnName));
//		}
//		System.out.println();
//	    }

	}
	catch (Exception e) {
	    System.out.println(e);
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
    public void testSearchDocumentsContentSize() {

	Provider provider = null;
	try {
	    provider = new Provider();
	    String token = provider.login("admin", "admin", "");

	    ILoggedUserInfo lui = new LoggedUserInfo("admin", "", token);

	    provider.setCurrentUser(lui);

	    // provider.setCurrentUser(lui);

	    Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
	    // searchCriteria.put("CONTENT_SIZE", Arrays.asList("122621"));
	    searchCriteria.put("CONTENT_SIZE", Arrays.asList("[0 TO 5]"));
	    // searchCriteria.put("PARENT_FOLDER_ID", Arrays.asList(""));

	    List<String> returnProperties = new ArrayList<String>();
	    // returnProperties.add("COD_ENTE");
	    // returnProperties.add("COD_AOO");
	    // returnProperties.add("CLASSIFICA");
	    // returnProperties.add("DOCNUM");
	    // returnProperties.add("PROGR_FASCICOLO");
	    // returnProperties.add("PARENT_FOLDER_ID");
	    // returnProperties.add("MODIFIED");
	    // returnProperties.add("CREATION_DATE");
	    returnProperties.add("CONTENT_SIZE");

	    DataTable<String> res = provider.searchDocuments(searchCriteria, null, returnProperties, -1, null);

	    for (DataRow<String> row : res) {
		System.out.println("-----------------");
		for (String columnName : res.getColumnNames()) {
		    System.out.println(columnName + "=" + row.get(columnName));
		}
		System.out.println();
	    }

	}
	catch (Exception e) {
	    System.out.println(e);
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
    public void testRegex() {

	String regexpSize = ".*\\|size\\=([^\\|]*)\\|.*";
	Pattern patternSize = Pattern.compile(regexpSize);
	String str = "contentUrl=store://2013/9/20/15/58/258b41d2-c28b-4985-8b8b-7bb7b4b6b56b.bin|mimetype=application/msword|size=284658|encoding=UTF-8|locale=en_US_|id=301";
	Matcher m = patternSize.matcher(str);

	if (m.matches()) {

	    System.out.println("match!");
	    System.out.println(m.group(1));

	}
	else {
	    System.out.println("NO match!");
	}
    }

    @Test
    public void testSearchAnagraficheContentSize() {

	Provider provider = null;
	try {
	    provider = new Provider();
	    String token = provider.login("admin", "admin", "");

	    ILoggedUserInfo lui = new LoggedUserInfo("admin", "", token);

	    provider.setCurrentUser(lui);

	    // provider.setCurrentUser(lui);

	    Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();

	    // searchCriteria.put("CONTENT_SIZE", Arrays.asList("[-1 TO 5]"));

	    List<String> returnProperties = new ArrayList<String>();
	    returnProperties.add("CONTENT_SIZE");

	    List<Map<String, String>> res = provider.searchAnagrafiche("FASCICOLO", searchCriteria, returnProperties);

	    System.out.println(res.size() + " risultati");
	    for (Map<String, String> map : res) {
		System.out.println("-----------------");
		for (String columnName : map.keySet()) {
		    System.out.println(columnName + "=" + map.get(columnName));
		}
		System.out.println();
	    }

	}
	catch (Exception e) {
	    System.out.println(e);
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
    public void testSearchAnagrafiche2() {

	try {
	    Provider provider = new Provider();
	    String token = provider.login("admin", "admin", "");

	    ILoggedUserInfo lui = new LoggedUserInfo("admin", "", token);

	    provider.setCurrentUser(lui);

	    Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
	    searchCriteria.put("COD_ENTE", Arrays.asList("P_RA"));
	    searchCriteria.put("PARENT_CLASSIFICA", Arrays.asList("04", "05", "06"));

	    List<String> returnProperties = new ArrayList<String>();
	    returnProperties.add("COD_ENTE");
	    returnProperties.add("COD_AOO");
	    returnProperties.add("CLASSIFICA");
	    // returnProperties.add("PROGR_FASCICOLO");
	    // returnProperties.add("DES_FASCICOLO");
	    // returnProperties.add("PARENT_PROGR_FASCICOLO");

	    long mm = 60000;
	    long hh = 60000 * 60;

	    long startTime = new Date().getTime();
	    long end = startTime;
	    List<Map<String, String>> res = null;
	    long total = 0;
	    while (total < (30 * mm)) {
		long start = new Date().getTime();
		res = provider.searchAnagrafiche("TITOLARIO", searchCriteria, returnProperties);
		end = new Date().getTime();
		total = end - startTime;
		System.out.println("res: " + res.size() + ", tempo: " + (end - start) + ". Tempo totale: " + total);
	    }

	}
	catch (Exception e) {
	    System.out.println(e);
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
    public void testSearchAnagrafiche3() {

	try {
	    Provider provider = new Provider();
	    String token = provider.login("admin", "admin", "");

	    ILoggedUserInfo lui = new LoggedUserInfo("admin", "", token);

	    provider.setCurrentUser(lui);

	    Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
	    searchCriteria.put("COD_ENTE", Arrays.asList("P_RA"));
	    searchCriteria.put("PARENT_CLASSIFICA", Arrays.asList("07", "08", "09"));

	    List<String> returnProperties = new ArrayList<String>();
	    returnProperties.add("COD_ENTE");
	    returnProperties.add("COD_AOO");
	    returnProperties.add("CLASSIFICA");
	    // returnProperties.add("PROGR_FASCICOLO");
	    // returnProperties.add("DES_FASCICOLO");
	    // returnProperties.add("PARENT_PROGR_FASCICOLO");

	    long mm = 60000;
	    long hh = 60000 * 60;

	    long startTime = new Date().getTime();
	    long end = startTime;
	    List<Map<String, String>> res = null;
	    long total = 0;
	    while (total < (30 * mm)) {
		long start = new Date().getTime();
		res = provider.searchAnagrafiche("TITOLARIO", searchCriteria, returnProperties);
		end = new Date().getTime();
		total = end - startTime;
		System.out.println("res: " + res.size() + ", tempo: " + (end - start) + ". Tempo totale: " + total);
	    }

	}
	catch (Exception e) {
	    System.out.println(e);
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
    public void testSearchAnagrafiche4() {

	try {
	    Provider provider = new Provider();
	    String token = provider.login("admin", "admin", "");

	    ILoggedUserInfo lui = new LoggedUserInfo("admin", "", token);

	    provider.setCurrentUser(lui);

	    Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
	    searchCriteria.put("COD_ENTE", Arrays.asList("P_RA"));
	    searchCriteria.put("PARENT_CLASSIFICA", Arrays.asList("10", "11", "12"));

	    List<String> returnProperties = new ArrayList<String>();
	    returnProperties.add("COD_ENTE");
	    returnProperties.add("COD_AOO");
	    returnProperties.add("CLASSIFICA");
	    // returnProperties.add("PROGR_FASCICOLO");
	    // returnProperties.add("DES_FASCICOLO");
	    // returnProperties.add("PARENT_PROGR_FASCICOLO");

	    long mm = 60000;
	    long hh = 60000 * 60;

	    long startTime = new Date().getTime();
	    long end = startTime;
	    List<Map<String, String>> res = null;
	    long total = 0;
	    while (total < (30 * mm)) {
		long start = new Date().getTime();
		res = provider.searchAnagrafiche("TITOLARIO", searchCriteria, returnProperties);
		end = new Date().getTime();
		total = end - startTime;
		System.out.println("res: " + res.size() + ", tempo: " + (end - start) + ". Tempo totale: " + total);
	    }

	}
	catch (Exception e) {
	    System.out.println(e);
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

    // ILoggedUserInfo lui = new LoggedUserInfo("admin","",token);
    //
    // provider.setCurrentUser(lui);
    //
    // Map<String,List<String>> searchCriteria = new HashMap<String,
    // List<String>>();
    // List<String> exclude = new ArrayList<String>();
    // searchCriteria.put("GROUP_ID", Arrays.asList("AOOPRORA_*"));
    //
    // provider.deleteGroups(searchCriteria, exclude,289);

    private Provider getProvider(ILoggedUserInfo userInfo) throws DocerException {

	if (provider == null)
	    provider = new Provider();
	if (userInfo != null)
	    provider.setCurrentUser(userInfo);
	return provider;

    }

    @Test
    public void testLoginAdmin2() {
	try {

	    String token = getProvider(null).login("admin", "admin", "");
	    System.out.println(token);
	}
	catch (Exception e) {
	    System.out.println(e);
	}
    }

    @Test
    public void testLoginAdmin() {
	try {

	    String token = getProvider(null).login(userId, password, "");
	    admin = new LoggedUserInfo(userId, "", token);
	}
	catch (Exception e) {
	    System.out.println(e);
	}
    }

    // /**
    // * Esegue la creazione di un'anagrafica Ente e del relativo Gruppo Ente
    // *
    // * @param enteInfo Profilo dell'anagrafica Ente
    // * @throws DocerException
    // */
    // public void createEnte(IEnteInfo enteInfo) throws DocerException;
    //
    // /**
    // * Esegue la creazione di un'anagrafica AOO
    // *
    // * @param aooInfo Profilo dell'anagrafica AOO
    // * @throws DocerException
    // */
    // public void createAOO(IAOOInfo aooInfo) throws DocerException;
    //
    // /**
    // * Esegue la creazione di un'anagrafica voce di Titolario
    // *
    // * @param titolarioInfo Profilo dell'anagrafica voce di Titolario
    // * @throws DocerException
    // */
    // public void createTitolario(ITitolarioInfo titolarioInfo) throws
    // DocerException;
    //
    // /**
    // * Esegue la creazione di un'anagrafica Fascicolo
    // *
    // * @param fascicoloInfo Profilo dell'anagrafica Fascicolo
    // * @throws DocerException
    // */
    // public void createFascicolo(IFascicoloInfo fascicoloInfo) throws
    // DocerException;
    //
    // /**
    // * Esegue la creazione di un'anagrafica custom
    // *
    // * @param customItemInfo Profilo dell'anagrafica custom
    // * @throws DocerException
    // */
    // public void createCustomItem(ICustomItemInfo customItemInfo) throws
    // DocerException;
    //
    // /**
    // * Esegue la creazione di un Utente
    // *
    // * @param userProfile Profilo dell'Utente
    // * @throws DocerException
    // */
    // public void createUser(IUserProfileInfo userProfile) throws
    // DocerException;
    //
    // /**
    // * Esegue la creazione di un Gruppo
    // *
    // * @param groupProfile Profilo del Gruppo
    // * @throws DocerException
    // */
    // public void createGroup(IGroupProfileInfo groupProfile) throws
    // DocerException;

    @Test
    public void testReplace() {
    
	String a = "d:test";
	a = a.replaceAll("^d:", "docarea:");
	System.out.println(a);
    }
    @Test
    public void testCreateDocumentCiclico() {

	String filePath = "C:\\test4.txt";

	Provider provider = null;

	try {

	    provider = new Provider();

	    ILoggedUserInfo uinfo = new LoggedUserInfo();
	    String ticket = provider.login("admin", "admin", null);

	    uinfo.setUserId("admin");
	    uinfo.setTicket(ticket);

	    long docnum = 10413;

	    Map<String, String> info = new HashMap<String, String>();
	    info.put("COD_ENTE", "EMR");
	    info.put("COD_AOO", "AOO_EMR");
	    info.put("TYPE_ID", "documento");

	    FileDataSource source = new FileDataSource(new File(filePath));
	    DataHandler handler = new DataHandler(source);

	    IFascicoloInfo finfo = new FascicoloInfo();
	    finfo.setCodiceEnte("EMR");
	    finfo.setCodiceAOO("AOO_EMR");
	    finfo.setClassifica("1");
	    finfo.setAnnoFascicolo("2013");

	    Map<String, String> metadatiFascicolazione = new HashMap<String, String>();
	    metadatiFascicolazione.put("COD_ENTE", "EMR");
	    metadatiFascicolazione.put("COD_AOO", "AOO_EMR");
	    metadatiFascicolazione.put("CLASSIFICA", "1");
	    metadatiFascicolazione.put("ANNO_FASCICOLO", "2013");

	    provider.setCurrentUser(uinfo);

	    for (int i = 0; i < 100000; i++) {

		long start = new Date().getTime();
		info.put("DOCNAME", docnum + ".txt");
		info.put("DOCNUM", String.valueOf(docnum));
		String docid = provider.createDocument("documento", info, handler.getInputStream());
		System.out.println("creato " + docid);

		finfo.setNumeroFascicolo(String.valueOf(docnum));
		finfo.setProgressivo(String.valueOf(docnum));
		finfo.setDescrizione("des fascicolo " + docnum);

		provider.createFascicolo(finfo);
		System.out.println("creato fascicolo " + docnum);

		metadatiFascicolazione.put("PROGR_FASCICOLO", String.valueOf(docnum));
		provider.updateProfileDocument(docid, metadatiFascicolazione);

		long end = new Date().getTime();

		System.out.println("documento " + docnum + " fascicolato: " + (end - start) + " msec");

		docnum++;
	    }

	}
	catch (Exception e) {

	    try {
		// if(fileInputStream!=null)
		// fileInputStream.close();
	    }
	    catch (Exception e2) {
	    }

	    System.out.println(e.getMessage());
	    // assertTrue(false);
	}
	finally {
	    if (provider != null) {
		try {
		    provider.logout();
		}
		catch (DocerException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
	    }

	}

    }

    @Test
    public void testSearchDocumentsTimes() {

	Provider provider = null;

	try {

	    provider = new Provider();

	    ILoggedUserInfo uinfo = new LoggedUserInfo();
	    String ticket = provider.login("admin", "admin", null);

	    uinfo.setUserId("admin");
	    uinfo.setTicket(ticket);

	    Map<String, String> info = new HashMap<String, String>();
	    info.put("COD_ENTE", "EMR");
	    info.put("COD_AOO", "AOO_EMR");
	    info.put("TYPE_ID", "documento");

	    Map<String, String> metadatiFascicolazione = new HashMap<String, String>();
	    metadatiFascicolazione.put("COD_ENTE", "EMR");
	    metadatiFascicolazione.put("COD_AOO", "AOO_EMR");
	    metadatiFascicolazione.put("CLASSIFICA", "1");
	    metadatiFascicolazione.put("ANNO_FASCICOLO", "2013");

	    provider.setCurrentUser(uinfo);

	    Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();

	    List<String> returnProperties = Arrays.asList("DOCNUM");

	    int maxResults = 0;
	    long start = new Date().getTime();
	    DataTable<String> r = provider.searchDocuments(searchCriteria, null, returnProperties, maxResults, null);
	    long end = new Date().getTime();
	    System.out.println(r.getRows().size() + " risultati in " + (end - start) + " msec");
	    System.out.println();

	    maxResults = 1;
	    start = new Date().getTime();
	    r = provider.searchDocuments(searchCriteria, null, returnProperties, maxResults, null);
	    end = new Date().getTime();
	    System.out.println(r.getRows().size() + " risultati in " + (end - start) + " msec");
	    System.out.println();

	    maxResults = 5;
	    start = new Date().getTime();
	    r = provider.searchDocuments(searchCriteria, null, returnProperties, maxResults, null);
	    end = new Date().getTime();
	    System.out.println(r.getRows().size() + " risultati in " + (end - start) + " msec");
	    System.out.println();

	    maxResults = 10;
	    start = new Date().getTime();
	    r = provider.searchDocuments(searchCriteria, null, returnProperties, maxResults, null);
	    end = new Date().getTime();
	    System.out.println(r.getRows().size() + " risultati in " + (end - start) + " msec");
	    System.out.println();

	    maxResults = 25;
	    start = new Date().getTime();
	    r = provider.searchDocuments(searchCriteria, null, returnProperties, maxResults, null);
	    end = new Date().getTime();
	    System.out.println(r.getRows().size() + " risultati in " + (end - start) + " msec");
	    System.out.println();

	    maxResults = 50;
	    start = new Date().getTime();
	    r = provider.searchDocuments(searchCriteria, null, returnProperties, maxResults, null);
	    end = new Date().getTime();
	    System.out.println(r.getRows().size() + " risultati in " + (end - start) + " msec");
	    System.out.println();

	    maxResults = 100;
	    start = new Date().getTime();
	    r = provider.searchDocuments(searchCriteria, null, returnProperties, maxResults, null);
	    end = new Date().getTime();
	    System.out.println(r.getRows().size() + " risultati in " + (end - start) + " msec");
	    System.out.println();

	    maxResults = 250;
	    start = new Date().getTime();
	    r = provider.searchDocuments(searchCriteria, null, returnProperties, maxResults, null);
	    end = new Date().getTime();
	    System.out.println(r.getRows().size() + " risultati in " + (end - start) + " msec");
	    System.out.println();

	    maxResults = 500;
	    start = new Date().getTime();
	    r = provider.searchDocuments(searchCriteria, null, returnProperties, maxResults, null);
	    end = new Date().getTime();
	    System.out.println(r.getRows().size() + " risultati in " + (end - start) + " msec");
	    System.out.println();

	    maxResults = 750;
	    start = new Date().getTime();
	    r = provider.searchDocuments(searchCriteria, null, returnProperties, maxResults, null);
	    end = new Date().getTime();
	    System.out.println(r.getRows().size() + " risultati in " + (end - start) + " msec");
	    System.out.println();

	    maxResults = 1000;
	    start = new Date().getTime();
	    r = provider.searchDocuments(searchCriteria, null, returnProperties, maxResults, null);
	    end = new Date().getTime();
	    System.out.println(r.getRows().size() + " risultati in " + (end - start) + " msec");
	    System.out.println();

	}
	catch (Exception e) {

	    try {
		// if(fileInputStream!=null)
		// fileInputStream.close();
	    }
	    catch (Exception e2) {
	    }

	    System.out.println(e.getMessage());
	    // assertTrue(false);
	}
	finally {
	    if (provider != null) {
		try {
		    provider.logout();
		}
		catch (DocerException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
	    }

	}

    }

    @Test
    public void testCreateDocument() {

	String filePath = "C:\\test.txt";

	// FileInputStream fileInputStream = null;
	//
	// File file = new File(filePath);
	// try {
	// fileInputStream = new FileInputStream(file);
	// } catch (FileNotFoundException e1) {
	//
	// System.out.println(e1);
	// return;
	// }

	try {

	    Map<String, String> info = new HashMap<String, String>();
	    info.put("COD_ENTE", ente);
	    info.put("COD_AOO", aoo);
	    info.put("DOCNAME", "test.pdf");
	    info.put("TYPE_ID", "documento");
	    info.put("CLASSIFICA", "1");
	    info.put("PROGR_FASCICOLO", "1");
	    info.put("ANNO_FASCICOLO", "2013");
	    // info.put("CREATION_DATE", "2007-03-02T15:10:43.359+01:00");

	    FileDataSource source = new FileDataSource(new File(filePath));
	    DataHandler handler = new DataHandler(source);

	    docid_1 = getProvider(admin).createDocument("documento", info, handler.getInputStream());
	    System.out.println("creato " + docid_1);
	    // assertTrue(true);

	}
	catch (Exception e) {

	    try {
		// if(fileInputStream!=null)
		// fileInputStream.close();
	    }
	    catch (Exception e2) {
	    }

	    System.out.println(e.getMessage());
	    // assertTrue(false);
	}

    }

    @Test
    public void testDocUrl() {

	// String filePath = "C:\\test.txt";
	//
	FileInputStream fileInputStream = null;
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

	try {

	    String token = getProvider(null).login("admin", "admin", "");
	    ILoggedUserInfo uinfo = new LoggedUserInfo("admin", "", token);

	    Map<String, String> info = new HashMap<String, String>();
	    info.put("COD_ENTE", "C_I840");
	    info.put("COD_AOO", "C-I840-01");
	    info.put("DOCNAME", "http://www.kdm.it/123124.it");
	    info.put("TYPE_ID", "documento");
	    info.put("ARCHIVE_TYPE", "URL");
	    // info.put("CREATION_DATE", "2007-03-02T15:10:43.359+01:00");

	    docid_1 = getProvider(uinfo).createDocument("documento", info, fileInputStream);
	    System.out.println("creato " + docid_1);

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
	    // byte[] b = provider.downloadLastVersion(docid_1, "C:\\bcklogs",
	    // 10000000);
	    // if (b != null)
	    // System.out.println(b.length);
	    //
	    // String version = getProvider(admin).addNewVersion(docid_1,
	    // fileInputStream);
	    // System.out.println("creata versione " + version);
	    //
	    // b = null;
	    // b = provider.downloadVersion(docid_1, version, "C:\\bcklogs",
	    // 10000000);
	    // if (b != null)
	    // System.out.println(b.length);

	    // assertTrue(true);

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
    public void testVersion() {

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

	try {

	    testLoginAdmin();

	    Map<String, String> info = new HashMap<String, String>();
	    info.put("COD_ENTE", ente);
	    info.put("COD_AOO", aoo);
	    info.put("DOCNAME", "test.txt");
	    info.put("TYPE_ID", "documento");
	    // info.put("CREATION_DATE", "2007-03-02T15:10:43.359+01:00");

	    docid_1 = getProvider(admin).createDocument("documento", info, fileInputStream);
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

	    byte[] b = provider.downloadLastVersion(docid_1, "C:\\bcklogs", 10000000);
	    if (b != null)
		System.out.println(b.length);

	    String version = getProvider(admin).addNewVersion(docid_1, fileInputStream);
	    System.out.println("creata versione " + version);

	    b = null;
	    b = provider.downloadVersion(docid_1, version, "C:\\bcklogs", 10000000);
	    if (b != null)
		System.out.println(b.length);

	    // assertTrue(true);

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
    public void testCreateUser() {

	try {

	    testLoginAdmin();
	    IUserProfileInfo userProfile = new UserProfileInfo();
	    userProfile.setUserId("luca.biasin");
	    userProfile.setUserPassword("luca.biasin");
	    userProfile.setFirstName("luca");
	    userProfile.setLastName("biasin");
	    userProfile.setFullName("luca biasin");
	    getProvider(admin).createUser(userProfile);

	    assertTrue(true);

	}
	catch (Exception e) {
	    System.out.println(e.getMessage());
	    assertTrue(false);
	}
	finally {
	    try {
		getProvider(admin).logout();
	    }
	    catch (DocerException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}

    }

    // public DataTable<String> searchDocuments(Map<String, List<String>>
    // searchCriteria, List<String> keywords, List<String> returnProperties,int
    // maxResults, Map<String,EnumSearchOrder> orderby) throws DocerException;
    //
    // /**
    // * Recupera la lista dei documenti related al documento con ID docId
    // *
    // * @param docId Id del documento
    // * @return Lista di Id dei related
    // * @throws DocerException
    // */
    // public List<String> getRelatedDocuments(String docId) throws
    // DocerException;
    //
    // /**
    // * Aggiunge elementi alla catena dei related di un documento
    // *
    // * @param docId Id del documento
    // * @param related Lista degli Id dei documenti da aggiungere alla catena
    // dei related del documento docId
    // * @throws DocerException
    // */
    // public void addRelatedDocuments(String docId, List<String> related)
    // throws DocerException;
    //
    // /**
    // * Elimina elementi dalla catena dei related di un documento
    // *
    // * @param docId Id del documento
    // * @param related Lista degli Id dei documenti da eliminare dalla catena
    // dei related del documento dodId
    // * @throws DocerException
    // */
    // public void removeRelatedDocuments(String docId, List<String> related)
    // throws DocerException;
    //
    // /**
    // * Recupera la lista dei riferimenti del documento docId
    // *
    // * @param docId Id del documento
    // * @return L'insieme dei riferimenti del documento docId
    // * @throws DocerException
    // */
    // public List<String> getRiferimentiDocuments(String docId) throws
    // DocerException;
    //
    // /**
    // * Aggiunge riferimenti al documento docId
    // *
    // * @param docId Id del documento
    // * @param riferimenti Lista dei riferimenti da aggiungere all'insieme dei
    // riferimenti del documento docId
    // * @throws DocerException
    // */
    // public void addRiferimentiDocuments(String docId, List<String>
    // riferimenti) throws DocerException;
    //
    // /**
    // * Elimina riferimenti dal documento docId
    // *
    // * @param docId Id del documento
    // * @param riferimenti Lista dei riferimenti da eliminare dall'insieme dei
    // riferimenti del documento docId
    // * @throws DocerException
    // */
    // public void removeRiferimentiDocuments(String docId, List<String>
    // riferimenti) throws DocerException;
    //
    // /**
    // * Recupera il profilo di un Utente insieme alla lista dei Gruppi a cui e'
    // associato
    // *
    // * @param userId Id dell'Utente
    // * @return Profilo dell'Utente insieme alla lista dei group-id dei Gruppi
    // a cui esso e' associato
    // * @throws DocerException
    // */
    // public IUserInfo getUser(String userId) throws DocerException;
    //
    // /**
    // * Recupera il profilo di un Gruppo insieme alla lista degli Utenti ad
    // esso associati
    // *
    // * @param groupId Id del Gruppo
    // * @return Profilo del Gruppo insieme alla lista degli user-id degli
    // Utenti ad esso associati
    // * @throws DocerException
    // */
    // public IGroupInfo getGroup(String groupId) throws DocerException;
    //
    // /**
    // * Aggiorna il profilo di un'anagrafica Ente
    // *
    // * @param enteId Id dell'anagrafica Ente
    // * @param enteNewInfo Profilo dell'Ente contenente i metadati da
    // modificare
    // * @throws DocerException
    // */
    // public void updateEnte(IEnteId enteId, IEnteInfo enteNewInfo) throws
    // DocerException;
    //
    // /**
    // * Aggiora il profilo di un'anagrafica AOO
    // *
    // * @param aooId Id dell'anagrafica AOO
    // * @param aooNewInfo Profilo della AOO contenente i metadati da modificare
    // * @throws DocerException
    // */
    // public void updateAOO(IAOOId aooId, IAOOInfo aooNewInfo) throws
    // DocerException;
    //
    // /**
    // * Aggiora il profilo di un'anagrafica voce di Titolario
    // *
    // * @param titolarioId Id dell'anagrafica voce di Titolario
    // * @param titolarioNewInfo Profilo della voce di Titolario contenente i
    // metadati da modificare
    // * @throws DocerException
    // */
    // public void updateTitolario(ITitolarioId titolarioId, ITitolarioInfo
    // titolarioNewInfo) throws DocerException;
    //
    // /**
    // * Aggiora il profilo di un'anagrafica Fascicolo
    // *
    // * @param fascicoloId Id dell'anagrafica Fascicolo
    // * @param fascicoloNewInfo Profilo dell'anagrafica Fascicolo contenente i
    // metadati da modificare
    // * @throws DocerException
    // */
    // public void updateFascicolo(IFascicoloId fascicoloId, IFascicoloInfo
    // fascicoloNewInfo) throws DocerException;
    //
    // /**
    // * Aggiora il profilo di un'anagrafica custom
    // *
    // * @param customId Id dell'anagrafica custom
    // * @param customNewInfo Profilo dell'anagrafica custom contenente i
    // metadati da modificare
    // * @throws DocerException
    // */
    // public void updateCustomItem(ICustomItemId customId, ICustomItemInfo
    // customNewInfo) throws DocerException;
    //
    // /**
    // * Esegue l'aggiornamento del profilo di un Utente
    // *
    // * @param userId Id dell'Utente da aggiornare
    // * @param userNewInfo Il profilo dell'Utente contenente i metadati da
    // modificare
    // * @throws DocerException
    // */
    // public void updateUser(String userId, IUserProfileInfo userNewInfo)
    // throws DocerException;
    //
    // /**
    // * Esegue l'aggiornamento del profilo di un Gruppo
    // *
    // * @param groupId Id del Gruppo da aggiornare
    // * @param groupNewInfo Il profilo del Gruppo contenente i metadati da
    // modificare
    // * @throws DocerException
    // */
    // public void updateGroup(String groupId, IGroupProfileInfo groupNewInfo)
    // throws DocerException;
    //
    // /**
    // * Elimina l'associazione tra un Utente ed una lista di Gruppi a cui esso
    // e' associato
    // *
    // * @param userId Id dell'Utente
    // * @param groups List di Id dei Gruppi da cui l'Utente deve essere rimosso
    // * @throws DocerException
    // */
    // public void removeGroupsFromUser(String userId, List<String> groups)
    // throws DocerException;
    //
    // /**
    // * Elimina l'associazione tra un Gruppo ed una lista di Utenti ad esso
    // associati
    // *
    // * @param groupId Id del Gruppo
    // * @param users Id degli Utenti da rimuovere dal Gruppo
    // * @throws DocerException
    // */
    // public void removeUsersFromGroup(String groupId, List<String> users)
    // throws DocerException;
    //
    // /**
    // * Esegue l'associazione di un Utente ad una Lista di Gruppi
    // *
    // * @param userId Id dell'Utente
    // * @param groups Lista di Id dei Gruppi da associare all'Utente
    // * @throws DocerException
    // */
    // public void addGroupsToUser(String userId, List<String> groups) throws
    // DocerException;
    //
    // /**
    // * Esegue l'associazione ad un Gruppo di una lista di Utenti
    // * @param groupId Id del Gruppo
    // * @param users Lista di Id degli Utenti da associare al Gruppo
    // * @throws DocerException
    // */
    // public void addUsersToGroup(String groupId, List<String> users) throws
    // DocerException;
    //

    // public void setACLDocument(String docId, Map<String, EnumACLRights> acls)
    // throws DocerException;
    //
    // /**
    // * Recupera la ACL assegnate ad un documento
    // *
    // * @param docId Id del documeto
    // * @return Lista delle ACL del documento, Id di Utenti e Gruppi (key) -
    // permesso (value)
    // * @throws DocerException
    // */
    // public Map<String, EnumACLRights> getACLDocument(String docId) throws
    // DocerException;
    //
    // /**
    // * Recupera lo stato di blocco esclusivo di un documento
    // *
    // * @param docId Id del documento
    // * @return Oggetto che contiene le informazioni sullo stato di blocco
    // esclusico
    // * @throws DocerException
    // */
    // public ILockStatus isCheckedOutDocument(String docId) throws
    // DocerException;
    //
    // /**
    // * Esegue il blocco esclusivo di un documento
    // *
    // * @param docId Id del documento
    // * @throws DocerException
    // */
    // public void lockDocument(String docId) throws DocerException;
    //
    // /**
    // * Esegue lo sblocco di un documento in stato di blocco esclusivo
    // *
    // * @param docId Id del documento
    // * @throws DocerException
    // */
    // public void unlockDocument(String docId) throws DocerException;
    //
    // /**
    // * Esegue l'aggiornamento del profilo di un documento
    // *
    // * @param docId Id del documento
    // * @param metadata I metadati da modificare nel profilo del documento
    // * @throws DocerException
    // */
    // public void updateProfileDocument(String docId, Map<String, String>
    // metadata) throws DocerException;
    //
    // /**
    // * Esegue la cancellazione di un documento nel sistema documentale
    // *
    // * @param docId Id del documento
    // * @throws DocerException
    // */
    // public void deleteDocument(String docId) throws DocerException;
    //
    // /**
    // * Recupera l'ultima versione del File associato al documento
    // *
    // * @param docId Id del documento
    // * @param destinationFilePath Percorso fisico, univoco, su cui deve essere
    // scritto lo Stream
    // * del file nel caso in cui la sua dimensione superi il parametro
    // maxFileLength (in bytes)
    // * @param maxFileLength Dimensione massima del file fisico entro la quale
    // il file deve essere
    // * restituito come byte[], altrimenti, salvato su destinationFilePath
    // * @return Il file fisico se con dimensione minore o uguale a maxFilePath
    // altrimenti null
    // * @throws DocerException
    // */
    // public byte[] downloadLastVersion(String docId, String
    // destinationFilePath, long maxFileLength) throws DocerException;
    //
    // /**
    // * Recupera la lista delle versioni del documento
    // *
    // * @param docId Id del documento
    // * @return La lista degli Id delle versioni del documento
    // * @throws DocerException
    // */
    // public List<String> getVersions(String docId) throws DocerException;
    //
    // /**
    // * Crea una nuova versione del file del documento
    // *
    // * @param docId Id del documento
    // * @param file Il documento fisico
    // * @return Id della versione creata
    // * @throws DocerException
    // */
    // public String addNewVersion(String docId, InputStream file) throws
    // DocerException;
    //
    // /**
    // * Sostituisce l'ultima versione del file del documento
    // *
    // * @param docId Id del documento
    // * @param file Documento fisico
    // * @throws DocerException
    // */
    // public void replaceLastVersion(String docId,InputStream file) throws
    // DocerException;
    //
    // /**
    // * Recupera il file del documento nella versione richiesta
    // *
    // * @param docId Id del documento
    // * @param versionNumber Id della versione
    // * @param destinationFilePath Percorso fisico, univoco, su cui deve essere
    // scritto lo Stream
    // * del file nel caso in cui la sua dimensione superi il parametro
    // maxFileLength (in bytes)
    // * @param maxFileLength Dimensione massima del file fisico entro la quale
    // il file deve essere
    // * restituito come byte[], altrimenti, salvato su destinationFilePath
    // * @return Il file fisico della versione se con dimensione minore o uguale
    // a maxFilePath altrimenti null
    // * @throws DocerException
    // */
    // public byte[] downloadVersion(String docId, String versionNumber, String
    // destinationFilePath, long maxFileLength) throws DocerException;
    //
    // /**
    // * Esegue una ricerca tra le anagrafiche, per tipo
    // *
    // * @param type Tipo o TYPE_ID dell'anagrafica (ENTE, AOO, TITOLARIO,
    // FASCICOLO, oggetti custom...)
    // * @param searchCriteria Criteri di ricerca (criteri per la stessa key
    // sono in OR logico, per key diverse in AND logico)
    // * @param returnProperties Metadati che devono essere restituiti dalla
    // ricerca
    // * @return Lista dei profili trovati per il tipo di anagrafica cercata
    // * @throws DocerException
    // */
    // public List<Map<String,String>> searchAnagrafiche(String type,
    // Map<String, List<String>> searchCriteria, List<String> returnProperties)
    // throws DocerException;
    //
    // /**
    // * Esegue una ricerca tra gli Utenti del sistema documentale
    // *
    // * @param searchCriteria Criteri di ricerca (criteri per la stessa key
    // sono in OR logico, per key diverse in AND logico)
    // * @return Lista dei profili degli Utenti trovati
    // * @throws DocerException
    // */
    // public List<IUserProfileInfo> searchUsers(Map<String, List<String>>
    // searchCriteria) throws DocerException;
    //
    // /**
    // * Esegue una ricerca tra i Gruppi del sistema documentale
    // *
    // * @param searchCriteria Criteri di ricerca (criteri per la stessa key
    // sono in OR logico, per key diverse in AND logico)
    // * @return Lista dei profili dei Gruppi trovati
    // * @throws DocerException
    // */
    // public List<IGroupProfileInfo> searchGroups(Map<String, List<String>>
    // searchCriteria) throws DocerException;
    //
    // /**
    // * Recupera la History di un documento
    // *
    // * @param docId Id del documento
    // * @return Lista degli delle azioni eseguite sul documento (il numero ed
    // il tipo di storicizzazione delle azioni dipende dal sistema documentale)
    // * @throws DocerException
    // */
    // public List<IHistoryItem> getHistory(String docId) throws DocerException;
    //
    // /**
    // * Recupera i diritti equivalenti (o efficaci) di un Utente su un
    // documento considerando anche i permessi impliciti ed i permessi assegnati
    // ai suoi Gruppi di appartenenza
    // *
    // * @param docId Id del documento
    // * @param userid Id dell'Utente
    // * @return Diritti equivalenti (o efficaci) dell'Utente sul documento
    // * @throws DocerException
    // */
    // public EnumACLRights getEffectiveRights(String docId, String userid)
    // throws DocerException;
    //
    // /**
    // * Recupera i diritti equivalenti (o efficaci) di un Utente su un
    // documento considerando anche i permessi impliciti ed i permessi assegnati
    // ai suoi Gruppi di appartenenza
    // *
    // * @param id dell' anagrafica
    // * @param userid Id dell'Utente
    // * @return Diritti equivalenti (o efficaci) dell'Utente sull'anagrafica
    // * @throws DocerException
    // */
    // public EnumACLRights getEffectiveRightsAnagrafiche(String type,
    // Map<String,String> id, String userid) throws DocerException;
    //
    // /**
    // * Recupera i diritti equivalenti (o efficaci) di un Utente su un
    // documento considerando anche i permessi impliciti ed i permessi assegnati
    // ai suoi Gruppi di appartenenza
    // *
    // * @param folderId id della folder
    // * @param userid Id dell'Utente
    // * @return Diritti equivalenti (o efficaci) dell'Utente sulla folder
    // * @throws DocerException
    // */
    // public EnumACLRights getEffectiveRightsFolder(String folderId, String
    // userid) throws DocerException;
    //
    // /**
    // * Verifica la validita' di un Ticket di sessione
    // *
    // * @param userId Id dell'Utente
    // * @param codiceEnte Identificativo univoco dell'Ente su cui si intente
    // operare
    // * @param ticket Ticket di sessione
    // * @return true se il Ticket di sessione e' ancora valido altrimenti false
    // * @throws DocerException
    // */
    // public boolean verifyTicket(String userId, String codiceEnte, String
    // ticket) throws DocerException;
    //
    // /**
    // * Assegna le ACL ad una voce di Titolario (con sovrascrittura)
    // *
    // * @param titolarioId Id della voce di Titolario
    // * @param acls Lista delle ACL della voce di Titolario: ad ogni Id di
    // Utente o Gruppo (key), corrisponde un permesso (value)
    // * @throws DocerException
    // */
    // public void setACLTitolario(ITitolarioId titolarioId, Map<String,
    // EnumACLRights> acls) throws DocerException;
    //
    // /**
    // * Recupera la lista dei permessi di una voce di Titolario
    // *
    // * @param titolarioId Id della voce di Titolario
    // * @return Lista delle ACL della voce di Titolario: ad ogni Id di Utente o
    // Gruppo (key), corrisponde un permesso (value)
    // * @throws DocerException
    // */
    // public Map<String, EnumACLRights> getACLTitolario(ITitolarioId
    // titolarioId) throws DocerException;
    //
    // /**
    // * Assegna le ACL ad un Fascicolo (con sovrascrittura)
    // *
    // * @param fascicoloId Id del Fascicolo
    // * @param acls Lista delle ACL del Fascicolo: ad ogni Id di Utente o
    // Gruppo (key), corrisponde un permesso (value)
    // * @throws DocerException
    // */
    // public void setACLFascicolo(IFascicoloId fascicoloId, Map<String,
    // EnumACLRights> acls) throws DocerException;
    //
    // /**
    // * Recupera la lista dei permessi di un Fascicolo
    // *
    // * @param fascicoloId Id del Fascicolo
    // * @return Lista delle ACL del Fascicolo: ad ogni Id di Utente o Gruppo
    // (key), corrisponde un permesso (value)
    // * @throws DocerException
    // */
    // public Map<String, EnumACLRights> getACLFascicolo(IFascicoloId
    // fascicoloId) throws DocerException;
    //
    // /**
    // * Esegue la creazione di una Folder nel sistema documentale
    // *
    // * @param folderInfo Profilo della Folder
    // * @return Id della Folder
    // * @throws DocerException
    // */
    // public String createFolder(IFolderInfo folderInfo) throws DocerException;
    //
    // /**
    // * Esegue l'aggiornamento del profilo di una Folder nel sistema
    // documentale
    // *
    // * @param folderId Id della Folder
    // * @param folderNewInfo Il profilo della Folder contenente i metadati da
    // modificare
    // * @throws DocerException
    // */
    // public void updateFolder(String folderId, IFolderInfo folderNewInfo)
    // throws DocerException;
    //
    // /**
    // * Esegue la cancellazione di una Folder dal sistema documentale
    // * @param folderId Id della Folder
    // * @throws DocerException
    // */
    // public void deleteFolder(String folderId) throws DocerException;
    //
    // /**
    // * Assegna le ACL ad una Folder (con sovrascrittura)
    // *
    // * @param folderId Id della Folder
    // * @param acls Lista delle ACL della Folder: ad ogni Id di Utente o Gruppo
    // (key), corrisponde un permesso (value)
    // * @throws DocerException
    // */
    // public void setACLFolder(String folderId, Map<String, EnumACLRights>
    // acls) throws DocerException;
    //
    // /**
    // * Recupera la lista dei permessi di una Folder
    // *
    // * @param folderId Id della Folder
    // * @return Lista delle ACL della Folder: ad ogni Id di Utente o Gruppo
    // (key), corrisponde un permesso (value)
    // * @throws DocerException
    // */
    // public Map<String, EnumACLRights> getACLFolder(String folderId) throws
    // DocerException;
    //
    // /**
    // * Recupera la lista dei documenti contenuti nella Folder
    // * @param folderId Id della Folder
    // * @return Lista degli Id dei documenti contenuti nella Folder
    // * @throws DocerException
    // */
    // public List<String> getFolderDocuments(String folderId) throws
    // DocerException;
    //
    // /**
    // * Aggiunge documenti ad una Folder (anche come associazione logica,
    // dipende dal sistema documentale)
    // *
    // * @param folderId Id della Folder
    // * @param documents Lista degli Id dei documenti da aggiungere alla Folder
    // * @throws DocerException
    // */
    // public void addToFolderDocuments(String folderId, List<String> documents)
    // throws DocerException;
    //
    // /**
    // * Rimuove documenti da una Folder
    // * @param folderId Id della Folder
    // * @param documents Lista di Id dei documenti da rimuovere dalla Folder
    // * @throws DocerException
    // */
    // public void removeFromFolderDocuments(String folderId, List<String>
    // documents) throws DocerException;
    //
    // /**
    // * Esegue una ricerca di Folder
    // *
    // * @param searchCriteria Criteri di ricerca (criteri per la stessa chiave
    // sono in OR logico, per chiavi diverse in AND logico)
    // * @param returnProperties Metadati che devono essere restituiti dalla
    // ricerca
    // * @param maxResults Numero massimo dei risultati
    // * @param orderby Criteri di ordinamento dei risultati, nome del metadato
    // (key) - ASC/DESC (value)
    // * @return DataTable che contiene un array di DataRow - ogni DataRow
    // rappresenta il profilo di un risultato
    // * @throws DocerException
    // */
    // public DataTable<String> searchFolders(Map<String, List<String>>
    // searchCriteria, List<String> returnProperties,int maxResults,
    // Map<String,EnumSearchOrder> orderby) throws DocerException;
    //
    // /**
    // * Aggiunge una nuova Versione Avanzata
    // *
    // * @param docIdLastVersion docId del documento a cui va aggiunta una
    // Versione Avanzata
    // * @param docIdNewVersion docId del documento che rappresenta la Versione
    // Avanzata
    // * @throws DocerException
    // */
    // public void addNewAdvancedVersion(String docIdLastVersion, String
    // docIdNewVersion) throws DocerException;
    //
    //
    // /**
    // *
    // * @param docId id del documento di riferimento
    // * @return La lista dei docId di tutte le Versioni Avanzate del documento
    // compreso il docId stesso
    // * @throws DocerException
    // */
    // public List<String> getAdvancedVersions(String docId) throws
    // DocerException;
    //
    //
    @Test
    public void testLogoutAdmin() {
	try {
	    getProvider(admin).logout();
	}
	catch (Exception e) {
	    System.out.println(e);
	}
    }

    @Test
    public void dbConservazione() {

	Map<String, String> properties = new HashMap<String, String>();
	properties.put("XML_TEST", "VAL_XML_TEST");
	properties.put("XML_1", "VAL_XML_1");
	properties.put("XML_2", "VAL_XML_2");
	properties.put("DOCID", "641684681");

	String url = "jdbc:mysql://localhost/alfresco";

	try {
	    Class.forName("com.mysql.jdbc.Driver");
	}
	catch (ClassNotFoundException e) {
	    System.out.println("ClassNotFoundException: " + e.getMessage());
	    return;
	}
	Connection conn;
	try {
	    conn = DriverManager.getConnection(url, "root", "root");
	}
	catch (SQLException e) {
	    System.out.println(e.getMessage());
	    return;
	}

	long idJob = 10000;

	String field_query = "";
	String update = "";

	PreparedStatement preparedStatement = null;
	Map<Integer, String> indexValuesMap = new HashMap<Integer, String>();
	Integer docIdIndex = 0;

	indexValuesMap.clear();

	docIdIndex = 0;
	field_query = "";

	for (String key : properties.keySet()) {

	    String fieldName = key.toUpperCase();
	    String fieldValue = properties.get(key);
	    int index = indexValuesMap.size() + 1;

	    if (fieldName.equals("DOCID")) {
		fieldName = "docId";
		docIdIndex = index;
	    }

	    indexValuesMap.put(index, fieldValue);
	    field_query += fieldName + " = ?, ";
	}

	update = "update CCD_JOB p set " + field_query + " DT_ULT_MOD = now() where ID_JOB = ?";

	try {
	    preparedStatement = (PreparedStatement) conn.prepareStatement(update);
	}
	catch (SQLException e) {
	    System.out.println(e.getMessage());
	    return;
	}

	for (Integer valueIndex : indexValuesMap.keySet()) {
	    String value = indexValuesMap.get(valueIndex);
	    if (valueIndex == docIdIndex) {
		try {
		    preparedStatement.setLong(valueIndex, new Long(value).longValue());
		}
		catch (NumberFormatException e) {
		    System.out.println(e.getMessage());
		    return;
		}
		catch (SQLException e) {
		    System.out.println(e.getMessage());
		    return;
		}
		continue;
	    }
	    try {
		preparedStatement.setString(valueIndex, value);
	    }
	    catch (SQLException e) {
		System.out.println(e.getMessage());
		return;
	    }
	}

	try {
	    preparedStatement.setLong(indexValuesMap.size() + 1, idJob);
	}
	catch (SQLException e) {
	    System.out.println(e.getMessage());
	    return;
	}

	System.out.println(preparedStatement.toString().replaceAll(".+:", ""));
	String a = "";

	try {
	    conn.close();
	}
	catch (SQLException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    @Test
    public void execute() throws Exception {

	Map<String, String> properties = new HashMap<String, String>();
	properties.put("XML_PROFILO_DOC_PRINCIPALE", "VAL_XML_PROFILO_DOC_PRINCIPALE");
	properties.put("TIPO_DOC", "VAL_TIPO_DOC");
	properties.put("FORZA_ACCETTAZIONE", "true");
	properties.put("DOCID", "641684681");

	Map<Integer, String> indexValuesMap = new HashMap<Integer, String>();
	Integer docIdIndex = 0;

	String url = "jdbc:mysql://192.168.0.174/CCD";

	try {
	    Class.forName("com.mysql.jdbc.Driver");
	}
	catch (ClassNotFoundException e) {
	    System.out.println("ClassNotFoundException: " + e.getMessage());
	    return;
	}
	Connection conn;
	try {
	    conn = DriverManager.getConnection(url, "alfresco", "alfresco");
	}
	catch (SQLException e) {
	    System.out.println(e.getMessage());
	    return;
	}
	PreparedStatement preparedStatement = null;
	try {

	    indexValuesMap.clear();

	    long autoIncKeyFromFunc = -1;

	    String columns = "ID_JOB, STATO, DT_INS, DT_ULT_MOD, DT_CHIAMATA";
	    String values = "default, 'A', now(), NULL, NULL";

	    docIdIndex = 0;

	    for (String key : properties.keySet()) {

		String fieldName = key.toUpperCase();
		String fieldValue = properties.get(key);
		int index = indexValuesMap.size() + 1;

		if (fieldName.equals("DOCID")) {
		    fieldName = "docId";
		    docIdIndex = index;
		}

		indexValuesMap.put(index, fieldValue);

		columns += ", " + fieldName;
		values += ", ?";
	    }

	    // INSERT INTO table_name (column1,column2,column3,...)
	    // VALUES (value1,value2,value3,...);

	    String insertMysql = "insert into CCD_JOB (" + columns + ") values (" + values + ")";

	    preparedStatement = (PreparedStatement) conn.prepareStatement(insertMysql, new String[] { "ID_JOB" });

	    for (Integer valueIndex : indexValuesMap.keySet()) {
		String value = indexValuesMap.get(valueIndex);
		if (valueIndex == docIdIndex) {
		    preparedStatement.setLong(valueIndex, new Long(value).longValue());
		    continue;
		}
		preparedStatement.setString(valueIndex, value);
	    }

	    System.out.println(preparedStatement.toString().replaceAll(".+:", ""));

	    preparedStatement.executeUpdate();

	    ResultSet rs = preparedStatement.executeQuery("SELECT LAST_INSERT_ID()");
	    if (rs.next()) {
		autoIncKeyFromFunc = rs.getLong(1);
	    }
	    else {
		// throw an exception from here
	    }

	    System.out.println(autoIncKeyFromFunc);

	}
	finally {

	    if (preparedStatement != null) {
		preparedStatement.close();
		preparedStatement = null;
	    }

	    if (conn != null) {
		conn.close();
		conn = null;
	    }
	}
    }

    @Test
    public void compareModels() {

	BufferedWriter out = null;
	try {
	    File dir = new File("C:/");
	    String filename = "docareaModelSimple.1.3.4.Match-" + new Date().getTime() + ".txt";

	    File f = new File(dir, filename);
	    out = new BufferedWriter(new FileWriter(f.getAbsolutePath()));

	    String modelName1 = "/docareaModelSimple.1.3.4.xml";
	    InputStream is = this.getClass().getResourceAsStream(modelName1);

	    StringWriter writer = new StringWriter();
	    IOUtils.copy(is, writer, "UTF-8");
	    String model1 = writer.toString();
	    writer.close();

	    String modelName2 = "/docareaModelSimple.cloud.xml";
	    is = this.getClass().getResourceAsStream(modelName2);

	    writer = new StringWriter();
	    IOUtils.copy(is, writer, "UTF-8");
	    String model2 = writer.toString();
	    writer.close();

	    out.write("allineamento modello alfresco alla 1.3.4: ");
	    out.write("\n");
	    out.write(modelName1);
	    out.write("\n");
	    out.write(modelName2);
	    out.write("\n");
	    out.write("");
	    out.write("\n");
	    out.write("\n");
	    model1 = model1.replaceAll("\\<!--[\\s\\S]*?--\\>", "");
	    model2 = model2.replaceAll("\\<!--[\\s\\S]*?--\\>", "");

	    String regexpPropName = "\\< *(type|constraint|property) +name\\=(\"docarea:[a-zA-Z0-9]+\")";
	    Pattern patternPropName = Pattern.compile(regexpPropName, Pattern.MULTILINE);

	    Matcher matcherModel = patternPropName.matcher(model1);

	    int count = 0;
	    while (matcherModel.find()) {

		count++;
		String propname = matcherModel.group(2);
		String regexpPropNode = "\\< *" + matcherModel.group(1) + "[^\\<\\>]* +name\\=" + matcherModel.group(2) + "[^\\<\\>]*\\>[\\s\\S]*?< */ *" + matcherModel.group(1) + " *>";
		Pattern patternPropNode = Pattern.compile(regexpPropNode, Pattern.MULTILINE);

		Matcher matcherModel1 = patternPropNode.matcher(model1);
		Matcher matcherModel2 = patternPropNode.matcher(model2);

		if (!matcherModel1.find()) {

		    out.write("\n");
		    out.write("\n");
		    out.write("\n");
		    out.write("ERROR proprieta' " + matcherModel.group(2) + " esiste SU 1.3.4 ma non trovato nodo xml su modello");
		    continue;
		}

		if (!matcherModel2.find()) {

		    String regexpPropNode22 = "\\< *[a-zA-Z0-9]+[^\\<\\>]* +name\\=" + matcherModel.group(2);
		    Pattern patternPropName22 = Pattern.compile(regexpPropNode22, Pattern.MULTILINE);
		    Matcher matcherModel22 = patternPropName22.matcher(model2);

		    if (matcherModel22.find()) {
			out.write("\n");
			out.write("\n");
			out.write("\n");
			out.write("\n");
			out.write("ATTENZIONE!!! TIPO MODIFICATO:");
			out.write("\n");
			out.write("\n");
			out.write(matcherModel1.group(0));
			out.write("\n");
			out.write("-------------------------------------------");
			out.write("\n");
			out.write(matcherModel22.group(0) + " ...");
			continue;
		    }

		    out.write("\n");
		    out.write("\n");
		    out.write("\n");
		    out.write("\n");
		    out.write("DA AGGIUNGERE: " + matcherModel1.group(0));
		    continue;
		}

		String prop1 = matcherModel1.group(0).replaceAll("\\s+", " ");
		String prop2 = matcherModel2.group(0).replaceAll("\\s+", " ");

		if (prop1.equals(prop2)) {
		    continue;
		}

		out.write("\n");
		out.write("\n");
		out.write("\n");
		out.write("\n");
		out.write("DIFFERENZA: <" + matcherModel.group(1) + ">");
		out.write("\n");
		out.write("\n");
		out.write(matcherModel1.group(0));
		out.write("\n");
		out.write("----------------------------------------------------------------------");
		out.write("\n");
		out.write(matcherModel2.group(0));

	    }

	}
	catch (Exception e) {
	    try {
		if (out != null) {
		    out.write(e.getMessage());
		}

	    }
	    catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	    }
	    System.out.println(e.getMessage());
	}
	finally {
	    if (out != null) {
		try {
		    out.close();
		}
		catch (IOException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
	    }
	}

    }

    @Test
    public void testSetACLTitolarioDatagraph() {

	Provider provider = null;
	try {

	    provider = new Provider();

	    ILoggedUserInfo uinfo = new LoggedUserInfo();
	    String ticket = provider.login("admin", "Kdm.2001", null);

	    uinfo.setUserId("admin");
	    uinfo.setCodiceEnte(null);
	    uinfo.setTicket(ticket);

	    provider.setCurrentUser(uinfo);

	    TitolarioId titolarioId = new TitolarioId();
	    titolarioId.setCodiceEnte("ENTE_DATAGRAPH");
	    titolarioId.setCodiceAOO("AOO_DATAGRAPH");
	    titolarioId.setClassifica("1.0.0");

	    Map<String, EnumACLRights> acls = new HashMap<String, EnumACLRights>();
	    acls.put("ENTE_DATAGRAPH", EnumACLRights.fullAccess);
	    provider.setACLTitolario(titolarioId, acls);

	    System.out.println(acls.toString());

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
    public void testFormatString() {
	String template = "{%s,%s,%s,%s,%s,%s}";
	String formatted = String.format(template, null, "cod_aoo", "classifica", null, "progr_fascicolo", null);

	System.out.println(formatted);

    }

    @Test
    public void testSearchAnagraficheCustom() {

	Provider provider = null;
	try {
	    provider = new Provider();
	    String token = provider.login("admin", "admin", "");

	    ILoggedUserInfo lui = new LoggedUserInfo("admin", "", token);

	    provider.setCurrentUser(lui);

	    Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
	    searchCriteria.put("COD_ENTE", Arrays.asList("ENTEPROVA"));
	    // searchCriteria.put("PARENT_CLASSIFICA",
	    // Arrays.asList("01","02","03"));
	    //
	    //
	    List<String> returnProperties = new ArrayList<String>();
	    returnProperties.add("COD_ENTE");
	    returnProperties.add("COD_AOO");
	    returnProperties.add("ENABLED");

	    List<Map<String, String>> res = provider.searchAnagrafiche("LISTA_REGISTRI", searchCriteria, returnProperties);

	    for (int i = 0; i < res.size(); i++) {
		System.out.println("-------------------------");
		for (String key : res.get(i).keySet()) {
		    System.out.println(key + "=" + res.get(i).get(key));
		}
	    }



	}
	catch (Exception e) {
	    System.out.println(e);
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
    public void testModel() throws IOException {

	String model = FileUtils.readFileToString(new File("C:/Users/user1/eclipse_workspace/docer135/1.3.5/dist/alfresco/extension/docareaModelSimple.xml"));
	model = model.replaceAll("<!--[\\s\\S]*?-->", "<!--REPLACED-->");
	
	String old = FileUtils.readFileToString(new File("C:/Users/user1/Desktop/CARPI/docareaModelSimple.xml"));
	old = old.replaceAll("<!--[\\s\\S]*?-->", "<!--REPLACED-->");
	
	String modelAlfrescoProviderConfig = FileUtils.readFileToString(new File("src/test/resources/alfresco_provider_config.xml"));
	modelAlfrescoProviderConfig = modelAlfrescoProviderConfig.replaceAll("<!--[\\s\\S]*?-->", "<!--REPLACED-->");
	
	
	String oldAlfrescoProviderConfig = FileUtils.readFileToString(new File("C:/Users/user1/Desktop/CARPI/alfresco_provider_config.xml"));
	oldAlfrescoProviderConfig = oldAlfrescoProviderConfig.replaceAll("<!--[\\s\\S]*?-->", "<!--REPLACED-->");
	
	Pattern regex = Pattern.compile("(\\<property +name=\"docarea:[^\"]+\")");
	
	boolean ok = true;
	Matcher m1 = regex.matcher(model);
        while(m1.find()) {
                        
            //System.out.println(m1.group(1));
            
            if(old.contains(m1.group(1))){
        	continue;
            }
            ok = false;
            System.out.println("----------> " +m1.group(1));
        }
        

        if(!ok){
            System.out.println("ERRORS");
            return;
        }
        
        m1 = regex.matcher(old);
        while(m1.find()) {
            
            String strToFind = m1.group(1).replaceAll(".+docarea\\:", "}").replaceAll("\"", "")+";";
            
            if(oldAlfrescoProviderConfig.contains(strToFind)){
        	continue;
            }
            
            System.out.println(">>>>>> " +m1.group(1));
        }
        
        
        regex = Pattern.compile("\\<entry key=\"([^\"]+)\"[\\s\\S]*?<");
	
	m1 = regex.matcher(modelAlfrescoProviderConfig);
        while(m1.find()) {
                        
            String strToFind = "<entry key=\"" +m1.group(1) +"\"";
            //System.out.println(strToFind);
            
            if(oldAlfrescoProviderConfig.contains(strToFind)){
        	continue;
            }
            if(strToFind.contains(" key=\"#")) {
        	continue;
            }
            System.out.println("----------> " +m1.group(1));
        }
        

        
	
    }
    
    
    @Test
    public void testAddLink() {

	Provider provider = null;
	try {
	    provider = new Provider();
	    String token = provider.login("admin", "admin", "");

	    ILoggedUserInfo lui = new LoggedUserInfo("admin", "", token);

	    provider.setCurrentUser(lui);

	    //provider.addLink("workspace://SpacesStore/4dbe95a6-b371-4229-921e-b5bf2605aae1", "workspace://SpacesStore/f90defaf-4374-495b-8bb5-616f2e754c8a", "test.txt");
	    //provider.addLink("4dbe95a6-b371-4229-921e-b5bf2605aae1", "bae5c288-bafa-40c0-8b8a-40bf91d79692", "test.txt");
	    //provider.addLink2("4dbe95a6-b371-4229-921e-b5bf2605aae1", "f90defaf-4374-495b-8bb5-616f2e754c8a","test.txt");
	    

	}
	catch (Exception e) {
	    System.out.println(e);
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
    public void testRaplaceFile() throws ParseException {

	Date date = ISO8601DateFormat.getInstance().parse("2014-10-28T00:00:00.000+01:00");
    }

    
    @Test
    public void testGetVersion() {

	InputStream is = null;

	Provider provider = null;
	try {

	    provider = new Provider();

	    ILoggedUserInfo uinfo = new LoggedUserInfo();
	    String ticket = provider.login("admin", "082c2f28df91f2345f7481de01380371", null);

	    uinfo.setUserId("admin");
	    uinfo.setCodiceEnte(null);
	    uinfo.setTicket(ticket);

	    provider.setCurrentUser(uinfo);

	    List<String> versions = provider.getVersions("14076");
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
	    
	    try {
		provider.logout();
	    }
	    catch (DocerException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}

    }
}
