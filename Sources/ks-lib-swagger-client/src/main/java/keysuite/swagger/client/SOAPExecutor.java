package keysuite.swagger.client;

import bpmn2.ServiceException;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.Module;
import io.swagger.models.ModelImpl;
import io.swagger.models.Path;
import io.swagger.models.Swagger;
import io.swagger.models.parameters.BodyParameter;
import io.swagger.models.parameters.Parameter;
import io.swagger.util.DeserializationModule;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;


public class SOAPExecutor {

    private static final Logger logger = LoggerFactory.getLogger(SOAPExecutor.class);

    private Swagger swagger;
    private Object result;
    private Map<String,String> inputHeaders = new HashMap<>();
    private Map<String,String> inputHttpHeaders = new HashMap<>();
    private Map<String,String> outputHeaders = new HashMap<>();
    private Map<String,String> outputHttpHeaders = new HashMap<>();
    private Map<String,Object> input = new HashMap<>();

    public SOAPExecutor(Swagger swagger) {
        this.swagger = swagger;
    }

    public Swagger getSwagger() {
        return swagger;
    }

    public String getOpenAPI() {
        return getOpenAPI(swagger);
    }

    public static String getOpenAPI(Swagger swagger) {

        ObjectMapper mapper = new ObjectMapper();

        Module deserializerModule = new DeserializationModule(true, true);
        mapper.registerModule(deserializerModule);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        ObjectWriter ow = mapper.writer(new DefaultPrettyPrinter());

        try {
            String json = ow.writeValueAsString(swagger);

            return json;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String toJSON(Object obj) {
        return RestUtils.toJSON(obj);
    }

    public Map<String,Object> getResult() {
        if (result instanceof Map)
            return (Map) result;
        else
            return null;
    }

    public Object getResultValue() {

        if (result instanceof Map)
            return ((Map)result).values().iterator().next();
        else
            return result;
    }

    public Object getResult(String key) {

        if (result instanceof Map)
            return ((Map)result).get(key);
        else
            return null;
    }

    public Map<String,String> getOutputHTTPHeaders() {
        return outputHttpHeaders;
    }

    public String getOutputHTTPHeader(String header) {
        return outputHttpHeaders.get(header);
    }
    public Map<String,String> getOutputHeaders() {
        return outputHeaders;
    }

    public String getOutputHeader(String header) {
        return outputHeaders.get(header);
    }

    public void addInputHeader(String name, String value)
    {
        this.inputHeaders.put(name,value);
    }

    public void addInputHTTPHeader(String name, String value)
    {
        this.inputHttpHeaders.put(name,value);
    }

    public void addInput(String name, Object value)
    {
        this.input.put(name,value);
    }

    public void setInputMap( Map<String,Object> input) {
        this.input = input;
    }

    public Object execute(String operation) throws ServiceException {
        return executeByMap(operation,input);
    }

    public Object execute(String operation, Object... params) throws ServiceException {

        Path path = swagger.getPath(operation);

        if (path==null && !operation.startsWith("#"))
            path = swagger.getPath("#"+operation);

        if (path==null)
            throw new RuntimeException("operation not found");

        io.swagger.models.Operation sOperation = path.getPost();

        Map<String,Object> map = new HashMap<>();

        int idx = 0;
        for( Parameter p : sOperation.getParameters() )
        {
            if (p instanceof BodyParameter)
            {
                for ( String key :  ((BodyParameter) p).getSchema().getProperties().keySet() )
                {
                    map.put( key, params[idx++] );

                    if (idx>=params.length)
                        break;
                }
            }
        }

        return executeByMap(sOperation,map);
    }

    public Object executeByMap(String operation, Map<String,Object> params) throws ServiceException {

        Path path = swagger.getPath(operation);

        if (path==null && !operation.startsWith("#"))
            path = swagger.getPath("#"+operation);

        if (path==null)
            throw new RuntimeException("operation not found");

        return executeByMap(path.getPost(),params);
    }

    private Object executeByMap(io.swagger.models.Operation sOperation, Map<String,Object> params) throws ServiceException {

        String host = swagger.getSchemes().get(0).toValue().toLowerCase() + "://" + swagger.getHost() + swagger.getBasePath();

        outputHeaders.clear();

        RequestBuilder builder = RequestBuilder.post().setUri(host);

        ModelImpl model=null;

        for ( Parameter p : sOperation.getParameters() )
            if (p.getIn().equals("body"))
            {
                model = (ModelImpl) ((BodyParameter) p).getSchema();
                break;
            }

        if (model==null)
            throw new RuntimeException("invalid model for SOAP");

        SOAPEnvelopeBuilder soapBuilder = new SOAPEnvelopeBuilder(inputHeaders,params,model);

        HttpEntity entity = soapBuilder.build();
        builder.setEntity(entity);

        HttpClient httpClient=null;
        try {
            HttpUriRequest request = builder.build();

            if (logger.isDebugEnabled())
                logger.debug("request line:{} headers:{}", request.getRequestLine(), request.getAllHeaders());

            Object srvTimeout = params.get("srvTimeout");

            Integer readTimeout = srvTimeout!=null ? Integer.parseInt(srvTimeout.toString()) : SwaggerClient.READ_TIMEOUT;
            Integer connectTimeout = Math.max(new Double(readTimeout/SwaggerClient.CONNECT_TIMEOUT_RATIO).intValue(), SwaggerClient.CONNECT_TIMEOUT_MIN);

            httpClient = RestUtils.getHttpClient(readTimeout, connectTimeout);

            //add HTTP SOAPAction Header
            request.addHeader("SOAPAction", sOperation.getOperationId() );

            /*if (model.getVendorExtensions().containsKey("x-SOAPAction")){
                builder.addHeader("SOAPAction", (String) model.getVendorExtensions().get("x-SOAPAction"));
            }
            else if (model.getXml()!=null)
            {
                String tns = model.getXml().getNamespace();
                String xmlPath = model.getXml().getName();
                int idx = xmlPath.indexOf("/");
                String soapAction;
                if (idx>0)
                {
                    soapAction = xmlPath.substring(0,idx);
                } else {
                    soapAction = xmlPath;
                }
                String tnsPrefix = model.getXml().getPrefix();

                if (!Strings.isNullOrEmpty(tnsPrefix))
                    builder.addHeader("SOAPAction",tnsPrefix+":"+soapAction);
                else if (!Strings.isNullOrEmpty(tns))
                    builder.addHeader("SOAPAction",tns+soapAction);
                else
                    builder.addHeader("SOAPAction",soapAction);
            }*/

            //add custom http headers
            for (String key : this.inputHttpHeaders.keySet())
                request.addHeader(key,inputHttpHeaders.get(key));

            HttpResponse httpResponse = httpClient.execute(request);

            SOAPEnvelopeParser parser = new SOAPEnvelopeParser(httpResponse.getEntity(),sOperation.getResponses());

            Object soapResponse = parser.parse();

            outputHeaders.putAll(parser.getOutputHeaders());

            Header[] headers = httpResponse.getAllHeaders();

            //retirieve response http headers
            for (int idx=0;idx<headers.length;idx++)
                outputHttpHeaders.put(headers[idx].getName(),headers[idx].getValue());

            if (logger.isDebugEnabled())
                logger.debug("request status:{} headers:{} entity:{}", httpResponse.getStatusLine(), httpResponse.getAllHeaders(), httpResponse.getEntity());

            if (httpResponse.getStatusLine().getStatusCode()!=200){
                if (soapResponse instanceof Map)
                    throw SOAPFaultException.create( (Map) soapResponse);
                else
                    throw SOAPFaultException.create( new HashMap<String,Object>());
            }

            result = soapResponse;
            inputHeaders.clear();
            input.clear();
            return result;

        } catch (ServiceException e) {

            throw e;
        } catch (Exception e) {
            logger.error("error executing REST",e);
            throw new RuntimeException(e);
        }finally {
            try {
                if (httpClient!=null)
                    ((CloseableHttpClient) httpClient).close();
            } catch( Exception e ) {
                // no idea if this throws something, but we still don't care!
            }
        }
    }
}
