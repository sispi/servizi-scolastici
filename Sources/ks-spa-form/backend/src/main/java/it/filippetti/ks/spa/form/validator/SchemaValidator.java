/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.spa.form.validator;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.core.io.ClassPathResource;

/**
 *
 * @author marco.mazzocchetti
 */
public class SchemaValidator implements ConstraintValidator<Schema, ObjectNode> {
    
    private static JsonSchema schema;
    static {
        try {
            schema = JsonSchemaFactory
                .getInstance(SpecVersion.VersionFlag.V6)
                .getSchema(new ClassPathResource("json/schema.schema.json")
                .getInputStream());
        } catch (IOException e) {
           throw new ExceptionInInitializerError();
        }
    }


    @Override
    public void initialize(Schema schema) {
    }

    @Override
    public boolean isValid(ObjectNode value, ConstraintValidatorContext context) {

        boolean valid;
        List<String> errors;
        
        if (value == null) {
            valid = true;
        } else {
            // check syntax
            errors = schema
                .validate(value)
                .stream()
                .map(m -> m.getMessage())
                .collect(Collectors.toList());

            valid = errors.isEmpty();
            
            if (!valid) {
                context.disableDefaultConstraintViolation();
                for (String error : errors) {
                    context
                        .buildConstraintViolationWithTemplate(error)
                        .addConstraintViolation();
                }
            }
        }
        return valid;
  }
}