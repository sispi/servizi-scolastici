package keysuite.docer.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import it.kdm.doctoolkit.zookeeper.ApplicationProperties;
import it.kdm.orchestratore.session.Session;
import it.kdm.orchestratore.utils.BasicXSL;
import it.kdm.orchestratore.utils.ResourceUtils;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import keysuite.docer.client.Documento;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cms.*;
import org.bouncycastle.util.Store;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

public class ConvertTools {
    protected final static Logger logger = LoggerFactory.getLogger(Tools.class);

    protected final static int memorySize = 16 * 1024 * 1024;
    public static Map callConvertFile(WebClient client, String hostConvertService, InputStreamResource file, String filename, String remoteUrl, Map<String, String> params, String outputFormat, Map<String, Object> body, Environment env) throws Exception {
        params = validateParams(params);
        boolean isFile = StringUtils.isNotEmpty(filename);
        String ext= getExt(filename);
        String error="";
        Map<String, Object> resultMap = new HashMap<>();


        //SE devo convertire a partire da testo
        String htmlHeader=null;
        String htmlFooter=null;
        String htmlBody=null;
        String format = "html";
        Map propertiesFTL = null;
        String docNum = null;
        InputStreamResource headerFile = null;
        InputStreamResource footerFile = null;

        if(body != null){
            htmlBody = body.containsKey("htmlText") ? (String) body.get("htmlText") : null;
            htmlHeader = body.containsKey("headerHtmlText") ? (String) body.get("headerHtmlText") : null;
            htmlFooter = body.containsKey("footerHtmlText") ? (String) body.get("footerHtmlText") : null;
            format = body.containsKey("format") ? (String) body.get("format") : "html";
            docNum = body.containsKey("format") ? (String) body.get("DOCNUM") : null;
            propertiesFTL = body.containsKey("propertiesFTL") ? (Map)body.get("propertiesFTL") : null;


            if(StringUtils.isNotEmpty(htmlBody)){
                isFile=true;
                ext= "html";
                if(format.equalsIgnoreCase("ftl")){
                    htmlBody = renderFTL(htmlBody,propertiesFTL,docNum,env);
                }
                InputStream isBody= new ByteArrayInputStream(htmlBody.getBytes(StandardCharsets.UTF_8));


                file = new InputStreamResource(isBody);
                if(StringUtils.isNotEmpty(htmlHeader)) {
                    if(format.equalsIgnoreCase("ftl")){
                        htmlHeader = renderFTL(htmlHeader,propertiesFTL,docNum,env);
                    }
                    InputStream isHeader = new ByteArrayInputStream(htmlHeader.getBytes(StandardCharsets.UTF_8));
                    headerFile = new InputStreamResource(isHeader);
                }
                if(StringUtils.isNotEmpty(htmlFooter)) {
                    if(format.equalsIgnoreCase("ftl")){
                        htmlFooter = renderFTL(htmlFooter,propertiesFTL,docNum,env);
                    }
                    InputStream isFooter = new ByteArrayInputStream(htmlFooter.getBytes(StandardCharsets.UTF_8));
                    footerFile = new InputStreamResource(isFooter);
                }


            }else{
                throw new RuntimeException("il body non può essere vuoto");
            }
        }


        //Controllo se p7m o p7m a matrioska ed eventuale estrazione p7m o fattura
        if(!Strings.isNullOrEmpty(ext) && ext.toLowerCase().endsWith("p7m")) {
            InputStream stream =file.getInputStream();
            byte[] bytes = IOUtils.toByteArray(stream);
            IOUtils.closeQuietly(stream);
            String name = filename.substring(0,filename.lastIndexOf(".p7m"));
            HashMap<String, Object> res  = estraiFileFromP7m(bytes, name);
            if(res != null) {
                InputStream is = (InputStream) res.get("stream");
                String fName = (String) res.get("name");
                if (is != null){
                    file = new InputStreamResource(is);
                }
                if(!Strings.isNullOrEmpty(fName)){
                    filename = fName;
                    ext = getExt(filename);
                }
            }
        }
        //controllo se il file è un xml o un xml di tipo fattura
        if(!Strings.isNullOrEmpty(ext) && ext.toLowerCase().endsWith("xml")){
            InputStream stream = file.getInputStream();
            byte[] bytes = IOUtils.toByteArray(stream);
            IOUtils.closeQuietly(stream);
            Map isXml = isXML(filename, bytes);
            if(isXml!=null){
                try{
                    filename = (String)isXml.get("name");
                }catch (Exception e1){}
                try{
                    bytes = (byte[])isXml.get("outFile");
                }catch (Exception e1){}
            }
            InputStream inputStream = new ByteArrayInputStream(bytes);
            file= new InputStreamResource(inputStream);
            ext = getExt(filename);

        }

        //se si tratta di un html forzo il filename a index.html per gestire l'eccezione del converter
        if(!Strings.isNullOrEmpty(ext) && ext.equalsIgnoreCase("html")){
            filename= "index.html";
        }

        List<String> officeExt = Arrays.asList(ApplicationProperties.get("converter.extensions.office", "txt,rtf,fodt,doc,docx,odt,xls,xlsx,ods,ppt,pptx,odp").split(","));
        List<String> htmlExt = Arrays.asList(ApplicationProperties.get("converter.extensions.html", "html,ftl").split(","));


        //nel caso si tratti di PDF salto la conversione con gotenberg utilizzando pdfbox
        if(!Strings.isNullOrEmpty(ext) && ext.equalsIgnoreCase("pdf")){
            String totalPage = null;
            if( params != null){
                String pageRanges = params.get("pageRanges");
                if(!Strings.isNullOrEmpty(pageRanges)){
                    String[] splittedPageRanges = pageRanges.split(",");
                    PDDocument pdDocument = PDDocument.load(file.getInputStream());
                    totalPage= ""+pdDocument.getNumberOfPages();
                    PDDocument newDoc = new PDDocument();
                    for(String page: splittedPageRanges){
                        String[] pgInterval = page.split("-");
                        if(pgInterval.length==2){
                            Integer startPage = null;
                            Integer endPage = null;
                            try{
                                startPage = Integer.parseInt(pgInterval[0]);
                                startPage--;
                            }catch (Exception e){ }
                            try{
                                endPage = Integer.parseInt(pgInterval[1]);
                                endPage--;
                            }catch (Exception e){}
                            if(startPage!=null && endPage!=null){
                                if(startPage >=0 && endPage>=0 && endPage>=startPage) {
                                    for (int i = startPage; i <= endPage; i++) {
                                        try{
                                            newDoc.addPage(pdDocument.getPage(i));
                                        }catch (Exception e ){
                                            int pagina =i+1;
                                            error+= "Pagina "+ page+" non trovata. ";
                                        }

                                    }
                                }
                            }
                        }else{
                            Integer pg = null;
                            try{
                                pg = Integer.parseInt(page);
                                pg--;
                            }catch (Exception e1){
                                error= "Pagina "+ page + " non valida. ";
                            }
                            if(pg!=null && pg>=0) {
                                try {
                                    newDoc.addPage(pdDocument.getPage(pg));
                                }catch (Exception e){
                                    error= "Pagina "+ page+" non trovata. ";
                                }
                            }
                        }
                    }
                    if(newDoc != null && newDoc.getNumberOfPages()>0) {
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        newDoc.save(out);
                        newDoc.close();
                        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
                        file = new InputStreamResource(in);
                    }else{
                        error="Errore nella conversione del documento, "+ error;
                    }
                }
            }
            resultMap.put("file", file);
            resultMap.put("ext", ext);
            resultMap.put("totalPage", totalPage);
            resultMap.put("error", error);
//            return resultMap;
        }

        //nel ceso si tratti di richiesta di un html e di output html salto la conversione
        if(!Strings.isNullOrEmpty(ext) && ext.equalsIgnoreCase("html") && outputFormat.equalsIgnoreCase("html")){
            resultMap.put("file", file);
            resultMap.put("ext", ext);
            return resultMap;
        }

        //controlli se si tratta di conversione di url o file
        if(StringUtils.isNotEmpty(remoteUrl)){
            hostConvertService+="/url";
        }else if(htmlExt.stream().anyMatch(ext::equalsIgnoreCase)){
            hostConvertService+="/html";
            if(filename.endsWith(".ftl")){
                filename = filename.replace(".ftl",".html");
            }
        }else if(officeExt.stream().anyMatch(ext::equalsIgnoreCase) || ext.equalsIgnoreCase("pdf")){
            hostConvertService+="/office";
            if(isFile && !ext.equalsIgnoreCase("pdf")){
                //COPIO LO STREAM
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                InputStream stream = file.getInputStream();
                org.apache.commons.io.IOUtils.copy(stream, baos);
                IOUtils.closeQuietly(stream);
                byte[] bytes = baos.toByteArray();
                baos.close();
                ByteArrayInputStream baist = new ByteArrayInputStream(bytes);
                ByteArrayInputStream baist2 = new ByteArrayInputStream(bytes);
                file = new InputStreamResource(baist2);

                //Provo a calcolore il numero di pagine di doc, docx, xls, xslx in caso negativo il volere è -1
                int totalPage = getNumberOfPageOffice(baist, ext);
                if(totalPage>-1){
                    resultMap.put("totalPage", ""+totalPage);
                }
            }
        }else{
            throw new RuntimeException("file non convertibile");
        }


        //Avvio la conversione con gotenberg
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        if(isFile) {
            if(ext.equalsIgnoreCase("pdf")){
                filename = filename.replaceAll("pdf","doc");
            }
            builder.part("file", file).filename(filename);
            if(headerFile != null)
                builder.part("file", headerFile).filename("header.html");
            if(footerFile != null)
                builder.part("file", footerFile).filename("footer.html");
        }else if(StringUtils.isNotEmpty(remoteUrl)){
            builder.part("remoteURL",remoteUrl);
        }
        if(params != null && params.keySet().size()>0){
            for(String parKey: params.keySet()){
                if(!ext.equalsIgnoreCase("pdf") || (ext.equalsIgnoreCase("pdf") && !parKey.equalsIgnoreCase("pageRanges"))) {
                    builder.part(parKey, params.get(parKey));
                }
            }
        }
        InputStreamResource result = client.post()
                .uri(hostConvertService)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .bodyValue(builder.build()).retrieve()
                .bodyToMono(InputStreamResource.class)
                .block();
        resultMap.put("file", result);
        resultMap.put("ext", "pdf");
        return resultMap;
    }


