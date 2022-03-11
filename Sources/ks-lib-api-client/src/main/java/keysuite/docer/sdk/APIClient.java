package keysuite.docer.sdk;

import bpmn2.ServiceException;
import com.google.common.base.Strings;
import io.jsonwebtoken.Claims;
import io.swagger.models.Operation;
import io.swagger.models.Swagger;
import keysuite.desktop.exceptions.*;
import keysuite.docer.client.ClientUtils;
import keysuite.docer.client.DocerBean;
import keysuite.docer.client.FileServiceCommon;
import keysuite.swagger.client.ClientFactory;
import keysuite.swagger.client.SwaggerClient;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import static keysuite.docer.client.ClientUtils.throwKSException;

public class APIClient implements IDocerAPI {

    protected final static Logger logger = LoggerFactory.getLogger(APIClient.class);

    //static String CREATE = "#PUT";
    static String PUT = "#PUT";
    static String POST = "#POST";
    static String GET = "#GET";
    static String PATCH = "#PATCH";
    static String DELETE = "#DELETE";

    private final WSDocumentiClient documenti;
    private final WSTitolariClient titolari;
    private final WSFascicoliClient fascicoli;
    private final FileServiceClient files;
    private final WSAnagraficheClient anagrafiche;
    private final WSCartelleClient cartelle;
    private final WSFirmaClient firma;
    private final WSGruppiClient gruppi;
    private final WSUtentiClient utenti;

    static {
        SwaggerClient.READ_TIMEOUT = Integer.parseInt(System.getProperty("client.timeout", "10000"));
    }

    //private Authenticator authenticator;

    SwaggerClient client;
    Swagger swagger;
    String apiUri;
    SwaggerClient.addBeaererHeader bearer;
    Map<String,String> headers;
    Long[] delays = new Long[0];
    Long[] serverDelays = new Long[0];

    public APIClient(){
        this(ClientUtils.simpleJWTToken("system","guest","none"));
    }

    public APIClient(SwaggerClient.addBeaererHeader bearer){
        this(System.getProperty("docer.url"),bearer);
    }

    public APIClient(String Authorization){
        this(System.getProperty("docer.url"),Authorization);
    }

    public APIClient(String apiUri,String Authorization){
        this(apiUri,new SwaggerClient.addBeaererHeader() {
            @Override
            public String bearer() {
                return Authorization;
            }
        });
    }

    public APIClient(String apiUri,SwaggerClient.addBeaererHeader bearer){
        this.bearer = bearer;

        if (Strings.isNullOrEmpty(apiUri)){
            throw new IllegalArgumentException("apiUri null");
        }

        if (bearer==null){
            throw new IllegalArgumentException("bearer null");
        }

        try {
            this.apiUri = apiUri;
            if (!this.apiUri.endsWith("/"))
                this.apiUri+="/";
            swagger = ClientFactory.getRESTService(this.apiUri+"v2/api-docs");
            client = new SwaggerClient(swagger);
            client.setBinaryOutput(true);
            //client.setReadTimeout(READ_TIMEOUT);

            headers = new LinkedHashMap<>();
            headers.put("retryPolicy","no-retry");

            client.setConfigureHeaders(bearer,headers);

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        documenti = new WSDocumentiClient(this);
        titolari = new WSTitolariClient(this);
        fascicoli = new WSFascicoliClient(this);
        files = new FileServiceClient(this);
        anagrafiche = new WSAnagraficheClient(this);
        cartelle = new WSCartelleClient(this);
        firma = new WSFirmaClient(this);
        gruppi = new WSGruppiClient(this);
        utenti = new WSUtentiClient(this);
    }

    public String loginJWT(String codAoo, String username, String password) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden{
        try{
            String res = client.execute( "/system/login#GET", codAoo, password, username );
            return res;
        } catch (Exception e){
            throw throwKSException(e);
        }
    }

    public APIClient setTimeout(int timeout){
        client.setReadTimeout(timeout);
        return this;
    }

    public APIClient setClientRetries(Long... delays){
        this.delays = delays;
        return this;
    }

    public APIClient setServerRetries(Long... delays){
        this.serverDelays = delays;
        if (delays!=null && delays.length>0)
            headers.put("retryPolicy", StringUtils.join(delays,","));
        else
            headers.put("retryPolicy","no-retry");
        client.setConfigureHeaders(bearer,headers);
        return this;
    }

    public String getCurrentUser(){
         String token = this.bearer.bearer();
         Claims claims = ClientUtils.parseJWTTokenWithoutSecret(token);
         return claims.getSubject();
    }

    public String getCurrentAOO(){
        String token = this.bearer.bearer();
        Claims claims = ClientUtils.parseJWTTokenWithoutSecret(token);
        return claims.getAudience();
    }

    public FileServiceCommon getFileServiceCommon(){
        String token = this.bearer.bearer();
        Claims claims = ClientUtils.parseJWTTokenWithoutSecret(token);
        return new FileServiceCommon(claims.getAudience(),claims.getSubject());
    }

    public <T> T execute(String url, Object... params) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {

        int executions = -1;

        String operationId = UUID.randomUUID().toString();

        while(true) {
            executions++;

            try {
                DocerBean.baseUrl.set(apiUri);
                headers.put("operationId",operationId);
                T res = client.execute( url, params);

                return res;
            } catch (Exception e) {

                boolean canRetry = e instanceof CodeException && ((CodeException) e).getCode().isRetryable();

                if (e instanceof RuntimeException && e.getCause()!=null)
                    e = (Exception) e.getCause();

                canRetry = canRetry || (e instanceof IOException);

                if (!canRetry && e instanceof ServiceException){
                    String code = ((ServiceException)e).getCode();
                    try {
                        canRetry = Code.get(code).isRetryable();
                    } catch (Exception e2) {}
                }

                if (canRetry && executions<delays.length){

                    Operation operation = client.findOperation(url);

                    long delay = delays[executions];
                    try {
                        Thread.sleep(delay);
                    } catch (InterruptedException interruptedException) {
                        throw new RuntimeException(interruptedException);
                    }
                    logger.warn("retry #"+(executions+1)+" after "+delay+"ms");
                    continue;
                }

                throw throwKSException(e);
            }
        }
    }

    @Override
    public APIClient ping() {
        client.execute("/system/ping");
        return this;
    }

    public void deleteByQuery(String query) throws KSExceptionForbidden, KSExceptionBadRequest, KSExceptionNotFound {
        client.execute( "/system/deleteByQuery" , query );
    }

    public void commit() throws KSExceptionForbidden, KSExceptionBadRequest, KSExceptionNotFound {
        client.execute( "/system/commit" );
    }

    @Override
    public WSDocumentiClient documenti() {
        return documenti;
    }

    @Override
    public WSFascicoliClient fascicoli() {
        return fascicoli;
    }

    @Override
    public WSTitolariClient titolari() {
        return titolari;
    }

    @Override
    public FileServiceClient files() {
        return files;
    }

    @Override
    public WSAnagraficheClient anagrafiche() {
        return anagrafiche;
    }

    @Override
    public WSCartelleClient cartelle() {
        return cartelle;
    }

    @Override
    public WSFirmaClient firma() {
        return firma;
    }

    @Override
    public WSGruppiClient gruppi() {
        return gruppi;
    }

    @Override
    public WSUtentiClient utenti() {
        return utenti;
    }
}
