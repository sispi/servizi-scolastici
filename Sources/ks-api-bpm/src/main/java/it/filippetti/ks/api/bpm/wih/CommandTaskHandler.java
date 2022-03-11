/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.wih;

import it.filippetti.ks.api.bpm.ApplicationContextHolder;
import it.filippetti.ks.api.bpm.command.Command;
import it.filippetti.ks.api.bpm.command.CommandContext;
import it.filippetti.ks.api.bpm.command.callback.CommandCallback;
import it.filippetti.ks.api.bpm.command.callback.WorkItemCommandCallback;
import it.filippetti.ks.api.bpm.service.ExecutorService;
import static it.filippetti.ks.api.bpm.wih.WorkItemSupport.getBusinessKey;
import java.util.List;
import java.util.Map;
import org.kie.api.executor.RequestInfo;
import org.kie.api.executor.STATUS;
import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.api.runtime.process.WorkItemManager;
import org.kie.api.runtime.query.QueryContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author marco.mazzocchetti
 */
public abstract class CommandTaskHandler implements WorkItemHandler, WorkItemSupport {
    
    private static final Logger log = LoggerFactory.getLogger(CommandTaskHandler.class);
    
    private Class<? extends Command> commandClass;
    private Class<? extends CommandCallback> callbackClass;

    public CommandTaskHandler(
        Class<? extends Command> commandClass, 
        Class<? extends WorkItemCommandCallback> callbackClass) {
        
        this.commandClass = commandClass;
        this.callbackClass = callbackClass;
    }
    
    @Override
    public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
        
        if (getClass().equals(CommandTaskHandler.class)) {
            log.info(String.format(">>> executeWorkItem 'Command Task' (%d, %s, %s)", 
                workItem.getId(), commandClass, callbackClass));
        }
        
        Boolean async, autoComplete;
        Integer retries;
        Long retryDelay;
        Map<String, Object> metadata, results;
        CommandContext context;
        
        // get metadata
        metadata = getMetaData(workItem);

        // get execution parameters
        async = metadata.containsKey("customAsync") ? 
           Boolean.valueOf((String) metadata.get("customAsync")) :
           Boolean.valueOf(getParameter(workItem, "Async", Object.class, "false").toString());

        autoComplete = 
            !async || 
            Boolean.valueOf(getParameter(workItem, "AutoComplete", Object.class, "false").toString());
        
        retries = Integer.valueOf(getParameter(workItem, "Retries", Object.class, "0").toString());
        
        retryDelay = Long.valueOf(getParameter(workItem, "RetryDelay", Object.class, "0").toString());
        
        // init results
        results = null;
        
        // schedule command
        if (async) {
            context = new CommandContext(workItem, callbackClass, retries, retryDelay);
            getExecutorService().scheduleRequest(commandClass.getName(), context);
        } 
        // execute command
        else {
            context = new CommandContext(workItem);
            while (retries >= 0) {
                try {
                    results = newCommand().execute(context).getData();
                } catch (Throwable t) {
                    if (retries > 0) {
                        sleep(retryDelay);
                        context.markAsRetry();
                    } else {
                        throw t;
                    }
                } finally {
                    retries--;
                }
            }
        }
        
        // complete
        if (autoComplete) {
            manager.completeWorkItem(workItem.getId(), results);
        }
    }

    @Override
    public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {
        
        Boolean async;
        
        if (getClass().equals(CommandTaskHandler.class)) {
            log.info(String.format(">>> abortWorkItem 'Command Task' (%d, %s, %s)", 
                workItem.getId(), commandClass, callbackClass));
        }

        manager.abortWorkItem(workItem.getId());
        
        async = Boolean.parseBoolean(getParameter(workItem, "Async", Object.class, "false").toString());
        if (async) {
            cancelActiveRequests(workItem);
        }
    }

    private Command newCommand() {
        try {
            return commandClass.getConstructor().newInstance();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
    
    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
        }
    }
    
    private void cancelActiveRequests(WorkItem workItem) {

        List<RequestInfo> requests;
        
        requests = getExecutorService().getRequestsByBusinessKey(
            getBusinessKey(workItem), 
            new QueryContext());
        if (requests != null) {
            for (RequestInfo request : requests) {
                if (request.getStatus() == STATUS.QUEUED ||
                    request.getStatus() == STATUS.RUNNING ||    
                    request.getStatus() == STATUS.RETRYING) {
                    getExecutorService().cancelRequest(request.getId());
                }
            }
        }
    }
    
    private ExecutorService getExecutorService() {
        return ApplicationContextHolder.getBean(ExecutorService.class);
    } 
}
