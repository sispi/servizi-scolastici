/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.model;

import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author marco.mazzocchetti
 */
@Entity(name = "InAppNotification")
@Table(
    name = "KS_NOTIFICATION",
    indexes = {
        @Index(columnList = "tenant, organization, expireTs"),
        @Index(columnList = "instance_id, expireTs"),
        @Index(columnList = "task_id, expireTs")
    }
)
@Sortables(
    defaultSort = "priority:DESC,notifyTs:ASC",
    value = {
        @Sortable(property = "priority"),  
        @Sortable(property = "notifyTs", pathExpression = "id"),  
        @Sortable(property = "expireTs")  
    }
)
@Fetchables({
    @Fetchable(property = "body"),
    @Fetchable(property = "instance", type = Instance.class),
    @Fetchable(property = "task", type = Task.class),
    @Fetchable(property = "tags"),
    @Fetchable(property = "recipients", type = NotificationRecipient.class),
    @Fetchable(property = "attachments", type = NotificationAttachment.class)
})
public class Notification extends Auditable {

    public static final Date NEVER_EXPIRE_TS =  new GregorianCalendar(9999, 0, 0, 0, 0, 0).getTime();
        
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;    
    
    @Basic
    @Column(nullable = false)
    private String tenant;

    @Basic
    @Column(nullable = false)
    private String organization;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(nullable = true)
    private Instance instance;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(nullable = true)
    private Task task;
    
    @Basic
    @Column(nullable = false)
    private String subject;    

    @OneToOne(mappedBy = "notification", fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = true)
    private NotificationBody body; 

    @Basic
    @Column(nullable = false)
    private Boolean priority;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date notifyTs;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)    
    private Date expireTs;

    @OrderBy("value ASC")    
    @OneToMany(mappedBy = "notification", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<NotificationTag> tags;   
    
    @OrderBy("userId ASC")
    @OneToMany(mappedBy = "notification", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<NotificationRecipient> recipients;   
    
    @OrderBy("name ASC")
    @OneToMany(mappedBy = "notification", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<NotificationAttachment> attachments;
    
    public Notification() {
    }

    public Notification(
        NotificationContext context,
        String subject, 
        String body, 
        Boolean priority, 
        Date expireTs,
        Set<String> tags, 
        Set<String> recipients, 
        Map<String, byte[]> attachments) {
        this.tenant = context.getTenant();
        this.organization = context.getOrganization();
        this.instance = context.getInstance();
        this.task = context.getTask();
        this.subject = StringUtils.abbreviate(subject, 255);
        this.body = new NotificationBody(this, body);
        this.priority = priority;
        this.notifyTs = new Date();
        this.expireTs = expireTs;
        this.tags = Stream
            .concat(context.getDefaultTags().stream(), tags.stream())
            .map(t -> new NotificationTag(this, t))
            .collect(Collectors.toList());
        this.recipients = recipients
            .stream()
            .map(r -> new NotificationRecipient(this, r))
            .collect(Collectors.toList());
        this.attachments = attachments
            .entrySet()
            .stream()
            .map(a -> new NotificationAttachment(this, a.getKey(), a.getValue()))
            .collect(Collectors.toList());
    }
    
    @Override
    public Long getId() {
        return id;
    }

    public String getTenant() {
        return tenant;
    }

    public String getOrganization() {
        return organization;
    }

    public Instance getInstance() {
        return instance;
    }
    
    public Task getTask() {
        return task;
    }

    public String getSubject() {
        return subject;
    }

    public NotificationBody getBody() {
        return body;
    }

    public boolean hasPriority() {
        return priority;
    }

    public Date getNotifyTs() {
        return notifyTs;
    }

    public Date getExpireTs() {
        return expireTs;
    }

    public List<String> getTags() {
        return tags
            .stream()
            .map(t -> t.getValue())
            .sorted()
            .collect(Collectors.toList());
    }

    public boolean isRecipient(AuthenticationContext context) {
        return recipients
            .stream()
            .anyMatch(r -> r.getUserId().equals(context.getUserId()));
    }

    public NotificationRecipient getRecipient(AuthenticationContext context) {
        return getRecipient(context.getUserId());
    }

    public NotificationRecipient getRecipient(String userId) {
        return recipients
            .stream()
            .filter(r -> r.getUserId().equals(userId))
            .findFirst()
            .orElse(null);
    }
    
    public List<NotificationRecipient> getRecipients() {
        return Collections.unmodifiableList(recipients);
    }

    public List<NotificationAttachment> getAttachments() {
        return Collections.unmodifiableList(attachments);
    }

    public NotificationAttachment getAttachment(String name) {
        return attachments
            .stream()
            .filter(a -> a.getName().equals(name))
            .findFirst()
            .orElse(null);
    }    
}
