package keysuite.docer.server;

import com.github.underscore.lodash.U;
import com.google.common.base.Strings;
import io.jsonwebtoken.Claims;
import it.kdm.docer.PEC.BusinessLogic;
import it.kdm.docer.providers.solr.SolrBaseUtil;
import it.kdm.docer.sdk.Constants;
import it.kdm.docer.sdk.exceptions.DocerException;
import it.kdm.orchestratore.session.Session;
import it.kdm.solr.client.SolrClient;
import keysuite.cache.ClientCacheAuthUtils;
import keysuite.desktop.exceptions.KSExceptionBadRequest;
import keysuite.desktop.exceptions.KSExceptionForbidden;
import keysuite.desktop.exceptions.KSExceptionNotFound;
import keysuite.desktop.exceptions.KSRuntimeException;
import keysuite.docer.client.*;
import keysuite.docer.client.corrispondenti.ICorrispondente;
import keysuite.docer.query.ISearchParams;
import keysuite.docer.query.ISearchResponse;
import keysuite.solr.SolrSimpleClient;
import org.apache.commons.httpclient.auth.AuthenticationException;
import org.apache.commons.lang.StringUtils;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.net.URL;
import java.util.*;

import static keysuite.docer.client.ClientUtils.throwKSException;
import static keysuite.docer.client.ClientUtils.throwKSExceptionDefBadRequest;
import static keysuite.docer.client.Documento.DEFAULT_TYPE;

//import keysuite.docer.providers.SolrBaseUtil;

@Component
public class DocumentiService extends BaseService implements IDocumenti, EnvironmentAware {

    @Autowired
    FileService fileService;

    @Override
    public ISearchResponse search(ISearchParams params) throws KSExceptionBadRequest {
        params.add("fq", "type:documento");
        return solrSimpleSearch(params);
    }

    @Override
    public DocerBean create(DocerBean bean) throws KSExceptionBadRequest, KSExceptionForbidden, KSExceptionNotFound {
        return create((Documento) bean);
    }

    @Override
    public Documento create(Documento documento) throws KSExceptionBadRequest, KSExceptionForbidden, KSExceptionNotFound {
        Documento[] docs = createMulti(false, documento);
        if (docs.length > 0)
            return docs[0];
        else
            return null;
    }

    @Override
    public DocerBean update(DocerBean bean) throws KSExceptionBadRequest, KSExceptionForbidden, KSExceptionNotFound {
        return update((Documento) bean);
    }

    @Override
    public Documento[] createMulti(boolean relate, Documento... documenti) throws KSExceptionBadRequest, KSExceptionForbidden, KSExceptionNotFound {

        addExtraData("count", documenti.length);

        boolean is200 = false;
        boolean is201 = false;
        Documento principale = null;
        List<Documento> out = new ArrayList<>();

        List<String> related = new ArrayList<>();

        for (int i = 0; i < documenti.length; i++) {

            //String docnum = documento.getDocnum();

            /*if (docnum!=null){
                throw new KSExceptionBadRequest("non si può specificare docnum in creazione");
            }*/

            Documento documento = documenti[i];

            if (Strings.isNullOrEmpty(documento.getTipologia()))
                documento.setTipologia(DEFAULT_TYPE);

            try {

                String docnum = documento.getDocnum();

                if (!relate && docnum != null) {
                    throw new KSExceptionBadRequest("non si può specificare docnum in una operazione di creazione");
                }

                /*

                GUID!=null  trovato    DOCNUM!=null  related

          (0)                               Si          No          Non ammesso a priori

          (1)   !   Si         Si           Si          Si          Related senza creazione (devono coincidere i docnum)
          (2)       Si         Si           No          No          Creazione idempotente (retry)
          (3)       Si         Si           No          Si          Creazione idempotente (retry) con related
          (4)   !   Si         No           Si          Si          Non ammesso
          (5)       Si         No           No          No          Creazione idempotente
          (6)       Si         No           No          Si          Creazione idempotente con related

          (7)       No                      No          No          Creazione normale
          (8)       No                      No          Si          Creazione normale con related
          (9)       No                      Si          Si          Related senza creazione

                */

                String GUID = documento.getGuid();

                //idempotenza in creazione (docnum deve essere null)
                if (!Strings.isNullOrEmpty(GUID)) {
                    Documento gd = solrGetByGUID(GUID);

                    if (docnum != null && gd == null) { //(4)
                        throw new KSExceptionBadRequest("non si può specificare docnum in una operazione di creazione idempotente");
                    }

                    if (gd != null && docnum != null && !docnum.equals(gd.getDocnum())) //(1 KO)
                        throw new KSExceptionBadRequest("docnum e guid non coincidono");

                    if (gd != null) {

                        if (relate) { // (1 OK) e (3)
                            docnum = gd.getDocnum();
                            related.add(docnum);
                        } //else (2)

                        is200 = true;

                        if (i == 0)
                            principale = gd;

                        out.add(gd);

                        continue;

                    } //else (5) e (6)

                } else {
                    //i documenti successivi al primo possono essere citati per docnum per fare correlazione
                    if (docnum != null) { //(9)
                        assert (relate);

                        related.add(docnum);

                        if (i == 0)
                            principale = documento;

                        is200 = true;

                        out.add(documento);

                        continue;
                    } //(7) e (8)
                }

                assert (docnum == null);

                // 5,6,7,8

                InputStream is = documento.getStream();

                if (is == null) {

                    URL url = documento.getURL();

                    if (url == null) {
                        if (Documento.ArchiveType.PAPER.equals(documento.getArchiveType()))
                            documento.setOtherField("NO_FILE", "true");
                    } else {
                        if (Documento.ArchiveType.URL.equals(documento.getArchiveType())) {
                            documento.setOtherField("DOC_URL", url.toString());
                        } else {
                            NamedInputStream stream = fileService.openURL(url);

                            is = stream.getStream();

                            if (documento.getDocname() == null && url != null) {
                                documento.setDocname(stream.getName());
                            }
                        }
                    }
                }

                checkAcl(null, documento);
                checkExtraField(documento);

                Map map = ServerUtils.toDocerMap(documento);

                if (documento.getRiferimenti() != null)
                    map.put("RIFERIMENTI", StringUtils.join(documento.getRiferimenti(), ","));

                //String ticket = getTicket();

                docnum = newBLDocer().createDocument(getTicket(), map, is);

                Documento newDoc = solrGet(docnum, Documento.TYPE);

                if (i == 0) {
                    principale = newDoc;
                    addExtraData("docnum", docnum);
                } else {
                    related.add(docnum);
                }

                out.add(newDoc);

            } catch (Exception de) {
                throw throwKSException(de);
            }
        }

        if (relate && documenti.length > 1) {
            relate(principale.getDocnum(), related.toArray(new String[0]));
        }

        if (Session.getResponse() != null) {
            if (is200 && is201)
                Session.getResponse().setStatus(207);
            else if (!is200)
                Session.getResponse().setStatus(201);
        }

        return out.toArray(new Documento[out.size()]);
    }

