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
@ApiModel("UpdateConfiguration")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateConfigurationDTO {
    
    @ApiModelProperty(position = 2, example = "true", required = false)
    private Boolean paymentEnabled;

    @ApiModelProperty(position = 3, example = "Ar6DH767asda78", required = false)
    private String secretKey;
    
    @ApiModelProperty(position = 4, example = "mariorossi", required = false)
    private String secretUser;
    
    @ApiModelProperty(position = 5, example = "hy7sastg76s", required = false)
    private String servicePassword;
    
    @ApiModelProperty(position = 6, example = "pt155", required = false)
    private String terminalId;

    @ApiModelProperty(position = 7, example = "EUR", required = false)
    private String currencyCode;

    @ApiModelProperty(position = 8, example = "it", required = false)
    private String langId;

    @ApiModelProperty(position = 9, example = "immediato", required = false)
    private String cashMode;

    @ApiModelProperty(position = 10, example = "1", required = true)
    private Long channelId;
    
    @ApiModelProperty(position = 11, example = "false", required = true)
    private Boolean defaultConf;

    public UpdateConfigurationDTO() {
    }

    public Boolean getPaymentEnabled() {
        return paymentEnabled;
    }

    public void setPaymentEnabled(Boolean paymentEnabled) {
        this.paymentEnabled = paymentEnabled;
    }

    public Boolean getDefaultConf() {
        return defaultConf;
    }

    public void setDefaultConf(Boolean defaultConf) {
        this.defaultConf = defaultConf;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getSecretUser() {
        return secretUser;
    }

    public void setSecretUser(String secretUser) {
        this.secretUser = secretUser;
    }

    public String getServicePassword() {
        return servicePassword;
    }

    public void setServicePassword(String servicePassword) {
        this.servicePassword = servicePassword;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getLangId() {
        return langId;
    }

    public void setLangId(String langId) {
        this.langId = langId;
    }

    public String getCashMode() {
        return cashMode;
    }

    public void setCashMode(String cashMode) {
        this.cashMode = cashMode;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }
    
    @Override
    public String toString() {
        return "{" +
            ", paymentEnabled='" + getPaymentEnabled() + "'" +
            ", secretKey='" + getSecretKey() + "'" +
            ", secretUser='" + getSecretUser() + "'" +
            ", servicePassword='" + getServicePassword() + "'" +
            ", terminalId='" + getTerminalId() + "'" +
            ", currencyCode='" + getCurrencyCode() + "'" +
            ", langId='" + getLangId() + "'" +
            ", cashMode='" + getCashMode() + "'" +
            ", channelId='" + getChannelId()+ "'" +
            ", defaultConf='" + getDefaultConf() + "'" +
            "}";
    }
}
