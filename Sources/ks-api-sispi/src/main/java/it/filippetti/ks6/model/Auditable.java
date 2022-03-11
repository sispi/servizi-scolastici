/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks6.model;

import java.util.Date;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 *
 * @author mazzocchetti
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Auditable extends Identifiable {

	private static final long serialVersionUID = 1L;

	@CreatedBy
	@LastModifiedBy
	private String lastModifiedBy;

	@CreatedDate
	@LastModifiedDate
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastModifiedTs;

	public Auditable() {
		super();
	}

	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	public Date getLastModifiedTs() {
		return lastModifiedTs;
	}
}