    @Override
    public Documento get(String docnum) throws KSExceptionNotFound {
        return solrGet(docnum, Documento.TYPE);
    }

    @Override
    public Documento update(Documento documento) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {
        String docnum = documento.getDocnum();
        try {
            Documento oldBean = get(documento.getDocnum());
            //Map oldMap = ServerUtils.toDocerMap(oldBean);
            checkAcl(oldBean, documento);
            checkExtraField(documento);

            List<String> oldRifsL = oldBean.getRiferimenti() == null ? new ArrayList<>() : Arrays.asList(oldBean.getRiferimenti());
            List<String> newRifsL = documento.getRiferimenti() == null ? new ArrayList<>() : Arrays.asList(documento.getRiferimenti());

            Map updateMap = ServerUtils.toUpdateMap(oldBean, documento);

            if (newRifsL.size() != oldRifsL.size() || oldRifsL.retainAll(newRifsL))
                updateMap.put("RIFERIMENTI", StringUtils.join(newRifsL.toArray(new String[0]), ","));

            if (oldBean.otherFields().get("NUM_PG") != null) {
                if (updateMap.containsKey("MITTENTI"))
                    throw new KSExceptionBadRequest("documento protocollato: non possibile modificare MITTENTI");
                if (updateMap.containsKey("DESTINATARI"))
                    throw new KSExceptionBadRequest("documento protocollato: non possibile modificare DESTINATARI");
                if (updateMap.containsKey("TIPO_PROTOCOLLAZIONE"))
                    throw new KSExceptionBadRequest("documento protocollato: non possibile modificare TIPO_PROTOCOLLAZIONE");
            }

            newBLDocer().updateProfileDocument(getTicket(), docnum, updateMap);
            return solrGet(docnum, Documento.TYPE);
        } catch (DocerException e) {
            throw throwKSExceptionDefBadRequest(e);
        } catch (Exception e) {
            throw new KSRuntimeException(e);
        }
    }

    @Override
    public void delete(String docnum) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {
        try {
            newBLDocer().deleteDocument(getTicket(), docnum);
        } catch (DocerException e) {
            throw new KSExceptionBadRequest(e);
        } catch (Exception e) {
            throw new KSRuntimeException(e);
        }
    }

