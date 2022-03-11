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
import javax.persistence.UniqueConstraint;

/**
 *
 * @author marco.mazzocchetti
 */
@Entity
@Table(
    name = "KS_NOTIFICATION_RECIPIENT",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"notification_id", "userId"})
    },
    indexes = {
        @Index(columnList = "notification_id, userId, readTs")
    }        
)
@Fetchables({
    @Fetchable(property = "notification", type = Notification.class)
})
public class NotificationRecipient extends Auditable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;        

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false)
    private Notification notification;    
    
    @Basic
    @Column(nullable = false)
    private String userId;

    @Temporal(TemporalType.TIMESTAMP)
    private Date readTs;

    public NotificationRecipient() {
    }

    protected NotificationRecipient(Notification notification, String userId) {
        this.notification = notification;
        this.userId = userId;
    }

    @Override
    public Long getId() {
        return id;
    }

    public Notification getNotification() {
        return notification;
    }

    public String getUserId() {
        return userId;
    }

    public boolean isActive(AuthenticationContext context) {
        return 
            // is recipient
            userId.equals(context.getUserId()) &&    
            // not expired
            !notification.getExpireTs().before(new Date()) && 
            // not read
            readTs == null;
    }

    public boolean isRead() {
        return readTs != null;
    }

    public Date getReadTs() {
        return readTs;
    }
    
    public void markAsRead() {
        if (readTs == null) {
            readTs = new Date();
        }
    }
}
