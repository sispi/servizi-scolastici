/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.portal.service;

import it.filippetti.ks.api.portal.exception.ValidationException;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.springframework.stereotype.Service;

/**
 * 
 * @author marco.mazzocchetti
 */
@Service
public class ValidationService {
    
    private ValidatorFactory factory;
    
    @PostConstruct
    public void init() {
        factory = Validation
            .byDefaultProvider()
            .configure()
            .buildValidatorFactory();
    }
    
    public <T> T validate(T object) throws ValidationException {
        
        Set<ConstraintViolation<Object>> constraintViolations;
        
        constraintViolations = factory.getValidator().validate(object);
        if (constraintViolations.size() > 0) {
            throw new ValidationException(constraintViolations);
        }
        return object;
    }
    
    public boolean isValid(Object request) {
        
        return factory.getValidator().validate(request).isEmpty();
    }
    
    public Validator getValidator() {
        return factory.getValidator();
    }
}
