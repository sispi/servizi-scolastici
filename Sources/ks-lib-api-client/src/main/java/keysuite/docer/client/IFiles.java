package keysuite.docer.client;

import keysuite.desktop.exceptions.KSExceptionBadRequest;
import keysuite.desktop.exceptions.KSExceptionForbidden;
import keysuite.desktop.exceptions.KSExceptionNotFound;

import java.io.InputStream;

public interface IFiles {

    //files
    String create(InputStream file, String groupId) throws KSExceptionNotFound,KSExceptionBadRequest, KSExceptionForbidden;

    String createGroup(String store) throws KSExceptionNotFound,KSExceptionBadRequest, KSExceptionForbidden;

    //files/{fileId}
    void update(InputStream file, String fileId, String range) throws KSExceptionNotFound,KSExceptionBadRequest, KSExceptionForbidden;
    InputStream get(String fileId, String range) throws KSExceptionNotFound,KSExceptionBadRequest, KSExceptionForbidden;
    void delete(String fileId) throws KSExceptionNotFound,KSExceptionForbidden,KSExceptionBadRequest;
    void deleteGroup(String groupId) throws KSExceptionNotFound,KSExceptionForbidden,KSExceptionBadRequest;
}
