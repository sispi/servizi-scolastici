/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.command.callback;

import bpmn2.WorkflowException;
import it.filippetti.ks.api.bpm.command.CommandContext;
import it.filippetti.ks.api.bpm.service.InstanceService;
import org.jbpm.executor.AsyncJobException;
import org.jbpm.process.core.context.exception.ExceptionScope;
import org.jbpm.process.instance.context.exception.ExceptionScopeInstance;
import org.jbpm.workflow.instance.node.WorkItemNodeInstance;
import org.kie.api.command.ExecutableCommand;
import org.kie.api.executor.ExecutionResults;
import org.kie.api.runtime.Context;
import org.kie.api.runtime.process.WorkItem;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author marco.mazzocchetti
 */
public class WorkItemCommandCallback extends NotifyOnErrorCommandCallback {

    @Autowired
    private InstanceService instanceService;
    
    public WorkItemCommandCallback() {
        super();
    }

    @Override
    public void onCommandDone(CommandContext ctx, ExecutionResults results) 
        throws Exception {

        instanceService.completeWorkItem(ctx.getWorkItem(), results.getData());
    }

    @Override
    public void onCommandError(CommandContext ctx, Throwable exception) 
        throws Exception {
        
        if (exception instanceof AsyncJobException) {
            exception = exception.getCause();
        }

        if (!(exception instanceof WorkflowException) || 
            !handleException(ctx.getWorkItem(), (WorkflowException) exception)) {
            super.onCommandError(ctx, exception);
        }
    }   
    
    /**
     * 
     * @param workItem
     * @param exception
     * @return 
     */
    protected boolean handleException(WorkItem workItem, WorkflowException exception) {
        
        return instanceService.execute(
            getUnitId(workItem), 
            (ExecutableCommand<Boolean>) (Context context) -> {
                String exceptionName;
                WorkItemNodeInstance nodeInstance;
                ExceptionScopeInstance exceptionScopeInstance;

                exceptionName = exception.getClass().getName();
                nodeInstance = getNodeInstance(workItem);
                exceptionScopeInstance = (ExceptionScopeInstance) nodeInstance
                        .resolveContextInstance(
                            ExceptionScope.EXCEPTION_SCOPE, 
                            exceptionName);
                if (exceptionScopeInstance != null) {
                    // remove listeners to avoid node triggering on abort
                    nodeInstance.removeEventListeners();
                    //
                    exceptionScopeInstance.handleException(exceptionName, exception);
                    return true;
                } else {
                    return false;
                }
            });        
    }
}
