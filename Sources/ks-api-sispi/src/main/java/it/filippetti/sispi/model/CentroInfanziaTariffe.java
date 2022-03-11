package it.filippetti.sispi.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PORTSCU_SCU_CENTRITARIFFE")
public class CentroInfanziaTariffe implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "CTA_COD")
	private Long id;

	@Column(name = "CTA_NUMCODICEASILO")
	private Long numCodiceAsilo;

	@Column(name = "CTA_DESDENOMASILO")
	private String denominazioneAsilo;

	@Column(name = "CTA_DESCATEGORIA")
	private String categoria;

	@Column(name = "CTA_DESUDE")
	private String ude;

	@Column(name = "CTA_FLGCOMUNALE")
	private String flagComunale;

	@Column(name = "CTA_NUMANNO")
	private Integer annoRiferimento;

	@Column(name = "CTA_NUMISEEDA")
	private Float iseeDa;

	@Column(name = "CTA_NUMISEEA")
	private Float iseeA;

	@Column(name = "CTA_DESFASCIAORARIA1")
	private String fasciaOraria1;

	@Column(name = "CTA_NUMIMPORTOFASCIAORARIA1")
	private Float importoFasciaOraria1;

	@Column(name = "CTA_DESFASCIAORARIA2")
	private String fasciaOraria2;

	@Column(name = "CTA_NUMIMPORTOFASCIAORARIA2")
	private Float importoFasciaOraria2;

	@Column(name = "CTA_DESFASCIAORARIA3")
	private String fasciaOraria3;

	@Column(name = "CTA_NUMIMPORTOFASCIAORARIA3")
	private Float importoFasciaOraria3;

	@Column(name = "CTA_DESFASCIAORARIA4")
	private String fasciaOraria4;

	@Column(name = "CTA_NUMIMPORTOFASCIAORARIA4")
	private Float importoFasciaOraria4;

	@Column(name = "CTA_DESFASCIAORARIA5")
	private String fasciaOraria5;

	@Column(name = "CTA_NUMIMPORTOFASCIAORARIA5")
	private Float importoFasciaOraria5;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getNumCodiceAsilo() {
		return numCodiceAsilo;
	}

	public void setNumCodiceAsilo(Long numCodiceAsilo) {
		this.numCodiceAsilo = numCodiceAsilo;
	}

	public String getDenominazioneAsilo() {
		return denominazioneAsilo;
	}

	public void setDenominazioneAsilo(String denominazioneAsilo) {
		this.denominazioneAsilo = denominazioneAsilo;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public String getUde() {
		return ude;
	}

	public void setUde(String ude) {
		this.ude = ude;
	}

	public String getFlagComunale() {
		return flagComunale;
	}

	public void setFlagComunale(String flagComunale) {
		this.flagComunale = flagComunale;
	}

	public Integer getAnnoRiferimento() {
		return annoRiferimento;
	}

	public void setAnnoRiferimento(Integer annoRiferimento) {
		this.annoRiferimento = annoRiferimento;
	}

	public Float getIseeDa() {
		return iseeDa;
	}

	public void setIseeDa(Float iseeDa) {
		this.iseeDa = iseeDa;
	}

	public Float getIseeA() {
		return iseeA;
	}

	public void setIseeA(Float iseeA) {
		this.iseeA = iseeA;
	}

	public String getFasciaOraria1() {
		return fasciaOraria1;
	}

	public void setFasciaOraria1(String fasciaOraria1) {
		this.fasciaOraria1 = fasciaOraria1;
	}

	public Float getImportoFasciaOraria1() {
		return importoFasciaOraria1;
	}

	public void setImportoFasciaOraria1(Float importoFasciaOraria1) {
		this.importoFasciaOraria1 = importoFasciaOraria1;
	}

	public String getFasciaOraria2() {
		return fasciaOraria2;
	}

	public void setFasciaOraria2(String fasciaOraria2) {
		this.fasciaOraria2 = fasciaOraria2;
	}

	public Float getImportoFasciaOraria2() {
		return importoFasciaOraria2;
	}

	public void setImportoFasciaOraria2(Float importoFasciaOraria2) {
		this.importoFasciaOraria2 = importoFasciaOraria2;
	}

	public String getFasciaOraria3() {
		return fasciaOraria3;
	}

	public void setFasciaOraria3(String fasciaOraria3) {
		this.fasciaOraria3 = fasciaOraria3;
	}

	public Float getImportoFasciaOraria3() {
		return importoFasciaOraria3;
	}

	public void setImportoFasciaOraria3(Float importoFasciaOraria3) {
		this.importoFasciaOraria3 = importoFasciaOraria3;
	}

	public String getFasciaOraria4() {
		return fasciaOraria4;
	}

	public void setFasciaOraria4(String fasciaOraria4) {
		this.fasciaOraria4 = fasciaOraria4;
	}

	public Float getImportoFasciaOraria4() {
		return importoFasciaOraria4;
	}

	public void setImportoFasciaOraria4(Float importoFasciaOraria4) {
		this.importoFasciaOraria4 = importoFasciaOraria4;
	}

	public String getFasciaOraria5() {
		return fasciaOraria5;
	}

	public void setFasciaOraria5(String fasciaOraria5) {
		this.fasciaOraria5 = fasciaOraria5;
	}

	public Float getImportoFasciaOraria5() {
		return importoFasciaOraria5;
	}

	public void setImportoFasciaOraria5(Float importoFasciaOraria5) {
		this.importoFasciaOraria5 = importoFasciaOraria5;
	}
}
