/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.payment.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import it.filippetti.ks.api.payment.model.Outcome;
import java.util.Date;

/**
 *
 * @author dino
 */
@ApiModel(value = "CreateTransaction")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateTransactionDTO {
    
    @ApiModelProperty(position = 1, example = "S6trAhS")
    private String shopCode;

    @ApiModelProperty(position = 2, example = "2021-02-02T10:19:04.657Z")
    private Date requestDate;

    @ApiModelProperty(position = 3, example = "2021-02-02T10:19:04.657Z")
    private Date responseDate;
    
    @ApiModelProperty(position = 4, example = "s001")
    private String paymentService;

    @ApiModelProperty(position = 5, example = "3")
    private Integer rating;

    @ApiModelProperty(position = 6, example = "1")
    private Long paymentId;

    @ApiModelProperty(position = 7, example = "pippo")
    private String userId;
    
    @ApiModelProperty(position = 8, example = "OK")
    private Outcome outcome;
    
    @ApiModelProperty(position = 9, example = "aaa")
    private String organization;
    
    public CreateTransactionDTO() {
    }

    public String getShopCode() {
        return shopCode;
    }

    public void setShopCode(String shopCode) {
        this.shopCode = shopCode;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    public Date getResponseDate() {
        return responseDate;
    }

    public void setResponseDate(Date responseDate) {
        this.responseDate = responseDate;
    }

    public String getPaymentService() {
        return paymentService;
    }

    public void setPaymentService(String paymentService) {
        this.paymentService = paymentService;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }
    
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    public Outcome getOutcome() {
        return outcome;
    }

    public void setOutcome(Outcome outcome) {
        this.outcome = outcome;
    }
    
    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }
    
    @Override
    public String toString() {
        return "{" +
            ", shopCode='" + getShopCode() + "'" +
            ", requestDate='" + getRequestDate() + "'" +
            ", responseDate='" + getResponseDate() + "'" +
            ", paymentService='" + getPaymentService() + "'" +
            ", rating='" + getRating() + "'" +
            ", userId='" + getUserId()+ "'" +
            ", paymentId='" + getPaymentId()+ "'" +
            ", outcome='" + getOutcome()+ "'" +
            ", organization='" + getOrganization()+ "'" +
            "}";
    }
    
}
