/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.repository;

import it.filippetti.ks.api.bpm.model.AuthenticationContext;
import it.filippetti.ks.api.bpm.model.Event;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
public interface EventRepository extends BaseRepository<Event, Long> {

    public default Event findByEventId(AuthenticationContext context, String eventId) {
        return findByEventId(context.getTenant(), context.getOrganization(), eventId);
    }

    public default List<Event> findByEventId(AuthenticationContext context, Set<String> eventId) {
        return findByEventId(context.getTenant(), context.getOrganization(), eventId);
    }

    public default Page<Event> findAll(AuthenticationContext context, Pageable pageable) {
        return findAll(context.getTenant(), context.getOrganization(), pageable);
    }
      
    @Query(value = ""
        + "SELECT DISTINCT e "
        + "FROM Event e "
        + "INNER JOIN e.deployments j "            
        + "INNER JOIN j.deployment d "                        
        + "WHERE d.tenant = :tenant "
        + "AND (d.shared = true OR d.organization = :organization)",
    countQuery = ""
        + "SELECT COUNT(DISTINCT id) "
        + "FROM Event e "
        + "INNER JOIN e.deployments j "            
        + "INNER JOIN j.deployment d "                        
        + "WHERE d.tenant = :tenant "
        + "AND (d.shared = true OR d.organization = :organization)")
    public Page<Event> findAll(
        @Param("tenant") String tenant,
        @Param("organization") String organization,             
        Pageable pageable);   

    @Query(value = ""
        + "SELECT DISTINCT e "
        + "FROM Event e "
        + "INNER JOIN e.deployments j "            
        + "INNER JOIN j.deployment d "                        
        + "WHERE  e.eventId IN :eventId "
        + "AND d.tenant = :tenant "
        + "AND (d.shared = true OR d.organization = :organization)")
    public List<Event> findByEventId(
        @Param("tenant") String tenant,
        @Param("organization") String organization,   
        @Param("eventId") Set<String> eventId);   

    @Query(value = ""
        + "SELECT DISTINCT e "
        + "FROM Event e "
        + "INNER JOIN e.deployments j "            
        + "INNER JOIN j.deployment d "                        
        + "WHERE  e.eventId = :eventId "
        + "AND d.tenant = :tenant "
        + "AND (d.shared = true OR d.organization = :organization)")
    public Event findByEventId(
        @Param("tenant") String tenant,
        @Param("organization") String organization,   
        @Param("eventId") String eventId);   
    
    @Transactional
    @Modifying
    @Query(value = ""
        + "DELETE FROM Event e WHERE e.deployments IS EMPTY")
    public int deleteOrphans();      
}
