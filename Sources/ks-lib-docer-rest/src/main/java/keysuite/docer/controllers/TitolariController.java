package keysuite.docer.controllers;

import io.swagger.annotations.*;
import it.kdm.orchestratore.session.Session;
import keysuite.desktop.exceptions.KSExceptionBadRequest;
import keysuite.desktop.exceptions.KSExceptionForbidden;
import keysuite.desktop.exceptions.KSExceptionNotFound;
import keysuite.docer.client.HistoryItem;
import keysuite.docer.client.Titolario;
import keysuite.docer.interceptors.Logging;
import keysuite.docer.query.ISearchResponse;
import keysuite.docer.server.BaseService;
import keysuite.docer.server.ServerUtils;
import keysuite.docer.server.TitolariService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static keysuite.docer.client.ClientUtils.getOnlyClass;
import static keysuite.docer.client.ClientUtils.getOnlyPiano;

@RestController
@Logging(group = "DocerServices")
@CrossOrigin(origins = "*")
@RequestMapping("titolari")
public class TitolariController extends BaseController {

    @Autowired
    TitolariService titolariService;

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
        return titolariService.search( ServerUtils.parseQueryString(Session.getRequest().getQueryString()) ) ;
    }

    private void checkIdempotency(Titolario titolario) throws KSExceptionBadRequest{
        if (titolario.getGuid()==null && titolario.getClassifica()==null)
            throw new KSExceptionBadRequest("per creazione con idempotenza deve essere impostato il guid");
    }

    @Logging(section = "createTitolario")
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
    public Titolario create(@RequestBody Titolario titolario) throws KSExceptionForbidden, KSExceptionBadRequest, KSExceptionNotFound {
        checkIdemHeader(titolario);
        if ("true".equals(Session.getRequest().getAttribute("isRetry")))
            checkIdempotency(titolario);
        return titolariService.create(titolario);
    }

    @Logging(section = "createTitolario")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses({
            @ApiResponse( code = 201 , message = "Created",
                    responseHeaders = @ResponseHeader(name = "Location", response = URI.class)),
            @ApiResponse( code = 400 , message = "BadRequest"),
            @ApiResponse( code = 403 , message = "Forbidden")
    })
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    @PutMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Titolario createOrReplace(@RequestBody Titolario titolario) throws KSExceptionForbidden, KSExceptionBadRequest, KSExceptionNotFound {
        checkIdemHeader(titolario);
        checkIdempotency(titolario);
        return titolariService.create(titolario);
    }

    @Logging(section = "createTitolario")
    @ApiResponses({
            @ApiResponse( code = 200 , message = "OK"),
            @ApiResponse( code = 201 , message = "Created",
                    responseHeaders = @ResponseHeader(name = "Location", response = URI.class)),
            @ApiResponse( code = 400 , message = "BadRequest"),
            @ApiResponse( code = 403 , message = "Forbidden")
    })
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    @PutMapping(value = "{classifica}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Titolario createOrReplace(@PathVariable String classifica, @RequestBody Titolario titolario) throws KSExceptionForbidden, KSExceptionBadRequest, KSExceptionNotFound {
        titolario.setClassifica(getOnlyClass(classifica));
        titolario.setPianoClass(getOnlyPiano(classifica));
        return titolariService.create(titolario);
    }

    @ApiResponses({
            @ApiResponse( code = 200 , message = "OK"),
            @ApiResponse( code = 404 , message = "NotFound")
    })
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    @GetMapping(value = "{classifica}",produces = MediaType.APPLICATION_JSON_VALUE)
    public Titolario get(@PathVariable String classifica)  throws KSExceptionNotFound {
        return titolariService.get(getOnlyClass(classifica),getOnlyPiano(classifica));
    }

    @Logging(section = "updateTitolario")
    @ApiResponses({
            @ApiResponse( code = 200 , message = "OK"),
            @ApiResponse( code = 404 , message = "NotFound"),
            @ApiResponse( code = 400 , message = "BadRequest"),
            @ApiResponse( code = 403 , message = "Forbidden")
    })
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    @PatchMapping( value="{classifica}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Titolario update(@PathVariable String classifica,@RequestBody Titolario titolario) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {
        titolario.setClassifica(getOnlyClass(classifica));
        titolario.setPianoClass(getOnlyPiano(classifica));
        return titolariService.update(titolario);
    }

    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @ApiResponses({
            @ApiResponse( code = 202 , message = "NoContent"),
            @ApiResponse( code = 404 , message = "NotFound"),
            @ApiResponse( code = 400 , message = "BadRequest"),
            @ApiResponse( code = 403 , message = "Forbidden")
    })
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    @DeleteMapping( "{classifica}" )
    public void delete(@PathVariable String classifica) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {
        titolariService.delete(getOnlyClass(classifica),getOnlyPiano(classifica));
    }

    @GetMapping( "{classifica}/history" )
    @ApiResponses({
            @ApiResponse( code = 200 , message = "OK")
    })
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    public HistoryItem[] getHistory(@PathVariable String classifica) throws KSExceptionNotFound {
        return titolariService.getHistory(getOnlyClass(classifica),getOnlyPiano(classifica));
    }

    @GetMapping("piano")
    @ApiResponses({
            @ApiResponse( code = 200 , message = "OK")
    })
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    public String pianoClassificazione(@RequestParam(required = false) String anno) {
        return titolariService.pianoClassificazione(anno);
    }

}