    @Override
    public Documento[] related(String docnum, String... tipo) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {
        try {

            //il documento richiesto va sempre per primo ed il principale subito dopo

            String[] relatedIds = solrGetRelated(docnum);

            if (relatedIds == null) {
                return new Documento[]{get(docnum)};
            }

            List<Documento> docs = solrGets(relatedIds);

            if (docs.size() == 0) {
                return new Documento[]{get(docnum)};
            }

            Integer principale = null;
            Integer thisdoc = null;

            for (int i = 0; i < docs.size(); i++) {
                if (docnum.equals(docs.get(i).getDocnum()))
                    thisdoc = i;
                if ("PRINCIPALE".equals(docs.get(i).getTipoComponente()))
                    principale = i;
            }

            if (principale != null && principale != 0) {
                Documento p = docs.get(principale);
                docs.remove((int) principale);
                docs.add(0, p);
            }

            if (thisdoc == null) {
                Documento p = get(docnum);
                docs.add(0, p);
            } else if (thisdoc != 0) {
                Documento p = docs.get(thisdoc);
                docs.remove((int) thisdoc);
                docs.add(0, p);
            }

            List<Documento> output;

            if (tipo != null && tipo.length > 0) {
                output = new ArrayList<>();
                Set<String> tipi = new HashSet<String>(Arrays.asList(tipo));
                tipo = tipi.toArray(new String[0]);
                for (int i = 0; i < tipo.length; i++) {
                    for (int j = 0; j < docs.size(); j++) {
                        Documento doc = docs.get(j);
                        String tipo_componente = doc.getTipoComponente();
                        if (tipo_componente == null)
                            tipo_componente = "";
                        if (tipo[i].equals(tipo_componente))
                            if (j == thisdoc)
                                output.add(0, doc);
                            else
                                output.add(doc);
                    }
                }
            } else {
                output = docs;
            }

            return output.toArray(new Documento[output.size()]);

        } catch (Exception e) {
            throw throwKSException(e);
        }
    }

    @Override
    public void addAdvancedVersion(String docnum, String version) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {
        try {
            newBLDocer().addNewAdvancedVersion(getTicket(), docnum, version);
        } catch (Exception e) {
            throw throwKSException(e);
        }
    }

    @Override
    public Documento[] getAdvancedVersions(String docnum) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {
        try {

            //vengono resituite dalla più vecchia alla più nuova

            String[] relatedIds = solrGetAdvancedVersions(docnum);

            if (relatedIds == null) {
                return new Documento[]{get(docnum)};
            }

            List<Documento> docs = solrGets(relatedIds);

            return docs.toArray(new Documento[0]);

        } catch (Exception e) {
            throw throwKSException(e);
        }
    }

    @Override
    public void relate(String docnum, String... related) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {
        try {
            newBLDocer().addRelated(getTicket(), docnum, Arrays.asList(related));
        } catch (Exception e) {
            throw throwKSException(e);
        }
    }

    @Override
    public void unrelate(String docnum, String... related) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {
        try {
            newBLDocer().removeRelated(getTicket(), docnum, Arrays.asList(related));
        } catch (Exception e) {
            throw throwKSException(e);
        }
    }


    @Override
    public InputStream download(String docnum, Integer version, String Range) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {
        try {

            //Integer offset = 0;
            //Integer length = null;

            if (Range != null && !Range.equals("0-")) {
                if (Range.startsWith("bytes="))
                    Range = Range.substring(6);

                Map<String, Object> extra = new LinkedHashMap<>();
                extra.put("Range", Range);

                SolrBaseUtil.extraFields.set(extra);
            }

            InputStream stream;

            if (version != null && version != -1) {
                stream = newBLDocer().streamVersion(getTicket(), docnum, "" + version);
            } else {
                stream = newBLDocer().streamDocument(getTicket(), docnum);
            }
            return stream;

            //if (length!=null)
            //    assert length == bytes.length;

            //return new ByteArrayInputStream(bytes);
        } catch (Exception e) {
            throw throwKSException(e);
        }
    }

    @Override
    public Documento.Version[] versions(String docnum) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {
        try {
            return this.get(docnum).getVersions();
            //List<String> versions = BLDocer.getVersions(getTicket(),docnum);
            //return versions;
        } catch (Exception e) {
            throw throwKSException(e);
        }
    }

    @Override
    public Documento upload(String docnum, URL url, boolean replace) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {

        NamedInputStream stream = fileService.openURL(url);

        return upload(docnum, stream.getStream(), replace);

        /*if ("file".equals(url.getProtocol())) {
            try {
                return upload(docnum, new FileInputStream(url.getFile()),replace);
            } catch (FileNotFoundException e) {
                throw new KSExceptionNotFound(e);
            }
        }
        else{
            try {
                HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
                urlConn.setRequestProperty("Authorization", Session.getUserInfoNoExc().getJwtToken() );
                return upload(docnum, urlConn.getInputStream(),replace);
            } catch (IOException e) {
                throw new KSRuntimeException(e);
            }
        }*/
    }

