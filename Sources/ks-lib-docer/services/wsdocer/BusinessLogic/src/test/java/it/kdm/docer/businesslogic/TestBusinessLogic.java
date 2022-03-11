package it.kdm.docer.businesslogic;

import static org.junit.Assert.*;
import it.kdm.docer.businesslogic.configuration.enums.EnumBLPropertyType;
import it.kdm.docer.businesslogic.configuration.types.FieldDescriptor;
import it.kdm.docer.commons.Config;
import it.kdm.docer.commons.TicketCipher;
import it.kdm.docer.commons.Utils;
import it.kdm.docer.sdk.Constants;
import it.kdm.docer.sdk.EnumACLRights;
import it.kdm.docer.sdk.EnumSearchOrder;
import it.kdm.docer.sdk.EnumStatoArchivistico;
import it.kdm.docer.sdk.classes.KeyValuePair;
import it.kdm.docer.sdk.exceptions.DocerException;
import it.kdm.docer.sdk.interfaces.IKeyValuePair;
import it.kdm.docer.sdk.interfaces.ISearchItem;

import java.io.ByteArrayInputStream;
import java.io.Console;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.security.KeyException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.stream.XMLStreamException;

import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.jaxen.JaxenException;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.junit.Test;

public class TestBusinessLogic {

    static String ente = "";
    static String userAdmin = "admin";
    static String userAdminPassword = "082c2f28df91f2345f7481de01380371";

    static String token = null;

    static String cod_ente = "EMR";
    static String cod_aoo = "AOO_EMR";
    static BusinessLogic businessLogic = null;

    static String docid_1 = "2339";
    static String docid_2 = "3713";
    static String docid_3 = "3716";

    static String docid_4 = "";
    static String docid_5 = "";
    static String docid_6 = "";

    static String user1 = "user1";
    static String user2 = "user2";
    static String user3 = "user3";

    private static String regexpFascicoliSec = "^([^/]+)/([12][0-9][0-9][0-9])/(.+)$";
    private static Pattern patternFascicoliSec = Pattern.compile(regexpFascicoliSec);
    private static int classificaPosition = 1;
    private static int annoFascicoloPosition = 2;
    private static int progrFascicoloPosition = 3;

    @Test
    public void testMapsAreEqual() {

	Map<String, EnumACLRights> map1 = new HashMap<String, EnumACLRights>();
	Map<String, EnumACLRights> map2 = new HashMap<String, EnumACLRights>();
	map1.put("aa", EnumACLRights.undefined);
	map2.put("aa", EnumACLRights.undefined);
	map2.put("ab", EnumACLRights.undefined);
	
	System.out.println(mapsAreEqual(map1, map2));
    }

    private boolean mapsAreEqual(Map<String, EnumACLRights> map1, Map<String, EnumACLRights> map2) {

	for (String key : map1.keySet()) {
	    EnumACLRights value1 = map1.get(key);
	    EnumACLRights value2 = map2.get(key);
	    if (!value1.equals(value2)) {
		return false;
	    }
	}

	for (String key : map2.keySet()) {
	    EnumACLRights value2 = map2.get(key);
	    EnumACLRights value1 = map1.get(key);
	    if (!value2.equals(value1)) {
		return false;
	    }
	}

	return true;
    }

    
    @Test
    public void testTicket() throws KeyException{
	TicketCipher cipher = new TicketCipher();
	
	String ticket = "auth:standard|uid:admin|ipaddr:192.168.0.50|app:|ticketDocerServices:KxH8ivy0btF5fBy_KWLre014lWD4FVmGrYmbPGD7remdWFXbsxtdhRiyS2yWP0Ddrccdo2BvR5QdDo9crEmYZqB4VOfPN144JBsOLDSrD5FyuDYGO4Rg-WPuiVzP2FLSMrgU06Hu4Z8j-LSQGH1IxvvRQrDmvG08MRLI4r_8A9avpjKTgXYM82CxymGhJfA_4zciJixhLYuj2CWTEeoQm060r40od_dv9YfII8ynkBs|ticketWSRegistrazione:mNi5zyQ_C1dmhMfWLAVWUu0EDqWofvLYiyTFFdqUK8jepXn3etEA-2eex8XV2ZmNLj3-A9Mwjg1ko61JCQhLTM4ONmhKRvwH5wnTaX4izcRGmq2Cb50I5lO-_6V_IMzpT2Lauj96DgEKKCqhQFxPiccbKGQUl39knYOxoCBQjC74Jkt2NObi1bu7fMVQdk_wd5QaEV0Ava4oIsk60lkzuwr0QGMrSeeGckfduqtCEvmiMIoZqUSSOvmSj2iYxeqMmw9MDb6U2cuTsC5RO9-JGXKXYIZ1LKcYrWJPPT8sHZMlXezhd4-AfjXPmQV1ajxHN95a5OVXdmOfmue1kMYGa5zIPK8wOQn2FfXEPasrXjHxYER8jELAFwtX-pi1TgXufcDMyTIpfJUwqgUptvu3W0AetcYVhCZVwsZ9XKxNSOUKbhJZUUhwD69AjbbRXcogJ2ceFj1dw6oGbo8u5RNyB4N18U_Bbfc2zZmgrrbiUV4f5NVdTKfXDJG9v5zIyrdY|ticketWSProtocollazione:k69GXz403e6eWSFL--vjIcf4NyXaQli6dYMGBeI_ngHhdpwI_Lvy2IHBtiqDw3BvPisxf_Bmk4IRr-90YmLl9t4R2UT954B7TzgFc81ksb6PIJqTLQsvm1T7C3xvY2cyDgTUPsXkqhke0UcIMdfB4KTh5J_AYb1ATc5tOYUL32noeiwELeT9Sxj-hpPhHWmHlTxVknHZnAvCldK1YtX8TUC6tN6ku4H-vU1_pNhe35tWvuL_oiiI4Z5dTtOf_1YPGWp7SycpKuhylAAGLenaD8SQTkeiAq4wkzenPspIuHx5oE3pzvYB-L1iPtd8MZUesZO3rJM0IJM6Jm8o4vYxkvZdFs5u03Og-GnP2VxRHu2gx-e9LI_v8crKhGPqVBrgKcTumnQOjz6jGL38m2bhHVio6wKk_-F0NOzjgz65Roh6-_uthaWQXmmT2a-z7bW8MYCY9dZRMyBIejUfWuYigrhPWP0Xj6mrSDm1YhnxW8k|ticketWSFascicolazione:i8QqtE3s-0cdQnNV8fRTRAaBEKzu0R9bf8y1G2-LnlR_BptDuDrLwjxqWsueZzGEXlg_xOWi0ofKDdyuM_Q0TPmtV1N40hGoQpLxSSvtidDLdNH9-3EVuChV2OmNBaILxodBJX9cGj23eVMU7vbHvbsgtoZ1qmQRbtDRWlyYn1gIm7PVMBZb0wsI5XICBLDdSgVHXo6GB1NLw81-nDstpJLu8WbmegStomyFGb6Sif-kqQDPEU-T2I-VbTJ0hGQ9SgwTm06CqYrugvfMTeJshVjqSUS8C7gE7AKpk9qhU9uBsAgLJwdVZxLJTGSQnlI07O2pNl8h2HVwhzBx0ou6PIY9VVecHEGhXfIFkThRrzX2H9kADmWcEgx1LX6XyP2lN_2YZy5x_3pdKZKmSx7G3HqdvUk5e3NtoIktmqV_Fg8ECuMZTx-Yb8ntOEM1BxQjbTk_ijqPd4GnzzVyxDG0MUvN5l3LczLsK8UGWc_bDcfELqVCtSBN5b0im1AEPOOED8TsPU1HEftZhLVwYy0YvxyYQ3MMjNEt1-IUdzWFtH4OWPYQxBCcqyLeK7SagG74pKkAzxFPk9iPlW0ydIRkPUGBFV9fow50Hipjc9TVYLJVn-A4hcwGEkD5Btl5Pk0w8h7yRQhArN18unpgw7rhQhauSL1VsBNToQpWawbk5WlCtQUQIblsRgZgbstoDaPJsvfc_6r7LC9NhmEPQDjDLp7OMtITdRycT6NLROjS9FWkZdfWJJpjs1xRcbV-_aynVJV6jQNtgu9l9YvrxPO9iTkVQuCW4bKwj2l-UUGBzaGdnjLWC_-sbJ8hZxo_xWVSmip6w3srvmPnJDrj6hsZb6pQQPyCan-w3F0QNm4UZeY2hcoFYFC7HIK1LVNLdLjRKgk8wJmvgURA-81gui4PeFyjk6j_WcmBPQXS8gA5T9lDmJOgs1zP_o_Eo_bwIPJRjoa9w1jgK-gTktkQa7z04enX6tdHgQXVWChw1VMY-DsLdcp2vqD8kTHiUklu8hXJ7nuBz_doXqpvrnfcnX-oFJ92s2mIJxA4zFsQUQImXeSVIld5cIupNS3vOCMkuc-d5xFb7QTt4x4tINTcFc51AXn2CxJt49EjJNLZDsw7eYyYBcEcdb8G23pTCmJZ-qhU|ticketWSVerificaDocumenti:XDzRo9PAYofHSDZcGFF_2vAGxTYkQuh7ks5cM3yPTlw-VXyRDTldkyeNVh7W9zHk0enlFCZcDjphxhJ4RTV2D9z5Ks1Jv56dAw29_F9fKSUDFSPf4Kvzn4-3QCBrg1TJ-VFugR4s1hSfXbYw8KqX9aXmj4lztIClAtmOvqKQnmhS0_LGOL-oI6fSfbnIPyLVhwm3Zz3uKpdr9_M-C4DtPG8Jk-hBCNrO1xTaPh45W3ItqljP6UC0noyqPUjH6elDgnQmti61xd5Y9ECDkIFV4LHcXSQK8ZWsHUUMAMBktQR5PGK4PgsF6NjqmUI7_Ges45oHoycAAxm2VzyU8i1qeXVxckbbwiYusP0KIqcxJIMkZlqh8N31FzfCO6RgU4-m|ticketWSConservazione:KxH8ivy0btF5fBy_KWLre014lWD4FVmGrYmbPGD7remdWFXbsxtdhRiyS2yWP0Ddrccdo2BvR5QdDo9crEmYZqB4VOfPN144JBsOLDSrD5FyuDYGO4Rg-WPuiVzP2FLSMrgU06Hu4Z8j-LSQGH1IxvvRQrDmvG08MRLI4r_8A9avpjKTgXYM82CxymGhJfA_4zciJixhLYuj2CWTEeoQm060r40od_dv9YfII8ynkBs|ticketWSConservazioneBatch:KxH8ivy0btF5fBy_KWLre014lWD4FVmGrYmbPGD7remdWFXbsxtdhRiyS2yWP0Ddrccdo2BvR5QdDo9crEmYZqB4VOfPN144JBsOLDSrD5FyuDYGO4Rg-WPuiVzP2FLSMrgU06Hu4Z8j-LSQGH1IxvvRQrDmvG08MRLI4r_8A9avpjKTgXYM82CxymGhJfA_4zciJixhLYuj2CWTEeoQm060r40od_dv9YfII8ynkBs|userGroup:ALFRESCO_ADMINISTRATORS;EMAIL_CONTRIBUTORS;O_UO_PROPONENTE_1;RUP_UO_PROPONENTE_1;R_UO_APPALTI_CONTRATTI;SYS_ADMINS;S_UO_SEGRETERIA;UO_APPALTI_CONTRATTI;UO_PROPONENTE_1;UO_SEGRETERIA;site_swsdp_SiteManager|";
//	    ticket = Utils.addTokenKey(ticket, "codiceEnte", "codiceEnte");
//	    ticket = Utils.addTokenKey(ticket, "userid", "userid");
//	    ticket = Utils.addTokenKey(ticket, "dmsticket", "dmsticket");
//
//	    
//	    System.out.println(ticket);
//	    
//	    ticket = cipher.encryptTicket(ticket);
//	    
//	    System.out.println(ticket);
//	    
//	    
		ticket = Utils.extractTokenKey(ticket, String.format("ticket%s", "WSRegistrazione"));
	
	    ticket = cipher.decryptTicket(ticket);
	    //ticket = cipher.decryptTicket(ticket);
	    
	    System.out.println(ticket);
	    
	    
    }
    
    @Test
    public void testCollection() throws DocerException {

	List<String> list = new ArrayList<String>();
	list.add("a");
	list.add("a");
	list.add("a");
	list.add("b");
	list.add("c");
	System.out.println(list.size());

	List<String> list2 = new ArrayList<String>();
	list2.add("a");
	list2.add("a");
	list2.add("a");
	list2.add("a");
	list2.add("b");
	System.out.println(list2.size());

	list.addAll(list2);

	System.out.println(list.size());

	System.out.println(list.toString());

	    Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
	     searchCriteria.put("COD_ENTE", Arrays.asList("EMR","EMR2"));
	     searchCriteria.put("COD_AOO", Arrays.asList("C-I840-01"));
	     searchCriteria.put("ID_REGISTRO", Arrays.asList("DETE1"));
	     searchCriteria.put("A_REGISTRAZ", Arrays.asList("2013"));
		System.out.println(searchCriteria.toString());
    }

    @Test
    public void testNumero() throws DocerException {

	int max = Integer.MAX_VALUE;
	System.out.println(max);
	long time = new Date().getTime();
	String numpg = String.valueOf(time).replaceAll("...$", "");
	System.out.println(numpg);

    }

    @Test
    public void testKeyFormat() throws DocerException {

	String format = "%->%";
	String key = String.format(format, "Ente1","Aoo1");
	System.out.println(key);
    }
    
    @Test
    public void testMaps() throws DocerException {

	Map<String, FieldDescriptor> map = new HashMap<String, FieldDescriptor>();
	map.put("test", new FieldDescriptor("propName", EnumBLPropertyType.STRING, "format", false));

	Map<String, FieldDescriptor> map2 = new HashMap<String, FieldDescriptor>();

	map2.putAll(map);

	map2.put("yyy", new FieldDescriptor("xxx", EnumBLPropertyType.BOOLEAN, "yyy", true));
	map2.put("test", new FieldDescriptor("propName2", EnumBLPropertyType.BOOLEAN, "format2", true));

	String a = "";
    }

    @Test
    public void testFascicoliSecondariFormat() throws DocerException, IOException, XMLStreamException, JaxenException {

	String fascicolo = "1.0/0/2013/1";

	regexpFascicoliSec = "^([^/]+)/([12][0-9][0-9][0-9])/(.+)$";
	patternFascicoliSec = Pattern.compile(regexpFascicoliSec);
	    
	Matcher m = patternFascicoliSec.matcher(fascicolo);

	String classifica = "";
	String annoFascicolo = "";
	String progrFascicolo = "";

	if (m.matches()) {
	    classifica = m.group(classificaPosition);
	    annoFascicolo = m.group(annoFascicoloPosition);
	    progrFascicolo = m.group(progrFascicoloPosition);
	}

	System.out.println("classifica: " + classifica);
	System.out.println("annoFascicolo: " + annoFascicolo);
	System.out.println("progrFascicolo: " + progrFascicolo);

    }
    
    @Test
    public void testFascicoliSecondariMapping() throws DocerException, IOException, XMLStreamException, JaxenException {

	String fascicolo = "1.0.0/2013/1";

	File configFile = new File("/src/test/resources/confstub.xml");

	if (configFile == null || !configFile.exists())
	    throw new DocerException("Config file non trovato: " + configFile.getAbsolutePath());

	Config c = new Config();

	c.setConfigFile(configFile);

	OMElement element = c.getNode("//configuration/group[@name='business_logic_variables']/section[@name='props']/fascicoli_secondari_mapping");

	QName regexpQname = new QName("regexp");
	QName classificaPosQname = new QName("classifica_position");
	QName annoFascicoloQname = new QName("anno_fascicolo_position");
	QName progrFascicoloQname = new QName("progr_fascicolo_position");

	OMAttribute omAtt = element.getAttribute(regexpQname);
	if (omAtt != null) {

	    String appoRegex = omAtt.getAttributeValue();
	    patternFascicoliSec = Pattern.compile(appoRegex);
	    regexpFascicoliSec = appoRegex;
	}

	omAtt = element.getAttribute(classificaPosQname);
	if (omAtt != null) {
	    classificaPosition = Integer.parseInt(omAtt.getAttributeValue().replace("$", ""));
	}

	omAtt = element.getAttribute(annoFascicoloQname);
	if (omAtt != null) {
	    annoFascicoloPosition = Integer.parseInt(omAtt.getAttributeValue().replace("$", ""));
	}

	omAtt = element.getAttribute(progrFascicoloQname);
	if (omAtt != null) {
	    progrFascicoloPosition = Integer.parseInt(omAtt.getAttributeValue().replace("$", ""));
	}

	Matcher m = patternFascicoliSec.matcher(fascicolo);

	String classifica = "";
	String annoFascicolo = "";
	String progrFascicolo = "";

	if (m.matches()) {
	    classifica = m.group(classificaPosition);
	    annoFascicolo = m.group(annoFascicoloPosition);
	    progrFascicolo = m.group(progrFascicoloPosition);
	}

	System.out.println("classifica: " + classifica);
	System.out.println("annoFascicolo: " + annoFascicolo);
	System.out.println("progrFascicolo: " + progrFascicolo);

    }

    @Test
    public void testReplace() {

	System.out.println(StringUtils.isEmpty(null));
	System.out.println(StringUtils.isEmpty(""));

	String fascSecondari = "${classifica}/${anno_fascicolo}/${progr_fascicolo}";

	fascSecondari = fascSecondari.replace("${classifica}", "1.0.0");
	fascSecondari = fascSecondari.replace("${anno_fascicolo}", "2013");
	fascSecondari = fascSecondari.replace("${progr_fascicolo}", "1");

	System.out.println(fascSecondari);

    }

    @Test
    public void testInitBL() throws DocerException {

	try {
	    BusinessLogic bl = getBL();
	    String token = bl.login("EMR", "admin", "admin");
	    System.out.println(token);
	    
	}
	catch (Exception e) {
	    System.out.println(e.getMessage());
	    // assertTrue(false);
	}

    }
    
    @Test
    public void testGetACLFascicolo() throws DocerException {

	try {
	    String ticket = getBL().login("EMR", "luca", "luca");

	    Map<String, String> id = new HashMap<String, String>();
	    id.put("COD_ENTE", "EMR");
	    id.put("COD_AOO", "AOO_EMR");
	    id.put("CLASSIFICA", "1");
	    id.put("ANNO_FASCICOLO", "2012");
	    id.put("PROGR_FASCICOLO", "1");

	    Map<String, EnumACLRights> acl = getBL().getACLFascicolo(ticket, id);
	    System.out.println(acl.toString());
	}
	catch (Exception e) {
	    System.out.println(e.getMessage());
	    // assertTrue(false);
	}

    }

    @Test
    public void testDecrypt() {

	String ticket = "j0VBSlZxDZQgj7luX_2tK0Xnf52Vc2dYHROU3d1rYx8yKK7NJ_dyRMNoeB68NBlx4FGKnCBznNiphdAQ-8534ia9t1inDUO3V3t85uVA5FQBk2XIK1FvTIpLsw_cC_NT01e9RIG5cv5fhpgjz67fYIrcp8l2VQKjhtZ69tZY8eumhUy856TCF3UzE34se4qQw-TdtJVCgFwuJk_MV7U7L5QtR-eEjz8-y3Nzw9jfp7s";
	TicketCipher tc = null;
	tc = new TicketCipher();
	ticket = tc.decryptTicket(ticket);	    
	System.out.println("1 -> " +ticket);
	
	ticket = tc.decryptTicket(ticket);	    
	System.out.println("2 -> " +ticket);
	
	ticket = tc.decryptTicket(ticket);	    
	System.out.println("3 -> " +ticket);
	
	ticket = tc.decryptTicket(ticket);	    
	System.out.println("4 -> " +ticket);

    }

    @Test
    public void testTest() {

	// String fascSecondari =
	// "${classifica}/${anno_fascicolo}/${progr_fascicolo}";
	//
	// fascSecondari = fascSecondari.replaceAll("${classifica}", "1.0.0");
	// fascSecondari = fascSecondari.replaceAll("${anno_fascicolo}",
	// "2013");
	// fascSecondari = fascSecondari.replaceAll("${progr_fascicolo}", "1");
	//
	// System.out.println(fascSecondari);

	DateTimeFormatter fm = ISODateTimeFormat.dateTime();
	DateTime d = fm.parseDateTime("2013-10-13T15:01:39.649+02:00");

	System.out.println("2013-10-13T15:01:39.649+02:00 --> " + d.toString("dd/MM/yyyy"));

	d = new DateTime();
	String strDate = d.toString("yyyy-MM-dd HH.mm.ss");
	System.out.println(strDate);
	// String strDate = d.toString();

	try {
	    DateTime date = parseDateTime(strDate);
	    System.out.println(date.toString());
	}
	catch (Exception e) {
	    System.out.println(e.getMessage());
	}

	// String ticket = "MKWWfEEgQ3ytD9MiY8bR2fnLhAEprGwIe_Wgp4Buro8";
	// TicketCipher tc = null;
	// try {
	// tc = new TicketCipher();
	// System.out.println(tc.decryptTicket(ticket));
	// }
	// catch (DocerException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }

    }

    private DateTime parseDateTime(String datetime) {
	DateTimeFormatter fm = ISODateTimeFormat.dateTime();
	return fm.parseDateTime(datetime);
    }

