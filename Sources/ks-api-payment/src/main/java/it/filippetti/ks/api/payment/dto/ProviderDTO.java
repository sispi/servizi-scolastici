/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.payment.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 *
 * @author dino
 */
@ApiModel(value = "Provider") 
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProviderDTO {
    
    @ApiModelProperty(position = 1, required = true, example = "1") 
    private Long id;
    
    @ApiModelProperty(position = 2, required = true, example = "PM Pay") 
    private String name;

    @ApiModelProperty(position = 3, required = false, example = "PMPay") 
    private String packageClass;

    @ApiModelProperty(position = 4, required = false, example = "marioRossi") 
    private String serviceUser;

    @ApiModelProperty(position = 5, required = false, example = "https://www.serviceabc.com") 
    private String serviceUrl;

    @ApiModelProperty(position = 6, required = false, example = "https://www.serviceabc.com/wsdl") 
    private String wsdlUrl;

    @ApiModelProperty(position = 16, required = false, notes = "The logo blob", example = "null")
    private byte[] logo;

    public ProviderDTO() {
    }

    @ApiModel("Page<Provider>")
    public static class Page extends PageDTO<ProviderDTO> {}


    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackageClass() {
        return this.packageClass;
    }

    public void setPackageClass(String packageClass) {
        this.packageClass = packageClass;
    }

    public String getServiceUser() {
        return this.serviceUser;
    }

    public void setServiceUser(String serviceUser) {
        this.serviceUser = serviceUser;
    }

    public String getServiceUrl() {
        return this.serviceUrl;
    }

    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    public String getWsdlUrl() {
        return this.wsdlUrl;
    }

    public void setWsdlUrl(String wsdlUrl) {
        this.wsdlUrl = wsdlUrl;
    }

    public byte[] getLogo() {
        return this.logo;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }
    

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", name='" + getName() + "'" +
            ", packageClass='" + getPackageClass() + "'" +
            ", serviceUser='" + getServiceUser() + "'" +
            ", serviceUrl='" + getServiceUrl() + "'" +
            ", wsdlUrl='" + getWsdlUrl() + "'" +
            ", logo='" + getLogo() + "'" +
            "}";
    }

    
}
