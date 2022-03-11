package it.kdm.docer.registrazione.database;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Registro {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String oggetto;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar data;
	
	@Column(nullable=true)
	private Long numero;
	
	@Column(nullable=false)
	private String ente;
	
	@Column(nullable=false)
	private String aoo;
	
	@Column(nullable=false)
	private String registro;

	private Integer anno;
	
	@Column(nullable=false)
	private String docId;

    @Column(nullable = true, name = "\"user\"")
    private String user;

    @Column(nullable = false)
    private Character tipoRichiesta;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOggetto() {
		return oggetto;
	}

	public void setOggetto(String oggetto) {
		this.oggetto = oggetto;
	}

	public Calendar getData() {
		return data;
	}

	public void setData(Calendar data) {
		this.data = data;
		this.anno = data.get(Calendar.YEAR);
	}

	public Long getNumero() {
		return numero;
	}

	public void setNumero(Long numero) {
		this.numero = numero;
	}

	public String getEnte() {
		return ente;
	}

	public void setEnte(String ente) {
		this.ente = ente;
	}

	public String getAoo() {
		return aoo;
	}

	public void setAoo(String aoo) {
		this.aoo = aoo;
	}

	public String getRegistro() {
		return registro;
	}

	public void setRegistro(String registro) {
		this.registro = registro;
	}

	public Integer getAnno() {
		return anno;
	}
	
	public void setAnno(Integer anno) {
		this.anno = anno;
	}

	public String getDocId() {
		return docId;
	}

	public void setDocId(String docId) {
		this.docId = docId;
	}

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Character getTipoRichiesta() {
        return tipoRichiesta;
    }

    public void setTipoRichiesta(Character provider) {
        this.tipoRichiesta = provider;
    }
}