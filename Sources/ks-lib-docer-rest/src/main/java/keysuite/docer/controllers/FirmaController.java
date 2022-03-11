package keysuite.docer.controllers;

import com.google.common.base.Strings;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ResponseHeader;
import it.kdm.orchestratore.session.Session;
import keysuite.desktop.exceptions.KSExceptionBadRequest;
import keysuite.desktop.exceptions.KSExceptionForbidden;
import keysuite.desktop.exceptions.KSExceptionNotFound;
import keysuite.desktop.exceptions.KSRuntimeException;
import keysuite.docer.client.NamedInputStream;
import keysuite.docer.client.verificafirma.VerificaFirmaDTO;
import keysuite.docer.server.FileService;
import keysuite.docer.server.FirmaService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static keysuite.docer.client.ClientUtils.throwKSException;

@RestController
@RequestMapping("firma")
public class FirmaController extends BaseController {

    @Autowired
    FirmaService firmaService;

    @ApiResponses({
            @ApiResponse( code = 200 , message = "OK"),
            @ApiResponse( code = 404 , message = "NotFound"),
            @ApiResponse( code = 400 , message = "BadRequest"),
            @ApiResponse( code = 403 , message = "Forbidden")
    })
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    @GetMapping("/print")
    public URL[] print(String text, @RequestParam(required = false) String style, URL[] urls) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {
        return firmaService.print(text,style,urls);
    }

    @ApiResponses({
            @ApiResponse( code = 200 , message = "OK"),
            @ApiResponse( code = 404 , message = "NotFound"),
            @ApiResponse( code = 400 , message = "BadRequest"),
            @ApiResponse( code = 403 , message = "Forbidden")
    })
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    @PostMapping("/firmaAutomatica")
    public URL[] firmaAutomatica(String alias, String pin, String tipo, URL[] urls) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {
        return firmaService.firmaAutomatica(alias,pin,tipo,urls);
    }

    @ApiResponses({
            @ApiResponse( code = 200 , message = "OK"),
            @ApiResponse( code = 404 , message = "NotFound"),
            @ApiResponse( code = 400 , message = "BadRequest"),
            @ApiResponse( code = 403 , message = "Forbidden")
    })
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    @PostMapping("/timestamp")
    public URL[] timestamp(String tipo, URL[] urls) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {
        return firmaService.timestamp(tipo,urls);
    }

    @ApiResponses({
            @ApiResponse( code = 200 , message = "OK"),
            @ApiResponse( code = 404 , message = "NotFound"),
            @ApiResponse( code = 400 , message = "BadRequest"),
            @ApiResponse( code = 403 , message = "Forbidden")
    })
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    @PostMapping("/firmaRemota")
    public URL[] firmaRemota(String alias, String pin, String tipo, String OTP, URL[] urls) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {
        return firmaService.firmaRemota(alias,pin,tipo,OTP,urls);
    }

    @ApiResponses({
            @ApiResponse( code = 200 , message = "OK"),
            @ApiResponse( code = 400 , message = "BadRequest"),
            @ApiResponse( code = 403 , message = "Forbidden")
    })
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    @GetMapping("/requestOTP")
    public void requestOTP(String alias, String pin) throws KSExceptionForbidden, KSExceptionBadRequest {
        firmaService.requestOTP(alias,pin);
    }

    @ApiResponses({
            @ApiResponse( code = 200 , message = "OK"),
            @ApiResponse( code = 404 , message = "NotFound"),
            @ApiResponse( code = 400 , message = "BadRequest"),
            @ApiResponse( code = 403 , message = "Forbidden")
    })
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    @GetMapping(value="/verificaFirme",produces = MediaType.APPLICATION_JSON_VALUE)
    public VerificaFirmaDTO[] verificaFirme(
            @RequestParam URL[] urls,
            @RequestParam(required = false) String verificationDate,
            @RequestParam(required = false) String policyFile
            ) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {
        return firmaService.verificaFirme(verificationDate,policyFile,urls);
    }

