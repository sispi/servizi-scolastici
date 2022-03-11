package it.filippetti.ks.api.payment.payment;

/**
 *
 * @author Raffaele Dell'Aversana
 * @since 14 Jul 2018
 */
public abstract class PaymentException extends Exception {

    public PaymentException() {
    }

    public PaymentException(String message) {
        super(message);
    }

    public PaymentException(String message, Throwable cause) {
        super(message, cause);
    }

    public PaymentException(Throwable cause) {
        super(cause);
    }

    public PaymentException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
