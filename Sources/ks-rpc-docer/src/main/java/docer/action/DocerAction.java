package docer.action;

import com.google.common.base.Strings;
import docer.configuration.DocerConfiguration;
import docer.exception.ActionRuntimeException;
import docer.utils.DocerPropertyResolver;
import it.kdm.doctoolkit.model.*;
import it.kdm.doctoolkit.zookeeper.ApplicationProperties;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import keysuite.cache.ClientCacheAuthUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author marco.mazzocchetti
 */
public abstract class DocerAction {

    public static final String TOKEN = "userToken";
    //public static final String AUTHORIZATION = "Authorization";

    private static final Logger log = LoggerFactory.getLogger(DocerAction.class);
    public static ThreadLocal<String> bearer = new ThreadLocal<>();

    private String tenant;
    private String organization;
    private String userId;
    //private WorkItem workItem;
    private Map<String, Object> settings;
    protected DocerConfiguration ServerProperties;
    private Map<String, Object> systemProperties;
    protected Map<String, Object> parameters;
    Long processInstanceId;
    Long workItemId;
    String deploymentId;
    Boolean isRetry = false;

    static {
        ApplicationProperties.setEnv(new DocerPropertyResolver());
    }

    public DocerAction() {
        ServerProperties = new DocerConfiguration();
        systemProperties = new HashMap<>();
    }

    public DocerAction isRetry(Boolean isRetry){
        this.isRetry = isRetry;
        return this;
    }

    public DocerAction withWorkItemId(Long workItemId) {
        this.workItemId = workItemId;
        return this;
    }

    public Long getWorkItemId() {
        return workItemId;
    }

    public DocerAction withProcessInstanceId(Long processInstanceId) {
        this.processInstanceId = processInstanceId;
        return this;
    }

    public Long getProcessInstanceId() {
        return processInstanceId;
    }

    public DocerAction withDeploymentId(String deploymentId) {
        this.deploymentId = deploymentId;
        this.getSystemProperties().put("ente",getEnte());
        this.getSystemProperties().put("aoo",getAoo());
        return this;
    }

    public String getDeplomentId() {
        return deploymentId;
    }
    
    public abstract Map<String, Object> execute(Map<String, Object> inputs) throws ActionRuntimeException;

    protected String createWorkitemKey(long processinstanceId, long workitemId){
        return String.valueOf(processinstanceId)+":"+ String.valueOf(workitemId);
    }

    protected String createWorkitemKey(){
        return String.valueOf(getProcessInstanceId())+":"+ String.valueOf(getWorkItemId());
    }

    public String getToken(Map<String,Object> inputs) {
        String token = (String) inputs.get("userToken");

        if (!token.contains("uid:")){
            token = ClientCacheAuthUtils.getInstance().convertToDocerToken(token);
        }

        return token;
    }

    public String getJWTToken(Map<String,Object> inputs){
        String token = (String) inputs.get("userToken");

        if (token.contains("uid:")){
            ClientCacheAuthUtils.getInstance().convertToJWTToken(token);
        }

        return token;
    }

    public Map<String, Object> getSystemProperties() {
        return systemProperties;
    }

    public String getEnte() {
        return deploymentId.split(":")[0].split("\\.")[0];
    }

    public String getAoo() {
        return deploymentId.split(":")[0].split("\\.")[1];
    }

    //Trasforma gli Oggetti innestati in strutture HashMap per il motore
    public HashMap<String ,Object> documentToHashmap(Documento documento)throws DocumentException {
        HashMap<String ,Object> hashdoc =  new HashMap< String , Object>(documento.properties);
        hashdoc.put("MITTENTI",documentToHashmap(documento,"MITTENTI"));
        hashdoc.put("DESTINATARI",documentToHashmap(documento,"DESTINATARI"));
        hashdoc.put("FIRMATARIO",documentToHashmap(documento,"FIRMATARIO"));
        hashdoc.put("VISTO",documentToHashmapVistatore(documento,"VISTO"));
        return hashdoc;

    }

