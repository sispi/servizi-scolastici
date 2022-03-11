package it.filippetti.sispi.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "FASCIA_ISTITUTO")
@DynamicUpdate
public class FasciaIstituto implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "fascia_istituto_seq")
	@SequenceGenerator(name = "fascia_istituto_seq", sequenceName = "FASCIA_ISTITUTO_SEQ", allocationSize = 1)
	private Long id;

	@Column(name = "CODICE_FISCALE")
	@NotNull(message = "Codice fiscale obbligatorio")
	private String codiceFiscale;

	@Column(name = "ANNO_SCOLASTICO")
	@NotNull(message = "Anno scolastico obbligatorio")
	private String annoScolastico;

	@Column(name = "DESCRIZIONE")
	@NotNull(message = "Descrizione obbligatoria")
	private String descrizione;

	@Column(name = "SORT")
	@NotNull(message = "Sort obbligatorio")
	private Integer sort;

	@Column(name = "NOME_ISTITUTO")
	@NotNull(message = "Sort obbligatorio")
	private String nomeIstituto;

	@Column(name = "FASCIA")
	@NotNull(message = "Sort obbligatorio")
	private String fascia;

	@Column(name = "FASCIA_ORARIA")
	@NotNull(message = "Sort obbligatorio")
	private String fasciaOraria;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "RATA_ID")
	private Rata rata;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}

	public String getAnnoScolastico() {
		return annoScolastico;
	}

	public void setAnnoScolastico(String annoScolastico) {
		this.annoScolastico = annoScolastico;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getNomeIstituto() {
		return nomeIstituto;
	}

	public void setNomeIstituto(String nomeIstituto) {
		this.nomeIstituto = nomeIstituto;
	}

	public String getFascia() {
		return fascia;
	}

	public void setFascia(String fascia) {
		this.fascia = fascia;
	}

	public String getFasciaOraria() {
		return fasciaOraria;
	}

	public void setFasciaOraria(String fasciaOraria) {
		this.fasciaOraria = fasciaOraria;
	}

	public Rata getRata() {
		return rata;
	}

	public void setRata(Rata rata) {
		this.rata = rata;
	}

}
