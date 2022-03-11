package keysuite.docer;

import it.kdm.docer.commons.configuration.ResourceCache;
//import it.kdm.doctoolkit.services.SOLRClient;
//import it.kdm.doctoolkit.zookeeper.ApplicationProperties;
//import it.kdm.orchestratore.session.Session;
import keysuite.SessionUtils;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import keysuite.cache.ClientCache;
import keysuite.cache.ClientCacheAuthUtils;
import org.apache.commons.io.FileUtils;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@ComponentScan(basePackages = { "it.kdm.docer.ws","keysuite.docer" })
public class ApplicationInit implements EnvironmentAware {

    protected final static Logger logger = LoggerFactory.getLogger(ApplicationInit.class);

    private void checkResourceFile(File file){
        if (!file.exists()) {
            try {
                FileUtils.copyInputStreamToFile(this.getClass().getClassLoader().getResourceAsStream("docer/"+file.getName()), file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            logger.warn(file.toString() + " copied from resource");
        }
    }

    @Override
    public void setEnvironment(Environment environment) {

        /*String KEYSUITE_CONFIG = ClientUtils.getConfigHome();
        String DOCER_CONFIG = new File(KEYSUITE_CONFIG,"resources").getAbsolutePath();

        System.setProperty("DOCER_CONFIG", DOCER_CONFIG);
        System.setProperty("KEYSUITE_CONFIG", KEYSUITE_CONFIG);*/

        String check = ResourceCache.configURI;
        //assert DOCER_CONFIG.equals(check);

        //String autosync = environment.getProperty("docer.autosync");
        //if (autosync!=null)
        //    System.setProperty( "docer.autosync" ,autosync );

        //Session.setSelfAttachOn();

        //ApplicationProperties.setEnv(environment);

        CloudSolrClient solrClient = new CloudSolrClient.Builder((List) null).withZkHost(Arrays.asList(System.getProperty("zkHost").split(","))).build();

        ClientCache.setQueryHelper( (String q, String fl) -> {

            if (solrClient.getDefaultCollection()==null){
                String collection = solrClient.getZkStateReader().getClusterState().getCollectionsMap().keySet().iterator().next();
                solrClient.setDefaultCollection(collection);
            }

            ModifiableSolrParams params = new ModifiableSolrParams().set("ticket","admin");

            if (fl!=null)
                params.set("fl",fl);

            params.set("rows",10000);

            try {
                if (q.matches("^[^\\s]+@[^\\s]+$")) {
                    Map doc = solrClient.getById(q, params);
                    List<Map> results = new ArrayList<>();
                    if (doc != null)
                        results.add(doc);
                    return results;
                } else {
                    QueryResponse response = solrClient.query(params.set("q", q));
                    return (List) response.getResults();
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        ClientCacheAuthUtils.setThreadBearerResolver( () -> {
            return SessionUtils.getRequest().getHeader("Authorization");
        });

        //logger.info("admin token:\r\neyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImF1ZCI6IkFPT19URVNUIiwiaWF0IjoxNTk5NDk5MDQzLCJleHAiOjE3MDA2MDI2NDN9.Dwg4HCZ_r-sT_QWPwGPk14o_m2r5Dx4HJBKisH6qIDvRs5MhDX82CgzIjne1aaWkFIDjH0mY1ExGNlUIIym1sQ");
    }

    /*@Bean
    @RequestScope
    IWSDocer wsDocer() throws Exception{
        return new WSDocerService(env);
    }*/




}
