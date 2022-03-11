package keysuite.docer.controllers;

import io.swagger.annotations.*;
import it.kdm.orchestratore.session.Session;
import keysuite.desktop.exceptions.KSExceptionBadRequest;
import keysuite.desktop.exceptions.KSExceptionForbidden;
import keysuite.desktop.exceptions.KSExceptionNotFound;
import keysuite.docer.client.HistoryItem;
import keysuite.docer.client.User;
import keysuite.docer.interceptors.Logging;
import keysuite.docer.query.ISearchResponse;
import keysuite.docer.server.BaseService;
import keysuite.docer.server.ServerUtils;
import keysuite.docer.server.UtentiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

@RestController
@Logging(group = "DocerServices")
@CrossOrigin(origins = "*")
@RequestMapping("utenti")
public class UtentiController extends BaseController {

    @Autowired
    UtentiService utentiService;

    @ApiResponses({
            @ApiResponse( code = 200 , message = "OK"),
            @ApiResponse( code = 400 , message = "BadRequest")
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = BaseService.SEARCH_PARAMS, paramType = "query", example = "fq=name:test*"),
            @ApiImplicitParam(name = "Authorization",paramType = "header")
    })
    public ISearchResponse search() throws KSExceptionBadRequest {
        return utentiService.search( ServerUtils.parseQueryString(Session.getRequest().getQueryString()) ) ;
    }

    private void checkIdempotency(User utente) throws KSExceptionBadRequest{
        if (utente.getGuid()==null && utente.getUserName()==null)
            throw new KSExceptionBadRequest("per creazione con idempotenza deve essere impostato il guid");
    }

    @Logging(section = "createUser")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses({
            @ApiResponse( code = 201 , message = "Created",
                    responseHeaders = @ResponseHeader(name = "Location", response = URI.class)),
            @ApiResponse( code = 400 , message = "BadRequest"),
            @ApiResponse( code = 403 , message = "Forbidden")
    })
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public User create(@RequestBody User utente) throws KSExceptionBadRequest, KSExceptionForbidden, KSExceptionNotFound {
        checkIdemHeader(utente);
        if ("true".equals(Session.getRequest().getAttribute("isRetry")))
            checkIdempotency(utente);
        return utentiService.create(utente);
    }

    @ApiResponses({
            @ApiResponse( code = 200 , message = "OK"),
            @ApiResponse( code = 404 , message = "NotFound")
    })
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    @GetMapping(value="{username}",produces = MediaType.APPLICATION_JSON_VALUE)
    public User get(@PathVariable String username) throws KSExceptionNotFound {
        return utentiService.get(username);
    }

    @Logging(section = "createUser")
    @ApiResponses({
            @ApiResponse( code = 200 , message = "OK"),
            @ApiResponse( code = 201 , message = "Created",
                    responseHeaders = @ResponseHeader(name = "Location", response = URI.class)),
            @ApiResponse( code = 400 , message = "BadRequest"),
            @ApiResponse( code = 403 , message = "Forbidden")
    })
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    @PutMapping(value="{username}",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public User createOrReplace(@PathVariable String username, @RequestBody User utente) throws KSExceptionBadRequest, KSExceptionForbidden, KSExceptionNotFound {
        utente.setUserName(username);
        checkIdemHeader(utente);
        checkIdempotency(utente);
        return utentiService.create(utente);
    }

    @Logging(section = "updateUser")
    @ApiResponses({
            @ApiResponse( code = 200 , message = "OK"),
            @ApiResponse( code = 201 , message = "Created"),
            @ApiResponse( code = 400 , message = "BadRequest"),
            @ApiResponse( code = 403 , message = "Forbidden")
    })
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    @PatchMapping(value="{username}",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public User update(@PathVariable String username, @RequestBody User utente) throws KSExceptionBadRequest, KSExceptionForbidden, KSExceptionNotFound {
        utente.setUserName(username);
        return utentiService.update(utente);
    }

    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @ApiResponses({
            @ApiResponse( code = 204 , message = "NoContent"),
            @ApiResponse( code = 404 , message = "NotFound"),
            @ApiResponse( code = 400 , message = "BadRequest"),
            @ApiResponse( code = 403 , message = "Forbidden")
    })
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    @DeleteMapping( value= {"{username}" })
    public void delete(@PathVariable String username) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {
        utentiService.delete(username);
    }

    @GetMapping("{username}/history")
    @ApiResponses({
            @ApiResponse( code = 200 , message = "OK")
    })
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    public HistoryItem[] getHistory(@PathVariable String username) throws KSExceptionNotFound {
        return utentiService.getHistory(username);
    }

    @GetMapping("{username}/options")
    @ApiResponses({
            @ApiResponse( code = 200 , message = "OK")
    })
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    public Map<String,Object> options(@PathVariable String username) throws KSExceptionNotFound {
        return utentiService.getOptions(username);
    }

    @Logging(section = "updateUser")
    @ApiResponses({
            @ApiResponse( code = 200 , message = "OK"),
            @ApiResponse( code = 201 , message = "Created"),
            @ApiResponse( code = 400 , message = "BadRequest"),
            @ApiResponse( code = 403 , message = "Forbidden")
    })
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    @PatchMapping(value="{username}/options",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public User setOptions(@PathVariable String username, @RequestBody Map<String,Object> options) throws KSExceptionBadRequest, KSExceptionForbidden, KSExceptionNotFound {
        return utentiService.setOptions(username,options);
    }



}
