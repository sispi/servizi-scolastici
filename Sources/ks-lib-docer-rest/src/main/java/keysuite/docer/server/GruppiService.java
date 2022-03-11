package keysuite.docer.server;

import com.google.common.base.Strings;
import it.kdm.docer.providers.solr.Provider;
import it.kdm.docer.providers.solr.SolrBaseUtil;
import it.kdm.docer.sdk.classes.LoggedUserInfo;
import it.kdm.docer.sdk.exceptions.DocerException;
import it.kdm.docer.sdk.interfaces.ILoggedUserInfo;
import it.kdm.orchestratore.session.Session;
import keysuite.cache.ClientCache;
import keysuite.desktop.exceptions.*;
import keysuite.docer.client.DocerBean;
import keysuite.docer.client.Group;
import keysuite.docer.client.HistoryItem;
import keysuite.docer.client.IGruppi;
import keysuite.docer.query.ISearchParams;
import keysuite.docer.query.ISearchResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class GruppiService extends BaseService implements IGruppi {

    @Override
    public ISearchResponse search(ISearchParams params) throws KSExceptionBadRequest {
        params.add("fq","type:group");
        return solrSimpleSearch(params);
    }

    @Override
    public Group get(String groupId) throws KSExceptionNotFound {
        String t = Group.TYPE;
        if (groupId.contains("@")){
            t = groupId.split("@")[1];
            groupId = groupId.split("@")[0];
        }
        return solrGet(groupId,t);
    }

    @Override
    public DocerBean create(DocerBean bean) throws KSExceptionBadRequest, KSExceptionForbidden, KSExceptionNotFound {
        return create( (Group) bean);
    }

    @Override
    public DocerBean update(DocerBean bean) throws KSExceptionBadRequest, KSExceptionForbidden, KSExceptionNotFound {
        return update( (Group) bean);
    }

    public void location() throws Exception{
        Provider provider = new Provider();

        ILoggedUserInfo info = new LoggedUserInfo("admin","system","admin");

        SolrInputDocument doc = new SolrInputDocument();

        doc.setField("name","DOCAREA");

        provider.solrExecuteUpdate(info,"DOCAREA!@location",doc, SolrBaseUtil.SolrExecuteAction.INSERT,null,null);
    }

    @Override
    public Group create(Group gruppo) throws KSExceptionBadRequest, KSExceptionForbidden, KSExceptionNotFound {

        String GUID = gruppo.getGuid();

        try {

            if (!Strings.isNullOrEmpty(GUID)){
                Group grp = solrGetNoExc(gruppo.getGroupId(),Group.TYPE);
                if (grp!=null) {
                    if (!GUID.equals(grp.getGuid()))
                        throw new KSRuntimeException(Code.H409,"guid not match");
                    if (Session.getResponse()!=null)
                        Session.getResponse().setStatus(200);
                    return grp;
                }
            }

            checkAcl(null,gruppo);
            checkExtraField(gruppo);

            if (Group.TYPE_ENTE.equals(gruppo.getType())) {
                Map<String,String> profile = new LinkedHashMap<>();
                if (!Strings.isNullOrEmpty(gruppo.getGroupId()))
                    profile.put("COD_ENTE",gruppo.getGroupId());
                else
                    profile.put("COD_ENTE",gruppo.getCodEnte());
                profile.put("DES_ENTE",gruppo.getGroupName());

                if (Strings.isNullOrEmpty(gruppo.getPrefix())){
                    profile.put("PREFIX","");
                } else {
                    profile.put("PREFIX",gruppo.getPrefix());
                }

                //profile.put("PREFIX", Strings.isNullOrEmpty(gruppo.getPrefix()) ? "false" : "true" );

                newBLDocer().createEnte(getTicket(), profile);
            }
            else if (Group.TYPE_AOO.equals(gruppo.getType())) {
                Map<String, String> profile = new LinkedHashMap<>();
                profile.put("COD_ENTE", gruppo.getCodEnte());
                if (!Strings.isNullOrEmpty(gruppo.getGroupId()))
                    profile.put("COD_AOO",gruppo.getGroupId());
                else
                    profile.put("COD_AOO",gruppo.getCodAoo());
                profile.put("DES_AOO", gruppo.getGroupName());

                newBLDocer().createAOO(getTicket(), profile );

                String id = String.format("%s.%s!%s!@aoo", Session.getSede(),gruppo.getCodEnte(),profile.get("COD_AOO"));

                return solrGet(id,gruppo.getType());

            } else {

                //se non impostato il padre è per default l'aoo
                String PARENT_GROUP_ID = gruppo.getParentGroupId();
                if (PARENT_GROUP_ID==null){
                    String aoo = gruppo.getCodAoo();
                    if (aoo==null)
                        PARENT_GROUP_ID = Session.getUserInfoNoExc().getCodAoo();
                    else
                        PARENT_GROUP_ID = aoo;
                    gruppo.setParentGroupId(PARENT_GROUP_ID);
                }

                Map<String,String> map = ServerUtils.toDocerMap(gruppo);

                map.remove("COD_AOO");
                if ("everyone".equals(gruppo.getGroupId()) || "admins".equals(gruppo.getGroupId())){
                    map.remove("COD_ENTE");
                    map.remove("PARENT_GROUP_ID");
                } else {
                    Group parent = ClientCache.getInstance().getGroup(Session.getUserInfoNoExc().getCodEnte(), PARENT_GROUP_ID);

                    if (parent==null)
                        throw new KSExceptionBadRequest("PARENT_GROUP_ID "+ PARENT_GROUP_ID + " non esiste");

                    //un padre di un gruppo struttura deve essere sempre struttura (aoo o ufficio)
                    if (gruppo.isStruttura() && (!parent.isStruttura() || parent.isEnte()))
                        throw new KSExceptionBadRequest("il padre di un gruppo struttura può essere l'aoo o un altro gruppo struttura");

                    //sotto gruppi e sotto ruoli
                    if (!parent.isAOO() && !parent.isEnte()){

                        String prefix = parent.getGroupId()+ServerUtils.UP_SEP;

                        if (!gruppo.getGroupId().startsWith(prefix))
                            throw new KSExceptionBadRequest("un sotto gruppo o sotto ruolo deve iniziare col codice del padre");

                        String suffix = gruppo.getGroupId().substring(prefix.length());

                        if (suffix.contains(ServerUtils.UP_SEP))
                            throw new KSExceptionBadRequest("un sotto gruppo o sotto ruolo non deve contenere il carattere "+ServerUtils.UP_SEP);
                    }

                    //se sono configurati i suffissi...
                    /*String group_roles = System.getProperty(ServerUtils.RUOLI_GRUPPO);

                    if (!Strings.isNullOrEmpty(group_roles)){
                        boolean endsWith = StringUtils.endsWithAny(gruppo.getGroupId(), group_roles.split(","));

                        if (gruppo.isStruttura() && endsWith)
                            throw new KSExceptionBadRequest("un gruppo struttura non deve finire per uno dei suffissi:" + group_roles);

                        if (!gruppo.isStruttura() && !endsWith && !parent.isAOO() && !parent.isEnte())
                            throw new KSExceptionBadRequest("un sotto ruolo deve finire per uno dei suffissi:" + group_roles);
                    }*/
                }

                newBLDocer().createGroup(getTicket(),map );
            }

            return solrGet(gruppo.getGroupId(),gruppo.getType());

        } catch (DocerException e) {
            throw new KSExceptionBadRequest(e);
        } catch (Exception e ){
            throw new KSRuntimeException(e);
        }
    }

    @Override
    public Group update(Group gruppo) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {
        String groupId = gruppo.getGroupId();
        try {
            Group oldBean = solrGet(groupId,gruppo.getType());
            checkAcl(oldBean,gruppo);
            checkExtraField(gruppo);

            Map<String,String> uMap = ServerUtils.toUpdateMap(oldBean, gruppo);

            if (Group.TYPE_ENTE.equals(gruppo.getType())) {
                String GROUP_NAME = uMap.remove("GROUP_NAME");
                uMap.remove("COD_ENTE");
                if (GROUP_NAME!=null)
                    uMap.put("DES_ENTE",GROUP_NAME);
                newBLDocer().updateEnte(getTicket(), groupId, uMap );
            } else if (Group.TYPE_AOO.equals(gruppo.getType())) {
                String GROUP_NAME = uMap.remove("GROUP_NAME");
                uMap.remove("COD_ENTE");
                uMap.remove("COD_AOO");
                if (GROUP_NAME!=null)
                    uMap.put("DES_AOO",GROUP_NAME);
                newBLDocer().updateAOO(getTicket(), ServerUtils.toDocerKey(gruppo), uMap );
            } else {
                if ("everyone".equals(gruppo.getGroupId()) || "admins".equals(gruppo.getGroupId())){
                    uMap.remove("COD_ENTE");
                    uMap.remove("COD_AOO");
                    newBLDocer().updateGroup(getTicket(), groupId, uMap);
                } else {
                    newBLDocer().updateGroup(getTicket(),  ClientCache.suffix(oldBean.getPrefix()) + groupId, uMap);
                }
            }
            return solrGet(groupId,gruppo.getType());
        } catch (DocerException e) {
            throw new KSExceptionBadRequest(e);
        } catch (Exception e ){
            throw new KSRuntimeException(e);
        }
    }

    @Override
    public void delete(String groupId) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {

        //controllo se ci sono gruppi figli oppure acl collegate

        String solrId = getSolrId(groupId, Group.TYPE);
        ISearchResponse resp = solrSimpleSearch("parent:"+solrId+ " groups:" + solrId + " acl_explicit:"+solrId+":*");

        if (resp.getRecordCount()>0){
            throw new KSExceptionBadRequest("ci sono elemento collegati a:"+solrId);
        }

        solrDelete(groupId,Group.TYPE);
    }

    @Override
    public HistoryItem[] getHistory(String groupId) throws KSExceptionNotFound {
        return get(groupId).getHistoryItems();
    }

}
