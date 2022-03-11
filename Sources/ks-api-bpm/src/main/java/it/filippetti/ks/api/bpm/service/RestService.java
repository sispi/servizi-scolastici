/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.service;

import java.util.Map;
import keysuite.swagger.client.SwaggerClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 
 * @author marco.mazzocchetti
 */
@Service
public class RestService {
    
    private static final Logger log = LoggerFactory.getLogger(RestService.class);

    private SwaggerClient swaggerClient;

    public RestService() {
        swaggerClient = new SwaggerClient();
    }
    
    /**
     * 
     * @param parameters
     * @return
     */
    public Map<String, Object> execute(Map<String, Object> parameters) {
        return swaggerClient.executeByMap(null, parameters);
    }
}
