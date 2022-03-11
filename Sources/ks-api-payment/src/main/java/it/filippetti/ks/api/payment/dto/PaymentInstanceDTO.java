/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.payment.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 *
 * @author dino
 */
@ApiModel(value = "Payment") 
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentInstanceDTO {
    
    @ApiModelProperty(position = 1, example = "0") 
    private Long id;

    @ApiModelProperty(position = 2, required = true, example = "PORTAL")
    private String referenceClientId;

    @ApiModelProperty(position = 3, required = true, example = "PrototipoProcesso1.0")
    private String referenceProcessId;

    @ApiModelProperty(position = 4, required = true, example = "77")
    private String referenceInstanceId;

    @ApiModelProperty(position = 5, example = "yiuyw-32434-rwerw-werw")
    private String referenceUserId;
    
    @ApiModelProperty(position = 5, example = "9292348234234")
    private String iuv;

    @ApiModelProperty(position = 6, example = "578")
    private String paymentServiceSpecificId;

    @ApiModelProperty(position = 7, example = "OT6523")
    private String requestCode;

    @ApiModelProperty(position = 8, example = "EUR")
    private String currencyCode;
    
    @ApiModelProperty(position = 9, example = "VISA")
    private String paymentService;
        
    @ApiModelProperty(position = 10, example = "Tassa scolastica")
    private String causal;

    @ApiModelProperty(position = 11, example = "2021-02-02T10:19:04.657Z")
    private Date expiryDate;

    @ApiModelProperty(position = 12, example = "2021-02-02T10:19:04.657Z")
    private Date processingDate;

    @ApiModelProperty(position = 13, example = "CC")
    private String paymentType;

    @ApiModelProperty(position = 14, example = "102.92")
    private Double totalAmount;

    @ApiModelProperty(position = 15, example = "92.92")
    private Double netAmount;

    @ApiModelProperty(position = 16, example = "OK")
    private String transactionResult;

    @ApiModelProperty(position = 17, notes = "The receipt blob", example = "null")
    private byte[] receipt;

    @ApiModelProperty(position = 18, example = "document/pdf")
    private String receiptContentType;

    @ApiModelProperty(position = 19, example = "P_01")
    private String protocolNumber;
    
    @ApiModelProperty(position = 20, example = "2021")
    private Long protocolYear;
    
    @ApiModelProperty(position = 21, notes = "The custom receipt blob", example = "null")
    private byte[] customReceipt;

    @ApiModelProperty(position = 22, example = "document/pdf")
    private String customReceiptContentType;

    @ApiModelProperty(position = 23, example = "AB/45")
    private String invoiceNumber;
    
    @ApiModelProperty(position = 24, example = "2021")
    private Long invoiceYear;
    
    @ApiModelProperty(position = 25, example = "The channel")
    private ChannelDTO channel;
    
    @ApiModelProperty(position = 26, example = "The customer")
    private CustomerDTO customer;

    @ApiModelProperty(position = 27, example = "http://portal.pippo.com/paymentcallback")
    private String callbackRedirect;

    @ApiModelProperty(position = 28, example = "http://portal.pippo.com/paymentcallback")
    private String callbackStatus;
    
    @ApiModelProperty(position = 29, example = "123e4567-e89b-12d3-a456-426614174000")
    private String uuid;

    @ApiModelProperty(position = 30, example = "hasdha23423dshd")
    private String paymentSystemReference;

    @ApiModelProperty(position = 31, example = "2021-02-02T10:19:04.657Z")
    private Date creationDate;
    

    public PaymentInstanceDTO() {
    }

    @ApiModel("Page<Payment>")
    public static class Page extends PageDTO<PaymentInstanceDTO> {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getReferenceUserId() {
        return referenceUserId;
    }

    public void setReferenceUserId(String referenceUserId) {
        this.referenceUserId = referenceUserId;
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

    public ChannelDTO getChannel() {
        return channel;
    }

    public void setChannel(ChannelDTO channel) {
        this.channel = channel;
    }

    public CustomerDTO getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDTO customer) {
        this.customer = customer;
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

    public String getPaymentSystemReference() {
        return paymentSystemReference;
    }

    public void setPaymentSystemReference(String paymentSystemReference) {
        this.paymentSystemReference = paymentSystemReference;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public String toString() {
        return "PaymentInstanceDTO{" +
                "id=" + id +
                ", referenceClientId='" + referenceClientId + '\'' +
                ", referenceProcessId='" + referenceProcessId + '\'' +
                ", referenceInstanceId='" + referenceInstanceId + '\'' +
                ", referenceUserId='" + referenceUserId + '\'' +
                ", iuv='" + iuv + '\'' +
                ", paymentServiceSpecificId='" + paymentServiceSpecificId + '\'' +
                ", requestCode='" + requestCode + '\'' +
                ", currencyCode='" + currencyCode + '\'' +
                ", paymentService='" + paymentService + '\'' +
                ", causal='" + causal + '\'' +
                ", expiryDate=" + expiryDate +
                ", processingDate=" + processingDate +
                ", paymentType='" + paymentType + '\'' +
                ", totalAmount=" + totalAmount +
                ", netAmount=" + netAmount +
                ", transactionResult='" + transactionResult + '\'' +
                ", receipt=" + receipt +
                ", receiptContentType='" + receiptContentType + '\'' +
                ", protocolNumber='" + protocolNumber + '\'' +
                ", protocolYear=" + protocolYear +
                ", customReceipt=" + customReceipt +
                ", customReceiptContentType='" + customReceiptContentType + '\'' +
                ", invoiceNumber='" + invoiceNumber + '\'' +
                ", invoiceYear=" + invoiceYear +
                ", channel=" + channel +
                ", customer=" + customer +
                ", callbackRedirect='" + callbackRedirect + '\'' +
                ", callbackStatus='" + callbackStatus + '\'' +
                ", uuid='" + uuid + '\'' +
                ", paymentSystemReference='" + paymentSystemReference + '\'' +
                ", creationDate=" + creationDate +
                '}';
    }
}
