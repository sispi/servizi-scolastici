package keysuite.docer.client;

import keysuite.desktop.exceptions.KSExceptionBadRequest;
import keysuite.desktop.exceptions.KSExceptionForbidden;
import keysuite.desktop.exceptions.KSExceptionNotFound;

public interface ICartelle extends ISearchable {

    Folder create(Folder cartella) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden;
    Folder get(String folderId) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden;
    Folder update(Folder cartella) throws KSExceptionNotFound,KSExceptionForbidden,KSExceptionBadRequest;
    void delete(String folderId) throws KSExceptionNotFound,KSExceptionForbidden,KSExceptionBadRequest;
    HistoryItem[] getHistory(String folderId) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden;
}
