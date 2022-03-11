/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.configuration;

import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 *
 * @author marco.mazzocchetti
 */
@Configuration
@EnableConfigurationProperties(EmailConfiguration.Properties.class)
public class EmailConfiguration {
        
    @Autowired
    private EmailConfiguration.Properties properties;

    public boolean isFromAllowed(String from) {
        return properties.source(from) != null;
    }
    
    public Source source(String from) {
        return properties.source(from);
    }

    public Source defaultSource() {
        return properties.defaultSource();
    }

    public Collection<Source> sources() {
        return properties.sources();
    }   
    
    @ConfigurationProperties(prefix = "email.smtp")
    @ConstructorBinding
    public static class Properties {
        
        private String defaultFrom;
        private Map<String, Source> sources;

        public Properties(
            @NotNull String defaultFrom, 
            Set<Source> sources) {
            
            this.defaultFrom = defaultFrom;
            this.sources = new HashMap<>();
            if (sources != null) {
                for (Source source : sources) {
                    this.sources.put(source.from().getAddress(), source);
                }
            }
            if (this.defaultFrom == null || 
                !this.sources.containsKey(defaultFrom)) {
                throw new ExceptionInInitializerError();
            }
        }

        public Source defaultSource() {
            return sources.get(defaultFrom);
        }

        public Source source(String from) {
            return sources.get(from);
        }

        public Collection<Source> sources() {
            return Collections.unmodifiableCollection(sources.values());
        }
    }
    
    /**
     * 
     */
    @ConstructorBinding
    public static class Source {
        
        private InternetAddress from;
        private InternetAddress replyTo;
        private String host;
        private Integer port;
        private Boolean authentication;
        private String username;
        private String password;
        private String protocol;
        private Boolean tls;
        
        private JavaMailSenderImpl mailSender;

        public Source(
            @NotNull String from,
            String replyTo,
            String personal,
            @NotNull String host,
            @NotNull Integer port,
            @DefaultValue("true") Boolean authentication,
            @NotNull String username,
            @NotNull String password,
            @DefaultValue("smtp") String protocol,
            @DefaultValue("true") Boolean tls) {
            
            try {
                this.from = InternetAddress.parse(from, false)[0];
                if (personal != null) {
                    try {
                        this.from.setPersonal(personal);
                    } catch (UnsupportedEncodingException e) {
                        // skip personal, do not throw errors
                    }
                }
                if (replyTo != null) {
                    this.replyTo = InternetAddress.parse(replyTo, false)[0];
                }
                this.host = host;
                this.port = port;
                this.authentication = authentication;
                this.username = username;
                this.password = password;
                this.protocol = protocol;
                this.tls = tls;
            } catch (AddressException e) {
                throw new ExceptionInInitializerError();
            }
        }

        public InternetAddress from() {
            return from;
        }

        public InternetAddress replyTo() {
            return replyTo;
        }

        public JavaMailSender mailSender() {
            
            if (mailSender == null) {
                mailSender = new JavaMailSenderImpl();
                mailSender.setHost(host);
                mailSender.setPort(port);
                mailSender.setUsername(username);
                mailSender.setPassword(password);
                mailSender.getJavaMailProperties().put("mail.transport.protocol", protocol);
                mailSender.getJavaMailProperties().put("mail.smtp.auth", authentication);
                mailSender.getJavaMailProperties().put("mail.smtp.starttls.enable", tls);
                mailSender.getJavaMailProperties().put("mail.debug", !ApplicationProperties.get().profile().isCluster());
            }
            return mailSender;
        }    

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 97 * hash + Objects.hashCode(this.from);
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
            final Source other = (Source) obj;
            return Objects.equals(this.from, other.from);
        }
    }    
}
