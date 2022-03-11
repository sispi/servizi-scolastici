/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.portal.repository;

import it.filippetti.ks.api.portal.model.OperationResult;
import java.util.Date;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author marco.mazzocchetti
 */
public interface OperationResultRepository extends BaseRepository<OperationResult, String> {
    
    @Transactional
    @Modifying
    @Query(""
        + "DELETE FROM OperationResult o "
        + "WHERE o.lastModifiedTs < :ts")    
    public int clean(
        @Param("ts") Date ts);    
}
