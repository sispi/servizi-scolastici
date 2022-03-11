/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.portal.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 *
 * @author dino
 */
@ApiModel(value = "Media") 
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MediaDTO {
    
    @ApiModelProperty(position = 1, example = "0") 
    private Long id;
    
    @ApiModelProperty(position = 2, example = "caccia") 
    private String name;
    
    @ApiModelProperty(position = 3, example = "Immagine che rappresenta la caccia") 
    private String description;
    
    @ApiModelProperty(position = 4, example = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mNk+M9QDwADhgGAWjR9awAAAABJRU5ErkJggg==") 
    private String file;
    
    @ApiModelProperty(position = 5, example = "jpg")
    private String fileType;
    
    @ApiModelProperty(position = 6, example = "image/jpg")
    private String mimeType;
    
    @ApiModelProperty(position = 7, example = "98")
    private Long size;
    
    @ApiModelProperty(position = 8, example = "logo")
    private String myKey;

    public MediaDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getMyKey() {
        return myKey;
    }

    public void setMyKey(String myKey) {
        this.myKey = myKey;
    }
    
    @ApiModel("Page<Media>")
    public static class Page extends PageDTO<MediaDTO> {}

    @Override
    public String toString() {
        return "{"
                + "id='" + getId() + "'"
                + ", name='" + getName() + "'"
                + ", description='" + getDescription() + "'"
                + ", fileType='" + getFileType() + "'"
                + ", mimeType='" + getMimeType() + "'"
                + ", size='" + getSize() + "'"
                + ", key='" + getMyKey() + "'"
                + "}";
    }
    
}
