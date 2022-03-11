package keysuite.desktop.controllers;

import it.kdm.orchestratore.session.Session;
import java.net.URI;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import keysuite.cache.ClientCacheAuthUtils;
import keysuite.desktop.DesktopUtils;
import keysuite.desktop.exceptions.KSExceptionBadRequest;
import keysuite.desktop.security.SecurityHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;

@Controller
@RequestMapping(value="/auth")
public class AuthenticationController {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    @Autowired
    protected HttpServletRequest request;

    @Autowired
    protected Environment environment;

    @RequestMapping(value = "/executeLogin", method = {RequestMethod.POST, RequestMethod.GET})
    public String executeLogin(HttpServletRequest request,	HttpServletResponse response,
                               @RequestParam(value = "username", defaultValue = "") String username,
                               @RequestParam(value = "password", required = false ) String password,
                               @RequestParam(value = "aoo", defaultValue = "") String aoo,
                               @RequestParam(value = "requestURL", defaultValue = "/") String requestURL,
                               @RequestParam(value = "switchAoo", defaultValue = "false") Boolean switchAoo
                                ) throws Exception {

        try {

            if (switchAoo && Session.getUserInfo().getAllAoos().contains(aoo)){
                username = Session.getUserInfo().getUsername();
                password = ClientCacheAuthUtils.generateMD5(username);
            }

            SecurityHelper.logout();

            if ("".equals(aoo))
                aoo = DesktopUtils.getAOOforRealm(request);

            String url = String.format("/docer/v1/system/login?codAoo=%s&username=%s&password=%s",
                    aoo,username,password);

            String jwtToken = DesktopUtils.GET(url,null,String.class);
            SecurityHelper.AuthenticateRequest(response, jwtToken);
            return "redirect:" + requestURL;
        } catch (Exception exc) {
            logger.error("error login username:{}",username,exc);
            return "redirect:" + "login?username="+username+"&aoo="+aoo+"&message="+DesktopUtils.encodeURIComponent(exc.getMessage())+"&requestURL="+DesktopUtils.encodeURIComponent(requestURL);
        }
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST )
    public String register(HttpServletRequest request,	HttpServletResponse response,
                                @RequestParam(value = "username", required = false) String username,
                                @RequestParam(value = "password", required = false) String password,
                                @RequestParam(value = "password2", required = false) String password2,
                                @RequestParam(value = "aoo", required = false) String aoo
    ) throws Exception {

        try {

            if (aoo==null || username==null || password==null || password2==null)
                throw new KSExceptionBadRequest("aoo,username e password obbligatori");

            if (!password.equals(password2))
                throw new KSExceptionBadRequest("le password non coincidono");

            String url = String.format("%s/system/register?codAoo=%s&username=%s&password=%s",
                    environment.getProperty("docer.url"),aoo,username,password);

            WebClient client = DesktopUtils.buildAdminWebClient(aoo);

            client.post().uri(URI.create(url)).retrieve().toBodilessEntity().block();

            return "redirect:" + "login?username="+username+"&aoo="+aoo;
        } catch (Exception exc) {
            logger.error("error register username:{}",username,exc);
            return "redirect:" + "register?username="+username+"&aoo="+aoo+"&message="+DesktopUtils.encodeURIComponent(exc.getMessage());
        }
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(@RequestParam(value = "requestURL", defaultValue = "/") String requestURL) throws Exception{
        SecurityHelper.logout();
        return "redirect:" + requestURL;
    }

    @RequestMapping(value = "/redirect", method = RequestMethod.GET)
    public String redirect(@RequestParam(value = "requestURL", defaultValue = "/") String requestURL) throws Exception{
        return "redirect:" + requestURL;
    }

    @ExceptionHandler(Exception.class)
    public Object handleError(Model model, Throwable exception) {
        model.addAttribute("exception",exception);
        return "error";
    }
}
