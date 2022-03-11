/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.payment.repository;

import it.filippetti.ks.api.payment.model.AuthenticationContext;
import it.filippetti.ks.api.payment.model.PaymentInstance;
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
public interface PaymentInstanceRepository  extends BaseRepository<PaymentInstance, Long>{
    
    public default Page<PaymentInstance> findAllPaginated(AuthenticationContext context, Pageable pageable){
        return findAllPaginated(context.getTenant(), context.getOrganization(), pageable);
    }
    
    public default Page<PaymentInstance> findAllByUserIdPaginated(AuthenticationContext context, Pageable pageable){
        return findAllByReferenceUserIdPaginated(context.getTenant(), context.getOrganization(), context.getUserId() ,pageable);
    }
    
    public default Page<PaymentInstance> findAllByReferencePaginated(AuthenticationContext context, Pageable pageable, String referenceInstanceId, String referenceProcessId, String referenceClientId){
        return findAllByReferencePaginated(context.getTenant(), context.getOrganization(), referenceInstanceId, referenceProcessId, referenceClientId, pageable);
    }
    
    public PaymentInstance findByTenantAndOrganizationAndId(String tenant, String organization, Long id);
    
    public PaymentInstance findByTenantAndOrganizationAndUuid(String tenant, String organization, String uuid);
    
    @Query("SELECT p FROM PaymentInstance p WHERE p.tenant = :tenant AND p.organization = :organization")
    public Page<PaymentInstance> findAllPaginated(@Param("tenant") String tenant, @Param("organization") String organization, Pageable pageable);
    
    @Query("SELECT p FROM PaymentInstance p WHERE p.tenant = :tenant AND p.organization = :organization AND p.referenceUserId = :userId")
    public Page<PaymentInstance> findAllByReferenceUserIdPaginated(@Param("tenant") String tenant, @Param("organization") String organization, @Param("userId") String userId, Pageable pageable);
    
    @Query("SELECT p FROM PaymentInstance p WHERE p.tenant = :tenant AND p.organization = :organization AND p.referenceInstanceId = :referenceInstanceId AND p.referenceProcessId = :referenceProcessId AND p.referenceClientId = :referenceClientId")
    public Page<PaymentInstance> findAllByReferencePaginated(@Param("tenant") String tenant, @Param("organization") String organization, @Param("referenceInstanceId") String referenceInstanceId, @Param("referenceProcessId") String referenceProcessId, @Param("referenceClientId") String referenceClientId, Pageable pageable);

    public List<PaymentInstance> findAllByTransactionResultAndReceiptNull(String transactionResult);
    
    public List<PaymentInstance> findAllByTransactionResult(String transactionResult);

    @Query( "SELECT "
            + "COUNT(p.id) "
            + "FROM PaymentInstance p "
            + "WHERE p.tenant = :tenant AND p.organization = :organization")
    public Integer countByTenantAndOrganization(String tenant, String organization);
    
    @Query( "SELECT "
            + "SUM(p.totalAmount) "
            + "FROM PaymentInstance p "
            + "WHERE p.tenant = :tenant AND p.organization = :organization")
    public Double sumTotalAmountByTenantAndOrganization(String tenant, String organization);
    
/*
    @Query( "SELECT "
            + "function ('date_format', max(p.processingDate), '%Y, %m') as month, "
            + "SUM(p.totalAmount) as total "
            + "FROM PaymentInstance p "
            + "WHERE p.tenant = :tenant AND p.organization = :organization "
            + "AND p.processingDate BETWEEN :startDate AND :endDate "
            + "GROUP BY function('date_format', p.processingDate, '%Y, %m')")
        */    
    @Query( "SELECT "
            + "MONTH(p.processingDate) as month, "
            + "SUM(p.totalAmount) "
            + "FROM PaymentInstance p "
            + "WHERE p.tenant = :tenant AND p.organization = :organization "
            + "AND p.processingDate BETWEEN :startDate AND :endDate "
            + "GROUP BY MONTH(p.processingDate)")
    // List<Map<String,Object>>
    List<Object[]> getTotalsByMonth(@Param("tenant") String tenant, @Param("organization") String organization, @Param("startDate") Date startDate, @Param("endDate") Date endDate);
    
}
