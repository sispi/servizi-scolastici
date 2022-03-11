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
@ApiModel(value = "Configuration") 
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConfigurationDTO {
    
    @ApiModelProperty(position = 1, example = "0") 
    private Long id;
    
    @ApiModelProperty(position = 2, example = "true")
    private Boolean paymentEnabled;

    @ApiModelProperty(position = 3, example = "Ar6DH767asda78")
    private String secretKey;
    
    @ApiModelProperty(position = 4, example = "mariorossi")
    private String secretUser;
    
    @ApiModelProperty(position = 5, example = "hy7sastg76s")
    private String servicePassword;
    
    @ApiModelProperty(position = 6, example = "pt155")
    private String terminalId;

    @ApiModelProperty(position = 7, example = "EUR")
    private String currencyCode;

    @ApiModelProperty(position = 8, example = "it")
    private String langId;

    @ApiModelProperty(position = 9, example = "immediato")
    private String cashMode;

    @ApiModelProperty(position = 10, example = "channel")
    private ChannelDTO channel;
    
    @ApiModelProperty(position = 11, example = "false")
    private Boolean defaultConf;

    public ConfigurationDTO() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getDefaultConf() {
        return defaultConf;
    }

    public void setDefaultConf(Boolean defaultConf) {
        this.defaultConf = defaultConf;
    }

    public Boolean isPaymentEnabled() {
        return this.paymentEnabled;
    }

    public Boolean getPaymentEnabled() {
        return this.paymentEnabled;
    }

    public void setPaymentEnabled(Boolean paymentEnabled) {
        this.paymentEnabled = paymentEnabled;
    }

    public String getSecretKey() {
        return this.secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getSecretUser() {
        return this.secretUser;
    }

    public void setSecretUser(String secretUser) {
        this.secretUser = secretUser;
    }

    public String getServicePassword() {
        return this.servicePassword;
    }

    public void setServicePassword(String servicePassword) {
        this.servicePassword = servicePassword;
    }

    public String getTerminalId() {
        return this.terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getCurrencyCode() {
        return this.currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getLangId() {
        return this.langId;
    }

    public void setLangId(String langId) {
        this.langId = langId;
    }

    public String getCashMode() {
        return this.cashMode;
    }

    public void setCashMode(String cashMode) {
        this.cashMode = cashMode;
    }

    public ChannelDTO getChannel() {
        return this.channel;
    }

    public void setChannel(ChannelDTO channel) {
        this.channel = channel;
    }
    
    
    @ApiModel("Page<ConfigurationDTO>")
    public static class Page extends PageDTO<ConfigurationDTO> {}
    

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", paymentEnabled='" + isPaymentEnabled() + "'" +
            ", secretKey='" + getSecretKey() + "'" +
            ", secretUser='" + getSecretUser() + "'" +
            ", servicePassword='" + getServicePassword() + "'" +
            ", terminalId='" + getTerminalId() + "'" +
            ", currencyCode='" + getCurrencyCode() + "'" +
            ", langId='" + getLangId() + "'" +
            ", cashMode='" + getCashMode() + "'" +
            ", channel='" + getChannel() + "'" + 
            ", defaultConf='" + getDefaultConf() + "'" +
            "}";
    }
    
}
