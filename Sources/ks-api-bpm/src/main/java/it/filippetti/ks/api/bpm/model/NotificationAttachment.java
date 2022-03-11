/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.model;

import javax.activation.MimetypesFileTypeMap;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 *
 * @author marco.mazzocchetti
 */
@Entity
@Table(
    name = "KS_NOTIFICATION_ATTACHMENT",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"notification_id", "name"})}        
)
public class NotificationAttachment extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false)
    private Notification notification;

    @Basic
    @Column(nullable = false)
    private String name;
    
    @Basic
    @Column(nullable = false)    
    private String contentType;

    @OneToOne(mappedBy = "attachment", fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
    private NotificationAttachmentContent content;
    
    public NotificationAttachment() {
    }

    protected NotificationAttachment(Notification notification, String name, byte[] content) {
        this.notification = notification;
        this.name = name;
        this.content = new NotificationAttachmentContent(this, content);
        this.contentType = MimetypesFileTypeMap.getDefaultFileTypeMap().getContentType(name);
    }    

    @Override
    public Long getId() {
        return id;
    }

    public Notification getNotification() {
        return notification;
    }

    public String getName() {
        return name;
    }

    public String getContentType() {
        return contentType;
    }

    public NotificationAttachmentContent getContent() {
        return content;
    }
}
