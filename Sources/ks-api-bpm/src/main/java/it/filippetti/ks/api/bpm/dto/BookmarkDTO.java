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
@ApiModel("Bookmark")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookmarkDTO {
    
    @ApiModelProperty(position = 1, 
        example = "19")
    private Long id;
    
    @ApiModelProperty(position = 2, 
        example = "77")
    private Long instanceId;   
    
    @ApiModelProperty(position = 3, 
        example = "My description")
    private String description;    

    public BookmarkDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(Long instanceId) {
        this.instanceId = instanceId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    @ApiModel("Page<Bookmark>")
    public static class Page extends PageDTO<BookmarkDTO> {}
}
