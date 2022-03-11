/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.spa.form.service;

import it.filippetti.ks.spa.form.configuration.ApplicationProperties;
import java.util.Collection;
import java.util.Optional;
import javax.annotation.PostConstruct;
import keysuite.cache.ClientCache;
import keysuite.cache.ClientCacheAuthUtils;
import keysuite.docer.client.Actor;
import keysuite.docer.client.Group;
import keysuite.docer.client.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author marco.mazzocchetti
 */
@Service
public class IdentityService {
    
    public static final String TEST_TENANT = "ENTE_TEST";
    public static final String TEST_ORGANIZATION = "AOO_TEST";
            
    @Autowired
    private ApplicationProperties applicationProperties;
    
    private ClientCache clientCache;
    
    @PostConstruct
    public void init() {
        try {
            clientCache = ClientCache.getInstance();
        } catch (Throwable t) {
            throw new ExceptionInInitializerError(t);
        }
    }

    public Optional<Actor> getActor(String tenant, String actorId) {
        return Optional.ofNullable(clientCache.getActor(tenant, actorId));
    }
    
    public Collection<Actor> getActorsByEmail(String tenant, String email) {
        return clientCache.getActorsByEmail(tenant, email);
    }
    
    public Optional<User> getUser(String jwtToken) {
        return Optional.ofNullable(ClientCacheAuthUtils.getInstance().getUser(jwtToken));
    }
    
    public Optional<User> getUser(String tenant, String userId) {
        return Optional.ofNullable(clientCache.getUser(tenant, userId));
    }

    public Optional<User> getUser(String tenant, String organization, String userId) {
        return getUser(tenant, userId).filter(u -> u.isAdmin() || u.hasGroup(organization));
    }

    public Collection<Group> getUserGroups(String tenant, String userId) {
        return clientCache.getUserGroups(tenant, userId);
    }    
    
    public Optional<Group> getGroup(String tenant, String groupId) {
        return Optional.ofNullable(clientCache.getGroup(tenant, groupId));
    }
    
    public Collection<User> getGroupUsers(String tenant, String groupId) {
        return clientCache.getUsersInGroup(tenant, groupId);
    }    
    
    public String generateDocerToken(String jwtToken) {
        return ClientCacheAuthUtils.getInstance().convertToDocerToken(jwtToken);
    }

    public String generateAdminJwtToken(String tenant, String organization) {
        return ClientCacheAuthUtils.getInstance().simpleJWTToken(
            organization, 
            applicationProperties.adminUser());
    }

    public String generateAdminDocerToken(String tenant, String organization) {
        return generateDocerToken(generateAdminJwtToken(tenant, organization));
    }
    
    public String generateJwtToken(String tenant, String organization, String userId) {
        return ClientCacheAuthUtils.getInstance().simpleJWTToken(
            organization, 
            userId);
    }    
}
