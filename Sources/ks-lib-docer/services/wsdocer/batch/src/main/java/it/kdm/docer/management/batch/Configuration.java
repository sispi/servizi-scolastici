package it.kdm.docer.management.batch;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: Vaio
 * Date: 29/10/15
 * Time: 14.30
 * To change this template use File | Settings | File Templates.
 */
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
                in = Configuration.class.getResourceAsStream("/docer_batch_cm.cfg");
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
