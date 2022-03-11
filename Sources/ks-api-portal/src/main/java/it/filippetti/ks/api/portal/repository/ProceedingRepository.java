/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.portal.repository;

import it.filippetti.ks.api.portal.model.Proceeding;
import java.util.List;
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
public interface ProceedingRepository extends BaseRepository<Proceeding, Long>{
    /*
    public default Page<Proceeding> findAll(AuthenticationContext context, Pageable pageable){
        if (context.isAdmin()) {
            return findAll(context.getTenant(), context.getOrganization(), pageable);
        } else {
            return Page.<Proceeding>empty();
        }
    }
    */
    public List<Proceeding> findAllByTenantAndOrganizationAndServiceId(String tenant, String organization, Long serviceId);
    
    @Query("SELECT p FROM Proceeding p WHERE p.tenant = :tenant AND p.organization = :organization")
    public Page<Proceeding> findAll(@Param("tenant") String tenant, @Param("organization") String organization, Pageable pageable);
    
    public Proceeding findByTenantAndOrganizationAndId(String tenant, String organization, Long id);
    
    public Optional<Proceeding> findByTenantAndOrganizationAndProcessId(String tenant, String organization, String processId);
}
