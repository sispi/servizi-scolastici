package keysuite.docer.client;

import keysuite.desktop.exceptions.KSExceptionBadRequest;
import keysuite.desktop.exceptions.KSExceptionForbidden;
import keysuite.desktop.exceptions.KSExceptionNotFound;

public interface IFascicoli extends ISearchable {

    Fascicolo create(Fascicolo fascicolo) throws KSExceptionNotFound,KSExceptionBadRequest, KSExceptionForbidden;
    Fascicolo get(String fascicoloId) throws KSExceptionNotFound,KSExceptionBadRequest, KSExceptionForbidden;
    Fascicolo update(Fascicolo fascicolo) throws KSExceptionNotFound,KSExceptionForbidden,KSExceptionBadRequest;
    void delete(String fascicoloId) throws KSExceptionNotFound,KSExceptionForbidden,KSExceptionBadRequest;
    HistoryItem[] getHistory(String fascicoloId) throws KSExceptionNotFound,KSExceptionBadRequest, KSExceptionForbidden;
}
