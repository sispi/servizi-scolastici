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

public class WSFascicoliClient implements IFascicoli {

    final static String collectionPath = "/fascicoli";
    final static String itemPath = collectionPath+"/{fascicoloId}";

    APIClient client;
    public WSFascicoliClient(APIClient client){
        this.client = client;
    }

    @Override
    public Fascicolo create(Fascicolo fascicolo) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {
        Map res = this.client.execute( collectionPath+(fascicolo.getGuid()!=null?PUT:POST), fascicolo );
        return ClientUtils.toClientBean(res, Fascicolo.class);
    }

    @Override
    public Fascicolo get(String fascicoloId) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {
        return ClientUtils.toClientBean( (Map) this.client.execute( itemPath + GET,fascicoloId) , Fascicolo.class);
    }

    @Override
    public Fascicolo update(Fascicolo fascicolo) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {
        Map res = this.client.execute( itemPath+PATCH, fascicolo, fascicolo.getDocerId() );
        return ClientUtils.toClientBean(res, Fascicolo.class);
    }

    @Override
    public void delete(String fascicoloId) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {
        this.client.execute( itemPath + DELETE,fascicoloId);
    }

    @Override
    public HistoryItem[] getHistory(String fascicoloId) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {
        List res = this.client.execute( itemPath + "/history",fascicoloId);
        return ClientUtils.toClientBeans(res,HistoryItem[].class);
    }

    @Override
    public ISearchResponse search(ISearchParams params) throws KSExceptionBadRequest, KSExceptionForbidden, KSExceptionNotFound {
        return new SearchResponse( this.client.execute( collectionPath+GET , params ) );
    }
}
