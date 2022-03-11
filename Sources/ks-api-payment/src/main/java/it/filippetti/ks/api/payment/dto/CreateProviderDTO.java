/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.payment.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import it.filippetti.ks.api.payment.validator.NotBlank;
import javax.validation.constraints.NotNull;

/**
 *
 * @author dino
 */
@ApiModel("CreateProvider")
public class CreateProviderDTO {
    
    @ApiModelProperty(position = 1, required = true, example = "Banca Sella")
    @NotNull
    @NotBlank
    private String name;
    
    @ApiModelProperty(position = 3, required = false, example = "bancaSella") 
    private String packageClass;
    
    @ApiModelProperty(position = 4, required = false, example = "marioRossi") 
    private String serviceUser;
    
    @ApiModelProperty(position = 5, required = false, example = "https://www.serviceabc.com") 
    private String serviceUrl;
    
    @ApiModelProperty(position = 6, required = false, example = "https://www.serviceabc.com/wsdl") 
    private String wsdlUrl;
    
    @ApiModelProperty(position = 16, required = false, notes = "The logo blob", example = "null")
    private byte[] logo;

    public CreateProviderDTO() {
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
            ", serviceUser='" + getServiceUser()+ "'" +
            ", serviceUrl='" + getServiceUrl()+ "'" +
            "}";
    }
    
}
