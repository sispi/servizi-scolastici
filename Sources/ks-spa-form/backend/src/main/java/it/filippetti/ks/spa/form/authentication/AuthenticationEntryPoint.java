/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.spa.form.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.filippetti.ks.spa.form.dto.ErrorDTO;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

/**
 *
 * @author marco.mazzocchetti
 */
@Component
public class AuthenticationEntryPoint implements org.springframework.security.web.AuthenticationEntryPoint {
    
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void commence(
        HttpServletRequest request, 
        HttpServletResponse response, 
        AuthenticationException authenticationException)
        throws IOException {
        
        response.setContentType("application/json");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        objectMapper.writeValue(
            response.getWriter(), 
            new ErrorDTO(
                HttpStatus.UNAUTHORIZED.value(),
                "Authentication failed",
                List.of(authenticationException.getMessage())));
    }
}
