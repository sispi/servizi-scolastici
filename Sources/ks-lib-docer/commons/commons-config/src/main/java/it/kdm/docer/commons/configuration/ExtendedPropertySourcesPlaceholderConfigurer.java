package it.kdm.docer.commons.configuration;

import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ContextResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.io.File;

public class ExtendedPropertySourcesPlaceholderConfigurer extends PropertySourcesPlaceholderConfigurer {

    private Resource resolve(Resource location){

        if (location instanceof ContextResource){
            String path = ((ContextResource)location).getPathWithinContext();
            if (path.startsWith("/zk://")){
                File f = ConfigurationUtils.getFile(null,path.substring(1));
                location = new FileSystemResource(f);
            }
        }

        return location;
    }

    @Override
    public void setLocations(Resource[] locations) {

        for(int i=0; i<locations.length; i++)
            locations[i] = resolve(locations[i]);

        super.setLocations(locations);
    }

    @Override
    public void setLocation(Resource location) {

        location = resolve(location);

        super.setLocation(location);
    }
}
