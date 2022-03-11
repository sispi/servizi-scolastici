/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.payment.repository;

import it.filippetti.ks.api.payment.model.AuthenticationContext;
import it.filippetti.ks.api.payment.model.Channel;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author dino
 */
@Repository
public interface ChannelRepository extends BaseRepository<Channel, Long>{
    
    public default List<Channel> findAll(AuthenticationContext context){
        return findAll(context.getTenant(), context.getOrganization());
    }
    
    public Channel findByTenantAndOrganizationAndId(String tenant, String organization, Long id);
    
    @Query("SELECT c FROM Channel c WHERE c.tenant = :tenant AND c.organization = :organization")
    public List<Channel> findAll(@Param("tenant") String tenant, @Param("organization") String organization);
    
}
