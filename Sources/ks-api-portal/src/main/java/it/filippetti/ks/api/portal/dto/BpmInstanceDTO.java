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

/**
 *
 * @author dino
 */
@ApiModel(value = "BpmInstance") 
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BpmInstanceDTO {
    
    @ApiModelProperty(position = 1, example = "100260")
    private Long id;
    
    @ApiModelProperty(position = 2, example = "Comunicazione ISEE")
    private String name;
    
    @ApiModelProperty(position = 3, example = "1.0")
    private String version;

    @ApiModelProperty(position = 4, example = "Comunicazione ISEE")
    private String businessName;
    
    @ApiModelProperty(position = 5, example = "852fc587-724b-4e1e-821b-5788ea052703")
    private String creatorUserId;
   
    @ApiModelProperty(position = 6, example = "852fc587-724b-4e1e-821b-5788ea052703")
    private String initiatorUserId;
    
    @ApiModelProperty(position = 7, example = "2022-01-07T17:51:41.640Z")
    private Date startTs;
    
    @ApiModelProperty(position = 8, example = "2022-01-07T17:51:42.389Z")
    private Date lastActivityTs;
    
    @ApiModelProperty(position = 9, example = "1")
    private Integer status;
    
    @ApiModelProperty(position = 10, example = "true")
    private Boolean root;
   
    @ApiModelProperty(position = 11, example = "1")
    private String operationId;
    
    @ApiModelProperty(position = 12, notes = "Comunicazione ISEE1.0")
    private String processId;
    
    @ApiModelProperty(position = 13, example = "2021-02-02T10:19:04.657Z")
    private Object input;
    
    @ApiModelProperty(position = 14, example = "")
    private Object[] nextTasks;
    
    public BpmInstanceDTO() {
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

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getCreatorUserId() {
        return creatorUserId;
    }

    public void setCreatorUserId(String creatorUserId) {
        this.creatorUserId = creatorUserId;
    }

    public String getInitiatorUserId() {
        return initiatorUserId;
    }

    public void setInitiatorUserId(String initiatorUserId) {
        this.initiatorUserId = initiatorUserId;
    }

    public Date getStartTs() {
        return startTs;
    }

    public void setStartTs(Date startTs) {
        this.startTs = startTs;
    }

    public Date getLastActivityTs() {
        return lastActivityTs;
    }

    public void setLastActivityTs(Date lastActivityTs) {
        this.lastActivityTs = lastActivityTs;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Boolean getRoot() {
        return root;
    }

    public void setRoot(Boolean root) {
        this.root = root;
    }

    public Object[] getNextTasks() {
        return nextTasks;
    }

    public void setNextTasks(Object[] nextTasks) {
        this.nextTasks = nextTasks;
    }
    

    /**
     * @return String return the operationId
     */
    public String getOperationId() {
        return operationId;
    }

    /**
     * @param operationId the operationId to set
     */
    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    /**
     * @return String return the processId
     */
    public String getProcessId() {
        return processId;
    }

    /**
     * @param processId the processId to set
     */
    public void setProcessId(String processId) {
        this.processId = processId;
    }

    /**
     * @return Object return the input
     */
    public Object getInput() {
        return input;
    }

    /**
     * @param input the input to set
     */
    public void setInput(Object input) {
        this.input = input;
    }
    
    @ApiModel("Page<Instance>")
    public static class Page extends PageDTO<InstanceDTO> {}

    @Override
    public String toString() {
        return "BpmInstanceDTO{" + "id=" + id + ", name=" + name + ", version=" + version + ", businessName=" + businessName + ", creatorUserId=" + creatorUserId + ", initiatorUserId=" + initiatorUserId + ", startTs=" + startTs + ", lastActivityTs=" + lastActivityTs + ", status=" + status + ", root=" + root + ", operationId=" + operationId + ", processId=" + processId + ", input=" + input + ", nextTasks=" + nextTasks + '}';
    }
    
}
