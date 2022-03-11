/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.configuration;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonTokenId;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.deser.std.UntypedObjectDeserializer;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 *
 * @author marco.mazzocchetti
 */
@Configuration
public class JacksonConfiguration {

    private SimpleModule isoDateModule;
    
    public JacksonConfiguration() {
        isoDateModule = new SimpleModule(
            "IsoDateModule", 
            Version.unknownVersion(), 
            Map.of(
                Object.class, new IsoDateAwareObjectDeserializer(), 
                Date.class, new IsoDateDeserializer()), 
            List.of(new IsoDateSerializer()));
    }
    
    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        return new ObjectMapper()
            .configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true)
            .configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, true)
            .enable(SerializationFeature.INDENT_OUTPUT)
            .registerModule(isoDateModule);
    }    
    
    @Bean("internalObjectMapper")
    public ObjectMapper internalObjectMapper() {
        return new ObjectMapper()
            // this is required for serialized values comparison (i.e. correlation keys)    
            .configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true)
            .enable(SerializationFeature.INDENT_OUTPUT)
            .registerModule(isoDateModule);
    } 
    
    /**
     * 
     */
    public static class IsoDateAwareObjectDeserializer extends UntypedObjectDeserializer {

        public IsoDateAwareObjectDeserializer() {
            super(null, null);
        }

        @Override
        public Object deserialize(JsonParser parser, DeserializationContext context) 
            throws IOException, JsonProcessingException {
            if (parser.getCurrentTokenId() == JsonTokenId.ID_STRING) {
                try {
                    return Date.from(
                        Instant.from(
                            DateTimeFormatter.ISO_INSTANT.parse(parser.getText())));
                } catch (DateTimeParseException e) {
                    return super.deserialize(parser, context);
                }
            } else {
                return super.deserialize(parser, context);
            }
        }
    }    
    
    /**
     * 
     */
    public static class IsoDateDeserializer extends StdDeserializer<Date> {

        public IsoDateDeserializer() {
            super(Date.class);
        }

        @Override
        public Date deserialize(JsonParser parser, DeserializationContext context) throws IOException, JsonProcessingException {
            if (parser.getCurrentTokenId() == JsonTokenId.ID_STRING) {
                try {
                    return Date.from(
                        Instant.from(
                            DateTimeFormatter.ISO_INSTANT.parse(parser.getText())));
                } catch (DateTimeParseException e) {
                    throw InvalidFormatException.from(
                        parser, 
                        String.format(
                            "Invalid date format, " + 
                            "expected ISO8601 format (yyyy-MM-ddThh:mm:ssZ): %s", 
                            e.getMessage()), 
                        parser.getText(), 
                        Date.class);
                }
            } else {
                throw MismatchedInputException.from(
                    parser, 
                    Date.class,  
                    String.format(
                        "Cannot deserialize instance of `%s` out of %s token, " + 
                        "expected STRING", 
                        Date.class,
                        parser.getCurrentToken()));           
            }        
        }
    }
    
    /**
     * 
     */
    public static class IsoDateSerializer extends StdSerializer<Date> {

        public IsoDateSerializer() {
            super(Date.class);
        }


        @Override
        public void serialize(Date value, JsonGenerator generator, SerializerProvider serializers) 
            throws IOException {
            // prevent failure in case of sql date
            value = new Date(value.getTime());            
            try {
                generator.writeString(DateTimeFormatter.ISO_INSTANT.format(value.toInstant()));
            } catch (DateTimeException e) {
                throw new IOException(e);
            }
        }
    }
    
}
