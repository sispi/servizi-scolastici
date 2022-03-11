/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.portal.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import it.filippetti.ks.api.portal.validator.NotBlank;
import javax.validation.constraints.NotNull;

/**
 *
 * @author dino
 */
@ApiModel("UpdateService")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateServiceDTO {
    
    @ApiModelProperty(position = 1, required = true, example = "1", notes = "The service id")
    @NotNull
    private Long id;
    
    @ApiModelProperty(position = 3, required = false, example = "1", notes = "The parent id")
    @NotNull
    private Long parentId;
    
    @ApiModelProperty(position = 4, required = true, example = "Ambiente", notes = "The name")
    @NotNull
    @NotBlank
    private String name;
    
    @ApiModelProperty(position = 5, required = false, example = "1", notes = "The position")
    private Integer position;
    
    @ApiModelProperty(position = 6, required = false, example = "base64", notes = "The base64 of the logo")
    private String logo;
    
    @ApiModelProperty(position = 7, required = true, example = "SERVICE_001", notes = "The code")
    @NotNull
    @NotBlank
    private String code;
    
    @ApiModelProperty(position = 8, required = false, example = "true", notes = "Is valid?")
    private boolean valid;
    
    @ApiModelProperty(position = 9, required = false, example = "false", notes = "Is an external service?")
    private boolean externalService;
    
    @ApiModelProperty(position = 10, required = false, example = "www.test.com", notes = "The link")
    private String link;

    public UpdateServiceDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public boolean isExternalService() {
        return externalService;
    }

    public void setExternalService(boolean externalService) {
        this.externalService = externalService;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
    
    @Override
    public String toString(){
        return "{"
                + "id='" + getId() + "', "
                + "parentId='" + getParentId()+ "', "
                + "name='" + getName() + "', "
                + "position='" + getPosition() + "', "
                + "logo='" + getLogo() + "', "
                + "code='" + getCode() + "', "
                + "valid='" + isValid() + "', "
                + "externalService='" + isExternalService() + "', "
                + "link='" + getLink()
                + "'}";
    }
}
