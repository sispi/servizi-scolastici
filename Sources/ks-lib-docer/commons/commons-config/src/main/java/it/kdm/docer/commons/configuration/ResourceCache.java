package it.kdm.docer.commons.configuration;

import org.apache.logging.log4j.util.Strings;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Properties;

public class ResourceCache {

    public static class ResourceProperties extends Properties {

        //long ts;
        //boolean isXML;
        ResourceCache.ResourceFile propFile;

        private ResourceProperties(ResourceCache.ResourceFile propFile) throws IOException {
            super();
            this.propFile = propFile;
            update();
        }

        private void update() throws IOException {
            clear();
            propFile = propFile.update();

            try ( InputStream in = new FileInputStream(propFile) ) {
                if (propFile.origPath.endsWith(".xml"))
                    loadFromXML(in);
                else
                    load(in);
            }
        }

        /*public boolean isExpired(){

            if (propFile instanceof zkFile){
                long maxAge = System.currentTimeMillis() - updateTimeInMinute;
                boolean isValid = (ts>maxAge && ts>startUp);
                return !isValid;
            } else
                return (propFile.lastModified() > ts);
        }*/

        @Override
        public String getProperty(String key){

            if (!propFile.isValid())
                try {
                    update();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            return super.getProperty(key);
        }

        @Override
        public synchronized int size(){

            if (!propFile.isValid())
                return -1;

            return super.size();
        }
    }

    public static class ResourceFile extends File {

        private ResourceType type;
        private String origPath;
        private long ts;
        private File fsFile;
        private Properties prop=null;

        public ResourceFile( ResourceType type, String path, String origPath){
            super(path);
            this.type = type;
            this.origPath = origPath;
            this.ts = System.currentTimeMillis();

            if(origPath.startsWith("zk:"))
                this.fsFile = getZkTempFile(origPath);
            else
                this.fsFile = new File(origPath);
        }

        /*public ResourceType getType() {
            return type;
        }*/

        public String getPropertyByKey(String key, String defaultValue){
            Properties prop = getProp();
            return prop.getProperty(key,defaultValue);
        }

