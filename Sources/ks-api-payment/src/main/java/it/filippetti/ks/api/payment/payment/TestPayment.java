package it.filippetti.ks.api.payment.payment;

import java.math.BigDecimal;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Raffaele Dell'Aversana
 * @since 14 Jun 2018
 */
public class TestPayment {

    public static void main(String[] args) throws PaymentException {
        try {
            PaymentService paymentService = PaymentService.getInstance("PMPay");
            PaymentResponse esito;
            {
            PaymentRequest request = new PaymentRequest();
            request.setUsername("WS_CP001");
            request.setPassword("CP001_PWD");
            request.setRequestDate(new Date());
            request.setRequestId("945ed5ed6ba6cb262366191172713eb1df7524c3");
            request.setCompanyId("CP001");
            request.setInternalReference("2");
            request.setFiscalCode("FRTDNI73L06A404A");
            request.setClientType("pc");
            request.setUrlOk("http://portale.comune.spoltore.it:8082/api/paymentInstances/esitoTransazioneOK");
            request.setUrlKo("http://portale.comune.spoltore.it:8082/api/paymentInstances/esitoTransazioneKo");
            request.setUrlCancel("http://portale.comune.spoltore.it:8082/api/paymentInstances/esitoTransazioneCancel");
            request.setUrlS2S("http://portale.comune.spoltore.it:8082/api/paymentInstances/esitoTransazioneS2S");
            request.setFirstName("Paolo");
            request.setLastName("Rossi");
            request.setPaymentReason("Pagamento test-Nulla Osta installazione appostamento fisso di caccia in zona umida");
            request.setPaymentService("PASS");
            request.setCurrency("EUR");
            request.setTotalAmount(BigDecimal.valueOf(100.0));
            request.setPaymentDate(request.getRequestDate());

            esito = paymentService.payment(request);
            }
            System.out.println(esito);
            
            ReceiptRequest request = new ReceiptRequest();
            request.setUsername("WS_CP001");
            request.setPassword("CP001_PWD");
            request.setRequestDate(esito.getRequestDate());
            request.setRequestId("945ed5ed6ba6cb262366191172713eb1df7524c3");
            request.setCompanyId("CP001");
            request.setUniquePaymentId(esito.getUniquePaymentId());
            // opzionale
            request.setInternalReference("2");

            ReceiptResponse ricevuta = paymentService.getReceipt(request);
            System.out.println(ricevuta);
        } catch (PaymentServiceException ex) {
            Logger.getLogger(TestPayment.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
