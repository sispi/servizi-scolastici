package it.kdm.docer.conservazione.converter;

import it.kdm.docer.conservazione.ConservazioneException;
import it.kdm.utils.TransformerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.stream.StreamSource;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.impl.jaxp.OMResult;
import org.apache.axiom.om.impl.jaxp.OMSource;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;

public class Converter {

	private static final String KEY_FORMAT = "%s__%s";

	Logger log = org.slf4j.LoggerFactory.getLogger(Converter.class);
	
	private final static String TRANSFORM_TEMPLATE = "%s.xslt";
	private final static String DEFAULT_TYPE = "default";

	private static Map<String, Templates> cachedTransforms = new ConcurrentHashMap<String, Templates>();
	private File templateFolder;

	public Converter(File templatesDir) {
		this.templateFolder = templatesDir;
	}
	
	public File getTemplateFolder() {
		return templateFolder;
	}

	public void setTemplateFolder(File templateFolder) {
		this.templateFolder = templateFolder;
	}

	public OMElement converter(OMElement xmlDocument, String documentType,
			String codEnte, String codAoo) throws ConservazioneException {
		try {
			log.debug("Incoming Document: " + xmlDocument.toString());
			if (documentType == null || documentType.equals(""))
				documentType = DEFAULT_TYPE;

			String key = String.format(KEY_FORMAT, String.format("%s_%s", codEnte, codAoo),
					documentType);

			//key = key.toLowerCase();
			
			log.info("Requested template: " + key);
			if (!cachedTransforms.containsKey(key)
					|| !cachedTransforms.containsKey(documentType)) {
				InputStream transform = this.getTransform(key);
				if (transform == null) {

					transform = this.getTransform(documentType);
					key = documentType; //.toLowerCase();
					if (transform == null) {
						throw new ConservazioneException(
								"Cannot convert documents of type "
										+ documentType);
					}

				}

				Templates template = TransformerFactory.newInstance()
						.newTemplates(new StreamSource(transform));
				cachedTransforms.put(key, template);

			}
			
			Templates template;
			if(cachedTransforms.containsKey(key)) {
				template = cachedTransforms.get(key);
			} else if(cachedTransforms.containsKey(documentType)){
				template = cachedTransforms.get(documentType);
			} else {
				template = cachedTransforms.get(DEFAULT_TYPE);
			}
			
			Transformer transformer = template.newTransformer();

			OMResult result = new OMResult();
			transformer.transform(new OMSource(xmlDocument), result);

			log.debug("Transformed Document: " + result.getRootElement().toString());
			return result.getRootElement();

		} catch (Exception e) {
			throw new ConservazioneException(e);
		}
	}

	private InputStream getTransform(String key) throws IOException {
		File file = new File(this.templateFolder, String.format(
				TRANSFORM_TEMPLATE, key));
		if(!file.exists()) {
			log.debug("Template not found: " + key);
			return null;
		}
		
		log.debug("Template found: " + key);
		return FileUtils.openInputStream(file);
	}

}
