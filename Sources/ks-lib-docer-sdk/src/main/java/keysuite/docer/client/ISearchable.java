package keysuite.docer.client;

import keysuite.desktop.exceptions.KSExceptionBadRequest;
import keysuite.desktop.exceptions.KSExceptionForbidden;
import keysuite.desktop.exceptions.KSExceptionNotFound;
import keysuite.docer.query.ISearchParams;
import keysuite.docer.query.ISearchResponse;

public interface ISearchable {
    ISearchResponse search(ISearchParams params) throws KSExceptionNotFound,KSExceptionBadRequest, KSExceptionForbidden;
}
