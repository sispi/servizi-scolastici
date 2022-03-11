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
import javax.validation.constraints.NotNull;

/**
 *
 * @author dino
 */
@ApiModel(value = "UpdatePayment")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdatePaymentInstanceDTO {
    
    @NotNull
    @ApiModelProperty(position = 1, example = "1")
    private Long id;
    
    @ApiModelProperty(position = 2, required = true, example = "82")
    private Long referenceId;
    
    @ApiModelProperty(position = 3, required = true, example = "Instance")
    private String referenceClass;
    
    @ApiModelProperty(position = 4, required = true, example = "PORTAL")
    private String referenceProject;
    
    @ApiModelProperty(position = 5, required = true, example = "9292348234234")
    private String iuv;

    @ApiModelProperty(position = 6, required = false, example = "578")
    private String paymentServiceSpecificId;

    @ApiModelProperty(position = 7, required = false, example = "OT6523")
    private String requestCode;

    @ApiModelProperty(position = 8, required = false, example = "EUR")
    private String currencyCode;
    
    @ApiModelProperty(position = 9, required = false, example = "VISA")
    private String paymentService;
        
    @ApiModelProperty(position = 10, required = false, example = "Tassa scolastica")
    private String causal;

    @ApiModelProperty(position = 11, required = false, example = "2021-02-02T10:19:04.657Z")
    private Date expiryDate;

    @ApiModelProperty(position = 12, required = false, example = "2021-02-02T10:19:04.657Z")
    private Date processingDate;

    @ApiModelProperty(position = 13, required = false, example = "CC")
    private String paymentType;

    @ApiModelProperty(position = 14, required = true, example = "102.92")
    private Double totalAmount;

    @ApiModelProperty(position = 15, required = true, example = "92.92")
    private Double netAmount;

    @ApiModelProperty(position = 16, required = false, example = "OK")
    private String transactionResult;

    @ApiModelProperty(position = 17, required = false, notes = "The receipt blob", example = "null")
    private byte[] receipt;

    @ApiModelProperty(position = 18, required = false, example = "document/pdf")
    private String receiptContentType;

    @ApiModelProperty(position = 19, required = false, example = "P_01")
    private String protocolNumber;
    
    @ApiModelProperty(position = 20, required = false, example = "2021")
    private Long protocolYear;
    
    @ApiModelProperty(position = 21, required = false, notes = "The custom receipt blob", example = "null")
    private byte[] customReceipt;

    @ApiModelProperty(position = 22, required = false, example = "document/pdf")
    private String customReceiptContentType;

    @ApiModelProperty(position = 23, required = false, example = "AB/45")
    private String invoiceNumber;
    
    @ApiModelProperty(position = 24, required = false, example = "2021")
    private Long invoiceYear;
    
    @ApiModelProperty(position = 25, required = true, example = "The customer")
    private Long customerId;

    public UpdatePaymentInstanceDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
    
    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", referenceId='" + getReferenceId() + "'" +
            ", referenceProject='" + getReferenceProject() + "'" +
            ", iuv='" + getIuv() + "'" +
            ", paymentServiceSpecificId='" + getPaymentServiceSpecificId() + "'" +
            ", requestCode='" + getRequestCode() + "'" +
            ", currencyCode='" + getCurrencyCode() + "'" +
            ", paymentService='" + getPaymentService() + "'" +
            ", causal='" + getCausal() + "'" +
            ", expiryDate='" + getExpiryDate() + "'" +
            ", processingDate='" + getProcessingDate() + "'" +
            ", paymentType='" + getPaymentType() + "'" +
            ", totalAmount='" + getTotalAmount() + "'" +
            ", netAmount='" + getNetAmount() + "'" +
            ", transactionResult='" + getTransactionResult() + "'" +
            ", receipt='" + getReceipt() + "'" +
            ", receiptContentType='" + getReceiptContentType() + "'" +
            ", protocolNumber='" + getProtocolNumber() + "'" +
            ", protocolYear='" + getProtocolYear() + "'" +
            ", customReceipt='" + getCustomReceipt() + "'" +
            ", customReceiptContentType='" + getCustomReceiptContentType() + "'" +
            ", invoiceNumber='" + getInvoiceNumber() + "'" +
            ", invoiceYear='" + getInvoiceYear() + "'" +
            ", customerId='" + getCustomerId()+ "'" +
            "}";
    }
}
