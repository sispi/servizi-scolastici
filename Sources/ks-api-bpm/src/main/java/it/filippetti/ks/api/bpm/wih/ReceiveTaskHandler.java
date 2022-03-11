/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.wih;

import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.api.runtime.process.WorkItemManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 *
 * @author marco.mazzocchetti
 */
@Component
@WorkItemHandlerName("Receive Task")
public class ReceiveTaskHandler implements WorkItemHandler {
    
    private static final Logger log = LoggerFactory.getLogger(ReceiveTaskHandler.class);
    
    public ReceiveTaskHandler() {
    }
    
    /**
     * This method doesn't do anything, as we manage correlation 
     * for receive tasks and message catch events in a single place
     * @See InstanceListener.afterNodeTriggered
     * 
     * @param workItem
     * @param manager 
     */
    @Override
    public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {

        log.info(String.format(">>> executeWorkItem 'Receive Task' (%d)", workItem.getId()));
    }

    /**
     * 
     * @param workItem
     * @param manager 
     */
    @Override
    public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {

        log.info(String.format(">>> abortWorkItem 'Receive Task' (%d)", workItem.getId()));
        
        manager.abortWorkItem(workItem.getId());
    }
}
