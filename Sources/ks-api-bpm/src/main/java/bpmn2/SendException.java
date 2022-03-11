/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bpmn2;

import it.filippetti.ks.api.bpm.exception.CorrelationException;

/**
 *
 * @author marco.mazzocchetti
 */
public class SendException extends WorkflowException {

    public SendException(CorrelationException correlationException) {
        super(correlationException.getBusinessError().name(), 
            correlationException.getMessage(), 
            null);
    }
}
