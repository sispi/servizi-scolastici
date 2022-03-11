package it.filippetti.sispi.iscrizioni;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ImportIscrizioniBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<String> iseeImportati;
	private List<String> iseeError;

	public ImportIscrizioniBean() {
		this.iseeImportati = new ArrayList<String>();
		this.iseeError = new ArrayList<String>();
	}

	public List<String> getIseeImportati() {
		return iseeImportati;
	}

	public void setIseeImportati(List<String> iseeImportati) {
		this.iseeImportati = iseeImportati;
	}

	public List<String> getIseeError() {
		return iseeError;
	}

	public void setIseeError(List<String> iseeError) {
		this.iseeError = iseeError;
	}
}
