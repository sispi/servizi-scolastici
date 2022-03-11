/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.model;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author marco.mazzocchetti
 */
@Entity
@Table(
    name = "KS_NOTIFICATION_BODY"
)
public class NotificationBody extends Identifiable {
    
    @Id
    private Long id;
    
    @MapsId 
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id")    
    private Notification notification;
    
    @Lob
    @Basic(fetch = FetchType.EAGER)
    private String value;
    
    public NotificationBody() {
    }

    public NotificationBody(Notification notification, String value) {
        this.notification = notification;
        this.value = value;
    }

    @Override
    public Long getId() {
        return id;
    }

    public Notification getNotification() {
        return notification;
    }

    public String getValue() {
        return value;
    }
}
