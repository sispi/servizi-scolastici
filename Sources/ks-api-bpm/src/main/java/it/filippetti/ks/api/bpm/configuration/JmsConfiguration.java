/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.configuration;

import javax.annotation.PostConstruct;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.jms.pool.PooledConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.connection.CachingConnectionFactory;

/**
 *
 * @author marco.mazzocchetti
 */
@Configuration
public class JmsConfiguration {
   
    private static final Logger log = LoggerFactory.getLogger(JmsConfiguration.class);

    @Value("${jms.brokerUrl:vm://embedded-broker?broker.persistent=false}")
    private String brokerUrl;

    @Value("${jms.brokerUser}")
    private String brokerUser;

    @Value("${jms.brokerPassword}")
    private String brokerPassword;    
    
    @Profile("local")
    @PostConstruct
    public void init() {
        // make embedded broker accessible outside jvm
        if (brokerUrl.startsWith("vm://")) {
            this.brokerUrl = "tcp://localhost:61616";
            try {
                BrokerService broker;

                broker = new BrokerService();
                broker.addConnector(brokerUrl);
                broker.setPersistent(false);
                broker.start();
            } catch (Throwable e) {
                throw new ExceptionInInitializerError(e);
            }
        }
    }
    
    @Bean
    public ActiveMQConnectionFactory connectionFactory() {
        
        ActiveMQConnectionFactory connectionFactory;
        
        connectionFactory = new ActiveMQConnectionFactory();
        connectionFactory.setBrokerURL(brokerUrl);
        connectionFactory.setUserName(brokerUser);
        connectionFactory.setPassword(brokerPassword);
        return connectionFactory;
    }

    @Bean
    public CachingConnectionFactory cachingConnectionFactory() {
        return new CachingConnectionFactory(connectionFactory());
    }

    @Bean(destroyMethod = "stop")
    protected PooledConnectionFactory pooledConnectionFactory() {
        
        PooledConnectionFactory pooledConnectionFactory;
        
        pooledConnectionFactory = new PooledConnectionFactory();
        pooledConnectionFactory.setConnectionFactory(connectionFactory());
        pooledConnectionFactory.setCreateConnectionOnStartup(true);
        pooledConnectionFactory.setMaxConnections(1);
        return pooledConnectionFactory;
    }    
}
