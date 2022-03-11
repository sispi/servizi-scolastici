package it.kdm.docer.sdk.interfaces;

import it.kdm.docer.sdk.classes.KeyValuePair;

public interface ISearchItem {

	public void setMetadata(KeyValuePair[] metadata);	
	public KeyValuePair[] getMetadata();
	
}
