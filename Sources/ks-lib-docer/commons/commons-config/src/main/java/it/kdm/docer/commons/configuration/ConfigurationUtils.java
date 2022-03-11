package it.kdm.docer.commons.configuration;

import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.util.Properties;

public final class ConfigurationUtils {

    private final static Logger LOG	= LoggerFactory.getLogger(ConfigurationUtils.class);

    public static ResourceCache.ResourceFile getFile(String ente, String path){

        // cerco con ente
        if (Strings.isNotEmpty(ente)){
            ResourceCache.ResourceFile file = ResourceCache.get(ente.toUpperCase() + "/" + path);
            if (file.exists()){
                //LOG.info(String.format("[GETCONFIGFILE] file di configurazione %s trovato su zookeeper path %s", path, pathConEnte));
                return file;
            }
        }

        // cerco senza ente
        return ResourceCache.get(path);
    }

    public static ResourceCache.ResourceFile loadResourceFile(String path) throws ConfigurationLoadingException {

        try{
            ResourceCache.ResourceFile resourceFile = getFile(null,path);

            if (!resourceFile.exists())
                throw new FileNotFoundException(path);

            return resourceFile;
        } catch ( Exception e ){
            throw new ConfigurationLoadingException(String.format("Impossibile caricare il file di configurazione %s", path), e);
        }
    }
	
	public static Properties loadProperties(String propsFileName) throws ConfigurationLoadingException {
        ResourceCache.ResourceFile confFile = loadResourceFile(propsFileName);
        return confFile.getProp();
	}







}
