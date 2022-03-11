/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.payment.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 *
 * @author marco.mazzocchetti
 */
public class NotBlankValidator implements ConstraintValidator<NotBlank, String> {
	
    @Override
    public void initialize(NotBlank optionalNotBlank){
    }

    @Override
    public boolean isValid(String value, final ConstraintValidatorContext context)
    {
         return value == null || !value.trim().isEmpty();
    }
}