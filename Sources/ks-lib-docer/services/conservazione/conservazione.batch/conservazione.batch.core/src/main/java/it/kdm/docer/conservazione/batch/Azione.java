package it.kdm.docer.conservazione.batch;

public enum Azione {
	CONSERVAZIONE('C'),
	MODIFICA_METADATI('M'),
	AGGIUNGI_DOCUMENTO('A');
	
	private char value;

	Azione(char val) {
		this.value = val;
	}

	public char toChar() {
		return value;
	}
	
	public String description() {
		return super.toString();
	}
	
	@Override
	public String toString() {
		return Character.toString(value);
	}
	
	public static Azione fromChar(char chr) {
		for (Azione azione : Azione.values()) {
			if (azione.toChar() == chr) {
				return azione;
			}
		}
		
		throw new IllegalArgumentException("Unrecognised char " + chr);
	}
	
	public static Azione fromString(String str) {
		if(str == null || str.length() == 0) {
			throw new IllegalArgumentException("Empty or null string");
		}
		
		char chr = str.charAt(0);
		return fromChar(chr);
	}
}
