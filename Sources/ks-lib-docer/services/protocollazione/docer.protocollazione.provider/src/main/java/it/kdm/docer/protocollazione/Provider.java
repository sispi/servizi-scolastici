/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.kdm.docer.protocollazione;

import it.kdm.docer.commons.configuration.ConfigurationUtils;
import it.kdm.docer.commons.configuration.Configurations;
import it.kdm.docer.registrazione.database.Registrazioni;
import it.kdm.docer.registrazione.database.Registro;
import it.kdm.docer.sdk.interfaces.ILoggedUserInfo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.util.AXIOMUtil;
import org.apache.axiom.om.xpath.AXIOMXPath;
import org.apache.commons.io.IOUtils;
import org.jaxen.JaxenException;
import org.joda.time.format.ISODateTimeFormat;
import org.springframework.context.support.ClassPathXmlApplicationContext;


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

    public String login(String username, String password, String library) {
        return "ticketProviderTest";
    }

    public void logout(String token) throws ProtocollazioneException {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public String protocolla(String datiProtocollo) throws ProtocollazioneException {
        try {
            /* formato risposta:
            <esito>
                <codice>0 OK|1 WARNING</codice>
                <descrizione></descrizione>
                <esito_protocollo>
                    <NUM_PG/> <!-- è il numero di protocollo assegnato al documentoo -->
                    <DATA_PG/> <!-- è la data di protocollo assegnata al documento -->
                    <ANNO_PG/> <!-- è l’anno di protocollo assegnato al documento -->
                    <OGGETTO_PG/> <!-- è l’oggetto del protocollo assegnato al documento -->
                    <REGISTRO_PG/> <!-- è il registro del protocollo assegnato al documento -->
                </esito_protocollo>
            </esito>
            */

            OMElement datiReg = AXIOMUtil.stringToOM(datiProtocollo);

            String oggetto = xpath(datiReg, "//Intestazione/Oggetto");
            String tipoRichiesta = xpath(datiReg, "//Intestazione/Flusso/TipoRichiesta");
            String docId = xpath(datiReg, "//Documenti/Documento/@id");
            String ente = xpath(datiReg, "//Documenti/Documento/Metadati/Parametro[@nome='COD_ENTE']/@valore");
            String aoo = xpath(datiReg, "//Documenti/Documento/Metadati/Parametro[@nome='COD_AOO']/@valore");

            String xpathProvider = String.format("//provider[@ente='%s' and @aoo='%s']", ente, aoo);

            String attribute = CONFIGURATIONS.getConfiguration(ente).getConfig()
                    .getNode(xpathProvider).getAttributeValue(new QName("perpetuo"));

            boolean perpetuo = Boolean.parseBoolean(attribute);

            Registro registro = new Registro();
            registro.setEnte(ente);
            registro.setAoo(aoo);
            registro.setRegistro("PROTOCOLLO_PG");
            registro.setDocId(docId);
            registro.setOggetto(oggetto);

            registro.setUser(currentUser.getUserId());
            registro.setTipoRichiesta(tipoRichiesta.charAt(0));

            registro = registrazioni.registra(registro, perpetuo);
            
            File response = ConfigurationUtils.loadResourceFile("providerDataTest.xml");
            
            String xml = new String(Files.readAllBytes(response.toPath()));
            OMElement doc = AXIOMUtil.stringToOM(xml);
            
            AXIOMXPath xpath = new AXIOMXPath("//DATA_PG"); 
            OMElement elem = (OMElement)xpath.selectSingleNode(doc);
            elem.setText(ISODateTimeFormat.dateTime().print(
                    registro.getData().getTimeInMillis()));
            
            xpath = new AXIOMXPath("//NUM_PG");
            elem = (OMElement)xpath.selectSingleNode(doc);
            elem.setText(Long.toString(registro.getNumero()));
            
            xpath = new AXIOMXPath("//ANNO_PG");
            elem = (OMElement)xpath.selectSingleNode(doc);
            elem.setText(Integer.toString(registro.getAnno()));
            
            xpath = new AXIOMXPath("//REGISTRO_PG");
            elem = (OMElement)xpath.selectSingleNode(doc);
            elem.setText(registro.getRegistro());
            
            xpath = new AXIOMXPath("//OGGETTO_PG");
            elem = (OMElement)xpath.selectSingleNode(doc);
            elem.setText(registro.getOggetto());
            
            return doc.toString();
            
            //throw new UnsupportedOperationException("Not supported yet.");
        } catch (IOException ex) {
            org.slf4j.LoggerFactory.getLogger(Provider.class.getName()).error(ex.getMessage(), ex);
            throw new ProtocollazioneException(ex);
        } catch (JaxenException ex) {
            org.slf4j.LoggerFactory.getLogger(Provider.class.getName()).error(ex.getMessage(), ex);
            throw new ProtocollazioneException(ex);
        } catch (XMLStreamException ex) {
            org.slf4j.LoggerFactory.getLogger(Provider.class.getName()).error(ex.getMessage(), ex);
            throw new ProtocollazioneException(ex);
        }
    }

    private String xpath(OMElement xml, String xpath) throws JaxenException {
        AXIOMXPath xpathExpr = new AXIOMXPath(xpath);
        return xpathExpr.stringValueOf(xml);
    }

    public void setCurrentUser(ILoggedUserInfo info) throws ProtocollazioneException {
        this.currentUser = info;
    }
    
    
}
