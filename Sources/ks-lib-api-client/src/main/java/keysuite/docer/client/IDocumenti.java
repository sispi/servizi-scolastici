package keysuite.docer.client;

import keysuite.desktop.exceptions.KSExceptionBadRequest;
import keysuite.desktop.exceptions.KSExceptionForbidden;
import keysuite.desktop.exceptions.KSExceptionNotFound;
import keysuite.docer.client.corrispondenti.ICorrispondente;

import java.io.InputStream;
import java.net.URL;
import java.util.Map;

public interface IDocumenti  extends ISearchable{

    Documento create(Documento documento) throws KSExceptionNotFound,KSExceptionBadRequest, KSExceptionForbidden;
    Documento[] createMulti(boolean relate, Documento... documenti) throws KSExceptionNotFound,KSExceptionBadRequest, KSExceptionForbidden;
    Documento get(String docnum) throws KSExceptionNotFound,KSExceptionBadRequest, KSExceptionForbidden;
    Documento update(Documento documento) throws KSExceptionNotFound,KSExceptionBadRequest, KSExceptionForbidden;
    void delete(String docnum) throws KSExceptionNotFound,KSExceptionBadRequest, KSExceptionForbidden;
    Documento[] related(String docnum, String... tipo) throws KSExceptionNotFound,KSExceptionBadRequest, KSExceptionForbidden;
    void relate(String docnum, String... related) throws KSExceptionNotFound,KSExceptionBadRequest, KSExceptionForbidden;
    void unrelate(String docnum, String... related) throws KSExceptionNotFound,KSExceptionBadRequest, KSExceptionForbidden;
    InputStream download(String docnum, Integer version, String Range) throws KSExceptionNotFound,KSExceptionBadRequest, KSExceptionForbidden;
    Documento.Version[] versions(String docnum) throws KSExceptionNotFound,KSExceptionBadRequest, KSExceptionForbidden;
    Documento upload(String docnum, InputStream file, boolean replace) throws KSExceptionNotFound,KSExceptionBadRequest, KSExceptionForbidden;
    Documento upload(String docnum, URL url, boolean replace) throws KSExceptionNotFound,KSExceptionBadRequest, KSExceptionForbidden;
    Documento lock(String docnum, String expiration) throws KSExceptionNotFound,KSExceptionBadRequest, KSExceptionForbidden;
    Documento unlock(String docnum) throws KSExceptionNotFound,KSExceptionBadRequest, KSExceptionForbidden;
    Documento classifica(String docnum, String classifica, String piano, String[] secondarie) throws KSExceptionNotFound,KSExceptionForbidden,KSExceptionBadRequest;
    Documento fascicola(String docnum, String primario, String... secondari) throws KSExceptionNotFound,KSExceptionForbidden,KSExceptionBadRequest;
    Documento protocolla(String docnum, String verso, String oggetto, ICorrispondente mittente, ICorrispondente... destinatari) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest;
    Documento annullaProtocollazione(String docnum, String motivazione, String riferimento) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest;
    Documento registra(String docnum, String registro, String oggetto) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest;
    Documento annullaRegistrazione(String docnum, String motivazione, String riferimento) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest;

    void addAdvancedVersion(String docnum, String version) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden;
    Documento[] getAdvancedVersions(String docnum) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden;

    Map<String,String> registri() ;
    Map<String,String> tipologie(String tipo) ;
    HistoryItem[] getHistory(String docnum) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden;

    String invioPEC(String docnum, String oggetto, boolean generaAnnesso, boolean allegati, boolean verificaFirme, ICorrispondente... destinatari ) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest;

    Documento archivia(String docnum) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest;
    Documento conserva(String docnum) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest;
    Documento pubblica(String docnum, String registro, String oggetto, String dataInizio, String dataFine) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest;
    Documento annullaPubblicazione(String docnum, String motivazione, String dataAnnullamento) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest;


}
