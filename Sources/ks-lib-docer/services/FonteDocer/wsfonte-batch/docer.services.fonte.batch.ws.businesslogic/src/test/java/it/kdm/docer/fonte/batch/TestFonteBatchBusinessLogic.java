package it.kdm.docer.fonte.batch;

import it.kdm.docer.clients.DocerServicesStub.KeyValuePair;
import it.kdm.docer.fonte.batch.popolamentoFonte.BatchPopolamentoFonte;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.activation.DataHandler;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Appender;
import org.apache.log4j.FileAppender;
import org.slf4j.Logger;
import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

//import com.jcraft.jsch.ChannelSftp;
//import com.jcraft.jsch.JSch;
//import com.jcraft.jsch.JSchException;
//import com.jcraft.jsch.Session;
//import com.jcraft.jsch.SftpException;

public class TestFonteBatchBusinessLogic {

    @Test
    public void validateXML() throws Exception {

        try {

            InputStream xml = this.getClass().getResourceAsStream("/schema/test.xml");
            if (xml == null) {
                throw new Exception("file xml non trovato");
            }

            InputStream is = this.getClass().getResourceAsStream("/schema/rer-import-docer-0920.xsd");
            if (is == null) {
                throw new Exception("file di validazione XSD non trovato: getResourceAsStream: /schema/rer-import-docer-0920.xsd");
            }

            Source xsdSchemaSource = new StreamSource(is);

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            DocumentBuilder builder = dbf.newDocumentBuilder();
            Document doc = builder.parse(xml);

            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(xsdSchemaSource);
            javax.xml.validation.Validator validator = schema.newValidator();

            validator.validate(new DOMSource(doc));

        }
        catch (SAXException e) {
            throw new Exception("Errore validazione XML sorgente: " + e.getMessage());
        }
    }

    @Test
    public void testLog() {

        KeyValuePair kvp1 = new KeyValuePair();
        kvp1.setKey("key1");
        kvp1.setValue("value1");

        KeyValuePair kvp2 = new KeyValuePair();
        kvp2.setKey("key2");
        kvp2.setValue("value2");

        KeyValuePair[] arr = new KeyValuePair[] { kvp1, kvp2 };

        System.out.println(arr.toString());

        // Map<String, List<String>> searchCriteria = new HashMap<String,
        // List<String>>();
        //
        // searchCriteria.put("COD_ENTE", Arrays.asList("codEnte"));
        // searchCriteria.put("COD_AOO", Arrays.asList("codAoo"));
        // searchCriteria.put("PARENT_CLASSIFICA",
        // Arrays.asList("parentClassifica","xxxx"));
        //
        // System.out.println(searchCriteria.toString());

    }

    @Test
    public void testLogin() {
        BusinessLogic businessLogic;
        try {
            businessLogic = new BusinessLogic();
        }
        catch (Exception e) {
            System.out.println("Errore istanziamento Business Logic");
            return;
        }

        String ticket;
        try {
            ticket = businessLogic.login("admin", "Kdm.2001", "");
        }
        catch (Exception e) {
            System.out.println("Errore Login: " + e.getMessage());
            return;
        }

        System.out.println(ticket);
    }

    @Test
    public void testScheduleBatchPopolamentoFonte() {
        BusinessLogic businessLogic;
        try {
            businessLogic = new BusinessLogic();
        }
        catch (Exception e) {
            System.out.println("Errore istanziamento Business Logic");
            return;
        }

        String ticket;
        try {
            ticket = businessLogic.login("admin", "Kdm.2001", "");
        }
        catch (Exception e) {
            System.out.println("Errore Login: " + e.getMessage());
            return;
        }

        System.out.println(ticket);

        try {
            businessLogic.scheduleBatchPopolamentoFonte(ticket);
        }
        catch (Exception e) {
            System.out.println("Errore: businessLogic.scheduleBatchPopolamentoFonte: " + e.getMessage());
        }
        finally {
            try {
                businessLogic.unscheduleBatchPopolamentoFonte(ticket);
            }
            catch (Exception e) {
                System.out.println("Errore: businessLogic.unscheduleBatchPopolamentoFonte: " + e.getMessage());
            }
        }
    }

