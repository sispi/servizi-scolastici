/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.kdm.docer.registrazione;

import it.kdm.docer.commons.configuration.ConfigurationUtils;
import it.kdm.docer.commons.configuration.Configurations;
import it.kdm.docer.registrazione.database.Registrazioni;
import it.kdm.docer.registrazione.database.Registro;
import it.kdm.docer.sdk.interfaces.ILoggedUserInfo;
import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.util.AXIOMUtil;
import org.apache.axiom.om.xpath.AXIOMXPath;
import org.apache.commons.io.IOUtils;
import org.jaxen.JaxenException;
import org.joda.time.format.ISODateTimeFormat;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lorenzo
 */
public class Provider implements IProvider {

	private static Registrazioni registrazioni;

    private static Configurations CONFIGURATIONS = new Configurations();

    static {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("classpath*:META-INF/database/applicationContext.xml");
        registrazioni = ctx.getBean(Registrazioni.class);
    }

    private ILoggedUserInfo currentUser;

    public Provider() {
    	//ctx.close();
    }
	
	public String login(String username, String password, String codiceEnte) {
        return "ticketProviderTest";
    }

    public void logout(String token) throws RegistrazioneException {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public String registra(String registroId, String datiRegistrazione) throws RegistrazioneException {
        try {
            /* formato risposta:
            <esito>
              <codice>0 OK</codice>
              <descrizione></descrizione>
              <dati_registro>
                  <DataRegistrazione/>
                  <NumeroRegistrazione/>
                  <OggettoRegistrazione/>
                  <IDRegistrazione/>
              </dati_registro>
            </esito>
            */
      
        	File response = ConfigurationUtils.loadResourceFile("providerResponse.xml");           
            String xml = new String(Files.readAllBytes(response.toPath()));
            OMElement doc = AXIOMUtil.stringToOM(xml);
            
            OMElement datiReg = AXIOMUtil.stringToOM(datiRegistrazione);
           
			String oggetto = xpath(datiReg, "//Intestazione/Oggetto");
			String docId = xpath(datiReg, "//Documenti/Documento/@id");
			String ente = xpath(datiReg, "//Documenti/Documento/Metadati/Parametro[@nome='COD_ENTE']/@valore");
			String aoo = xpath(datiReg, "//Documenti/Documento/Metadati/Parametro[@nome='COD_AOO']/@valore");

            String xpathProvider = String.format("//provider[@ente='%s' and @aoo='%s' and @registro='%s']", ente, aoo, registroId);

            String attribute = CONFIGURATIONS.getConfiguration(ente).getConfig()
                    .getNode(xpathProvider).getAttributeValue(new QName("perpetuo"));

            boolean perpetuo = Boolean.parseBoolean(attribute);

			Registro registro = new Registro();
			registro.setEnte(ente);
			registro.setAoo(aoo);
			registro.setRegistro(registroId);
			registro.setDocId(docId);
			registro.setOggetto(oggetto);

            registro.setTipoRichiesta('R');
            registro.setUser(currentUser.getUserId());

            registro = registrazioni.registra(registro, perpetuo);
			
            AXIOMXPath xpath = new AXIOMXPath("//OggettoRegistrazione");
            OMElement elem = (OMElement)xpath.selectSingleNode(doc);
            elem.setText(registro.getOggetto());
            
            xpath = new AXIOMXPath("//DataRegistrazione"); 
            elem = (OMElement)xpath.selectSingleNode(doc);
            elem.setText(ISODateTimeFormat.dateTime().print(
            				registro.getData().getTimeInMillis()));
            
            xpath = new AXIOMXPath("//NumeroRegistrazione");
            elem = (OMElement)xpath.selectSingleNode(doc);
            elem.setText(Long.toString(registro.getNumero()));
            
            xpath = new AXIOMXPath("//IDRegistro");
            elem = (OMElement)xpath.selectSingleNode(doc);
            elem.setText(registro.getRegistro());
            
            return doc.toString();
            
            //throw new UnsupportedOperationException("Not supported yet.");
        } catch (IOException ex) {
            org.slf4j.LoggerFactory.getLogger(Provider.class.getName()).error(ex.getMessage(), ex);
            throw new RegistrazioneException(ex);
        } catch (XMLStreamException ex) {
            org.slf4j.LoggerFactory.getLogger(Provider.class.getName()).error(ex.getMessage(), ex);
            throw new RegistrazioneException(ex);
        } catch (JaxenException ex) {
            org.slf4j.LoggerFactory.getLogger(Provider.class.getName()).error(ex.getMessage(), ex);
            throw new RegistrazioneException(ex);
        }
    }

	private String xpath(OMElement xml, String xpath) throws JaxenException {
		AXIOMXPath xpathExpr = new AXIOMXPath(xpath); 
		return xpathExpr.stringValueOf(xml);
	}
    
    public void setCurrentUser(it.kdm.docer.sdk.interfaces.ILoggedUserInfo info) throws RegistrazioneException {
        currentUser = info;
    }
    
    
}
