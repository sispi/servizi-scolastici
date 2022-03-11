package keysuite.docer.sdk;

import keysuite.desktop.exceptions.KSExceptionBadRequest;
import keysuite.desktop.exceptions.KSExceptionForbidden;
import keysuite.desktop.exceptions.KSExceptionNotFound;
import keysuite.desktop.exceptions.KSRuntimeException;
import keysuite.docer.client.*;
import keysuite.docer.query.ISearchParams;
import keysuite.docer.query.ISearchResponse;

import java.util.List;
import java.util.Map;

import static keysuite.docer.sdk.APIClient.*;

public class WSTitolariClient implements ITitolari {

    final static String collectionPath = "/titolari";
    final static String itemPath = collectionPath+"/{classifica}";

    APIClient client;
    public WSTitolariClient(APIClient client){
        this.client = client;
    }

    @Override
    public Titolario create(Titolario titolario) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {

        Map res = client.execute( collectionPath+(titolario.getGuid()!=null?PUT:POST), titolario );
        return ClientUtils.toClientBean(res, Titolario.class);

    }

    @Override
    public Titolario get(String classifica, String piano) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {
        return ClientUtils.toClientBean( (Map) this.client.execute( itemPath + GET,ClientUtils.getClassWithPiano(classifica,piano)) , Titolario.class);
    }

    @Override
    public Titolario update(Titolario titolario) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {
        Map res = this.client.execute( itemPath+PATCH,ClientUtils.getClassWithPiano(titolario.getClassifica(),titolario.getPianoClass()) , titolario);
        return ClientUtils.toClientBean(res, Titolario.class);
    }

    @Override
    public void delete(String classifica, String piano) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {
        this.client.execute( itemPath + DELETE,ClientUtils.getClassWithPiano(classifica,piano));
    }

    @Override
    public HistoryItem[] getHistory(String classifica, String piano) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {
        List res = this.client.execute( itemPath + "/history",ClientUtils.getClassWithPiano(classifica,piano));
        return ClientUtils.toClientBeans(res,HistoryItem[].class);
    }

    @Override
    public String pianoClassificazione(String anno)  {

        try {
            Object piano = this.client.execute( collectionPath + "/piano",anno);
            if ("".equals(piano))
                piano = null;
            return (String) piano;

        } catch (Exception e) {
            throw new KSRuntimeException(e);
        }
    }

    @Override
    public ISearchResponse search(ISearchParams params) throws KSExceptionBadRequest, KSExceptionForbidden, KSExceptionNotFound {
        return new SearchResponse( this.client.execute( collectionPath+GET , params ) );
    }
}
