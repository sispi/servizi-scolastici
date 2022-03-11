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
@ApiModel("InstanceContext")
public class InstanceContextDTO {
    
    @ApiModelProperty(position = 1, 
        notes = "Context definition id, '0' stands for main context",
        example = "0")
    private String id;

    @ApiModelProperty(position = 2, 
        notes = "Context instance id, '0' stands for main context",
        example = "0")
    private String instanceId;

    public InstanceContextDTO() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }    
}
