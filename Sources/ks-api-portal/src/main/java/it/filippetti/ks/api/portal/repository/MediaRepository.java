/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.portal.repository;

import it.filippetti.ks.api.portal.model.AuthenticationContext;
import it.filippetti.ks.api.portal.model.Media;
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
public interface MediaRepository extends BaseRepository<Media, Long>{
    
    public default Page<Media> findAll(AuthenticationContext context, Pageable pageable){
        if (context.isAdmin()) {
            return findAll(context.getTenant(), context.getOrganization(), pageable);
        } else {
            return Page.<Media>empty();
        }
    }
    
    @Query("SELECT m FROM Media m WHERE m.tenant = :tenant AND m.organization = :organization")
    public Page<Media> findAll(@Param("tenant") String tenant, @Param("organization") String organization, Pageable pageable);
    
    public Optional<Media> findByTenantAndOrganizationAndId(String tenant, String organization, Long id);
    
    public Optional<Media> findByTenantAndMyKey(String tenant, String myKey);
    
}