    @Test
    public void testTest() {

//        System.out.println(String.valueOf("".charAt(0)));
        
        System.out.println(Integer.valueOf(""));
        System.out.println(Integer.valueOf(null));
    }

    @Test
    public void testExecuteBatchPopolamentoFonte() {

        try {
            BusinessLogic businessLogic = new BusinessLogic();
            businessLogic.executeBatchPopolamentoFonte("allowed", businessLogic.getConfig(), businessLogic.getConfigurationProperties());
        }
        catch (Exception e) {
            System.out.println("Errore istanziamento Business Logic");
            return;
        }

    }
    

    @Test
    public void testExecuteBatchPopolamentoRaccoglitore() {

        try {
            BusinessLogic businessLogic = new BusinessLogic();
            businessLogic.executeBatchPopolamentoRaccoglitore("allowed",businessLogic.getConfig(), businessLogic.getConfigurationProperties());
        }
        catch (Exception e) {
            System.out.println("Errore istanziamento Business Logic");
            return;
        }

    }
    
    @Test
    public void testCopyStructure() {

        try {
            File sourceRoot = new File("D:/alfresco2/alf_data.bck");
            if(!sourceRoot.exists()){
                throw new Exception(sourceRoot.getAbsolutePath() +" not exists");
            }
            File destRoot = new File("D:/alfresco2/alf_data.copy");
            if(!destRoot.exists()){
                destRoot.mkdir();
            }
            
            explore(sourceRoot,destRoot);
            
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }

    }
    
    private void explore(File sourceNode, File parentDestNode) throws IOException{
        
        if(sourceNode.isFile()){
            File f = new File(parentDestNode, sourceNode.getName());
            if(!f.exists()){
                System.out.println("creato File " +f.getAbsolutePath());
                f.createNewFile();
            }            
            return;
        }
        
        File d = new File(parentDestNode, sourceNode.getName());
        if(!d.exists()){
            System.out.println("creata Dir" +d.getAbsolutePath());
            d.mkdir();
        }
        
        File[] files = sourceNode.listFiles();
        for(File file: files){
            explore(file, d );      
        }
        
    }


    @Test
    public void testTimestamp() {
        Date d = new Date();

        System.out.println(d.getTime());
    }

    @Test
    public void testZip() {

        try {
            String zipFile = "C:/FileIO/zipdemo.zip";

            File archive = new File(zipFile);

            if (!archive.exists()) {
                archive.getParentFile().mkdir();
                archive.createNewFile();
            }

            List<File> sourceFiles = new ArrayList<File>();

            sourceFiles.add(new File("C:/test.pdf"));
            sourceFiles.add(new File("C:/test.txt"));
            sourceFiles.add(new File("C:/test.log"));

            // create byte buffer
            byte[] buffer = new byte[1024];

            /*
             * To create a zip file, use ZipOutputStream(OutputStream out)
             * constructor of ZipOutputStream class.
             */

            // create object of FileOutputStream
            FileOutputStream fout = new FileOutputStream(zipFile);

            // create object of ZipOutputStream from FileOutputStream
            ZipOutputStream zout = new ZipOutputStream(fout);

            for (int i = 0; i < sourceFiles.size(); i++) {

                System.out.println("Adding " + sourceFiles.get(i).getName());
                // create object of FileInputStream for source file
                FileInputStream fin = new FileInputStream(sourceFiles.get(i));

                /*
                 * To begin writing ZipEntry in the zip file, use void
                 * putNextEntry(ZipEntry entry) method of ZipOutputStream class.
                 * This method begins writing a new Zip entry to the zip file
                 * and positions the stream to the start of the entry data.
                 */

                ZipEntry ze = new ZipEntry(sourceFiles.get(i).getName());

                zout.putNextEntry(ze);

                /*
                 * After creating entry in the zip file, actually write the
                 * file.
                 */
                int length;

                while ((length = fin.read(buffer)) > 0) {
                    zout.write(buffer, 0, length);
                }

                /*
                 * After writing the file to ZipOutputStream, use void
                 * closeEntry() method of ZipOutputStream class to close the
                 * current entry and position the stream to write the next
                 * entry.
                 */

                zout.closeEntry();

                // close the InputStream
                fin.close();

            }

            // close the ZipOutputStream
            zout.close();

            System.out.println("Zip file has been created!");

        }
        catch (IOException ioe) {
            System.out.println("IOException :" + ioe);
        }
    }

