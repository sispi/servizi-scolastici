/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.payment.model;

import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author dino
 */
@Entity
@Table(name = "CHANNEL")
@Sortables(
    defaultSort = "id:ASC",
    value = {
        @Sortable(property = "id"), @Sortable(property = "name")
    }
)
public class Channel extends Auditable {
    
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)    
    private Long id;

    @Basic
    @Column(nullable = false)
    private String name;

    @Basic
    @Column(nullable = false)
    private String tenant;
    
    @Basic
    @Column(nullable = false)
    private String organization;

    @Basic
    @ManyToOne(fetch = FetchType.LAZY, optional = false, targetEntity = Provider.class)
    private Provider provider;

    public Channel() {
    }

    public Channel(String name, String tenant, String organization, Provider provider) {
        this.name = name;
        this.tenant = tenant;
        this.organization = organization;
        this.provider = provider;
    }

    public Channel(Long id, String name, String tenant, String organization, Provider provider) {
        this.id = id;
        this.name = name;
        this.tenant = tenant;
        this.organization = organization;
        this.provider = provider;
    }

    @Override
    public Long getId() {
        return id;
    }

    public String getTenant() {
        return tenant;
    }

    public void setTenant(String tenant) {
        this.tenant = tenant;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrganization() {
        return this.organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public Provider getProvider() {
        return this.provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Channel)) {
            return false;
        }
        Channel channel = (Channel) o;
        return Objects.equals(id, channel.id) && Objects.equals(name, channel.name) && Objects.equals(organization, channel.organization) && Objects.equals(provider, channel.provider);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, organization, provider);
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", name='" + getName() + "'" +
            ", organization='" + getOrganization() + "'" +
            ", provider='" + getProvider() + "'" +
            "}";
    }

    
}
