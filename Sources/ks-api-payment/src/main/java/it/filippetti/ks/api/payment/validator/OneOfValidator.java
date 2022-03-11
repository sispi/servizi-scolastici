/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.payment.validator;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 
 * @author marco.mazzocchetti
 */
public class OneOfValidator implements ConstraintValidator<OneOf, Object> {
    
    private Set<String> allowedValues;
    private String message;
    
    @Override
    public void initialize(OneOf nameOf) {

          allowedValues = new HashSet<>();
          for (String value :  nameOf.value()) {
              allowedValues.add(value);
          }
          message = nameOf.message();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        
        boolean valid;
        
        valid = true;
        
        if (value != null) {
            if (value instanceof Collection) {
                for (Object itemValue : ((Collection) value)) {
                    valid = allowedValues.contains(itemValue.toString());
                    if (!valid) {
                        break;
                    }
                }
            }
            else if (value instanceof Object[]) {
                for (Object itemValue : ((Object[]) value)) {
                    valid = allowedValues.contains(itemValue.toString());
                    if (!valid) {
                        break;
                    }
                }
            }
            else {
                valid = allowedValues.contains(value.toString());
            }
        }
        if (!valid) {
            context.disableDefaultConstraintViolation();
            context
                .buildConstraintViolationWithTemplate(
                    message
                        .replaceAll("\\$\\{allowedValues\\}", allowedValues.toString()))
                .addConstraintViolation();
        }
                    
        return valid;
  }
}