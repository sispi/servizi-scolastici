package it.kdm.docer.sdk.classes;

import javax.activation.DataHandler;

public class StreamDescriptor {
	
	private long byteSize;
	private DataHandler handler;
	
	/**
	 * Returns the size of the current stream, in bytes.
	 * @return the number of bytes contained in the stream
	 */
	public long getByteSize() {
		return byteSize;
	}
	
	
	public void setByteSize(long size) {
		this.byteSize = size;
	}
	
	public DataHandler getHandler() {
		return handler;
	}
	
	public void setHandler(DataHandler handler) {
		this.handler = handler;
	}
}
