package it.kdm.docer.commons.docerservice.provider;

import it.kdm.docer.commons.docerservice.LoginMode;

/**
 * Created by Łukasz Kwasek on 21/01/15.
 */
public class AbstractProvider implements IProvider {

    private LoginMode loginMode;

    public AbstractProvider(LoginMode loginMode) {
        this.loginMode = loginMode;
    }
}
