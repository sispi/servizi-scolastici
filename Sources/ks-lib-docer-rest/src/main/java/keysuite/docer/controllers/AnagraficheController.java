package keysuite.docer.controllers;

import io.swagger.annotations.*;
import it.kdm.orchestratore.session.Session;
import keysuite.desktop.exceptions.KSExceptionBadRequest;
import keysuite.desktop.exceptions.KSExceptionForbidden;
import keysuite.desktop.exceptions.KSExceptionNotFound;
import keysuite.docer.client.Anagrafica;
import keysuite.docer.client.HistoryItem;
import keysuite.docer.client.SearchParams;
import keysuite.docer.interceptors.Logging;
import keysuite.docer.query.ISearchResponse;
import keysuite.docer.server.AnagraficheService;
import keysuite.docer.server.BaseService;
import keysuite.docer.server.ServerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Logging(group = "DocerServices")
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("anagrafiche")
public class AnagraficheController extends BaseController {

    @Autowired
    AnagraficheService anagraficheService;

    @ApiResponses({
            @ApiResponse( code = 200 , message = "OK"),
            @ApiResponse( code = 400 , message = "BadRequest")
    })
    @GetMapping(value="",produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization",paramType = "header"),
            @ApiImplicitParam(name = BaseService.SEARCH_PARAMS, paramType = "query", example = "fq=name:test*")

    })
    public ISearchResponse search() throws KSExceptionBadRequest {
        SearchParams params =ServerUtils.parseQueryString(Session.getRequest().getQueryString());
        return anagraficheService.search(params) ;
    }

    @ApiResponses({
            @ApiResponse( code = 200 , message = "OK"),
            @ApiResponse( code = 400 , message = "BadRequest")
    })
    @GetMapping(value="{type}",produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization",paramType = "header"),
            @ApiImplicitParam(name = BaseService.SEARCH_PARAMS, paramType = "query", example = "fq=name:test*")

    })
    public ISearchResponse search(@PathVariable String type) throws KSExceptionBadRequest {
        SearchParams params =ServerUtils.parseQueryString(Session.getRequest().getQueryString());
        params.addFq("type",type);
        return anagraficheService.search(params) ;
    }

    @Logging(section = "createAnagraficaCustom")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses({
            @ApiResponse( code = 201 , message = "Created",
                    responseHeaders = @ResponseHeader(name = "Location", response = URI.class)),
            @ApiResponse( code = 400 , message = "BadRequest"),
            @ApiResponse( code = 403 , message = "Forbidden")
    })
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    @PostMapping(value="{type}",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Anagrafica create(@PathVariable String type,@RequestBody Anagrafica anagrafica) throws KSExceptionForbidden, KSExceptionBadRequest, KSExceptionNotFound {
        anagrafica.setTypeId(type);
        checkIdemHeader(anagrafica);
        if ("true".equals(Session.getRequest().getAttribute("isRetry")))
            checkIdempotency(anagrafica);
        return anagraficheService.create(anagrafica);
    }

    private void checkIdempotency(Anagrafica anagrafica) throws KSExceptionBadRequest{
        if (anagrafica.getGuid()==null && anagrafica.getCodice()==null)
            throw new KSExceptionBadRequest("per creazione con idempotenza deve essere impostato il guid");
    }

    @Logging(section = "createAnagraficaCustom")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses({
            @ApiResponse( code = 201 , message = "Created",
                    responseHeaders = @ResponseHeader(name = "Location", response = URI.class)),
            @ApiResponse( code = 400 , message = "BadRequest"),
            @ApiResponse( code = 403 , message = "Forbidden")
    })
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    @PutMapping(value="{type}",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Anagrafica createOrReplace(@PathVariable String type,@RequestBody Anagrafica anagrafica) throws KSExceptionForbidden, KSExceptionBadRequest, KSExceptionNotFound {
        anagrafica.setTypeId(type);
        checkIdemHeader(anagrafica);
        checkIdempotency(anagrafica);
        return anagraficheService.create(anagrafica);
    }

    @ApiResponses({
            @ApiResponse( code = 200 , message = "OK"),
            @ApiResponse( code = 404 , message = "NotFound")
    })
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    @GetMapping(value="{type}/{codice}",produces = MediaType.APPLICATION_JSON_VALUE)
    public Anagrafica get(@PathVariable String type, @PathVariable String codice) throws KSExceptionNotFound {
        return anagraficheService.get(codice,type);
    }

    @Logging(section = "createAnagraficaCustom")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses({
            @ApiResponse( code = 201 , message = "Created"),
            @ApiResponse( code = 400 , message = "BadRequest"),
            @ApiResponse( code = 403 , message = "Forbidden")
    })
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    @PutMapping(value="{type}/{codice}",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Anagrafica createOrReplace(@PathVariable String type, @PathVariable String codice, @RequestBody Anagrafica anagrafica) throws KSExceptionForbidden, KSExceptionBadRequest, KSExceptionNotFound {
        anagrafica.setTypeId(type);
        anagrafica.setCodice(codice);
        return anagraficheService.create(anagrafica);
    }

    @Logging(section = "updateAnagraficaCustom")
    @ApiResponses({
            @ApiResponse( code = 200 , message = "OK"),
            @ApiResponse( code = 404 , message = "NotFound"),
            @ApiResponse( code = 400 , message = "BadRequest"),
            @ApiResponse( code = 403 , message = "Forbidden")
    })
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    @PatchMapping( value="{type}/{codice}", consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public Anagrafica update(@PathVariable String type,@PathVariable String codice, @RequestBody Anagrafica anagrafica) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {
        anagrafica.setTypeId(type);
        anagrafica.setCodice(codice);
        return anagraficheService.update(anagrafica);
    }

    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @ApiResponses({
            @ApiResponse( code = 204 , message = "NoContent"),
            @ApiResponse( code = 404 , message = "NotFound"),
            @ApiResponse( code = 400 , message = "BadRequest"),
            @ApiResponse( code = 403 , message = "Forbidden")
    })
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    @DeleteMapping( value= "{type}/{codice}")
    public void delete(@PathVariable String type,@PathVariable String codice) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {
        anagraficheService.delete(type,codice);
    }

    @GetMapping("{type}/{codice}/history")
    @ApiResponses({
            @ApiResponse( code = 200 , message = "OK")
    })
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    public HistoryItem[] getHistory(@PathVariable String type,@PathVariable String codice) throws KSExceptionNotFound {
        return anagraficheService.getHistory(type,codice);
    }

}
