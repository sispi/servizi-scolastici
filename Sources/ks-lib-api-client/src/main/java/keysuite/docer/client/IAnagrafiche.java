package keysuite.docer.client;

import keysuite.desktop.exceptions.KSExceptionBadRequest;
import keysuite.desktop.exceptions.KSExceptionForbidden;
import keysuite.desktop.exceptions.KSExceptionNotFound;
import keysuite.docer.query.ISearchParams;
import keysuite.docer.query.ISearchResponse;

public interface IAnagrafiche extends ISearchable {

    Anagrafica create(Anagrafica anagrafica) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden;
    Anagrafica get(String type, String codice) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden;
    Anagrafica update(Anagrafica anagrafica) throws KSExceptionNotFound,KSExceptionForbidden,KSExceptionBadRequest;
    void delete(String type, String codice) throws KSExceptionNotFound,KSExceptionForbidden,KSExceptionBadRequest;
    HistoryItem[] getHistory(String type, String codice) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden;
    ISearchResponse search(String type,ISearchParams params) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden;
}
