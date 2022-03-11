/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.spa.form.repository;

import it.filippetti.ks.spa.form.model.AuthenticationContext;
import it.filippetti.ks.spa.form.model.Section;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author marco.mazzocchetti
 */
@Repository
public interface SectionRepository extends BaseRepository<Section, Section.Id> {
    
    public default Section findByKey(
        AuthenticationContext context, 
        String key) {
        return findByKey(
            context.getTenant(), 
            context.getOrganization(), 
            key);
    }

    public default Page<Section> findAll(
        AuthenticationContext context, 
        String key, 
        Pageable pageable) {
        return findAll(
            context.getTenant(),
            context.getOrganization(),
            key == null ? "" : key.replaceAll("%", ""),
            pageable);
    }

    public default boolean existsByKey(
        AuthenticationContext context,
        String key) {
        return existsById(new Section.Id(
            context.getTenant(),
            context.getOrganization(),
            key));
    }
    
    @Query(""
        + "SELECT s "
        + "FROM Section s "
        + "WHERE s.id.tenant = :tenant "         
        + "AND s.id.organization = :organization "
        + "AND s.id.key = :key")
    public Section findByKey(
        @Param("tenant") String tenant,
        @Param("organization") String organization,             
        @Param("key") String key);   

    @Query(""
        + "SELECT s "
        + "FROM Section s "
        + "WHERE s.id.tenant = :tenant "
        + "AND s.id.organization = :organization "
        + "AND LOWER(s.id.key) LIKE CONCAT(:key, '%')")
    public Page<Section> findAll(
        @Param("tenant") String tenant,
        @Param("organization") String organization,
        @Param("key") String key,
        Pageable pageable);     
}
