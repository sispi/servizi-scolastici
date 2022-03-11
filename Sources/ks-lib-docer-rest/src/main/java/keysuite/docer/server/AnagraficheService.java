package keysuite.docer.server;

import com.google.common.base.Strings;
import it.kdm.docer.sdk.exceptions.DocerException;
import it.kdm.orchestratore.session.Session;
import keysuite.desktop.exceptions.*;
import keysuite.docer.client.*;
import keysuite.docer.query.ISearchParams;
import keysuite.docer.query.ISearchResponse;
import org.apache.solr.common.SolrDocument;
import org.springframework.stereotype.Component;

import java.util.Map;

import static keysuite.docer.client.ClientUtils.throwKSException;

@Component
public class AnagraficheService extends BaseService implements IAnagrafiche {

    @Override
    public ISearchResponse search(ISearchParams params) throws KSExceptionBadRequest {
        params.add("fq","-type:(fascicolo documento folder titolario user group ente aoo related)");
        return solrSimpleSearch(params);
    }

    @Override
    public ISearchResponse search(String type, ISearchParams params) throws KSExceptionBadRequest {
        params.add("fq","type:"+type);
        return solrSimpleSearch(params);
    }

    @Override
    public DocerBean create(DocerBean bean) throws KSExceptionBadRequest, KSExceptionForbidden, KSExceptionNotFound {
        return create( (Anagrafica) bean);
    }

    @Override
    public DocerBean update(DocerBean bean) throws KSExceptionBadRequest, KSExceptionForbidden, KSExceptionNotFound {
        return update( (Anagrafica) bean);
    }

    @Override
    public Anagrafica create(Anagrafica anagrafica) throws KSExceptionForbidden, KSExceptionBadRequest, KSExceptionNotFound {
        try {

            if (anagrafica.getCodice()==null)
                anagrafica.setCodice("__NULL__");

            String GUID = anagrafica.getGuid();
            boolean byGUID = !Strings.isNullOrEmpty(GUID);
            if (byGUID){
                Anagrafica gd;
                if (anagrafica.getCodice()!=null && !"__NULL__".equals(anagrafica.getCodice())) {
                    gd = solrGetNoExc(anagrafica.getCodice(), anagrafica.getTypeId());
                    if (gd!=null && !GUID.equals(gd.getGuid()))
                        throw new KSRuntimeException(Code.H409,"guid not match");
                } else {
                    gd = solrGetByGUID(GUID);
                }
                if (gd != null){
                    if (Session.getResponse()!=null)
                        Session.getResponse().setStatus(200);
                    return gd;
                }
            }

            checkAcl(null,anagrafica);
            checkExtraField(anagrafica);

            newBLDocer().createAnagraficaCustom(getTicket(),ServerUtils.toDocerMap(anagrafica));

            SolrDocument result = getLastAddedSolrDocument();

            if (result!=null){
                String id =(String) result.getFieldValue("id");
                if (id!=null)
                    return solrGet(id);
            }

            if (byGUID)
                return solrGetByGUID(GUID);
            else
                return solrGet(anagrafica.getCodice(),anagrafica.getType());

        } catch (Exception e) {
            throw throwKSException(e);
        }
    }

    @Override
    public Anagrafica get(String type, String codice) throws KSExceptionNotFound {
        return solrGet(codice,type);
    }

    @Override
    public Anagrafica update(Anagrafica anagrafica) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {
        String type = anagrafica.getTypeId();
        String codice = anagrafica.getCodice();
        try {
            anagrafica.setTypeId(type);

            Anagrafica oldBean = get(type,codice);
            checkAcl(oldBean,anagrafica);
            checkExtraField(anagrafica);

            Map<String,String> map = ServerUtils.toUpdateMap(oldBean,anagrafica);
            newBLDocer().updateAnagraficaCustom(getTicket(),ServerUtils.toDocerKey(anagrafica),map);
            return solrGet(codice, type);
        } catch (DocerException e) {
            throw new KSExceptionBadRequest(e);
        } catch (Exception e ){
            throw new KSRuntimeException(e);
        }
    }

    @Override
    public void delete(String type,String codice) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {

        //controllo se ci sono anagrafiche figlie o documenti collegati

        String solrId = getSolrId(codice, type);

        ISearchResponse resp = solrSimpleSearch("q","-id:"+solrId+" (parent:"+solrId+" COD_"+type+":"+codice+")");

        if (resp.getRecordCount()>0){
            throw new KSExceptionBadRequest("ci sono elemento collegati a:"+codice);
        }

        solrDelete(codice, type);
    }

    @Override
    public HistoryItem[] getHistory(String type,String codice) throws KSExceptionNotFound {
        return get(type,codice).getHistoryItems();
    }



}
