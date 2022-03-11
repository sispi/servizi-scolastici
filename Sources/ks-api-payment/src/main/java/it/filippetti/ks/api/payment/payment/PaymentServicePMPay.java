package it.filippetti.ks.api.payment.payment;

import it.filippetti.ks.api.payment.model.PaymentInstance;
import it.filippetti.ks.api.payment.pmpay.PMPayService;
import it.filippetti.ks.api.payment.pmpay.PagamentoRequest;
import it.filippetti.ks.api.payment.pmpay.PagamentoResponse;
import it.filippetti.ks.api.payment.pmpay.RicevutaRequest;
import it.filippetti.ks.api.payment.pmpay.RicevutaResponse;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * implementazione del servizio PMPay basato sul PaymentService
 *
 * @author Raffaele Dell'Aversana
 * @since 14 Jun 2018
 * @updated 15 April by Roberta
 */
public class PaymentServicePMPay implements PaymentService {

    public static PaymentServicePMPay getInstance() {
        return new PaymentServicePMPay();
    }

    @Override
    public PaymentResponse payment(PaymentRequest request) throws PaymentException {
        
        PaymentInstance paymentInstance = request.getPaymentInstance();

        String idPortale = paymentInstance.getId().toString(); // Check
        String servizio = paymentInstance.getPaymentService(); // Check
        String causale = paymentInstance.getCausal();
        BigDecimal importoTotale=BigDecimal.valueOf(paymentInstance.getTotalAmount());
        String importoInCentesimi=String.valueOf(importoTotale.intValue());
        String ente = "KDMTE";
        String secretKey=request.getCompanyId();
        String baseUrl=request.getBaseUrl();

        String mac = DigestUtils.shaHex(importoInCentesimi + idPortale + ente + causale + servizio + secretKey);

        PagamentoRequest req = new PagamentoRequest();
        req.setIdClient(request.getUsername());
        req.setPwdClient(request.getPassword());

        req.setDataRichiesta(request.getRequestDate());
        req.setIdRichiesta(paymentInstance.getId().toString());
        req.setCodiceAzienda(secretKey);

        req.setRifInterno(paymentInstance.getId().toString());
        req.setCodiceFiscale(request.getRatePayer().getFiscalCode());
        req.setTipoClient("ALTRO");

        req.setUrlOk(request.getUrlOk());
        req.setUrlKo(request.getUrlKo());
        req.setUrlCancel(request.getUrlCancel());
        req.setUrlS2S(request.getUrlS2S());

        req.setNome(request.getRatePayer().getFirstName());
        req.setCognome(request.getRatePayer().getLastName());

        req.setCausalePagamento(causale);
        req.setServizioPagamento(servizio);

        req.setDivisa(request.getCurrency());
        req.setImportoTotale(importoTotale);
        req.setDataPagamento(request.getPaymentDate());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String dataScadenza = sdf.format(paymentInstance.getExpiryDate());

        PagamentoResponse esito = PMPayService.payment(req);

        return new PaymentResponse() {
            @Override
            public String getUniquePaymentId() {
                return esito.getIUV();
            }

            @Override
            public Date getRequestDate() {
                return esito.getDataRichiesta();
            }

            @Override
            public String getCompanyId() {
                return esito.getCodiceAzienda();
            }

            @Override
            public String getRequestId() {
                return esito.getIdRichiesta();
            }

            @Override
            public String getUrlToCall() {
                
                String redirectUrl = String.format(esito.getUrlToCall(),
                        ente,
                        servizio,
                        idPortale,
                        importoInCentesimi,
                        causale,
                        dataScadenza,
                        mac,
                        baseUrl);

                return redirectUrl;
            }

            @Override
            public String getErrorCode() {
                BigInteger error = esito.getCodiceErrore();
                return error != null ? error.toString() : null;
            }

            @Override
            public String getErrorDescription() {
                return esito.getDescrizioneErrore();
            }

        };

    }

    @Override
    public String verify(PaymentRequest request) throws PaymentException {
        return null;
    }

    @Override
    public ReceiptResponse getReceipt(ReceiptRequest rr) throws PaymentException {
        RicevutaRequest request = new RicevutaRequest();
        request.setIdClient(rr.getUsername());
        request.setPwdClient(rr.getPassword());
        request.setDataRichiesta(rr.getRequestDate());
        request.setIdRichiesta(rr.getRequestId());
        request.setCodiceAzienda(rr.getCompanyId());
        request.setIuv(rr.getUniquePaymentId());
        request.setRifInterno(rr.getInternalReference());

        RicevutaResponse ricevuta = PMPayService.getRicevuta(request);
        return new ReceiptResponse() {
            @Override
            public Date getRequestDate() {
                return ricevuta.getDataRichiesta();
            }

            @Override
            public String getRequestId() {
                return ricevuta.getIdRichiesta();
            }

            @Override
            public String getCompanyId() {
                return ricevuta.getCodiceAzienda();
            }

            @Override
            public String getEsito() {
                return null;
            }

            @Override
            public String getStream() {
                return ricevuta.getStreamRt();
            }

            @Override
            public String getFormat() {
                return null;
            }

            @Override
            public String getErrorCode() {
                BigInteger error = ricevuta.getCodiceErrore();
                return (error!=null) ? error.toString() : null;
            }

            @Override
            public String getErrorDescription() {
                return ricevuta.getDescrizioneErrore();
            }

            @Override
            public HashMap<String, Object> getTemplateVariables() {
                return null;
            }
        };
    }

}
