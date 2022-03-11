package it.kdm.docer.sdk.classes;

import java.util.ArrayList;
import java.util.List;

import it.kdm.docer.sdk.interfaces.ISearchItem;

public class SearchItem implements ISearchItem{

	KeyValuePair[] metadata = new KeyValuePair[0];
	
	public void setMetadata(KeyValuePair[] metadata) {
		this.metadata = (KeyValuePair[])metadata;		
	}

	public KeyValuePair[] getMetadata() {
		return this.metadata;
	}
	
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		for(KeyValuePair pair : metadata) {
			builder.append(pair.toString());
			builder.append(", ");
		}
		
		builder.delete(builder.length()-2, builder.length());
		builder.append("]");
		
		return builder.toString();
		
	}
}
