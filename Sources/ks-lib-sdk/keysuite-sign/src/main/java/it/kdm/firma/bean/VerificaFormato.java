package it.kdm.firma.bean;

public class VerificaFormato {
	public static final String NOME = "VerificaFormato";
	public static final String NOME_VERIFICA_RICONOSCIMENTO_FORMATO = "VerificaRiconoscimentoFormato";
	public static final String NOME_MESSAGGIO_RICONOSCIMENTO_FORMATO = "MessaggioRiconoscimentoFormato";
	
	private String verificaRiconoscimentoFormato;
	private String messaggioRiconoscimentoFormato;
	
	public VerificaFormato(boolean verificaRiconoscimentoFormato, String messaggioRiconoscimentoFormato) {
		this.verificaRiconoscimentoFormato = verificaRiconoscimentoFormato ? EsitoVerifiche.POSITIVE : EsitoVerifiche.NEGATIVE;
		this.messaggioRiconoscimentoFormato = messaggioRiconoscimentoFormato;
	}

	public String getVerificaRiconoscimentoFormato() {
		return verificaRiconoscimentoFormato;
	}

	public void setVerificaRiconoscimentoFormato(boolean verificaRiconoscimentoFormato) {
		this.verificaRiconoscimentoFormato = verificaRiconoscimentoFormato ? EsitoVerifiche.POSITIVE : EsitoVerifiche.NEGATIVE;
	}

	public String getMessaggioRiconoscimentoFormato() {
		return messaggioRiconoscimentoFormato;
	}

	public void setMessaggioRiconoscimentoFormato(String messaggioRiconoscimentoFormato) {
		this.messaggioRiconoscimentoFormato = messaggioRiconoscimentoFormato;
	}
	
}