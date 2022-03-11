/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.spa.form.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author marco.mazzocchetti
 */
@ApiModel(
    value = "Error", 
    description = "Standard object returned in all error responses")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorDTO {
    
    public static final String KEY_STATUS = "status";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_DETAILS = "details";    
    public static final String KEY_DETAILS_MESSAGES = "messages";    
    
    @ApiModelProperty(position = 1, 
        example = "422", 
        notes = "Http status code")
    private Integer status;

    @ApiModelProperty(position = 2, 
        example = "FORM_ALREADY_EXISTS",    
        notes = ""
        + "Business error code, valued only for status 422.<br/>"
        + "See operation responses to know when and how code applies.<br/>"
        + "Allowed values are:"
        + "<li>FORM_ALREADY_EXISTS"
        + "<li>FORM_TEMPLATE_UNDEFINED"
        + "<li>FORM_VIEW_GENERATION_ERROR")            
    private String code;
    
    @ApiModelProperty(position = 3, 
        example = "Invalid asset", 
        notes = "Main error message")    
    private String message;
    
    @ApiModelProperty(position = 4, 
        example = "{\"messages\":[]}",
        notes = "Additional error detail messages, if any")    
    private Map<String, Object> details;

    public ErrorDTO(Integer status, String code, String message, List<String> detailMessages) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.details = Map.of(KEY_DETAILS_MESSAGES, Collections.unmodifiableList(detailMessages));
    }

    public ErrorDTO(Integer status, String message, List<String> detailMessages) {
        this.status = status;
        this.message = message;
        this.details = Map.of(KEY_DETAILS_MESSAGES, Collections.unmodifiableList(detailMessages));
    }

    public ErrorDTO(Integer status, String message) {
        this.status = status;
        this.message = message;
        this.details = Map.of(KEY_DETAILS_MESSAGES, Collections.EMPTY_LIST);
    }
    
    public Integer getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
    
    public Map<String, Object> getDetails() {
        return details;
    }
}
