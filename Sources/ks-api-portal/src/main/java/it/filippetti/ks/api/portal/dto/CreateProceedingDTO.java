/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.portal.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import javax.validation.constraints.NotNull;

/**
 *
 * @author dino
 */
@ApiModel("CreateProceeding")
public class CreateProceedingDTO {
    
    @ApiModelProperty(position = 1, required = true, example = "1", notes = "The proceeding id")
    @NotNull
    private Long serviceId;
    
    @ApiModelProperty(position = 2, required = true, example = "Rilascio licenza di pesca") 
    private String title;
    
    @ApiModelProperty(position = 3, required = false, example = "test") 
    private String body;
    
    @ApiModelProperty(position = 4, required = false, example = "1") 
    private Long configurationId;
    
    @ApiModelProperty(position = 5, required = false, example = "Test timeline") 
    private String processId;
    
    @ApiModelProperty(position = 6, required = false, example = "APP_REQ") 
    private String applicantRequirement;
    
    @ApiModelProperty(position = 7, required = false, example = "2 marche da bollo di 16 euro") 
    private String costs;
    
    @ApiModelProperty(position = 8, required = false, example = "Carta d'Identità") 
    private String attachments;
    
    @ApiModelProperty(position = 9, required = false, example = "Ufficio Caccia e Pesca") 
    private String howToSubmit;
    
    @ApiModelProperty(position = 10, required = false, example = "90 giorni") 
    private String timeNeeded;
    
    @ApiModelProperty(position = 11, required = false, example = "Mario Rossi") 
    private String accountableStaff;
    
    @ApiModelProperty(position = 12, example = "Ufficio Rilasci") 
    private String accountableOffice;
    
    @ApiModelProperty(position = 13, required = false, example = "Liugi Bianchi") 
    private String operatorStaff;
    
    @ApiModelProperty(position = 14, required = false, example = "true") 
    private Boolean isActive;
    
    @ApiModelProperty(position = 15, required = false, example = "true") 
    private Boolean isOnline;
    
    @ApiModelProperty(position = 16, required = false, example = "false") 
    private Boolean multipleInstance;
    
    @ApiModelProperty(position = 17, required = false, example = "false") 
    private Boolean filingPlan;
    
    @ApiModelProperty(position = 18, required = false, example = "2021-02-02T10:19:04.657Z") 
    private Date startDate;
    
    @ApiModelProperty(position = 19, required = false, example = "2021-02-02T10:19:04.657Z") 
    private Date endDate;
    
    @ApiModelProperty(position = 20, required = false, example = "false") 
    private Boolean updating;
    
    @ApiModelProperty(position = 21, required = false, example = "false")
    private Boolean activeCommunication;
    
    @ApiModelProperty(position = 22, required = false, example = "1") 
    private Integer version;
    
    @ApiModelProperty(position = 23, required = false, example = "1")
    private String uoId;
    
    @ApiModelProperty(position = 24, required = false, example = "nulla_osta_porto_darmi.js")
    private String customTemplate;
    
    @ApiModelProperty(position = 25, required = true, example = "false")
    private Boolean showCustomTemplate;
    
    @ApiModelProperty(position = 26, required = false, example = "false")
    private Boolean uniqueInstance;
    
    @ApiModelProperty(position = 27, required = false, example = "false")
    private Boolean sendIfExpired;
    
    @ApiModelProperty(position = 28, required = false, example = "false")
    private Boolean singleInstance;

    public CreateProceedingDTO() {
    }

    public Boolean getSingleInstance() {
        return singleInstance;
    }

