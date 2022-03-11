package it.kdm.docer.fonte.webservices.businesslogic;

import static org.junit.Assert.*;
import it.kdm.docer.commons.TicketCipher;
import it.kdm.docer.commons.Utils;
import it.kdm.docer.fonte.webservices.businesslogic.BusinessLogic;
import it.kdm.docer.fonte.webservices.businesslogic.utility.AnagraficaType;
import it.kdm.docer.fonte.webservices.businesslogic.utility.DocumentType;
import it.kdm.docer.fonte.webservices.businesslogic.utility.FieldDescriptor;
import it.kdm.docer.sdk.classes.KeyValuePair;
import it.kdm.docer.sdk.exceptions.DocerException;
import it.kdm.ws.ISearchItem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.junit.Test;

public class TestBusinessLogic {

    static String ente = "ENTE_TEST4";
    static String userAdmin = "admin";
    static String userAdminPasword = "admin";

    static String tokenNormal = null;
    static String normalUser = "utente1_4";
    static String normalPassword = normalUser; 
    
    static String token = null;
    static String cod_ente = "ENTE_TEST";
    static String cod_aoo = "AOO_TEST";
    static BusinessLogic businessLogic = null;

    static String docid_1 = "1213";
    static String docid_2 = "1212";
    static String docid_3 = "1213";

    static String docid_4 = "1214";
    static String docid_5 = "1215";
    static String docid_6 = "";

    static String user1 = "user1";
    static String user2 = "user2";
    static String user3 = "user3";    

    
    @Test
    public void testConfigurationAnagrafiche() throws KeyException, DocerException {
        
    	getBL();
    	
    	// anagrafiche
    	for(String key : BusinessLogic.ANAGRAFICHE_TYPES.keySet()){
    		System.out.println("");
    		System.out.println("----------------------------");
    		System.out.println("Anagrafica definita: "+key);
    		AnagraficaType at = BusinessLogic.ANAGRAFICHE_TYPES.get(key);
    		System.out.println("type: " +at.getTypeId());
    		System.out.println("isAnagraficaCustom: " +at.isAnagraficaCustom());
    		System.out.println("codicePropName: " +at.getCodicePropName());
    		System.out.println("descrizionePropName: " +at.getDescrizionePropName());
    		System.out.println("-- metadati base --");
    		for(FieldDescriptor fd : at.baseFields.values()){
    			System.out.println("propName: " +fd.getPropName() +"; type: " +fd.getType() +"; anagraficaTypeId: " +fd.getAnagraficaTypeId() +"; format: " +fd.getFormat() +"; inheritedFromBase: " +fd.isInheritedFromBase());
    		}
    		System.out.println("-- profili custom --");
    		for(String chiave : at.customProfiles.keySet()){
    			System.out.println("...metadati definiti per Ente e AOO: " +chiave);
        		for(FieldDescriptor fdCustom : at.customProfiles.get(chiave).values()){
        			System.out.println("propName: " +fdCustom.getPropName() +"; type: " +fdCustom.getType() +"; anagraficaTypeId: " +fdCustom.getAnagraficaTypeId() +"; format: " +fdCustom.getFormat() +"; inheritedFromBase: " +fdCustom.isInheritedFromBase());    			
        		}
    		}
    	}    	
        
    }
    

    @Test
    public void testConfigurationDocumenti() throws KeyException, DocerException {
        
    	getBL();
    	    	
    	// documenti
    	for(String key : BusinessLogic.DOCUMENT_TYPES.keySet()){
    		System.out.println("");
    		System.out.println("----------------------------");
    		System.out.println("Document type: "+key);
    		DocumentType dt = BusinessLogic.DOCUMENT_TYPES.get(key);
    		System.out.println("type: " +dt.getTypeId());
    		System.out.println("description: " +dt.getDescription());
    		System.out.println("description: " +dt.getDescription());
    		System.out.println("-- metadati base --");
    		for(FieldDescriptor fd : dt.baseFields.values()){
    			System.out.println("propName: " +fd.getPropName() +"; type: " +fd.getType() +"; anagraficaTypeId: " +fd.getAnagraficaTypeId() +"; format: " +fd.getFormat() +"; inheritedFromBase: " +fd.isInheritedFromBase());    			
    		}		
    		System.out.println("-- profili custom --");
    		for(String chiave : dt.customProfiles.keySet()){
    			System.out.println("...metadati definiti per Ente e AOO: " +chiave);
        		for(FieldDescriptor fdCustom : dt.customProfiles.get(chiave).values()){
        			System.out.println("propName: " +fdCustom.getPropName() +"; type: " +fdCustom.getType() +"; anagraficaTypeId: " +fdCustom.getAnagraficaTypeId() +"; format: " +fdCustom.getFormat() +"; inheritedFromBase: " +fdCustom.isInheritedFromBase());    			
        		}
    		}
    	}
        
    }

    
    @Test
    public void testDecrypt() throws KeyException {
        
        String tok  = "F54TjLuHLl-zjstQKi3CDPKAHPE3c702syBg9GqGok4dBnWLmvB82LkXPAfi7l6x1znFinCuU76BsMc0EVAdSo-yLXuZF1AwZ7eT84iXIRJzpCvzZCF8mXq0RLOCXY-H1TLkoAVJQq-Trhed7k1vfTQxDapV-SD6hUHv7pHPHBSZYR7B6A5z8WXkHWiPDlSQ5f64soDFhkcHEeik8o6mKGIEB321hWfo1N-uXxDdLEW1sJ3qIAV-Vocw7054e-F9kSstyAJo8XQjayDhd4TiawNrTFMu4n7wT_3HaNK-WHMsybv6dQhyQnDcJ-l9a8SvaxwvuQ-iG_Gt6tCGL0cTqVOTYQlCAkXg7knXCe5Z0PvVwtu-VQlS9Qf3tfgHBl1Fug7rJ4HxM1gqmcBmR2o8uYAhvoOYpkbSf8BvGaLghxM";
        TicketCipher cipher = new TicketCipher();
        String dtok = cipher.decryptTicket(tok);
        System.out.println(dtok);
        System.out.println(Utils.extractTokenKey(dtok  , "Provider_EMR_AOO_EMR"));
    }
    
