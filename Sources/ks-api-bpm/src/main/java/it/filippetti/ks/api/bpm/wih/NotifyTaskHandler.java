/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.wih;

import it.filippetti.ks.api.bpm.exception.NotificationException;
import it.filippetti.ks.api.bpm.model.Instance;
import it.filippetti.ks.api.bpm.model.Notification;
import it.filippetti.ks.api.bpm.model.NotificationContext;
import it.filippetti.ks.api.bpm.repository.InstanceRepository;
import it.filippetti.ks.api.bpm.service.NotificationService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.mail.Message;
import org.apache.commons.lang3.StringUtils;
import org.jbpm.process.core.timer.DateTimeUtils;
import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.api.runtime.process.WorkItemManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author marco.mazzocchetti
 */
@Component
@WorkItemHandlerName({"Notify Task", "SimpleSendMail", "SendTask"})
public class NotifyTaskHandler implements WorkItemHandler, WorkItemSupport {
    
    private static final Logger log = LoggerFactory.getLogger(NotifyTaskHandler.class);

    // parameters
    private static final String FROM = "mail_from";
    private static final String REPLY_TO = "mail_replyto";
    private static final String TO_RECIPIENTS = "mail_to";
    private static final String CC_RECIPIENTS = "mail_cc";
    private static final String BCC_RECIPIENTS = "mail_bcc";
    private static final String SUBJECT = "mail_subject";
    private static final String BODY = "mail_body";
    private static final String ATTACHMENTS = "mail_attachments";
    private static final String MAIL = "notify_mail";
    private static final String PRIORITY = "notify_priority";
    private static final String EXPIRATION = "notify_expire";
    private static final String TAGS = "notify_tags";
    // legacy SendTask for backward compatibility
    private static final String MESSAGE = "message";
    
    // results
    private static final String SEND_DATE = "send_date";
    private static final String RECIPIENTS = "result_to";

    
    @Autowired
    private NotificationService notificationService;

    @Autowired
    private InstanceRepository instanceRepository;
    
    @Override
    public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
        
        log.info(String.format(">>> executeWorkItem 'Notify Task' (%d)", workItem.getId()));
        
        Map<String, Object> message, results;
        List<String> from, replyTo, toRecipients, ccRecipients, bccRecipients, attachments, tags;
        Boolean email, priority;
        Date expireTs;
        String subject, body;
        Instance instance;
        Notification notification;
        
        // get instance
        instance = instanceRepository.findById(workItem.getProcessInstanceId()).orElseThrow(() -> {
            throw new IllegalStateException(String.format(
                    "Instance %d not found", 
                    workItem.getProcessInstanceId()));
            });

        // message (detect if it's legacy SendTask workitem)
        message = getParameter(workItem, MESSAGE, Map.class, null);
        
        // legacy SendTask workitem
        if (message != null) {
            // email
            email = true;
            // from
            from = new ArrayList<>(); 
            from.add((String) ((Map) message.get("mittente")).get("IndirizzoTelematico"));
            // replyTo
            replyTo = Collections.EMPTY_LIST;        
            // priority
            priority = false;
            // expireTs
            expireTs = null;
            // recipients
            toRecipients = new ArrayList<>();
            toRecipients.add((String) ((Map) message.get("destinatario")).get("IndirizzoTelematico"));
            ccRecipients = Collections.EMPTY_LIST;
            bccRecipients = Collections.EMPTY_LIST;
            // attachments
            Object source = message.get("allegati");
            if (source instanceof Collection) {
                attachments = ((Collection<Map>) source)
                    .stream()
                    .map(a -> String.format(
                        "%s/documenti/%s/file",
                        System.getProperty("docer.url"),
                        a.get("DOCNUM").toString()))
                    .collect(Collectors.toList());
            } else {
                attachments = Collections.EMPTY_LIST;
            }          
            // tags
            tags = Collections.EMPTY_LIST;
            // subject
            subject = (String) message.get("subject");
            // body
            body = (String) message.get("body");

            // silently skip old processes that use legacy SendTask handler on null subject/recipent 
            if (subject == null || toRecipients.get(0) == null) {
                results = new HashMap<>();
                results.put(SEND_DATE, null);
                results.put(RECIPIENTS, Collections.EMPTY_LIST);
                manager.completeWorkItem(workItem.getId(), results);
                return;
            }
        }
        // Notify Task or SimpleSendMail workitem
        else {
            // email
            email = getParameter(workItem, MAIL, Boolean.class, true);
            // from
            from = extractActors(workItem.getParameters(), FROM);
            // replyTo
            replyTo = extractActors(workItem.getParameters(), REPLY_TO);        
            // priority
            priority = getParameter(workItem, PRIORITY, Boolean.class, false);
            // expireTs
            String expiration = getParameter(workItem, EXPIRATION, String.class);
            if (StringUtils.isBlank(expiration)) {
                expireTs = null;
            } else if(DateTimeUtils.isPeriod(expiration)) {
                expireTs = new Date(
                    DateTimeUtils.parseDateAsDuration(expiration.substring(1)) + System.currentTimeMillis());
            }else{
                expireTs = new Date(
                    DateTimeUtils.parseDateTime(expiration));
            }
            // recipients
            toRecipients = extractActors(workItem.getParameters(), TO_RECIPIENTS);
            ccRecipients = extractActors(workItem.getParameters(), CC_RECIPIENTS);
            bccRecipients = extractActors(workItem.getParameters(), BCC_RECIPIENTS);
            // attachments
            Object source = getParameter(workItem, ATTACHMENTS, Object.class);
            if (source == null) {
                attachments = Collections.EMPTY_LIST;
            } else if (source instanceof Collection) {
                attachments = ((Collection<?>) source)
                    .stream()
                    .map(a -> a.toString())
                    .collect(Collectors.toList());
            } else {
                attachments = Arrays.asList(source.toString().split(",", -1));
            }          
            // tags
            tags = getParameter(workItem, TAGS, List.class, Collections.EMPTY_LIST);
            // subject
            subject = getParameter(workItem, SUBJECT, String.class);
            // body
            body = getParameter(workItem, BODY, String.class);
        }
        