    private void testZip(Map<String, File> fileMap, File archive) {

        try {

            if (!archive.exists()) {
                archive.getParentFile().mkdir();
                archive.createNewFile();
            }

            // create byte buffer
            byte[] buffer = new byte[1024];

            /*
             * To create a zip file, use ZipOutputStream(OutputStream out)
             * constructor of ZipOutputStream class.
             */

            // create object of FileOutputStream
            FileOutputStream fout = new FileOutputStream(archive.getAbsolutePath());

            // create object of ZipOutputStream from FileOutputStream
            ZipOutputStream zout = new ZipOutputStream(fout);

            for (String filename : fileMap.keySet()) {

                System.out.println("Adding " + filename);
                // create object of FileInputStream for source file
                FileInputStream fin = new FileInputStream(fileMap.get(filename));

                /*
                 * To begin writing ZipEntry in the zip file, use void
                 * putNextEntry(ZipEntry entry) method of ZipOutputStream class.
                 * This method begins writing a new Zip entry to the zip file
                 * and positions the stream to the start of the entry data.
                 */

                ZipEntry ze = new ZipEntry(filename);

                zout.putNextEntry(ze);

                /*
                 * After creating entry in the zip file, actually write the
                 * file.
                 */
                int length;

                while ((length = fin.read(buffer)) > 0) {
                    zout.write(buffer, 0, length);
                }

                /*
                 * After writing the file to ZipOutputStream, use void
                 * closeEntry() method of ZipOutputStream class to close the
                 * current entry and position the stream to write the next
                 * entry.
                 */

                zout.closeEntry();

                // close the InputStream
                fin.close();

            }

            // close the ZipOutputStream
            zout.close();

            System.out.println("Zip file has been created!");

        }
        catch (IOException ioe) {
            System.out.println("IOException :" + ioe);
        }
    }

    @Test
    public void getLog4jFilePath() {

        Map<String, String> log4jLocations = getLogLocations();

        System.out.println(log4jLocations.toString());

        return;

    }

    @SuppressWarnings("unchecked")
    private static Map<String, String> getLogLocations() {

        Collection<Logger> allLoggers = new ArrayList<Logger>();

        Logger rootLogger = Logger.getRootLogger();

        allLoggers.add(rootLogger);

        for (Enumeration<Logger> loggers =

        rootorg.slf4j.LoggerFactory.getLoggerRepository().getCurrentLoggers();

        loggers.hasMoreElements();) {

            allLoggers.add(loggers.nextElement());

        }

        Set<FileAppender> fileAppenders =

        new LinkedHashSet<FileAppender>();

        for (Logger logger : allLoggers) {

            for (Enumeration<Appender> appenders =

            logger.getAllAppenders();

            appenders.hasMoreElements();) {

                Appender appender = appenders.nextElement();

                if (appender instanceof FileAppender) {

                    fileAppenders.add((FileAppender)appender);

                }

            }

        }

        Map<String, String> locations =

        new LinkedHashMap<String, String>();

        for (FileAppender appender : fileAppenders) {

            locations.put(appender.getName(), appender.getFile());

        }

        return locations;

    }

    // @Test
    // public void testExecuteBatchPopolamentoFonte2() {
    // BusinessLogic businessLogic;
    // try {
    // businessLogic = new BusinessLogic();
    // }
    // catch (Exception e) {
    // System.out.println("Errore istanziamento Business Logic: " +
    // e.getMessage());
    // return;
    // }
    //
    // BatchPopolamentoFonte batchPopolamentoFonte;
    // try {
    // batchPopolamentoFonte = new BatchPopolamentoFonte();
    //
    // batchPopolamentoFonte.execute(null);
    // }
    // catch (Exception e) {
    // System.out.println("Errore BatchPopolamentoFonte: " + e.getMessage());
    // return;
    // }
    // finally {
    // try {
    // businessLogic.shutdown();
    // }
    // catch (Throwable e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
    // }
    //
    // }
    //

