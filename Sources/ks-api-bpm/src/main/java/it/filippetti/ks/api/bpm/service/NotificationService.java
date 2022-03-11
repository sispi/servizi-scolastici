/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.service;

import com.google.common.base.Charsets;
import com.google.common.collect.Sets;
import it.filippetti.ks.api.bpm.configuration.ApplicationProperties;
import it.filippetti.ks.api.bpm.configuration.EmailConfiguration;
import it.filippetti.ks.api.bpm.dto.CreateNotificationDTO;
import it.filippetti.ks.api.bpm.dto.NotificationDTO;
import it.filippetti.ks.api.bpm.dto.PageDTO;
import it.filippetti.ks.api.bpm.exception.ApplicationException;
import it.filippetti.ks.api.bpm.exception.AuthorizationException;
import it.filippetti.ks.api.bpm.exception.NotFoundException;
import it.filippetti.ks.api.bpm.exception.NotificationException;
import it.filippetti.ks.api.bpm.mapper.MappingContext;
import it.filippetti.ks.api.bpm.mapper.dto.NotificationMapper;
import it.filippetti.ks.api.bpm.model.AuthenticationContext;
import it.filippetti.ks.api.bpm.model.Fetcher;
import it.filippetti.ks.api.bpm.model.Notification;
import it.filippetti.ks.api.bpm.model.NotificationAttachment;
import it.filippetti.ks.api.bpm.model.NotificationContext;
import it.filippetti.ks.api.bpm.model.Pager;
import it.filippetti.ks.api.bpm.model.TagsFilter;
import it.filippetti.ks.api.bpm.repository.NotificationRepository;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.activation.DataHandler;
import javax.activation.MimetypesFileTypeMap;
import javax.annotation.PreDestroy;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import javax.servlet.http.HttpServletResponse;
import keysuite.docer.client.User;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.mvel2.templates.TemplateRuntime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.MimeTypeUtils;

/**
 *
 * @author marco.mazzocchetti
 */
