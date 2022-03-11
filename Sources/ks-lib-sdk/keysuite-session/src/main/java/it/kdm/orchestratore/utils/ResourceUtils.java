package it.kdm.orchestratore.utils;

import com.google.common.base.Strings;
//import freemarker.cache.MultiTemplateLoader;
//import freemarker.cache.TemplateLoader;
//import it.kdm.doctoolkit.utils.Utils;
import it.kdm.orchestratore.session.Session;
import keysuite.desktop.exceptions.Code;
import keysuite.desktop.exceptions.KSRuntimeException;
import keysuite.docer.client.ClientUtils;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.info.BuildProperties;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.ReflectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.lang.reflect.Field;
import java.util.*;

public class ResourceUtils {

    static class FileInputStream2 extends FileInputStream{
        protected File file;
        FileInputStream2(File file) throws FileNotFoundException {
            super(file);
            this.file = file;
        }
        public File getFile(){
            return this.file;
        }
    }

    final static String RESOURCES = "resources";
    public final static String KEYSUITE_CONFIG = "KEYSUITE_CONFIG";

    private static final Logger logger = LoggerFactory.getLogger(ResourceUtils.class);

    //public final static String resVer = ""+Math.abs((KDMUtils.getWarVersion()+"."+ KDMUtils.getWarTimestamp()).hashCode());

    /*public static String getResVer(){
        return resVer;
    }*/

    public static String getConfigHome(){
        //return System.getProperty(KEYSUITE_CONFIG, "./config");
        return ClientUtils.getConfigHome();
    }

    public static String getResourceRoot(String sub){
        String path;
        if (Strings.isNullOrEmpty(sub) || sub.equals(RESOURCES)) {
            path = System.getProperty(RESOURCES,getConfigHome()+"/"+RESOURCES);
        } else if (sub.startsWith(RESOURCES)) {
            path = System.getProperty(sub);
        } else {
            path = System.getProperty(RESOURCES+"."+sub);
        }
        if (path!=null && path.startsWith("./")) {
            String def = System.getProperty(RESOURCES,getConfigHome()+"/"+RESOURCES);
            path = new File(new File(def).getParent(), path.substring(1)).toString();
        }
        return path;

    }
    
    public static String getDomain(){
        //prendo il primo perchè è il piu specifico
        String key = getResourcePaths().keySet().iterator().next();
        if (RESOURCES.equals(key))
            return "default";
        else
            return key.substring(RESOURCES.length()+1);
    }

    public static Map<String,String> getResourceRoots(){
        Map<String,String> roots = new LinkedHashMap<>();

        for( String key : System.getProperties().stringPropertyNames() ){
            if (RESOURCES.equals(key) || key.startsWith(RESOURCES+"."))
                roots.put(key,System.getProperty(key));
        }
        return roots;
    }

    public static Map<String,String> getResourcePaths(){

        HttpServletRequest request = Session.getRequest();

        Map<String,String> roots = new LinkedHashMap<>();

        if (request!=null) {

            String serverName = Session.getServerName(request);

            if (serverName.matches(".*[a-zA-Z].*")){

                while(true){
                    String rsx = getResourceRoot(serverName);
                    if (rsx!=null)
                        roots.put(RESOURCES + "." + serverName, rsx);
                    int idx = serverName.indexOf(".");
                    if (idx==-1)
                        break;
                    serverName = serverName.substring(idx+1);
                }
            }
        }

        roots.put(RESOURCES,getResourceRoot(null));

        return roots;
    }

    public static File getResourceFile(InputStream in){

        //long timeStamp = 0;
        if (in instanceof FileInputStream2){
            /*try {
                Field f = FileInputStream.class.getDeclaredField("path");
                ReflectionUtils.makeAccessible(f);
                String tpath = (String) f.get(in);
                //timeStamp = new File(tpath).lastModified();
                File file = new File(tpath);
                if (file.exists())
                    return file;
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }*/
            return ((FileInputStream2)in).getFile();
        }
        return null;
        //return timeStamp;
    }

