package keysuite.docer.controllers;

import io.swagger.annotations.*;
import it.kdm.orchestratore.session.Session;
import keysuite.desktop.exceptions.KSExceptionBadRequest;
import keysuite.desktop.exceptions.KSExceptionForbidden;
import keysuite.desktop.exceptions.KSExceptionNotFound;
import keysuite.docer.client.Fascicolo;
import keysuite.docer.client.HistoryItem;
import keysuite.docer.interceptors.Logging;
import keysuite.docer.interceptors.NotRetryable;
import keysuite.docer.query.ISearchResponse;
import keysuite.docer.server.BaseService;
import keysuite.docer.server.FascicoliService;
import keysuite.docer.server.ServerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.net.URI;

@RestController
@Logging(group = "WSFascicolazione")
@CrossOrigin(origins = "*")
@RequestMapping("fascicoli")
public class FascicoliController extends BaseController {

    @Autowired
    FascicoliService fascicoliService;

    @ApiResponses({
            @ApiResponse( code = 200 , message = "OK"),
            @ApiResponse( code = 400 , message = "BadRequest")
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization",paramType = "header"),
            @ApiImplicitParam(name = BaseService.SEARCH_PARAMS, paramType = "query", example = "fq=name:test*")

    })
    public ISearchResponse search() throws KSExceptionBadRequest {
        return fascicoliService.search( ServerUtils.parseQueryString(Session.getRequest().getQueryString()) ) ;
    }

    @NotRetryable
    @Logging(section = "creaFascicolo")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses({
            @ApiResponse( code = 201 , message = "Created",
                    responseHeaders = @ResponseHeader(name = "Location", response = URI.class)),
            @ApiResponse( code = 400 , message = "BadRequest"),
            @ApiResponse( code = 403 , message = "Forbidden")
    })
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Fascicolo create(@RequestBody Fascicolo fascicolo) throws KSExceptionForbidden, KSExceptionBadRequest, KSExceptionNotFound {

        checkIdemHeader(fascicolo);

        if ("true".equals(Session.getRequest().getAttribute("isRetry")))
            checkIdempotency(fascicolo);

        return fascicoliService.create(fascicolo);
    }

    @Logging(section = "creaFascicolo")
    @ApiResponses({
            @ApiResponse( code = 200 , message = "OK"),
            @ApiResponse( code = 201 , message = "Created",
                    responseHeaders = @ResponseHeader(name = "Location", response = URI.class)),
            @ApiResponse( code = 400 , message = "BadRequest"),
            @ApiResponse( code = 403 , message = "Forbidden")
    })
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    @PutMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Fascicolo createOrReplace(@RequestBody Fascicolo fascicolo) throws KSExceptionForbidden, KSExceptionBadRequest, KSExceptionNotFound {
        checkIdemHeader(fascicolo);
        checkIdempotency(fascicolo);
        return fascicoliService.create(fascicolo);
    }

    private void checkIdempotency(Fascicolo fascicolo) throws KSExceptionBadRequest{
        if (fascicolo.getGuid()==null && fascicolo.getDocerId()==null)
            throw new KSExceptionBadRequest("per creazione con idempotenza deve essere impostato il guid");
    }

    @ApiIgnore
    @GetMapping(value = "**",produces = MediaType.APPLICATION_JSON_VALUE)
    public Fascicolo getDummy() throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {
        return get(Session.getRequest().getServletPath().substring("/fascicoli/".length()));
    }

    @ApiResponses({
            @ApiResponse( code = 200 , message = "OK"),
            @ApiResponse( code = 404 , message = "NotFound")
    })
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    @GetMapping(value = "{fascicoloId}",produces = MediaType.APPLICATION_JSON_VALUE)
    public Fascicolo get(@PathVariable String fascicoloId) throws KSExceptionNotFound {
        return fascicoliService.get(fascicoloId);
    }

    @Logging(section = "creaFascicolo")
    @ApiResponses({
            @ApiResponse( code = 200 , message = "OK"),
            @ApiResponse( code = 201 , message = "Created",
                    responseHeaders = @ResponseHeader(name = "Location", response = URI.class)),
            @ApiResponse( code = 400 , message = "BadRequest"),
            @ApiResponse( code = 403 , message = "Forbidden")
    })
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    @PutMapping(value = "{fascicoloId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Fascicolo createOrReplace(@PathVariable String fascicoloId, @RequestBody Fascicolo fascicolo) throws KSExceptionForbidden, KSExceptionBadRequest, KSExceptionNotFound {
        fascicolo.setDocerid(fascicoloId);
        return fascicoliService.create(fascicolo);
    }

    @Logging(section = "updateFascicolo")
    @ApiIgnore
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    @PatchMapping( value="**" , consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Fascicolo updateDummy(@RequestBody Fascicolo fascicolo) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {
        return update(Session.getRequest().getServletPath().substring("/fascicoli/".length()),fascicolo);
    }

    @Logging(section = "updateFascicolo")
    @ApiResponses({
            @ApiResponse( code = 200 , message = "OK"),
            @ApiResponse( code = 404 , message = "NotFound"),
            @ApiResponse( code = 400 , message = "BadRequest"),
            @ApiResponse( code = 403 , message = "Forbidden")
    })
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    @PatchMapping( value = "{fascicoloId}" , consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Fascicolo update(@PathVariable String fascicoloId, @RequestBody Fascicolo fascicolo) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {
        fascicolo.setDocerid(fascicoloId);
        return fascicoliService.update(fascicolo);
    }

    @ApiIgnore
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    @DeleteMapping( value= "**")
    public void deleteDummy() throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {
        delete(Session.getRequest().getServletPath().substring("/fascicoli/".length()));
    }

    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @ApiResponses({
            @ApiResponse( code = 202 , message = "NoContent"),
            @ApiResponse( code = 404 , message = "NotFound"),
            @ApiResponse( code = 400 , message = "BadRequest"),
            @ApiResponse( code = 403 , message = "Forbidden")
    })
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    @DeleteMapping( value= "{fascicoloId}")
    public void delete(@PathVariable String fascicoloId) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {
        fascicoliService.delete(fascicoloId);
    }

    @ApiIgnore
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    @GetMapping( value= "**/history")
    public HistoryItem[] getHistoryDummy() throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {
        return getHistory(Session.getRequest().getServletPath().substring("/fascicoli/".length()));
    }

    @GetMapping("{fascicoloId}/history")
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    @ApiResponses({
            @ApiResponse( code = 200 , message = "OK")
    })
    public HistoryItem[] getHistory(@PathVariable String fascicoloId) throws KSExceptionNotFound {
        return fascicoliService.getHistory(fascicoloId);
    }
}
