/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.repository;

import it.filippetti.ks.api.bpm.model.Instance;
import it.filippetti.ks.api.bpm.model.InstanceAccess;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author marco.mazzocchetti
 */
public interface InstanceAccessRepository extends BaseRepository<InstanceAccess, Long> {
    
    @Transactional
    @Modifying
    @Query(""
        + "DELETE FROM InstanceAccess a "
        + "WHERE a.instance = :instance")    
    public void deleteByInstance(
        @Param("instance") Instance instance);      
}