    @Test
    public void testAll() {
	testLogin();
	// testSearchDocuments();

	// testAdvancedVersion();
	// testRiferimenti();

	// testFolder();

	// testCreateAnagrafiche();
	// testCreateUsers();
	// testCreateGroups();
	// testSetGroupsOfUser();
	// testSetUsersOfGroup();

	// testCreateDocument1();
	// testCreateDocument2();
	// testCreateDocument3();
	// testCreateDocument4();
	// testCreateDocument5();
	// testCreateDocument6();

	docid_1 = "73919";
	docid_2 = "73920";

	testRelated();
	// testSetACLDocumento();
	// testAddRelated();
	// testCreateVersion2();
	// testCreateVersion3();
	// testGetVersions();
	// testLockDocument();
	// testUnlockDocument();
	// testUpdateProfileDocument();
	// testClassificaDocumento1();
	// testSetACLDocumento();

	// long min = 100000;
	// long max = 0;
	//
	// long mm = 60000;
	// long hh = 60000 * 60;
	//
	// long startTime = new Date().getTime();
	// long end = startTime;
	// long total = 0;
	//
	// while( total < (120*mm)){
	// end = new Date().getTime();
	// total = end - startTime;
	//
	// testFascicolaDocumento1();
	// testAnnullaFascicoloDocumento();
	// testFascicolaDocumento1();
	// testFascicolaDocumento3();
	// testFascicolaDocumento4();
	// testAnnullaFascicoloDocumento();
	//
	// }

	// testAnnullaFascicolaDocumento1();
	// testFascicolaDocumento2();
	// testAnnullaFascicolaDocumento2();
	//
	// testPubblicaDocumento();
	// testPubblicaDocumento();
	// testGetProfileDocument();
	// testDownloadDocument();
	testLogout();

    }

    @Test
    public void testGetEnumStatoPantarei() {

	System.out.println(BusinessLogic.getEnumStatoPantarei(""));
	System.out.println(BusinessLogic.getEnumStatoPantarei(null));

    }

    @Test
    public void testLockAndUnlock() {

	testLogin();

	try {
	    getBL().lockDocument(getToken(), docid_1);

	}
	catch (Exception e) {
	    System.out.println(e.getMessage());
	    // assertTrue(false);
	}

	try {
	    getBL().unlockDocument(getToken(), docid_1);

	}
	catch (Exception e) {
	    System.out.println(e.getMessage());
	    // assertTrue(false);
	}

	testLogout();
    }

    @Test
    public void testGetGroup() {

	testLogin();

	Map<String, String> info = new HashMap<String, String>();
	info.put("GROUP_ID", "group1");
	info.put("GROUP_NAME", "group1");
	info.put("PARENT_GROUP_ID", cod_aoo);
	try {
	    getBL().getUser(getToken(), "group1");
	    // assertTrue(true);
	}
	catch (Exception e) {
	    System.out.println(e.getMessage());
	    // assertTrue(false);
	}
    }

    @Test
    public void testCreateGroup() {

	testLogin();

	Map<String, String> info = new HashMap<String, String>();
	info.put("GROUP_ID", "group1");
	info.put("GROUP_NAME", "group1");
	info.put("PARENT_GROUP_ID", cod_aoo);
	try {
	    getBL().createGroup(getToken(), info);
	    System.out.println("gruppo group1 creato");
	    // assertTrue(true);
	}
	catch (Exception e) {
	    System.out.println(e.getMessage());
	    // assertTrue(false);
	}
    }

    @Test
    public void testAll2() {
	testLogin();
	// testSearchDocuments();

	docid_1 = "61782";
	docid_2 = "61783";
	docid_3 = "61784";
	docid_4 = "61663";
	docid_5 = "61664";
	docid_6 = "61665";

	// testRelated();
	// testAdvancedVersion();
	// testRiferimenti();

	// testFolder();

	// testCreateAnagrafiche();
	// testCreateUsers();
	// testCreateGroups();
	// testSetGroupsOfUser();
	// testSetUsersOfGroup();
	// testCreateDocument1();
	// testCreateDocument2();
	// testCreateDocument3();
	// testSetACLDocumento();
	// testAddRelated();
	// testCreateVersion2();
	// testCreateVersion3();
	// testGetVersions();
	// testLockDocument();
	// testUnlockDocument();
	// testUpdateProfileDocument();
	// testClassificaDocumento1();
	// testSetACLDocumento();

	long min = 100000;
	long max = 0;

	long mm = 60000;
	long hh = 60000 * 60;

	long startTime = new Date().getTime();
	long end = startTime;
	long total = 0;

	while (total < (120 * mm)) {
	    end = new Date().getTime();
	    total = end - startTime;

	    testFascicolaDocumento1();
	    testAnnullaFascicoloDocumento();
	    testFascicolaDocumento1();
	    testFascicolaDocumento3();
	    testFascicolaDocumento4();
	    testAnnullaFascicoloDocumento();

	}

	// testAnnullaFascicolaDocumento1();
	// testFascicolaDocumento2();
	// testAnnullaFascicolaDocumento2();
	//
	// testPubblicaDocumento();
	// testPubblicaDocumento();
	// testGetProfileDocument();
	// testDownloadDocument();
	testLogout();

    }

    // @Test
    // public void testEquals() {
    //
    // try{
    // String a = "ciccio";
    // String b = null;
    // System.out.println(a.equals(b));
    // System.out.println(a.equals(a));
    // //assertTrue(true);
    //
    // }
    // catch(Exception e){
    // System.out.println(e.getMessage());
    // //assertTrue(false);
    // }
    //
    // }

    private BusinessLogic getBL() {

	if (businessLogic == null) {
	    try {
		Properties p = new Properties();
		p.loadFromXML(this.getClass().getResourceAsStream("/docer_config.xml"));

		// appena viene invocato il WS recupero l'informazione del
		// 'provider'
		// dal file 'config.xml'
		String providerName = p.getProperty("provider");

		BusinessLogic.setConfigPath(p.getProperty("CONFIG_DIR"));

		businessLogic = new BusinessLogic(providerName, 1000);
	    }
	    catch (Exception e) {
		System.out.println(e.getMessage());
	    }
	}

	return businessLogic;

    }

    private String getToken() {
	if (token == null)
	    testLogin();
	return token;
    }

    @Test
    public void testCreateMassive() {

	testLogin();

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
	// testCreateDocument1();
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
	//

	try {
	    getBL().lockDocument(token, "57294");
	}
	catch (DocerException e) {

	    e.printStackTrace();
	}

	testLogout();
    }

    @Test
    public void testWriteLog() {

	Map<String, String> map = new HashMap<String, String>();
	map.put("a", "value");
	map.put("b", "value");
	map.put("c", "value");

	System.out.println(map.keySet());
    }

    @Test
    public void test2() {
	testLogin();

    }

    @Test
    public void testAddNewAdvancedVersion() {

	try {
	    testLogin();
	    // getBL().addNewAdvancedVersion(getToken(), "1319", "3713");

	    getBL().addNewAdvancedVersion(getToken(), "7743", "307672");

	    // getBL().addNewAdvancedVersion(getToken(), "3713", "3716");
	    System.out.println("advanced version ok");
	}
	catch (DocerException e) {
	    System.out.println("advanced version KO: " + e.getMessage());
	    return;
	}

    }

    @Test
    public void testAddRiferimenti() {

	try {

	    getBL().addRiferimentiDocuments(getToken(), docid_1, Arrays.asList(docid_2));
	    getBL().addRiferimentiDocuments(getToken(), docid_3, Arrays.asList(docid_1, docid_2, docid_3));
	    System.out.println("addRiferimentiDocuments ok");
	}
	catch (DocerException e) {
	    System.out.println("addRiferimentiDocuments KO: " + e.getMessage());
	    return;
	}
    }

    @Test
    public void testRemoveRiferimenti() {

	try {

	    getBL().removeRiferimentiDocuments(getToken(), docid_1, Arrays.asList(docid_2, docid_3));
	    getBL().removeRiferimentiDocuments(getToken(), docid_2, Arrays.asList(docid_1, docid_3));
	    getBL().removeRiferimentiDocuments(getToken(), docid_3, Arrays.asList(docid_1, docid_2));

	    List<String> list = getBL().getRiferimentiDocuments(getToken(), docid_1);
	    System.out.println("riferimenti " + docid_1 + ": ");
	    for (String id : list) {
		System.out.println(id);
	    }
	    list = getBL().getRiferimentiDocuments(getToken(), docid_2);
	    System.out.println("riferimenti " + docid_2 + ": ");
	    for (String id : list) {
		System.out.println(id);
	    }

	    list = getBL().getRiferimentiDocuments(getToken(), docid_3);
	    System.out.println("riferimenti " + docid_3 + ": ");
	    for (String id : list) {
		System.out.println(id);
	    }

	    System.out.println("removeRiferimentiDocuments ok");
	}
	catch (DocerException e) {
	    System.out.println("removeRiferimentiDocuments KO: " + e.getMessage());
	    return;
	}
    }

    @Test
    public void testSearchDocuments() {

	try {

	    Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();

	    //searchCriteria.put("TYPE_ID", Arrays.asList("DOCUMENTO"));
	    searchCriteria.put("COD_ENTE", Arrays.asList(cod_ente));
	    searchCriteria.put("COD_AOO", Arrays.asList(cod_aoo));

	    List<ISearchItem> res = getBL().searchDocuments(token, searchCriteria, null, 100, null);
	    System.out.println("ricerca ok risultati: " + res.size());

	}
	catch (Exception e) {

	    System.out.println("ricerca KO: " + e.getMessage());
	    // assertTrue(false);

	}

    }

    @Test
    public void testSearchOr() {

	String token = null;
	try {

	    token = getBL().login("", "admin", "admin");

	    Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
	    
	    searchCriteria.put("COD_ENTE", Arrays.asList("EMR"));
	    searchCriteria.put("COD_AOO", Arrays.asList("AOO_EMR"));
	    searchCriteria.put("CLASSIFICA", Arrays.asList("2.2.1"));
	    searchCriteria.put("ANNO_FASCICOLO", Arrays.asList("2012"));
	    searchCriteria.put("PROGR_FASCICOLO", Arrays.asList(""));
	    
	    List<ISearchItem> res = getBL().searchDocuments(token, searchCriteria, new ArrayList<String>(), 10, null);

	    System.out.println("trovati " + res.size() + " risultati");

	     for (ISearchItem isi : res) {
		 System.out.println("---------------");
		 System.out.println(isi.toString());
	     }
	    
	    

	}
	catch (Exception e) {

	    System.out.println("search KO: " + e.getMessage());
	    // assertTrue(false);

	}
	finally {
	    try {
		getBL().logout(token);
	    }
	    catch (DocerException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }

	}

    }
    
    @Test
    public void testContentSize() {

	String token = null;
	try {

	    token = getBL().login("", "admin", "admin");

	    Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
	    //searchCriteria.put("COD_ENTE", Arrays.asList("EMR"));
	    // searchCriteria.put("CONTENT_SIZE", Arrays.asList("3256"));
	    // searchCriteria.put("COD_AOO", Arrays.asList("C-I840-01"));
	    // searchCriteria.put("ID_REGISTRO", Arrays.asList("DETE1"));
	    // searchCriteria.put("A_REGISTRAZ", Arrays.asList("2013"));

	    List<ISearchItem> res = getBL().searchDocuments(token, searchCriteria, null, 10, null);

	    System.out.println("trovati " + res.size() + " risultati");

	     for (ISearchItem isi : res) {
	    
		 System.out.println("----------");
    	     	System.out.println(isi.toString());
    	     
	    
	     }

	    // Map<String, String> info = new HashMap<String, String>();
	    // info.put("CONTENT_SIZE", "0");
	    // getBL().updateProfileDocument(getToken(), "1809", info);
	    // System.out.println("update ok");
	    //
	    //
	    //
	    // System.out.println("trovati " +res.size() +" risultati");
	    //
	    // for (ISearchItem isi : res) {
	    //
	    // System.out.println("----------");
	    // for (KeyValuePair kvp : isi.getMetadata()) {
	    // System.out.println(kvp.getKey() +"=" +kvp.getValue());
	    // }
	    //
	    //
	    // }

	}
	catch (Exception e) {

	    System.out.println("search KO: " + e.getMessage());
	    // assertTrue(false);

	}
	finally {
	    try {
		getBL().logout(token);
	    }
	    catch (DocerException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }

	}

    }

//    @Test
//    public void testOrderBy() {
//
//	String token = null;
//	try {
//
//	    token = getBL().login("", "admin", "admin");
//
//	    // getBL().addRelated(token, "61935", Arrays.asList("61670"));
//
//	    // getBL().removeRelated(token, "61935", Arrays.asList("61670"));
//	    Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
//	    // searchCriteria.put("COD_ENTE", Arrays.asList("EMR"));
//	    // searchCriteria.put("COD_AOO", Arrays.asList("C-I840-01"));
//	    // searchCriteria.put("ID_REGISTRO", Arrays.asList("DETE1"));
//	    // searchCriteria.put("A_REGISTRAZ", Arrays.asList("2013"));
//
//	    Map<String, EnumSearchOrder> orderby = new HashMap<String, EnumSearchOrder>();
//	    orderby.put("DOCNUM", EnumSearchOrder.DESC);
//
//	    List<ISearchItem> res = getBL().searchDocuments(token, searchCriteria, null, 10000, orderby);
//
//	    
//	    long start = System.currentTimeMillis();
//
//	    res = getBL().searchDocuments(token, searchCriteria, null, 10000, orderby);
//
//	    long end = System.currentTimeMillis();
//	    
//	    System.out.println("trovati " + res.size() + " risultati in " +(end-start));
//
////	    for (ISearchItem isi : res) {
////
////		System.out.println(isi.toString());
////	    }
//
//	    start = System.currentTimeMillis();
//
//	    res = getBL().searchDocuments2(token, searchCriteria, null, 1000, orderby);
//
//	    end = System.currentTimeMillis();
//	    
//	    System.out.println("trovati " + res.size() + " risultati in " +(end-start));
//	    
//	}
//	catch (Exception e) {
//
//	    System.out.println("testAllegati KO: " + e.getMessage());
//	    // assertTrue(false);
//
//	}
//	finally {
//	    try {
//		getBL().logout(token);
//	    }
//	    catch (DocerException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	    }
//
//	}
//    }

    @Test
    public void testRelated1() {

	String token = null;
	try {

	    token = getBL().login("", "admin", "admin");

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

	    String cod_ente = "EMR";
	    String cod_aoo = "AOO_EMR";

	    // creo principale
	    Map<String, String> info = new HashMap<String, String>();
	    info.put("COD_ENTE", cod_ente);
	    info.put("COD_AOO", cod_aoo);
	    info.put("DOCNAME", "principale.txt");
	    info.put("TYPE_ID", "documento");
	    info.put("FIRMATARIO", "<Segnatura><Intestazione><Oggetto>a</Oggetto></Intestazione></Segnatura>");

	    String docnum1 = getBL().createDocument(token, info, fileInputStream);
	    System.out.println("creato " + docnum1);
	    
	    // creo allegato
	    info.clear();
	    info.put("COD_ENTE", cod_ente);
	    info.put("COD_AOO", cod_aoo);
	    info.put("DOCNAME", "allegato.txt");
	    info.put("TYPE_ID", "documento");
	    info.put("FIRMATARIO", "");

	    try {
		fileInputStream = new FileInputStream(file);
	    }
	    catch (FileNotFoundException e1) {

		System.out.println(e1);
		return;
	    }
	    
	    String docnum2 = getBL().createDocument(token, info, fileInputStream);
	    System.out.println("creato " + docnum2);

	    try {
		fileInputStream = new FileInputStream(file);
	    }
	    catch (FileNotFoundException e1) {

		System.out.println(e1);
		return;
	    }

	    
	    // creo annesso
	    info.clear();
	    info.put("COD_ENTE", cod_ente);
	    info.put("COD_AOO", cod_aoo);
	    info.put("DOCNAME", "annesso.txt");
	    info.put("TYPE_ID", "documento");
	    info.put("FIRMATARIO", "");

	    String docnum3 = getBL().createDocument(token, info, fileInputStream);
	    System.out.println("creato " + docnum3);

	    try {
		fileInputStream = new FileInputStream(file);
	    }
	    catch (FileNotFoundException e1) {

		System.out.println(e1);
		return;
	    }

	    // creo annotazione
	    info.clear();
	    info.put("COD_ENTE", cod_ente);
	    info.put("COD_AOO", cod_aoo);
	    info.put("DOCNAME", "annotazione.txt");
	    info.put("TYPE_ID", "documento");
	    info.put("FIRMATARIO", "");

	    String docnum4 = getBL().createDocument(token, info, fileInputStream);
	    System.out.println("creato " + docnum4);

	    // relazioni
	    getBL().addRelated(token, docnum1, Arrays.asList(docnum2, docnum3, docnum4));

	    System.out.println("addRelated " + docnum1 + " -> " + Arrays.asList(docnum2, docnum3, docnum4).toString());

	    List<String> related = getBL().getRelatedDocuments(token, docnum1);
	    System.out.println("related di " + docnum1 + ": " + related.toString());
	    related = getBL().getRelatedDocuments(token, docnum2);
	    System.out.println("related di " + docnum2 + ": " + related.toString());
	    related = getBL().getRelatedDocuments(token, docnum3);
	    System.out.println("related di " + docnum3 + ": " + related.toString());
	    related = getBL().getRelatedDocuments(token, docnum4);
	    System.out.println("related di " + docnum4 + ": " + related.toString());

	    // protocollo
	    info.clear();
	    info.put("NUM_PG", "11111");
	    info.put("DATA_PG", "2012-06-12T10:22:12.000+02:00");
	    info.put("REGISTRO_PG", "PG");
	    info.put("ANNO_PG", "2012");
	    info.put("OGGETTO_PG", "oggetto protocollo");
	    info.put("TIPO_PROTOCOLLAZIONE", "E");
	    info.put("TIPO_FIRMA", "FD");
	    info.put("MITTENTI", "AAA");

	    getBL().protocollaDocumento(token, docnum1, info);


	    System.out.println("protocollato " + docnum1);

	    
	    try {
		fileInputStream = new FileInputStream(file);
	    }
	    catch (FileNotFoundException e1) {

		System.out.println(e1);
		return;
	    }

	    // creo annotazione
	    info.clear();
	    info.put("COD_ENTE", cod_ente);
	    info.put("COD_AOO", cod_aoo);
	    info.put("DOCNAME", "post_annotazione.txt");
	    info.put("TYPE_ID", "documento");
	    info.put("TIPO_COMPONENTE", "ANNESSO");
	    info.put("FIRMATARIO", "");

	    String docnum5 = getBL().createDocument(token, info, fileInputStream);
	    System.out.println("creato " + docnum5);

	    getBL().addRelated(token, docnum1, Arrays.asList(docnum5));
	    System.out.println("addRelated annotazione dopo protocollazione" + docnum1 + " -> " + docnum5);

	    related = getBL().getRelatedDocuments(token, docnum1);
	    System.out.println("related di " + docnum1 + ": " + related.toString());
	    related = getBL().getRelatedDocuments(token, docnum2);
	    System.out.println("related di " + docnum2 + ": " + related.toString());
	    related = getBL().getRelatedDocuments(token, docnum3);
	    System.out.println("related di " + docnum3 + ": " + related.toString());
	    related = getBL().getRelatedDocuments(token, docnum4);
	    System.out.println("related di " + docnum4 + ": " + related.toString());
	    related = getBL().getRelatedDocuments(token, docnum5);
	    System.out.println("related di " + docnum5 + ": " + related.toString());

	    
	    try {
		fileInputStream = new FileInputStream(file);
	    }
	    catch (FileNotFoundException e1) {

		System.out.println(e1);
		return;
	    }

	    // creo allegato
	    info.clear();
	    info.put("COD_ENTE", cod_ente);
	    info.put("COD_AOO", cod_aoo);
	    info.put("DOCNAME", "principale2.txt");
	    info.put("TYPE_ID", "documento");
	    info.put("FIRMATARIO", "<Segnatura><Intestazione><Oggetto>a</Oggetto></Intestazione></Segnatura>");

	    String docnum6 = getBL().createDocument(token, info, fileInputStream);
	    System.out.println("creato " + docnum6);

	    try{
		getBL().addRelated(token, docnum1, Arrays.asList(docnum6));
		System.out.println("ERRORE addRelated --> Aggiunto allegato a Protocollato ");
	    }
	    catch(Exception e){
		System.out.println("OK --> " +e.getMessage());
	    }

	    try{
		getBL().addRelated(token, docnum6, Arrays.asList(docnum1));
		System.out.println("ERRORE 2 addRelated --> Aggiunto allegato a Protocollato ");
	    }
	    catch(Exception e){
		System.out.println("OK --> " +e.getMessage());
	    }

	}
	catch (Exception e) {

	    System.out.println("testAllegati KO: " + e.getMessage());
	    // assertTrue(false);

	}
	finally {
	    try {
		getBL().logout(token);
	    }
	    catch (DocerException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }

	}
    }
    

