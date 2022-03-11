package keysuite.desktop;

import com.google.common.base.Strings;
import it.kdm.orchestratore.utils.ResourceUtils;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.*;

public class DesktopResourceLoader extends DefaultResourceLoader implements ResourceLoader {

    public static class DesktopResource extends AbstractResource {

        InputStream in;
        String description;

        DesktopResource(String description, InputStream in){
            this.description = description;
            this.in = in;
        }

        @Override
        public String getDescription() {
            return description;
        }

        @Override
        public boolean exists(){
            return in!=null;
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return in;
        }
    }

    private static final Logger logger = LoggerFactory.getLogger(DesktopResourceLoader.class);

    public DesktopResourceLoader(){
        /*String val = System.getProperty(ResourceUtils.KEYSUITE_CONFIG);
        if (!Strings.isNullOrEmpty(val))
            return;
        File dev = new File("src/main").getAbsoluteFile();
        File local = new File("config").getAbsoluteFile();
        if (local.exists())
            System.setProperty(ResourceUtils.KEYSUITE_CONFIG, local.toString());
        if (dev.exists())
            System.setProperty(ResourceUtils.KEYSUITE_CONFIG, dev.toString());
        else
            System.setProperty(ResourceUtils.KEYSUITE_CONFIG, ResourceUtils.getConfigHome() );*/
    }

    public void clearCache(){

    }

    /**
     * Restituisce la risorsa cercando nel seguente ordine:<br>
     *     1) sul file system per un file specifico di AOO nel folder (resources root)/resources.(AOO)<br>
     *     2) sul file system per un file nel folder (resources root)<br>
     *     3) nelle risorse incluse
     *
     * @param location è il path della risorsa
     * @return restituisce la risorsa richiesta
     */
    @Override
    public Resource getResource(String location) {

        if (location.startsWith("URL [file:")){
            location = location.substring("URL [file:".length());
            location = location.substring(0,location.length()-1);
            return new FileSystemResource(location);
        }

        if (location.startsWith("file [")){
            location = location.substring("file [".length());
            location = location.substring(0,location.length()-1);
            return new FileSystemResource(location);
        }

        if (location.startsWith("file:")){
            location = location.substring("file:".length());
            return new FileSystemResource(location);
        }

        if (location.contains(":"))
            return super.getResource(location);

        InputStream in = ResourceUtils.getResourceNoExc(location);
        if (in == null)
            return super.getResource(location);
        Resource res = new DesktopResource(location,in);
        return res;
    }

    @Override
    public ClassLoader getClassLoader() {
        return ResourceUtils.class.getClassLoader();
    }

}
