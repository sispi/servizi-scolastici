package keysuite.docer.sdk;

import keysuite.desktop.exceptions.KSExceptionBadRequest;
import keysuite.desktop.exceptions.KSExceptionForbidden;
import keysuite.desktop.exceptions.KSExceptionNotFound;
import keysuite.docer.client.*;
import keysuite.docer.query.ISearchParams;
import keysuite.docer.query.ISearchResponse;

import java.util.List;
import java.util.Map;

import static keysuite.docer.sdk.APIClient.*;
import static keysuite.docer.sdk.APIClient.POST;

public class WSUtentiClient implements IUtenti {

    final static String collectionPath = "/utenti";
    final static String itemPath = collectionPath + "/{username}";

    APIClient client;
    public WSUtentiClient(APIClient client){
        this.client = client;
    }

    @Override
    public User create(User utente) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {
        Map res = client.execute( collectionPath+(utente.getGuid()!=null?PUT:POST), utente );
        return ClientUtils.toClientBean(res, User.class);
    }

    @Override
    public User get(String username) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {
        return ClientUtils.toClientBean( (Map) this.client.execute( itemPath + GET,username) , User.class);
    }

    @Override
    public User update(User utente) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {
        Map res = this.client.execute( itemPath+PATCH, utente.getUserName(),utente );
        return ClientUtils.toClientBean(res, User.class);
    }

    @Override
    public void delete(String username) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {
        this.client.execute( itemPath + DELETE,username);
    }

    @Override
    public HistoryItem[] getHistory(String username) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {
        List res = this.client.execute( itemPath + "/history",username);
        return ClientUtils.toClientBeans(res,HistoryItem[].class);
    }

    @Override
    public ISearchResponse search(ISearchParams params) throws KSExceptionBadRequest, KSExceptionForbidden, KSExceptionNotFound {
        return new SearchResponse( this.client.execute( collectionPath+GET , params ) );
    }
}
