/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.portal.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import java.util.List;

/**
 *
 * @author marco.mazzocchetti
 */
@ApiModel(value = "LegacyInstance") 
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LegacyInstanceDTO {
    
    @ApiModelProperty(position = 1)    
    private Long id;

    @ApiModelProperty(position = 2)    
    private String service;

    @ApiModelProperty(position = 3)    
    private String proceedings;

    @ApiModelProperty(position = 4)    
    private String processId;

    @ApiModelProperty(position = 5)    
    private Date startTs;

    @ApiModelProperty(position = 6)    
    private Date endTs;

    @ApiModelProperty(position = 7)    
    private Integer status;

    @ApiModelProperty(position = 8)    
    private String accountableStaff;

    @ApiModelProperty(position = 9)    
    private List<Activity> activities;

    @ApiModelProperty(position = 10)    
    private List<Document> documents;

    @ApiModelProperty(position = 11)    
    private List<Payment> payments;

    public LegacyInstanceDTO() {
    }

    public LegacyInstanceDTO(
        Long id, 
        String service, 
        String proceedings, 
        String processId, 
        Date startTs, 
        Date endTs, 
        Integer status, 
        String accountableStaff, 
        List<Activity> activities, 
        List<Document> documents, 
        List<Payment> payments) {
        this.id = id;
        this.service = service;
        this.proceedings = proceedings;
        this.processId = processId;
        this.startTs = startTs;
        this.endTs = endTs;
        this.status = status;
        this.accountableStaff = accountableStaff;
        this.activities = activities;
        this.documents = documents;
        this.payments = payments;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getProceedings() {
        return proceedings;
    }

    public void setProceedings(String proceedings) {
        this.proceedings = proceedings;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public Date getStartTs() {
        return startTs;
    }

    public void setStartTs(Date startTs) {
        this.startTs = startTs;
    }

    public Date getEndTs() {
        return endTs;
    }

    public void setEndTs(Date endTs) {
        this.endTs = endTs;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getAccountableStaff() {
        return accountableStaff;
    }

    public void setAccountableStaff(String accountableStaff) {
        this.accountableStaff = accountableStaff;
    }

    public List<Activity> getActivities() {
        return activities;
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }
    
    /**
     * 
     */
    @ApiModel(value = "LegacyInstanceActivity") 
    @JsonInclude(JsonInclude.Include.NON_NULL)    
    public static class Activity {
        
        @ApiModelProperty(position = 1)    
        private Integer step;

        @ApiModelProperty(position = 2)    
        private String label;

        @ApiModelProperty(position = 3)    
        private Date timestamp;

        @ApiModelProperty(position = 4)    
        private String type;

        @ApiModelProperty(position = 5)    
        private Integer state;

        public Activity() {
        }

        public Activity(
            Integer step, 
            String label, 
            Date timestamp, 
            String type, 
            Integer state) {
            this.step = step;
            this.label = label;
            this.timestamp = timestamp;
            this.type = type;
            this.state = state;
        }

        public Integer getStep() {
            return step;
        }

        public void setStep(Integer step) {
            this.step = step;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public Date getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(Date timestamp) {
            this.timestamp = timestamp;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
        
        public Integer getState() {
            return state;
        }

        public void setState(Integer state) {
            this.state = state;
        }
    }

    /**
     * 
     */
    @ApiModel(value = "LegacyInstanceDocument") 
    @JsonInclude(JsonInclude.Include.NON_NULL)        
    public static class Document {
        
        @ApiModelProperty(position = 1)    
        private Long id;

        @ApiModelProperty(position = 2)    
        private String name;

        @ApiModelProperty(position = 3)    
        private String direction;

        @ApiModelProperty(position = 4)    
        private String type;

        @ApiModelProperty(position = 5)    
        private Date createTs;        

        @ApiModelProperty(position = 6)    
        private Date protocolTs;        

        @ApiModelProperty(position = 7)    
        private String protocolNumber;

        public Document() {
        }

        public Document(
            Long id, 
            String name, 
            String direction,
            String type, 
            Date createTs, 
            Date protocolTs, 
            String protocolNumber) {
            this.id = id;
            this.name = name;
            this.direction = direction;
            this.type = type;
            this.createTs = createTs;
            this.protocolTs = protocolTs;
            this.protocolNumber = protocolNumber;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDirection() {
            return direction;
        }

        public void setDirection(String direction) {
            this.direction = direction;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Date getCreateTs() {
            return createTs;
        }

        public void setCreateTs(Date createTs) {
            this.createTs = createTs;
        }

        public Date getProtocolTs() {
            return protocolTs;
        }

        public void setProtocolTs(Date protocolTs) {
            this.protocolTs = protocolTs;
        }

        public String getProtocolNumber() {
            return protocolNumber;
        }

        public void setProtocolNumber(String protocolNumber) {
            this.protocolNumber = protocolNumber;
        }
    }

    /**
     * 
     */
    @ApiModel(value = "LegacyInstancePayment") 
    @JsonInclude(JsonInclude.Include.NON_NULL)        
    public static class Payment {
        
        @ApiModelProperty(position = 1)    
        private Long id;
        
        @ApiModelProperty(position = 2)    
        private String reason;

        @ApiModelProperty(position = 3)    
        private String amount;

        @ApiModelProperty(position = 4)    
        private Date processingTs;        

        @ApiModelProperty(position = 5)    
        private String status;    

        public Payment() {
        }

        public Payment(
            Long id, 
            String reason, 
            String amount, 
            Date processingTs, 
            String status) {
            this.id = id;
            this.reason = reason;
            this.amount = amount;
            this.processingTs = processingTs;
            this.status = status;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public Date getProcessingTs() {
            return processingTs;
        }

        public void setProcessingTs(Date processingTs) {
            this.processingTs = processingTs;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
