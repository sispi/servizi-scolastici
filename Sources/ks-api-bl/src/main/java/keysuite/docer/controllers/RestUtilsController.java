package keysuite.docer.controllers;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import io.swagger.annotations.*;
import it.kdm.doctoolkit.utils.Utils;
import it.kdm.doctoolkit.zookeeper.ApplicationProperties;
import it.kdm.orchestratore.beans.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import keysuite.docer.server.RestUtilsService;
import keysuite.docer.utils.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.HandlerMapping;

@Controller
@RequestMapping("rest-utils")
@Api(description = "RestUtils Controller")
public class RestUtilsController {

    @Autowired
    RestUtilsService restUtilsService;
    Logger log = LoggerFactory.getLogger(RestUtilsController.class);

    @RequestMapping(value ="/convert-html2pdf/convertHtmlText2PDF", method = RequestMethod.POST, produces="application/json")
    @ApiOperation(value = "getPathPDFFromHtmlText" , notes = "restituisce il path del pdf generato dall'html")
    public @ResponseBody
    Map getPDFfromHTML(HttpServletRequest request, HttpServletResponse response) throws IOException, InterruptedException {

        Map<String,String> params = new HashMap();
        Gson gson = new Gson();
        JsonReader jsonReader = null;
        try{
            jsonReader = new JsonReader(new InputStreamReader(request.getInputStream(), "ISO-8859-1"));
        }catch (Exception ex1){
            ex1.printStackTrace();
            throw new RuntimeException("Errore nella lettura dell'InputStream");
        }
        try{
            if(jsonReader != null) {
                params = gson.fromJson(jsonReader, Map.class);
            }
        }catch (Exception ex2){
            ex2.printStackTrace();
            throw new RuntimeException("Errore nella conversione dell'inputstream in mappa");
        }

        Iterator it2 = request.getParameterMap().entrySet().iterator();
        while (it2.hasNext()) {
            Map.Entry pair = (Map.Entry) it2.next();
            String key= (String) pair.getKey();
            String[] stringList= (String[]) pair.getValue();
            String value= stringList[0];
            params.put(key,value);
        }

        return restUtilsService.getPDFfromHTML(params) ;
    }

    @RequestMapping(value = "/mails/{emailAddress}/{folder}", method = RequestMethod.GET, produces = "application/json")
    @ApiOperation(value = "getMails", notes = "Restituisce le mails in funzione dei parametri di input")
    public @ResponseBody
    ResultCall<List<ImapMailItemList>> getMails(
            @ApiParam(value = "test value", example = "2017-03-27T15:09:29.00Z") @RequestParam(value = "lastTimestamp", required = false) String lastTimestamp, // usare @ApiParam(value={description}) per documentare pin
            @RequestParam(value = RestUtilsService.HOST_MAIL) String host,
            @PathVariable(value = RestUtilsService.EMAIL_ADDRESS) String email,
            @RequestParam(value = RestUtilsService.USERNAME) String username,
            @RequestParam(value = RestUtilsService.PORT) Integer port,
            @RequestParam(value = RestUtilsService.PASSWORD) String password,
            @RequestParam(value = RestUtilsService.PROVIDER) String provider,
            @RequestParam(value = RestUtilsService.SSL) Boolean ssl,
            @RequestParam(value = RestUtilsService.LIMIT, required = false, defaultValue = "10") Integer limit,
            @PathVariable(value = RestUtilsService.FOLDER) String folder
    ) {


        String pass = PasswordUtils.hashPassword(password);
        ResultCall<List<ImapMailItemList>> result = new ResultCall<List<ImapMailItemList>>();
        result.setData(null);
        result.setListSize(0);
        result.setMessage("Errore, password non valida");
        result.setStatus(0);

        Map<String, Object> conf = new HashMap<String, Object>();

        conf.put(RestUtilsService.HOST_MAIL, host);
        conf.put(RestUtilsService.EMAIL_ADDRESS, email);
        conf.put(RestUtilsService.USERNAME, username);
        conf.put(RestUtilsService.PORT, port);
        conf.put(RestUtilsService.PASSWORD, password);
        conf.put(RestUtilsService.PROVIDER, provider);
        conf.put(RestUtilsService.SSL, ssl);
        conf.put(RestUtilsService.FOLDER, folder);


        result = restUtilsService.getLastImapList(conf, lastTimestamp, limit);
        return result;
    }


