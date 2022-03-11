package keysuite.desktop.controllers;

import io.swagger.models.HttpMethod;
import io.swagger.models.Operation;
import io.swagger.models.Response;
import io.swagger.models.Swagger;
import io.swagger.models.parameters.HeaderParameter;
import io.swagger.models.parameters.Parameter;
import io.swagger.models.parameters.QueryParameter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import keysuite.desktop.DesktopUtils;
import keysuite.desktop.exceptions.*;
import keysuite.swagger.client.ClientFactory;
import keysuite.swagger.client.SwaggerClient;
import static keysuite.swagger.client.SwaggerClient.HTTP_STATUS_PIN;
import static keysuite.swagger.client.SwaggerClient.PIN_RESULT;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
public class SwaggerController {

    @Autowired
    Environment env;

    @Autowired
    protected HttpServletRequest request;

    private String findOperationUrl(HttpServletRequest request,Swagger swagger){

        String servletPath = request.getServletPath();

        int idx = servletPath.indexOf("/","/swagger/".length()+1);

        String url = servletPath.substring(idx+1);

        String basePath = swagger.getBasePath();
        if (basePath!=null && basePath.startsWith("/"))
            basePath = basePath.substring(1);

        if (url.startsWith(basePath))
            url = url.substring(basePath.length()+1);

        for( String key: swagger.getPaths().keySet()){
            String regex = key.replaceAll("(\\{[^}]+})","[^/]+");
            if (regex.startsWith("#"))
                regex  = regex.substring(1);
            if (regex.startsWith("/"))
                regex  = regex.substring(1);
            if (url.matches(regex)){
                return key;
            }
        }

        return null;
    }

    @RequestMapping({"/swagger/{service}/**","/swagger/{service}"})
    public Object view(@PathVariable String service, HttpServletResponse response, HttpServletRequest request) throws KSException {

        //String path = env.getProperty("swagger."+service);
        //if (path==null)
        //    path = "swagger/"+service+".json";

        Swagger swagger = null;
        try {
            swagger = ClientFactory.getRESTService("swagger/"+service+".json");
        } catch (IOException e) {
            throw new KSRuntimeException(e);
        }

        if (swagger==null)
            throw  new KSExceptionNotFound("servizio non trovato:"+service);

        if (request.getServletPath().indexOf("/","/swagger/".length()+1)==-1){

            return ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON).body(swagger);
        }

        SwaggerClient client = new SwaggerClient(swagger);

        String operationUrl = findOperationUrl(request,swagger);

        if (operationUrl==null){
            throw new KSExceptionNotFound("operation not found");
        }

        Map<HttpMethod,Operation> map = swagger.getPath(operationUrl).getOperationMap();

        HttpMethod httpMethod = HttpMethod.valueOf(request.getMethod());

        Operation operation = map.get(httpMethod);

        if (operation==null && map.keySet().size()==1)
            operation = map.values().iterator().next();

        if (operationUrl==null){
            throw new KSExceptionNotFound("operation not found");
        }

        Map<String,Parameter> parameterMap = new LinkedHashMap<>();
        Map<String,Object> params = new LinkedHashMap<>(request.getParameterMap());

        for( Parameter parameter : operation.getParameters()){

            if (parameter instanceof QueryParameter && parameter.getName().equals("params") ){
                params.put("params",request.getQueryString());
            }

            if (parameter instanceof HeaderParameter && parameter.getName().equals("Authorization")){
                String header = request.getHeader(parameter.getName());
                if (header!=null)
                    params.put(parameter.getName(),header);
            }
            parameterMap.put(parameter.getName(),parameter);
        }

        if (request.getContentLength()>0){
            try{
                if (request.getContentType().equals(MediaType.APPLICATION_JSON)){
                    String bodyString = IOUtils.toString(request.getInputStream());
                    Map<String,Object> body = DesktopUtils.parseJson(bodyString,Map.class);
                    params.putAll(body);
                }
            } catch (Exception e){
                throw new KSExceptionBadRequest("invalid content");
            }
        }

        Map<String,Object> results  = client.executeByMap(operationUrl , params );

        Object result = results.remove(PIN_RESULT);

        Integer httpCode = (Integer) results.remove(HTTP_STATUS_PIN);

        if (httpCode!=null){

            response.setStatus(httpCode);

            Response resp = operation.getResponses().get(""+httpCode);

            if (resp != null && resp.getHeaders()!=null){
                for( String key : resp.getHeaders().keySet()) {
                    String header = (String) results.get(key);
                    if (header!=null)
                        response.addHeader(key,header);
                }
            }
        }

        return result;
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Object handleError(Model model, Throwable exception) {
        CodeException ce = CodeException.getCodeException(exception); //.setUrl(URI);
        if (ce.getUrl()==null){
            ce.setUrl(request.getRequestURL().toString());
        }
        return ce;
    }
}
