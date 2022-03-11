/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 *
 * @author marco.mazzocchetti
 */
@ApiModel("InstanceEvent")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InstanceEventDTO {
    
    @ApiModelProperty(position = 1,
        example = "myMessage",
        notes = "Event id")      
    private String id;

    @ApiModelProperty(position = 3,
        example = "message",
        notes = ""
        + "Event type.<br/>"
        + "Allowed values are:"
        + "<li>signal"
        + "<li>message"
        + "<li>timer"
        + "<li>conditional"
        + "<li>link"
        + "<li>escalation"
        + "<li>error"
        + "<li>compensation"
        + "<li>terminate"
        + "<li>none")      
    private String type; 

    @ApiModelProperty(position = 3,
        example = "message",
        notes = ""
        + "Event node type.<br/>"
        + "Allowed values are:"
        + "<li>start"
        + "<li>catch"
        + "<li>throw"
        + "<li>boundary"
        + "<li>end")          
    private String nodeType; 

    @ApiModelProperty(position = 4,
        example = "true",
        notes = ""
        + "Indicates if the node represents a correlable message event (start, catch).<br/>"
        + "If the node is also active then istance is currently waiting for the described "
        + "message")      
    private Boolean correlable;

    public InstanceEventDTO() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public Boolean getCorrelable() {
        return correlable;
    }

    public void setCorrelable(Boolean correlable) {
        this.correlable = correlable;
    }
}
