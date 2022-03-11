package keysuite.desktop.components;

import org.springframework.stereotype.Component;

@Component
public class SessionInitializator {

    //@Autowired
    //private Environment env;

    /*@Bean
    @RequestScope
    UserInfo getUserInfo(){
        return Session.getUserInfoNoExc();
    }*/

    /*@Bean("webClient")
    @RequestScope
    public WebClient getWebClient(){
        //String blUrl = env.getProperty("bl-server.url", ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString()+"/") ;
        return DesktopUtils.buildWebClient();
    }*/

    /*@Bean("docerClient")
    public APIClient getDocerClient(){

        String apiUrl = DesktopUtils.buildRequestUrl("/docer/v1/");

        APIClient APIClient = ((keysuite.docer.sdk.APIClient)
                APIFactory.getClient(apiUrl))
                .setTimeout(20000)
                .bearer( () -> Session.getUserInfoNoExc().getJwtToken() )
                .setClientRetries(500L,1500L)
                .setServerRetries(500L,1500L);
        return APIClient;
    }*/

    /*@Bean("bpmClient")
    @RequestScope
    public WebClient getBpmClient(){
        String bpmUrl = env.getProperty("bpm-server.url", ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString()+"/" );
        return DesktopUtils.buildWebClient(bpmUrl);
    }*/
}
