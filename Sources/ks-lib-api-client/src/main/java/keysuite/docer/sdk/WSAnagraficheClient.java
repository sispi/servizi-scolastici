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

public class WSAnagraficheClient implements IAnagrafiche {

    final static String collectionPath0 = "/anagrafiche";
    final static String collectionPath = "/anagrafiche/{type}";
    final static String itemPath = collectionPath + "/{codice}";

    APIClient client;
    public WSAnagraficheClient(APIClient client){
        this.client = client;
    }

    @Override
    public Anagrafica create(Anagrafica anagrafica) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {
        Map res = client.execute( collectionPath+(anagrafica.getGuid()!=null?PUT:POST), anagrafica, anagrafica.getType() );
        return ClientUtils.toClientBean(res, Anagrafica.class);
    }

    @Override
    public Anagrafica get(String type, String codice) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {
        return ClientUtils.toClientBean( (Map) this.client.execute( itemPath + GET,type,codice) , Anagrafica.class);
    }

    @Override
    public Anagrafica update(Anagrafica anagrafica) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {
        Map res = this.client.execute( itemPath+PATCH, anagrafica, anagrafica.getCodice(), anagrafica.getTypeId() );
        return ClientUtils.toClientBean(res, Anagrafica.class);
    }

    @Override
    public void delete(String type, String codice) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {
        this.client.execute( itemPath + DELETE,codice,type);
    }

    @Override
    public HistoryItem[] getHistory(String type, String codice) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {
        List res = this.client.execute( itemPath + "/history",codice,type);
        return ClientUtils.toClientBeans(res,HistoryItem[].class);
    }

    @Override
    public ISearchResponse search(String type, ISearchParams params) throws KSExceptionBadRequest, KSExceptionForbidden, KSExceptionNotFound {
        return new SearchResponse( this.client.execute( collectionPath+GET , params, type ) );
    }

    @Override
    public ISearchResponse search(ISearchParams params) throws KSExceptionBadRequest, KSExceptionForbidden, KSExceptionNotFound {
        return new SearchResponse( this.client.execute( collectionPath0+GET , params ) );
    }
}
