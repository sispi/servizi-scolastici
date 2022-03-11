/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.exception;

/**
 *
 * @author marco.mazzocchetti
 */
public enum BusinessError {  
    ASSET_NOT_VALID, 
    DEPLOYMENT_ALREADY_EXISTS, 
    DEPLOYMENT_BUILD_ERROR,
    DEPLOYMENT_DEPENDENCY_NOT_FOUND,
    DEPLOYMENT_ALREADY_SHARED, 
    DEPLOYMENT_IN_USE_BY_DEPENDANT,
    DEPLOYMENT_IN_USE_BY_INSTANCE, 
    DEPLOYMENT_NOT_FOUND,
    DEPLOYMENT_NOT_ACTIVE,
    DEPLOYMENT_OUT_OF_SYNC,
    CONFIGURATION_ALREADY_EXISTS,
    CONFIGURATION_PERMISSION_NOT_VALID,
    CONFIGURATION_NOT_FOUND,
    CONFIGURATION_NOT_ACTIVE,
    CONFIGURATION_NOT_RUNNABLE,
    CONFIGURATION_IN_USE_BY_INSTANCE, 
    INSTANCE_NOT_ACTIVE,
    INSTANCE_WORKFLOW_ERROR, 
    INSTANCE_NODE_NOT_ACTIVE,
    INSTANCE_COMMAND_NOT_FOUND, 
    INSTANCE_COMMAND_EXECUTING, 
    TASK_ACTION_TARGET_NOT_VALID,
    EVENT_PUBLISHING_NOT_SUPPORTED, 
    EVENT_CORRELATION_ERROR, 
    NOTIFICATION_ARGUMENT_NOT_VALID,
    FORM_TEMPLATE_UNDEFINED,
    FORM_VIEW_GENERATION_ERROR;
    
    public String code() {
        return this.name();
    }
}