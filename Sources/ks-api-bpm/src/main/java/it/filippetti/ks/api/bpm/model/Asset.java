/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.model;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author marco.mazzocchetti
 */
@Entity
@Table(
    name = "KS_ASSET"
)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@Fetchables({
    @Fetchable(property = "content")
})
public abstract class Asset extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Basic
    @Column(nullable = false)
    private String name;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)    
    private AssetType type;

    @OneToOne(mappedBy = "asset", fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
    private AssetContent content;

    public Asset() {
    }

    protected Asset(String name, byte[] content) {
        this.name = name;
        this.type = AssetType.valueFor(name);
        this.content = new AssetContent(this, content);
    }
    
    @Override
    public Long getId() {
        return id;
    }    

    public String getName() {
        return name;
    }

    public AssetType getType() {
        return type;
    }

    public AssetContent getContent() {
        return content;
    }
}
