/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 *
 * @author marco.mazzocchetti
 */
@ApiModel(
    value = "Asset", 
    description = ""
    + "Fetchable:"
    + "<li>content")    
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AssetDTO {
    
    @ApiModelProperty(position = 1,  
        example = "settings",
        notes = "Asset type.<br/>"
            + "Allowed values are:"
            + "<li>metadata"
            + "<li>definition"
            + "<li>settings"
            + "<li>form_start"
            + "<li>form_preview"
            + "<li>form_status"
            + "<li>form_settings"
            + "<li>form_clone"
            + "<li>form_task"
            + "<li>preview"
            + "<li>project")    
    private String type;

    @ApiModelProperty(position = 2,  
        example = "application/json",
        notes = "Asset content type")    
    private String contentType;
     
    @ApiModelProperty(position = 3,
        notes = ""
        + "Asset content expressed as json object or raw text string (e.g. xml, html), "
        + "depending on content type")     
    private Object content;

    public AssetDTO() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }
}
