package it.kdm.docer.conservazione.ws;

import javax.activation.DataHandler;

public class File {

	private String id;
	private String name;
	private long size;
	private DataHandler handler;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public DataHandler getHandler() {
		return handler;
	}

	public void setHandler(DataHandler handler) {
		this.handler = handler;
	}

}
