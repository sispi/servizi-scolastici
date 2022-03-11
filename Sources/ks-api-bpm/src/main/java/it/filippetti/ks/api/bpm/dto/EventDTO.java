/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Map;

/**
 *
 * @author marco.mazzocchetti
 */
@ApiModel(
    value = "Event", 
    description = ""
    + "Sortable:"
    + "<li>id (default)") 
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventDTO {
    
    @ApiModelProperty(position = 1, 
        example = "myMessage")      
    private String id;
    
    @ApiModelProperty(position = 2, 
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
        example = "false",
        notes = ""
        + "Indicates if event is a start message event.<br/>"
        + "Only for message type")      
    private Boolean start;

    @ApiModelProperty(position = 4, 
        example = "...",
        notes = ""
        + "Mvel expression for context correlation.<br/>"
        + "Only for message type")      
    private String correlationSubscription;

    @ApiModelProperty(position = 5, 
        example = "...",
        notes = ""
        + "Mvel expression for conversation correlation.<br/>"
        + "Only for message type")      
    private String retrievalExpression;

    @ApiModelProperty(position = 6, 
        example = "myQueue",
        notes = ""
        + "Queue name for external routing.<br/>"
        + "Only for message type")            
    private String queue;

    @ApiModelProperty(hidden = true)      
    private String summary;

    @ApiModelProperty(hidden = true)      
    private String schema;

    @ApiModelProperty(hidden = true)      
    private Map<String, Object> properties;

    public EventDTO() {
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

    public Boolean getStart() {
        return start;
    }

    public void setStart(Boolean start) {
        this.start = start;
    }

    public String getCorrelationSubscription() {
        return correlationSubscription;
    }

    public void setCorrelationSubscription(String correlationSubscription) {
        this.correlationSubscription = correlationSubscription;
    }

    public String getRetrievalExpression() {
        return retrievalExpression;
    }

    public void setRetrievalExpression(String retrievalExpression) {
        this.retrievalExpression = retrievalExpression;
    }

    public String getQueue() {
        return queue;
    }

    public void setQueue(String queue) {
        this.queue = queue;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }
    
    @ApiModel("Page<Event>")
    public static class Page extends PageDTO<EventDTO> {}         
}
