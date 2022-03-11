package keysuite.docer.controllers;

import io.swagger.annotations.*;
import it.kdm.orchestratore.session.Session;
import keysuite.desktop.exceptions.KSExceptionBadRequest;
import keysuite.desktop.exceptions.KSExceptionForbidden;
import keysuite.desktop.exceptions.KSExceptionNotFound;
import keysuite.docer.client.Folder;
import keysuite.docer.client.HistoryItem;
import keysuite.docer.interceptors.Logging;
import keysuite.docer.interceptors.NotRetryable;
import keysuite.docer.query.ISearchResponse;
import keysuite.docer.server.BaseService;
import keysuite.docer.server.CartelleService;
import keysuite.docer.server.ServerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@Logging(group = "DocerServices")
@CrossOrigin(origins = "*")
@RequestMapping("cartelle")
public class CartelleController extends BaseController {

    @Autowired
    CartelleService cartelleService;

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
        return cartelleService.search( ServerUtils.parseQueryString(Session.getRequest().getQueryString()) ) ;
    }

    @NotRetryable
    @Logging(section = "createFolder")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses({
            @ApiResponse( code = 201 , message = "Created",
                    responseHeaders = @ResponseHeader(name = "Location", response = URI.class)),
            @ApiResponse( code = 400 , message = "BadRequest"),
            @ApiResponse( code = 403 , message = "Forbidden")
    })
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public Folder create(@RequestBody Folder cartella) throws KSExceptionForbidden, KSExceptionBadRequest, KSExceptionNotFound {

        checkIdemHeader(cartella);
        if ("true".equals(Session.getRequest().getAttribute("isRetry")))
            checkIdempotency(cartella);

        return cartelleService.create(cartella);
    }

    private void checkIdempotency(Folder folder) throws KSExceptionBadRequest{
        if (folder.getGuid()==null)
            throw new KSExceptionBadRequest("per creazione con idempotenza deve essere impostato il guid");
    }

    @Logging(section = "createFolder")
    @ApiResponses({
            @ApiResponse( code = 200 , message = "OK"),
            @ApiResponse( code = 201 , message = "Created",
                    responseHeaders = @ResponseHeader(name = "Location", response = URI.class)),
            @ApiResponse( code = 400 , message = "BadRequest"),
            @ApiResponse( code = 403 , message = "Forbidden")
    })
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public Folder createOrReplace(@RequestBody Folder cartella) throws KSExceptionForbidden, KSExceptionBadRequest, KSExceptionNotFound {
        checkIdemHeader(cartella);
        checkIdempotency(cartella);
        return cartelleService.create(cartella);
    }

    @ApiResponses({
            @ApiResponse( code = 200 , message = "OK"),
            @ApiResponse( code = 404 , message = "NotFound")
    })
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    @GetMapping(value="{folderId}",produces = MediaType.APPLICATION_JSON_VALUE)
    public Folder get(@PathVariable String folderId) throws KSExceptionNotFound {
        return cartelleService.get(folderId);
    }

    @Logging(section = "updateFolder")
    @ApiResponses({
            @ApiResponse( code = 200 , message = "OK"),
            @ApiResponse( code = 404 , message = "NotFound"),
            @ApiResponse( code = 400 , message = "BadRequest"),
            @ApiResponse( code = 403 , message = "Forbidden")
    })
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    @PatchMapping( value="{folderId}",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public Folder update(@PathVariable String folderId,@RequestBody Folder cartella) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {
        cartella.setFolderId(folderId);
        return cartelleService.update(cartella);
    }

    public Folder update(Folder cartella) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {
        return update(cartella.getFolderId(),cartella);
    }

    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @ApiResponses({
            @ApiResponse( code = 204 , message = "NoContent"),
            @ApiResponse( code = 404 , message = "NotFound"),
            @ApiResponse( code = 400 , message = "BadRequest"),
            @ApiResponse( code = 403 , message = "Forbidden")
    })
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    @DeleteMapping( value= "{folderId}")
    public void delete(@PathVariable String folderId) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {
        cartelleService.delete(folderId);
    }

    @GetMapping("{folderId}/history")
    @ApiResponses({
            @ApiResponse( code = 200 , message = "OK")
    })
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    public HistoryItem[] getHistory(@PathVariable String folderId) throws KSExceptionNotFound {
        return cartelleService.getHistory(folderId);
    }

}
