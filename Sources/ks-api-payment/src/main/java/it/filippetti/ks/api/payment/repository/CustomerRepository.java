/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.payment.repository;

import it.filippetti.ks.api.payment.model.AuthenticationContext;
import it.filippetti.ks.api.payment.model.Customer;
import java.util.Optional;
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
public interface CustomerRepository extends BaseRepository<Customer, Long>{
    
    public default Page<Customer> findAllPaginated(AuthenticationContext context, Pageable pageable){
        return findAllPaginated(context.getTenant(), context.getOrganization(), pageable);
    }
    
    public Customer findByTenantAndOrganizationAndId(String tenant, String organization, Long id);
    
    @Query("SELECT c FROM Customer c WHERE c.tenant = :tenant AND c.organization = :organization")
    public Page<Customer> findAllPaginated(@Param("tenant") String tenant, @Param("organization") String organization, Pageable pageable);
    
    public Optional<Customer> findByBpmUserId(String bpmUserId);
    
}
