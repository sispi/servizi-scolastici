/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.spa.form.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.node.ArrayNode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 *
 * @author marco.mazzocchetti
 */
@ApiModel(
    value = "Section", 
    description = ""
    + "Fetchable:"
    + "<ul><li>value</ul>"
    + "Sortable:"
    + "<ul><li>key (default)</ul>") 
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SectionDTO {
    
    @ApiModelProperty(position = 1, 
        example = "1") 
    private String key;
    
    @ApiModelProperty(position = 2, 
        example = "mario.rossi") 
    private String lastModifiedBy;

    @ApiModelProperty(position = 3, 
        example = "2020-11-10T09:59:42.000Z") 
    private Date lastModifiedTs;  
    
    @ApiModelProperty(position = 2) 
    private ArrayNode value;   

    public SectionDTO() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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

    public ArrayNode getValue() {
        return value;
    }

    public void setValue(ArrayNode value) {
        this.value = value;
    }
    
    @ApiModel("Page<Section>")
    public static class Page extends PageDTO<SectionDTO> {}    
}
