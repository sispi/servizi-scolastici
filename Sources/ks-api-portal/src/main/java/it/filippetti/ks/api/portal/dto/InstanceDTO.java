/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.portal.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 *
 * @author dino
 */
@ApiModel(value = "Instance") 
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InstanceDTO {
    
    @ApiModelProperty(position = 1, example = "1")
    private Long id;
    
    @ApiModelProperty(position = 2, notes = "The Proceeding")
    private ProceedingDTO proceeding;
    
    @ApiModelProperty(position = 3, example = "2021-02-02T10:19:04.657Z")
    private Date creationDate;
    
    @ApiModelProperty(position = 4, notes = "The form in json format") 
    private String model;
    
    @ApiModelProperty(position = 5, example = "2021-02-03T10:19:04.657Z") 
    private Date dispatchDate;
    
    @ApiModelProperty(position = 6, example = "false")
    private Boolean sent;
    
    @ApiModelProperty(position = 7, example = "1")
    private Long bpmInstanceId;

    public InstanceDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBpmInstanceId() {
        return bpmInstanceId;
    }

    public void setBpmInstanceId(Long bpmInstanceId) {
        this.bpmInstanceId = bpmInstanceId;
    }

    public ProceedingDTO getProceeding() {
        return proceeding;
    }

    public void setProceeding(ProceedingDTO proceeding) {
        this.proceeding = proceeding;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Date getDispatchDate() {
        return dispatchDate;
    }

    public void setDispatchDate(Date dispatchDate) {
        this.dispatchDate = dispatchDate;
    }

    public Boolean getSent() {
        return sent;
    }

    public void setSent(Boolean sent) {
        this.sent = sent;
    }
    
    @ApiModel("Page<Instance>")
    public static class Page extends PageDTO<InstanceDTO> {}
    
    @Override
    public String toString() {
        return "{"
                + "id='" + getId() + "'"
                + ", proceeding='" + getProceeding() + "'"
                + ", creationDate='" + getCreationDate() + "'"
                + ", model='" + getModel() + "'"
                + ", dispatchDate='" + getDispatchDate() + "'"
                + ", sent='" + getSent() + "'"
                + ", bpmInstanceId='" + getBpmInstanceId() + "'"
                + "}";
    }

}
