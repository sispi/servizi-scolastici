/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.HashMap;
import java.util.Map;
import javax.validation.constraints.NotNull;

/**
 *
 * @author marco.mazzocchetti
 */
@ApiModel("CompleteInstanceNode")
public class CompleteInstanceNodeDTO extends OperationDTO {

    @ApiModelProperty(position = 1, required = false,
        notes = "Node output") 
    @NotNull
    private Map<String, Object> output;
    
    public CompleteInstanceNodeDTO() {
        output = new HashMap<>();
    }

    public Map<String, Object> getOutput() {
        return output;
    }

    public void setOutput(Map<String, Object> output) {
        this.output = output;
    }    
}
