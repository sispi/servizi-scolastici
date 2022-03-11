/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.model;

import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author marco.mazzocchetti
 */
@Entity
@Table(
    name = "KS_CHAT_MESSAGE",
    indexes = {
        @Index(columnList = "chat_id, sendTs")
    }        
)
@Sortables(
    defaultSort = "sendTs:ASC",
    value = {
        @Sortable(property = "sendTs", pathExpression = "id")
    }
)
@Fetchables({
    @Fetchable(property = "text")
})
public class ChatMessage extends Auditable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)    
    private Long id;
    
    @Basic
    @Column(nullable = false, unique = true)
    private String uuid;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false)
    private Chat chat; 

    @Basic
    @Column(nullable = false)
    private String senderInstanceName;    

    @Basic
    @Column(nullable = false)
    private String senderUserId;  

    @Basic
    @Column(nullable = false)
    private String senderDisplayName;  

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date sendTs;    

    @Basic
    @Column(nullable = false, length = 1024)
    private String text;    

    public ChatMessage() {
    }

    public ChatMessage(
        String uuid,
        Chat chat, 
        String senderInstanceName, 
        String senderUserId, 
        String senderDisplayName,
        String text) {
        this.uuid = uuid;
        this.chat = chat;
        this.senderInstanceName = senderInstanceName;
        this.senderUserId = senderUserId;
        this.senderDisplayName = senderDisplayName;
        this.sendTs = new Date();
        this.text = StringUtils.abbreviate(text, 1024);
    }
    
    @Override
    public Long getId() {
        return id;
    }

    public String getUuid() {
        return uuid;
    }

    public Chat getChat() {
        return chat;
    }

    public String getSenderInstanceName() {
        return senderInstanceName;
    }

    public String getSenderUserId() {
        return senderUserId;
    }

    public String getSenderDisplayName() {
        return senderDisplayName;
    }

    public Date getSendTs() {
        return sendTs;
    }
    
    public String getText() {
        return text;
    }    
    
    public boolean isSender(AuthenticationContext context) {
        return context.getUserId().equals(senderUserId);
    }
}
