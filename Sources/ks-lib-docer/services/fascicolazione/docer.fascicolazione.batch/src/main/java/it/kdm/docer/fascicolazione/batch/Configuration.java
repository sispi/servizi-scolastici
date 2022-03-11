package it.kdm.docer.fascicolazione.batch;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@SuppressWarnings("serial")
public class Configuration extends Properties {

    private static Configuration INSTANCE;

	public static void init(String configFilePath) {
		synchronized (Configuration.class) {
			if (INSTANCE == null) {
				INSTANCE = new Configuration(configFilePath);
			}
		}
	}

	public static Configuration getInstance() {
		return INSTANCE;
	}

	private Configuration(String configFilePath) {
        InputStream in = null;
        try {
            if (configFilePath == null)
                in = Configuration.class.getResourceAsStream("/batch.properties");
            else {
                in = new FileInputStream(configFilePath);
            }
            load(in);
        } catch (Exception e) {
            System.out.println("Cannot load any configuration file: " + e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }
	}

}