    @Test
    public void testRelated2() {

	String token = null;
	try {

	    token = getBL().login("", "admin", "admin");

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

	    String cod_ente = "EMR";
	    String cod_aoo = "AOO_EMR";

	    // creo principale
	    Map<String, String> info = new HashMap<String, String>();
	    info.put("COD_ENTE", cod_ente);
	    info.put("COD_AOO", cod_aoo);
	    info.put("DOCNAME", "principale.txt");
	    info.put("TYPE_ID", "documento");
	    info.put("FIRMATARIO", "<Segnatura><Intestazione><Oggetto>a</Oggetto></Intestazione></Segnatura>");

	    String docnum1 = getBL().createDocument(token, info, fileInputStream);
	    System.out.println("creato " + docnum1);
	    
	    // creo allegato
	    info.clear();
	    info.put("COD_ENTE", cod_ente);
	    info.put("COD_AOO", cod_aoo);
	    info.put("DOCNAME", "allegato.txt");
	    info.put("TYPE_ID", "documento");
	    info.put("FIRMATARIO", "");

	    try {
		fileInputStream = new FileInputStream(file);
	    }
	    catch (FileNotFoundException e1) {

		System.out.println(e1);
		return;
	    }
	    
	    String docnum2 = getBL().createDocument(token, info, fileInputStream);
	    System.out.println("creato " + docnum2);


	    // classifico
	    info.clear();
	    info.put("CLASSIFICA", "1");
	    
	    getBL().classificaDocumento(token, docnum2, info);
	    System.out.println("aggiunta classifica a documento " +docnum2);
	    	    
	    try{
		getBL().addRelated(token, docnum1, Arrays.asList(docnum2));
		System.out.println("aggiunto related...");
	    }
	    catch(Exception e){
		System.out.println("OK --> " +e.getMessage());
	    }

	    
//	    // protocollo
//	    info.clear();
//	    info.put("NUM_PG", "11111");
//	    info.put("DATA_PG", "2012-06-12T10:22:12.000+02:00");
//	    info.put("REGISTRO_PG", "PG");
//	    info.put("ANNO_PG", "2012");
//	    info.put("OGGETTO_PG", "oggetto protocollo");
//	    info.put("TIPO_PROTOCOLLAZIONE", "E");
//	    info.put("TIPO_FIRMA", "FD");
//	    info.put("MITTENTI", "AAA");
//
//	    getBL().protocollaDocumento(token, docnum1, info);
//	    System.out.println("Protocollato...");
//	    
//	    // classifico
//	    info.clear();
//	    info.put("CLASSIFICA", "1");
//	    
//	    getBL().classificaDocumento(token, docnum1, info);
//	    System.out.println("Classificato...");
//	    
//	    
//	    
//	    try {
//		fileInputStream = new FileInputStream(file);
//	    }
//	    catch (FileNotFoundException e1) {
//
//		System.out.println(e1);
//		return;
//	    }
//
//	    // creo allegato
//	    info.clear();
//	    info.put("COD_ENTE", cod_ente);
//	    info.put("COD_AOO", cod_aoo);
//	    info.put("DOCNAME", "principale2.txt");
//	    info.put("TYPE_ID", "documento");
//	    info.put("TIPO_COMPONENTE", "ALLEGATO");
//	    info.put("FIRMATARIO", "<Segnatura><Intestazione><Oggetto>a</Oggetto></Intestazione></Segnatura>");
//
//	    String docnum6 = getBL().createDocument(token, info, fileInputStream);
//	    System.out.println("creato " + docnum6);
//
//	    try{
//		getBL().addRelated(token, docnum1, Arrays.asList(docnum6));
//		System.out.println("ERRORE addRelated --> Aggiunto allegato a Protocollato ");
//	    }
//	    catch(Exception e){
//		System.out.println("OK --> " +e.getMessage());
//	    }
//
//	    try{
//		getBL().addRelated(token, docnum6, Arrays.asList(docnum1));
//		System.out.println("ERRORE 2 addRelated --> Aggiunto allegato a Protocollato ");
//	    }
//	    catch(Exception e){
//		System.out.println("OK --> " +e.getMessage());
//	    }

	}
	catch (Exception e) {

	    System.out.println("testAllegati KO: " + e.getMessage());
	    // assertTrue(false);

	}
	finally {
	    try {
		getBL().logout(token);
	    }
	    catch (DocerException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }

	}
    }
    
    

    @Test
    public void testConfiguration() {

	String ticket = null;
	try {

	    ticket = getBL().login("", "admin", "admin");
	    	    
	    try{
		List<IKeyValuePair> list = getBL().getDocumentTypes(ticket);
		
		for(IKeyValuePair kvp : list){
		    System.out.println(kvp.getKey()+"=" +kvp.getValue());
		}
		
		System.out.println("-------------");
		
		list = getBL().getDocumentTypes(ticket, "aaa", "bbb");
		
		for(IKeyValuePair kvp : list){
		    System.out.println(kvp.getKey()+"=" +kvp.getValue());
		}

	    }
	    catch(Exception e){
		System.out.println("OK --> " +e.getMessage());
	    }

	}
	catch (Exception e) {

	    System.out.println("testConfiguration KO: " + e.getMessage());
	    // assertTrue(false);

	}
	finally {
	    try {
		getBL().logout(ticket);
	    }
	    catch (DocerException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }

	}
    }

    
    @Test
    public void testRelatedComplete() {

	String token = null;
	try {

	    token = getBL().login("", "admin", "admin");

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

	    String cod_ente = "EMR";
	    String cod_aoo = "AOO_EMR";

	    // creo principale
	    Map<String, String> info = new HashMap<String, String>();
	    info.put("COD_ENTE", cod_ente);
	    info.put("COD_AOO", cod_aoo);
	    info.put("DOCNAME", "principale.txt");
	    info.put("TYPE_ID", "documento");
	    info.put("FIRMATARIO", "<Segnatura><Intestazione><Oggetto>a</Oggetto></Intestazione></Segnatura>");

	    // creo allegato
	    String docnum1 = getBL().createDocument(token, info, fileInputStream);
	    System.out.println("creato " + docnum1);

	    info.clear();
	    info.put("COD_ENTE", cod_ente);
	    info.put("COD_AOO", cod_aoo);
	    info.put("DOCNAME", "allegato.txt");
	    info.put("TYPE_ID", "documento");
	    info.put("FIRMATARIO", "");

	    try {
		fileInputStream = new FileInputStream(file);
	    }
	    catch (FileNotFoundException e1) {

		System.out.println(e1);
		return;
	    }
	    
	    String docnum2 = getBL().createDocument(token, info, fileInputStream);
	    System.out.println("creato " + docnum2);

	    try {
		fileInputStream = new FileInputStream(file);
	    }
	    catch (FileNotFoundException e1) {

		System.out.println(e1);
		return;
	    }

	    
	    // creo annesso
	    info.clear();
	    info.put("COD_ENTE", cod_ente);
	    info.put("COD_AOO", cod_aoo);
	    info.put("DOCNAME", "annesso.txt");
	    info.put("TYPE_ID", "documento");
	    info.put("FIRMATARIO", "");

	    String docnum3 = getBL().createDocument(token, info, fileInputStream);
	    System.out.println("creato " + docnum3);

	    try {
		fileInputStream = new FileInputStream(file);
	    }
	    catch (FileNotFoundException e1) {

		System.out.println(e1);
		return;
	    }

	    // creo annotazione
	    info.clear();
	    info.put("COD_ENTE", cod_ente);
	    info.put("COD_AOO", cod_aoo);
	    info.put("DOCNAME", "annotazione.txt");
	    info.put("TYPE_ID", "documento");
	    info.put("FIRMATARIO", "");

	    String docnum4 = getBL().createDocument(token, info, fileInputStream);
	    System.out.println("creato " + docnum4);

	    getBL().addRelated(token, docnum1, Arrays.asList(docnum2, docnum3, docnum4));

	    System.out.println("addRelated " + docnum1 + " -> " + Arrays.asList(docnum2, docnum3, docnum4).toString());

	    List<String> related = getBL().getRelatedDocuments(token, docnum1);
	    System.out.println("related di " + docnum1 + ": " + related.toString());
	    related = getBL().getRelatedDocuments(token, docnum2);
	    System.out.println("related di " + docnum2 + ": " + related.toString());
	    related = getBL().getRelatedDocuments(token, docnum3);
	    System.out.println("related di " + docnum3 + ": " + related.toString());
	    related = getBL().getRelatedDocuments(token, docnum4);
	    System.out.println("related di " + docnum4 + ": " + related.toString());

	    info.clear();
	    info.put("NUM_PG", "11111");
	    info.put("DATA_PG", "2012-06-12T10:22:12.000+02:00");
	    info.put("REGISTRO_PG", "PG");
	    info.put("ANNO_PG", "2012");
	    info.put("OGGETTO_PG", "oggetto protocollo");
	    info.put("TIPO_PROTOCOLLAZIONE", "E");
	    info.put("TIPO_FIRMA", "FD");
	    info.put("MITTENTI", "AAA");

	    getBL().protocollaDocumento(token, docnum1, info);


	    System.out.println("protocollato " + docnum1);

	    
	    try {
		fileInputStream = new FileInputStream(file);
	    }
	    catch (FileNotFoundException e1) {

		System.out.println(e1);
		return;
	    }

	    // creo annotazione
	    info.clear();
	    info.put("COD_ENTE", cod_ente);
	    info.put("COD_AOO", cod_aoo);
	    info.put("DOCNAME", "post_annotazione.txt");
	    info.put("TYPE_ID", "documento");
	    info.put("TIPO_COMPONENTE", "ANNESSO");
	    info.put("FIRMATARIO", "");

	    String docnum5 = getBL().createDocument(token, info, fileInputStream);
	    System.out.println("creato " + docnum5);

	    getBL().addRelated(token, docnum1, Arrays.asList(docnum5));
	    System.out.println("addRelated annotazione dopo protocollazione" + docnum1 + " -> " + docnum5);

	    related = getBL().getRelatedDocuments(token, docnum1);
	    System.out.println("related di " + docnum1 + ": " + related.toString());
	    related = getBL().getRelatedDocuments(token, docnum2);
	    System.out.println("related di " + docnum2 + ": " + related.toString());
	    related = getBL().getRelatedDocuments(token, docnum3);
	    System.out.println("related di " + docnum3 + ": " + related.toString());
	    related = getBL().getRelatedDocuments(token, docnum4);
	    System.out.println("related di " + docnum4 + ": " + related.toString());
	    related = getBL().getRelatedDocuments(token, docnum5);
	    System.out.println("related di " + docnum5 + ": " + related.toString());



	    
	    try {
		fileInputStream = new FileInputStream(file);
	    }
	    catch (FileNotFoundException e1) {

		System.out.println(e1);
		return;
	    }

	    info = new HashMap<String, String>();
	    info.put("COD_ENTE", cod_ente);
	    info.put("COD_AOO", cod_aoo);
	    info.put("DOCNAME", "principale2.txt");
	    info.put("TYPE_ID", "documento");
	    info.put("FIRMATARIO", "<Segnatura><Intestazione><Oggetto>a</Oggetto></Intestazione></Segnatura>");

	    String docnum6 = getBL().createDocument(token, info, fileInputStream);
	    System.out.println("creato " + docnum6);

	    info.clear();
	    info.put("COD_ENTE", cod_ente);
	    info.put("COD_AOO", cod_aoo);
	    info.put("DOCNAME", "allegato2.txt");
	    info.put("TYPE_ID", "documento");
	    info.put("FIRMATARIO", "");

	    try {
		fileInputStream = new FileInputStream(file);
	    }
	    catch (FileNotFoundException e1) {

		System.out.println(e1);
		return;
	    }

	    String docnum7 = getBL().createDocument(token, info, fileInputStream);
	    System.out.println("creato " + docnum7);

	    try {
		fileInputStream = new FileInputStream(file);
	    }
	    catch (FileNotFoundException e1) {

		System.out.println(e1);
		return;
	    }

	    info.clear();
	    info.put("COD_ENTE", cod_ente);
	    info.put("COD_AOO", cod_aoo);
	    info.put("DOCNAME", "annesso2.txt");
	    info.put("TYPE_ID", "documento");
	    info.put("FIRMATARIO", "");

	    String docnum8 = getBL().createDocument(token, info, fileInputStream);
	    System.out.println("creato " + docnum8);

	    try {
		fileInputStream = new FileInputStream(file);
	    }
	    catch (FileNotFoundException e1) {

		System.out.println(e1);
		return;
	    }

	    info.clear();
	    info.put("COD_ENTE", cod_ente);
	    info.put("COD_AOO", cod_aoo);
	    info.put("DOCNAME", "annotazione2.txt");
	    info.put("TYPE_ID", "documento");
	    info.put("FIRMATARIO", "");

	    String docnum9 = getBL().createDocument(token, info, fileInputStream);
	    System.out.println("creato " + docnum9);

	    getBL().addRelated(token, docnum6, Arrays.asList(docnum7, docnum8, docnum9));

	    System.out.println("addRelated " + docnum6 + " -> " + Arrays.asList(docnum7, docnum8, docnum9).toString());

	    related = getBL().getRelatedDocuments(token, docnum6);
	    System.out.println("related di " + docnum6 + ": " + related.toString());
	    related = getBL().getRelatedDocuments(token, docnum7);
	    System.out.println("related di " + docnum7 + ": " + related.toString());
	    related = getBL().getRelatedDocuments(token, docnum8);
	    System.out.println("related di " + docnum8 + ": " + related.toString());
	    related = getBL().getRelatedDocuments(token, docnum9);
	    System.out.println("related di " + docnum9 + ": " + related.toString());

	    info.clear();
	    info.put("NUM_PG", "22222");
	    info.put("DATA_PG", "2012-06-12T10:22:12.000+02:00");
	    info.put("REGISTRO_PG", "PG");
	    info.put("ANNO_PG", "2014");
	    info.put("OGGETTO_PG", "oggetto protocollo");
	    info.put("TIPO_PROTOCOLLAZIONE", "E");
	    info.put("TIPO_FIRMA", "FD");
	    info.put("MITTENTI", "AAA");

	    getBL().protocollaDocumento(token, docnum6, info);


	    System.out.println("protocollato " + docnum6);

	    
	    getBL().addRelated(token, docnum1, Arrays.asList(docnum9));
	    System.out.println("addRelated " + docnum1 + " -> " + docnum9);

	    
	    
	    // getBL().addRelated(token, "61935", Arrays.asList("61670"));

	    // getBL().removeRelated(token, "61935", Arrays.asList("61670"));

	    // info.put("COD_ENTE", "ENTE8");
	    // info.put("COD_AOO", "AOO11");
	    // info.put("CLASSIFICA", "1.1.0");
	    // info.put("ANNO_FASCICOLO", "2012");
	    // info.put("PROGR_FASCICOLO", "2");
	    //
	    // getBL().fascicolaDocumento(token, "61760", info);

	    // info.clear();
	    // info.put("COD_ENTE", "ENTE8");
	    // info.put("COD_AOO", "AOO11");
	    // info.put("NUM_PG", "1");
	    // info.put("REGISTRO_PG", "REG PG");
	    // // info.put("DATA_PG", "2012-06-12T10:22:12.584+02:00");
	    // // info.put("DATA_PG", "2012-06-12");
	    // // info.put("DATA_PG", "2012-06-12 10:22:12");
	    // info.put("DATA_PG", "2012-06-12T10:22:12.000+02:00");
	    // info.put("ANNO_PG", "2012");
	    // info.put("OGGETTO_PG", "oggetto protocollo");
	    // info.put("TIPO_PROTOCOLLAZIONE", "E");
	    // info.put("TIPO_FIRMA", "FD");
	    // info.put("MITTENTI", "AAA");
	    //
	    // getBL().protocollaDocumento(token, "61935", info);
	    //
	    // getBL().addRelated(token, "61935", Arrays.asList("61670"));

	}
	catch (Exception e) {

	    System.out.println("testAllegati KO: " + e.getMessage());
	    // assertTrue(false);

	}
	finally {
	    try {
		getBL().logout(token);
	    }
	    catch (DocerException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }

	}
    }

    
    @Test
    public void testCreateDocumentCiclico() {

	String token = null;
	try {

	    token = getBL().login("", "admin", "admin");

	    String filePath = "C:\\test.txt";

	    String cod_ente = "ENTETEST";
	    String cod_aoo = "AOO_TEST";

	    
	    for(int i=0; i<1010; i++){
		FileInputStream fileInputStream = null;

		    File file = new File(filePath);
		    try {
			fileInputStream = new FileInputStream(file);
		    }
		    catch (FileNotFoundException e1) {

			System.out.println(e1);
			return;
		    }

		    // creo principale
		    Map<String, String> info = new HashMap<String, String>();
		    info.put("COD_ENTE", cod_ente);
		    info.put("COD_AOO", cod_aoo);
		    info.put("DOCNAME",  new Date().getTime() +".txt");
		    info.put("TYPE_ID", "documento");

		    String docnum1 = getBL().createDocument(token, info, fileInputStream);
		    System.out.println("creato " + docnum1);

		    // classifico
		    info.clear();
		    info.put("CLASSIFICA", "CLASSIFICATEST");
		    info.put("PROGR_FASCICOLO", "FASCICOLOTEST/FIGLIO");
		    info.put("ANNO_FASCICOLO", "2014");
		    
		    getBL().fascicolaDocumento(token, docnum1, info);
		    System.out.println("Fascicolato...");
	    
	    }
	    

	}
	catch (Exception e) {

	    System.out.println("KO: " + e.getMessage());
	    // assertTrue(false);

	}
	finally {
	    try {
		getBL().logout(token);
	    }
	    catch (DocerException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }

	}
    }

    
    @Test
    public void testAllegati() {

	String token = null;
	try {

	    token = getBL().login("", "admin", "admin");

	    getBL().addRelated(token, "14199", Arrays.asList("14206"));

	    System.out.println("testAllegati ok");
	}
	catch (Exception e) {

	    System.out.println("testAllegati KO: " + e.getMessage());
	    // assertTrue(false);

	}
	finally {
	    try {
		getBL().logout(token);
	    }
	    catch (DocerException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }

	}
    }

//    @Test
//    public void testCreateEnteAsSysadmin() {
//
//	String token = null;
//	try {
//
//	    token = getBL().login("", "sysadmin", "sysadmin");
//	    Map<String, String> info = new HashMap<String, String>();
//	    info.put("COD_ENTE", "ENTE_LUCA4");
//	    info.put("DES_ENTE", "des ENTE");
//	    info.put("ENABLED", "true");
//	    getBL().createEnte(token, info);
//
//	    Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
//
//	    searchCriteria.put("COD_ENTE", Arrays.asList("ENTE_LUCA4"));
//
//	    long startTime = new Date().getTime();
//	    long endTime = new Date().getTime();
//	    int i = 0;
//	    List<ISearchItem> res = new ArrayList<ISearchItem>();
//	    while (res.size() == 0) {
//		res = getBL().searchAnagrafiche(token, "ENTE", searchCriteria);
//
//		System.out.println("eseguita ricerca ");
//		if (res.size() == 0)
//		    continue;
//
//		endTime = new Date().getTime();
//
//		if ((endTime - startTime) > 20000) {
//
//		    System.out.println("interrotto dopo " + (endTime - startTime) + " msec");
//		    break;
//		}
//
//		System.out.println("risultato avuto dopo " + (endTime - startTime) + " msec");
//		ISearchItem si = res.get(0);
//		for (KeyValuePair kvp : si.getMetadata()) {
//		    System.out.println(kvp.getKey() + "=" + kvp.getValue());
//		}
//	    }
//	}
//	catch (Exception e) {
//
//	    System.out.println("ricerca KO: " + e.getMessage());
//	    // assertTrue(false);
//
//	}
//	finally {
//	    try {
//		getBL().logout(token);
//	    }
//	    catch (DocerException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	    }
//
//	}
//    }

    
    
    @Test
    public void testSearchAnagr1() {

	String token = null;
	try {

	    token = getBL().login("", "admin", "admin");

	    Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();

	    searchCriteria.put("COD_ENTE", Arrays.asList("*"));

	    long startTime = new Date().getTime();
	    long endTime = new Date().getTime();
	    int i = 0;
	    List<ISearchItem> res = new ArrayList<ISearchItem>();
	    while (res.size() == 0) {
		res = getBL().searchAnagrafiche(token, "initmdrichiedenti", searchCriteria);

		System.out.println("eseguita ricerca ");
		if (res.size() == 0)
		    continue;

		endTime = new Date().getTime();

		if ((endTime - startTime) > 20000) {

		    System.out.println("interrotto dopo " + (endTime - startTime) + " msec");
		    break;
		}

		System.out.println("risultato avuto dopo " + (endTime - startTime) + " msec");
		ISearchItem si = res.get(0);
		for (KeyValuePair kvp : si.getMetadata()) {
		    System.out.println(kvp.getKey() + "=" + kvp.getValue());
		}
	    }
	}
	catch (Exception e) {

	    System.out.println("ricerca KO: " + e.getMessage());
	    // assertTrue(false);

	}
	finally {
	    try {
		getBL().logout(token);
	    }
	    catch (DocerException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }

	}
    }
    
    @Test
    public void testSearchAnagr() {

	String token = null;
	try {

	    token = getBL().login("", "admin", "admin");

	    Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();

	    searchCriteria.put("COD_ENTE", Arrays.asList("*"));

	    long startTime = new Date().getTime();
	    long endTime = new Date().getTime();
	    int i = 0;
	    List<ISearchItem> res = new ArrayList<ISearchItem>();
	    while (res.size() == 0) {
		res = getBL().searchAnagrafiche(token, "ENTE", searchCriteria);

		System.out.println("eseguita ricerca ");
		if (res.size() == 0)
		    continue;

		endTime = new Date().getTime();

		if ((endTime - startTime) > 20000) {

		    System.out.println("interrotto dopo " + (endTime - startTime) + " msec");
		    break;
		}

		System.out.println("risultato avuto dopo " + (endTime - startTime) + " msec");
		ISearchItem si = res.get(0);
		for (KeyValuePair kvp : si.getMetadata()) {
		    System.out.println(kvp.getKey() + "=" + kvp.getValue());
		}
	    }
	}
	catch (Exception e) {

	    System.out.println("ricerca KO: " + e.getMessage());
	    // assertTrue(false);

	}
	finally {
	    try {
		getBL().logout(token);
	    }
	    catch (DocerException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }

	}
    }

    @Test
    public void testSearchFascicoli1000() {

	String token = null;
	try {

	    token = getBL().login("", "admin", "admin");

	    Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();

	    searchCriteria.put("COD_ENTE", Arrays.asList("EMR"));

	    long startTime = new Date().getTime();
	    long endTime = new Date().getTime();
	    int i = 0;
	    List<ISearchItem> res = getBL().searchAnagrafiche(token, "FASCICOLO", searchCriteria);

		System.out.println("eseguita ricerca ");

		endTime = new Date().getTime();

		System.out.println("risultato avuto dopo " + (endTime - startTime) + " msec");
		
//		ISearchItem si = res.get(0);
//		for (KeyValuePair kvp : si.getMetadata()) {
//		    System.out.println(kvp.getKey() + "=" + kvp.getValue());
//		}
	    
	}
	catch (Exception e) {

	    System.out.println("ricerca KO: " + e.getMessage());
	    // assertTrue(false);

	}
	finally {
	    try {
		getBL().logout(token);
	    }
	    catch (DocerException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }

	}
    }
    