    @Test
    public void testRunScheduler() {
        BusinessLogic businessLogic;
        try {
            businessLogic = new BusinessLogic();
        }
        catch (Exception e) {
            System.out.println("Errore istanziamento Business Logic: " + e.getMessage());
            return;
        }

        try {
            businessLogic.scheduleBatchPopolamentoFonte("allowed");
        }
        catch (Exception e) {
            System.out.println("scheduleBatchPopolamentoFonte: " + e.getMessage());
            return;
        }

        try {
            businessLogic.unscheduleBatchPopolamentoFonte("allowed");
        }
        catch (Exception e) {
            System.out.println("unscheduleBatchPopolamentoFonte: " + e.getMessage());
            return;
        }

    }

    @Test
    public void testBoolean() {

        System.out.println(Boolean.valueOf("FALSE"));
        System.out.println(Boolean.valueOf("TRUE"));
        System.out.println(Boolean.valueOf("True"));
        System.out.println(Boolean.valueOf("true"));
        System.out.println(Boolean.valueOf("1"));
        System.out.println(Boolean.valueOf(null));
    }

    // @Test
    // public void testExecuteBatchPopolamentoRaccoglitore() {
    // BusinessLogic businessLogic;
    // try {
    // businessLogic = new BusinessLogic();
    // }
    // catch (Exception e) {
    // System.out.println("Errore istanziamento Business Logic: " +
    // e.getMessage());
    // return;
    // }
    //
    // BatchPopolamentoRaccoglitore batchPopolamentoRaccoglitore;
    // try {
    // batchPopolamentoRaccoglitore = new BatchPopolamentoRaccoglitore();
    //
    // batchPopolamentoRaccoglitore.execute(null);
    // }
    // catch (Exception e) {
    // System.out.println("Errore BatchPopolamentoRaccoglitore: " +
    // e.getMessage());
    // return;
    // }
    // finally {
    // try {
    // businessLogic.shutdown();
    // }
    // catch (Throwable e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
    // }
    //
    // }
//    @Test
//    public void testSendMail() {
//        
//        try {
//            BusinessLogic businessLogic = new BusinessLogic();
//            
//            BatchPopolamentoFonte.setConfig(businessLogic.getConfig(), businessLogic.getConfigurationProperties());
//            BatchPopolamentoFonte bpf = new BatchPopolamentoFonte();
//            bpf.testEmail();
//            
//        }
//        catch (Exception e) {
//            System.out.println("Errore istanziamento Business Logic: " + e.getMessage());
//            return;
//        }
//
//    }

    // @Test
    // public void testSendMail() {
    // BusinessLogic businessLogic;
    // try {
    // businessLogic = new BusinessLogic();
    // }
    // catch (Exception e) {
    // System.out.println("Errore istanziamento Business Logic: " +
    // e.getMessage());
    // return;
    // }
    //
    // BatchPopolamentoFonte batchPopolamentoFonte;
    // try {
    // batchPopolamentoFonte = new BatchPopolamentoFonte();
    //
    // // batchPopolamentoFonte.testEmail();
    //
    // batchPopolamentoFonte.testEmail();
    // }
    // catch (Exception e) {
    // System.out.println("Errore testEmail: " + e.getMessage());
    // return;
    // }
    // finally {
    // try {
    // businessLogic.shutdown();
    // }
    // catch (Throwable e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
    // }
    //
    // }

    // @Test
    // public void sendViaSFTP() throws JSchException, SftpException {
    //
    // String idFonte = "EMR1";
    // String sftp_host = "93.62.155.226";
    // String sftp_username = "raccoglitore";
    // String sftp_password = "rac384_hjsj78";
    // int sftp_port = -1;
    //
    // JSch.setConfig("StrictHostKeyChecking", "no");
    //
    // JSch jsch = new JSch();
    //
    // Session session;
    //
    // if (sftp_port > 0) {
    // session = jsch.getSession(sftp_username, sftp_host, sftp_port);
    // }
    // else {
    // session = jsch.getSession(sftp_username, sftp_host);
    // }
    //
    // session.setPassword(sftp_password);
    //
    // try {
    // session.connect();
    //
    // ChannelSftp channel = (ChannelSftp)session.openChannel("sftp");
    //
    // try {
    // channel.connect();
    //
    // File file = new File("c:/flusso_dia.xml");
    //
    // String ftpDirectory = channel.getHome() + "/" + idFonte;
    //
    // channel.put(file.getAbsolutePath(), ftpDirectory);
    //
    // System.out.println(file.getAbsolutePath() + " uploaded to " +
    // ftpDirectory);
    // }
    // catch (Exception e) {
    // System.out.println(e.getMessage());
    // }
    // finally {
    // channel.exit();
    // }
    // }
    // catch (Exception e) {
    // System.out.println(e.getMessage());
    // }
    // finally {
    // session.disconnect();
    // }
    //
    // }

