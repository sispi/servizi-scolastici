package it.kdm.docer.businesslogic.configuration;

import it.kdm.docer.commons.configuration.Configurations;
import it.kdm.docer.sdk.exceptions.DocerException;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ConfigurationManager extends Configurations {

    private Map<String, ConfigByEnte> map = new HashMap<String, ConfigByEnte>();

//    public void setConfigurationFile(File configurationFile){
//	super.setConfigurationFile(configurationFile);
//    } 

    public void writeConfig(String ente, String xml) throws IOException, XMLStreamException {
        super.writeConfig(ente, xml);
        ConfigByEnte blcbe = new ConfigByEnte();
        blcbe.initConfiguration(super.getConfiguration(ente).getConfig());
        map.put(ente, blcbe);
    }

//    public String readConfig(String ente) throws DocerException{
//	return super.readConfig(ente);
//    }

    public ConfigByEnte getConfig(String ente) throws DocerException {

        if (ente == null) {
            throw new DocerException("impossibile individuare il file di configurazione: l'ente specificato e' null");
        }

        ente = ente.toUpperCase();

        ConfigByEnte blcbe = map.get(ente);

        if (blcbe == null) {
            // leggo configurazione di default (quella non per ente)
            blcbe = new ConfigByEnte();
            try {
                blcbe.initConfiguration(super.getConfiguration(ente).getConfig());
            } catch (IOException e) {
                e.printStackTrace();
                throw new DocerException("ConfigByEnte.getConfig(" + ente + "): " + e.getMessage());
            } catch (XMLStreamException e) {
                e.printStackTrace();
                throw new DocerException("ConfigByEnte.getConfig(" + ente + "): " + e.getMessage());
            }
            map.put(ente, blcbe);
        }

        return blcbe;

    }

}
