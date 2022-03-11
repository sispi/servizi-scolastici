package keysuite.docer.controllers;

import io.swagger.annotations.*;
import it.kdm.orchestratore.session.Session;
import keysuite.desktop.exceptions.KSExceptionBadRequest;
import keysuite.desktop.exceptions.KSExceptionForbidden;
import keysuite.desktop.exceptions.KSExceptionNotFound;
import keysuite.docer.client.Group;
import keysuite.docer.client.HistoryItem;
import keysuite.docer.interceptors.Logging;
import keysuite.docer.query.ISearchResponse;
import keysuite.docer.server.BaseService;
import keysuite.docer.server.GruppiService;
import keysuite.docer.server.ServerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@Logging(group = "DocerServices")
@CrossOrigin(origins = "*")
@RequestMapping("gruppi")
public class GruppiController extends BaseController {

    @Autowired
    GruppiService gruppiService;

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
        return gruppiService.search( ServerUtils.parseQueryString(Session.getRequest().getQueryString()) ) ;
    }

    private void checkIdempotency(Group gruppo) throws KSExceptionBadRequest{
        if (gruppo.getGuid()==null && gruppo.getGroupId()==null)
            throw new KSExceptionBadRequest("per creazione con idempotenza deve essere impostato il guid");
    }

    @Logging(section = "createGroup")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses({
            @ApiResponse( code = 201 , message = "Created",
                    responseHeaders = @ResponseHeader(name = "Location", response = URI.class)),
            @ApiResponse( code = 400 , message = "BadRequest"),
            @ApiResponse( code = 403 , message = "Forbidden")
    })
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public Group create(@RequestBody Group gruppo) throws KSExceptionBadRequest, KSExceptionForbidden, KSExceptionNotFound {
        checkIdemHeader(gruppo);
        if ("true".equals(Session.getRequest().getAttribute("isRetry")))
            checkIdempotency(gruppo);
        return gruppiService.create(gruppo);
    }

    @ApiResponses({
            @ApiResponse( code = 200 , message = "OK"),
            @ApiResponse( code = 404 , message = "NotFound")
    })
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    @GetMapping(value="{groupId}",produces = MediaType.APPLICATION_JSON_VALUE)
    public Group get(@PathVariable String groupId) throws KSExceptionNotFound {
        return gruppiService.get(groupId);
    }

    @Logging(section = "createGroup")
    @ApiResponses({
            @ApiResponse( code = 200 , message = "OK"),
            @ApiResponse( code = 201 , message = "Created",
                    responseHeaders = @ResponseHeader(name = "Location", response = URI.class)),
            @ApiResponse( code = 400 , message = "BadRequest"),
            @ApiResponse( code = 403 , message = "Forbidden")
    })
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    @PutMapping(value="{groupId}",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public Group createOrReplace(@PathVariable String groupId, @RequestBody Group gruppo) throws KSExceptionBadRequest, KSExceptionForbidden, KSExceptionNotFound {
        gruppo.setGroupId(groupId);
        checkIdemHeader(gruppo);
        checkIdempotency(gruppo);
        return gruppiService.create(gruppo);
    }

    @Logging(section = "updateGroup")
    @ApiResponses({
            @ApiResponse( code = 200 , message = "OK"),
            @ApiResponse( code = 404 , message = "NotFound"),
            @ApiResponse( code = 400 , message = "BadRequest"),
            @ApiResponse( code = 403 , message = "Forbidden")
    })
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    @PatchMapping( value="{groupId}", consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public Group update(@PathVariable String groupId, @RequestBody Group gruppo) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {
        gruppo.setGroupId(groupId);
        return gruppiService.update(gruppo);
    }

    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @ApiResponses({
            @ApiResponse( code = 204 , message = "NoContent"),
            @ApiResponse( code = 404 , message = "NotFound"),
            @ApiResponse( code = 400 , message = "BadRequest"),
            @ApiResponse( code = 403 , message = "Forbidden")
    })
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    @DeleteMapping( value= {"{groupId}" })
    public void delete(@PathVariable String groupId) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {
        gruppiService.delete(groupId);
    }

    @GetMapping("{groupId}/history")
    @ApiResponses({
            @ApiResponse( code = 200 , message = "OK")
    })
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    public HistoryItem[] getHistory(@PathVariable String groupId) throws KSExceptionNotFound {
        return gruppiService.getHistory(groupId);
    }



}
