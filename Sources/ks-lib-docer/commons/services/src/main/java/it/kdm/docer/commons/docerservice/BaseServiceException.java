package it.kdm.docer.commons.docerservice;

/**
 * Created by Łukasz Kwasek on 17/12/14.
 */
public class BaseServiceException extends Throwable {

    public BaseServiceException() {
    }

    public BaseServiceException(String message) {
        super(message);
    }

    public BaseServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaseServiceException(Throwable cause) {
        super(cause);
    }

    public BaseServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public BaseServiceException(String s, Exception p1) {
        super(s, p1);
    }
}
