package it.kdm.docer.sdk.frontend.utils;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.Transformer;
import javax.xml.transform.stream.StreamSource;

import org.apache.axiom.om.impl.jaxp.OMResult;
import org.slf4j.Logger;

import it.kdm.docer.commons.configuration.ConfigurationUtils;
import it.kdm.docer.sdk.exceptions.DocerException;
import it.kdm.utils.TransformerFactory;
import net.sf.saxon.FeatureKeys;
import net.sf.saxon.TransformerFactoryImpl;

public enum ResourceManager {
	
	INSTANCE;
	private Logger log = org.slf4j.LoggerFactory.getLogger(ResourceManager.class);
	
	private Map<String, Long> timestampMap;
	private Map<String, String> resourceMap;

	private ResourceManager () {
		log.debug("Initialization");
		this.timestampMap = Collections.synchronizedMap(new HashMap<String, Long>());
		this.resourceMap = Collections.synchronizedMap(new HashMap<String, String>());
	}
	
	public synchronized String getTemplate(String action) throws DocerException {
		log.debug(String.format("Requested template for %s", action));
		try {
			
			File xslt = getXsltFile("kforms.xslt");
			File file = ConfigurationUtils.loadResourceFile(String.format("templates/%s.xhtml", action));
			long timestamp = file.lastModified();
			
			if(!this.timestampMap.containsKey(action)) {
				this.timestampMap.put(action, timestamp);
			}
			
			if(this.timestampMap.get(action) < timestamp || 
					!this.resourceMap.containsKey(action)) {
				
				TransformerFactoryImpl saxonFactory = TransformerFactory.newInstance();
				saxonFactory.setAttribute(FeatureKeys.XINCLUDE, "true");
				Transformer trans = saxonFactory.newTransformer(
						new StreamSource(xslt));	
				OMResult result = new OMResult();
				
				log.debug("getTemplate() -> parsing and transforming template");
				trans.transform(new StreamSource(file), result);
				log.debug("getTemplate() -> parsed and transformed template");
				
				String content = result.getRootElement().toString();
				
				this.timestampMap.put(action, timestamp);
				this.resourceMap.put(action, content);
			}
			
			return this.resourceMap.get(action);
			
		} catch (DocerException e) {
			log.error(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new DocerException(e);
		}
	}
	
	private File getXsltFile(String fileName) throws DocerException
	{
        try {
            File file = ConfigurationUtils.loadResourceFile(String.format("templates/library/%s", fileName));

            if (!file.exists())
                throw new DocerException("xsltFile not found: " + file.getAbsolutePath());

            return file;
        } catch (Exception e) {
            throw new DocerException(e);
        }
    }
}
