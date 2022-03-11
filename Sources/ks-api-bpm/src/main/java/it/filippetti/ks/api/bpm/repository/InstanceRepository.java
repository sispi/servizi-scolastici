/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.repository;

import it.filippetti.ks.api.bpm.model.AuthenticationContext;
import it.filippetti.ks.api.bpm.model.Configuration;
import it.filippetti.ks.api.bpm.model.Instance;
import it.filippetti.ks.api.bpm.model.InstanceReference;
import it.filippetti.ks.api.bpm.model.InstanceStatusFilter;
import java.util.Date;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author marco.mazzocchetti
 */
@Repository
@Transactional
public interface InstanceRepository extends BaseRepository<Instance, Long> {

    public default Instance findById(AuthenticationContext context, Long id) {
        return findById(
            context.getTenant(),
            context.getOrganization(),
            context.isAdmin(),
            context.getIdentities(),
            id);
    }

    public default Page<Instance> findAll(
        AuthenticationContext context, 
        String processId,
        InstanceStatusFilter status,
        Boolean taskOwner,
        boolean rootOnly, boolean activeOnly, boolean searchableOnly, boolean unreadChatOnly, boolean errorOnly,
        Pageable pageable) {
        return findAll(
            context.getTenant(),
            context.getOrganization(),
            processId != null ? processId.replaceAll("%", "").toLowerCase().trim() + "%" : "%",
            status.getValues(),
            taskOwner,
            context.getUserId(),
            context.isAdmin(),
            context.getIdentities(),
            rootOnly, activeOnly, searchableOnly, unreadChatOnly, errorOnly,
            pageable);
    }

    /**
     * 
     * @param batchSize
     * @return 
     */
    public default List<Long> findArchiviables(int batchSize) {
        return findArchiviables(new Date(), PageRequest.of(0, batchSize));
    }    
    
    /**
     * 
     * @param instance 
     */
    @Override
    @Transactional
    public default void delete(Instance instance) {
        deleteInstanceLog(instance.getId());
        deleteInstanceIO(instance);
        deleteInstance(instance);
    }
    
    @Query(""
        + "SELECT i "
        + "FROM Instance i "
        + "WHERE i.id = :id "
        + "AND i.tenant = :tenant "    
        + "AND i.organization = :organization "    
        + "AND ("
            + ":isAdmin = true OR "            
            + "EXISTS ("
                + "SELECT 1 "
                + "FROM InstanceAccess a "
                + "WHERE a.rootInstance = i.rootInstance "
                + "AND a.identity IN :identities))")         
    public Instance findById(
        @Param("tenant") String tenant,
        @Param("organization") String organization,
        @Param("isAdmin") boolean isAdmin,
        @Param("identities") Set<String> identities,
        @Param("id") Long id);

