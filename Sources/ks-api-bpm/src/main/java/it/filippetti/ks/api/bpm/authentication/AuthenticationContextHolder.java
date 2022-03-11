/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.authentication;

import it.filippetti.ks.api.bpm.model.AuthenticationContext;
import java.util.Optional;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author marco.mazzocchetti
 */
public class AuthenticationContextHolder {

    protected static void set(AuthenticationContextImpl authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
    
    public static AuthenticationContext get() throws AuthenticationException {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            throw new AuthenticationCredentialsNotFoundException("Missing credentials");
        }
        return (AuthenticationContext) SecurityContextHolder.getContext().getAuthentication();
    }
    
    public static Optional<AuthenticationContext> getOptional() {
        return Optional.ofNullable(
            (AuthenticationContext) SecurityContextHolder.getContext().getAuthentication());
    }
}
