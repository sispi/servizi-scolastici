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
@ApiModel(value = "UpdateProvider")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateProviderDTO {
    
    @ApiModelProperty(position = 1, required = true, example = "Banca Sella") 
    private String name;

    @ApiModelProperty(position = 2, required = false, example = "bancaSella") 
    private String packageClass;

    @ApiModelProperty(position = 3, required = false, example = "marioRossi") 
    private String serviceUser;

    @ApiModelProperty(position = 4, required = false, example = "https://www.serviceabc.com") 
    private String serviceUrl;

    @ApiModelProperty(position = 5, required = false, example = "https://www.serviceabc.com/wsdl") 
    private String wsdlUrl;

    @ApiModelProperty(position = 6, required = false, notes = "The logo blob", example = "null")
    private byte[] logo;

    public UpdateProviderDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackageClass() {
        return packageClass;
    }

    public void setPackageClass(String packageClass) {
        this.packageClass = packageClass;
    }

    public String getServiceUser() {
        return serviceUser;
    }

    public void setServiceUser(String serviceUser) {
        this.serviceUser = serviceUser;
    }

    public String getServiceUrl() {
        return serviceUrl;
    }

    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    public String getWsdlUrl() {
        return wsdlUrl;
    }

    public void setWsdlUrl(String wsdlUrl) {
        this.wsdlUrl = wsdlUrl;
    }

    public byte[] getLogo() {
        return logo;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }
    
    @Override
    public String toString() {
        return "{" +
            " name='" + getName() + "'" +
            ", packageClass='" + getPackageClass() + "'" +
            ", serviceUser='" + getServiceUser() + "'" +
            ", serviceUrl='" + getServiceUrl() + "'" +
            ", wsdlUrl='" + getWsdlUrl() + "'" +
            ", logo='" + getLogo() + "'" +
            "}";
    }
    
}
