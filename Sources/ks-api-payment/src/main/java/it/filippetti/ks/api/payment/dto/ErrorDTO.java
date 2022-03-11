/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.payment.dto;

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
        example = "ASSET_NOT_VALID",    
        notes = ""
        + "Business error code, valued only for status 422.<br/>"
        + "See operation responses to know when and how code applies.<br/>"
        + "Allowed values are:"
        + "<li>ASSET_NOT_VALID"
        + "<li>IDENTITY_NOT_VALID"
        + "<li>DEPLOYMENT_ALREADY_EXISTS"
        + "<li>DEPLOYMENT_BUILD_ERROR"
        + "<li>DEPLOYMENT_DEPENDENCY_NOT_FOUND"
        + "<li>DEPLOYMENT_ALREADY_SHARED"
        + "<li>DEPLOYMENT_IN_USE_BY_DEPENDANT"
        + "<li>DEPLOYMENT_IN_USE_BY_INSTANCE"
        + "<li>DEPLOYMENT_NOT_FOUND"
        + "<li>DEPLOYMENT_NOT_ACTIVE"
        + "<li>DEPLOYMENT_OUT_OF_SYNC"
        + "<li>CONFIGURATION_ALREADY_EXISTS"
        + "<li>CONFIGURATION_PERMISSION_NOT_VALID"                
        + "<li>CONFIGURATION_NOT_FOUND"
        + "<li>CONFIGURATION_NOT_ACTIVE"
        + "<li>CONFIGURATION_NOT_RUNNABLE"
        + "<li>CONFIGURATION_IN_USE_BY_INSTANCE"
        + "<li>INSTANCE_NOT_ACTIVE"
        + "<li>INSTANCE_WORKFLOW_ERROR"
        + "<li>INSTANCE_NODE_NOT_ACTIVE"
        + "<li>INSTANCE_NODE_EXECUTING"
        + "<li>INSTANCE_NODE_ACTION_NOT_SUPPORTED"
        + "<li>TASK_ACTION_TARGET_NOT_VALID"
        + "<li>TASK_ACTION_NOT_ALLOWED"
        + "<li>EVENT_SIGNALLING_NOT_SUPPORTED" 
        + "<li>EVENT_CORRELATION_ERROR"
        + "<li>NOTIFICATION_ARGUMENT_NOT_VALID")            
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
