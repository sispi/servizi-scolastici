package keysuite.desktop.controllers;

import com.google.common.base.Strings;

import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import keysuite.desktop.DesktopUtils;
import keysuite.desktop.components.UrlRewriter;
import keysuite.desktop.exceptions.KSException;
import keysuite.desktop.exceptions.KSRuntimeException;
import keysuite.desktop.security.ConfigAppBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Controller
//@PreAuthorize("hasRole('admins')")
public class MainController {

    @Autowired
    UrlRewriter urlRewriter;

    @Autowired
    protected HttpServletRequest request;

    @RequestMapping(value = "**",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String  view(Model model, HttpServletRequest request) throws KSException {
        return view(model,request,null);
    }

    @RequestMapping("**")
    public String view(Model model, HttpServletRequest request, @RequestBody(required = false) Object body) throws KSException {

        String rewritten = (String) request.getAttribute("rewritten-url" );

        if (rewritten!=null){
            request.setAttribute("rewritten-url",null );
            return "forward:"+rewritten;
        }

        String requestURL = DesktopUtils.getRequestURL(request);

        model.addAttribute("requestURL",requestURL);

        Map app = ConfigAppBean.getBeanForCurrentRequest();

        /*String context = "";
        if (app.containsKey("pattern")) {
            Pattern pattern = (Pattern) app.get("pattern");
            Matcher matcher = pattern.matcher(request.getRequestURL().toString());
            if (matcher.find() && matcher.groupCount()>0){
                context = matcher.group(1);
                if (!context.startsWith("/"))
                    context = "/" + context;
            }
        }*/

        String context = (String) app.getOrDefault("context" , "");
        String path = request.getServletPath().substring(context.length());

        model.addAttribute("context", context );
        request.setAttribute("context", context );
        model.addAttribute("query", DesktopUtils.getParameters(request) );

        if (request instanceof MultipartHttpServletRequest){
            MultipartHttpServletRequest mreq = (MultipartHttpServletRequest) request;
            model.addAttribute("files",mreq.getFileMap());
        }

        if (body!=null){
            model.addAttribute("requestBody",body);
        }

        if (path.endsWith("/"))
            path = path.substring(0,path.length()-1);

        /*if ( Boolean.TRUE.equals(request.getAttribute("has-tilde"))) {
            //col tilde cerco sempre nel root delle risorse ignorando il folder dell'app
            path = path.substring(2);
            model.addAttribute("path", path );
            model.addAttribute("view", path );
            return path;
        } else */
        if (path.length()==0){
            //cerco l'homepage dell'app e seguo se ha qualcosa dopo il context
            String link = (String) app.get("link");
            if (link!=null) {
                String p = link.substring(link.indexOf("/","https://x".length()));
                if (p.endsWith("/"))
                    p = p.substring(0,p.length()-1);
                if(!p.equals(context))
                    return "redirect:" + link;
            }
        } else {
            path = path.substring(1);
        }

        String view = app.get("folder") + path;

        model.addAttribute("path", path );
        model.addAttribute("view", view );

        return view;
    }

    @ExceptionHandler(Exception.class)
    public Object handleError(Model model, Throwable exception) {
        model.addAttribute("exception",exception);
        return "error";
    }
}