    public static int getNumberOfPageOffice(InputStream stream, String ext){
        int pageSize = -1;
        try {
            if (ext.equalsIgnoreCase("xls")) {
                HSSFWorkbook workbook = new HSSFWorkbook(stream);
                Integer sheetNums = workbook.getNumberOfSheets();
                if (sheetNums > 0) {
                    pageSize = workbook.getSheetAt(0).getRowBreaks().length + 1;
                }
            } else if (ext.equalsIgnoreCase("xlsx")) {
                XSSFWorkbook xwb = new XSSFWorkbook(stream);
                Integer sheetNums = xwb.getNumberOfSheets();
                if (sheetNums > 0) {
                    pageSize = xwb.getSheetAt(0).getRowBreaks().length + 1;
                }
            } else if (ext.equalsIgnoreCase("docx")) {
                XWPFDocument docx = new XWPFDocument(stream);
                pageSize = docx.getProperties().getExtendedProperties().getUnderlyingProperties().getPages();
            } else if (ext.equalsIgnoreCase("doc")) {
                HWPFDocument wordDoc = new HWPFDocument(stream);
                return wordDoc.getSummaryInformation().getPageCount();
            }
        }catch (Exception e){
            logger.error(e.getMessage());
        }finally {
            IOUtils.closeQuietly(stream);
        }
        return pageSize;
    }

