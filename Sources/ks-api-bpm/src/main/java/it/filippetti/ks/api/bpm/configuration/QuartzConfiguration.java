/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.configuration;

import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.quartz.QuartzAutoConfiguration;
import org.springframework.boot.autoconfigure.quartz.SchedulerFactoryBeanCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author marco.mazzocchetti
 */
@Configuration
@EnableAutoConfiguration(exclude = QuartzAutoConfiguration.class)
public class QuartzConfiguration {
    
    private static final Logger log = LoggerFactory.getLogger(QuartzConfiguration.class);
    
    @Bean
    public SchedulerFactoryBeanCustomizer schedulerFactoryBeanCustomizer() {
        return bean -> {
            bean.setAutoStartup(false);
        };
    }
    
    public static class InstanceIdGenerator implements org.quartz.spi.InstanceIdGenerator {

        public InstanceIdGenerator() {
        }

        @Override
        public String generateInstanceId() throws SchedulerException {
            return ApplicationProperties.get().node();
        }
    }
}
