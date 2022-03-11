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
@ApiModel(value = "Transaction") 
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionDTO {
    
    @ApiModelProperty(position = 1, example = "0") 
    private Long id;
    
    @ApiModelProperty(position = 2, example = "T001")
    private String tenant;
    
    @ApiModelProperty(position = 3, example = "AOO1")
    private String organization;

    @ApiModelProperty(position = 4, example = "S6trAhS")
    private String shopCode;

    @ApiModelProperty(position = 5, example = "2021-02-02T10:19:04.657Z")
    private Date requestDate;

    @ApiModelProperty(position = 6, example = "2021-02-02T10:19:04.657Z")
    private Date responseDate;
    
    @ApiModelProperty(position = 7, example = "s001")
    private String paymentService;

    @ApiModelProperty(position = 8, example = "3")
    private Integer rating;
    
    @ApiModelProperty(position = 9, example = "129")
    private String userId;

    @ApiModelProperty(position = 10, example = "The payment")
    private PaymentInstanceDTO paymentInstance;

    public TransactionDTO() {
    }


    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenant() {
        return this.tenant;
    }

    public void setTenant(String tenant) {
        this.tenant = tenant;
    }

    public String getOrganization() {
        return this.organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getShopCode() {
        return this.shopCode;
    }

    public void setShopCode(String shopCode) {
        this.shopCode = shopCode;
    }

    public Date getRequestDate() {
        return this.requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    public Date getResponseDate() {
        return this.responseDate;
    }

    public void setResponseDate(Date responseDate) {
        this.responseDate = responseDate;
    }

    public String getPaymentService() {
        return this.paymentService;
    }

    public void setPaymentService(String paymentService) {
        this.paymentService = paymentService;
    }

    public Integer getRating() {
        return this.rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public PaymentInstanceDTO getPaymentInstance() {
        return this.paymentInstance;
    }

    public void setPaymentInstance(PaymentInstanceDTO paymentInstance) {
        this.paymentInstance = paymentInstance;
    }
    
    @ApiModel("Page<TransactionDTO>")
    public static class Page extends PageDTO<TransactionDTO> {}

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", tenant='" + getTenant() + "'" +
            ", organization='" + getOrganization() + "'" +
            ", shopCode='" + getShopCode() + "'" +
            ", requestDate='" + getRequestDate() + "'" +
            ", responseDate='" + getResponseDate() + "'" +
            ", paymentService='" + getPaymentService() + "'" +
            ", rating='" + getRating() + "'" +
            ", userId='" + getUserId() + "'" +
            ", paymentInstance='" + getPaymentInstance() + "'" +
            "}";
    }

    
    
}
