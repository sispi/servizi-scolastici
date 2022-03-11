/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.payment.payment;

import static it.filippetti.ks.api.payment.payment.PayParam.Service.*;
import java.util.Date;

/**
 *
 * @author Raffaele Dell'Aversana
 * @since 14 Jul 2018
 */
public abstract class PaymentResponse {

    @PayParam(service = PMPAY, param = "IUV")
    public abstract String getUniquePaymentId();

    @PayParam(service = PMPAY, param = "dataRichiesta")
    public abstract Date getRequestDate();

    @PayParam(service = PMPAY, param = "codiceAzienda")
    public abstract String getCompanyId();

    @PayParam(service = PMPAY, param = "idRichiesta")
    public abstract String getRequestId();

    @PayParam(service = PMPAY, param = "urlToCall")
    public abstract String getUrlToCall();

    @PayParam(service = PMPAY, param = "codiceErrore")
    public abstract String getErrorCode();

    @PayParam(service = PMPAY, param = "descrizioneErrore")
    public abstract String getErrorDescription();

}
