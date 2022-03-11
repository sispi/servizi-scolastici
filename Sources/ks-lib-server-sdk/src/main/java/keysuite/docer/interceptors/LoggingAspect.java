package keysuite.docer.interceptors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.underscore.lodash.U;
import com.google.common.base.Strings;
import it.kdm.docer.commons.configuration.ResourceCache;
import it.kdm.docer.sdk.classes.xsd.KeyValuePair;
import it.kdm.docer.ws.DualProtocolSaajSoapMessageFactory;
import it.kdm.docer.ws.WSTransformer;
import keysuite.SessionUtils;
import keysuite.desktop.exceptions.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.ThreadContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;
import org.springframework.ws.soap.SoapMessage;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBElement;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.nio.charset.Charset;
import java.util.*;

//import keysuite.docer.providers.SolrBaseUtil;

public class LoggingAspect {

    protected final static Logger logger = LoggerFactory.getLogger(LoggingAspectWS.class);

    protected final static Logger auditLogger = LoggerFactory.getLogger("AUDIT");

    protected ResourceCache.ResourceFile auditConf = null;
    //List<Map> auditGroups = null;
    protected Map<String,Map> sections;

    protected final static ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    protected Map getAuditSection(String group,String section){

        if (auditConf==null || !auditConf.isValid()){
            auditConf = null;
            sections = null;
            ResourceCache.ResourceFile res = ResourceCache.get("audit.xml");
            String xml;
            try {
                xml = FileUtils.readFileToString(res, Charset.defaultCharset());

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Map<String,Object> map = (Map<String,Object>) U.fromXml(xml);

            List<Map> auditGroups = U.get(map,"audit.group");

            Map<String,Map> _sections = new LinkedHashMap<>();

            for( Map g : auditGroups ){

                String name = (String) g.get("-name");
                Object sections = g.get("section");

                if (sections==null)
                    continue;

                if (sections instanceof Map)
                    sections = Collections.singletonList((Map) sections);

                for( Map m : (List<Map>) sections ){

                    try {

                        String sLvl = (String) m.getOrDefault("-level", "debug");
                        String sName = (String) m.get("-name");

                        if (Strings.isNullOrEmpty(sName))
                            continue;

                        m.put("-level", sLvl);

                        _sections.put(name + sName, m);

                        Object p = m.get("param");

                        if (p!=null) {
                            if (p instanceof Map)
                                p = Collections.singletonList((Map) p);

                            Map<String, String> params = new LinkedHashMap<>();
                            Map<String, String> xparams = new LinkedHashMap<>();

                            for (Map m2 : (List<Map>) p){

                                if (m2.containsKey("#item"))
                                    continue;

                                String aname = (String) m2.get("-name");
                                String xname = (String) m2.get("-xname");

                                if (xname==null){
                                    String xpath = (String) m2.get("-xpath");
                                    if (xpath!=null && xpath.contains(":"))
                                        xname = xpath.split(":")[1].split("/")[0];
                                }

                                if (aname==null && xname==null){
                                    aname = "request";
                                    xname = "request";
                                }

                                if (aname!=null)
                                    params.put( aname, (String) m2.getOrDefault("-level", sLvl));

                                if (xname!=null)
                                    xparams.put( xname, (String) m2.getOrDefault("-level", sLvl));
                            }

                            m.put("params", params);
                            m.put("xparams", xparams);
                        }

                        Object r = m.get("result");

                        if (r!=null) {
                            if (r instanceof Map)
                                r = Collections.singletonList((Map) r);

                            Map<String, String> results = new LinkedHashMap<>();
                            Map<String, String> xresults = new LinkedHashMap<>();

                            for (Map m2 : (List<Map>) r){

                                if (m2.containsKey("#item"))
                                    continue;

                                String aname = (String) m2.get("-name");
                                String xname = (String) m2.get("-xname");

                                if (aname==null && xname==null){
                                    aname = "return";
                                    xname = "return";
                                }

                                if (aname!=null)
                                    results.put(aname, (String) m2.getOrDefault("-level", sLvl));

                                if (xname!=null)
                                    xresults.put(xname, (String) m2.getOrDefault("-level", sLvl));
                            }

                            m.put("results", results);
                            m.put("xresults", xresults);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            sections = _sections;
            auditConf = res;
        }

        return sections.get(group+section);
    }

    //@Autowired
    //SampleJmsMessageSender jmsMessageSender;

    public LoggingAspect(){
    }

    protected boolean shouldLog(String level){
        return "info".equals(level) && auditLogger.isInfoEnabled() ||
                "debug".equals(level) && auditLogger.isDebugEnabled() ||
                "trace".equals(level) && auditLogger.isTraceEnabled() ;
    }

    protected String logIn(ProceedingJoinPoint joinPoint){
        String operationId = UUID.randomUUID().toString();
        return log(TraceMessage.Type.IN,operationId,0,joinPoint,null);
    }

    protected String logOut(long elapsed, String operationId, ProceedingJoinPoint joinPoint,Object extraData){
        return log(TraceMessage.Type.OUT,operationId,elapsed,joinPoint,extraData);
    }

    protected String logError(String operationId, ProceedingJoinPoint joinPoint, Exception message){
        return log(TraceMessage.Type.EXCEPTION,operationId,0,joinPoint,message);
    }

    protected String getString(Object value){
        if (value==null)
            return "";
        if (value instanceof String || value instanceof Number || value instanceof Date)
            return String.valueOf(value);
        else {
            try {
                return mapper.writeValueAsString(value);
            } catch (JsonProcessingException e) {
                return String.valueOf(value);
            }
        }
    }

    protected String getJson(Object obj){
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    protected String getThreadEnvelope(){

        SoapMessage message = DualProtocolSaajSoapMessageFactory.soapMessage.get();
        if (message==null)
            return null;

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            message.writeTo(out);
            return new String(out.toByteArray());
        } catch (IOException e) {
            return null;
        }
    }

    protected Object getPart(Object obj, String part){

        if (obj==null)
            return null;

        if (obj instanceof List) {
            List list = (List) obj;
            if (list.size()>0){
                if (list.get(0) instanceof KeyValuePair){
                    obj = WSTransformer.toMap(list,false);
                }
            }
        }

        Object value;

        if (Strings.isNullOrEmpty(part)){
            value = obj;
        } else {
            value = null;
            if (obj instanceof Map)
                value = ((Map)obj).get(part);
            else {
                Method m = ReflectionUtils.findMethod(obj.getClass(), "get"+part.toUpperCase().charAt(0)+part.substring(1));
                if (m!=null){
                    try {
                        value = m.invoke(obj);
                    } catch (Exception e) {
                    }
                }
            }
        }

        if (value instanceof JAXBElement)
            value = ((JAXBElement) value).getValue();

        return value;
    }

    String log( TraceMessage.Type tipo, String operationId, long elapsed, ProceedingJoinPoint joinPoint, Object extraData){

        HttpServletRequest req = SessionUtils.getRequest();

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method mth = signature.getMethod();
        Class cls = signature.getDeclaringType();

        String service = cls.getSimpleName();

        boolean endpoint = service.endsWith("Endpoint");

        String method = mth.getName();

        Logging logging = mth.getAnnotation(Logging.class);
        Logging loggingClass = (Logging) cls.getAnnotation(Logging.class);

        if (loggingClass!=null){
            String grp = loggingClass.group();
            if (!Strings.isNullOrEmpty(grp)){
                service = grp;
            }
        }

        if (logging!=null){
            String section = logging.section();
            if (!Strings.isNullOrEmpty(section)){
                method = section;
            }
        }

        if (TraceMessage.Type.IN.equals(tipo)) {
            //UserInfo ui = Session.getUserInfo();

            //ThreadContext.clearMap();
            //ThreadContext.clearStack();
            ThreadContext.put("ip", req.getRemoteAddr() );
            ThreadContext.put("startTs", "" + System.currentTimeMillis());
            ThreadContext.put("operationId", operationId);
            ThreadContext.put("method", method);
            ThreadContext.put("service", service);

        }

        Map section = getAuditSection(service,method);

        if (section==null)
            return null;

        String level = (String) section.getOrDefault("-level","debug");

        if (TraceMessage.Type.IN.equals(tipo)) {

            String message = "";
            if (shouldLog(level)){
                List<String> extras = new ArrayList<>();
                //String sid = null;
                //String sidArg = loggingClass.sidArg();

                Parameter[] pars = mth.getParameters();
                Object[] args = joinPoint.getArgs();

                Map<String,String> params = endpoint ?
                        (Map<String,String>) section.get("xparams") :
                        (Map<String,String>) section.get("params");

                if ("login".equals(method)){
                    if (endpoint){
                        ThreadContext.put("uid", (String) getPart(args[0],"username") );
                        ThreadContext.put("ente", (String) getPart(args[0],"codiceEnte") );
                    } else {
                        ThreadContext.put("uid", (String) args[1] );
                        ThreadContext.put("ente", (String) args[0] );
                        //TODO
                    }
                }

                for( String key: params.keySet() ){

                    String lvl = params.getOrDefault(key,"debug");

                    String part = null;
                    if (key.contains(".")){
                        part = key.split("\\.")[1];
                        key = key.split("\\.")[0];
                    }

                    String extra = "";
                    if (shouldLog(lvl)){
                        Object input = null;
                        if (key.equalsIgnoreCase("request") && part!=null){
                            key = part;
                            part = null;
                        }
                        if (endpoint){
                            input = args[0];
                            if (key.equalsIgnoreCase("request")){
                                input = getThreadEnvelope();
                            } else {
                                input = getPart(input,key);
                            }
                        } else {
                            if (key.equalsIgnoreCase("request")){
                                input = getJson(args);
                            } else {
                                for (int i = 0; i < pars.length; i++) {
                                    if (pars[i].getName().equals(key)) {
                                        input = args[i];
                                        break;
                                    }
                                }
                            }
                        }
                        extra = getString(getPart( input, part ));

                        extras.add(extra);
                    }


                }

                message = StringUtils.join(extras,'|');

                ThreadContext.put("extra-in", message );
            }

            if (auditLogger.isDebugEnabled()) {
                auditLogger.debug(message);
            }

        } else if (TraceMessage.Type.OUT.equals(tipo)) {

            if (shouldLog(level)){
                List<String> extras = new ArrayList<>();

                Map<String,String> results = endpoint ?
                        (Map<String,String>) section.get("xresults"):
                        (Map<String,String>) section.get("results");
                Map<String,Object> reqExtra = (Map) req.getAttribute("extraData");

                if (results!=null){
                    for( String key: results.keySet() ){

                        String lvl = results.getOrDefault(key,"debug");

                        String part = null;
                        if (key.contains(".")){
                            part = key.split("\\.")[1];
                            key = key.split("\\.")[0];
                        }

                        String extra = "";
                        if (shouldLog(lvl)){
                            Object output = extraData;
                            if (endpoint)
                                output = getPart(output,"return");

                            if (!"return".equals(key)){
                                if (reqExtra!=null && reqExtra.containsKey(key))
                                    output = reqExtra.get(key);
                                else {
                                    output = getPart(output,key);
                                }
                            }

                            extra = getString(getPart(output,part));

                        }
                        extras.add(extra);
                    }
                }

                String message = StringUtils.join(extras,'|');

                ThreadContext.put("extra-out", message);
                ThreadContext.put("elapsed" , elapsed+"ms");

                if ("info".equals(level))
                    auditLogger.info(message);
                else if ("debug".equals(level))
                    auditLogger.debug(message);
                else if ("trace".equals(level))
                    auditLogger.trace(message);
            }

        } else if (TraceMessage.Type.EXCEPTION.equals(tipo)) {

            String code = "";
            if (extraData instanceof CodeException)
                code = ((CodeException)extraData).getCode().toString();

            String message = String.format("%s|%s",code,extraData);

            ThreadContext.put("extra-out", message );
            ThreadContext.put("elapsed" , elapsed+"ms");

            auditLogger.error(extraData.toString());

        } else if (TraceMessage.Type.EXCEPTION.equals(tipo)) {
            if (auditLogger.isDebugEnabled()) {
                auditLogger.debug(String.valueOf(extraData));
            }
        }

        return operationId;
    }



    //@Pointcut("execution(@org.springframework.web.bind.annotation.RestController * *(..))")
    //public void isAnnotated() {}

    protected JAXBElement<String> getToken(Object request){

        Class requestCls = request.getClass();

        try {
            JAXBElement<String> jToken = (JAXBElement<String>) requestCls.getDeclaredMethod("getToken").invoke(request);
            return jToken;
        } catch (Exception e) {
            return null;
        }
    }

    /*@AfterThrowing(pointcut = "@within(org.springframework.web.bind.annotation.RestController)", throwing = "ex")
    public void logError(Exception ex) {
        //ex.printStackTrace();
        logger.error("unhandled exception",ex);
    }*/

    public static class TraceMessage {

        public enum Type {
            IN,
            OUT,
            EXCEPTION,
            DEBUG
        }
    }
}
