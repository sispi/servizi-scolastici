/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.authentication;

import com.google.common.collect.Lists;
import it.filippetti.ks.api.bpm.configuration.ApplicationProperties;
import java.util.Collections;
import java.util.List;
import org.kie.internal.identity.IdentityProvider;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;

/**
 *
 * @author marco.mazzocchetti
 */
public class AuthenticationIdentityProvider implements IdentityProvider {

    public AuthenticationIdentityProvider() {
    }

    @Override
    public String getName() {
        try {
            return AuthenticationContextHolder.get().getUserId();
        } catch (AuthenticationCredentialsNotFoundException e) {
            return ApplicationProperties.get().systemUser();
        }
    }

    @Override
    public List<String> getRoles() {
        try {
            return Lists.newArrayList(AuthenticationContextHolder.get().getRoles());
        } catch (AuthenticationCredentialsNotFoundException e) {
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public boolean hasRole(String role) {
        try {
            return AuthenticationContextHolder.get().hasRole(role);
        } catch (AuthenticationCredentialsNotFoundException e) {
            return false;
        }
    }
}
