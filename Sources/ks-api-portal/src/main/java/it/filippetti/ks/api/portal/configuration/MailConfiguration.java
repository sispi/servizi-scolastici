/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.portal.configuration;

import java.util.Properties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 *
 * @author dino
 */
@Configuration
public class MailConfiguration {
    
    @Value( "${mail.protocol}" )
    private String protocol;
    
    @Value( "${mail.host}" )
    private String host;
    
    @Value( "${mail.port}" )
    private int port;
    
    @Value( "${mail.username}" )
    private String username;
    
    @Value( "${mail.password}" )
    private String password;
    
    @Value( "${mail.properties.mail.smtp.auth}" )
    private String auth;
    
    @Value( "${mail.properties.mail.smtp.starttls.enable}" )
    private String starttlsEnable;
    
    @Bean
    public JavaMailSender javaMailSender() { 
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(username);
        mailSender.setPassword(password);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", protocol);
        props.put("mail.smtp.auth", auth);
        props.put("mail.smtp.starttls.enable", starttlsEnable);
        //props.put("mail.debug", "true");

        return mailSender;
    }
    
}
