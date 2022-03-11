/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.payment.repository;

import it.filippetti.ks.api.payment.model.AuthenticationContext;
import it.filippetti.ks.api.payment.model.Example;
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
public interface ExampleRepository extends BaseRepository<Example, Long> {

    public default Example findById(AuthenticationContext context, Long id) {
        if (context.isAdmin()) {
            return findById(context.getTenant(), context.getOrganization(), id);
        } else {
            return null;
        }
    }

    public default Page<Example> findAll(AuthenticationContext context, Pageable pageable) {
        if (context.isAdmin()) {
            return findAll(
                context.getTenant(),
                context.getOrganization(),
                pageable);
        } else {
            return Page.<Example>empty();
        }

    }

    @Query(""
        + "SELECT e "
        + "FROM Example e "
        + "WHERE  e.id = :id "
        + "AND e.tenant = :tenant "         
        + "AND e.organization = :organization")
    public Example findById(
        @Param("tenant") String tenant,
        @Param("organization") String organization,             
        @Param("id") Long id);   

    @Query(""
        + "SELECT e "
        + "FROM Example e "
        + "WHERE e.tenant = :tenant "
        + "AND e.organization = :organization")
    public Page<Example> findAll(
        @Param("tenant") String tenant,
        @Param("organization") String organization,
        Pageable pageable);   
}
