/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.wih;

import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.drools.core.process.instance.impl.WorkItemImpl;
import org.jbpm.process.instance.impl.NoOpExecutionErrorHandler;
import org.jbpm.ruleflow.instance.RuleFlowProcessInstance;
import org.jbpm.runtime.manager.impl.AbstractRuntimeManager;
import org.jbpm.runtime.manager.impl.error.ExecutionErrorManagerImpl;
import org.jbpm.workflow.instance.node.WorkItemNodeInstance;
import org.kie.api.definition.process.Node;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.manager.RuntimeManager;
import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemManager;
import org.kie.internal.runtime.error.ExecutionErrorHandler;
import org.kie.internal.runtime.error.ExecutionErrorManager;
import org.kie.internal.runtime.manager.RuntimeManagerRegistry;
import org.kie.internal.runtime.manager.context.ProcessInstanceIdContext;

/**
 *
 * @author marco.mazzocchetti
 */
public interface WorkItemSupport {
    
    /**
     * 
     * @param workItem
     * @param name
     * @return 
     */
    public default <T> T getParameter(WorkItem workItem, String name, Class<T> type)
        throws IllegalArgumentException {
        return getParameter(workItem, name, type, null);
    }    
    
    /**
     * 
     * @param workItem
     * @param name
     * @param defaultValue
     * @return 
     */
    public default <T> T getParameter(WorkItem workItem, String name, Class<T> type, T defaultValue)
        throws IllegalArgumentException {
        
        Object value;
        
        value  = workItem.getParameter(name);
        if (value == null || type == String.class && StringUtils.isBlank((String) value)) {
            value = defaultValue;
        }
        try {
            return type.cast(value);
        } catch (ClassCastException e) {
            throw new IllegalArgumentException(String.format(
                "Invalid parameter '%s' type: expected %s, found %s", 
                name, 
                type.getSimpleName(), 
                value.getClass().getSimpleName()));
        }
    }
    
    /**
     * 
     * @param <T>
     * @param workItem
     * @param name
     * @param type
     * @return 
     */
    public default <T> T getRequiredParameter(WorkItem workItem, String name, Class<T> type)
        throws IllegalArgumentException {
        
        T value;
        
        value = getParameter(workItem, name, type, null);
        
        if (value == null) {
            throw new IllegalArgumentException(String.format("Missing parameter: %s", name));
        }
        return value;
    }    
    
    /**
     * 
     * @param workItem
     * @return 
     */
    public default String getUnitId(WorkItem workItem) {
        return ((WorkItemImpl) workItem).getDeploymentId();
    }
    
    /**
     * 
     * @param workItem
     * @return 
     */
    public default RuntimeManager getRuntimeManager(WorkItem workItem) {
        return RuntimeManagerRegistry.get().getManager(getUnitId(workItem));

    }

    /**
     * 
     * @param workItem
     * @return 
     */
    public default RuntimeEngine getRuntimeEngine(WorkItem workItem) {
        return getRuntimeManager(workItem)
            .getRuntimeEngine(ProcessInstanceIdContext.get(workItem.getProcessInstanceId()));
    }

    /**
     * 
     * @param workItem
     * @return 
     */
    public default KieSession getKieSession(WorkItem workItem) {
        return getRuntimeEngine(workItem).getKieSession();
    }
    
    /**
     * 
     * @param workItem
     * @return 
     */
    public default WorkItemManager getWorkItemManager(WorkItem workItem) {
        return getKieSession(workItem).getWorkItemManager();
    }
    
    /**
     * 
     * @param workItem
     * @return 
     */
    public default RuleFlowProcessInstance getProcessInstance(WorkItem workItem) {
        return (RuleFlowProcessInstance) getKieSession(workItem)
            .getProcessInstance(workItem.getProcessInstanceId(), true);   
    }
    
    /**
     * 
     * @param workItem
     * @return 
     */
    public default WorkItemNodeInstance getNodeInstance(WorkItem workItem) {
        return (WorkItemNodeInstance) getProcessInstance(workItem)
            .getNodeInstance(((WorkItemImpl) workItem).getNodeInstanceId(), true);
    }  
    
    /**
     * 
     * @param workItem
     * @return 
     */
    public default Node getNode(WorkItem workItem) {
        return getNodeInstance(workItem).getNode();
    }  
    
    /**
     * 
     * @param workItem
     * @return 
     */
    public default Map<String, Object> getMetaData(WorkItem workItem) {
        return getNode(workItem).getMetaData();
    }
    
    /**
     * 
     * @param workItem
     * @return 
     */
    public default  ExecutionErrorHandler getExecutionErrorHandler(WorkItem workItem) {
        
        ExecutionErrorManager errorManager;
        
        errorManager = ((AbstractRuntimeManager) getRuntimeManager(workItem)).getExecutionErrorManager();
        if (errorManager == null) {
            return new NoOpExecutionErrorHandler();
        }
        return ((ExecutionErrorManagerImpl) errorManager).createHandler();
    }    
    /**
     * 
     * @param workItem
     * @return 
     */
    public static String getBusinessKey(WorkItem workItem) {
        return String.valueOf(workItem.getId());
    }    
    
    public static String getTenant(WorkItem workItem) {
        return ((WorkItemImpl) workItem).getDeploymentId().split(":")[0].split("\\.")[0];
    }    

    public static String getOrganization(WorkItem workItem) {
        return ((WorkItemImpl) workItem).getDeploymentId().split(":")[0].split("\\.")[1];
    }        
}
