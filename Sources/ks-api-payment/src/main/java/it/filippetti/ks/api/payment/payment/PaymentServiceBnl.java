package it.filippetti.ks.api.payment.payment;

import it.filippetti.ks.api.payment.bnl.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * implementazione del servizio Bnl basato sul PaymentService
 *
 */

public class PaymentServiceBnl implements PaymentService {

    public static PaymentServiceBnl getInstance() {
        return new PaymentServiceBnl();
    }

    @Override
    public PaymentResponse payment(PaymentRequest request) throws PaymentException {

        PagamentoRequest pagamentoRequest = new PagamentoRequest();
        pagamentoRequest.setShopUserRef(request.getRatePayer().getEmail());
        pagamentoRequest.setShopUserName(request.getRatePayer().getLastName() + "," + request.getRatePayer().getFirstName());
        pagamentoRequest.setShopUserAccount(request.getRatePayer().getEmail());
        pagamentoRequest.setLangKey(request.getRatePayer().getLangKey());
        String idPaymentInstance=String.valueOf(request.getPaymentInstance().getId());
        pagamentoRequest.setShopID(idPaymentInstance+"-enac-tmp");

        Double importoNetto=request.getPaymentInstance().getNetAmount();
        String amount=String.format(Locale.US, "%.2f", importoNetto).replace(".","");
        pagamentoRequest.setAmount(Long.parseLong(amount));

        pagamentoRequest.setDescription(request.getPaymentInstance().getCausal());

        pagamentoRequest.setNotifyUrl(request.getBaseUrl()+"/api/paymentInstances/verifyPaymentInstance?idrichiesta="+idPaymentInstance);
        pagamentoRequest.setErrorUrl(pagamentoRequest.getNotifyUrl());

        pagamentoRequest.setCurrency(request.getCurrency());
        pagamentoRequest.setSignatureKey(request.getCompanyId());

        System.out.print("Print PagamentoRequest: " + pagamentoRequest.toString());

        PagamentoResponse esito = BnlService.initPayment(pagamentoRequest);

        return new PaymentResponse() {
            @Override
            public String getUniquePaymentId() {
                return esito.getPaymentID();
            }

            @Override
            public Date getRequestDate() {
                return esito.getDataElaborazione().toGregorianCalendar().getTime();
            }

            @Override
            public String getCompanyId() {
                return null;
            }

            @Override
            public String getRequestId() {
                return idPaymentInstance;
            }

            @Override
            public String getUrlToCall() {
                return esito.getRedirectURL();
            }

            @Override
            public String getErrorCode() {
                String error = esito.getCodiceErrore();
                return error != null ? error : null;
            }

            @Override
            public String getErrorDescription() {
                return esito.getDescrizioneErrore();
            }
        };

    }


    @Override
    public String verify(PaymentRequest request) throws PaymentException {
        PagamentoRequest pagamentoRequest = new PagamentoRequest();
        String idPaymentInstance=String.valueOf(request.getPaymentInstance().getId());
        pagamentoRequest.setShopID(idPaymentInstance+"-enac-tmp");
        pagamentoRequest.setIuv(request.getPaymentInstance().getIuv());
        pagamentoRequest.setSignatureKey(request.getCompanyId());
        PagamentoResponse esito = BnlService.verifyPayment(pagamentoRequest);
        if(esito.getCodiceErrore().equals("IGFS_000")){
            return "OK";
        }else if(esito.getCodiceErrore().equals("IGFS_20090")){
            return "CANCELLED";
        }
        else {
            //generic error
            return esito.getCodiceErrore();
        }
    }


    @Override
    public ReceiptResponse getReceipt(ReceiptRequest request) {

        HashMap<String, Object> map=new HashMap<String, Object>();

        String causale=request.getLevy().getCausal();
        String[] partsCausale = causale.split(";");
        String descrizione=partsCausale[0];
        String code=partsCausale[1].trim();
        String tipo=partsCausale[2];
        String quantita=partsCausale[3];
        String descrizioneComplessiva=descrizione+" ("+code+") - "+quantita;

        String importoComplessivo=String.valueOf(request.getLevy().getTotalAmount())+" euro";
        DateTime date= new DateTime(request.getLevy().getProcessingDate());
        DateTimeFormatter dtfOut = DateTimeFormat.forPattern("dd-MM-yyyy");
        String formattedDate=dtfOut.print(date);
        String name=request.getLevy().getCustomer().getLastName() +" "+request.getLevy().getCustomer().getFirstName();
        String fiscalCode=request.getLevy().getCustomer().getFiscalCode();

        map.put("titolo", "del "+formattedDate);
        map.put("descrizione", descrizioneComplessiva);
        map.put("tipo", tipo);
        map.put("importo", importoComplessivo);
        map.put("dataElaborazione", date);
        map.put("name", name);
        map.put("fiscalCode", fiscalCode);

        return new ReceiptResponse() {
            @Override
            public Date getRequestDate() {
                return null;
            }

            @Override
            public String getRequestId() {
                return null;
            }

            @Override
            public String getCompanyId() {
                return null;
            }

            @Override
            public String getEsito() {
                return "OK";
            }

            @Override
            public String getStream() {
                return null;
            }

            @Override
            public String getFormat() {
                return "html";
            }

            @Override
            public String getErrorCode() {
                return "0";
            }

            @Override
            public String getErrorDescription() {
                return null;
            }

            @Override
            public HashMap<String, Object> getTemplateVariables() {
                return map;
            }
        };
    }

}
