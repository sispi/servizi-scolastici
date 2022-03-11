package it.kdm.docer.commons.configuration;

import org.apache.commons.lang.NotImplementedException;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;

public class Configurations {

    //private Map<String, Configuration> configurations = new HashMap<String, Configuration>();

    private String configurationFile = "configuration.xml";

    //private String configurationPropertiesName = "configuration.properties";
    //private String configurationFilePropName = "config.path";

    public void setConfigurationFile(String configurationFile) {
        this.configurationFile = configurationFile;
        //this.configurationFile = configurationFile.getPath();
        //this.configurationPropertiesName = null;
        //this.configurationFilePropName = null;
    }

    /*public void setConfigurationProperties(String configurationPropertiesName, String configurationFilePropName) {
        this.configurationPropertiesName = configurationPropertiesName;
        this.configurationFilePropName = configurationFilePropName;
        this.configurationFile = null;
    }*/

    public Configuration getConfiguration(String ente) throws IOException, XMLStreamException {

        /*if (ente == null) {
            throw new IOException("impossibile individuare il file di configurazione: l'ente specificato e' null");
        }*/

        return new Configuration(ente, this.configurationFile );

        /*Configuration configuration = configurations.get(ente);

        if (configuration == null) {
            configuration = new Configuration(ente, getConfigurationFile());
            configurations.put(ente, configuration);
        }

        return configuration;*/
    }

    public void writeConfig(String ente, String xml) throws IOException, XMLStreamException {

        throw new NotImplementedException();
        /*if (StringUtils.isNotEmpty(xml)) {
            Configuration configuration = getConfiguration(ente);
            configuration.writeConfig(xml);
        }*/
    }

    public String readConfig(String ente) throws IOException, XMLStreamException {

        Configuration configuration = getConfiguration(ente);
        return configuration.readConfig();

    }

    /*private File getConfigurationFile() throws IOException {

        if (configurationPropertiesName != null) {
          
            try {

                Properties props = ConfigurationUtils.loadProperties(configurationFilePropName);

                // alcune configurazioni leggono da uno altre dall'altro
                String configurationFileName = props.getProperty(configurationFilePropName);

                if (configurationFileName == null) {
                    throw new IOException("Property " + configurationFilePropName + " non trovata nel file " + configurationPropertiesName);
                }

                File configFile = ConfigurationUtils.getFile(configurationFileName);

                if (!configFile.exists()) {
                    throw new IOException("File di configurazione non trovato: " + configFile.getAbsolutePath());
                }

                this.configurationFile = configFile;

            } catch (ConfigurationLoadingException e) {   
            	throw new IOException(e);
            }

        }

        return this.configurationFile;

    }*/

}
