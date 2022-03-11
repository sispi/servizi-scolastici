package keysuite.docer.server;

import com.google.common.base.Strings;
import com.sun.mail.util.MailSSLSocketFactory;
import it.kdm.orchestratore.beans.EmailBean;
import it.kdm.orchestratore.beans.GenericResultSet;
import it.kdm.orchestratore.beans.ImapMailItemList;
import it.kdm.orchestratore.beans.ResultCall;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeUtility;
import javax.mail.search.MessageIDTerm;
import javax.mail.search.SearchTerm;
import keysuite.docer.bl.IRestUtilsBridge;
import keysuite.docer.utils.DBManager;
import keysuite.docer.utils.ReadMail;
import keysuite.docer.utils.Tools;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringSubstitutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class RestUtilsService  extends BaseService implements IRestUtilsBridge<DBManager,ResultCall> {


    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SS'Z'");
    Logger log = LoggerFactory.getLogger(RestUtilsService.class);

    public static final String HOST_MAIL = "host_mail";
    public static final String EMAIL_ADDRESS = "emailAddress";
    public static final String USERNAME = "username";
    public static final String PORT = "port";
    public static final String PASSWORD ="password";
    public static final String PROVIDER ="provider";
    public static final String SSL ="ssl";
    public static final String LIMIT ="limit";
    public static final String FOLDER ="folder";
    public static final String KEY ="key";
    public static final String FILENAME ="fileName";
    public static final String MESSAGE_ID ="messageId";
    public static final String FILE_TYPE ="fileType";

    public static final String FILE_TYPE_URL ="url";
    public static final String FILE_TYPE_FILE ="file";


    public ResultCall<List<ImapMailItemList>> getLastImapList(Map<String, Object> conf, String lastDate, int limit) {

        limit--;
        //int limit = 10;
        Calendar startDate = Calendar.getInstance();
        startDate.setTime(new Date());

        Properties props = new Properties();


        String host         = (String)conf.get(HOST_MAIL);
        String emailAddress = (String)conf.get(EMAIL_ADDRESS);
        String username     = (String)conf.get(USERNAME);
        Integer port         = (Integer)conf.get(PORT);
        String password     = (String)conf.get(PASSWORD);
        String provider     = (String)conf.get(PROVIDER);
        Boolean ssl          = (Boolean)conf.get(SSL);
        String folder       = (String)conf.get(FOLDER);

        props.setProperty("mail.imap.ssl.enable", ssl.toString());

        if(ssl) {
            props.setProperty("mail.imaps.ssl.protocols", "TLSv1.2\nTLSv1.1\nTLSv1\nSSLv3");
            if(System.getProperty("java.version").startsWith("1.7")) {
                try {
                    MailSSLSocketFactory mailSocketFactory = new MailSSLSocketFactory("TLSv1.2");
                    props.put("mail.imaps.ssl.socketFactory", mailSocketFactory);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        String[] hostAndParameter =null;
        try {
            hostAndParameter = host.split("\\|");
            if (hostAndParameter != null && hostAndParameter.length > 1) {
                for (int i = 1; i < hostAndParameter.length; i++) {
                    String property = hostAndParameter[i];
                    String[] propertiesArray = property.split(":");
                    if (propertiesArray.length == 2) {
                        props.setProperty(propertiesArray[0], propertiesArray[1]);
                    }
                }

                host = hostAndParameter[0];
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        Date compairDate = null;


        ResultCall<List<ImapMailItemList>> result = new ResultCall<List<ImapMailItemList>>();
        result.setData(null);
        result.setListSize(0);
        result.setMessage("Errore generico ImapDaoImpl");
        result.setStatus(0);


        if(org.springframework.util.StringUtils.isEmpty(lastDate)){
            lastDate = "1970-01-01T00:00:00.000Z";
        }

        try{
            compairDate = sdf.parse(lastDate);
        }catch (Exception ex1){
            ex1.printStackTrace();
        }

        if(null == compairDate){
            result.setMessage("formato lastDate non valido");
        }else{



            if(provider != null && provider.length()>0 && !provider.endsWith("s") && new Boolean(ssl)){
                provider +="s";
            }

            try {
                //Connect to the server
                Session session = Session.getInstance(props, null);
                Store store = session.getStore(provider);
                store.connect(host, port, username, password);

                //open the inbox folder
                Folder inbox = store.getFolder(folder);
                inbox.open(Folder.READ_ONLY);

                // get a list of javamail messages as an array of messages
                Message[] messages = inbox.getMessages();

                List<ImapMailItemList> messaggi = new ArrayList<>();
                if(lastDate == null){
                    log.warn("INIZIALIZZAZIONE CASELLA: "+ emailAddress + " con lastTimestamp null, verra' letta dall'inizio");
                    messaggi = getMessages(messages, 0, limit, emailAddress);
                }else{
                    log.warn("INIZIALIZZAZIONE CASELLA: "+ emailAddress + " con lastTimestamp "+lastDate);
                    int startIndex = getStartIndex(messages, compairDate,0, messages.length-1);


                    while (startIndex>=0 && startIndex<messages.length){
                        Message m =messages[startIndex];
                        //String msgid = m.getHeader("message-id")[0];
                        String date = sdf.format(m.getReceivedDate());
                        if(date.compareTo(lastDate)<0){
                            startIndex++;
                            break;
                        }
                        startIndex--;
                    }
                    if(startIndex<0)startIndex =0;

                    List<String>messaggiDB = getMessageDB(lastDate, emailAddress);

                    while (startIndex<messages.length){

                        Message m =messages[startIndex];
                        String msgid = m.getHeader("message-id")[0];

                        if(!messaggiDB.contains(msgid)){
                            messaggi.addAll(getMessages(messages, startIndex, startIndex, emailAddress));
                            if(messaggi.size()==(limit+1)) {
                                log.warn("LETTI "+messaggi.size()+ "MESSAGGI PER LA  CASELLA: "+ emailAddress);
                                break;
                            }
                        }else{
                            log.warn("MSGID: "+msgid+" GIA' INSERITO PER LA CASELLA: "+ emailAddress);
                        }

                        startIndex++;
                    }
                }


                if(messaggi.size()==0){
                    result.setMessage("Nessun nuovo messaggio");
                }else{
                    result.setMessage("Messaggi letti con successo");
                    result.setData(messaggi);
                    result.setStatus(1);
                    result.setListSize(messaggi.size());
                }


                //close the inbox folder but do not
                //remove the messages from the server
                inbox.close(false);
                store.close();

            }catch(Exception ex){
                result.setMessage(result.getMessage() + ": " +ex.getMessage());
                ex.printStackTrace();
            }


        }

        Calendar endDate = Calendar.getInstance();
        endDate.setTime(new Date());
        long duration = endDate.getTimeInMillis() - startDate.getTimeInMillis();
        result.setDuration(duration);
//        }

        return result;

    }

    private int getStartIndex(Message[] messaggi, Date searchDate, int start, int end) throws MessagingException {

        if(start>messaggi.length-1){
            return start;
        }

        int index= (start+end)/2;


        Date currentDate = messaggi[index].getReceivedDate();

        if(currentDate.equals(searchDate)) {
            return index+1;
        }else  if(currentDate.after(searchDate)){

            end = index;
            if(end == start){
                return start;
            }

            return getStartIndex(messaggi, searchDate, start, end);
        }else{
            start = index+1;
            return getStartIndex(messaggi, searchDate, start, end);
        }

    }

    private List<ImapMailItemList> getMessages(Message[] messaggi, int start, int end, String emailAddress ) throws MessagingException {
        List<ImapMailItemList> result = new ArrayList<ImapMailItemList>();

        if(start>end){
            int appo = end;
            end = start;
            start = appo;
        }

        if(start > messaggi.length-1){
            return result;
        }
        if(end > messaggi.length-1){
            end = messaggi.length-1;
        }

        Date creationDate = new Date();
        for (int i = end; i >= start; i--) {
            Message m = messaggi[i];

            ImapMailItemList item = new ImapMailItemList();
            item.setCreationDate(sdf.format(creationDate));
            item.setDate(sdf.format(m.getReceivedDate()));
            item.setMailbox(emailAddress);
            item.setState(0);
            Enumeration e = m.getAllHeaders();
            while (e.hasMoreElements()) {
                Header header = (Header) e.nextElement();
                if (header.getName().equalsIgnoreCase("message-id")) {
                    item.setMsgid(header.getValue());
                    if(item.getMsgid() == null || item.getMsgid().equalsIgnoreCase("") || item.getMsgid().equalsIgnoreCase("<>")){
                        item.setMsgid("<"+new Date().getTime()+"-SPAM>");
                        item.setState(555); ///SPAM
                    }
                    break;
                }
            }

            if(m != null){
                //Provo a settare il subject
                if(m.getSubject() != null) {
                    try{
                        item.setSubject(MimeUtility.decodeText(m.getSubject()));
                    }catch (Exception exSubject){log.error("Impossibile parsare il Subject per la mail con msgid:" + item.getMsgid() + " del: "+item.getCreationDate() + " della casella: "+ emailAddress);}
                }
                if(m.getAllRecipients() != null) {
                    //Provo a settare i bcc
                    Address bccAddr[] = m.getRecipients(Message.RecipientType.BCC);
                    String bcc=null;
                    if(bccAddr != null && bccAddr.length>0){
                        bcc = "";
                        for(Address a: bccAddr){
                            bcc+=a.toString()+",";
                        }
                        if(bcc.endsWith(",")){
                            bcc = bcc.substring(0, bcc.length()-1);
                        }
                    }
                    item.setMailBcc(bcc);

                    //Provo a settare i cc
                    Address ccAddr[] = m.getRecipients(Message.RecipientType.CC);
                    String cc=null;
                    if(ccAddr != null && ccAddr.length>0){
                        cc = "";
                        for(Address a: ccAddr){
                            cc+=a.toString()+",";
                        }
                        if(cc.endsWith(",")){
                            cc = cc.substring(0, cc.length()-1);
                        }
                    }
                    item.setMailCc(cc);

                    //Provo a settare i to
                    Address toAddr[] = m.getRecipients(Message.RecipientType.TO);
                    String to=null;
                    if(toAddr != null && toAddr.length>0){
                        to = "";
                        for(Address a: toAddr){
                            to+=a.toString()+",";
                        }
                        if(to.endsWith(",")){
                            to = to.substring(0, to.length()-1);
                        }
                    }
                    item.setMailTo(to);

                    //Provo a settare il from
                    Address fromAddr[] = m.getFrom();
                    String from=null;
                    if(fromAddr != null && fromAddr.length>0){
                        from = "";
                        for(Address a: fromAddr){
                            from+=a.toString()+",";
                        }
                        if(from.endsWith(",")){
                            from = from.substring(0, from.length()-1);
                        }
                        item.setMailFrom(from);
                    }
                }


                Integer hasAttachment = 0;
                try {
                    if(m.getContent() != null){
                        if(m.getContent() instanceof Multipart) {
                            Multipart multiPart = (Multipart) m.getContent();
                            for (int x = 0; x < multiPart.getCount(); x++) {
                                MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(x);
                                if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
                                    // yes, it has an attachment
                                    hasAttachment = 1;
                                    break;
                                }
                            }
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                item.setHasAttachment(hasAttachment);

            }

            result.add(item);
        }



        return result;
    }

    public ResultCall<EmailBean> getImapMail(Map<String, Object> conf, String messageId, String fileType, String alternativeDownloadPath){

        Calendar startDate = Calendar.getInstance();
        startDate.setTime(new Date());

        Properties props = new Properties();
        String host         = (String)conf.get(HOST_MAIL);
        String emailAddress = (String)conf.get(EMAIL_ADDRESS);
        String username     = (String)conf.get(USERNAME);
        Integer port         = (Integer)conf.get(PORT);
        String password     = (String)conf.get(PASSWORD);
        String provider     = (String)conf.get(PROVIDER);
        Boolean ssl          = (Boolean)conf.get(SSL);
        String folder       = (String)conf.get(FOLDER);

        props.setProperty("mail.imap.ssl.enable", ssl.toString());
        if(ssl) {
            props.setProperty("mail.imaps.ssl.protocols", "TLSv1.2\nTLSv1.1\nTLSv1\nSSLv3");
            if(System.getProperty("java.version").startsWith("1.7")) {
                try {
                    MailSSLSocketFactory mailSocketFactory = new MailSSLSocketFactory("TLSv1.2");
                    props.put("mail.imaps.ssl.socketFactory", mailSocketFactory);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        String[] hostAndParameter =null;
        try {
            hostAndParameter = host.split("\\|");
            if (hostAndParameter != null && hostAndParameter.length > 1) {
                for (int i = 1; i < hostAndParameter.length; i++) {
                    String property = hostAndParameter[i];
                    String[] propertiesArray = property.split(":");
                    if (propertiesArray.length == 2) {
                        props.setProperty(propertiesArray[0], propertiesArray[1]);
                    }
                }

                host = hostAndParameter[0];
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        ResultCall<EmailBean> result = new ResultCall<EmailBean>();
        result.setData(null);
        result.setListSize(0);
        result.setMessage("Errore generico ImapDaoImpl");
        result.setStatus(0);


        if(org.springframework.util.StringUtils.isEmpty(messageId)){
            result.setMessage("messageId e' obbligatorio");
        }else{
            if(provider != null && provider.length()>0 && !provider.endsWith("s") && new Boolean(ssl)){
                provider +="s";
            }
            Folder inbox=null;
            Store store = null;
            try {

                //Connect to the server
                Session session = Session.getInstance(props, null);
                store = session.getStore(provider);
                store.connect(host, port, username, password);

                //open the inbox folder
                inbox = store.getFolder(folder);
                inbox.open(Folder.READ_ONLY);

                // get a list of javamail messages as an array of messages
                SearchTerm termMessageId = new MessageIDTerm(messageId);
                Message[] messaggi = inbox.search(termMessageId);

                if(messaggi != null && messaggi.length ==1){
                    Message m = messaggi[0];

                    //caso 200 OK
                    //caso 204 daControllare OK Malformed pec
                    //caso 500 altrimenti

                    ReadMail readMail = new ReadMail();
                    EmailBean emailBean = readMail.parseMail(m, session,messageId, fileType, alternativeDownloadPath);
                    if(emailBean.getDaControllare() != null && emailBean.getDaControllare()){
                        result.setStatus(204);
                        result.setMessage("KO - Mail da controllare");
                    }else{
                        result.setStatus(200);
                        result.setMessage("OK - Mail letta con successo");
                    }

                    result.setData(emailBean);

                }else{

                    result.setMessage("Nessun messaggio trovato per il messageId: "+ messageId);
                    result.setStatus(205);
                }

            }catch (Exception ex1){
                result.setStatus(500);
                ex1.printStackTrace();
            }finally {
                if(inbox!=null && inbox.isOpen()) {
                    try {
                        inbox.close(false);
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                }
                if(store!=null && store.isConnected()) {
                    try {
                        store.close();
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        Calendar endDate = Calendar.getInstance();
        endDate.setTime(new Date());
        long duration = endDate.getTimeInMillis() - startDate.getTimeInMillis();
        result.setDuration(duration);

        return result;
    }


    @Override
    public boolean clearAllTempFile(){
        boolean result = false;
        String attachmentsDirectory = null;
        try {
            attachmentsDirectory = env.getProperty("mail.attachDirEmailPec");
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        }
        File mailBackupDirFile = new File(attachmentsDirectory + File.separator + "mails" + File.separator);
        if(mailBackupDirFile.exists() && mailBackupDirFile.isDirectory()){
            File[] listFile = mailBackupDirFile.listFiles();
            for(File f: listFile){
                if(f.exists() && f.isDirectory()){
                    try {
                        org.apache.commons.io.FileUtils.deleteDirectory(f);
                    } catch (IOException e) {
                        log.error("Errore nell'eliminazione del file: "+ f.getAbsolutePath(),e);
                    }
                }else{
                    f.delete();
                }
            }
            result = true;
        }

        return result;
    }


    @Override
    public boolean clearAllMsgIdFiles(String messageId){
        boolean result = false;


        String attachmentsDirectory = null;
        try {
            attachmentsDirectory = env.getProperty("mail.attachDirEmailPec");
        } catch (Exception e) {
            log.error("Impossibile leggere la cartella mail.attachDirEmailPec perchè non presente o mancata configurazione", e);
            result = false;
        }

        String dir = attachmentsDirectory + File.separator + "mails" + File.separator ;

        File mailBackupDirFile = new File(dir);

        if(mailBackupDirFile.exists() && mailBackupDirFile.isDirectory()){
            File[] listFile = mailBackupDirFile.listFiles();
            for(File f: listFile){
                searchAndRemoveMessageId(f, messageId);
            }
            result = true;
        }

        return result;

    }


    private void searchAndRemoveMessageId(File f, String messageId){
        String messageIdDir;
        //setto il messageId eliminando i caratteri "<" e ">" che causano problemi
        messageIdDir = messageId.replace("<", "");
        messageIdDir = messageIdDir.replace(">", "");
        messageIdDir = messageIdDir.replace("+", "");

        if(f.exists()) {
            if (f.isDirectory() && f.getAbsolutePath().contains(messageIdDir)) {
                try {
                    org.apache.commons.io.FileUtils.deleteDirectory(f);
                } catch (IOException e) {
                    //e.printStackTrace();
                    log.warn("Impossibile eliminare il file: " + f.getAbsolutePath(), e);
                    //throw e;
                }
            } else if (f.isDirectory()) {
                File[] files = f.listFiles();
                for (File x : files) {
                    searchAndRemoveMessageId(x, messageIdDir);
                }
            } else if (f.isFile() && f.getAbsolutePath().contains(messageIdDir)) {
                f.delete();
            }
        }

    }


    private List<String> getMessageDB(String lastDate, String emailAddress){
        List<String> result = new ArrayList<>();
        try {
            HashMap<String, String> mysqlConnectionProperty = getApplicationDBConnection();

            DBManager dbManagerMysql = DBManager.getInstance(mysqlConnectionProperty);
            String query = "Select msgid from mails where date >= \"${date}\" and mailbox=\"${mailbox}\"";
            String dbType = dbManagerMysql.getDbType();
            if(dbType != null && dbType.equalsIgnoreCase("microsoft sql server")){
                query = "Select msgid from mails where [date] >= '${date}' and mailbox='${mailbox}'";
            }
            Map<String, String> map = new HashMap<>();
            map.put("date", lastDate);
            map.put("mailbox", emailAddress);
            StringSubstitutor sbr = new StringSubstitutor(map);
            query = sbr.replace(query);

            GenericResultSet grs = dbManagerMysql.executeSelect(query, null);
            if(grs != null && grs.getValori() != null && grs.getValori().size()>0){

                for(Object[] vall : grs.getValori()){
                    result.add(vall[0].toString());
                }
            }
        }catch (Exception ex1){
            throw new RuntimeException(ex1);
        }
        return result;
    }

    private HashMap<String, String> getApplicationDBConnection() throws Exception{
        HashMap<String, String> connectionProperty=new HashMap<String, String>();

        String dbJndi = env.getProperty(DBManager.PROP_DB_JNDI_DATASOURCE);

        connectionProperty.put(DBManager.PROP_DB_JNDI_DATASOURCE, dbJndi);
        return connectionProperty;
    }


    public Map getPDFfromHTML(Map<String,String> params) throws IOException, InterruptedException {

        UUID guid = java.util.UUID.randomUUID();
        String defDir;

        try {
            defDir = env.getProperty("html2pdf.fileDir");
            if(StringUtils.isEmpty(defDir)){
                defDir = "/root/bpm-config/upload";
            }
        } catch (Exception e) {
            defDir = "/root/bpm-config/upload";
        }

        String suffixDir="";

        if(params.get("suffixDir")!=null && !params.get("suffixDir").isEmpty()){
            suffixDir=params.get("suffixDir");
        }

        String nomeCartella= String.valueOf(System.currentTimeMillis());
        nomeCartella=nomeCartella+"-"+guid.toString();
        String  dirPath=defDir+ File.separator+nomeCartella+suffixDir;

        File directory = new File(dirPath);

        // se la cartella esiste allora construirò una nuovca cartella
        while(directory.exists() && directory.isDirectory()){
            nomeCartella+=String.valueOf(System.currentTimeMillis());
            dirPath=defDir+File.separator+nomeCartella+suffixDir;
            directory = new File(dirPath);
        }

        directory.mkdir();

        //creo il file html sul file nella cartella precedentemente creata
        String fileSource=this.creaFile(directory.getAbsolutePath()+File.separator+"tmpWebPage.html",params.get("htmlText"));
        String fileSourceHeader=this.creaFile(directory.getAbsolutePath()+File.separator+"headerWebPage.html",params.get("headerHtmlText"));
        String fileSourcefooter=this.creaFile(directory.getAbsolutePath()+File.separator+"footerWebPage.html",params.get("footerHtmlText"));
        String footerOption= (!fileSourcefooter.isEmpty()) ? "--footer-html "+fileSourcefooter: "";
        String headerOption=(!fileSourceHeader.isEmpty())? "--header-html "+fileSourceHeader:"";
        String fileDest=  directory.getAbsolutePath()+File.separator+"tmpWebPage.pdf";

        String cmd=null;
        String options=null;

        try {
            options = env.getProperty("html2pdf.options");
            if(params.get("options")!=null && !StringUtils.isEmpty(params.get("options"))) {
                //in caso di option esistenti nei parametri della richiesta allora sovrascrivo la variabile
                options = params.get("options");
            }
            if(StringUtils.isEmpty(options)){
                options = "--page-size A4 --dpi 300";
            }
        } catch (Exception e) {
            e.printStackTrace();
            options = "--page-size A4 --dpi 300";
        }


        try {
            cmd = env.getProperty("html2pdf.cmd");
            if(StringUtils.isEmpty(cmd)){
                cmd = "wkhtmltopdf";
            }
        } catch (Exception e) {
            e.printStackTrace();
            cmd = "wkhtmltopdf";
        }

        String cmdExec=cmd+" "+options+" "+footerOption+" "+headerOption+" "+fileSource+" "+fileDest;
        Process process=null;
        try{
            Runtime rt = Runtime.getRuntime();
            process = rt.exec(cmdExec);

            //resto in attesa della risposta dal processo
            StringBuilder output = new StringBuilder();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            String line2=null;
            while ((line2 = reader.readLine()) != null) {
                output.append(line2 + "\n");
            }

            int exitVal = process.waitFor();
            if (exitVal == 0) {
                System.out.println("Success!");
                System.out.println(output);
            } else {
                //abnormal...
                System.out.println("No Success!");
            }

        }
        catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        finally {
            process.destroy();
        }

        Map<String,String> responseMap = new HashMap();
        responseMap.put("filePathPDF",fileDest);

        return responseMap;
    }



    private String creaFile(String pathName, String contenuto) throws IOException {

        String fileSource="";
        if(contenuto!=null && !contenuto.isEmpty()) {
            File file = new File(pathName);
            Writer out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(file), "UTF8"));
            out.append(contenuto);
            out.flush();
            out.close();
            //preparo i parametri da passare al processo
            fileSource = file.getAbsolutePath();
        }
        return  fileSource;
    }

    @Override
    public void setEnvironment(Environment environment) {

    }


    @Override
    public void deleteTableValue( DBManager dbManager, String tableName, String columnName, Object processInstanceId) throws SQLException {

        String deleteQuery = "Delete from "+tableName+" where "+columnName+"  = ?";
        List<Object> values = new ArrayList<>();
        values.add(processInstanceId);
        dbManager.executeUpdate(deleteQuery, values);

    }

    public void deleteTableValueIn( DBManager dbManager, String tableName, String columnName, String valuesIn) throws SQLException{

        int maxDelete = 1000;
        if(!Strings.isNullOrEmpty(valuesIn)) {
            String deleteQuery = "Delete from " + tableName + " where " + columnName + "  in (?)";
            List<Object> values = new ArrayList<>();
            String[] toDelete = StringUtils.split(valuesIn,",");
            if(toDelete.length<maxDelete) {
                values.add(valuesIn);
                dbManager.executeUpdateRaw(deleteQuery, values);
            }else{
                int numArray = (toDelete.length / maxDelete)  + (toDelete.length % maxDelete)>0 ? 1:0;
                int index = 0;
                for(int i=0;i<numArray; i++) {
                    int end  = index+maxDelete;
                    end = end<toDelete.length ? end: toDelete.length;
                    String[] iesimoArray = Arrays.copyOfRange(toDelete, index, end);
                    String arrayString = StringUtils.join(iesimoArray, ",");
                    values  = new ArrayList<>();
                    values.add(arrayString);
                    dbManager.executeUpdateRaw(deleteQuery, values);
                    index = end;
                }
            }
        }
    }


    public void updateStateNodeInstanceStates(Object processInstanceId, int newState,  DBManager dbManagerMysql) throws SQLException{
        String updateStato = "Update NodeInstanceStates set stato = ?, aggiornamento = ? where processInstanceId = ? ";
        List<Object> values = new ArrayList<>();
        values.add(newState);
        values.add(new Date());
        values.add(processInstanceId);
        dbManagerMysql.executeUpdate(updateStato, values);
    }




    public void restoreDb(String dbPath, String tableName, String processInstanceId) throws Exception{
        HashMap<String, String> mysqlConnectionProperty = getApplicationDBConnection();

        DBManager dbManagerMysql = DBManager.getInstance(mysqlConnectionProperty);

        File newDbFile = new File(dbPath);

        if(newDbFile.exists()){
            HashMap<String, String> sqliteConnectionProperty = new HashMap<>();
            sqliteConnectionProperty.put(DBManager.DB_FILEPATH, dbPath);
            DBManager dbManagerSqlite = DBManager.getInstance(DBManager.DbType.sqlite.toString(), sqliteConnectionProperty);


            String queryNodeInstanceLog = "Select * from " + tableName + " where processInstanceId  = ?";
            List<Object> parQuery = new ArrayList<>();
            parQuery.add(processInstanceId);
            GenericResultSet resSet = dbManagerSqlite.executeSelect(queryNodeInstanceLog, parQuery);

            String insertSqliteScript = "Insert into " + tableName + " (";
            int i = 1;
            for (String colNames : resSet.getColumnNames()) {
                if (i == resSet.getColumnNames().length)
                    insertSqliteScript += colNames;
                else
                    insertSqliteScript += colNames + ",";

                i++;
            }
            insertSqliteScript += ") values (";
            i = 1;
            for (String colNames : resSet.getColumnNames()) {
                if (i == resSet.getColumnNames().length)
                    insertSqliteScript += "?";
                else
                    insertSqliteScript += "?,";

                i++;
            }
            insertSqliteScript += ");";

            for (Object[] valori : resSet.getValori()) {
                List<Object> toInsert = new ArrayList<>();
                toInsert.addAll(Arrays.asList(valori));

                dbManagerMysql.executeUpdate(insertSqliteScript, toInsert);
            }


        }


    }

    public void backupDb(String dbTable, Object[] val, DBManager dbManagerMysql, boolean removefile) throws Exception{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            //Insert into sqlite db
            String dbFolder = env.getProperty("db.folder");
            if (!new File(dbFolder).exists()) {
                Files.createDirectory(Paths.get(dbFolder));
            }

            String queryNodeInstanceLog = "Select * from " + dbTable + " where processInstanceId  = ?";
            List<Object> parQuery = new ArrayList<>();
            parQuery.add(val[0]);
            GenericResultSet resSet = dbManagerMysql.executeSelect(queryNodeInstanceLog, parQuery);

            if (resSet != null && resSet.getValori() != null && resSet.getValori().size() > 0) {

                Calendar c = Calendar.getInstance();
                if(val[1] instanceof Date)
                    c.setTime((Date) val[1]);
                else if (val[1] instanceof String){
                    try {
                        Date d = sdf.parse(val[1].toString());
                        c.setTime(d);
                    }catch (Exception ex){
                        log.warn("Errore conversione data");
                    }
                }
                String year = "" + c.get(Calendar.YEAR);
                int monthInt = c.get(Calendar.MONTH);
                monthInt++;
                String month = "" + monthInt;
                String day = "" + c.get(Calendar.DATE);

                if (!new File(dbFolder + "/" + year).exists()) {
                    Files.createDirectory(Paths.get(dbFolder + "/" + year));
                }

                if (!new File(dbFolder + "/" + year + "/" + month).exists()) {
                    Files.createDirectory(Paths.get(dbFolder + "/" + year + "/" + month));
                }

                if (!new File(dbFolder + "/" + year + "/" + month + "/" + day).exists()) {
                    Files.createDirectory(Paths.get(dbFolder + "/" + year + "/" + month + "/" + day));
                }


                String dbFilePath = dbFolder + "/" + year + "/" + month + "/" + day + "/" + val[0] + ".db3";
                String name="default.db3";
                String defaultDbFile = dbFolder + "/default.db3";

                File defaultDbFilef = new File(defaultDbFile);
                if(!defaultDbFilef.exists()){
                    try(InputStream is = ServerProperties.class.getResourceAsStream("/"+name)) {
                        FileUtils.copyInputStreamToFile(is, defaultDbFilef);
                    }catch (Exception e){
                        log.error("Impossibile creare il db di default");
                    }
                }

                File newDbFile = new File(dbFilePath);
                if(newDbFile.exists() && removefile)
                    newDbFile.delete();

                if(!newDbFile.exists())
                    FileUtils.copyFile(new File(defaultDbFile), new File(dbFilePath));

                HashMap<String, String> sqliteConnectionProperty = new HashMap<>();
                sqliteConnectionProperty.put(DBManager.DB_FILEPATH, dbFilePath);
                DBManager dbManagerSqlite = DBManager.getInstance(DBManager.DbType.sqlite.toString(), sqliteConnectionProperty);

                String insertSqliteScript = "Insert into " + dbTable + " (";
                int i = 1;
                for (String colNames : resSet.getColumnNames()) {
                    if (i == resSet.getColumnNames().length)
                        insertSqliteScript += colNames;
                    else
                        insertSqliteScript += colNames + ",";

                    i++;
                }
                insertSqliteScript += ") values (";
                i = 1;
                for (String colNames : resSet.getColumnNames()) {
                    if (i == resSet.getColumnNames().length)
                        insertSqliteScript += "?";
                    else
                        insertSqliteScript += "?,";

                    i++;
                }
                insertSqliteScript += ");";

                for (Object[] valori : resSet.getValori()) {
                    List<Object> toInsert = new ArrayList<>();

                    for(Object v: valori){
                        Object valore = null;
                        if(v instanceof Date){
                            valore = sdf.format(v);
                        }else{
                            valore = v;
                        }
                        toInsert.add(valore);
                    }

                    dbManagerSqlite.executeUpdate(insertSqliteScript, toInsert);
                }


            }
        }catch (IOException ioe){
            throw ioe;
        }catch (SQLException sqle){
            throw sqle;
        }catch (Exception ex){
            throw ex;
        }
    }


    public DBManager getSqliteDBManager(String processInstanceID, String endDate)throws Exception{

        DBManager dbManagerSqlite = null;
        String dbFolder = env.getProperty("db.folder");
        if (!new File(dbFolder).exists()) {
            Files.createDirectory(Paths.get(dbFolder));
        }

        Date d = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);

        Calendar c = Calendar.getInstance();
        c.setTime(d);
        String year = "" + c.get(Calendar.YEAR);
        int monthInt = c.get(Calendar.MONTH);
        monthInt++;
        String month = "" + monthInt;
        String day = "" + c.get(Calendar.DATE);

        if (!new File(dbFolder + "/" + year).exists()) {
            Files.createDirectory(Paths.get(dbFolder + "/" + year));
        }

        if (!new File(dbFolder + "/" + year + "/" + month).exists()) {
            Files.createDirectory(Paths.get(dbFolder + "/" + year + "/" + month));
        }

        if (!new File(dbFolder + "/" + year + "/" + month + "/" + day).exists()) {
            Files.createDirectory(Paths.get(dbFolder + "/" + year + "/" + month + "/" + day));
        }


        String dbFilePath = dbFolder + "/" + year + "/" + month + "/" + day + "/" + processInstanceID + ".db3";

        File newDbFile = new File(dbFilePath);

        if(newDbFile.exists()) {
            HashMap<String, String> sqliteConnectionProperty = new HashMap<>();
            sqliteConnectionProperty.put(DBManager.DB_FILEPATH, dbFilePath);
            dbManagerSqlite = DBManager.getInstance(DBManager.DbType.sqlite.toString(), sqliteConnectionProperty);
        }

        return dbManagerSqlite;
    }


    public void clearInstances(Map<String, Map<String, Object>> processInstanceIdArray, DBManager dbManagerMysql, boolean completely, String identifierBackup){
        try{

            for(String processId: processInstanceIdArray.keySet()){
                Map<String,Object> valueMap = processInstanceIdArray.get(processId);
                Integer retentionDay = valueMap.containsKey("retentionDay") ? (Integer)(valueMap.get("retentionDay")) : -1;
                Boolean deleteTask = valueMap.containsKey("deleteTask") ? (Boolean)(valueMap.get("deleteTask")) : false;
                String processInstanceIDsString = (valueMap.get("processInstanceId") != null && !Strings.isNullOrEmpty((String)valueMap.get("processInstanceId"))) ? ((String)valueMap.get("processInstanceId")) : null;

                //TODO DELETE

                //ELIMINO LA VARIABLE INSTANCE
                try {
                    deleteTableValueIn(dbManagerMysql, "VariableInstance", "instanceId" , processInstanceIDsString);
                }catch (Exception e){
                    log.error("ClearInstances " + identifierBackup + " - Inpossibile eliminare la variableInstance per le istanze: " + processInstanceIDsString, e);
                }

                //ELIMINO LA VARIABLEINSTANCELOG
                try {
                    deleteTableValueIn(dbManagerMysql, "VariableInstanceLog", "processInstanceId" , processInstanceIDsString);
                }catch (Exception e){
                    log.error("ClearInstances "+ identifierBackup +" - Inpossibile eliminare la variableInstance per le istanze: " + processInstanceIDsString, e);
                }

                //ELIMINO LA WorkItemInfo
                try {
                    deleteTableValueIn(dbManagerMysql, "WorkItemInfo", "processInstanceId" , processInstanceIDsString);
                }catch (Exception e){
                    log.error("ClearInstances "+ identifierBackup +" - Inpossibile eliminare la WorkItemInfo per le istanze: " + processInstanceIDsString, e);
                }



                //ELIMINO LA RequestInfo
                try {
                    String queryRequestInfo = "Select id from RequestInfo where SUBSTRING_INDEX(businessKey,':', 1) in (" + processInstanceIDsString+")";

                    GenericResultSet grsRequestInfoIds = dbManagerMysql.executeSelect(queryRequestInfo, null);

                    if(grsRequestInfoIds != null && grsRequestInfoIds.getValori() != null && grsRequestInfoIds.getValori().size()>0){

                        List<String> idRequestInfo = new ArrayList<>();

                        for(Object[] vall : grsRequestInfoIds.getValori()) {
                            String requestInfoId = vall[0].toString();
                            idRequestInfo.add(requestInfoId);
                        }
                        if(idRequestInfo.size()>0) {
                            String joinIdRequestInfo = StringUtils.join(idRequestInfo, ",");
                            if (!Strings.isNullOrEmpty(joinIdRequestInfo)) {
                                try {
                                    deleteTableValueIn(dbManagerMysql, "ErrorInfo", "REQUEST_ID", joinIdRequestInfo);
                                } catch (Exception exErrorInfo) {
                                    log.error("ClearInstances " + identifierBackup + " - Inpossibile eliminare la ErrorInfo con REQUEST_ID: " + joinIdRequestInfo, exErrorInfo);
                                }
                                try {
                                    deleteTableValueIn(dbManagerMysql, "RequestInfo", "id", joinIdRequestInfo);
                                } catch (Exception exRequestInfo) {
                                    log.error("ClearInstances " + identifierBackup + " - Inpossibile eliminare le RequestInfo con id: " + joinIdRequestInfo, exRequestInfo);
                                }
                            }
                        }
                    }

                }catch (Exception e){
                    log.error("ClearInstances " + identifierBackup + " - Inpossibile eliminare la RequestInfo o la ErrorInfo per le istanze: " + processInstanceIDsString, e);
                }

                //ELIMINO LA CorrelationKeyInfo
                try {
                    String queryCorrelationKey = "Select keyId from CorrelationKeyInfo where processInstanceId in ("+processInstanceIDsString+")";

                    GenericResultSet grsCorrelationKEyIds = dbManagerMysql.executeSelect(queryCorrelationKey, null);

                    if(grsCorrelationKEyIds != null && grsCorrelationKEyIds.getValori() != null && grsCorrelationKEyIds.getValori().size()>0) {

                        List<String> correlationKeyArray = new ArrayList<>();
                        for (Object[] vall : grsCorrelationKEyIds.getValori()) {
                            String correlationKeyId = vall[0].toString();
                            correlationKeyArray.add(correlationKeyId);
                        }
                        if (correlationKeyArray.size() > 0) {
                            String correlationKeyString = StringUtils.join(correlationKeyArray, ",");
                            if (!Strings.isNullOrEmpty(correlationKeyString)) {

                                try {
                                    deleteTableValueIn(dbManagerMysql, "CorrelationPropertyInfo", "correlationKey_keyId", correlationKeyString);
                                } catch (Exception errorCorrelationPropertyInfo) {
                                    log.error("ClearInstances " + identifierBackup + " - Inpossibile eliminare la CorrelationPropertyInfo con correlationKey_keyId: " + correlationKeyString, errorCorrelationPropertyInfo);
                                }


                                try {
                                    deleteTableValueIn(dbManagerMysql, "CorrelationKeyInfo", "keyId", correlationKeyString);
                                } catch (Exception errorCorrelationPropertyInfo) {
                                    log.error("ClearInstances " + identifierBackup + " -Inpossibile eliminare la CorrelationKeyInfo con keyId: " + correlationKeyString, errorCorrelationPropertyInfo);
                                }
                            }
                        }
                    }
                }catch (Exception e){
                    log.error("ClearInstances " +  identifierBackup +" - Inpossibile eliminare le CorrelationKeys per le istanze: " + processInstanceIDsString, e);
                }



                //ELIMINO LA ProcessInstanceInfo
                try {
                    deleteTableValueIn(dbManagerMysql, "ProcessInstanceInfo", "InstanceId" , processInstanceIDsString);
                }catch (Exception e){
                    log.error("ClearInstances " + identifierBackup + " - Inpossibile eliminare la ProcessInstanceInfo per le istanze: " + processInstanceIDsString, e);
                }

                if(completely){
                    //ELIMINO LA ProcessInstanceLog
                    //spostato qua perchè nella pulizia "normale" inibiva la visualizzazione sia in elenco che in dettaglio
                    try {
                        deleteTableValueIn(dbManagerMysql, "ProcessInstanceLog", "processInstanceId" , processInstanceIDsString);
                    }catch (Exception e){
                        log.error("ClearInstances " + identifierBackup + " - Inpossibile eliminare la ProcessInstanceLog per le istanza: " + processInstanceIDsString, e);
                    }



                    try {
                        deleteTableValueIn(dbManagerMysql, "NodeInstanceExtendedLog", "processInstanceId" , processInstanceIDsString);
                    }catch (Exception e){
                        log.error("ClearInstances " + identifierBackup + " - Inpossibile eliminare la ProcessInstanceInfo per le istanze: " + processInstanceIDsString, e);
                    }
                    //ELIMINO LA NodeInstanceLogResult
                    try {
                        deleteTableValueIn(dbManagerMysql, "NodeInstanceLogResult", "processInstanceId" , processInstanceIDsString);
                    }catch (Exception e){
                        log.error("ClearInstances " + identifierBackup + " - Inpossibile eliminare la ProcessInstanceInfo per le istanze: " + processInstanceIDsString, e);
                    }
                    //ELIMINO LA NodeInstanceLog
                    try {
                        deleteTableValueIn(dbManagerMysql, "NodeInstanceLog", "processInstanceId" , processInstanceIDsString);
                    }catch (Exception e){
                        log.error("ClearInstances " + identifierBackup + " - Inpossibile eliminare la ProcessInstanceInfo per le istanze: " + processInstanceIDsString, e);
                    }
                    try {
                        deleteTableValueIn(dbManagerMysql, "ProcessInstanceLogKDM", "id" , processInstanceIDsString);
                    }catch (Exception e){
                        log.error("ClearInstances " + identifierBackup + " - Inpossibile eliminare la ProcessInstanceLogKDM per le istanze: " + processInstanceIDsString, e);
                    }

                    try {
                        deleteTableValueIn(dbManagerMysql, "ProcessInstanceSecurity", "instanceId" , processInstanceIDsString);
                    }catch (Exception e){
                        log.error("ClearInstances " + identifierBackup + " - Inpossibile eliminare la ProcessInstanceLogKDM per le istanze: " + processInstanceIDsString, e);
                    }

                    try {
                        deleteTableValueIn(dbManagerMysql, "ProcessDetails", "processInstanceId" , processInstanceIDsString);
                    }catch (Exception e){
                        log.error("ClearInstances " + identifierBackup + " - Inpossibile eliminare la ProcessInstanceLogKDM per le istanze: " + processInstanceIDsString, e);
                    }


                    if(deleteTask){
                        //Recupero i taskId da eliminare
                        String queryTaskIds = "Select id, workItemId, documentContentId, faultContentId, outputContentId from Task where processInstanceId in ("+processInstanceIDsString+")";
                        String taskIdsString = "";
                        String workItemIdsString = "";
                        String documentContentIdsString = "";
                        String faultContentIdsString = "";
                        String outputContentIdsString = "";

                        try{
                            GenericResultSet grsTasks = dbManagerMysql.executeSelect(queryTaskIds, null);

                            if(grsTasks != null && grsTasks.getValori() != null && grsTasks.getValori().size()>0) {

                                List<String> taskIdsArray = new ArrayList<>();
                                List<String> workItemIdsArray = new ArrayList<>();
                                List<String> documentIdsArray = new ArrayList<>();
                                List<String> faultIdsArray = new ArrayList<>();
                                List<String> outputIdsArray = new ArrayList<>();
                                for (Object[] vall : grsTasks.getValori()) {
                                    String taskId = vall[0].toString();
                                    String workItemId = vall[1].toString();
                                    String documentContentId = vall[2] != null ? vall[2].toString() : null;
                                    String faultContentId = vall[3] != null ? vall[3].toString() : null;
                                    String outputContentId = vall[4] != null ? vall[4].toString() : null;

                                    taskIdsArray.add(taskId);
                                    workItemIdsArray.add(workItemId);
                                    if(documentContentId != null)documentIdsArray.add(documentContentId);
                                    if(faultContentId != null)faultIdsArray.add(faultContentId);
                                    if(outputContentId != null)outputIdsArray.add(outputContentId);
                                }

                                if(taskIdsArray.size()>0){
                                    taskIdsString = StringUtils.join(taskIdsArray,",");
                                }
                                if(workItemIdsArray.size()>0){
                                    workItemIdsString = StringUtils.join(workItemIdsArray, ",");
                                }
                                if(documentIdsArray.size()>0){
                                    documentContentIdsString = StringUtils.join(documentIdsArray, ",");
                                }
                                if(faultIdsArray.size()>0){
                                    faultContentIdsString = StringUtils.join(faultIdsArray, ",");
                                }
                                if(outputIdsArray.size()>0){
                                    outputContentIdsString = StringUtils.join(outputIdsArray, ",");
                                }
                            }
                        }catch (Exception e){

                        }

                        if(!Strings.isNullOrEmpty(taskIdsString)){
                            //Attachment
                            try {
                                deleteTableValueIn(dbManagerMysql, "Attachment", "TaskData_Attachments_Id" , taskIdsString);
                            }catch (Exception e){
                                log.error("ClearInstances " + identifierBackup + " - Inpossibile eliminare la Attachment per i taskid : " + taskIdsString, e);
                            }
                            //PeopleAssignments_PotOwners
                            try {
                                deleteTableValueIn(dbManagerMysql, "PeopleAssignments_PotOwners", "task_Id" , taskIdsString);
                            }catch (Exception e){
                                log.error("ClearInstances " + identifierBackup + " - Inpossibile eliminare la PeopleAssignments_PotOwners per i taskid : " + taskIdsString, e);
                            }
                            //PeopleAssignments_ExclOwners
                            try {
                                deleteTableValueIn(dbManagerMysql, "PeopleAssignments_ExclOwners", "task_Id" , taskIdsString);
                            }catch (Exception e){
                                log.error("ClearInstances " + identifierBackup + " - Inpossibile eliminare la PeopleAssignments_ExclOwners per i taskid : " + taskIdsString, e);
                            }

                            //PeopleAssignments_Recipients
                            try {
                                deleteTableValueIn(dbManagerMysql, "PeopleAssignments_Recipients", "task_Id" , taskIdsString);
                            }catch (Exception e){
                                log.error("ClearInstances " + identifierBackup + " - Inpossibile eliminare la PeopleAssignments_Recipients per i taskid : " + taskIdsString, e);
                            }
                            //PeopleAssignments_Stakeholders
                            try {
                                deleteTableValueIn(dbManagerMysql, "PeopleAssignments_Stakeholders", "task_Id" , taskIdsString);
                            }catch (Exception e){
                                log.error("ClearInstances " + identifierBackup + " - Inpossibile eliminare la PeopleAssignments_Stakeholders per i taskid : " + taskIdsString, e);
                            }
                            //PeopleAssignments_BAs
                            try {
                                deleteTableValueIn(dbManagerMysql, "PeopleAssignments_BAs", "task_Id" , taskIdsString);
                            }catch (Exception e){
                                log.error("ClearInstances " + identifierBackup + " - Inpossibile eliminare la PeopleAssignments_BAs per i taskid : " + taskIdsString, e);
                            }
                            //I18NText Task_Names_Id
                            try {
                                deleteTableValueIn(dbManagerMysql, "I18NText", "Task_Names_Id" , taskIdsString);
                            }catch (Exception e){
                                log.error("ClearInstances " + identifierBackup + " - Inpossibile eliminare la I18NText ->Task_Names_Id  per i taskid : " + taskIdsString, e);
                            }
                            //I18NText Task_Descriptions_Id
                            try {
                                deleteTableValueIn(dbManagerMysql, "I18NText", "Task_Descriptions_Id" , taskIdsString);
                            }catch (Exception e){
                                log.error("ClearInstances " + identifierBackup + " - Inpossibile eliminare la I18NText ->Task_Descriptions_Id  per i taskid : " + taskIdsString, e);
                            }
                            //I18NText Task_Subjects_Id
                            try {
                                deleteTableValueIn(dbManagerMysql, "I18NText", "Task_Subjects_Id" , taskIdsString);
                            }catch (Exception e){
                                log.error("ClearInstances " + identifierBackup + " - Inpossibile eliminare la I18NText ->Task_Subjects_Id  per i taskid : " + taskIdsString, e);
                            }
                            //task_comment
                            try {
                                deleteTableValueIn(dbManagerMysql, "task_comment", "TaskData_Comments_Id" , taskIdsString);
                            }catch (Exception e){
                                log.error("ClearInstances " + identifierBackup + " - Inpossibile eliminare la task_comment  per i taskid : " + taskIdsString, e);
                            }

                            //AuditTaskImpl
                            try {
                                deleteTableValueIn(dbManagerMysql, "AuditTaskImpl", "task_Id" , taskIdsString);
                            }catch (Exception e){
                                log.error("ClearInstances " + identifierBackup + " - Inpossibile eliminare la AuditTaskImpl  per i taskid : " + taskIdsString, e);
                            }
                            //BAMTaskSummary
                            try {
                                deleteTableValueIn(dbManagerMysql, "BAMTaskSummary", "task_Id" , taskIdsString);
                            }catch (Exception e){
                                log.error("ClearInstances " + identifierBackup + " - Inpossibile eliminare la BAMTaskSummary  per i taskid : " + taskIdsString, e);
                            }
                            //TaskEvent
                            try {
                                deleteTableValueIn(dbManagerMysql, "TaskEvent", "taskId" , taskIdsString);
                            }catch (Exception e){
                                log.error("ClearInstances " + identifierBackup + " - Inpossibile eliminare la TaskEvent  per i taskid : " + taskIdsString, e);
                            }
                            //TaskDetails
                            try {
                                deleteTableValueIn(dbManagerMysql, "TaskDetails", "workItemId" , workItemIdsString);
                            }catch (Exception e){
                                log.error("ClearInstances " + identifierBackup + " - Inpossibile eliminare la TaskDetails  per i workItemId : " + workItemIdsString, e);
                            }

                            //Content documentContentIdsString
                            try {
                                deleteTableValueIn(dbManagerMysql, "Content", "id" , documentContentIdsString);
                            }catch (Exception e){
                                log.error("ClearInstances " + identifierBackup +  " - Inpossibile eliminare la Content  per ids : " + documentContentIdsString, e);
                            }
                            //Content faultContentIdsString
                            try {
                                deleteTableValueIn(dbManagerMysql, "Content", "id" , faultContentIdsString);
                            }catch (Exception e){
                                log.error("ClearInstances " + identifierBackup + " - Inpossibile eliminare la Content  per ids : " + faultContentIdsString, e);
                            }
                            //Content faultContentIdsString
                            try {
                                deleteTableValueIn(dbManagerMysql, "Content", "id" , outputContentIdsString);
                            }catch (Exception e){
                                log.error("ClearInstances  " + identifierBackup + " - Inpossibile eliminare la Content  per ids : " + outputContentIdsString, e);
                            }
                            //TaskEvent
                            try {
                                deleteTableValueIn(dbManagerMysql, "Task", "id" , taskIdsString);
                            }catch (Exception e){
                                log.error("ClearInstances " + identifierBackup + " - Inpossibile eliminare la Task  per ids : " + taskIdsString, e);
                            }

                        }

                    }

                }

            }

        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
    }

    public void deleteAllTable(DBManager dbmanager, Object processInstanceID) throws SQLException{
        deleteTableValue(dbmanager, "NodeInstanceLog", "processInstanceId", processInstanceID);

        try {
            deleteTableValue(dbmanager, "NodeInstanceLogResult", "processInstanceId", processInstanceID);
        }catch (Exception e1){
            log.error("Errore rimozione records NodeInstanceLogResult per processInstanceID = "+processInstanceID+" " + e1.getMessage());
        }
        try {
            deleteTableValue(dbmanager, "VariableInstanceLog", "processInstanceId", processInstanceID);
        }catch (Exception e2){
            log.error("Errore rimozione records VariableInstanceLog per processInstanceID = "+processInstanceID+" " + e2.getMessage());
        }
        try {
            deleteTableValue(dbmanager, "NodeInstanceExtendedLog", "processInstanceId", processInstanceID);
        }catch (Exception e3){
            log.error("Errore rimozione records NodeInstanceExtendedLog per processInstanceID = "+processInstanceID+" " + e3.getMessage());
        }
        //Change column into InstanceID
        try {
            deleteTableValue(dbmanager, "VariableInstance", "instanceId", processInstanceID);
        }catch (Exception e4){
            log.error("Errore rimozione records VariableInstance per processInstanceID = "+processInstanceID+" " + e4.getMessage());
        }

        String querySelectForRequestInfo = "select id from RequestInfo where businessKey like '"+processInstanceID+"%' or (businessKey is null and status = ?) ";
        List<Object> par = new ArrayList<>();
        par.add("DONE");
        GenericResultSet grs = dbmanager.executeSelect(querySelectForRequestInfo, par);
        if(grs != null && grs.getValori() != null && grs.getValori().size()>0){
            List<Object[]> records = grs.getValori();
            for(Object[] obj: records){
                if(obj[0] != null){
                    try {
                        deleteTableValue(dbmanager, "RequestInfo", "id", obj[0]);
                    }catch (Exception e5){
                        log.error("Errore rimozione records RequestInfo per id = "+obj[0]+" " + e5.getMessage());
                    }
                }
            }
        }


    }




    public File compress(List<File> files, String password , String comment ) {

        try {

            File dest = Tools.tempFile("zip");

            String destinationPath = dest.getPath();

            ZipFile zipFile = new ZipFile(destinationPath);

            ZipParameters parameters = new ZipParameters();
            parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
            parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);

            if (!Strings.isNullOrEmpty(password)){
                parameters.setEncryptFiles(true);
                parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_AES);
                parameters.setAesKeyStrength(Zip4jConstants.AES_STRENGTH_256);
                parameters.setPassword(password);
            }

            for ( File f : files ){
                if (f.isDirectory())
                    zipFile.addFolder(f,parameters);
                else
                    zipFile.addFile(f,parameters);
            }

            if (!Strings.isNullOrEmpty(comment)){
                zipFile.setComment(comment);
            }

            return dest;
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    public List<File> uncompress(File file, String password){
        List<File> files = new ArrayList<>();

        try {

            File dest = Tools.tempFile(null);

            String destinationPath = dest.getPath();

            ZipFile zipFile = new ZipFile(file);

            if (!Strings.isNullOrEmpty(password))
                zipFile.setPassword(password);

            zipFile.extractAll(destinationPath);

            List<FileHeader> headers = zipFile.getFileHeaders();

            for( FileHeader header : headers ){
                String fileName = header.getFileName();
                files.add(new File(destinationPath,fileName));
            }

            return files;
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

}
