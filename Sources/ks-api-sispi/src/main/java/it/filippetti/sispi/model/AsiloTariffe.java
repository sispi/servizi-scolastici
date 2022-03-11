package it.filippetti.sispi.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SCU_ASILOTARIFFE")
public class AsiloTariffe implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ATA_COD")
	private Long id;

	@Column(name = "ATA_DESDENOMASILO")
	private String denominazioneAsilo;

	@Column(name = "ATA_DESCATEGORIA")
	private String descrizioneCategoria;

	@Column(name = "ATA_NUMANNO")
	private Integer annoRiferimento;

	@Column(name = "ATA_DESFASCIAORARIA1")
	private String fasciaOraria1;

	@Column(name = "ATA_NUMIMPORTOFASCIAORARIA1")
	private Float importoFasciaOraria1;

	@Column(name = "ATA_DESFASCIAORARIA2")
	private String fasciaOraria2;

	@Column(name = "ATA_NUMIMPORTOFASCIAORARIA2")
	private Float importoFasciaOraria2;

	@Column(name = "ATA_DESFASCIAORARIA3")
	private String fasciaOraria3;

	@Column(name = "ATA_NUMIMPORTOFASCIAORARIA3")
	private Float importoFasciaOraria3;

	@Column(name = "ATA_DESFASCIAORARIA4")
	private String fasciaOraria4;

	@Column(name = "ATA_NUMIMPORTOFASCIAORARIA4")
	private Float importoFasciaOraria4;

	@Column(name = "ATA_DESFASCIAORARIA5")
	private String fasciaOraria5;

	@Column(name = "ATA_NUMIMPORTOFASCIAORARIA5")
	private Float importoFasciaOraria5;

	@Column(name = "ATA_NUMISEEDA")
	private Float importoIseeDa;

	@Column(name = "ATA_NUMISEEA")
	private Float importoIseeA;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDenominazioneAsilo() {
		return denominazioneAsilo;
	}

	public void setDenominazioneAsilo(String denominazioneAsilo) {
		this.denominazioneAsilo = denominazioneAsilo;
	}

	public String getDescrizioneCategoria() {
		return descrizioneCategoria;
	}

	public void setDescrizioneCategoria(String descrizioneCategoria) {
		this.descrizioneCategoria = descrizioneCategoria;
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

	public Float getImportoIseeDa() {
		return importoIseeDa;
	}

	public void setImportoIseeDa(Float importoIseeDa) {
		this.importoIseeDa = importoIseeDa;
	}

	public Float getImportoIseeA() {
		return importoIseeA;
	}

	public void setImportoIseeA(Float importoIseeA) {
		this.importoIseeA = importoIseeA;
	}

	public Integer getAnnoRiferimento() {
		return annoRiferimento;
	}

	public void setAnnoRiferimento(Integer annoRiferimento) {
		this.annoRiferimento = annoRiferimento;
	}
}
