package keysuite.desktop.components;

import com.google.common.base.Strings;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.annotation.PostConstruct;

import it.kdm.orchestratore.utils.ResourceUtils;
import keysuite.desktop.DesktopResourceLoader;
import keysuite.desktop.DesktopUtils;
import keysuite.desktop.security.ConfigAppBean;
import keysuite.desktop.security.SecurityConfig;
import keysuite.desktop.zuul.ZuulRegistration;
import keysuite.docer.client.ClientUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.keycloak.adapters.springboot.KeycloakSpringBootProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.*;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.stereotype.Component;

@Component
public class DesktopResourceWatcher implements EnvironmentAware {

    private static final Logger logger = LoggerFactory.getLogger(DesktopResourceWatcher.class);

    ConfigurableEnvironment env;

    @Autowired
    DesktopResourceLoader resourceLoader;

    @Autowired
    ZuulRegistration zuulRegistration;

    @Autowired
    UrlRewriter urlRewriter;

    @Autowired
    SecurityConfig securityConfig;

    @Autowired
    ReloadableResourceBundleMessageSource messageSource;

    @Autowired(required = false)
    private KeycloakSpringBootProperties keycloakSpringBootProperties;

    @Override
    public void setEnvironment(Environment environment) {
        this.env = (ConfigurableEnvironment) environment;
    }

    @PostConstruct
    public void postConstruct() {
        configureWatcher();
    }

    public void configureWatcher() {
        try {
            final WatchService watchService = FileSystems.getDefault().newWatchService();
            logger.info("WatchService INITIATED");

            Executors.newSingleThreadExecutor().execute(() -> {
                try {
                    Path path = Paths.get(ResourceUtils.getConfigHome());

                    Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                        @Override
                        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                            dir.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);
                            logger.info("Registered dir {} for config changes", dir.toString());
                            return FileVisitResult.CONTINUE;
                        }
                    });

                    WatchKey watchKey;
                    initAll();
                    while ((watchKey = watchService.take()) != null) {
                        List<WatchEvent<?>> n = watchKey.pollEvents();
                        for( WatchEvent<?> x : n )
                            logger.warn(x.kind().toString()+":"+x.context().toString());
                        initAll();

                        watchKey.reset();
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            });

            Runtime.getRuntime().addShutdownHook(new Thread() {
                public void run() {
                    try {
                        watchService.close();
                        logger.info("WatchService CLOSE");
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                    }
                }
            });
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public void initAll(){
        try{
            ConfigAppBean.clearCache();
            //ApplicationProperties.clearCache();
            messageSource.clearCache();
            resourceLoader.clearCache();

            initPropertySources();

            zuulRegistration.reload();

            if (keycloakSpringBootProperties!=null) {
                initKeycloakClaimMappings();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    Collection<String> allKeys = new HashSet<>();
    Collection<String> spKeys = new HashSet<>();

    public Collection<String> getPropertyNames(String filter){
        if (filter!=null)
            filter = filter.replace(".","\\.").replace("**",".+").replace("*","[^\\.]+");
        Collection<String> keys = new HashSet<>();
        for( String key : allKeys ){
            if (!Strings.isNullOrEmpty(filter)){
                if (!key.matches(filter))
                    continue;
            }
            keys.add(key);
        }
        return keys;
    }

    public void initKeycloakClaimMappings() throws IOException {
        File f = new File(ClientUtils.getConfigHome(), "application-keycloak.yaml");
        if (!f.exists())
            return;

        String yaml = FileUtils.readFileToString(f, "utf-8");
        Map<String, Object> map = DesktopUtils.parseYAML(yaml);
        map = (Map) ((Map)map.get("keycloak")).get("config");
        Map config = keycloakSpringBootProperties.getConfig();
        if (map!=null && config!=null){
            config.clear();
            config.putAll(map);
        }
    }

    public void initPropertySources(){

        allKeys.clear();

        MutablePropertySources propSrcs = env.getPropertySources();

        List<PropertySource> files = StreamSupport.stream(propSrcs.spliterator(), false)
                .filter(ps -> ps instanceof ResourcePropertySource)
                .collect(Collectors.toList());

        for( PropertySource ps : files ){

            Resource res = resourceLoader.getResource(ps.getName());
            Properties properties = null;
            try {
                properties = new Properties();
                properties.load(res.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }

            //if ("system.properties".equals(ps.getName())){
            if (ps.getName().contains("system.properties")){
                for( String key : spKeys )
                    System.clearProperty(key);
                spKeys.clear();

                List<String> keys = new ArrayList(properties.stringPropertyNames());
                for( String key : keys ) {
                    System.setProperty(key, properties.getProperty(key));
                    spKeys.add(key);
                }

                securityConfig.getFilter().initPatterns();
            }

            try {
                Resource res2 = resourceLoader.getResource(ps.getName() );

                propSrcs.replace( ps.getName() , new ResourcePropertySource(res2) );
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        for( PropertySource ps : propSrcs ){
            if (ps instanceof MapPropertySource){
                List<String> keys = Arrays.asList(((MapPropertySource) ps).getPropertyNames());
                allKeys.addAll(keys);
            }
        }

        try{
            OrderedProperties props = new OrderedProperties();
            //props.load(ResourceUtils.getResourceAsStream("rewrite.properties"));
            FileSystemResource res1 = new FileSystemResource(System.getProperty("KEYSUITE_CONFIG")+"/rewrite.properties");
            FileSystemResource res2 = new FileSystemResource(System.getProperty("KEYSUITE_CONFIG")+"/resources/rewrite.properties");

            if (res1.exists())
                props.load(res1.getInputStream());
            else
                props.load(res2.getInputStream());

            List<String> keys = new ArrayList(props.stringPropertyNames());
            //Collections.reverse(keys);
            urlRewriter.clear();

            for( String regex : keys )
                urlRewriter.addRule(regex, props.getProperty(regex));

        } catch (IOException e){
            logger.error(e.getMessage(),e);
        }

        //ApplicationProperties.setEnv(env);
    }
}