        public Properties getProp(){
            if ( (origPath.endsWith(".xml") || origPath.endsWith(".properties")) &&
                    prop == null && this.exists() ){
                try {
                    prop = new ResourceProperties(this);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return prop;
        }

        public boolean isValid(){

            // se non esiste il file esterno allora la risorsa è sempre buona perchè non può mai cambiare
            if (ResourceType.CLASSPATH.equals(type))
                return !fsFile.exists();

            // per ZK consideriamo un tempo di scadenza
            if (ResourceType.ZOOKEEPER.equals(type))
                return (fsFile.exists() && (ts+updateTimeInMs) > fsFile.lastModified());

            // per il FS confrontiamo semplicemente con il ts
            if (ResourceType.FILESYSTEM.equals(type))
                return (fsFile.exists() && ts > fsFile.lastModified());

            //altri casi (INVALID)
            return false;

            /*if (origPath.startsWith("zk:")){
                if (fsFile.exists() && (ts+updateTimeInMs) > fsFile.lastModified())
                    return true;
            } else if (ResourceType.CLASSPATH.equals(type)){
                if (!fsFile.exists())
                    return true;
            } else {
                if (fsFile.exists())
                    return true;
            }
            return false;*/
        }

        public ResourceFile update(){
            if (isValid())
                return this;
            else {
                this.type = ResourceType.INVALID;
                return get(origPath);
            }
        }
    }

    private final static Logger LOG	= LoggerFactory.getLogger(ResourceCache.class);


    public static final String ZK_HOST = "zkHost";
    public static final String UPDATE_TIME_IN_MINUTE = "updateTimeInMs";
    public static final String CONFIG_URI = "DOCER_CONFIG";

    private static long updateTimeInMs;
    private static String zkHost;
    private static String configURI;

    static {
        updateTimeInMs = Long.parseLong(getGlobalProperty(UPDATE_TIME_IN_MINUTE,"5"))*60*1000;
        zkHost = getGlobalProperty(ZK_HOST,"127.0.0.1:9983");

        ClassLoader loader = ConfigurationUtils.class.getClassLoader();

        String webContext;

        try {
            Field f = loader.getClass().getDeclaredField("contextName");
            f.setAccessible(true);
            webContext = (String) f.get(loader);
        } catch (Exception e) {

            try{
                webContext = (String) loader.getClass().getMethod("getContextName").invoke(loader);
            } catch (Exception e2){
                webContext = "";
            }
        }

        if (Strings.isNotEmpty(webContext) && !webContext.startsWith("/"))
            webContext = "/" + webContext;

        configURI = getGlobalProperty(CONFIG_URI,"/opt/docer") + webContext;
    }

    public enum ResourceType {
        ZOOKEEPER,
        FILESYSTEM,
        CLASSPATH,
        INVALID
    }

    private static Object zkLock = new Object();
    private static ZooKeeper zoo;

    private static ZooKeeper getZkClient(){

        if (zoo==null || !zoo.getState().equals(ZooKeeper.States.CONNECTED)){
            synchronized (zkLock){
                ZKConnection zkConnection = new ZKConnection();
                try {

                    zoo = zkConnection.connect(zkHost);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return zoo;
    }

    public static String getGlobalProperty(String prop, String def){
        String value = System.getProperty(prop);
        if (value!=null)
            return value;
        value = System.getenv(prop);
        if (value!=null)
            return value;
        return def;
    }

    private static void updateZkTmp(File tmp , byte [] data) throws IOException {

        if (data!=null && data.length>0){
            try (FileOutputStream fos = new FileOutputStream(tmp)){
                fos.write(data);
            }
        } else if (tmp.exists()){
            if (tmp.length()>0) {
                tmp.delete();
                tmp.createNewFile();
            } else {
                tmp.setLastModified(System.currentTimeMillis());
            }
        } else {
            tmp.createNewFile();
        }
    }

    private static String normalize(String path){
        path = path.replaceAll("\\\\","/");
        if (path.startsWith("zk:"))
            path = path.substring(3);
        path = path.replaceAll("/+","/");
        return path;
    }

    private static File getZkTempFile(String path){

        path = normalize(path);

        String tmpDir = System.getProperty("java.io.tmpdir");

        File tmpFile = new File(tmpDir,"zk_"+path.hashCode()+".tmp");

        return tmpFile;
    }

    private static File getRxTempFile(String path){

        path = normalize(path);

        String tmpDir = System.getProperty("java.io.tmpdir");

        File tmpFile = new File(tmpDir,"rx_"+path.hashCode()+".tmp");

        return tmpFile;
    }

    private static File fillFromZk(String path) {

        path = normalize(path);

        final File tmpFile = getZkTempFile(path);

        ZooKeeper ZOOKEEPER = getZkClient();

        try {

            byte[] rawData = ZOOKEEPER.getData(path, new Watcher() {

                public void process(WatchedEvent we) {
                    try {
                        tmpFile.delete();
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }

            } , null );

            updateZkTmp(tmpFile,rawData);

            //return new zkFile(tmpFile.getPath(),path);

        } catch (Exception e ){
            if (e instanceof KeeperException &&  ((KeeperException)e).code().equals(KeeperException.Code.NONODE)) {

                try {
                    updateZkTmp(tmpFile,null);
                } catch (IOException e1) {
                    throw new RuntimeException(e1);
                }
                return new File(tmpFile.toString()+".notexists");

            } else {
                String errmsg = String.format("Impossibile caricare il file di configurazione %s da zookeeper.",
                        path);
                throw new RuntimeException(errmsg,e);
            }
        }

        return tmpFile;
    }

    public static ResourceFile get(String path){

        String resolvedPath = path;
        if (!path.contains(":") && !path.startsWith("/") && !path.startsWith("\\") )
            resolvedPath = new File(configURI,path).getPath();

        ResourceFile rFile;
        if (resolvedPath.startsWith("zk:")){

            File zkFile = fillFromZk(resolvedPath);

            rFile = new ResourceFile(ResourceType.ZOOKEEPER,zkFile.getPath(),path);

            if (zkFile.exists()){
                LOG.info(String.format("[GETCONFIGFILE] file di configurazione %s trovato su zookeeper", path, zkFile.getPath()));
                return rFile;
            }

        } else {

            File fsFile = new File(resolvedPath);

            rFile = new ResourceFile(ResourceType.FILESYSTEM,fsFile.getPath(),path);

            if (fsFile.exists()){
                LOG.info(String.format("[GETCONFIGFILE] file di configurazione %s letto da path %s", path,fsFile.getPath()));
                return rFile;
            }
        }

        if (path.startsWith(configURI))
            path = path.substring(configURI.length()+1);
        else if (path.contains(":"))
            path = new File(path).getName();

        URL resourceURL = ConfigurationUtils.class.getClassLoader().getResource(path);

        if (resourceURL != null) {
            try{

                String rPath;
                if ("vfs".equals(resourceURL.toURI().getScheme())){
                    //jboss
                    File targetFile = getRxTempFile(path);

                    if (!targetFile.exists()){

                        InputStream initialStream = ConfigurationUtils.class.getClassLoader().getResourceAsStream(path);

                        byte[] buffer = new byte[initialStream.available()];
                        initialStream.read(buffer);

                        OutputStream outStream = new FileOutputStream(targetFile);
                        outStream.write(buffer);
                        outStream.close();
                        targetFile.deleteOnExit();
                    }

                    rPath = targetFile.getPath();

                } else {
                    rPath = Paths.get(resourceURL.toURI()).toString();
                }

                rFile = new ResourceFile(ResourceType.CLASSPATH,rPath,path);
                LOG.info(String.format("[GETCONFIGFILE] file di configurazione %s letto da risorse %s", path,rPath));
            } catch (URISyntaxException | IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            LOG.info(String.format("[GETCONFIGFILE] file di configurazione %s non trovato in risorse", path));
        }

        return rFile;
    }
}
