/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.payment.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 *
 * @author dino
 */
@ApiModel("CreateConfiguration")
public class CreateConfigurationDTO {
    
    @ApiModelProperty(position = 1, required = false, example = "true")
    private Boolean paymentEnabled;

    @ApiModelProperty(position = 2, required = false, example = "CP001")
    private String secretKey;
    
    @ApiModelProperty(position = 3, required = false, example = "WS_CP001")
    private String secretUser;
    
    @ApiModelProperty(position = 4, required = false, example = "CP001_PWD")
    private String servicePassword;
    
    @ApiModelProperty(position = 5, required = false, example = "pt155")
    private String terminalId;

    @ApiModelProperty(position = 6, required = false, example = "EUR")
    private String currencyCode;

    @ApiModelProperty(position = 7, required = false, example = "it")
    private String langId;

    @ApiModelProperty(position = 8, required = false, example = "immediato")
    private String cashMode;
    
    @ApiModelProperty(position = 9, required = true,  example = "false")
    private Boolean defaultConf;

    public CreateConfigurationDTO() {
    }

    public Boolean getDefaultConf() {
        return defaultConf;
    }

    public void setDefaultConf(Boolean defaultConf) {
        this.defaultConf = defaultConf;
    }

    public Boolean getPaymentEnabled() {
        return paymentEnabled;
    }

    public void setPaymentEnabled(Boolean paymentEnabled) {
        this.paymentEnabled = paymentEnabled;
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
    
    @Override
    public String toString() {
        return "{" +
            ", paymentEnabled='" + getPaymentEnabled()+ "'" +
            ", secretKey='" + getSecretKey() + "'" +
            ", secretUser='" + getSecretUser() + "'" +
            ", servicePassword='" + getServicePassword() + "'" +
            ", terminalId='" + getTerminalId() + "'" +
            ", currencyCode='" + getCurrencyCode() + "'" +
            ", langId='" + getLangId() + "'" +
            ", cashMode='" + getCashMode() + "'" +
            ", defaultConf='" + getDefaultConf() + "'" +
            "}";
    }
}
