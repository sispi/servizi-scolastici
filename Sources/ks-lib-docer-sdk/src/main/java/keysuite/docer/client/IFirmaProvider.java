package keysuite.docer.client;

import keysuite.desktop.exceptions.KSExceptionBadRequest;
import keysuite.desktop.exceptions.KSExceptionForbidden;
import keysuite.desktop.exceptions.KSExceptionNotFound;
import keysuite.docer.client.verificafirma.VerificaFirmaDTO;

public interface IFirmaProvider {

    NamedInputStream[] firmaAutomatica(
            String alias,
            String pin,
            String tipo,
            NamedInputStream... streams
    ) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest;

    NamedInputStream[] firmaRemota(
            String alias,
            String pin,
            String tipo,
            String OTP,
            NamedInputStream... streams
    ) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest;

    NamedInputStream[] timestamp(
            String tipo,
            NamedInputStream... streams
    ) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest;

    void requestOTP(
            String alias,
            String pin
    ) throws KSExceptionForbidden, KSExceptionBadRequest;

    NamedInputStream[] print(
            String text,
            String style,
            NamedInputStream... streams
    ) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest;

    // il metodo verifica firme può tornare VerificaFirmaDTO o un bean convertibile ad esso tramite jackson

    Object[] verificaFirme(
            String verificationDate,
            String policyFile,
            NamedInputStream... streams
    ) throws KSExceptionBadRequest;

}
