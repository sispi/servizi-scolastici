/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.repository;

import it.filippetti.ks.api.bpm.model.AuthenticationContext;
import it.filippetti.ks.api.bpm.model.Instance;
import it.filippetti.ks.api.bpm.model.Notification;
import it.filippetti.ks.api.bpm.model.TagsFilter;
import it.filippetti.ks.api.bpm.model.Task;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author marco.mazzocchetti
 */
public interface NotificationRepository extends BaseRepository<Notification, Long> {
    
    /**
     * 
     * @param context
     * @param id
     * @return 
     */
    public default Notification findById(AuthenticationContext context, Long id) {
        return findById(
            context.getTenant(), 
            context.getOrganization(),
            id);
    }
    
    /**
     * 
     * @param context
     * @param instance
     * @param activeOnly
     * @param tags
     * @param pageable
     * @return 
     */
    public default Page<Notification> findByInstance(
        AuthenticationContext context, Instance instance, boolean activeOnly, TagsFilter tags, Pageable pageable) {
        return findByInstance(
            instance,
            activeOnly ? new Date() : Date.from(Instant.EPOCH),
            activeOnly ? context.getUserId() : null,
            tags.getValues().size(),
            tags.getValues(),
            pageable);   
    }    
    
    /**
     * 
     * @param context
     * @param task
     * @param activeOnly
     * @param tags
     * @param pageable
     * @return 
     */
    public default Page<Notification> findByTask(
        AuthenticationContext context, Task task, boolean activeOnly, TagsFilter tags, Pageable pageable) {
        return findByTask(
            task,
            activeOnly ? new Date() : Date.from(Instant.EPOCH),
            activeOnly ? context.getUserId() : null,
            tags.getValues().size(),
            tags.getValues(),
            pageable);   
    }       

    /**
     * 
     * @param context
     * @param activeOnly
     * @param tags
     * @param pageable
     * @return 
     */
    public default Page<Notification> findAll(
        AuthenticationContext context, boolean activeOnly, TagsFilter tags, Pageable pageable) {
        return findAll(
            context.getTenant(),
            context.getOrganization(),
            activeOnly ? new Date() : Date.from(Instant.EPOCH),
            activeOnly ? context.getUserId() : null,
            tags.getValues().size(),
            tags.getValues(),
            pageable);
    }    
    
    /**
     * 
     * @param batchSize
     * @return 
     */
    public default List<Long> findCleanables(int batchSize) {
        return findCleanables(new Date(), PageRequest.of(0, batchSize));
    }    
    
    /**
     * 
     * @param instance 
     */
    public default void deleteByInstance(Instance instance) {
        deleteNotificationBodyByInstance(instance);
        deleteNotificationTagByInstance(instance);
        deleteNotificationRecipientByInstance(instance);
        deleteNotificationAttachmentContentByInstance(instance);
        deleteNotificationAttachmentByInstance(instance);
        deleteNotificationByInstance(instance);
    }
    
    @Query(""
        + "SELECT n "
        + "FROM InAppNotification n "
        + "WHERE n.id = :id "
        + "AND n.tenant = :tenant "    
        + "AND n.organization = :organization")    
    public Notification findById(
        @Param("tenant") String tenant,
        @Param("organization") String organization,
        @Param("id") Long id);

    @Query(""
        + "SELECT n "
        + "FROM InAppNotification n "
        + "WHERE n.instance = :instance "
        + "AND n.expireTs > :expireTs "
        + "AND ("
            + ":userId IS NULL OR "
            + "EXISTS ("
                + "SELECT 1 "
                + "FROM NotificationRecipient r "
                + "WHERE r.notification = n "
                + "AND r.userId = :userId "
                + "AND r.readTs IS NULL)) "
        + "AND ("
            + ":tagsSize = 0 OR "
            + ":tagsSize = ("
                + "SELECT count(t) "
                + "FROM NotificationTag t "
                + "WHERE t.notification = n "
                + "AND t.value IN :tags))")    
    public Page<Notification> findByInstance(
        @Param("instance") Instance instance,
        @Param("expireTs") Date expireTs,
        @Param("userId") String userId,
        @Param("tagsSize") int tagsSize,
        @Param("tags") Set<String> tags,
        Pageable pageable);    

