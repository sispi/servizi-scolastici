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
@ApiModel(value = "Channel") 
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChannelDTO {
    
    @ApiModelProperty(position = 1, example = "0") 
    private Long id;

    @ApiModelProperty(position = 2, example = "Name") 
    private String name;

    @ApiModelProperty(position = 3, notes = "Banca Sella") 
    private ProviderDTO provider;
    
    public ChannelDTO() {
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

    public ProviderDTO getProvider() {
        return provider;
    }

    public void setProvider(ProviderDTO provider) {
        this.provider = provider;
    }

    @ApiModel("Page<ChannelDTO>")
    public static class Page extends PageDTO<ChannelDTO> {}

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", name='" + getName() + "'" +
            ", provider='" + getProvider() + "'" +
            "}";
    }

}
