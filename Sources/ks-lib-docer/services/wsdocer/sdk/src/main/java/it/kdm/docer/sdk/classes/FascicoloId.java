package it.kdm.docer.sdk.classes;

import java.util.HashMap;
import java.util.Map;

import it.kdm.docer.sdk.EnumBoolean;
import it.kdm.docer.sdk.interfaces.IFascicoloId;

public class FascicoloId implements IFascicoloId{

	String progressivo = null;
    String codiceEnte = null;
    String codiceAOO = null;
    String classifica = null;
    String descrizione = null;
    EnumBoolean enabled = EnumBoolean.UNSPECIFIED;
    String numero = null;
    String anno = null;
    String pianoClassificazione = null;
    Map<String, String> extraInfo = new HashMap<String, String>();
    
    public String getProgressivo() {		
		return this.progressivo;
	}
    
	public void setProgressivo(String progressivo) {
		this.progressivo = progressivo;		
	}
	
	public String getNumeroFascicolo() {		
		return this.numero;
	}
    
	public void setNumeroFascicolo(String numeroFascicolo) {
		this.numero = numeroFascicolo;		
	}	
    
	public String getAnnoFascicolo() {		
		return this.anno;
	}
    
	public void setAnnoFascicolo(String annoFascicolo) {
		this.anno = annoFascicolo;		
	}	
	
	public String getCodiceEnte() {		
		return this.codiceEnte;
	}	
	public void setCodiceEnte(String codiceEnte) {
		this.codiceEnte = codiceEnte;		
	}	
	
	public String getCodiceAOO() {		
		return this.codiceAOO;
	}
	public void setCodiceAOO(String codiceAOO) {
		this.codiceAOO = codiceAOO;	
	}

	public String getClassifica() {		
		return this.classifica;
	}
	public void setClassifica(String classifica) {
		this.classifica = classifica;		
	}

    // DOCER-36 Piano di classificazione
    public String getPianoClassificazione() {
        return pianoClassificazione;
    }
    public void setPianoClassificazione(String pianoClassificazione) {
        this.pianoClassificazione = pianoClassificazione;
    }
}
