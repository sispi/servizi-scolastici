/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.command.callback;

import it.filippetti.ks.api.bpm.command.CommandContext;
import org.kie.api.executor.ExecutionResults;

/**
 *
 * @author marco.mazzocchetti
 */
public class NoOpCommandCallback extends CommandCallback {

    @Override
    public void onCommandDone(CommandContext ctx, ExecutionResults results) 
        throws Exception {
    }

    @Override
    public void onCommandError(CommandContext ctx, Throwable exception) 
        throws Exception {
    }    
}
