package it.filippetti.ks.api.payment.pmpay;

import it.filippetti.ks.api.payment.pmpay.authpa.jaxb.RICHIEDIIUVRESPONSE;
import it.filippetti.ks.api.payment.pmpay.paypa.jaxb.PAGAMENTORESPONSE;
import java.math.BigInteger;

/**
 * Bean che riporta l'esito del pagamento
 *
 * @author Raffaele Dell'Aversana
 * @since 05 Jun 2018
 */
public class PagamentoResponse {

    private final PAGAMENTORESPONSE pagamentoResponse;
    private final RICHIEDIIUVRESPONSE iuvResponse;

    protected PagamentoResponse(PAGAMENTORESPONSE pagamentoResponse, RICHIEDIIUVRESPONSE iuvResponse) {
        this.pagamentoResponse = pagamentoResponse;
        this.iuvResponse = iuvResponse;
    }

    public String getIUV() {
        return iuvResponse.getIUV();
    }

    public java.util.Date getDataRichiesta() {
        System.out.println(pagamentoResponse.getDATARICHIESTA());
        return new java.util.Date();
        // return pagamentoResponse.getDATARICHIESTA().toGregorianCalendar().getTime();
    }

    public String getIdRichiesta() {
        return pagamentoResponse.getIDRICHIESTA();
    }

    public String getCodiceAzienda() {
        return pagamentoResponse.getCODICEAZIENDA();
    }

    public String getUrlToCall() {
        return pagamentoResponse.getURLTOCALL();
    }

    public BigInteger getCodiceErrore() {
        return pagamentoResponse.getCODICEERRORE();
    }

    public String getDescrizioneErrore() {
        return pagamentoResponse.getDESCRIZIONEERRORE();
    }

    @Override
    public String toString() {
        return "PMPayResponse{"
                + "dataRichiesta=" + getDataRichiesta() + ", "
                + "idRichiesta=" + getIdRichiesta() + ", "
                + "codiceAzienda=" + getCodiceAzienda() + ", "
                + "urlToCall=" + getUrlToCall() + ", "
                + "codiceErrore=" + getCodiceErrore() + ", "
                + "descrizioneErrore=" + getDescrizioneErrore()
                + '}';
    }
}
