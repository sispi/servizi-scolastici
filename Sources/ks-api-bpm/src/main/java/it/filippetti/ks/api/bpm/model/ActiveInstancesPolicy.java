/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.model;

import it.filippetti.ks.api.bpm.ApplicationContextHolder;
import it.filippetti.ks.api.bpm.repository.InstanceRepository;
import it.filippetti.ks.api.bpm.service.DeploymentService;
import it.filippetti.ks.api.bpm.service.InstanceService;
import java.util.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 *
 * @author marco.mazzocchetti
 */
public enum ActiveInstancesPolicy {
    
    check,
    abort,
    ignore;

    private static final Logger log = LoggerFactory.getLogger(ActiveInstancesPolicy.class);

    public Function<String, Boolean> getOperation() {
        switch (this) {
            case check: return (String unitId) -> {
                InstanceRepository instanceRepository = ApplicationContextHolder.getBean(InstanceRepository.class);
                
                if (!TransactionSynchronizationManager.isActualTransactionActive()) {
                    throw new IllegalStateException();
                }
                
                return instanceRepository.findActiveReferencesByProcessUnitId(unitId).isEmpty();
            };
            case abort: return (var unitId) -> {
                DeploymentService deploymentService = ApplicationContextHolder.getBean(DeploymentService.class);
                InstanceRepository instanceRepository = ApplicationContextHolder.getBean(InstanceRepository.class);
                InstanceService instanceService = ApplicationContextHolder.getBean(InstanceService.class);
                
                if (!TransactionSynchronizationManager.isActualTransactionActive()) {
                    throw new IllegalStateException();
                }

                // in some scenario race conditions can happen 
                // due to the jbpm thread unsafe implementation
                // see AbstractDeploymentService.commonDeploy
                synchronized (deploymentService) {
                    for (InstanceReference reference : instanceRepository.findActiveReferencesByProcessUnitId(unitId)) {
                        try {
                            // abort instance
                            instanceService.abortProcessInstance(reference.getUnitId(), reference.getId());
                        } catch (Throwable t) {
                            log.warn(t.getMessage());
                            return false;
                        }
                    }
                }
                return true;
            };
            case ignore: return (String unitId) -> true;
            default:
                throw new IncompatibleClassChangeError();
        }
    }
}
