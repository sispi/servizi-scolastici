/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.configuration;

import it.filippetti.ks.api.bpm.authentication.AuthenticationIdentityProvider;
import it.filippetti.ks.api.bpm.component.TenantAwareRuntimeManagerFactory;
import it.filippetti.ks.api.bpm.service.DeploymentService;
import it.filippetti.ks.api.bpm.service.ExecutorService;
import it.filippetti.ks.api.bpm.service.InstanceService;
import it.filippetti.ks.api.bpm.service.TaskService;
import it.filippetti.ks.api.bpm.wih.WorkItemHandlerName;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import javax.persistence.EntityManagerFactory;
import org.jbpm.executor.ExecutorServiceFactory;
import org.jbpm.executor.impl.event.ExecutorEventSupportImpl;
import org.jbpm.kie.services.impl.FormManagerService;
import org.jbpm.kie.services.impl.bpmn2.BPMN2DataServiceImpl;
import org.jbpm.runtime.manager.impl.DefaultRegisterableItemsFactory;
import org.jbpm.runtime.manager.impl.jpa.EntityManagerFactoryManager;
import org.jbpm.services.api.DefinitionService;
import org.jbpm.services.api.RuntimeDataService;
import org.jbpm.shared.services.impl.TransactionalCommandService;
import org.jbpm.springboot.autoconfigure.JBPMProperties;
import org.kie.api.event.process.ProcessEventListener;
import org.kie.api.event.rule.AgendaEventListener;
import org.kie.api.event.rule.RuleRuntimeEventListener;
import org.kie.api.runtime.manager.RegisterableItemsFactory;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.manager.RuntimeManagerFactory;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.api.task.TaskLifeCycleEventListener;
import org.kie.internal.identity.IdentityProvider;
import org.kie.internal.runtime.conf.AuditMode;
import org.kie.internal.runtime.conf.DeploymentDescriptor;
import org.kie.internal.runtime.conf.PersistenceMode;
import org.kie.internal.runtime.conf.RuntimeStrategy;
import org.kie.internal.runtime.manager.deploy.DeploymentDescriptorImpl;
import org.kie.spring.manager.SpringRuntimeManagerFactoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;

/**
 *
 * @author marco.mazzocchetti
 */
@Configuration
@ConfigurationProperties(prefix = "application.jbpm")
public class JbpmConfiguration {

    private static final String PERSISTENCE_UNIT_NAME = "org.jbpm.domain";
    
    private static final String QUARTZ_PROPS = "org.quartz.properties";
    private static final String QUARTZ_FAILED_DELAY = "org.jbpm.timer.quartz.delay";
    private static final String QUARTZ_FAILED_RETRIES = "org.jbpm.timer.quartz.retries";
    private static final String QUARTZ_RESECHEDULE_DELAY = "org.jbpm.timer.quartz.reschedule.delay";
    private static final String QUARTZ_START_DELAY = "org.jbpm.timer.delay";

    @Autowired
    private PlatformTransactionManager transactionManager;
    
    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private JBPMProperties properties;
    
    private List<Map<String, String>> commandTasks;
    
    public JbpmConfiguration() {
    }

    public List<Map<String, String>> getCommandTasks() {
        return commandTasks;
    }

    public void setCommandTasks(List<Map<String, String>> commandTasks) {
        this.commandTasks = commandTasks;
    }

    @Bean("defaultDeploymentDescriptor")
    public DeploymentDescriptor defaultDeploymentDescriptor() { 
        return new DeploymentDescriptorImpl()
            .getBuilder()
            .auditMode(AuditMode.JPA)
            .auditPersistenceUnit(JbpmConfiguration.PERSISTENCE_UNIT_NAME)
            .persistenceMode(PersistenceMode.JPA)
            .persistenceUnit(JbpmConfiguration.PERSISTENCE_UNIT_NAME)
            .runtimeStrategy(RuntimeStrategy.PER_PROCESS_INSTANCE)
            .get();
    }
    
