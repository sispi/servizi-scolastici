package keysuite.docer.controllers;

import it.kdm.orchestratore.session.Session;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import keysuite.desktop.exceptions.CodeException;
import keysuite.desktop.exceptions.KSException;
import keysuite.desktop.exceptions.KSRuntimeException;
import static keysuite.docer.client.ClientUtils.getKSException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class BaseController {

    @Autowired
    Environment env;

    @ExceptionHandler(Exception.class)
    public Object handleError(Exception exception , HttpServletResponse response) {
        exception = (Exception) getKSException(exception);

        String msg = env.getProperty("error."+((CodeException)exception).getCode().name());

        if (msg!=null){
            if (exception instanceof KSRuntimeException){
                ((KSRuntimeException)exception).setMessage(msg);
            }
            if (exception instanceof KSException){
                ((KSException)exception).setMessage(msg);
            }
        }

        response.setStatus(((CodeException)exception).getCode().getHttpStatus());
        return exception;
    }

    public static void addExtraData(String extraData){
        HttpServletRequest req = Session.getRequest();
        if (req==null)
            return;
        String old = (String) req.getAttribute("extraData");

        if (old!=null)
            extraData = old+";"+extraData;

        req.setAttribute("extraData",extraData);
    }

}
