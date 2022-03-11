/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.repository;

import it.filippetti.ks.api.bpm.model.DeploymentStatus;
import java.util.Date;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author marco.mazzocchetti
 */
@Repository
public interface DeploymentStatusRepository extends BaseRepository<DeploymentStatus, String> {

    public default DeploymentStatus findByUnitId(String unitId) {
        return findById(unitId).orElse(null);
    }

    public default List<DeploymentStatus> findUpdates(Date ts, int fetchSize) {
        return findUpdates(ts, PageRequest.of(0, fetchSize));
    }
    
    @Query(""
        + "SELECT s "
        + "FROM DeploymentStatus s "
        + "WHERE s.updateTs > :ts "
        + "ORDER BY s.updateTs ASC")
    public List<DeploymentStatus> findUpdates(
        @Param("ts") Date ts,
        Pageable pageable);
}
