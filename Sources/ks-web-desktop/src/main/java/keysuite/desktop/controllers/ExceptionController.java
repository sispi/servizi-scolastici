package keysuite.desktop.controllers;

import it.kdm.orchestratore.utils.ResourceUtils;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import keysuite.desktop.exceptions.CodeException;
import keysuite.desktop.exceptions.KSRuntimeException;
import org.apache.logging.log4j.util.Strings;
import org.keycloak.adapters.spi.AuthenticationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.NestedServletException;

@Controller
public class ExceptionController {

    @Autowired
    protected HttpServletRequest request;

    /*@PostMapping(value = "/error", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public Object errorPostForm(Model model)  {
        return handleError(null,request,model,null);
    }*/

    /*@PostMapping(value = "/error", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Object errorPostMultipart(Model model)  {
        return handleError(null,request,model,null);
    }*/

    @RequestMapping(value = "/error", produces = {"application/json","text/html"})
    public Object errorGet(Model model, HttpServletResponse response)  {

        //response.addHeader("error-page","true");

        String ERROR_REQUEST_URI = (String) request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);
        Integer ERROR_STATUS_CODE = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Throwable ERROR_EXCEPTION = (Throwable) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);

        if (ERROR_EXCEPTION == null){
            Object obj = request.getAttribute(AuthenticationError.class.getName());
            if (obj instanceof Throwable)
                ERROR_EXCEPTION = (Throwable) obj;
        }

        if (ERROR_EXCEPTION instanceof NestedServletException)
            ERROR_EXCEPTION = ERROR_EXCEPTION.getCause();

        if (ERROR_EXCEPTION==null){
            if (ERROR_STATUS_CODE!=null)
                ERROR_EXCEPTION = new KSRuntimeException( ERROR_STATUS_CODE );
            else
                ERROR_EXCEPTION = new KSRuntimeException( 500 );
        }

        CodeException ce = CodeException.getCodeException(ERROR_EXCEPTION); //.setUrl(URI);
        if (ce.getUrl()==null){
            if (ERROR_REQUEST_URI!=null)
                ce.setUrl(ERROR_REQUEST_URI);
            else
                ce.setUrl(request.getRequestURL().toString());
        }

        int status = ce.getCode().getHttpStatus();

        response.setStatus(status);

        if ( (request.getHeader("accept")!=null && request.getHeader("accept").contains("application/json"))
                        || Strings.isNotEmpty(request.getHeader("X-fragment"))
                        || response.getContentType()!=null && response.getContentType().contains("application/json")
        ) { //ajax mode!
            response.setContentType("application/json");
            model.addAttribute("body",ce);
            return "";
        } else if ("true".equals(request.getAttribute("isResource"))) {
            response.reset();
            response.setStatus(status);
            model.addAttribute("body",ce);
            response.setContentType("application/json");
            return "";
            //return ResponseEntity.status(status);
        } else {

            response.setContentType("text/html");
            model.addAttribute("exception",ce);
            model.addAttribute("context", request.getAttribute("context") );

            if (ce.getUrl()!=null)
                model.addAttribute("requestURL",ce.getUrl());

            String se = "error-"+status;

            if (ResourceUtils.getResourceNoExc("templates/error/"+se+".html")!=null || ResourceUtils.getResourceNoExc("templates/error/"+se+".ftl")!=null)
                return "error/"+se;
            return "error/error";
        }
    }

    /*@PostMapping(value = "/errorModal")
    public Object errorPost(Model model,HttpServletResponse response, @RequestBody KSException exception)  {
        model.addAttribute("exception",exception);
        return "error";
    }*/

    @ExceptionHandler(Exception.class)
    public Object handleError(Model model, Throwable exception) {
        model.addAttribute("exception",exception);
        return "error";
    }
}
