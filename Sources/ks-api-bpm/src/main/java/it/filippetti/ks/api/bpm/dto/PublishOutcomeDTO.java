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
@ApiModel("PublishOutcome")
public class PublishOutcomeDTO {
    
    @ApiModelProperty(position = 1,
        notes = "Outcome variable value, if applies and if any")    
    private Object outcome;

    public PublishOutcomeDTO(Object outcome) {
        this.outcome = outcome;
    }

    public Object getOutcome() {
        return outcome;
    }

    public void setOutcome(Object outcome) {
        this.outcome = outcome;
    }
}
