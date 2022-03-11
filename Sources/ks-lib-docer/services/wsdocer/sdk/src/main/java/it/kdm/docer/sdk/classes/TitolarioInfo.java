package it.kdm.docer.sdk.classes;

import it.kdm.docer.sdk.EnumBoolean;
import it.kdm.docer.sdk.interfaces.ITitolarioInfo;

import java.util.HashMap;
import java.util.Map;


public class TitolarioInfo implements ITitolarioInfo{

    private String pianoClassificazione = null;
    private String classifica = null;
    private String codiceTitolario = null;
    private String descrizione = null;
    private EnumBoolean enabled = EnumBoolean.UNSPECIFIED;
    private String codiceEnte  = null;
    private String codiceAOO = null;
    private String parentClassifica = null;
    private Map<String, String> extraInfo = new HashMap<String, String>();

    public String getPianoClassificazione() {
        return pianoClassificazione;
    }

    public void setPianoClassificazione(String pianoClassificazione) {
        this.pianoClassificazione = pianoClassificazione;
    }

    public String getClassifica() {
		return this.classifica;
	}
	
	public void setClassifica(String classifica) {
		this.classifica = classifica;
	}
	
	public String getCodiceTitolario() {		
		return this.codiceTitolario;
	}
	
	public void setCodiceTitolario(String codiceTitolario) {
		this.codiceTitolario = codiceTitolario;
	}
	
	public String getParentClassifica() {		
		return this.parentClassifica;
	}
	
	public void setParentClassifica(String parentClassifica) {
		this.parentClassifica = parentClassifica;
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
	
	public void setCodiceEnte(String codEnte) {
		this.codiceEnte= codEnte;
	}		
	
	public String getCodiceAOO() {	
		return this.codiceAOO;
	}

	public void setCodiceAOO(String codAOO) {
		this.codiceAOO = codAOO;
	}

	public Map<String, String> getExtraInfo() {
		return this.extraInfo;
	}

	public void setExtraInfo(Map<String, String> extraInfo) {
		this.extraInfo = extraInfo;
	}
	
}
