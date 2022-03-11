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
import javax.persistence.Table;

/**
 *
 * @author dino
 */
@Entity
@Table(name = "CUSTOMER")
public class Customer extends Auditable{
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)    
    private Long id;
    
    @Basic
    @Column(nullable = false)
    private String customerType;
    
    @Basic
    @Column(nullable = false)
    private String firstName;
    
    @Basic
    @Column(nullable = false)
    private String lastName;
    
    @Basic
    @Column(nullable = true)
    private String address;
    
    @Basic
    @Column(nullable = true)
    private String city;
    
    @Basic
    @Column(nullable = true)
    private String zip;
    
    @Basic
    @Column(nullable = true)
    private String stateCode;
    
    @Basic
    @Column(nullable = true)
    private String countryCode;
    
    @Basic
    @Column(nullable = true)
    private String email;
    
    @Basic
    @Column(nullable = true)
    private String phone;
    
    @Basic
    @Column(nullable = true)
    private String operatorStaff;
    
    @Basic
    @Column(nullable = false)
    private String tenant;
    
    @Basic
    @Column(nullable = false)
    private String organization;
    
    @Basic
    @Column(nullable = false)
    private String fiscalCode;
    
    @Basic
    @Column(nullable = false)
    private String langKey;
    
    @Basic
    @Column(nullable = false)
    private String bpmUserId;

    public Customer() {
    }

    public Customer(
            String customerType,
            String firstName,
            String lastName,
            String address,
            String city,
            String zip,
            String stateCode,
            String countryCode,
            String email,
            String phone,
            String operatorStaff,
            String tenant,
            String organization,
            String fiscalCode,
            String langKey,
            String bpmUserId
    ) {
        this.customerType = customerType;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.city = city;
        this.zip = zip;
        this.stateCode = stateCode;
        this.countryCode = countryCode;
        this.email = email;
        this.phone = phone;
        this.operatorStaff = operatorStaff;
        this.tenant = tenant;
        this.organization = organization;
        this.fiscalCode = fiscalCode;
        this.langKey = langKey;
        this.bpmUserId = bpmUserId;
    }

    public Customer(
            Long id,
            String customerType,
            String firstName,
            String lastName,
            String address,
            String city,
            String zip,
            String stateCode,
            String countryCode,
            String email,
            String phone,
            String operatorStaff,
            String tenant,
            String organization,
            String fiscalCode,
            String langKey,
            String bpmUserId
    ) {
        this.id = id;
        this.customerType = customerType;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.city = city;
        this.zip = zip;
        this.stateCode = stateCode;
        this.countryCode = countryCode;
        this.email = email;
        this.phone = phone;
        this.operatorStaff = operatorStaff;
        this.tenant = tenant;
        this.organization = organization;
        this.fiscalCode = fiscalCode;
        this.langKey = langKey;
        this.bpmUserId = bpmUserId;
    }

    @Override
    public Long getId() {
        return id;
    }

    public String getBpmUserId() {
        return bpmUserId;
    }

    public void setBpmUserId(String bpmUserId) {
        this.bpmUserId = bpmUserId;
    }

    public String getLangKey() {
        return langKey;
    }

    public void setLangKey(String langKey) {
        this.langKey = langKey;
    }

    public String getTenant() {
        return tenant;
    }

    public void setTenant(String tenant) {
        this.tenant = tenant;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getCustomerType() {
        return this.customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZip() {
        return this.zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getStateCode() {
        return this.stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getCountryCode() {
        return this.countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOperatorStaff() {
        return this.operatorStaff;
    }

    public void setOperatorStaff(String operatorStaff) {
        this.operatorStaff = operatorStaff;
    }

    public String getFiscalCode() {
        return fiscalCode;
    }

    public void setFiscalCode(String fiscalCode) {
        this.fiscalCode = fiscalCode;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Customer)) {
            return false;
        }
        Customer customer = (Customer) o;
        return Objects.equals(id, customer.id) && Objects.equals(customerType, customer.customerType) && Objects.equals(firstName, customer.firstName) && Objects.equals(lastName, customer.lastName) && Objects.equals(address, customer.address) && Objects.equals(city, customer.city) && Objects.equals(zip, customer.zip) && Objects.equals(stateCode, customer.stateCode) && Objects.equals(countryCode, customer.countryCode) && Objects.equals(email, customer.email) && Objects.equals(phone, customer.phone) && Objects.equals(operatorStaff, customer.operatorStaff);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, customerType, firstName, lastName, address, city, zip, stateCode, countryCode, email, phone, operatorStaff);
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", customerType='" + getCustomerType() + "'" +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", fiscalCode='" + getFiscalCode() + "'" +
            ", langKey='" + getLangKey() + "'" +
            ", address='" + getAddress() + "'" +
            ", city='" + getCity() + "'" +
            ", zip='" + getZip() + "'" +
            ", stateCode='" + getStateCode() + "'" +
            ", countryCode='" + getCountryCode() + "'" +
            ", email='" + getEmail() + "'" +
            ", phone='" + getPhone() + "'" +
            ", operatorStaff='" + getOperatorStaff() + "'" +
            "}";
    }

    
}
