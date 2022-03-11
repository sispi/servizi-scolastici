package it.filippetti.ks.api.payment.payment;

/**
 *
 * @author Raffaele Dell'Aversana
 * @since 14 Jul 2018
 */
class PaymentServiceException extends PaymentException {

    public PaymentServiceException() {
    }

    public PaymentServiceException(String message) {
        super(message);
    }

    public PaymentServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public PaymentServiceException(Throwable cause) {
        super(cause);
    }

    public PaymentServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    
}
