/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.portal.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;

/**
 *
 * @author dino
 */
@ApiModel("UpdateMedia")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateMediaDTO {
    
    @ApiModelProperty(position = 1, required = true, example = "1", notes = "The proceeding id")
    @NotNull
    private Long id;
    
    @ApiModelProperty(position = 2, required = true, example = "caccia")
    @NotNull
    private String name;
    
    @ApiModelProperty(position = 3, required = false, example = "Immagine che rappresenta la caccia")
    private String description;
    
    @ApiModelProperty(position = 4, required = true, example = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mNk+M9QDwADhgGAWjR9awAAAABJRU5ErkJggg==")
    @NotNull
    private String file;
            
    @ApiModelProperty(position = 5, required = true, example = "jpg")
    @NotNull
    private String fileType;
    
    @ApiModelProperty(position = 6, required = true, example = "image/jpg")
    @NotNull
    private String mimeType;
    
    @ApiModelProperty(position = 7, required = false, example = "98")
    private Long size;
    
    @ApiModelProperty(position = 8, required = false, example = "logo")
    private String myKey;

    public UpdateMediaDTO() {
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
