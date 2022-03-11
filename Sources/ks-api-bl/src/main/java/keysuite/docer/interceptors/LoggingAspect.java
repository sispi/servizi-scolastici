package keysuite.docer.interceptors;

import com.google.common.base.Strings;
import it.kdm.orchestratore.session.Session;
import it.kdm.orchestratore.utils.KDMUtils;
import java.util.HashMap;
import java.util.Map;
import keysuite.desktop.exceptions.Code;
import keysuite.desktop.exceptions.CodeException;
import keysuite.desktop.exceptions.KSRuntimeException;
import keysuite.docer.client.DocerBean;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    static Map<String,Logger> logs = new HashMap<>();

    static Logger getLogger(String serviceName, String methodName){
        String category = "docer.rest."+serviceName+"."+methodName;

        Logger LOG = logs.get(category);
        if (LOG==null){
            LOG = LoggerFactory.getLogger(category);
            logs.put(category,LOG);
        }
        return LOG;
    }

    protected final static Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Autowired
    Environment env;

    public LoggingAspect(){
    }

    @Around("@within(org.springframework.web.bind.annotation.RestController)")
    public Object logBeforeContactServiceMethoodsInvocation(ProceedingJoinPoint joinPoint) throws Throwable{

        long t0 = System.currentTimeMillis();

        String baseUrl = KDMUtils.getBaseURL(Session.getRequest());

        String ctx = env.getProperty("server.servlet.context-path");

        DocerBean.baseUrl.set(baseUrl+ctx);

        if(Session.attach() == null){
            throw new KSRuntimeException(Code.H401);
        }

        String retryPolicy = Session.getRequest().getHeader("retryPolicy");
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
//                logOut(elapsed, operationId,joinPoint);
                return res;
            } catch (Exception e) {

                if (e instanceof CodeException && ((CodeException) e).getCode().isRetryable() && executions<delays.length){

                        long delay = delays[executions];
                        Thread.sleep(delay);
                        logger.warn("retry #"+(executions+1)+" after "+delay+"ms");
                        continue;
                }
                throw e;
            }
        }
    }

    @AfterThrowing(pointcut = "@within(org.springframework.web.bind.annotation.RestController)", throwing = "ex")
    public void logError(Exception ex) {
        logger.error("unhandled exception",ex);
    }
}
