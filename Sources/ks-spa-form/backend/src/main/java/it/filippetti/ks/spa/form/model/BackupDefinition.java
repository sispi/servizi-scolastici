/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.spa.form.model;

import com.fasterxml.jackson.databind.node.ObjectNode;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author marco.mazzocchetti
 */
@Entity
@Table(
    name = "KS_BACKUP_DEFINITION"
)
public class BackupDefinition extends Identifiable {

    @Id
    private Long id;
    
    @MapsId 
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id")    
    private Backup backup;

    @Embedded
    private JsonLob value;

    public BackupDefinition() {
    }

    protected BackupDefinition(Backup backup, ObjectNode value) {
        this.backup = backup;
        this.value = new JsonLob(value);
    }

    @Override
    public Long getId() {
        return id;
    }

    public Backup getBackup() {
        return backup;
    }

    public ObjectNode getValue() {
        return (ObjectNode) value.asJsonNode();
    }
}
