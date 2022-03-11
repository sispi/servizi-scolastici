package keysuite.docer.controllers;

import io.jsonwebtoken.Claims;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import it.kdm.orchestratore.session.Session;
import keysuite.cache.ClientCache;
import keysuite.desktop.exceptions.KSExceptionBadRequest;
import keysuite.desktop.exceptions.KSExceptionForbidden;
import keysuite.desktop.exceptions.KSExceptionNotFound;
import keysuite.docer.client.ClientUtils;
import keysuite.docer.interceptors.Logging;
import keysuite.docer.query.ISearchResponse;
import keysuite.docer.server.DocumentiService;
import keysuite.docer.server.SystemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@Logging(group = "AuthenticationService")
@CrossOrigin(origins = "*")
@RequestMapping("/system")
public class SystemController extends BaseController {

    final Logger logger = LoggerFactory.getLogger(SystemController.class);

    @Autowired
    DocumentiService documentiService;

    @Autowired
    SystemService systemService;

    @ApiResponses({
            @ApiResponse( code = 200 , message = "OK")
    })
    @GetMapping("/ping")
    public void ping() {
        return ;
    }

    @ApiResponses({
            @ApiResponse( code = 200 , message = "OK"),
            @ApiResponse( code = 403 , message = "Forbidden")
    })
    @PostMapping(value="/deleteByQuery")
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    public void deleteByQuery(@RequestParam String query) throws KSExceptionForbidden {
        documentiService.deleteByQuery(query);
    }

    @ApiResponses({
            @ApiResponse( code = 200 , message = "OK")
    })
    @PostMapping(value="/commit")
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    public void commit()  throws KSExceptionForbidden {
        documentiService.commit();
    }

    @ApiResponses({
            @ApiResponse( code = 200 , message = "OK")
    })
    @PostMapping(value="/caches/clear")
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    public Integer clearcaches()  throws KSExceptionForbidden {
        ClientCache.getInstance().init();
        return ClientCache.getInstance().getCount();
    }

    @ApiResponses({
            @ApiResponse( code = 200 , message = "OK")
    })
    @PostMapping(value="/caches/refresh")
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    public Integer updatecaches()  throws KSExceptionForbidden {
        return ClientCache.getInstance().refresh();
    }

    @ApiResponses({
            @ApiResponse( code = 200 , message = "OK")
    })
    @PostMapping(value="/caches/count")
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    public Integer countcaches()  throws KSExceptionForbidden {
        return ClientCache.getInstance().getCount();
    }


    @Logging(section = "login")
    @ApiResponses({
            @ApiResponse( code = 200 , message = "OK")
    })
    @RequestMapping(value="/login", method = {RequestMethod.GET,RequestMethod.POST})
    public String login(String codAoo, String username, String password, HttpServletRequest request, HttpServletResponse response) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {

        String token = documentiService.loginJWT(codAoo,username,password);
        if ("true".equals(request.getParameter("setCookie"))) {
            Cookie cookie = new Cookie(Session.keySession,token);
            cookie.setPath("/");
            Claims claims = ClientUtils.parseJWTTokenWithoutSecret(token);
            Long seconds = claims.getExpiration().getTime() - System.currentTimeMillis();
            if (seconds<Integer.MAX_VALUE)
                cookie.setMaxAge(seconds.intValue());
            response.addCookie(cookie);
            response.addHeader("Bearer-Subject",claims.getSubject());
            response.addHeader("Bearer-Audience",claims.getAudience());
            response.addDateHeader("Bearer-Exp",claims.getExpiration().getTime());
        }
        return token;
    }

    @Logging(section = "impersonate")
    @ApiResponses({
            @ApiResponse( code = 200 , message = "OK")
    })
    @RequestMapping(value="/impersonate", method = {RequestMethod.GET,RequestMethod.POST})
    public String impersonate(String codAoo, String username) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {
        return systemService.impersonate(codAoo,username);
    }

    @Logging(section = "register")
    @ApiResponses({
            @ApiResponse( code = 200 , message = "OK")
    })
    @RequestMapping(value="/register", method = {RequestMethod.GET,RequestMethod.POST})
    public void register(String codAoo, String username, String password) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {
        systemService.register(codAoo,username,password);
    }

    @GetMapping("/solr/{qt}")
    public ISearchResponse adminsolr(@PathVariable String qt, HttpServletRequest request) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {
        return systemService.adminsolr(qt,request);
    }

    @GetMapping( value = "init", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map init(
            @RequestParam(defaultValue = "true") boolean includeInfo,
            @RequestParam(defaultValue = "false") boolean forceUpdate) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {
        return systemService.importCsv_byfilename("init.csv",false,includeInfo,forceUpdate,true);
    }

    @GetMapping( value = "importCsv", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map importCsv_byfilename(
            @RequestParam(required = true) String filename,
            @RequestParam(defaultValue = "true") boolean continueOnError,
            @RequestParam(defaultValue = "false") boolean includeInfo,
            @RequestParam(defaultValue = "false") boolean forceUpdate,
            @RequestParam(defaultValue = "false") boolean init
            ) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {

        return systemService.importCsv_byfilename(filename,continueOnError,includeInfo,forceUpdate,init);
    }

    @PostMapping( value = "importCsv", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map importCsv_bycontent(   @RequestBody String csv,
                            @RequestParam(defaultValue = "true") boolean continueOnError,
                            @RequestParam(defaultValue = "false") boolean includeInfo,
                            @RequestParam(defaultValue = "false") boolean forceUpdate,
                            @RequestParam(defaultValue = "false") boolean init
                            ) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {

        return systemService.importCsv_bycontent(csv,continueOnError,includeInfo,forceUpdate,init);
    }

    @GetMapping( value = "reload", produces = MediaType.TEXT_PLAIN_VALUE)
    public String applySystemProperties(HttpServletResponse response) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {
        String newcontent = systemService.applySystemPropertiesToConf();
        if (newcontent!=null) {
            return newcontent;
        } else {
            return "NOT MODIFIED";
        }
    }

}