    private List<HashMap<String, Object>> documentToHashmap(Documento documento , String keyOutHash )throws DocumentException{

        List<HashMap<String, Object>> docMittenti = new ArrayList<HashMap<String, Object>>();
        List<Corrispondente> listCorrispondenti = null;
        if("MITTENTI".equals(keyOutHash))
            listCorrispondenti = documento.getMittenti();
        else if("DESTINATARI".equals(keyOutHash))
            listCorrispondenti = documento.getDestinatari();
        else if("FIRMATARIO".equals(keyOutHash))
            listCorrispondenti = documento.getFirmatari();


        for (Corrispondente c : listCorrispondenti) {
            if(c instanceof Amministrazione){
                HashMap<String, Object> hashAmm = new HashMap< String , Object>(c.properties);
                HashMap<String, Object> aoo = new HashMap<String,Object>(((Amministrazione) c).getAOO().properties);
                hashAmm.put("AOO", aoo);
                hashAmm.put("UO", ((Amministrazione) c).getUnitaOrganizzativa().properties);
                hashAmm.put("@type" , "Amministrazione");
                docMittenti.add(new HashMap< String , Object>(hashAmm));
            }else if(c instanceof PersonaFisica){
                c.properties.put("@type" , "PersonaFisica");
                docMittenti.add(new HashMap< String , Object>(c.properties));

            }else if(c instanceof PersonaGiuridica){
                c.properties.put("@type" , "PersonaGiuridica");
                docMittenti.add(new HashMap< String , Object>(c.properties));
            }

        }
        return docMittenti;
    }

    private List<HashMap<String, Object>> documentToHashmapVistatore(Documento documento , String keyOutHash )throws DocumentException{
        List<HashMap<String, Object>> docVistatori = new ArrayList<HashMap<String, Object>>();
        List<Vistatore> listVistatore = null;
        if("VISTO".equals(keyOutHash))
            listVistatore = documento.getVistatore();
        for (Vistatore c : listVistatore) {
            docVistatori.add(new HashMap< String , Object>(c.properties));
        }
        return docVistatori;
    }

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch(NumberFormatException e) {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }

    public static InputStream getStreamFromUrl(String url) throws IOException {

        String baseUrl = ApplicationProperties.getEnv().getProperty("url.download.file");

        int idx = url.indexOf("/files/upload/");

        if (idx>0){
            String basePath = ApplicationProperties.getEnv().getProperty("mail.attachDirEmailPec");
            File f = new File(basePath,url.substring(idx+"/files/upload/".length()));
            if (f.exists())
                return new FileInputStream(f);
        }

        HttpURLConnection urlConn = (HttpURLConnection) new URL(url).openConnection();

        if (bearer.get()!=null)
            urlConn.setRequestProperty("Authorization", bearer.get() );

        return urlConn.getInputStream();
    }

    public static String getUrlFromStream(InputStream stream) throws IOException {
        String basePath = ApplicationProperties.getEnv().getProperty("mail.attachDirEmailPec");
        String baseUrl = ApplicationProperties.getEnv().getProperty("url.download.file");

        String name = UUID.randomUUID().toString()+".tmp";

        File dest = new File(basePath,name);

        FileUtils.copyInputStreamToFile(stream,dest);

        return baseUrl+"/"+name;
    }

    public static String getUrlFromFile(File file) throws IOException {
        String basePath = ApplicationProperties.getEnv().getProperty("mail.attachDirEmailPec");
        String baseUrl = ApplicationProperties.getEnv().getProperty("url.download.file");

        if (file.exists()){
            if (file.getAbsolutePath().startsWith(basePath)){
                return baseUrl+"/"+file.getAbsolutePath().substring(basePath.length());
            } else {
                String ext = FilenameUtils.getExtension(file.getName());
                if (Strings.isNullOrEmpty(ext))
                    ext = "tmp";
                String name = UUID.randomUUID().toString()+"."+ext;

                FileUtils.copyFile(file,new File(basePath,name));

                return baseUrl+"/"+name;
            }
        } else {
            throw new FileNotFoundException(file.getAbsolutePath());
        }
    }

    public static File getFileFromUrl(String url) throws IOException{

        String basePath = ApplicationProperties.getEnv().getProperty("mail.attachDirEmailPec");
        //String baseUrl = ApplicationProperties.getEnv().getProperty("url.download.file");

        int idx = url.indexOf("/files/upload/");

        if (idx>0){
            File f = new File(basePath,url.substring(idx+"/files/upload/".length()));
            if (f.exists())
                return f;
        }

        //if (url.toLowerCase().startsWith(baseUrl.toLowerCase())){
        //    return new File(basePath,url.substring(baseUrl.length()));
        //} else {
        String name = UUID.randomUUID().toString()+".tmp";

        InputStream is = getStreamFromUrl(url);

        File dest = new File(basePath,name);

        FileUtils.copyInputStreamToFile(is,dest);

        return dest;
        //}
    }

}
