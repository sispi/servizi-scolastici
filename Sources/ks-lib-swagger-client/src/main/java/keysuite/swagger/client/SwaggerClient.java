package keysuite.swagger.client;

import bpmn2.ServiceException;
import com.google.common.base.Strings;
import io.swagger.models.*;
import io.swagger.models.parameters.*;
import io.swagger.models.properties.ArrayProperty;
import io.swagger.models.properties.ObjectProperty;
import io.swagger.models.properties.Property;
import io.swagger.parser.Swagger20Parser;
import keysuite.docer.client.FileServiceCommon;
import keysuite.docer.query.ISearchParams;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.*;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.BodyPart;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.*;


public class SwaggerClient {

    public int getReadTimeout() {
        return readTimeout;
    }

    public String getHost() {
        if (this.swaggerApiDoc!=null)
            return this.swaggerApiDoc.getHost();
        else
            return null;
    }

    public void setHost(String host) {
        if (this.swaggerApiDoc!=null)
            this.swaggerApiDoc.setHost(host);
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    int readTimeout = READ_TIMEOUT;

    public boolean isBinaryOutput() {
        return binaryOutput;
    }

    public void setBinaryOutput(boolean binaryOutput) {
        this.binaryOutput = binaryOutput;
    }

    @FunctionalInterface
    public interface addBeaererHeader {
        String bearer();
    }

    private static final Logger logger = LoggerFactory.getLogger(SwaggerClient.class);

    public static final String ACCEPTED_STATUS = "acceptedStatus";

    public static final String API_DOC = "apiDoc";
    public static final String REST_URL = "restUrl";
    public static final String HTTP_METHOD = "httpMethod";
    public static final String HOST = "host";
    public static final String HTTP_STATUS_PIN = "httpStatus";
    public static final String CONTENT_TYPE = "contentType";

    public static final String RESPONSE_200 = "200";
    public static final String SWAGGER_BODY = "body";
    //public static final String PIN_BODY = "BODY";
    public static final String PIN_RESULT = "Result";
    //    public static final String GET = "GET";
//    public static final String POST = "POST";
//    public static final String PUT = "PUT";
//    public static final String DELETE = "DELETE";
    public static final String CONTENT_DISPOSITION = "Content-Disposition";
    //public static final String TEXT = "text/";

    public static final String APPLICATION_OCTET_STREAM = "application/octet-stream";
    public static final String MULTIPART_FORMDATA = "multipart/form-data";
    public static final String APPLICATION_URLENCODED = "application/x-www-form-urlencoded";
    public static final String APPLICATION_JSON = "application/json";
    public static final String APPLICATION_XML = "application/xml";

    public static final String WORK_ITEM = "workItem";
    public static final String PIN_FAULT = "Fault";

    public static Integer READ_TIMEOUT = Integer.parseInt(System.getProperty("dynSync.SRV_TIMEOUT", "6000"));
    public static Integer CONNECT_TIMEOUT_RATIO = Integer.parseInt(System.getProperty("dynSync.CONNECT_TIMEOUT_RATIO", "10"));
    public static Integer CONNECT_TIMEOUT_MIN = Integer.parseInt(System.getProperty("dynSync.CONNECT_TIMEOUT_MIN", "6000"));

    //public static final String OUTPUT_TYPE = "outputType";
    //public static final String MULTIPART_MIXED = "multipart/mixed";

    //private static final String isoDateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    //private static final SimpleDateFormat ddf = new SimpleDateFormat(isoDateFormat);

    /*static{
        ddf.setTimeZone(TimeZone.getTimeZone("UTC"));
    }*/

    /*public WorkItemNodeInstance getNodeInstance(long workItemId, WorkflowProcessInstance processInstance) {
        return findWorkItemNodeInstance(workItemId, processInstance.getNodeInstances());
    }

    private static WorkItemNodeInstance findWorkItemNodeInstance(long workItemId, Collection<org.kie.api.runtime.process.NodeInstance> nodeInstances) {
        for (org.kie.api.runtime.process.NodeInstance nodeInstance: nodeInstances) {
            if (nodeInstance instanceof WorkItemNodeInstance) {
                WorkItemNodeInstance workItemNodeInstance = (WorkItemNodeInstance) nodeInstance;
                org.kie.api.runtime.process.WorkItem workItem = workItemNodeInstance.getWorkItem();
                if (workItem != null && workItemId == workItem.getId()) {
                    return workItemNodeInstance;
                }
            }
            if (nodeInstance instanceof NodeInstanceContainer) {
                WorkItemNodeInstance result = findWorkItemNodeInstance(workItemId, ((NodeInstanceContainer) nodeInstance).getNodeInstances());
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }*/

    public void setConfigureHeaders(SwaggerClient.addBeaererHeader addBeaererHeader, Map<String,String> headers) {
        this.addBeaererHeader = addBeaererHeader;
        this.headers = headers;
    }

    private addBeaererHeader addBeaererHeader;
    private Map<String,String> headers;

    public SwaggerClient(){

    }

    Swagger swaggerApiDoc = null;
    private boolean binaryOutput = false;

    public SwaggerClient(Swagger swagger){
        swaggerApiDoc = swagger;
    }

    public static class SwaggerResult<T> {
        Integer httpCode;
        T result;

        public Integer getHttpCode() {
            return httpCode;
        }

        public T getResult() {
            return result;
        }

        public Map<String, String> getHeaders() {
            return headers;
        }

        Map<String,String> headers = new LinkedHashMap<>();
    }

    public <T> T execute(String url, Object... params) throws ServiceException {
        SwaggerResult<T> sr = executeWithHeaders(url,params);
        return sr.getResult();
    }

    public SwaggerResult executeWithHeaders(String url, Object... params) throws ServiceException {
        if (swaggerApiDoc==null)
            throw new RuntimeException("api not provided");

        Operation operation = findOperation(url);

        if (operation.getParameters().size()>0){
            Parameter first = operation.getParameters().get(0);
            if (first instanceof HeaderParameter && first.getName().equals("Authorization")){
                List pl = new ArrayList(Arrays.asList(params));
                pl.add(0,this.addBeaererHeader.bearer());
                params = pl.toArray();
            }
        }

        if (operation==null)
            throw new RuntimeException("operation not found:"+url);

        Map<String,Object> map = new LinkedHashMap<>();

        int idx = 0;
        for( Parameter p : operation.getParameters() )
        {
            if (p instanceof BodyParameter && operation.getParameters().size() <= params.length && ((BodyParameter) p).getSchema().getProperties()!=null )
            {
                for ( String key :  ((BodyParameter) p).getSchema().getProperties().keySet() )
                {
                    if (idx>=params.length)
                        break;

                    map.put( key, params[idx++] );
                }
            } else {
                if (idx>=params.length)
                    break;

                map.put( p.getName(), params[idx++] );
            }
        }

        Map<String,Object> results = executeByMap(url,map);

        SwaggerResult sr = new SwaggerResult();

        sr.httpCode = (Integer) results.remove(HTTP_STATUS_PIN);
        sr.result = results.remove(PIN_RESULT);

        Response resp = operation.getResponses().get(""+sr.httpCode);

        if (resp != null && resp.getHeaders()!=null){
            for( String key : resp.getHeaders().keySet())
                sr.headers.put(key,results.get(key));
        }

        /*for( String key : results.keySet() ){
            Object v = results.get(key);
            if (v instanceof String){
                sr.headers.put(key,(String)v);
            }
        }*/

        return sr;
    }

    public Map<String, Object> executeByMap(String url, Map<String, Object> params) throws ServiceException {

        logger.info("REST execute with data {}", params);

        params = new HashMap<String,Object>(params);

        HttpClient httpClient=null;
        try {

            String apiDocJson = (String) params.remove(API_DOC);
            String host = (String) params.remove(HOST);
            String acceptedStatus = (String) params.remove(ACCEPTED_STATUS);
            String contentType = (String) params.remove(CONTENT_TYPE);
            Object srvTimeout = params.remove("srvTimeout");

            //String outputType = (String) params.remove(OUTPUT_TYPE);

            params.remove("CommandClass");
            params.remove("callType");
            params.remove("TaskName");
            params.remove("userToken");
            params.remove(HTTP_METHOD);
            String restUrl = (String) params.remove(REST_URL);

            Swagger swagger;

            if (!Strings.isNullOrEmpty(apiDocJson))
                swagger = new Swagger20Parser().parse(apiDocJson);
            else
                swagger = swaggerApiDoc;

            if (swagger == null)
                throw new RuntimeException("json api Docs not provied");

            if (Strings.isNullOrEmpty(acceptedStatus))
                acceptedStatus = "200-299";

            Operation operation = null;
            HttpMethod method = null;

            if (url==null && !Strings.isNullOrEmpty(restUrl)){
                int l = swagger.getBasePath().length();
                if (restUrl.startsWith(swagger.getBasePath()) && l>1)
                    url = restUrl.substring(l);
                else
                    url = restUrl;
            }

            if (url==null){
                url = swagger.getPaths().keySet().iterator().next();
                Path rest = swagger.getPath(url);
                Map<HttpMethod,Operation> oMap = rest.getOperationMap();
                method = oMap.keySet().iterator().next();
                operation = oMap.get(method);
            } else {
                method = parseMethod(url);
                if (method==null){
                    Path rest = swagger.getPath(url);
                    if (rest==null && url.indexOf("#")>0)
                        rest = swagger.getPath(url.substring(url.indexOf("#")));
                    if (rest!=null) {
                        Map<HttpMethod, Operation> oMap = rest.getOperationMap();
                        method = oMap.keySet().iterator().next();
                        operation = oMap.values().iterator().next();
                    }
                }else{
                    operation = findOperation(url);
                }
            }

            if (operation == null)
                throw new RuntimeException("operation with url "+url+" not found");

            Integer _readTimeout = srvTimeout!=null ? Integer.parseInt(srvTimeout.toString()) : readTimeout;
            Integer _connectTimeout = Math.max(new Double(readTimeout/CONNECT_TIMEOUT_RATIO).intValue(), CONNECT_TIMEOUT_MIN);

            logger.info("INPUT TIMEOUT:{} CONNECT:{} READ:{}",srvTimeout,_connectTimeout,_readTimeout);

            //apiDocJson = String.format(
            //        "{ \"swagger\" : \"2.0\", \"paths\": { \"%s\": { \"%s\": %s }}}" , restUrl, method.toLowerCase(), apiDocJson );





            //Map<String,Model> definitions = swagger.getDefinitions();

            if (Strings.isNullOrEmpty(contentType))
            {
                List<String> consumes = operation.getConsumes();
                if (consumes==null)
                    swagger.getConsumes();

                if (consumes!=null && consumes.size()>0)
                    contentType = operation.getConsumes().get(0);
            }

            if (Strings.isNullOrEmpty(host)) //System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
                host = swagger.getHost();

            if (Strings.isNullOrEmpty(host))
                throw new RuntimeException("host not provided");

            if (!host.toLowerCase().startsWith("http"))
            {
                List<Scheme> schemes = swagger.getSchemes();

                if (schemes!=null && schemes.size()>0 && !schemes.contains(Scheme.HTTP))
                    host = schemes.get(0).toValue() + "://" + host;
                else
                    host = Scheme.HTTP.toValue() + "://" + host;
            }

            if (!Strings.isNullOrEmpty(swagger.getBasePath()) && swagger.getBasePath().length()>1)
                host = host + swagger.getBasePath();

            //if (operation.getTags()!=null && operation.getTags().contains("SOAP"))
            //    restUrl = "";
            /*if (restUrl.contains("#"))
            {
                //operation.setOperationId(restUrl.split("#")[1]);
                operation.addTag("SOAP");
            }*/

            /*if (operation.getVendorExtensions()==null ||
                    operation.getVendorExtensions().get("x-namespaces")==null)
                if (swagger.getVendorExtensions()!=null)
                    operation.setVendorExtension("x-namespaces",swagger.getVendorExtensions().get("x-namespaces"));
            */

            RequestBuilder methodObject = configureRequest(contentType, operation, method, host,url, params);

            if (addBeaererHeader!=null){
                String bearer = addBeaererHeader.bearer();
                if (bearer!=null) {
                    if (!bearer.startsWith("Bearer "))
                        bearer = "Bearer " + bearer;
                    methodObject.addHeader("Authorization", bearer);
                }
            }

            if (headers!=null && headers.size()>0){
                for ( String key : headers.keySet()){
                    methodObject.addHeader(key, headers.get(key));
                }
            }

            if (methodObject.getFirstHeader("Authorization")==null && 
                operation.getSecurity()!=null && 
                operation.getSecurity().size()>0 && params.get("Authorization") instanceof String) {
                methodObject.addHeader("Authorization", (String) params.get("Authorization"));
            }

            String foldeRef = FileServiceCommon.getThreadFolderRef();

            if (foldeRef!=null)
                methodObject.addHeader("thread-folder-ref", foldeRef);

            HttpUriRequest request = methodObject.build();

            if (logger.isDebugEnabled())
                logger.debug("request line:{} headers:{}", request.getRequestLine(), request.getAllHeaders());

            httpClient = RestUtils.getHttpClient(_readTimeout, _connectTimeout);

            HttpResponse response = httpClient.execute(request);

            if (logger.isDebugEnabled())
                logger.debug("request status:{} headers:{} entity:{}", response.getStatusLine(), response.getAllHeaders(), response.getEntity());

            Map<String, Object> results = postProcessResult(binaryOutput, acceptedStatus, operation, response);

            logger.info("execution results:{}",results);

            return results;

        } catch (ServiceException e) {

            //logger.error("bpmn2.ServiceException", e);
            /*WorkItemHandlerRuntimeException wihRe = new WorkItemHandlerRuntimeException(e);
            for( String key : e.getFault().keySet() ) {
                wihRe.setInformation(key, e.getFault().get(key) );
            }
            wihRe.setInformation(WorkItemHandlerRuntimeException.WORKITEMHANDLERTYPE, this.getClass().getSimpleName());*/
            //wihRe.setInformation("nodeId",nodeId);
            throw e;
        } /*catch (RESTServiceException e) {
            logger.error("RESTServiceException",e);
            throw e;
        } */catch (Exception e) {
            //logger.error("error executing REST",e);
            throw new RuntimeException(e);
        } finally {
            try {
                if (httpClient!=null)
                    ((CloseableHttpClient) httpClient).close();
            } catch( Exception e ) {
                // no idea if this throws something, but we still don't care!
            }
        }
    }


    private HttpMethod parseMethod(String url){
        HttpMethod httpMethod = null;
        int idx = url.lastIndexOf("#");
        if (idx>0){
            try{
                httpMethod = HttpMethod.valueOf(url.substring(idx+1).toUpperCase());
            } catch (Exception e){}
        }
        return httpMethod;
    }

    public Operation findOperation(String url){

        HttpMethod httpMethod = parseMethod(url);
        if (httpMethod!=null)
            url = url.substring(0,url.lastIndexOf("#"));

        Path path = swaggerApiDoc.getPath(url);
        //if (path==null)
        //    path = swaggerApiDoc.getPath( swaggerApiDoc.getBasePath() + url);

        if (path==null)
            throw new RuntimeException("url not found:"+url);

        Map<HttpMethod,Operation> oMap = path.getOperationMap();

        if (httpMethod!=null)
            return oMap.get(httpMethod);

        if (oMap.size()==1)
            return oMap.values().iterator().next();

        return null;
    }

    public String getBodyKey(String url){
        Operation operation = findOperation(url);
        int idx = 0;
        for( Parameter p : operation.getParameters() )
        {
            if (p instanceof BodyParameter)
            {
                return p.getName();

            }
        }
        return null;
    }



    /*public static String toBase64(byte[] bytes)
    {
        return Base64.encodeBase64String(bytes);
    }

    public static String formatDate(Date date)
    {
        return ddf.format(date);
    }*/

    /*public static String join(List<String> strings,String sep)
    {
        return org.apache.commons.lang3.StringUtils.join(strings,sep);
    }*/

    /*protected static String getParamAsString(Object value,String collectionFormat) {
        List<String> strings = getParamAsStrings(value,collectionFormat);
        if (strings.size()>0)
            return strings.get(0);
        else
            return "";
    }*/


    @SuppressWarnings("unchecked")
    protected static void setBody(String contentType, Operation operation, RequestBuilder builder, final Map<String, Object> params) {

        //body and form parameters cannot exist together

        // If type is "file", the consumes MUST be either "multipart/form-data",
        // " application/x-www-form-urlencoded" or both and the parameter MUST be in "formData"


        logger.info("setBody params:{}",params);

        BodyParameter bodyParam = new BodyParameter();
        bodyParam.setName(SWAGGER_BODY);

        for ( Parameter p : operation.getParameters() ) {
            if (p.getIn().equals(SWAGGER_BODY)) {
                bodyParam = (BodyParameter) p;
                break;
            }
        }

        String bodyPIN;

        if (bodyParam.getName()!=null)
            bodyPIN = bodyParam.getName();
        else
            bodyPIN = SWAGGER_BODY;

        Object bodyValue = params.remove(bodyPIN);

        //retrocompatibilità;
        if (bodyValue==null && params.containsKey("Body"))
            bodyValue = params.remove("Body");

        ModelImpl model = bodyParam.getSchema() instanceof ModelImpl ? (ModelImpl) bodyParam.getSchema() : null;

        /*if (bodyParam.getSchema() instanceof RefModel)
            model = (ModelImpl) definitions.get(((RefModel)bodyParam.getSchema()).getSimpleRef()) ;
        else
            model = (ModelImpl) bodyParam.getSchema();*/

        if (model!=null && model.getProperties()!=null && model.getProperties().size()>0)
        {
            if (logger.isDebugEnabled())
                logger.debug("body model contains keys {}",model.getProperties());

            if (bodyValue instanceof Map)
            {
                for (String key : model.getProperties().keySet()) {

                    if (params.containsKey(key))
                        ((Map)bodyValue).put(key, params.remove(key));
                }
            }
            else
            {
                logger.warn("Properties not consumed by body because not Map");
            }
        }

        if (operation.getTags()!=null && operation.getTags().contains("SOAP")) {

            Map<String,Object> soapParams = new LinkedHashMap<>();

            if (bodyValue instanceof Map){
                soapParams.putAll((Map)bodyValue);
            }
            soapParams.putAll(params);

            SOAPEnvelopeBuilder soapBuilder = new SOAPEnvelopeBuilder(null,soapParams,model);

            String charset = "utf-8";
            if (!Strings.isNullOrEmpty(contentType)){
                ContentType ct = ContentType.parse(contentType);
                if (ct.getCharset()!=null)
                    charset = ct.getCharset().toString().toLowerCase();
            }

            HttpEntity entity = soapBuilder.build(charset);
            builder.setEntity(entity);

            String soapAction =  operation.getOperationId();

            if (Strings.isNullOrEmpty(soapAction)){
                if (operation.getVendorExtensions().containsKey("x-SOAPAction")){
                    soapAction = (String) model.getVendorExtensions().get("x-SOAPAction");
            }
            else if (model.getXml()!=null)
            {
                String tns = model.getXml().getNamespace();
                String xmlPath = model.getXml().getName();
                int idx = xmlPath.indexOf("/");

                if (idx>0)
                {
                    soapAction = xmlPath.substring(0,idx);
                } else {
                    soapAction = xmlPath;
                }
                String tnsPrefix = model.getXml().getPrefix();

                if (!Strings.isNullOrEmpty(tnsPrefix))
                        soapAction = tnsPrefix+":"+soapAction;
                else if (!Strings.isNullOrEmpty(tns))
                        soapAction = tns+soapAction;
                }
            }

            if (!Strings.isNullOrEmpty(soapAction))
                    builder.addHeader("SOAPAction",soapAction);

            return;
        }

        List<Map.Entry<String,Object>> nvPairs = new ArrayList<Map.Entry<String,Object>>();

        boolean isForm = false;

        for ( Parameter param : operation.getParameters() )
        {
            if (param instanceof FormParameter) {

                String key = param.getName();

                isForm = isForm || params.containsKey(key);

                if (params.containsKey(key)) {

                    FormParameter fp = (FormParameter) param;

                    List values = RestUtils.getParamAsList(params.remove(key), fp.getCollectionFormat());

                    //List values = RestUtils.getParamAsStrings(inputValue, fp.getCollectionFormat());

                    for (Object val : values) {
                        nvPairs.add(new AbstractMap.SimpleEntry<>(key, val));
                    }
                }
            }
        }

        String bodyString = null;

        boolean bodyIsJson = false;

        if (bodyValue!=null){
            if ( (bodyValue instanceof Object && RestUtils.checkContentType(contentType, APPLICATION_JSON)) || bodyValue instanceof Map || bodyValue instanceof List || bodyValue.getClass().isArray() )
            {
                bodyString = RestUtils.toJSON(bodyValue);
                bodyIsJson = true;
            }
            else
            {
                bodyString = bodyValue.toString();
                bodyIsJson = RestUtils.isJSON(bodyString);
            }
        }

        // ci sono form values oppure c'è più di un file

        //if (nvPairs.size()>0 || files.size()>1 || checkContentType( contentType, MULTIPART_FORMDATA,APPLICATION_URLENCODED) )
        if (isForm)
        {
            if (bodyString !=null)
            {
                if (!bodyIsJson && RestUtils.isFile(bodyString))
                {
                    try {
                        String json = FileUtils.readFileToString(RestUtils.getFile(bodyValue.toString()), RestUtils.DEFAULT_CHARSET);
                        bodyValue = RestUtils.parseJSON(json);
                        bodyIsJson = true;
                    } catch (IOException e) {
                        logger.info("not possible to read file as string");
                    } catch (Exception e) {
                        logger.info("form body file is not json");
                    }
                }

                if (bodyIsJson)
                {
                    logger.info("form body file is json");

                    if ( !(bodyValue instanceof Map) )
                        bodyValue = RestUtils.parseJSON(bodyString);

                    for( Object key : ((Map)bodyValue).keySet() )
                    {
                        List<String> strings = RestUtils.getParamAsStrings( ((Map)bodyValue).get(key),null);

                        for( String s : strings)
                            nvPairs.add(new AbstractMap.SimpleEntry<>(key.toString(), s ));
                    }
                }
                else
                {
                    logger.info("added body to form. key:{} value:{}",bodyPIN,bodyString);
                    nvPairs.add(new AbstractMap.SimpleEntry<>(bodyPIN, bodyString ));

                }
            }

            logger.info("REST will receive a form:{}",nvPairs);

            HttpEntity entity;

            if (!RestUtils.checkContentType(contentType, APPLICATION_URLENCODED) )
            {
                logger.info("body contentType is:{}", MULTIPART_FORMDATA);

                if (contentType!=null && !contentType.equals(MULTIPART_FORMDATA))
                    logger.warn("body differs in content type :{}",contentType);

                MultipartEntityBuilder mpBuilder = MultipartEntityBuilder.create();

                for ( Map.Entry<String,Object> nv : nvPairs) {
                    if (RestUtils.isStreamSource(nv.getValue())) {
                        InputStreamBody isb = RestUtils.getStreamSource(nv.getValue());
                        mpBuilder.addBinaryBody(nv.getKey(), isb.getInputStream(), isb.getContentType(), isb.getFilename());
                    } else {

                        String str = null;
                        if (!(nv.getValue() instanceof String)){
                            try{
                                str = RestUtils.toJSON(nv.getValue());
                            } catch (Exception e){
                                str = String.valueOf(nv.getValue());
                            }
                        } else {
                            str = String.valueOf(nv.getValue());
                        }

                        if (RestUtils.isJSON(str)){
                            mpBuilder.addBinaryBody(nv.getKey(), IOUtils.toInputStream(str), ContentType.APPLICATION_JSON, "file.json");
                            //mpBuilder.addTextBody(nv.getKey(), str, ContentType.APPLICATION_JSON);
                        } else if (RestUtils.isXML(str)){
                            mpBuilder.addBinaryBody(nv.getKey(), IOUtils.toInputStream(str), ContentType.APPLICATION_XML, "file.xml");
                            //mpBuilder.addTextBody(nv.getKey(), str, ContentType.APPLICATION_XML);
                        } else {
                            mpBuilder.addTextBody(nv.getKey(), str);
                        }
                    }
                }

                entity = mpBuilder.build();
            } else {
                logger.info("body contentType is:{}",APPLICATION_URLENCODED);

                if (contentType!=null && !contentType.equals(APPLICATION_URLENCODED))
                    logger.warn("body differs in content type :{}",contentType);

                try {

                    List<BasicNameValuePair> formPairs = new ArrayList<>();

                    for ( Map.Entry<String,Object> nv : nvPairs) {
                        Object value = nv.getValue();

                        if (RestUtils.isStreamSource(value)) {
                            InputStreamBody isb = RestUtils.getStreamSource(value);
                            value = RestUtils.percentEncode(isb.getInputStream());
                        }

                        formPairs.add(new BasicNameValuePair(nv.getKey(), String.valueOf(value)));
                    }

                    entity = new UrlEncodedFormEntity(formPairs);

                    if (logger.isDebugEnabled())
                        logger.debug("body www urlencoded:{}",formPairs);

                } catch (UnsupportedEncodingException e) {
                    logger.error("problem with body",e);
                    throw new RuntimeException("Cannot set body for REST request [" + builder.getMethod() + "] " + builder.getUri(), e);
                }
            }

            builder.setEntity(entity);
        }
        else if (bodyValue!=null)
        {
            if (RestUtils.isFile(bodyString))
            {
                FileEntity entity = new FileEntity(RestUtils.getFile(bodyString));

                builder.setEntity(entity);

                if (contentType!=null)
                {
                    entity.setContentType(contentType);
                }
                else
                {
                    entity.setContentType(APPLICATION_OCTET_STREAM);
                }

                logger.info("body is file:{} ",bodyString);
            } else {

                AbstractHttpEntity entity;
                try {
                    if (  bodyValue instanceof InputStream && RestUtils.checkContentType(contentType, APPLICATION_OCTET_STREAM)) {
                        entity = new InputStreamEntity((InputStream) bodyValue);
                    } else if (  bodyValue instanceof byte[] && RestUtils.checkContentType(contentType, APPLICATION_OCTET_STREAM)){
                        entity = new ByteArrayEntity( (byte[]) bodyValue );
                    } else {

                        if (RestUtils.checkContentType(contentType, "text/plain")){
                            if ("base64".equals(model.getFormat()) || "byte".equals(model.getType())){
                                byte[] bytes = Base64.decodeBase64(bodyString);
                                entity = new ByteArrayEntity(bytes);
                            }else{
                                entity = new StringEntity(bodyString, Charset.forName("utf-8"));
                            }
                        } else if (RestUtils.checkContentType(contentType, APPLICATION_OCTET_STREAM)){
                            byte[] bytes = Base64.decodeBase64(bodyString);
                            entity = new ByteArrayEntity(bytes);
                        } else {
                            entity = new StringEntity(bodyString, Charset.forName("utf-8"));
                        }
                    }
                    builder.setEntity(entity);

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                if (contentType!=null)
                {
                    entity.setContentType(contentType);
                }
                else if (bodyIsJson || RestUtils.isJSON(bodyString))
                {
                    entity.setContentType(APPLICATION_JSON);
                }
                else if (RestUtils.isXML(bodyString))
                {
                    entity.setContentType(APPLICATION_XML);
                }
                else
                {
                    logger.info("no specific body string format");
                }

                logger.info("body contentType is:{}",entity.getContentType());
            }
        }
    }


    protected static Map<String,Object> postProcessResult(boolean binaryOutput, String acceptedStatus, Operation operation, HttpResponse response) throws ServiceException {

        final HttpEntity entity = response.getEntity(); new String(new byte[]{0,1,2,3});

        Map<String,Object> results = new HashMap<String,Object>();

        if (operation.getTags()!=null && operation.getTags().contains("SOAP"))
        {
            SOAPEnvelopeParser parser = new SOAPEnvelopeParser(entity,operation.getResponses());
            Object result = parser.parse();
            Map<String,Object> SOAPResult = null;
            if (result instanceof Map)
                SOAPResult = (Map) result;

            if (parser.getResponseCode()==null || !RestUtils.checkStatus(parser.getResponseCode(),acceptedStatus)){
                Map<String,Object> details = SOAPResult==null ? new HashMap<String,Object>() : SOAPResult;
                if (parser.getOutputHeaders()!=null){
                    details.putAll(parser.getOutputHeaders());
                }
                throw SOAPFaultException.create( details, parser.getResponseCode());
            }

            if (SOAPResult!=null){
                if (parser.getResponseCode()==200)
                    results.putAll(SOAPResult);
                else if (SOAPResult.containsKey("Detail"))
                    SOAPResult.putAll( (Map) SOAPResult.get("Detail") );
                else if (SOAPResult.containsKey("detail"))
                    SOAPResult.putAll( (Map) SOAPResult.get("detail") );
            }

            results.put(HTTP_STATUS_PIN, parser.getResponseCode());
            results.put(PIN_RESULT, result);

            return results;
        }

        StatusLine statusLine = response.getStatusLine();
        Integer responseCode = statusLine.getStatusCode();

        if (!RestUtils.checkStatus(responseCode,acceptedStatus))
        {
            logger.error("rest error code:{}",responseCode);

            String code = responseCode.toString();
            String reason = statusLine.getReasonPhrase();

            String entResponse = null;
            Map<String,Object> fault = new HashMap<String,Object>();
            if (entity!=null)
            {
                try {
                    entResponse = EntityUtils.toString(entity);
                } catch (IOException e) {
                    //e.printStackTrace();
                }

                if (entResponse!=null){
                    logger.error("REST error respone:\n{}",entResponse);
                    Exception typedExc = null;
                    try {
                        fault = RestUtils.parseJSON(entResponse);

                        String name = (String) fault.get("@class");

                        if (name!=null){

                            try{
                                Class cls = Class.forName(name);
                                typedExc = (Exception) RestUtils.mapper.readValue(entResponse,cls);

                            } catch (Exception e2){

                            }
                        }


                        if (fault.containsKey("reason"))
                            reason = fault.get("reason").toString();
                        else if (fault.containsKey("message"))
                            reason = fault.get("message").toString();

                        if (fault.containsKey("code"))
                            code = fault.get("code").toString();



                    } catch (Exception e) {
                        fault.put("error", RestUtils.parseScalar(entResponse));
                        //e.printStackTrace();
                    }

                    if (typedExc!=null)
                        throw new ServiceException(code,reason,typedExc);
                }
            }

            throw new ServiceException(code,reason,fault);

            //throw new RESTServiceException(responseCode,entResponse,operation.getDescription());
        }

        logger.debug("rest responseCode {}",responseCode);
        results.put(HTTP_STATUS_PIN, responseCode);

        Property oBody = null;
        Map<String,Response> responses = operation.getResponses();
        Response resp = null;

        if (responses.containsKey(responseCode.toString()))
            resp = responses.get(responseCode.toString());
        else if (responses.containsKey("default"))
            resp = responses.get("default");
        else
            resp = responses.get(RESPONSE_200);

        if (resp!=null)
        {
            oBody = resp.getSchema();

            if (resp.getHeaders()!=null)
            {
                logger.info("setting response headers:{}",resp.getHeaders().keySet());
                for( String name : resp.getHeaders().keySet() )
                {
                    if (response.containsHeader(name))
                        results.put(name,response.getFirstHeader(name).getValue());
                }
            }
        }

        if (entity==null)
        {
            return results;
        }

        String filename = null;

        if (response.containsHeader(CONTENT_DISPOSITION))
        {
            String disposition = response.getFirstHeader(CONTENT_DISPOSITION).getValue();
            filename = disposition.replaceFirst("(?i)^.*filename=\"?([^\"]+)\"?.*$", "$1");
        }
        else if (response.containsHeader("filename"))
        {
            filename = response.getFirstHeader("filename").getValue();
        }

        if (!Strings.isNullOrEmpty(filename))
            try {
                filename = URLDecoder.decode(filename, "ISO-8859-1" );
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }

        Object result = null;



        /*
        object      object
        file        file                (only formData)
        boolean	    boolean
        integer	    integer	int32	    signed 32 bits
        long	    integer	int64	    signed 64 bits
        float	    number	float
        double	    number	double
        string	    string
        byte	    string	byte	    base64 encoded characters
        binary	    string	binary	    any sequence of octets
        date	    string	date	    As defined by full-date - RFC3339
        dateTime	string	date-time	As defined by date-time - RFC3339
        password	string	password	Used to hint UIs the input needs to be obscured.
         */

        String outputFormat = null;
        String outputType = "object";
        String collectionFormat = null;

        if (oBody!=null)
        {
            outputFormat = oBody.getFormat();
            outputType = oBody.getType();
        }

        boolean outputIsMap = "object".equals(outputType) || "ref".equals(outputType) ;

        boolean outputIsArray = (oBody instanceof ArrayProperty);

        if (outputIsArray)
        {
            ArrayProperty ap = (ArrayProperty) oBody;

            collectionFormat = RestUtils.getCollectionFormat(ap.getFormat());

            if (ap.getItems()!=null)
            {
                outputFormat = ap.getItems().getFormat();
                outputType = ap.getItems().getType();
            }
        }

        boolean outputIsFile = ("file".equals(outputType) || "byte".equals(outputFormat) || "base64".equals(outputFormat) || "binary".equals(outputFormat));

        if (filename!=null || entity.getContentType()!=null &&  RestUtils.checkContentType(entity.getContentType().getValue(), APPLICATION_OCTET_STREAM))
            outputIsFile = true;

        /*if (entity.getContentType()!=null &&  RestUtils.checkContentType(entity.getContentType().getValue(), "application/*" , "audio/*" , "video/*" , "image/*") )
        {
            try {
                result = entity.getContent();
                //long length = entity.getContentLength();

                if (result instanceof EofSensorInputStream){
                //if (length==-1) {
                    byte[] bytes = IOUtils.toByteArray((InputStream)result);
                    result = new ByteArrayInputStream(bytes);
                }

                //InputStream stream = RestUtils.toStream(entity, outputFormat);

                //result = RestUtils.to File(stream,filename);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        } else */
        if (outputIsMap)
        {
            try
            {
                result = EntityUtils.toString(entity, RestUtils.DEFAULT_CHARSET);

                if (result!=null)
                {
                    Map<String,Object> resultMap;

                    try{
                        resultMap = RestUtils.parseJSON( (String) result );

                        if ("object".equals(outputType) && outputFormat!=null)
                            resultMap.put("@type",outputFormat);

                        if (oBody instanceof ObjectProperty && ((ObjectProperty) oBody).getProperties()!=null)
                        {
                            for( String key : ((ObjectProperty) oBody).getProperties().keySet() )
                            {
                                Property prop = ((ObjectProperty) oBody).getProperties().get(key);

                                String mkey = key;

                                Object value = resultMap.get(key);

                                if (value instanceof Map && prop.getFormat()!=null && "object".equals(prop.getType()))
                                    resultMap.put("@type",prop.getFormat());

                                results.put(mkey, value);
                            }
                        }
                        else
                        {
                            results.putAll( resultMap );
                        }

                    } catch(Exception e){
                        //logger.warn("Risultato REST JSON non valido",e);

                        resultMap = new HashMap<>();
                        resultMap.put("response", RestUtils.parseScalar( (String) result));
                    }
                    //DEVE essere una mappa
                    result = resultMap;
                }
            }
            catch(Exception e){

                throw new RuntimeException(e);
            }
        }
        else if (outputIsFile)
        {
            if (entity.getContentType()!=null && RestUtils.checkContentType(entity.getContentType().getValue(), RestUtils.MULTIPART))
            {
                result = new ArrayList<String>();

                try {
                    MimeMultipart list = RestUtils.getMultipartContent(entity.getContent(),entity.getContentType().getValue(),null);

                    for( int i=0; i<list.getCount(); i++)
                    {
                        BodyPart item = list.getBodyPart(i);

                        InputStream stream = RestUtils.toStream(item.getInputStream(),outputFormat);
                        
                        Object f;
                        if (binaryOutput)
                            f = stream;
                        else
                            f = RestUtils.toFile(stream,item.getFileName());

                        if (!outputIsArray)
                        {
                            //se il risultato atteso non è una lista prendo il primo
                            result = f;
                            break;
                        }

                        //DEVE essere una lista di path
                        ((List)result).add(f);
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            else
            {
                try {
                    /*InputStream stream = entity.getContent();
                    if ("byte".equals(outputFormat) || "base64".equals(outputFormat) )
                    {
                        String base64 = EntityUtils.toString(entity, RestUtils.DEFAULT_CHARSET);
                        byte[] bytes = Base64.decodeBase64(base64);
                        stream = new ByteArrayInputStream(bytes);
                        //result = RestUtils.to File(new ByteArrayInputStream(bytes),filename);
                    }*/
                    InputStream stream = RestUtils.toStream(entity,outputFormat);

                    if (binaryOutput)
                        result = stream;
                    else
                        result = RestUtils.toFile( stream ,filename);

                    //logger.debug("REST produced file :{}",result);

                } catch (IOException e1) {
                    throw new RuntimeException(e1);
                }

                if (outputIsArray)
                {
                    //DEVE essere una lista
                    result = new ArrayList(Collections.singletonList(result));
                }
            }
        }
        else // non è un file e non è una mappa
        {
            try {
                result = EntityUtils.toString(entity, RestUtils.DEFAULT_CHARSET);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            if (result!=null) {

                //è un array di object ??
                if (outputIsArray && ("object".equals(outputType) || "ref".equals(outputType)) )
                {
                    List<Object> list;

                    try{
                        char first = result.toString().trim().charAt(0);

                        if (first=='[')
                            list = RestUtils.parseJSONList( (String) result);
                        else if (first=='{'){
                            Map<String,Object> jsonMap = RestUtils.parseJSON( (String) result);
                            list = new ArrayList();
                            list.add(jsonMap);
                        } else {
                            list = new ArrayList();
                            list.add(RestUtils.parseScalar( (String) result));
                        }
                    } catch(Exception e) {
                        logger.warn("Risultato atteso con JSON ma invalido");

                        list = new ArrayList();
                        list.add(RestUtils.parseScalar( (String) result));
                    }

                    //DEVE essere una lista di mappe
                    for (int i=0; i<list.size(); i++) {
                        Map res;
                        if (list.get(i) instanceof Map)
                            res = (Map) list.get(i);
                        else {
                            res = new HashMap<>();
                            res.put("result",list.get(i));
                            list.set(i,res);
                        }

                        if (outputFormat!=null) {
                            res.put("@type",outputFormat);
                        }
                    }

                    result = list;
                }
                else
                {
                    List<Object> list = new ArrayList<Object>();
                    if (outputIsArray)
                    {
                        list = Arrays.asList( (Object[]) result.toString().split(collectionFormat) );
                    }
                    else
                    {
                        list.add(result);
                    }

                    if (!"string".equals(outputType) && outputType!=null)
                    {
                        for( int i=0; i< list.size(); i++)
                        {
                            list.set(i, RestUtils.cast(outputType,list.get(i) ));
                        }
                    }

                    if (outputIsArray)
                        result = list;
                    else if (list.size()>0)
                        result = list.get(0);
                }
            }
        }

        logger.info("set Result PIN with {}", result);
        results.put(PIN_RESULT, result);

        return results;
    }

    /*public static Map<String,Object> tryJSON(String json)
    {
        try
        {
            return parseJSON(json);
        }
        catch(Exception e)
        {
            return null;
        }
    }*/

    protected static RequestBuilder configureRequest(String contentType, Operation operation, HttpMethod method, String host, String restUrl, Map<String, Object> params) {

        logger.info("configureRequest method:{},host:{},rest:{},params:{}",method,host,restUrl,params);

        String urlStr = host + restUrl;

        Map<String,Object> cParams = new HashMap<String,Object>(params);

        for ( Parameter param : operation.getParameters() ) {
            String key = param.getName();

            if (params.containsKey(key)) {

                if (param instanceof PathParameter) {

                    String cf = ((PathParameter) param).getCollectionFormat();
                    List<String> strings = RestUtils.getParamAsStrings(cParams.remove(key), cf);

                    String pathParam;
                    if (strings.size()==0)
                        pathParam = "";
                    else
                        pathParam = strings.get(0);

                    urlStr = urlStr.replace("{" + key + "}", pathParam);
                }
            }
        }

        RequestBuilder builder;
        if (HttpMethod.GET.equals(method)) {
            builder = RequestBuilder.get().setUri(urlStr);
        } else if (HttpMethod.POST.equals(method)) {
            builder = RequestBuilder.post().setUri(urlStr);
            setBody(contentType,operation,builder, params);
        } else if (HttpMethod.PUT.equals(method)) {
            builder = RequestBuilder.put().setUri(urlStr);
            setBody(contentType,operation,builder, params);
        } else if (HttpMethod.DELETE.equals(method)) {
            builder = RequestBuilder.delete().setUri(urlStr);
        } else if (HttpMethod.PATCH.equals(method)) {
            builder = RequestBuilder.patch().setUri(urlStr);
            setBody(contentType,operation,builder, params);
        } else {
            throw new IllegalArgumentException("Method " + method + " is not supported");
        }



        for ( Parameter param : operation.getParameters() )
        {
            String key = param.getName();

            /*if (params.containsKey(key)) {

                if (param instanceof PathParameter) {
                    String cf = ((PathParameter) param).getCollectionFormat();
                    List<String> strings = RestUtils.getParamAsStrings(cParams.remove(key), cf);
                    String pathParam = strings.get(0);
                    urlStr = urlStr.replace("{" + key + "}", pathParam);
                    builder.setUri(urlStr);
                }
            }*/


            if (param instanceof HeaderParameter)
            {
                String cf = ((HeaderParameter) param).getCollectionFormat();

                if (!cParams.containsKey(key) && ((HeaderParameter) param).getDefault()!=null )
                    cParams.put(key,((HeaderParameter) param).getDefault());

                if (cParams.containsKey(key)) {
                    List<String> strings = RestUtils.getParamAsStrings(cParams.remove(key), cf);
                    for (String s : strings)
                        builder.addHeader(key, s);
                }
            }

            if (param instanceof QueryParameter)
            {
                String cf = ((QueryParameter) param).getCollectionFormat();

                if (!cParams.containsKey(key) && ((QueryParameter) param).getDefault()!=null )
                    cParams.put(key,((QueryParameter) param).getDefault());

                if (cParams.containsKey(key)) {

                    Object value = cParams.remove(key);

                    if (value instanceof ISearchParams){
                        for(String p: ((ISearchParams)value).keySet()){
                            List<String> strings = RestUtils.getParamAsStrings(((ISearchParams)value).get(p), cf);
                            for (String s : strings)
                                builder.addParameter(p, s);
                        }
                    } else {
                        List<String> strings = RestUtils.getParamAsStrings(value, cf);
                        for (String s : strings)
                            builder.addParameter(key, s);
                    }


                }
            }


        }

        return builder;

    }

}
