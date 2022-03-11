package keysuite.docer.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import it.kdm.doctoolkit.zookeeper.ApplicationProperties;
import it.kdm.orchestratore.session.Session;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import keysuite.desktop.exceptions.KSExceptionBadRequest;
import keysuite.desktop.exceptions.KSExceptionForbidden;
import keysuite.desktop.exceptions.KSExceptionNotFound;
import keysuite.docer.client.Documento;
import keysuite.docer.sdk.APIClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

public class DocumentiTools {

    static final int memorySize = 16 * 1024 * 1024;

    public static Documento getDocumentoByDocNum(String docNum) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {
        Documento doc = null;
        APIClient apiClient = new APIClient( Session.getUserInfo().getJwtToken());
        doc = apiClient.documenti().get(docNum);
//
//        if (StringUtils.isNotEmpty(docNum)) {
//            String documentaleServiceHost = env.getProperty("docer.url");
//            if (!documentaleServiceHost.endsWith("/")) {
//                documentaleServiceHost += "/";
//            }
//            String getDocumentoURL = documentaleServiceHost + "documenti/";
//
//
//            //recupero le properties a partire dal docnum e le aggiungo alle properties in input
//            WebClient client = WebClient
//                    .builder()
//                    .defaultHeader("Authorization", "Bearer " + Session.getUserInfo().getJwtToken())
//                    .codecs(configurer -> configurer
//                            .defaultCodecs()
//                            .maxInMemorySize(memorySize))
//                    .build();
//            ClientResponse clientResponseDocumento = client.get().uri(getDocumentoURL + docNum).exchange().block();
//            doc = clientResponseDocumento.bodyToMono(Documento.class).block();
//        }
        return doc;
    }

    public static List<Documento> getRelatedByDocNum(String docNum, String tipoComponente) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {
        APIClient apiClient = new APIClient(Session.getUserInfo().getJwtToken());
        Documento[] doc = apiClient.documenti().related(docNum, tipoComponente);
        List<Documento> docs = Arrays.asList(doc);
//        if (StringUtils.isNotEmpty(docNum)) {
//            String documentaleServiceHost = env.getProperty("docer.url");
//            if (!documentaleServiceHost.endsWith("/")) {
//                documentaleServiceHost += "/";
//            }
//            String getDocumentoURL = documentaleServiceHost + "documenti/" + docNum + "/related";
//
//
//            //recupero le properties a partire dal docnum e le aggiungo alle properties in input
//            WebClient client = WebClient
//                    .builder()
//                    .defaultHeader("Authorization", "Bearer " + Session.getUserInfo().getJwtToken())
//                    .codecs(configurer -> configurer
//                            .defaultCodecs()
//                            .maxInMemorySize(memorySize))
//                    .build();
//            ClientResponse clientResponseDocumento = client.get().uri(getDocumentoURL).exchange().block();
//
//            docs = clientResponseDocumento.bodyToMono(docs.getClass()).block();
//        }
//        if (docs.size() > 0 && StringUtils.isNotEmpty(tipoComponente)) {
//            ArrayList<Documento> filtered = new ArrayList<Documento>();
//            for (Documento doc : docs) {
//                if (doc.getTipoComponente().equalsIgnoreCase(tipoComponente)) {
//                    filtered.add(doc);
//                }
//            }
//            docs = filtered;
//        }
        return docs;
    }

    public static boolean isRegistrazDisabled(Documento currDoc) {
        boolean isRegistrazDisabled = false;
        if (currDoc.otherFields().containsKey("N_REGISTRAZ")) {
            if (!Strings.isNullOrEmpty(currDoc.otherFields().get("N_REGISTRAZ").toString()))
                if (currDoc.otherFields().containsKey("ANNULL_REGISTRAZ") && StringUtils.isNotEmpty(currDoc.otherFields().get("ANNULL_REGISTRAZ").toString()) && currDoc.otherFields().get("ANNULL_REGISTRAZ").toString().equalsIgnoreCase("SI")) {
                    isRegistrazDisabled = false;
                } else {
                    isRegistrazDisabled = true;
                }
        }
        return isRegistrazDisabled;
    }

    public static boolean isProtocolloDisabled(Documento currDoc) throws Exception {
        boolean isProtocolDisabled = false; //ANNULLATO_PG = SI
        if (currDoc.otherFields().containsKey("NUM_PG")) {
            if (!Strings.isNullOrEmpty(currDoc.otherFields().get("NUM_PG").toString())) {
                if (currDoc.otherFields().containsKey("ANNULLATO_PG") && StringUtils.isNotEmpty(currDoc.otherFields().get("ANNULLATO_PG").toString()) && currDoc.otherFields().get("ANNULLATO_PG").toString().equalsIgnoreCase("SI")) {
                    isProtocolDisabled = false;
                } else {
                    isProtocolDisabled = true;
                }
            }
        }
        return isProtocolDisabled;
    }

    public static Map documenToMap(Documento doc) throws JsonProcessingException {
        Map propertiesFtl = new HashMap();

        if (doc != null) {
            propertiesFtl = doc.otherFields();
            ObjectMapper om = new ObjectMapper();
            String jsonDoc = om.writeValueAsString(doc);
            Map mappa = om.readValue(jsonDoc, Map.class);
            if (mappa.containsKey("otherFields")) {
                mappa.remove("otherFields");
            }
            propertiesFtl.putAll(mappa);
        }

        return propertiesFtl;
    }

