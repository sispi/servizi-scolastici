/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.payment.configuration;

import com.google.common.collect.Lists;
import it.filippetti.ks.api.payment.dto.ErrorDTO;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

/**
 *
 * @author marco.mazzocchetti
 */
@Configuration
public class DefaultErrorConfiguration {
    
    private final static String PATH = "/error";

    @Bean
    public ErrorAttributes errorAttributes() {
        return new DefaultErrorAttributes() {
            @Override
            public Map<String, Object> getErrorAttributes(WebRequest webRequest, boolean includeStackTrace) {
                
                Map<String, Object> errorAttributes;
                Integer statusCode;
                HttpStatus status = null;
                Throwable exception;

                errorAttributes = new LinkedHashMap<>();
                statusCode = (Integer) webRequest.getAttribute(
                     "javax.servlet.error.status_code", RequestAttributes.SCOPE_REQUEST);
                if (statusCode != null) {
                    status = HttpStatus.resolve(statusCode);
                } 
                if (status != null) {
                    errorAttributes.put(ErrorDTO.KEY_STATUS, status.value());
                    errorAttributes.put(ErrorDTO.KEY_MESSAGE, status.getReasonPhrase());
                } else {
                    errorAttributes.put(ErrorDTO.KEY_STATUS, null);
                    errorAttributes.put(ErrorDTO.KEY_MESSAGE, null);
                }
                exception = getError(webRequest);
                if (exception != null && exception.getMessage() != null) {
                    errorAttributes.put(ErrorDTO.KEY_DETAILS, Map.of(
                        ErrorDTO.KEY_DETAILS_MESSAGES, Lists.newArrayList(exception.getMessage())));
                } else {
                    errorAttributes.put(ErrorDTO.KEY_DETAILS, 
                        Map.of(ErrorDTO.KEY_DETAILS_MESSAGES, Collections.EMPTY_LIST));
                }
                return errorAttributes;
            }
        };
    }
    
    @RestController
    public class Controller implements ErrorController {

        @Autowired
        private ErrorAttributes errorAttributes;    
        
        @Override
        public String getErrorPath() {
            return PATH;
        }  

        @GetMapping(
            path = PATH,    
            produces = "application/json"
        )
        Map<String, Object> get(WebRequest request) {
            return errorAttributes.getErrorAttributes(request, false);
        }    
    }
}