package it.kdm.docer.fonte.batch;

import java.io.File;

public class DirectoryUtils {

    
    public static File createTempDirectory(String applicationName, File parentCacheDir) throws Exception{       
        
        File cacheDir = null;
        
        if(!parentCacheDir.getName().equals("DocerServicesFonte")){
        
            cacheDir = new File(parentCacheDir, "DocerServicesFonte");
            
            if(!cacheDir.exists()){
                cacheDir.mkdir();
            }
        }
        
        
        cacheDir = new File(cacheDir, applicationName);
        
        if(!cacheDir.exists()){
            cacheDir.mkdir();
        }
        
        return cacheDir;
    }
    
    public static File createSubDirectory(String directoryName, File parentDir) throws Exception{       
        
        File cacheDir = new File(parentDir, directoryName);
        
        if(!cacheDir.exists()){
            cacheDir.mkdir();
        }
        
        return cacheDir;
    }
}