    public static Map downloadFileByDocNum(String docNum) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {
        Map result = new HashMap();
        APIClient apiClient = new APIClient(Session.getUserInfo().getJwtToken());
        InputStream is = apiClient.documenti().download(docNum, null,null);
        if (is != null) {
//            result.put("fileName", fileName);
            result.put("fileStream", is);
        }
//        if (StringUtils.isNotEmpty(docNum)) {
//            String documentaleServiceHost = env.getProperty("docer.url");
//            if (!documentaleServiceHost.endsWith("/")) {
//                documentaleServiceHost += "/";
//            }
//            String getDocumentoURL = documentaleServiceHost + "documenti/";
//
//
//            //recupero le properties a partire dal docnum e le aggiungo alle properties in input
//            WebClient client = WebClient
//                    .builder()
//                    .defaultHeader("Authorization", "Bearer " + Session.getUserInfo().getJwtToken())
//                    .codecs(configurer -> configurer
//                            .defaultCodecs()
//                            .maxInMemorySize(memorySize))
//                    .build();
//
//            ClientResponse clientResponse = client.get().uri(getDocumentoURL + docNum + "/file").exchange().block();
//            String fileName = clientResponse.headers().asHttpHeaders().getContentDisposition().getFilename();
//            InputStreamResource fileStream = clientResponse.bodyToMono(InputStreamResource.class).block();
//            result.put("fileName", fileName);
//            result.put("fileStream", fileStream);
//        }
        return result;
    }

    public static Documento inserisciCopia(InputStream is, String pwdQr, String docNum, String nomeDocumento) throws Exception {

        APIClient apiClient = new APIClient(Session.getUserInfo().getJwtToken());

        Documento doc = new Documento();
        doc.setDocname(nomeDocumento);
        doc.setTipoComponente("ANNESSO");
        doc.setTipologia("COPIA_ANALOGICA");
        doc.setCodEnte(Session.getUserInfo().getCodEnte());
        doc.setCodAoo(Session.getUserInfo().getCodAoo());
        doc.setStream(is);
        doc.setOtherField("PWD_QR", pwdQr);
        Documento principale = new Documento();
        principale.setDocnum(docNum);

        doc = apiClient.documenti().create(doc);
        apiClient.documenti().relate(docNum, doc.getDocnum());
        return doc;
    }

    public static Map<String, String> callUploadService(String docNum, Documento doc) throws Exception {
        String psw = doc.otherFields().get("PWD_QR").toString();
        String nomeDocumento = doc.getName();

        Map file = DocumentiTools.downloadFileByDocNum(doc.getDocnum());

        InputStream in = null;
        InputStreamResource isr = null;
        if(file != null && file.containsKey("fileStream")){
            in = (InputStream) file.get("fileStream");
            isr = new InputStreamResource(in);
        }
        String url = ApplicationProperties.get("keysuiteQr.uploadServiceUrl", "http://192.168.0.9:7778/rest/upload");
        url += "?docNum=" + docNum + "&password=" + psw;

        WebClient client = WebClient
                .builder()
                .codecs(configurer -> configurer
                        .defaultCodecs()
                        .maxInMemorySize(memorySize))
                .build();

        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("file", isr).filename(nomeDocumento);

        ClientResponse clientResponse = client.post().uri(url).contentType(MediaType.MULTIPART_FORM_DATA)
                .bodyValue(builder.build()).exchange().block();

        Map<String,String> result = new HashMap<String, String>();
        result.put("code",  ""+clientResponse.rawStatusCode());
        String res = clientResponse.bodyToMono(String.class).block();
        result.put("response", res);

        return result;
    }


    public static WebClient getWebClient(){
        return  WebClient
                .builder()
                .defaultHeader("Authorization", "Bearer " + Session.getUserInfo().getJwtToken())
                .codecs(configurer -> configurer
                        .defaultCodecs()
                        .maxInMemorySize(memorySize))
                .build();
    }

    public static String getConvertURL(Environment env){
        String hostConvertService = env.getProperty("convert.host", "http://localhost:3000/");
        if (!hostConvertService.endsWith("/")) {
            hostConvertService += "/";
        }
        hostConvertService += "convert";
        return hostConvertService;
    }
    public static String getDocumentaleURL(Environment env){
        String documentaleServiceHost = env.getProperty("docer.url");
        if (!documentaleServiceHost.endsWith("/")) {
            documentaleServiceHost += "/";
        }
        return documentaleServiceHost;
    }
    public static String getTempFilePath(Environment env){
        String tempFilePath = env.getProperty("tempfiles.upload", System.getProperty("java.io.tmpdir"));
        tempFilePath = !tempFilePath.endsWith("/") ? tempFilePath + "/" : tempFilePath;
        return tempFilePath;
    }
    public static String getConvertFolder(Environment env){
        String convertFolder = env.getProperty("converter.tempPath", "convert/");
        convertFolder = !convertFolder.endsWith("/") ? convertFolder + "/" : convertFolder;
        convertFolder = !convertFolder.startsWith("/") ? "/" + convertFolder : convertFolder;

        return convertFolder;
    }
}
