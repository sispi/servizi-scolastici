package it.kdm.docer.commons.configuration;

import it.kdm.docer.commons.Config;

import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;

public class Configuration {

    private Config c = null;

    /*public Configuration(String ente) throws IOException, XMLStreamException {

		c = new Config();
		if (StringUtils.isNotEmpty(ente)) {
			rebase(ente);
			File configFile = new File(c.getConfigFile().getParentFile(), ente.toUpperCase() + "_" + c.getConfigFile().getName());
			if (!configFile.exists()) {
			FileUtils.copyFile(c.getConfigFile(), configFile);
			}

			c.setConfigFile(configFile);
			c.loadConfig();
		}

    }*/

    public Configuration(String ente, String configurationFile) throws IOException, XMLStreamException {

		if (ente == null) {
			throw new IOException("impossibile individuare il file di configurazione: l'ente specificato e' null");
		}

		File configFile = ConfigurationUtils.getFile(ente,configurationFile);

		c = new Config(configFile);

		/*if (!configurationFile.exists()) {
			throw new IOException("File di configurazione non trovato: " + configurationFile.getAbsolutePath());
		}

		c = new Config(configurationFile);

		if (StringUtils.isNotEmpty(ente)) {
			File configFile = new File(c.getConfigFile().getParentFile(), ente.toUpperCase() + "_" + c.getConfigFile().getName());
			if (!configFile.exists()) {
			FileUtils.copyFile(c.getConfigFile(), configFile);
			}

			c.setConfigFile(configFile);
			c.loadConfig();
		}*/

    }

	/*private void rebase(String ente) throws IOException, XMLStreamException {
		File configurationFile;File origFile;
		configurationFile = c.getConfigFile();

		if (configurationFile instanceof zkFile)
            origFile = new File( ((zkFile)configurationFile).getZkPath());
        else
            origFile = configurationFile;

		String newPath = new File(origFile.getParentFile(), ente.toUpperCase() + "_" + origFile.getName()).getPath();

		File configFile = ConfigurationUtils.getFile(newPath);

		if (configFile.exists()){
            c.setConfigFile(configFile);
            c.loadConfig();
        }
	}*/

	/*public boolean writeConfig(String xml) throws IOException, XMLStreamException {
    	throw new NotImplementedException();
		c.writeConfig(xml);

		c.loadConfig();

		return true;
    }*/

    public String readConfig() throws XMLStreamException, IOException {
		return c.readConfig();
    }

    public Config getConfig() {
		return c;
    }

}
