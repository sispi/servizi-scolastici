/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.wih;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import it.filippetti.ks.api.bpm.model.Task;
import it.filippetti.ks.api.bpm.repository.TaskRepository;
import it.filippetti.ks.api.bpm.service.TaskService;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.api.runtime.process.WorkItemManager;
import org.kie.api.task.model.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 *
 * @author marco.mazzocchetti
 */
@Component
@WorkItemHandlerName({"Complete HT Task", "closeHt"})
public class CompleteHTTaskHandler implements WorkItemHandler, WorkItemSupport {

    private static final Logger log = LoggerFactory.getLogger(CompleteHTTaskHandler.class);

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskRepository taskRepository;

    @Override
    public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {

        log.info(String.format(">>> executeWorkItem 'Complete HT Task' (%d)", workItem.getId()));

        String humanToken, userId, docType, docNum;
        Map<String, Object> taskOutput;
        Map<String, Object> results;
        Date endTs;
        Task task;

        humanToken = null;
        endTs = new Date();
        results = new HashMap<String, Object>();

        // join transaction
        TransactionStatus tx = transactionManager.getTransaction(
                new DefaultTransactionDefinition());
        try {
            try {
                // get parameters
                humanToken =  getRequiredParameter(workItem, "userToken", String.class);
                userId = extractTokenKey(humanToken, "uid");
                docType = getRequiredParameter(workItem, "operazione", String.class);
                docNum = getRequiredParameter(workItem, "document", Map.class).get("DOCNUM").toString();
                taskOutput = getParameter(workItem, "htOutput", Map.class, new HashMap<String, Object>());

                // get task to complete
                task = taskRepository.findCompletableByUserAndAttachment(userId, docNum, docType);
                if (task != null) {
                    // build task output
                    taskOutput.put("audit_taskid", task.getId());
                    taskOutput.put("audit_desbilling", task.getName());
                    taskOutput.put("audit_createdate", task.getTaskData().getCreatedOn());
                    taskOutput.put("audit_startdate", task.getStartTs());
                    taskOutput.put("audit_enddate", endTs);
                    taskOutput.put("audit_duration", endTs.getTime() - task.getStartTs().getTime());
                    taskOutput.put("audit_userid", userId);
                    taskOutput.put("audit_sequence", docNum);

                    // complete target task
                    if (task.getTaskData().getStatus() == Status.Reserved) {
                        taskService.start(task.getId(), userId);
                    }
                    taskService.complete(task.getId(), userId, taskOutput);

                    // put work item results
                    results.put("esito", 1);
                    results.put("userToken", humanToken);
                    results.put("outcome", "EVENTO-" + docType);
                } else {
                    results.put("esito", -1);
                    results.put("userToken", humanToken);
                }
            // every exception raised during target task completion 
            // is ignored and reported with results
            } catch (Throwable t) {
                results.put("esito", -1);
                results.put("userToken", humanToken);
                results.put("outcome", "ERROR-" + t.getMessage());
            }
            // complete current task
            manager.completeWorkItem(workItem.getId(), results);
        // every exception raised during current task completion 
        // causes a rollback
        } catch (Throwable t) {
            // rollback
            transactionManager.rollback(tx);
            throw t;
        }
        // commit
        transactionManager.commit(tx);
    }

    @Override
    public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {

        log.info(String.format(">>> abortWorkItem 'Complete HT Task' (%d)", workItem.getId()));
    }
    
    
    private static String extractOptionalTokenKey(String token, String key, String def) {
        Preconditions.checkArgument(
            token != null,
            "Attempted to extract a key from a null token");
        Preconditions.checkArgument(
            !Strings.isNullOrEmpty(key),
            "Attempted to extract an empty or null key from a token");
        Pattern pattern = Pattern.compile(String.format("(?:\\||^)%s:([^|]*?)\\|.*", key));
        Matcher matcher = pattern.matcher(token);
        if (!matcher.find()) {
            return def;
        }
        return matcher.group(1);
    }

    private static String extractTokenKey(String token, String key) {
        String value = extractOptionalTokenKey(token, key, null);
        if (value==null) {
            throw new IllegalArgumentException(String.format(
                "Key %s not found in token: %s", 
                key, token));
        }
        return value;
    }    
}
