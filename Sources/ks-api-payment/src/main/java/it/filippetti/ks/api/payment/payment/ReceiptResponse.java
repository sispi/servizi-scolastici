package it.filippetti.ks.api.payment.payment;

import static it.filippetti.ks.api.payment.payment.PayParam.Service.*;
import java.util.HashMap;

/**
 *
 * @author Raffaele Dell'Aversana
 * @since 14 Jul 2018
 */
public abstract class ReceiptResponse {

    @PayParam(service = PMPAY, param = "dataRichiesta")
    public abstract java.util.Date getRequestDate();

    @PayParam(service = PMPAY, param = "idRichiesta")
    public abstract String getRequestId();

    @PayParam(service = PMPAY, param = "codiceAzienda")
    public abstract String getCompanyId();

    @PayParam(service = BNL, param = "esito")
    public abstract String getEsito();

    /**
     *
     * @return Stream in base64 della ricevuta oppure formato html
     */
    @PayParam(service = BNL, param = "streamRt")
    @PayParam(service = PMPAY, param = "streamRt")
    public abstract String getStream();

    @PayParam(service = BNL, param = "format")
    public abstract String getFormat();

    @PayParam(service = PMPAY, param = "codiceErrore")
    @PayParam(service = CPAY, param = "S", note = "S è sia codice che descrizione")
    public abstract String getErrorCode();

    @PayParam(service = PMPAY, param = "descrizioneErrore")
    @PayParam(service = CPAY, param = "S", note = "S è sia codice che descrizione")
    public abstract String getErrorDescription();


    /*** manage variables for standard receipt template***/
    @PayParam(service = BNL, param = "templateVariables")
    public abstract HashMap<String,Object> getTemplateVariables();

    @Override
    public String toString() {
        return "RicevutaResponse{"
                + "companyId=" + getCompanyId()+ ", "
                + "requestId=" + getRequestId()+ ", "
                + "errorCode=" + getErrorCode()+ ", "
                + "errorDescription=" + getErrorDescription()+ ", "
                + "streamRT present:" + (getStream()!=null)+ ", "
                + "format=" + (getFormat()!=null)
                + '}';
    }

}
