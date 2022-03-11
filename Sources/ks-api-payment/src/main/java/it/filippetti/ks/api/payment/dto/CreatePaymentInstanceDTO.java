/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.payment.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 *
 * @author dino
 */
@ApiModel(value = "CreatePayment")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreatePaymentInstanceDTO {
    
    @ApiModelProperty(position = 1, required = true, example = "PORTAL")
    private String referenceClientId;
    
    @ApiModelProperty(position = 2, required = true, example = "PrototipoProcesso1.0")
    private String referenceProcessId;
    
    @ApiModelProperty(position = 3, required = true, example = "77")
    private String referenceInstanceId;

    @ApiModelProperty(position = 4, example = "yiuyw-32434-rwerw-werw")
    private String referenceUserId;

    @ApiModelProperty(position = 5, required = false, example = "578")
    private String paymentServiceSpecificId;

    @ApiModelProperty(position = 6, required = false, example = "REFEZIONE_SCOLASTICA")
    private String paymentService;
        
    @ApiModelProperty(position = 7, required = false, example = "REFEZIONE SCOLASTICA 2021")
    private String causal;

    @ApiModelProperty(position = 8, required = true, example = "102.92")
    private Double totalAmount;

    @ApiModelProperty(position = 9, required = true, example = "92.92")
    private Double netAmount;

    @ApiModelProperty(position = 10, required = false, example = "EUR")
    private String currencyCode;

    @ApiModelProperty(position = 11, required = true, example = "Il customer")
    private CustomerDTO customer;

    @ApiModelProperty(position = 12, example = "http://portal.pippo.com/paymentcallback")
    private String callbackRedirect;

    @ApiModelProperty(position = 13, example = "http://portal.pippo.com/paymentcallback")
    private String callbackStatus;
    
    public CreatePaymentInstanceDTO() {
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

    public String getPaymentServiceSpecificId() {
        return paymentServiceSpecificId;
    }

    public void setPaymentServiceSpecificId(String paymentServiceSpecificId) {
        this.paymentServiceSpecificId = paymentServiceSpecificId;
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

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
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


    @Override
    public String toString() {
        return "CreatePaymentInstanceDTO{" +
                "referenceClientId='" + referenceClientId + '\'' +
                ", referenceProcessId='" + referenceProcessId + '\'' +
                ", referenceInstanceId='" + referenceInstanceId + '\'' +
                ", referenceUserId='" + referenceUserId + '\'' +
                ", paymentServiceSpecificId='" + paymentServiceSpecificId + '\'' +
                ", paymentService='" + paymentService + '\'' +
                ", causal='" + causal + '\'' +
                ", totalAmount=" + totalAmount +
                ", netAmount=" + netAmount +
                ", currencyCode='" + currencyCode + '\'' +
                ", customer=" + customer +
                ", callbackRedirect='" + callbackRedirect + '\'' +
                ", callbackStatus='" + callbackStatus + '\'' +
                '}';
    }
}
