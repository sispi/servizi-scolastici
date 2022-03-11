package it.filippetti.ks.api.payment.pmpay;

import java.util.Date;

/**
 * Bean dei parametri per effettuare login
 *
 * @author Raffaele Dell'Aversana
 * @since 14 Jun 2018
 */
public abstract class LoginRequest {

    private String idClient;
    private String pwdClient;
    private Date dataRichiesta;
    private String idRichiesta;
    private String codiceAzienda;

    public String getIdClient() {
        return idClient;
    }

    public void setIdClient(String idClient) {
        this.idClient = idClient;
    }

    public String getPwdClient() {
        return pwdClient;
    }

    public void setPwdClient(String pwdClient) {
        this.pwdClient = pwdClient;
    }

    public Date getDataRichiesta() {
        return dataRichiesta;
    }

    public void setDataRichiesta(Date dataRichiesta) {
        this.dataRichiesta = dataRichiesta;
    }

    public String getIdRichiesta() {
        return idRichiesta;
    }

    public void setIdRichiesta(String idRichiesta) {
        this.idRichiesta = idRichiesta;
    }

    public String getCodiceAzienda() {
        return codiceAzienda;
    }

    public void setCodiceAzienda(String codiceAzienda) {
        this.codiceAzienda = codiceAzienda;
    }

}
