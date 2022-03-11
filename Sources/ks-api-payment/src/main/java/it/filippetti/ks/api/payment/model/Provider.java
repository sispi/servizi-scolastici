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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 *
 * @author dino
 */
@Entity
@Table(name = "PROVIDER")
public class Provider extends Auditable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)    
    private Long id;

    @Basic
    @Column(nullable = false)
    private String name;

    @Basic
    @Lob
    @Size(max = 1000000)
    @Column(nullable = true)
    private byte[] logo;
    
    @Basic
    @Column(nullable = true)
    private String packageClass;
    
    @Basic
    @Column(nullable = true)
    private String serviceUser;

    @Basic
    @Column(nullable = true)
    private String serviceUrl;
    
    @Basic
    @Column(nullable = true)
    private String wsdlUrl;

    public Provider() {
    }

    public Provider(String name, byte[] logo, String packageClass, String serviceUser, String serviceUrl, String wsdlUrl) {
        this.name = name;
        this.logo = logo;
        this.packageClass = packageClass;
        this.serviceUser = serviceUser;
        this.serviceUrl = serviceUrl;
        this.wsdlUrl = wsdlUrl;
    }

    public Provider(Long id, String name, byte[] logo, String packageClass, String serviceUser, String serviceUrl, String wsdlUrl) {
        this.id = id;
        this.name = name;
        this.logo = logo;
        this.packageClass = packageClass;
        this.serviceUser = serviceUser;
        this.serviceUrl = serviceUrl;
        this.wsdlUrl = wsdlUrl;
    }

    @Override
    public Long getId() {
        return id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getLogo() {
        return this.logo;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    public String getPackageClass() {
        return this.packageClass;
    }

    public void setPackageClass(String packageClass) {
        this.packageClass = packageClass;
    }

    public String getServiceUser() {
        return this.serviceUser;
    }

    public void setServiceUser(String serviceUser) {
        this.serviceUser = serviceUser;
    }

    public String getServiceUrl() {
        return this.serviceUrl;
    }

    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    public String getWsdlUrl() {
        return this.wsdlUrl;
    }

    public void setWsdlUrl(String wsdlUrl) {
        this.wsdlUrl = wsdlUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Provider)) {
            return false;
        }
        Provider provider = (Provider) o;
        return Objects.equals(id, provider.id) && Objects.equals(name, provider.name) && Objects.equals(logo, provider.logo) && Objects.equals(packageClass, provider.packageClass) && Objects.equals(serviceUser, provider.serviceUser) && Objects.equals(serviceUrl, provider.serviceUrl) && Objects.equals(wsdlUrl, provider.wsdlUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, logo, packageClass, serviceUser, serviceUrl, wsdlUrl);
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", name='" + getName() + "'" +
            ", logo='" + getLogo() + "'" +
            ", packageClass='" + getPackageClass() + "'" +
            ", serviceUser='" + getServiceUser() + "'" +
            ", serviceUrl='" + getServiceUrl() + "'" +
            ", wsdlUrl='" + getWsdlUrl() + "'" +
            "}";
    }
   
}