    @ApiResponses({
            @ApiResponse( code = 200 , message = "OK"),
            @ApiResponse( code = 400 , message = "BadRequest")
    })
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    @PostMapping(
            value="/verificaFirme",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public VerificaFirmaDTO[] verificaFirmeFile(
            @RequestBody(required = false) Resource file,
            @RequestParam(required = false) String verificationDate,
            @RequestParam(required = false) String policyFile
    ) throws KSExceptionBadRequest {
        try {
            NamedInputStream nis = NamedInputStream.getNamedInputStream(file.getInputStream(), file.getFilename());
            NamedInputStream[] niss = new NamedInputStream[]{nis};
            return firmaService.verificaFirme(verificationDate, policyFile, niss);
        } catch (IOException ioe) {
            throw new KSExceptionBadRequest(ioe);
        }
    }

    @PostMapping(
            value = "/verificaFirme/multipart",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiResponses({
            @ApiResponse( code = 200 , message = "OK"),
            @ApiResponse( code = 400 , message = "BadRequest")
    })
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    public VerificaFirmaDTO[] verificaFirmeMultiPart(@RequestParam("file") MultipartFile file,
                                                      @RequestParam(required = false) String verificationDate,
                                                      @RequestParam(required = false) String policyFile) throws KSExceptionBadRequest {

        try {
            NamedInputStream nis = NamedInputStream.getNamedInputStream(file.getInputStream(), file.getOriginalFilename());
            NamedInputStream[] niss = new NamedInputStream[]{nis};

            return firmaService.verificaFirme(verificationDate, policyFile, niss);
        } catch (IOException ioe) {
            throw new KSExceptionBadRequest(ioe);
        }
    }

    @PostMapping(
            value = "/verificaFirme/multiparts",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiResponses({
            @ApiResponse( code = 200 , message = "OK"),
            @ApiResponse( code = 400 , message = "BadRequest")
    })
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    public VerificaFirmaDTO[] verificaFirmeMultiParts(@RequestParam("files") MultipartFile[] files,
                                       @RequestParam(required = false) String verificationDate,
                                       @RequestParam(required = false) String policyFile) throws KSExceptionBadRequest {

        try {
            NamedInputStream[] niss = new NamedInputStream[files.length];

            for (int i = 0; i < files.length; i++)
                niss[i] = NamedInputStream.getNamedInputStream(files[i].getInputStream(), files[i].getOriginalFilename());

            return firmaService.verificaFirme(verificationDate, policyFile, niss);
        } catch (IOException ioe) {
            throw new KSExceptionBadRequest(ioe);
        }
    }

    @ApiResponses({
            @ApiResponse( code = 200 , message = "OK"),
            @ApiResponse( code = 404 , message = "NotFound"),
            @ApiResponse( code = 400 , message = "BadRequest"),
            @ApiResponse( code = 403 , message = "Forbidden")
    })
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    @GetMapping("/report")
    public String report(
            @RequestParam URL[] urls,
            @RequestParam(required = false) String verificationDate,
            @RequestParam(required = false) String policyFile,
            @RequestParam(defaultValue = "false") boolean bootstrap3 ) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {

        List<String> reports = firmaService.reports(verificationDate,policyFile,urls,bootstrap3);

        return StringUtils.join(reports,"<hr/>");
    }

    @ApiResponses({
            @ApiResponse( code = 200 , message = "OK"),
            @ApiResponse( code = 404 , message = "NotFound"),
            @ApiResponse( code = 400 , message = "BadRequest"),
            @ApiResponse( code = 403 , message = "Forbidden")
    })
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    @GetMapping(value = "/pdfReport", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public InputStreamResource report(
            @RequestParam URL url,
            @RequestParam(required = false) String verificationDate,
            @RequestParam(required = false) String policyFile,
            @RequestParam(defaultValue = "false") boolean detailed ) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {


        try {
            NamedInputStream stream = firmaService.getPDFReport(verificationDate,policyFile,url,detailed);

            ContentDisposition contentDisposition = ContentDisposition.builder("inline")
                    .filename(stream.getName())
                    .build();

            String cd = contentDisposition.toString();

            Session.getResponse().setHeader("Content-Length",""+stream.getStream().available());
            Session.getResponse().setHeader("Content-Disposition",cd);

            return new InputStreamResource(stream.getStream(),stream.getName());
        } catch (Exception exception) {
            throw new KSRuntimeException(exception);
        }
    }
}
