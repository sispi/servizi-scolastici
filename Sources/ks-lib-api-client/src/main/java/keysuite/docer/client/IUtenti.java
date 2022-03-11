package keysuite.docer.client;

import keysuite.desktop.exceptions.KSExceptionBadRequest;
import keysuite.desktop.exceptions.KSExceptionForbidden;
import keysuite.desktop.exceptions.KSExceptionNotFound;

public interface IUtenti extends ISearchable {

    User create(User utente) throws KSExceptionNotFound,KSExceptionBadRequest, KSExceptionForbidden;
    User get(String username) throws KSExceptionNotFound,KSExceptionBadRequest, KSExceptionForbidden;
    User update(User utente) throws KSExceptionNotFound,KSExceptionForbidden,KSExceptionBadRequest;
    void delete(String username) throws KSExceptionNotFound,KSExceptionForbidden,KSExceptionBadRequest;
    HistoryItem[] getHistory(String username) throws KSExceptionNotFound,KSExceptionBadRequest, KSExceptionForbidden;

}
