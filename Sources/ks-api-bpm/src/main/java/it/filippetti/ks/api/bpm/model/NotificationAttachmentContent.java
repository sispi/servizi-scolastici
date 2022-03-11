/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.model;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import javax.persistence.Basic;
import javax.persistence.Column;
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
    name = "KS_NOTIFICATION_ATTACHMENT_CONTENT"
)
public class NotificationAttachmentContent extends Auditable {
    
    @Id 
    private Long id;    

    @MapsId 
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id")
    private NotificationAttachment attachment;
    
    @Lob
    @Basic(fetch = FetchType.EAGER)
    @Column(nullable = false)
    private byte[] data;

    public NotificationAttachmentContent() {
    }

    protected NotificationAttachmentContent(NotificationAttachment attachment, byte[] data) {
        this.attachment = attachment;
        this.data = data;
    }
    
    @Override
    public Long getId() {
        return id;
    }

    public NotificationAttachment getAttachment() {
        return attachment;
    }
    
    public byte[] asBytes() {
        return Arrays.copyOf(data, data.length);
    }  
    
    public InputStream asInputStream() {
        return new ByteArrayInputStream(data);
    }
    
    public void write(OutputStream outputStream) throws IOException {
        outputStream.write(data);
    }    
}
