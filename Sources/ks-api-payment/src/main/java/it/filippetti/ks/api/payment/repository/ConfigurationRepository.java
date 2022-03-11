/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.payment.repository;

import it.filippetti.ks.api.payment.model.AuthenticationContext;
import it.filippetti.ks.api.payment.model.Channel;
import it.filippetti.ks.api.payment.model.Configuration;
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
public interface ConfigurationRepository extends BaseRepository<Configuration, Long>{
    
    public Configuration findByTenantAndOrganizationAndId(String tenant, String organization, Long id);
    
    public default Page<Configuration> findAllPaginated(AuthenticationContext context, Pageable pageable){
        return findAllPaginated(context.getTenant(), context.getOrganization(), pageable);
    }
    
    public default Page<Configuration> findAllByChannelPaginated(AuthenticationContext context, Pageable pageable, Channel channel){
        return findAllByChannelPaginated(context.getTenant(), context.getOrganization(), pageable, channel);
    }
    
    @Query("SELECT c FROM Configuration c WHERE c.tenant = :tenant AND c.organization = :organization AND c.channel = :channel")
    public Page<Configuration> findAllByChannelPaginated(@Param("tenant") String tenant, @Param("organization") String organization, Pageable pageable, @Param("channel") Channel channel);
    
    @Query("SELECT c FROM Configuration c WHERE c.tenant = :tenant AND c.organization = :organization")
    public Page<Configuration> findAllPaginated(@Param("tenant") String tenant, @Param("organization") String organization, Pageable pageable);
    
    public List<Configuration> findAllByTenantAndOrganizationAndChannelId(String tenant, String organization, Long channelId);

    // Usato in contesto cron
    public Configuration findByChannelIdAndDefaultConfTrue(Long id);

}
