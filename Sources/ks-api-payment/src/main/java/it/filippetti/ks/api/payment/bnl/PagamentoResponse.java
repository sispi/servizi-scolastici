package it.filippetti.ks.api.payment.bnl;

import javax.xml.datatype.XMLGregorianCalendar;


/**
 * Bean che riporta l'esito del pagamento
 *
 * @author Roberta Martella
 * @since 12 April 2019
 */

public class PagamentoResponse {

    private String paymentID;
    private String redirectURL;
    private String codiceErrore;
    private String descrizioneErrore;
    private XMLGregorianCalendar dataElaborazione;

    public String getRedirectURL() {
        return redirectURL;
    }

    public void setRedirectURL(String redirectURL) {
        this.redirectURL = redirectURL;
    }

    public String getCodiceErrore() {
        return codiceErrore;
    }

    public void setCodiceErrore(String codiceErrore) {
        this.codiceErrore = codiceErrore;
    }

    public String getDescrizioneErrore() {
        return descrizioneErrore;
    }

    public void setDescrizioneErrore(String descrizioneErrore) {
        this.descrizioneErrore = descrizioneErrore;
    }

    public XMLGregorianCalendar getDataElaborazione() {
        return dataElaborazione;
    }

    public void setDataElaborazione(XMLGregorianCalendar dataElaborazione) {
        this.dataElaborazione = dataElaborazione;
    }

    public String getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(String paymentID) {
        this.paymentID = paymentID;
    }


    @Override
    public String toString() {
        return "BnlResponse{"
            + "paymentID=" + getPaymentID() + ", "
            + "redirectURL=" + getRedirectURL() + ", "
            + "dataElaborazione=" + getDataElaborazione().toString() + ", "
            + "codiceErrore=" + getCodiceErrore() + ", "
            + "descrizioneErrore=" + getDescrizioneErrore()
            + '}';
    }
}
