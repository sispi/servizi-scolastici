/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.payment.authentication;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 *
 * @author marco.mazzocchetti
 */
public class AuthenticationFilter extends OncePerRequestFilter {
    
    private static final Logger log = LoggerFactory.getLogger(AuthenticationFilter.class);
    
    @Override
    protected void doFilterInternal(
        HttpServletRequest request, HttpServletResponse response, FilterChain chain) 
        throws ServletException, IOException {
        
        try {
            AuthenticationContextHolder.set(new AuthenticationContextImpl(request));
        } catch (AuthenticationServiceException e) {
            log.error(e.getMessage(), e);
            throw e;
        }
        chain.doFilter(request, response);
    }
}
