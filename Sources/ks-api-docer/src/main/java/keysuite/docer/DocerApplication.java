package keysuite.docer;

//import it.kdm.doctoolkit.services.SOLRClient;
import java.io.File;
import java.util.Map;
import keysuite.docer.client.ClientUtils;
import keysuite.docer.client.FileServiceCommon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.solr.SolrAutoConfiguration;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class,
        SolrAutoConfiguration.class,
        ThymeleafAutoConfiguration.class,
        ErrorMvcAutoConfiguration.class
})
@EnableConfigurationProperties
public class DocerApplication {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RED = "\u001B[31m";

    /*@Bean
    public ErrorPageRegistrar errorPageRegistrar() {
        return new ErrorPageRegistrar() {
            @Override
            public void registerErrorPages(ErrorPageRegistry registry) {
                ErrorPage errorPage = new ErrorPage("/error");
                registry.addErrorPages(errorPage);
            }
        };
    }*/

    protected final static Logger logger = LoggerFactory.getLogger(DocerApplication.class);

    public static void main(String[] args) {

        System.setProperty("nashorn.args","--no-deprecation-warning=true --language=es6");
        System.setProperty("java.net.preferIPv4Stack","true");
        /*String envHome = System.getenv("DOCER_CONFIG");
        String sysHome = System.getProperty("DOCER_CONFIG");

        if (envHome!=null && sysHome==null)
            System.setProperty("DOCER_CONFIG",envHome);*/

        String KEYSUITE_CONFIG = ClientUtils.getConfigHome();
        String DOCER_CONFIG = new File(KEYSUITE_CONFIG,"resources").getAbsolutePath();

        System.setProperty("DOCER_CONFIG", DOCER_CONFIG);
        System.setProperty("KEYSUITE_CONFIG", KEYSUITE_CONFIG);

        ConfigurableApplicationContext context = SpringApplication.run(DocerApplication.class, args);

        //System.out.println("---"+context.getEnvironment().getProperty("testmessage","EMPTY-TEST-MESSAGE")+"---");
        System.out.println(ANSI_GREEN);
        System.out.println("spring.profiles.active="+ context.getEnvironment().getProperty("spring.profiles.active"));
        System.out.println("server.port="+ context.getEnvironment().getProperty("server.port"));
        System.out.println("zkHost="+System.getProperty( "zkHost" ));
        System.out.println("docer.url="+System.getProperty("docer.url","http://localhost:8080"));
        System.out.println("KEYSUITE_CONFIG="+System.getProperty("KEYSUITE_CONFIG"));
        System.out.println("DOCER_CONFIG="+System.getProperty("DOCER_CONFIG"));
        //System.out.println("client.timeout="+ SwaggerClient.READ_TIMEOUT);
        System.out.println("retryPolicy="+ context.getEnvironment().getProperty("retryPolicy"));

        Map<String,String> stores = FileServiceCommon.getAllStorePaths();

        for( String key : stores.keySet() ){
            System.out.println(key+"="+ stores.get(key));
        }

        //System.out.println("JWT realm mapping:"+ System.getProperty("keycloakFilter","disabled"));

        System.out.println(ANSI_RESET);

        /*SampleJmsMessageSender sender = context.getBean(SampleJmsMessageSender.class);

        sender.sendTraceMessage(new TraceMessage("service1","method1"));
        sender.sendTraceMessage(new TraceMessage("service1","method2"));*/
    }

    //TODO login noDocer con verifica password su solr
    //TODO verifica idempotenza dei vari metodi
    //TODO ActorsCache in lazy loading non becca gli utenti appena creati
}
