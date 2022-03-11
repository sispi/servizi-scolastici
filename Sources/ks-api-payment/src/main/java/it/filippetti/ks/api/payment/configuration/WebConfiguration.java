/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.payment.configuration;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 *
 * @author marco.mazzocchetti
 */
@Configuration
public class WebConfiguration implements WebMvcConfigurer {
    
    @Override
    public void extendMessageConverters (List<HttpMessageConverter<?>> converters) {
        
        // remove xml message converter from MVC
        // https://stackoverflow.com/questions/53486032/avoid-jackson-xmlmapper-being-used-as-default-objectmapper-with-spring
        converters.removeIf(c -> 
            (c instanceof MappingJackson2XmlHttpMessageConverter));
        //
        //
    }    
}
