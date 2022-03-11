package keysuite.docer.server;

import com.google.common.base.Strings;
import it.kdm.docer.sdk.exceptions.DocerException;
import it.kdm.orchestratore.session.Session;
import keysuite.desktop.exceptions.*;
import keysuite.docer.client.*;
import keysuite.docer.query.ISearchParams;
import keysuite.docer.query.ISearchResponse;
import org.springframework.stereotype.Component;

@Component
public class CartelleService extends BaseService implements ICartelle {

    @Override
    public ISearchResponse search(ISearchParams params) throws KSExceptionBadRequest {
        params.add("fq","type:folder");
        return solrSimpleSearch(params);
    }

    @Override
    public DocerBean create(DocerBean bean) throws KSExceptionBadRequest, KSExceptionForbidden, KSExceptionNotFound {
        return create( (Folder) bean);
    }

    @Override
    public DocerBean update(DocerBean bean) throws KSExceptionBadRequest, KSExceptionForbidden, KSExceptionNotFound {
        return update( (Folder) bean);
    }

    @Override
    public Folder create(Folder cartella) throws KSExceptionBadRequest, KSExceptionForbidden, KSExceptionNotFound {
        try {

            String GUID = cartella.getGuid();
            if (!Strings.isNullOrEmpty(GUID)){
                Folder gd = solrGetByGUID(GUID);
                if (gd != null){
                    if (Session.getResponse()!=null)
                        Session.getResponse().setStatus(200);
                    return gd;
                }
            }

            checkAcl(null,cartella);
            checkExtraField(cartella);

            String folderId = newBLDocer().createFolder(getTicket(),ServerUtils.toDocerMap(cartella));

            return solrGet(folderId,Folder.TYPE);

        } catch (DocerException e) {
            throw new KSExceptionBadRequest(e);
        } catch (Exception e ){
            throw new KSRuntimeException(e);
        }
    }

    @Override
    public Folder get(String folderId) throws KSExceptionNotFound {
        return solrGet(folderId,Folder.TYPE);
    }

    @Override
    public Folder update(Folder cartella) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {
        String folderId = cartella.getFolderId();
        try {

            Folder oldBean = get(cartella.getFolderId());
            checkAcl(oldBean,cartella);
            checkExtraField(cartella);

            newBLDocer().updateFolder(getTicket(),folderId, ServerUtils.toUpdateMap(oldBean,cartella) );
            return solrGet(folderId,Folder.TYPE);
        } catch (DocerException e) {
            throw new KSExceptionBadRequest(e);
        } catch (Exception e ){
            throw new KSRuntimeException(e);
        }
    }

    @Override
    public void delete(String folderId) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {
        try {
            newBLDocer().deleteFolder(getTicket(),folderId);
        } catch (DocerException e) {
            throw new KSExceptionBadRequest(e);
        } catch (Exception e ){
            throw new KSRuntimeException(e);
        }
    }

    @Override
    public HistoryItem[] getHistory(String folderId) throws KSExceptionNotFound {
        return get(folderId).getHistoryItems();
    }

}
