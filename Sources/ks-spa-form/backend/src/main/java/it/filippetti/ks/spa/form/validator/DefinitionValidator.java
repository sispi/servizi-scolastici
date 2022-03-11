/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.spa.form.validator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.core.io.ClassPathResource;

/**
 *
 * @author marco.mazzocchetti
 */
public class DefinitionValidator implements ConstraintValidator<Definition, ObjectNode> {
    
    private static JsonSchema schema;
    static {
        try {
            schema = JsonSchemaFactory
                .getInstance(SpecVersion.VersionFlag.V6)
                .getSchema(new ClassPathResource("json/definition.schema.json")
                .getInputStream());
        } catch (IOException e) {
           throw new ExceptionInInitializerError();
        }
    }


    @Override
    public void initialize(Definition definition) {
    }

    @Override
    public boolean isValid(ObjectNode value, ConstraintValidatorContext context) {

        boolean valid;
        List<String> errors;
        JsonNode t, s, e;
        Set<String> keys;
        
        if (value == null) {
            valid = true;
        } else {
            // check syntax
            errors = schema
                .validate(value)
                .stream()
                .map(m -> m.getMessage())
                .collect(Collectors.toList());

            // check references
            if (errors.isEmpty()) {
                keys = new HashSet<>();
                t = value.get("tabs");
                for (int i = 0; i < t.size(); i++) {
                    s = t.get(i).get("sections");
                    for (int j = 0; j < s.size(); j++) {
                        e = s.get(j).get("elements");
                        if (e.isTextual()) {
                            keys.add(e.textValue());
                            if (value.get("sections").path(e.textValue()).isMissingNode()) {
                                errors.add(String.format(
                                    "$.tabs[%d].sections[%d].elements: missing section key '%s'", 
                                    i, j, e.textValue()));
                            }
                        }
                    }
                }
                value.get("sections").fieldNames().forEachRemaining(key -> {
                    if (!keys.contains(key)) {
                        errors.add(String.format(
                            "$.sections.%s: unreferenced section key", 
                            key));
                    }
                });
            }

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