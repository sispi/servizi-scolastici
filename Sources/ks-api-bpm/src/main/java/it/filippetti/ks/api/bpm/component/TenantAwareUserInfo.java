/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.component;

import it.filippetti.ks.api.bpm.ApplicationContextHolder;
import it.filippetti.ks.api.bpm.model.DeploymentUnit;
import it.filippetti.ks.api.bpm.service.IdentityService;
import java.util.Iterator;
import java.util.stream.Collectors;
import org.kie.api.task.model.Group;
import org.kie.api.task.model.OrganizationalEntity;
import org.kie.internal.task.api.TaskModelProvider;
import org.kie.internal.task.api.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author marco.mazzocchetti
 */
public class TenantAwareUserInfo implements UserInfo {
    
    @Autowired
    private IdentityService identityService;
    
    private DeploymentUnit deploymentUnit;    

    public TenantAwareUserInfo(DeploymentUnit deploymentUnit) {
        ApplicationContextHolder.autowireObject(this);
        this.deploymentUnit = deploymentUnit;
    }

    
    @Override
    public String getDisplayName(OrganizationalEntity entity) {
        return identityService.
            getActor(tenant(), entity.getId())
            .map(a -> a.getDisplayName())
            .orElse(null);
    }

    @Override
    public Iterator<OrganizationalEntity> getMembersForGroup(Group group) {
        return identityService
            .getGroupUsers(tenant(), group.getId())
            .stream()
            .map(u -> (OrganizationalEntity) TaskModelProvider
                .getFactory()
                .newUser(u.getUserName()))
            .collect(Collectors.toList())
            .iterator();
    }

    @Override
    public boolean hasEmail(Group group) {
        return identityService
            .getGroup(tenant(), group.getId())
            .map(g -> g.getEmail() != null)
            .orElse(false);
    }

    @Override
    public String getEmailForEntity(OrganizationalEntity entity) {
        return identityService
            .getActor(tenant(), entity.getId())
            .map(a -> a.getEmail())
            .orElse(null);
    }

    @Override
    public String getLanguageForEntity(OrganizationalEntity entity) {
        return identityService
            .getActor(tenant(), entity.getId())
            .map(a -> a.getLanguage())
            .orElse(null);
    }
    
    @Override
    public String getEntityForEmail(String email) {
        return identityService
            .getActorsByEmail(tenant(), email)
            .stream()
            .map(u -> u.getName())
            .findFirst()
            .orElse(null);
    }  
    
    private String tenant() {
        return deploymentUnit.getTenant();
    }    
}
