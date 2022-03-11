package keysuite.docer.interceptors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.underscore.lodash.U;
import com.google.common.base.Strings;
import it.kdm.docer.commons.configuration.ResourceCache;
import it.kdm.docer.providers.solr.SolrBaseUtil;
import it.kdm.docer.sdk.classes.xsd.KeyValuePair;
import it.kdm.docer.ws.DualProtocolSaajSoapMessageFactory;
import it.kdm.docer.ws.WSTransformer;
import it.kdm.orchestratore.session.Session;
import it.kdm.orchestratore.session.UserInfo;
import keysuite.SessionUtils;
import keysuite.desktop.exceptions.*;
import keysuite.docer.client.DocerBean;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.ThreadContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.ws.wsdl.wsdl11.SimpleWsdl11Definition;
import org.springframework.ws.wsdl.wsdl11.Wsdl11Definition;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBElement;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.nio.charset.Charset;
import java.util.*;

//import keysuite.docer.providers.SolrBaseUtil;

@Aspect
@Component
public class LoggingAspectRest extends LoggingAspect {

    @Autowired
    Environment env;

        //@Around("execution(* *.*(..))")
    //@Around("execution(* keysuite.docer.client.IFascicolazione.*(..))")
    @Around("@within(org.springframework.web.bind.annotation.RestController)")
    public Object adviseRestBeforeInvocation(ProceedingJoinPoint joinPoint) throws Throwable{

        ThreadContext.clearMap();
        ThreadContext.clearStack();

        //Access-Control-Allow-Origin

        SessionUtils.getResponse().setHeader("Access-Control-Allow-Origin","*");

        long t0 = System.currentTimeMillis();

        String baseUrl = System.getProperty("docer.public-url");

        if (baseUrl==null){
            HttpServletRequest req = SessionUtils.getRequest();

            baseUrl = Session.getHostURL(req);
            String prefix = req.getHeader("X-Forwarded-Prefix");

            if (prefix!=null)
                baseUrl += prefix;
        }

        //String ctx = env.getProperty("server.servlet.context-path","");

        DocerBean.baseUrl.set(baseUrl);
        SolrBaseUtil.extraFields.set(null);

        //if (!(joinPoint.getTarget() instanceof SystemController)) {
        String method = joinPoint.getSignature().getName();

        if ((joinPoint.getSignature().getDeclaringType().getSimpleName().equals("FilesController") && method.equals("get"))){
            UserInfo ui = Session.getUserInfo();
            if (ui!=null && ui.getUsername()!=null){
                ThreadContext.put("uid", ui.getUsername() );
                ThreadContext.put("ente", ui.getCodEnte() );
                ThreadContext.put("aoo", ui.getCodAoo() );
            }
        } else if (    !method.equals("index") &&
                !method.equals("adminsolr") &&
                !method.equals("login") &&
                !method.equals("ping") &&
                !SessionUtils.getRequest().getMethod().equals("OPTIONS")) {

            UserInfo ui = Session.getUserInfo();
            if (ui.getUsername()==null)
                throw new KSRuntimeException(Code.H401);
            logger.debug("ui:" + ui.getUsername());

            ThreadContext.put("uid", ui.getUsername() );
            ThreadContext.put("ente", ui.getCodEnte() );
            ThreadContext.put("aoo", ui.getCodAoo() );
        }

        String operationId = logIn(joinPoint);

        String retryPolicy = SessionUtils.getRequest().getHeader("retryPolicy");
        Long[] delays;
        if (retryPolicy!=null){
            delays = new Long[0];

            if (!Strings.isNullOrEmpty(retryPolicy) && !retryPolicy.equals("no-retry")){

                String[] str = retryPolicy.split(",");
                delays = new Long[str.length];
                for( int i=0; i<str.length; i++){
                    delays[i]= Long.parseLong(str[i]);
                }
            }

        }else{
            delays = env.getProperty("retryPolicy", Long[].class,new Long[0]);
        }

        int executions = -1;

        while(true) {
            executions++;

            try {
                Object res = joinPoint.proceed();
                long elapsed = System.currentTimeMillis()-t0;
                logOut(elapsed, operationId,joinPoint,res);
                return res;
            } catch (Exception e) {

                MethodSignature signature = (MethodSignature) joinPoint.getSignature();

                if (e instanceof CodeException && ((CodeException) e).getCode().isRetryable() && executions<delays.length){

                    SessionUtils.getRequest().setAttribute("isRetry","true");

                    logger.warn("unhandled exception but retry",e);

                    long delay = delays[executions];
                    Thread.sleep(delay);
                    logger.warn("retry #"+(executions+1)+" after "+delay+"ms");
                    continue;
                }
                if (!(e instanceof KSExceptionNotFound) && !(e instanceof KSExceptionBadRequest) && !(e instanceof KSExceptionForbidden))
                    logger.error("unhandled exception",e);

                logError(operationId,joinPoint,e);
                throw e;
            }
        }
    }

}
