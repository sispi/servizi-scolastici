/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.payment.validator;

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
@Constraint(validatedBy = OneOfValidator.class)
@Target({ElementType.FIELD, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface OneOf {

  String[] value();
  
  String message() default "invalid value, allowed one of ${allowedValues}";
  
  Class<?>[] groups() default {};
  
  Class<? extends Payload>[] payload() default {};
}
