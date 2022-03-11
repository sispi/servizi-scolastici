/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.configuration;

import it.filippetti.ks.api.bpm.service.ChatService;
import javax.annotation.PostConstruct;
import javax.jms.ConnectionFactory;
import org.apache.activemq.jms.pool.PooledConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jms.annotation.JmsListenerConfigurer;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerEndpointRegistrar;
import org.springframework.jms.config.SimpleJmsListenerEndpoint;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.transaction.PlatformTransactionManager;

/**
 *
 * @author marco.mazzocchetti
 */
@Configuration
public class ChatConfiguration implements JmsListenerConfigurer {

    private static final Logger log = LoggerFactory.getLogger(ChatConfiguration.class);

    @Autowired
    private ConnectionFactory connectionFactory;

    @Autowired
    private CachingConnectionFactory cachingConnectionFactory;

    @Autowired
    private PooledConnectionFactory pooledConnectionFactory;
    
    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Value("${application.chat.acceptUnknownChatMessages:false}")
    private Boolean acceptUnknownChatMessages;

    @Value("${application.chat.jms.topicName:chat}")
    private String topicName;    
    
    @Autowired
    private TaskExecutor taskExecutor;

    @Autowired
    private ChatService chatService;
    
    private JmsTemplate jmsTemplate;
    
    @PostConstruct
    public void init() {
    
        jmsTemplate = new JmsTemplate();
        jmsTemplate.setPubSubDomain(true);
        jmsTemplate.setConnectionFactory(cachingConnectionFactory);
        jmsTemplate.setMessageConverter(chatService);
        jmsTemplate.setDefaultDestinationName(topicName);
    }
    
    public boolean acceptUnknownChatMessages() {
        return acceptUnknownChatMessages;
    }
    
    public String topicName() {
        return topicName;
    }
    
    public JmsTemplate jmsTemplate() {
        return jmsTemplate;
    }

    @Bean("chatJmsListenerContainerFactory")
    public JmsListenerContainerFactory<?> jmsListenerContainerFactory() {

        DefaultJmsListenerContainerFactory factory;

        factory = new DefaultJmsListenerContainerFactory();
        if (applicationProperties.profile().isCluster()) {
            factory.setConnectionFactory(connectionFactory);        
        } else {
            factory.setConnectionFactory(pooledConnectionFactory);
        }
        factory.setAutoStartup(true);
        factory.setPubSubDomain(true);
        factory.setConcurrency("1-1");
        factory.setClientId(applicationProperties.node());
        factory.setTaskExecutor(taskExecutor);
        factory.setTransactionManager(transactionManager);
        factory.setMessageConverter(chatService);
        return factory;
    }
    
    @Override
    public void configureJmsListeners(JmsListenerEndpointRegistrar registrar) {

        SimpleJmsListenerEndpoint endpoint;
        
        endpoint = new SimpleJmsListenerEndpoint();
        endpoint.setId(topicName);
        endpoint.setDestination(topicName);
        endpoint.setMessageListener(chatService);
        registrar.registerEndpoint(endpoint, jmsListenerContainerFactory());
    }    
}
