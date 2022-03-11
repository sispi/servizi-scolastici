package it.filippetti.sispi.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "GIORNI_FREQUENZA_ISTITUTO")
@DynamicUpdate
public class GiorniFrequenzaIstituto implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "giorni_frequenza_seq")
	@SequenceGenerator(name = "giorni_frequenza_seq", sequenceName = "GIORNI_FREQUENZA_ISTITUTO_SEQ", allocationSize = 1)
	private Long id;

	@Column(name = "ANNO_SCOLASTICO")
	private String annoScolastico;

	@Column(name = "SORT")
	private Integer sort;

	@Column(name = "DESCRIZIONE")
	private String descrizione;

	@Column(name = "GIORNI_FREQUENZA")
	private Integer giorniFrequenza;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAnnoScolastico() {
		return annoScolastico;
	}

	public void setAnnoScolastico(String annoScolastico) {
		this.annoScolastico = annoScolastico;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public Integer getGiorniFrequenza() {
		return giorniFrequenza;
	}

	public void setGiorniFrequenza(Integer giorniFrequenza) {
		this.giorniFrequenza = giorniFrequenza;
	}
}
