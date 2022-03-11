package it.kdm.docer.conservazione.batch;

public enum Stato {
	IN_ATTESA('A'),
	COMPLETATO('C'),
	ERRORE('E'),
	WAITING('W'),
	CONSERVATO('X'),
	ERRORE_DOCER('F');
	
	private char value;

	Stato(char val) {
		this.value = val;
	}

	public char toChar() {
		return value;
	}
	
	@Override
	public String toString() {
		return Character.toString(value);
	}
	
	public static Stato fromChar(char chr) {
		for (Stato stato : Stato.values()) {
			if (stato.toChar() == chr) {
				return stato;
			}
		}
		
		throw new IllegalArgumentException("Unrecognised char " + chr);
	}
	
	public static Stato fromString(String str) {
		if(str == null || str.length() == 0) {
			throw new IllegalArgumentException("Empty or null string");
		}
		
		char chr = str.charAt(0);
		return fromChar(chr);
	}
}