    @Query(""
        + "SELECT n "
        + "FROM InAppNotification n "
        + "WHERE n.task = :task "
        + "AND n.expireTs > :expireTs "
        + "AND ("
            + ":userId IS NULL OR "
            + "EXISTS ("
                + "SELECT 1 "
                + "FROM NotificationRecipient r "
                + "WHERE r.notification = n "
                + "AND r.userId = :userId "
                + "AND r.readTs IS NULL)) "
        + "AND ("
            + ":tagsSize = 0 OR"
            + ":tagsSize = ("
                + "SELECT count(t) "
                + "FROM NotificationTag t "
                + "WHERE t.notification = n "
                + "AND t.value IN :tags))")    
    public Page<Notification> findByTask(
        @Param("task") Task instance,
        @Param("expireTs") Date expireTs,
        @Param("userId") String userId,
        @Param("tagsSize") int tagsSize,
        @Param("tags") Set<String> tags,
        Pageable pageable);    
    
    @Query(""
        + "SELECT n "
        + "FROM InAppNotification n "
        + "WHERE n.tenant = :tenant "
        + "AND n.organization = :organization "
        + "AND n.expireTs > :expireTs "
        + "AND ("
            + ":userId IS NULL OR "
            + "EXISTS ("
                + "SELECT 1 "
                + "FROM NotificationRecipient r "
                + "WHERE r.notification = n "
                + "AND r.userId = :userId "
                + "AND r.readTs IS NULL)) "
        + "AND ("
            + ":tagsSize = 0 OR "
            + ":tagsSize = ("
                + "SELECT count(t) "
                + "FROM NotificationTag t "
                + "WHERE t.notification = n "
                + "AND t.value IN :tags))")    
    public Page<Notification> findAll(
        @Param("tenant") String tenant,
        @Param("organization") String organization,
        @Param("expireTs") Date expireTs,
        @Param("userId") String userId,
        @Param("tagsSize") int tagsSize,
        @Param("tags") Set<String> tags,
        Pageable pageable);    
    
    @Query(""
        + "SELECT n.id "
        + "FROM InAppNotification n "
        + "WHERE n.instance IS NULL "
        + "AND n.expireTs < :ts")    
    public List<Long> findCleanables(
        @Param("ts") Date ts,
        Pageable pageable);
    
    @Transactional
    @Modifying
    @Query(""
        + "DELETE FROM InAppNotification n "
        + "WHERE n.instance = :instance")    
    public void deleteNotificationByInstance(
        @Param("instance") Instance instance);

    @Transactional
    @Modifying
    @Query(""
        + "DELETE FROM NotificationTag n "
        + "WHERE EXISTS ("
            + "SELECT 1 "
            + "FROM InAppNotification j "
            + "WHERE j = n.notification "
            + "AND j.instance = :instance)")    
    public void deleteNotificationTagByInstance(
        @Param("instance") Instance instance);   

    @Transactional
    @Modifying
    @Query(""
        + "DELETE FROM NotificationBody n "
        + "WHERE EXISTS ("
            + "SELECT 1 "
            + "FROM InAppNotification j "
            + "WHERE j = n.notification "
            + "AND j.instance = :instance)")    
    public void deleteNotificationBodyByInstance(
        @Param("instance") Instance instance);

    @Transactional
    @Modifying
    @Query(""
        + "DELETE FROM NotificationRecipient n "
        + "WHERE EXISTS ("
            + "SELECT 1 "
            + "FROM InAppNotification j "
            + "WHERE j = n.notification "
            + "AND j.instance = :instance)")    
    public void deleteNotificationRecipientByInstance(
        @Param("instance") Instance instance);     

    @Transactional
    @Modifying
    @Query(""
        + "DELETE FROM NotificationAttachment n "
        + "WHERE EXISTS ("
            + "SELECT 1 "
            + "FROM InAppNotification j "
            + "WHERE j = n.notification "
            + "AND j.instance = :instance)")    
    public void deleteNotificationAttachmentByInstance(
        @Param("instance") Instance instance);   
    
    @Transactional
    @Modifying
    @Query(""
        + "DELETE FROM NotificationAttachmentContent n "
        + "WHERE EXISTS ("
            + "SELECT 1 "
            + "FROM InAppNotification j "
            + "WHERE j = n.attachment.notification "
            + "AND j.instance = :instance)")    
    public void deleteNotificationAttachmentContentByInstance(
        @Param("instance") Instance instance);     
}
