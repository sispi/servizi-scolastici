package it.filippetti.ks.api.bpm.wih;

import it.filippetti.ks.api.bpm.command.RestCommand;
import it.filippetti.ks.api.bpm.command.callback.WorkItemCommandCallback;
import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@WorkItemHandlerName({"Service Task", "dyn-rest-call"})
public class ServiceTaskHandler extends CommandTaskHandler {

    private static final Logger log = LoggerFactory.getLogger(ServiceTaskHandler.class);
    
    public ServiceTaskHandler() {
        super(RestCommand.class, WorkItemCommandCallback.class);
    }

    @Override
    public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
        
        log.info(String.format(">>> executeWorkItem 'Service Task' (%d)", workItem.getId()));
        
        // adapt legacy parameters
        String callType = getParameter(workItem, "callType", String.class);
        workItem.getParameters().put("Async", !"sync".equals(callType));
        workItem.getParameters().put("AutoComplete", "autocomplete".equals(callType));
        
        super.executeWorkItem(workItem, manager);
    }

    @Override
    public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {

        log.info(String.format(">>> abortWorkItem 'Service Task' (%d)", workItem.getId()));
        
        super.abortWorkItem(workItem,manager);    
    }
}