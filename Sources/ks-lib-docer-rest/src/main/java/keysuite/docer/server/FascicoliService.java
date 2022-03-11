package keysuite.docer.server;

import com.github.underscore.lodash.U;
import com.google.common.base.Strings;
import it.kdm.orchestratore.session.Session;
import keysuite.desktop.exceptions.*;
import keysuite.docer.client.DocerBean;
import keysuite.docer.client.Fascicolo;
import keysuite.docer.client.HistoryItem;
import keysuite.docer.client.IFascicoli;
import keysuite.docer.query.ISearchParams;
import keysuite.docer.query.ISearchResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.time.Year;
import java.util.Map;

import static keysuite.docer.client.ClientUtils.throwKSException;

@Component
public class FascicoliService extends BaseService implements IFascicoli {



    @Override
    public ISearchResponse search(ISearchParams params) throws KSExceptionBadRequest {
        params.add("fq","type:fascicolo");
        return solrSimpleSearch(params);
    }

    @Override
    public Fascicolo get(String fascicoloId) throws KSExceptionNotFound {
        return solrGet(fascicoloId,Fascicolo.TYPE);
    }

    @Override
    public DocerBean create(DocerBean bean) throws KSExceptionBadRequest, KSExceptionForbidden, KSExceptionNotFound {
        return create( (Fascicolo) bean);
    }

    @Override
    public DocerBean update(DocerBean bean) throws KSExceptionBadRequest, KSExceptionForbidden, KSExceptionNotFound {
        return update( (Fascicolo) bean);
    }

    @Override
    public Fascicolo create(Fascicolo fascicolo) throws KSExceptionBadRequest, KSExceptionForbidden, KSExceptionNotFound {

        try{
            String classifica = fascicolo.getClassifica();

            if (Strings.isNullOrEmpty(classifica)){
                throw new KSExceptionBadRequest("classifica obbligatoria");
            }

            String GUID = fascicolo.getGuid();
            String anno = fascicolo.getAnno();

            if (Strings.isNullOrEmpty(anno)){
                anno = ""+ Year.now().getValue();
                fascicolo.setAnno(anno);
            }

            String fascicoloId = fascicolo.getDocerId();

            if (!Strings.isNullOrEmpty(GUID)){
                Fascicolo gd;
                if (fascicoloId!=null){
                    gd = solrGetNoExc(fascicoloId,Fascicolo.TYPE);
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

            checkAcl(null,fascicolo);
            checkExtraField(fascicolo);

            String[] acls = fascicolo.getAcls();

            fascicolo.setAcls(null);
            Map<String,String> fasc = ServerUtils.toDocerMap(fascicolo);

            if (acls!=null)
                fasc.put("ACL_EXPLICIT_SS", StringUtils.join(acls,","));

            String xml = wsFascicolazioneEndpoint.creaFascicolo(getToken(),fasc,fascicoloId!=null);

            Map jm = (Map) U.fromXml(xml);

            String PROGR_FASCICOLO = (String) U.get(jm,"esito.esito_fascicolo.PROGR_FASCICOLO");

            fascicolo.setProgrFascicolo(PROGR_FASCICOLO);

            /*if (isFascicolazioneInterna()){

                //String PROGR_FASCICOLO = null;
                if (fascicoloId==null){

                    Fascicolazione provider = new Fascicolazione();

                    String CLASSIFICA = fascicolo.getClassifica();
                    String ANNO_FASCICOLO = fascicolo.getAnno();
                    String PARENT_PROGR_FASCICOLO = fascicolo.getParentProgrFascicolo();

                    UserInfo ui = Session.getUserInfoNoExc();

                    Integer next = provider.staccaNumero(ui.getCodEnte(),ui.getCodAoo(),CLASSIFICA,ANNO_FASCICOLO,PARENT_PROGR_FASCICOLO,false);

                    String PROGR_FASCICOLO = ""+next;

                    if (!Strings.isNullOrEmpty(PARENT_PROGR_FASCICOLO))
                        PROGR_FASCICOLO = PARENT_PROGR_FASCICOLO + "/" + PARENT_PROGR_FASCICOLO;

                    fascicolo.setProgrFascicolo(PROGR_FASCICOLO);
                }

                BLDocer.createFascicolo(getTicket(),ServerUtils.toDocerMap(fascicolo));

                //BLDocer.createFascicolo( getTicket(), ServerUtils.toDocerMap(fascicolo));
            } else {

                String[] acls = fascicolo.getAcls();

                fascicolo.setAcls(null);

                //Map<String,Object> res = null;

                //if (fascicolo.getAcls()!=null)
                //    throw new KSExceptionBadRequest("non è possibile impostare acl in creazione con fascicolazione esterna");

                Map<String,String> fasc = ServerUtils.toDocerMap(fascicolo);

                if (acls!=null){
                    fasc.put("ACL_EXPLICIT_SS", StringUtils.join(acls,","));
                    //ServerUtils.docerAclMap(ServerUtils.aclMap(acls));
                }

                String xml = wsFascicolazioneEndpoint.creaFascicolo(getToken(),fasc,fascicoloId!=null);

                Map jm = (Map) U.fromXml(xml);

                String PROGR_FASCICOLO = (String) U.get(jm,"esito.esito_fascicolo.PROGR_FASCICOLO");

                //String PROGR_FASCICOLO = jm.get("esito","esito_fascicolo","PROGR_FASCICOLO");

                fascicolo.setProgrFascicolo(PROGR_FASCICOLO);


            }*/
            return get(fascicolo.getDocerId());
        } catch (Exception de){
            throw throwKSException(de);
        }
    }

    @Override
    public Fascicolo update(Fascicolo fascicolo) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {

        try{
            Fascicolo oldBean = get(fascicolo.getDocerId());
            checkAcl(oldBean,fascicolo);
            checkExtraField(fascicolo);

            Map<String,String> map = ServerUtils.toUpdateMap(oldBean,fascicolo);

            if (map.size()>0){
                wsFascicolazioneEndpoint.updateFascicolo(getToken(),ServerUtils.toDocerKey(fascicolo),map);
                /*if (isFascicolazioneInterna()){
                    BLDocer.updateFascicolo(getTicket(),ServerUtils.toDocerKey(fascicolo),map);
                } else {
                    //WSFascicolazioneClient.execute("#updateFascicolo", getToken(), ServerUtils.toDocerKey(fascicolo),map);
                    wsFascicolazioneEndpoint.updateFascicolo(getToken(),ServerUtils.toDocerKey(fascicolo),map);
                }*/
            }

            return get(fascicolo.getDocerId());

        } catch (Exception e) {
            throw throwKSException(e);
        }

    }

    @Override
    public void delete(String fascicoloId) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {

        //controllo se ci sono fascicoli figli o documenti fascicolati

        String solrId = getSolrId(fascicoloId,Fascicolo.TYPE);
        ISearchResponse resp = solrSimpleSearch("parent:"+solrId);

        if (resp.getRecordCount()>0){
            throw new KSExceptionBadRequest("ci sono elemento collegati a:"+fascicoloId);
        }

        solrDelete(fascicoloId, Fascicolo.TYPE);
    }

    @Override
    public HistoryItem[] getHistory(String fascicoloId) throws KSExceptionNotFound {
        return get(fascicoloId).getHistoryItems();
    }
}
