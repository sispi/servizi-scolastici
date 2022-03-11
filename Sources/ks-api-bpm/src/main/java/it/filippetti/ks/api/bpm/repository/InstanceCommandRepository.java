/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.repository;

import it.filippetti.ks.api.bpm.model.Instance;
import it.filippetti.ks.api.bpm.model.InstanceCommand;
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
public interface InstanceCommandRepository extends BaseRepository<InstanceCommand, Long> {

    /**
     * 
     * @param instance 
     */
    public default void deleteByInstance(Instance instance) {
        deleteCommandErrorByInstance(instance);
        deleteCommandByInstance(instance);
    }
 
    @Transactional
    @Modifying
    @Query(""
        + "DELETE FROM InstanceCommand c "
        + "WHERE c.instance = :instance")    
    public void deleteCommandByInstance(
        @Param("instance") Instance instance);

    @Transactional
    @Modifying
    @Query(""
        + "DELETE FROM InstanceCommandError c "
        + "WHERE EXISTS ("
            + "SELECT 1 "
            + "FROM InstanceCommand j "
            + "WHERE j = c.command "
            + "AND j.instance = :instance)")    
    public void deleteCommandErrorByInstance(
        @Param("instance") Instance instance);    
}