    @Override
    public Documento upload(String docnum, InputStream file, boolean replace) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {

        try {

            if (replace)
                newBLDocer().replaceLastVersion(getTicket(), docnum, file);
            else
                newBLDocer().addNewVersion(getTicket(), docnum, file);

            Documento documento = solrGet(docnum, Documento.TYPE);

            /*documento.updateLastVersion(replace,Session.getUserInfoNoExc().getUsername());

            try {
                update(documento);
            } catch (Exception e){
                e.printStackTrace();
            }*/

            return documento;

        } catch (Exception e) {
            throw throwKSException(e);
        }
    }

    @Override
    public Documento lock(String docnum, String expiration) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {
        try {

            if (expiration != null) {
                long duration;
                if (expiration.matches("\\d+"))
                    duration = Long.parseLong(expiration);
                else {
                    Date exp = ClientUtils.dateFormat.parse(expiration);
                    duration = exp.getTime() - System.currentTimeMillis();
                }

                Map<String, Object> extra = new LinkedHashMap<>();
                extra.put("expiration", duration);
                SolrBaseUtil.extraFields.set(extra);
            }

            newBLDocer().lockDocument(getTicket(), docnum);
            return solrGet(docnum, Documento.TYPE);
        } catch (Exception e) {
            throw throwKSException(e);
        }
    }

    @Override
    public Documento unlock(String docnum) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {
        try {
            newBLDocer().unlockDocument(getTicket(), docnum);
            return solrGet(docnum, Documento.TYPE);
        } catch (Exception e) {
            throw throwKSException(e);
        }
    }

    @Override
    public Documento classifica(String docnum, String classifica, String piano, String[] secondarie) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {
        try {

            Documento oldBean = null;

            if (classifica != null) {

                oldBean = get(docnum);

                String oldClassifica = oldBean.getClassifica();

                if (!classifica.equals(oldClassifica)) {

                    Map tit = new LinkedHashMap();
                    tit.put("CLASSIFICA", classifica);
                    if (!Strings.isNullOrEmpty(piano))
                        tit.put("PIANO_CLASS", piano);
                    newBLDocer().classificaDocumento(getTicket(), docnum, tit);
                }
            }

            if (secondarie != null) {

                if (oldBean == null)
                    oldBean = get(docnum);

                Map tit = new LinkedHashMap();
                if (secondarie.length == 0 || secondarie.length == 1 && "".equals(secondarie[0]))
                    tit.put(Documento.CLASSIFICHE, "");
                else
                    tit.put(Documento.CLASSIFICHE, StringUtils.join(secondarie, ","));
                newBLDocer().updateProfileDocument(getTicket(), docnum, tit);
            }

            return solrGet(docnum, Documento.TYPE);
        } catch (Exception e) {
            throw throwKSException(e);
        }
    }

    @Override
    public Documento fascicola(String docnum, String primario, String[] secondari) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {

        try {
            String xml = ServerUtils.buildXMLFascicolazione(primario, secondari);
            wsFascicolazioneEndpoint.fascicolaById(getToken(), docnum, xml);

            /*if (isFascicolazioneInterna()){

                Map<String,String> docMap = new LinkedHashMap<>();

                Documento documento = get(docnum);

                if (primario==null){
                    primario = documento.getFascicoloPrimario();
                }

                docMap.put("COD_ENTE",Session.getUserInfoNoExc().getCodEnte());
                docMap.put("COD_AOO",Session.getUserInfoNoExc().getCodAoo());

                if ("".equals(primario)) {
                    docMap.put("ANNO_FASCICOLO","");
                    docMap.put("PROGR_FASCICOLO","");
                    docMap.put("PIANO_CLASS","");
                } else if (primario!=null){
                    String[] splitted = ClientUtils.splitFascicoloId(primario);
                    docMap.put("CLASSIFICA",splitted[0]);
                    docMap.put("ANNO_FASCICOLO",splitted[1]);
                    docMap.put("PROGR_FASCICOLO",splitted[2]);
                    docMap.put("PIANO_CLASS",splitted[3]);
                } else {
                    docMap.put("CLASSIFICA",null);
                    docMap.put("ANNO_FASCICOLO",null);
                    docMap.put("PROGR_FASCICOLO",null);
                    docMap.put("PIANO_CLASS",null);
                }

                String FASC_SECONDARI = null;

                if (secondari==null){
                    FASC_SECONDARI = (String) documento.otherFields().get("FASC_SECONDARI");
                } else if (secondari.length==0) {
                    FASC_SECONDARI = "";
                } else {
                    FASC_SECONDARI = StringUtils.join(secondari,";")+";" ;
                }

                if (!"".equals(primario))
                    docMap.put("FASC_SECONDARI",FASC_SECONDARI);

                BLDocer.fascicolaDocumento(getTicket(),docnum,docMap);

            } else {
                String xml = ServerUtils.buildXMLFascicolazione(primario,secondari);
                //WSFascicolazioneClient.execute("#fascicolaById",getToken(),Integer.parseInt(docnum),xml);
                wsFascicolazioneEndpoint.fascicolaById(getToken(),docnum,xml);
            }*/

            return solrGet(docnum, Documento.TYPE);
        } catch (Exception se) {
            throw throwKSException(se);
        }
    }

