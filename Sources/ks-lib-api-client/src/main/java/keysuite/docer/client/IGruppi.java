package keysuite.docer.client;

import keysuite.desktop.exceptions.KSExceptionBadRequest;
import keysuite.desktop.exceptions.KSExceptionForbidden;
import keysuite.desktop.exceptions.KSExceptionNotFound;

public interface IGruppi extends ISearchable {

    Group create(Group titolario) throws KSExceptionNotFound,KSExceptionBadRequest, KSExceptionForbidden;
    Group get(String groupId) throws KSExceptionNotFound,KSExceptionBadRequest, KSExceptionForbidden;
    Group update(Group gruppo) throws KSExceptionNotFound,KSExceptionForbidden,KSExceptionBadRequest;
    void delete(String groupId) throws KSExceptionNotFound,KSExceptionForbidden,KSExceptionBadRequest;
    HistoryItem[] getHistory(String groupid) throws KSExceptionNotFound,KSExceptionBadRequest, KSExceptionForbidden;
}
