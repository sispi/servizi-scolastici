package it.filippetti.ks.api.payment.bnl;

import it.filippetti.ks.api.payment.payment.PaymentException;

public class BnlException extends PaymentException {

    public BnlException() {
    }

    public BnlException(String message) {
        super(message);
    }

    public BnlException(String message, Throwable cause) {
        super(message, cause);
    }

    public BnlException(Throwable cause) {
        super(cause);
    }

    public BnlException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