    @Test
    public void testTest() {
        DateTime d = new DateTime();
        String strDate = d.toString("yyyy-MM-dd'T'HH:mm:ss.SSS");
        Map<String, String> a = new HashMap<String, String>();
        System.out.println(a.get(null));
        
        
        try {
            DateTime date = parseDateTime("");
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    private DateTime parseDateTime(String datetime) {
        DateTimeFormatter fm = ISODateTimeFormat.dateTime();
        return fm.parseDateTime(datetime);
    }

//    @Test
//    public void testAll() {
//        testLogin();
//        // testSearchDocuments();
//
//        testAdvancedVersion();
//        // testRiferimenti();
//
//        // testFolder();
//
//        // testCreateAnagrafiche();
//        // testCreateUsers();
//        // testCreateGroups();
//        // testSetGroupsOfUser();
//        // testSetUsersOfGroup();
//
//        // testCreateDocument1();
//        // testCreateDocument2();
//        // testCreateDocument3();
//        // testCreateDocument4();
//        // testCreateDocument5();
//        // testCreateDocument6();
//
//        docid_1 = "73919";
//        docid_2 = "73920";
//
//        testRelated();
//        // testSetACLDocumento();
//        // testAddRelated();
//        // testCreateVersion2();
//        // testCreateVersion3();
//        // testGetVersions();
//        // testLockDocument();
//        // testUnlockDocument();
//        // testUpdateProfileDocument();
//        // testClassificaDocumento1();
//        // testSetACLDocumento();
//
//        // long min = 100000;
//        // long max = 0;
//        //
//        // long mm = 60000;
//        // long hh = 60000 * 60;
//        //
//        // long startTime = new Date().getTime();
//        // long end = startTime;
//        // long total = 0;
//        //
//        // while( total < (120*mm)){
//        // end = new Date().getTime();
//        // total = end - startTime;
//        //
//        // testFascicolaDocumento1();
//        // testAnnullaFascicoloDocumento();
//        // testFascicolaDocumento1();
//        // testFascicolaDocumento3();
//        // testFascicolaDocumento4();
//        // testAnnullaFascicoloDocumento();
//        //
//        // }
//
//        // testAnnullaFascicolaDocumento1();
//        // testFascicolaDocumento2();
//        // testAnnullaFascicolaDocumento2();
//        //
//        // testPubblicaDocumento();
//        // testPubblicaDocumento();
//        // testGetProfileDocument();
//        // testDownloadDocument();
//        testLogout();
//
//    }
//
//    @Test
//    public void testGetGroup() {
//
//        testLogin();
//
//        Map<String, String> info = new HashMap<String, String>();
//        info.put("GROUP_ID", "group1");
//        info.put("GROUP_NAME", "group1");
//        info.put("PARENT_GROUP_ID", cod_aoo);
//        try {
//            getBL().getUser(getToken(), "group1");
//            // assertTrue(true);
//        }
//        catch (Exception e) {
//            System.out.println(e.getMessage());
//            // assertTrue(false);
//        }
//    }

//    @Test
//    public void testCreateGroup() {
//
//        testLogin();
//
//        Map<String, String> info = new HashMap<String, String>();
//        info.put("GROUP_ID", "group1");
//        info.put("GROUP_NAME", "group1");
//        info.put("PARENT_GROUP_ID", cod_aoo);
//        try {
//            getBL().createGroup(getToken(), info);
//            System.out.println("gruppo group1 creato");
//            // assertTrue(true);
//        }
//        catch (Exception e) {
//            System.out.println(e.getMessage());
//            // assertTrue(false);
//        }
//    }
//
//    @Test
//    public void testAll2() {
//        testLogin();
//        // testSearchDocuments();
//
//        docid_1 = "61782";
//        docid_2 = "61783";
//        docid_3 = "61784";
//        docid_4 = "61663";
//        docid_5 = "61664";
//        docid_6 = "61665";
//
//        // testRelated();
//        // testAdvancedVersion();
//        // testRiferimenti();
//
//        // testFolder();
//
//        // testCreateAnagrafiche();
//        // testCreateUsers();
//        // testCreateGroups();
//        // testSetGroupsOfUser();
//        // testSetUsersOfGroup();
//        // testCreateDocument1();
//        // testCreateDocument2();
//        // testCreateDocument3();
//        // testSetACLDocumento();
//        // testAddRelated();
//        // testCreateVersion2();
//        // testCreateVersion3();
//        // testGetVersions();
//        // testLockDocument();
//        // testUnlockDocument();
//        // testUpdateProfileDocument();
//        // testClassificaDocumento1();
//        // testSetACLDocumento();
//
//        long min = 100000;
//        long max = 0;
//
//        long mm = 60000;
//        long hh = 60000 * 60;
//
//        long startTime = new Date().getTime();
//        long end = startTime;
//        long total = 0;
//
//        while (total < (120 * mm)) {
//            end = new Date().getTime();
//            total = end - startTime;
//
//            testFascicolaDocumento1();
//            testAnnullaFascicoloDocumento();
//            testFascicolaDocumento1();
//            testFascicolaDocumento3();
//            testFascicolaDocumento4();
//            testAnnullaFascicoloDocumento();
//
//        }
//
//        // testAnnullaFascicolaDocumento1();
//        // testFascicolaDocumento2();
//        // testAnnullaFascicolaDocumento2();
//        //
//        // testPubblicaDocumento();
//        // testPubblicaDocumento();
//        // testGetProfileDocument();
//        // testDownloadDocument();
//        testLogout();
//
//    }
//
//    // @Test
//    // public void testEquals() {
//    //
//    // try{
//    // String a = "ciccio";
//    // String b = null;
//    // System.out.println(a.equals(b));
//    // System.out.println(a.equals(a));
//    // //assertTrue(true);
//    //
//    // }
//    // catch(Exception e){
//    // System.out.println(e.getMessage());
//    // //assertTrue(false);
//    // }
//    //
//    // }
//
    private static long DISK_BUFFER_THRESHOLD = 3000000;
    private static String DISK_BUFFER_DIRECTORY = "";
    private static int PRIMARYSEARCH_MAX_RESULTS = 1000;
    
    private BusinessLogic getBL() throws DocerException {

        if (businessLogic == null) {                
                
                
                Properties p = new Properties();
                try {
                    p.loadFromXML(this.getClass().getResourceAsStream(
                            "/docer_config.xml"));
                } catch (InvalidPropertiesFormatException e) {
                    throw new DocerException(
                            "500 - read config.xml: InvalidPropertiesFormatException: "
                                    + e.getMessage());
                } catch (IOException e) {
                    throw new DocerException("500 - read config.xml: IOException: "
                            + e.getMessage());
                }

                // appena viene invocato il WS recupero l'informazione del 'provider'
                // dal file 'config.xml'
                String providerName = p.getProperty("provider");

                try {
                    DISK_BUFFER_THRESHOLD = Long.parseLong(p
                            .getProperty("DISK_BUFFER_THRESHOLD"));
                } catch (Exception provEx) {
                   throw new DocerException(
                            "500 - DISK_BUFFER_THRESHOLD config key wrong or missing: "
                                    + provEx.getMessage());
                }

                try {
                    DISK_BUFFER_DIRECTORY = p.getProperty("DISK_BUFFER_DIRECTORY");
                } catch (Exception provEx) {                   
                    throw new DocerException(
                            "500 - DISK_BUFFER_DIRECTORY config key wrong or missing: "
                                    + provEx.getMessage());
                }

                try {
                    PRIMARYSEARCH_MAX_RESULTS = Integer.parseInt(p
                            .getProperty("PRIMARYSEARCH_MAX_RESULTS"));
                } catch (Exception provEx) {                  
                    throw new DocerException(
                            "500 - PRIMARYSEARCH_MAX_RESULTS config key missing: "
                                    + provEx.getMessage());
                }

                try {
                    BusinessLogic.setConfigPath(p.getProperty("CONFIG_DIR"));
                } catch (Exception provEx) {                  
                    throw new DocerException(
                            "500 - CONFIG_DIR config key missing: "
                                    + provEx.getMessage());
                }

                try {
                    businessLogic = new BusinessLogic(providerName, PRIMARYSEARCH_MAX_RESULTS);
                    
                } catch (DocerException e) {
                    throw new DocerException("500 - errore istanza Business Logic: " +e.getMessage());
                }
                           
        }

        
        return businessLogic;
        
    }
//
    private String getToken() {
        if (token == null){
            testLogin();
        }            
        return token;
    }
//
//    private String getTokenNormal() {
//        if (tokenNormal == null){
//            testLoginNormal();
//        }            
//        return tokenNormal;
//    }
//    
//    @Test
//    public void testCreateMassive() {
//
//        testLogin();
//
//        // long min = 100000;
//        // long max = 0;
//        //
//        // long mm = 60000;
//        // long hh = 60000 * 60;
//        //
//        // long startTime = new Date().getTime();
//        // long end = startTime;
//        // long total = 0;
//        // long start = 0;
//        // long time = 0;
//        // List<Map<String,String>> res = null;
//        //
//        // while( total < (120*mm)){
//        // start = new Date().getTime();
//        // testCreateDocument1();
//        // end = new Date().getTime();
//        // time = end-start;
//        // total = end - startTime;
//        // if(time<min){
//        // min = time;
//        // }
//        // if(time>max){
//        // max = time;
//        // }
//        // System.out.println("creato docnum: " +docid_1 +", tempo: " +time
//        // +". Min: " +min +". Max: " +max);
//        // }
//        //
//
//        try {
//            getBL().lockDocument(token, "57294");
//        }
//        catch (DocerException e) {
//
//            e.printStackTrace();
//        }
//
//        testLogout();
//    }
//
//    @Test
//    public void testWriteLog() {
//
//        Map<String, String> map = new HashMap<String, String>();
//        map.put("a", "value");
//        map.put("b", "value");
//        map.put("c", "value");
//
//        System.out.println(map.keySet());
//    }
//
//    @Test
//    public void test2() {
//        testLogin();
//
//    }
//
//    @Test
//    public void testAddNewAdvancedVersion() {
//
//        try {
//
//            getBL().addNewAdvancedVersion(getToken(), docid_1, docid_2);
//            getBL().addNewAdvancedVersion(getToken(), docid_1, docid_3);
//            System.out.println("advanced version ok");
//        }
//        catch (DocerException e) {
//            System.out.println("advanced version KO: " + e.getMessage());
//            return;
//        }
//
//    }
//
//    @Test
//    public void testAddRiferimenti() {
//
//        try {
//
//            getBL().addRiferimentiDocuments(getToken(), docid_1, Arrays.asList(docid_2));
//            getBL().addRiferimentiDocuments(getToken(), docid_3, Arrays.asList(docid_1, docid_2, docid_3));
//            System.out.println("advanced version ok");
//        }
//        catch (DocerException e) {
//            System.out.println("advanced version KO: " + e.getMessage());
//            return;
//        }
//    }
//
//    @Test
//    public void testSearchDocuments() {
//
//        try {
//
//            Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
//
//            searchCriteria.put("TYPE_ID", Arrays.asList("DOCUMENTO"));
//            searchCriteria.put("COD_ENTE", Arrays.asList(cod_ente));
//            searchCriteria.put("COD_AOO", Arrays.asList(cod_aoo));
//
//            List<ISearchItem> res = getBL().searchDocuments(token, searchCriteria, null, 100, null);
//            System.out.println("ricerca ok risultati: " + res.size());
//
//        }
//        catch (Exception e) {
//
//            System.out.println("ricerca KO: " + e.getMessage());
//            // assertTrue(false);
//
//        }
//
//    }
//
//    @Test
//    public void testOrderBy() {
//
//        String token = "";
//        try {
//
//            token = getBL().login("", "admin", "admin");
//
//            // getBL().addRelated(token, "61935", Arrays.asList("61670"));
//
//            // getBL().removeRelated(token, "61935", Arrays.asList("61670"));
//            Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
//            searchCriteria.put("COD_ENTE", Arrays.asList("C_I840"));
//            searchCriteria.put("COD_AOO", Arrays.asList("C-I840-01"));
//            searchCriteria.put("ID_REGISTRO", Arrays.asList("DETE1"));
//            searchCriteria.put("A_REGISTRAZ", Arrays.asList("2013"));
//
//            Map<String, EnumSearchOrder> orderby = new HashMap<String, EnumSearchOrder>();
//            orderby.put("N_REGISTRAZ", EnumSearchOrder.DESC);
//
//            List<ISearchItem> res = getBL().searchDocuments(token, searchCriteria, null, 1000, orderby);
//            for (ISearchItem isi : res) {
//
//                if (isi.getMetadata() == null) {
//                    continue;
//                }
//                String nRegistraz = "nothing";
//                for (KeyValuePair kvp : isi.getMetadata()) {
//                    if (kvp.getKey().equalsIgnoreCase("N_REGISTRAZ")) {
//                        nRegistraz = kvp.getValue();
//                        break;
//                    }
//                }
//
//                System.out.println(nRegistraz);
//            }
//
//        }
//        catch (Exception e) {
//
//            System.out.println("testAllegati KO: " + e.getMessage());
//            // assertTrue(false);
//
//        }
//        finally {
//            try {
//                getBL().logout(token);
//            }
//            catch (DocerException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//
//        }
//    }
//
//    @Test
//    public void testAllegati() {
//
//        String token = "";
//        try {
//
//            token = getBL().login("", "admin", "admin");
//
//            // getBL().addRelated(token, "61935", Arrays.asList("61670"));
//
//            // getBL().removeRelated(token, "61935", Arrays.asList("61670"));
//
//            Map<String, String> info = new HashMap<String, String>();
//            // info.put("COD_ENTE", "ENTE8");
//            // info.put("COD_AOO", "AOO11");
//            // info.put("CLASSIFICA", "1.1.0");
//            // info.put("ANNO_FASCICOLO", "2012");
//            // info.put("PROGR_FASCICOLO", "2");
//            //
//            // getBL().fascicolaDocumento(token, "61760", info);
//
//            info.clear();
//            info.put("COD_ENTE", "ENTE8");
//            info.put("COD_AOO", "AOO11");
//            info.put("NUM_PG", "1");
//            info.put("REGISTRO_PG", "REG PG");
//            // info.put("DATA_PG", "2012-06-12T10:22:12.584+02:00");
//            // info.put("DATA_PG", "2012-06-12");
//            // info.put("DATA_PG", "2012-06-12 10:22:12");
//            info.put("DATA_PG", "2012-06-12T10:22:12.000+02:00");
//            info.put("ANNO_PG", "2012");
//            info.put("OGGETTO_PG", "oggetto protocollo");
//            info.put("TIPO_PROTOCOLLAZIONE", "E");
//            info.put("TIPO_FIRMA", "FD");
//            info.put("MITTENTI", "AAA");
//
//            getBL().protocollaDocumento(token, "61935", info);
//
//            getBL().addRelated(token, "61935", Arrays.asList("61670"));
//
//        }
//        catch (Exception e) {
//
//            System.out.println("testAllegati KO: " + e.getMessage());
//            // assertTrue(false);
//
//        }
//        finally {
//            try {
//                getBL().logout(token);
//            }
//            catch (DocerException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//
//        }
//    }
//
//    @Test
//    public void testAllegati2() {
//
//        String token = "";
//        try {
//
//            token = getBL().login("", "admin", "admin");
//
//
//            //getBL().removeRelated(token, "1210", Arrays.asList("1211"));
//            
//            getBL().addRelated(token, "1210", Arrays.asList("1211"));
//            getBL().removeRelated(token, "1210", Arrays.asList("1211"));
//
//        }
//        catch (Exception e) {
//
//            System.out.println("testAllegati KO: " + e.getMessage());
//            // assertTrue(false);
//
//        }
//        finally {
//            try {
//                getBL().logout(token);
//            }
//            catch (DocerException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//
//        }
//    }
//    
//    @Test
//    public void testCreateEnteAsSysadmin() {
//
//        String token = "";
//        try {
//
//            token = getBL().login("", "sysadmin", "sysadmin");
//            Map<String, String> info = new HashMap<String, String>();
//            info.put("COD_ENTE", "ENTE_LUCA4");
//            info.put("DES_ENTE", "des ENTE");
//            info.put("ENABLED", "true");
//            getBL().createEnte(token, info);
//
//            Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
//
//            searchCriteria.put("COD_ENTE", Arrays.asList("ENTE_LUCA4"));
//
//            long startTime = new Date().getTime();
//            long endTime = new Date().getTime();
//            int i = 0;
//            List<ISearchItem> res = new ArrayList<ISearchItem>();
//            while (res.size() == 0) {
//                res = getBL().searchAnagrafiche(token, "ENTE", searchCriteria);
//
//                System.out.println("eseguita ricerca ");
//                if (res.size() == 0)
//                    continue;
//
//                endTime = new Date().getTime();
//
//                if ((endTime - startTime) > 20000) {
//
//                    System.out.println("interrotto dopo " + (endTime - startTime) + " msec");
//                    break;
//                }
//
//                System.out.println("risultato avuto dopo " + (endTime - startTime) + " msec");
//                ISearchItem si = res.get(0);
//                for (KeyValuePair kvp : si.getMetadata()) {
//                    System.out.println(kvp.getKey() + "=" + kvp.getValue());
//                }
//            }
//        }
//        catch (Exception e) {
//
//            System.out.println("ricerca KO: " + e.getMessage());
//            // assertTrue(false);
//
//        }
//        finally {
//            try {
//                getBL().logout(token);
//            }
//            catch (DocerException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//
//        }
//    }


    @Test
	public void testSearchAnagr() {

		String token = null;
		try {

			token = getBL().login("", "admin", "admin");

			Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();

			searchCriteria.put("$KEYWORDS", Arrays.asList("PMNNDR83T02D643M"));

			List<ISearchItem> res = new ArrayList<ISearchItem>();
				
			res = getBL().searchAnagrafiche(token, "FASCICOLO", searchCriteria);

				System.out.println("eseguita ricerca ");
				if (res.size() == 0)
					return;


				ISearchItem si = res.get(0);
				for (Entry<String, String> kvp : si.entrySet()) {
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

//    @Test
//    public void testSearchAnagr() {
//
//        String token = "";
//        try {
//
//            token = getBL().login("", "admin", "admin");
//
//            Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
//
//            searchCriteria.put("COD_ENTE", Arrays.asList("*"));
//
//            long startTime = new Date().getTime();
//            long endTime = new Date().getTime();
//            int i = 0;
//            List<ISearchItem> res = new ArrayList<ISearchItem>();
//            while (res.size() == 0) {
//                res = getBL().searchAnagrafiche(token, "ENTE", searchCriteria);
//
//                System.out.println("eseguita ricerca ");
//                if (res.size() == 0)
//                    continue;
//
//                endTime = new Date().getTime();
//
//                if ((endTime - startTime) > 20000) {
//
//                    System.out.println("interrotto dopo " + (endTime - startTime) + " msec");
//                    break;
//                }
//
//                System.out.println("risultato avuto dopo " + (endTime - startTime) + " msec");
//                ISearchItem si = res.get(0);
//                for (KeyValuePair kvp : si.getMetadata()) {
//                    System.out.println(kvp.getKey() + "=" + kvp.getValue());
//                }
//            }
//        }
//        catch (Exception e) {
//
//            System.out.println("ricerca KO: " + e.getMessage());
//            // assertTrue(false);
//
//        }
//        finally {
//            try {
//                getBL().logout(token);
//            }
//            catch (DocerException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//
//        }
//    }
//
//    @Test
//    public void testSearchEnte() {
//
//        String token = "";
//        try {
//
//            token = getBL().login("", "sysadmin", "sysadmin");
//
//            Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();
//
//            searchCriteria.put("COD_ENTE", Arrays.asList("TEE"));
//
//            List<ISearchItem> res = getBL().searchAnagrafiche(token, "ENTE", searchCriteria);
//            System.out.println("ricerca ok risultati: " + res.size());
//
//            ISearchItem si = res.get(0);
//            for (KeyValuePair kvp : si.getMetadata()) {
//                System.out.println(kvp.getKey() + "=" + kvp.getValue());
//            }
//        }
//        catch (Exception e) {
//
//            System.out.println("ricerca KO: " + e.getMessage());
//            // assertTrue(false);
//
//        }
//        finally {
//            try {
//                getBL().logout(token);
//            }
//            catch (DocerException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//
//        }
//
//    }
//
//    @Test
//    public void testClassificaDocumento1() {
//
//        try {
//
//            Map<String, String> info = new HashMap<String, String>();
//            info.put("COD_ENTE", cod_ente);
//            info.put("COD_AOO", cod_aoo);
//            info.put("CLASSIFICA", "1.0.0");
//
//            getBL().classificaDocumento(getToken(), docid_1, info);
//            System.out.println("classifica ok");
//
//        }
//        catch (Exception e) {
//
//            System.out.println("classifica OK: " + e.getMessage());
//            // assertTrue(false);
//        }
//
//    }
//
//    @Test
//    public void testClassificaDocumento2() {
//
//        try {
//
//            Map<String, String> info = new HashMap<String, String>();
//            info.put("COD_ENTE", cod_ente);
//            info.put("COD_AOO", cod_aoo);
//            info.put("CLASSIFICA", "1.1.0");
//
//            getBL().classificaDocumento(getToken(), docid_1, info);
//            System.out.println("classifica ok");
//            // assertTrue(true);
//
//        }
//        catch (Exception e) {
//
//            System.out.println("classifica OK: " + e.getMessage());
//            // assertTrue(false);
//        }
//
//    }
//
//    @Test
//    public void testFascicolaDocumento1() {
//
//        try {
//
//            Map<String, String> info = new HashMap<String, String>();
//            info.put("COD_ENTE", cod_ente);
//            info.put("COD_AOO", cod_aoo);
//            info.put("CLASSIFICA", "1.0.0");
//            info.put("ANNO_FASCICOLO", "2012");
//            info.put("PROGR_FASCICOLO", "1");
//
//            getBL().fascicolaDocumento(getToken(), docid_1, info);
//            System.out.println("fascicola 1 ok");
//            // assertTrue(true);
//
//        }
//        catch (Exception e) {
//
//            System.out.println("fascicola 1 KO: " + e.getMessage());
//            // assertTrue(false);
//        }
//    }
//
//    @Test
//    public void testAnnullaFascicoloDocumento() {
//
//        try {
//
//            Map<String, String> info = new HashMap<String, String>();
//            info.put("COD_ENTE", cod_ente);
//            info.put("COD_AOO", cod_aoo);
//            info.put("CLASSIFICA", "1.1.0");
//            info.put("ANNO_FASCICOLO", "");
//            info.put("PROGR_FASCICOLO", "");
//
//            getBL().fascicolaDocumento(getToken(), docid_1, info);
//            System.out.println("annulla fascicolazione ok");
//            // assertTrue(true);
//
//        }
//        catch (Exception e) {
//
//            System.out.println("annulla fascicolazione KO: " + e.getMessage());
//            // assertTrue(false);
//        }
//    }
//
//    @Test
//    public void testFascicolaDocumento3() {
//
//        try {
//
//            Map<String, String> info = new HashMap<String, String>();
//            info.put("COD_ENTE", cod_ente);
//            info.put("COD_AOO", cod_aoo);
//            info.put("CLASSIFICA", "1.1.0");
//            info.put("ANNO_FASCICOLO", "2012");
//            info.put("PROGR_FASCICOLO", "2/1/XXX");
//
//            getBL().fascicolaDocumento(getToken(), docid_1, info);
//            System.out.println("fascicola 3 ok");
//            // assertTrue(true);
//
//        }
//        catch (Exception e) {
//
//            System.out.println("fascicola 3 KO: " + e.getMessage());
//            // assertTrue(false);
//        }
//    }
//
//    @Test
//    public void testFascicolaDocumento4() {
//
//        try {
//
//            Map<String, String> info = new HashMap<String, String>();
//            info.put("COD_ENTE", cod_ente);
//            info.put("COD_AOO", cod_aoo);
//            info.put("CLASSIFICA", "1.1.0");
//            info.put("ANNO_FASCICOLO", "2012");
//            info.put("PROGR_FASCICOLO", "2");
//
//            getBL().fascicolaDocumento(getToken(), docid_1, info);
//            System.out.println("fascicolazione 4 ok");
//            // assertTrue(true);
//
//        }
//        catch (Exception e) {
//            System.out.println("fascicolazione 4 KO: " + e.getMessage());
//            // assertTrue(false);
//        }
//    }
//
//    @Test
//    public void testFascicolaDocumento5() {
//
//        try {
//
//            Map<String, String> info = new HashMap<String, String>();
//            info.put("COD_ENTE", cod_ente);
//            info.put("COD_AOO", cod_aoo);
//            info.put("CLASSIFICA", "1.0.0");
//            info.put("ANNO_FASCICOLO", "2012");
//            info.put("PROGR_FASCICOLO", "1");
//
//            getBL().fascicolaDocumento(getToken(), docid_1, info);
//            System.out.println("fascicola 2 ok");
//            // assertTrue(true);
//
//        }
//        catch (Exception e) {
//
//            System.out.println("fascicola 2 OK: " + e.getMessage());
//            // assertTrue(false);
//        }
//    }
//
//    @Test
//    public void testAnnullaFascicolaDocumento2() {
//
//        try {
//
//            Map<String, String> info = new HashMap<String, String>();
//            // info.put("COD_ENTE", cod_ente);
//            // info.put("COD_AOO", cod_aoo);
//            // info.put("CLASSIFICA", "1.1.0");
//            info.put("ANNO_FASCICOLO", "");
//            info.put("PROGR_FASCICOLO", "");
//
//            getBL().fascicolaDocumento(getToken(), docid_1, info);
//            System.out.println("annulla fascicolazione 2 ok");
//            // assertTrue(true);
//
//        }
//        catch (Exception e) {
//
//            System.out.println("annulla fascicolazione 2 KO " + e.getMessage());
//            // assertTrue(false);
//        }
//    }
//
//    @Test
//    public void testPubblicaDocumento() {
//
//        try {
//
//            Map<String, String> info = new HashMap<String, String>();
//            info.put("COD_ENTE", cod_ente);
//            info.put("COD_AOO", cod_aoo);
//
//            info.put(Constants.doc_pubblicazione_registro, "xxx");
//            info.put(Constants.doc_pubblicazione_anno, "2012");
//            info.put(Constants.doc_pubblicazione_data_fine, "2012-12-12");
//            info.put(Constants.doc_pubblicazione_data_inizio, "2012-12-12");
//            info.put(Constants.doc_pubblicazione_numero, "222");
//            info.put(Constants.doc_pubblicazione_oggetto, "oggetto");
//            info.put(Constants.doc_pubblicazione_pubblicato, "true");
//
//            getBL().pubblicaDocumento(getToken(), docid_1, info);
//            System.out.println("pubblica ok");
//            // assertTrue(true);
//
//        }
//        catch (Exception e) {
//
//            System.out.println(e.getMessage());
//            // assertTrue(false);
//        }
//
//    }
//
//    @Test
//    public void test1() {
//        testLogin();
//        docid_1 = "18278";
//        testGetProfileDocument();
//        testLogout();
//    }
//
    @Test
    public void testGetBL() throws DocerException {
        BusinessLogic bl = getBL();                    
        
    }
    
    @Test
    public void testLoginAdmin() {
        try {
            token = getBL().login("", userAdmin, userAdminPasword);
            System.out.println("admin - login effettuata");
            // assertTrue(true);
        }
        catch (DocerException e) {
            System.out.println(e.getMessage());
            // assertTrue(false);
        }
    }
    
    @Test
    public void testLogin() {
        try {
            token = getBL().login(ente, userAdmin, userAdminPasword);
            System.out.println("login effettuata");
            // assertTrue(true);
        }
        catch (DocerException e) {
            System.out.println(e.getMessage());
            // assertTrue(false);
        }
    }
//    
//    @Test
//    public void testLoginNormal() {
//        try {
//            tokenNormal = getBL().login(ente, normalUser, normalPassword);
//            System.out.println("login effettuata");
//            // assertTrue(true);
//        }
//        catch (DocerException e) {
//            System.out.println(e.getMessage());
//            // assertTrue(false);
//        }
//    }
//    
//    @Test
//    public void testGetEffectiveRights() {
//        try {
//            token = getBL().login("", "admin", "admin");
//
//            EnumACLRights rights = getBL().getUserRights(token, "17448", "admin");
//
//            System.out.println(rights);
//        }
//        catch (DocerException e) {
//            System.out.println(e.getMessage());
//            // assertTrue(false);
//        }
//        finally {
//            try {
//                getBL().logout(token);
//            }
//            catch (DocerException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
//    }
//
//   
//
//    @Test
//    public void testCreateAnagrafiche() throws DocerException {
//
//        token = getBL().login("", "admin", "admin");
//
//        // // ENTE
//        Map<String, String> info = new HashMap<String, String>();
//        // info.put("COD_ENTE", cod_ente);
//        // info.put("DES_ENTE", "des ENTE");
//        // info.put("ENABLED", "true");
//        // try {
//        // getBL().createEnte(getToken(), info);
//        // System.out.println("ente creato");
//        // }
//        // catch (Exception e) {
//        // System.out.println(e.getMessage());
//        // }
//
//        // creao AOO
//        info.clear();
//        info.put("COD_ENTE", "ENTE1");
//        info.put("COD_AOO", "AOO11");
//        info.put("DES_AOO", "des AOO 11");
//        info.put("ENABLED", "true");
//        try {
//            getBL().createAOO(getToken(), info);
//            System.out.println("aoo creato");
//            // assertTrue(true);
//        }
//        catch (Exception e) {
//            System.out.println(e.getMessage());
//            // assertTrue(false);
//        }
//
//        // // TiToLARIO
//        // info.clear();
//        // info.put("COD_ENTE", cod_ente);
//        // info.put("COD_AOO", cod_aoo);
//        // info.put("CLASSIFICA", "1.0.0");
//        // info.put("DES_TITOLARIO", "des 1.0.0");
//        // info.put("ENABLED", "true");
//        // try {
//        // getBL().createTitolario(getToken(), info);
//        // System.out.println("titolario 1.0.0 creato");
//        // // assertTrue(true);
//        // }
//        // catch (Exception e) {
//        // System.out.println(e.getMessage());
//        // // assertTrue(false);
//        // }
//        //
//        // // TIT2
//        // info.clear();
//        // info.put("COD_ENTE", cod_ente);
//        // info.put("COD_AOO", cod_aoo);
//        // info.put("PARENT_CLASSIFICA", "1.0.0");
//        // info.put("CLASSIFICA", "1.1.0");
//        // info.put("DES_TITOLARIO", "des 1.1.0");
//        // info.put("ENABLED", "true");
//        // try {
//        // getBL().createTitolario(getToken(), info);
//        // System.out.println("classifica 1.1.0 creato");
//        // // assertTrue(true);
//        // }
//        // catch (Exception e) {
//        // System.out.println(e.getMessage());
//        // // assertTrue(false);
//        // }
//        //
//        // // FASC
//        // info.clear();
//        // info.put("COD_ENTE", cod_ente);
//        // info.put("COD_AOO", cod_aoo);
//        // info.put("CLASSIFICA", "1.0.0");
//        // info.put("ANNO_FASCICOLO", "2012");
//        // info.put("PROGR_FASCICOLO", "1");
//        // info.put("DES_FASCICOLO", "des fascicolo 1");
//        // info.put("ENABLED", "true");
//        // try {
//        // getBL().createFascicolo(getToken(), info);
//        // System.out.println("fascicolo 1 creato");
//        // // assertTrue(true);
//        // }
//        // catch (Exception e) {
//        // System.out.println(e.getMessage());
//        // // assertTrue(false);
//        // }
//        //
//        // // FASC2
//        // info.clear();
//        // info.put("COD_ENTE", cod_ente);
//        // info.put("COD_AOO", cod_aoo);
//        // info.put("CLASSIFICA", "1.0.0");
//        // info.put("ANNO_FASCICOLO", "2012");
//        // info.put("PROGR_FASCICOLO", "1/1");
//        // info.put("DES_FASCICOLO", "des fascicolo 1/1");
//        // info.put("ENABLED", "true");
//        // try {
//        // getBL().createFascicolo(getToken(), info);
//        // System.out.println("fascicolo 1/1 creato");
//        // // assertTrue(true);
//        // }
//        // catch (Exception e) {
//        // System.out.println(e.getMessage());
//        // // assertTrue(false);
//        // }
//        //
//        // info.clear();
//        // info.put("COD_ENTE", cod_ente);
//        // info.put("COD_AOO", cod_aoo);
//        // info.put("CLASSIFICA", "1.1.0");
//        // info.put("ANNO_FASCICOLO", "2012");
//        // info.put("PROGR_FASCICOLO", "2");
//        // info.put("DES_FASCICOLO", "des fascicolo 2");
//        // info.put("ENABLED", "true");
//        // try {
//        // getBL().createFascicolo(token, info);
//        // System.out.println("fascicolo 2 creato");
//        // }
//        // catch (Exception e) {
//        // System.out.println(e.getMessage());
//        // }
//        // info.clear();
//        // info.put("COD_ENTE", cod_ente);
//        // info.put("COD_AOO", cod_aoo);
//        // info.put("CLASSIFICA", "1.1.0");
//        // info.put("ANNO_FASCICOLO", "2012");
//        // info.put("PROGR_FASCICOLO", "2/1/XXX");
//        // info.put("DES_FASCICOLO", "des fascicolo 2/1/XXX");
//        // info.put("ENABLED", "true");
//        //
//        // info.put("DATA_APERTURA", "2012-06-12T10:22:12.584+02:00");
//        // info.put("DATA_CHIUSURA", "2012-06-12T10:22:12.584+02:00");
//        // info.put("CF_PERSONA", "2012-06-12T10:22:12.584+02:00");
//        // info.put("CF_AZIENDA", "2012-06-12T10:22:12.584+02:00");
//        // info.put("ID_PROC", "2012060200");
//        // info.put("ID_IMMOBILE", "20121111111");
//        //
//        // try {
//        // getBL().createFascicolo(token, info);
//        // System.out.println("fascicolo 2/1/XXX creato");
//        // }
//        // catch (Exception e) {
//        // System.out.println(e.getMessage());
//        // }
//        // Map<String, String> id = new HashMap<String, String>();
//        //
//        // id.put("COD_ENTE", cod_ente);
//        // id.put("COD_AOO", cod_aoo);
//        // id.put("CLASSIFICA", "1.1.0");
//        // id.put("ANNO_FASCICOLO", "2012");
//        // id.put("PROGR_FASCICOLO", "2/1/XXX");
//        //
//        // try {
//        // info = getBL().getFascicolo(token, id);
//        // }
//        // catch (DocerException e1) {
//        // System.out.println("getFascicolo: " + e1.getMessage());
//        // }
//        // if (info == null) {
//        // System.out.println("fascicolo non trovato xxx");
//        // }
//        // else
//        // for (String key : info.keySet()) {
//        // System.out.println(key + " = " + info.get(key));
//        // }
//        //
//        // info.clear();
//        // info.put("TYPE_ID", "AREA_TEMATICA");
//        // info.put("COD_ENTE", cod_ente);
//        // info.put("COD_AOO", cod_aoo);
//        // info.put("COD_AREA", "AREA1");
//        // info.put("DES_AREA", "des AREA1");
//        // info.put("ENABLED", "true");
//        // try {
//        // getBL().createAnagraficaCustom(token, info);
//        // System.out.println("area 1 creato");
//        // }
//        // catch (Exception e) {
//        // System.out.println(e.getMessage());
//        // }
//        //
//        // info.clear();
//        // info.put("TYPE_ID", "AREA_TEMATICA");
//        // info.put("COD_ENTE", cod_ente);
//        // info.put("COD_AOO", cod_aoo);
//        // info.put("COD_AREA", "AREA2");
//        // info.put("DES_AREA", "des AREA2");
//        // info.put("ENABLED", "true");
//        // try {
//        // getBL().createAnagraficaCustom(token, info);
//        // System.out.println("area 2 creato");
//        // }
//        // catch (Exception e) {
//        // System.out.println(e.getMessage());
//        // }
//
//        // assertTrue(true);
//
//        // assertTrue(false);
//
//    }
//
//    //
//    // @Test
//    // public void testGetDocumentTypes() {
//    //
//    //
//    // Properties p = new Properties();
//    // try {
//    // p.loadFromXML(this.getClass().getResourceAsStream(
//    // "/docer_config.xml"));
//    //
//    // // appena viene invocato il WS recupero l'informazione del 'provider'
//    // // dal file 'config.xml'
//    // String providerName = p.getProperty("provider");
//    //
//    // BusinessLogic.setConfigPath(p.getProperty("CONFIG_DIR"));
//    //
//    // BusinessLogic bl = new BusinessLogic(providerName, 1000);
//    //
//    //
//    // String token = bl.login("Ente1", "admin", "admin");
//    //
//    // List<IKeyValuePair> list = bl.getDocumentTypes(token, "ENTE1", "AOO11");
//    // for(IKeyValuePair kvp : list){
//    // System.out.println(kvp.getKey() +" -> " +kvp.getValue());
//    // }
//    //
//    //
//    // bl.writeConfig(token, null);
//    //
//    // bl = new BusinessLogic(providerName, 1000);
//    // list = bl.getDocumentTypes(token, "ENTE1", "AOO11");
//    // for(IKeyValuePair kvp : list){
//    // System.out.println(kvp.getKey() +" -> " +kvp.getValue());
//    // }
//    // }
//    // catch(Exception e){
//    // System.out.println(e.getMessage());
//    // //assertTrue(false);
//    // }
//    //
//    // }
//
//    // @Test
//    // public void testBusinessLogic() {
//    //
//    //
//    // Properties p = new Properties();
//    // try {
//    // p.loadFromXML(this.getClass().getResourceAsStream(
//    // "/docer_config.xml"));
//    //
//    // // appena viene invocato il WS recupero l'informazione del 'provider'
//    // // dal file 'config.xml'
//    // String providerName = p.getProperty("provider");
//    //
//    // BusinessLogic.setConfigPath(p.getProperty("CONFIG_DIR"));
//    //
//    // BusinessLogic bl = new BusinessLogic(providerName, 1000);
//    //
//    // bl = new BusinessLogic(providerName, 1000);
//    // String token = bl.login("DOCAREA", "admin", "admin");
//    //
//    // bl.writeConfig(token, null);
//    // bl = new BusinessLogic(providerName, 1000);
//    // token = bl.login("DOCAREA", "admin", "admin");
//    // }
//    // catch(Exception e){
//    // System.out.println(e.getMessage());
//    // //assertTrue(false);
//    // }
//    //
//    // }
//
//    // @Test
//    // public void testCreateEnteeAOO(){
//    //
//    // String token = null;
//    // BusinessLogic bl = null;
//    // Properties p = new Properties();
//    // try {
//    // p.loadFromXML(this.getClass().getResourceAsStream(
//    // "/docer_config.xml"));
//    // }
//    // catch(Exception e){
//    // System.out.println(e.getMessage());
//    // return;
//    // }
//    //
//    // // appena viene invocato il WS recupero l'informazione del 'provider'
//    // // dal file 'config.xml'
//    // String providerName = p.getProperty("provider");
//    //
//    // BusinessLogic.setConfigPath(p.getProperty("CONFIG_DIR"));
//    //
//    // bl = new BusinessLogic(providerName, 1000);
//    // token = bl.login("ente1", "admin", "admin");
//    //
//    // // creo ENTE
//    // Map<String,String> info = new HashMap<String,String>();
//    // info.put("COD_ENTE", "ENTEX");
//    // info.put("DES_ENTE", "des ENTEX");
//    // info.put("ENABLED", "true");
//    // try{
//    // bl.createEnte(token, info);
//    // System.out.println("ente creato");
//    // }
//    // catch(Exception e){
//    // System.out.println(e.getMessage());
//    // }
//    //
//    //
//    //
//    // // creao AOO
//    // info.clear();
//    // info.put("COD_ENTE", "ENTEX");
//    // info.put("COD_AOO", "AOOX");
//    // info.put("DES_AOO", "des AOOX");
//    // info.put("ENABLED", "true");
//    // try{
//    // bl.createAOO(token, info);
//    // System.out.println("aoo creato");
//    // }
//    // catch(Exception e){
//    // System.out.println(e.getMessage());
//    // }
//    // }
//    //
//
//    @Test
//    public void testCreateTestUsers() {
//
//        List<String> users = new ArrayList<String>();
//        users.add("user1");
//        users.add("LUCA.BIASIN");
//        users.add("UT_PROTO_");
//        users.add("LUCA.BIASIN2");
//        
//        try {
//            token = getBL().login("", "admin","admin");
//        }
//        catch (DocerException e1) {
//            System.out.println(e1.getMessage());
//            return;
//        }
//        
//        Map<String, String> info = new HashMap<String, String>();
//        for(String uid : users){
//                        
//            info.put("USER_ID", uid);
//            info.put("FULL_NAME", uid);
//            info.put("COD_ENTE", "ENTE1");
//            info.put("COD_AOO", "AOO11");
//            
//            try {
//                getBL().createUser(getToken(), info);
//                System.out.println("utente " + uid + " creato");
//                // assertTrue(true);
//            }
//            catch (Exception e) {
//                System.out.println(e.getMessage());
//                // assertTrue(false);
//            }
//
//        }
//        
//        
//    }
//
//    
//    @Test
//    public void testCreateUsers() {
//
//        Map<String, String> info = new HashMap<String, String>();
//        info.put("USER_ID", user1);
//        info.put("FULL_NAME", user1);
//        info.put("COD_ENTE", cod_ente);
//        info.put("COD_AOO", cod_aoo);
//        try {
//            getBL().createUser(getToken(), info);
//            System.out.println("utente " + user1 + " creato");
//            // assertTrue(true);
//        }
//        catch (Exception e) {
//            System.out.println(e.getMessage());
//            // assertTrue(false);
//        }
//
//        info.clear();
//        info.put("USER_ID", user2);
//        info.put("FULL_NAME", user2);
//        info.put("COD_ENTE", cod_ente);
//        info.put("COD_AOO", cod_aoo);
//        try {
//            getBL().createUser(getToken(), info);
//            System.out.println("utente " + user2 + " creato");
//            // assertTrue(true);
//        }
//        catch (Exception e) {
//            System.out.println(e.getMessage());
//            // assertTrue(false);
//        }
//
//        info.clear();
//        info.put("USER_ID", user3);
//        info.put("FULL_NAME", user3);
//        info.put("COD_ENTE", cod_ente);
//        info.put("COD_AOO", cod_aoo);
//        try {
//            getBL().createUser(getToken(), info);
//            System.out.println("utente " + user3 + " creato");
//            // assertTrue(true);
//        }
//        catch (Exception e) {
//            System.out.println(e.getMessage());
//            // assertTrue(false);
//        }
//
//    }
//
//    @Test
//    public void testCreateGroups() {
//
//        Map<String, String> info = new HashMap<String, String>();
//        info.put("GROUP_ID", "group1");
//        info.put("GROUP_NAME", "group1");
//        info.put("PARENT_GROUP_ID", cod_aoo);
//        try {
//            getBL().createGroup(getToken(), info);
//            System.out.println("gruppo group1 creato");
//            // assertTrue(true);
//        }
//        catch (Exception e) {
//            System.out.println(e.getMessage());
//            // assertTrue(false);
//        }
//
//        info.clear();
//        info.put("GROUP_ID", "group2");
//        info.put("GROUP_NAME", "group2");
//        info.put("PARENT_GROUP_ID", cod_aoo);
//        try {
//            getBL().createGroup(getToken(), info);
//            System.out.println("gruppo group2 creato");
//            // assertTrue(true);
//        }
//        catch (Exception e) {
//            System.out.println(e.getMessage());
//            // assertTrue(false);
//        }
//
//        info.clear();
//        info.put("GROUP_ID", "group3");
//        info.put("GROUP_NAME", "group3");
//        info.put("PARENT_GROUP_ID", "group2");
//        try {
//            getBL().createGroup(getToken(), info);
//            System.out.println("gruppo group3 creato sotto group2");
//            // assertTrue(true);
//        }
//        catch (Exception e) {
//            System.out.println(e.getMessage());
//            // assertTrue(false);
//        }
//
//    }
//
//    
//    @Test
//    public void testUpdateUser_1() {
//
//        
//        try {
//            token = getBL().login("", "admin", "admin");
//            Map<String,String> info = new HashMap<String,String>();
//            
//            info.put("first_name", "luca");
//            info.put("full_name", "Luca Biasin");
//            getBL().updateUser(getToken(),"luca.biasin",info);
//
//
//                        
//        }
//        catch (Exception e) {
//            System.out.println(e.getMessage());
//            // assertTrue(false);
//        }
//
//    }
//
//    
//    @Test
//    public void testUpdateGroup_1() {
//
//        
//        try {
//            token = getBL().login("", "admin", "admin");
//            Map<String,String> info = new HashMap<String,String>();
//            
//            info.put("group_name", "GruPPo1");
//            getBL().updateGroup(getToken(),"GruPPo1",info);
//            
//            info = getBL().getGroup(getToken(),"GruPPo1");
//
//            for(String key : info.keySet()){
//                System.out.println(key +" -> " +String.valueOf(info.get(key)));
//            }
//
//                        
//        }
//        catch (Exception e) {
//            System.out.println(e.getMessage());
//            // assertTrue(false);
//        }
//
//    }
//    
//    @Test
//    public void testUpdateUsersOfGroup() {
//
//        
//        try {
//            token = getBL().login("", "admin", "admin");
//            //getBL().updateUsersOfGroup(getToken(), "Gruppo1", Arrays.asList("luca.biasin", "valeria.barile","simone.gigli"),Arrays.asList("luca.biasin", "valeria.barile","simone.gigli"));          
//            getBL().updateUsersOfGroup(getToken(), "Gruppo1", Arrays.asList("luca.biasin", "valeria.barile","simone.gigli"),null);
//            List<String> users = getBL().getUsersOfGroup(getToken(), "Gruppo1");
//
//            for(String id : users){
//                System.out.println(id);
//            }
//                        
//        }
//        catch (Exception e) {
//            System.out.println(e.getMessage());
//            // assertTrue(false);
//        }
//
//    }
//    
//    
//    @Test
//    public void testSetUsersOfGroup_1() {
//
//        
//        try {
//            token = getBL().login("", "admin", "admin");
//            getBL().setUsersOfGroup(getToken(), "Gruppo1", Arrays.asList("luca.biasin", "valeria.barile","simone.gigli"));          
//            List<String> users = getBL().getUsersOfGroup(getToken(), "Gruppo1");
//
//            for(String id : users){
//                System.out.println(id);
//            }
//                        
//        }
//        catch (Exception e) {
//            System.out.println(e.getMessage());
//            // assertTrue(false);
//        }
//
//    }
//    
//    
//    @Test
//    public void testRemoveAllUsersOfGroup_1() {
//
//        
//        try {
//            token = getBL().login("", "admin", "admin");
//            getBL().setUsersOfGroup(getToken(), "Gruppo1", Arrays.asList(""));          
//            List<String> users = getBL().getUsersOfGroup(getToken(), "Gruppo1");
//
//            for(String id : users){
//                System.out.println(id);
//            }
//                        
//        }
//        catch (Exception e) {
//            System.out.println(e.getMessage());
//            // assertTrue(false);
//        }
//
//    }
//    
//    @Test
//    public void testSetGroupsOfUser() {
//
//        
//        try {
//            token = getBL().login("", "admin", "admin");
//            getBL().setGroupsOfUser(getToken(), "valeria.barile", Arrays.asList("Gruppo1", "GRUPPO1","GruPPo1"));          
//            List<String> groups = getBL().getGroupsOfUser(getToken(), "valeria.barile");
//
//            for(String id : groups){
//                System.out.println(id);
//            }
//                        
//        }
//        catch (Exception e) {
//            System.out.println(e.getMessage());
//            // assertTrue(false);
//        }
//
//    }
//    
//    @Test
//    public void testRemoveAllGroupsOfUser() {
//
//        
//        try {
//            token = getBL().login("", "admin", "admin");
//            getBL().setGroupsOfUser(getToken(), "valeria.barile", Arrays.asList(""));          
//            List<String> groups = getBL().getGroupsOfUser(getToken(), "valeria.barile");
//
//            for(String id : groups){
//                System.out.println(id);
//            }
//                        
//        }
//        catch (Exception e) {
//            System.out.println(e.getMessage());
//            // assertTrue(false);
//        }
//
//    }
//
//    @Test
//    public void testSetUsersOfGroup() {
//
//        try {
//            getBL().setUsersOfGroup(getToken(), "group3", Arrays.asList(user1, user2, user3));
//            System.out.println("impostati " + user1 + ", " + user2 + ", " + user3 + " come utenti di  group3");
//            // assertTrue(true);
//
//        }
//        catch (Exception e) {
//            System.out.println(e.getMessage());
//            // assertTrue(false);
//        }
//
//    }
//
//    @Test
//    public void testCreateDoc() {
//
//        String filePath = "C:\\test.txt";
//
//        FileInputStream fileInputStream = null;
//
//        File file = new File(filePath);
//        try {
//            fileInputStream = new FileInputStream(file);
//        }
//        catch (FileNotFoundException e1) {
//
//            System.out.println(e1);
//            return;
//        }
//
//        try {
//
//            token = getBL().login("", "admin", "admin");
//            
//            Map<String, String> info = new HashMap<String, String>();
//            info.put("COD_ENTE", "ENTE_LUCA");
//            info.put("COD_AOO", "AOO_LUCA_TEST");
//            info.put("DOCNAME", "test.txt");
//            info.put("TYPE_ID", "documento");
//            //info.put("TIPO_COMPONENTE", "ANNESSO");
//            //info.put("FIRMATARIO", "<Segnatura><Intestazione><Oggetto>a</Oggetto></Intestazione></Segnatura>");
//            
//            String docnum = getBL().createDocument(getToken(), info, fileInputStream);
//            System.out.println("creato " + docnum);
//            return;
//            // System.out.println(docid_1);
//            // assertTrue(true);
//
//        }
//        catch (Exception e) {
//
//            System.out.println(e.getMessage());
//
//            if (fileInputStream != null)
//                try {
//                    fileInputStream.close();
//                }
//                catch (IOException e1) {
//                    // TODO Auto-generated catch block
//                    e1.printStackTrace();
//                }
//            // System.out.println(e.getMessage());
//            // assertTrue(false);
//            return;
//        }
//        finally{
//            try {
//                getBL().logout(token);
//            }
//            catch (DocerException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
//
//    }
//
//    
//    
//    @Test
//    public void testCreateDocUrl() {
//       
//        try {
//            token = getBL().login("", "admin", "admin");
//        }
//        catch (DocerException e1) {
//            System.out.println(e1.getMessage());
//            return;
//        }
//        
//        String docnum = "";
//        try {
//           
//            Map<String, String> info = new HashMap<String, String>();
//            info.put("COD_ENTE", "ENTE1");
//            info.put("COD_AOO", "AOO11");
//            info.put("DOCNAME", "test.txt");
//            info.put("TYPE_ID", "documento");
//            info.put("ARCHIVE_TYPE", "URL");
//            info.put("DOC_URL", "http://www.google.com/documento.txt");
//            info.put("DOCNUM", "11111111111");
//            //info.put("TIPO_COMPONENTE", "ANNESSO");
//            //info.put("FIRMATARIO", "<Segnatura><Intestazione><Oggetto>a</Oggetto></Intestazione></Segnatura>");
//            
//            docnum = getBL().createDocument(getToken(), info, null);
//            System.out.println("creato " + docnum);
//            
//            // System.out.println(docid_1);
//            // assertTrue(true);
//
//        }
//        catch (Exception e) {
//
//            System.out.println(e.getMessage());
//        }
//        
//        
//        try {
//                      
//            
//            String versionid = getBL().addNewVersion(getToken(), docnum, null);
//            System.out.println("creata versione " + versionid);
//            
//            // System.out.println(docid_1);
//            // assertTrue(true);
//
//        }
//        catch (Exception e) {
//
//            System.out.println("add new version" +e.getMessage());
//        }
//        
//        
//        try {
//                                 
//             getBL().replaceLastVersion(getToken(), docnum, null);
//            System.out.println("replace versione");
//            
//            // System.out.println(docid_1);
//            // assertTrue(true);
//
//        }
//        catch (Exception e) {
//
//            System.out.println("replace version" +e.getMessage());
//        }
//        
//        try {
//            byte[] file = getBL().downloadDocument(getToken(), docnum, "c:\\windows", 1000);
//            System.out.println("download document, file size: " + file.length);
//
//            // assertTrue(true);
//
//        }
//        catch (Exception e) {
//
//            System.out.println("download" +e.getMessage());
//            // assertTrue(false);
//        }
//
// 
//        try {
//            byte[] file = getBL().downloadVersion(getToken(), docnum, "1", "c:\\windows", 1000);
//            System.out.println("download document, file size: " + file.length);
//            // assertTrue(true);
//
//        }
//        catch (Exception e) {
//
//            System.out.println("downloadVersion " +e.getMessage());
//            // assertTrue(false);
//        }
//        
//        try {
//            getBL().logout(token);
//        }
//        catch (DocerException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    
//
//    }
//    
//    @Test
//    public void testAddRelated() {
//       
//        try {
//
//            token = getBL().login("", "admin", "admin");
//            
//           
//            getBL().addRelated(token, "1059843", Arrays.asList("1059846"));
//            System.out.println("add related 1059843" );
//            return;
//            // System.out.println(docid_1);
//            // assertTrue(true);
//
//        }
//        catch (Exception e) {
//
//            System.out.println(e.getMessage());
//
//          
//            return;
//        }
//        finally{
//            try {
//                getBL().logout(token);
//            }
//            catch (DocerException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
//
//    }
//    
//    
//    @Test
//    public void testSetACLMaster() {
//       
//        try {
//
//            token = getBL().login("", "admin", "admin");
//            
//            Map<String, EnumACLRights> acls = new HashMap<String, EnumACLRights>();
//            
//            getBL().setACLDocument(token, "1059843", acls);
//            System.out.println("set acls 1059843" );
//            return;
//            // System.out.println(docid_1);
//            // assertTrue(true);
//
//        }
//        catch (Exception e) {
//
//            System.out.println(e.getMessage());
//
//          
//            return;
//        }
//        finally{
//            try {
//                getBL().logout(token);
//            }
//            catch (DocerException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
//
//    }
//    
//    
//    @Test
//    public void testSetACLAnnesso() {
//       
//        try {
//
//            token = getBL().login("", "admin", "admin");
//            
//            Map<String, EnumACLRights> acls = new HashMap<String, EnumACLRights>();
//            
//            getBL().setACLDocument(token, "1059844", acls);
//            System.out.println("set acls 1059844" );
//            return;
//            // System.out.println(docid_1);
//            // assertTrue(true);
//
//        }
//        catch (Exception e) {
//
//            System.out.println(e.getMessage());
//
//          
//            return;
//        }
//        finally{
//            try {
//                getBL().logout(token);
//            }
//            catch (DocerException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
//
//    }
//    
//    @Test
//    public void testProto() {
//       
//        try {
//
//            token = getBL().login("", "admin", "admin");
//            
//            Map<String, String> info = new HashMap<String, String>();
//            info.put("COD_ENTE", "ente_luca");
//            info.put("COD_AOO", "aoo_luca_test");
//            info.put("DATA_PG", "2013-04-24");
//            info.put("REGISTRO_PG", "PG");
//            info.put("NUM_PG", "1111");
//            info.put("TIPO_PROTOCOLLAZIONE", "ND");
//            info.put("TIPO_FIRMA", "NF");
//            
//            getBL().protocollaDocumento(token, "1059843", info);
//            System.out.println("add related 1059843" );
//            return;
//            // System.out.println(docid_1);
//            // assertTrue(true);
//
//        }
//        catch (Exception e) {
//
//            System.out.println(e.getMessage());
//
//          
//            return;
//        }
//        finally{
//            try {
//                getBL().logout(token);
//            }
//            catch (DocerException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
//
//    }
//    
    @Test
    public void testCreateDocumentoFonte() {

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
            info.put("DOCNUM", "123123");
            info.put("COD_ENTE", "ente1");
            info.put("COD_AOO", "aoo11");
            info.put("DOCNAME", "bersani.txt");
            info.put("TYPE_ID", "documento");
            
//            info.put("CREATION_DATE", "2012-06-12T10:22:12.584+02:00");
//            info.put("N_REGISTRAZ", "1");
//            info.put("D_REGISTRAZ", "2012-06-12T10:22:12.584+02:00");
//            info.put("D_ANNULL_REGISTRAZ", "2012-06-12T10:22:12.584+02:00");
//            info.put("A_REGISTRAZ", "2012");
//            info.put("NUM_PG", "1");
//            info.put("DATA_PG", "2012-06-12T10:22:12.584+02:00");
//            info.put("ANNO_PG", "2012");
//            info.put("D_ANNULL_PG", "2012-06-12T10:22:12.584+02:00");
//            info.put("DATA_PG_MITTENTE", "2012-06-12T10:22:12.584+02:00");
//            info.put("ANNO_FASCICOLO", "2012");
//            info.put("NUMERO_PUB", "1");
//            info.put("DATA_INIZIO_PUB", "2012-06-12T10:22:12.584+02:00");
//            info.put("DATA_FINE_PUB", "2012-06-12T10:22:12.584+02:00");
//            info.put("ANNO_PUB", "2012");
//            info.put("D_CO_CER", "2012-06-12T10:22:12.584+02:00");


            token = getBL().login("", "admin", "admin");
            
            docid_1 = getBL().createDocument(getToken(), info, fileInputStream);
            System.out.println("create document 1 ok: " + docid_1);
                      

        }
        catch (Exception e) {

            System.out.println("create document 1 KO: " + e.getMessage());

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

//    @Test
//    public void testCreateDocument2() {
//
//        String filePath = "C:\\test.txt";
//
//        FileInputStream fileInputStream = null;
//
//        File file = new File(filePath);
//        try {
//            fileInputStream = new FileInputStream(file);
//        }
//        catch (FileNotFoundException e1) {
//
//            System.out.println(e1);
//            return;
//        }
//
//        try {
//
//            Map<String, String> info = new HashMap<String, String>();
//            info.put("COD_ENTE", cod_ente);
//            info.put("COD_AOO", cod_aoo);
//            info.put("DOCNAME", "test.txt");
//            info.put("TYPE_ID", "documento");
//
//            docid_2 = getBL().createDocument(getToken(), info, fileInputStream);
//            System.out.println("create document 2 ok " + docid_2);
//            // assertTrue(true);
//
//        }
//        catch (Exception e) {
//
//            System.out.println("create document 2 KO: " + e.getMessage());
//
//            if (fileInputStream != null)
//                try {
//                    fileInputStream.close();
//                }
//                catch (IOException e1) {
//                    // TODO Auto-generated catch block
//                    e1.printStackTrace();
//                }
//        }
//
//    }
//
//    @Test
//    public void testCreateDocument3() {
//
//        String filePath = "C:\\test.txt";
//
//        FileInputStream fileInputStream = null;
//
//        File file = new File(filePath);
//        try {
//            fileInputStream = new FileInputStream(file);
//        }
//        catch (FileNotFoundException e1) {
//
//            System.out.println(e1);
//            return;
//        }
//
//        try {
//
//            Map<String, String> info = new HashMap<String, String>();
//            info.put("COD_ENTE", cod_ente);
//            info.put("COD_AOO", cod_aoo);
//            info.put("DOCNAME", "test.txt");
//            info.put("TYPE_ID", "documento");
//
//            docid_3 = getBL().createDocument(getToken(), info, fileInputStream);
//            System.out.println("create document ok: " + docid_3);
//
//        }
//        catch (Exception e) {
//
//            if (fileInputStream != null)
//                try {
//                    fileInputStream.close();
//                }
//                catch (IOException e1) {
//                    // TODO Auto-generated catch block
//                    e1.printStackTrace();
//                }
//            System.out.println("create document KO: " + e.getMessage());
//            // assertTrue(false);
//        }
//
//    }
//
//    @Test
//    public void testCreateDocument4() {
//
//        String filePath = "C:\\test.txt";
//
//        FileInputStream fileInputStream = null;
//
//        File file = new File(filePath);
//        try {
//            fileInputStream = new FileInputStream(file);
//        }
//        catch (FileNotFoundException e1) {
//
//            System.out.println(e1);
//            return;
//        }
//
//        try {
//
//            Map<String, String> info = new HashMap<String, String>();
//            info.put("COD_ENTE", cod_ente);
//            info.put("COD_AOO", cod_aoo);
//            info.put("DOCNAME", "test.txt");
//            info.put("TYPE_ID", "documento");
//
//            docid_4 = getBL().createDocument(getToken(), info, fileInputStream);
//            System.out.println("create document ok: " + docid_4);
//
//        }
//        catch (Exception e) {
//
//            if (fileInputStream != null)
//                try {
//                    fileInputStream.close();
//                }
//                catch (IOException e1) {
//                    // TODO Auto-generated catch block
//                    e1.printStackTrace();
//                }
//            System.out.println("create document KO: " + e.getMessage());
//            // assertTrue(false);
//        }
//
//    }
//
//    @Test
//    public void testCreateDocument5() {
//
//        String filePath = "C:\\test.txt";
//
//        FileInputStream fileInputStream = null;
//
//        File file = new File(filePath);
//        try {
//            fileInputStream = new FileInputStream(file);
//        }
//        catch (FileNotFoundException e1) {
//
//            System.out.println(e1);
//            return;
//        }
//
//        try {
//
//            Map<String, String> info = new HashMap<String, String>();
//            info.put("COD_ENTE", cod_ente);
//            info.put("COD_AOO", cod_aoo);
//            info.put("DOCNAME", "test.txt");
//            info.put("TYPE_ID", "documento");
//
//            docid_5 = getBL().createDocument(getToken(), info, fileInputStream);
//            System.out.println("create document ok: " + docid_5);
//
//        }
//        catch (Exception e) {
//
//            if (fileInputStream != null)
//                try {
//                    fileInputStream.close();
//                }
//                catch (IOException e1) {
//                    // TODO Auto-generated catch block
//                    e1.printStackTrace();
//                }
//            System.out.println("create document KO: " + e.getMessage());
//            // assertTrue(false);
//        }
//
//    }
//
//    @Test
//    public void testCreateDocument6() {
//
//        String filePath = "C:\\test.txt";
//
//        FileInputStream fileInputStream = null;
//
//        File file = new File(filePath);
//        try {
//            fileInputStream = new FileInputStream(file);
//        }
//        catch (FileNotFoundException e1) {
//
//            System.out.println(e1);
//            return;
//        }
//
//        try {
//
//            Map<String, String> info = new HashMap<String, String>();
//            info.put("COD_ENTE", cod_ente);
//            info.put("COD_AOO", cod_aoo);
//            info.put("DOCNAME", "test.txt");
//            info.put("TYPE_ID", "documento");
//
//            docid_6 = getBL().createDocument(getToken(), info, fileInputStream);
//            System.out.println("create document ok: " + docid_6);
//
//        }
//        catch (Exception e) {
//
//            if (fileInputStream != null)
//                try {
//                    fileInputStream.close();
//                }
//                catch (IOException e1) {
//                    // TODO Auto-generated catch block
//                    e1.printStackTrace();
//                }
//            System.out.println("create document KO: " + e.getMessage());
//            // assertTrue(false);
//        }
//
//    }
//
//    @Test
//    public void testFolder() {
//
//        String fid1 = "";
//        String fid2 = "";
//        String fid3 = "";
//        String fid4 = "";
//
//        String folderid = "61648";
//
//        Map<String, String> profile = new HashMap<String, String>();
//        try {
//
//            profile.put("FOLDER_NAME", "folder pubblica 1");
//            profile.put("PARENT_FOLDER_ID", folderid);
//            profile.put("DES_FOLDER", "des folder pubblica 1");
//            profile.put("FOLDER_OWNER", "");
//            try {
//                fid1 = getBL().createFolder(getToken(), profile);
//            }
//            catch (DocerException e) {
//                System.out.println("folder step 1 KO: " + e.getMessage());
//            }
//            profile.put("FOLDER_NAME", "folder pubblica 1.1");
//            profile.put("PARENT_FOLDER_ID", fid1);
//            profile.put("DES_FOLDER", "des folder pubblica 1.1");
//            profile.put("FOLDER_OWNER", "");
//            try {
//                fid2 = getBL().createFolder(getToken(), profile);
//            }
//            catch (DocerException e) {
//                System.out.println("folder step 2 KO: " + e.getMessage());
//            }
//
//            profile.put("FOLDER_NAME", "folder privata 1");
//            profile.put("PARENT_FOLDER_ID", folderid);
//            profile.put("DES_FOLDER", "des folder privata 1");
//            profile.put("FOLDER_OWNER", userAdmin);
//            try {
//                fid3 = getBL().createFolder(getToken(), profile);
//            }
//            catch (DocerException e) {
//                System.out.println("folder step 3 KO: " + e.getMessage());
//            }
//
//            profile.put("FOLDER_NAME", "folder privata 1.1");
//            profile.put("PARENT_FOLDER_ID", fid3);
//            profile.put("DES_FOLDER", "des folder privata 1.1");
//            profile.put("FOLDER_OWNER", userAdmin);
//            try {
//                fid4 = getBL().createFolder(getToken(), profile);
//            }
//            catch (DocerException e) {
//                System.out.println("folder step 4 KO: " + e.getMessage());
//            }
//
//            try {
//                getBL().addToFolderDocuments(getToken(), fid1, Arrays.asList(docid_1));
//            }
//            catch (DocerException e) {
//                System.out.println("folder step 5 KO: " + e.getMessage());
//            }
//            try {
//                getBL().addToFolderDocuments(getToken(), fid2, Arrays.asList(docid_2));
//            }
//            catch (DocerException e) {
//                System.out.println("folder step 6 KO: " + e.getMessage());
//            }
//            try {
//                getBL().addToFolderDocuments(getToken(), fid3, Arrays.asList(docid_3));
//            }
//            catch (DocerException e) {
//                System.out.println("folder step 7 KO: " + e.getMessage());
//            }
//            try {
//                getBL().addToFolderDocuments(getToken(), fid4, Arrays.asList(docid_4));
//            }
//            catch (DocerException e) {
//                System.out.println("folder step 8 KO: " + e.getMessage());
//            }
//
//            try {
//                getBL().removeFromFolderDocuments(getToken(), fid1, Arrays.asList(docid_1));
//            }
//            catch (DocerException e) {
//                System.out.println("folder step 9 KO: " + e.getMessage());
//            }
//            try {
//                getBL().removeFromFolderDocuments(getToken(), fid2, Arrays.asList(docid_2));
//            }
//            catch (DocerException e) {
//                System.out.println("folder step 10 KO: " + e.getMessage());
//            }
//            try {
//                getBL().removeFromFolderDocuments(getToken(), fid3, Arrays.asList(docid_3));
//            }
//            catch (DocerException e) {
//                System.out.println("folder step 11 KO: " + e.getMessage());
//            }
//            try {
//                getBL().removeFromFolderDocuments(getToken(), fid4, Arrays.asList(docid_4));
//            }
//            catch (DocerException e) {
//                System.out.println("folder step 12 KO: " + e.getMessage());
//            }
//
//            System.out.println("foldering END");
//
//        }
//        catch (Exception e) {
//
//            System.out.println("Folder KO: " + e.getMessage());
//
//        }
//        finally {
//            List<String> rifs = Arrays.asList(fid4, fid3, fid2, fid1);
//            List<String> docs = Arrays.asList(docid_1, docid_2, docid_3, docid_4);
//
//            for (String key : rifs) {
//
//                for (String doc : docs) {
//                    try {
//                        getBL().removeFromFolderDocuments(getToken(), key, Arrays.asList(doc));
//                    }
//                    catch (DocerException e1) {
//                        System.out.println("removeFromFolderDocuments: " + e1.getMessage());
//                    }
//                }
//
//                try {
//                    getBL().deleteFolder(getToken(), key);
//                }
//                catch (DocerException e1) {
//                    System.out.println("deleteFolder: " + e1.getMessage());
//                }
//            }
//
//            System.out.println("folder ripristinate");
//        }
//    }
//
//    @Test
//    public void testRelated() {
//
//        try {
//
//            List<String> list = Arrays.asList("77903");
//
//            // Map<String, String> metadata = new HashMap<String, String>();
//            // metadata.put("TIPO_COMPONENTE", "");
//            // for (String docid : list) {
//            //
//            // getBL().updateProfileDocument(getToken(), docid, metadata);
//            // }
//            token = getBL().login("", "admin", "admin");
//            
//            getBL().addRelated(getToken(), "77921", list);
//
//            List<String> res = getBL().getRelatedDocuments(getToken(), docid_1);
//            if (res.size() != 2 || !res.contains(docid_2) || !res.contains(docid_3)) {
//                System.out.println("related step 1 KO");
//                return;
//            }
//            res = getBL().getRelatedDocuments(getToken(), docid_2);
//            if (res.size() != 2 || !res.contains(docid_1) || !res.contains(docid_3)) {
//                System.out.println("related step 2 KO");
//                return;
//            }
//            res = getBL().getRelatedDocuments(getToken(), docid_3);
//            if (res.size() != 2 || !res.contains(docid_1) || !res.contains(docid_2)) {
//                System.out.println("related step 3 KO");
//                return;
//            }
//
//            return;
//
//            // getBL().removeRelated(getToken(), docid_1,
//            // Arrays.asList(docid_2));
//            // res = getBL().getRelatedDocuments(getToken(), docid_1);
//            // if (res.size() != 1 || !res.contains(docid_3)) {
//            // System.out.println("related step 4 KO");
//            // return;
//            // }
//            //
//            // getBL().addRelated(getToken(), docid_1, Arrays.asList(docid_2,
//            // docid_3));
//            // getBL().addRelated(getToken(), docid_4, Arrays.asList(docid_5,
//            // docid_6));
//            // getBL().addRelated(getToken(), docid_3, Arrays.asList(docid_2,
//            // docid_3));
//            // getBL().addRelated(getToken(), docid_5, Arrays.asList(docid_5,
//            // docid_6));
//            // // completo chain
//            // getBL().addRelated(getToken(), docid_2, Arrays.asList(docid_5,
//            // docid_6));
//            //
//            // res = getBL().getRelatedDocuments(getToken(), docid_1);
//            // if (res.size() != 5 || !res.contains(docid_2) ||
//            // !res.contains(docid_3) || !res.contains(docid_4) ||
//            // !res.contains(docid_5) || !res.contains(docid_6)) {
//            // System.out.println("related step 5 KO");
//            // return;
//            // }
//            // res = getBL().getRelatedDocuments(getToken(), docid_2);
//            // if (res.size() != 5 || !res.contains(docid_1) ||
//            // !res.contains(docid_3) || !res.contains(docid_4) ||
//            // !res.contains(docid_5) || !res.contains(docid_6)) {
//            // System.out.println("related step 6 KO");
//            // return;
//            // }
//            // res = getBL().getRelatedDocuments(getToken(), docid_3);
//            // if (res.size() != 5 || !res.contains(docid_1) ||
//            // !res.contains(docid_2) || !res.contains(docid_4) ||
//            // !res.contains(docid_5) || !res.contains(docid_6)) {
//            // System.out.println("related step 7 KO");
//            // return;
//            // }
//            // res = getBL().getRelatedDocuments(getToken(), docid_4);
//            // if (res.size() != 5 || !res.contains(docid_1) ||
//            // !res.contains(docid_2) || !res.contains(docid_3) ||
//            // !res.contains(docid_5) || !res.contains(docid_6)) {
//            // System.out.println("related step 8 KO");
//            // return;
//            // }
//            // res = getBL().getRelatedDocuments(getToken(), docid_5);
//            // if (res.size() != 5 || !res.contains(docid_1) ||
//            // !res.contains(docid_2) || !res.contains(docid_3) ||
//            // !res.contains(docid_4) || !res.contains(docid_6)) {
//            // System.out.println("related step 9 KO");
//            // return;
//            // }
//            // res = getBL().getRelatedDocuments(getToken(), docid_6);
//            // if (res.size() != 5 || !res.contains(docid_1) ||
//            // !res.contains(docid_2) || !res.contains(docid_3) ||
//            // !res.contains(docid_4) || !res.contains(docid_5)) {
//            // System.out.println("related step 9 KO");
//            // return;
//            // }
//            //
//            // // clean
//            // getBL().removeRelated(getToken(), docid_3, Arrays.asList(docid_4,
//            // docid_5));
//            // res = getBL().getRelatedDocuments(getToken(), docid_3);
//            // if (res.size() != 3 || !res.contains(docid_1) ||
//            // !res.contains(docid_2) || !res.contains(docid_6)) {
//            // System.out.println("related step 10 KO");
//            // return;
//            // }
//            //
//            // getBL().removeRelated(getToken(), docid_1, Arrays.asList(docid_2,
//            // docid_3, docid_6));
//            // res = getBL().getRelatedDocuments(getToken(), docid_1);
//            // if (res.size() != 0) {
//            // System.out.println("related step 11 KO");
//            // return;
//            // }
//
//            // System.out.println("related ok");
//
//        }
//        catch (Exception e) {
//
//            System.out.println("add related KO: " + e.getMessage());
//            // assertTrue(false);
//        }
//        finally {
//
//            // List<String> rifs = Arrays.asList(docid_1, docid_2, docid_3,
//            // docid_4, docid_5, docid_6);
//            //
//            // for (String key1 : rifs) {
//            // for (String key2 : rifs) {
//            // try {
//            // getBL().removeRelated(getToken(), key1, Arrays.asList(key2));
//            // }
//            // catch (DocerException e1) {
//            // System.out.println("removeRelated: " + e1.getMessage());
//            // }
//            // }
//            // }
//            // System.out.println("eliminazione delle correlazioni ok");
//        }
//    }
//
//    
//    @Test
//    public void testReplaceLastVersion() {
//
//        try {
//
//            List<String> list = Arrays.asList(docid_1, docid_2, docid_3, docid_4, docid_5, docid_6);
//
//            try{
//                getBL().lockDocument(getToken(),docid_1);
//                System.out.println("lockDocument ok");
//            }
//            catch(DocerException e){
//                System.out.println("errore lockDocument " +e.getMessage());
//            } 
//            
//            String filePath = "C:\\test2.txt";
//
//            FileInputStream fileInputStream = null;
//
//            File file = new File(filePath);
//            try {
//                fileInputStream = new FileInputStream(file);
//            }
//            catch (FileNotFoundException e1) {
//
//                System.out.println(e1);
//                return;
//            }
//            
//            try{
//                getBL().replaceLastVersion(getToken(), docid_1, fileInputStream );
//                System.out.println("replaceLastVersion ok");
//            }
//            catch(DocerException e){
//                System.out.println("errore replaceLastVersion " +e.getMessage());
//            }
//            finally{
//                fileInputStream.close();
//            }
//                        
//
//            try {
//                fileInputStream = new FileInputStream(file);
//            }
//            catch (FileNotFoundException e1) {
//
//                System.out.println(e1);
//                return;
//            }
//
//            try{
//                getBL().replaceLastVersion(getTokenNormal(), docid_1, fileInputStream );
//                System.out.println("replaceLastVersion 2 ok");
//            }
//            catch(DocerException e){
//                System.out.println("errore replaceLastVersion 2 " +e.getMessage());
//            }
//            finally{
//                fileInputStream.close();
//            }
//            
//            
//            try{
//                getBL().unlockDocument(getToken(), docid_1);
//                System.out.println("unlockDocument ok");
//            }
//            catch(DocerException e){
//                System.out.println("errore unlockDocument " +e.getMessage());
//            }            
//            
//        }
//        catch (Exception e) {
//
//            System.out.println("testReplaceLastVersion  KO: " + e.getMessage());
//            // assertTrue(false);
//        }
//
//    }
//    
//    @Test
//    public void testAdvancedVersion() {
//
//        try {
//
//            List<String> list = Arrays.asList(docid_1, docid_2, docid_3, docid_4, docid_5, docid_6);
//
//            Map<String, String> metadata = new HashMap<String, String>();
//            metadata.put("TIPO_COMPONENTE", "PRINCIPALE");
//            for (String docid : list) {
//
//                getBL().updateProfileDocument(getToken(), docid, metadata);
//            }
//
//            getBL().addNewAdvancedVersion(getToken(), docid_1, docid_2);
//            List<String> res = getBL().getAdvancedVersions(getToken(), docid_1);
//            if (res.size() != 2 || !res.contains(docid_1) || !res.contains(docid_2)) {
//                System.out.println("advanced versions step 1 KO");
//                return;
//            }
//            res = getBL().getAdvancedVersions(getToken(), docid_2);
//            if (res.size() != 2 || !res.contains(docid_1) || !res.contains(docid_2)) {
//                System.out.println("advanced versions step 2 KO");
//                return;
//            }
//            getBL().addNewAdvancedVersion(getToken(), docid_2, docid_3);
//            res = getBL().getAdvancedVersions(getToken(), docid_1);
//            if (res.size() != 3 || !res.contains(docid_1) || !res.contains(docid_2) || !res.contains(docid_3)) {
//                System.out.println("advanced versions step 3 KO");
//                return;
//            }
//            res = getBL().getAdvancedVersions(getToken(), docid_2);
//            if (res.size() != 3 || !res.contains(docid_1) || !res.contains(docid_2) || !res.contains(docid_3)) {
//                System.out.println("advanced versions step 4 KO");
//                return;
//            }
//            res = getBL().getAdvancedVersions(getToken(), docid_3);
//            if (res.size() != 3 || !res.contains(docid_1) || !res.contains(docid_2) || !res.contains(docid_3)) {
//                System.out.println("advanced versions step 5 KO");
//                return;
//            }
//
//            getBL().addNewAdvancedVersion(getToken(), docid_4, docid_5);
//            getBL().addNewAdvancedVersion(getToken(), docid_5, docid_6);
//            getBL().addNewAdvancedVersion(getToken(), docid_1, docid_6);
//
//            res = getBL().getAdvancedVersions(getToken(), docid_1);
//            if (res.size() != 6 || !res.contains(docid_1) || !res.contains(docid_2) || !res.contains(docid_3) || !res.contains(docid_4) || !res.contains(docid_5) || !res.contains(docid_6)) {
//                System.out.println("advanced versions  step 6 KO");
//                return;
//            }
//            res = getBL().getAdvancedVersions(getToken(), docid_2);
//            if (res.size() != 6 || !res.contains(docid_1) || !res.contains(docid_2) || !res.contains(docid_3) || !res.contains(docid_4) || !res.contains(docid_5) || !res.contains(docid_6)) {
//                System.out.println("advanced versions  step 7 KO");
//                return;
//            }
//            res = getBL().getAdvancedVersions(getToken(), docid_3);
//            if (res.size() != 6 || !res.contains(docid_1) || !res.contains(docid_2) || !res.contains(docid_3) || !res.contains(docid_4) || !res.contains(docid_5) || !res.contains(docid_6)) {
//                System.out.println("advanced versions  step 8 KO");
//                return;
//            }
//            res = getBL().getAdvancedVersions(getToken(), docid_4);
//            if (res.size() != 6 || !res.contains(docid_1) || !res.contains(docid_2) || !res.contains(docid_3) || !res.contains(docid_4) || !res.contains(docid_5) || !res.contains(docid_6)) {
//                System.out.println("advanced versions  step 9 KO");
//                return;
//            }
//            res = getBL().getAdvancedVersions(getToken(), docid_5);
//            if (res.size() != 6 || !res.contains(docid_1) || !res.contains(docid_2) || !res.contains(docid_3) || !res.contains(docid_4) || !res.contains(docid_5) || !res.contains(docid_6)) {
//                System.out.println("advanced versions  step 10 KO");
//                return;
//            }
//            res = getBL().getAdvancedVersions(getToken(), docid_6);
//            if (res.size() != 6 || !res.contains(docid_1) || !res.contains(docid_2) || !res.contains(docid_3) || !res.contains(docid_4) || !res.contains(docid_5) || !res.contains(docid_6)) {
//                System.out.println("advanced versions  step 11 KO");
//                return;
//            }
//
//            System.out.println("advanced versions ok");
//
//        }
//        catch (Exception e) {
//
//            System.out.println("advanced versions  KO: " + e.getMessage());
//            // assertTrue(false);
//        }
//
//    }
//
//    @Test
//    public void testRiferimenti() {
//
//        try {
//
//            // ADD
//            getBL().addRiferimentiDocuments(getToken(), docid_1, Arrays.asList(docid_2, docid_3));
//
//            List<String> res = getBL().getRiferimentiDocuments(getToken(), docid_1);
//            if (res.size() != 2 || !res.contains(docid_2) || !res.contains(docid_3)) {
//                System.out.println("riferimenti step 1.1 KO");
//                return;
//            }
//
//            res = getBL().getRiferimentiDocuments(getToken(), docid_2);
//            if (res.size() != 1 || !res.contains(docid_1)) {
//                System.out.println("riferimenti step 1.2 KO");
//                return;
//            }
//
//            res = getBL().getRiferimentiDocuments(getToken(), docid_3);
//            if (res.size() != 1 || !res.contains(docid_1)) {
//                System.out.println("riferimenti step 1.3 KO");
//                return;
//            }
//
//            // ADD
//            getBL().addRiferimentiDocuments(getToken(), docid_3, Arrays.asList(docid_4));
//
//            res = getBL().getRiferimentiDocuments(getToken(), docid_3);
//            if (res.size() != 2 || !res.contains(docid_1) || !res.contains(docid_4)) {
//                System.out.println("riferimenti step 2.0 KO");
//                return;
//            }
//
//            res = getBL().getRiferimentiDocuments(getToken(), docid_4);
//            if (res.size() != 1 || !res.contains(docid_3)) {
//                System.out.println("riferimenti step 2.1 KO");
//                return;
//            }
//
//            res = getBL().getRiferimentiDocuments(getToken(), docid_1);
//            if (res.size() != 2 || !res.contains(docid_2) || !res.contains(docid_3)) {
//                System.out.println("riferimenti step 2.3 KO");
//                return;
//            }
//
//            res = getBL().getRiferimentiDocuments(getToken(), docid_2);
//            if (res.size() != 1 || !res.contains(docid_1)) {
//                System.out.println("riferimenti step 2.4 KO");
//                return;
//            }
//
//            // ADD
//            getBL().addRiferimentiDocuments(getToken(), docid_2, Arrays.asList(docid_4, docid_5, docid_6));
//
//            res = getBL().getRiferimentiDocuments(getToken(), docid_1);
//            if (res.size() != 2 || !res.contains(docid_2) || !res.contains(docid_3)) {
//                System.out.println("riferimenti step 3.0 KO");
//                return;
//            }
//
//            res = getBL().getRiferimentiDocuments(getToken(), docid_2);
//            if (res.size() != 4 || !res.contains(docid_1) || !res.contains(docid_4) || !res.contains(docid_5) || !res.contains(docid_6)) {
//                System.out.println("riferimenti step 3.1 KO");
//                return;
//            }
//
//            res = getBL().getRiferimentiDocuments(getToken(), docid_3);
//            if (res.size() != 2 || !res.contains(docid_1) || !res.contains(docid_4)) {
//                System.out.println("riferimenti step 3.2 KO");
//                return;
//            }
//
//            res = getBL().getRiferimentiDocuments(getToken(), docid_4);
//            if (res.size() != 2 || !res.contains(docid_3) || !res.contains(docid_2)) {
//                System.out.println("riferimenti step 3.3 KO");
//                return;
//            }
//
//            res = getBL().getRiferimentiDocuments(getToken(), docid_5);
//            if (res.size() != 1 || !res.contains(docid_2)) {
//                System.out.println("riferimenti step 3.4 KO");
//                return;
//            }
//
//            res = getBL().getRiferimentiDocuments(getToken(), docid_6);
//            if (res.size() != 1 || !res.contains(docid_2)) {
//                System.out.println("riferimenti step 3.5 KO");
//                return;
//            }
//
//            System.out.println("rifeirmenti ok");
//
//        }
//        catch (Exception e) {
//
//            System.out.println("riferimenti  KO: " + e.getMessage());
//            // assertTrue(false);
//        }
//        finally {
//            List<String> rifs = Arrays.asList(docid_1, docid_2, docid_3, docid_4, docid_5, docid_6);
//
//            for (String key1 : rifs) {
//                for (String key2 : rifs) {
//                    try {
//                        getBL().removeRiferimentiDocuments(getToken(), key1, Arrays.asList(key2));
//                    }
//                    catch (DocerException e1) {
//                        System.out.println("delete: " + e1.getMessage());
//                    }
//                }
//            }
//
//            System.out.println("eliminazione riferimenti effettuatua");
//        }
//
//    }
//
//    @Test
//    public void testCreateVersion2() {
//
//        String filePath = "C:\\test2.txt";
//
//        FileInputStream fileInputStream = null;
//
//        File file = new File(filePath);
//        try {
//            fileInputStream = new FileInputStream(file);
//        }
//        catch (FileNotFoundException e1) {
//
//            System.out.println(e1);
//            return;
//        }
//
//        try {
//            getBL().addNewVersion(getToken(), docid_1, fileInputStream);
//            System.out.println("create version2 ok");
//            // assertTrue(true);
//
//        }
//        catch (Exception e) {
//
//            System.out.println("create version2 KO: " + e.getMessage());
//
//            if (fileInputStream != null)
//                try {
//                    fileInputStream.close();
//                }
//                catch (IOException e1) {
//                    // TODO Auto-generated catch block
//                    e1.printStackTrace();
//                }
//        }
//
//    }
//
//    @Test
//    public void testCreateVersion3() {
//
//        String filePath = "C:\\test3.txt";
//
//        FileInputStream fileInputStream = null;
//
//        File file = new File(filePath);
//        try {
//            fileInputStream = new FileInputStream(file);
//        }
//        catch (FileNotFoundException e1) {
//
//            System.out.println(e1);
//            return;
//        }
//
//        try {
//            getBL().addNewVersion(getToken(), docid_1, fileInputStream);
//            System.out.println("create version3 ok");
//            // assertTrue(true);
//
//        }
//        catch (Exception e) {
//
//            if (fileInputStream != null)
//                try {
//                    fileInputStream.close();
//                }
//                catch (IOException e1) {
//                    // TODO Auto-generated catch block
//                    e1.printStackTrace();
//                }
//            System.out.println("create version3 KO: " + e.getMessage());
//            // assertTrue(false);
//        }
//
//    }
//
//    @Test
//    public void testGetVersions() {
//
//        try {
//            List<String> versions = getBL().getVersions(getToken(), docid_1);
//            for (String v : versions) {
//                System.out.println("versione: " + v);
//            }
//
//        }
//        catch (Exception e) {
//
//            System.out.println(e.getMessage());
//            // assertTrue(false);
//        }
//
//    }
//
//    @Test
//    public void testLockDocument() {
//
//        try {
//            getBL().lockDocument(getToken(), docid_1);
//            System.out.println("lock ok");
//            // assertTrue(true);
//
//        }
//        catch (Exception e) {
//
//            System.out.println("lock KO: " + e.getMessage());
//            // assertTrue(false);
//        }
//
//    }
//
//    @Test
//    public void testUnlockDocument() {
//
//        try {
//
//            getBL().unlockDocument(getToken(), docid_1);
//            System.out.println("unock ok");
//            // assertTrue(true);
//
//        }
//        catch (Exception e) {
//
//            System.out.println(e.getMessage());
//            // assertTrue(false);
//        }
//
//    }
//
//    // @Test
//    // public void testGetAdvancedVersions(){
//    //
//    // String token = null;
//    // BusinessLogic bl = null;
//    // Properties p = new Properties();
//    //
//    // try {
//    // p.loadFromXML(this.getClass().getResourceAsStream(
//    // "/docer_config.xml"));
//    //
//    // // appena viene invocato il WS recupero l'informazione del 'provider'
//    // // dal file 'config.xml'
//    // String providerName = p.getProperty("provider");
//    //
//    // BusinessLogic.setConfigPath(p.getProperty("CONFIG_DIR"));
//    //
//    // bl = new BusinessLogic(providerName, 1000);
//    // token = bl.login("ente1", "admin", "admin");
//    //
//    // List<String> av = bl.getAdvancedVersions(token, "1422");
//    // for(String id : av){
//    // System.out.println(id);
//    // }
//    // //assertTrue(true);
//    //
//    // }
//    // catch(Exception e){
//    // if(bl!=null && token!=null)
//    // bl.logout(token);
//    //
//    // System.out.println(e.getMessage());
//    // //assertTrue(false);
//    // }
//    //
//    // }
//    //
//    // @Test
//    // public void testAddNewAdvancedVersion(){
//    //
//    // String token = null;
//    // BusinessLogic bl = null;
//    // Properties p = new Properties();
//    //
//    // try {
//    // p.loadFromXML(this.getClass().getResourceAsStream(
//    // "/docer_config.xml"));
//    //
//    // // appena viene invocato il WS recupero l'informazione del 'provider'
//    // // dal file 'config.xml'
//    // String providerName = p.getProperty("provider");
//    //
//    // BusinessLogic.setConfigPath(p.getProperty("CONFIG_DIR"));
//    //
//    // bl = new BusinessLogic(providerName, 1000);
//    // token = bl.login("ente1", "admin", "admin");
//    //
//    // bl.addNewAdvancedVersion(token, "1421", "1423");
//    // //assertTrue(true);
//    //
//    // }
//    // catch(Exception e){
//    // if(bl!=null && token!=null)
//    // bl.logout(token);
//    //
//    // System.out.println(e.getMessage());
//    // //assertTrue(false);
//    // }
//    //
//    // }
//
//    @Test
//    public void testUpdateProfileDocument() {
//
//        try {
//
//            String docid = "122074";
//
//            token = getBL().login("", "admin", "admin");
//            
//            Map<String, String> info = new HashMap<String, String>();
//            info.put("COD_ENTE", "EMR");
//            info.put("COD_AOO", "AOO_EMR");
//            info.put("CLASSIFICA", "100.50.20");
//            info.put("ANNO_FASCICOLO", "2009");
//            info.put("PROGR_FASCICOLO", "1");
//            // info.put("DATA_PG", "2012-06-12T10:22:12.584+02:00");
//            // info.put("D_REGISTRAZ", "2012-06-12T10:22:12.584+02:00");
//            // info.put("DATA_INIZIO_PUB", "2012-06-12T10:22:12.584+02:00");
//
//            getBL().fascicolaDocumento(getToken(), docid, info);
//            System.out.println("update ok");
//            // assertTrue(true);
//
//        }
//        catch (Exception e) {
//
//            System.out.println(e.getMessage());
//            // assertTrue(false);
//        }
//        finally{
//            try {
//                getBL().logout(getToken());
//            }
//            catch (DocerException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
//
//    }
//
//    @Test
//    public void testSetACLDocumento() {
//
//        
//        try {
//
//            token = getBL().login("", "admin", "admin");
//            
//            Map<String, EnumACLRights> acls = new HashMap<String, EnumACLRights>();
//            acls.put("luca.biasin", EnumACLRights.fullAccess);
//            acls.put("valeria.barile", EnumACLRights.readOnly);
//            acls.put("simone.gigli", EnumACLRights.readOnly);
//
//            acls.put("Gruppo1", EnumACLRights.normalAccess);
//            acls.put("GruPPo1", EnumACLRights.normalAccess);
//            acls.put("GRUPPO1", EnumACLRights.fullAccess);
//            
//            getBL().setACLDocument(getToken(),  "74079", acls);
//            
//            acls = getBL().getACLDocument(getToken(),  "74079");
//            for(String id : acls.keySet()){
//                System.out.println(id +"=" +acls.get(id));
//            }
//            
//            
//            // assertTrue(true);
//
//        }
//        catch (Exception e) {
//
//            System.out.println(e.getMessage());
//            // assertTrue(false);
//        }
//        finally{
//            try {
//                getBL().logout(getToken());
//            }
//            catch (DocerException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
//
//    }
//
//    // @Test
//    // public void testRegistraDocumento(){
//    //
//    // String token = null;
//    // BusinessLogic bl = null;
//    // Properties p = new Properties();
//    //
//    // try {
//    // p.loadFromXML(this.getClass().getResourceAsStream(
//    // "/docer_config.xml"));
//    //
//    // // appena viene invocato il WS recupero l'informazione del 'provider'
//    // // dal file 'config.xml'
//    // String providerName = p.getProperty("provider");
//    //
//    // BusinessLogic.setConfigPath(p.getProperty("CONFIG_DIR"));
//    //
//    // bl = new BusinessLogic(providerName, 1000);
//    // token = bl.login("ente1", "admin", "admin");
//    //
//    // Map<String,String> info = new HashMap<String,String>();
//    // info.put("COD_ENTE", "ENTE1");
//    // info.put("COD_AOO", "AOO11");
//    // info.put("N_REGISTRAZ", "1");
//    // info.put("ID_REGISTRO", "REG2");
//    // info.put("D_REGISTRAZ", "2012-06-12T10:22:12.584+02:00");
//    // //info.put("ANNO_REGISTRAZ", "2012");
//    // info.put("O_REGISTRAZ", "oggetto registraz");
//    // info.put("TIPO_FIRMA","FD");
//    // bl.registraDocumento(token,"1422", info);
//    // System.out.println("ok");
//    // //assertTrue(true);
//    //
//    // }
//    // catch(Exception e){
//    // if(bl!=null && token!=null)
//    // bl.logout(token);
//    //
//    // System.out.println(e.getMessage());
//    // //assertTrue(false);
//    // }
//    //
//    // }
//    //
//    //
//
//    @Test
//    public void testGetGroupsOfUser() {
//
//        try {
//            token = getBL().login("", "admin", "admin");
//
//            List<String> groups = getBL().getGroupsOfUser(token, "admin");
//
//            for (String gid : groups) {
//                System.out.println(gid);
//            }
//
//        }
//        catch (Exception e) {
//            System.out.println(e.getMessage());
//            // assertTrue(false);
//        }
//        finally {
//            try {
//                getBL().logout(token);
//            }
//            catch (DocerException e) {
//            }
//        }
//
//    }
//
//    @Test
//    public void testReadConfig() {
//
//        try {
//            token = getBL().login("", "admin", "admin");
//
//            String config = getBL().readConfig(token);
//
//            System.out.println(config);
//                    }
//        catch (Exception e) {
//            System.out.println(e.getMessage());
//            // assertTrue(false);
//        }
//        finally {
//            try {
//                getBL().logout(token);
//            }
//            catch (DocerException e) {
//            }
//        }
//
//    }
//
//    @Test
//    public void testConservazione() {
//
//        try {
//            token = getBL().login("", "admin", "admin");
//
//            // getBL().addRelated(token, "61935", Arrays.asList("61670"));
//
//            // getBL().removeRelated(token, "61935", Arrays.asList("61670"));
//            Map<String, String> metadata = new HashMap<String, String>();
//            metadata.put("STATO_CONSERV", "1"); // da conservare
//
//            getBL().updateProfileDocument(token, "61670", metadata);
//
//            System.out.println("ok");
//
//        }
//        catch (Exception e) {
//            System.out.println(e.getMessage());
//            // assertTrue(false);
//        }
//        finally {
//            try {
//                getBL().logout(token);
//            }
//            catch (DocerException e) {
//            }
//        }
//
//    }
//    
//    
//    
//    @Test
//    public void testRelated2() {
//
//        try {
//            token = getBL().login("", "admin", "admin");
//
//            //getBL().addRelated(token, "77888", Arrays.asList("77889","77890","77891","77892"));
//
//            System.out.println(" ----- related di 77888");
//            List<String> rel = getBL().getRelatedDocuments(token, "77888");
//            for(String r : rel){
//                System.out.println(r);
//            }
//            
//            
//            getBL().removeRelated(token, "77888", Arrays.asList("77890"));
//            
//            System.out.println(" ----- related di 77888");
//            
//            rel = getBL().getRelatedDocuments(token, "77888");
//            for(String r : rel){
//                System.out.println(r);
//            }
//            
//            System.out.println(" ----- related di 77889");            
//            rel = getBL().getRelatedDocuments(token, "77889");
//            for(String r : rel){
//                System.out.println(r);
//            }
//            
//            System.out.println(" ----- related di 77890");            
//            rel = getBL().getRelatedDocuments(token, "77890");
//            for(String r : rel){
//                System.out.println(r);
//            }
//            
//            System.out.println(" ----- related di 77891");            
//            rel = getBL().getRelatedDocuments(token, "77891");
//            for(String r : rel){
//                System.out.println(r);
//            }
//            
//            System.out.println(" ----- related di 77892");            
//            rel = getBL().getRelatedDocuments(token, "77892");
//            for(String r : rel){
//                System.out.println(r);
//            }
//            
//            System.out.println("ok");
//
//        }
//        catch (Exception e) {
//            System.out.println(e.getMessage());
//            // assertTrue(false);
//        }
//        finally {
//            try {
//                getBL().logout(token);
//            }
//            catch (DocerException e) {
//            }
//        }
//
//    }
//    
//    
//    
//    @Test
//    public void testCreateSottoFascicolo() {
//
//        try {
//            token = getBL().login("entex", "luca.biasin", "luca.biasin");
//
//
//            Map<String, String> metadata = new HashMap<String, String>();
// 
//            metadata.put("cod_ente", "entex"); 
//            metadata.put("cod_aoo", "aoox"); 
//            metadata.put("classifica", "1.0.0");
//            metadata.put("parent_progr_fascicolo", "1");
//            metadata.put("progr_fascicolo", "1/1");
//            metadata.put("anno_fascicolo", "2013");
//            metadata.put("des_fascicolo", "des 1/1"); 
//            metadata.put("enabled", "true"); 
//
//            getBL().createFascicolo(token, metadata);
//
//            System.out.println("ok");
//
//        }
//        catch (Exception e) {
//            System.out.println(e.getMessage());
//            // assertTrue(false);
//        }
//        finally {
//            try {
//                getBL().logout(token);
//            }
//            catch (DocerException e) {
//            }
//        }
//
//    }
//
//    @Test
//    public void testUpdateSottoFascicolo() {
//
//        try {
//            token = getBL().login("", "admin", "admin");
//
//
//            Map<String, String> id = new HashMap<String, String>();
//            id.put("cod_ente", "entex"); 
//            id.put("cod_aoo", "aoox");
//            id.put("classifica", "1.0.0"); 
//            
//            id.put("progr_fascicolo", "1/1"); 
//            id.put("anno_fascicolo", "2013");
//            id.put("des_fascicolo", "2013");
//            
//            Map<String, String> metadata = new HashMap<String, String>();
//            metadata.put("des_fascicolo", "des 1/1"); // da conservare
//            metadata.put("enabled", "true"); // da conservare
//            metadata.put("classifica", "1.0.0"); 
//            
//            getBL().updateFascicolo(token,id, metadata);
//
//            System.out.println("ok");
//
//        }
//        catch (Exception e) {
//            System.out.println(e.getMessage());
//            // assertTrue(false);
//        }
//        finally {
//            try {
//                getBL().logout(token);
//            }
//            catch (DocerException e) {
//            }
//        }
//
//    }
//              
    
//    @Test
//    public void testCreateFascicolo() {
//
//        try {
//            token = getBL().login("C_I840", "admin", "admin");
//
//
//            Map<String, String> metadata = new HashMap<String, String>();
// 
//            metadata.put("cod_ente", "C_I840"); 
//            metadata.put("cod_aoo", "C-I840-01"); 
//            metadata.put("classifica", "1.1");
//            metadata.put("progr_fascicolo", "C079F5FB-42ED-4880-A88B-B751FAFB93E2");
//            metadata.put("anno_fascicolo", "2013");
////            metadata.put("des_fascicolo", "des di prova per la Fonte"); 
////            metadata.put("cf_persona", "BSNLCU74a01H501M");
////            metadata.put("enabled", "true"); 
//
//            getBL().createFascicolo(token, metadata);
//                                              
//            metadata = getBL().getProfileDocument(token, "17624");
//            for(String key : metadata.keySet()){
//                System.out.println(key +"=" +metadata.get(key));
//            }
//            
//            getBL().createDocument(token, metadata,null);
//            getBL().createDocument(token, getBL().getProfileDocument(token, "17672"),null);
//            getBL().createDocument(token, getBL().getProfileDocument(token, "17675"),null);
//            
//            Map<String,String> f = getBL().getFascicolo(token, metadata);
//            for(String key : f.keySet()){
//                System.out.println(key +"=" +f.get(key));
//            }
//            
//            List<Fascicolo> list = recordManager.getFascicoliToUpload();
//            for(Fascicolo ff : list){
//                System.out.println(ff.getId());
//                System.out.println((ff.getId()));
//            }
//
//        }
//        catch (Exception e) {
//            System.out.println(e.getMessage());
//            // assertTrue(false);
//        }
//        finally {
//            try {
//                getBL().logout(token);
//            }
//            catch (DocerException e) {
//            }
//        }
//
//    }
//
//    @Test
//    public void testUpdateFascicolo() {
//
//        try {
//            token = getBL().login("entex", "luca.biasin", "luca.biasin");
//
//
//            Map<String, String> id = new HashMap<String, String>();
//            id.put("cod_ente", "entex"); 
//            id.put("cod_aoo", "aoox");
//            id.put("classifica", "1.0.0"); 
//            id.put("progr_fascicolo", "1"); 
//            id.put("anno_fascicolo", "2013");
//            
//            
//            Map<String, String> metadata = new HashMap<String, String>();
//            metadata.put("des_fascicolo", "des 1"); // da conservare
//            metadata.put("enabled", "true"); // da conservare
//
//            getBL().updateFascicolo(token,id, metadata);
//
//            System.out.println("ok");
//
//        }
//        catch (Exception e) {
//            System.out.println(e.getMessage());
//            // assertTrue(false);
//        }
//        finally {
//            try {
//                getBL().logout(token);
//            }
//            catch (DocerException e) {
//            }
//        }
//
//    }
//    
//    
//    
//
//    @Test
//    public void testCreateSottoTitolario() {
//
//        try {
//            token = getBL().login("entex", "luca.biasin", "luca.biasin");
//
//
//            Map<String, String> metadata = new HashMap<String, String>();
// 
//            metadata.put("cod_ente", "entex"); 
//            metadata.put("cod_aoo", "aoox"); 
//            metadata.put("parent_classifica", "1.0.0");
//            metadata.put("classifica", "1.1.0");
//            metadata.put("des_titolario", "des 1.1.0"); 
//            metadata.put("enabled", "true"); 
//
//            getBL().createTitolario(token, metadata);
//
//            System.out.println("ok");
//
//        }
//        catch (Exception e) {
//            System.out.println(e.getMessage());
//            // assertTrue(false);
//        }
//        finally {
//            try {
//                getBL().logout(token);
//            }
//            catch (DocerException e) {
//            }
//        }
//
//    }
//
//    @Test
//    public void testUpdateSottoTitolario() {
//
//        try {
//            token = getBL().login("entex", "luca.biasin", "luca.biasin");
//
//
//            Map<String, String> id = new HashMap<String, String>();
//            id.put("cod_ente", "entex"); 
//            id.put("cod_aoo", "aoox");
//            id.put("classifica", "1.1.0"); 
//            
//            
//            Map<String, String> metadata = new HashMap<String, String>();
//            metadata.put("des_titolario", "des 1.1.0 "); // da conservare
//            metadata.put("enabled", "true"); // da conservare
//
//            getBL().updateTitolario(token,id, metadata);
//
//            System.out.println("ok");
//
//        }
//        catch (Exception e) {
//            System.out.println(e.getMessage());
//            // assertTrue(false);
//        }
//        finally {
//            try {
//                getBL().logout(token);
//            }
//            catch (DocerException e) {
//            }
//        }
//
//    }
//    
//    @Test
//    public void testCreateAoo() {
//
//        try {
//            token = getBL().login("entex", "admin", "admin");
//
//
//            Map<String, String> metadata = new HashMap<String, String>();
// 
//            metadata.put("cod_ente", "entex"); 
//            metadata.put("cod_aoo", "aooz"); 
//            metadata.put("classifica", "1.0.0");
//            metadata.put("des_aoo", "des aooy"); 
//            metadata.put("enabled", "true"); 
//
//            getBL().createAOO(token, metadata);
//
//            System.out.println("ok");
//
//        }
//        catch (Exception e) {
//            System.out.println(e.getMessage());
//            // assertTrue(false);
//        }
//        finally {
//            try {
//                getBL().logout(token);
//            }
//            catch (DocerException e) {
//            }
//        }
//
//    }
//   
//    
//    @Test
//    public void testGetEnte() {
//
//        try {
//            token = getBL().login("entex", "admin", "admin");
//
//            Map<String,String> aoo = getBL().getEnte(token, "entex");
//
//            for(String key : aoo.keySet()){
//                System.out.println(key +"=" +aoo.get(key));
//            }
//            
//
//        }
//        catch (Exception e) {
//            System.out.println(e.getMessage());
//            // assertTrue(false);
//        }
//        finally {
//            try {
//                getBL().logout(token);
//            }
//            catch (DocerException e) {
//            }
//        }
//
//    }
//    
//    @Test
//    public void testGetAoo() {
//
//        try {
//            token = getBL().login("", "admin", "admin");
//
//            Map<String, String> id = new HashMap<String, String>();
//            
//            id.put("cod_ente", "ENTE1"); 
//            id.put("cod_aoo", "AOO11");             
//            
//            Map<String,String> aoo = getBL().getAOO(token, id);
//
//            for(String key : aoo.keySet()){
//                System.out.println(key +"=" +aoo.get(key));
//            }
//            
//
//        }
//        catch (Exception e) {
//            System.out.println(e.getMessage());
//            // assertTrue(false);
//        }
//        finally {
//            try {
//                getBL().logout(token);
//            }
//            catch (DocerException e) {
//            }
//        }
//
//    }
//    
//    @Test
//    public void testUpdateAoo() {
//
//        try {
//            token = getBL().login("entex", "admin", "admin");
//
//            Map<String, String> id = new HashMap<String, String>();
//            
//            id.put("cod_ente", "entex"); 
//            id.put("cod_aoo", "aooy");             
//            
//
//            Map<String, String> metadata = new HashMap<String, String>();
// 
//
//            metadata.put("des_aoo", "des aooy"); 
//            metadata.put("enabledx", "true"); 
//
//            getBL().updateAOO(token, id,metadata);
//
//            System.out.println("ok");
//
//        }
//        catch (Exception e) {
//            System.out.println(e.getMessage());
//            // assertTrue(false);
//        }
//        finally {
//            try {
//                getBL().logout(token);
//            }
//            catch (DocerException e) {
//            }
//        }
//
//    }
//    
//    @Test
//    public void testCreateTitolario() {
//
//        try {
//            token = getBL().login("entex", "admin", "admin");
//
//
//            Map<String, String> metadata = new HashMap<String, String>();
// 
//            metadata.put("cod_ente", "entex"); 
//            metadata.put("cod_aoo", "aoox"); 
//            metadata.put("classifica", "1.1.4");
//            metadata.put("parent_classifica", "1.1.3");
//            metadata.put("des_titolario", "des 1.1.1"); 
//            metadata.put("enabled", "true"); 
//
//            getBL().createTitolario(token, metadata);
//
//            System.out.println("ok");
//
//        }
//        catch (Exception e) {
//            System.out.println(e.getMessage());
//            // assertTrue(false);
//        }
//        finally {
//            try {
//                getBL().logout(token);
//            }
//            catch (DocerException e) {
//            }
//        }
//
//    }
//
//    @Test
//    public void testUpdateTitolario() {
//
//        try {
//            token = getBL().login("entex", "admin", "admin");
//
//
//            Map<String, String> id = new HashMap<String, String>();
//            id.put("cod_ente", "entex"); 
//            id.put("cod_aoo", "aoox");
//            id.put("classifica", "1.0.4"); 
//            
//            
//            Map<String, String> metadata = new HashMap<String, String>();
//            metadata.put("des_titolario", "des 1.0.0 "); // da conservare
//            metadata.put("enabled", "true"); // da conservare
//
//            getBL().updateTitolario(token,id, metadata);
//
//            System.out.println("ok");
//
//        }
//        catch (Exception e) {
//            System.out.println(e.getMessage());
//            // assertTrue(false);
//        }
//        finally {
//            try {
//                getBL().logout(token);
//            }
//            catch (DocerException e) {
//            }
//        }
//
//    }
//    
//    
//    @Test
//    public void testCreateCustomItem() {
//
//        try {
//            token = getBL().login("", "admin", "admin");
//
//
//            Map<String, String> metadata = new HashMap<String, String>();
//            metadata.put("type_id", "area_tematica"); 
//            metadata.put("cod_ente", "entex"); 
//            metadata.put("cod_aoo", "aooy"); 
//            metadata.put("cod_area", "areay");
//            metadata.put("des_area", "des areay"); 
//            metadata.put("enabled", "true");
//            metadata.put("custom_field_int", "1"); 
//
//            getBL().createAnagraficaCustom(token, metadata);
//
//            System.out.println("ok");
//
//        }
//        catch (Exception e) {
//            System.out.println(e.getMessage());
//            // assertTrue(false);
//        }
//        finally {
//            try {
//                getBL().logout(token);
//            }
//            catch (DocerException e) {
//            }
//        }
//
//    }
//    
//    @Test
//    public void testUpdateCustomItem() {
//
//        try {
//            token = getBL().login("", "admin", "admin");
//
//            Map<String, String> id = new HashMap<String, String>();
//            id.put("type_id", "area_tematica"); 
//            id.put("cod_ente", "entex"); 
//            id.put("cod_aoo", "aooy"); 
//            id.put("cod_area", "areay");
//            
//            Map<String, String> metadata = new HashMap<String, String>();
//            metadata.put("cod_area", "areay");
//            metadata.put("des_area", "des areax"); 
//            metadata.put("enabled", "true");
//            metadata.put("custom_field_int", "1"); 
//            
//            getBL().updateAnagraficaCustom(token,id, metadata);
//
//            System.out.println("ok");
//
//        }
//        catch (Exception e) {
//            System.out.println(e.getMessage());
//            // assertTrue(false);
//        }
//        finally {
//            try {
//                getBL().logout(token);
//            }
//            catch (DocerException e) {
//            }
//        }
//
//    }
//
//    
//    @Test
//    public void testWriteConfig() {
//
//        try {
//            token = getBL().login("", "admin", "admin");
//
//            getBL().writeConfig(token, "<configuration xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" xmlns:ev=\"http://www.w3.org/2001/xml-events\" xmlns:xi=\"http://www.w3.org/2001/XInclude\" xmlns:xforms=\"http://www.w3.org/2002/xforms\" xmlns:xhtml=\"http://www.w3.org/1999/xhtml\" xmlns:saxon=\"http://saxon.sf.net/\" xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" xmlns:kforms=\"http://kforms.tempuri.org\" xmlns:xbl=\"http://www.w3.org/ns/xbl\" xmlns:version=\"java:org.orbeon.oxf.common.Version\" xmlns:sxc=\"http://sxc.tempuri.org\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:fr=\"http://orbeon.org/oxf/xml/form-runner\" xmlns:fn=\"http://www.w3.org/2005/02/xpath-functions\" xmlns:xxforms=\"http://orbeon.org/oxf/xml/xforms\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n<group name=\"business_logic_variables\">\n<section name=\"props\">\n</section>\n</group>\n<group description=\"abc\" name=\"impianto\" readonly=\"true\">\n<section name=\"fieldxs\">\n</section>\n<section name=\"baseprofile\">\n</section>\n<section name=\"document_types\">\n</section>\n<section name=\"anagrafiche_types\">\n</section>\n<section name=\"hitlists\">\n</section>\n</group>\n<group description=\"abc\" name=\"form_dinamiche\">\n<section name=\"documenti\">\n</section>\n<section name=\"anagrafiche\">\n</section>\n</group>\n</configuration>");
//
//            System.out.println("ok");
//
//        }
//        catch (Exception e) {
//            System.out.println(e.getMessage());
//            // assertTrue(false);
//        }
//        finally {
//            try {
//                getBL().logout(token);
//            }
//            catch (DocerException e) {
//            }
//        }
//
//    }
//    
//    @Test
//    public void testUpdateAOO() {
//
//        try {
//            token = getBL().login("", "admin", "admin");
//
//
//            Map<String, String> id = new HashMap<String, String>();
//            id.put("cod_ente", "entex"); 
//            id.put("cod_aoo", "aoox"); 
//            id.put("des_aoo", "aoox");
//            
//            Map<String, String> metadata = new HashMap<String, String>();
//            metadata.put("des_aoo", "des aooxxx"); // da conservare
//            metadata.put("enabled", "true"); // da conservare
//            metadata.put("cod_ente", "entex"); 
//            metadata.put("cod_aoo", "aoox");
//            
//            getBL().updateAOO(token,id, metadata);
//
//            System.out.println("ok");
//
//        }
//        catch (Exception e) {
//            System.out.println(e.getMessage());
//            // assertTrue(false);
//        }
//        finally {
//            try {
//                getBL().logout(token);
//            }
//            catch (DocerException e) {
//            }
//        }
//
//    }
//    
//    
//    
//    @Test
//    public void testCreateAoo1() {
//
//        try {
//            token = getBL().login("entex", "admin", "admin");
//
//            // getBL().addRelated(token, "61935", Arrays.asList("61670"));
//
//            // getBL().removeRelated(token, "61935", Arrays.asList("61670"));
//            Map<String, String> metadata = new HashMap<String, String>();
//            metadata.put("cod_ente", "entex"); // da conservare
//            metadata.put("cod_aoo", "aoox"); // da conservare
//            metadata.put("des_aoo", "des aoox"); // da conservare
//            metadata.put("enabled", "true"); // da conservare
//
//            getBL().createAOO(token, metadata);
//
//            System.out.println("ok");
//
//        }
//        catch (Exception e) {
//            System.out.println(e.getMessage());
//            // assertTrue(false);
//        }
//        finally {
//            try {
//                getBL().logout(token);
//            }
//            catch (DocerException e) {
//            }
//        }
//
//    }
//
//    @Test
//    public void testUpdateAoo1() {
//
//        try {
//            token = getBL().login("entex", "admin", "admin");
//
//
//            Map<String, String> id = new HashMap<String, String>();
//            id.put("cod_ente", "entex"); 
//            id.put("cod_aoo", "aoox"); 
//            
//            
//            Map<String, String> metadata = new HashMap<String, String>();
//            metadata.put("des_aoo", "des aooxxx"); // da conservare
//            metadata.put("enabled", "true"); // da conservare
//
//            getBL().updateAOO(token,id, metadata);
//
//            System.out.println("ok");
//
//        }
//        catch (Exception e) {
//            System.out.println(e.getMessage());
//            // assertTrue(false);
//        }
//        finally {
//            try {
//                getBL().logout(token);
//            }
//            catch (DocerException e) {
//            }
//        }
//
//    }
//    
//    @Test
//    public void testCreateEnte1() {
//
//        try {
//            token = getBL().login("", "admin", "admin");
//
//            // getBL().addRelated(token, "61935", Arrays.asList("61670"));
//
//            // getBL().removeRelated(token, "61935", Arrays.asList("61670"));
//            Map<String, String> metadata = new HashMap<String, String>();
//            metadata.put("cod_ente", "ente1"); // da conservare
//            metadata.put("des_ente", "des ente1"); // da conservare
//            metadata.put("enabled", "true"); // da conservare
//
//            getBL().createEnte(token, metadata);
//
//            System.out.println("ok");
//
//        }
//        catch (Exception e) {
//            System.out.println(e.getMessage());
//            // assertTrue(false);
//        }
//        finally {
//            try {
//                getBL().logout(token);
//            }
//            catch (DocerException e) {
//            }
//        }
//
//    }
//
//    
//    @Test
//    public void testUpdateEnte1() {
//
//        try {
//            token = getBL().login("", "admin", "admin");
//
//            // getBL().addRelated(token, "61935", Arrays.asList("61670"));
//
//            // getBL().removeRelated(token, "61935", Arrays.asList("61670"));
//            Map<String, String> metadata = new HashMap<String, String>();
//            metadata.put("cod_ente", "entex"); // da conservare
//            metadata.put("des_ente", "des entex"); // da conservare
//            metadata.put("enabled", "true"); // da conservare
//
//            getBL().updateEnte(token,"entex", metadata);
//
//            System.out.println("ok");
//
//        }
//        catch (Exception e) {
//            System.out.println(e.getMessage());
//            // assertTrue(false);
//        }
//        finally {
//            try {
//                getBL().logout(token);
//            }
//            catch (DocerException e) {
//            }
//        }
//
//    }
//    
//        
//
//       
//    
//    
//    @Test
//    public void testProtocollaDocumento() {
//
//        String token = null;
//        BusinessLogic bl = null;
//        Properties p = new Properties();
//
//        try {
//            p.loadFromXML(this.getClass().getResourceAsStream("/docer_config.xml"));
//
//            // appena viene invocato il WS recupero l'informazione del
//            // 'provider'
//            // dal file 'config.xml'
//            String providerName = p.getProperty("provider");
//
//            BusinessLogic.setConfigPath(p.getProperty("CONFIG_DIR"));
//
//            bl = new BusinessLogic(providerName, 1000);
//            token = bl.login("ENTE8", "admin", "admin");
//
//            Map<String, String> info = new HashMap<String, String>();
//            info.put("COD_ENTE", "ENTE8");
//            info.put("COD_AOO", "AOO11");
//            info.put("NUM_PG", "1");
//            info.put("REGISTRO_PG", "REG PG");
//            // info.put("DATA_PG", "2012-06-12T10:22:12.584+02:00");
//            // info.put("DATA_PG", "2012-06-12");
//            // info.put("DATA_PG", "2012-06-12 10:22:12");
//            info.put("DATA_PG", "2012-06-12T10:22:12.000+02:00");
//            info.put("ANNO_PG", "2012");
//            info.put("OGGETTO_PG", "oggetto protocollo");
//            info.put("TIPO_PROTOCOLLAZIONE", "E");
//            info.put("TIPO_FIRMA", "FD");
//            info.put("MITTENTI", "AAA");
//
//            bl.protocollaDocumento(token, "73919", info);
//            System.out.println("ok");
//            // assertTrue(true);
//
//        }
//        catch (Exception e) {
//            if (bl != null && token != null)
//                try {
//                    bl.logout(token);
//                }
//                catch (DocerException e1) {
//                    // TODO Auto-generated catch block
//                    e1.printStackTrace();
//                }
//
//            System.out.println(e.getMessage());
//            // assertTrue(false);
//        }
//
//    }
//
//    @Test
//    public void testGetProfileDocument() {
//
//        try {
//            Map<String, String> profile = getBL().getProfileDocument(getToken(), docid_1);
//
//            System.out.println("-----------------------");
//            System.out.println("Profilo del documento: " + docid_1);
//            System.out.println();
//            for (String key : profile.keySet()) {
//                System.out.println(key + " = " + profile.get(key));
//            }
//            System.out.println("-----------------------");
//
//            // assertTrue(true);
//
//        }
//        catch (Exception e) {
//
//            System.out.println(e.getMessage());
//            // assertTrue(false);
//        }
//
//    }
//
//    @Test
//    public void testDownloadDocument() {
//
//        try {
//            byte[] file = getBL().downloadDocument(getToken(), docid_1, "c:\\windows", 1000);
//            System.out.println("download document, file size: " + file.length);
//
//            // assertTrue(true);
//
//        }
//        catch (Exception e) {
//
//            System.out.println(e.getMessage());
//            // assertTrue(false);
//        }
//
//    }
//
//    @Test
//    public void testLogout() {
//        try {
//            getBL().logout(getToken());
//            System.out.println("logout effettuata");
//            // assertTrue(true);
//        }
//        catch (DocerException e) {
//            System.out.println(e.getMessage());
//            // assertTrue(false);
//        }
//    }

}
