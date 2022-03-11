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

/**
 *
 * @author marco.mazzocchetti
 */
@ApiModel(
    value = "InstanceVariable", 
    description = ""
    + "Fetchable:"
    + "<li>value")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InstanceVariableDTO {

    @ApiModelProperty(position = 1, 
        example = "true", 
        notes = "Indicates if it's a business variable")     
    private Boolean business;

    @ApiModelProperty(position = 2, 
        example = "false", 
        notes = "Indicates if it's a input variable")     
    private Boolean in;

    @ApiModelProperty(position = 3, 
        example = "false", 
        notes = "Indicates if it's a output variable")     
    private Boolean out;

    @ApiModelProperty(position = 4, 
        example = "false", 
        notes = "Indicates if it's a configuration variable")     
    private Boolean config;

    @ApiModelProperty(position = 5, 
        example = "2020-11-11T14:57:48.000Z", 
        notes = "Last time variable was modified during instance execution")     
    private Date lastModifiedTs;
    
    @ApiModelProperty(position = 6, 
        notes = "Instance variable value")     
    private Object value;

    public InstanceVariableDTO() {
    }

    public Boolean getBusiness() {
        return business;
    }

    public void setBusiness(Boolean business) {
        this.business = business;
    }

    public Boolean getIn() {
        return in;
    }

    public void setIn(Boolean in) {
        this.in = in;
    }

    public Boolean getOut() {
        return out;
    }

    public void setOut(Boolean out) {
        this.out = out;
    }

    public Boolean getConfig() {
        return config;
    }

    public void setConfig(Boolean config) {
        this.config = config;
    }

    public Date getLastModifiedTs() {
        return lastModifiedTs;
    }

    public void setLastModifiedTs(Date lastModifiedTs) {
        this.lastModifiedTs = lastModifiedTs;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
    
    public interface Map extends java.util.Map<String, InstanceVariableDTO> {
    }
}
