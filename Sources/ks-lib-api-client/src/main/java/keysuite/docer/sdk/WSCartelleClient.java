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

public class WSCartelleClient implements ICartelle {

    final static String collectionPath = "/cartelle";
    final static String itemPath = collectionPath + "/{folderId}";

    APIClient client;
    public WSCartelleClient(APIClient client){
        this.client = client;
    }

    @Override
    public Folder create(Folder cartella) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {
        Map res = client.execute( collectionPath+(cartella.getGuid()!=null?PUT:POST), cartella );
        return ClientUtils.toClientBean(res, Folder.class);
    }

    @Override
    public Folder get(String folderId) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {
        return ClientUtils.toClientBean( (Map) this.client.execute( itemPath + GET,folderId) , Folder.class);
    }

    @Override
    public Folder update(Folder cartella) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {
        Map res = this.client.execute( itemPath+PATCH, cartella, cartella.getFolderId() );
        return ClientUtils.toClientBean(res, Folder.class);
    }

    @Override
    public void delete(String folderId) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {
        this.client.execute( itemPath + DELETE,folderId);
    }

    @Override
    public HistoryItem[] getHistory(String folderId) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {
        List res = this.client.execute( itemPath + "/history",folderId);
        return ClientUtils.toClientBeans(res,HistoryItem[].class);
    }

    @Override
    public ISearchResponse search(ISearchParams params) throws KSExceptionBadRequest, KSExceptionForbidden, KSExceptionNotFound {
        return new SearchResponse( this.client.execute( collectionPath+GET , params ) );
    }
}
