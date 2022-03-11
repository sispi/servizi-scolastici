/*
7 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.kdm.docer.fascicolazione;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import org.apache.commons.io.IOUtils;
import org.jaxen.JaxenException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import it.kdm.docer.commons.XMLUtil;
import it.kdm.docer.commons.configuration.ConfigurationUtils;
import it.kdm.docer.commons.configuration.Configurations;
import it.kdm.docer.registrazione.database.Registrazioni;
import it.kdm.docer.registrazione.database.Registro;
import it.kdm.docer.sdk.classes.KeyValuePair;
import it.kdm.docer.sdk.interfaces.ILoggedUserInfo;

/**
 * @author lorenzo
 */
public class Provider implements IProvider {

    private static Configurations CONFIGURATIONS = new Configurations();

    private static Registrazioni registrazioni;

    static {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("classpath*:META-INF/database/applicationContext.xml");
        registrazioni = ctx.getBean(Registrazioni.class);
    }

    private ILoggedUserInfo currentUser;

    public String login(String username, String password, String codiceEnte) {
        return "ticketProviderTest";
    }

    public void logout(String token) throws FascicolazioneException {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public String fascicola(String datiFascicolo) throws FascicolazioneException {
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
                    <PARENT_PROGR_FASCICOLO>
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

            String ente = null;
            String aoo = null;
            String classifica = null;
            
            String anno = null;
            
            Calendar calendar = Calendar.getInstance();  
            calendar.setTime(new Date());  
            String thisAnno = String.valueOf(calendar.get(Calendar.YEAR)); 
            
            String progressivoParent = null;
            String descrizione = null;

            for (KeyValuePair pair : metadati) {
                String key = pair.getKey();
                if (key.equals("COD_ENTE")) {
                    ente = pair.getValue();
                } else if (key.equals("COD_AOO")) {
                    aoo = pair.getValue();
                } else if (key.equals("ANNO_FASCICOLO")) {
                    anno = pair.getValue();
                } else if (key.equals("CLASSIFICA")) {
                    classifica = pair.getValue();
                } else if (key.equals("PARENT_PROGR_FASCICOLO")) {
                    progressivoParent = pair.getValue();
                } else if (key.equals("DES_FASCICOLO")) {
                    descrizione = pair.getValue();
                }
            }

            String xpathProvider = String.format("//provider[@ente='%s' and @aoo='%s']", ente, aoo);

            String attribute = CONFIGURATIONS.getConfiguration(ente).getConfig()
                    .getNode(xpathProvider).getAttributeValue(new QName("perpetuo"));

            boolean perpetuo = Boolean.parseBoolean(attribute);

            if(anno == null || anno.equals("")){
            	anno = thisAnno;
            }
            Registro registro = new Registro();
            registro.setEnte(ente);
            registro.setAoo(aoo);

            String idRegistro;
            if (progressivoParent != null && !progressivoParent.equals("")) {         
                idRegistro = String.format("%s/%s/%s", classifica, anno, progressivoParent);
            } else {
                idRegistro = String.format("%s/%s", classifica, anno);
            }

            registro.setRegistro(idRegistro);
            registro.setDocId(UUID.randomUUID().toString());
            registro.setOggetto(descrizione);

            registro.setUser(currentUser.getUserId());
            registro.setTipoRichiesta('F');

            registrazioni.registra(registro, perpetuo);

            File response = ConfigurationUtils.loadResourceFile("providerResponse.xml");
            String xml = new String(Files.readAllBytes(response.toPath()));

            XMLUtil dom = new XMLUtil(xml);

            dom.setNodeValue("//esito_fascicolo/COD_ENTE", registro.getEnte());
            dom.setNodeValue("//esito_fascicolo/COD_AOO", registro.getAoo());
            dom.setNodeValue("//esito_fascicolo/CLASSIFICA", classifica);
            dom.setNodeValue("//esito_fascicolo/DES_FASCICOLO", registro.getOggetto());
            dom.setNodeValue("//esito_fascicolo/ANNO_FASCICOLO", anno);

            StringBuilder progressivo = new StringBuilder();
            if (progressivoParent != null && !progressivoParent.equals("")) {
            	
            	dom.setNodeValue("//esito_fascicolo/PARENT_PROGR_FASCICOLO", progressivoParent);
                progressivo.append(progressivoParent);
                progressivo.append("/");                
                dom.setNodeValue("//esito_fascicolo/ANNO_FASCICOLO", anno);
            }
            progressivo.append(Long.toString(registro.getNumero()));
            dom.setNodeValue("//esito_fascicolo/PROGR_FASCICOLO", progressivo.toString());

            return dom.toXML();

            //throw new UnsupportedOperationException("Not supported yet.");
        } catch (IOException ex) {
            org.slf4j.LoggerFactory.getLogger(Provider.class.getName()).error(ex.getMessage(), ex);
            throw new FascicolazioneException(ex);
        } catch (XMLStreamException ex) {
            org.slf4j.LoggerFactory.getLogger(Provider.class.getName()).error(ex.getMessage(), ex);
            throw new FascicolazioneException(ex);
        } catch (JaxenException ex) {
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

            XMLUtil dom = new XMLUtil(xml);

            for (KeyValuePair metadata : metadati) {
                String xpath = String.format("//%s", metadata.getKey().toUpperCase());
                if (dom.getNode(xpath) != null) {
                    dom.setNodeValue(xpath, metadata.getValue());
                }
            }

            return dom.toXML();

            //throw new UnsupportedOperationException("Not supported yet.");
        } catch (IOException ex) {
            org.slf4j.LoggerFactory.getLogger(Provider.class.getName()).error(ex.getMessage(), ex);
            throw new FascicolazioneException(ex);
        } catch (XMLStreamException ex) {
            org.slf4j.LoggerFactory.getLogger(Provider.class.getName()).error(ex.getMessage(), ex);
            throw new FascicolazioneException(ex);
        } catch (JaxenException ex) {
            org.slf4j.LoggerFactory.getLogger(Provider.class.getName()).error(ex.getMessage(), ex);
            throw new FascicolazioneException(ex);
        }
    }

    public boolean updateACLFascicolo(String token, KeyValuePair[] fascicoloid, KeyValuePair[] acl) throws FascicolazioneException {
        return true;
    }

    public String updateFascicolo(String token, KeyValuePair[] fascicoloid, KeyValuePair[] metadati) throws FascicolazioneException {

        for (KeyValuePair pair : fascicoloid) {
            if (pair.getKey().equals("PROGR_FASCICOLO"))
                return pair.getValue();
        }
        return "PROGR_FASCICOLO_NON_TROVATO_IN_FASCICOLO_ID";

    }



}
