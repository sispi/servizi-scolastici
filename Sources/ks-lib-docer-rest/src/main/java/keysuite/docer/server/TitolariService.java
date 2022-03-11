package keysuite.docer.server;

import com.google.common.base.Strings;
import it.kdm.docer.sdk.exceptions.DocerException;
import it.kdm.orchestratore.session.Session;
import keysuite.desktop.exceptions.*;
import keysuite.docer.client.DocerBean;
import keysuite.docer.client.HistoryItem;
import keysuite.docer.client.ITitolari;
import keysuite.docer.client.Titolario;
import keysuite.docer.query.ISearchParams;
import keysuite.docer.query.ISearchResponse;
import org.springframework.stereotype.Component;

import java.util.Map;

import static keysuite.docer.client.ClientUtils.getClassWithPiano;
import static keysuite.docer.client.ClientUtils.throwKSException;
import static keysuite.docer.server.ServerUtils.toDocerMap;

//import keysuite.docer.providers.SolrBaseUtil;

@Component
public class TitolariService extends BaseService implements ITitolari {

    //Map<String,String> isPianoClass = new HashMap<>();

    @Override
    public ISearchResponse search(ISearchParams params) throws KSExceptionBadRequest {
        params.add("fq","type:titolario");
        return solrSimpleSearch(params);
    }

    @Override
    public DocerBean create(DocerBean bean) throws KSExceptionBadRequest, KSExceptionForbidden, KSExceptionNotFound {
        return create( (Titolario) bean);
    }

    @Override
    public DocerBean update(DocerBean bean) throws KSExceptionBadRequest, KSExceptionForbidden, KSExceptionNotFound {
        return update( (Titolario) bean);
    }

    @Override
    public Titolario create(Titolario titolario) throws KSExceptionBadRequest, KSExceptionForbidden, KSExceptionNotFound {

        String GUID = titolario.getGuid();

        try{

            if (!Strings.isNullOrEmpty(GUID)){
                Titolario tit = solrGetNoExc(getClassWithPiano(titolario),Titolario.TYPE);
                if (tit!=null){
                    if (!GUID.equals(tit.getGuid()))
                        throw new KSRuntimeException(Code.H409,"guid not match");
                    if (Session.getResponse()!=null)
                        Session.getResponse().setStatus(200);
                    return tit;
                }
            }
            checkAcl(null,titolario);
            checkExtraField(titolario);
            /*String aoo = Session.getUserInfoNoExc().getCodAoo();
            String ente = Session.getUserInfoNoExc().getCodEnte();
            String piano = isPianoClass.get(aoo);

            if (piano==null){
                String xml = BLDocer.readConfig(getTicket());
                Map<String,Object> map = (Map) U.fromXml(xml);
                Object piano_class_default = U.get(map,"configuration.group[0].section.piano_class_default");
                List<Map> l = piano_class_default instanceof List ? (List<Map>) piano_class_default : Collections.singletonList( (Map) piano_class_default);
                piano = "";
                for ( Map p : l )
                    if (p!=null &&
                            (ente.equals(p.get("-ente")) || ("*".equals(p.get("-ente"))) ) &&
                            (aoo.equals(p.get("-aoo")) || ("*".equals(p.get("-aoo"))) ) ){
                        if ("true".equals(p.get("-active"))){
                            piano = (String) p.getOrDefault("-value","");
                        }
                    }
                isPianoClass.put(aoo,piano);
            }

            String PIANO_CLASS = titolario.getPianoClass();

            if (!Strings.isNullOrEmpty(PIANO_CLASS) && Strings.isNullOrEmpty(piano)){
                String CLASSIFICA = titolario.getClassifica();
                Map<String,Object> extra = new LinkedHashMap<>();
                titolario.setPianoClass(null);
                titolario.setClassifica(titolario.getClassifica()+"$"+PIANO_CLASS);
                extra.put("PIANO_CLASS",PIANO_CLASS);
                extra.put("CLASSIFICA",CLASSIFICA);
                extra.put("COD_TITOLARIO",CLASSIFICA);
                SolrBaseUtil.extraFields.set(extra);
            }*/

            String pianoClass = newBLDocer().createTitolario( getTicket(), toDocerMap(titolario));

            /*if (Strings.isNullOrEmpty(PIANO_CLASS) && !Strings.isNullOrEmpty(piano)){
                if ("*".equals(piano))
                    PIANO_CLASS = "" + new DateTime(DateTimeZone.UTC).getYear();
                else
                    PIANO_CLASS = piano;
                titolario.setPianoClass(PIANO_CLASS);
            }*/

            if (!Strings.isNullOrEmpty(pianoClass))
                titolario.setPianoClass(pianoClass);

            return solrGet(getClassWithPiano(titolario),Titolario.TYPE);
        } catch (Exception de){
            throw throwKSException(de);
        }
    }

    @Override
    public Titolario get(String classifica, String piano)  throws KSExceptionNotFound {
        return solrGet(getClassWithPiano(classifica,piano), Titolario.TYPE );
    }

    @Override
    public Titolario update(Titolario titolario) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {
        try {
            Titolario oldBean = get(titolario.getClassifica(),titolario.getPianoClass());
            checkAcl(oldBean,titolario);
            checkExtraField(titolario);
            Map<String,String> docerMap = ServerUtils.toUpdateMap(oldBean,titolario);
            Map<String,String> keyMap = ServerUtils.toDocerKey(titolario);
            newBLDocer().updateTitolario(getTicket(),keyMap,docerMap);
            return solrGet(getClassWithPiano(titolario),Titolario.TYPE);
        } catch (DocerException e) {
            throw new KSExceptionBadRequest(e);
        } catch (Exception e ){
            throw new KSRuntimeException(e);
        }
    }

    @Override
    public void delete(String classifica, String piano) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {
        //controllo se ci sono figli o documenti classificati

        String solrId = getSolrId(getClassWithPiano(classifica,piano) , Titolario.TYPE);
        ISearchResponse resp = solrSimpleSearch("-id:"+solrId+" parent:"+solrId+" CLASSIFICA:"+classifica);

        if (resp.getRecordCount()>0){
            throw new KSExceptionBadRequest("ci sono elemento collegati a:"+solrId);
        }

        solrDelete(getClassWithPiano(classifica,piano),Titolario.TYPE);
    }

    @Override
    public HistoryItem[] getHistory(String classifica,String piano) throws KSExceptionNotFound {
        return get(classifica,piano).getHistoryItems();
    }

    @Override
    public String pianoClassificazione(String anno) {
        return getPianoClassAnno(Session.getUserInfoNoExc().getCodAoo(), anno);
    }


}
