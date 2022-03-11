package it.filippetti.sispi.model;

public enum Modalita {
	pagopa, bollettinoonline, bollettino;

	public static Modalita getEnumFromId(Integer idEnum) {
		try {
			return Modalita.values()[idEnum];
		} catch (Exception e) {

		}
		return null;
	}
}