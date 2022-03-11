package keysuite.docer.sdk;

import com.fasterxml.jackson.core.JsonProcessingException;
import keysuite.desktop.exceptions.KSExceptionBadRequest;
import keysuite.desktop.exceptions.KSExceptionForbidden;
import keysuite.desktop.exceptions.KSExceptionNotFound;
import keysuite.desktop.exceptions.KSRuntimeException;
import keysuite.docer.client.*;
import keysuite.docer.client.corrispondenti.ICorrispondente;
import keysuite.docer.query.ISearchParams;
import keysuite.docer.query.ISearchResponse;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static keysuite.docer.sdk.APIClient.*;

public class WSDocumentiClient implements IDocumenti {

    final static String collectionPath = "/documenti";
    final static String itemPath = collectionPath + "/{docnum}";

    APIClient client;
    public WSDocumentiClient(APIClient client){
        this.client = client;
    }

    @Override
    public Documento create(Documento documento) throws KSExceptionBadRequest, KSExceptionForbidden, KSExceptionNotFound {
        Documento[] docs = createMulti(false,documento);
        if (docs.length>0)
            return docs[0];
        else
            return null;
    }

    @Override
    public Documento[] createMulti(boolean relate, Documento... documenti) throws KSExceptionBadRequest, KSExceptionForbidden, KSExceptionNotFound {

        List files = new ArrayList<>();

        boolean idem = false;

        if (documenti.length==1){
            Documento doc = documenti[0];
            if (doc.getGuid()!=null && (!relate||doc.getDocnum()==null))
                idem = true;

            Map res = this.client.execute( collectionPath + (idem?PUT:POST), documenti[0]);

            Documento d =  ClientUtils.toClientBean(res, Documento.class);
            return new Documento[] {d};
        } else {
            for( Documento doc : documenti){

                if (doc.getGuid()!=null && (!relate||doc.getDocnum()==null))
                    idem = true;

                files.add(doc);

                if (doc.getStream()!=null){
                    files.add(doc.getStream());
                    doc.setStream(null);
                }
            }

            List res = this.client.execute( collectionPath + "/multipart" + (idem?PUT:POST), files, relate);

            return ClientUtils.toClientBeans(res, Documento[].class);
        }




    }

    @Override
    public Documento get(String docnum) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {
        return ClientUtils.toClientBean( (Map) this.client.execute( itemPath + GET,docnum) , Documento.class);
    }

    @Override
    public Documento update(Documento documento) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {
        return ClientUtils.toClientBean( (Map) this.client.execute( itemPath + PATCH, documento.getDocnum(), documento ) , Documento.class);
    }

    @Override
    public void delete(String docnum) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {
        this.client.execute( itemPath + DELETE,docnum);
    }

    @Override
    public Documento[] related(String docnum, String... tipo) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {
        List<Documento> rels = this.client.execute( itemPath + "/related" + GET,docnum,tipo);
        return ClientUtils.toClientBeans(rels,Documento[].class);
    }

    @Override
    public Documento[] getAdvancedVersions(String docnum) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {
        List<Documento> rels = this.client.execute( itemPath + "/advanced" + GET,docnum);
        return ClientUtils.toClientBeans(rels,Documento[].class);
    }

    @Override
    public void relate(String docnum, String... related) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {
        this.client.execute( itemPath + "/related" + POST,docnum,related);
    }

    @Override
    public void addAdvancedVersion(String docnum, String version) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {
        this.client.execute( itemPath + "/advanced" + POST,docnum,version);
    }

    @Override
    public void unrelate(String docnum, String... related) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {
        this.client.execute( itemPath + "/related" + DELETE,docnum,related);
    }

    @Override
    public InputStream download(String docnum, Integer idx, String Range) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {
        InputStream stream = client.execute( itemPath+"/file"+GET, Range, docnum,idx);
        return stream;
    }

    @Override
    public Documento.Version[] versions(String docnum) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {
        List<Map> maps = client.execute( itemPath+"/versions"+GET, docnum);

        try {
            Documento.Version[] versions = ClientUtils.OM.readValue(ClientUtils.OM.writeValueAsString(maps), Documento.Version[].class);
            return versions;
        } catch (JsonProcessingException e) {
            throw new KSRuntimeException(e);
        }



    }

    @Override
    public Documento upload(String docnum, InputStream file, boolean replace) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {
        if (replace)
            return ClientUtils.toClientBean(client.execute( itemPath+"/file"+PUT, docnum,file),Documento.class);
        else
            return ClientUtils.toClientBean(client.execute( itemPath+"/file"+POST, docnum,file),Documento.class);
    }

