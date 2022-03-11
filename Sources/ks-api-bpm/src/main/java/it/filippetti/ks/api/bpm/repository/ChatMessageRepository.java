/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.repository;

import it.filippetti.ks.api.bpm.model.Chat;
import it.filippetti.ks.api.bpm.model.ChatMessage;
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
public interface ChatMessageRepository extends BaseRepository<ChatMessage, Long> {

    @Query(""
        + "SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END "
        + "FROM ChatMessage m "
        + "WHERE m.uuid = :uuid")
    public boolean existsByUuid(
        @Param("uuid") String uuid); 
    
    @Query(""
        + "SELECT m "
        + "FROM ChatMessage m "
        + "WHERE m.chat = :chat")
    public Page<ChatMessage> findByChat(
        @Param("chat") Chat chat,
        Pageable pageable); 
    
    @Transactional
    @Modifying
    @Query(""
        + "DELETE FROM ChatMessage m "
        + "WHERE m.chat.members IS EMPTY")    
    public void deleteWithNoMembers(); 
}