    public static InputStream getResourceNoExc(String resource){
        try{
            return getResourceAsStream(resource);
        } catch (FileNotFoundException fnf){
            return null;
        }
    }

    public static InputStream getResourceAsStream(HttpServletRequest request, String ente, String aoo, String resource) throws FileNotFoundException {
        return getResourceAsStream(resource);
    }

    public static boolean deleteResource(String resource) {

        if (resource.contains(":classpath"))
            throw new KSRuntimeException(Code.H400, "Non puoi eliminare nelle risorse");

        try {
            File file = getResourceFile(resource);

            if (file!=null && file.exists()){
                FileUtils.deleteQuietly(file);
                return true;
            }

        } catch (FileNotFoundException e) {
        }

        return false;
    }

    public static boolean updateResource(String resource, InputStream stream) throws IOException {

        if (resource.contains(":classpath")){
            throw new KSRuntimeException(Code.H400, "Non puoi salvare nelle risorse.");
        }

        File file = null;
        try {
            file = getResourceFile(resource);
        } catch (FileNotFoundException e) {
        }

        if (file==null)
            throw new KSRuntimeException(Code.H404, "Risorsa non trovata");

        boolean create = !file.exists();

        FileUtils.copyInputStreamToFile(stream,file);

        return create;
    }

    public static List<String> getResources(String path){
        Set results = new HashSet<>();

        if (path==null)
            return new ArrayList(results);

        if (path.startsWith("/"))
            path = path.substring(1);

        if (path.startsWith(":resources")){
            int idx = path.indexOf("/");
            File dir;
            if (idx==-1) {
                dir = new File( getResourceRoot(path.substring(1)) );
            } else {
                dir = new File( getResourceRoot(path.substring(1,idx)), path.substring(idx+1));
            }

            if (dir.exists() && dir.isDirectory()) {
                File[] content = dir.listFiles();

                if (content!=null){
                    for (File f : content) {
                        if (f.getName().equals("META-INF"))
                            continue;
                        results.add(f.getName());
                    }
                }
            } else {
                return null;
            }
        } else if (path.startsWith(":classpath")){
            ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

            int idx = path.indexOf("/");
            if (idx==-1) {
                path = "";
            } else {
                path = path.substring(idx+1);
            }

            Resource[] resources;
            try {
                if (path.endsWith("/"))
                    path = path.substring(0,path.length()-1);
                resources = resolver.getResources("classpath*:"+path+"/*");

                if (path.equals("")){
                    results.add("static");
                    results.add("templates");
                }

                if (path.equals("static")){
                    results.add("js");
                    results.add("css");
                    results.add("fonts");
                    results.add("images");
                    results.add("templates");
                    results.add("vendor");
                }

                for (Resource resource : resources) {
                    String desc = resource.getURL().toString();
                    if (!desc.contains("/desktop"))
                        continue;
                    String fn = resource.getFilename();

                    if (fn.contains(".") || !path.equals("")){
                        results.add(fn);
                    }
                }

            } catch (IOException e) {
                return null;
            }
        } else if (path.equals("")){


            for( String key : System.getProperties().stringPropertyNames() ){
                if (RESOURCES.equals(key) || key.startsWith(RESOURCES+"."))
                    results.add(":"+key);
            }
            results.add(":resources");
            results.add(":classpath");
        } else {

            List<String> cr = getResources(":classpath/"+path);

            boolean found = false;

            if (cr!=null) {
                results.addAll(cr);
                found = true;
            }

            Collection<String> roots = getResourcePaths().values();

            for ( String root : roots ){
                File dir = new File(root, path);

                if (dir.exists() && dir.isDirectory()){
                    found = true;
                    File[] content = dir.listFiles();

                    for (File f : content) {
                        if (f.getName().equals("META-INF"))
                            continue;
                        results.add(f.getName());
                    }
                }
            }

            if (!found)
                return null;
        }

        List<String> res = new ArrayList(results);
        Collections.sort(res);

        return res;
    }

