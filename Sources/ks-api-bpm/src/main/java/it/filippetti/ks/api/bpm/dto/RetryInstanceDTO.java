/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Map;

/**
 *
 * @author marco.mazzocchetti
 */
@ApiModel("RetryInstance")
public class RetryInstanceDTO extends OperationDTO {

    @ApiModelProperty(position = 1, required = true,
        example = "77",
        notes = "Instance command id") 
    private Long commandId;

    @ApiModelProperty(position = 1, required = false,
        notes = "Instance command context, if not valued original context will be applied") 
    private Map<String, Object> context;
    
    public RetryInstanceDTO() {
    }

    public Long getCommandId() {
        return commandId;
    }

    public void setCommandId(Long commandId) {
        this.commandId = commandId;
    }

    public Map<String, Object> getContext() {
        return context;
    }

    public void setContext(Map<String, Object> context) {
        this.context = context;
    }
}
