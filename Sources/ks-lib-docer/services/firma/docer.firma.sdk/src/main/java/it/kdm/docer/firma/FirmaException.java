package it.kdm.docer.firma;

/**
 * Created by Łukasz Kwasek on 23/06/15.
 */
public class FirmaException extends Exception {
    public FirmaException() {
    }

    public FirmaException(String message) {
        super(message);
    }

    public FirmaException(String message, Throwable cause) {
        super(message, cause);
    }

    public FirmaException(Throwable cause) {
        super(cause);
    }

    public FirmaException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