    @Override
    public Documento protocolla(String docnum, String verso, String oggetto, ICorrispondente mittente, ICorrispondente[] destinatari) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {
        try {
            if (verso == null || oggetto == null || mittente == null || destinatari == null || destinatari.length == 0) {
                Documento oldBean = get(docnum);

                if (mittente == null)
                    mittente = oldBean.getMittente();

                if (destinatari == null || destinatari.length == 0)
                    destinatari = oldBean.getDestinatari();

                if (verso == null)
                    verso = (String) oldBean.otherFields().get("TIPO_PROTOCOLLAZIONE");

                if (oggetto == null) {
                    if (oldBean.otherFields().get("NUM_PG") == null)
                        oggetto = (String) oldBean.otherFields().get("OGGETTO_PROTOCOLLAZIONE");
                    else
                        oggetto = (String) oldBean.otherFields().get("OGGETTO_PG");
                }

            }

            if (Strings.isNullOrEmpty(oggetto))
                throw new KSExceptionBadRequest("oggetto obbligatorio");
            if (Strings.isNullOrEmpty(verso))
                throw new KSExceptionBadRequest("verso obbligatorio");
            if (mittente == null)
                throw new KSExceptionBadRequest("mittente obbligatorio");
            if (destinatari == null || destinatari.length == 0)
                throw new KSExceptionBadRequest("destinatari obbligatori");

            String xml = ServerUtils.buildXMLProtocollazione(oggetto, verso, mittente, destinatari);
            wsProtocollazioneEndpoint.protocollaById(getToken(), docnum, xml);
            /*if (oggetto!=null) {
                BaseController.addExtraData("oggetto", oggetto);
            }

            if (mittente!=null) {
                BaseController.addExtraData("mittente", mittente.toString());
            }

            if (destinatari!=null) {
                for( ICorrispondente destinatario : destinatari ){
                    BaseController.addExtraData("destinatario", destinatario.toString());
                }
            }*/

            /*if (isProtocollazioneInterna()){

                Documento doc = solrGet(docnum, Documento.TYPE);

                String NUM_PG = (String) doc.otherFields().get("NUM_PG");

                Map metadata = new HashMap();

                if (NUM_PG==null){
                    Protocollazione provider = new Protocollazione();
                    UserInfo ui = Session.getUserInfoNoExc();

                    DateTime now = new DateTime(DateTimeZone.UTC);
                    Integer next = provider.staccaNumero(ui.getCodEnte(),ui.getCodAoo(),false);

                    //String DATA_PG = ClientUtils.dateFormat.format(now);

                    String TIPO_FIRMA = (String) doc.otherFields().get("TIPO_FIRMA");
                    if (Strings.isNullOrEmpty(TIPO_FIRMA))
                        TIPO_FIRMA = "NF";

                    metadata.put("TIPO_PROTOCOLLAZIONE", verso);
                    metadata.put("ANNO_PG", ""+now.getYear());
                    metadata.put("DATA_PG", now.toString());
                    metadata.put("REGISTRO_PG", "PG");
                    metadata.put("NUM_PG", ""+next);
                    metadata.put("TIPO_FIRMA", TIPO_FIRMA);
                }

                if (oggetto!=null) {
                    metadata.put("OGGETTO_PG", oggetto);
                }

                if (mittente!=null){
                    metadata.put("MITTENTI","<Mittenti>"+mittente.toXml("Mittente")+"</Mittenti>");
                }

                if (destinatari!=null && destinatari.length>0){
                    String value = "<Destinatari>";
                    for ( ICorrispondente corr : destinatari )
                        value += corr.toXml("Destinatario");
                    value += "</Destinatari>";
                    metadata.put("DESTINATARI",value);
                }

                BLDocer.protocollaDocumento(getTicket(),docnum,metadata);
            } else {
                String xml = ServerUtils.buildXMLProtocollazione(oggetto,verso,mittente,destinatari);
                //WSProtocollazioneClient.execute("#protocollaById",getToken(),Integer.parseInt(docnum),xml);
                wsProtocollazioneEndpoint.protocollaById(getToken(),docnum,xml);
            }*/

            return solrGet(docnum, Documento.TYPE);
        } catch (Exception se) {
            throw throwKSException(se);
        }
    }

    @Override
    public Documento annullaProtocollazione(String docnum, String motivazione, String riferimento) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {

        Map<String, String> metadata = new LinkedHashMap<>();
        metadata.put("ANNULLATO_PG", "SI");
        metadata.put("D_ANNULL_PG", ClientUtils.dateFormat.format(new Date()));
        metadata.put("M_ANNULL_PG", motivazione);
        metadata.put("P_ANNULL_PG", riferimento);

        try {
            newBLDocer().protocollaDocumento(getTicket(), docnum, metadata);
            return solrGet(docnum, Documento.TYPE);
        } catch (Exception se) {
            throw throwKSException(se);
        }
    }

