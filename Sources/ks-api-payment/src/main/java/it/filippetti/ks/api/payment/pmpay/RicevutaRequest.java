package it.filippetti.ks.api.payment.pmpay;

/**
 * Bean dei parametri per effettuare richieste di ricevuta
 *
 * @author Raffaele Dell'Aversana
 * @since 13 Jun 2018
 */
public class RicevutaRequest extends LoginRequest {

    // facoltativo
    private String rifInterno = "";

    // iuv
    private String iuv;

    /**
     *
     * @return identificativo della pratica gestito dal portale dell’Azienda Ente
     */
    public String getRifInterno() {
        return rifInterno;
    }

    /**
     * identificativo della pratica gestito dal portale dell’Azienda Ente
     * @param rifInterno (parametro opzionale)
     */
    public void setRifInterno(String rifInterno) {
        this.rifInterno = rifInterno;
    }

    public String getIuv() {
        return iuv;
    }

    public void setIuv(String iuv) {
        this.iuv = iuv;
    }

    @Override
    public String toString() {
        return "RicevutaRequest{" +
            "rifInterno='" + rifInterno + '\'' +
            ", iuv='" + iuv + '\'' +
            ", codiceAzienda='" + getCodiceAzienda() + '\'' +
            ", idClient='" + getIdClient() + '\'' +
            ", idRichiesta='" + getIdRichiesta() + '\'' +
            ", pwdClient='" + getPwdClient() + '\'' +
            ", dataRichiesta='" + getDataRichiesta() + '\'' +
            '}';
    }
}
