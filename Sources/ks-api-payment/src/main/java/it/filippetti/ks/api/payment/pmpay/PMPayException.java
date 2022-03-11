package it.filippetti.ks.api.payment.pmpay;

import it.filippetti.ks.api.payment.payment.PaymentException;

/**
 *
 * @author Raffaele Dell'Aversana
 * @since 14 Jul 2018
 */
public class PMPayException extends PaymentException {

    public PMPayException() {
    }

    public PMPayException(String message) {
        super(message);
    }

    public PMPayException(String message, Throwable cause) {
        super(message, cause);
    }

    public PMPayException(Throwable cause) {
        super(cause);
    }

    public PMPayException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
}
