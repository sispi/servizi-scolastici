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
@ApiModel("NotificationAttachment")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotificationAttachmentDTO {
   
    @ApiModelProperty(position = 1, 
        example = "attachment-1.txt")        
    private String name;
    
    @ApiModelProperty(position = 2, 
        example = "plain/text")        
    private String contentType;

    public NotificationAttachmentDTO() {
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
