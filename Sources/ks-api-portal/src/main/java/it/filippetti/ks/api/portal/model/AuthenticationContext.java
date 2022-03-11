/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.portal.model;

import it.filippetti.ks.api.portal.configuration.ApplicationProperties;
import java.util.Arrays;
import java.util.Set;
import keysuite.docer.client.User;

/**
 *
 * @author marco.mazzocchetti
 */
public interface AuthenticationContext {
    
    public String getUserId();
    public String getTenant();
    public String getOrganization();
    public String getJwtToken();
    public String getDocerToken();
    public Set<String> getIdentities();
    public Set<String> getRoles();
    public boolean isAdmin();
    public boolean isUser();
    public boolean isAnonymous();
    public User getDetails();

    public default boolean hasRole(String... roles) {
        return Arrays.stream(roles).allMatch(r -> getRoles().contains(r));
    }

    public default boolean hasAnyRole(String... roles) {
        return Arrays.stream(roles).anyMatch(r -> getRoles().contains(r));
    }    

    public static boolean isAdmin(String... identities) {
        return Arrays.stream(identities).anyMatch(i -> 
            i.equals(ApplicationProperties.get().adminUser()) ||
            i.equals(ApplicationProperties.get().adminRole()));         
    }
    
    public static AuthenticationContext system(String tenant, String organization) {
        return new AuthenticationContext() {
            @Override
            public String getUserId() {
                return ApplicationProperties.get().systemUser();
            }

            @Override
            public String getTenant() {
                return tenant;
            }

            @Override
            public String getOrganization() {
                return organization;
            }

            @Override
            public String getJwtToken() {
                return null;
            }

            @Override
            public String getDocerToken() {
                return null;
            }

            @Override
            public Set<String> getIdentities() {
                return Set.of(getUserId(), getTenant(), getOrganization());
            }

            @Override
            public Set<String> getRoles() {
                return Set.of(ApplicationProperties.get().adminRole());
            }

            @Override
            public boolean isAdmin() {
                return true;
            }

            @Override
            public boolean isUser() {
                return false;
            }

            @Override
            public boolean isAnonymous() {
                return false;
            }
            
            @Override
            public User getDetails() {
                return null;
            }
        };
    }    
}