    @Query(""
        + "SELECT i "
        + "FROM Instance i "
        + "WHERE i.tenant = :tenant "
        + "AND i.organization = :organization "
        + "AND LOWER(i.processId) LIKE :processId "      
        + "AND i.root = CASE "
            + "WHEN :rootOnly = true "
            + "THEN true "
            + "ELSE i.root END "          
        + "AND i.active = CASE "
            + "WHEN :activeOnly = true "
            + "THEN true "
            + "ELSE i.active END "            
        + "AND i.configuration.searchable = CASE "
            + "WHEN :searchableOnly = true "
            + "THEN true "
            + "ELSE i.configuration.searchable END "            
        + "AND ("
            + "COALESCE(:status, NULL) IS NULL OR "
            + "i.status IN :status) "   
        + "AND ("
            + ":unreadChatOnly = false OR "
            + "EXISTS ("
                + "SELECT 1 "
                + "FROM ChatMember m "
                + "WHERE m.instance = i.rootInstance "
                + "AND m.lastReadTs < m.chat.lastSendTs)) "         
        + "AND ("
            + ":isAdmin = true OR "
            + "EXISTS ("
                + "SELECT 1 "
                + "FROM InstanceAccess a "
                + "WHERE a.rootInstance = i.rootInstance "
                + "AND a.identity IN :identities)) "
        + "AND ("
            + ":errorOnly = false OR "
            + "EXISTS ("
                + "SELECT 1 "
                + "FROM InstanceCommand e "
                + "WHERE e.instance = i "
                + "AND e.status = 'ERROR' "
                + "AND e.executionTs = ("
                    + "SELECT MAX(c.executionTs) "
                    + "FROM InstanceCommand c "
                    + "WHERE c.instance = i))) "
        + "AND ("
            + ":taskOwner IS NULL OR "
            + ":taskOwner = true AND EXISTS ("
                + "SELECT 1 "
                + "FROM TaskImpl t "
                + "INNER JOIN t.peopleAssignments.potentialOwners a "
                + "WHERE t.taskData.processInstanceId = i.id "
                + "AND ("
                    + "t.taskData.status = 'Ready' AND a.id IN :identities OR "
                    + "t.taskData.status = 'InProgress' AND t.taskData.actualOwner.id = :userId)) OR "
            + ":taskOwner = false AND NOT EXISTS ("
                + "SELECT 1 "
                + "FROM TaskImpl t "
                + "INNER JOIN t.peopleAssignments.potentialOwners a "
                + "WHERE t.taskData.processInstanceId = i.id "
                + "AND ("
                    + "t.taskData.status = 'Ready' AND a.id IN :identities OR "
                    + "t.taskData.status = 'InProgress' AND t.taskData.actualOwner.id = :userId)))")         
    public Page<Instance> findAll(
        @Param("tenant") String tenant,
        @Param("organization") String organization,
        @Param("processId") String processId,  
        @Param("status") Set<Integer> status,
        @Param("taskOwner") Boolean taskOwner,
        @Param("userId") String userId,
        @Param("isAdmin") boolean isAdmin,
        @Param("identities") Set<String> identities,
        @Param("rootOnly") boolean rootOnly,
        @Param("activeOnly") boolean activeOnly,
        @Param("searchableOnly") boolean searchableOnly,
        @Param("unreadChatOnly") boolean unreadChatOnly,
        @Param("errorOnly") boolean errorOnly,
        Pageable pageable);   
    
    @Query(""
        + "SELECT new InstanceReference("
            + "i.id, "
            + "i.unitId, "
            + "i.tenant, "
            + "i.organization) "
        + "FROM Instance i "
        + "WHERE i.id = :id "
        + "AND i.status = 1")    
    public InstanceReference findActiveReferenceById(
        @Param("id") Long id);
    
    @Query(""
        + "SELECT new InstanceReference("
            + "i.id, "
            + "i.unitId, "
            + "i.tenant, "
            + "i.organization) "
        + "FROM Instance i "
        + "WHERE i.processUnitId = :processUnitId "
        + "AND i.status = 1")    
    public List<InstanceReference> findActiveReferencesByProcessUnitId(
        @Param("processUnitId") String processUnitId);

    @Query(""
        + "SELECT new InstanceReference("
            + "i.id, "
            + "i.unitId, "
            + "i.tenant, "
            + "i.organization) "
        + "FROM Instance i "
        + "WHERE i.configuration = :configuration "
        + "AND i.status = 1")    
    public List<InstanceReference> findActiveReferencesByConfiguration(
        @Param("configuration") Configuration configuration); 
    
    @Query(""
        + "SELECT i.id "
        + "FROM Instance i "
        + "WHERE i.archiveTs < :ts "
        + "AND i.status <> 1")    
    public List<Long> findArchiviables(
        @Param("ts") Date ts,
        Pageable pageable); 

    @Transactional
    @Modifying
    @Query(""
        + "DELETE FROM Instance i "
        + "WHERE i = :instance")    
    public void deleteInstance(
        @Param("instance") Instance instance);   

    @Transactional
    @Modifying
    @Query(""
        + "DELETE FROM InstanceIO i "
        + "WHERE i.instance = :instance")    
    public void deleteInstanceIO(
        @Param("instance") Instance instance);   
    
    @Transactional
    @Modifying
    @Query(""
        + "DELETE FROM ProcessInstanceLog i "
        + "WHERE i.id = :instanceId")    
    public void deleteInstanceLog(
        @Param("instanceId") Long instanceId);    
}
