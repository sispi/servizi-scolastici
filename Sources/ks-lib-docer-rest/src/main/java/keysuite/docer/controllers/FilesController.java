package keysuite.docer.controllers;

import com.google.common.base.Strings;
import io.swagger.annotations.*;
import it.kdm.orchestratore.session.Session;
import keysuite.desktop.exceptions.KSExceptionBadRequest;
import keysuite.desktop.exceptions.KSExceptionForbidden;
import keysuite.desktop.exceptions.KSExceptionNotFound;
import keysuite.docer.client.DocerBean;
import keysuite.docer.client.FileServiceCommon;
import keysuite.docer.sdk.APIClient;
import keysuite.docer.server.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static keysuite.docer.client.ClientUtils.throwKSException;

@RestController
@RequestMapping("files")
public class FilesController extends BaseController {

    @Autowired
    FileService fileService;

    @Autowired
    protected Environment env;

    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses({
            @ApiResponse( code = 201 , message = "Created",
                    responseHeaders = @ResponseHeader(name = "Location", response = URI.class)),
            @ApiResponse( code = 400 , message = "BadRequest")
    })
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    @PostMapping(consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public String create(
            @RequestBody(required = false) Resource file,
            @RequestParam(required = false, defaultValue = FileService.DEFAULT) String groupId,
            @RequestParam(required = false) String fileName
            ) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {
        try {
            if (!Strings.isNullOrEmpty(fileName))
                groupId+="/"+fileName;
            String fileId = fileService.create(file.getInputStream(),groupId);
            Session.getResponse().setHeader("Location", DocerBean.baseUrl.get()+"/files/"+fileId );
            return fileId;

        } catch (Exception e) {
            throw throwKSException(e);
        }
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(
            value = "multipart",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses({
            @ApiResponse( code = 201 , message = "Created",
                    responseHeaders = @ResponseHeader(name = "Location", response = URI.class)),
            @ApiResponse( code = 400 , message = "BadRequest")
    })
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    public String createMultiPart(@RequestParam("file") MultipartFile file,
                                  @RequestParam(required = false, defaultValue = FileService.DEFAULT) String groupId) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {
        String fileId = create(file.getResource(),groupId,file.getOriginalFilename());
        Session.getResponse().setHeader("Location", DocerBean.baseUrl.get()+"/files/"+fileId );
        return fileId;
    }

    @PostMapping(
            value = "multiparts",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses({
            @ApiResponse( code = 201 , message = "Created",
                    responseHeaders = @ResponseHeader(name = "Location", response = URI.class)),
            @ApiResponse( code = 400 , message = "BadRequest")
    })
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    public String[] createMultiParts(@RequestParam("files") MultipartFile[] files,
                                  @RequestParam(required = false, defaultValue = FileService.DEFAULT) String groupId) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {
        List<String> ids = new ArrayList<>();
        for (MultipartFile file : files)
            ids.add(create(file.getResource(),groupId,file.getOriginalFilename()));
        return ids.toArray(new String[0]);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses({
            @ApiResponse( code = 201 , message = "Created",
                    responseHeaders = @ResponseHeader(name = "Location", response = URI.class)),
            @ApiResponse( code = 400 , message = "BadRequest")
    })
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    @PostMapping(value="createGroup")
    public String createGroup(
            @RequestParam(required = false, defaultValue = FileService.DEFAULT) String store) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {
        try {
            String groupId = fileService.createGroup(store);
            return groupId;
        } catch (Exception e) {
            throw throwKSException(e);
        }
    }

    @ApiResponses({
            @ApiResponse( code = 200 , message = "OK"),
            @ApiResponse( code = 206 , message = "Partial Content"),
            @ApiResponse( code = 404 , message = "NotFound"),
            @ApiResponse( code = 403 , message = "Forbidden"),
            @ApiResponse( code = 400 , message = "BadRequest")
    })
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    @GetMapping(value="{fileId}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE )
    public ResponseEntity get(
            @PathVariable String fileId,
            @RequestHeader(required = false,defaultValue = "0-") String Range,
            @RequestParam(required = false) String bytes,
            HttpServletResponse response,
            HttpServletRequest request

    ) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {

        try {
            if (bytes!=null && Range.equals("0-"))
                Range = bytes;

            Range = Range.trim();
            if (Range.startsWith("bytes"))
                Range = Range.substring("bytes=".length());

            String fileName = request.getParameter("fileName");
            String inline = request.getParameter("inline");
            String ctype = request.getParameter("ct");

            if (!Strings.isNullOrEmpty(fileName) && !fileId.contains("."))
                fileId += "/"+fileName;

            if (Strings.isNullOrEmpty(fileName))
                fileName = fileService.getFilename(fileId);
            String disposition = "true".equals(inline) ? "inline" : "attachment";

            ContentDisposition contentDisposition = ContentDisposition.builder(disposition)
                    .filename(fileName)
                    .build();

            String cd = contentDisposition.toString();

            InputStream stream = fileService.get(fileId,Range);

            HttpStatus status = HttpStatus.OK;

            if (!Range.equals("0-")) {

                String[] parts = Range.split("-");

                long start = Long.parseLong(parts[0]);
                long length,end;

                if (parts.length==1){
                    end = start+stream.available();
                    length = end;
                } else {
                    end = Long.parseLong(parts[1]);

                    FileServiceCommon fsCommon = new FileServiceCommon(Session.getUserInfo().getCodAoo(),Session.getUserInfo().getUsername());
                    length = fsCommon.getFile(fileId).length();
                }

                String cr = String.format("bytes %s-%s/%s",start,end-1,length );
                response.setHeader("Content-Range", cr);
                status = HttpStatus.PARTIAL_CONTENT;
            }

            //response.setHeader("Content-Type", ContentType.APPLICATION_OCTET_STREAM.getMimeType());
            response.setHeader("Accept-Ranges", "bytes");
            response.setHeader("Content-Disposition",cd);
            response.setHeader("X-Frame-Options","SAMEORIGIN");

            MediaType mt = MediaType.APPLICATION_OCTET_STREAM;

            if (ctype!=null)
                mt = MediaType.valueOf(ctype);

            //Session.getResponse().setHeader("Content-Length",""+stream.available());

            return ResponseEntity.status(status)
                    .contentType(mt)
                    .contentLength(stream.available())
                    .body(new InputStreamResource(stream));


        } catch (Exception e2) {
            throw throwKSException(e2);
        } finally {
            //IOUtils.closeQuietly(stream2);
        }
    }

    @GetMapping(value="**", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE )
    public ResponseEntity get(@RequestHeader(required = false,defaultValue = "0-") String Range,
                        @RequestParam(required = false) String bytes,
                        HttpServletResponse response,
                        HttpServletRequest request
                        ) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {
        String fileId = request.getServletPath().substring("/files".length()+1);
        return get(fileId,Range,bytes,response,request);
    }

    @ApiResponses({
            @ApiResponse( code = 200 , message = "OK"),
            @ApiResponse( code = 404 , message = "NotFound"),
            @ApiResponse( code = 403 , message = "Forbidden"),
            @ApiResponse( code = 400 , message = "BadRequest")
    })
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    @PatchMapping(value="{fileId}", consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void update(
            @RequestBody Resource file,
            @RequestHeader(required = false,defaultValue = "0-") String Range,@PathVariable String fileId) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {
        try {
            fileService.update(file.getInputStream(),Range,fileId);
        } catch (Exception e) {
            throw throwKSException(e);
        }
    }

    @ApiResponses({
            @ApiResponse( code = 200 , message = "OK"),
            @ApiResponse( code = 404 , message = "NotFound"),
            @ApiResponse( code = 403 , message = "Forbidden"),
            @ApiResponse( code = 400 , message = "BadRequest")
    })
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    @PatchMapping(value="{fileId}/multipart", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void updateMultiPart(@RequestParam("file") MultipartFile file, @RequestHeader(required = false,defaultValue = "0-") String Range,@PathVariable String fileId) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {
        update(file.getResource(),Range,fileId);
    }

    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @ApiResponses({
            @ApiResponse( code = 202 , message = "NoContent"),
            @ApiResponse( code = 404 , message = "NotFound"),
            @ApiResponse( code = 403 , message = "Forbidden"),
            @ApiResponse( code = 400 , message = "BadRequest")
    })
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    @DeleteMapping(value="{fileId}")
    public void delete(@PathVariable String fileId) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {
        fileService.delete(fileId);
    }

    @DeleteMapping(value="**")
    public void delete(HttpServletResponse response,
                              HttpServletRequest request) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {
        String fileId = request.getServletPath().substring("/files".length()+1);
        delete(fileId);
    }

    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @ApiResponses({
            @ApiResponse( code = 204 , message = "NoContent"),
            @ApiResponse( code = 404 , message = "NotFound"),
            @ApiResponse( code = 403 , message = "Forbidden"),
            @ApiResponse( code = 400 , message = "BadRequest")
    })
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    @PostMapping("/deleteGroup/{groupId}")
    public void deleteGroup(@PathVariable String groupId) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {
        fileService.deleteGroup(groupId);
    }
}
