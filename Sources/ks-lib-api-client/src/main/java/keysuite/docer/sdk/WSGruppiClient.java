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

public class WSGruppiClient implements IGruppi {

    final static String collectionPath = "/gruppi";
    final static String itemPath = collectionPath + "/{groupId}";

    APIClient client;
    public WSGruppiClient(APIClient client){
        this.client = client;
    }

    @Override
    public Group create(Group gruppo) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {
        Map res = client.execute( collectionPath+(gruppo.getGuid()!=null?PUT:POST), gruppo );
        return ClientUtils.toClientBean(res, Group.class);
    }

    @Override
    public Group get(String groupId) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {
        return ClientUtils.toClientBean( (Map) this.client.execute( itemPath + GET,groupId) , Group.class);
    }

    @Override
    public Group update(Group gruppo) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {
        Map res = this.client.execute( itemPath+PATCH, gruppo.getGroupId(),gruppo );
        return ClientUtils.toClientBean(res, Group.class);
    }

    @Override
    public void delete(String groupId) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {
        this.client.execute( itemPath + DELETE,groupId);
    }

    @Override
    public HistoryItem[] getHistory(String groupId) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {
        List res = this.client.execute( itemPath + "/history",groupId);
        return ClientUtils.toClientBeans(res,HistoryItem[].class);
    }

    @Override
    public ISearchResponse search(ISearchParams params) throws KSExceptionBadRequest, KSExceptionForbidden, KSExceptionNotFound {
        return new SearchResponse( this.client.execute( collectionPath+GET , params ) );
    }
}
