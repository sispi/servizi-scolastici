/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.payment.model;

import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author dino
 */
@Entity
@Table(name = "TRANSACTION")
public class Transaction extends Auditable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)    
    private Long id;
    
    @Basic
    @Column(nullable = false)    
    private String tenant;
    
    @Basic
    @Column(nullable = false)
    private String organization;

    @Basic
    @Column(nullable = true)    
    private String shopCode;
    
    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = true)
    private Date requestDate;
    
    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = true)
    private Date responseDate;
    
    @Basic
    @Column(nullable = true)    
    private String paymentService;
    
    @Basic
    @Column(nullable = true)    
    private Integer rating;
    
    @Basic
    @Column(nullable = false)
    private String userId;

    @Basic
    @Column(nullable = true)
    private Outcome outcome;
    
    @Basic
    @ManyToOne(fetch = FetchType.LAZY, optional = false, targetEntity = PaymentInstance.class)
    private PaymentInstance paymentInstance;

    public Transaction() {
    }

    public Transaction(Long id, Outcome outcome, String tenant, String organization, String shopCode, Date requestDate, Date responseDate, String paymentService, Integer rating, String userId, PaymentInstance paymentInstance) {
        this.id = id;
        this.outcome = outcome;
        this.tenant = tenant;
        this.organization = organization;
        this.shopCode = shopCode;
        this.requestDate = requestDate;
        this.responseDate = responseDate;
        this.paymentService = paymentService;
        this.rating = rating;
        this.userId = userId;
        this.paymentInstance = paymentInstance;
    }

    public Transaction(Outcome outcome, String tenant, String organization, String shopCode, Date requestDate, Date responseDate, String paymentService, Integer rating, String userId, PaymentInstance paymentInstance) {
        this.outcome = outcome;
        this.tenant = tenant;
        this.organization = organization;
        this.shopCode = shopCode;
        this.requestDate = requestDate;
        this.responseDate = responseDate;
        this.paymentService = paymentService;
        this.rating = rating;
        this.userId = userId;
        this.paymentInstance = paymentInstance;
    }

    @Override
    public Long getId() {
        return id;
    }
    
    public Outcome getOutcome() {
        return this.outcome;
    }

    public void setOutcome(Outcome outcome) {
        this.outcome = outcome;
    }

    public String getTenant() {
        return this.tenant;
    }

    public void setTenant(String tenant) {
        this.tenant = tenant;
    }

    public String getOrganization() {
        return this.organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getShopCode() {
        return this.shopCode;
    }

    public void setShopCode(String shopCode) {
        this.shopCode = shopCode;
    }

    public Date getRequestDate() {
        return this.requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    public Date getResponseDate() {
        return this.responseDate;
    }

    public void setResponseDate(Date responseDate) {
        this.responseDate = responseDate;
    }

    public String getPaymentService() {
        return this.paymentService;
    }

    public void setPaymentService(String paymentService) {
        this.paymentService = paymentService;
    }

    public Integer getRating() {
        return this.rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public PaymentInstance getPaymentInstance() {
        return this.paymentInstance;
    }

    public void setPaymentInstance(PaymentInstance payment) {
        this.paymentInstance = paymentInstance;
    }

    public Transaction outcome(Outcome outcome) {
        setOutcome(outcome);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Transaction)) {
            return false;
        }
        Transaction transaction = (Transaction) o;
        return Objects.equals(outcome, transaction.outcome) && Objects.equals(id, transaction.id) && Objects.equals(tenant, transaction.tenant) && Objects.equals(organization, transaction.organization) && Objects.equals(shopCode, transaction.shopCode) && Objects.equals(requestDate, transaction.requestDate) && Objects.equals(responseDate, transaction.responseDate) && Objects.equals(paymentService, transaction.paymentService) && Objects.equals(rating, transaction.rating) && Objects.equals(userId, transaction.userId) && Objects.equals(paymentInstance, transaction.paymentInstance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(outcome, id, tenant, organization, shopCode, requestDate, responseDate, paymentService, rating, userId, paymentInstance);
    }

    @Override
    public String toString() {
        return "{" +
            " outcome='" + getOutcome() + "'" +
            ", id='" + getId() + "'" +
            ", tenant='" + getTenant() + "'" +
            ", organization='" + getOrganization() + "'" +
            ", shopCode='" + getShopCode() + "'" +
            ", requestDate='" + getRequestDate() + "'" +
            ", responseDate='" + getResponseDate() + "'" +
            ", paymentService='" + getPaymentService() + "'" +
            ", rating='" + getRating() + "'" +
            ", userId='" + getUserId() + "'" +
            ", paymentInstance='" + getPaymentInstance() + "'" +
            "}";
    }

}
