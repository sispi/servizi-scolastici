/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.kdm.docer.protocollazione;

import it.kdm.docer.commons.configuration.ConfigurationUtils;
import it.kdm.docer.sdk.interfaces.ILoggedUserInfo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.stream.XMLStreamException;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.impl.llom.util.AXIOMUtil;
import org.apache.axiom.om.xpath.AXIOMXPath;
import org.apache.commons.io.IOUtils;
import org.jaxen.JaxenException;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;



/**
 *
 * @author lorenzo
 */
public class ProviderPG implements IProvider {
    
    private ILoggedUserInfo currentUser;
    private static int COUNTER = 1;
    
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
            
            File response = ConfigurationUtils.loadResourceFile("providerDataTest.xml");
            
            String xml = new String(Files.readAllBytes(response.toPath()));
            OMElement doc = AXIOMUtil.stringToOM(xml);
            
            AXIOMXPath xpath = new AXIOMXPath("//DATA_PG"); 
            OMElement elem = (OMElement)xpath.selectSingleNode(doc);
            elem.setText(ISODateTimeFormat.dateTime().print(new DateTime()));
            
            xpath = new AXIOMXPath("//NUM_PG");
            elem = (OMElement)xpath.selectSingleNode(doc);
            elem.setText(Integer.toString(COUNTER++));
            
            xpath = new AXIOMXPath("//ANNO_PG");
            elem = (OMElement)xpath.selectSingleNode(doc);
            elem.setText("2012");
            
            xpath = new AXIOMXPath("//REGISTRO_PG");
            elem = (OMElement)xpath.selectSingleNode(doc);
            elem.setText("PG");
            
            OMElement datiProt = AXIOMUtil.stringToOM(datiProtocollo);
            xpath = new AXIOMXPath("//Intestazione/Oggetto");
            elem = (OMElement)xpath.selectSingleNode(datiProt);
            String oggetto = elem.getText();
            
            xpath = new AXIOMXPath("//OGGETTO_PG");
            elem = (OMElement)xpath.selectSingleNode(doc);
            elem.setText(oggetto);
            
            return doc.toString();
            
            //throw new UnsupportedOperationException("Not supported yet.");
        } catch (IOException ex) {
            org.slf4j.LoggerFactory.getLogger(ProviderPG.class.getName()).error(ex.getMessage(), ex);
            throw new ProtocollazioneException(ex);
        } catch (JaxenException ex) {
            org.slf4j.LoggerFactory.getLogger(ProviderPG.class.getName()).error(ex.getMessage(), ex);
            throw new ProtocollazioneException(ex);
        } catch (XMLStreamException ex) {
            org.slf4j.LoggerFactory.getLogger(ProviderPG.class.getName()).error(ex.getMessage(), ex);
            throw new ProtocollazioneException(ex);
        }
    }

    public void setCurrentUser(ILoggedUserInfo info) throws ProtocollazioneException {
        this.currentUser = info;
    }
    
    
}
