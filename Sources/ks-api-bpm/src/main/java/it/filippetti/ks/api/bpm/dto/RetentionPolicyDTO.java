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
@ApiModel("RetentionPolicy")
public class RetentionPolicyDTO {
    
    @ApiModelProperty(position = 1, required = false,
        example = "365",    
        notes = ""
        + "Number of days that terminated instances data must be retained before archiviation.<br/>"
        + "Value -1 indicates unlimited retention, 0 indicates immediate archiviation")      
    private Integer days;
    
    @ApiModelProperty(position = 2, required = false,
        example = "true",    
        notes = ""
        + "Indicates if runtime non-business data generated during execution "
        + "(e.g. nodes, non-business variables) must be discarded at instance termination")      
    private Boolean clean;

    public RetentionPolicyDTO() {
    }

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public Boolean getClean() {
        return clean;
    }

    public void setClean(Boolean clean) {
        this.clean = clean;
    }
}
