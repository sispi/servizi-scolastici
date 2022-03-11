/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.portal.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;

/**
 *
 * @author dino
 */
@ApiModel(value = "Service") 
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServiceTreeDTO {
    
    @ApiModelProperty(position = 1, example = "0") 
    private Long id;
    
    @ApiModelProperty(position = 3, notes = "The Children")
    private List<ServiceTreeDTO> children;
    
    @ApiModelProperty(position = 4, example = "Ambiente")
    private String name;
    
    @ApiModelProperty(position = 5, example = "1")
    private Integer position;
    
    @ApiModelProperty(position = 6, example = "base64")
    private String logo;
    
    @ApiModelProperty(position = 7, example = "SERVICE_001")
    private String code;
    
    @ApiModelProperty(position = 8, example = "true")
    private boolean valid;
    
    @ApiModelProperty(position = 9, example = "false")
    private boolean externalService;
    
    @ApiModelProperty(position = 10, example = "www.test.com")
    private String link;

    public ServiceTreeDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<ServiceTreeDTO> getChildren() {
        return children;
    }

    public void setChildren(List<ServiceTreeDTO> children) {
        this.children = children;
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
}