    public static Map validateParams(Map params){
        if(params != null && params.keySet().size()>0){
            if(params.containsKey("pageRanges")) {
                String pageRamgesString = (String)params.get("pageRanges");
                String[] pageRangesSplitted = pageRamgesString.split(",");
                List<String> pagesRangesArray = new ArrayList<>();
                for(String pageSplit: pageRangesSplitted){
                    String[] splitRange = pageSplit.split("-");
                    if(splitRange.length==1){
                        pageSplit = ""+splitRange[0]+"-"+splitRange[0];
                    }
                    pagesRangesArray.add(pageSplit);
                }
                pageRamgesString = StringUtils.join(pagesRangesArray,",");
                params.put("pageRanges",pageRamgesString);
            }
        }

        return params;
    }

    public static String getExt(String filename){
        boolean isFile = StringUtils.isNotEmpty(filename);

        String ext= null;
        if(isFile) {
            int posExt = filename.lastIndexOf(".") + 1;
            if (posExt > 0) {
                ext = filename.substring(posExt);
            }
        }
        return ext;
    }
    public static HashMap<String, Object> estraiFileFromP7m(byte[] file,String name) throws Exception {
        HashMap res = new HashMap<String, Object>();
        if(name.toLowerCase().endsWith("p7m")){
            byte[] outFile = extractP7M(file);
            name = name.substring(0,name.lastIndexOf(".p7m"));
            res = estraiFileFromP7m(outFile, name);
        }else {
            byte[] outFile = extractP7M(file);

            Map isXml= isXML(name, outFile);
            if(isXml!=null){
                try{
                    name = (String)isXml.get("name");
                }catch (Exception e1){}
                try{
                    outFile = (byte[])isXml.get("outFile");
                }catch (Exception e1){}
            }

            InputStream inputStream = new ByteArrayInputStream(outFile);
            res.put("stream", inputStream);
            res.put("name", name);
        }
        return res;
    }

