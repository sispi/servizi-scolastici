/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.command;

import it.filippetti.ks.api.bpm.command.callback.CommandCallback;
import it.filippetti.ks.api.bpm.command.callback.NoOpCommandCallback;
import it.filippetti.ks.api.bpm.model.Instance;
import it.filippetti.ks.api.bpm.wih.WorkItemSupport;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.drools.core.process.instance.impl.WorkItemImpl;
import org.kie.api.runtime.process.WorkItem;

/**
 *
 * @author marco.mazzocchetti
 */
public class CommandContext extends org.kie.api.executor.CommandContext 
    implements WorkItemSupport {
    
    public CommandContext(org.kie.api.executor.CommandContext commandContext) {
        super(commandContext.getData());
    }

    public CommandContext() {
        this(NoOpCommandCallback.class, 0, 0);
    }

    public CommandContext(
        Class<? extends CommandCallback> callback) {
        this(callback, 0, 0);
    }
    
    public CommandContext(
        Class<? extends CommandCallback> callback, 
        int retries, long retryDelay) {
        super(Stream.of(new Object[][] { 
            {"callbacks", callback.getName()}, 
            {"retries", retries},
            {"retryDelay", String.valueOf(retryDelay)},
            {"isRetry", false}})
            .collect(Collectors
            .toMap(e -> (String) e[0], e -> e[1])));
    }

    public CommandContext(
        Instance instance) {
        this(instance, NoOpCommandCallback.class, 0, 0);
    } 

    public CommandContext(
        Instance instance, 
        Class<? extends CommandCallback> callback) {
        this(instance, callback, 0, 0);
    }
    
    public CommandContext(
        Instance instance, 
        Class<? extends CommandCallback> callback, 
        int retries, long retryDelay) {
        super(Stream.of(new Object[][] { 
            {"callbacks", callback.getName()}, 
            {"deploymentId", instance.getUnitId()}, 
            {"processInstanceId", instance.getUnitId()},
            {"retries", retries},
            {"retryDelay", String.valueOf(retryDelay)},
            {"isRetry", false}})
            .collect(Collectors
            .toMap(e -> (String) e[0], e -> e[1])));
    }

    public CommandContext(
        WorkItem workItem) {
        this(workItem, NoOpCommandCallback.class, 0, 0);
    }

    public CommandContext(
        WorkItem workItem, 
        Class<? extends CommandCallback> callback) {
        this(workItem, callback, 0, 0);
    }
    
    public CommandContext(
        WorkItem workItem, 
        Class<? extends CommandCallback> callback, 
        int retries, long retryDelay) {
        super(Stream.of(new Object[][] { 
            {"workItem", workItem}, 
            {"callbacks", callback.getName()}, 
            {"deploymentId", ((WorkItemImpl) workItem).getDeploymentId()}, 
            {"processInstanceId", workItem.getProcessInstanceId()},
            {"businessKey", WorkItemSupport.getBusinessKey(workItem)},
            {"retries", retries},
            {"retryDelay", String.valueOf(retryDelay)},
            {"isRetry", false}})
            .collect(Collectors
            .toMap(e -> (String) e[0], e -> e[1])));
    }

    public String getFolderRef() {
        if (getInstanceId() != null) {
            return String.format("instance-%d", getInstanceId());
        } else {
            return null;
        }
    }
    
    public String getCommandName() {
        return (String) getData("commandName");
    }
    
    public String getUnitId() {
        return (String) getData("deploymentId");
    }

    public Long getInstanceId() {
        return (Long) getData("processInstanceId");
    }

    public WorkItem getWorkItem() {
        return (WorkItem) getData("workItem");
    }

    public Integer getRetries() {
        return (Integer) getData("retries");
    }

    public Long getRetryDelay() {
        return Long.parseLong((String) getData("retryDelay"));
    }

    public Boolean isRetry() {
        return (Boolean) getData("isRetry");
    }
    
    public void markAsRetry() {
        setData("isRetry", true);
    }
    
    public String getBusinessKey() {
        return (String) getData("businessKey");
    }
    
    public Class<? extends CommandCallback> getCallback() {
        try {
            return (Class<? extends CommandCallback>) Class.forName((String) getData("callbacks"));
        } catch (ClassNotFoundException e) {
            return null;
        }
    }       

    public void putData(Map<String, Object> data) {
        getData().putAll(data);
    }
    
    /**
     * 
     * @param parameters
     * @param merge
     * @return 
     */
    public CommandContext applyWorkItemParameters(Map<String, Object> parameters, boolean merge) {
        
        WorkItem workItem;
        
        workItem = getWorkItem();
        if (workItem == null) {
            throw new UnsupportedOperationException();
        }
        if (!merge) {
            workItem.getParameters().clear();
        }
        workItem.getParameters().putAll(parameters);
        return this;
    }
    
    public CommandContext applyData(Map<String, Object> data, boolean merge) {
        if (!merge) {
            getData().clear();
        }
        getData().putAll(data);
        return this;
    }
}