    public void setSingleInstance(Boolean singleInstance) {
        this.singleInstance = singleInstance;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public Boolean getUniqueInstance() {
        return uniqueInstance;
    }

    public void setUniqueInstance(Boolean uniqueInstance) {
        this.uniqueInstance = uniqueInstance;
    }

    public Boolean getSendIfExpired() {
        return sendIfExpired;
    }

    public void setSendIfExpired(Boolean sendIfExpired) {
        this.sendIfExpired = sendIfExpired;
    }

    public String getCustomTemplate() {
        return customTemplate;
    }

    public void setCustomTemplate(String customTemplate) {
        this.customTemplate = customTemplate;
    }

    public Boolean getShowCustomTemplate() {
        return showCustomTemplate;
    }

    public void setShowCustomTemplate(Boolean showCustomTemplate) {
        this.showCustomTemplate = showCustomTemplate;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Long getConfigurationId() {
        return configurationId;
    }

    public void setConfigurationId(Long configurationId) {
        this.configurationId = configurationId;
    }

    public String getApplicantRequirement() {
        return applicantRequirement;
    }

    public void setApplicantRequirement(String applicantRequirement) {
        this.applicantRequirement = applicantRequirement;
    }

    public String getCosts() {
        return costs;
    }

    public void setCosts(String costs) {
        this.costs = costs;
    }

    public String getAttachments() {
        return attachments;
    }

    public void setAttachments(String attachments) {
        this.attachments = attachments;
    }

    public String getHowToSubmit() {
        return howToSubmit;
    }

    public void setHowToSubmit(String howToSubmit) {
        this.howToSubmit = howToSubmit;
    }

    public String getTimeNeeded() {
        return timeNeeded;
    }

    public void setTimeNeeded(String timeNeeded) {
        this.timeNeeded = timeNeeded;
    }

    public String getAccountableStaff() {
        return accountableStaff;
    }

    public void setAccountableStaff(String accountableStaff) {
        this.accountableStaff = accountableStaff;
    }

    public String getAccountableOffice() {
        return accountableOffice;
    }

    public void setAccountableOffice(String accountableOffice) {
        this.accountableOffice = accountableOffice;
    }

    public String getOperatorStaff() {
        return operatorStaff;
    }

    public void setOperatorStaff(String operatorStaff) {
        this.operatorStaff = operatorStaff;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(Boolean isOnline) {
        this.isOnline = isOnline;
    }

    public Boolean getMultipleInstance() {
        return multipleInstance;
    }

    public void setMultipleInstance(Boolean multipleInstance) {
        this.multipleInstance = multipleInstance;
    }

    public Boolean getFilingPlan() {
        return filingPlan;
    }

    public void setFilingPlan(Boolean filingPlan) {
        this.filingPlan = filingPlan;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Boolean getUpdating() {
        return updating;
    }

    public void setUpdating(Boolean updating) {
        this.updating = updating;
    }

    public Boolean getActiveCommunication() {
        return activeCommunication;
    }

    public void setActiveCommunication(Boolean activeCommunication) {
        this.activeCommunication = activeCommunication;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getUoId() {
        return uoId;
    }

    public void setUoId(String uoId) {
        this.uoId = uoId;
    }

    @Override
    public String toString() {
        return "{"
                + "serviceId='" + getServiceId() + "'"
                + ", title='" + getTitle() + "'"
                + ", body='" + getBody() + "'"
                + ", configurationId='" + getConfigurationId() + "'"
                + ", applicantRequirement='" + getApplicantRequirement() + "'"
                + ", costs='" + getCosts() + "'"
                + ", attachments='" + getAttachments() + "'"
                + ", howToSubmit='" + getHowToSubmit() + "'"
                + ", timeNeeded='" + getTimeNeeded() + "'"
                + ", accountableStaff='" + getAccountableStaff() + "'"
                + ", accountableOffice='" + getAccountableOffice() + "'"
                + ", operatorStaff='" + getOperatorStaff() + "'"
                + ", isActive='" + getIsActive() + "'"
                + ", isOnline='" + getIsOnline() + "'"
                + ", multipleInstance='" + getMultipleInstance() + "'"
                + ", uniqueInstance='" + getUniqueInstance() + "'"
                + ", singleInstance='" + getSingleInstance() + "'"
                + ", filingPlan='" + getFilingPlan() + "'"
                + ", startDate='" + getStartDate() + "'"
                + ", endDate='" + getEndDate() + "'"
                + ", updating='" + getUpdating() + "'"
                + ", activeCommunication='" + getActiveCommunication() + "'"
                + ", version='" + getVersion() + "'"
                + ", uoId='" + getUoId() + "'"
                + ", customTemplate='" + getCustomTemplate() + "'"
                + ", showCustomTemplate='" + getShowCustomTemplate() + "'"
                + "}";
    }
    
}