    @Bean
    public DeploymentService deploymentService(
        EntityManagerFactory entityManagerFactory, 
        RuntimeManagerFactory runtimeManagerFactory,
        DefinitionService definitionService, 
        FormManagerService formService, 
        ExecutorService executorService,        
        IdentityProvider identityProvider) {
        
        EntityManagerFactoryManager.get().addEntityManagerFactory(PERSISTENCE_UNIT_NAME, entityManagerFactory);
        
        DeploymentService deploymentService = new DeploymentService();
        deploymentService.setBpmn2Service(definitionService);
        deploymentService.setEmf(entityManagerFactory);
        deploymentService.setIdentityProvider(identityProvider);
        deploymentService.setManagerFactory(runtimeManagerFactory);
        deploymentService.setFormManagerService(formService);
        deploymentService.setContext(applicationContext);
        deploymentService.addListener(((BPMN2DataServiceImpl) definitionService));
        deploymentService.setExecutorService(executorService);
        
        return deploymentService;
    }
    
    @Bean
    public InstanceService instanceService(
        @Lazy RuntimeDataService runtimeDataService, 
        @Lazy DeploymentService deploymentService) {
        
        InstanceService instanceService = new InstanceService();
        instanceService.setDataService(runtimeDataService);
        instanceService.setDeploymentService(deploymentService);
        
        return instanceService;
    }
    
    @Bean
    public TaskService taskService(
        @Lazy RuntimeDataService runtimeDataService,
        @Lazy DeploymentService deploymentService) {
        
        TaskService taskService = new TaskService();
        taskService.setDataService(runtimeDataService);
        taskService.setDeploymentService(deploymentService);
        
        return taskService;
    }
    
    @Bean
    public ExecutorService executorService(
        EntityManagerFactory entityManagerFactory,
        TransactionalCommandService transactionalCommandService) {
        
        ExecutorService executorService = new ExecutorService(ExecutorServiceFactory.newExecutorService(
            entityManagerFactory, 
            transactionalCommandService, 
            new ExecutorEventSupportImpl()));
        
        executorService.setInterval(properties.getExecutor().getInterval());
        executorService.setRetries(properties.getExecutor().getRetries());
        executorService.setThreadPoolSize(properties.getExecutor().getThreadPoolSize());
        executorService.setTimeunit(TimeUnit.valueOf(properties.getExecutor().getTimeUnit()));

        return executorService;
    }

    @Bean
    public RuntimeManagerFactory runtimeManagerFactory(
        @Lazy DeploymentService deploymentService) {
        
        SpringRuntimeManagerFactoryImpl runtimeManager = new TenantAwareRuntimeManagerFactory(deploymentService);
        runtimeManager.setTransactionManager((AbstractPlatformTransactionManager) transactionManager);
        
        if (properties.getQuartz().isEnabled()) {
            System.setProperty(QUARTZ_PROPS, properties.getQuartz().getConfiguration());
            System.setProperty(QUARTZ_FAILED_DELAY, String.valueOf(properties.getQuartz().getFailedJobDelay()));
            System.setProperty(QUARTZ_FAILED_RETRIES, String.valueOf(properties.getQuartz().getFailedJobRetry()));
            System.setProperty(QUARTZ_RESECHEDULE_DELAY, String.valueOf(properties.getQuartz().getRescheduleDelay()));
            System.setProperty(QUARTZ_START_DELAY, String.valueOf(properties.getQuartz().getStartDelay()));
        } else {
            System.clearProperty(QUARTZ_PROPS);
            System.clearProperty(QUARTZ_FAILED_DELAY);
            System.clearProperty(QUARTZ_FAILED_RETRIES);
            System.clearProperty(QUARTZ_RESECHEDULE_DELAY);
            System.clearProperty(QUARTZ_START_DELAY);
        }
        return runtimeManager;
    }
    
    @Bean
    public IdentityProvider identityProvider() {
        return new AuthenticationIdentityProvider();
    }
    
    @Bean
    public RegisterableItemsFactory registerableItemsFactory() {
        return new RegisterableItemsFactoryConfiguration(applicationContext);
    }    

