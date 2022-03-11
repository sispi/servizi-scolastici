/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.repository;

import it.filippetti.ks.api.bpm.model.AuthenticationContext;
import it.filippetti.ks.api.bpm.model.Instance;
import it.filippetti.ks.api.bpm.model.Task;
import it.filippetti.ks.api.bpm.model.TaskAssignedAsFilter;
import it.filippetti.ks.api.bpm.model.TaskAttachmentsFilter;
import it.filippetti.ks.api.bpm.model.TaskExpirationFilter;
import it.filippetti.ks.api.bpm.model.TaskStatusFilter;
import java.util.Date;
import java.util.Set;
import org.kie.api.task.model.Status;
import org.kie.internal.task.api.model.InternalTask;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author marco.mazzocchetti
 */
public interface TaskRepository extends BaseRepository<Task, Long> {

    public default Task findById(AuthenticationContext context, Long id) {
        return findById(
            context.getTenant(),
            context.getOrganization(),
            context.getIdentities(),
            context.isAdmin(),
            id);
    }

    public default Page<Task> findAll(
        AuthenticationContext context, 
        String businessKey,
        String processId, 
        TaskAssignedAsFilter assignedAs,
        TaskStatusFilter status,
        TaskExpirationFilter expiration,
        TaskAttachmentsFilter attachments,
        String subject,
        boolean activeOnly, 
        Pageable pageable) {
        return findAll(
            context.getTenant(),
            context.getOrganization(),
            context.getUserId(),
            context.getIdentities(),
            context.isAdmin(),
            businessKey,
            processId != null ? processId.replaceAll("%", "").toLowerCase().trim() + "%" : "%",
            activeOnly,
            assignedAs.getValue(), 
            status.getValues(), 
            expiration.getValue(),
            attachments.isAny(),
            attachments.getValues(),
            subject != null ? subject.replaceAll("%", "").toLowerCase().trim() + "%" : "%",
            pageable);
    }

    @Transactional
    public default void deleteByInstance(Instance instance) {
        deleteTaskBamLogByInstance(instance.getId());
        deleteTaskAuditLogByInstance(instance.getId());
        deleteTaskEventLogByInstance(instance.getId());
        deleteTaskVariableLogByInstance(instance.getId());
        // too many jbpm tables to manage by hand, we use jpa cascade
        deleteAll(instance.getTasks(false));
        flush();
    }
    
    @Query(""
        + "SELECT t "
        + "FROM Task t "
        + "WHERE t.id = :id "
        + "AND t.tenant = :tenant "    
        + "AND t.organization = :organization "   
        + "AND ("
            + ":isAdmin = true OR "
            + "EXISTS ("
                + "SELECT 1 "
                + "FROM InstanceAccess a "
                + "WHERE a.rootInstance = t.rootInstance "
                + "AND a.identity IN :identities))")
    public Task findById(
        @Param("tenant") String tenant,
        @Param("organization") String organization,
        @Param("identities") Set<String> identities,
        @Param("isAdmin") boolean isAdmin,
        @Param("id") Long id);

    @Query(""
        + "SELECT t "
        + "FROM Task t "
        + "WHERE t.instance = :instance "
        + "AND t.active = CASE "
            + "WHEN :activeOnly = true "
            + "THEN true "
            + "ELSE t.active END")            
    public Page<Task> findByInstance(
        @Param("instance") Instance instance,
        @Param("activeOnly") boolean activeOnly,
        Pageable pageable);   

    @Query(""
        + "SELECT t "
        + "FROM Task t "
        + "WHERE t.rootInstance = :rootInstance "
        + "AND t.active = CASE "
            + "WHEN :activeOnly = true "
            + "THEN true "
            + "ELSE t.active END")
    public Page<Task> findByRootInstance(
        @Param("rootInstance") Instance rootInstance,    
        @Param("activeOnly") boolean activeOnly,
        Pageable pageable);   
    
