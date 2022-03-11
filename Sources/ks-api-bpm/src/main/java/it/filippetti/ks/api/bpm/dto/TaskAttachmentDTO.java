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
    value = "TaskAttachment", 
    description = ""
    + "Attachments are used only to track task correlated document(s) "
    + "reference and hence have no content")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaskAttachmentDTO {
    
    @ApiModelProperty(position = 1,
        example = "12345678", 
        notes = ""
        + "Document number (docNum)")    
    private String name;
    
    @ApiModelProperty(position = 1,
        example = "My document type", 
        notes = "Document type")    
    private String contentType;   

    public TaskAttachmentDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