    public static String getResourceETag(String resource) throws FileNotFoundException{
        InputStream stream = getResourceNoExc(resource);
        return getResourceETag(resource,stream);
    }

    static Long builtTime = null;

    public static String getResourceETag(String resource,InputStream stream) throws FileNotFoundException{
        if (builtTime==null){
            BuildProperties bp = (BuildProperties) Session.getBean("buildProperties");
            builtTime = bp.getTime().getEpochSecond();
        }
        File f=null;
        if (stream!=null){
            f = getResourceFile(stream);
        }
        long time = f!=null? f.lastModified() : builtTime;

        return ""+time+resource.hashCode();
    }


    public static File getResourceFile(String resource) throws FileNotFoundException {
        if (resource==null)
            return null;

        if (resource.startsWith("/"))
            resource = resource.substring(1);

        if (resource.startsWith(":resources")){
            int idx = resource.indexOf("/");
            String parent;
            if (idx==-1) {
                throw new FileNotFoundException("not found:"+resource);
            } else {
                parent = getResourceRoot(resource.substring(1,idx));
                resource = resource.substring(idx+1);
            }

            File file = new File(parent, resource);

            return file;
            /*if (file.exists())
                return file;
            else
                return null;*/

        } else if (resource.startsWith(":classpath")){
            return null;
        }

        List<String> roots = new ArrayList<>();

        if ("system.properties".equals(resource)){
            roots.add(getResourceRoot(null));
        } else {
            roots.addAll(getResourcePaths().values());
        }

        for ( String root : roots ){
            File file = new File(root, resource);

            if (file.exists())
                return file;
        }

        return null;
    }

    public static String getAbsolutePath(String resource) throws FileNotFoundException {
        if (resource.startsWith("/"))
            resource = resource.substring(1);
        if (resource.startsWith(":"))
            return resource;

        Map<String,String> roots = getResourcePaths();
        File file = getResourceFile(resource);
        if (file==null)
            return "/:classpath/"+resource;

        String root = null;
        for ( String key : roots.keySet() ) {
            File parent = new File(roots.get(key));
            if (file.getAbsolutePath().startsWith(parent.getAbsolutePath())) {
                root = key;
                break;
            }
        }
        if (root==null)
            throw new FileNotFoundException("not found:"+resource);

        return "/:"+root+"/"+resource;
    }

    public static InputStream getResourceAsStream(String resource) throws FileNotFoundException{

        if (resource==null)
            throw new FileNotFoundException("not found:"+resource);

        if (resource.startsWith("/"))
            resource = resource.substring(1);

        File file = getResourceFile(resource);

        if (file!=null && file.isDirectory())
            throw new FileNotFoundException("not found:"+resource);

        if (file!=null && file.exists()){
            return new FileInputStream2(file);
        }

        if (resource.startsWith(":classpath")){
            int idx = resource.indexOf("/");
            if (idx==-1) {
                throw new FileNotFoundException("not found:"+resource);
            } else {
                resource = resource.substring(idx+1);
            }
        }

        InputStream initialStream = ResourceUtils.class.getClassLoader().getResourceAsStream(resource);

        if (initialStream==null)
            throw new FileNotFoundException("not found:"+resource);

        HttpServletRequest request = Session.getRequest();

        if (request!=null && "true".equals(request.getParameter("export"))){

            File resFile = new File(getResourceRoot(null), resource);

            if (!resFile.exists()){
                try{
                    FileUtils.copyInputStreamToFile(initialStream, resFile);
                    initialStream = new FileInputStream2(resFile);
                } catch (Exception io){
                    throw new RuntimeException();
                }
            }
        }

        return initialStream;
    }
}
