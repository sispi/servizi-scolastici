package keysuite.docer.controllers;

import it.kdm.orchestratore.session.Session;
import keysuite.desktop.exceptions.CodeException;
import keysuite.docer.client.DocerBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;

import static keysuite.docer.client.ClientUtils.getKSException;

public class BaseController {

    @Autowired
    Environment env;

    @ExceptionHandler(Exception.class)
    public Object handleError(Exception exception , HttpServletResponse response) {
        CodeException ce = (CodeException) getKSException(exception);

        /*String msg = env.getProperty("error."+ce.getCode().name());

        if (msg!=null){
            if (ce instanceof KSRuntimeException){
                ((KSRuntimeException)ce).setMessage(msg);
            }
            if (ce instanceof KSException){
                ((KSException)ce).setMessage(msg);
            }
        }*/

        response.setStatus(ce.getCode().getHttpStatus());
        return ResponseEntity.status(ce.getCode().getHttpStatus()).body(ce);
    }

    protected void checkIdemHeader(DocerBean... beans){
        String operationId = Session.getRequest().getHeader("operationid");
        if (beans!=null && operationId!=null){
            for( int i=0; i<beans.length; i++ ){
                DocerBean bean = beans[i];
                if (bean.getGuid()==null){
                    String GUID = operationId;
                    if (i>0){
                        GUID = GUID + "-" +i;
                    }
                    bean.setGuid(GUID);
                }
            }
        }
    }

}
