/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.repository;

import it.filippetti.ks.api.bpm.model.Instance;
import it.filippetti.ks.api.bpm.model.InstanceContext;
import it.filippetti.ks.api.bpm.model.InstanceVariable;
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
public interface InstanceVariableRepository extends BaseRepository<InstanceVariable, Long> {
    
    @Query(""
        + "SELECT v "
        + "FROM InstanceVariable v "
        + "WHERE v.instance = :instance "            
        + "AND v.context = :context "
        + "AND v.name = :name")        
    public InstanceVariable findById(
        @Param("instance") Instance instance,
        @Param("context") InstanceContext context,
        @Param("name") String name);
    
    @Query(""
        + "SELECT v "
        + "FROM InstanceVariable v "
        + "WHERE v.instance.id = :instanceId "            
        + "AND v.context = :context "
        + "AND v.name = :name")        
    public InstanceVariable findById(
        @Param("instanceId") Long instanceId,
        @Param("context") InstanceContext context,
        @Param("name") String name);
    
    /**
     * 
     * @param instance
     * @return 
     */
    @Transactional
    public default void deleteNonPublicByInstance(Instance instance) {
        deleteVariableLogByInstance(instance.getId());
        deleteNonPublicVariableValueByInstance(instance);
        deleteNonPublicVariableByInstance(instance);
    }

    /**
     * 
     * @param instance
     * @return 
     */
    @Transactional
    public default void deleteByInstance(Instance instance) {
        deleteVariableLogByInstance(instance.getId());
        deleteVariableValueByInstance(instance);
        deleteVariableByInstance(instance);
    }
    
    @Transactional
    @Modifying
    @Query(""
        + "DELETE FROM InstanceVariable v "
        + "WHERE v.instance = :instance "
        + "AND v.public_ = false")    
    public void deleteNonPublicVariableByInstance(
        @Param("instance") Instance instance);

    @Transactional
    @Modifying
    @Query(""
        + "DELETE FROM InstanceVariableValue v "
        + "WHERE EXISTS ("
            + "SELECT 1 "
            + "FROM InstanceVariable j "
            + "WHERE j = v.variable "
            + "AND j.instance = :instance "
            + "AND j.public_ = false)")    
 
    public void deleteNonPublicVariableValueByInstance(
        @Param("instance") Instance instance);

    @Transactional
    @Modifying
    @Query(""
        + "DELETE FROM InstanceVariable v "
        + "WHERE v.instance = :instance")    
    public void deleteVariableByInstance(
        @Param("instance") Instance instance);

    @Transactional
    @Modifying
    @Query(""
        + "DELETE FROM InstanceVariableValue v "
        + "WHERE EXISTS ("
            + "SELECT 1 "
            + "FROM InstanceVariable j "
            + "WHERE j = v.variable "
            + "AND j.instance = :instance)")    
    public void deleteVariableValueByInstance(
        @Param("instance") Instance instance);
    
    @Transactional
    @Modifying
    @Query(""
        + "DELETE FROM VariableInstanceLog n "
        + "WHERE n.processInstanceId = :instanceId")    
    public void deleteVariableLogByInstance(
        @Param("instanceId") Long instanceId);      
}