    @Query(value = ""
        + "SELECT t "
        + "FROM Task t "
        + "WHERE t.tenant = :tenant "
        + "AND t.organization = :organization "
        + "AND ("
            + ":isAdmin = true OR "
            + "EXISTS ("
                + "SELECT 1 "
                + "FROM InstanceAccess a "
                + "WHERE a.rootInstance = t.rootInstance "
                + "AND a.identity IN :identities)) "
        + "AND t.active = CASE "
            + "WHEN :activeOnly = true "
            + "THEN true "
            + "ELSE t.active END "            
        + "AND t.businessKey = CASE "
            + "WHEN COALESCE(:businessKey, NULL) IS NOT NULL "
            + "THEN :businessKey "
            + "ELSE t.businessKey END "            
        + "AND LOWER(t.instance.processId) LIKE :processId "            
        + "AND LOWER(t.internalTask.subject) LIKE :subject "            
        + "AND ("
            + "COALESCE(:status, NULL) IS NULL OR "
            + "t.internalTask.taskData.status IN :status) "   
        + "AND ("
            + "COALESCE(:expireTs, NULL) IS NULL OR "
            + "t.internalTask.taskData.expirationTime IS NOT NULL AND "
                + "t.internalTask.taskData.expirationTime < :expireTs) "
        + "AND ("
            + ":assignedAs IS NULL OR "
            + ":assignedAs = 0 AND "
                + "t.internalTask.taskData.actualOwner.id = :userId OR "
            + ":assignedAs = 1 AND EXISTS("
                + "SELECT 1 "
                + "FROM TaskImpl i "
                + "INNER JOIN i.peopleAssignments.potentialOwners a "
                + "WHERE i.id = t.id "
                + "AND a.id IN :identities) OR "
            + ":assignedAs = 2 AND EXISTS("
                + "SELECT 1 "
                + "FROM TaskImpl i "
                + "INNER JOIN i.peopleAssignments.taskStakeholders a "
                + "WHERE i.id = t.id "
                + "AND a.id IN :identities) OR "
            + ":assignedAs = 0 AND EXISTS("
                + "SELECT 1 "
                + "FROM TaskImpl i "
                + "INNER JOIN i.peopleAssignments.businessAdministrators a "
                + "WHERE i.id = t.id "
                + "AND a.id IN :identities)) "
        + "AND ("
            + "COALESCE(:attachments, NULL) IS NULL OR "
            + "EXISTS ("
                + "SELECT 1 "
                + "FROM TaskImpl i "
                + "INNER JOIN i.taskData.attachments a "
                + "WHERE i.id = t.id "
                + "AND (:anyAttachment = true OR a.name IN :attachments)))")         
    public Page<Task> findAll(
        @Param("tenant") String tenant,
        @Param("organization") String organization,
        @Param("userId") String userId,
        @Param("identities") Set<String> identities,
        @Param("isAdmin") boolean isAdmin,
        @Param("businessKey") String businessKey,
        @Param("processId") String processId,
        @Param("activeOnly") boolean activeOnly,
        @Param("assignedAs") Integer assignedAs,
        @Param("status") Set<Status> status,
        @Param("expireTs") Date expireTs,
        @Param("anyAttachment") boolean anyAttachment,
        @Param("attachments") Set<String> attachments,
        @Param("subject") String subject,
        Pageable pageable);   
    
    @Query(""
        + "SELECT t "
        + "FROM Task t "
        + "WHERE t.workItemId = :workItemId")    
    public Task findByWorkItemId(
        @Param("workItemId") Long workItemId);    

    @Query(""
        + "SELECT t "
        + "FROM TaskImpl t "
        + "WHERE t.taskData.workItemId = :workItemId")    
    public InternalTask findInternalByWorkItemId(
        @Param("workItemId") Long workItemId);   

    @Query(""
        + "SELECT t "
        + "FROM TaskImpl t "
        + "WHERE t.id = :id")    
    public InternalTask findInternalById(
        @Param("id") Long id);   
    
    @Query(""
    + "SELECT t "
    + "FROM Task t "
    + "INNER JOIN t.internalTask.taskData.attachments a "
    + "WHERE t.internalTask.taskData.actualOwner.id = :userId "
    + "AND t.internalTask.taskData.status in ('InProgress', 'Reserved') "
    + "AND a.name = :attachmentName "
    + "AND a.contentType = :attachmentContentType")
    public Task findCompletableByUserAndAttachment(
        @Param("userId") String userId,
        @Param("attachmentName") String attachmentName,
        @Param("attachmentContentType") String attachmentContentType);
        
    @Transactional
    @Modifying
    @Query(""
        + "DELETE FROM AuditTaskImpl t "
        + "WHERE t.processInstanceId = :instanceId")    
    public void deleteTaskAuditLogByInstance(
        @Param("instanceId") Long instanceId);

    @Transactional
    @Modifying
    @Query(""
        + "DELETE FROM BAMTaskSummaryImpl t "
        + "WHERE t.processInstanceId = :instanceId")    
    public void deleteTaskBamLogByInstance(
        @Param("instanceId") Long instanceId);

    @Transactional
    @Modifying
    @Query(""
        + "DELETE FROM TaskEventImpl t "
        + "WHERE t.processInstanceId = :instanceId")    
    public void deleteTaskEventLogByInstance(
        @Param("instanceId") Long instanceId);

    @Transactional
    @Modifying
    @Query(""
        + "DELETE FROM TaskVariableImpl t "
        + "WHERE t.processInstanceId = :instanceId")    
    public void deleteTaskVariableLogByInstance(
        @Param("instanceId") Long instanceId);
   
}
