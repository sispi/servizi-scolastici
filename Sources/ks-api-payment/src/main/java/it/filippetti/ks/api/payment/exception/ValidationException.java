/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.payment.exception;

import java.util.Comparator;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;

/**
 * 
 * @author marco.mazzocchetti
 */
public class ValidationException extends ApplicationException {
    
    public static final String DEFAULT_MESSAGE = "Validation failed";

    public ValidationException() {
        super(DEFAULT_MESSAGE);
    }
    
    public ValidationException(String details) {
        super(DEFAULT_MESSAGE, details);
    }
    
    public ValidationException(Set<ConstraintViolation<Object>> constraintViolations) {
        super(
            DEFAULT_MESSAGE,
            constraintViolations
                .stream()
                .map(v -> String.format(
                    "%s: %s", 
                    v.getPropertyPath().toString().replaceAll("\\.<.*>", ""), 
                    v.getMessage()))
                .sorted(Comparator.naturalOrder())
                .collect(Collectors.toList()));
    }
}
