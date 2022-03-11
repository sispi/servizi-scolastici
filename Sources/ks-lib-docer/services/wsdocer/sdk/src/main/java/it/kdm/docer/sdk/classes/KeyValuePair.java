package it.kdm.docer.sdk.classes;

import it.kdm.docer.sdk.interfaces.IKeyValuePair;

public class KeyValuePair implements IKeyValuePair {

	String key = null;
	String value = null;
	
	public KeyValuePair() {
		key = null;
		value = null;
	}
	
	public KeyValuePair(String key, String value) {
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public String getValue() {
		return this.value;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String toString() {
		return String.format("(%s : %s)", key, value);
	}
}
