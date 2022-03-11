/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * 
 * @author marco.mazzocchetti
 */
@Constraint(validatedBy = ListRequiresValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ListRequires {
  
  String[] value();
  
  String message() default "requires at least one item of ${requiredValues}";
  
  Class<?>[] groups() default {};
  
  Class<? extends Payload>[] payload() default {};    
}
