package it.filippetti.sispi.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "SCU_ISCRIZIONI2021")
@DynamicUpdate
public class ScuIscrizioni implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID_PORTALE")
	private String idPortale;

	@Column(name = "TIPO_RICHIEDENTE")
	private String tipoRichiedente;

	@Column(name = "TIPO_RICHIESTA")
	private String tipoRichiesta;

	@Column(name = "CF_RICHIEDENTE")
	private String cfRichiedente;

	@Column(name = "NOME_RICHIEDENTE")
	private String nomeRichiedente;

	@Column(name = "COGNOME_RICHIEDENTE")
	private String cognomeRichiedente;

	@Column(name = "EMAIL")
	private String email;

	@Column(name = "DATA_NASCITA_MINORE")
	private String dataNascitaMinore;

	@Column(name = "NOME_MINORE")
	private String nomeMinore;

	@Column(name = "COGNOME_MINORE")
	private String cognomeMinore;

	@Column(name = "CF_MINORE")
	private String cfMinore;

	@Column(name = "ID_FAMIGLIA")
	private Long idFamiglia;

	@Column(name = "NOME_ASILO")
	private String nomeAsilo;

	@Column(name = "NOME_SCUOLA")
	private String nomeScuola;

	@Column(name = "FASCIA")
	private String fascia;

	@Column(name = "AMMISSIONE")
	private String ammissione;

	@Column(name = "FLGVALUTAZIONE")
	private String flgValutazione;

	@Column(name = "ISEE")
	private Float isee;

	public String getIdPortale() {
		return idPortale;
	}

	public void setIdPortale(String idPortale) {
		this.idPortale = idPortale;
	}

	public String getTipoRichiedente() {
		return tipoRichiedente;
	}

	public void setTipoRichiedente(String tipoRichiedente) {
		this.tipoRichiedente = tipoRichiedente;
	}

	public String getTipoRichiesta() {
		return tipoRichiesta;
	}

	public void setTipoRichiesta(String tipoRichiesta) {
		this.tipoRichiesta = tipoRichiesta;
	}

	public String getCfRichiedente() {
		return cfRichiedente;
	}

	public void setCfRichiedente(String cfRichiedente) {
		this.cfRichiedente = cfRichiedente;
	}

	public String getNomeRichiedente() {
		return nomeRichiedente;
	}

	public void setNomeRichiedente(String nomeRichiedente) {
		this.nomeRichiedente = nomeRichiedente;
	}

	public String getCognomeRichiedente() {
		return cognomeRichiedente;
	}

	public void setCognomeRichiedente(String cognomeRichiedente) {
		this.cognomeRichiedente = cognomeRichiedente;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDataNascitaMinore() {
		return dataNascitaMinore;
	}

	public void setDataNascitaMinore(String dataNascitaMinore) {
		this.dataNascitaMinore = dataNascitaMinore;
	}

	public String getNomeMinore() {
		return nomeMinore;
	}

	public void setNomeMinore(String nomeMinore) {
		this.nomeMinore = nomeMinore;
	}

	public String getCognomeMinore() {
		return cognomeMinore;
	}

	public void setCognomeMinore(String cognomeMinore) {
		this.cognomeMinore = cognomeMinore;
	}

	public String getCfMinore() {
		return cfMinore;
	}

	public void setCfMinore(String cfMinore) {
		this.cfMinore = cfMinore;
	}

	public Long getIdFamiglia() {
		return idFamiglia;
	}

	public void setIdFamiglia(Long idFamiglia) {
		this.idFamiglia = idFamiglia;
	}

	public String getNomeAsilo() {
		return nomeAsilo;
	}

	public void setNomeAsilo(String nomeAsilo) {
		this.nomeAsilo = nomeAsilo;
	}

	public String getNomeScuola() {
		return nomeScuola;
	}

	public void setNomeScuola(String nomeScuola) {
		this.nomeScuola = nomeScuola;
	}

	public String getFascia() {
		return fascia;
	}

	public void setFascia(String fascia) {
		this.fascia = fascia;
	}

	public String getAmmissione() {
		return ammissione;
	}

	public void setAmmissione(String ammissione) {
		this.ammissione = ammissione;
	}

	public String getFlgValutazione() {
		return flgValutazione;
	}

	public void setFlgValutazione(String flgValutazione) {
		this.flgValutazione = flgValutazione;
	}

	public Float getIsee() {
		return isee;
	}

	public void setIsee(Float isee) {
		this.isee = isee;
	}

}
