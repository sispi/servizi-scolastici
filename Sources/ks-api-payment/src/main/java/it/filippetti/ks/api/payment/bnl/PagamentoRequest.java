package it.filippetti.ks.api.payment.bnl;

/**
 * Bean dei parametri per effettuare richieste di pagamento Bnl
 */
public class PagamentoRequest
{
    //customer
    private String shopUserRef;
    private String shopUserName;
    private String shopUserAccount;
    private String langKey;

    //payment
    private String shopID;
    private Long amount;
    private String description;
    private String iuv; //in order to verify

    //redirect
    private String notifyUrl;
    private String errorUrl;

    //system
    private String signatureKey;
    private String currency;

    public String getShopUserRef() {
        return shopUserRef;
    }

    public void setShopUserRef(String shopUserRef) {
        this.shopUserRef = shopUserRef;
    }

    public String getShopUserName() {
        return shopUserName;
    }

    public void setShopUserName(String shopUserName) {
        this.shopUserName = shopUserName;
    }

    public String getShopUserAccount() {
        return shopUserAccount;
    }

    public void setShopUserAccount(String shopUserAccount) {
        this.shopUserAccount = shopUserAccount;
    }

    public String getLangKey() {
        return langKey;
    }

    public void setLangKey(String langKey) {
        this.langKey = langKey;
    }

    public String getShopID() {
        return shopID;
    }

    public void setShopID(String shopID) {
        this.shopID = shopID;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getErrorUrl() {
        return errorUrl;
    }

    public void setErrorUrl(String errorUrl) {
        this.errorUrl = errorUrl;
    }

    public String getSignatureKey() {
        return signatureKey;
    }

    public void setSignatureKey(String signatureKey) {
        this.signatureKey = signatureKey;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getIuv() {
        return iuv;
    }

    public void setIuv(String iuv) {
        this.iuv = iuv;
    }

    @Override
    public String toString() {
        return "PagamentoRequest{" +
            "shopUserRef='" + shopUserRef + '\'' +
            ", shopUserName='" + shopUserName + '\'' +
            ", shopUserAccount='" + shopUserAccount + '\'' +
            ", langKey='" + langKey + '\'' +
            ", shopID='" + shopID + '\'' +
            ", amount=" + amount +
            ", description='" + description + '\'' +
            ", iuv='" + iuv + '\'' +
            ", notifyUrl='" + notifyUrl + '\'' +
            ", errorUrl='" + errorUrl + '\'' +
            ", signatureKey='" + signatureKey + '\'' +
            ", currency='" + currency + '\'' +
            '}';
    }
}
