package keysuite.docer.controllers;

import com.google.common.base.Strings;
import io.swagger.models.Swagger;
import it.kdm.orchestratore.appdoc.utils.HTMLWriter;
import it.kdm.orchestratore.session.Session;
import java.io.*;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import keysuite.desktop.exceptions.KSException;
import keysuite.docer.client.Documento;
import keysuite.docer.utils.ConvertTools;
import keysuite.docer.utils.DocumentiTools;
import keysuite.docer.utils.Tools;
import keysuite.docer.utils.qrCodeUtils.*;
import keysuite.swagger.client.ClientFactory;
import keysuite.swagger.client.SwaggerClient;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;
import org.jbpm.process.core.timer.DateTimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.MediaType;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
@RequestMapping("bl")
public class BusinessLogicController {
    @Autowired
    protected Environment env;

    protected final static Logger logger = LoggerFactory.getLogger(BusinessLogicController.class);
    final int memorySize = 16 * 1024 * 1024;



    @RequestMapping(value = "serviceBridge/{service}/{operation}", method = {RequestMethod.GET, RequestMethod.POST,RequestMethod.PUT,RequestMethod.PATCH,RequestMethod.DELETE})
    @ResponseBody
    public Object serviceBridge(@PathVariable(name="service") String service, @PathVariable (name="operation") String operation, HttpServletRequest request, HttpServletResponse response){

        Object result=null;
        try {
            String url = env.getProperty("serviceBridge."+service);
            if(StringUtils.isNotEmpty(url)) {
                boolean isWSDL = url.toLowerCase().endsWith("wsdl");
                if (!isWSDL && !url.endsWith("/")) {
                    url = url + "/";
                }

                Swagger swagger = null;

                if (isWSDL) {
                    swagger = ClientFactory.getSOAPService(url);
                    if(!operation.startsWith("#")){
                        operation = "#"+operation;
                    }
                } else {
                    swagger = ClientFactory.getRESTService(url + "v2/api-docs");
                }

                if (swagger != null) {
                    Map parameters = request.getParameterMap();
                    SwaggerClient client = new SwaggerClient(swagger);
                    if(!request.getMethod().equalsIgnoreCase("GET")){
                        InputStream is = request.getInputStream();
                        if(is != null){
                            String key = client.getBodyKey(operation);
                            if(key!= null){
                                parameters.put(key, is);
                            }
                        }
                    }
                    result = client.executeByMap(operation, parameters);
                }
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return new KSException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }

        return result;
    }

    @PostMapping(value="convert/file", produces = "application/json",consumes = "multipart/form-data")
    public Map<String, Object>convertFile(
            @RequestParam(required = false)Map<String, String> params,
            @RequestParam(value = "file", required = true) List<MultipartFile>files,
            @RequestParam(value = "ft", required = false , defaultValue = "pdf") String ft){

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("status", 500);
        result.put("message","Errore conversione");
        String error ="";
        long start = Calendar.getInstance().getTime().getTime();

        InputStreamResource fileStream = null;
        InputStream stream = null;



        if (files != null && files.size() > 0) {

            String fileName = "";
            MultipartFile file = null;
            String cacheFileName = null;
            String extAfterConver= null;
            String totalPages = null;
            String tempFilePath = DocumentiTools.getTempFilePath(env);

            String convertFolder = DocumentiTools.getConvertFolder(env);env.getProperty("converter.tempPath", "convert/");

            try {
                //Caso in cui viene passato un file come stream
                file = files.get(0);
                fileName = file.getOriginalFilename();
                if (StringUtils.isEmpty(fileName)) {
                    String ext = "";
                    if (StringUtils.isEmpty(file.getContentType())) {
                        ext = ".docx";//DEfault per avviare la conversione con il plugin office
                    } else {
                        MimeTypes allTypes = MimeTypes.getDefaultMimeTypes();
                        MimeType mType = allTypes.forName(file.getContentType());
                        ext = mType.getExtension();
                    }
                    fileName = "file_" + Calendar.getInstance().getTimeInMillis() + ext;
                }
                fileStream = new InputStreamResource(file.getInputStream());
                logger.info("letto documento: " + fileName + " da body");

                WebClient client = DocumentiTools.getWebClient();
                String hostConvertService = DocumentiTools.getConvertURL(env);

                if (fileStream != null) {
                    logger.info("Inizio la conversione in pdf del documento: " + fileName);
                    //avvio la conversione con le varie logiche di business
                    Map resConvert = ConvertTools.callConvertFile(client, hostConvertService, fileStream, fileName, null, params, ft, null,env);
                    if (resConvert != null) {
                        String convertError = resConvert.containsKey("error") ? (String) resConvert.get("error") : "";
                        if (StringUtils.isNotEmpty(convertError)) {
                            error += (String) resConvert.get("error");
                        } else if (resConvert.containsKey("file")) {
                            stream = ((InputStreamResource) resConvert.get("file")).getInputStream();
                            extAfterConver = (String) resConvert.get("ext");
                            totalPages = (String) resConvert.get("totalPage");
                        }
                    }
                }

                if (Strings.isNullOrEmpty(error)) {
                    if (StringUtils.isEmpty(fileName)) {
                        fileName = "file_" + Calendar.getInstance().getTimeInMillis() + ".pdf";
                    }
                    if (stream != null) {
                        HashMap<String, Object> content = new HashMap<>();

                        String downloadFileName = cacheFileName;
                        File f = new File(tempFilePath + convertFolder + downloadFileName);
                        if (Strings.isNullOrEmpty(cacheFileName)) {
                            downloadFileName = Calendar.getInstance().getTimeInMillis() + "_" + fileName+"."+extAfterConver;
                            f = new File(tempFilePath + convertFolder + downloadFileName);
                            FileUtils.copyInputStreamToFile(stream, f);
                        }
                        int pages = -1;

                        if (extAfterConver.equalsIgnoreCase("pdf")) {
                            PDDocument document = PDDocument.load(f);
                            pages = document.getNumberOfPages();
                            if (ft.equalsIgnoreCase("html")) {

                                String inFile = f.getAbsolutePath();
                                String outFile = inFile.replace(".pdf", ".html");

                                if (!new File(outFile).exists()) {
                                    HTMLWriter.writeHTML(inFile, outFile);
                                }
                                f = new File(outFile);
                                downloadFileName = f.getName();

                            }
                        }


                        tempFilePath = tempFilePath.endsWith("/") ? tempFilePath.substring(0, tempFilePath.length() - 1) : tempFilePath;
                        convertFolder = convertFolder.startsWith("/") ? convertFolder.substring(1) : convertFolder;
                        String subUri = tempFilePath.substring(tempFilePath.lastIndexOf("/")) + "/" + convertFolder;
                        String documentaleServiceHost = DocumentiTools.getDocumentaleURL(env);
                        String fileUri = documentaleServiceHost + "files" + subUri + downloadFileName;
                        long size = FileUtils.sizeOf(f);
                        fileName = fileName.substring(0, fileName.lastIndexOf(".")) + "." + ft;

                        content.put("name", fileName);
                        content.put("uri", fileUri);
                        content.put("size", Tools.readableFileSize(size));
                        if (totalPages != null) {
                            content.put("totalPages", totalPages);
                        } else if (StringUtils.isNotEmpty(cacheFileName) && cacheFileName.contains("_")) {
                            //Provo a capire il total page dal cache_file_name
                            String totalPage = cacheFileName.substring(cacheFileName.lastIndexOf("_") + 1);
                            totalPages = totalPage.substring(0, totalPage.indexOf("."));
                            content.put("totalPages", totalPages);
                        }

                        content.put("pages", pages);//TODO numero di pagina convertite -1 in caso di tutte
                        result.put("file", content);
                        result.put("status", 200);
                        result.put("message", "Conversione avvenuta con successo");
                    }
                }else{
                    result.put("message", error);
                }

            }catch (Exception e){
                result.put("error",e.getMessage());
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                e.printStackTrace(pw);
                result.put("stackTrace",sw.getBuffer().toString());
            }finally {
                long end = Calendar.getInstance().getTime().getTime();
                result.put("duration", Tools.formatDifference(start, end));
                if(stream != null){
                    IOUtils.closeQuietly(stream);
                }
                if(fileStream != null){
                    try {
                        InputStream realInputStream = null;
                        Field inputStream = fileStream.getClass().getSuperclass().getDeclaredField("inputStream");
                        inputStream.setAccessible(true);
                        realInputStream = (InputStream) inputStream.get(fileStream);
                        if(realInputStream != null){
                            IOUtils.closeQuietly(realInputStream);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }else{
            result.put("error", "Il parametro file è mandatorio");
            long end = Calendar.getInstance().getTime().getTime();
            result.put("duration", Tools.formatDifference(start, end));
        }



        return result;
    }



    /**
     *
     * @param params
     *  es.
     *     paperWidth=8.27
     *     paperHeight=11.69
     *     marginTop=0
     *     marginBottom=0
     *     marginLeft=0
     *     marginRight=0
     *     landscape=true
     *     scale=0.75
     *     pageRanges='1-3,5'
     *     ft=pdf/html (formato di output)
     * @return
     * @throws IOException
     * @throws ServletException
     * @throws MimeTypeException
     */
    @PostMapping(value="convert" , produces = "application/json",consumes = "application/json")
    public Map<String,Object> convert(
            @RequestParam(required = false)Map<String,String> params,
            @RequestParam(value = "ft",required = false, defaultValue = "pdf") String ft, @RequestBody(required = false) Map<String,Object> body) {

        Map<String, Object> result= new HashMap<>();
        result.put("status", 500);
        result.put("message","Errore conversione");
        String error ="";
        long start = Calendar.getInstance().getTime().getTime();
        InputStreamResource fileStream = null;
        InputStream stream = null;

        try {
            HttpServletRequest request = Session.getRequest();
            String requestUrl = request.getRequestURL() + "?" + request.getQueryString();
            String docnum = null;
            String url = null;
            String cacheFileName = null;

            String extAfterConver = null;
            String totalPages = null;



            String fileName = "";
            MediaType contentType = null;

            String tempFilePath = DocumentiTools.getTempFilePath(env);
            String convertFolder = DocumentiTools.getConvertFolder(env);
            //Estrapolo gli eventuali parametri docnum o url
            if (params != null) {
                docnum = params.containsKey("docnum") ? params.remove("docnum") : null;
                url = params.containsKey("url") ? params.remove("url") : null;
            }

            String hostConvertService = DocumentiTools.getConvertURL(env);

            String documentaleServiceHost =DocumentiTools.getDocumentaleURL(env);
            String getDocumentoURL = documentaleServiceHost + "documenti/";

            //Inizializzo il client per le connessioni a docer
            WebClient client = DocumentiTools.getWebClient();

                //caso in cui viene passato o il docnum o l'url
                if (StringUtils.isNotEmpty(docnum)) {
                    //Recupero il profilo del documento per poter leggere il modifiedOn e creare a partire da esso e dall'url un hash univoco per il nome del file da salvare in cache
                    ClientResponse clientResponseDocumento = client.get().uri(getDocumentoURL + docnum).exchange().block();
                    Documento doc = clientResponseDocumento.bodyToMono(Documento.class).block();
                    if (doc != null) {

                        String modifiedOn = (String) doc.getContentModifiedOn();
                        cacheFileName = requestUrl + "&modifiedOn=" + modifiedOn;
                        String firstPartFileName = DigestUtils.md5DigestAsHex(cacheFileName.toLowerCase().getBytes());
                        cacheFileName = firstPartFileName + ".pdf";

                        //Se non esiste creo la cartella con i file della cache del converter
                        if (!new File(Paths.get(tempFilePath + convertFolder).toString()).exists()) {
                            Files.createDirectory(Paths.get(tempFilePath + convertFolder));
                        }

                        //Cerco il nome del file per inizio del nome, questo è necessario per poter aggiungere dopo il pagesize nello stesso nome salvato in cache
                        List<File> cachedFiles = Tools.existDocumentsLikeName(firstPartFileName, Paths.get(tempFilePath, convertFolder).toString());
                        if (cachedFiles != null && cachedFiles.size() > 0) {
                            File cacheFile = null;
                            if(cachedFiles.size()>1){
                                for(File s: cachedFiles){
                                    if(s.getName().toLowerCase().endsWith("pdf")){
                                        cacheFile = s;
                                        break;
                                    }
                                }
                            }
                            if(cacheFile == null) {
                                cacheFile = cachedFiles.get(0);
                            }

                            cacheFileName = cacheFile.getName();
                            stream = new FileInputStream(cacheFile);
                            fileName = doc.getDocname();
                            extAfterConver = ConvertTools.getExt(cacheFileName);
                        }else {
                            //scarico il doc da docer ma non lo salvo su filesystem, rimane solo stream
                            ClientResponse clientResponse = client.get().uri(getDocumentoURL + docnum + "/file").exchange().block();
                            fileName = clientResponse.headers().asHttpHeaders().getContentDisposition().getFilename();
                            fileStream = clientResponse.bodyToMono(InputStreamResource.class).block();
                            logger.info("letto documento: " + fileName + " con docnum: " + docnum);
                        }
                    } else {
                        error = "Impossibile recuperare il profilo del documento: " + docnum;
                    }

                } else if (StringUtils.isNotEmpty(url)) {
                    //Caso url verifico che sia un url con contenuto web o un download url con un file
                    url = URLDecoder.decode(url, "UTF-8");
                    ClientResponse clientResponse = client.get().uri(url).exchange().block();
                    contentType = clientResponse.headers().asHttpHeaders().getContentType();
                    ContentDisposition contendDisposition = clientResponse.headers().asHttpHeaders().getContentDisposition();
                    if ((contentType != null && !contentType.toString().toLowerCase().contains(MediaType.TEXT_HTML.toString().toLowerCase())) || contendDisposition != null && !Strings.isNullOrEmpty(contendDisposition.getType()) && contendDisposition.getType().equalsIgnoreCase("attachment")) {
                        fileName = clientResponse.headers().asHttpHeaders().getContentDisposition().getFilename();
                        fileStream = clientResponse.bodyToMono(InputStreamResource.class).block();
                    }
                    logger.info("letto documento: " + fileName + " con docnum: " + docnum);
                }



            if (stream == null && fileStream != null || StringUtils.isNotEmpty(url) || body != null) {
                if (fileStream != null) {
                    logger.info("Inizio la conversione in pdf del documento: " + fileName);
                    url = null;
                } else if (body != null){
                    logger.info("Inizio la conversione da stringa");
                } else{
                    logger.info("Inizio la conversione dell'url: " + url);
                }
                //avvio la conversione con le varie logiche di business
                Map resConvert = ConvertTools.callConvertFile(client, hostConvertService, fileStream, fileName, url, params, ft, body,env);
                if (resConvert != null) {
                    String convertError = resConvert.containsKey("error") ? (String) resConvert.get("error") : "";
                    if (StringUtils.isNotEmpty(convertError)) {
                        error += (String) resConvert.get("error");
                    } else if (resConvert.containsKey("file")) {
                        stream = ((InputStreamResource) resConvert.get("file")).getInputStream();
                        extAfterConver = (String) resConvert.get("ext");
                        totalPages = (String) resConvert.get("totalPage");
                    }
                }
            }

                logger.info("Conversione avvenuta con successo");

                if (Strings.isNullOrEmpty(error)) {
                    try {
                        if (StringUtils.isNotEmpty(fileName)) {
                            if (StringUtils.isNotEmpty(docnum) && StringUtils.isNotEmpty(cacheFileName)) {
                                if (!cacheFileName.toLowerCase().endsWith(extAfterConver)) {
                                    cacheFileName = cacheFileName.substring(0, cacheFileName.lastIndexOf("."));
                                    cacheFileName += "." + extAfterConver;
                                }
                                String extCacheFile = ConvertTools.getExt(cacheFileName);
                                cacheFileName = cacheFileName.substring(0, cacheFileName.lastIndexOf("."));
                                if (StringUtils.isNotEmpty(totalPages)) {
                                    cacheFileName = cacheFileName + "_" + totalPages;
                                }
                                cacheFileName = cacheFileName + "." + extCacheFile;
                                Files.copy(stream, Paths.get(tempFilePath + convertFolder + cacheFileName), StandardCopyOption.REPLACE_EXISTING);
                                stream = new FileInputStream(new File(tempFilePath + convertFolder + cacheFileName));
                            }
                        } else {
                            fileName = "web_" + Calendar.getInstance().getTimeInMillis() + ".pdf";
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                if (stream != null) {


                    HashMap<String, Object> content = new HashMap<>();



                    String downloadFileName = cacheFileName;
                    File f = new File(tempFilePath + convertFolder + downloadFileName);
                    if (Strings.isNullOrEmpty(cacheFileName)) {
                        downloadFileName = Calendar.getInstance().getTimeInMillis() + "_" + fileName;
                        f = new File(tempFilePath + convertFolder + downloadFileName);
                        FileUtils.copyInputStreamToFile(stream, f);
                    }
                    int pages = -1;

                    if (extAfterConver.equalsIgnoreCase("pdf")) {
                        PDDocument document = PDDocument.load(f);
                        pages = document.getNumberOfPages();
                        if (ft.equalsIgnoreCase("html")) {

                            String inFile = f.getAbsolutePath();
                            String outFile = inFile.replace(".pdf", ".html");

                            if (!new File(outFile).exists()) {
                                HTMLWriter.writeHTML(inFile, outFile);
                            }
                            f = new File(outFile);
                            downloadFileName = f.getName();

                        }
                    }


                    tempFilePath = tempFilePath.endsWith("/") ? tempFilePath.substring(0, tempFilePath.length() - 1) : tempFilePath;
                    convertFolder = convertFolder.startsWith("/") ? convertFolder.substring(1) : convertFolder;
                    String subUri = tempFilePath.substring(tempFilePath.lastIndexOf("/")) + "/" + convertFolder;

                    String fileUri = documentaleServiceHost + "files" + subUri + downloadFileName;
                    long size = FileUtils.sizeOf(f);
                    fileName = fileName.substring(0, fileName.lastIndexOf(".")) + "." + ft;

                    content.put("name", fileName);
                    content.put("uri", fileUri);
                    content.put("size", Tools.readableFileSize(size));
                    if (totalPages != null) {
                        content.put("totalPages", totalPages);
                    } else if (org.apache.commons.lang3.StringUtils.isNotEmpty(cacheFileName) && cacheFileName.contains("_")) {
                        //Provo a capire il total page dal cache_file_name
                        String totalPage = cacheFileName.substring(cacheFileName.lastIndexOf("_") + 1);
                        totalPages = totalPage.substring(0, totalPage.indexOf("."));
                        content.put("totalPages", totalPages);
                    }

                    content.put("pages", pages);//TODO numero di pagina convertite -1 in caso di tutte
                    result.put("file", content);
                    result.put("status", 200);
                    result.put("message", "Conversione avvenuta con successo");
                }
            }else{
                result.put("message", error);
            }


        }catch (Exception e){
            result.put("message", e.getMessage());
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            result.put("stackTrace",sw.getBuffer().toString());
        }finally {
            long end = Calendar.getInstance().getTime().getTime();
            result.put("duration", Tools.formatDifference(start, end));
            if(stream != null){
                IOUtils.closeQuietly(stream);
            }
            if(fileStream != null){

                try {
                    InputStream realInputStream = null;
                    Field inputStream = fileStream.getClass().getSuperclass().getDeclaredField("inputStream");
                    inputStream.setAccessible(true);
                    realInputStream = (InputStream) inputStream.get(fileStream);
                    if(realInputStream != null){
                        IOUtils.closeQuietly(realInputStream);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return result;

    }


    @GetMapping(value = "/creaCopiaAnalogica")
    public Map creaCopiaAnalogica(HttpServletRequest request, HttpServletResponse response,
                                   @RequestParam(value = "docNum", required = true) String docNum) throws Exception {
        long start = Calendar.getInstance().getTime().getTime();
        Map result = new HashMap();
        result.put("status", 500);
        result.put("message","Errore nella creazione della copia analogica");


        final String nomeDocumento = "copia_analogica_" + docNum + ".pdf";
        //Il documento per il quale la copia va creata
        Documento currDoc = DocumentiTools.getDocumentoByDocNum(docNum);

        List<Documento> annessi = DocumentiTools.getRelatedByDocNum(docNum, "ANNESSO");

        Documento copiaAnalogica = null;
        for (Documento doc : annessi) {
            if (doc.getDocname().equalsIgnoreCase(nomeDocumento)) {
//				Se la copia esiste già fra gli annessi non dobbiamo fare altro che ottenere il nome e scaricarla
                copiaAnalogica = doc;
                break;
            }
        }

        if(copiaAnalogica == null){
            boolean isRegistrazDisabled = DocumentiTools.isRegistrazDisabled(currDoc);
            boolean isProtocolDisabled = DocumentiTools.isProtocolloDisabled(currDoc);

            if (!(isRegistrazDisabled || isProtocolDisabled)) {
                throw new Exception("Error per \"diritti mancanti\" o il documento non è registrato e/o protocollato");
            }

            String tipologia = currDoc.getTipologia();
            ResolutionUtils rs = new ResolutionUtils();
            QrCodeGenerator qr = new QrCodeGenerator();
            DataUtils dataUtils = new DataUtils(tipologia);
            ScapigliaturaUtils su = dataUtils.getScapigliaturaUtils();


            Date dataCreazione = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:sss").parse(currDoc.getCreated());
            String date_ins = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(dataCreazione);

            String date_exp = "Senza scadenza";
            String dataScadenzaDoc = dataUtils.getProperty("dataScadenzaDoc", null);
            Date dataScadenza = null;
            if (dataScadenzaDoc != null) {
                if (DateTimeUtils.isPeriod(dataScadenzaDoc)) {
//					Se data scadenza è un periodo dobbiamo esprimerla come un intervallo di tempo
//					che va da quest'ultima al giorno corrente
                    Long longDateValue = DateTimeUtils.parseDateAsDuration(dataScadenzaDoc.substring(1));
                    dataScadenza = new Date(System.currentTimeMillis() + longDateValue);
                } else {
//					Altrimenti è una data normale
                    dataScadenza = new Date(DateTimeUtils.parseDateTime(dataScadenzaDoc));
                }
            }
            date_exp = dataScadenza != null ? new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(dataScadenza) : date_exp;
            String psw = PasswordGenerator.getPWD();


            dataUtils.put("usrid", docNum);
            dataUtils.put("psw", psw);
            dataUtils.put("date_ins", date_ins);
            dataUtils.put("date_exp", date_exp);
            dataUtils.put("signed", 0);
            dataUtils.put("firme", "");

//			Parametri per la scapigliatura
            dataUtils.putAll(DocumentiTools.documenToMap(currDoc));

            String digest = currDoc.otherFields().containsKey("content_hash") ?currDoc.otherFields().get("content_hash").toString(): "";
            dataUtils.put("content_hash", digest);

            String content = dataUtils.generate("content");
            dataUtils.put("content", new String(Base64.getEncoder().encode(content.getBytes())));

            String qrCode1 = dataUtils.generate("qrcode_1");
            try {
                qrCode1 = qr.createQrCodeImageBase64(qrCode1);
            } catch (Exception e) {
                qrCode1 = qr.createQrCodeImageBase64("qrCode1");
            }
            dataUtils.put("qrCode1", qrCode1);
            dataUtils.put("qrCode1h", rs.changeResolution(50).toString());
            dataUtils.put("qrCode1w", rs.changeResolution(50).toString());

            String qrCode2 = dataUtils.generate("qrcode_2");
            qrCode2 = qr.createQrCodeImageBase64(qrCode2);
            dataUtils.put("qrCode2", qrCode2);
            dataUtils.put("qrCode2h", rs.changeResolution(40).toString());
            dataUtils.put("qrCode2w", rs.changeResolution(40).toString());

            String html = dataUtils.generate("body");

            String fileDestUri = null;

            try {
                Map<String, Object> params = new HashMap<>();
                params.put("htmlText", html);

                Map respMap= convert(null,"pdf", params);

                if(respMap != null && respMap.containsKey("file")){
                    Map mapFile = (Map)respMap.get("file");
                    if(mapFile != null && !mapFile.isEmpty() && mapFile.containsKey("uri")){
                        fileDestUri = (String)mapFile.get("uri");
                    }
                }
            } catch (Exception e) {
                fileDestUri = null;
            }

//			Carichiamo in doc il file dove è stato salvato il nostro pdf dopo aver convertito il corpo in html
            InputStream inputStreamResource = null;
            if(currDoc.getDocname().toLowerCase().endsWith("pdf")) {
                Map fileCurrDoc = DocumentiTools.downloadFileByDocNum(docNum);
                if(fileCurrDoc.containsKey("fileStream")) {
                    inputStreamResource = (InputStream) fileCurrDoc.get(("fileStream"));
                }
            }else{
                Map<String, String> params = new HashMap<>();
                params.put("docnum", docNum);
                Map resultConvertPDF = convert(params, "pdf", null);
                if(resultConvertPDF != null && resultConvertPDF.containsKey("file")){
                    Map resConvPdfFile = (Map)resultConvertPDF.get("file");
                    if(resConvPdfFile != null && !resConvPdfFile.isEmpty() && resConvPdfFile.containsKey("uri")){
                        String fileConvertUri = (String)resConvPdfFile.get("uri");
                        inputStreamResource =  new URL(fileDestUri).openStream();
                    }
                }
            }

            PDDocument doc = null;
            PDDocument newDoc = null;
            if(inputStreamResource != null) {
                doc = PDDocument.load(inputStreamResource);
            }
            if(StringUtils.isNotEmpty(fileDestUri)) {
                URL url = new URL(fileDestUri);
                newDoc = PDDocument.load(url.openStream());
            }
            if(newDoc != null){
                for (PDPage newPage : newDoc.getPages()) {
                    doc.addPage(newPage);
                }
            }

            if(doc != null) {
                su.addScapigliatura(doc);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                doc.save(baos);
                doc.close();
                ByteArrayInputStream in = new ByteArrayInputStream(baos.toByteArray());
                copiaAnalogica = DocumentiTools.inserisciCopia(in, psw, docNum, nomeDocumento);
            }
        }

//		Viene fatta una chiamata al metodo di Dario passando l'input stream del pdf
        if(copiaAnalogica != null) {
            Map<String, String> outputResult = DocumentiTools.callUploadService(docNum, copiaAnalogica);
            Integer code = Integer.parseInt(outputResult.get("code"));
            String message = outputResult.get("response");
            if (code >= 400 && code != 9006) {
                throw new Exception(message);
            }

            long end = Calendar.getInstance().getTime().getTime();
            result.put("duration",Tools.formatDifference(start,end));
            result.put("uri", copiaAnalogica.getURL().toString());
            result.put("docnum", copiaAnalogica.getDocnum());
            result.put("status","200");
            result.put("message","OK");
        }

        return result;
    }



}

