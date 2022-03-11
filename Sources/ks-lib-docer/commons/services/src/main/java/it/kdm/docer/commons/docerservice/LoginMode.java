package it.kdm.docer.commons.docerservice;

/**
 * Created by Łukasz Kwasek on 21/01/15.
 */
public enum LoginMode {
    STANDARD, SSO;

    public static LoginMode fromString(String mode) {
        return LoginMode.valueOf(mode.toUpperCase());
    }
}