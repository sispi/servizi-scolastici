package it.kdm.docer.sdk.classes;

import java.util.HashMap;
import java.util.Map;

import it.kdm.docer.sdk.EnumBoolean;
import it.kdm.docer.sdk.interfaces.IFascicoloInfo;

//public class FascicoloInfo extends it.kdm.ws.SearchItem implements IFascicoloInfo{
public class FascicoloInfo implements IFascicoloInfo{

    String codiceEnte = null;
    String codiceAOO = null;
    String classifica = null;
    String pianoClassificazione = null;
    String descrizione = null;
    EnumBoolean enabled = EnumBoolean.UNSPECIFIED;
    String numeroFascicolo = null;
    String progressivo = null;
    String parentProgressivo = null;
    String annoFascicolo = null;
    Map<String, String> extraInfo = new HashMap<String, String>();

    
	public String getNumeroFascicolo() {
	        //return this.get("numeroFascicolo");
		return this.numeroFascicolo;
	}
    
	public void setNumeroFascicolo(String numeroFascicolo) {
		this.numeroFascicolo = numeroFascicolo;		
	}	
    
	public String getAnnoFascicolo() {		
		return this.annoFascicolo;
	}
    
	public void setAnnoFascicolo(String annoFascicolo) {
		this.annoFascicolo = annoFascicolo;		
	}
	
	public String getDescrizione() {		
		return this.descrizione;
	}	
	
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;		
	}
	
	public EnumBoolean getEnabled() {		
		return this.enabled;
	}	
	
	public void setEnabled(EnumBoolean enabled) {		
		this.enabled = enabled;
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

    public String getPianoClassificazione() {
        return pianoClassificazione;
    }

    public void setPianoClassificazione(String pianoClassificazione) {
        this.pianoClassificazione = pianoClassificazione;
    }

    public String getProgressivo() {
		return this.progressivo;
	}

	public void setProgressivo(String progressivo) {
		this.progressivo = progressivo;
	}
	
	public String getParentProgressivo() {
		return this.parentProgressivo;
	}
	
	public void setParentProgressivo(String parentProgressivo) {
		this.parentProgressivo = parentProgressivo;
	}

	public void setExtraInfo(Map<String, String> extraInfo) {
		this.extraInfo = extraInfo;
	}
	public Map<String, String> getExtraInfo() {
		return this.extraInfo;
	}


}
