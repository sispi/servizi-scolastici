/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.payment.model;

import java.util.Arrays;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author dino
 */
@Entity
@Table(name = "PAYMENT_INSTANCE")

@Sortables(
    defaultSort = "id:ASC",
    value = {
        @Sortable(property = "id")
    }
)

public class PaymentInstance extends Auditable{
    
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)    
    private Long id;
    
    @Basic
    @Column(nullable = true)
    private String referenceClientId;
    
    @Basic
    @Column(nullable = true)
    private String referenceProcessId;

    @Basic
    @Column(nullable = true)
    private String referenceInstanceId;

    @Basic
    @Column(nullable = false)    
    private String tenant;
    
    @Basic
    @Column(nullable = false)
    private String organization;
    
    @Basic
    @Column(nullable = true)
    private String iuv;

    @Basic
    @Column(nullable = true)
    private String paymentServiceSpecificId;
    
    @Basic
    @Column(nullable = true)
    private String requestCode;
    
    @Basic
    @Column(nullable = true)
    private String currencyCode;
    
    @Basic
    @Column(nullable = false)
    private String paymentService;

    @Basic
    @Column(nullable = true)
    private String causal;
    
    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = true)
    private Date creationDate;

    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = true)
    private Date expiryDate;
    
    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = true)
    private Date processingDate;

    @Basic
    @Column(nullable = true)
    private String paymentType;

    @Basic
    @Column(nullable = false)
    private Double totalAmount;

    @Basic
    @Column(nullable = false)
    private Double netAmount;

    @Basic
    @Column(nullable = true)
    private String transactionResult;

    @Basic
    @Lob
    @Column(nullable = true)
    private byte[] receipt;

    @Basic
    @Column(nullable = true)
    private String receiptContentType;

    @Basic
    @Column(nullable = true)
    private String protocolNumber;

    @Basic
    @Column(nullable = true)
    private Long protocolYear;

    @Basic
    @Lob
    @Column(nullable = true)
    private byte[] customReceipt;

    @Basic
    @Column(nullable = true)
    private String customReceiptContentType;

    @Basic
    @Column(nullable = true)
    private String invoiceNumber;

    @Basic
    @Column(nullable = true)
    private Long invoiceYear;

    @Basic
    @ManyToOne(fetch = FetchType.LAZY, optional = true, targetEntity = Customer.class)
    private Customer customer;
    
    @Basic
    @ManyToOne(fetch = FetchType.LAZY, optional = true, targetEntity = Channel.class)
    private Channel channel;
    
    @Basic
    @Column(nullable = false)
    private String referenceUserId;

    @Basic
    @Column(nullable = true)
    private String callbackRedirect;

    @Basic
    @Column(nullable = true)
    private String callbackStatus;

    @Basic
    @Column(nullable = true)
    private String uuid;

    @Basic
    @Column(nullable = true)
    private Outcome outcome;

    @Basic
    @Column(nullable = true)
    private String paymentSystemReference;

    public PaymentInstance() {
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getReferenceClientId() {
        return referenceClientId;
    }

    public void setReferenceClientId(String referenceClientId) {
        this.referenceClientId = referenceClientId;
    }

    public String getReferenceProcessId() {
        return referenceProcessId;
    }

    public void setReferenceProcessId(String referenceProcessId) {
        this.referenceProcessId = referenceProcessId;
    }

    public String getReferenceInstanceId() {
        return referenceInstanceId;
    }

    public void setReferenceInstanceId(String referenceInstanceId) {
        this.referenceInstanceId = referenceInstanceId;
    }

    public String getTenant() {
        return tenant;
    }

    public void setTenant(String tenant) {
        this.tenant = tenant;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getIuv() {
        return iuv;
    }

    public void setIuv(String iuv) {
        this.iuv = iuv;
    }

    public String getPaymentServiceSpecificId() {
        return paymentServiceSpecificId;
    }

    public void setPaymentServiceSpecificId(String paymentServiceSpecificId) {
        this.paymentServiceSpecificId = paymentServiceSpecificId;
    }

    public String getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(String requestCode) {
        this.requestCode = requestCode;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getPaymentService() {
        return paymentService;
    }

    public void setPaymentService(String paymentService) {
        this.paymentService = paymentService;
    }

    public String getCausal() {
        return causal;
    }

    public void setCausal(String causal) {
        this.causal = causal;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Date getProcessingDate() {
        return processingDate;
    }

    public void setProcessingDate(Date processingDate) {
        this.processingDate = processingDate;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Double getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(Double netAmount) {
        this.netAmount = netAmount;
    }

    public String getTransactionResult() {
        return transactionResult;
    }

    public void setTransactionResult(String transactionResult) {
        this.transactionResult = transactionResult;
    }

    public byte[] getReceipt() {
        return receipt;
    }

    public void setReceipt(byte[] receipt) {
        this.receipt = receipt;
    }

    public String getReceiptContentType() {
        return receiptContentType;
    }

    public void setReceiptContentType(String receiptContentType) {
        this.receiptContentType = receiptContentType;
    }

    public String getProtocolNumber() {
        return protocolNumber;
    }

    public void setProtocolNumber(String protocolNumber) {
        this.protocolNumber = protocolNumber;
    }

    public Long getProtocolYear() {
        return protocolYear;
    }

    public void setProtocolYear(Long protocolYear) {
        this.protocolYear = protocolYear;
    }

    public byte[] getCustomReceipt() {
        return customReceipt;
    }

    public void setCustomReceipt(byte[] customReceipt) {
        this.customReceipt = customReceipt;
    }

    public String getCustomReceiptContentType() {
        return customReceiptContentType;
    }

    public void setCustomReceiptContentType(String customReceiptContentType) {
        this.customReceiptContentType = customReceiptContentType;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public Long getInvoiceYear() {
        return invoiceYear;
    }

    public void setInvoiceYear(Long invoiceYear) {
        this.invoiceYear = invoiceYear;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public String getReferenceUserId() {
        return referenceUserId;
    }

    public void setReferenceUserId(String referenceUserId) {
        this.referenceUserId = referenceUserId;
    }

    public String getCallbackRedirect() {
        return callbackRedirect;
    }

    public void setCallbackRedirect(String callbackRedirect) {
        this.callbackRedirect = callbackRedirect;
    }

    public String getCallbackStatus() {
        return callbackStatus;
    }

    public void setCallbackStatus(String callbackStatus) {
        this.callbackStatus = callbackStatus;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Outcome getOutcome() {
        return outcome;
    }

    public void setOutcome(Outcome outcome) {
        this.outcome = outcome;
    }

    public String getPaymentSystemReference() {
        return paymentSystemReference;
    }

    public void setPaymentSystemReference(String paymentSystemReference) {
        this.paymentSystemReference = paymentSystemReference;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PaymentInstance)) return false;
        if (!super.equals(o)) return false;
        PaymentInstance that = (PaymentInstance) o;
        return Objects.equals(getId(), that.getId()) &&
                Objects.equals(getReferenceClientId(), that.getReferenceClientId()) &&
                Objects.equals(getReferenceProcessId(), that.getReferenceProcessId()) &&
                Objects.equals(getReferenceInstanceId(), that.getReferenceInstanceId()) &&
                Objects.equals(getTenant(), that.getTenant()) &&
                Objects.equals(getOrganization(), that.getOrganization()) &&
                Objects.equals(getIuv(), that.getIuv()) &&
                Objects.equals(getPaymentServiceSpecificId(), that.getPaymentServiceSpecificId()) &&
                Objects.equals(getRequestCode(), that.getRequestCode()) &&
                Objects.equals(getCurrencyCode(), that.getCurrencyCode()) &&
                Objects.equals(getPaymentService(), that.getPaymentService()) &&
                Objects.equals(getCausal(), that.getCausal()) &&
                Objects.equals(getCreationDate(), that.getCreationDate()) &&
                Objects.equals(getExpiryDate(), that.getExpiryDate()) &&
                Objects.equals(getProcessingDate(), that.getProcessingDate()) &&
                Objects.equals(getPaymentType(), that.getPaymentType()) &&
                Objects.equals(getTotalAmount(), that.getTotalAmount()) &&
                Objects.equals(getNetAmount(), that.getNetAmount()) &&
                Objects.equals(getTransactionResult(), that.getTransactionResult()) &&
                Arrays.equals(getReceipt(), that.getReceipt()) &&
                Objects.equals(getReceiptContentType(), that.getReceiptContentType()) &&
                Objects.equals(getProtocolNumber(), that.getProtocolNumber()) &&
                Objects.equals(getProtocolYear(), that.getProtocolYear()) &&
                Arrays.equals(getCustomReceipt(), that.getCustomReceipt()) &&
                Objects.equals(getCustomReceiptContentType(), that.getCustomReceiptContentType()) &&
                Objects.equals(getInvoiceNumber(), that.getInvoiceNumber()) &&
                Objects.equals(getInvoiceYear(), that.getInvoiceYear()) &&
                Objects.equals(getCustomer(), that.getCustomer()) &&
                Objects.equals(getChannel(), that.getChannel()) &&
                Objects.equals(getReferenceUserId(), that.getReferenceUserId()) &&
                Objects.equals(getCallbackRedirect(), that.getCallbackRedirect()) &&
                Objects.equals(getCallbackStatus(), that.getCallbackStatus()) &&
                Objects.equals(getUuid(), that.getUuid()) &&
                Objects.equals(getPaymentSystemReference(), that.getPaymentSystemReference()) &&
                getOutcome() == that.getOutcome();
    }

    @Override
    public int hashCode() {

        int result = Objects.hash(super.hashCode(), getId(), getReferenceClientId(), getReferenceProcessId(), getReferenceInstanceId(), getTenant(), getOrganization(), getIuv(), getPaymentServiceSpecificId(), getRequestCode(), getCurrencyCode(), getPaymentService(), getCausal(), getCreationDate(), getExpiryDate(), getProcessingDate(), getPaymentType(), getTotalAmount(), getNetAmount(), getTransactionResult(), getReceiptContentType(), getProtocolNumber(), getProtocolYear(), getCustomReceiptContentType(), getInvoiceNumber(), getInvoiceYear(), getCustomer(), getChannel(), getReferenceUserId(), getCallbackRedirect(), getCallbackStatus(), getUuid(), getOutcome());
        result = 31 * result + Arrays.hashCode(getReceipt());
        result = 31 * result + Arrays.hashCode(getCustomReceipt());
        return result;
    }

    @Override
    public String toString() {
        return "PaymentInstance{" +
                "id=" + id +
                ", referenceClientId='" + referenceClientId + '\'' +
                ", referenceProcessId='" + referenceProcessId + '\'' +
                ", referenceInstanceId='" + referenceInstanceId + '\'' +
                ", tenant='" + tenant + '\'' +
                ", organization='" + organization + '\'' +
                ", iuv='" + iuv + '\'' +
                ", paymentServiceSpecificId='" + paymentServiceSpecificId + '\'' +
                ", requestCode='" + requestCode + '\'' +
                ", currencyCode='" + currencyCode + '\'' +
                ", paymentService='" + paymentService + '\'' +
                ", causal='" + causal + '\'' +
                ", creationDate=" + creationDate +
                ", expiryDate=" + expiryDate +
                ", processingDate=" + processingDate +
                ", paymentType='" + paymentType + '\'' +
                ", totalAmount=" + totalAmount +
                ", netAmount=" + netAmount +
                ", transactionResult='" + transactionResult + '\'' +
                ", receiptContentType='" + receiptContentType + '\'' +
                ", protocolNumber='" + protocolNumber + '\'' +
                ", protocolYear=" + protocolYear +
                ", customReceiptContentType='" + customReceiptContentType + '\'' +
                ", invoiceNumber='" + invoiceNumber + '\'' +
                ", invoiceYear=" + invoiceYear +
                ", channel=" + channel +
                ", referenceUserId='" + referenceUserId + '\'' +
                ", callbackRedirect='" + callbackRedirect + '\'' +
                ", callbackStatus='" + callbackStatus + '\'' +
                ", uuid='" + uuid + '\'' +
                ", outcome=" + outcome +
                ", paymentSystemReference=" + paymentSystemReference +
                '}';
    }

    public PaymentInstance(String referenceClientId, String referenceProcessId, String referenceInstanceId, String tenant, String organization, String iuv, String paymentServiceSpecificId, String requestCode, String currencyCode, String paymentService, String causal, Date creationDate, Date expiryDate, Date processingDate, String paymentType, Double totalAmount, Double netAmount, String transactionResult, byte[] receipt, String receiptContentType, String protocolNumber, Long protocolYear, byte[] customReceipt, String customReceiptContentType, String invoiceNumber, Long invoiceYear, Customer customer, Channel channel, String referenceUserId, String callbackRedirect, String callbackStatus, String uuid, Outcome outcome, String paymentSystemReference) {
        this.referenceClientId = referenceClientId;
        this.referenceProcessId = referenceProcessId;
        this.referenceInstanceId = referenceInstanceId;
        this.tenant = tenant;
        this.organization = organization;
        this.iuv = iuv;
        this.paymentServiceSpecificId = paymentServiceSpecificId;
        this.requestCode = requestCode;
        this.currencyCode = currencyCode;
        this.paymentService = paymentService;
        this.causal = causal;
        this.creationDate = creationDate;
        this.expiryDate = expiryDate;
        this.processingDate = processingDate;
        this.paymentType = paymentType;
        this.totalAmount = totalAmount;
        this.netAmount = netAmount;
        this.transactionResult = transactionResult;
        this.receipt = receipt;
        this.receiptContentType = receiptContentType;
        this.protocolNumber = protocolNumber;
        this.protocolYear = protocolYear;
        this.customReceipt = customReceipt;
        this.customReceiptContentType = customReceiptContentType;
        this.invoiceNumber = invoiceNumber;
        this.invoiceYear = invoiceYear;
        this.customer = customer;
        this.channel = channel;
        this.referenceUserId = referenceUserId;
        this.callbackRedirect = callbackRedirect;
        this.callbackStatus = callbackStatus;
        this.uuid = uuid;
        this.outcome = outcome;
        this.paymentSystemReference = paymentSystemReference;
    }
}