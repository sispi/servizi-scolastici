/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.wih;

import it.filippetti.ks.api.bpm.command.DocerCommand;
import it.filippetti.ks.api.bpm.command.callback.WorkItemCommandCallback;
import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 *
 * @author marco.mazzocchetti
 */
@Component
@WorkItemHandlerName({"Docer Task", "DocerJMS"})
public class DocerTaskHandler extends CommandTaskHandler {

    private static final Logger log = LoggerFactory.getLogger(DocerTaskHandler.class);
    
    public DocerTaskHandler() {
        super(DocerCommand.class, WorkItemCommandCallback.class);
    }

    @Override
    public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
        
        log.info(String.format(">>> executeWorkItem 'Docer Task' (%d)", workItem.getId()));
        
        // adapt legacy default
        workItem.getParameters().put("Async", true);
        
        super.executeWorkItem(workItem, manager);
        
    }

    @Override
    public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {

        log.info(String.format(">>> abortWorkItem 'Docer Task' (%d)", workItem.getId()));
        
        super.abortWorkItem(workItem,manager);    
    }
}