    public static Map isXML(String name, byte[] outFile) throws Exception {
        Map<String, Object> res = new HashMap<>();
        if(name.toLowerCase().endsWith("xml")){
            String xmlString = new String(outFile);
            if (xmlString.contains("FatturaElettronica")) {
                InputStream fileInputStreamXsl = ResourceUtils.getResourceNoExc("Foglio_di_stile_fatturaPA_v1.2.1.xsl");
                if(fileInputStreamXsl != null){
                    outFile = BasicXSL.xsl(new ByteArrayInputStream(outFile), fileInputStreamXsl);
                    name = name.substring(0,name.toLowerCase().lastIndexOf(".xml"))+".html";
                    IOUtils.closeQuietly(fileInputStreamXsl);
                }
            }else{
                name = name.substring(0,name.toLowerCase().lastIndexOf(".xml"))+".txt";
            }
        }
        res.put("name",name);
        res.put("outFile", outFile);
        return res;

    }


    public static byte[] extractP7M(byte[] buffer) throws IOException, CMSException {

        if(org.apache.commons.codec.binary.Base64.isBase64(buffer)){
            buffer = Base64.decodeBase64(buffer);
        }

        //Corresponding class of signed_data is CMSSignedData
        CMSSignedData signature = new CMSSignedData(buffer);
        Store cs = signature.getCertificates();
        SignerInformationStore signers = signature.getSignerInfos();
        Collection c = signers.getSigners();
        Iterator it = c.iterator();

        //the following array will contain the content of xml document
        byte[] data = null;

        while (it.hasNext()) {
            SignerInformation signer = (SignerInformation) it.next();
            Collection certCollection = cs.getMatches(signer.getSID());
            Iterator certIt = certCollection.iterator();
            X509CertificateHolder cert = (X509CertificateHolder) certIt.next();

            CMSProcessable sc = signature.getSignedContent();
            data = (byte[]) sc.getContent();
        }
        return data;
    }
    public static String renderFTL(String template, Map properties, String docNum, Environment env) throws IOException, TemplateException {
        String documentaleServiceHost = env.getProperty("docer.url");
        if (!documentaleServiceHost.endsWith("/")) {
            documentaleServiceHost += "/";
        }
        String getDocumentoURL = documentaleServiceHost + "documenti/";

        Map propertiesFtl=new HashMap();
        String parsedFTL = null;
        Writer out = new StringWriter();
        Configuration cfg = new freemarker.template.Configuration(Configuration.VERSION_2_3_28);
        //cfg.setClassForTemplateLoading(BLApplication.class,"/static/bpmn/templates");
        cfg.setObjectWrapper(new DefaultObjectWrapper(Configuration.VERSION_2_3_28));
        cfg.setNumberFormat("0.######");

        if(org.apache.commons.lang.StringUtils.isNotEmpty(docNum)){
            //recupero le properties a partire dal docnum e le aggiungo alle properties in input
            WebClient client = WebClient
                    .builder()
                    .defaultHeader("Authorization", "Bearer " + Session.getUserInfo().getJwtToken())
                    .codecs(configurer -> configurer
                            .defaultCodecs()
                            .maxInMemorySize(memorySize))
                    .build();
            ClientResponse clientResponseDocumento = client.get().uri(getDocumentoURL + docNum).exchange().block();
            Documento doc = clientResponseDocumento.bodyToMono(Documento.class).block();


            if (doc != null) {
                propertiesFtl = doc.otherFields();
                ObjectMapper om = new ObjectMapper();
                String jsonDoc  = om.writeValueAsString(doc);
                Map mappa = om.readValue(jsonDoc,Map.class);
                if(mappa.containsKey("otherFields")){
                    mappa.remove("otherFields");
                }
                propertiesFtl.putAll(mappa);
            }
        }
        propertiesFtl.putAll(properties);

        Template t = new Template("template",new StringReader(template), cfg);
        t.process(propertiesFtl,out);
        parsedFTL = out.toString();

        return parsedFTL;
    }
}