    @Test
    public void testSearchDocumenti1000() {

	String token = null;
	try {

	    token = getBL().login("", "admin", "admin");

	    Map<String,String> userProfile = getBL().getUser(token, "admin");
	    
	    
	    Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();

	    

	    long startTime = new Date().getTime();
	    long endTime = new Date().getTime();
	    
	    searchCriteria.put("COD_ENTE", Arrays.asList("EMR"));
	    // searchCriteria.put("COD_AOO", Arrays.asList("C-I840-01"));
	    // searchCriteria.put("ID_REGISTRO", Arrays.asList("DETE1"));
	    // searchCriteria.put("A_REGISTRAZ", Arrays.asList("2013"));

	    Map<String, EnumSearchOrder> orderby = new HashMap<String, EnumSearchOrder>();
	    orderby.put("CREATION_DATE", EnumSearchOrder.DESC);

	    List<ISearchItem> res = getBL().searchDocuments(token, searchCriteria, null, 10000, orderby);

		System.out.println("trovati " +res.size() +" risultati");

		endTime = new Date().getTime();

		System.out.println("risultato avuto dopo " + (endTime - startTime) + " msec");
		
//		for(ISearchItem isi : res){
//			System.out.println("----------------------");
//			System.out.println("");
//			for (KeyValuePair kvp : isi.getMetadata()) {
//			    System.out.println(kvp.getKey() + "=" + kvp.getValue());
//			}
//		}
		
	    
	}
	catch (Exception e) {

	    System.out.println("ricerca KO: " + e.getMessage());
	    // assertTrue(false);

	}
	finally {
	    try {
		getBL().logout(token);
	    }
	    catch (DocerException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }

	}
    }
    
    @Test
    public void testSearchAnagrafiche() {

	String token = null;
	try {

	    token = getBL().login("", "admin", "admin");

	    Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();

	    searchCriteria.put("COD_ENTE", Arrays.asList("ENTETEST"));

	    List<ISearchItem> res = new ArrayList<ISearchItem>();
	    while (res.size() == 0) {
		System.out.println("-----------ENTE-------------");
		res = getBL().searchAnagrafiche(token, "ENTE", searchCriteria);

		ISearchItem si = res.get(0);
			for (KeyValuePair kvp : si.getMetadata()) {
			    System.out.println(kvp.getKey() + "=" + kvp.getValue());
			}
	    }

	    searchCriteria.put("COD_AOO", Arrays.asList("AOOTEST"));

	    res = new ArrayList<ISearchItem>();
	    while (res.size() == 0) {
		System.out.println("-----------AOO-------------");
		res = getBL().searchAnagrafiche(token, "AOO", searchCriteria);

		ISearchItem si = res.get(0);
		for (KeyValuePair kvp : si.getMetadata()) {
		    System.out.println(kvp.getKey() + "=" + kvp.getValue());
		}
	    }

	    searchCriteria.put("CLASSIFICA", Arrays.asList("CLASSIFICATEST"));

	    res = new ArrayList<ISearchItem>();
	    while (res.size() == 0) {
		System.out.println("-----------TITOLARIO-------------");
		res = getBL().searchAnagrafiche(token, "TITOLARIO", searchCriteria);

		ISearchItem si = res.get(0);
		for (KeyValuePair kvp : si.getMetadata()) {
		    System.out.println(kvp.getKey() + "=" + kvp.getValue());
		}
	    }

	    searchCriteria.put("ANNO_FASCICOLO", Arrays.asList("2014"));
	    searchCriteria.put("PROGR_FASCICOLO", Arrays.asList("FASCICOLOTEST"));

	    res = new ArrayList<ISearchItem>();
	    while (res.size() == 0) {
		System.out.println("-----------FASCICOLO-------------");
		res = getBL().searchAnagrafiche(token, "FASCICOLO", searchCriteria);

		ISearchItem si = res.get(0);
		for (KeyValuePair kvp : si.getMetadata()) {
		    System.out.println(kvp.getKey() + "=" + kvp.getValue());
		}
	    }

	}
	catch (Exception e) {

	    System.out.println("ricerca KO: " + e.getMessage());
	    // assertTrue(false);

	}
	finally {
	    try {
		getBL().logout(token);
	    }
	    catch (DocerException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }

	}
    }

    @Test
    public void testSearchEnte() {

	String token = null;
	try {

	    token = getBL().login("", "sysadmin", "sysadmin");

	    Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();

	    searchCriteria.put("COD_ENTE", Arrays.asList("TEE"));

	    List<ISearchItem> res = getBL().searchAnagrafiche(token, "ENTE", searchCriteria);
	    System.out.println("ricerca ok risultati: " + res.size());

	    ISearchItem si = res.get(0);
	    for (KeyValuePair kvp : si.getMetadata()) {
		System.out.println(kvp.getKey() + "=" + kvp.getValue());
	    }
	}
	catch (Exception e) {

	    System.out.println("ricerca KO: " + e.getMessage());
	    // assertTrue(false);

	}
	finally {
	    try {
		getBL().logout(token);
	    }
	    catch (DocerException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }

	}

    }

    @Test
    public void testClassificaDocumento1() {

	try {

	    Map<String, String> info = new HashMap<String, String>();
	    info.put("COD_ENTE", cod_ente);
	    info.put("COD_AOO", cod_aoo);
	    info.put("CLASSIFICA", "1.0.0");

	    getBL().classificaDocumento(getToken(), docid_1, info);
	    System.out.println("classifica ok");

	}
	catch (Exception e) {

	    System.out.println("classifica OK: " + e.getMessage());
	    // assertTrue(false);
	}

    }

    @Test
    public void testClassificaDocumento2() {

	try {

	    Map<String, String> info = new HashMap<String, String>();
	    info.put("COD_ENTE", cod_ente);
	    info.put("COD_AOO", cod_aoo);
	    info.put("CLASSIFICA", "1.1.0");

	    getBL().classificaDocumento(getToken(), docid_1, info);
	    System.out.println("classifica ok");
	    // assertTrue(true);

	}
	catch (Exception e) {

	    System.out.println("classifica OK: " + e.getMessage());
	    // assertTrue(false);
	}

    }

    @Test
    public void testFascicolaDocumento1() {

	try {

	    Map<String, String> info = new HashMap<String, String>();
	    info.put("COD_ENTE", "EMR");
	    info.put("COD_AOO", "AOO_EMR");
	    info.put("CLASSIFICA", "1");
	    info.put("ANNO_FASCICOLO", "2013");
	    info.put("PROGR_FASCICOLO", "1");

	    getBL().fascicolaDocumento(getToken(), docid_1, info);
	    System.out.println("fascicola 1 ok");
	    // assertTrue(true);

	}
	catch (Exception e) {

	    System.out.println("fascicola 1 KO: " + e.getMessage());
	    // assertTrue(false);
	}
    }

    @Test
    public void testAnnullaFascicoloDocumento() {

	try {

	    Map<String, String> info = new HashMap<String, String>();
	    info.put("COD_ENTE", cod_ente);
	    info.put("COD_AOO", cod_aoo);
	    info.put("CLASSIFICA", "200.100.10.20");
	    info.put("ANNO_FASCICOLO", "2005");
	    info.put("PROGR_FASCICOLO", "5");

	    // info.put("CLASSIFICA", "1");
	    // info.put("ANNO_FASCICOLO", "2013");
	    // info.put("PROGR_FASCICOLO", "1");

	    // info.put("CLASSIFICA", "1");
	    // info.put("ANNO_FASCICOLO", "");
	    // info.put("PROGR_FASCICOLO", "");

	    info.put("FASC_SECONDARI", "");
	    // info.put("FASC_SECONDARI", "");

	    getBL().fascicolaDocumento(getToken(), docid_1, info);
	    System.out.println("annulla fascicolazione ok");
	    // assertTrue(true);

	}
	catch (Exception e) {

	    System.out.println("annulla fascicolazione KO: " + e.getMessage());
	    // assertTrue(false);
	}
    }

    @Test
    public void testFascicolaDocumento3() {

	try {

	    Map<String, String> info = new HashMap<String, String>();
	    info.put("COD_ENTE", cod_ente);
	    info.put("COD_AOO", cod_aoo);
	    info.put("CLASSIFICA", "1.1.0");
	    info.put("ANNO_FASCICOLO", "2012");
	    info.put("PROGR_FASCICOLO", "2/1/XXX");

	    getBL().fascicolaDocumento(getToken(), docid_1, info);
	    System.out.println("fascicola 3 ok");
	    // assertTrue(true);

	}
	catch (Exception e) {

	    System.out.println("fascicola 3 KO: " + e.getMessage());
	    // assertTrue(false);
	}
    }

    @Test
    public void testFascicolaDocumento4() {

	try {

	    Map<String, String> info = new HashMap<String, String>();
	    info.put("COD_ENTE", cod_ente);
	    info.put("COD_AOO", cod_aoo);
	    info.put("CLASSIFICA", "1.1.0");
	    info.put("ANNO_FASCICOLO", "2012");
	    info.put("PROGR_FASCICOLO", "2");

	    getBL().fascicolaDocumento(getToken(), docid_1, info);
	    System.out.println("fascicolazione 4 ok");
	    // assertTrue(true);

	}
	catch (Exception e) {
	    System.out.println("fascicolazione 4 KO: " + e.getMessage());
	    // assertTrue(false);
	}
    }

    @Test
    public void testFascicolaDocumento5() {

	try {

	    Map<String, String> info = new HashMap<String, String>();
	    info.put("COD_ENTE", cod_ente);
	    info.put("COD_AOO", cod_aoo);
	    info.put("CLASSIFICA", "1.0.0");
	    info.put("ANNO_FASCICOLO", "2012");
	    info.put("PROGR_FASCICOLO", "1");

	    getBL().fascicolaDocumento(getToken(), docid_1, info);
	    System.out.println("fascicola 2 ok");
	    // assertTrue(true);

	}
	catch (Exception e) {

	    System.out.println("fascicola 2 OK: " + e.getMessage());
	    // assertTrue(false);
	}
    }

    @Test
    public void testAnnullaFascicolaDocumento2() {

	try {

	    Map<String, String> info = new HashMap<String, String>();
	    // info.put("COD_ENTE", cod_ente);
	    // info.put("COD_AOO", cod_aoo);
	    // info.put("CLASSIFICA", "1.1.0");
	    info.put("ANNO_FASCICOLO", "");
	    info.put("PROGR_FASCICOLO", "");

	    getBL().fascicolaDocumento(getToken(), docid_1, info);
	    System.out.println("annulla fascicolazione 2 ok");
	    // assertTrue(true);

	}
	catch (Exception e) {

	    System.out.println("annulla fascicolazione 2 KO " + e.getMessage());
	    // assertTrue(false);
	}
    }

    @Test
    public void testPubblicaDocumento() {

	try {

	    Map<String, String> info = new HashMap<String, String>();
	    info.put("COD_ENTE", cod_ente);
	    info.put("COD_AOO", cod_aoo);

	    info.put(Constants.doc_pubblicazione_registro, "xxx");
	    info.put(Constants.doc_pubblicazione_anno, "2012");
	    info.put(Constants.doc_pubblicazione_data_fine, "2012-12-12");
	    info.put(Constants.doc_pubblicazione_data_inizio, "2012-12-12");
	    info.put(Constants.doc_pubblicazione_numero, "222");
	    info.put(Constants.doc_pubblicazione_oggetto, "oggetto");
	    info.put(Constants.doc_pubblicazione_pubblicato, "true");

	    getBL().pubblicaDocumento(getToken(), docid_1, info);
	    System.out.println("pubblica ok");
	    // assertTrue(true);

	}
	catch (Exception e) {

	    System.out.println(e.getMessage());
	    // assertTrue(false);
	}

    }

    @Test
    public void test1() {
	testLogin();
	// docid_1 = "18278";
	testGetProfileDocument();
	testLogout();
    }

    @Test
    public void testLogin() {
	try {
	    token = getBL().login(ente, userAdmin, userAdminPassword);
	    System.out.println("login effettuata");
	    // assertTrue(true);
	}
	catch (DocerException e) {
	    System.out.println(e.getMessage());
	    // assertTrue(false);
	}
    }

    @Test
    public void testWC() {
	try {
	    token = getBL().login("", "admin", "admin");

	    InputStream is = this.getClass().getResourceAsStream("/confstub.xml");

	    StringWriter writer = new StringWriter();
	    IOUtils.copy(is, writer, "UTF-8");
	    String theString = writer.toString();

	    getBL().writeConfig(token, theString);

	    System.out.println("");
	}
	catch (DocerException e) {
	    System.out.println(e.getMessage());
	    // assertTrue(false);
	}
	catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	finally {
	    try {
		getBL().logout(token);
	    }
	    catch (DocerException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}
    }

    @Test
    public void testGetEffectiveRights() {
	try {
	    token = getBL().login("", "admin", "Kdm.2001");

	    EnumACLRights rights = getBL().getUserRights(token, "4170as", "System");

	    System.out.println(rights);
	}
	catch (DocerException e) {
	    System.out.println(e.getMessage());
	    // assertTrue(false);
	}
	finally {
	    try {
		getBL().logout(token);
	    }
	    catch (DocerException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}
    }

    @Test
    public void testCreateAnagrafiche() throws DocerException {

	token = getBL().login("", "admin", "admin");

	// // ENTE
	Map<String, String> info = new HashMap<String, String>();
	// info.put("COD_ENTE", cod_ente);
	// info.put("DES_ENTE", "des ENTE");
	// info.put("ENABLED", "true");
	// try {
	// getBL().createEnte(getToken(), info);
	// System.out.println("ente creato");
	// }
	// catch (Exception e) {
	// System.out.println(e.getMessage());
	// }

	// creao AOO
	info.clear();
	info.put("COD_ENTE", "ENTE2");
	info.put("COD_AOO", "AOO3");
	info.put("DES_AOO", "des AOO 3");
	info.put("ENABLED", "true");
	try {
	    getBL().createAOO(getToken(), info);
	    System.out.println("aoo creato");
	    // assertTrue(true);
	}
	catch (Exception e) {
	    System.out.println(e.getMessage());
	    // assertTrue(false);
	}

	// // TiToLARIO
	// info.clear();
	// info.put("COD_ENTE", cod_ente);
	// info.put("COD_AOO", cod_aoo);
	// info.put("CLASSIFICA", "1.0.0");
	// info.put("DES_TITOLARIO", "des 1.0.0");
	// info.put("ENABLED", "true");
	// try {
	// getBL().createTitolario(getToken(), info);
	// System.out.println("titolario 1.0.0 creato");
	// // assertTrue(true);
	// }
	// catch (Exception e) {
	// System.out.println(e.getMessage());
	// // assertTrue(false);
	// }
	//
	// // TIT2
	// info.clear();
	// info.put("COD_ENTE", cod_ente);
	// info.put("COD_AOO", cod_aoo);
	// info.put("PARENT_CLASSIFICA", "1.0.0");
	// info.put("CLASSIFICA", "1.1.0");
	// info.put("DES_TITOLARIO", "des 1.1.0");
	// info.put("ENABLED", "true");
	// try {
	// getBL().createTitolario(getToken(), info);
	// System.out.println("classifica 1.1.0 creato");
	// // assertTrue(true);
	// }
	// catch (Exception e) {
	// System.out.println(e.getMessage());
	// // assertTrue(false);
	// }
	//
	// // FASC
	// info.clear();
	// info.put("COD_ENTE", cod_ente);
	// info.put("COD_AOO", cod_aoo);
	// info.put("CLASSIFICA", "1.0.0");
	// info.put("ANNO_FASCICOLO", "2012");
	// info.put("PROGR_FASCICOLO", "1");
	// info.put("DES_FASCICOLO", "des fascicolo 1");
	// info.put("ENABLED", "true");
	// try {
	// getBL().createFascicolo(getToken(), info);
	// System.out.println("fascicolo 1 creato");
	// // assertTrue(true);
	// }
	// catch (Exception e) {
	// System.out.println(e.getMessage());
	// // assertTrue(false);
	// }
	//
	// // FASC2
	// info.clear();
	// info.put("COD_ENTE", cod_ente);
	// info.put("COD_AOO", cod_aoo);
	// info.put("CLASSIFICA", "1.0.0");
	// info.put("ANNO_FASCICOLO", "2012");
	// info.put("PROGR_FASCICOLO", "1/1");
	// info.put("DES_FASCICOLO", "des fascicolo 1/1");
	// info.put("ENABLED", "true");
	// try {
	// getBL().createFascicolo(getToken(), info);
	// System.out.println("fascicolo 1/1 creato");
	// // assertTrue(true);
	// }
	// catch (Exception e) {
	// System.out.println(e.getMessage());
	// // assertTrue(false);
	// }
	//
	// info.clear();
	// info.put("COD_ENTE", cod_ente);
	// info.put("COD_AOO", cod_aoo);
	// info.put("CLASSIFICA", "1.1.0");
	// info.put("ANNO_FASCICOLO", "2012");
	// info.put("PROGR_FASCICOLO", "2");
	// info.put("DES_FASCICOLO", "des fascicolo 2");
	// info.put("ENABLED", "true");
	// try {
	// getBL().createFascicolo(token, info);
	// System.out.println("fascicolo 2 creato");
	// }
	// catch (Exception e) {
	// System.out.println(e.getMessage());
	// }
	// info.clear();
	// info.put("COD_ENTE", cod_ente);
	// info.put("COD_AOO", cod_aoo);
	// info.put("CLASSIFICA", "1.1.0");
	// info.put("ANNO_FASCICOLO", "2012");
	// info.put("PROGR_FASCICOLO", "2/1/XXX");
	// info.put("DES_FASCICOLO", "des fascicolo 2/1/XXX");
	// info.put("ENABLED", "true");
	//
	// info.put("DATA_APERTURA", "2012-06-12T10:22:12.584+02:00");
	// info.put("DATA_CHIUSURA", "2012-06-12T10:22:12.584+02:00");
	// info.put("CF_PERSONA", "2012-06-12T10:22:12.584+02:00");
	// info.put("CF_AZIENDA", "2012-06-12T10:22:12.584+02:00");
	// info.put("ID_PROC", "2012060200");
	// info.put("ID_IMMOBILE", "20121111111");
	//
	// try {
	// getBL().createFascicolo(token, info);
	// System.out.println("fascicolo 2/1/XXX creato");
	// }
	// catch (Exception e) {
	// System.out.println(e.getMessage());
	// }
	// Map<String, String> id = new HashMap<String, String>();
	//
	// id.put("COD_ENTE", cod_ente);
	// id.put("COD_AOO", cod_aoo);
	// id.put("CLASSIFICA", "1.1.0");
	// id.put("ANNO_FASCICOLO", "2012");
	// id.put("PROGR_FASCICOLO", "2/1/XXX");
	//
	// try {
	// info = getBL().getFascicolo(token, id);
	// }
	// catch (DocerException e1) {
	// System.out.println("getFascicolo: " + e1.getMessage());
	// }
	// if (info == null) {
	// System.out.println("fascicolo non trovato xxx");
	// }
	// else
	// for (String key : info.keySet()) {
	// System.out.println(key + " = " + info.get(key));
	// }
	//
	// info.clear();
	// info.put("TYPE_ID", "AREA_TEMATICA");
	// info.put("COD_ENTE", cod_ente);
	// info.put("COD_AOO", cod_aoo);
	// info.put("COD_AREA", "AREA1");
	// info.put("DES_AREA", "des AREA1");
	// info.put("ENABLED", "true");
	// try {
	// getBL().createAnagraficaCustom(token, info);
	// System.out.println("area 1 creato");
	// }
	// catch (Exception e) {
	// System.out.println(e.getMessage());
	// }
	//
	// info.clear();
	// info.put("TYPE_ID", "AREA_TEMATICA");
	// info.put("COD_ENTE", cod_ente);
	// info.put("COD_AOO", cod_aoo);
	// info.put("COD_AREA", "AREA2");
	// info.put("DES_AREA", "des AREA2");
	// info.put("ENABLED", "true");
	// try {
	// getBL().createAnagraficaCustom(token, info);
	// System.out.println("area 2 creato");
	// }
	// catch (Exception e) {
	// System.out.println(e.getMessage());
	// }

	// assertTrue(true);

	// assertTrue(false);

    }

    
     @Test
     public void testGetDocumentTypes() {
    
    
     Properties p = new Properties();
     try {
     p.loadFromXML(this.getClass().getResourceAsStream(
     "/docer_config.xml"));
    
     // appena viene invocato il WS recupero l'informazione del 'provider'
     // dal file 'config.xml'
     String providerName = p.getProperty("provider");
    
     BusinessLogic.setConfigPath(p.getProperty("CONFIG_DIR"));
    
     BusinessLogic bl = new BusinessLogic(providerName, 1000);
    
    
     String token = bl.login("ENTEPROVA", "admin", "admin");
    
     List<IKeyValuePair> list = bl.getDocumentTypes(token, "ENTEPROVA", "AOOPROVA");
     
     System.out.println("\n\n\n\n");
     for(IKeyValuePair kvp : list){
	 System.out.println(kvp.getKey() +" -> " +kvp.getValue());
     }
    
     list = bl.getDocumentTypes(token);
     
     System.out.println("\n\n\n\n");
     for(IKeyValuePair kvp : list){
	 System.out.println(kvp.getKey() +" -> " +kvp.getValue());
     }
    
     
     //bl.writeConfig(token, null);
//    
//     bl = new BusinessLogic(providerName, 1000);
//     list = bl.getDocumentTypes(token, "ENTE1", "AOO11");
//     for(IKeyValuePair kvp : list){
//     System.out.println(kvp.getKey() +" -> " +kvp.getValue());
//     }
     }
     catch(Exception e){
     System.out.println(e.getMessage());
     //assertTrue(false);
     }
    
     }

    // @Test
    // public void testBusinessLogic() {
    //
    //
    // Properties p = new Properties();
    // try {
    // p.loadFromXML(this.getClass().getResourceAsStream(
    // "/docer_config.xml"));
    //
    // // appena viene invocato il WS recupero l'informazione del 'provider'
    // // dal file 'config.xml'
    // String providerName = p.getProperty("provider");
    //
    // BusinessLogic.setConfigPath(p.getProperty("CONFIG_DIR"));
    //
    // BusinessLogic bl = new BusinessLogic(providerName, 1000);
    //
    // bl = new BusinessLogic(providerName, 1000);
    // String token = bl.login("DOCAREA", "admin", "admin");
    //
    // bl.writeConfig(token, null);
    // bl = new BusinessLogic(providerName, 1000);
    // token = bl.login("DOCAREA", "admin", "admin");
    // }
    // catch(Exception e){
    // System.out.println(e.getMessage());
    // //assertTrue(false);
    // }
    //
    // }

    // @Test
    // public void testCreateEnteeAOO(){
    //
    // String token = null;
    // BusinessLogic bl = null;
    // Properties p = new Properties();
    // try {
    // p.loadFromXML(this.getClass().getResourceAsStream(
    // "/docer_config.xml"));
    // }
    // catch(Exception e){
    // System.out.println(e.getMessage());
    // return;
    // }
    //
    // // appena viene invocato il WS recupero l'informazione del 'provider'
    // // dal file 'config.xml'
    // String providerName = p.getProperty("provider");
    //
    // BusinessLogic.setConfigPath(p.getProperty("CONFIG_DIR"));
    //
    // bl = new BusinessLogic(providerName, 1000);
    // token = bl.login("ente1", "admin", "admin");
    //
    // // creo ENTE
    // Map<String,String> info = new HashMap<String,String>();
    // info.put("COD_ENTE", "ENTEX");
    // info.put("DES_ENTE", "des ENTEX");
    // info.put("ENABLED", "true");
    // try{
    // bl.createEnte(token, info);
    // System.out.println("ente creato");
    // }
    // catch(Exception e){
    // System.out.println(e.getMessage());
    // }
    //
    //
    //
    // // creao AOO
    // info.clear();
    // info.put("COD_ENTE", "ENTEX");
    // info.put("COD_AOO", "AOOX");
    // info.put("DES_AOO", "des AOOX");
    // info.put("ENABLED", "true");
    // try{
    // bl.createAOO(token, info);
    // System.out.println("aoo creato");
    // }
    // catch(Exception e){
    // System.out.println(e.getMessage());
    // }
    // }
    //

    @Test
    public void testCreateUsers() {

	Map<String, String> info = new HashMap<String, String>();
	info.put("USER_ID", user1);
	info.put("FULL_NAME", user1);
	info.put("COD_ENTE", cod_ente);
	info.put("COD_AOO", cod_aoo);
	try {
	    getBL().createUser(getToken(), info);
	    System.out.println("utente " + user1 + " creato");
	    // assertTrue(true);
	}
	catch (Exception e) {
	    System.out.println(e.getMessage());
	    // assertTrue(false);
	}

	info.clear();
	info.put("USER_ID", user2);
	info.put("FULL_NAME", user2);
	info.put("COD_ENTE", cod_ente);
	info.put("COD_AOO", cod_aoo);
	try {
	    getBL().createUser(getToken(), info);
	    System.out.println("utente " + user2 + " creato");
	    // assertTrue(true);
	}
	catch (Exception e) {
	    System.out.println(e.getMessage());
	    // assertTrue(false);
	}

	info.clear();
	info.put("USER_ID", user3);
	info.put("FULL_NAME", user3);
	info.put("COD_ENTE", cod_ente);
	info.put("COD_AOO", cod_aoo);
	try {
	    getBL().createUser(getToken(), info);
	    System.out.println("utente " + user3 + " creato");
	    // assertTrue(true);
	}
	catch (Exception e) {
	    System.out.println(e.getMessage());
	    // assertTrue(false);
	}

    }

    @Test
    public void testCreateGroups() {

	Map<String, String> info = new HashMap<String, String>();
	info.put("GROUP_ID", "group1");
	info.put("GROUP_NAME", "group1");
	info.put("PARENT_GROUP_ID", cod_aoo);
	try {
	    getBL().createGroup(getToken(), info);
	    System.out.println("gruppo group1 creato");
	    // assertTrue(true);
	}
	catch (Exception e) {
	    System.out.println(e.getMessage());
	    // assertTrue(false);
	}

	info.clear();
	info.put("GROUP_ID", "group2");
	info.put("GROUP_NAME", "group2");
	info.put("PARENT_GROUP_ID", cod_aoo);
	try {
	    getBL().createGroup(getToken(), info);
	    System.out.println("gruppo group2 creato");
	    // assertTrue(true);
	}
	catch (Exception e) {
	    System.out.println(e.getMessage());
	    // assertTrue(false);
	}

	info.clear();
	info.put("GROUP_ID", "group3");
	info.put("GROUP_NAME", "group3");
	info.put("PARENT_GROUP_ID", "group2");
	try {
	    getBL().createGroup(getToken(), info);
	    System.out.println("gruppo group3 creato sotto group2");
	    // assertTrue(true);
	}
	catch (Exception e) {
	    System.out.println(e.getMessage());
	    // assertTrue(false);
	}

    }

    @Test
    public void testSetGroupsOfUser() {

	try {
	    getBL().setGroupsOfUser(getToken(), user1, Arrays.asList(cod_ente, cod_aoo, "group1", "group2"));
	    System.out.println("impostati " + cod_ente + ", " + cod_aoo + " group1 e group2 come gruppi di user1");
	    // assertTrue(true);

	}
	catch (Exception e) {
	    System.out.println(e.getMessage());
	    // assertTrue(false);
	}

    }

    @Test
    public void testSetUsersOfGroup() {

	try {
	    getBL().setUsersOfGroup(getToken(), "group3", Arrays.asList(user1, user2, user3));
	    System.out.println("impostati " + user1 + ", " + user2 + ", " + user3 + " come utenti di  group3");
	    // assertTrue(true);

	}
	catch (Exception e) {
	    System.out.println(e.getMessage());
	    // assertTrue(false);
	}

    }

    @Test
    public void testCreateDoc() {

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

	    token = getBL().login("", "admin", "admin");

	    Map<String, String> info = new HashMap<String, String>();

	    info.put("FIRMATARIO", "<Segnatura><Intestazione><Oggetto>a</Oggetto></Intestazione></Segnatura>");
	 

            info.put("DOCNAME","Test Documento Protocollato.pdf.p7m");
            info.put("TYPE_ID","DOCUMENTI_PROTOCOLLATI_PARER");
            info.put("COD_ENTE","ENTE_PARER");
            info.put("COD_AOO","AOO_PARER");
            info.put("TIPO_COMPONENTE","PRINCIPALE");
            info.put("modalita_trasmissione","modalita trasmissione");
            info.put("descrizione_documento","LETTERA");
            info.put("note_doc_prot","note doc prot");
            info.put("operatore_di_protocollo","operatore di protocollo");
            info.put("reg_emergenza_registro","PROTOCOLLO_PG_EMERGENZA");
            info.put("reg_emergenza_anno","2014");
            info.put("reg_emergenza_numero","1");
            info.put("reg_emergenza_data","2014-06-11T16:20:30.45");
            info.put("validita","validita");
            info.put("esecutore_annullamento","esecutore annullamento");
            info.put("tempo_di_conservazione","1");
            info.put("identificazione_repository","identificazione repository");


	    String docnum = getBL().createDocument(getToken(), info, fileInputStream);
	    System.out.println("creato " + docnum);
	    return;
	    // System.out.println(docid_1);
	    // assertTrue(true);

	}
	catch (Exception e) {

	    System.out.println(e.getMessage());

	    if (fileInputStream != null)
		try {
		    fileInputStream.close();
		}
		catch (IOException e1) {
		    // TODO Auto-generated catch block
		    e1.printStackTrace();
		}
	    // System.out.println(e.getMessage());
	    // assertTrue(false);
	    return;
	}
	finally {
	    try {
		getBL().logout(token);
	    }
	    catch (DocerException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}

    }

    @Test
    public void testCreateDocument1() {

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

	    Map<String, String> info = new HashMap<String, String>();
	    info.put("COD_ENTE", "EMR");
	    info.put("COD_AOO", "AOO2");
	    info.put("DOCNAME", "bersani.txt");
	    info.put("TYPE_ID", "documento");
	    info.put("p_annull_pg", "D");
	    info.put("ARCHIVE_TYPE", "URL");
	    info.put("DOC_URL", "http://kdm.it/filex.txt");

	    fileInputStream = null;
	    docid_1 = getBL().createDocument(getToken(), info, fileInputStream);
	    System.out.println("create document 1 ok: " + docid_1);
	    // assertTrue(true);

	}
	catch (Exception e) {

	    System.out.println("create document 1 KO" + e.getMessage());

	    if (fileInputStream != null)
		try {
		    fileInputStream.close();
		}
		catch (IOException e1) {
		    // TODO Auto-generated catch block
		    e1.printStackTrace();
		}
	    // System.out.println(e.getMessage());
	    // assertTrue(false);
	}

    }

    @Test
    public void testCreateDocument2() {

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

	    Map<String, String> info = new HashMap<String, String>();
	    info.put("COD_ENTE", cod_ente);
	    info.put("COD_AOO", cod_aoo);
	    info.put("DOCNAME", "test.txt");
	    info.put("TYPE_ID", "documento");

	    docid_2 = getBL().createDocument(getToken(), info, fileInputStream);
	    System.out.println("create document 2 ok " + docid_2);
	    // assertTrue(true);

	}
	catch (Exception e) {

	    System.out.println("create document 2 KO: " + e.getMessage());

	    if (fileInputStream != null)
		try {
		    fileInputStream.close();
		}
		catch (IOException e1) {
		    // TODO Auto-generated catch block
		    e1.printStackTrace();
		}
	}

    }

