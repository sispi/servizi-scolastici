/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.repository;

import it.filippetti.ks.api.bpm.model.Instance;
import it.filippetti.ks.api.bpm.model.InstanceNode;
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
public interface InstanceNodeRepository extends BaseRepository<InstanceNode, Long> {

    
    @Transactional
    public default void deleteByInstance(Instance instance) {
        deleteNodeLogByInstance(instance.getId());
        deleteNodeIOByInstance(instance);
        deleteNodeByInstance(instance);
    }

    @Query(""
        + "SELECT n "
        + "FROM InstanceNode n "
        + "WHERE n.instance = :instance "
        + "AND n.nodeInstanceId = :nodeInstanceId")        
    public InstanceNode findById(
        @Param("instance") Instance instance,    
        @Param("nodeInstanceId") Long nodeInstanceId);

    @Query(""
        + "SELECT n "
        + "FROM InstanceNode n "
        + "WHERE n.instance = :instance "
        + "AND n.active = CASE "
            + "WHEN :activeOnly = true "
            + "THEN true "
            + "ELSE n.active END")        
    public Page<InstanceNode> findByInstance(
        @Param("instance") Instance instance,
        @Param("activeOnly") boolean activeOnly, 
        Pageable pageable);
    
    @Transactional
    @Modifying
    @Query(""
        + "DELETE FROM InstanceNode n "
        + "WHERE n.instance = :instance")    
    public void deleteNodeByInstance(
        @Param("instance") Instance instance);

    @Transactional
    @Modifying
    @Query(""
        + "DELETE FROM InstanceNodeIO n "
        + "WHERE EXISTS ("
            + "SELECT 1 "
            + "FROM InstanceNode j "
            + "WHERE j = n.node "
            + "AND j.instance = :instance)")    
    public void deleteNodeIOByInstance(
        @Param("instance") Instance instance);

    @Transactional
    @Modifying
    @Query(""
        + "DELETE FROM NodeInstanceLog n "
        + "WHERE n.processInstanceId = :instanceId")    
    public void deleteNodeLogByInstance(
        @Param("instanceId") Long instanceId);    
}
