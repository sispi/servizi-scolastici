package it.kdm.docer.sdk.classes;

import it.kdm.docer.sdk.interfaces.ITitolarioId;


public class TitolarioId implements ITitolarioId{

    private String pianoClassificazione = null;
	private String classifica = null;    
	private String codiceTitolario = null;
    private String enteId = null;
    private String aooId = null;

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
	
	public String getCodiceEnte() {		
		return this.enteId;
	}
	
	public void setCodiceEnte(String codEnte) {
		this.enteId = codEnte;
	}		
	
	public String getCodiceAOO() {	
		return this.aooId;
	}

	public void setCodiceAOO(String codAOO) {
		this.aooId =codAOO;
	}

}
