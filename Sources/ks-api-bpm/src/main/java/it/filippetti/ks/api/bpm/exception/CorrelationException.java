/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.exception;

import it.filippetti.ks.api.bpm.model.CorrelationError;

/**
 *
 * @author marco.mazzocchetti
 */
public class CorrelationException extends BusinessException {

    private CorrelationError correlationError;

    public CorrelationException(CorrelationError correlationError, String details) {
        super(BusinessError.EVENT_CORRELATION_ERROR, details);
        this.correlationError = correlationError;
    }

    public CorrelationError getCorrelationError() {
        return correlationError;
    }
}
