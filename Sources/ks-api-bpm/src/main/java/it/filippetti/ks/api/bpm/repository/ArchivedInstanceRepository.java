/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.repository;

import it.filippetti.ks.api.bpm.model.ArchivedInstance;
import org.springframework.stereotype.Repository;

/**
 *
 * @author marco.mazzocchetti
 */
@Repository
public interface ArchivedInstanceRepository extends BaseRepository<ArchivedInstance, Long> {
    
}
