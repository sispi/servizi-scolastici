/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.model;

import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author marco.mazzocchetti
 */
@Entity
@Table(
    name = "KS_CHAT_MEMBER"
)
@Fetchables({
    @Fetchable(property = "members", type = ChatMember.class),
    @Fetchable(property = "messages", type = ChatMessage.class)       
})
public class ChatMember extends Auditable {

    @Id
    private Long id;    
    
    @MapsId 
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id")
    private Instance instance;    
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false)
    private Chat chat;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date lastReadTs;

    public ChatMember() {
    }

    public ChatMember(Instance instance, Chat chat) {
        this.instance = instance;
        this.chat = chat;
        this.lastReadTs = chat.getLastSendTs();
    }

    @Override
    public Long getId() {
        return id;
    }

    public Instance getInstance() {
        return instance;
    }

    public Chat getChat() {
        return chat;
    }

    public Date getLastReadTs() {
        return lastReadTs;
    }
    
    public boolean isRead() {
        return lastReadTs.after(chat.getLastSendTs());
    }
    
    public void markAsRead() {
        lastReadTs = new Date();
    }
    
    public Set<ChatMember> getMembers() {
        return chat.getMembers();
    }
    
    public List<ChatMessage> getMessages() {
        return chat.getMessages();
    }
}
