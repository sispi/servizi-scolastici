package it.filippetti.ks.api.bpm.command;

import it.filippetti.ks.api.bpm.service.RestService;
import java.util.*;
import keysuite.docer.client.FileServiceCommon;
import org.kie.api.executor.ExecutionResults;
import org.springframework.beans.factory.annotation.Autowired;


public class RestCommand extends Command {

    @Autowired
    private RestService restService;
    
    @Override
    public ExecutionResults execute(CommandContext context) {

        Map<String,Object> results;
        ExecutionResults executionResults;
        
        try {
            FileServiceCommon.setThreadFolderRef(context.getFolderRef());

            results = restService.execute(context.getWorkItem().getParameters());

            executionResults = new ExecutionResults();
            executionResults.setData(results);
            return executionResults;
        } 
        finally {
            FileServiceCommon.setThreadFolderRef(null);
        }
    }
}
