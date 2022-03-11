package it.filippetti.ks.api.bpm.configuration;

import com.google.common.collect.Lists;
import it.filippetti.ks.api.bpm.model.Correlation;
import it.filippetti.ks.api.bpm.model.DeploymentReference;
import it.filippetti.ks.api.bpm.model.InstanceReference;
import java.util.Collections;
import java.util.List;
import org.hibernate.boot.Metadata;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.integrator.spi.Integrator;
import org.hibernate.jpa.boot.spi.IntegratorProvider;
import org.hibernate.service.spi.SessionFactoryServiceRegistry;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author marco.mazzocchetti
 */
public class JpaClassImportConfiguration implements Integrator, IntegratorProvider {
    
    private static final Class[] classes = {
        DeploymentReference.class,
        InstanceReference.class,
        Correlation.class
    };
            
    @Override
    public void integrate(
        Metadata metadata,
        SessionFactoryImplementor sessionFactory, 
        SessionFactoryServiceRegistry serviceRegistry) {
        for (Class clazz : classes) {
            metadata.getImports().put(clazz.getSimpleName(), clazz.getName());
        }
    }

    @Override
    public void disintegrate(
        SessionFactoryImplementor sessionFactory,
        SessionFactoryServiceRegistry serviceRegistry) {
    }

    @Override
    public List<Integrator> getIntegrators() {
       return Collections.unmodifiableList(Lists.newArrayList(this));
    }
}