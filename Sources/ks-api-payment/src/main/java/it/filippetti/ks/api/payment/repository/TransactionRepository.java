/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.payment.repository;

import it.filippetti.ks.api.payment.model.AuthenticationContext;
import it.filippetti.ks.api.payment.model.Transaction;
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
public interface TransactionRepository extends BaseRepository<Transaction, Long>{
    public default Page<Transaction> findAllPaginated(AuthenticationContext context, Pageable pageable){
        return findAllPaginated(context.getTenant(), context.getOrganization(), pageable);
    }
    
    public Transaction findByTenantAndOrganizationAndId(String tenant, String organization, Long id);
    
    @Query("SELECT t FROM Transaction t WHERE t.tenant = :tenant AND t.organization = :organization")
    public Page<Transaction> findAllPaginated(@Param("tenant") String tenant, @Param("organization") String organization, Pageable pageable);
    
    public List<Transaction> findAllByTenantAndOrganizationAndPaymentInstanceId(String tenant, String organization, Long paymentId);
    
    public Transaction findTop1ByTenantAndOrganizationAndPaymentInstanceIdOrderByRequestDateDesc(String tenant, String organization, Long paymentId);
    
   

}