    @RequestMapping(value = "/imapMail", method = RequestMethod.GET, produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "200 - Mail letta con successo"),
            @ApiResponse(code = 204, message = "204 - Mail da Controllare"),
            @ApiResponse(code = 500, message = "Errore generico")})
    public @ResponseBody
    EmailBean getMail(
            @RequestParam(value = RestUtilsService.MESSAGE_ID) String messageId,
            @RequestParam(value = RestUtilsService.HOST_MAIL) String host,
            @RequestParam(value = RestUtilsService.EMAIL_ADDRESS) String email,
            @RequestParam(value = RestUtilsService.USERNAME) String username,
            @RequestParam(value = RestUtilsService.PORT) Integer port,
            @RequestParam(value = RestUtilsService.PASSWORD) String password,
            @RequestParam(value = RestUtilsService.PROVIDER) String provider,
            @RequestParam(value = RestUtilsService.SSL) Boolean ssl,
            @RequestParam(value = RestUtilsService.FOLDER) String folder,
            @RequestParam(value = RestUtilsService.FILE_TYPE, required = false, defaultValue = "url") String fileType, HttpServletRequest request, HttpServletResponse response) {


        String alternativeDownloadPath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/rest-utils/file";

        request.getRequestURL().toString();

        ResultCall<EmailBean> result = new ResultCall<EmailBean>();
        result.setData(null);
        result.setListSize(0);
        result.setMessage("Errore, password non valida");
        result.setStatus(0);

        Map<String, Object> conf = new HashMap<String, Object>();

        conf.put(RestUtilsService.HOST_MAIL, host);
        conf.put(RestUtilsService.EMAIL_ADDRESS, email);
        conf.put(RestUtilsService.USERNAME, username);
        conf.put(RestUtilsService.PORT, port);
        conf.put(RestUtilsService.PASSWORD, password);
        conf.put(RestUtilsService.PROVIDER, provider);
        conf.put(RestUtilsService.SSL, ssl);
        conf.put(RestUtilsService.FOLDER, folder);
        try {
            result = restUtilsService.getImapMail(conf, messageId, fileType, alternativeDownloadPath);
            response.setStatus(result.getStatus());
        } catch (Exception e) {
            result.setMessage("formato lastDate non valido");
        }
        return result.getData();
    }

    @RequestMapping(value = "/file/**", method = RequestMethod.GET)
    public @ResponseBody
    void getFile(HttpServletRequest request,
                 @RequestParam(value = RestUtilsService.FILENAME) String fileName, HttpServletResponse response) {

        String pathUri = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        pathUri = StringUtils.removeStartIgnoreCase(pathUri, "/rest-utils/file");
        File fileParent = new File(pathUri);

        File filein = new File(fileParent, fileName);


        if (!filein.exists()) {
            try {
                filein = new File(fileParent, URLDecoder.decode(fileName, "UTF-8"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (!filein.exists()) {
            File path = new File(Utils.getConfigHome(), "upload");
            File defFile = new File(path, pathUri);
            if (defFile.exists()) {
                try {
                    filein = new File(defFile, fileName);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (!filein.exists()) {
                    try {
                        filein = new File(defFile, URLDecoder.decode(fileName, "UTF-8"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }


        //logger.info("getFileMultiplePath: {}", filein.getAbsolutePath());
        if (filein.exists()) {
            response.setContentType(Tools.getMimeType(filein));
            response.setContentLength((int) filein.length());
            try {
                InputStream in = FileUtils.openInputStream(filein);
                FileCopyUtils.copy(in, response.getOutputStream());
                response.setHeader("Content-disposition", "attachment; filename=" + fileName);
                response.setStatus(HttpServletResponse.SC_OK);
            } catch (IOException ee) {
                ee.printStackTrace();
            }
        } else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            throw new RuntimeException("getFileMultiplePath file (" + filein.toString() + ") non presente o non leggibile");

        }
    }

    @RequestMapping(value = "/getFile/**", method = RequestMethod.GET)
    public @ResponseBody
    void getGetFile(HttpServletRequest request,
                    @RequestParam(value = RestUtilsService.FILENAME, defaultValue = "") String fileName, HttpServletResponse response) {

        String pathUri = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        pathUri = StringUtils.removeStartIgnoreCase(pathUri, "/rest-utils/getFile");
        File fileParent = new File(pathUri);

        File filein = new File(fileParent, fileName);


        if (!filein.exists()) {
            try {
                filein = new File(fileParent, URLDecoder.decode(fileName, "UTF-8"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        if (!filein.exists()) {
            File path = new File(Utils.getConfigHome(), "upload");
            File defFile = new File(path, pathUri);
            if (defFile.exists()) {
                try {
                    filein = new File(defFile, fileName);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (!filein.exists()) {
                    try {
                        filein = new File(defFile, URLDecoder.decode(fileName, "UTF-8"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }


        //logger.info("getFileMultiplePath: {}", filein.getAbsolutePath());
        if (filein.exists()) {
            response.setContentType(Tools.getMimeType(filein));
            response.setContentLength((int) filein.length());
            try {
                InputStream in = FileUtils.openInputStream(filein);
                FileCopyUtils.copy(in, response.getOutputStream());
                response.setHeader("Content-disposition", "attachment; filename=" + fileName);
                response.setStatus(HttpServletResponse.SC_OK);
            } catch (IOException ee) {
                ee.printStackTrace();
            }
        } else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            throw new RuntimeException("getFileMultiplePath file (" + filein.toString() + ") non presente o non leggibile");

        }

    }

    @RequestMapping(value = "/mails/clearAllTempFolder", method = RequestMethod.GET, produces = "application/json")
    @ApiOperation(value = "clearAllTempFolder", notes = "Pulisce tutti i file temporanei delle mails scaricate")
    //notes = description del blocco value=nome del blocco
    public @ResponseBody
    ResultCall<String> clearAllTempFolder() {
        Calendar startDate = Calendar.getInstance();
        startDate.setTime(new Date());

        ResultCall<String> result = new ResultCall<String>();
        result.setData(null);
        result.setMessage("Errore interno");
        result.setStatus(0);
        Boolean res = false;
        try {
            res = restUtilsService.clearAllTempFile();
            result.setData(res.toString());
            result.setMessage("Success");
            result.setStatus(1);
            Calendar endDate = Calendar.getInstance();
            endDate.setTime(new Date());
            long duration = endDate.getTimeInMillis() - startDate.getTimeInMillis();
            result.setDuration(duration);
            result.setListSize(0);
        } catch (Exception ioe) {
            ioe.printStackTrace();
            result.setMessage(ioe.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "/mails/clearAllMsgIdFiles", method = RequestMethod.GET, produces = "application/json")
    @ApiOperation(value = "clearAllMsgIdFiles", notes = "Pulisce tutti i file temporanei della mail con msgid indicato")
    //notes = description del blocco value=nome del blocco
    public @ResponseBody
    ResultCall<Boolean> clearAllMsgIdFiles(@RequestParam(value = RestUtilsService.MESSAGE_ID) String messageId) {
        ResultCall<Boolean> result = new ResultCall<Boolean>();
        result.setData(true);
        result.setMessage("File messaggio: "+messageId+" eliminati con successo");
        result.setStatus(1);
        final String msgid = messageId;

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Calendar startDate = Calendar.getInstance();
                startDate.setTime(new Date());

                try {
                    restUtilsService.clearAllMsgIdFiles(msgid);
                    Calendar endDate = Calendar.getInstance();
                    endDate.setTime(new Date());
                    long duration = endDate.getTimeInMillis() - startDate.getTimeInMillis();
                } catch (Exception ioe) {
                    log.error("Errore nella rimozione dei file relativi alla mail con msgid: " + msgid, ioe);
                }
            }
        }, 0);


        return result;
    }

    @RequestMapping(value = "/removeFileOrFolder", method = RequestMethod.GET, produces = "application/json")
    @ApiOperation(value = "removeFileOrFolder", notes = "Elimina il file o folder del path specifico")
    public @ResponseBody
    ResultCall<Boolean> removeFileOrFolder(@RequestParam(value = "fullPath", required = true) String fullPath, HttpServletRequest request, HttpServletResponse response) {

        ResultCall<Boolean> result = new ResultCall<Boolean>();
        result.setStatus(1);
        result.setMessage("Il path: " + fullPath +" è stato eliminato con successo");

        Tools.removeQuietly(fullPath);


        return result;
    }




    @RequestMapping(value ="/node-instance/deepClearInstances", method = RequestMethod.GET, produces = "application/json")
    @ApiOperation(value = "deepClearInstances" , notes = "Deep clear dei processi")
    public @ResponseBody
    ResultCall<GenericResultSet> deepClearInstances(@RequestParam(value = "max_instance", defaultValue = "50", required = false) Integer maxInstance, @RequestParam(value="processId", required = false) String processId) {
        ResultCall<GenericResultSet> result = new ResultCall<>();
        Calendar startDate = Calendar.getInstance();
        startDate.setTime(new Date());
        String identifierBackup = ""+startDate.getTime().getTime();
        log.warn("DeepClearInstances-LOG "+identifierBackup+": START AT "+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startDate.getTime()));

        HashMap<String, HashMap<String, Object>> processConfiguration = new HashMap<>();
        DBManager dbManagerMysql=null;
        String processConfigurationQuery=null;
        try {
            HashMap<String, String> mysqlConnectionProperty = Tools.getApplicationDBConnection();
            dbManagerMysql = DBManager.getInstance(mysqlConnectionProperty);

        }catch (Exception e){
            throw  new RuntimeException(e);
        }

        if(processId != null){
            processConfigurationQuery = "Select processId, retention_day, deleteTask from ProcessConfiguration where processId = '"+processId+"' and retention_day > -1";
        }else{
            processConfigurationQuery = "Select processId, retention_day, deleteTask from ProcessConfiguration where retention_day > -1";
        }
        log.warn("DeepClearInstances-LOG "+identifierBackup+": prima query:: "+processConfigurationQuery);

        try {
            GenericResultSet grs = dbManagerMysql.executeSelect(processConfigurationQuery, null);

            if (grs != null && grs.getValori() != null && grs.getValori().size() > 0) {
                for (Object[] vall : grs.getValori()) {
                    HashMap<String, Object> val = new HashMap<>();
                    val.put("retention_day",vall[1] != null ? new Integer(vall[1].toString()): null );

                    Object deleteTaskObj = vall[2];
                    Boolean deleteTask = new Boolean("false");
                    if(deleteTaskObj != null) {
                        if (deleteTaskObj instanceof Boolean) {
                            deleteTask = (Boolean) deleteTaskObj;
                        } else if( deleteTaskObj instanceof Number){
                            if(new Integer(vall[2].toString()).intValue() ==1){
                                deleteTask = new Boolean("true");
                            }
                        }else if(deleteTaskObj instanceof String){
                            deleteTask = new Boolean(deleteTaskObj.toString());
                        }
                    }
                    val.put("deleteTask",deleteTask);
                    processConfiguration.put(vall[0].toString(), val);
                }
            }
        }catch (Exception ex1){
            log.error("DeepClearInstances-LOG "+identifierBackup+": Impossibile leggere la processConfiguration", ex1);
            result.setStatus(500);
            result.setMessage(ex1.getMessage());
            throw new RuntimeException(ex1);
        }finally {
            result.setDuration(System.currentTimeMillis()-startDate.getTimeInMillis());
        }

        for(String procId: processConfiguration.keySet()){

            Integer retentionDay = (Integer) processConfiguration.get(procId).get("retention_day");
            Boolean deleteTask = (Boolean) processConfiguration.get(procId).get("deleteTask");

            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            cal.add(Calendar.DATE, -retentionDay);
            Date compareDate = cal.getTime();
            String compareDateStr = new SimpleDateFormat("yyyy-MM-dd").format(compareDate);

            String queryNodeInstanceLog ="select q.processInstanceId from " +
                    "(Select nil.processInstanceId from NodeInstanceLog nil  " +
                    "where processId = '"+procId+"' " +
                    "and not exists (SELECT 1 from ProcessInstanceInfo pii where nil.processInstanceId = pii.InstanceId) " +
                    "group by nil.processInstanceId having max(nil.log_date) < '"+compareDateStr+"' )q " +
                    "order by q.processInstanceId asc";

            log.warn("DeepClearInstances-LOG "+identifierBackup+" query per select configuration: "+queryNodeInstanceLog);

            List<String> procInstanceIdFromNodeInstanceLog = new ArrayList<>();
            String procInstanceIdFromNodeInstanceLogString = "";

            try {
                GenericResultSet grs = dbManagerMysql.executeSelect(queryNodeInstanceLog, null, maxInstance);

                if (grs != null && grs.getValori() != null && grs.getValori().size() > 0) {
                    for (Object[] vall : grs.getValori()) {
                        HashMap<String, Object> val = new HashMap<>();
                        procInstanceIdFromNodeInstanceLog.add(vall[0].toString());
                    }
                }
            }catch (Exception ex1){
                log.error("DeepClearInstances-LOG "+identifierBackup+": Impossibile leggere la processConfiguration", ex1);
                result.setStatus(500);
                result.setMessage(ex1.getMessage());
                throw new RuntimeException(ex1);
            }finally {
                result.setDuration(System.currentTimeMillis()-startDate.getTimeInMillis());
            }


            if(procInstanceIdFromNodeInstanceLog.size()>0){
                procInstanceIdFromNodeInstanceLogString = StringUtils.join(procInstanceIdFromNodeInstanceLog, ",");
                String subQuery ="";
                if(dbManagerMysql.getDbType().equalsIgnoreCase("mysql")) {
                    subQuery = "select GROUP_CONCAT(pd2.processInstanceId), max(pc.deleteTask), max(pc.retention_day), pc.processId from ProcessConfiguration pc inner join ProcessDetails pd2 on (pc.processId = pd2.processId) where pd2.primaryProcessInstanceId in(select pd1.primaryProcessInstanceId from ProcessDetails pd1  where pd1.processInstanceId in (" + procInstanceIdFromNodeInstanceLogString + ")) group by processId";
                }else if (dbManagerMysql.getDbType().equalsIgnoreCase("microsoft sql server")){
                    subQuery = "select dbo.group_concat(pd2.processInstanceId), max(pc.deleteTask), max(pc.retention_day), pc.processId from ProcessConfiguration pc inner join ProcessDetails pd2 on (pc.processId = pd2.processId) where pd2.primaryProcessInstanceId in(select pd1.primaryProcessInstanceId from ProcessDetails pd1  where pd1.processInstanceId in (" + procInstanceIdFromNodeInstanceLogString + ")) group by processId";
                }else if (dbManagerMysql.getDbType().equalsIgnoreCase("oracle")){
                    subQuery = "select  LISTAGG(pd2.processInstanceId, ', '), max(pc.deleteTask), max(pc.retention_day), pc.processId from ProcessConfiguration pc inner join ProcessDetails pd2 on (pc.processId = pd2.processId) where pd2.primaryProcessInstanceId in(select pd1.primaryProcessInstanceId from ProcessDetails pd1  where pd1.processInstanceId in (" + procInstanceIdFromNodeInstanceLogString + ")) group by processId";
                }

                log.warn("DeepClearInstances-LOG "+identifierBackup+" subquery perProcessId: "+subQuery);

                try{
                    Map<String, Map<String, Object>> processInstanceIdMap = new HashMap<>();
                    //List<String> processInstanceIdArray = new ArrayList<String>();

                    GenericResultSet grs1 = dbManagerMysql.executeSelect(subQuery, null);

                    if(grs1 != null && grs1.getValori() != null && grs1.getValori().size()>0){
                        String colProcessId = null;
                        String colProcessInstanceId = null;
                        Integer colRetentionDay = null;
                        Boolean colDeleteTask = null;
                        for(Object[] vall : grs1.getValori()){
                            colDeleteTask = new Boolean("false");
                            colProcessInstanceId = vall[0].toString();
                            colRetentionDay = (Integer) ((Number)vall[2]).intValue();
                            colProcessId = vall[3].toString(); //
                            Map<String, Object> mappa = new HashMap<>();
                            mappa.put("processInstanceId", colProcessInstanceId);
                            Object deleteTaskObj = vall[1];
                            if(deleteTaskObj != null) {
                                if (deleteTaskObj instanceof Boolean) {
                                    colDeleteTask = (Boolean) deleteTaskObj;
                                } else if( deleteTaskObj instanceof Number){
                                    if(new Integer(vall[1].toString()).intValue() ==1){
                                        colDeleteTask = new Boolean("true");
                                    }
                                }else if(deleteTaskObj instanceof String){
                                    colDeleteTask = new Boolean(deleteTaskObj.toString());
                                }
                            }

                            mappa.put("deleteTask", colDeleteTask);
                            mappa.put("retentionDay", colRetentionDay);
                            processInstanceIdMap.put(colProcessId, mappa);

                            //log.info("BACKUPPROCESS-LOG: Recuperato id: "+colProcessInstanceId);
                        }
                    }
                    log.warn("DeepClearInstances-LOG "+identifierBackup+": start clearInstances from deepClearInstances");
                    restUtilsService.clearInstances(processInstanceIdMap, dbManagerMysql, true,identifierBackup);
                }catch (Exception ee){
                    log.error("DeepClearInstances-LOG "+identifierBackup+": errore nell'esecuzione della query: "+subQuery, ee);
                }
            }

        }
        result.setStatus(200);
        result.setMessage("DeepClearInstances-LOG "+identifierBackup+": Operazione conclusa con successo");
        result.setDuration(System.currentTimeMillis()-startDate.getTimeInMillis());

        return result;
    }

    @RequestMapping(value ="/node-instance/clearInstances", method = RequestMethod.GET, produces = "application/json")
    @ApiOperation(value = "clearInstances" , notes = "Backup massivo dei processi con relativa eliminazione delle vechie istanze")
    public @ResponseBody
    ResultCall<GenericResultSet> clearInstances(@RequestParam(value = "max_instance", defaultValue = "50", required = false) Integer maxInstance, @RequestParam(value="processId", required = false) String processId, @RequestParam(value="processInstanceId", required=false) String processInstanceId ) {
        ResultCall<GenericResultSet> result = new ResultCall<>();

        Calendar startDate = Calendar.getInstance();
        startDate.setTime(new Date());
        String identifierBackup = ""+startDate.getTime().getTime();
        log.warn("ClearInstances-LOG "+identifierBackup+": START AT "+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startDate.getTime()));

        try{
            HashMap<String, String> mysqlConnectionProperty = Tools.getApplicationDBConnection();

            String query="select pil.processInstanceId from ProcessInstanceLog pil where not exists (SELECT 1 from ProcessInstanceInfo pii where pil.processInstanceId = pii.InstanceId) and pil.parentProcessInstanceId is null "; // order by processInstanceId desc limit" +maxInstance;

            //CASO BACKUP DI UN'UNICA ISTANZA ACCEDUTA PER PROCESSINSTANCEID
            if(!Strings.isNullOrEmpty(processInstanceId)){
                query += " and pil.processInstanceId = '"+ processInstanceId+"'";
                log.warn("ClearInstances-LOG "+identifierBackup+": clear instance for processInstanceId: "+processInstanceId);
            }
            //CASO BACKUP DI ISTANZE DELLO STESSO TIPO
            else if(!Strings.isNullOrEmpty(processId)){
                query += " and pil.processId = '"+ processId+"'";
                log.warn("ClearInstances-LOG "+identifierBackup+": clear instance for processId: "+processId);
            }

            query+=" order by processInstanceId asc ";
            log.warn("ClearInstances-LOG "+identifierBackup+": clear instance max Result: "+maxInstance);
            log.info("ClearInstances-LOG "+identifierBackup+": query --> "+query);
            List<String> primaryProcessInstanceIdArray = new ArrayList<>();

            DBManager dbManagerMysql = DBManager.getInstance(mysqlConnectionProperty);

            GenericResultSet grs = dbManagerMysql.executeSelect(query, null, maxInstance);

            if(grs != null && grs.getValori() != null && grs.getValori().size()>0){
                String primaryProcessInstanceId = null;
                for(Object[] vall : grs.getValori()){
                    List<Object> values = new ArrayList<>();
                    primaryProcessInstanceId = vall[0].toString();
                    primaryProcessInstanceIdArray.add(primaryProcessInstanceId);
                    log.info("ClearInstances-LOG "+identifierBackup+": Recuperato id: "+primaryProcessInstanceId);
                }
            }

            String primaryProcessInstanceIdString =  StringUtils.join(primaryProcessInstanceIdArray, ",");
            log.warn("ClearInstances-LOG "+identifierBackup+": Recuperati primaryProcessInstanceId : "+primaryProcessInstanceIdString);
            primaryProcessInstanceIdString = "("+primaryProcessInstanceIdString+")";

            String queryProcessInstanceId = "";

            if(dbManagerMysql.getDbType().equalsIgnoreCase("mysql")) {
                queryProcessInstanceId = "SELECT GROUP_CONCAT(pd.processInstanceId), max(pc.deleteTask), max(pc.retention_day), pc.processId  from ProcessDetails pd INNER JOIN ProcessConfiguration pc on (pd.processId = pc.processId) " +
                        "where primaryProcessInstanceId in "+ primaryProcessInstanceIdString +" group by processId";
            }else if(dbManagerMysql.getDbType().equalsIgnoreCase("microsoft sql server")){
                queryProcessInstanceId = "SELECT dbo.group_concat(pd.processInstanceId), max(pc.deleteTask), max(pc.retention_day), pc.processId  from ProcessDetails pd INNER JOIN ProcessConfiguration pc on (pd.processId = pc.processId) " +
                        "where primaryProcessInstanceId in "+ primaryProcessInstanceIdString +" group by processId";
            }else if(dbManagerMysql.getDbType().equalsIgnoreCase("oracle")){
                queryProcessInstanceId = "SELECT LISTAGG(pd.processInstanceId, ', '), max(pc.deleteTask), max(pc.retention_day), pc.processId  from ProcessDetails pd INNER JOIN ProcessConfiguration pc on (pd.processId = pc.processId) " +
                        "where primaryProcessInstanceId in "+ primaryProcessInstanceIdString +" group by processId";
            }
            log.warn("ClearInstances-LOG "+identifierBackup+": query grappolo --> "+queryProcessInstanceId);
            Map<String, Map<String, Object>> processInstanceIdMap = new HashMap<>();

            GenericResultSet grs1 = dbManagerMysql.executeSelect(queryProcessInstanceId, null);

            if(grs1 != null && grs1.getValori() != null && grs1.getValori().size()>0){
                String colProcessId = null;
                String colProcessInstanceId = null;
                Integer colRetentionDay = null;
                Boolean colDeleteTask = null;
                for(Object[] vall : grs1.getValori()){
                    colProcessInstanceId = vall[0].toString();
                    colDeleteTask = (Integer) ((Number)vall[1]).intValue() ==0 ? false : true;
                    colRetentionDay = (Integer) ((Number)vall[2]).intValue();
                    colProcessId = vall[3].toString(); //
                    Map<String, Object> mappa = new HashMap<>();
                    mappa.put("processInstanceId", colProcessInstanceId);
                    mappa.put("deleteTask", colDeleteTask);
                    mappa.put("retentionDay", colRetentionDay);
                    processInstanceIdMap.put(colProcessId, mappa);

                    log.info("ClearInstances-LOG "+identifierBackup+" Recuperato id: "+colProcessInstanceId);
                }
            }
            log.warn("DeepClearInstances-LOG "+identifierBackup+" start clearInstances from ClearInstances");
            restUtilsService.clearInstances(processInstanceIdMap, dbManagerMysql, false,identifierBackup);

        }catch (Exception e){
            log.error("ClearInstances-LOG "+identifierBackup+" ERRORE: "+e.getMessage(), e);
            result.setMessage(e.getMessage());
            result.setStatus(500);
        }finally {
            result.setDuration(System.currentTimeMillis()-startDate.getTimeInMillis());
        }
        result.setMessage("ClearInstances - "+ identifierBackup + " Operazione conclusa con successo");
        result.setStatus(200);

        return result;
    }

    @RequestMapping(value ="/node-instance/backupProcess", method = RequestMethod.GET, produces = "application/json")
    @ApiOperation(value = "backupProcess" , notes = "Crea copia di backup dei processi")
    public @ResponseBody
    ResultCall<GenericResultSet> backupProcess(){

        ResultCall<GenericResultSet> result = new ResultCall<>();
        Calendar startDate = Calendar.getInstance();
        startDate.setTime(new Date());
        String identifierBackup = ""+startDate.getTime().getTime();

        log.info("BACKUPPROCESS-LOG "+identifierBackup+": START AT "+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startDate.getTime()));

        try {
            HashMap<String, String> mysqlConnectionProperty = Tools.getApplicationDBConnection();

            DBManager dbManagerMysql = DBManager.getInstance(mysqlConnectionProperty);

            String query = dbManagerMysql.getNativeQuery(dbManagerMysql.getDbType()+".getBackupNativeFromProcessInstanceLog", null);
            GenericResultSet grs = dbManagerMysql.executeSelect(query, null);

            if(grs != null && grs.getValori() != null && grs.getValori().size()>0){

                String updateNodeInstanceStates = "Insert into NodeInstanceStates (processInstanceId, stato, aggiornamento, processEndDate) values (?,?,?,?)";
                String processInstanceID = null;
                for(Object[] vall : grs.getValori()){
                    List<Object> values = new ArrayList<>();
                    processInstanceID = vall[0].toString();
                    values.add(vall[0]);
                    values.add(0);
                    values.add(new Date());
                    values.add(vall[1]);
                    log.info("BACKUPPROCESS-LOG: updateNodeInstanceStates con processInstanceId: "+processInstanceID);
                    dbManagerMysql.executeUpdate(updateNodeInstanceStates, values);
                }

            }

            query = dbManagerMysql.getNativeQuery(dbManagerMysql.getDbType()+".getBackupNativeFromNodeInstanceStates", null);
            grs = dbManagerMysql.executeSelect(query, null);

            if(grs != null && grs.getValori() != null && grs.getValori().size() == 1 ){
                //Aggiorno lo stato
                Object[] toUpdate = grs.getValori().get(0);

                if(toUpdate[2] != null && Integer.parseInt(toUpdate[2].toString()) == 0) {
                    restUtilsService.updateStateNodeInstanceStates(toUpdate[0], 1, dbManagerMysql);

                    restUtilsService.backupDb("NodeInstanceLog", toUpdate, dbManagerMysql, false);
                    restUtilsService.backupDb("Task", toUpdate, dbManagerMysql, false);

                    restUtilsService.updateStateNodeInstanceStates(toUpdate[0], 2, dbManagerMysql);

                    //TODO ELIMINA
                    restUtilsService.deleteAllTable(dbManagerMysql, toUpdate[0]);
                    restUtilsService.updateStateNodeInstanceStates(toUpdate[0], 3, dbManagerMysql);
                }else if(toUpdate[2] != null && Integer.parseInt(toUpdate[2].toString()) == 1){

                    restUtilsService.backupDb("NodeInstanceLog", toUpdate, dbManagerMysql,true);
                    restUtilsService.backupDb("Task", toUpdate, dbManagerMysql, false);

                    restUtilsService.updateStateNodeInstanceStates(toUpdate[0], 2, dbManagerMysql);

                    //TODO ELIMINA
                    restUtilsService.deleteAllTable(dbManagerMysql, toUpdate[0]);

                    restUtilsService.updateStateNodeInstanceStates(toUpdate[0], 3, dbManagerMysql);
                }else if(toUpdate[2] != null && Integer.parseInt(toUpdate[2].toString()) == 2){
                    //TODO ELIMINA

                    restUtilsService.deleteAllTable(dbManagerMysql, toUpdate[0]);
                    restUtilsService.updateStateNodeInstanceStates(toUpdate[0], 3, dbManagerMysql);
                }
            }


        }catch (Exception w){
            result.setStatus(0);
            result.setMessage(w.getMessage());
            log.error(w.getMessage(), w.getCause());
        }

        Calendar endDate = Calendar.getInstance();
        endDate.setTime(new Date());
        long duration = endDate.getTimeInMillis() - startDate.getTimeInMillis();
        result.setDuration(duration);

        log.info("BACKUPPROCESS-LOG "+identifierBackup+": END AT "+new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(endDate.getTime()));
        return result;
    }

    @RequestMapping(value ="/node-instance/backupProcessById", method = RequestMethod.GET, produces = "application/json")
    @ApiOperation(value = "backupProcessById" , notes = "Crea copia di backup del processi con  processInstanceId passato")
    public @ResponseBody
    ResultCall<GenericResultSet> backupProcessById(@RequestParam(value="processInstanceId", required=true) Long processInstanceId){

        ResultCall<GenericResultSet> result = new ResultCall<>();


        try {
            HashMap<String, String> mysqlConnectionProperty = Tools.getApplicationDBConnection();
            DBManager dbManagerMysql = DBManager.getInstance(mysqlConnectionProperty);

            String query = dbManagerMysql.getNativeQuery(dbManagerMysql.getDbType()+".getBackupNativeFromProcessInstanceLogByProcessIntanceId", null);
            List<Object> par = new ArrayList<>();
            par.add(processInstanceId);
            GenericResultSet grs = dbManagerMysql.executeSelect(query, par);


            if(grs != null && grs.getValori() != null && grs.getValori().size()>0){

                String updateNodeInstanceStates = "Insert into NodeInstanceStates (processInstanceId, stato, aggiornamento, processEndDate) values (?,?,?,?)";
                for(Object[] vall : grs.getValori()){
                    List<Object> values = new ArrayList<>();
                    values.add(vall[0]);
                    values.add(0);
                    values.add(new Date());
                    values.add(vall[1]);
                    dbManagerMysql.executeUpdate(updateNodeInstanceStates, values);
                }
            }

            query = dbManagerMysql.getNativeQuery(dbManagerMysql.getDbType()+".getBackupNativeFromNodeInstanceStates", null);
            grs = dbManagerMysql.executeSelect(query, null);

            if(grs != null && grs.getValori() != null && grs.getValori().size() == 1 ){
                //Aggiorno lo stato
                Object[] toUpdate = grs.getValori().get(0);

                if(toUpdate[2] != null && Integer.parseInt(toUpdate[2].toString()) == 0) {
                    restUtilsService.updateStateNodeInstanceStates(toUpdate[0], 1, dbManagerMysql);

                    restUtilsService.backupDb("NodeInstanceLog", toUpdate, dbManagerMysql, false);
                    restUtilsService.backupDb("Task", toUpdate, dbManagerMysql, false);

                    restUtilsService.updateStateNodeInstanceStates(toUpdate[0], 2, dbManagerMysql);

                    //TODO ELIMINA
                    restUtilsService.deleteAllTable(dbManagerMysql,toUpdate[0]);

                    restUtilsService.updateStateNodeInstanceStates(toUpdate[0], 3, dbManagerMysql);
                }else if(toUpdate[2] != null && Integer.parseInt(toUpdate[2].toString()) == 1){

                    restUtilsService.backupDb("NodeInstanceLog", toUpdate, dbManagerMysql,true);
                    restUtilsService.backupDb("Task", toUpdate, dbManagerMysql, false);

                    restUtilsService.updateStateNodeInstanceStates(toUpdate[0], 2, dbManagerMysql);

                    //TODO ELIMINA
                    restUtilsService.deleteAllTable(dbManagerMysql, toUpdate[0]);

                    restUtilsService.updateStateNodeInstanceStates(toUpdate[0], 3, dbManagerMysql);
                }else if(toUpdate[2] != null && Integer.parseInt(toUpdate[2].toString()) == 2){
                    //TODO ELIMINA
                    restUtilsService.deleteAllTable(dbManagerMysql, toUpdate[0]);

                    restUtilsService.updateStateNodeInstanceStates(toUpdate[0], 3, dbManagerMysql);
                }
            }


        }catch (Exception w){
            result.setStatus(0);
            result.setMessage(w.getMessage());
            log.error(w.getMessage(), w.getCause());
        }


        return result;
    }

    @RequestMapping(value ="/node-instance/restoreProcess", method = RequestMethod.GET, produces = "application/json")
    @ApiOperation(value = "restoreProcess" , notes = "Ripristina la copia di backup dei processi")
    public @ResponseBody
    ResultCall<GenericResultSet> restoreProcess(@RequestParam(value = "processInstanceId")String processInstanceId, @RequestParam(value="end_date") String endDate){

        ResultCall<GenericResultSet> result = new ResultCall<>();

        try{
            String dbFolder = ApplicationProperties.get("db.folder");
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


            String dbFilePath = dbFolder + "/" + year + "/" + month + "/" + day + "/" + processInstanceId + ".db3";


            restUtilsService.restoreDb(dbFilePath, "NodeInstanceLog", processInstanceId);
            restUtilsService.restoreDb(dbFilePath, "task", processInstanceId);

        }catch (Exception w){
            result.setStatus(0);
            result.setMessage(w.getMessage());
            log.error(w.getMessage(), w.getCause());
        }


        return result;
    }

    @RequestMapping(value ="/node-instance/getHistory", method = RequestMethod.GET, produces = "application/json")
    @ApiOperation(value = "getHistory" , notes = "Ripristina la copia di backup dei processi")
    public @ResponseBody
    ResultCall<List<NodeInstanceLogSummary>> getHistory(@RequestParam(value = "processInstanceId")String processInstanceId, @RequestParam(value="end_date", required = false)String endDate){

        Long lid = Long.parseLong(processInstanceId);
        if (lid<0)
        {
            lid = -lid;
            return getEventHistory(lid.toString());
        }

        ResultCall<List<NodeInstanceLogSummary>> result = new ResultCall<>();

        try {
            HashMap<String, String> mysqlConnectionProperty = Tools.getApplicationDBConnection();

            DBManager dbManagerMysql = DBManager.getInstance(mysqlConnectionProperty);



            String selectExists = "select * from NodeInstanceStates where processInstanceId = ?";

            List<Object> paramsSelectExists = new ArrayList<>();
            paramsSelectExists.add(processInstanceId);
            GenericResultSet resExist = dbManagerMysql.executeSelect(selectExists, paramsSelectExists);
            String nativeQueries = null;
            List<Object> paramsSelectNodeInstanceLog = new ArrayList<>();
            paramsSelectNodeInstanceLog.add(processInstanceId);
            paramsSelectNodeInstanceLog.add(processInstanceId);
            GenericResultSet resultQuery = null;
            if(endDate != null &&(resExist != null && resExist.getValori() != null && resExist.getValori().size()>0)){
                int positionStato = -1;
                for(int i = 0; i< resExist.getColumnNames().length; i++){
                    String col = resExist.getColumnNames()[i];
                    if(col.equalsIgnoreCase("stato")){
                        positionStato = i;
                    }
                }

                Object stato = resExist.getValori().get(0)[positionStato];


                if(stato != null && Integer.parseInt(stato.toString()) ==3 ){
                    nativeQueries = dbManagerMysql.getNativeQuery("getInstanceHistoryItemsByParamsNative", null);
                    DBManager dbManagerSqlite = restUtilsService.getSqliteDBManager(processInstanceId, endDate);
                    resultQuery =dbManagerSqlite.executeSelect(nativeQueries, paramsSelectNodeInstanceLog);
                }else{
                    nativeQueries = dbManagerMysql.getNativeQuery(dbManagerMysql.getDbType()+".getInstanceHistoryItemsByParamsNative", null);
                    resultQuery =dbManagerMysql.executeSelect(nativeQueries, paramsSelectNodeInstanceLog);
                }

            }else{
                nativeQueries = dbManagerMysql.getNativeQuery(dbManagerMysql.getDbType()+".getInstanceHistoryItemsByParamsNative", null);
                resultQuery =dbManagerMysql.executeSelect(nativeQueries, paramsSelectNodeInstanceLog);
            }

            if(resultQuery != null && resultQuery.getValori() != null && resultQuery.getValori().size()>0){

                ArrayList<NodeInstanceLogSummary> nodeInstaqnceLogsSummay = new ArrayList<NodeInstanceLogSummary>();
                for(Object[] v: resultQuery.getValori()){

                    Integer workItemId = v[10] != null ? v[10] instanceof Integer ? (int)v[10] : new Integer(v[10].toString()) : null;
                    Integer workItemId2 = null;

                    try{
                        workItemId2 = v[11] != null ? v[11] instanceof Integer ? (int)v[11] : new Integer(v[11].toString()) : null;
                    } catch (Exception e){

                    }
                    Integer nodeInstanceId = v[2] != null ? v[2] instanceof Integer ? (int)v[2] : new Integer(v[2].toString()) : null;
                    Date myEndDate = null;
                    if(v[9] != null){
                        if(v[9] instanceof Date)
                            myEndDate = (Date) v[9];
                        else{
                            try{
                                myEndDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(v[9].toString());
                            }catch (Exception e){
                                log.warn("Errore conversione data");
                            }
                        }
                    }
                    Date myStartDate = null;
                    if(v[8] != null){
                        if(v[8] instanceof Date)
                            myStartDate = (Date) v[8];
                        else{
                            try{
                                myStartDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(v[8].toString());
                            }catch (Exception e){ log.warn("Errore conversione data");}
                        }
                    }
                    String myNodeName = v[5] != null ? v[5].toString() : null;
                    String myNodeType = v[6] != null ? v[6].toString() : null;
                    String myProcessId = v[4] != null ? v[4].toString() : null;
                    String myNodeId = v[1] != null ? v[1].toString() : null;
                    String myConnection = v[3] != null ? v[3].toString() : null;
                    Integer myNodeRawId = v[0] != null ? v[0] instanceof Integer ? (int)v[0] : new Integer(v[0].toString()) : null;

                    NodeInstanceLogSummary nodeInstanceLogSummary = new NodeInstanceLogSummary();
                    nodeInstanceLogSummary.setWorkItemId(workItemId);
                    nodeInstanceLogSummary.setWorkItemId2(workItemId2);
                    nodeInstanceLogSummary.setNodeInstanceId(nodeInstanceId);
                    nodeInstanceLogSummary.setProcessInstanceId(new Integer(processInstanceId));
                    if (myEndDate != null)
                        nodeInstanceLogSummary.setEndDate(Tools.getSimpleDate(myEndDate));
                    if (myStartDate != null)
                        nodeInstanceLogSummary.setStartDate(Tools.getSimpleDate(myStartDate));

                    nodeInstanceLogSummary.setNodeName(myNodeName);
                    nodeInstanceLogSummary.setNodeType(myNodeType);
                    nodeInstanceLogSummary.setProcessId(myProcessId);
                    nodeInstanceLogSummary.setNodeId(myNodeId);
                    nodeInstanceLogSummary.setConnection(myConnection);
                    nodeInstanceLogSummary.setNodeRawId(myNodeRawId);

                    nodeInstaqnceLogsSummay.add(nodeInstanceLogSummary);

                }

                result.setStatus(1);
                result.setMessage("Record Letti con successo");
                result.setData(nodeInstaqnceLogsSummay);
                result.setListSize(nodeInstaqnceLogsSummay.size());

            }


        } catch (Exception w) {
            result.setStatus(0);
            result.setMessage(w.getMessage());
            log.error(w.getMessage(), w.getCause());
        }
        return result;
    }

    @RequestMapping(value ="/node-instance/getEventHistory", method = RequestMethod.GET, produces = "application/json")
    @ApiOperation(value = "getEventHistory" , notes = "Recupera la lista degli eventi avvenuti in una istanza")
    public @ResponseBody
    ResultCall<List<NodeInstanceLogSummary>> getEventHistory(@RequestParam(value = "processInstanceId")String processInstanceId){

        ResultCall<List<NodeInstanceLogSummary>> result = new ResultCall<>();

        try {
            HashMap<String, String> mysqlConnectionProperty = Tools.getApplicationDBConnection();

            DBManager dbManagerMysql = DBManager.getInstance(mysqlConnectionProperty);

            String nativeQueries = null;
            GenericResultSet resultQuery = null;

            List<Object> paramsSelectNodeInstanceLog = new ArrayList<>();
            paramsSelectNodeInstanceLog.add(processInstanceId);

            nativeQueries = dbManagerMysql.getNativeQuery("getInstanceHistoryEventsByParamsNative", null);
            resultQuery =dbManagerMysql.executeSelect(nativeQueries, paramsSelectNodeInstanceLog);

            if(resultQuery != null && resultQuery.getValori() != null && resultQuery.getValori().size()>0){

                ArrayList<NodeInstanceLogSummary> nodeInstaqnceLogsSummay = new ArrayList<NodeInstanceLogSummary>();
                for(Object[] v: resultQuery.getValori()){

                    Integer workItemId = v[10] != null ? v[10] instanceof Integer ? (int)v[10] : new Integer(v[10].toString()) : null;

                    Integer workItemId2 = null;
                    Integer nodeInstanceId = null;
                    Date myEndDate = null;
                    if(v[9] != null){
                        if(v[9] instanceof Date)
                            myEndDate = (Date) v[9];
                        else{
                            try{
                                myEndDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(v[9].toString());
                            }catch (Exception e){log.warn("Errore conversione data"); }
                        }
                    }
                    Date myStartDate = null;
                    if(v[8] != null){
                        if(v[8] instanceof Date)
                            myStartDate = (Date) v[8];
                        else{
                            try{
                                myStartDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(v[8].toString());
                            }catch (Exception e){ log.warn("Errore conversione data");}
                        }
                    }
                    String myNodeName = v[5] != null ? v[5].toString() : null;
                    String myNodeType = v[6] != null ? v[6].toString() : null;
                    String myProcessId = v[4] != null ? v[4].toString() : null;
                    String myNodeId = v[1] != null ? v[1].toString() : null;
                    String myConnection = v[3] != null ? v[3].toString() : null;
                    Integer myNodeRawId = v[0] != null ? v[0] instanceof Integer ? (int)v[0] : new Integer(v[0].toString()) : null;

                    NodeInstanceLogSummary nodeInstanceLogSummary = new NodeInstanceLogSummary();
                    nodeInstanceLogSummary.setWorkItemId(workItemId);
                    nodeInstanceLogSummary.setWorkItemId2(workItemId2);
                    nodeInstanceLogSummary.setNodeInstanceId(nodeInstanceId);
                    nodeInstanceLogSummary.setProcessInstanceId(new Integer(processInstanceId));
                    if (myEndDate != null)
                        nodeInstanceLogSummary.setEndDate(Tools.getSimpleDate(myEndDate));
                    if (myStartDate != null)
                        nodeInstanceLogSummary.setStartDate(Tools.getSimpleDate(myStartDate));

                    nodeInstanceLogSummary.setNodeName(myNodeName);
                    nodeInstanceLogSummary.setNodeType(myNodeType);
                    nodeInstanceLogSummary.setProcessId(myProcessId);
                    nodeInstanceLogSummary.setNodeId(myNodeId);
                    nodeInstanceLogSummary.setConnection(myConnection);
                    nodeInstanceLogSummary.setNodeRawId(myNodeRawId);

                    nodeInstaqnceLogsSummay.add(nodeInstanceLogSummary);

                }

                result.setStatus(1);
                result.setMessage("Record Letti con successo");
                result.setData(nodeInstaqnceLogsSummay);
                result.setListSize(nodeInstaqnceLogsSummay.size());

            } else {
                result.setStatus(1);
                result.setMessage("Record Letti con successo ma vuoti");
                ArrayList<NodeInstanceLogSummary> nodeInstaqnceLogsSummay = new ArrayList<NodeInstanceLogSummary>();
                result.setData(nodeInstaqnceLogsSummay);
                result.setListSize(nodeInstaqnceLogsSummay.size());
            }


        } catch (Exception w) {
            result.setStatus(0);
            result.setMessage(w.getMessage());
            log.error(w.getMessage(), w.getCause());
        }
        return result;
    }




    @RequestMapping(value ="/zip/compress", method = RequestMethod.GET, produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message="200 - Compresso con successo"),
            @ApiResponse(code= 500, message = "Errore generico")})
    @ApiOperation(value = "compress" , notes = "restituisce path del file compresso ed eventualmento cifrato")
    public @ResponseBody
    CompressResponse compress(
            @ApiParam(value="array di path o url dei file o folder da comprimere")
            @RequestParam(value = "files", required = true) URI[] files,

            @ApiParam(value="password per criptare lo zip")
            @RequestParam(value = "password", required = false) String password ,

            @ApiParam(value="commento da aggiungere allo zip")
            @RequestParam(value = "comment", required = false) String comment,
            HttpServletRequest request
    ){
        CompressResponse result = new CompressResponse();

        List<File> inputFiles = new ArrayList<>();

        try {

            for( URI uri : files ){
                inputFiles.add(Tools.toFile(uri));
            }

            File zip = restUtilsService.compress(inputFiles,password,comment);

            result.setFile(Tools.toURI(zip));
            result.setUrl(Tools.toURL(request,zip));

            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @RequestMapping(value ="/zip/uncompress", method = RequestMethod.GET, produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message="200 - Espanso con successo"),
            @ApiResponse(code= 500, message = "Errore generico")})
    @ApiOperation(value = "uncompress" , notes = "restituisce path del folder dove è stata effettuata la decompressione")
    public @ResponseBody
    UncompressResponse uncompress(
            @ApiParam(required = true)
            @RequestParam(value = "file") URI file,

            @ApiParam()
            @RequestParam(value = "password", required = false) String password,
            HttpServletRequest request
    ){
        UncompressResponse result = new UncompressResponse();

        try {

            List<File> files = restUtilsService.uncompress(Tools.toFile(file),password);

            List<URI> uris = new ArrayList<>();
            List<URI> urls = new ArrayList<>();

            for( File f : files ){
                uris.add(Tools.toURI(f));
                urls.add(Tools.toURL(request,f));
            }

            result.setFiles(uris.toArray(new URI[uris.size()]));
            result.setUrls(urls.toArray(new URI[urls.size()]));

            return result;
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
}
