package it.kdm.docer.firma.utils;

import org.apache.commons.configuration.ConfigurationConverter;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import it.kdm.docer.commons.configuration.ConfigurationUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * Created by Łukasz Kwasek on 23/06/15.
 */
public class ConfigUtils {

    public static final String OTP_CERTH_PATH = "otpCerthPath";
    public static final String PK_BOX_SECUR_PIN = "pkBoxSecurPin";
    public static final String INPUT_DIRECTORY = "provider.directory.input";
    public static final String OUTPUT_DIRECTORY = "provider.directory.output";
    public static final String ACESS_PERMISSIONS = "accessPermissions";
    public static final String NO_RESTRICT_ACCESS="0";

    private PropertiesConfiguration conf;
    private File otpCerthFile;
    private File pkBoxSecurPinFile;
    private File inputDirectory;
    private File outputDirectory;
    private String accessPermissions;

    public ConfigUtils() throws ConfigurationException, IOException {
    	
    	File config = ConfigurationUtils.loadResourceFile("provider-config.properties");
        conf = new PropertiesConfiguration(config);        
        otpCerthFile = file(conf.getString(OTP_CERTH_PATH));
        pkBoxSecurPinFile = file(conf.getString(PK_BOX_SECUR_PIN));
        inputDirectory = dir(conf.getString(INPUT_DIRECTORY));
        outputDirectory = dir(conf.getString(OUTPUT_DIRECTORY));
        accessPermissions=conf.getString(ACESS_PERMISSIONS,NO_RESTRICT_ACCESS);
    }

    private File file(String path) throws IOException {

        File ret;

        if (StringUtils.isNotBlank(path)) {
            InputStream is = this.getClass().getResourceAsStream(path);

            if (is != null) {
                ret = File.createTempFile("config_", ".wsfirma");
                FileUtils.copyInputStreamToFile(is, ret);
            } else {
                ret = new File(path);
            }
        } else {
            throw new IllegalArgumentException("Non è stato passato un path valido");
        }

        if (!ret.exists()) {
            throw new IllegalArgumentException(String.format("%s non è un path valido", path));
        }

        return ret;
    }

    private File dir(String path) throws IOException {

        if (StringUtils.isNotBlank(path)) {
            File ret = new File(path);

            if (!ret.exists()) {
                ret.mkdirs();
            }

            return ret;
        } else {
            throw new IllegalArgumentException("Non è stato passato un path valido");
        }

    }

    public Map<String, String> map() {

        Map<String, String> map = ConfigurationConverter.getMap(conf);
        map.put(OTP_CERTH_PATH, otpCerthFile.getAbsolutePath());
        map.put(PK_BOX_SECUR_PIN, pkBoxSecurPinFile.getAbsolutePath());
        map.put(INPUT_DIRECTORY, inputDirectory.getAbsolutePath());
        map.put(OUTPUT_DIRECTORY, outputDirectory.getAbsolutePath());
        map.put(ACESS_PERMISSIONS, accessPermissions);

        return map;
    }

    public File getOtpCerthFile() {
        return otpCerthFile;
    }

    public File getPkBoxSecurPinFile() {
        return pkBoxSecurPinFile;
    }

    public File getInputDirectory() {
        return inputDirectory;
    }

    public File getOutputDirectory() {
        return outputDirectory;
    }

    public String getAccessPermissions() {
        return accessPermissions;
    }

    public void setAccessPermissions(String accessPermissions) {
        this.accessPermissions = accessPermissions;
    }
}
