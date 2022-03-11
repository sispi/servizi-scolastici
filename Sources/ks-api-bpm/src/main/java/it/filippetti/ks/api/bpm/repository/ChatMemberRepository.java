/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.repository;

import it.filippetti.ks.api.bpm.model.ChatMember;
import it.filippetti.ks.api.bpm.model.Instance;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author marco.mazzocchetti
 */
public interface ChatMemberRepository extends BaseRepository<ChatMember, Long> {

    @Transactional
    @Modifying
    @Query(""
        + "DELETE FROM ChatMember m "
        + "WHERE m.instance = :instance")    
    public void deleteByInstance(
        @Param("instance") Instance instance);    
}
