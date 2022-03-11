/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.portal.repository;

import it.filippetti.ks.api.portal.model.AuthenticationContext;
import it.filippetti.ks.api.portal.model.Service;
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
public interface ServiceRepository extends BaseRepository<Service, Long>{
    public default Page<Service> findAll(AuthenticationContext context, Pageable pageable){
        if (context.isAdmin()) {
            return findAll(context.getTenant(), context.getOrganization(), pageable);
        } else {
            return Page.<Service>empty();
        }
    }
    
    public List<Service> findAllByParentId(Long parentId);
    
    public default Service findOne(AuthenticationContext context, Long id){
        return findOne(context.getTenant(), context.getOrganization(), id);
    }
    
    // public List<Service> findAllByAooIdAndParentIsNull(Long aooId);
    public List<Service> findAllByTenantAndOrganizationAndParentIsNull(String tenant, String organization);
    
    @Query("SELECT s FROM Service s WHERE s.tenant = :tenant AND s.organization = :organization AND s.id = :id")
    public Service findOne(@Param("tenant") String tenant, @Param("organization") String organization, @Param("id") Long id);
    
    @Query("SELECT s FROM Service s WHERE s.tenant = :tenant AND s.organization = :organization")
    public Page<Service> findAll(@Param("tenant") String tenant, @Param("organization") String organization, Pageable pageable);   
}
