/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.command;

import it.filippetti.ks.api.bpm.ApplicationContextHolder;
import it.filippetti.ks.api.bpm.exception.CommandException;
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
public abstract class Command implements org.kie.api.executor.Command {
    
    private static final Logger log = LoggerFactory.getLogger(Command.class);
    
    @Autowired
    private PlatformTransactionManager transactionManager;

    public Command() {
        ApplicationContextHolder.autowireObject(this);
    }

    @Override
    public final ExecutionResults execute(org.kie.api.executor.CommandContext context) {

        ExecutionResults executionResults;
        
        // join transaction
        TransactionStatus tx = transactionManager.getTransaction(
            new DefaultTransactionDefinition());
        try {
            // execute
            executionResults = execute((CommandContext) context);
        } catch (Throwable t) {
            // rollback
            transactionManager.rollback(tx);
            // throw error
            throw t;
        }
        // commit
        transactionManager.commit(tx);
        // return results
        return executionResults;
    }

    /**
     * 
     * @param context
     * @return
     * @throws Exception 
     */
    protected abstract ExecutionResults execute(CommandContext context) throws CommandException;
}
