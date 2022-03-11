package keysuite.docer.client;

import keysuite.desktop.exceptions.KSExceptionBadRequest;
import keysuite.desktop.exceptions.KSExceptionForbidden;
import keysuite.desktop.exceptions.KSExceptionNotFound;

public interface IPubblicazioneProvider {

    String pubblica(
            String registro,
            String oggetto,
            String dataInizio,
            String dataFine,
            Documento... documenti
    ) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest;

    void annulla(
            String registro,
            String numero,
            String anno,
            String data,
            String motivo
    ) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest;

}
