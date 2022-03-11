package it.filippetti.sispi.portalescuola;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@ConfigurationProperties(prefix = "sispi.portale", ignoreUnknownFields = false)
@Validated
public class PortaleScuolaProperties {

	@NotBlank
	private String url;

	@NotEmpty
	private String[] codiciScuola;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String[] getCodiciScuola() {
		return codiciScuola;
	}

	public void setCodiciScuola(String[] codiciScuola) {
		this.codiciScuola = codiciScuola;
	}
}
