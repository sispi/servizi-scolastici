/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 *
 * @author marco.mazzocchetti
 */
@ApiModel("Operation")
public class OperationDTO {
    
    @ApiModelProperty(position = 0, required = false, 
        example = "123e4567-e89b-12d3-a456-426614174000", 
        notes = "Client generated uuid to allow non-idempotent operation retries")     
    private String operationId;

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }
}
