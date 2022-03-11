/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.listener;

import it.filippetti.ks.api.bpm.ApplicationContextHolder;
import it.filippetti.ks.api.bpm.exception.NotificationException;
import it.filippetti.ks.api.bpm.model.NotificationContext;
import it.filippetti.ks.api.bpm.model.Task;
import it.filippetti.ks.api.bpm.repository.TaskRepository;
import it.filippetti.ks.api.bpm.service.NotificationService;
import it.filippetti.ks.api.bpm.wih.HumanTaskHandler;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.mail.Message;
import org.jbpm.services.task.deadlines.NotificationListener;
import org.kie.internal.task.api.UserInfo;
import org.kie.internal.task.api.model.EmailNotification;
import org.kie.internal.task.api.model.EmailNotificationHeader;
import org.kie.internal.task.api.model.NotificationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author marco.mazzocchetti
 */
public class TaskNotificationListener implements NotificationListener {

    private static final Logger log = LoggerFactory.getLogger(TaskNotificationListener.class);

    private static interface Variable {
        public static final String PROCESS_INSTANCE_ID = "processInstanceId";
        public static final String PROCESS_SESSION_ID = "processSessionId";
        public static final String WORK_ITEM_ID = "workItemId";
        public static final String TASK_ID = "taskId";
        public static final String TASK_VARIABLES = "doc";
        public static final String POTENTIAL_OWNERS = "owners";
        public static final String ACTUAL_OWNER = "actualOwner";
        public static final String DOC_NUM = "docNum";
    }

    public TaskNotificationListener() {
        ApplicationContextHolder.autowireObject(this);
    }
    
    @Autowired
    private NotificationService notificationService;
    
    @Autowired
    private TaskRepository taskRepository;
    
    @Override
    public void onNotification(NotificationEvent event, UserInfo userInfo) {

        log.info(String.format(">>> onNotification (%d)", event.getTask().getId()));

        EmailNotification email;
        EmailNotificationHeader emailHeader;
        Map<String, Object> emailVariables;
        Task task;
        HashMap<String,Object> variables;
        String docNum;
        Collection<String> tags, recipients, attachments;

        // skip if not supported
        if (!(event.getNotification() instanceof EmailNotification)) {
            return;
        }
        
        // get email data
        email = (EmailNotification) event.getNotification();
        emailVariables = event.getContent();
        emailHeader = email.getEmailHeaders().values().stream().findFirst().get();

        // get task
        task = taskRepository.findById(event.getTask().getId()).orElseThrow(() -> { 
            throw new IllegalStateException(String.format(
                "Task %s not found", event.getTask().getId()));
        });
        
        // get docnum variable
        docNum = (String) emailVariables.get(HumanTaskHandler.DOC_CORRELATO);
        
        // setup notification context variables
        variables = new HashMap<>();
        variables.put(Variable.PROCESS_INSTANCE_ID, task.getTaskData().getProcessInstanceId());
        variables.put(Variable.PROCESS_SESSION_ID, task.getTaskData().getProcessSessionId());
        variables.put(Variable.WORK_ITEM_ID, task.getTaskData().getWorkItemId());
        variables.put(Variable.TASK_ID, task.getId());
        variables.put(Variable.TASK_VARIABLES, emailVariables);
        variables.put(Variable.ACTUAL_OWNER, task.getTaskData().getActualOwner());
        variables.put(Variable.POTENTIAL_OWNERS, task.getPeopleAssignments().getPotentialOwners());
        variables.put(Variable.DOC_NUM, docNum);
        
        // setup tags
        if (docNum != null) {
            tags = Set.of(String.format("docNum:%s", docNum));
        } else {
            tags = Collections.EMPTY_SET;
        }
        
        // extract recipients
        recipients = email
            .getRecipients()
            .stream()
            .map(r -> r.getId())
            .collect(Collectors.toSet());
        
        // extract attachments
        Object source = emailVariables.get("attachments");
        if (source == null) {
            attachments = Collections.EMPTY_LIST;
        }
        else if (source instanceof Collection) {
            attachments = ((Collection<?>) source)
                .stream()
                .map(a -> a.toString())
                .collect(Collectors.toList());
        } else {
            attachments = Arrays.asList(source.toString().split(",", -1));
        }       

        // build and send notification
        try {
            notificationService
                .newNotification(NotificationContext.of(task, variables))
                .email(!emailHeader.getBody().contains("<!-- NO_MAIL_NOTIFY -->"))
                .withFrom(emailHeader.getFrom())
                .withReplyTo(emailHeader.getReplyTo())
                .withSubject(emailHeader.getSubject())
                .withBody(emailHeader.getBody())
                .withExpireTs(task.getTaskData().getExpirationTime())
                .withPriority(email.getPriority() > 0)
                .withTags(tags)
                .withRecipients(Message.RecipientType.TO, recipients)
                .withAttachments(attachments)
                .build()
                .create();
        } catch (NotificationException e) {
            log.error(e.getMessage(), e);
        }
    }
}
