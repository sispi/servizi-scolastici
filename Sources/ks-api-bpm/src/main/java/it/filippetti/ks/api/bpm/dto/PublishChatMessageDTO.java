/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

/**
 *
 * @author marco.mazzocchetti
 */
@ApiModel("PublishChatMessage")
public class PublishChatMessageDTO {
   
    @ApiModelProperty(position = 1, 
        example = "Bla, bla, bla")       
    @NotNull
    @Length(min = 1, max = 1024)
    private String text;

    public PublishChatMessageDTO() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
