/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.portal.model;

import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

/**
 *
 * @author dino
 */
@Entity
@Table(name = "PROCEEDING", uniqueConstraints = @UniqueConstraint(columnNames = {"tenant", "processId"}))
@Sortables(
    defaultSort = "id:ASC",
    value = {
        @Sortable(property = "id")
    }
)
public class Proceeding extends Auditable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)    
    private Long id;
    
    @Basic
    @ManyToOne(fetch = FetchType.LAZY, optional = false, targetEntity = Service.class)
    private Service service;
    
    @Basic
    @Column(nullable = false)
    private String title;
    
    @Lob
    @Column(nullable = true)
    private String body;
    
    @Basic
    @Column(nullable = true)
    private Long configurationId;
    
    @Basic
    @Column(nullable = true)
    private String processId;
    
    @Basic
    @Column(nullable = true)
    private String applicantRequirement;
    
    @Basic
    @Column(nullable = true)
    private String costs;
    
    @Basic
    @Column(nullable = true)
    private String attachments;
    
    @Basic
    @Column(nullable = true)
    private String howToSubmit;
    
    @Basic
    @Column(nullable = true)
    private String timeNeeded;
    
    @Basic
    @Column(nullable = true)
    private String accountableStaff;
    
    @Basic
    @Column(nullable = true)
    private String accountableOffice;
    
    @Basic
    @Column(nullable = true)
    private String operatorStaff;
    
    @Basic
    @Column(nullable = true)
    private Boolean isActive;
    
    @Basic
    @Column(nullable = true)
    private Boolean isOnline;
    
    @Basic
    @Column(nullable = true)
    private Boolean multipleInstance;
    
    @Basic
    @Column(nullable = true)
    private Boolean filingPlan;
    
    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = true)
    private Date startDate;
    
    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = true)
    private Date endDate;
    
    @Basic
    @Column(nullable = true)
    private Boolean updating;
    
    @Basic
    @Column(nullable = true)
    private Boolean activeCommunication;
    
    @Basic
    @Column(nullable = true)
    private Integer version;
    
    @Basic
    @Column(nullable = false)    
    private String tenant;
    
    @Basic
    @Column(nullable = false)
    private String organization;
    
    @Basic
    @Column(nullable = true)
    private String uo;
    
    @Basic
    @Column(nullable = true)
    private String customTemplate;
    
    @Basic
    @Column(nullable = false)
    private Boolean showCustomTemplate;
    
    @Basic
    @Column(nullable = true)
    private Boolean uniqueInstance;
    
    @Basic
    @Column(nullable = true)
    private Boolean sendIfExpired;
    
    @Basic
    @Column(nullable = true)
    private Boolean singleInstance;

    public Proceeding() {
    }

    public Proceeding(
            Service service,
            String title,
            String body,
            Long configurationId,
            String processId,
            String applicantRequirement,
            String costs,
            String attachments,
            String howToSubmit,
            String timeNeeded,
            String accountableStaff,
            String accountableOffice,
            String operatorStaff,
            Boolean isActive,
            Boolean isOnline,
            Boolean multipleInstance,
            Boolean filingPlan,
            Date startDate,
            Date endDate,
            Boolean updating,
            Boolean activeCommunication,
            Integer version,
            String tenant,
            String organization,
            String uo,
            String customTemplate,
            Boolean showCustomTemplate,
            Boolean uniqueInstance,
            Boolean sendIfExpired,
            Boolean singleInstance
    ) {
        this.service = service;
        this.title = title;
        this.body = body;
        this.configurationId = configurationId;
        this.processId = processId;
        this.applicantRequirement = applicantRequirement;
        this.costs = costs;
        this.attachments = attachments;
        this.howToSubmit = howToSubmit;
        this.timeNeeded = timeNeeded;
        this.accountableStaff = accountableStaff;
        this.accountableOffice = accountableOffice;
        this.operatorStaff = operatorStaff;
        this.isActive = isActive;
        this.isOnline = isOnline;
        this.multipleInstance = multipleInstance;
        this.filingPlan = filingPlan;
        this.startDate = startDate;
        this.endDate = endDate;
        this.updating = updating;
        this.activeCommunication = activeCommunication;
        this.version = version;
        this.tenant = tenant;
        this.organization = organization;
        this.uo = uo;
        this.customTemplate = customTemplate;
        this.showCustomTemplate = showCustomTemplate;
        this.uniqueInstance = uniqueInstance;
        this.sendIfExpired = sendIfExpired;
        this.singleInstance = singleInstance;
    }

    public Proceeding(
            Long id,
            Service service,
            String title,
            String body,
            Long configurationId,
            String processId,
            String applicantRequirement,
            String costs,
            String attachments,
            String howToSubmit,
            String timeNeeded,
            String accountableStaff,
            String accountableOffice,
            String operatorStaff,
            Boolean isActive,
            Boolean isOnline,
            Boolean multipleInstance,
            Boolean filingPlan,
            Date startDate,
            Date endDate,
            Boolean updating,
            Boolean activeCommunication,
            Integer version,
            String tenant,
            String organization,
            String uo,
            String customTemplate,
            Boolean showCustomTemplate,
            Boolean uniqueInstance,
            Boolean sendIfExpired,
            Boolean singleInstance
            ) {
        this.id = id;
        this.service = service;
        this.title = title;
        this.body = body;
        this.configurationId = configurationId;
        this.processId = processId;
        this.applicantRequirement = applicantRequirement;
        this.costs = costs;
        this.attachments = attachments;
        this.howToSubmit = howToSubmit;
        this.timeNeeded = timeNeeded;
        this.accountableStaff = accountableStaff;
        this.accountableOffice = accountableOffice;
        this.operatorStaff = operatorStaff;
        this.isActive = isActive;
        this.isOnline = isOnline;
        this.multipleInstance = multipleInstance;
        this.filingPlan = filingPlan;
        this.startDate = startDate;
        this.endDate = endDate;
        this.updating = updating;
        this.activeCommunication = activeCommunication;
        this.version = version;
        this.tenant = tenant;
        this.organization = organization;
        this.uo = uo;
        this.customTemplate = customTemplate;
        this.showCustomTemplate = showCustomTemplate;
        this.uniqueInstance = uniqueInstance;
        this.sendIfExpired = sendIfExpired;
        this.singleInstance = singleInstance;
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

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
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

    public String getUo() {
        return uo;
    }

    public void setUo(String uo) {
        this.uo = uo;
    }

    @Override
    public Long getId() {
        return id;
    }
    
}
