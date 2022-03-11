package keysuite.docer.client;

import keysuite.desktop.exceptions.KSExceptionBadRequest;
import keysuite.desktop.exceptions.KSExceptionForbidden;
import keysuite.desktop.exceptions.KSExceptionNotFound;
import keysuite.docer.client.verificafirma.VerificaFirmaDTO;

import java.net.URL;

public interface IFirma {
    URL[] firmaAutomatica(
            String alias,
            String pin,
            String tipo,
            URL... urls
    ) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest;

    URL[] firmaRemota(
            String alias,
            String pin,
            String tipo,
            String OTP,
            URL... urls
    ) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest;

    URL[] timestamp(
            String tipo,
            URL... urls
    ) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest;

    /*Documento[] firmaAutomaticaDocumenti(
            String alias,
            String pin,
            String tipo,
            boolean relate,
            URL... urls

    ) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest;

    Documento[] firmaRemotaDocumenti(
            String alias,
            String pin,
            String tipo,
            String OTP,
            boolean relate,
            URL... urls

    ) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest;*/

    void requestOTP(
            String alias,
            String pin
    ) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest;

    VerificaFirmaDTO[] verificaFirme(
            String verificationDate, String policyFile,
            URL... urls
            ) throws KSExceptionNotFound,KSExceptionForbidden,KSExceptionBadRequest;

}
