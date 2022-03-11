package it.filippetti.sispi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.filippetti.sispi.pagamentoretta.RataUtils;
import java.io.Serializable;
import java.util.Locale;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "RATA")
@DynamicUpdate
public class Rata implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "rata_seq")
	@SequenceGenerator(name = "rata_seq", sequenceName = "RATA_SEQ", allocationSize = 1)
	private Long id;

	@Column(name = "DESCRIZIONE")
	private String descrizione;

	@Column(name = "SORT")
	private Integer sort;

	@Column(name = "CF_ISCRITTO")
	private String cfIscritto;

	@Column(name = "VALORE_RATA")
	private String valoreRata;

	@Column(name = "ANNO_SCOLASTICO")
	private String annoScolastico;

	@Column(name = "MOTIVAZIONE_VARIAZIONE")
	private String motivazioneVariazione;

	@Column(name = "SCONTO_FAMIGLIA")
	private String scontoFamiglia;

	@Column(name = "GIORNI_FREQUENZA")
	private String giorniFrequenza;

	@Column(name = "VALORE_CONGUAGLIO")
	private String valoreConguaglio;

	@OneToMany(mappedBy = "rata", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JsonIgnore
	private Set<Pagamento> pagamenti;

	public Rata() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Set<Pagamento> getPagamenti() {
		return pagamenti;
	}

	public void setPagamenti(Set<Pagamento> pagamenti) {
		this.pagamenti = pagamenti;
	}

	public String getCfIscritto() {
		return cfIscritto;
	}

	public void setCfIscritto(String cfIscritto) {
		this.cfIscritto = cfIscritto;
	}

	public String getValoreRata() {
		return valoreRata;
	}

	public void setValoreRata(String valoreRata) {
		this.valoreRata = getImportoFormattato(valoreRata);
	}

	public String getAnnoScolastico() {
		return annoScolastico;
	}

	public void setAnnoScolastico(String annoScolastico) {
		this.annoScolastico = annoScolastico;
	}

	public String getMotivazioneVariazione() {
		return motivazioneVariazione;
	}

	public void setMotivazioneVariazione(String motivazioneVariazione) {
		this.motivazioneVariazione = motivazioneVariazione;
	}

	public String getScontoFamiglia() {
		return scontoFamiglia;
	}

	public void setScontoFamiglia(String scontoFamiglia) {
		this.scontoFamiglia = scontoFamiglia;
	}

	public String getGiorniFrequenza() {
		return giorniFrequenza;
	}

	public void setGiorniFrequenza(String giorniFrequenza) {
		this.giorniFrequenza = giorniFrequenza;
	}

	public String getValoreConguaglio() {
		return valoreConguaglio;
	}

	public void setValoreConguaglio(String valoreConguaglio) {
		this.valoreConguaglio = getImportoFormattato(valoreConguaglio);
	}

	@Transient
	@JsonIgnore
	public Float getImportoTotale() {
		Float result = RataUtils.convertStringToFloat(getValoreRata())
				+ RataUtils.convertStringToFloat(getValoreConguaglio());
		return result;
	}

	@Transient
	@JsonIgnore
	public String getImportoFormattato(String importoString) {
		String result = importoString;
		if (importoString != null) {
			try {
				result = String.format(Locale.US, "%.2f", Float.valueOf(importoString));
			} catch (Exception e) {
			}
		}
		return result;
	}

}
