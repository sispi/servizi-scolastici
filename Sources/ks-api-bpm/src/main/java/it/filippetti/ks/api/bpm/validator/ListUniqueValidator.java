/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.validator;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 
 * @author marco.mazzocchetti
 */
public class ListUniqueValidator implements ConstraintValidator<ListUnique, List> {

    private String message;

    @Override
    public void initialize(ListUnique listUnique) {
          message = listUnique.message();
    }

    @Override
    public boolean isValid(List value, ConstraintValidatorContext context) {
        
        Set<Object> items;
        boolean valid;
        String invalidValue;

        items = new HashSet<>();
        valid = true;
        invalidValue = null;
        
        if (value != null) {
            for (Object item: value) {
                if (valid = !items.contains(item))
                    items.add(item);
                else {
                    invalidValue = item != null ? item.toString() : "null";
                    break;
                }
            }
        }
        if (!valid) {
            context.disableDefaultConstraintViolation();
            context
                .buildConstraintViolationWithTemplate(
                    message
                        .replaceAll("\\$\\{invalidValue\\}", invalidValue))
                .addConstraintViolation();
        }
        return valid;
  }
}