    // @Test
    // public void testDownloadAndZip() {
    // BusinessLogic businessLogic;
    // try {
    // businessLogic = new BusinessLogic();
    // }
    // catch (Exception e) {
    // System.out.println("Errore istanziamento Business Logic: " +
    // e.getMessage());
    // return;
    // }
    //
    // BatchPopolamentoFonte batchPopolamentoFonte;
    // try {
    // batchPopolamentoFonte = new BatchPopolamentoFonte();
    //
    // String filename1 =
    // batchPopolamentoFonte.testGetFileNameFromFonte("68703");
    //
    // DataHandler dh1 =
    // batchPopolamentoFonte.testDownloadDocumentFromFonte("68703");
    //
    // String guid1 = UUID.randomUUID().toString();
    // File f1 = new File("C:/", guid1);
    // saveToDisk(dh1, f1);
    //
    // String filename2 =
    // batchPopolamentoFonte.testGetFileNameFromFonte("838780");
    //
    // filename2 = "filename2/" +filename2;
    //
    // DataHandler dh2 =
    // batchPopolamentoFonte.testDownloadDocumentFromFonte("838780");
    //
    // String guid2 = UUID.randomUUID().toString();
    // File f2 = new File("C:/", guid2);
    // saveToDisk(dh2, f2);
    //
    // String filename3 =
    // batchPopolamentoFonte.testGetFileNameFromFonte("852299");
    //
    // filename3 = "filename3/" +filename3;
    //
    // DataHandler dh3 =
    // batchPopolamentoFonte.testDownloadDocumentFromFonte("852299");
    //
    // String guid3 = UUID.randomUUID().toString();
    // File f3 = new File("C:/", guid3);
    // saveToDisk(dh3, f3);
    //
    // Map<String, File> fileMap = new HashMap<String, File>();
    // fileMap.put(filename1, f1);
    // fileMap.put(filename2, f2);
    // fileMap.put(filename3, f3);
    //
    // File archive = new File("C:/", "archive.zip");
    // testZip(fileMap, archive);
    //
    // }
    // catch (Exception e) {
    // System.out.println(e.getMessage());
    // return;
    // }
    // finally {
    // try {
    // businessLogic.shutdown();
    // }
    // catch (Throwable e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
    // }
    //
    // }

    private void saveToDisk(DataHandler dh, File destFile) throws Exception {

        FileOutputStream filesystemOutputStream = null;

        int status = 0;
        try {

            // buffer

            int read = 1;
            int totalRead = 0;

            // stream per scrivere su filesystem
            filesystemOutputStream = new FileOutputStream(destFile.getAbsolutePath());

            InputStream is = dh.getDataSource().getInputStream();
            byte[] buffer = new byte[4096];
            int bytesRead = 0;
            while ((bytesRead = is.read(buffer)) != -1) {
                filesystemOutputStream.write(buffer, 0, bytesRead);
            }

            filesystemOutputStream.close();

        }
        catch (IOException e) {
            throw new Exception("saveToDisk: IOException: " + e.getMessage());

        }
        // catch (RemoteException e) {
        // throw new DocerException(-1295, "Exception: DownloadFile: "
        // +e.getMessage());
        // }
        finally {

            try {
                if (dh.getInputStream() != null) {
                    dh.getInputStream().close();
                }

                if (filesystemOutputStream != null) {
                    filesystemOutputStream.close();
                }

            }
            catch (IOException e2) {

            }

        }
    }

}
