package keysuite.docer.controllers;

import it.kdm.orchestratore.utils.ResourceUtils;
import keysuite.docer.client.FileServiceCommon;
import keysuite.solr.SolrSimpleClient;
import keysuite.swagger.client.SwaggerClient;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Map;

@RestController
public class OptionsController {

    @Autowired
    Environment env;

    @RequestMapping(value="/", method={RequestMethod.OPTIONS})
    public void OPTIONS() {
    }

    @RequestMapping(value="/", method={RequestMethod.GET})
    public void index(HttpServletRequest request, HttpServletResponse response) throws IOException {

        InputStream is = ResourceUtils.getResourceAsStream("static/index.html");

        String body = IOUtils.toString(is, Charset.defaultCharset());

        String sp = "";

        sp += "<br/>spring.profiles.active="+ env.getProperty("spring.profiles.active");
        sp += "<br/>server.port="+ env.getProperty("server.port");
        sp += "<br/>zkHost="+System.getProperty( SolrSimpleClient.ZK_HOST );
        sp += "<br/>docer.url="+System.getProperty("docer.url","http://localhost:8080");
        sp += "<br/>KEYSUITE_CONFIG="+System.getProperty("KEYSUITE_CONFIG");
        sp += "<br/>DOCER_CONFIG="+System.getProperty("DOCER_CONFIG");
        sp += "<br/>client.timeout="+ SwaggerClient.READ_TIMEOUT;
        sp += "<br/>retryPolicy="+ env.getProperty("retryPolicy");

        Map<String,String> stores = FileServiceCommon.getAllStorePaths();

        for( String key : stores.keySet() ){
            sp += "<br/>" + key+"="+ stores.get(key) ;
        }

        body = body.replace("${SP}", sp);

        response.setContentType("text/html");

        Writer writer = response.getWriter();
        writer.write(body);
        writer.flush();
        writer.close();



        //StreamUtils.copy(is,response.getOutputStream());
    }




}
