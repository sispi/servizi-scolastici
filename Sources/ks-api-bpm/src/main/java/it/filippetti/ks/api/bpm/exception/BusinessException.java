/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.exception;

import java.util.List;

/**
 *
 * @author marco.mazzocchetti
 */
public class BusinessException extends ApplicationException {
    
    public static final String DEFAULT_MESSAGE = "Operation failed";
    
    private BusinessError businessError;

    public BusinessException(BusinessError businessError) {
        super(DEFAULT_MESSAGE);
        this.businessError = businessError;
    }

    public BusinessException(BusinessError businessError, String details) {
        super(details);
        this.businessError = businessError;
    }

    public BusinessException(BusinessError businessError, List<String> details) {
        super(DEFAULT_MESSAGE, details);
        this.businessError = businessError;
    }
    
    public BusinessException(BusinessError businessError, Throwable cause) {
        super(DEFAULT_MESSAGE, cause);
        this.businessError = businessError;
    }

    public BusinessException(BusinessError businessError, String details, Throwable cause) {
        super(details, cause);
        this.businessError = businessError;
    }

    public BusinessException(BusinessError businessError, List<String> details, Throwable cause) {
        super(DEFAULT_MESSAGE, details, cause);
    }    
    
    public BusinessError getBusinessError() {
        return businessError;
    }
}
