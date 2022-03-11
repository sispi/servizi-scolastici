package it.filippetti.ks.api.payment.payment;

import it.filippetti.ks.api.payment.dto.CustomerDTO;
import it.filippetti.ks.api.payment.model.PaymentInstance;
import it.filippetti.ks.api.payment.model.Transaction;
import static it.filippetti.ks.api.payment.payment.PayParam.Service.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Raffaele Dell'Aversana
 * @since 14 Jul 2018
 * @update 12 April 2019 by Roberta
 */

public class PaymentRequest extends LoginRequest {
    private String internalReference;
    private String fiscalCode;
    private String clientType;
    private String urlCancel, urlS2S;
    private String firstName, lastName;
    private String paymentReason, paymentService, currency;
    private String baseUrl;

    private BigDecimal totalAmount;
    private Date paymentDate;

    private CustomerDTO ratePayer;
    private String urlOk, urlKo;
    private String backUrl;
    private Long referenceId;
    private String referenceClass;
    private String referenceProject;
    private PaymentInstance paymentInstance;
    private List<Transaction> transactions;

    /**
     *
     * @return internal reference of the payment
     */
    @PayParam(service = PMPAY, param="rifInterno")
    public String getInternalReference() {
        return internalReference;
    }

    @PayParam(service = PMPAY, param="rifInterno")
    public void setInternalReference(String internalReference) {
        this.internalReference = internalReference;
    }

    @PayParam(service = PMPAY, param="codiceFiscale")
    public String getFiscalCode() {
        return fiscalCode;
    }

    @PayParam(service = PMPAY, param="codiceFiscale")
    public void setFiscalCode(String fiscalCode) {
        this.fiscalCode = fiscalCode;
    }

    @PayParam(service = PMPAY, param="tipoClient")
    public String getClientType() {
        return clientType;
    }

    @PayParam(service = PMPAY, param="tipoClient")
    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    @PayParam(service = PMPAY, param="urlOk")
    @PayParam(service = BNL, param="urlOk")
    public String getUrlOk() {
        return urlOk;
    }

    @PayParam(service = PMPAY, param="urlOk")
    @PayParam(service = BNL, param="urlOk")
    public void setUrlOk(String urlOk) {
        this.urlOk = urlOk;
    }

    @PayParam(service = PMPAY, param="urlKo")
    @PayParam(service = BNL, param="urlKo")
    public String getUrlKo() {
        return urlKo;
    }

    @PayParam(service = PMPAY, param="urlKo")
    @PayParam(service = BNL, param="urlKo")
    public void setUrlKo(String urlKo) {
        this.urlKo = urlKo;
    }

    @PayParam(service = PMPAY, param="urlCancel")
    public String getUrlCancel() {
        return urlCancel;
    }

    @PayParam(service = PMPAY, param="urlCancel")
    public void setUrlCancel(String urlCancel) {
        this.urlCancel = urlCancel;
    }

    @PayParam(service = PMPAY, param="urlS2S")
    public String getUrlS2S() {
        return urlS2S;
    }

    @PayParam(service = PMPAY, param="urlS2S")
    public void setUrlS2S(String urlS2S) {
        this.urlS2S = urlS2S;
    }

    @PayParam(service = PMPAY, param="nome")
    public String getFirstName() {
        return firstName;
    }

    @PayParam(service = PMPAY, param="nome")
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @PayParam(service = PMPAY, param="cognome")
    public String getLastName() {
        return lastName;
    }

    @PayParam(service = PMPAY, param="cognome")
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @PayParam(service = PMPAY, param="causalePagamento")
    public String getPaymentReason() {
        return paymentReason;
    }

    @PayParam(service = PMPAY, param="causalePagamento")
    public void setPaymentReason(String paymentReason) {
        this.paymentReason = paymentReason;
    }

    @PayParam(service = PMPAY, param="servizioPagamento")
    public String getPaymentService() {
        return paymentService;
    }

    @PayParam(service = PMPAY, param="servizioPagamento")
    public void setPaymentService(String paymentService) {
        this.paymentService = paymentService;
    }

    @PayParam(service = PMPAY, param="divisa")
    public String getCurrency() {
        return currency;
    }

    @PayParam(service = PMPAY, param="divisa")
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @PayParam(service = PMPAY, param="importoTotale")
    @PayParam(service = BNL, param="importoTotale")
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    @PayParam(service = PMPAY, param="importoTotale")
    @PayParam(service = BNL, param="importoTotale")
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    @PayParam(service = PMPAY, param="dataPagamento")
    @PayParam(service = BNL, param="dataPagamento")
    public Date getPaymentDate() {
        return paymentDate;
    }

    @PayParam(service = PMPAY, param="dataPagamento")
    @PayParam(service = BNL, param="dataPagamento")
    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    @PayParam(service = PMPAY, param="baseUrl")
    @PayParam(service = BNL, param="baseUrl")
    public String getBaseUrl() {
        return baseUrl;
    }

    @PayParam(service = PMPAY, param="baseUrl")
    @PayParam(service = BNL, param="baseUrl")
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getBackUrl() {
        return backUrl;
    }

    public void setBackUrl(String backUrl) {
        this.backUrl = backUrl;
    }

    public Long getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(Long referenceId) {
        this.referenceId = referenceId;
    }

    public String getReferenceClass() {
        return referenceClass;
    }

    public void setReferenceClass(String referenceClass) {
        this.referenceClass = referenceClass;
    }

    public String getReferenceProject() {
        return referenceProject;
    }

    public void setReferenceProject(String referenceProject) {
        this.referenceProject = referenceProject;
    }

    public CustomerDTO getRatePayer() {
        return ratePayer;
    }

    public void setRatePayer(CustomerDTO ratePayer) {
        this.ratePayer = ratePayer;
    }

    public PaymentInstance getPaymentInstance() {
        return paymentInstance;
    }

    public void setPaymentInstance(PaymentInstance paymentInstance) {
        this.paymentInstance = paymentInstance;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

}
