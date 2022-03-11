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
@ApiModel("CreateChannel")
public class CreateChannelDTO {
    
    @ApiModelProperty(position = 1, required = true, example = "Name", notes = "The name")
    @NotNull
    @NotBlank
    private String name;
    
    @NotNull
    @ApiModelProperty(position = 2, required = true, example = "1")
    private Long providerId;

    public CreateChannelDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getProviderId() {
        return providerId;
    }

    public void setProviderId(Long providerId) {
        this.providerId = providerId;
    }
    
    @Override
    public String toString() {
        return "{" +
            " name='" + getName() + "'" +
            ", provider='" + getProviderId() + "'" +
            "}";
    }
}
