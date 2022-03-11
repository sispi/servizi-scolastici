/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.command.callback;

import it.filippetti.ks.api.bpm.command.CommandContext;
import it.filippetti.ks.api.bpm.configuration.ApplicationProperties;
import it.filippetti.ks.api.bpm.model.Instance;
import it.filippetti.ks.api.bpm.model.NotificationContext;
import it.filippetti.ks.api.bpm.repository.InstanceRepository;
import it.filippetti.ks.api.bpm.service.NotificationService;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.mail.Message;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jbpm.workflow.instance.NodeInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author marco.mazzocchetti
 */
public class NotifyOnErrorCommandCallback extends NoOpCommandCallback {

    private static final Logger log = LoggerFactory.getLogger(NotifyOnErrorCommandCallback.class);

    @Autowired
    private ApplicationProperties applicationProperties;
    
    @Autowired
    private NotificationService notificationService;

    @Autowired    
    private InstanceRepository instanceRepository;
    
    @Override
    public void onCommandError(CommandContext ctx, Throwable exception) 
        throws Exception {
        
        Instance instance;
        NodeInstance nodeInstance;
        Collection<String> recipients;

        if (ctx.getInstanceId() == null) {
            throw new IllegalStateException();
        }
        
        // get instance
        instance = instanceRepository
            .findById(ctx.getInstanceId())
            .orElseThrow(() -> {
                throw new IllegalStateException(String.format(
                    "Instance %d not found", 
                    ctx.getInstanceId()));
            });
        // get recipients
        recipients = instance
            .getConfiguration()
            .orElseThrow(() -> {
                throw new IllegalStateException(String.format(
                    "Instance %d configuration not found", 
                    ctx.getInstanceId()));
            })
            .getSettings()
            .getList("sysMail");
        if (recipients == null || recipients.isEmpty()) {
            recipients = List.of(applicationProperties.adminUser());
        }
        
        // create notification
        notificationService
            .newNotification(NotificationContext.of(
                instance, 
                Map.of(
                    "backofficeBaseUrl", applicationProperties.backofficeBaseUrl(),
                    "commandName", ctx.getCommandName(),
                    "message", exception.getMessage() != null ? 
                        exception.getMessage() : 
                        "N/A",
                    "organization", instance.getOrganization(),
                    "processId", instance.getProcessId(), 
                    "instanceId", instance.getId(), 
                    "instanceNodeId", 
                        ctx.getWorkItem() != null && 
                        (nodeInstance = getNodeInstance(ctx.getWorkItem())) != null ? 
                            nodeInstance.getId() : 
                            "N/A")))
            .withTemplate("error")
            .withTag("error")
            .withRecipients(Message.RecipientType.TO, recipients)
            .withAttachment(ExceptionUtils.getStackTrace(exception))
            .build()
            .create();
    }
}
