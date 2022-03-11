/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.payment.utilities;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.filippetti.ks.api.payment.exception.ApplicationException;
import it.filippetti.ks.api.payment.model.AuthenticationContext;
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
public class MicroserviceUtil {
    
    private static final Logger log = LoggerFactory.getLogger(MicroserviceUtil.class);
    
    public static Object callToMicroservice(AuthenticationContext context, String baseUrl, String url, HttpMethod httpMethod) throws ApplicationException{
        log.info("Call to microservice...");
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("AUTHORIZATION", context.getJwtToken());
        HttpEntity<?> request = new HttpEntity(String.class, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<?> responseEntity = restTemplate.exchange(baseUrl + url, httpMethod, request, Object.class);
        return responseEntity.getBody();
    }
    
    public static Object callToMicroservice(AuthenticationContext context, String baseUrl, String url, String httpMethod, Object data) throws ApplicationException, URISyntaxException, IOException, InterruptedException{
        log.info("Call to microservice...");
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(data);
        HttpRequest httpRequest = HttpRequest.newBuilder()
            .uri(new URI(baseUrl + url))
            .header("Content-Type", "application/json").header("AUTHORIZATION", context.getJwtToken())
            .method(httpMethod, HttpRequest.BodyPublishers.ofString(json)).build();
        HttpResponse response = HttpClient.newHttpClient().send(httpRequest, HttpResponse.BodyHandlers.discarding());
        return response;
    }
    
}
