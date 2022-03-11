package keysuite.docer.client;

import keysuite.desktop.exceptions.KSExceptionBadRequest;
import keysuite.desktop.exceptions.KSExceptionForbidden;
import keysuite.desktop.exceptions.KSExceptionNotFound;

public interface IConservazioneProvider {

    String[] conserva(
            Documento... documenti
    ) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest;

}
