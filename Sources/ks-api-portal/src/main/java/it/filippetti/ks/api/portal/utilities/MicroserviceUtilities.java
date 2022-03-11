/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.portal.utilities;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.filippetti.ks.api.portal.exception.ApplicationException;
import it.filippetti.ks.api.portal.model.AuthenticationContext;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author dino
 */
public class MicroserviceUtilities {
    
    private static final Logger log = LoggerFactory.getLogger(MicroserviceUtilities.class);
    
    
    public static Object callToMicroservice(AuthenticationContext context, String baseUrl, String url, HttpMethod httpMethod) throws ApplicationException{
        log.info("Call to microservice 1...");
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("AUTHORIZATION", context.getJwtToken());
        HttpEntity<?> request = new HttpEntity(String.class, headers);
        RestTemplate restTemplate = new RestTemplate();
        
        String u = baseUrl + url;
        log.info("Call to microservice 1 URL {}", u);
        
        ResponseEntity<?> responseEntity = restTemplate.exchange(u, httpMethod, request, Object.class);
        
        log.info("Call to microservice 1 Body {}", responseEntity.getBody());
        
        return responseEntity.getBody();
    }
    
    
    
    public static Object callToMicroservice(AuthenticationContext context, String baseUrl, String url, String httpMethod, Object data, String adminToken) throws ApplicationException{
        log.info("Call to microservice 2...");
        try {
            
            String token = adminToken != null ? adminToken : context.getJwtToken();
            
            String u = baseUrl + url;
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(data);
            HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(new URI(u))
                .header("Content-Type", "application/json").header("AUTHORIZATION", token)
                .method(httpMethod, HttpRequest.BodyPublishers.ofString(json)).build();
            
            log.info("Call to microservice 2 URL {}", u);
            
            HttpResponse response = HttpClient.newHttpClient().send(httpRequest, HttpResponse.BodyHandlers.discarding());
            
            log.info("Call to microservice 2 Body {} with code {}", response.body(), response.statusCode());
            
            return response.body();
        } catch(IOException | InterruptedException | URISyntaxException e){
            log.info("Call to microservice 2 error {}", e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    
    public static Object callToMicroservice(AuthenticationContext context, String baseUrl, String url, String httpMethod, Object data) throws ApplicationException{
        log.info("Call to microservice 3...");
        return callToMicroservice(context, baseUrl, url, httpMethod, data, null);
        
    }
    
}
