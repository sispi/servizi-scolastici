/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.kdm.docer.fascicolazione;

import it.kdm.docer.commons.configuration.ConfigurationUtils;
import it.kdm.docer.commons.docerservice.LoginMode;
import it.kdm.docer.sdk.classes.KeyValuePair;
import it.kdm.docer.sdk.interfaces.ILoggedUserInfo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;
/**
 *
 * @author stefano.vigna
 */
public class ProviderExceptionTest implements IProvider{
    private ILoggedUserInfo currentUser;
    
    public String login(String username, String password, String codiceEnte) {
        return "ticketProviderExceptionTest";
    }

    public void logout(String token) throws FascicolazioneException {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public String fascicola(String datiFascicolo) throws FascicolazioneException {
        throw new UnsupportedOperationException("Chiamato provider per fascicolazione. (eccezione generata per test)");
    }

    
    public void setCurrentUser(it.kdm.docer.sdk.interfaces.ILoggedUserInfo info) throws FascicolazioneException {
        this.currentUser = info;
    }

    public String creaFascicolo(it.kdm.docer.sdk.classes.KeyValuePair[] metadati) throws FascicolazioneException {
                 try {
            /* formato risposta:
            <esito>
                <codice>0 OK|1 WARNING</codice>
                <descrizione></descrizione>
                <esito_fascicolo>
                    <COD_ENTE/> 
                    <COD_AOO/> 
                    <CLASSIFICA/> 
                    <ANNO_FASCICOLO/> 
                    <PROGR_FASCICOLO/> 
                </esito_fascicolo>
            </esito>
            */
            
            File response = ConfigurationUtils.loadResourceFile("providerResponse.xml");
            String xml = new String(Files.readAllBytes(response.toPath()));
            
            return xml;
            
            //throw new UnsupportedOperationException("Not supported yet.");
        } catch (IOException ex) {
            org.slf4j.LoggerFactory.getLogger(Provider.class.getName()).error(ex.getMessage(), ex);
            throw new FascicolazioneException(ex);
        }
    }

    public String forzaNuovoFascicolo(KeyValuePair[] metadati) throws FascicolazioneException {
                try {
            /* formato risposta:
            <esito>
                <codice>0 OK|1 WARNING</codice>
                <descrizione></descrizione>
                <esito_fascicolo>
                    <COD_ENTE/> 
                    <COD_AOO/> 
                    <CLASSIFICA/> 
                    <ANNO_FASCICOLO/> 
                    <PROGR_FASCICOLO/> 
                </esito_fascicolo>
            </esito>
            */
            
            File response = ConfigurationUtils.loadResourceFile("providerResponse.xml");
            String xml = new String(Files.readAllBytes(response.toPath()));
            
            return xml;
            
            //throw new UnsupportedOperationException("Not supported yet.");
        } catch (IOException ex) {
            org.slf4j.LoggerFactory.getLogger(Provider.class.getName()).error(ex.getMessage(), ex);
            throw new FascicolazioneException(ex);
        }
    }
    
      public boolean updateACLFascicolo(String token, KeyValuePair[] fascicoloid, KeyValuePair[] acl) throws FascicolazioneException {
        return true;
    }

    public String updateFascicolo(String token, KeyValuePair[] fascicoloid, KeyValuePair[] metadati) throws FascicolazioneException {
        
        for(KeyValuePair pair:fascicoloid){
            if (pair.getKey().equals("PROGR_FASCICOLO"))
                return pair.getValue();
        }
        return "PROGR_FASCICOLO_NON_TROVATO_IN_FASCICOLO_ID";
        
    }


    public LoginMode loginMode() {
        return LoginMode.STANDARD;
    }
}
