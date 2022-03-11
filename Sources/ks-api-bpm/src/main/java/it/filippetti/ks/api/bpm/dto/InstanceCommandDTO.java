/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author marco.mazzocchetti
 */
@ApiModel(
    value = "InstanceCommand", 
    description = ""
    + "Fetchable:"
    + "<li>node"
    + "<li>context"
    + "<li>results"
    + "<li>errors") 
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InstanceCommandDTO {

    @ApiModelProperty(position = 1,
        example = "77")   
    private Long id;

    @ApiModelProperty(position = 2,
        example = "it.filippetti.ks.api.bpm.command.RoutingCommand",
        notes = "Command class name")  
    private String name;

    @ApiModelProperty(position = 3,
        example = "2020-11-11T14:57:48.000Z")  
    private Date executionTs;

    @ApiModelProperty(position = 4,
        example = "1", 
        notes = "Number of actual executions")   
    private Integer executions;

    @ApiModelProperty(position = 5,
        example = "DONE", 
        notes = ""
        + "Command status.<br/>"
        + "Allowed values are:"
        + "<li>QUEUED"
        + "<li>DONE"
        + "<li>CANCELLED"
        + "<li>ERROR"
        + "<li>RETRYING"
        + "<li>RUNNING")      
    private String status;
    
    @ApiModelProperty(position = 6,
        example = "Ready to execute")      
    private String message;
    
    @ApiModelProperty(position = 7,
        example = "0", 
        notes = "Max number of retries attempted")      
    private Integer retries;
    
    @ApiModelProperty(position = 8,
        notes = "Command node")           
    private InstanceNodeDTO node;
    
    @ApiModelProperty(position = 9,
        notes = "Command context")           
    private Map<String, Object> context;

    @ApiModelProperty(position = 10,
        notes = "Command results")            
    private Map<String, Object> results;
    
    @ApiModelProperty(position = 11,
        notes = "Command execution errors")      
    private List<InstanceCommandErrorDTO> errors;

    public InstanceCommandDTO() {
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

    public Date getExecutionTs() {
        return executionTs;
    }

    public void setExecutionTs(Date executionTs) {
        this.executionTs = executionTs;
    }

    public Integer getExecutions() {
        return executions;
    }

    public void setExecutions(Integer executions) {
        this.executions = executions;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getRetries() {
        return retries;
    }

    public void setRetries(Integer retries) {
        this.retries = retries;
    }

    public InstanceNodeDTO getNode() {
        return node;
    }

    public void setNode(InstanceNodeDTO node) {
        this.node = node;
    }

    public Map<String, Object> getContext() {
        return context;
    }

    public void setContext(Map<String, Object> context) {
        this.context = context;
    }

    public Map<String, Object> getResults() {
        return results;
    }

    public void setResults(Map<String, Object> results) {
        this.results = results;
    }

    public List<InstanceCommandErrorDTO> getErrors() {
        return errors;
    }

    public void setErrors(List<InstanceCommandErrorDTO> errors) {
        this.errors = errors;
    }    
    
    public interface Lista extends java.util.List<InstanceCommandDTO> {}
}
