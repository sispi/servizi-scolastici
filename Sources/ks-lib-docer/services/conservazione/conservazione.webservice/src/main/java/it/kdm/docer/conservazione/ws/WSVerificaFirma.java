package it.kdm.docer.conservazione.ws;

import it.kdm.docer.commons.configuration.ConfigurationUtils;
import it.kdm.docer.conservazione.ConservazioneException;
import it.kdm.docer.conservazione.ConservazioneResult;
import it.kdm.docer.conservazione.bl.IConservazioneBL;
import it.kdm.docer.conservazione.bl.IConservazioneBL.TipoConservazione;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

import javax.activation.DataHandler;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class WSVerificaFirma {

	Logger log = org.slf4j.LoggerFactory.getLogger(WSVerificaFirma.class);
	
	private String tempDir;

	private ApplicationContext ctx;
	
	public WSVerificaFirma() throws IOException, ClassNotFoundException, URISyntaxException {
		Properties props = ConfigurationUtils.loadProperties("META-INF/wsconservazione.properties");
		
		this.tempDir = props.getProperty("temp.dir");
		log.debug("tempDir: " + this.tempDir);
		
		this.ctx = new ClassPathXmlApplicationContext("META-INF/applicationContext.xml");
	}
		
	public ConservazioneResult verificaFirma(String token, DataHandler xmlDocumento, String tipoDocumento, 
			String applicazioneChiamante, String tipoConservazione,
			boolean forzaAccettazione, boolean forzaConservazione,
			boolean forzaCollegamento, File[] files, String ente, String aoo) throws ConservazioneException {
	
		try {
			StAXOMBuilder builder = new StAXOMBuilder(xmlDocumento.getInputStream());
			OMElement doc = builder.getDocumentElement();
			
			java.io.File tempDir = new java.io.File(new URI(this.tempDir));
			
			if(!tempDir.exists()) {
				FileUtils.forceMkdir(tempDir);
			}
			
			it.kdm.docer.conservazione.File[] tempFiles = new it.kdm.docer.conservazione.File[files.length];
			
			for(int i=0; i<files.length; i++) {
				java.io.File tempFile = new java.io.File(tempDir, files[i].getName());
				FileUtils.copyInputStreamToFile(files[i].getHandler().getInputStream(),
						tempFile);
				it.kdm.docer.conservazione.File file = new it.kdm.docer.conservazione.File();
				file.setFile(tempFile);
				file.setId(files[i].getId());
				tempFiles[i] = file;
			}
			
			IConservazioneBL conservazione = (IConservazioneBL)this.ctx.getBean("businessLogic-verifica");
			
			return conservazione.versamento(doc, tempFiles, tipoDocumento, applicazioneChiamante,
					TipoConservazione.valueOf(tipoConservazione),
					forzaAccettazione, forzaConservazione, forzaCollegamento,
					ente, aoo);
			
			
		} catch (Exception e) {
			throw new ConservazioneException(e);
		}
	}
	
}
