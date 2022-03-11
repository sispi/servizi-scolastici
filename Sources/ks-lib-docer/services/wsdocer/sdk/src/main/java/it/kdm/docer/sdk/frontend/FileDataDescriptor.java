package it.kdm.docer.sdk.frontend;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import javax.activation.DataHandler;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;

//import eu.medsea.mimeutil.MimeType;
//import eu.medsea.mimeutil.MimeUtil;

public class FileDataDescriptor {
	
	private static final String DEFAULT_CONTENT_TYPE = "application/octet-stream";
	
	private static Logger log = org.slf4j.LoggerFactory.getLogger(FileDataDescriptor.class);
	
	private String filename;
	private String contentType;
	private long size = -1;
	
	private InputStream inputStream;
	
	public FileDataDescriptor(InputStream data) {
		this(data, "");
		log.debug("FileDataDescriptor(InputStream)");
	}
	
	public FileDataDescriptor(InputStream data, String filename) {
		this(data, filename, DEFAULT_CONTENT_TYPE);
		log.debug("FileDataDescriptor(InputStream, String)");
	}
	
	public FileDataDescriptor(InputStream data, String filename, String contentType) {
		log.debug("FileDataDescriptor(InputStream, String, String)");
		this.inputStream = data;
		this.filename = filename == null ? "" : filename ;
		this.contentType = (contentType == null || contentType.equals("") ?
							DEFAULT_CONTENT_TYPE : contentType);
	}
	
	public FileDataDescriptor(InputStream data, String filename, 
			String contentType, long size) {
		log.debug("FileDataDescriptor(InputStream, String, String)");
		this.inputStream = data;
		this.filename = filename == null ? "" : filename ;
		this.contentType = (contentType == null || contentType.equals("") ?
							DEFAULT_CONTENT_TYPE : contentType);
		this.size = size;
	}

//	public FileDataDescriptor(File file) throws IOException {
//		log.debug("FileDataDescriptor(File)");
//		Collection<?> types = MimeUtil.getMimeTypes(file);
//		MimeType type = MimeUtil.getMostSpecificMimeType(types);
//		String mimeType = (MimeUtil.isMimeTypeKnown(type) ? 
//							type.toString() : DEFAULT_CONTENT_TYPE);
//		
//		this.inputStream = FileUtils.openInputStream(file);
//		this.filename = file.getName();
//		this.contentType = mimeType;
//		this.size = file.length();
//	}
	
	public FileDataDescriptor(DataHandler handler) throws IOException {
		this(handler.getInputStream(),
				handler.getName(),
				handler.getContentType());
		log.debug("FileDataDescriptor(DataHandler)");
	}

	public String getFilename() {
		log.debug("getFilename()");
		return this.filename;
	}

	public void setFilename(String filename) {
		log.debug("setFilename(String)");
		this.filename = filename;
	}

	public String getContentType() {
		log.debug("getContentType()");
		return this.contentType;
	}
	
	public long getSize() {
		log.debug("getSize()");
		return this.size;
	}

	public void setContentType(String contentType) {
		log.debug("setContentType(String)");
		this.contentType = contentType;
	}

	public InputStream getInputStream() {
		log.debug("getInputStream()");
		return this.inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		log.debug("setInputStream(InputStream)");
		this.inputStream = inputStream;
	}
	
	public void setSize(long size) {
		log.debug("setSize(size)");
		this.size = size;
	}
}
