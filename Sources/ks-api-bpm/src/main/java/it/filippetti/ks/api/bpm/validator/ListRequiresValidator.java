/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.validator;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 
 * @author marco.mazzocchetti
 */
public class ListRequiresValidator implements ConstraintValidator<ListRequires, List> {

    private Set<String> requiredValues;
    private String message;

    @Override
    public void initialize(ListRequires listRequires) {
            
          requiredValues = new HashSet<>(Arrays.asList(listRequires.value()));
          message = listRequires.message();
    }

    @Override
    public boolean isValid(List value, ConstraintValidatorContext context) {
        
        Set<String> values;
        boolean valid;
        
        valid = true;
        
        if (value != null) {
            values = new HashSet<>();
            for (Object item: value)
                values.add(item != null ? item.toString() : "null");
            values.retainAll(requiredValues);
            valid = !values.isEmpty();
        }
        if (!valid) {
            context.disableDefaultConstraintViolation();
            context
                .buildConstraintViolationWithTemplate(
                    message
                        .replaceAll("\\$\\{requiredValues\\}", requiredValues.toString()))
                .addConstraintViolation();
        }
        return valid;
  }
}