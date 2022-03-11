/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.portal.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 *
 * @author dino
 */
@ApiModel("ContactUs")
public class ContactUsDTO {
    
    @ApiModelProperty(position = 1, required = true, example = "Info")
    private String subject;
    
    @ApiModelProperty(position = 2, required = true, example = "What are the times for issuing the fishing license?")
    private String body;
    
    @ApiModelProperty(position = 3, required = true, notes = "The sender's email")
    private String from;
    
    @ApiModelProperty(position = 4, required = true, example = "Mario Rossi")
    private String name;
    
    @ApiModelProperty(position = 5, example = "3333333333")
    private String telephone;

    @ApiModelProperty(position = 6, example = "info@abc.com")
    private String to;
    
    @ApiModelProperty(position = 7, example = "[file1.jpg, file2.pdf]")
    private String[] attachment;
    
    public ContactUsDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String[] getAttachment() {
        return attachment;
    }

    public void setAttachment(String[] attachment) {
        this.attachment = attachment;
    }
    
    
    
}
