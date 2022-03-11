/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.component;

import it.filippetti.ks.api.bpm.service.DeploymentService;
import org.kie.api.runtime.manager.RuntimeEnvironment;
import org.kie.api.runtime.manager.RuntimeManager;
import org.kie.spring.manager.SpringRuntimeManagerFactoryImpl;

/**
 *
 * @author marco.mazzocchetti
 */
public class TenantAwareRuntimeManagerFactory extends SpringRuntimeManagerFactoryImpl {

    private DeploymentService deploymentService;
    
    public TenantAwareRuntimeManagerFactory(DeploymentService deploymentService) {
        this.deploymentService = deploymentService;
    }

    @Override
    public RuntimeManager newPerProcessInstanceRuntimeManager(RuntimeEnvironment environment, String identifier) {
        
        setUserInfo(new TenantAwareUserInfo(
            deploymentService.getDeploymentUnit(identifier)));
        setUserGroupCallback(new TenantAwareUserGroupCallback(
            deploymentService.getDeploymentUnit(identifier)));
        
        return super.newPerProcessInstanceRuntimeManager(environment, identifier);
    }
}
