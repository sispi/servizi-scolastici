/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import it.filippetti.ks.api.bpm.validator.NotBlank;
import it.filippetti.ks.api.bpm.validator.OneOf;
import java.util.Map;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 *
 * @author marco.mazzocchetti
 */
@ApiModel("PublishEvent")
public class PublishEventDTO {

    @ApiModelProperty(position = 1, required = false,
        notes = "Event payload")      
    private Map<String, Object> payload;

    @ApiModelProperty(position = 2, required = false,
        notes = "Event scope, if not valued default to organization scope")          
    @NotNull
    @Valid
    private Scope scope;

    @ApiModelProperty(position = 3, required = false,
        example = "false",
        notes = "Indicates if publishing must run asynchronously or not")              
    @NotNull
    private Boolean async;
    
    @ApiModelProperty(position = 4, required = false,
        example = "instanceId",
        notes = ""
        + "Variable name of eventually correlated instance of which to return "
        + "the value as outcome, if any.<br/>"
        + "Applied only for synchronous message events")       
    @NotBlank
    private String outcomeVariable;

    public PublishEventDTO() {
        scope = new Scope("organization", null);
        async = false;
    }

    public PublishEventDTO(Map<String, Object> payload, Boolean async, String outcomeVariable) {
        this.payload = payload;
        this.async = async;
        this.outcomeVariable = outcomeVariable;
    }

    public Map<String, Object> getPayload() {
        return payload;
    }

    public void setPayload(Map<String, Object> payload) {
        this.payload = payload;
    }

    public Scope getScope() {
        return scope;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }

    public Boolean getAsync() {
        return async;
    }

    public void setAsync(Boolean async) {
        this.async = async;
    }

    public String getOutcomeVariable() {
        return outcomeVariable;
    }

    public void setOutcomeVariable(String outcomeVariable) {
        this.outcomeVariable = outcomeVariable;
    }
    
    /**
     * 
     */
    public static class Scope {
        
        @ApiModelProperty(position = 1, required = false,
            example = "organization",
            notes = ""
            + "Publishing scope type.<br/>"
            + "Allowed values are:"
            + "<li>organization"
            + "<li>instance")         
        @NotNull
        @OneOf({"organization", "instance"})
        private String type;

        @ApiModelProperty(position = 2, required = false,
            example = "0",
            notes = ""
            + "Instance id to signal.<br/>"
            + "Applied only for instance scope type")         
        private Long id;

        public Scope() {
        }

        public Scope(String type, Long id) {
            this.type = type;
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }
    }
}
