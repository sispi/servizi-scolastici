package it.filippetti.ks.api.payment.pmpay;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author raffaele
 */
public class PMPayMainTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws PMPayException {
        // debug


        System.setProperty("com.sun.xml.ws.transport.http.client.HttpTransportPipe.dump", "true");
        System.setProperty("com.sun.xml.internal.ws.transport.http.client.HttpTransportPipe.dump", "true");
        System.setProperty("com.sun.xml.ws.transport.http.HttpAdapter.dump", "true");
        System.setProperty("com.sun.xml.internal.ws.transport.http.HttpAdapter.dump", "true");

        System.out.println("####################TEST PAGAMENTO####################################");

        PagamentoResponse esito;
        {
            PagamentoRequest request = new PagamentoRequest();
            request.setIdClient("WS_CP001");
            request.setPwdClient("CP001_PWD");
            request.setDataRichiesta(new Date());
            request.setIdRichiesta("945ed5ed6ba6cb262366191172713eb1df7524c3");
            request.setCodiceAzienda("CP001");
            request.setRifInterno("2");
            request.setCodiceFiscale("FRTDNI73L06A404A");
            request.setTipoClient("ALTRO");
            request.setUrlOk("http://portale.palermo.palermo.it:8080/api/paymentInstances/redirectOutcome");
            request.setUrlKo("http://portale.palermo.palermo.it:8080/api/paymentInstances/redirectOutcome");
            request.setUrlCancel("http://portale.palermo.palermo.it:8080/api/paymentInstances/redirectOutcome");
            request.setUrlS2S("https://portaleso.collaudo.comune.palermo.it/api/paymentInstances/outcomePaymentInstance");
            // request.setUrlOk("http://portale.comune.spoltore.it:8082/api/paymentInstances/esitoTransazioneOK");
            // request.setUrlKo("http://portale.comune.spoltore.it:8082/api/paymentInstances/esitoTransazioneKo");
            // request.setUrlCancel("http://portale.comune.spoltore.it:8082/api/paymentInstances/esitoTransazioneCancel");
            // request.setUrlS2S("http://portale.comune.spoltore.it:8082/api/paymentInstances/esitoTransazioneS2S");
            request.setNome("Paolo");
            request.setCognome("Rossi");
            request.setCausalePagamento("Pagamento test-Nulla Osta installazione appostamento fisso di caccia in zona umida");
            request.setServizioPagamento("PASS");
            request.setDivisa("EUR");
            request.setImportoTotale(BigDecimal.valueOf(100.0));
            request.setDataPagamento(request.getDataRichiesta());

            esito = PMPayService.payment(request);
        }
        System.out.println(esito);

        System.out.println("####################TEST RICEVUTA#####################################");

        RicevutaResponse ricevuta;
        {
            RicevutaRequest request = new RicevutaRequest();
            request.setIdClient("WS_CP001");
            request.setPwdClient("CP001_PWD");
            request.setDataRichiesta(esito.getDataRichiesta());
            request.setIdRichiesta("945ed5ed6ba6cb262366191172713eb1df7524c3");
            request.setCodiceAzienda("CP001");
            request.setIuv(esito.getIUV());
            // opzionale
            request.setRifInterno("2");

            ricevuta = PMPayService.getRicevuta(request);
        }
        System.out.println(ricevuta);
    }
}
