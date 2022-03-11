/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.component;

import it.filippetti.ks.api.bpm.ApplicationContextHolder;
import it.filippetti.ks.api.bpm.model.DeploymentUnit;
import it.filippetti.ks.api.bpm.service.IdentityService;
import java.util.List;
import java.util.stream.Collectors;
import org.kie.internal.task.api.UserGroupCallback;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author marco.mazzocchetti
 */
public class TenantAwareUserGroupCallback implements UserGroupCallback {

    @Autowired
    private IdentityService identityService;
    
    private DeploymentUnit deploymentUnit;

    public TenantAwareUserGroupCallback(DeploymentUnit deploymentUnit) {
        ApplicationContextHolder.autowireObject(this);
        this.deploymentUnit = deploymentUnit;
    }
    
    @Override
    public boolean existsUser(String userId) {
        return identityService.getUser(tenant(), userId).isPresent();
    }

    @Override
    public boolean existsGroup(String groupId) {
        return identityService.getGroup(tenant(), groupId).isPresent();
    }

    @Override
    public List<String> getGroupsForUser(String userId) {
        return identityService.getUserGroups(tenant(), userId)
            .stream()
            .map(g -> g.getGroupId())
            .collect(Collectors.toList());
    }
    
    private String tenant() {
        return deploymentUnit.getTenant();
    }
}
