/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.rpc.docer.configuration;

import it.filippetti.ks.rpc.docer.service.DocerActionServiceImpl;
import keysuite.remoting.DocerActionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.httpinvoker.HttpInvokerServiceExporter;

/**
 *
 * @author marco.mazzocchetti
 */
@Configuration
public class ServiceExporterConfiguration {
    
        @Bean(name = "/") 
        HttpInvokerServiceExporter service() {
            HttpInvokerServiceExporter exporter = new HttpInvokerServiceExporter();
            exporter.setService(new DocerActionServiceImpl());
            exporter.setServiceInterface(DocerActionService.class);
            return exporter;
    }    
}
