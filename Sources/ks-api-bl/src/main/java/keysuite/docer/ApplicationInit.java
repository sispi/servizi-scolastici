package keysuite.docer;

import it.kdm.doctoolkit.services.SOLRClient;
import it.kdm.doctoolkit.zookeeper.ApplicationProperties;
import keysuite.swagger.client.SwaggerClient;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class ApplicationInit implements EnvironmentAware {



    @Override
    public void setEnvironment(Environment environment) {
        ApplicationProperties.setEnv(environment);

        System.setProperty( SOLRClient.ZK_HOST , environment.getProperty("spring.solr.zkHost","") );
        System.setProperty( "docer.url" , environment.getProperty("docer.url","") );

        SwaggerClient.READ_TIMEOUT = environment.getProperty("server.swagger.timeout",Integer.class,10000);
    }


}
