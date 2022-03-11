package keysuite.docer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.solr.SolrAutoConfiguration;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class,
        SolrAutoConfiguration.class,
        ThymeleafAutoConfiguration.class,
        ErrorMvcAutoConfiguration.class
})
@EnableZuulProxy
public class BLApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(BLApplication.class, args);
    }

    //TODO gestire prefix per utenti e gruppi
    //TODO verifica idempotenza dei vari metodi
    //TODO aggancio JMS per log
    //TODO ActorsCache in lazy loading non becca gli utenti appena creati
}
