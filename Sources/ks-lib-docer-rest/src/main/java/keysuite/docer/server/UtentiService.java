package keysuite.docer.server;

import com.google.common.base.Strings;
import it.kdm.docer.sdk.exceptions.DocerException;
import it.kdm.orchestratore.session.Session;
import it.kdm.orchestratore.session.UserInfo;
import keysuite.cache.ClientCache;
import keysuite.cache.ClientCacheAuthUtils;
import keysuite.desktop.exceptions.*;
import keysuite.docer.client.DocerBean;
import keysuite.docer.client.HistoryItem;
import keysuite.docer.client.IUtenti;
import keysuite.docer.client.User;
import keysuite.docer.query.ISearchParams;
import keysuite.docer.query.ISearchResponse;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class UtentiService extends BaseService implements IUtenti {

    @Override
    public void setEnvironment(Environment environment) {
        super.setEnvironment(environment);

        /*boolean autoSyncUser = environment.getProperty("docer.autosync", Boolean.class, true);

        if (autoSyncUser) {
            ClientCacheAuthUtils.setSyncHelpers(
                    (User user) -> this.create(user),
                    (User user) -> this.update(user)
            );
        }*/
    }

    @Override
    public ISearchResponse search(ISearchParams params) throws KSExceptionBadRequest {
        params.add("fq","type:user");
        return solrSimpleSearch(params);
    }
    
    @Override
    public User get(String username) throws KSExceptionNotFound {
        return solrGet(username,User.TYPE);
    }

    @Override
    public DocerBean create(DocerBean bean) throws KSExceptionBadRequest, KSExceptionForbidden, KSExceptionNotFound {
        return create( (User) bean);
    }

    @Override
    public DocerBean update(DocerBean bean) throws KSExceptionBadRequest, KSExceptionForbidden, KSExceptionNotFound {
        return update( (User) bean);
    }

    @Override
    public User create(User utente) throws KSExceptionBadRequest, KSExceptionForbidden, KSExceptionNotFound {

        String GUID = utente.getGuid();

        try {

            if (!Strings.isNullOrEmpty(GUID)){
                User usr = solrGetNoExc(utente.getUserName(),User.TYPE);
                if (usr!=null) {
                    if (!GUID.equals(usr.getGuid()))
                        throw new KSRuntimeException(Code.H409,"guid not match");
                    if (Session.getResponse()!=null)
                        Session.getResponse().setStatus(200);
                    return usr;
                }
            }

            if (!"admin".equals(utente.getUserName())){
                String codEnte = Session.getUserInfoNoExc().getCodEnte();
                String codAoo = Session.getUserInfoNoExc().getCodAoo();
                if (!utente.hasGroup(codEnte))
                    utente.addGroups(codEnte);
                if (!utente.hasGroup(codAoo))
                    utente.addGroups(codAoo);
                if (!utente.hasGroup("everyone"))
                    utente.addGroups("everyone");
            }

            checkAcl(null,utente);
            checkExtraField(utente);

            Map<String,String> map = ServerUtils.toDocerMap(utente);

            if ("admin".equals(utente.getUserName())){
                map.remove("COD_ENTE");
                map.remove("COD_AOO");
            }

            newBLDocer().createUser(getTicket(),map);
            return solrGet(utente.getUserName(),User.TYPE);

        } catch (DocerException e) {
            throw new KSExceptionBadRequest(e);
        } catch (Exception e ){
            throw new KSRuntimeException(e);
        }
    }

    @Override
    public User update(User utente) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {
        try {
            User oldBean = get(utente.getUserName());
            checkAcl(oldBean,utente);
            checkExtraField(utente);
            newBLDocer().updateUser(getTicket(), ClientCache.suffix(oldBean.getPrefix())+utente.getUserName(), ServerUtils.toUpdateMap(oldBean,utente));
            return solrGet(utente.getUserName(),User.TYPE);

        } catch (DocerException e) {
            throw new KSExceptionBadRequest(e);
        } catch (Exception e ){
            throw new KSRuntimeException(e);
        }
    }



    @Override
    public void delete(String username) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {

        //controllo se ci sono acl collegate

        String solrId = getSolrId(username, User.TYPE);
        ISearchResponse resp = solrSimpleSearch("acl_explicit:"+solrId+":*");

        if (resp.getRecordCount()>0){
            throw new KSExceptionBadRequest("ci sono elemento collegati a:"+solrId);
        }

        solrDelete(username,User.TYPE);
    }

    @Override
    public HistoryItem[] getHistory(String username) throws KSExceptionNotFound {
        return get(username).getHistoryItems();
    }

    public Map<String,Object> getOptions(String username) throws KSExceptionNotFound {
        return this.get(username).getOptions();
    }

    public User setOptions(String username, Map<String,Object> options) throws KSExceptionBadRequest, KSExceptionForbidden, KSExceptionNotFound {
        User utente = this.get(username);

        Collection<String> permissions = utente.getPermissions();

        String ticket = null;

        if (permissions.contains( "write" )){
            ticket = getTicket();
        } else {
            UserInfo ui = Session.getUserInfo();
            if (username.equals(ui.getUsername())){
                String t0 = String.format("ente:%s|uid:admin|dmsticket:admin|",utente.getCodEnte());
                ticket = ClientCacheAuthUtils.getInstance().encrypt(t0);
            } else {
                throw new KSExceptionForbidden("non autorizzato a cambiare le options");
            }
        }

        utente.setOptions(options);

        Map<String,String> docerMap = ServerUtils.toDocerMap(utente);

        Map<String,String> newoptions = new LinkedHashMap<>();

        for( String key: docerMap.keySet() ){
            if (key.startsWith(User.opt_prefix))
                newoptions.put( key, docerMap.get(key) );
        }

        //utente.setOptions(options);

        try {
            newBLDocer().updateUser(ticket,ClientCache.suffix(utente.getPrefix())+utente.getUserName(), newoptions );
            return solrGet(utente.getUserName(),User.TYPE);

        } catch (DocerException e) {
            throw new KSExceptionBadRequest(e);
        } catch (Exception e ){
            throw new KSRuntimeException(e);
        }
    }
}
