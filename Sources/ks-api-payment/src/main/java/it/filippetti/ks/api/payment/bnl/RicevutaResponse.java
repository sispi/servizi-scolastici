package it.filippetti.ks.api.payment.bnl;

import it.filippetti.ks.api.payment.pmpay.paypa.jaxb.RICHIEDIRTRESPONSE;
import java.math.BigInteger;

/**
 * Bean della risposta alle richieste di ricevuta
 *
 * @author Raffaele Dell'Aversana
 * @since 14 Jun 2018
 */
public class RicevutaResponse {
    private final RICHIEDIRTRESPONSE rtResponse;

    protected RicevutaResponse(RICHIEDIRTRESPONSE rtResponse) {
        this.rtResponse = rtResponse;
    }

    public java.util.Date getDataRichiesta() {
        return rtResponse.getDATARICHIESTA().toGregorianCalendar().getTime();
    }

    public String getIdRichiesta() {
        return rtResponse.getIDRICHIESTA();
    }

    /**
     * Codice assegnato alla Azienda Esercente da PmPay,
     * fornito in fase di registrazione.
     * Deve corrispondere a quelle passato in RICHIEDI_RT_REQUEST
     * @return codice azienda
     */
    public String getCodiceAzienda() {
        return rtResponse.getCODICEAZIENDA();
    }

    /**
     *
     * @return Stream in base64 della RT restituita
     */
    public String getStreamRt() {
        return rtResponse.getSTREAMRT();
    }

    /**
     * codice dell'eventuale errore
     * @return
     */
    public BigInteger getCodiceErrore() {
        return rtResponse.getCODICEERRORE();
    }

    /**
     * descrizione dell'eventuale errore
     * @return
     */
    public String getDescrizioneErrore() {
        return rtResponse.getDESCRIZIONEERRORE();
    }

    @Override
    public String toString() {
        return "RicevutaResponse{"
                + "codiceAzienda=" + getCodiceAzienda()+", "
                + "idRichiesta=" + getIdRichiesta()+", "
                + "codiceErrore=" + getCodiceErrore()+", "
                + "descrizioneErrore=" + getDescrizioneErrore()+", "
                + "streamRT=" + getStreamRt()
                + '}';
    }

}
