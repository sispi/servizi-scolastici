package keysuite.desktop.zuul;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import keysuite.desktop.components.DesktopResourceWatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.filters.discovery.DiscoveryClientRouteLocator;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class ZuulRegistration implements EnvironmentAware {

    public static final String prefix = "zuul.routes.";

    @Autowired
    DiscoveryClientRouteLocator discoveryClientRouteLocator;

    @Autowired
    DesktopResourceWatcher desktopResourceWatcher;

    @Autowired
    Environment environment;

    public void reload(){

        ZuulProperties properties;

        Field f = null;
        try {
            f = discoveryClientRouteLocator.getClass().getDeclaredField("properties");
            f.setAccessible(true);
            properties = (ZuulProperties) f.get(discoveryClientRouteLocator);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        properties.getRoutes().clear();

        //da caricare anche da header.json

        Collection<String> routes = desktopResourceWatcher.getPropertyNames(prefix+"*.url");

        for( String route : routes ){
            String name = route.split("\\.")[2];
            String url = environment.getProperty(prefix+name+".url");
            String path = environment.getProperty(prefix+name+".path");
            Boolean strip = environment.getProperty(prefix+name+".stripPrefix",Boolean.class,true);
            Boolean retryable = environment.getProperty(prefix+name+".retryable",Boolean.class,false);

            ZuulProperties.ZuulRoute zuulRoute = new ZuulProperties.ZuulRoute();
            zuulRoute.setId(name);
            zuulRoute.setServiceId(name);
            zuulRoute.setPath(path);
            zuulRoute.setUrl(url);
            zuulRoute.setStripPrefix(strip);
            zuulRoute.setRetryable(retryable);

            discoveryClientRouteLocator.addRoute(zuulRoute);
        }

        //Map<String,Map> apps = ConfigAppBean.getApps();

        Set<String> ignoredPatterns = environment.getProperty("zuul.ignoredPatterns",Set.class);

        if(ignoredPatterns==null)
            ignoredPatterns = new HashSet<>();

        //Set<String> ignoredPatterns = new HashSet<String>(Arrays.asList(fixed.split(",")));

        /*for ( Map app : apps.values() ){
            if (app.containsKey("context"))
                ignoredPatterns.add("/"+app.get("context")+"/**");
        }*/

        properties.setIgnoredPatterns(ignoredPatterns);

        discoveryClientRouteLocator.refresh();
    }

    @Override
    public void setEnvironment(Environment environment) {
        //reload();
    }
}