    @Override
    public Documento upload(String docnum, URL url, boolean replace) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {
        if (replace)
            return ClientUtils.toClientBean(client.execute( itemPath+"/fileUrl"+PUT, docnum,url),Documento.class);
        else
            return ClientUtils.toClientBean(client.execute( itemPath+"/fileUrl"+POST, docnum,url),Documento.class);
    }

    public Documento upload(String docnum, String url, boolean replace) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {
        return upload(docnum, new Documento.StringURL().convert(url), replace );
    }

    @Override
    public Documento lock(String docnum, String expiration) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {
        Map res = this.client.execute( itemPath + "/lock", docnum, expiration);
        return ClientUtils.toClientBean(res, Documento.class);
    }

    @Override
    public Documento unlock(String docnum) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {
        Map res = this.client.execute( itemPath + "/unlock", docnum);
        return ClientUtils.toClientBean(res, Documento.class);
    }

    @Override
    public Documento classifica(String docnum, String classifica, String piano, String... secondarie) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {
        Map res = this.client.execute( itemPath + "/classifica", classifica, docnum , piano, secondarie);
        return ClientUtils.toClientBean(res, Documento.class);
    }

    @Override
    public Documento fascicola(String docnum, String primario, String... secondari) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {
        Map res = this.client.execute( itemPath + "/fascicola", docnum, primario, secondari);
        return ClientUtils.toClientBean(res, Documento.class);
    }

    @Override
    public Documento protocolla(String docnum, String verso, String oggetto, ICorrispondente mittente, ICorrispondente... destinatari) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {

        ICorrispondente[] corrispondenti = new ICorrispondente[ 1 + (destinatari!=null?destinatari.length:0) ];
        corrispondenti[0] = mittente;
        for( int i=1; i<corrispondenti.length; i++){
            corrispondenti[i] = destinatari[i-1];
        }
        Map res = this.client.execute( itemPath + "/protocolla",corrispondenti, docnum, oggetto, verso);
        return ClientUtils.toClientBean(res, Documento.class);

    }

    @Override
    public Documento annullaProtocollazione(String docnum, String motivazione, String riferimento) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {
        return ClientUtils.toClientBean(this.client.execute( itemPath + "/annullaProtocollazione",docnum, motivazione, riferimento), Documento.class);
    }

    @Override
    public Documento registra(String docnum, String registro, String oggetto) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {
        Map res = this.client.execute( itemPath + "/registra",docnum, oggetto, registro);
        return ClientUtils.toClientBean(res, Documento.class);
    }

    @Override
    public Documento annullaRegistrazione(String docnum, String motivazione, String riferimento) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {
        return ClientUtils.toClientBean(this.client.execute( itemPath + "/annullaRegistrazione",docnum, motivazione, riferimento), Documento.class);
    }

    @Override
    public Map<String,String> registri() {

        try {
            return this.client.execute( collectionPath + "/registri");
        } catch (Exception e) {
            throw new KSRuntimeException(e);
        }
    }

    @Override
    public Map<String, String> tipologie(String tipo) {
        try {
            return this.client.execute( collectionPath + "/tipologie",tipo);
        } catch (Exception e) {
            throw new KSRuntimeException(e);
        }
    }

    @Override
    public HistoryItem[] getHistory(String docnum) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {
        List res = this.client.execute( itemPath + "/history",docnum);
        return ClientUtils.toClientBeans(res,HistoryItem[].class);
    }

    @Override
    public String invioPEC(String docnum, String oggetto, boolean generaAnnesso, boolean allegati, boolean verificaFirme, ICorrispondente... destinatari ) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {
        String res = this.client.execute( itemPath + "/invioPEC", allegati, destinatari, docnum, generaAnnesso, oggetto, verificaFirme);
        return res;
    }

    @Override
    public Documento archivia(String docnum) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {
        Documento res = this.client.execute( itemPath + "/archivia", docnum);
        return res;
    }

    @Override
    public Documento conserva(String docnum) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {
        Documento res = this.client.execute( itemPath + "/conserva", docnum);
        return res;
    }

    @Override
    public Documento pubblica(String docnum, String registro, String oggetto, String dataInizio, String dataFine) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {
        Documento res = this.client.execute( itemPath + "/pubblica", dataFine, dataInizio, docnum, oggetto);
        return res;
    }

    @Override
    public Documento annullaPubblicazione(String docnum, String motivazione, String dataAnnullamento) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {
        Documento res = this.client.execute( itemPath + "/annullaPubblicazione",dataAnnullamento, docnum, motivazione);
        return res;
    }

    @Override
    public ISearchResponse<Documento> search(ISearchParams params) throws KSExceptionBadRequest, KSExceptionForbidden, KSExceptionNotFound {
        Map map = this.client.execute( collectionPath+GET , params );
        return new SearchResponse( map );
    }
}
