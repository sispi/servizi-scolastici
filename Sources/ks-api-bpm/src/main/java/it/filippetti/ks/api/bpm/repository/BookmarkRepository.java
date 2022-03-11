/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.repository;

import it.filippetti.ks.api.bpm.model.AuthenticationContext;
import it.filippetti.ks.api.bpm.model.Bookmark;
import it.filippetti.ks.api.bpm.model.Instance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author marco.mazzocchetti
 */
public interface BookmarkRepository extends BaseRepository<Bookmark, Long> {
    
    public default Bookmark findById(AuthenticationContext context, Long id) {
        return findById(id, context.getUserId());
    }
    
    public default Page<Bookmark> findAll(AuthenticationContext context, Pageable pageable) {
        return findAll(context.getUserId(), pageable);
    }     
    
    @Query(""
        + "SELECT b "
        + "FROM Bookmark b "
        + "WHERE b.userId = :userId")
    public Page<Bookmark> findAll(
        @Param("userId") String userId,
        Pageable pageable);   
    
    @Query(""
        + "SELECT b "
        + "FROM Bookmark b "
        + "WHERE b.id = :id "
        + "AND b.userId = :userId")
    public Bookmark findById(
        @Param("id") Long id,
        @Param("userId") String userId);      
    
    @Transactional
    @Modifying
    @Query(""
        + "DELETE FROM Bookmark b "
        + "WHERE b.instance = :instance")    
    public void deleteByInstance(
        @Param("instance") Instance instance);      
}
