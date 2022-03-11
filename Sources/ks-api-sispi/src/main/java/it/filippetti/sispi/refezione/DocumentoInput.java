package it.filippetti.sispi.refezione;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DocumentoInput {

	@JsonProperty("base64")
	private String base64;

	@JsonProperty("content-type")
	private String contentType;

	@JsonProperty("title")
	private String title;

	public String getBase64() {
		return base64;
	}

	public void setBase64(String base64) {
		this.base64 = base64;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
