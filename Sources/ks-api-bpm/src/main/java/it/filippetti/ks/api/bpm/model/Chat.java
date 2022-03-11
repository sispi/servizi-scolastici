/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author marco.mazzocchetti
 */
@Entity
@Table(
    name = "KS_CHAT"
)
public class Chat extends Auditable {
    
    @Id
    private String id;
    
    @OneToMany(mappedBy = "chat", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<ChatMember> members;

    @OrderBy("id ASC")
    @OneToMany(mappedBy = "chat", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ChatMessage> messages;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date lastSendTs;
    
    public Chat() {
    }

    public Chat(String id) {
        this.id = id;
        this.members = new HashSet<>();
        this.messages = new ArrayList<>();
        this.lastSendTs = new Date();
    }

    @Override
    public String getId() {
        return id;
    }

    public boolean isMember(Instance instance) {
        return members
            .stream()
            .map(m -> m.getInstance())
            .anyMatch(i -> i.equals(instance));
    }

    public Set<ChatMember> getMembers() {
        return members;
    }

    public List<ChatMessage> getMessages() {
        return messages;
    }

    public Date getLastSendTs() {
        return lastSendTs;
    }
    
    public void setLastSendTs(Date lastSendTs) {
        this.lastSendTs = lastSendTs;
    }
}