    @Override
    public Documento registra(String docnum, String registro, String oggetto) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {
        try {
            String xml = ServerUtils.buildXMLRegistrazione(oggetto);
            wsRegistrazioneEndpoint.registraById(getToken(), docnum, registro, xml);

            /*if (isRegistrazioneInterna()){

                Documento doc = solrGet(docnum, Documento.TYPE);

                String TIPO_FIRMA = (String) doc.otherFields().get("TIPO_FIRMA");
                if (Strings.isNullOrEmpty(TIPO_FIRMA))
                    TIPO_FIRMA = "NF";

                Registrazione provider = new Registrazione();

                DateTime now = new DateTime(DateTimeZone.UTC);

                UserInfo ui = Session.getUserInfoNoExc();

                Integer next = provider.staccaNumero(ui.getCodEnte(),ui.getCodAoo(),registro,false);

                //String D_REGISTRAZ = ClientUtils.dateFormat.format(new Date(0));

                Map metadata = new HashMap();
                metadata.put("O_REGISTRAZ",oggetto);
                metadata.put("ID_REGISTRO",registro);
                metadata.put("A_REGISTRAZ",""+now.getYear());
                metadata.put("D_REGISTRAZ",now.toString());
                metadata.put("N_REGISTRAZ",""+next);
                metadata.put("TIPO_FIRMA",TIPO_FIRMA);

                BLDocer.registraDocumento(getTicket(),docnum,metadata);
            } else {
                String xml = ServerUtils.buildXMLRegistrazione(oggetto);
                //WSRegistrazioneClient.execute("#registraById",getToken(),Integer.parseInt(docnum),registro,xml);
                wsRegistrazioneEndpoint.registraById(getToken(),docnum,registro,xml);
            }*/
            return solrGet(docnum, Documento.TYPE);

        } catch (Exception se) {
            throw throwKSException(se);
        }
    }

    @Override
    public Documento annullaRegistrazione(String docnum, String motivazione, String riferimento) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {

        Map<String, String> metadata = new LinkedHashMap<>();
        metadata.put("ANNULL_REGISTRAZ", "SI");
        metadata.put("D_ANNULL_REGISTRAZ", ClientUtils.dateFormat.format(new Date()));
        metadata.put("M_ANNULL_REGISTRAZ", motivazione);
        metadata.put("P_ANNULL_REGISTRAZ", riferimento);

        try {
            newBLDocer().registraDocumento(getTicket(), docnum, metadata);
            return solrGet(docnum, Documento.TYPE);
        } catch (Exception se) {
            throw throwKSException(se);
        }
    }

    @Override
    public Map<String, String> registri() {
        try {
            Map<String, String> res;

            /*if (isRegistrazioneInterna()){
                res = new HashMap<>();

                String[] registri = System.getProperty("registri","REGISTRO_GIORNALIERO").split(",");

                for ( String reg : registri){
                    res.put(reg, StringUtils.capitalize(reg.toLowerCase().replaceAll("_"," ")));
                }

            } else {
                res = WSRegistrazioneClient.execute("#getRegistri",getToken(),Session.getUserInfoNoExc().getCodEnte(),Session.getUserInfoNoExc().getCodAoo());
            }*/

            res = wsRegistrazioneEndpoint.getRegistri(getToken(), Session.getUserInfoNoExc().getCodEnte(), Session.getUserInfoNoExc().getCodAoo());

            for (String reg : res.keySet()) {
                if (Strings.isNullOrEmpty(res.get(reg))) {
                    res.put(reg, StringUtils.capitalize(reg.toLowerCase().replaceAll("_", " ")));
                }
            }

            return res;

        } catch (Exception se) {
            throw new KSRuntimeException(se);
        }
    }

    @Override
    public Map<String, String> tipologie(String tipo) {
        try {

            String xml = newBLDocer().readConfig(getTicket());
            Map<String, Object> map = (Map) U.fromXml(xml);

            Object _types = U.get(map, "configuration.group[1].section[2].type");

            List<Map> types = _types instanceof List ? (List<Map>) _types : Collections.singletonList((Map) _types);

            Map<String, String> res;
            res = new LinkedHashMap<>();

            for (Map type : types) {
                String name = (String) type.get("-name");
                String description = (String) type.get("-description");
                String tipologia = (String) type.get("-tipologia");

                if (!Strings.isNullOrEmpty(tipo) && !"*".equals(tipo)) {
                    if (!"*".equals(tipologia)) {
                        if (!tipo.equals(tipologia))
                            continue;
                    }
                }

                if (Strings.isNullOrEmpty(description)) {
                    description = StringUtils.capitalize(name.toLowerCase().replaceAll("_", " "));
                }
                res.put(name, description);
            }

            return res;

        } catch (Exception se) {
            throw new KSRuntimeException(se);
        }
    }

