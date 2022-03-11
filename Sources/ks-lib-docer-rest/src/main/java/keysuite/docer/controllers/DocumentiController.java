package keysuite.docer.controllers;

import com.google.common.base.Strings;
import io.swagger.annotations.*;
import it.kdm.docer.providers.solr.SolrBaseUtil;
import it.kdm.orchestratore.session.Session;
import keysuite.desktop.exceptions.KSExceptionBadRequest;
import keysuite.desktop.exceptions.KSExceptionForbidden;
import keysuite.desktop.exceptions.KSExceptionNotFound;
import keysuite.docer.client.ClientUtils;
import keysuite.docer.client.Documento;
import keysuite.docer.client.HistoryItem;
import keysuite.docer.client.NamedInputStream;
import keysuite.docer.client.corrispondenti.ICorrispondente;
import keysuite.docer.interceptors.Logging;
import keysuite.docer.interceptors.NotRetryable;
import keysuite.docer.query.ISearchResponse;
import keysuite.docer.server.*;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static keysuite.docer.client.ClientUtils.throwKSException;

@Logging(group = "DocerServices")
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("documenti")
public class DocumentiController extends BaseController {

    @Autowired
    DocumentiService documentiService;

    @Autowired
    FileService fileService;

    @Autowired
    FirmaService firmaService;

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
        return documentiService.search( ServerUtils.parseQueryString(Session.getRequest().getQueryString()) ) ;
    }

    private void checkIdempotency(boolean relate, Documento... documenti) throws KSExceptionBadRequest{
        for( Documento d : documenti ){
            if (d.getGuid()==null && (!relate || relate && d.getDocnum()==null) ){
                throw new KSExceptionBadRequest("per creazione con idempotenza deve essere impostato il guid");
            }
        }
    }

    @NotRetryable
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping( value = {"multi"},
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses({
            @ApiResponse( code = 201 , message = "Created",
                    responseHeaders = @ResponseHeader(name = "Location", response = URI.class)),
            @ApiResponse( code = 400 , message = "BadRequest"),
            @ApiResponse( code = 403 , message = "Forbidden")
    })
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    @Logging(section="createDocument")
    public Documento[] createDocuments(
            @ApiParam("può essere utilizzato per correlare nuovi documenti tra loro o con documenti esistenti")
            @RequestParam(required = false, defaultValue = "false") boolean relate,
            @RequestBody Documento... documenti) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {


        checkIdemHeader(documenti);

        if ("true".equals(Session.getRequest().getAttribute("isRetry")))
            checkIdempotency(relate,documenti);

        Documento[] out = documentiService.createMulti(relate,documenti);
        if (out.length>0){
            Documento documento = out[0];
            if (documento.getURL()!=null)
                Session.getResponse().setHeader("Location", documento.getURL().toString());
        }

        return out;
    }

    @NotRetryable
    @Logging(section="createDocument")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses({
            @ApiResponse( code = 201 , message = "Created",
                    responseHeaders = @ResponseHeader(name = "Location", response = URI.class)),
            @ApiResponse( code = 400 , message = "BadRequest"),
            @ApiResponse( code = 403 , message = "Forbidden")
    })
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    public Documento createDocument(
            @RequestBody Documento documento) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {
        Documento[] docs = createDocuments(false,documento);
        /*documento = documentiService.create(false,documento);
        if (documento.getURL()!=null)
            Session.getResponse().setHeader("Location", documento.getURL().toString());*/
        if (docs.length>0)
            return docs[0];
        else
            return null;
    }

    @NotRetryable
    @Logging(section="createDocument")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping( value = {"multipart"},
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses({
            @ApiResponse( code = 201 , message = "Created",
                    responseHeaders = @ResponseHeader(name = "Location", response = URI.class)),
            @ApiResponse( code = 400 , message = "BadRequest"),
            @ApiResponse( code = 403 , message = "Forbidden")
    })
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    public Documento[] createFiles(@RequestParam(required = false, defaultValue = "false") boolean relate, @RequestParam("files") MultipartFile[] files) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {
        return createDocuments(relate, toDocuments(files));
    }

    @Logging(section="createDocument")
    @PutMapping( value = {"multi"},
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses({
            @ApiResponse( code = 200 , message = "OK"),
            @ApiResponse( code = 201 , message = "Created",
                    responseHeaders = @ResponseHeader(name = "Location", response = URI.class)),
            @ApiResponse( code = 207 , message = "Multiple"),
            @ApiResponse( code = 400 , message = "BadRequest"),
            @ApiResponse( code = 403 , message = "Forbidden")
    })
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    public Documento[] createOrReplaceDocuments(@RequestParam(required = false, defaultValue = "false") boolean relate, @RequestBody Documento... documenti) throws KSExceptionBadRequest, KSExceptionForbidden, KSExceptionNotFound {

        checkIdemHeader(documenti);
        checkIdempotency(relate,documenti);

        return createDocuments(relate, documenti);
    }

    @Logging(section="createDocument")
    @PutMapping( /*value = {"/"},*/
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses({
            @ApiResponse( code = 200 , message = "OK"),
            @ApiResponse( code = 201 , message = "Created",
                    responseHeaders = @ResponseHeader(name = "Location", response = URI.class)),
            @ApiResponse( code = 207 , message = "Multiple"),
            @ApiResponse( code = 400 , message = "BadRequest"),
            @ApiResponse( code = 403 , message = "Forbidden")
    })
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    public Documento createOrReplaceDocument(@RequestBody Documento documento) throws KSExceptionBadRequest, KSExceptionForbidden, KSExceptionNotFound {

        checkIdempotency(false,documento);

        return createDocument(documento);
    }

    @Logging(section="createDocument")
    @PutMapping( value = {"multipart"},
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses({
            @ApiResponse( code = 200 , message = "OK"),
            @ApiResponse( code = 201 , message = "Created",
                    responseHeaders = @ResponseHeader(name = "Location", response = URI.class)),
            @ApiResponse( code = 207 , message = "Multiple"),
            @ApiResponse( code = 400 , message = "BadRequest"),
            @ApiResponse( code = 403 , message = "Forbidden")
    })
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    public Documento[] createOrReplaceFiles(@RequestParam(required = false, defaultValue = "false") boolean relate, @RequestParam("files") MultipartFile[] files) throws KSExceptionBadRequest, KSExceptionForbidden, KSExceptionNotFound {
        return createOrReplaceDocuments(relate, toDocuments(files));
    }

    @GetMapping("testdoc")
    @ApiResponses({
            @ApiResponse( code = 200 , message = "OK"),
            @ApiResponse( code = 201 , message = "Created",
                    responseHeaders = @ResponseHeader(name = "Location", response = URI.class)),
            @ApiResponse( code = 207 , message = "Multiple"),
            @ApiResponse( code = 400 , message = "BadRequest"),
            @ApiResponse( code = 403 , message = "Forbidden")
    })
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    public Documento createTestDoc() throws KSExceptionBadRequest, KSExceptionForbidden, KSExceptionNotFound {

        Documento doc = new Documento();

        doc.setDocname("strtest"+ UUID.randomUUID().toString()+".pdf");
        doc.setArchiveType(Documento.ArchiveType.PAPER);

        return createDocument(doc);
    }

    private Documento[] toDocuments(MultipartFile[] files){
        try {
            List<Documento> docs = new ArrayList<>();

            for (int i = 0; i < files.length; i++) {
                MultipartFile mp = files[i];
                String ct = mp.getContentType();
                InputStream is = mp.getInputStream();

                if (ct!=null && ct.toLowerCase().contains("application/json")) {
                    String json = IOUtils.toString(is, Charset.defaultCharset());
                    Documento doc = ClientUtils.OM.readValue(json,Documento.class);

                    if (doc.getStream()==null && doc.getURL()==null && i < (files.length-1)){
                        MultipartFile next = files[++i];
                        if (next.getContentType()==null || !next.getContentType().toLowerCase().contains("application/json")) {
                            doc.setStream(next.getInputStream());
                            if (doc.getDocname()==null)
                                doc.setDocname(next.getOriginalFilename());
                        }
                    }
                    docs.add(doc);
                } else {
                    Documento doc = new Documento();
                    doc.setDocname(mp.getOriginalFilename());
                    doc.setStream(mp.getInputStream());
                    docs.add(doc);
                }
            }
            return docs.toArray(new Documento[0]);
        } catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    @ApiResponses({
            @ApiResponse( code = 200 , message = "OK"),
            @ApiResponse( code = 404 , message = "NotFound")
    })
    @GetMapping(value = "{docnum}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    public Documento get(@PathVariable String docnum) throws KSExceptionNotFound {
        return documentiService.get(docnum);
    }

    @Logging(section="updateProfileDocument")
    @ApiResponses({
            @ApiResponse( code = 200 , message = "OK"),
            @ApiResponse( code = 404 , message = "NotFound"),
            @ApiResponse( code = 400 , message = "BadRequest"),
            @ApiResponse( code = 403 , message = "Forbidden")
    })
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    @PatchMapping( value= "{docnum}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Documento update( @PathVariable String docnum, @RequestBody Map<String,Object> documento ) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {
        Documento documento1 = ClientUtils.toClientBean(documento,Documento.class);
        documento1.setDocnum(docnum);
        for( String key: documento.keySet() )
            if (documento.get(key)==null)
                documento1.addNullField(key);

        return documentiService.update(documento1);
    }

    @Logging(section = "deleteDocument")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @ApiResponses({
            @ApiResponse( code = 202 , message = "NoContent"),
            @ApiResponse( code = 404 , message = "NotFound"),
            @ApiResponse( code = 400 , message = "BadRequest"),
            @ApiResponse( code = 403 , message = "Forbidden")
    })
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    @DeleteMapping( value= "{docnum}")
    public void delete(@PathVariable(required = true) String docnum) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {
        documentiService.delete(docnum);
    }

    @ApiResponses({
            @ApiResponse( code = 200 , message = "OK"),
            @ApiResponse( code = 404 , message = "NotFound")
    })
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    @GetMapping(value = "{docnum}/related", produces = MediaType.APPLICATION_JSON_VALUE)
    public Documento[] related(@PathVariable String docnum, String... tipo) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {
        return documentiService.related(docnum,tipo);
    }

    @ApiResponses({
            @ApiResponse( code = 200 , message = "OK"),
            @ApiResponse( code = 404 , message = "NotFound")
    })
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    @GetMapping(value = "{docnum}/advanced", produces = MediaType.APPLICATION_JSON_VALUE)
    public Documento[] getAdvancedVersions(@PathVariable String docnum) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {
        return documentiService.getAdvancedVersions(docnum);
    }

    @Logging(section = "addRelated")
    @ApiResponses({
            @ApiResponse( code = 200 , message = "OK"),
            @ApiResponse( code = 404 , message = "NotFound"),
            @ApiResponse( code = 400 , message = "BadRequest"),
            @ApiResponse( code = 403 , message = "Forbidden")
    })
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    @PostMapping( value= "{docnum}/related")
    public void relate(@PathVariable(required = true) String docnum, String... related) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {
        documentiService.relate(docnum,related);
    }

    @Logging(section = "addNewAdvancedVersion")
    @ApiResponses({
            @ApiResponse( code = 200 , message = "OK"),
            @ApiResponse( code = 404 , message = "NotFound"),
            @ApiResponse( code = 400 , message = "BadRequest"),
            @ApiResponse( code = 403 , message = "Forbidden")
    })
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    @PostMapping( value= "{docnum}/advanced")
    public void addAdvancedVersion(@PathVariable(required = true) String docnum, String version) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {
        documentiService.addAdvancedVersion(docnum,version);
    }

    @Logging(section = "removeRelated")
    @ApiResponses({
            @ApiResponse( code = 200 , message = "OK"),
            @ApiResponse( code = 404 , message = "NotFound"),
            @ApiResponse( code = 400 , message = "BadRequest"),
            @ApiResponse( code = 403 , message = "Forbidden")
    })
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    @DeleteMapping( value= "{docnum}/related")
    public void unrelate(@PathVariable(required = true) String docnum, String... related) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {
        documentiService.unrelate(docnum,related);
    }

    @Logging(section = "downloadDocument")
    @ApiResponses({
            @ApiResponse( code = 200 , message = "OK",
                    responseHeaders = @ResponseHeader(name = "content-disposition", response = String.class) ),
            @ApiResponse( code = 404 , message = "NotFound")
    })
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    @GetMapping(value="{docnum}/file", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE )
    public InputStreamResource download(
            @PathVariable String docnum,
            @RequestParam(required = false, defaultValue = "-1") Integer version,
            @RequestHeader(required = false,defaultValue = "0-") String Range ) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {

        try {

            Documento doc = documentiService.get(docnum);

            String docname = doc.getDocname();

            if (version!=null && version!=-1){
                Documento.Version[] versions = doc.getVersions();
                if (versions.length>(version-1)) {
                    String versionname = versions[version - 1].getDocName();
                    if (!Strings.isNullOrEmpty(versionname))
                        docname = versionname;
                }
            }

            ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
                    .filename(docname)
                    .build();

            String cd = contentDisposition.toString();



            InputStream stream = documentiService.download(docnum,version,Range);

            Map<String,Object> extra = SolrBaseUtil.extraFields.get();
            if (extra!=null && extra.containsKey("streamSize")){
                Number length = (Number) extra.get("streamSize") ;
                Session.getResponse().setHeader("Content-Length",""+length);
            }

            Session.getResponse().setHeader("Content-Disposition",cd);
            Session.getResponse().setHeader("Accept-Ranges", "bytes");

            if (Range!=null && !Range.equals("0-"))
                Session.getResponse().setStatus(206);

            return new InputStreamResource(stream);
        } catch (Exception e) {
            throw throwKSException(e);
        }
    }

    @ApiResponses({
            @ApiResponse( code = 200 , message = "OK",
                    responseHeaders = @ResponseHeader(name = "content-disposition", response = String.class) ),
            @ApiResponse( code = 404 , message = "NotFound")
    })
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    @GetMapping(value="{docnum}/versions", produces = MediaType.APPLICATION_JSON_VALUE )
    public Documento.Version[] versions(@PathVariable(required = true) String docnum) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {
        return documentiService.get(docnum).getVersions();
    }

    @Logging(section = "addNewVersion")
    @ApiResponses({
            @ApiResponse( code = 200 , message = "OK"),
            @ApiResponse( code = 404 , message = "NotFound"),
            @ApiResponse( code = 400 , message = "BadRequest"),
            @ApiResponse( code = 403 , message = "Forbidden")
    })
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    @PostMapping(value="{docnum}/file", consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE )
    public Documento addVersion(@PathVariable String docnum,@RequestBody Resource file) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {
        try {
            return documentiService.upload(docnum,file.getInputStream(),false);
        } catch (Exception e) {
            throw throwKSException(e);
        }
    }

    @Logging(section = "replaceLastVersion")
    @ApiResponses({
            @ApiResponse( code = 200 , message = "OK"),
            @ApiResponse( code = 404 , message = "NotFound"),
            @ApiResponse( code = 400 , message = "BadRequest"),
            @ApiResponse( code = 403 , message = "Forbidden")
    })
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    @PutMapping(value="{docnum}/file", consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE )
    public Documento replaceVersion(@PathVariable String docnum,@RequestBody Resource file) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {
        try {
            return documentiService.upload(docnum,file.getInputStream(),true);
        } catch (Exception e) {
            throw throwKSException(e);
        }
    }

    @Logging(section = "addNewVersion")
    @ApiResponses({
            @ApiResponse( code = 200 , message = "OK"),
            @ApiResponse( code = 404 , message = "NotFound"),
            @ApiResponse( code = 400 , message = "BadRequest"),
            @ApiResponse( code = 403 , message = "Forbidden")
    })
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    @PostMapping(value="{docnum}/fileUrl" )
    public Documento addVersionByUrl(@PathVariable String docnum, @RequestParam String url) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {
        try {
            URL url0 = new Documento.StringURL().convert(url);
            return documentiService.upload(docnum,url0,false);
        } catch (Exception e) {
            throw throwKSException(e);
        }
    }

    @Logging(section = "replaceLastVersion")
    @ApiResponses({
            @ApiResponse( code = 200 , message = "OK"),
            @ApiResponse( code = 404 , message = "NotFound"),
            @ApiResponse( code = 400 , message = "BadRequest"),
            @ApiResponse( code = 403 , message = "Forbidden")
    })
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    @PutMapping(value="{docnum}/fileUrl", consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE )
    public Documento replaceVersionByUrl(@PathVariable String docnum,@RequestParam String url) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {
        try {
            URL url0 = new Documento.StringURL().convert(url);
            return documentiService.upload(docnum,url0,true);
        } catch (Exception e) {
            throw throwKSException(e);
        }
    }

    @Logging(section = "lockDocument")
    @ApiResponses({
            @ApiResponse( code = 200 , message = "OK"),
            @ApiResponse( code = 404 , message = "NotFound"),
            @ApiResponse( code = 400 , message = "BadRequest"),
            @ApiResponse( code = 403 , message = "Forbidden")
    })
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    @PostMapping(value="{docnum}/lock",produces = MediaType.APPLICATION_JSON_VALUE)
    public Documento lock( @PathVariable String docnum, @RequestParam(required = false) String expiration ) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {
        return documentiService.lock(docnum,expiration);
    }

    @Logging(section = "unlockDocument")
    @ApiResponses({
            @ApiResponse( code = 200 , message = "OK"),
            @ApiResponse( code = 404 , message = "NotFound"),
            @ApiResponse( code = 400 , message = "BadRequest"),
            @ApiResponse( code = 403 , message = "Forbidden")
    })
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    @PostMapping(value="{docnum}/unlock",produces = MediaType.APPLICATION_JSON_VALUE)
    public Documento unlock( @PathVariable String docnum) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {
        return documentiService.unlock(docnum);
    }

    @Logging(section = "classificaDocumento")
    @ApiResponses({
            @ApiResponse( code = 200 , message = "OK"),
            @ApiResponse( code = 404 , message = "NotFound"),
            @ApiResponse( code = 400 , message = "BadRequest"),
            @ApiResponse( code = 403 , message = "Forbidden")
    })
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    @PostMapping( value= "{docnum}/classifica", produces = MediaType.APPLICATION_JSON_VALUE)
    public Documento classifica(@PathVariable String docnum,@RequestParam(required = false) String classifica,@RequestParam(required = false) String piano, @RequestParam(required = false) String[] secondarie) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {
        return documentiService.classifica(docnum,classifica,piano,secondarie);
    }

    @Logging(section = "fascicolaById", group = "WSFascicolazione")
    @ApiResponses({
            @ApiResponse( code = 200 , message = "OK"),
            @ApiResponse( code = 404 , message = "NotFound"),
            @ApiResponse( code = 400 , message = "BadRequest"),
            @ApiResponse( code = 403 , message = "Forbidden")
    })
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    @PostMapping(value="{docnum}/fascicola",produces = MediaType.APPLICATION_JSON_VALUE)
    public Documento fascicola(@PathVariable String docnum,@RequestParam(required = false) String primario,@RequestParam(required = false) String[] secondari) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {
        return documentiService.fascicola(docnum,primario,secondari);
    }

    @Logging(section = "protocollaById", group = "WSProtocollazione")
    @ApiResponses({
            @ApiResponse( code = 200 , message = "OK"),
            @ApiResponse( code = 404 , message = "NotFound"),
            @ApiResponse( code = 400 , message = "BadRequest"),
            @ApiResponse( code = 403 , message = "Forbidden")
    })
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    @PostMapping(value="{docnum}/protocolla",produces = MediaType.APPLICATION_JSON_VALUE)
    public Documento protocolla(@PathVariable String docnum, @RequestParam(required = false) @ApiParam(allowableValues = "E,I,U") String verso, @RequestParam(required = false) String oggetto,@RequestBody(required = false) ICorrispondente[] corrispondenti) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {

        ICorrispondente mittente = null;
        ICorrispondente[] destinatari = null;

        if (corrispondenti!=null && corrispondenti.length>0){
            mittente = corrispondenti[0];
            destinatari = new ICorrispondente[corrispondenti.length-1];

            for ( int idx = 1; idx<corrispondenti.length; idx++ ){
                destinatari[idx-1] = corrispondenti[idx];
            }
        }

        return documentiService.protocolla(docnum, verso, oggetto, mittente, destinatari);
    }

    @Logging(section = "protocollaById", group = "WSProtocollazione")
    @ApiResponses({
            @ApiResponse( code = 200 , message = "OK"),
            @ApiResponse( code = 404 , message = "NotFound"),
            @ApiResponse( code = 400 , message = "BadRequest"),
            @ApiResponse( code = 403 , message = "Forbidden")
    })
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    @PostMapping(value="{docnum}/annullaProtocollazione",produces = MediaType.APPLICATION_JSON_VALUE)
    public Documento annullaProtocollazione(@PathVariable String docnum,@RequestParam String motivazione,@RequestParam String riferimento) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {
        return documentiService.annullaProtocollazione(docnum,motivazione,riferimento);
    }

    @Logging(section = "registraById", group = "WSRegistrazione")
    @ApiResponses({
            @ApiResponse( code = 200 , message = "OK"),
            @ApiResponse( code = 404 , message = "NotFound"),
            @ApiResponse( code = 400 , message = "BadRequest"),
            @ApiResponse( code = 403 , message = "Forbidden")
    })
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    @PostMapping(value="{docnum}/registra",produces = MediaType.APPLICATION_JSON_VALUE)
    public Documento registra(@PathVariable String docnum, @RequestParam String registro, @RequestParam String oggetto) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {
        return documentiService.registra(docnum,registro,oggetto);
    }

    @Logging(section = "registraById", group = "WSRegistrazione")
    @ApiResponses({
            @ApiResponse( code = 200 , message = "OK"),
            @ApiResponse( code = 404 , message = "NotFound"),
            @ApiResponse( code = 400 , message = "BadRequest"),
            @ApiResponse( code = 403 , message = "Forbidden")
    })
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    @PostMapping(value="{docnum}/annullaRegistrazione",produces = MediaType.APPLICATION_JSON_VALUE)
    public Documento annullaRegistrazione(@PathVariable String docnum, @RequestParam String motivazione, @RequestParam String riferimento) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {
        return documentiService.annullaRegistrazione(docnum,motivazione,riferimento);
    }

    @GetMapping("registri")
    @ApiResponses({
            @ApiResponse( code = 200 , message = "OK")
    })
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    public Map<String,String> registri() {
        return documentiService.registri();
    }

    @GetMapping("tipologie")
    @ApiResponses({
            @ApiResponse( code = 200 , message = "OK")
    })
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    public Map<String,String> tipologie(@RequestParam(required = false) String tipo) {
        return documentiService.tipologie(tipo);
    }

    @GetMapping("{docnum}/history")
    @ApiResponses({
            @ApiResponse( code = 200 , message = "OK")
    })
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    public HistoryItem[] getHistory(@PathVariable String docnum) throws KSExceptionNotFound {
        return documentiService.getHistory(docnum);
    }

    @GetMapping("{docnum}/metadata")
    @ApiResponses({
            @ApiResponse( code = 200 , message = "OK")
    })
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    public Map<String,String> getMetadata(@PathVariable String docnum, @RequestParam(defaultValue = "false") Boolean onlyDetect, @RequestParam(defaultValue = ".*") String regex ) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {
        InputStream stream = documentiService.download(docnum, null, null);
        Documento doc = documentiService.get(docnum);

        NamedInputStream named = NamedInputStream.getNamedInputStream(stream,doc.getName());

        return documentiService.getMetadata(named,onlyDetect, regex);
    }

    @GetMapping("metadata")
    @ApiResponses({
            @ApiResponse( code = 200 , message = "OK")
    })
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    public Map<String,String> getMetadataByUrl(@RequestParam String url, @RequestParam(defaultValue = "false") Boolean onlyDetect,@RequestParam(defaultValue = ".*") String regex) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {

        URL url0 = new Documento.StringURL().convert(url);
        NamedInputStream named = fileService.openURL(url0);

        return documentiService.getMetadata(named,onlyDetect, regex);
    }

    @Logging(section = "invioPEC", group = "WSPEC")
    @ApiResponses({
            @ApiResponse( code = 200 , message = "OK"),
            @ApiResponse( code = 404 , message = "NotFound"),
            @ApiResponse( code = 400 , message = "BadRequest"),
            @ApiResponse( code = 403 , message = "Forbidden")
    })
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    @PostMapping(value="{docnum}/invioPEC")
    public String invioPEC(@PathVariable String docnum, @RequestParam(required = false) String oggetto, @RequestParam(required = false,defaultValue = "false") boolean generaAnnesso, @RequestParam(required = false,defaultValue = "false") boolean allegati, @RequestParam(required = false,defaultValue = "false") boolean verificaFirme, @RequestBody(required = false) ICorrispondente[] destinatari ) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {
        return documentiService.invioPEC(docnum, oggetto, generaAnnesso,allegati,verificaFirme, destinatari);
    }

    @Logging(section = "archivia")
    @ApiResponses({
            @ApiResponse( code = 200 , message = "OK"),
            @ApiResponse( code = 404 , message = "NotFound"),
            @ApiResponse( code = 400 , message = "BadRequest"),
            @ApiResponse( code = 403 , message = "Forbidden")
    })
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    @PostMapping(value="{docnum}/archivia")
    public Documento archivia(@PathVariable String docnum) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {
        return documentiService.archivia(docnum);
    }

    @Logging(section = "pubblica")
    @ApiResponses({
            @ApiResponse( code = 200 , message = "OK"),
            @ApiResponse( code = 404 , message = "NotFound"),
            @ApiResponse( code = 400 , message = "BadRequest"),
            @ApiResponse( code = 403 , message = "Forbidden")
    })
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    @PostMapping(value="{docnum}/pubblica",produces = MediaType.APPLICATION_JSON_VALUE)
    public Documento pubblica(@PathVariable String docnum, @RequestParam String registro, @RequestParam String oggetto, @RequestParam String dataInizio , @RequestParam String dataFine ) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {
        return documentiService.pubblica(docnum,registro,oggetto,dataInizio,dataFine);
    }

    @Logging(section = "annullaPubblicazione", group = "WSRegistrazione")
    @ApiResponses({
            @ApiResponse( code = 200 , message = "OK"),
            @ApiResponse( code = 404 , message = "NotFound"),
            @ApiResponse( code = 400 , message = "BadRequest"),
            @ApiResponse( code = 403 , message = "Forbidden")
    })
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    @PostMapping(value="{docnum}/annullaPubblicazione",produces = MediaType.APPLICATION_JSON_VALUE)
    public Documento annullaPubblicazione(@PathVariable String docnum, @RequestParam String motivazione, @RequestParam String dataAnnullamento) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {
        return documentiService.annullaPubblicazione(docnum,motivazione,dataAnnullamento);
    }

    @Logging(section = "conserva")
    @ApiResponses({
            @ApiResponse( code = 200 , message = "OK"),
            @ApiResponse( code = 404 , message = "NotFound"),
            @ApiResponse( code = 400 , message = "BadRequest"),
            @ApiResponse( code = 403 , message = "Forbidden")
    })
    @ApiImplicitParam(name = "Authorization",paramType = "header")
    @PostMapping(value="{docnum}/conserva",produces = MediaType.APPLICATION_JSON_VALUE)
    public Documento conserva(@PathVariable String docnum) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {
        return documentiService.conserva(docnum);
    }




}
