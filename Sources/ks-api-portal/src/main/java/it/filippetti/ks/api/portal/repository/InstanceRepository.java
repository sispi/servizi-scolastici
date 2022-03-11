/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.portal.repository;

import it.filippetti.ks.api.portal.model.Instance;
import java.util.Date;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author dino
 */
@Repository
public interface InstanceRepository  extends BaseRepository<Instance, Long>{
    
    public Instance findByTenantAndOrganizationAndId(String tenant, String organization, Long id);
    /*
    public default Page<Instance> findAll(AuthenticationContext context, Pageable pageable, Boolean sent){
        if (context.isAdmin()) {
            return findAll(context.getTenant(), context.getOrganization(), pageable, context.getUserId(), sent);
        } else {
            return Page.<Instance>empty();
        }
    }
    */
    public Instance findByTenantAndOrganizationAndBpmInstanceId(String tenant, String organization, Long bpmInstanceId);
    
    @Query("SELECT i FROM Instance i WHERE i.tenant = :tenant AND i.organization = :organization AND i.userId = :userId AND i.sent = :sent")
    public Page<Instance> findAll(@Param("tenant") String tenant, @Param("organization") String organization, Pageable pageable, @Param("userId") String userId, @Param("sent") Boolean sent);
    
    public List<Instance> findAllByTenantAndOrganizationAndUserIdAndSent(String tenant, String organization, String userId, Boolean sent);
    
    
    public Long countByTenantAndOrganizationAndUserIdAndProceedingId(String tenant, String organization, String userId, Long proceedingId);
    
    public List<Instance> findByTenantAndOrganizationAndUserIdAndProceedingId(String tenant, String organization, String userId, Long proceedingId);
    
    // Count draft instance
    public default Long countDrafts(String tenant, String organization, String userId, Long proceedingId){
        return countByTenantAndOrganizationAndUserIdAndProceedingIdAndBpmInstanceIdIsNullAndDispatchDateIsNull(tenant, organization, userId, proceedingId);
    }
    public Long countByTenantAndOrganizationAndUserIdAndProceedingIdAndBpmInstanceIdIsNullAndDispatchDateIsNull(String tenant, String organization, String userId, Long proceedingId);
    
    // Return draft instance
    public default List<Instance> findDrafts(String tenant, String organization, String userId, Long proceedingId){
        return findByTenantAndOrganizationAndUserIdAndProceedingIdAndBpmInstanceIdIsNullAndDispatchDateIsNull(tenant, organization, userId, proceedingId);
    }
    public List<Instance> findByTenantAndOrganizationAndUserIdAndProceedingIdAndBpmInstanceIdIsNullAndDispatchDateIsNull(String tenant, String organization, String userId, Long proceedingId);
    
    
    public List<Instance> findAllByTenantAndOrganizationAndUserIdAndSentAndProceedingId(String tenant, String organization, String userId, Boolean sent, Long proceedingId);
    
    public List<Instance> findAllByTenantAndOrganizationAndUserIdAndSentAndProceedingIdAndBpmInstanceIdIsNotNull(String tenant, String organization, String userId, Boolean sent, Long proceedingId);
    
     @Query( "SELECT "
            + "COUNT(i.id) "
            + "FROM Instance i "
            + "WHERE i.tenant = :tenant AND i.organization = :organization ")
    public Integer countByTenantAndOrganization(String tenant, String organization);
    
    @Query( "SELECT "
            + "MONTH(i.creationDate) as month, "
            + "COUNT(i.id) "
            + "FROM Instance i "
            + "WHERE i.tenant = :tenant AND i.organization = :organization "
            + "AND i.creationDate BETWEEN :startDate AND :endDate "
            + "GROUP BY MONTH(i.creationDate)")
    public List<Object[]> getTotalsByMonth(@Param("tenant") String tenant, @Param("organization") String organization, @Param("startDate") Date startDate, @Param("endDate") Date endDate);
}
