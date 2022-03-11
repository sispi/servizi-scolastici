/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.spa.form.repository;

import it.filippetti.ks.spa.form.model.AuthenticationContext;
import it.filippetti.ks.spa.form.model.Form;
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
public interface FormRepository extends BaseRepository<Form, Form.Id> {

    public default Form findById(
        AuthenticationContext context, 
        String id) {
        return findById(new Form.Id(context.getTenant(), context.getOrganization(), id)).orElse(null);
    }

    public default Page<Form> findAll(
        AuthenticationContext context, 
        String name, 
        Pageable pageable) {
        return findAll(
            context.getTenant(),
            context.getOrganization(),
            name == null ? "" : name.replaceAll("%", ""),
            pageable);
    }

    public default Page<Form> findByName(
        AuthenticationContext context, 
        String name, 
        Pageable pageable) {
        return findByName(
            context.getTenant(),
            context.getOrganization(),
            name == null ? "" : name,
            pageable);
    }

    public default boolean existsByName(
        AuthenticationContext context,
        String name, 
        String excludeId) {
        return existsByName(context.getTenant(), 
            context.getOrganization(), 
            name, 
            new Form.Id(context.getTenant(), context.getOrganization(), excludeId));
    }
    
    @Query(""
        + "SELECT f "
        + "FROM Form f "
        + "WHERE f.tenant = :tenant "
        + "AND f.organization = :organization "
        + "AND f.name = :name")
    public Page<Form> findByName(
        @Param("tenant") String tenant,
        @Param("organization") String organization,
        @Param("name") String name,
        Pageable pageable);  
    
    @Query(""
        + "SELECT f "
        + "FROM Form f "
        + "WHERE f.tenant = :tenant "
        + "AND f.organization = :organization "
        + "AND LOWER(f.name) LIKE LOWER(CONCAT(:name, '%'))")
    public Page<Form> findAll(
        @Param("tenant") String tenant,
        @Param("organization") String organization,
        @Param("name") String name,
        Pageable pageable);   
    
    @Query(""
        + "SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END "
        + "FROM Form f "
        + "WHERE f.tenant = :tenant "         
        + "AND f.organization = :organization "
        + "AND f.name = :name "
        + "AND (:excludeId IS NULL OR f.id != :excludeId)")
    public boolean existsByName(
        @Param("tenant") String tenant,
        @Param("organization") String organization,             
        @Param("name") String name, 
        @Param("excludeId") Form.Id excludeId); 
}