    @Override
    public HistoryItem[] getHistory(String docnum) throws KSExceptionNotFound {
        Documento doc = get(docnum);
        List<HistoryItem> history = new ArrayList(Arrays.asList(doc.getHistoryItems()));

        try {
            DocerBean related = solrGetFirst("related:" + getSolrId(docnum, "documento"), "type:related");
            if (related != null) {
                List<HistoryItem> items = new ArrayList<>(Arrays.asList(related.getHistoryItems()));
                items.remove(0);
                history.addAll(items);
                //Collections.sort(history, Comparator.comparing(HistoryItem::getTimestamp));
            }
        } catch (KSExceptionBadRequest ksExceptionBadRequest) {
            throw new RuntimeException(ksExceptionBadRequest);
        }

        return history.toArray(new HistoryItem[history.size()]);
    }

    public void deleteByQuery(String query) throws KSExceptionForbidden {
        if (!Session.getUserInfoNoExc().isAdmin())
            throw new KSExceptionForbidden("solo admin");
        new SolrSimpleClient().deleteByQuery(query);
    }

    public void commit() throws KSExceptionForbidden {
        if (!Session.getUserInfoNoExc().isAdmin())
            throw new KSExceptionForbidden("solo admin");
        new SolrSimpleClient().commit();
    }

    public Map<String, String> getMetadata(NamedInputStream stream, Boolean onlyDetect, String regex) {
        try {

            //InputStream sc = ResourceUtils.getResourceNoExc("tika-config.xml");

            //TikaConfig config = new TikaConfig(sc);

            Parser parser = new AutoDetectParser();

            Map<String, String> md = new LinkedHashMap<>();

            Metadata metadata = new Metadata();

            if (stream.getName() != null)
                metadata.add(Metadata.RESOURCE_NAME_KEY, stream.getName());

            if (onlyDetect) {
                TikaConfig tika = new TikaConfig();
                MediaType type = tika.getDetector().detect(
                        TikaInputStream.get(stream.getStream()), metadata);

                md.put(Metadata.CONTENT_TYPE, type.toString());
                md.put("Content-Subtype", type.getSubtype());
                md.put("Content-Basetype", type.getBaseType().toString());
                return md;

            }


            BodyContentHandler handler = new BodyContentHandler();


            ParseContext context = new ParseContext();

            parser.parse(stream.getStream(), handler, metadata, context);

            String[] metadataNames = metadata.names();


            for (String name : metadataNames) {
                if (name.matches(regex))
                    md.put(name, metadata.get(name));
            }
            return md;
        } catch (Exception e) {
            throw new KSRuntimeException(e);
        }
    }

    IPubblicazioneProvider getPubblicazioneProvider(String codEnte, String codAoo){
        String provider = env.getProperty("provider.pubblicazione");
        try {
            return (IPubblicazioneProvider) Class.forName(provider).getConstructor(Map.class).newInstance();
        } catch (Exception exc) {
            throw new KSRuntimeException(exc);
        }
    }

    IConservazioneProvider getConservazioneProvider(String codEnte, String codAoo){
        String provider = env.getProperty("provider.conservazione");
        try {
            return (IConservazioneProvider) Class.forName(provider).getConstructor(Map.class).newInstance();
        } catch (Exception exc) {
            throw new KSRuntimeException(exc);
        }
    }

    public Documento conserva(String docnum) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {

        try {
            Documento[] documenti = this.related(docnum);

            IConservazioneProvider provider = getConservazioneProvider(documenti[0].getCodEnte(),documenti[0].getCodAoo());

            String[] responses = provider.conserva(documenti);

            String userId = Session.getUserInfoNoExc().getUsername();

            Map[] maps = new HashMap[documenti.length];

            for ( int i=0; i<maps.length; i++){
                String resp = responses[i];

                maps[i] = new HashMap();
                maps[i].put("id", documenti[i].getSolrId());

                if (resp==null) {
                    maps[i].put("STATO_CONSERV", "3");
                } else {
                    maps[i].put("STATO_CONSERV", "4");
                    maps[i].put("STATO_CONSERV_ERR_MESSAGE", resp);
                }
            }

            SolrSimpleClient client = new SolrSimpleClient();
            client.multiUpdate(userId,null,maps );

            return solrGet(docnum, Documento.TYPE);
        } catch (Exception se) {
            throw throwKSException(se);
        }
    }

    public Documento archivia(String docnum) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {

        try {
            Documento[] documenti = this.related(docnum);
            String userId = Session.getUserInfoNoExc().getUsername();

            Map[] maps = new HashMap[documenti.length];

            for ( int i=0; i<maps.length; i++){
                maps[i] = new HashMap();
                maps[i].put("id", documenti[i].getSolrId());
            }

            Map<String,Object> params = new HashMap<>();
            params.put("archive", true);

            SolrSimpleClient client = new SolrSimpleClient();
            client.multiUpdate(userId,params,maps );

            return solrGet(docnum, Documento.TYPE);
        } catch (Exception se) {
            throw throwKSException(se);
        }
    }

