package it.kdm.docer.management.model;

public enum RunMode {
	TEST("t"), RUN("r"), ALL("a");

	private String text;

	RunMode(String text) {
		this.text = text;
	}

	public String getText() {
		return this.text;
	}

	public static RunMode fromString(String text) {
		if (text != null) {
			for (RunMode b : RunMode.values()) {
				if (text.equalsIgnoreCase(b.text)) {
					return b;
				}
			}
		}
		return null;
	}

}
