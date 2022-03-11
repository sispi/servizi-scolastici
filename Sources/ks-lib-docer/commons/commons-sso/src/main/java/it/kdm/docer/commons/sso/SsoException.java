package it.kdm.docer.commons.sso;

/**
 * Created by Łukasz Kwasek on 21/01/15.
 */
public class SsoException extends Exception {
    public SsoException(Exception e) {
        super(e);
    }
}
