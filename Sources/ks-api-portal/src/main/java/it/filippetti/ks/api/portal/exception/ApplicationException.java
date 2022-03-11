/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.portal.exception;

import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;

/**
 * 
 * @author marco.mazzocchetti
 */
public abstract class ApplicationException extends Exception {
    
    private List<String> details;

    public ApplicationException(Throwable cause) {
        super(cause);
        this.details = Collections.EMPTY_LIST;
    }

    public ApplicationException(String message) {
        super(message);
        this.details = Collections.EMPTY_LIST;
    }

    public ApplicationException(String message, String details) {
        super(message);
        this.details = Collections.unmodifiableList(Lists.newArrayList(details));
    }

    public ApplicationException(String message, List<String> details) {
        super(message);
        this.details = Collections.unmodifiableList(details);
    }
    
    public ApplicationException(String message, Throwable cause) {
        super(message, cause);
        this.details = Collections.EMPTY_LIST;
    }

    public ApplicationException(String message, String details, Throwable cause) {
        super(message, cause);
        this.details = Collections.unmodifiableList(Lists.newArrayList(details));
    }

    public ApplicationException(String message, List<String> details, Throwable cause) {
        super(message, cause);
        this.details = Collections.unmodifiableList(details);
    }
    
    public List<String> getDetails() {
        return details;
    }    
}