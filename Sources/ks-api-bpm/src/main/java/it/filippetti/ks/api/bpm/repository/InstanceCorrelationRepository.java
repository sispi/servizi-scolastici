/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.repository;

import it.filippetti.ks.api.bpm.model.Correlation;
import it.filippetti.ks.api.bpm.model.CorrelationKey;
import it.filippetti.ks.api.bpm.model.CorrelationType;
import it.filippetti.ks.api.bpm.model.EventScope;
import it.filippetti.ks.api.bpm.model.Instance;
import it.filippetti.ks.api.bpm.model.InstanceCorrelation;
import it.filippetti.ks.api.bpm.model.InstanceReference;
import java.util.List;
import javax.persistence.NonUniqueResultException;
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
public interface InstanceCorrelationRepository extends BaseRepository<InstanceCorrelation, Long> {

    /**
     * 
     * @param instance
     * @return
     * @throws NonUniqueResultException 
     */
    public default InstanceCorrelation findContextCorrelation(Instance instance) 
        throws NonUniqueResultException {
        
        List<InstanceCorrelation> correlations = findInstanceCorrelations(
            instance,
            CorrelationType.context);
        if (correlations.isEmpty()) {
            return null;
        }
        if (correlations.size() > 1) {
            throw new NonUniqueResultException();
        }
        return correlations.get(0);
    }

    /**
     * 
     * @param instance
     * @return 
     */
    public default List<InstanceCorrelation> findConversationCorrelations(Instance instance) {
        
        return findInstanceCorrelations(
            instance,
            CorrelationType.conversation);
    }

    /**
     * 
     * @param tenant
     * @param organization
     * @param messageId
     * @param correlationType
     * @param correlationKey
     * @param scope
     * @return
     * @throws NonUniqueResultException 
     */
    public default Correlation findCorrelation(
        String tenant,
        String organization,    
        String messageId, 
        CorrelationType correlationType,
        CorrelationKey correlationKey, 
        EventScope scope) 
        throws NonUniqueResultException {
        
        List<Correlation> correlations = findCorrelations(
            tenant,
            organization,
            messageId,
            correlationType,
            correlationKey.getHash(),
            scope.getType().ordinal(),
            scope.getId());
        if (correlations.isEmpty()) {
            return null;
        }
        if (correlations.size() > 1) {
            throw new NonUniqueResultException();
        }
        return correlations.get(0);
    }
    
    /**
     * 
     * @param tenant
     * @param organization
     * @param messageId
     * @param correlationType
     * @param correlationKey
     * @param scope
     * @return 
     */
    public default List<InstanceReference> findCorrelatedInstances(
        String tenant,
        String organization,
        String messageId, 
        CorrelationType correlationType,
        CorrelationKey correlationKey, 
        EventScope scope) {
        return  findCorrelatedInstances(
            tenant,
            organization,
            correlationType,
            correlationKey.getHash(),
            correlationType == CorrelationType.conversation ? 
                messageId : 
                InstanceCorrelation.ANY_MESSAGE_ID,
            scope.getType().ordinal(),
            scope.getId());
    }
    
    @Query(""
        + "SELECT c "
        + "FROM InstanceCorrelation c "
        + "WHERE c.instance = :instance "
        + "AND c.correlationType = :correlationType")        
    public List<InstanceCorrelation> findInstanceCorrelations(
        @Param("instance") Instance instance,    
        @Param("correlationType") CorrelationType correlationType);  

    @Query(""
        + "SELECT new Correlation(i.id, n.id, i.unitId, n.workItemId) "
        + "FROM InstanceNode n "
        + "INNER JOIN n.instance i "
        + "WHERE n.event.correlable = true "
        + "AND n.event.id = :messageId "
        + "AND n.active = true "
        + "AND i.tenant = :tenant "
        + "AND i.organization = :organization "
        + "AND ("
            + ":scopeType = 0 OR "
            + ":scopeType = 1 AND i.id = :scopeId) "
        + "AND EXISTS ("
            + "SELECT 1 "
            + "FROM InstanceCorrelation c "
            + "WHERE c.instance = i "            
            + "AND c.correlationType = :correlationType " 
            + "AND c.correlationKey.hash = :correlationKeyHash)")
    public List<Correlation> findCorrelations(
        @Param("tenant") String tenant,
        @Param("organization") String organization,
        @Param("messageId") String messageId, 
        @Param("correlationType") CorrelationType correlationType, 
        @Param("correlationKeyHash") String correlationKeyHash, 
        @Param("scopeType") int scopeType, 
        @Param("scopeId") Object scopeId);
    
    @Query(""
        + "SELECT new InstanceReference("
            + "i.id, "
            + "i.unitId, "
            + "i.tenant, "
            + "i.organization) "
        + "FROM Instance i "
        + "WHERE i.tenant = :tenant "
        + "AND i.organization = :organization "
        + "AND ("
            + ":scopeType = 0 OR "
            + ":scopeType = 1 AND i.id = :scopeId) "
        + "AND EXISTS ("
            + "SELECT 1 "
            + "FROM InstanceCorrelation c "
            + "WHERE c.instance = i "
            + "AND c.correlationType = :correlationType "
            + "AND c.correlationKey.hash = :correlationKeyHash " 
            + "AND c.messageId = :messageId)")  
    public List<InstanceReference> findCorrelatedInstances(
        @Param("tenant") String tenant,
        @Param("organization") String organization,
        @Param("correlationType") CorrelationType correlationType, 
        @Param("correlationKeyHash") String correlationKeyHash, 
        @Param("messageId") String messageId, 
        @Param("scopeType") int scopeType, 
        @Param("scopeId") Object scopeId);    
    
    @Transactional
    @Modifying
    @Query(""
        + "DELETE FROM InstanceCorrelation c "
        + "WHERE c.instance = :instance")    
    public void deleteByInstance(
        @Param("instance") Instance instance);        
}