    @Test
    public void testCreateDocument3() {

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

	    Map<String, String> info = new HashMap<String, String>();
	    info.put("COD_ENTE", cod_ente);
	    info.put("COD_AOO", cod_aoo);
	    info.put("DOCNAME", "test.txt");
	    info.put("TYPE_ID", "documento");

	    docid_3 = getBL().createDocument(getToken(), info, fileInputStream);
	    System.out.println("create document ok: " + docid_3);

	}
	catch (Exception e) {

	    if (fileInputStream != null)
		try {
		    fileInputStream.close();
		}
		catch (IOException e1) {
		    // TODO Auto-generated catch block
		    e1.printStackTrace();
		}
	    System.out.println("create document KO: " + e.getMessage());
	    // assertTrue(false);
	}

    }

    @Test
    public void testCreateDocument4() {

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

	    Map<String, String> info = new HashMap<String, String>();
	    info.put("COD_ENTE", cod_ente);
	    info.put("COD_AOO", cod_aoo);
	    info.put("DOCNAME", "test.txt");
	    info.put("TYPE_ID", "documento");

	    docid_4 = getBL().createDocument(getToken(), info, fileInputStream);
	    System.out.println("create document ok: " + docid_4);

	}
	catch (Exception e) {

	    if (fileInputStream != null)
		try {
		    fileInputStream.close();
		}
		catch (IOException e1) {
		    // TODO Auto-generated catch block
		    e1.printStackTrace();
		}
	    System.out.println("create document KO: " + e.getMessage());
	    // assertTrue(false);
	}

    }

    @Test
    public void testCreateDocument5() {

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

	    Map<String, String> info = new HashMap<String, String>();
	    info.put("COD_ENTE", cod_ente);
	    info.put("COD_AOO", cod_aoo);
	    info.put("DOCNAME", "test.txt");
	    info.put("TYPE_ID", "documento");

	    docid_5 = getBL().createDocument(getToken(), info, fileInputStream);
	    System.out.println("create document ok: " + docid_5);

	}
	catch (Exception e) {

	    if (fileInputStream != null)
		try {
		    fileInputStream.close();
		}
		catch (IOException e1) {
		    // TODO Auto-generated catch block
		    e1.printStackTrace();
		}
	    System.out.println("create document KO: " + e.getMessage());
	    // assertTrue(false);
	}

    }

    @Test
    public void testCreateDocument6() {

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

	    Map<String, String> info = new HashMap<String, String>();
	    info.put("COD_ENTE", cod_ente);
	    info.put("COD_AOO", cod_aoo);
	    info.put("DOCNAME", "test.txt");
	    info.put("TYPE_ID", "documento");

	    docid_6 = getBL().createDocument(getToken(), info, fileInputStream);
	    System.out.println("create document ok: " + docid_6);

	}
	catch (Exception e) {

	    if (fileInputStream != null)
		try {
		    fileInputStream.close();
		}
		catch (IOException e1) {
		    // TODO Auto-generated catch block
		    e1.printStackTrace();
		}
	    System.out.println("create document KO: " + e.getMessage());
	    // assertTrue(false);
	}

    }

    @Test
    public void testCreateDocumentArchiveType() {

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

	    Map<String, String> info = new HashMap<String, String>();
	    info.put("COD_ENTE", cod_ente);
	    info.put("COD_AOO", cod_aoo);
	    info.put("DOCNAME", "test.txt");
	    info.put("TYPE_ID", "documento");
	    info.put("ARCHIVE_TYPE", "ARCHIVE");

	    String docnum1 = getBL().createDocument(getToken(), info, fileInputStream);
	    System.out.println("create document ok: " + docnum1);

	    info.put("ARCHIVE_TYPE", "URL");
	    info.put("DOC_URL", "http://");

	    String docnum2 = getBL().createDocument(getToken(), info, null);
	    System.out.println("create document ok: " + docnum2);

	}
	catch (Exception e) {

	    if (fileInputStream != null)
		try {
		    fileInputStream.close();
		}
		catch (IOException e1) {
		    // TODO Auto-generated catch block
		    e1.printStackTrace();
		}
	    System.out.println("create document KO: " + e.getMessage());
	    // assertTrue(false);
	}

    }

    @Test
    public void testFolderUser() {

	String userid = "luca.biasin";
	String password = "luca.biasin";
	String cod_ente = "ENTETEST";
	String cod_aoo = "AOOTEST";

	String fid1 = null;
	String fid2 = null;
	String fid3 = null;
	String fid4 = null;

	Map<String, String> profile = new HashMap<String, String>();
	try {

	    String folderId = null;
	    String token = getBL().login(cod_ente, userid, password);

	    Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
	    searchCriteria.put("COD_ENTE", Arrays.asList(cod_ente));
	    searchCriteria.put("COD_AOO", Arrays.asList(cod_aoo));

	    List<ISearchItem> results = getBL().searchFolders(token, searchCriteria, -1, null);
	    if (results != null) {
		for (ISearchItem si : results) {
			for (KeyValuePair kvp : si.getMetadata()) {
			if (kvp.getKey().equalsIgnoreCase("FOLDER_ID")) {
			    folderId = kvp.getValue();
			    break;
			}
		    }
		}
	    }

	    profile.put("FOLDER_NAME", "folder pubblica 1");
	    profile.put("PARENT_FOLDER_ID", folderId);
	    profile.put("DES_FOLDER", "des folder pubblica 1");
	    profile.put("FOLDER_OWNER", "");
	    try {
		fid1 = getBL().createFolder(token, profile);
	    }
	    catch (DocerException e) {
		System.out.println("folder step 1 KO: " + e.getMessage());
	    }

	    profile.put("FOLDER_NAME", "folder pubblica 1.1");
	    profile.put("PARENT_FOLDER_ID", fid1);
	    profile.put("DES_FOLDER", "des folder pubblica 1.1");
	    profile.put("FOLDER_OWNER", "");
	    try {
		fid2 = getBL().createFolder(token, profile);
	    }
	    catch (DocerException e) {
		System.out.println("folder step 2 KO: " + e.getMessage());
	    }

	    // profile.put("FOLDER_NAME", "folder privata 1");
	    // profile.put("PARENT_FOLDER_ID", folderid);
	    // profile.put("DES_FOLDER", "des folder privata 1");
	    // profile.put("FOLDER_OWNER", "luca.biasin");
	    // try {
	    // fid3 = getBL().createFolder(getToken(), profile);
	    // }
	    // catch (DocerException e) {
	    // System.out.println("folder step 3 KO: " + e.getMessage());
	    // }
	    //
	    // profile.put("FOLDER_NAME", "folder privata 1.1");
	    // profile.put("PARENT_FOLDER_ID", fid3);
	    // profile.put("DES_FOLDER", "des folder privata 1.1");
	    // profile.put("FOLDER_OWNER", userAdmin);
	    // try {
	    // fid4 = getBL().createFolder(getToken(), profile);
	    // }
	    // catch (DocerException e) {
	    // System.out.println("folder step 4 KO: " + e.getMessage());
	    // }
	    //
	    // try {
	    // getBL().addToFolderDocuments(getToken(), fid1,
	    // Arrays.asList(docid_1));
	    // }
	    // catch (DocerException e) {
	    // System.out.println("folder step 5 KO: " + e.getMessage());
	    // }
	    // try {
	    // getBL().addToFolderDocuments(getToken(), fid2,
	    // Arrays.asList(docid_2));
	    // }
	    // catch (DocerException e) {
	    // System.out.println("folder step 6 KO: " + e.getMessage());
	    // }
	    // try {
	    // getBL().addToFolderDocuments(getToken(), fid3,
	    // Arrays.asList(docid_3));
	    // }
	    // catch (DocerException e) {
	    // System.out.println("folder step 7 KO: " + e.getMessage());
	    // }
	    // try {
	    // getBL().addToFolderDocuments(getToken(), fid4,
	    // Arrays.asList(docid_4));
	    // }
	    // catch (DocerException e) {
	    // System.out.println("folder step 8 KO: " + e.getMessage());
	    // }
	    //
	    // try {
	    // getBL().removeFromFolderDocuments(getToken(), fid1,
	    // Arrays.asList(docid_1));
	    // }
	    // catch (DocerException e) {
	    // System.out.println("folder step 9 KO: " + e.getMessage());
	    // }
	    // try {
	    // getBL().removeFromFolderDocuments(getToken(), fid2,
	    // Arrays.asList(docid_2));
	    // }
	    // catch (DocerException e) {
	    // System.out.println("folder step 10 KO: " + e.getMessage());
	    // }
	    // try {
	    // getBL().removeFromFolderDocuments(getToken(), fid3,
	    // Arrays.asList(docid_3));
	    // }
	    // catch (DocerException e) {
	    // System.out.println("folder step 11 KO: " + e.getMessage());
	    // }
	    // try {
	    // getBL().removeFromFolderDocuments(getToken(), fid4,
	    // Arrays.asList(docid_4));
	    // }
	    // catch (DocerException e) {
	    // System.out.println("folder step 12 KO: " + e.getMessage());
	    // }

	    System.out.println("foldering END");

	}
	catch (Exception e) {

	    System.out.println("Folder KO: " + e.getMessage());

	}
	finally {
	    List<String> rifs = Arrays.asList(fid4, fid3, fid2, fid1);
	    List<String> docs = Arrays.asList(docid_1, docid_2, docid_3, docid_4);

	    for (String key : rifs) {

		for (String doc : docs) {
		    try {
			getBL().removeFromFolderDocuments(getToken(), key, Arrays.asList(doc));
		    }
		    catch (DocerException e1) {
			System.out.println("removeFromFolderDocuments: " + e1.getMessage());
		    }
		}

		try {
		    getBL().deleteFolder(getToken(), key);
		}
		catch (DocerException e1) {
		    System.out.println("deleteFolder: " + e1.getMessage());
		}
	    }

	    System.out.println("folder ripristinate");
	}
    }

    @Test
    public void testAddToFolderDocument() {

	String userid = "admin";
	String password = "admin";
	String cod_ente = "ENTEPROVA";
	String cod_aoo = "TEST_FOLDERS";

	String fid1 = null;
	String fid2 = null;
	String fid3 = null;
	String fid4 = null;

	Map<String, String> profile = new HashMap<String, String>();
	try {

	    String folderId = null;
	    String token = getBL().login(cod_ente, userid, password);

	    try {
		getBL().addToFolderDocuments(token, "15268", Arrays.asList("22259"));
	    }
	    catch (DocerException e) {
		System.out.println("folder step 5 KO: " + e.getMessage());
	    }

	    System.out.println("foldering END");

	}
	catch (Exception e) {

	    System.out.println("Folder KO: " + e.getMessage());

	}
	finally {

	}
    }

    @Test
    public void testAddToFolderDocumentUser() {

	String userid = "luca.biasin";
	String password = "luca.biasin";
	String cod_ente = "ENTETEST";
	String cod_aoo = "AOOTEST";

	String fid1 = null;
	String fid2 = null;
	String fid3 = null;
	String fid4 = null;

	Map<String, String> profile = new HashMap<String, String>();
	try {

	    String folderId = null;
	    String token = getBL().login(cod_ente, userid, password);

	    Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
	    searchCriteria.put("COD_ENTE", Arrays.asList(cod_ente));
	    searchCriteria.put("COD_AOO", Arrays.asList(cod_aoo));
	    searchCriteria.put("PARENT_FOLDER_ID", Arrays.asList(""));

	    List<ISearchItem> results = getBL().searchFolders(token, searchCriteria, -1, null);
	    if (results != null) {
		if (results.size() != 1) {
		    throw new Exception(results.size() + " folder root trovate");
		}
		for (ISearchItem si : results) {
			for (KeyValuePair kvp : si.getMetadata()) {
			if (kvp.getKey().equalsIgnoreCase("FOLDER_ID")) {
			    folderId = kvp.getValue();
			    break;
			}
		    }
		}
	    }

	    searchCriteria.clear();
	    searchCriteria.put("COD_ENTE", Arrays.asList(cod_ente));
	    searchCriteria.put("COD_AOO", Arrays.asList(cod_aoo));
	    searchCriteria.put("FOLDER_NAME", Arrays.asList("folder pubblica 1"));
	    searchCriteria.put("FOLDER_OWNER", Arrays.asList(""));

	    results = getBL().searchFolders(token, searchCriteria, -1, null);

	    if (results == null || results.size() == 0) {

		profile.put("FOLDER_NAME", "folder pubblica 1");
		profile.put("PARENT_FOLDER_ID", folderId);
		profile.put("DES_FOLDER", "des folder pubblica 1");
		profile.put("FOLDER_OWNER", "");
		try {
		    fid1 = getBL().createFolder(token, profile);
		}
		catch (DocerException e) {
		    System.out.println("folder step 1 KO: " + e.getMessage());
		}

	    }
	    else if (results.size() > 1) {
		throw new Exception(results.size() + " folder \"folder pubblica 1\" trovate");
	    }
	    else if (results.size() == 1) {
		for (ISearchItem si : results) {
			for (KeyValuePair kvp : si.getMetadata()) {
			if (kvp.getKey().equalsIgnoreCase("FOLDER_ID")) {
			    fid1 = kvp.getValue();
			    break;
			}
		    }
		}
	    }

	    // profile.put("FOLDER_NAME", "folder privata 1");
	    // profile.put("PARENT_FOLDER_ID", folderid);
	    // profile.put("DES_FOLDER", "des folder privata 1");
	    // profile.put("FOLDER_OWNER", "luca.biasin");
	    // try {
	    // fid3 = getBL().createFolder(getToken(), profile);
	    // }
	    // catch (DocerException e) {
	    // System.out.println("folder step 3 KO: " + e.getMessage());
	    // }
	    //
	    // profile.put("FOLDER_NAME", "folder privata 1.1");
	    // profile.put("PARENT_FOLDER_ID", fid3);
	    // profile.put("DES_FOLDER", "des folder privata 1.1");
	    // profile.put("FOLDER_OWNER", userAdmin);
	    // try {
	    // fid4 = getBL().createFolder(getToken(), profile);
	    // }
	    // catch (DocerException e) {
	    // System.out.println("folder step 4 KO: " + e.getMessage());
	    // }
	    //
	    try {
		getBL().addToFolderDocuments(token, fid1, Arrays.asList("110"));
	    }
	    catch (DocerException e) {
		System.out.println("folder step 5 KO: " + e.getMessage());
	    }
	    // try {
	    // getBL().addToFolderDocuments(getToken(), fid2,
	    // Arrays.asList(docid_2));
	    // }
	    // catch (DocerException e) {
	    // System.out.println("folder step 6 KO: " + e.getMessage());
	    // }
	    // try {
	    // getBL().addToFolderDocuments(getToken(), fid3,
	    // Arrays.asList(docid_3));
	    // }
	    // catch (DocerException e) {
	    // System.out.println("folder step 7 KO: " + e.getMessage());
	    // }
	    // try {
	    // getBL().addToFolderDocuments(getToken(), fid4,
	    // Arrays.asList(docid_4));
	    // }
	    // catch (DocerException e) {
	    // System.out.println("folder step 8 KO: " + e.getMessage());
	    // }
	    //
	    // try {
	    // getBL().removeFromFolderDocuments(getToken(), fid1,
	    // Arrays.asList(docid_1));
	    // }
	    // catch (DocerException e) {
	    // System.out.println("folder step 9 KO: " + e.getMessage());
	    // }
	    // try {
	    // getBL().removeFromFolderDocuments(getToken(), fid2,
	    // Arrays.asList(docid_2));
	    // }
	    // catch (DocerException e) {
	    // System.out.println("folder step 10 KO: " + e.getMessage());
	    // }
	    // try {
	    // getBL().removeFromFolderDocuments(getToken(), fid3,
	    // Arrays.asList(docid_3));
	    // }
	    // catch (DocerException e) {
	    // System.out.println("folder step 11 KO: " + e.getMessage());
	    // }
	    // try {
	    // getBL().removeFromFolderDocuments(getToken(), fid4,
	    // Arrays.asList(docid_4));
	    // }
	    // catch (DocerException e) {
	    // System.out.println("folder step 12 KO: " + e.getMessage());
	    // }

	    System.out.println("foldering END");

	}
	catch (Exception e) {

	    System.out.println("Folder KO: " + e.getMessage());

	}
	finally {
	    List<String> rifs = Arrays.asList(fid4, fid3, fid2, fid1);
	    List<String> docs = Arrays.asList(docid_1, docid_2, docid_3, docid_4);

	    for (String key : rifs) {

		for (String doc : docs) {
		    try {
			getBL().removeFromFolderDocuments(getToken(), key, Arrays.asList(doc));
		    }
		    catch (DocerException e1) {
			System.out.println("removeFromFolderDocuments: " + e1.getMessage());
		    }
		}

		try {
		    getBL().deleteFolder(getToken(), key);
		}
		catch (DocerException e1) {
		    System.out.println("deleteFolder: " + e1.getMessage());
		}
	    }

	    System.out.println("folder ripristinate");
	}
    }

    @Test
    public void testFolder() {

	String fid1 = "";
	String fid2 = "";
	String fid3 = "";
	String fid4 = "";

	String folderid = "61648";

	Map<String, String> profile = new HashMap<String, String>();
	try {

	    profile.put("FOLDER_NAME", "folder pubblica 1");
	    profile.put("PARENT_FOLDER_ID", folderid);
	    profile.put("DES_FOLDER", "des folder pubblica 1");
	    profile.put("FOLDER_OWNER", "");
	    try {
		fid1 = getBL().createFolder(getToken(), profile);
	    }
	    catch (DocerException e) {
		System.out.println("folder step 1 KO: " + e.getMessage());
	    }
	    profile.put("FOLDER_NAME", "folder pubblica 1.1");
	    profile.put("PARENT_FOLDER_ID", fid1);
	    profile.put("DES_FOLDER", "des folder pubblica 1.1");
	    profile.put("FOLDER_OWNER", "");
	    try {
		fid2 = getBL().createFolder(getToken(), profile);
	    }
	    catch (DocerException e) {
		System.out.println("folder step 2 KO: " + e.getMessage());
	    }

	    profile.put("FOLDER_NAME", "folder privata 1");
	    profile.put("PARENT_FOLDER_ID", folderid);
	    profile.put("DES_FOLDER", "des folder privata 1");
	    profile.put("FOLDER_OWNER", userAdmin);
	    try {
		fid3 = getBL().createFolder(getToken(), profile);
	    }
	    catch (DocerException e) {
		System.out.println("folder step 3 KO: " + e.getMessage());
	    }

	    profile.put("FOLDER_NAME", "folder privata 1.1");
	    profile.put("PARENT_FOLDER_ID", fid3);
	    profile.put("DES_FOLDER", "des folder privata 1.1");
	    profile.put("FOLDER_OWNER", userAdmin);
	    try {
		fid4 = getBL().createFolder(getToken(), profile);
	    }
	    catch (DocerException e) {
		System.out.println("folder step 4 KO: " + e.getMessage());
	    }

	    try {
		getBL().addToFolderDocuments(getToken(), fid1, Arrays.asList(docid_1));
	    }
	    catch (DocerException e) {
		System.out.println("folder step 5 KO: " + e.getMessage());
	    }
	    try {
		getBL().addToFolderDocuments(getToken(), fid2, Arrays.asList(docid_2));
	    }
	    catch (DocerException e) {
		System.out.println("folder step 6 KO: " + e.getMessage());
	    }
	    try {
		getBL().addToFolderDocuments(getToken(), fid3, Arrays.asList(docid_3));
	    }
	    catch (DocerException e) {
		System.out.println("folder step 7 KO: " + e.getMessage());
	    }
	    try {
		getBL().addToFolderDocuments(getToken(), fid4, Arrays.asList(docid_4));
	    }
	    catch (DocerException e) {
		System.out.println("folder step 8 KO: " + e.getMessage());
	    }

	    try {
		getBL().removeFromFolderDocuments(getToken(), fid1, Arrays.asList(docid_1));
	    }
	    catch (DocerException e) {
		System.out.println("folder step 9 KO: " + e.getMessage());
	    }
	    try {
		getBL().removeFromFolderDocuments(getToken(), fid2, Arrays.asList(docid_2));
	    }
	    catch (DocerException e) {
		System.out.println("folder step 10 KO: " + e.getMessage());
	    }
	    try {
		getBL().removeFromFolderDocuments(getToken(), fid3, Arrays.asList(docid_3));
	    }
	    catch (DocerException e) {
		System.out.println("folder step 11 KO: " + e.getMessage());
	    }
	    try {
		getBL().removeFromFolderDocuments(getToken(), fid4, Arrays.asList(docid_4));
	    }
	    catch (DocerException e) {
		System.out.println("folder step 12 KO: " + e.getMessage());
	    }

	    System.out.println("foldering END");

	}
	catch (Exception e) {

	    System.out.println("Folder KO: " + e.getMessage());

	}
	finally {
	    List<String> rifs = Arrays.asList(fid4, fid3, fid2, fid1);
	    List<String> docs = Arrays.asList(docid_1, docid_2, docid_3, docid_4);

	    for (String key : rifs) {

		for (String doc : docs) {
		    try {
			getBL().removeFromFolderDocuments(getToken(), key, Arrays.asList(doc));
		    }
		    catch (DocerException e1) {
			System.out.println("removeFromFolderDocuments: " + e1.getMessage());
		    }
		}

		try {
		    getBL().deleteFolder(getToken(), key);
		}
		catch (DocerException e1) {
		    System.out.println("deleteFolder: " + e1.getMessage());
		}
	    }

	    System.out.println("folder ripristinate");
	}
    }

    @Test
    public void testSearchAndGetFolders() {

	try {

	    Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();

	    // searchCriteria.put("TYPE_ID", Arrays.asList("DOCUMENTO"));
	    // searchCriteria.put("COD_ENTE", Arrays.asList(cod_ente));
	    // searchCriteria.put("COD_AOO", Arrays.asList(cod_aoo));

	    try {
		List<ISearchItem> res = getBL().searchFolders(getToken(), searchCriteria, -1, null);

		System.out.println("trovati " + res.size() + " risultati");

		for (ISearchItem isi : res) {

		    String folderId = "";
		    System.out.println("----------");
		    for (KeyValuePair kvp : isi.getMetadata()) {

			if (kvp.getKey().equals("FOLDER_ID")) {
			    folderId = kvp.getValue();
			}
			System.out.println(kvp.getKey() + "=" + kvp.getValue());
		    }

		}

	    }
	    catch (DocerException e) {
		System.out.println("search folder KO: " + e.getMessage());
	    }

	}
	catch (Exception e) {

	    System.out.println("Folder KO: " + e.getMessage());

	}
	finally {
	    try {
		getBL().logout(getToken());
	    }
	    catch (DocerException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}
    }

    @Test
    public void testRelated() {

	try {

	    List<String> list = Arrays.asList("73923");

	    // Map<String, String> metadata = new HashMap<String, String>();
	    // metadata.put("TIPO_COMPONENTE", "");
	    // for (String docid : list) {
	    //
	    // getBL().updateProfileDocument(getToken(), docid, metadata);
	    // }

	    getBL().addRelated(getToken(), docid_1, list);

	    List<String> res = getBL().getRelatedDocuments(getToken(), docid_1);
	    if (res.size() != 2 || !res.contains(docid_2) || !res.contains(docid_3)) {
		System.out.println("related step 1 KO");
		return;
	    }
	    res = getBL().getRelatedDocuments(getToken(), docid_2);
	    if (res.size() != 2 || !res.contains(docid_1) || !res.contains(docid_3)) {
		System.out.println("related step 2 KO");
		return;
	    }
	    res = getBL().getRelatedDocuments(getToken(), docid_3);
	    if (res.size() != 2 || !res.contains(docid_1) || !res.contains(docid_2)) {
		System.out.println("related step 3 KO");
		return;
	    }

	    return;

	    // getBL().removeRelated(getToken(), docid_1,
	    // Arrays.asList(docid_2));
	    // res = getBL().getRelatedDocuments(getToken(), docid_1);
	    // if (res.size() != 1 || !res.contains(docid_3)) {
	    // System.out.println("related step 4 KO");
	    // return;
	    // }
	    //
	    // getBL().addRelated(getToken(), docid_1, Arrays.asList(docid_2,
	    // docid_3));
	    // getBL().addRelated(getToken(), docid_4, Arrays.asList(docid_5,
	    // docid_6));
	    // getBL().addRelated(getToken(), docid_3, Arrays.asList(docid_2,
	    // docid_3));
	    // getBL().addRelated(getToken(), docid_5, Arrays.asList(docid_5,
	    // docid_6));
	    // // completo chain
	    // getBL().addRelated(getToken(), docid_2, Arrays.asList(docid_5,
	    // docid_6));
	    //
	    // res = getBL().getRelatedDocuments(getToken(), docid_1);
	    // if (res.size() != 5 || !res.contains(docid_2) ||
	    // !res.contains(docid_3) || !res.contains(docid_4) ||
	    // !res.contains(docid_5) || !res.contains(docid_6)) {
	    // System.out.println("related step 5 KO");
	    // return;
	    // }
	    // res = getBL().getRelatedDocuments(getToken(), docid_2);
	    // if (res.size() != 5 || !res.contains(docid_1) ||
	    // !res.contains(docid_3) || !res.contains(docid_4) ||
	    // !res.contains(docid_5) || !res.contains(docid_6)) {
	    // System.out.println("related step 6 KO");
	    // return;
	    // }
	    // res = getBL().getRelatedDocuments(getToken(), docid_3);
	    // if (res.size() != 5 || !res.contains(docid_1) ||
	    // !res.contains(docid_2) || !res.contains(docid_4) ||
	    // !res.contains(docid_5) || !res.contains(docid_6)) {
	    // System.out.println("related step 7 KO");
	    // return;
	    // }
	    // res = getBL().getRelatedDocuments(getToken(), docid_4);
	    // if (res.size() != 5 || !res.contains(docid_1) ||
	    // !res.contains(docid_2) || !res.contains(docid_3) ||
	    // !res.contains(docid_5) || !res.contains(docid_6)) {
	    // System.out.println("related step 8 KO");
	    // return;
	    // }
	    // res = getBL().getRelatedDocuments(getToken(), docid_5);
	    // if (res.size() != 5 || !res.contains(docid_1) ||
	    // !res.contains(docid_2) || !res.contains(docid_3) ||
	    // !res.contains(docid_4) || !res.contains(docid_6)) {
	    // System.out.println("related step 9 KO");
	    // return;
	    // }
	    // res = getBL().getRelatedDocuments(getToken(), docid_6);
	    // if (res.size() != 5 || !res.contains(docid_1) ||
	    // !res.contains(docid_2) || !res.contains(docid_3) ||
	    // !res.contains(docid_4) || !res.contains(docid_5)) {
	    // System.out.println("related step 9 KO");
	    // return;
	    // }
	    //
	    // // clean
	    // getBL().removeRelated(getToken(), docid_3, Arrays.asList(docid_4,
	    // docid_5));
	    // res = getBL().getRelatedDocuments(getToken(), docid_3);
	    // if (res.size() != 3 || !res.contains(docid_1) ||
	    // !res.contains(docid_2) || !res.contains(docid_6)) {
	    // System.out.println("related step 10 KO");
	    // return;
	    // }
	    //
	    // getBL().removeRelated(getToken(), docid_1, Arrays.asList(docid_2,
	    // docid_3, docid_6));
	    // res = getBL().getRelatedDocuments(getToken(), docid_1);
	    // if (res.size() != 0) {
	    // System.out.println("related step 11 KO");
	    // return;
	    // }

	    // System.out.println("related ok");

	}
	catch (Exception e) {

	    System.out.println("add related KO: " + e.getMessage());
	    // assertTrue(false);
	}
	finally {

	    // List<String> rifs = Arrays.asList(docid_1, docid_2, docid_3,
	    // docid_4, docid_5, docid_6);
	    //
	    // for (String key1 : rifs) {
	    // for (String key2 : rifs) {
	    // try {
	    // getBL().removeRelated(getToken(), key1, Arrays.asList(key2));
	    // }
	    // catch (DocerException e1) {
	    // System.out.println("removeRelated: " + e1.getMessage());
	    // }
	    // }
	    // }
	    // System.out.println("eliminazione delle correlazioni ok");
	}
    }

    @Test
    public void testAdvancedVersion() {

	try {

	    List<String> list = Arrays.asList(docid_1, docid_2, docid_3, docid_4, docid_5, docid_6);

	    Map<String, String> metadata = new HashMap<String, String>();
	    metadata.put("TIPO_COMPONENTE", "PRINCIPALE");
	    for (String docid : list) {

		getBL().updateProfileDocument(getToken(), docid, metadata);
	    }

	    getBL().addNewAdvancedVersion(getToken(), docid_1, docid_2);
	    List<String> res = getBL().getAdvancedVersions(getToken(), docid_1);
	    if (res.size() != 2 || !res.contains(docid_1) || !res.contains(docid_2)) {
		System.out.println("advanced versions step 1 KO");
		return;
	    }
	    res = getBL().getAdvancedVersions(getToken(), docid_2);
	    if (res.size() != 2 || !res.contains(docid_1) || !res.contains(docid_2)) {
		System.out.println("advanced versions step 2 KO");
		return;
	    }
	    getBL().addNewAdvancedVersion(getToken(), docid_2, docid_3);
	    res = getBL().getAdvancedVersions(getToken(), docid_1);
	    if (res.size() != 3 || !res.contains(docid_1) || !res.contains(docid_2) || !res.contains(docid_3)) {
		System.out.println("advanced versions step 3 KO");
		return;
	    }
	    res = getBL().getAdvancedVersions(getToken(), docid_2);
	    if (res.size() != 3 || !res.contains(docid_1) || !res.contains(docid_2) || !res.contains(docid_3)) {
		System.out.println("advanced versions step 4 KO");
		return;
	    }
	    res = getBL().getAdvancedVersions(getToken(), docid_3);
	    if (res.size() != 3 || !res.contains(docid_1) || !res.contains(docid_2) || !res.contains(docid_3)) {
		System.out.println("advanced versions step 5 KO");
		return;
	    }

	    getBL().addNewAdvancedVersion(getToken(), docid_4, docid_5);
	    getBL().addNewAdvancedVersion(getToken(), docid_5, docid_6);
	    getBL().addNewAdvancedVersion(getToken(), docid_1, docid_6);

	    res = getBL().getAdvancedVersions(getToken(), docid_1);
	    if (res.size() != 6 || !res.contains(docid_1) || !res.contains(docid_2) || !res.contains(docid_3) || !res.contains(docid_4) || !res.contains(docid_5) || !res.contains(docid_6)) {
		System.out.println("advanced versions  step 6 KO");
		return;
	    }
	    res = getBL().getAdvancedVersions(getToken(), docid_2);
	    if (res.size() != 6 || !res.contains(docid_1) || !res.contains(docid_2) || !res.contains(docid_3) || !res.contains(docid_4) || !res.contains(docid_5) || !res.contains(docid_6)) {
		System.out.println("advanced versions  step 7 KO");
		return;
	    }
	    res = getBL().getAdvancedVersions(getToken(), docid_3);
	    if (res.size() != 6 || !res.contains(docid_1) || !res.contains(docid_2) || !res.contains(docid_3) || !res.contains(docid_4) || !res.contains(docid_5) || !res.contains(docid_6)) {
		System.out.println("advanced versions  step 8 KO");
		return;
	    }
	    res = getBL().getAdvancedVersions(getToken(), docid_4);
	    if (res.size() != 6 || !res.contains(docid_1) || !res.contains(docid_2) || !res.contains(docid_3) || !res.contains(docid_4) || !res.contains(docid_5) || !res.contains(docid_6)) {
		System.out.println("advanced versions  step 9 KO");
		return;
	    }
	    res = getBL().getAdvancedVersions(getToken(), docid_5);
	    if (res.size() != 6 || !res.contains(docid_1) || !res.contains(docid_2) || !res.contains(docid_3) || !res.contains(docid_4) || !res.contains(docid_5) || !res.contains(docid_6)) {
		System.out.println("advanced versions  step 10 KO");
		return;
	    }
	    res = getBL().getAdvancedVersions(getToken(), docid_6);
	    if (res.size() != 6 || !res.contains(docid_1) || !res.contains(docid_2) || !res.contains(docid_3) || !res.contains(docid_4) || !res.contains(docid_5) || !res.contains(docid_6)) {
		System.out.println("advanced versions  step 11 KO");
		return;
	    }

	    System.out.println("advanced versions ok");

	}
	catch (Exception e) {

	    System.out.println("advanced versions  KO: " + e.getMessage());
	    // assertTrue(false);
	}

    }

    @Test
    public void testAdvancedVersion2() {

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

	    token = getBL().login("", "admin", "admin");

	    Map<String, String> info = new HashMap<String, String>();
	    info.put("COD_ENTE", "EMR");
	    info.put("COD_AOO", "AOO_EMR");
	    info.put("TYPE_ID", "documento");
	    info.put("DOCNAME", "doc1.txt");
	    info.put("TIPO_COMPONENTE", "PRINCIPALE");
	    info.put("ABSTRACT", "Lettera");
	    info.put("ARCHIVE_TYPE", "ARCHIVE");

	    String docnum1 = getBL().createDocument(getToken(), info, fileInputStream);
	    System.out.println("creato " + docnum1);

	    fileInputStream.close();

	    try {
		fileInputStream = new FileInputStream(file);
	    }
	    catch (FileNotFoundException e1) {

		System.out.println(e1);
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

	    String docnum2 = getBL().createDocument(getToken(), info, fileInputStream);
	    System.out.println("creato " + docnum2);

	    fileInputStream.close();

	    getBL().addNewAdvancedVersion(getToken(), docnum1, docnum2);

	    Map<String, String> proto = new HashMap<String, String>();
	    proto.put("COD_ENTE", "EMR");
	    proto.put("COD_AOO", "AOO_EMR");
	    proto.put("NUM_PG", "1");
	    proto.put("REGISTRO_PG", "REG PG");
	    proto.put("DATA_PG", "2012-06-12T10:22:12.000+02:00");
	    proto.put("ANNO_PG", "2012");
	    proto.put("OGGETTO_PG", "oggetto protocollo");
	    proto.put("TIPO_PROTOCOLLAZIONE", "E");
	    proto.put("TIPO_FIRMA", "FD");

	    getBL().protocollaDocumento(getToken(), docnum1, proto);

	    getBL().addNewAdvancedVersion(getToken(), docnum1, docnum2);

	    info.clear();
	    info.put("COD_ENTE", "EMR");
	    info.put("COD_AOO", "AOO_EMR");
	    info.put("TYPE_ID", "documento");
	    info.put("DOCNAME", "doc2.txt");
	    info.put("TIPO_COMPONENTE", "PRINCIPALE");

	    try {
		fileInputStream = new FileInputStream(file);
	    }
	    catch (FileNotFoundException e1) {

		System.out.println(e1);
		return;
	    }

	    String docnum3 = getBL().createDocument(getToken(), info, fileInputStream);
	    System.out.println("creato " + docnum3);

	    fileInputStream.close();

	    getBL().addNewAdvancedVersion(getToken(), docnum2, docnum3);

	}

	catch (Exception e) {

	    System.out.println(e.getMessage());

	}
	finally {
	    try {
		getBL().logout(token);
	    }
	    catch (DocerException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}

    }

    @Test
    public void testRiferimenti() {

	try {

	    // ADD
	    getBL().addRiferimentiDocuments(getToken(), docid_1, Arrays.asList(docid_2, docid_3));

	    List<String> res = getBL().getRiferimentiDocuments(getToken(), docid_1);
	    if (res.size() != 2 || !res.contains(docid_2) || !res.contains(docid_3)) {
		System.out.println("riferimenti step 1.1 KO");
		return;
	    }

	    res = getBL().getRiferimentiDocuments(getToken(), docid_2);
	    if (res.size() != 1 || !res.contains(docid_1)) {
		System.out.println("riferimenti step 1.2 KO");
		return;
	    }

	    res = getBL().getRiferimentiDocuments(getToken(), docid_3);
	    if (res.size() != 1 || !res.contains(docid_1)) {
		System.out.println("riferimenti step 1.3 KO");
		return;
	    }

	    // ADD
	    getBL().addRiferimentiDocuments(getToken(), docid_3, Arrays.asList(docid_4));

	    res = getBL().getRiferimentiDocuments(getToken(), docid_3);
	    if (res.size() != 2 || !res.contains(docid_1) || !res.contains(docid_4)) {
		System.out.println("riferimenti step 2.0 KO");
		return;
	    }

	    res = getBL().getRiferimentiDocuments(getToken(), docid_4);
	    if (res.size() != 1 || !res.contains(docid_3)) {
		System.out.println("riferimenti step 2.1 KO");
		return;
	    }

	    res = getBL().getRiferimentiDocuments(getToken(), docid_1);
	    if (res.size() != 2 || !res.contains(docid_2) || !res.contains(docid_3)) {
		System.out.println("riferimenti step 2.3 KO");
		return;
	    }

	    res = getBL().getRiferimentiDocuments(getToken(), docid_2);
	    if (res.size() != 1 || !res.contains(docid_1)) {
		System.out.println("riferimenti step 2.4 KO");
		return;
	    }

	    // ADD
	    getBL().addRiferimentiDocuments(getToken(), docid_2, Arrays.asList(docid_4, docid_5, docid_6));

	    res = getBL().getRiferimentiDocuments(getToken(), docid_1);
	    if (res.size() != 2 || !res.contains(docid_2) || !res.contains(docid_3)) {
		System.out.println("riferimenti step 3.0 KO");
		return;
	    }

	    res = getBL().getRiferimentiDocuments(getToken(), docid_2);
	    if (res.size() != 4 || !res.contains(docid_1) || !res.contains(docid_4) || !res.contains(docid_5) || !res.contains(docid_6)) {
		System.out.println("riferimenti step 3.1 KO");
		return;
	    }

	    res = getBL().getRiferimentiDocuments(getToken(), docid_3);
	    if (res.size() != 2 || !res.contains(docid_1) || !res.contains(docid_4)) {
		System.out.println("riferimenti step 3.2 KO");
		return;
	    }

	    res = getBL().getRiferimentiDocuments(getToken(), docid_4);
	    if (res.size() != 2 || !res.contains(docid_3) || !res.contains(docid_2)) {
		System.out.println("riferimenti step 3.3 KO");
		return;
	    }

	    res = getBL().getRiferimentiDocuments(getToken(), docid_5);
	    if (res.size() != 1 || !res.contains(docid_2)) {
		System.out.println("riferimenti step 3.4 KO");
		return;
	    }

	    res = getBL().getRiferimentiDocuments(getToken(), docid_6);
	    if (res.size() != 1 || !res.contains(docid_2)) {
		System.out.println("riferimenti step 3.5 KO");
		return;
	    }

	    System.out.println("rifeirmenti ok");

	}
	catch (Exception e) {

	    System.out.println("riferimenti  KO: " + e.getMessage());
	    // assertTrue(false);
	}
	finally {
	    List<String> rifs = Arrays.asList(docid_1, docid_2, docid_3, docid_4, docid_5, docid_6);

	    for (String key1 : rifs) {
		for (String key2 : rifs) {
		    try {
			getBL().removeRiferimentiDocuments(getToken(), key1, Arrays.asList(key2));
		    }
		    catch (DocerException e1) {
			System.out.println("delete: " + e1.getMessage());
		    }
		}
	    }

	    System.out.println("eliminazione riferimenti effettuatua");
	}

    }

    @Test
    public void testRiferimenti2() {

	try {

	    // ADD
	    getBL().addRiferimentiDocuments(getToken(), docid_1, Arrays.asList(docid_2, docid_3));

	    List<String> res = getBL().getRiferimentiDocuments(getToken(), docid_1);
	    if (res.size() != 2 || !res.contains(docid_2) || !res.contains(docid_3)) {
		System.out.println("riferimenti step 1.1 KO");
		return;
	    }

	    // res = getBL().getRiferimentiDocuments(getToken(), docid_2);
	    // if (res.size() != 1 || !res.contains(docid_1)) {
	    // System.out.println("riferimenti step 1.2 KO");
	    // return;
	    // }
	    //
	    // res = getBL().getRiferimentiDocuments(getToken(), docid_3);
	    // if (res.size() != 1 || !res.contains(docid_1)) {
	    // System.out.println("riferimenti step 1.3 KO");
	    // return;
	    // }
	    //
	    // // ADD
	    // getBL().addRiferimentiDocuments(getToken(), docid_3,
	    // Arrays.asList(docid_4));
	    //
	    // res = getBL().getRiferimentiDocuments(getToken(), docid_3);
	    // if (res.size() != 2 || !res.contains(docid_1) ||
	    // !res.contains(docid_4)) {
	    // System.out.println("riferimenti step 2.0 KO");
	    // return;
	    // }
	    //
	    // res = getBL().getRiferimentiDocuments(getToken(), docid_4);
	    // if (res.size() != 1 || !res.contains(docid_3)) {
	    // System.out.println("riferimenti step 2.1 KO");
	    // return;
	    // }
	    //
	    // res = getBL().getRiferimentiDocuments(getToken(), docid_1);
	    // if (res.size() != 2 || !res.contains(docid_2) ||
	    // !res.contains(docid_3)) {
	    // System.out.println("riferimenti step 2.3 KO");
	    // return;
	    // }
	    //
	    // res = getBL().getRiferimentiDocuments(getToken(), docid_2);
	    // if (res.size() != 1 || !res.contains(docid_1)) {
	    // System.out.println("riferimenti step 2.4 KO");
	    // return;
	    // }
	    //
	    // // ADD
	    // getBL().addRiferimentiDocuments(getToken(), docid_2,
	    // Arrays.asList(docid_4, docid_5, docid_6));
	    //
	    // res = getBL().getRiferimentiDocuments(getToken(), docid_1);
	    // if (res.size() != 2 || !res.contains(docid_2) ||
	    // !res.contains(docid_3)) {
	    // System.out.println("riferimenti step 3.0 KO");
	    // return;
	    // }
	    //
	    // res = getBL().getRiferimentiDocuments(getToken(), docid_2);
	    // if (res.size() != 4 || !res.contains(docid_1) ||
	    // !res.contains(docid_4) || !res.contains(docid_5) ||
	    // !res.contains(docid_6)) {
	    // System.out.println("riferimenti step 3.1 KO");
	    // return;
	    // }
	    //
	    // res = getBL().getRiferimentiDocuments(getToken(), docid_3);
	    // if (res.size() != 2 || !res.contains(docid_1) ||
	    // !res.contains(docid_4)) {
	    // System.out.println("riferimenti step 3.2 KO");
	    // return;
	    // }
	    //
	    // res = getBL().getRiferimentiDocuments(getToken(), docid_4);
	    // if (res.size() != 2 || !res.contains(docid_3) ||
	    // !res.contains(docid_2)) {
	    // System.out.println("riferimenti step 3.3 KO");
	    // return;
	    // }
	    //
	    // res = getBL().getRiferimentiDocuments(getToken(), docid_5);
	    // if (res.size() != 1 || !res.contains(docid_2)) {
	    // System.out.println("riferimenti step 3.4 KO");
	    // return;
	    // }
	    //
	    // res = getBL().getRiferimentiDocuments(getToken(), docid_6);
	    // if (res.size() != 1 || !res.contains(docid_2)) {
	    // System.out.println("riferimenti step 3.5 KO");
	    // return;
	    // }
	    //
	    // System.out.println("rifeirmenti ok");

	}
	catch (Exception e) {

	    System.out.println("riferimenti  KO: " + e.getMessage());
	    // assertTrue(false);
	}
	finally {
	    List<String> rifs = Arrays.asList(docid_1, docid_2, docid_3, docid_4, docid_5, docid_6);

	    for (String key1 : rifs) {
		for (String key2 : rifs) {
		    try {
			getBL().removeRiferimentiDocuments(getToken(), key1, Arrays.asList(key2));
		    }
		    catch (DocerException e1) {
			System.out.println("delete: " + e1.getMessage());
		    }
		}
	    }

	    System.out.println("eliminazione riferimenti effettuatua");
	}

    }

    @Test
    public void testCreateVersion2() {

	String filePath = "C:\\test2.txt";

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
	    getBL().addNewVersion(getToken(), docid_1, fileInputStream);
	    System.out.println("create version2 ok");
	    // assertTrue(true);

	}
	catch (Exception e) {

	    System.out.println("create version2 KO: " + e.getMessage());

	    if (fileInputStream != null)
		try {
		    fileInputStream.close();
		}
		catch (IOException e1) {
		    // TODO Auto-generated catch block
		    e1.printStackTrace();
		}
	}

    }

    @Test
    public void testCreateVersion3() {

	String filePath = "C:\\test3.txt";

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
	    getBL().addNewVersion(getToken(), docid_1, fileInputStream);
	    System.out.println("create version3 ok");
	    // assertTrue(true);

	}
	catch (Exception e) {

	    if (fileInputStream != null)
		try {
		    fileInputStream.close();
		}
		catch (IOException e1) {
		    // TODO Auto-generated catch block
		    e1.printStackTrace();
		}
	    System.out.println("create version3 KO: " + e.getMessage());
	    // assertTrue(false);
	}

    }

    
    @Test
    public void testGetVersions2() {

	try {
	    List<String> versions = getBL().getVersions(getToken(), "13864");
	    for (String v : versions) {
		System.out.println("versione: " + v);
	    }

	}
	catch (Exception e) {

	    System.out.println(e.getMessage());
	    // assertTrue(false);
	}

    }
    
    @Test
    public void testGetVersions() {

	try {
	    List<String> versions = getBL().getVersions(getToken(), docid_1);
	    for (String v : versions) {
		System.out.println("versione: " + v);
	    }

	}
	catch (Exception e) {

	    System.out.println(e.getMessage());
	    // assertTrue(false);
	}

    }

    @Test
    public void testLockDocument() {

	try {
	    getBL().lockDocument(getToken(), docid_1);
	    System.out.println("lock ok");
	    // assertTrue(true);

	}
	catch (Exception e) {

	    System.out.println("lock KO: " + e.getMessage());
	    // assertTrue(false);
	}

    }

    @Test
    public void testUnlockDocument() {

	try {

	    getBL().unlockDocument(getToken(), docid_1);
	    System.out.println("unock ok");
	    // assertTrue(true);

	}
	catch (Exception e) {

	    System.out.println(e.getMessage());
	    // assertTrue(false);
	}

    }

    // @Test
    // public void testGetAdvancedVersions(){
    //
    // String token = null;
    // BusinessLogic bl = null;
    // Properties p = new Properties();
    //
    // try {
    // p.loadFromXML(this.getClass().getResourceAsStream(
    // "/docer_config.xml"));
    //
    // // appena viene invocato il WS recupero l'informazione del 'provider'
    // // dal file 'config.xml'
    // String providerName = p.getProperty("provider");
    //
    // BusinessLogic.setConfigPath(p.getProperty("CONFIG_DIR"));
    //
    // bl = new BusinessLogic(providerName, 1000);
    // token = bl.login("ente1", "admin", "admin");
    //
    // List<String> av = bl.getAdvancedVersions(token, "1422");
    // for(String id : av){
    // System.out.println(id);
    // }
    // //assertTrue(true);
    //
    // }
    // catch(Exception e){
    // if(bl!=null && token!=null)
    // bl.logout(token);
    //
    // System.out.println(e.getMessage());
    // //assertTrue(false);
    // }
    //
    // }
    //
    // @Test
    // public void testAddNewAdvancedVersion(){
    //
    // String token = null;
    // BusinessLogic bl = null;
    // Properties p = new Properties();
    //
    // try {
    // p.loadFromXML(this.getClass().getResourceAsStream(
    // "/docer_config.xml"));
    //
    // // appena viene invocato il WS recupero l'informazione del 'provider'
    // // dal file 'config.xml'
    // String providerName = p.getProperty("provider");
    //
    // BusinessLogic.setConfigPath(p.getProperty("CONFIG_DIR"));
    //
    // bl = new BusinessLogic(providerName, 1000);
    // token = bl.login("ente1", "admin", "admin");
    //
    // bl.addNewAdvancedVersion(token, "1421", "1423");
    // //assertTrue(true);
    //
    // }
    // catch(Exception e){
    // if(bl!=null && token!=null)
    // bl.logout(token);
    //
    // System.out.println(e.getMessage());
    // //assertTrue(false);
    // }
    //
    // }

    @Test
    public void testUpdateProfileDocument() {

	try {

	    Map<String, String> info = new HashMap<String, String>();
	    // info.put("COD_ENTE", "ENTEX");
	    // info.put("COD_AOO", "AOOX");
	    // info.put("DOCNAME", "test1.txt");
	    // info.put("TYPE_ID", "DOCUMENTO");
	    info.put("DOCNAME", "test_rinominato.txt");
	    info.put("TYPE_ID", "DOCUMENTO");

	    // info.put("DATA_PG", "2012-06-12T10:22:12.584+02:00");
	    // info.put("D_REGISTRAZ", "2012-06-12T10:22:12.584+02:00");
	    // info.put("DATA_INIZIO_PUB", "2012-06-12T10:22:12.584+02:00");

	    getBL().updateProfileDocument(getToken(), docid_1, info);
	    System.out.println("update ok");
	    // assertTrue(true);

	}
	catch (Exception e) {

	    System.out.println(e.getMessage());
	    // assertTrue(false);
	}

    }

    @Test
    public void testSetACLDocumento() {

	try {

	    Map<String, EnumACLRights> acls = new HashMap<String, EnumACLRights>();
	    acls.put("test", EnumACLRights.fullAccess);
	    // acls.put(user2, EnumACLRights.normalAccess);
	    // acls.put(user3, EnumACLRights.readOnly);

	    getBL().setACLDocument(getToken(), "1354", acls);
	    System.out.println("set acl ok");
	    // assertTrue(true);

	}
	catch (Exception e) {

	    System.out.println(e.getMessage());
	    // assertTrue(false);
	}

    }

    // @Test
    // public void testRegistraDocumento(){
    //
    // String token = null;
    // BusinessLogic bl = null;
    // Properties p = new Properties();
    //
    // try {
    // p.loadFromXML(this.getClass().getResourceAsStream(
    // "/docer_config.xml"));
    //
    // // appena viene invocato il WS recupero l'informazione del 'provider'
    // // dal file 'config.xml'
    // String providerName = p.getProperty("provider");
    //
    // BusinessLogic.setConfigPath(p.getProperty("CONFIG_DIR"));
    //
    // bl = new BusinessLogic(providerName, 1000);
    // token = bl.login("ente1", "admin", "admin");
    //
    // Map<String,String> info = new HashMap<String,String>();
    // info.put("COD_ENTE", "ENTE1");
    // info.put("COD_AOO", "AOO11");
    // info.put("N_REGISTRAZ", "1");
    // info.put("ID_REGISTRO", "REG2");
    // info.put("D_REGISTRAZ", "2012-06-12T10:22:12.584+02:00");
    // //info.put("ANNO_REGISTRAZ", "2012");
    // info.put("O_REGISTRAZ", "oggetto registraz");
    // info.put("TIPO_FIRMA","FD");
    // bl.registraDocumento(token,"1422", info);
    // System.out.println("ok");
    // //assertTrue(true);
    //
    // }
    // catch(Exception e){
    // if(bl!=null && token!=null)
    // bl.logout(token);
    //
    // System.out.println(e.getMessage());
    // //assertTrue(false);
    // }
    //
    // }
    //
    //

    @Test
    public void testGetGroupsOfUser() {

	try {
	    token = getBL().login("", "admin", "admin");

	    List<String> groups = getBL().getGroupsOfUser(token, "admin");

	    for (String gid : groups) {
		System.out.println(gid);
	    }

	}
	catch (Exception e) {
	    System.out.println(e.getMessage());
	    // assertTrue(false);
	}
	finally {
	    try {
		getBL().logout(token);
	    }
	    catch (DocerException e) {
	    }
	}

    }

    @Test
    public void testReadConfig() {

	try {
	    token = getBL().login("", "admin", "admin");

	    
	    token = Utils.addTokenKey(token, "calltype", "internal");

	    token = Utils.removeTokenKey(token, "calltype");
	    
	    String config = getBL().readConfig(token);

	    System.out.println(config);
	}
	catch (Exception e) {
	    System.out.println(e.getMessage());
	    // assertTrue(false);
	}
	finally {
	    try {
		getBL().logout(token);
	    }
	    catch (DocerException e) {
	    }
	}

    }

    @Test
    public void testConservazione() {

	try {
	    token = getBL().login("", "admin", "admin");

	    // getBL().addRelated(token, "61935", Arrays.asList("61670"));

	    // getBL().removeRelated(token, "61935", Arrays.asList("61670"));
	    Map<String, String> metadata = new HashMap<String, String>();
	    metadata.put("STATO_CONSERV", "1"); // da conservare

	    getBL().updateProfileDocument(token, "61670", metadata);

	    System.out.println("ok");

	}
	catch (Exception e) {
	    System.out.println(e.getMessage());
	    // assertTrue(false);
	}
	finally {
	    try {
		getBL().logout(token);
	    }
	    catch (DocerException e) {
	    }
	}

    }

    @Test
    public void testUpdatePofile2() {

	try {
	    token = getBL().login("", "admin", "admin");

	    Map<String, String> metadata = new HashMap<String, String>();
	    metadata.put("ARCHIVE_TYPE", "ARCHIVE"); // da conservare

	    getBL().updateProfileDocument(token, "1076543", metadata);

	    System.out.println("ok");

	}
	catch (Exception e) {
	    System.out.println(e.getMessage());
	    // assertTrue(false);
	}
	finally {
	    try {
		getBL().logout(token);
	    }
	    catch (DocerException e) {
	    }
	}

    }
    

    @Test
    public void testCreateSottoFascicolo() {

	try {
	    token = getBL().login("entex", "luca.biasin", "luca.biasin");

	    Map<String, String> metadata = new HashMap<String, String>();

	    metadata.put("cod_ente", "entex");
	    metadata.put("cod_aoo", "aoox");
	    metadata.put("classifica", "1.0.0");
	    metadata.put("parent_progr_fascicolo", "1");
	    metadata.put("progr_fascicolo", "1/1");
	    metadata.put("anno_fascicolo", "2013");
	    metadata.put("des_fascicolo", "des 1/1");
	    metadata.put("enabled", "true");

	    getBL().createFascicolo(token, metadata);

	    System.out.println("ok");

	}
	catch (Exception e) {
	    System.out.println(e.getMessage());
	    // assertTrue(false);
	}
	finally {
	    try {
		getBL().logout(token);
	    }
	    catch (DocerException e) {
	    }
	}

    }

    @Test
    public void testUpdateSottoFascicolo() {

	try {
	    token = getBL().login("entex", "luca.biasin", "luca.biasin");

	    Map<String, String> id = new HashMap<String, String>();
	    id.put("cod_ente", "entex");
	    id.put("cod_aoo", "aoox");
	    id.put("classifica", "1.0.0");
	    id.put("progr_fascicolo", "1/1");
	    id.put("anno_fascicolo", "2013");

	    Map<String, String> metadata = new HashMap<String, String>();
	    metadata.put("des_fascicolo", "des 1/1"); // da conservare
	    metadata.put("enabled", "true"); // da conservare

	    getBL().updateFascicolo(token, id, metadata);

	    System.out.println("ok");

	}
	catch (Exception e) {
	    System.out.println(e.getMessage());
	    // assertTrue(false);
	}
	finally {
	    try {
		getBL().logout(token);
	    }
	    catch (DocerException e) {
	    }
	}

    }

    @Test
    public void testCreateFascicolo() {

	try {
	    token = getBL().login("enteprova", "admin", "admin");

	    Map<String, String> metadata = new HashMap<String, String>();

	    metadata.put("cod_ente", "enteprova");
	    metadata.put("cod_aoo", "aooprova");
	    metadata.put("classifica", "CLass");
	    metadata.put("progr_fascicolo", "1");
	    metadata.put("anno_fascicolo", "2013");
	    metadata.put("des_fascicolo", "des 1");
	    metadata.put("enabled", "true");

	    getBL().createFascicolo(token, metadata);

	    System.out.println("ok");

	}
	catch (Exception e) {
	    System.out.println(e.getMessage());
	    // assertTrue(false);
	}
	finally {
	    try {
		getBL().logout(token);
	    }
	    catch (DocerException e) {
	    }
	}

    }

    
    @Test
    public void testCreateFascicoloCustom() {

	try {
	    token = getBL().login("enteprova", "luca", "luca");

	    Map<String, String> metadata = new HashMap<String, String>();

	    metadata.put("cod_ente", "enteprova");
	    metadata.put("cod_aoo", "aooprova");
	    metadata.put("classifica", "1.0.0");
	    metadata.put("parent_progr_fascicolo", "1");
	    metadata.put("progr_fascicolo", "1/1");
	    metadata.put("anno_fascicolo", "2013");
	    metadata.put("des_fascicolo", "des 1/1");
	    metadata.put("enabled", "true");

	    getBL().createFascicolo(token, metadata);

	    System.out.println("ok");

	}
	catch (Exception e) {
	    System.out.println(e.getMessage());
	    // assertTrue(false);
	}
	finally {
	    try {
		getBL().logout(token);
	    }
	    catch (DocerException e) {
	    }
	}

    }
    
    @Test
    public void testUpdateFascicolo() {

	try {
	    token = getBL().login("entex", "luca.biasin", "luca.biasin");

	    Map<String, String> id = new HashMap<String, String>();
	    id.put("cod_ente", "entex");
	    id.put("cod_aoo", "aoox");
	    id.put("classifica", "1.0.0");
	    id.put("progr_fascicolo", "1");
	    id.put("anno_fascicolo", "2013");

	    Map<String, String> metadata = new HashMap<String, String>();
	    metadata.put("des_fascicolo", "des 1"); // da conservare
	    metadata.put("enabled", "true"); // da conservare

	    getBL().updateFascicolo(token, id, metadata);

	    System.out.println("ok");

	}
	catch (Exception e) {
	    System.out.println(e.getMessage());
	    // assertTrue(false);
	}
	finally {
	    try {
		getBL().logout(token);
	    }
	    catch (DocerException e) {
	    }
	}

    }

    @Test
    public void testCreateSottoTitolario() {

	try {
	    token = getBL().login("entex", "luca.biasin", "luca.biasin");

	    Map<String, String> metadata = new HashMap<String, String>();

	    metadata.put("cod_ente", "entex");
	    metadata.put("cod_aoo", "aoox");
	    metadata.put("parent_classifica", "1.0.0");
	    metadata.put("classifica", "1.1.0");
	    metadata.put("des_titolario", "des 1.1.0");
	    metadata.put("enabled", "true");

	    getBL().createTitolario(token, metadata);

	    System.out.println("ok");

	}
	catch (Exception e) {
	    System.out.println(e.getMessage());
	    // assertTrue(false);
	}
	finally {
	    try {
		getBL().logout(token);
	    }
	    catch (DocerException e) {
	    }
	}

    }

    @Test
    public void testUpdateSottoTitolario() {

	try {
	    token = getBL().login("entex", "luca.biasin", "luca.biasin");

	    Map<String, String> id = new HashMap<String, String>();
	    id.put("cod_ente", "entex");
	    id.put("cod_aoo", "aoox");
	    id.put("classifica", "1.1.0");

	    Map<String, String> metadata = new HashMap<String, String>();
	    metadata.put("des_titolario", "des 1.1.0 "); // da conservare
	    metadata.put("enabled", "true"); // da conservare

	    getBL().updateTitolario(token, id, metadata);

	    System.out.println("ok");

	}
	catch (Exception e) {
	    System.out.println(e.getMessage());
	    // assertTrue(false);
	}
	finally {
	    try {
		getBL().logout(token);
	    }
	    catch (DocerException e) {
	    }
	}

    }

    @Test
    public void testCreateTitolario() {

	try {
	    token = getBL().login("entex", "admin", "admin");

	    Map<String, String> metadata = new HashMap<String, String>();

	    metadata.put("cod_ente", "entex");
	    metadata.put("cod_aoo", "aoox");
	    metadata.put("classifica", "1.0.0");
	    metadata.put("des_titolario", "des 1.0.0");
	    metadata.put("enabled", "true");

	    getBL().createTitolario(token, metadata);

	    System.out.println("ok");

	}
	catch (Exception e) {
	    System.out.println(e.getMessage());
	    // assertTrue(false);
	}
	finally {
	    try {
		getBL().logout(token);
	    }
	    catch (DocerException e) {
	    }
	}

    }

    @Test
    public void testUpdateTitolario() {

	try {
	    token = getBL().login("entex", "luca.biasin", "luca.biasin");

	    Map<String, String> id = new HashMap<String, String>();
	    id.put("cod_ente", "entex");
	    id.put("cod_aoo", "aoox");
	    id.put("classifica", "1.0.0");

	    Map<String, String> metadata = new HashMap<String, String>();
	    metadata.put("des_titolario", "des 1.0.0 "); // da conservare
	    metadata.put("enabled", "true"); // da conservare

	    getBL().updateTitolario(token, id, metadata);

	    System.out.println("ok");

	}
	catch (Exception e) {
	    System.out.println(e.getMessage());
	    // assertTrue(false);
	}
	finally {
	    try {
		getBL().logout(token);
	    }
	    catch (DocerException e) {
	    }
	}

    }

    @Test
    public void testCreateCustomItem() {

	try {
	    token = getBL().login("", "admin", "admin");

	    Map<String, String> metadata = new HashMap<String, String>();
	    metadata.put("type_id", "area_tematica");
	    metadata.put("cod_ente", "entetest");
	    metadata.put("cod_aoo", "aootest");
	    metadata.put("cod_area", "areay");
	    metadata.put("des_area", "des areay");
	    metadata.put("enabled", "true");

	    getBL().createAnagraficaCustom(token, metadata);

	    System.out.println("ok");

	}
	catch (Exception e) {
	    System.out.println(e.getMessage());
	    // assertTrue(false);
	}
	finally {
	    try {
		getBL().logout(token);
	    }
	    catch (DocerException e) {
	    }
	}

    }

    @Test
    public void testUpdateCustomItem() {

	try {
	    token = getBL().login("entex", "luca.biasin", "luca.biasin");

	    Map<String, String> id = new HashMap<String, String>();
	    id.put("cod_ente", "entex");
	    id.put("cod_aoo", "aoox");

	    Map<String, String> metadata = new HashMap<String, String>();
	    metadata.put("des_aoo", "des aooxxx"); // da conservare
	    metadata.put("enabled", "true"); // da conservare

	    getBL().updateAOO(token, id, metadata);

	    System.out.println("ok");

	}
	catch (Exception e) {
	    System.out.println(e.getMessage());
	    // assertTrue(false);
	}
	finally {
	    try {
		getBL().logout(token);
	    }
	    catch (DocerException e) {
	    }
	}

    }

    @Test
    public void testCreateAoo1() {

	try {
	    token = getBL().login("", "admin", "admin");

	    // getBL().addRelated(token, "61935", Arrays.asList("61670"));

	    // getBL().removeRelated(token, "61935", Arrays.asList("61670"));
	    Map<String, String> metadata = new HashMap<String, String>();
	    metadata.put("cod_ente", "ENTETEST"); // da conservare
	    metadata.put("cod_aoo", "aoo2"); // da conservare
	    metadata.put("des_aoo", "des aoo2"); // da conservare
	    metadata.put("enabled", "true"); // da conservare

	    getBL().createAOO(token, metadata);

	    System.out.println("ok");

	}
	catch (Exception e) {
	    System.out.println(e.getMessage());
	    // assertTrue(false);
	}
	finally {
	    try {
		getBL().logout(token);
	    }
	    catch (DocerException e) {
	    }
	}

    }

    @Test
    public void testUpdateAoo1() {

	try {
	    token = getBL().login("entex", "admin", "admin");

	    Map<String, String> id = new HashMap<String, String>();
	    id.put("cod_ente", "entex");
	    id.put("cod_aoo", "aoox");

	    Map<String, String> metadata = new HashMap<String, String>();
	    metadata.put("des_aoo", "des aooxxx"); // da conservare
	    metadata.put("enabled", "true"); // da conservare

	    getBL().updateAOO(token, id, metadata);

	    System.out.println("ok");

	}
	catch (Exception e) {
	    System.out.println(e.getMessage());
	    // assertTrue(false);
	}
	finally {
	    try {
		getBL().logout(token);
	    }
	    catch (DocerException e) {
	    }
	}

    }

    @Test
    public void testCreateEnte1() {

	try {
	    token = getBL().login("", "admin", "admin");

	    // getBL().addRelated(token, "61935", Arrays.asList("61670"));

	    // getBL().removeRelated(token, "61935", Arrays.asList("61670"));
	    Map<String, String> metadata = new HashMap<String, String>();
	    metadata.put("cod_ente", "entex"); // da conservare
	    metadata.put("des_ente", "des entex"); // da conservare
	    metadata.put("enabled", "true"); // da conservare

	    getBL().createEnte(token, metadata);

	    System.out.println("ok");

	}
	catch (Exception e) {
	    System.out.println(e.getMessage());
	    // assertTrue(false);
	}
	finally {
	    try {
		getBL().logout(token);
	    }
	    catch (DocerException e) {
	    }
	}

    }

    @Test
    public void testUpdateEnte1() {

	try {
	    token = getBL().login("entex", "luca.biasin", "luca.biasin");

	    // getBL().addRelated(token, "61935", Arrays.asList("61670"));

	    // getBL().removeRelated(token, "61935", Arrays.asList("61670"));
	    Map<String, String> metadata = new HashMap<String, String>();
	    metadata.put("cod_ente", "entex"); // da conservare
	    metadata.put("des_ente", "des entex"); // da conservare
	    metadata.put("enabled", "true"); // da conservare

	    getBL().updateEnte(token, "entex", metadata);

	    System.out.println("ok");

	}
	catch (Exception e) {
	    System.out.println(e.getMessage());
	    // assertTrue(false);
	}
	finally {
	    try {
		getBL().logout(token);
	    }
	    catch (DocerException e) {
	    }
	}

    }

    @Test
    public void testProtocollaDocumento() {

	String token = null;
	BusinessLogic bl = null;
	Properties p = new Properties();

	try {
	    p.loadFromXML(this.getClass().getResourceAsStream("/docer_config.xml"));

	    // appena viene invocato il WS recupero l'informazione del
	    // 'provider'
	    // dal file 'config.xml'
	    String providerName = p.getProperty("provider");

	    BusinessLogic.setConfigPath(p.getProperty("CONFIG_DIR"));

	    bl = new BusinessLogic(providerName, 1000);
	    token = bl.login("ENTETEST", "admin", "admin");

	    Map<String, String> info = new HashMap<String, String>();
	    info.put("COD_ENTE", "ENTETEST");
	    info.put("COD_AOO", "AOOTEST");
	    info.put("NUM_PG", "1");
	    info.put("REGISTRO_PG", "REG PG");
	    // info.put("DATA_PG", "2012-06-12T10:22:12.584+02:00");
	    // info.put("DATA_PG", "2012-06-12");
	    // info.put("DATA_PG", "2012-06-12 10:22:12");
	    info.put("DATA_PG", "2012-06-12T10:22:12.000+02:00");
	    info.put("ANNO_PG", "2012");
	    info.put("OGGETTO_PG", "oggetto protocollo");
	    info.put("TIPO_PROTOCOLLAZIONE", "E");
	    info.put("TIPO_FIRMA", "FD");
	    info.put("MITTENTI", "AAA");

	    bl.protocollaDocumento(token, "110", info);
	    System.out.println("ok");
	    // assertTrue(true);

	}
	catch (Exception e) {
	    if (bl != null && token != null)
		try {
		    bl.logout(token);
		}
		catch (DocerException e1) {
		    // TODO Auto-generated catch block
		    e1.printStackTrace();
		}

	    System.out.println(e.getMessage());
	    // assertTrue(false);
	}

    }

    @Test
    public void testRegistraDocumentoProtocollato() {

	String token = null;
	BusinessLogic bl = null;
	Properties p = new Properties();

	try {
	    p.loadFromXML(this.getClass().getResourceAsStream("/docer_config.xml"));

	    // appena viene invocato il WS recupero l'informazione del
	    // 'provider'
	    // dal file 'config.xml'
	    String providerName = p.getProperty("provider");

	    BusinessLogic.setConfigPath(p.getProperty("CONFIG_DIR"));

	    bl = new BusinessLogic(providerName, 1000);
	    token = bl.login("ENTETEST", "admin", "admin");

	    Map<String, String> info = new HashMap<String, String>();
	    info.put("COD_ENTE", "ENTETEST");
	    info.put("COD_AOO", "AOOTEST");
	    info.put("N_REGISTRAZ", "1");
	    info.put("ID_REGISTRO", "REG PG");
	    // info.put("DATA_PG", "2012-06-12T10:22:12.584+02:00");
	    // info.put("DATA_PG", "2012-06-12");
	    // info.put("DATA_PG", "2012-06-12 10:22:12");
	    // info.put("FIRMATARIO", "firmatari");
	    info.put("D_REGISTRAZ", "2012-06-12T10:22:01.000+02:00");
	    info.put("O_REGISTRAZ", "oggetto registrazione bis");
	    info.put("TIPO_FIRMA", "F");

	    bl.registraDocumento(token, "110", info);
	    System.out.println("ok");
	    // assertTrue(true);

	}
	catch (Exception e) {
	    if (bl != null && token != null)
		try {
		    bl.logout(token);
		}
		catch (DocerException e1) {
		    // TODO Auto-generated catch block
		    e1.printStackTrace();
		}

	    System.out.println(e.getMessage());
	    // assertTrue(false);
	}

    }

    @Test
    public void testClassificaDocumento() {

	String token = null;
	BusinessLogic bl = null;
	Properties p = new Properties();

	try {
	    p.loadFromXML(this.getClass().getResourceAsStream("/docer_config.xml"));

	    // appena viene invocato il WS recupero l'informazione del
	    // 'provider'
	    // dal file 'config.xml'
	    String providerName = p.getProperty("provider");

	    BusinessLogic.setConfigPath(p.getProperty("CONFIG_DIR"));

	    bl = new BusinessLogic(providerName, 1000);
	    token = bl.login("ENTETEST", "luca.biasin", "luca.biasin");

	    Map<String, String> info = new HashMap<String, String>();
	    info.put("COD_ENTE", "ENTETEST");
	    info.put("COD_AOO", "AOOTEST");

	    info.put("CLASSIFICA", "CLASSIFICATEST");
	    // info.put("ANNO_FASCICOLO", "2014");
	    // info.put("PROGR_FASCICOLO", "FASCICOLOTEST");

	    bl.classificaDocumento(token, "110", info);
	    System.out.println("ok");
	    // assertTrue(true);

	}
	catch (Exception e) {
	    if (bl != null && token != null)
		try {
		    bl.logout(token);
		}
		catch (DocerException e1) {
		    // TODO Auto-generated catch block
		    e1.printStackTrace();
		}

	    System.out.println(e.getMessage());
	    // assertTrue(false);
	}
    }

    @Test
    public void testFascicolaDocumento() {

	String token = null;
	BusinessLogic bl = null;
	Properties p = new Properties();

	try {
	    p.loadFromXML(this.getClass().getResourceAsStream("/docer_config.xml"));

	    // appena viene invocato il WS recupero l'informazione del
	    // 'provider'
	    // dal file 'config.xml'
	    String providerName = p.getProperty("provider");

	    BusinessLogic.setConfigPath(p.getProperty("CONFIG_DIR"));

	    bl = new BusinessLogic(providerName, 1000);
	    token = bl.login("ENTETEST", "luca.biasin", "luca.biasin");

	    Map<String, String> info = new HashMap<String, String>();
	    info.put("COD_ENTE", "ENTETEST");
	    info.put("COD_AOO", "AOOTEST");

	    info.put("CLASSIFICA", "CLASSIFICATEST");
	    info.put("ANNO_FASCICOLO", "2014");
	    info.put("PROGR_FASCICOLO", "FASCICOLOTEST/FIGLIO");
	    info.put("FASC_SECONDARI", "CLASSIFICATEST/2014/FASCICOLOTEST");

	    bl.fascicolaDocumento(token, "110", info);
	    System.out.println("ok");
	    // assertTrue(true);

	}
	catch (Exception e) {
	    if (bl != null && token != null)
		try {
		    bl.logout(token);
		}
		catch (DocerException e1) {
		    // TODO Auto-generated catch block
		    e1.printStackTrace();
		}

	    System.out.println(e.getMessage());
	    // assertTrue(false);
	}
    }

    @Test
    public void testAnnullaFascicolazione() {

	String token = null;
	BusinessLogic bl = null;
	Properties p = new Properties();

	try {
	    p.loadFromXML(this.getClass().getResourceAsStream("/docer_config.xml"));

	    // appena viene invocato il WS recupero l'informazione del
	    // 'provider'
	    // dal file 'config.xml'
	    String providerName = p.getProperty("provider");

	    BusinessLogic.setConfigPath(p.getProperty("CONFIG_DIR"));

	    bl = new BusinessLogic(providerName, 1000);
	    token = bl.login("ENTETEST", "luca.biasin", "luca.biasin");

	    Map<String, String> info = new HashMap<String, String>();
	    info.put("COD_ENTE", "ENTETEST");
	    info.put("COD_AOO", "AOOTEST");

	    info.put("CLASSIFICA", "CLASSIFICATEST");
	    info.put("PROGR_FASCICOLO", "");

	    bl.fascicolaDocumento(token, "110", info);
	    System.out.println("ok");
	    // assertTrue(true);

	}
	catch (Exception e) {
	    if (bl != null && token != null)
		try {
		    bl.logout(token);
		}
		catch (DocerException e1) {
		    // TODO Auto-generated catch block
		    e1.printStackTrace();
		}

	    System.out.println(e.getMessage());
	    // assertTrue(false);
	}
    }

    @Test
    public void testLoginUser() {

	try {

	    String ticket = getBL().login("ENTETEST", "admin", "admin");
	    System.out.println("login effettuata: " + ticket);

	}
	catch (Exception e) {

	    System.out.println(e.getMessage());
	    // assertTrue(false);
	}

    }

    @Test
    public void testGetFascicolo() {

	try {

	    String ticket = getBL().login("EMR", "admin", "admin");
	    System.out.println("login effettuata");

	    Map<String, String> id = new HashMap<String, String>();
	    id.put("COD_ENTE", "EMR");
	    id.put("COD_AOO", "AOO_EMR");
	    id.put("CLASSIFICA", "1");
	    id.put("ANNO_FASCICOLO", "2013");
	    id.put("PROGR_FASCICOLO", "12");

	    Map<String, String> profile = getBL().getFascicolo(ticket, id);

	    System.out.println("-----------------------");
	    System.out.println("Profilo fascicolo " + id.toString());
	    System.out.println();
	    for (String key : profile.keySet()) {
		System.out.println(key + " = " + profile.get(key));
	    }
	    System.out.println("-----------------------");

//	    id = new HashMap<String, String>();
//	    id.put("COD_ENTE", "ENTETEST2");
//	    id.put("COD_AOO", "AOOTEST2");
//	    id.put("CLASSIFICA", "CLASSIFICATEST");
//	    id.put("ANNO_FASCICOLO", "2014");
//	    id.put("PROGR_FASCICOLO", "FASCICOLOTEST");
//
//	    profile = getBL().getFascicolo(ticket, id);
//
//	    System.out.println("-----------------------");
//	    System.out.println("Profilo fascicolo " + id.toString());
//	    System.out.println();
//	    for (String key : profile.keySet()) {
//		System.out.println(key + " = " + profile.get(key));
//	    }
//	    System.out.println("-----------------------");

	}
	catch (Exception e) {

	    System.out.println(e.getMessage());
	    // assertTrue(false);
	}

    }

    @Test
    public void testGetProfileDocument2() {

	try {

	    String ticket = getBL().login("", "admin", "admin");
	    System.out.println("login effettuata");

	    Map<String, String> profile = getBL().getProfileDocument(ticket, "966");

	    System.out.println("-----------------------");
	    System.out.println("Profilo del documento 966");
	    System.out.println();
	    for (String key : profile.keySet()) {
		System.out.println(key + " = " + profile.get(key));
	    }
	    System.out.println("-----------------------");

	    // // assertTrue(true);
	    // profile = getBL().getProfileDocument(ticket, "831");
	    //
	    // System.out.println("-----------------------");
	    // System.out.println("Profilo del documento 831");
	    // System.out.println();
	    // for (String key : profile.keySet()) {
	    // System.out.println(key + " = " + profile.get(key));
	    // }
	    // System.out.println("-----------------------");
	    //
	    //
	    // profile = getBL().getProfileDocument(ticket, "832");
	    //
	    // System.out.println("-----------------------");
	    // System.out.println("Profilo del documento 832");
	    // System.out.println();
	    // for (String key : profile.keySet()) {
	    // System.out.println(key + " = " + profile.get(key));
	    // }
	    // System.out.println("-----------------------");

	}
	catch (Exception e) {

	    System.out.println(e.getMessage());
	    // assertTrue(false);
	}

    }

    @Test
    public void testGetProfileDocument() {

	try {
	    Map<String, String> profile = getBL().getProfileDocument(getToken(), docid_1);

	    System.out.println("-----------------------");
	    System.out.println("Profilo del documento: " + docid_1);
	    System.out.println();
	    for (String key : profile.keySet()) {
		System.out.println(key + " = " + profile.get(key));
	    }
	    System.out.println("-----------------------");

	    // assertTrue(true);

	}
	catch (Exception e) {

	    System.out.println(e.getMessage());
	    // assertTrue(false);
	}

    }

    @Test
    public void testDownloadDocument() {

	try {
	    byte[] file = getBL().downloadDocument(getToken(), "3823", "c:\\windows", 1000);
	    System.out.println("download document, file size: " + file.length);

	    file = getBL().downloadVersion(getToken(), "3823", "2", "c:\\windows", 1000);
	    System.out.println("download document, file size: " + file.length);

	    // assertTrue(true);

	}
	catch (Exception e) {

	    System.out.println(e.getMessage());
	    // assertTrue(false);
	}

    }

    @Test
    public void testLogout() {
	try {
	    getBL().logout(getToken());
	    System.out.println("logout effettuata");
	    // assertTrue(true);
	}
	catch (DocerException e) {
	    System.out.println(e.getMessage());
	    // assertTrue(false);
	}
    }

    
    @Test
    public void testConfig() {

	
	
	
	
	
	    Config c = null;
	    try {
		c = new Config();
	    }
	    catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	    catch (XMLStreamException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }

	    File f = null;
	    try {
		f = c.getConfigFile();
	    }
	    catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	    System.out.println(f.getAbsolutePath());
	    	    
	    try {
		System.out.println(c.readConfig());
	    }
	    catch (XMLStreamException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	    catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	    
	    try {
		File f1 = new File("C:/Users/user1/Desktop/test.txt");
		c.setConfigFile(f1);
	    }
	    catch (XMLStreamException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	    catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	    
	    
	    try {
		System.out.println(c.readConfig());
	    }
	    catch (XMLStreamException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	    catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	    
	    String a = "";
    }
    
    
}
