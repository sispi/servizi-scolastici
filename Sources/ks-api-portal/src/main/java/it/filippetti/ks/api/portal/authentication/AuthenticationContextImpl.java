/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.portal.authentication;

import io.jsonwebtoken.JwtException;
import it.filippetti.ks.api.portal.ApplicationContextHolder;
import it.filippetti.ks.api.portal.configuration.ApplicationProperties;
import it.filippetti.ks.api.portal.model.AuthenticationContext;
import it.filippetti.ks.api.portal.service.IdentityService;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import keysuite.docer.client.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 *
 * @author marco.mazzocchetti
 */
public class AuthenticationContextImpl implements AuthenticationContext, Authentication {

    private static final String HEADER = "Authorization";
    private static final String PREFIX = "Bearer ";
    
    private String jwtToken;
    private String docerToken;
    private String userId;
    private String tenant;
    private String organization;
    private Boolean admin;
    private Set<String> roles;
    private Set<String> identities;
    private Set<Authority> authorities;
    private User user;

    public AuthenticationContextImpl(HttpServletRequest request) throws AuthenticationException {
 
        IdentityService identityService;
        
        identityService = ApplicationContextHolder.getBean(IdentityService.class);
        try {
            // get jwt token
            jwtToken = request.getHeader(HEADER);
            if (StringUtils.isBlank(jwtToken)) {
                throw new BadCredentialsException("Missing credentials");
            }
            if (jwtToken.startsWith(PREFIX)) {
                jwtToken = jwtToken.substring(PREFIX.length());
            }
            
            // get user by jwt token
            user = identityService.getUser(jwtToken).orElse(null);
            if (user == null) {
                throw new UsernameNotFoundException("Invalid credentials");
            }
            
            // generate legacy docer token
            docerToken = identityService.generateDocerToken(jwtToken);

        } catch (AuthenticationException e) {
            throw e;
        } catch (JwtException e) {
            throw new BadCredentialsException("Invalid credentials");           
        } catch (Throwable t) {
            throw new AuthenticationServiceException("Unexpected authentication service error", t);
        }

        userId = user.getUserName();
        tenant = user.getCodEnte();
        organization = user.getCodAoo();
        roles = Set.of(user.getGroups());
        admin = user.isAdmin();
        
        identities = new HashSet<>();
        identities.add(userId);
        identities.add(tenant);
        identities.add(organization);
        identities.addAll(roles);
        identities = Collections.unmodifiableSet(identities);
        
        authorities = new HashSet<>();
        authorities.add(Authority.tenant(tenant));
        authorities.add(Authority.organization(organization));
        authorities.addAll(roles
            .stream()
            .map(r -> Authority.role(r))
            .collect(Collectors.toSet()));
        authorities = Collections.unmodifiableSet(authorities);        
    }

    @Override
    public String getUserId() {
        return userId;
    }    

    @Override
    public String getName() {
        return userId;
    }    

    @Override
    public Object getPrincipal() {
        return userId;
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
    public Set<Authority> getAuthorities() {
        return authorities;
    }

    @Override
    public Object getCredentials() {
        return jwtToken;
    }

    @Override
    public String getJwtToken() {
        return jwtToken;
    }

    @Override
    public String getDocerToken() {
        return docerToken;
    }

    @Override
    public User getDetails() {
        return user;
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public void setAuthenticated(boolean authenticated) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isAdmin() {
        return admin;
    }    
    
    @Override
    public boolean isUser() {
        return hasRole(ApplicationProperties.get().userRole());
    }    

    @Override
    public boolean isAnonymous() {
        return userId.equals(ApplicationProperties.get().anonymousUser());
    }    
    
    @Override
    public Set<String> getIdentities() {
        return identities;
    }

    @Override
    public Set<String> getRoles() {
        return roles;
    }
}
