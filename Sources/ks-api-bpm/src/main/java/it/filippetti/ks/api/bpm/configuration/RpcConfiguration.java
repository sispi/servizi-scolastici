/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.configuration;

import keysuite.remoting.DocerActionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;

/**
 *
 * @author marco.mazzocchetti
 */
@Configuration
public class RpcConfiguration {
   
    @Value("${rpc.docerActionService.url}")
    private String docerActionServiceUrl;
        
    @Bean
    public HttpInvokerProxyFactoryBean docerActionService() {
        HttpInvokerProxyFactoryBean invoker = new HttpInvokerProxyFactoryBean();
        invoker.setServiceUrl(docerActionServiceUrl);
        invoker.setServiceInterface(DocerActionService.class);
        return invoker;    
    }
}
