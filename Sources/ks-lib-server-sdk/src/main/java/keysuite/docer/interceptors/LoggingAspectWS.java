package keysuite.docer.interceptors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.underscore.lodash.U;
import com.google.common.base.Strings;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import it.kdm.docer.commons.TicketCipher;
import it.kdm.docer.commons.Utils;
import it.kdm.docer.commons.configuration.ResourceCache;
import it.kdm.docer.providers.solr.SolrBaseUtil;
import it.kdm.docer.sdk.classes.xsd.KeyValuePair;
//import it.kdm.orchestratore.appdoc.utils.URLHelper;
//import it.kdm.orchestratore.session.Session;
//import it.kdm.orchestratore.session.UserInfo;
//import it.kdm.orchestratore.utils.KDMUtils;
import keysuite.cache.ClientCache;
import keysuite.cache.ClientCacheAuthUtils;
import keysuite.desktop.exceptions.*;
import keysuite.SessionUtils;
import keysuite.docer.client.ClientUtils;
import keysuite.docer.client.DocerBean;
import keysuite.docer.client.Group;
import keysuite.docer.client.User;
import it.kdm.docer.ws.AuthenticationServiceEndpoint;
import it.kdm.docer.ws.DocerServicesEndpoint;
import it.kdm.docer.ws.DualProtocolSaajSoapMessageFactory;
import it.kdm.docer.ws.WSTransformer;
import org.apache.commons.httpclient.auth.AuthenticationException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.ThreadContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeTypeUtils;
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

import static it.kdm.docer.ws.AuthenticationServiceEndpoint.TICKET_KEY;

//import static it.kdm.docer.commons.handlers.ServiceFilterUtils.TICKET_KEY;

//import keysuite.docer.providers.SolrBaseUtil;

@Aspect
@Component
public class LoggingAspectWS extends LoggingAspect {

    @Autowired
    Environment env;

    @Around("@within(org.springframework.ws.server.endpoint.annotation.Endpoint)")
    public Object adviseWSBeforeInvocation(ProceedingJoinPoint joinPoint) throws Throwable {

        ThreadContext.clearMap();
        ThreadContext.clearStack();

        String baseUrl = SessionUtils.getBaseUrl(SessionUtils.getRequest());

        //String ctx = env.getProperty("server.servlet.context-path","");

        DocerBean.baseUrl.set( System.getProperty("docer.public-url",baseUrl) );
        SolrBaseUtil.extraFields.set(null);

        String operationId = null;
        try {
            if ("GET".equals(SessionUtils.getRequest().getMethod()))
                return joinPoint.proceed(joinPoint.getArgs());

            long t0 = System.currentTimeMillis();

            String serviceName = joinPoint.getSignature().getDeclaringType().getSimpleName();
            String method = joinPoint.getSignature().getName();
            Object request = joinPoint.getArgs()[0];
            //String IPAddress = Session.getRequest().getRemoteAddr();

            String token = null;
            JAXBElement<String> jToken = null;

            if (request instanceof String){
                token = (String) request;
            } else{
                jToken = getToken(request);
                if (jToken!=null)
                    token = jToken.getValue();
            }

            if (token!=null){
                //String token = jToken.getValue();

                //if (token==null)
                //    throw new Exception("null");

                String callType = Utils.extractTokenKeyNoExc(token, "calltype");
                String ticket;
                String codEnte;
                String codAoo;
                String uid;

                try {
                    if ("internal".equals(callType)) {
                        ticket = Utils.removeTokenKey(token, "calltype").replace("|", "");
                        String decrypted = new TicketCipher().decryptTicket(ticket);
                        codEnte = Utils.extractTokenKeyNoExc(decrypted, "ente");
                        uid = Utils.extractTokenKeyNoExc(decrypted, "uid");
                        codAoo = Utils.extractTokenKeyNoExc(decrypted, "aoo");
                    } else if (token.contains("|")) {
                        AuthenticationServiceEndpoint.verifyToken(token);
                        String docTicket = Utils.extractTokenKeyNoExc(token, TICKET_KEY);
                        codEnte = Utils.extractTokenKeyNoExc(token, "ente");
                        uid = Utils.extractTokenKeyNoExc(token, "uid");
                        codAoo = Utils.extractTokenKeyNoExc(token, "aoo");
                        ticket = new TicketCipher().decryptTicket(docTicket);

                    } else {
                        User user = ClientCacheAuthUtils.getInstance().getUser(token);
                        Claims claims = ClientUtils.parseJWTTokenWithoutSecret(token);

                        if (user==null){
                            throw new AuthenticationException("user "+claims.getSubject()+" not found");
                        }
                        ticket = ClientCacheAuthUtils.getInstance().getDMSTicket(user,(String) claims.get("secret"));
                        codEnte = user.getCodEnte();
                        codAoo = user.getCodAoo();
                        uid = user.getUserName();
                    }

                    if (!serviceName.equals(DocerServicesEndpoint.class.getSimpleName())){

                        if (!Strings.isNullOrEmpty(codAoo))
                            ticket = String.format("ente:%s|aoo:%s|uid:%s|documentale:%s|",codEnte,codAoo,uid,ticket);
                        else
                            ticket = String.format("ente:%s|uid:%s|documentale:%s|",codEnte,uid,ticket);

                        if ("verifyToken".equals(method)){
                            if (!token.contains("|")){
                                Claims claims = ClientUtils.parseJWTTokenWithoutSecret(token);

                                String exp = ""+claims.getExpiration().getTime();

                                ticket = Utils.addTokenKey(ticket, "expiration", exp);

                                String md5 = ClientCacheAuthUtils.generateMD5(ticket);

                                ticket = Utils.addTokenKey(ticket, "md5", md5);
                            } else {
                                ticket = token;
                            }
                        }


                    }

                    ThreadContext.put("uid", uid );
                    ThreadContext.put("ente", codEnte );
                    ThreadContext.put("aoo", codAoo );

                } catch (JwtException jwte) {
                    throw new AuthenticationException(jwte.getMessage());
                }
                if (codEnte!=null && !codEnte.equals("system")) {
                    Group ente = ClientCache.getInstance().getEnte(codEnte);
                    if(ente!=null){
                        String prefix = ente.getPrefix();
                        if (!Strings.isNullOrEmpty(prefix))
                            ThreadContext.put("prefix", prefix+"__");
                    }
                }

                if (jToken!=null)
                    jToken.setValue(ticket);
                else
                    joinPoint.getArgs()[0] = ticket;
            }

            String ct = SessionUtils.getRequest().getHeader("content-type");
            boolean isSoap = false;
            if (ct!=null && (ct.contains(MimeTypeUtils.TEXT_XML_VALUE) || ct.contains("soap")))
                isSoap = true;

            if (isSoap)
                operationId = logIn(joinPoint);

            Object res = joinPoint.proceed(joinPoint.getArgs());

            long elapsed = System.currentTimeMillis()-t0;

            if (operationId!=null)
                logOut(elapsed, operationId,joinPoint,res);

            return res;
        } catch ( Exception e) {
            if (operationId!=null)
                logError(operationId,joinPoint,e);
            throw e;
        }
    }
}
