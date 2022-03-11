/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.repository;

import it.filippetti.ks.api.bpm.model.Chat;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author marco.mazzocchetti
 */
public interface ChatRepository extends BaseRepository<Chat, String> {

    @Transactional
    @Modifying
    @Query(""
        + "DELETE FROM Chat c "
        + "WHERE c.members IS EMPTY")    
    public void deleteWithNoMembers(); 
}
