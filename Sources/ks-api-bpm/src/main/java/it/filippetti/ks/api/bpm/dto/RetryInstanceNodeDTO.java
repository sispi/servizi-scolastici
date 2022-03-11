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
@ApiModel("RetryInstanceNode")
public class RetryInstanceNodeDTO extends OperationDTO {

    @ApiModelProperty(position = 1, required = false,
        notes = "Node input, if not valued original input will be applied") 
    private Map<String, Object> input;
    
    public RetryInstanceNodeDTO() {
    }

    public Map<String, Object> getInput() {
        return input;
    }

    public void setInput(Map<String, Object> input) {
        this.input = input;
    }
}