@Service
public class NotificationService {
    
    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);
    
    @Autowired
    private ApplicationProperties applicationProperties;
    
    @Autowired
    private PlatformTransactionManager transactionManager;
    
    @Autowired
    private EmailConfiguration emailConfiguration;

    @Autowired
    private ValidationService validationService;
    
    @Autowired
    private OperationService operationService;

    @Autowired
    private IdentityService identityService;
    
    @Autowired
    private NotificationRepository notificationRepository;
    
    @Autowired
    private NotificationMapper notificationMapper;

    @Autowired
    private TaskScheduler taskScheduler;
    
    private ScheduledFuture cleaner;
    
    @EventListener(ApplicationReadyEvent.class)
    public void startup() {
        if (applicationProperties.isMainNode()) {
            log.info("Starting notification cleaner");
            cleaner = taskScheduler.scheduleWithFixedDelay(() -> {
                    cleanNotifications();
                }, 
                Duration.of(applicationProperties.notificationCleanerDelay(), ChronoUnit.MINUTES));        
        }
    }

    @PreDestroy
    public void shutdown() {
        if (applicationProperties.isMainNode()) {
            log.info("Shutting down notification cleaner");
            cleaner.cancel(false);
        }
    }
    
    /**
     * 
     * @param context
     * @param dto
     * @return
     * @throws ApplicationException 
     */
    public NotificationDTO createNotification(
        AuthenticationContext context, CreateNotificationDTO dto) 
        throws ApplicationException {

        validationService.validate(dto);
        
        return operationService.execute(dto.getOperationId(), () -> {

            Notification notification;
        
            validationService.validate(dto);
            
            // check authorization
            if (!context.isAdmin()) {
                throw new AuthorizationException();
            }        
            
            // start transaction
            TransactionStatus tx = transactionManager.getTransaction(
                new DefaultTransactionDefinition());
            try {
                notification = newNotification(NotificationContext.of(
                        context, 
                        dto.getContextVariables()))
                    .email(dto.getEmail())
                    .withSubject(dto.getSubject())
                    .withBody(dto.getBody())
                    .withFrom(dto.getFrom())
                    .withRecipients(Message.RecipientType.TO, dto.getToRecipients())
                    .withRecipients(Message.RecipientType.CC, dto.getCcRecipients())
                    .withRecipients(Message.RecipientType.BCC, dto.getBccRecipients())
                    .withTags(dto.getTags())
                    .withAttachments(dto.getAttachments())
                    .build()
                    .create();
            }
            catch (Throwable t) {
                // rollback
                transactionManager.rollback(tx);
                throw t;
            }
            // commit
            transactionManager.commit(tx);
            
            // map and return
            return notificationMapper.map(
                notification, 
                MappingContext.of(context));
        },
        NotificationDTO.class);
    }
    
    /**
     * 
     * @param context
     * @param instanceId
     * @param activeOnly
     * @param tags
     * @param pageNumber
     * @param pageSize
     * @param orderBy
     * @param fetch
     * @return
     * @throws ApplicationException 
     */ 
    public PageDTO<NotificationDTO> getNotifications(
        AuthenticationContext context, 
        boolean activeOnly,
        String tags,
        Integer pageNumber, Integer pageSize, String orderBy,
        String fetch) 
        throws ApplicationException {
        
        return notificationMapper.map(notificationRepository.findAll(context,
                activeOnly, 
                TagsFilter.of(tags),
                Pager.of(Notification.class, pageNumber, pageSize, orderBy)), 
            MappingContext.of(
                context, 
                Fetcher.of(Notification.class, fetch)));
    }

    /**
     * 
     * @param context
     * @param notificationId
     * @param fetch
     * @return
     * @throws ApplicationException 
     */   
    public NotificationDTO getNotification(
        AuthenticationContext context, Long notificationId, 
        String fetch) 
        throws ApplicationException {
        
        Notification notification;
        
        // get notification
        notification =  notificationRepository.findById(context, notificationId);
        if (notification == null) {
            throw new NotFoundException();
        }
        
        return notificationMapper.map(
            notification, 
            MappingContext.of(
                context, 
                Fetcher.of(Notification.class, fetch)));
    }

    /**
     * 
     * @param context
     * @param notificationId
     * @param attachmentName
     * @param response
     * @throws ApplicationException
     * @throws IOException 
     */   
    @Transactional(readOnly = true)    
    public void downloadNotificationAttachment(
        AuthenticationContext context, Long notificationId, String attachmentName, HttpServletResponse response)
        throws ApplicationException, IOException {
        
        Notification notification;
        NotificationAttachment attachment;
        
        // get notification
        notification =  notificationRepository.findById(context, notificationId);
        if (notification == null) {
            throw new NotFoundException();
        }
        // get attachment
        attachment = notification.getAttachment(attachmentName);
        if (attachment == null) {
            throw new NotFoundException();
        }

        response.setStatus(HttpStatus.OK.value());
        response.setContentType(attachment.getContentType());
        response.setHeader("Content-Disposition", String.format(
            "attachment; filename=\"%s\"", 
            attachment.getName())); 
        attachment.getContent().write(response.getOutputStream());        
    }
    
    /**
     * 
     * @param context
     * @param dto
     * @return
     * @throws ApplicationException 
     */
    public NotificationDTO markNotificationAsRead(
        AuthenticationContext context, Long notificationId) 
        throws ApplicationException {

        Notification notification;

        // start transaction
        TransactionStatus tx = transactionManager.getTransaction(
            new DefaultTransactionDefinition());
        try {
            // get notification
            notification =  notificationRepository.findById(context, notificationId);
            if (notification == null) {
                throw new NotFoundException();
            }
            // check authorization
            if (!notification.isRecipient(context)) {
                throw new AuthorizationException();
            }              
            // mark as read
            notification.getRecipient(context).markAsRead();
        }
        catch (Throwable t) {
            // rollback
            transactionManager.rollback(tx);
            throw t;
        }
        // commit
        transactionManager.commit(tx);

        // map and return
        return notificationMapper.map(
            notification, 
            MappingContext.of(context));
    }
    
    /**
     * 
     * @param context
     * @return 
     */
    public Builder newNotification(NotificationContext context) {
        return new Builder(context);
    }
    
    /**
     * 
     */
    public void cleanNotifications() {
        
        int c;
        List<Long> cleanables;
        
        c = 0;
        while ((cleanables = notificationRepository.findCleanables(
            applicationProperties.notificationCleanerBatchSize())).size() > 0) {
            for (Long id : cleanables) {
                // start transaction
                TransactionStatus tx = transactionManager.getTransaction(
                     new DefaultTransactionDefinition());
                try {
                    notificationRepository.deleteById(id);
                } catch (Throwable t) {
                    // rollback
                    transactionManager.rollback(tx);
                    log.error(String.format("Unexpected error cleaning notification %d", id), t);            
                }
                // commit
                transactionManager.commit(tx);
                c++;      
            }
        }
        if (c > 0) {
            log.info(String.format("Cleaned %d notification(s): %s", c, cleanables));      
        }
    }
    
    /**
     * 
     * @param request
     * @return
     * @throws NotificationException 
     */
    private Notification createNotification(Request request) throws NotificationException {
        
        Notification notification;
        MimeMessage mimeMessage;
        MimeBodyPart mimeBodyPart;
        Multipart multipart;
        JavaMailSender sender;
        
        if (request.getToRecipients().isEmpty()) {
            throw new NotificationException(String.format("ToRecipients resolves to an empty set"));            
        }
        if (request.getSubject() == null) {
            throw new NotificationException(String.format("Missing subject"));            
        }
        TransactionStatus tx = transactionManager.getTransaction(
            new DefaultTransactionDefinition());
        try{
            notification = notificationRepository.save(new Notification(
                request.getContext(),
                request.getSubject(), 
                request.getBody(), 
                request.getPriority(), 
                request.getExpireTs(), 
                request.getTags(), 
                request.getRecipients()
                    .stream()
                    .map(u -> u.getId())
                    .collect(Collectors.toSet()), 
                request.getAttachments()));
        } catch(Throwable t){
            transactionManager.rollback(tx);
            throw t;
        }
        transactionManager.commit(tx);
        
        if (applicationProperties.notificationEmailEnabled() && request.isEmail()) {
            try {
                // get sender
                sender = emailConfiguration
                    .source(request.getFrom().getAddress())
                    .mailSender();
                
                // build email message
                mimeMessage = sender.createMimeMessage();
                mimeMessage.setFrom(request.getFrom());
                if (request.getReplyTo() != null) {
                    mimeMessage.setReplyTo(new InternetAddress[]{request.getReplyTo()});
                }
                mimeMessage.addRecipients(Message.RecipientType.TO, request.getToRecipients()
                    .stream()
                    .filter(u -> u.getAddress() != null)
                    .map(u -> u.getAddress())
                    .toArray(InternetAddress[]::new));
                mimeMessage.addRecipients(Message.RecipientType.CC, request.getCcRecipients()
                    .stream()
                    .filter(u -> u.getAddress() != null)
                    .map(u -> u.getAddress())
                    .toArray(InternetAddress[]::new));
                mimeMessage.addRecipients(Message.RecipientType.BCC, request.getBccRecipients()
                    .stream()
                    .filter(u -> u.getAddress() != null)                        
                    .map(u -> u.getAddress())
                    .toArray(InternetAddress[]::new));
                mimeMessage.setSubject(request.getSubject());
                if (!request.getAttachments().isEmpty()) {
                    multipart = new MimeMultipart();
                    if (request.getBody() != null) {
                        mimeBodyPart = new MimeBodyPart();
                        mimeBodyPart.setDataHandler(new DataHandler(new ByteArrayDataSource(request.getBody(), MimeTypeUtils.TEXT_HTML_VALUE)));
                        multipart.addBodyPart(mimeBodyPart);
                    }
                    for (Map.Entry<String, byte[]> attachment : request.getAttachments().entrySet()) {
                        mimeBodyPart = new MimeBodyPart();
                        mimeBodyPart.setDataHandler(new DataHandler(new ByteArrayDataSource(
                            attachment.getValue(), 
                            MimetypesFileTypeMap.getDefaultFileTypeMap().getContentType(attachment.getKey()))));
                        mimeBodyPart.setFileName(attachment.getKey());
                        mimeBodyPart.setContentID(String.format("<%s>", attachment.getKey()));
                        multipart.addBodyPart(mimeBodyPart);
                    }
                    mimeMessage.setContent(multipart);
                } else if (request.getBody() != null) {
                    mimeMessage.setDataHandler(new DataHandler(new ByteArrayDataSource(
                        request.getBody(), 
                        MimeTypeUtils.TEXT_HTML_VALUE)));
                }
                mimeMessage.setSentDate(new Date());
                mimeMessage.setHeader("X-Mailer", "KeySuite Mail Service");

                // send email message
                sender.send(mimeMessage);
            } catch(IOException | MessagingException | MailException e){
                log.error(String.format("Email send error: %s", e.getMessage()));
            }
        }
        return notification;
    }

    /**
     * 
     * @param tags
     * @return 
     */
    private Set<String> resolveTags(Collection<String> tags) {
        return tags
            .stream()
            .filter(t -> !StringUtils.isBlank(t))
            .collect(Collectors.toUnmodifiableSet());
    }
    
    /**
     * 
     * @param context
     * @param from
     * @return
     */
    private InternetAddress resolveFrom(
        NotificationContext context, String from) { 
        
        String resolvedFrom;
        
        try {
            if (from.contains("<at>") || from.contains("@")) {
                resolvedFrom = from.replace("<at>", "@");
            } else {
                resolvedFrom = identityService.getActor(context.getTenant(), from).get().getEmail(); 
            }
            return emailConfiguration.source(resolvedFrom).from();
        } catch (NoSuchElementException | NullPointerException e) {
            log.warn(String.format("Invalid from: %s not allowed, using default", from));
            return emailConfiguration.defaultSource().from();
        }
    }
    
    /**
     * 
     * @param recipients
     * @return 
     */
    private Set<Recipient> resolveRecipients(
        NotificationContext context, Collection<String> recipients) 
        throws NotificationException { 
        
        User user;
        Set<Recipient> resolvedRecipient;
        
        recipients = recipients
            .stream()
            .filter(r -> !StringUtils.isBlank(r))
            .collect(Collectors.toList());
        resolvedRecipient = new HashSet<>();
        for (String recipient : recipients) {
            if(recipient.contains("<at>") || recipient.contains("@")) {
                resolvedRecipient.add(new Recipient(recipient.replace("<at>", "@")));
            }
            else if ((user = identityService.getUser(context.getTenant(), recipient).orElse(null)) != null) {
                resolvedRecipient.add(new Recipient(user));
            }
            else if (identityService.getGroup(context.getTenant(), recipient).orElse(null) != null) {
                for (User groupUser : identityService.getGroupUsers(context.getTenant(), recipient)) {
                    resolvedRecipient.add(new Recipient(groupUser));
                }
            } else {
                throw new NotificationException(String.format("Invalid recipient: %s", recipient));
            }
        }
        return Collections.unmodifiableSet(resolvedRecipient);
    }
    
    /**
     * 
     * @param attachments
     * @return 
     */
    private Map<String, byte[]> resolveAttachments(NotificationContext context, Collection<String> attachments) 
        throws NotificationException {
        
        Map<String, byte[]> resolvedAttachments;
        URL url;
        HttpURLConnection urlConnection;
        String urlString, urlAuthorization, urlHeader, urlFilename;        
        int i, urlResponseCode;
        
        attachments = attachments
            .stream()
            .filter(r -> !StringUtils.isBlank(r))
            .collect(Collectors.toList());
        i = 1;
        resolvedAttachments = new HashMap<>();
        if (!attachments.isEmpty()) {
            urlAuthorization = String.format(
                "Bearer %s", 
                identityService.generateAdminJwtToken(context.getTenant(), context.getOrganization()));
            for (String attachment : attachments) {
                if (attachment.matches("^https?://.+")) {
                    urlString = attachment.replace(
                        applicationProperties.documentBaseUrl(),
                        "file://" + applicationProperties.documentBasePath());
                    try {
                        url = new URL(urlString);
                        if (url.getProtocol().startsWith("http")) {
                            urlConnection = (HttpURLConnection) url.openConnection();
                            urlConnection.setRequestProperty("Authorization", urlAuthorization);
                            urlConnection.connect();
                            if ((urlResponseCode = urlConnection.getResponseCode()) == HttpStatus.OK.value()) {
                                urlHeader = urlConnection.getHeaderField("Content-Disposition");
                                if (urlHeader != null && urlHeader.contains("filename")) {
                                    urlFilename = urlHeader.split("=")[1].replaceAll ("\"", "").replaceAll ("]", "");
                                } else {
                                    urlFilename = new File(url.getFile()).getName();
                                }
                                if (StringUtils.isBlank(urlFilename)) {
                                    urlFilename = String.format("attachment-%d", i++); 
                                }
                                resolvedAttachments.put(
                                    urlFilename,
                                    IOUtils.toByteArray(urlConnection));
                            } else {
                                throw new IOException(String.format(
                                    "Response code is %d", 
                                    urlResponseCode));
                            }
                        }
                        // file
                        else {
                            resolvedAttachments.put(
                                new File(url.getFile()).getName(),
                                IOUtils.toByteArray(url));
                        }
                    } catch (MalformedURLException e) {
                        throw new NotificationException(String.format("Invalid attachment url: %s", urlString));
                    } catch (IOException e) {
                        throw new NotificationException(String.format("Error getting attachment at url %s: %s", urlString, e.getMessage()));
                    }
                } 
                else {
                    resolvedAttachments.put(
                        String.format("attachment-%d.txt", i++), 
                        attachment.getBytes(Charsets.UTF_8));
                }
            }
        }    
        return Collections.unmodifiableMap(resolvedAttachments);
    }    

    /**
     * 
     */
    public static class Recipient {
        
        private String id;
        private InternetAddress address;
        
        private Recipient(String email) {
            this(email, email, null);
        }
        
        private Recipient(User user) {
            this(user.getUserName(), user.getEmail(), user.getFullName());
        }

        private Recipient(String id, String email, String personal) {
            this.id = id;
            try {
                address = InternetAddress.parse(email, false)[0];
                if (personal != null) {
                    try {
                        address.setPersonal(personal);
                    } catch (UnsupportedEncodingException e) {
                        // skip personal, do not throw errors
                    }
                }
            } catch (NullPointerException | AddressException e) {
                log.warn(String.format("Invalid recipient '%s' email: %s", id, email));
            }
        }

        public String getId() {
            return id;
        }

        public InternetAddress getAddress() {
            return address;
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 59 * hash + Objects.hashCode(this.id);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Recipient other = (Recipient) obj;
            return Objects.equals(this.id, other.id);
        }
    }    
    
    /**
     * 
     */
    public class Request {
        
        private NotificationContext context;
        private Boolean email;
        private InternetAddress from;
        private InternetAddress replyTo;
        private String subject;
        private String body;
        private Boolean priority;
        private Date expireTs;
        private Set<String> tags;
        private Map<Message.RecipientType, Set<Recipient>> recipients;
        private Map<String, byte[]> attachments;

        private Request(NotificationContext context) {
            this.context = context;
            init();
        }

        public Boolean isEmail() {
            return email;
        }

        public NotificationContext getContext() {
            return context;
        }

        public InternetAddress getFrom() {
            return from;
        }

        public InternetAddress getReplyTo() {
            return replyTo;
        }

        public String getSubject() {
            return subject;
        }

        public String getBody() {
            return body;
        }

        public Boolean getPriority() {
            return priority;
        }

        public Date getExpireTs() {
            return expireTs;
        }

        public Set<String> getTags() {
            return Collections.unmodifiableSet(tags);
        }

        public Set<Recipient> getToRecipients() {
            return Collections.unmodifiableSet(recipients.get(Message.RecipientType.TO));
        }

        public Set<Recipient> getCcRecipients() {
            return Collections.unmodifiableSet(recipients.get(Message.RecipientType.CC));
        }

        public Set<Recipient> getBccRecipients() {
            return Collections.unmodifiableSet(recipients.get(Message.RecipientType.BCC));
        }

        public Set<Recipient> getRecipients() {
            
            return Stream
                .of(
                    recipients.get(Message.RecipientType.TO), 
                    recipients.get(Message.RecipientType.CC), 
                    recipients.get(Message.RecipientType.BCC))
                 .flatMap(x -> x.stream())
                 .collect(Collectors.toUnmodifiableSet());
        }

        public Map<String, byte[]> getAttachments() {
            return Collections.unmodifiableMap(attachments);
        }
        
        public Notification create() throws NotificationException {
            return createNotification(this);
        }

        private void init() {
            this.email = true;
            this.from = emailConfiguration.defaultSource().from();
            this.replyTo = emailConfiguration.defaultSource().replyTo();
            this.priority = false;
            this.expireTs = Notification.NEVER_EXPIRE_TS;
            this.tags = new HashSet<>();
            this.recipients = Map.of(
                Message.RecipientType.TO, new HashSet<>(),
                Message.RecipientType.CC, new HashSet<>(),
                Message.RecipientType.BCC, new HashSet<>());
            this.attachments = new HashMap<>();
        }    
    }
    
    /**
     * 
     */
    public class Builder {

        private Request request;

        private Builder(NotificationContext context) {
            request = new Request(context);
        }

        public Builder email(boolean email) {
            request.email = email;
            return this;
        }

        public Builder withFrom(String from) {
            if (from != null) {
                request.from = resolveFrom(request.context, from);
            }
            return this;
        }

        public Builder withReplyTo(String replyTo) throws NotificationException {
            try {
                request.replyTo = replyTo != null  ? 
                    InternetAddress.parse(replyTo, false)[0] : 
                    null;
            } catch (NullPointerException | AddressException e) {
                throw new NotificationException(String.format("Invalid replyTo: %s", e.getMessage()));
            }
            return this;
        }

        public Builder withSubject(String subject) throws NotificationException {
            try {
                request.subject = subject != null ? 
                    (String) TemplateRuntime.eval(subject, request.context.getVariables()) :
                    null;
            } catch (RuntimeException e) {
                throw new NotificationException(String.format("Invalid subject: %s", e.getMessage()));
            }
            return this;
        }

        public Builder withBody(String body) throws NotificationException {
            try {
                request.body = body != null ? 
                    (String) TemplateRuntime.eval(body, request.context.getVariables()) :
                    null;
            } catch (RuntimeException e) {
                throw new NotificationException(String.format("Invalid body: %s", e.getMessage()));
            }
            return this;
        }

        public Builder withTemplate(String template) throws NotificationException {
            try {
                withSubject(IOUtils.toString(new ClassPathResource(String.format(
                    "notification/template/%s.subject.txt", 
                     template)).getInputStream()));
                withBody(IOUtils.toString(new ClassPathResource(String.format(
                    "notification/template/%s.body.html", 
                    template)).getInputStream()));
            } catch (IOException e) {
                throw new NotificationException(String.format("Invalid template: %s", template));
            }
            return this;
        }

        public Builder withPriority(boolean priority) {
            request.priority = priority;
            return this;
        }

        public Builder withExpireTs(Date expireTs) {
            if (expireTs == null) {
                request.expireTs = Notification.NEVER_EXPIRE_TS;
            } else {
                request.expireTs = expireTs;
            }
            return this;
        }

        public Builder withTag(String tag) {
            return withTags(Sets.newHashSet(tag));
        }

        public Builder withTags(Collection<String> tags) {
            if (tags != null) {
                request.tags.addAll(resolveTags(tags));
            }
            return this;
        }

        public Builder withRecipient(Message.RecipientType type, String recipient) throws NotificationException {
            return withRecipients(type, Sets.newHashSet(recipient));
        }

        public Builder withRecipients(Message.RecipientType type, Collection<String> recipients) throws NotificationException {
            if (recipients != null) {
                request.recipients.get(type).addAll(resolveRecipients(request.context, recipients));
            }
            return this;
        }

        public Builder withAttachment(String attachment) throws NotificationException {
            return withAttachments(Sets.newHashSet(attachment));
        }

        public Builder withAttachments(Collection<String> attachments) throws NotificationException {
            if (attachments != null) {
                request.attachments.putAll(resolveAttachments(request.getContext(), attachments));
            }
            return this;
        }
        
        public Request build() {
            return request;
        }
    }   
}
