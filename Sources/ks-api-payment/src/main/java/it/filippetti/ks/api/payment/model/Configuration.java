/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.payment.model;

import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author dino
 */
@Entity
@Table(name = "CONFIGURATION")
public class Configuration extends Auditable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)    
    private Long id;
    
    @Basic
    @Column(nullable = true)
    private Boolean paymentEnabled;

    @Basic
    @Column(nullable = true)
    private String secretKey;
    
    @Basic
    @Column(nullable = true)
    private String secretUser;
    
    @Basic
    @Column(nullable = true)
    private String servicePassword;
    
    @Basic
    @Column(nullable = true)
    private String terminalId;
    
    @Basic
    @Column(nullable = true)
    private String currencyCode;

    @Basic
    @Column(nullable = true)
    private String langId;

    @Basic
    @Column(nullable = true)
    private String cashMode;

    @Basic
    @ManyToOne(fetch = FetchType.LAZY, optional = false, targetEntity = Channel.class)
    private Channel channel;
    
    @Basic
    @Column(nullable = false)
    private Boolean defaultConf;
    
    @Basic
    @Column(nullable = false)
    private String tenant;
    
    @Basic
    @Column(nullable = false)
    private String organization;
    
    public Configuration() {
    }

    public Configuration(
        Boolean paymentEnabled, 
        String secretKey, 
        String secretUser, 
        String servicePassword, 
        String terminalId, 
        String currencyCode, 
        String langId, 
        String cashMode, 
        Channel channel,
        Boolean defaultConf,
        String tenant,
        String organization) {
            this.paymentEnabled = paymentEnabled;
            this.secretKey = secretKey;
            this.secretUser = secretUser;
            this.servicePassword = servicePassword;
            this.terminalId = terminalId;
            this.currencyCode = currencyCode;
            this.langId = langId;
            this.cashMode = cashMode;
            this.channel = channel;
            this.defaultConf = defaultConf;
            this.tenant = tenant;
            this.organization = organization;
    }

    public Configuration(
        Long id,
        Boolean paymentEnabled, 
        String secretKey, 
        String secretUser, 
        String servicePassword, 
        String terminalId, 
        String currencyCode, 
        String langId, 
        String cashMode, 
        Channel channel,
        Boolean defaultConf,
        String tenant,
        String organization) {
            this.id = id;
            this.paymentEnabled = paymentEnabled;
            this.secretKey = secretKey;
            this.secretUser = secretUser;
            this.servicePassword = servicePassword;
            this.terminalId = terminalId;
            this.currencyCode = currencyCode;
            this.langId = langId;
            this.cashMode = cashMode;
            this.channel = channel;
            this.defaultConf = defaultConf;
            this.tenant = tenant;
            this.organization = organization;
    }

    @Override
    public Long getId() {
        return id;
    }

    public Boolean getDefaultConf() {
        return defaultConf;
    }

    public void setDefaultConf(Boolean defaultConf) {
        this.defaultConf = defaultConf;
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

    public Channel getChannel() {
        return this.channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Configuration)) {
            return false;
        }
        Configuration configuration = (Configuration) o;
        return Objects.equals(id, configuration.id) && Objects.equals(paymentEnabled, configuration.paymentEnabled) && Objects.equals(secretKey, configuration.secretKey) && Objects.equals(secretUser, configuration.secretUser) && Objects.equals(servicePassword, configuration.servicePassword) && Objects.equals(terminalId, configuration.terminalId) && Objects.equals(currencyCode, configuration.currencyCode) && Objects.equals(langId, configuration.langId) && Objects.equals(cashMode, configuration.cashMode) && Objects.equals(channel, configuration.channel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, paymentEnabled, secretKey, secretUser, servicePassword, terminalId, currencyCode, langId, cashMode, channel);
    }

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
            "}";
    }


}
