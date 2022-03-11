package it.filippetti.ks.api.payment.pmpay;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Bean dei parametri per effettuare richieste di pagamento
 * @author Raffaele Dell'Aversana
 * @since 05 Jun 2018
 */
public class PagamentoRequest extends LoginRequest {
    private String rifInterno;
    private String codiceFiscale;
    private String tipoClient;
    private String urlOk, urlKo, urlCancel, urlS2S;
    private String nome, cognome;
    private String causalePagamento, servizioPagamento, divisa;
    private BigDecimal importoTotale;
    private Date dataPagamento;

    public String getCausalePagamento() {
        return causalePagamento;
    }

    public void setCausalePagamento(String causalePagamento) {
        this.causalePagamento = causalePagamento;
    }

    public String getServizioPagamento() {
        return servizioPagamento;
    }

    public void setServizioPagamento(String servizioPagamento) {
        this.servizioPagamento = servizioPagamento;
    }

    public String getDivisa() {
        return divisa;
    }

    public void setDivisa(String divisa) {
        this.divisa = divisa;
    }

    public BigDecimal getImportoTotale() {
        return importoTotale;
    }

    public void setImportoTotale(BigDecimal importoTotale) {
        this.importoTotale = importoTotale;
    }

    public Date getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(Date dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public String getRifInterno() {
        return rifInterno;
    }

    public void setRifInterno(String rifInterno) {
        this.rifInterno = rifInterno;
    }

    public String getCodiceFiscale() {
        return codiceFiscale;
    }

    public void setCodiceFiscale(String codiceFiscale) {
        this.codiceFiscale = codiceFiscale;
    }

    public String getTipoClient() {
        return tipoClient;
    }

    public void setTipoClient(String tipoClient) {
        this.tipoClient = tipoClient;
    }

    public String getUrlOk() {
        return urlOk;
    }

    public void setUrlOk(String urlOk) {
        this.urlOk = urlOk;
    }

    public String getUrlKo() {
        return urlKo;
    }

    public void setUrlKo(String urlKo) {
        this.urlKo = urlKo;
    }

    public String getUrlCancel() {
        return urlCancel;
    }

    public void setUrlCancel(String urlCancel) {
        this.urlCancel = urlCancel;
    }

    public String getUrlS2S() {
        return urlS2S;
    }

    public void setUrlS2S(String urlS2S) {
        this.urlS2S = urlS2S;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    @Override
    public String toString() {
        return "PMPayRequest{" +
            "idClient='" + getIdClient() + '\'' +
            ", pwdClient='" + getPwdClient() + '\'' +
            ", dataRichiesta=" + getDataRichiesta() +
            ", idRichiesta='" + getIdRichiesta() + '\'' +
            ", codiceAzienda='" + getCodiceAzienda() + '\'' +
            ", rifInterno='" + rifInterno + '\'' +
            ", codiceFiscale='" + codiceFiscale + '\'' +
            ", tipoClient='" + tipoClient + '\'' +
            ", urlOk='" + urlOk + '\'' +
            ", urlKo='" + urlKo + '\'' +
            ", urlCancel='" + urlCancel + '\'' +
            ", urlS2S='" + urlS2S + '\'' +
            ", nome='" + nome + '\'' +
            ", cognome='" + cognome + '\'' +
            ", causalePagamento='" + causalePagamento + '\'' +
            ", servizioPagamento='" + servizioPagamento + '\'' +
            ", divisa='" + divisa + '\'' +
            ", importoTotale=" + importoTotale +
            ", dataPagamento=" + dataPagamento +
            '}';
    }
}
