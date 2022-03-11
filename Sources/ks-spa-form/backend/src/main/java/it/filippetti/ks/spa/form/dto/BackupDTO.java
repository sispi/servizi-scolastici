/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.spa.form.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 *
 * @author marco.mazzocchetti
 */
@ApiModel(
    value = "Backup", 
    description = ""
    + "Fetchable:"
    + "<ul><li>definition</ul>") 
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BackupDTO {

    @ApiModelProperty(position = 1, 
        example = "1") 
    private Integer index;
    
    @ApiModelProperty(position = 2, 
        example = "mario.rossi") 
    private String lastModifiedBy;

    @ApiModelProperty(position = 3, 
        example = "2020-11-10T09:59:42.000Z") 
    private Date lastModifiedTs;    

    @ApiModelProperty(position = 4) 
    private ObjectNode definition;

    public BackupDTO() {
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Date getLastModifiedTs() {
        return lastModifiedTs;
    }

    public void setLastModifiedTs(Date lastModifiedTs) {
        this.lastModifiedTs = lastModifiedTs;
    }

    public ObjectNode getDefinition() {
        return definition;
    }

    public void setDefinition(ObjectNode definition) {
        this.definition = definition;
    }
}
