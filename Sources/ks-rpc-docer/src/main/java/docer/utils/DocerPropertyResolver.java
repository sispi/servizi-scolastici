package docer.utils;

import it.kdm.doctoolkit.utils.Utils;
import java.io.File;
import java.util.Properties;
import org.springframework.core.env.PropertyResolver;

public class DocerPropertyResolver implements PropertyResolver {

    Properties props;

    public DocerPropertyResolver(){
        props = new Properties();

        String url = System.getProperty("docer.url");
        if (!url.startsWith("/"))
            url += "/";

        props.setProperty("mail.attachDirEmailPec",System.getProperty("tempfiles.upload" , new File(Utils.getConfigHome() , "upload" ).getAbsolutePath()));
        props.setProperty("url.download.file", url + "files/upload" );

        props.setProperty("documentale.docManager.server.host",url);
        props.setProperty("sede.DOCAREA",url);
        props.setProperty("documentale.auth.ws.endpoint","docersystem/services/AuthenticationService.AuthenticationServiceHttpSoap12Endpoint");
        props.setProperty("documentale.docer.ws.endpoint","WSDocer/services/DocerServices.DocerServicesHttpSoap12Endpoint");
        props.setProperty("documentale.prot.ws.endpoint","WSProtocollazione/services/WSProtocollazione.WSProtocollazioneHttpSoap12Endpoint");
        props.setProperty("documentale.fasc.ws.endpoint","WSFascicolazione/services/WSFascicolazione.WSFascicolazioneHttpSoap12Endpoint");
        props.setProperty("documentale.regis.ws.endpoint","WSRegistrazione/services/WSRegistrazione.WSRegistrazioneHttpSoap12Endpoint");


    }

    @Override
    public boolean containsProperty(String key) {
        return getProperty(key)!=null;
    }

    @Override
    public String getProperty(String key) {
        return getProperty(key, (String) null);
    }

    @Override
    public String getProperty(String key, String defaultValue) {
        String val = props.getProperty(key, System.getProperty(key,defaultValue) );
        return val;
    }

    @Override
    public <T> T getProperty(String key, Class<T> targetType) {
        return null;
    }

    @Override
    public <T> T getProperty(String key, Class<T> targetType, T defaultValue) {
        return null;
    }

    @Override
    public String getRequiredProperty(String key) throws IllegalStateException {
        return null;
    }

    @Override
    public <T> T getRequiredProperty(String key, Class<T> targetType) throws IllegalStateException {
        return null;
    }

    @Override
    public String resolvePlaceholders(String text) {
        return null;
    }

    @Override
    public String resolveRequiredPlaceholders(String text) throws IllegalArgumentException {
        return null;
    }
}
