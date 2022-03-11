/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.configuration;

import it.filippetti.ks.api.bpm.service.RoutingService;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.jms.ConnectionFactory;
import javax.validation.constraints.NotNull;
import org.apache.activemq.jms.pool.PooledConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.JmsListenerConfigurer;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerEndpointRegistrar;
import org.springframework.jms.config.SimpleJmsListenerEndpoint;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

/**
 *
 * @author marco.mazzocchetti
 */
@Configuration
@EnableConfigurationProperties(RoutingConfiguration.Properties.class)
public class RoutingConfiguration implements JmsListenerConfigurer {

    private static final Logger log = LoggerFactory.getLogger(RoutingConfiguration.class);

    @Autowired
    private ConnectionFactory connectionFactory;
    
    @Autowired
    private CachingConnectionFactory cachingConnectionFactory;
    
    @Autowired
    private PooledConnectionFactory pooledConnectionFactory;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private RoutingConfiguration.Properties properties;

    @Autowired
    private RoutingService routingService;
    
    private JmsTemplate jmsTemplate;
    
    @PostConstruct
    public void init() {
        
        jmsTemplate = new JmsTemplate();
        jmsTemplate.setPubSubDomain(false);
        jmsTemplate.setConnectionFactory(cachingConnectionFactory);
        jmsTemplate.setMessageConverter(routingService);
    }
    
    public Queue queue(String name) {
        return properties.queue(name);
    }

    public Collection<Queue> queues() {
        return properties.queues();
    }
        
    public JmsTemplate jmsTemplate() {
        return jmsTemplate;
    }

    @Bean("routingJmsListenerContainerFactory")
    public JmsListenerContainerFactory<?> jmsListenerContainerFactory() {

        DefaultJmsListenerContainerFactory factory;

        factory = new DefaultJmsListenerContainerFactory();
        if (applicationProperties.profile().isCluster()) {
            factory.setConnectionFactory(connectionFactory);        
        } else {
            factory.setConnectionFactory(pooledConnectionFactory);
        }
        factory.setAutoStartup(true);
        factory.setPubSubDomain(false);
        factory.setConcurrency("1-1");
        factory.setClientId(applicationProperties.node());
        factory.setTaskExecutor(taskExecutor);
        factory.setTransactionManager(transactionManager);
        factory.setMessageConverter(routingService);
        return factory;
    }

    @Override
    public void configureJmsListeners(JmsListenerEndpointRegistrar registrar) {

        SimpleJmsListenerEndpoint endpoint;

        for (RoutingConfiguration.Queue queueConfiguration : properties.queues()) {
            endpoint = new SimpleJmsListenerEndpoint();
            endpoint.setId(queueConfiguration.name());
            endpoint.setDestination(queueConfiguration.name());
            endpoint.setMessageListener(routingService);
            registrar.registerEndpoint(endpoint, jmsListenerContainerFactory());
        }
    }
    
    /**
     * 
     */
    @ConfigurationProperties(prefix = "application.routing.jms")
    @ConstructorBinding
    public static class Properties {

        private Map<String, Queue> queues;

        public Properties(
            Set<Queue> queues) {
            this.queues = new HashMap<>();
            if (queues != null) {
                for (Queue queue : queues) {
                    this.queues.put(queue.name(), queue);
                }
            }
        }

        public Queue queue(String name) {
            return queues.get(name);
        }

        public Collection<Queue> queues() {
            return Collections.unmodifiableCollection(queues.values());
        }
    }
    
    /**
     * 
     */
    @ConstructorBinding
    public static class Queue {

        private String name;
        private String tenant;
        private String organization;
        private String messageId;
        private Boolean async;
        private String dlq;
        private RoutingService.QueueCustomConsumer customConsumer;
        private RoutingService.QueueCustomProducer customProducer;
        
        public Queue(
            @NotNull String name, 
            @DefaultValue("$tenant") String tenant, 
            @DefaultValue("$organization") String organization, 
            @DefaultValue("$messageId") String messageId,
            @DefaultValue("false") Boolean async, 
            String dlq,
            Class<? extends RoutingService.QueueCustomConsumer> customConsumer,
            Class<? extends RoutingService.QueueCustomProducer> customProducer) {
            
            this.name = name;
            this.tenant = tenant;
            this.organization = organization;
            this.messageId = messageId;
            this.async = async;
            this.dlq = dlq;
            if (customConsumer != null) {
                try {
                    this.customConsumer = customConsumer.getConstructor().newInstance();
                } catch (Throwable e) {
                    throw new ExceptionInInitializerError(e);
                }
            }
            if (customProducer != null) {
                try {
                    this.customProducer = customProducer.getConstructor().newInstance();
                } catch (Throwable e) {
                    throw new ExceptionInInitializerError(e);
                }
            }
        }

        public String name() {
            return name;
        }

        public String tenant() {
            return tenant;
        }

        public String organization() {
            return organization;
        }

        public String messageId() {
            return messageId;
        }

        public Boolean async() {
            return async;
        }

        public String dlq() {
            return dlq;
        }

        public Boolean hasCustomConsumer() {
            return customConsumer != null;
        }

        public RoutingService.QueueCustomConsumer customConsumer() {
            return customConsumer;
        }
        
        public Boolean hasCustomProducer() {
            return customProducer != null;
        }

        public RoutingService.QueueCustomProducer customProducer() {
            return customProducer;
        }        

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 37 * hash + Objects.hashCode(this.name);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Queue other = (Queue) obj;
            return Objects.equals(this.name, other.name);
        }
    }    
}