    /**
     * 
     */
    public static class RegisterableItemsFactoryConfiguration extends DefaultRegisterableItemsFactory {
        
        private static final Logger log = LoggerFactory.getLogger(RegisterableItemsFactoryConfiguration.class);
        
        private ApplicationContext applicationContext;
        
        public RegisterableItemsFactoryConfiguration(ApplicationContext applicationContext) {
            super();
            this.applicationContext = applicationContext;
        }

        @Override
        public List<TaskLifeCycleEventListener> getTaskListeners() {

            List<TaskLifeCycleEventListener> listeners;
            
            // for task we need built-in listeners to work with task service
            listeners = super.getTaskListeners();
            listeners.addAll(getBeans(TaskLifeCycleEventListener.class));
            log.info(String.format(
                "Registered task listeners: %s", 
                listeners.stream().map(o -> o.getClass().getName()).collect(Collectors.toList())));
            return listeners;
        }

        @Override
        public List<RuleRuntimeEventListener> getRuleRuntimeEventListeners(RuntimeEngine runtime) {

            List<RuleRuntimeEventListener> listeners;
            
            listeners = new ArrayList<>();
            listeners.addAll(super.getEventListenerFromDescriptor(runtime, RuleRuntimeEventListener.class));
            listeners.addAll(getBeans(RuleRuntimeEventListener.class));
            log.info(String.format(
                "Registered rule listeners: %s", 
                listeners.stream().map(o -> o.getClass().getName()).collect(Collectors.toList())));
            return listeners;
        }

        @Override
        public List<AgendaEventListener> getAgendaEventListeners(RuntimeEngine runtime) {

            List<AgendaEventListener> listeners;
            
            listeners = new ArrayList<>();
            listeners.addAll(super.getEventListenerFromDescriptor(runtime, AgendaEventListener.class));
            listeners.addAll(getBeans(AgendaEventListener.class));
            log.info(String.format(
                "Registered agenda listeners: %s", 
                listeners.stream().map(o -> o.getClass().getName()).collect(Collectors.toList())));                 
            return listeners;
        }

        @Override
        public List<ProcessEventListener> getProcessEventListeners(RuntimeEngine runtime) {
            
            List<ProcessEventListener> listeners;
            
            // for process instances we need built-in listeners to work with process service
            listeners = super.getProcessEventListeners(runtime);
            listeners.addAll(getBeans(ProcessEventListener.class));
            log.info(String.format(
                "Registered process listeners: %s", 
                listeners.stream().map(o -> o.getClass().getName()).collect(Collectors.toList())));                 
            return listeners;
        }

        @Override
        public Map<String, WorkItemHandler> getWorkItemHandlers(RuntimeEngine runtime) {

            Map<String, WorkItemHandler> handlers;
            
            handlers = new HashMap<>();
            handlers.putAll(super.getWorkItemHandlersFromDescriptor(runtime));
            handlers.putAll(getWorkitemHandlers());
            log.info(String.format(
                "Registered work item handlers: %s", 
                handlers.entrySet()
                    .stream()
                    .map(e -> String.format(
                        "%s -> %s", 
                        e.getKey(), 
                        e.getValue().getClass().getName()))
                    .collect(Collectors.toList())));                 
            return handlers;
        }
        
        private synchronized <T> Collection<T> getBeans(Class<T> type) {
            return applicationContext.getBeansOfType(type).values();
        }        
        
        private synchronized Map<String, WorkItemHandler> getWorkitemHandlers() {
                
            Map<String, WorkItemHandler> handlers;

            handlers = new HashMap<>();
            for (WorkItemHandler handler : applicationContext.getBeansOfType(WorkItemHandler.class).values()) {
                if (handler.getClass().isAnnotationPresent(WorkItemHandlerName.class)) {
                    for (String name : handler.getClass().getAnnotation(WorkItemHandlerName.class).value()) {
                        if (name != null && !name.isEmpty()) {
                            handlers.put(name, handler);
                        }
                    }
                }
            }
            return handlers;
        }                
    }
}
