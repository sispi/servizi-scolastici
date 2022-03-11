package keysuite.docer.utils.qrCodeUtils;

import it.kdm.doctoolkit.zookeeper.ApplicationProperties;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import keysuite.docer.BLApplication;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrSubstitutor;

public class DataUtils extends HashMap {
    private String primaryPropertyRoot = "keysuiteQr.";
    private String propertyRoot = "keysuiteQr.";
    private String folder ="";

    private final Boolean test;
    private final List<String> testFiles = new ArrayList<>();
    private final ScapigliaturaUtils scapigliaturaUtils;

    public DataUtils(String tipologia) {
        if(StringUtils.isNotEmpty(tipologia)) {
            propertyRoot += tipologia + ".";
        }
        folder  = getProperty("folder") + '/' + tipologia + "/";
        test = Boolean.parseBoolean(getProperty("debug"));
        put(propertyRoot + "url", getProperty("url"));
        scapigliaturaUtils = new ScapigliaturaUtils(this);
    }

    public String generate(String resourceName) throws IOException {
        if(resourceName.equals("body") || resourceName.equals("scapigliatura")) resourceName += ".html";
        else if(resourceName.equals("qrcode_1") || resourceName.equals("qrcode_2") || resourceName.equals("content")) resourceName += ".txt";
        else throw new IOException(resourceName + " is not a valid resource");

        InputStream fis;
        try {
            File file = new File(folder+resourceName);
            if(file.exists()){
                fis = new FileInputStream(file);
            }else{
                fis = BLApplication.class.getResourceAsStream("/COPIA_ANALOGICA/DEFAULT/"+resourceName);
            }

        }catch (FileNotFoundException f){
            throw new FileNotFoundException("Could not find the file to generate: " + folder + resourceName);
        }
        String tmp = IOUtils.toString(fis, StandardCharsets.UTF_8.name());
        fis.close();
        tmp = new StrSubstitutor(this).replace(tmp);

//        if(test){
//            FileWriter fw;
//            try{
//                fw = new FileWriter(folder + "test_" + resourceName);
//            }catch (IOException e){
//                throw new IOException("Could not create: " + folder + resourceName);
//            }
//            fw.write(tmp);
//            fw.close();
//            testFiles.add(folder + "test_" + resourceName);
//        }
        return tmp;
    }

//    public void put(String name, Object value){
//        data.put(name, value);
//    }

    public String getProperty(String property){
       return getProperty(propertyRoot + property, primaryPropertyRoot + property);
    }
    public String getProperty(String property,String defaultValue){
        if(!property.startsWith(propertyRoot))
            return ApplicationProperties.get(propertyRoot + property, defaultValue);
        else{
            return ApplicationProperties.get(property, defaultValue);
        }
    }
    public void deleteTests(){
        for(String testFile : testFiles) new File(testFile).delete();
    }
    public ScapigliaturaUtils getScapigliaturaUtils(){
        return scapigliaturaUtils;
    }
}