    public Documento pubblica(String docnum, String registro, String oggetto, String dataInizio, String dataFine) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {


        try {
            Documento[] documenti = this.related(docnum);

            IPubblicazioneProvider provider = getPubblicazioneProvider(documenti[0].getCodEnte(),documenti[0].getCodAoo());

            String numero = provider.pubblica(registro, oggetto, dataInizio, dataFine, documenti);

            Map<String, String> metadata = new LinkedHashMap<>();
            //ClientUtils.dateFormat.format(new Date())
            metadata.put(Constants.doc_pubblicazione_data_fine, dataInizio);
            metadata.put(Constants.doc_pubblicazione_data_inizio, dataFine);

            metadata.put(Constants.doc_pubblicazione_registro, registro);
            metadata.put(Constants.doc_pubblicazione_numero, numero);
            metadata.put(Constants.doc_pubblicazione_oggetto, oggetto);

            newBLDocer().pubblicaDocumento(getTicket(), docnum, metadata);
            return solrGet(docnum, Documento.TYPE);
        } catch (Exception se) {
            throw throwKSException(se);
        }
    }

    public Documento annullaPubblicazione(String docnum, String motivo, String dataAnnullamento) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {

        try {
            Documento documento = get(docnum);

            IPubblicazioneProvider provider = getPubblicazioneProvider(documento.getCodEnte(),documento.getCodAoo());

            String anno = (String) documento.otherFields().get(Constants.doc_pubblicazione_anno);
            String numero = (String) documento.otherFields().get(Constants.doc_pubblicazione_numero);
            String registro = (String) documento.otherFields().get(Constants.doc_pubblicazione_registro);

            provider.annulla(registro, numero, anno, dataAnnullamento, motivo);

            Map<String, String> metadata = new LinkedHashMap<>();

            metadata.put(Constants.doc_pubblicazione_data_annullamento, dataAnnullamento);
            metadata.put(Constants.doc_pubblicazione_motivo_annullamento, motivo);

            newBLDocer().pubblicaDocumento(getTicket(), docnum, metadata);
            return solrGet(docnum, Documento.TYPE);
        } catch (Exception se) {
            throw throwKSException(se);
        }

    }

    public String invioPEC(String docnum, String oggetto, boolean generaAnnesso, boolean allegati, boolean verificaFirme, ICorrispondente... destinatari ) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {

        try {

            String xmldest = "<Destinatari>";

            for ( ICorrispondente dest : destinatari )
                xmldest += "\n" + dest.toXml("Destinatario");

            xmldest += "\n</Destinatari>";

            Map dest = (Map) U.fromXml(xmldest);

            Map map = new LinkedHashMap(){{
                put( "Segnatura", new LinkedHashMap(){{
                    put("Intestazione" , new LinkedHashMap(){{
                        put("Oggetto" , oggetto);
                        put("Flusso" , new LinkedHashMap<>(){{
                            put("ModalitaInvio", generaAnnesso ? "D" : "S");
                            put("TipoRichiesta", allegati ? "C" : "S");
                            put("ForzaInvio", verificaFirme ? "0" : "1");
                        }});
                        put("Destinatari" , dest.get("Destinatari") );
                    }});
                }});
            }};

            String xml = U.toXml(map);

            BusinessLogic bl = new BusinessLogic();

            String jwt = Session.getUserInfoNoExc().getJwtToken();

            User user = ClientCacheAuthUtils.getInstance().getUser(jwt);
            Claims claims = ClientUtils.parseJWTTokenWithoutSecret(jwt);

            if (user==null){
                throw new AuthenticationException("user "+claims.getSubject()+" not found");
            }
            String ticket = ClientCacheAuthUtils.getInstance().getDMSTicket(user,(String) claims.get("secret"));

            ticket = String.format("ente:%s|uid:%s|documentale:%s|",user.getCodEnte(),user.getUserName(),ticket);

            String result = bl.invioPEC(ticket, Integer.parseInt(docnum), xml );

            Map res = (Map) U.fromXml(result);

            String codice = (String) U.get(res, "Esito.Codice");
            Object desc = U.get(res, "Esito.Descrizione");
            String identificativo = (String) U.get(res, "Esito.Identificativo");

            String descrizione = "";
            if (desc instanceof String)
                descrizione = (String) desc;

            switch (codice){
                case "0":
                    return identificativo;
                case "404":
                    throw new KSExceptionNotFound(descrizione);
                case "401":
                    throw new KSExceptionForbidden(descrizione);
                default:
                    throw new KSExceptionBadRequest(descrizione);
            }

        } catch (Exception e) {
            throw new KSRuntimeException(e);
        }


    }

}
