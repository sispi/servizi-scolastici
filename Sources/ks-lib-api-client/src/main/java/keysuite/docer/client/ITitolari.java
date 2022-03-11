package keysuite.docer.client;

import keysuite.desktop.exceptions.KSExceptionBadRequest;
import keysuite.desktop.exceptions.KSExceptionForbidden;
import keysuite.desktop.exceptions.KSExceptionNotFound;

public interface ITitolari extends ISearchable {

    Titolario create(Titolario titolario) throws KSExceptionNotFound,KSExceptionBadRequest, KSExceptionForbidden;
    Titolario get(String classifica, String piano) throws KSExceptionNotFound,KSExceptionBadRequest, KSExceptionForbidden;
    Titolario update(Titolario fascicolo) throws KSExceptionNotFound,KSExceptionForbidden,KSExceptionBadRequest;
    void delete(String classifica, String piano) throws KSExceptionNotFound,KSExceptionForbidden,KSExceptionBadRequest;
    HistoryItem[] getHistory(String classifica, String piano) throws KSExceptionNotFound,KSExceptionBadRequest, KSExceptionForbidden;
    String pianoClassificazione(String anno) ;

}
