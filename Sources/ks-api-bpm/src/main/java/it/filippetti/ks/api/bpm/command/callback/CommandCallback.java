/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.command.callback;

import it.filippetti.ks.api.bpm.ApplicationContextHolder;
import it.filippetti.ks.api.bpm.command.CommandContext;
import it.filippetti.ks.api.bpm.wih.WorkItemSupport;
import org.kie.api.executor.ExecutionResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;


/**
 *
 * @author marco.mazzocchetti
 */
public abstract class CommandCallback 
    implements org.kie.api.executor.CommandCallback, WorkItemSupport {
    
    private static final Logger log = LoggerFactory.getLogger(CommandCallback.class);
    
    @Autowired
    private PlatformTransactionManager transactionManager;
    
    public CommandCallback() {
        ApplicationContextHolder.autowireObject(this);
    }

    @Override
    public final void onCommandDone(org.kie.api.executor.CommandContext ctx, ExecutionResults results) {

        // join transaction
        TransactionStatus tx = transactionManager.getTransaction(
            new DefaultTransactionDefinition());
        try {
            // execute
            onCommandDone(new CommandContext(ctx), results);
        } catch (Throwable t) {
            // rollback
            transactionManager.rollback(tx);
            // log error
            log.error(t.getMessage(), t);
        }
        // commit
        transactionManager.commit(tx);
    }

    @Override
    public final void onCommandError(org.kie.api.executor.CommandContext ctx, Throwable exception)  {

        // join transaction
        TransactionStatus tx = transactionManager.getTransaction(
            new DefaultTransactionDefinition());
        try {
            // execute
            onCommandError(new CommandContext(ctx), exception);
        } catch (Throwable t) {
            // rollback
            transactionManager.rollback(tx);
            // log error
            log.error(t.getMessage(), t);
        }
        // commit
        transactionManager.commit(tx);
    }
    
    public abstract void onCommandDone(CommandContext ctx, ExecutionResults results) 
        throws Exception;
    
    public abstract void onCommandError(CommandContext ctx, Throwable exception)
        throws Exception;    
}    