        // build and create notification
        try {
            notification = notificationService
                .newNotification(NotificationContext.of(instance, workItem.getParameters()))
                .email(email)
                .withFrom(from.isEmpty() ? null : from.get(0))
                .withReplyTo(replyTo.isEmpty() ? null : replyTo.get(0))
                .withSubject(subject)
                .withBody(body)
                .withPriority(priority)
                .withExpireTs(expireTs)
                .withRecipients(Message.RecipientType.TO, toRecipients)
                .withRecipients(Message.RecipientType.CC, ccRecipients)
                .withRecipients(Message.RecipientType.BCC, bccRecipients)
                .withAttachments(attachments)
                .withTags(tags)
                .build()
                .create();
        } catch(NotificationException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        
        // results
        results = new HashMap<>();
        results.put(SEND_DATE, notification.getNotifyTs());
        results.put(RECIPIENTS, notification.getRecipients()
            .stream()
            .map(r -> r.getId())
            .collect(Collectors.toList()));
        
        // complete work item
        manager.completeWorkItem(workItem.getId(), results);
    }

    @Override
    public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {
        
        log.info(String.format(">>> executeWorkItem 'Notify Task' (%d)", workItem.getId()));
    }
    
    /**
     * 
     * @param parameters
     * @param parameter
     * @return 
     */
    private List<String> extractActors(Map<String, Object> parameters, String parameter) {
        
        String value;
        
        value = checkActor(parameters, parameter);
        if (value != null) {
            return Arrays.asList(value.split(",", -1));
        } else {
            return Collections.EMPTY_LIST;
        }
    }
    
    private static String checkActor(Map<String,Object> workItem, String field) {

        Object value = workItem.get(field);

        if (value == null || "".equals(value))
            return null;

        List actors = new ArrayList();

        if (value instanceof Collection) {
            actors.addAll( (Collection) value );
        } else {
            actors.add(value);
        }

        for (int i=0; i<actors.size(); i++){
            value = actors.get(i);
            if (value instanceof Map) {
                String id = (String) ((Map)value).get("identity");
                if (id == null)
                    id = (String) ((Map)value).get("USER_ID");
                else if (id == null)
                    id = (String) ((Map)value).get("GROUP_ID");
                else if (id == null)
                    id = (String) ((Map)value).get("ACTOR_ID");
                if (id != null)
                    actors.set(i,id );
                else
                    actors.set(i,"" );
            }
            else if (value instanceof String) {
                String token = (String) value;
                int idx = token.indexOf("|uid:") + 5;
                if (idx > 5)
                    actors.set(i,token.substring(idx, token.indexOf("|",idx)));
            }
        }
        value = StringUtils.join(actors, System.getProperty("org.jbpm.ht.user.separator", ","));
        workItem.put(field,value);
        return (String) value;
    }    
}
