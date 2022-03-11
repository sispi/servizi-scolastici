/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author marco.mazzocchetti
 */
@Entity
@Table(
    name = "KS_NOTIFICATION_TAG",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"notification_id", "value"})
    }        
)
public class NotificationTag extends Identifiable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;        

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false)
    private Notification notification;    
    
    @Basic
    @Column(nullable = false)
    private String value;

    public NotificationTag() {
    }

    protected NotificationTag(Notification notification, String value) {
        this.notification = notification;
        this.value = StringUtils.abbreviate(value, 255);
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
