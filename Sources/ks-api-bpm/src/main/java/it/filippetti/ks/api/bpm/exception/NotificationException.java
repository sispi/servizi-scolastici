/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.exception;

/**
 *
 * @author marco.mazzocchetti
 */
public class NotificationException extends BusinessException {

    public NotificationException(String details) {
        super(BusinessError.NOTIFICATION_ARGUMENT_NOT_VALID, details);
    }

    public NotificationException(String details, Throwable cause) {
        super(BusinessError.NOTIFICATION_ARGUMENT_NOT_VALID, details, cause);
    }
}
