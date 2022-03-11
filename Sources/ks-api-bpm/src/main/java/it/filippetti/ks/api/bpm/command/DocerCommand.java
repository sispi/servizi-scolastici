package it.filippetti.ks.api.bpm.command;

import it.filippetti.ks.api.bpm.exception.CommandException;
import java.util.HashMap;
import java.util.Map;
import keysuite.desktop.exceptions.KSException;
import keysuite.remoting.DocerActionService;
import org.kie.api.executor.ExecutionResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author marco.mazzocchetti
 */
public class DocerCommand extends Command {

    private static final Logger log = LoggerFactory.getLogger(DocerCommand.class);
    
    @Autowired
    private DocerActionService docerActionService;
    
    @Override
    public ExecutionResults execute(CommandContext ctx) {

        Map results;
        ExecutionResults executionResults;

        try {
            results = docerActionService.execute(
                new HashMap<>(Map.of(
                    DocerActionService.DEPLOYMENT_ID, ctx.getUnitId(),
                    DocerActionService.INSTANCE_ID, ctx.getInstanceId(),
                    DocerActionService.WORKITEM_ID, ctx.getWorkItem().getId(),
                    DocerActionService.OPERATION_ID,  ctx.getWorkItem().getParameters().get("operation"),
                    DocerActionService.OPERATION_RETRY, ctx.isRetry()
                )),
                ctx.getWorkItem().getParameters());
        } catch (KSException e) {
            throw new CommandException(e);
        }

        executionResults = new ExecutionResults();
        executionResults.setData(results);
        
        return executionResults;
    }
}
